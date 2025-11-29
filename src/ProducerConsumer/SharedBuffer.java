package ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedBuffer{
    Queue<Integer> buffer = new LinkedList<Integer>();
    private final int capacity;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private volatile boolean Shutdown = false;
    public  SharedBuffer(int capacity){
        this.capacity = capacity;
    }
    public void produce(int value) throws InterruptedException {
        lock.lock();
        try {
            while (buffer.size() == capacity && !Shutdown) {
                notFull.await();
            }
            if (Shutdown) return;
            buffer.add(value);
            System.out.println(Thread.currentThread().getName()+"Produced: " + value);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Integer consume() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.isEmpty() && !Shutdown) {
                notEmpty.await();
            }
            if (Shutdown && buffer.isEmpty()) return null;
            Integer value = buffer.remove();
            System.out.println("                     "+Thread.currentThread().getName()+"Consumed: " + value);
            notFull.signalAll();
            return value;
        } finally {
            lock.unlock();
        }
    }
    public  void shutdown(){
        lock.lock();
        try {
            Shutdown = true;
            notFull.signalAll();
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

}
