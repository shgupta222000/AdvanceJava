GREEDY ALGORITHMS (INTERVAL & DECISION GREEDY)

Greedy is not a trick.
It’s a proof-based thinking pattern.

Interviewers use it to test:

decision making

confidence

reasoning under pressure

1️⃣ Story — Why Greedy Exists

Imagine you are making decisions step by step.

At every step:

“If I make the best local choice, will it still lead to a global best solution?”

If yes → Greedy works
If no → Greedy fails (needs DP)

2️⃣ When to THINK of Greedy (INTERVIEW TRIGGERS)

Immediately suspect Greedy when you hear:

✅ Maximize / Minimize
✅ Interval problems
✅ Reachability
✅ No future dependency mentioned
✅ Sorting helps
✅ “As early / as late as possible”

If overlapping subproblems → DP
If one-shot decisions → Greedy

3️⃣ CORE GREEDY TYPES (VERY IMPORTANT)

There are 3 big buckets.

🟢 TYPE 1 — INTERVAL GREEDY (MOST IMPORTANT)
🔹 Problem: Non-overlapping Intervals (LC 435)
Story:

You want to keep as many intervals as possible
(or remove minimum).

Key insight:

Always pick the interval that ends earliest

Why?

Leaves maximum room for future intervals

```java
public int eraseOverlapIntervals(int[][] intervals) {

    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);

    int count = 0;
    int prevEnd = intervals[0][1];

    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < prevEnd) {
            count++; // remove this
        } else {
            prevEnd = intervals[i][1];
        }
    }
    return count;
}
```
Interview Trap ❌

Sorting by start time ❌
Correct: end time ✅

🟢 TYPE 2 — REACHABILITY GREEDY
🔹 Problem: Jump Game (LC 55)
Story:

Each index gives you power to jump ahead.

Question:

Can I reach the last index?

Mental Model:

Track:

farthest reachable index so far

If at any point:

i > farthest → impossible

```java
public boolean canJump(int[] nums) {

    int farthest = 0;

    for (int i = 0; i < nums.length; i++) {
        if (i > farthest) return false;
        farthest = Math.max(farthest, i + nums[i]);
    }
    return true;
}
```
If problem asks minimum jumps → still greedy
But logic changes (Jump Game II)

🟢 TYPE 3 — GAS STATION GREEDY (CLASSIC)
🔹 Problem: Gas Station (LC 134)

This problem kills candidates.

Story:

Circular route

Gain gas

Spend gas

You need to find:

Starting index that completes the circuit

CORE INSIGHT (CRITICAL)

If total gas < total cost → impossible

If failed at station i:

Any station between start and i cannot be a valid start

```java
public int canCompleteCircuit(int[] gas, int[] cost) {

    int total = 0;
    int curr = 0;
    int start = 0;

    for (int i = 0; i < gas.length; i++) {
        int diff = gas[i] - cost[i];
        total += diff;
        curr += diff;

        if (curr < 0) {
            start = i + 1;
            curr = 0;
        }
    }
    return total >= 0 ? start : -1;
}
```
4️⃣ Why Greedy Works..?

Interviewers may ask:

“Why does this greedy choice work?”

Answer:

We prove that choosing this option never blocks future optimal solutions

(No math proof required — just intuition)
5️⃣ Famous Greedy Problems (MUST KNOW)

LC 55 / 45 — Jump Game

LC 134 — Gas Station

LC 435 — Non-overlapping Intervals

LC 452 — Minimum Arrows

LC 763 — Partition Labels

6️⃣ Interview Traps (VERY COMMON)

❌ Trying DP unnecessarily
❌ No justification of greedy choice
❌ Sorting by wrong key
❌ Thinking greedy always works

7️⃣ Sharp Interview Thinking Tricks 🧠
🔥 Trick 1

If sorting + single pass solves it → greedy candidate

🔥 Trick 2

If failing at position i eliminates all previous → greedy

🔥 Trick 3

Intervals → always think end time

8️⃣ One-Line Recognition Rule

If a locally optimal decision never hurts future choices → Greedy

