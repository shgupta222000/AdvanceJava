package ReaderWriterLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReaderWriterLock {

    private final Lock lock = new ReentrantLock();

    private final Condition canRead = lock.newCondition();
    private final Condition canWrite = lock.newCondition();

    private int readers = 0;
    private int writers = 0;        // 0 or 1 (only one writer allowed)
    private int writeRequests = 0;  // number of writers waiting (for next version)

    // ---------------- READ LOCK ----------------
    public void lockRead() throws InterruptedException {
        lock.lock();
        try {
            while (writers > 0) {         // writer active? readers wait
                canRead.await();
            }
            readers++;
        } finally {
            lock.unlock();
        }
    }

    public void unlockRead() {
        lock.lock();
        try {
            readers--;
            if (readers == 0) {
                canWrite.signal();  // if no readers left → writer can run
            }
        } finally {
            lock.unlock();
        }
    }

    // ---------------- WRITE LOCK ----------------
    public void lockWrite() throws InterruptedException {
        lock.lock();
        try {
            writeRequests++;

            while (readers > 0 || writers > 0) {   // wait if any activity
                canWrite.await();
            }

            writeRequests--;
            writers = 1;

        } finally {
            lock.unlock();
        }
    }

    public void unlockWrite() {
        lock.lock();
        try {
            writers = 0;

            // Give preference to readers in this version
            canRead.signalAll();
            canWrite.signal();

        } finally {
            lock.unlock();
        }
    }
}
