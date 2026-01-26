HEAP / PRIORITY QUEUE PATTERNS (INCLUDING TWO HEAPS)

Heaps are not about data storage.
They are about maintaining order dynamically.

1️⃣ Story — Why Heaps Exist

Imagine numbers are coming one by one.

You are asked:

largest so far?

smallest so far?

top K?

median so far?

Sorting every time ❌
Heaps say:

“Keep only what matters at the top.”

2️⃣ When to THINK of Heap (INTERVIEW TRIGGERS)

Immediately think Heap when you hear:

✅ Kth largest / smallest
✅ Top K
✅ Streaming data
✅ Schedule tasks
✅ Always need min / max quickly
✅ Dynamic ordering

3️⃣ Heap Basics (INTERVIEW MUST)
Java Heaps
```java
//Min Heap
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

//max Heap
PriorityHeap<Integer>maxHeap = new PriorityQueue<>(Collections.reverseOrder());
```
Operations:
add, poll -> O(log n)
peek -> O(1);

4️⃣ CORE HEAP PATTERNS (VERY IMPORTANT)

There are 4 patterns you must recognize.

🟢 PATTERN 1 — KTH LARGEST / SMALLEST
🔹 LC 215 — Kth Largest Element
Idea:

Maintain min heap of size K

Why?

Heap keeps K largest seen

Top is the Kth largest

```java
import java.util.PriorityQueue;

public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for(int n :nums){
        pq.offer(n);
        if(pq.size()> k)pq.poll();
    }
    return pq.peek();
}
```
Interview Trap ❌

Using max heap of size n → unnecessary


🟢 PATTERN 2 — K-WAY MERGE
Story:

You have:

multiple sorted lists

need merged sorted output

🔹 LC 23 — Merge K Sorted Lists
Idea:

Put first element of each list in min heap

Always extract smallest

Core Insight:

Heap size = K (not N)

🟢 PATTERN 3 — SLIDING WINDOW MAX
🔹 LC 239 — Sliding Window Maximum

Heap approach:

Push (value, index)

Remove outdated elements

This works but:

O(n log n)

Monotonic deque is better (we already covered)

👉 Interviewers accept heap if explained.

🟢 PATTERN 4 — TWO HEAPS (VERY IMPORTANT)

This deserves its own section.

🟣 TWO HEAPS PATTERN — THE BIG ONE
5️⃣ Story — Why Two Heaps Exist

You want:

Median of a stream

Median means:

left half

right half

We need:

quick max of left

quick min of right

👉 Two heaps.

6️⃣ Core Mental Model (CRITICAL)

Maintain:

Max Heap (left) → smaller half

Min Heap (right) → larger half

Invariant:
```text
size(left) == size(right)
OR
size(left) == size(right) + 1
```
7️⃣ Insert Logic (MEMORIZE THIS FLOW)

When a number comes:

If left empty or num ≤ left.peek → left

Else → right

Balance sizes

8️⃣ Median Logic

If sizes equal → avg of tops

Else → top of left

🔹 LC 295 — Find Median from Data Stream

```java
class MedianFinder {

    PriorityQueue<Integer> left;  // max heap
    PriorityQueue<Integer> right; // min heap

    public MedianFinder() {
        left = new PriorityQueue<>(Collections.reverseOrder());
        right = new PriorityQueue<>();
    }

    public void addNum(int num) {

        if (left.isEmpty() || num <= left.peek()) {
            left.offer(num);
        } else {
            right.offer(num);
        }

        // balance
        if (left.size() > right.size() + 1) {
            right.offer(left.poll());
        } else if (right.size() > left.size()) {
            left.offer(right.poll());
        }
    }

    public double findMedian() {
        if (left.size() == right.size()) {
            return (left.peek() + right.peek()) / 2.0;
        }
        return left.peek();
    }
}
```

🔟 Interview Traps (VERY COMMON)

❌ Forgetting to rebalance
❌ Putting larger half in max heap
❌ Returning wrong median for even count
❌ Not explaining invariants

11️⃣ Sharp Interview Thinking Tricks 🧠
🔥 Trick 1

Median → two heaps almost always

🔥 Trick 2

Top K frequent → heap + map

🔥 Trick 3

Scheduling problems → heap by end time

12️⃣ Famous Heap Problems (MUST KNOW)

LC 215 — Kth Largest

LC 295 — Median Finder ⭐

LC 23 — Merge K Lists

LC 239 — Sliding Window Max

LC 703 — Kth Largest in Stream

LC 621 — Task Scheduler

13️⃣ One-Line Recognition Rules

Kth / Top K → Heap

Streaming median → Two heaps

Multiple sorted lists → Heap

Dynamic scheduling → Heap

