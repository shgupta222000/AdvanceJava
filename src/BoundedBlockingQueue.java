import java.util.LinkedList;
import java.util.Queue;

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
