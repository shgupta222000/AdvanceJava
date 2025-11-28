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
public class BoundedBlockingQueue {

}
