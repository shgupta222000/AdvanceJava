package CustomThreadPool;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * SimpleThreadPool
 *
 * Fixed-size thread pool with bounded task queue implemented from scratch
 * (ReentrantLock + Condition). Provides execute(Runnable) and submit(Callable).
 *
 * Semantics:
 * - execute: blocks if queue is full (backpressure). Throws RejectedExecutionException if shutdown.
 * - submit: wraps Callable into a FutureTask and executes it.
 * - shutdown: stop accepting new tasks; workers finish queued tasks then exit.
 * - shutdownNow: stop accepting new tasks, clear queue, interrupt workers and return pending tasks.
 * - awaitTermination: waits for worker threads to terminate.
 */
public class SimpleThreadPool {

    private final int poolSize;
    private final int queueCapacity;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final List<Worker> workers = new ArrayList<>();
    private volatile boolean isShutdown = false;    // set by shutdown()
    private volatile boolean isStopped = false;     // set by shutdownNow()

    public SimpleThreadPool(int poolSize, int queueCapacity) {
        if (poolSize <= 0 || queueCapacity <= 0) {
            throw new IllegalArgumentException("poolSize and queueCapacity must be > 0");
        }
        this.poolSize = poolSize;
        this.queueCapacity = queueCapacity;
        startWorkers();
    }

    private void startWorkers() {
        for (int i = 0; i < poolSize; i++) {
            Worker w = new Worker("SimpleThreadPool-Worker-" + i);
            workers.add(w);
            w.thread.start();
        }
    }

    /**
     * Execute a Runnable. Blocks when queue is full.
     * Throws RejectedExecutionException if pool is shutdown.
     */
    public void execute(Runnable task) {
        Objects.requireNonNull(task);
        lock.lock();
        try {
            if (isShutdown || isStopped) {
                throw new RejectedExecutionException("ThreadPool is shutdown");
            }
            while (taskQueue.size() == queueCapacity) {
                // Wait until space available
                try {
                    notFull.await();
                } catch (InterruptedException ie) {
                    // Restore status and re-check shutdown
                    Thread.currentThread().interrupt();
                    throw new RejectedExecutionException("Interrupted while waiting to enqueue", ie);
                }
            }
            taskQueue.add(task);
            notEmpty.signal(); // notify a worker
        } finally {
            lock.unlock();
        }
    }

    /**
     * Submit a Callable and return a Future.
     * Blocks when queue is full.
     */
    public <T> Future<T> submit(Callable<T> callable) {
        Objects.requireNonNull(callable);
        FutureTask<T> ft = new FutureTask<>(callable);
        execute(ft); // will block or throw if shutdown
        return ft;
    }

    /**
     * Initiates an orderly shutdown: stop taking new tasks; workers finish queued tasks and exit.
     */
    public void shutdown() {
        lock.lock();
        try {
            isShutdown = true;
            // Wake workers so they can exit if queue empty
            notEmpty.signalAll();
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Attempts to stop all actively executing tasks, halts processing of waiting tasks,
     * returns list of pending tasks that were in the queue.
     */
    public List<Runnable> shutdownNow() {
        lock.lock();
        try {
            isShutdown = true;
            isStopped = true;

            // Drain queue
            List<Runnable> pending = new ArrayList<>(taskQueue);
            taskQueue.clear();

            // Wake all workers (they will see isStopped and exit)
            notEmpty.signalAll();
            notFull.signalAll();

            // Interrupt workers
            for (Worker w : workers) {
                w.thread.interrupt();
            }
            return pending;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Blocks until all worker threads have terminated, or timeout occurs.
     * Returns true if terminated, false if timeout elapsed.
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        long deadline = System.nanoTime() + nanos;

        for (Worker w : workers) {
            long timeLeft = deadline - System.nanoTime();
            if (timeLeft <= 0) return false;
            w.thread.join(TimeUnit.NANOSECONDS.toMillis(timeLeft));
        }
        return true;
    }

    /**
     * Returns true if shutdown() or shutdownNow() has been called.
     */
    public boolean isShutdown() {
        return isShutdown;
    }

    /**
     * Worker wrapper holding the thread and run loop.
     */
    private class Worker implements Runnable {
        private final Thread thread;

        Worker(String name) {
            this.thread = new Thread(this, name);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Runnable task;
                    lock.lock();
                    try {
                        // Wait for a task if queue empty
                        while (taskQueue.isEmpty()) {
                            if (isStopped) {
                                // Hard stop requested: exit immediately
                                return;
                            }
                            if (isShutdown) {
                                // No new tasks coming; if queue empty -> exit
                                if (taskQueue.isEmpty()) return;
                            }
                            try {
                                notEmpty.await();
                            } catch (InterruptedException ie) {
                                // If interrupted and stop requested -> exit
                                if (isStopped) return;
                                // else continue waiting or re-check conditions
                                Thread.currentThread().interrupt();
                                // fall through to re-check loop conditions
                            }
                        }
                        task = taskQueue.remove();
                        // signal producers that there is now room
                        notFull.signal();
                    } finally {
                        lock.unlock();
                    }

                    // Execute task outside lock
                    try {
                        task.run();
                    } catch (RuntimeException re) {
                        // swallow to prevent worker death; log if needed
                        System.err.println("Task threw exception: " + re.getMessage());
                        re.printStackTrace();
                    }
                }
            } finally {
                // thread exiting
            }
        }
    }
}
