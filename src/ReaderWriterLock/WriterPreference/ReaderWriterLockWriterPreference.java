package ReaderWriterLock.WriterPreference;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReaderWriterLockWriterPreference {
    private final Lock lock = new ReentrantLock();
    private final Condition canRead = lock.newCondition();
    private final Condition canWrite = lock.newCondition();
    private int readers = 0;
    private int writers = 0;        // 0 or 1 (only one writer allowed)
    private int writeRequests = 0;  // number of writers waiting
    // ---------------- READ LOCK ----------------
    public void lockRead() throws InterruptedException {
        lock.lock();
        try {
            while (writers > 0 || writeRequests > 0) { // writer active or waiting? readers wait
                canRead.await();
            }
            readers++;
        } finally {
            lock.unlock();
        }
    }
    public void unlockRead() throws InterruptedException {
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
    public  void unlockWrite() throws InterruptedException {
        lock.lock();
        try {
            writers = 0;
            if (writeRequests > 0) {
                canWrite.signal(); // give preference to waiting writers
            } else {
                canRead.signalAll(); // no writers waiting → wake all readers
            }
        } finally {
            lock.unlock();
        }
    }
}
/*
“How did you ensure writers don’t starve?”
“I maintain a writeRequests counter.
If a writer is waiting, new readers are blocked, even if there is no active writer.
This ensures writers eventually get exclusive access, removing starvation.”
 */