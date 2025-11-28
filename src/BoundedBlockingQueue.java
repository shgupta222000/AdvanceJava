import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*🚀 QUESTION 1 — Implement a Thread-Safe Bounded Blocking Queue
 *This is asked at Amazon, Uber, Google, Microsoft, Netflix for SDE-2/SDE-3.
 *You need to implement:
 * public class BoundedBlockingQueue<T> {

    public BoundedBlockingQueue(int capacity);

    public void enqueue(T item) throws InterruptedException;

    public T dequeue() throws InterruptedException;

    public int size();
}
* Requirements:
	1.	Queue has fixed capacity.
	2.	enqueue() blocks when queue is full.
	3.	dequeue() blocks when queue is empty.
	4.	Must support multiple producers and multiple consumers.
	5.	Must be thread-safe.
	6.	Must correctly handle InterruptedException.
	7.	size() returns the current number of elements (thread-safe).

* Expectations (Interview Version)

    When interviewer asks:

    “How will you design a bounded blocking queue?”

    They want to see if you understand:
    •	wait() and notifyAll()
    •	Lock ordering
    •	Conditions (with ReentrantLock)
    •	Avoiding deadlocks
    •	Using while instead of if to avoid spurious wakeups (VERY IMPORTANT)
    •	Proper synchronization around shared state

 */
/*
*Queue internal structure:

    Use:
        •	An array or LinkedList<T> for storage
        •	int head
        •	int tail
        •	int count

    Synchronization tools (two versions):
   -Version A (Brute): synchronized + wait + notifyAll
   - Easy to understand, good for interviews.
   - Version B (Better/Best): ReentrantLock + 2 Conditions
   - More control, better performance under high contention.
    notFull
    notEmpty
   -This is how real Java ArrayBlockingQueue works.
 */
public class BoundedBlockingQueue<T> {
    private final Queue<T> queue =new LinkedList<>();
    private final int capacity;
    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
    }
    public synchronized void enqueue(T item) throws InterruptedException{
            while (queue.size() == capacity) {
                wait();
            }
            queue.add(item);
            notifyAll();
    }
    public synchronized T dequeue() throws InterruptedException{
            while (queue.size() == 0) {
                wait();
            }
            T removedItem = queue.remove();
            notifyAll();
            return removedItem;
    }
    public synchronized int size(){
        return queue.size();
    }

    public static void main(String[] args) {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(5);
        // Example usage with multiple producers and consumers can be added here.
        Runnable producer = ()->{
            try{
                for (int i = 0; i < 10; i++) {
                    queue.enqueue(i);
                    System.out.println(Thread.currentThread().getName()+ "Produced: " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = ()->{
            try{
                for (int i = 0; i < 10; i++) {
                    int item = queue.dequeue();
                    System.out.println("        " + Thread.currentThread().getName() +"Consumed: " + item);
                    Thread.sleep(100);
                }
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        Thread p1 = new Thread(producer, "Producer-1");
        Thread p2 = new Thread(producer, "Producer-2");
        Thread c1 = new Thread(consumer, "Consumer-1");
        Thread c2 = new Thread(consumer, "Consumer-2");
        p1.start();
        p2.start();
        c1.start();
        c2.start();

        try {
            p1.join();
            p2.join();
            c1.join();
            c2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nFinal Queue Size = " + queue.size());
        System.out.println("Test Completed Successfully!");
    }
}
/*
ReentrantLock + Two Conditions (notFull, notEmpty)

This is how real-world, high-performance queues like ArrayBlockingQueue work.

⸻

⭐ WHY THIS VERSION IS BETTER?

✔ Avoids waking the wrong threads

notifyAll() wakes everyone, but Conditions allow you to wake only consumers OR only producers.

✔ Higher throughput under contention

ReentrantLock performs better than synchronized when many threads compete.

✔ More flexibility
	•	Fair locking
	•	TryLock
	•	Multiple condition variables
	•	Timeout support (optional)

✔ Clean separation of states
	•	notFull.await() → wait until space available
	•	notEmpty.await() → wait until items available
 */


/*
public class BoundedBlockingQueue<T> {


    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        this.capacity = capacity;
    }

    public void enqueue(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await(); // wait until space available
            }

            queue.add(item);

            // Signal consumers that item is available
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await(); // wait until item available
            }

            T removed = queue.remove();

            // Signal producers that space is available
            notFull.signal();

            return removed;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}
*/

/*
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue<T> {

    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    private volatile boolean isShutdown = false;

    public BoundedBlockingQueue(int capacity, boolean fair) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        this.capacity = capacity;

        // Fair lock (optional)
        this.lock = new ReentrantLock(fair);
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    // Default: unfair (better performance)
    public BoundedBlockingQueue(int capacity) {
        this(capacity, false);
    }

    // ------------ ENQUEUE (BLOCKING) ------------
    public void enqueue(T item) throws InterruptedException {
        lock.lockInterruptibly();  // better interrupt support
        try {
            while (queue.size() == capacity) {
                if (isShutdown) {
                    throw new IllegalStateException("Queue is shutdown");
                }
                notFull.await();
            }

            queue.add(item);

            // Only signal if queue became non-empty
            if (queue.size() == 1) {
                notEmpty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    // ------------ DEQUEUE (BLOCKING) ------------
    public T dequeue() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (queue.isEmpty()) {
                if (isShutdown) {
                    return null;  // graceful exit
                }
                notEmpty.await();
            }

            T removed = queue.remove();

            // Only signal if queue became non-full
            if (queue.size() == capacity - 1) {
                notFull.signal();
            }

            return removed;
        } finally {
            lock.unlock();
        }
    }

    // ------------ TIMED OFFER ------------
    public boolean offer(T item, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);

        lock.lockInterruptibly();
        try {
            while (queue.size() == capacity) {
                if (isShutdown) return false;
                if (nanos <= 0L) return false;

                nanos = notFull.awaitNanos(nanos);
            }

            queue.add(item);

            if (queue.size() == 1) {
                notEmpty.signal();
            }
            return true;

        } finally {
            lock.unlock();
        }
    }

    // ------------ TIMED POLL ------------
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);

        lock.lockInterruptibly();
        try {
            while (queue.isEmpty()) {
                if (isShutdown) return null;
                if (nanos <= 0L) return null;

                nanos = notEmpty.awaitNanos(nanos);
            }

            T item = queue.remove();

            if (queue.size() == capacity - 1) {
                notFull.signal();
            }
            return item;

        } finally {
            lock.unlock();
        }
    }

    // ------------ SHUTDOWN ------------
    public void shutdown() {
        lock.lock();
        try {
            isShutdown = true;
            notFull.signalAll();
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // ------------ SIZE ------------
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public boolean isShutdown() {
        return isShutdown;
    }
}

⭐ WHAT MAKES THIS VERSION “PRODUCTION LEVEL”?

This is the version expected at Uber, Meta, Microsoft, Amazon (SDE-2 / SDE-3).

✔ Fair lock option

If fair = true:
Threads acquire the lock in FIFO order → prevents starvation.

✔ interruptible lock acquisition

lock.lockInterruptibly() allows the thread to be interrupted while waiting for a lock.
Very important for real systems.

✔ Avoids unnecessary wakeups

Instead of always calling signal(), we call it only when needed.

✔ Timed operations (offer/poll)

✔ Graceful shutdown support
 */