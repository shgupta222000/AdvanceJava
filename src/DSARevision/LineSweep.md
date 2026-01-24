LINE SWEEP (INTERVAL EVENTS PATTERN)
This is NOT a single algorithm.
It is a thinking pattern used when:
“Things start and end, and something changes over time.”

1️⃣ Why does Line Sweep exist? (The core problem)
The real-world problem it solves
You are given intervals / events over time or space, and you are asked:
•	How many overlaps exist?
•	What is the maximum overlap?
•	Do any two intervals conflict?
•	What is the total covered length?
•	How many meetings are happening at the same time?

Naive way (why we need Line Sweep)
[1,4], [2,5], [7,9]
To check overlaps:
•	Compare every interval with every other interval
Check every pair of intervals → O(N^2)

Instead of checking every interval against every other (O(n²)),
we:

👉 Convert intervals into events
👉 Sort events
👉 Sweep from left to right
👉 Maintain active state

That’s it.

When to THINK of Line Sweep (INTERVIEW TRIGGERS)

The moment you hear:

✅ Intervals
✅ Start / End
✅ Overlapping
✅ Max simultaneous
✅ Min resources
✅ Skyline / Buildings
✅ Calendar / Meeting rooms

2️⃣ Key observation (the insight behind Line Sweep)
Overlaps only change at start or end points.
Between two consecutive points:
•	The state is constant
•	Nothing changes
So instead of checking interval vs interval:

➡️ We check events in order
This idea leads to Line Sweep.

3️⃣ The Mental Model (Very Important)
Imagine:
•	A vertical line sweeping from left to right on a number line
As it moves:
•	When it hits a start → something becomes active
•	When it hits an end → something becomes inactive
We only care about:
•	When something starts
•	When something ends

⸻

4️⃣ How Line Sweep Works (Core Algorithm)

Step 1: Convert intervals into events

For interval [start, end]:
•	(start, +1) → interval starts
•	(end, -1) → interval ends

Example
```text
[1,4] → (1,+1), (4,-1)
[2,5] → (2,+1), (5,-1)
```
Step 2: Sort events

Sort by:
1.	Time
2.	If same time → end before start (important!)
3. 
⸻

Step 3: Sweep

Maintain:

•	activeCount → how many intervals are currently active

As you process events:

•	+1 → increment
•	-1 → decrement

⸻

5️⃣ Example 1: Maximum Overlapping Intervals
```text
Intervals:
[1,4], [2,6], [5,8]
Step 1: Events
(1,+1), (4,-1), (2,+1), (6,-1), (5,+1), (8,-1)
Step 2: Sorted Events
(1,+1), (2,+1), (4,-1), (5,+1), (6,-1), (8,-1)
Step 3: Sweep
Time  Event  activeCount  maxOverlap
1     +1     1            1
2     +1     2            2
4     -1     1            2
5     +1     2            2
6     -1     1            2
8     -1     0            2
Answer: Maximum Overlap = 2
```
6️⃣ Example 2: Meeting Rooms (Leetcode Classic)

Problem
Minimum meeting rooms required

Insight

•	Maximum simultaneous meetings = rooms needed

👉 Same algorithm as above

⸻

7️⃣ Why sorting END before START matters

Edge Case
```text
[1,3] and [3,5]
These do NOT overlap
Events:
(1,+1), (3,-1), (3,+1), (5,-1)
If start processed first:
	•	Active becomes 2 ❌ (wrong)
Correct rule:
End must be processed before start at same time
```
Java Code Template
```java
class Solution {
    static class Event {
        int time;
        int type; // +1 start, -1 end
        Event(int t, int type) {
            this.time = t;
            this.type = type;
        }
    }

    public int maxOverlap(int[][] intervals) {
        List<Event> events = new ArrayList<>();

        for (int[] in : intervals) {
            events.add(new Event(in[0], +1));
            events.add(new Event(in[1], -1));
        }

        events.sort((a, b) -> {
            if (a.time != b.time)
                return a.time - b.time;
            return a.type - b.type; // end (-1) before start (+1)
        });

        int active = 0, max = 0;

        for (Event e : events) {
            active += e.type;
            max = Math.max(max, active);
        }

        return max;
    }
}
```
⸻

Famous LeetCode Problems (MUST KNOW)

⸻

🔹 LC 253 — Meeting Rooms II

Question:
Minimum rooms required so no meetings overlap.

Answer = Maximum active intervals

⸻

🔹 LC 1094 — Car Pooling

Passengers get:
•	picked up (start)
•	dropped (end)

If at any point: activePassengers > capacity → false
Just replace maxActive logic with capacity check.

LC 218 — The Skyline Problem (HARD)

This is Line Sweep + Heap

Story:
•	Buildings start → height added
•	Buildings end → height removed
•	Track current max height

Key idea:
•	Events: (x, height)
•	Start → negative height
•	End → positive height
•	Use max-heap

This problem PROVES Line Sweep mastery.

⸻

Advanced Variant — DIFFERENCE ARRAY (IMPORTANT)

When range updates matter.

Example:
•	Add +10 passengers from [2,5]

Instead of looping:
```text
diff[2] += 10
diff[6] -= 10
```

Then prefix sum gives actual count.

👉 Car Pooling can be solved this way too.

9️⃣ Interview Traps (VERY COMMON)

❌ Forgetting end-before-start ordering
❌ Treating [2,3] and [3,4] as overlapping
❌ Using nested loops on intervals
❌ Not converting to events

🔥 Sharp Interview Thinking Tricks

🧠 Trick 1 — Max overlap = resource count

Rooms, servers, CPUs → same logic

⸻

🧠 Trick 2 — If intervals are huge range

Use TreeMap instead of array

⸻

🧠 Trick 3 — Discrete coordinates?

Compress coordinates before sweep


If something starts, ends, and affects a running state → Line Sweep

⸻

9️⃣ What types of problems Line Sweep solves

1️⃣ Interval overlap problems

•	Meeting rooms

•	Max guests

•	Platform problem

⸻

2️⃣ Range coverage
•	Total covered length
•	Union of intervals

⸻

3️⃣ 2D problems (advanced)

•	Skyline problem
•	Rectangle overlap area

(Line sweep + data structure)

⸻

🔟 Time & Space Complexity
•	Events: 2N
•	Sorting: O(N log N)
•	Sweep: O(N)
Time: O(N log N)
Space: O(N)

⸻

1️⃣1️⃣ When NOT to use Line Sweep

•	When intervals are static and few
•	When order does not matter
•	When simpler greedy or prefix sum works

⸻

1️⃣2️⃣ One-liner interview explanation (memorize)

Line sweep converts interval problems into sorted start and end events and processes them in order to track active intervals efficiently.

⸻

1️⃣3️⃣ How interviewers escalate Line Sweep
1.	Basic overlap count
2.	Meeting rooms
3.	Skyline problem
4.	Rectangle union area
