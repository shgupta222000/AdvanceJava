PREFIX SUM & DIFFERENCE ARRAY

1️⃣ Story — Why Prefix Sum Exists

You’re given an array and asked many range questions:

sum from L to R?

how many subarrays?

how many times a value appears in a range?

Brute force:

Each query → O(n)

Multiple queries → TLE ❌

Prefix sum says:

“Pre-compute once. Answer forever.”

2️⃣ Prefix Sum — Core Idea (NO JARGON)
Original array: arr = [2, 4, 1, 3]

Prefix Sum Array:
```text
pref[0] = 2
pref[1] = 2 + 4 = 6
pref[2] = 7
pref[3] = 10

pref[i] = sum of elements from index 0 to i

Range Sum sum(L, R) = pref[R] - pref[L-1]
```
Template

```java
int[] pref = new int[n];
pref[0] = arr[0];

for (int i = 1; i < n; i++) {
    pref[i] = pref[i - 1] + arr[i];
}
```

🟢 PREFIX SUM PATTERN 1 — RANGE QUERIES
🔹 LC 303 — Range Sum Query (Immutable)

Exactly this concept.

🟢 PREFIX SUM PATTERN 2 — SUBARRAY SUM PROBLEMS ⭐⭐⭐

This is VERY IMPORTANT.

5️⃣ Subarray Sum = K (FAMOUS)
🔹 LC 560 — Subarray Sum Equals K

we want sum(i → j) = k

That Means
```text
prefix[j] - prefix[i-1] = k
prefix[i-1] = prefix[j] - k
If we have seen (prefix[j] - k) before, a valid subarray exists.
```

Prefix sum

HashMap (frequency)

```java
import java.util.HashMap;

public int subArraySumn(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0,1);//of prefix ,count
    int prefix = 0, count = 0;
    
    for(int n : nums){
        prefix +=n;
        count += map.getOrDefault(prefix-k,0);
        map.put(prefix,map.getOrDefault(prefix,0)+1);
    }
    return count;
}
```
Interview Traps ❌

❌ Forget map.put(0,1)
❌ Sliding window (fails with negatives)

6️⃣ Prefix Sum with 0s & 1s
🔹 LC 525 — Contiguous Array

Problem:

Equal number of 0s and 1s

Trick:

Convert:
```text
0 → -1
1 → +1
```
Then Problems becomes : longest subArray with sum =0;

🟢 PREFIX SUM PATTERN 3 — 2D PREFIX SUM
🔹 LC 304 — Range Sum Query 2D
Formula (IMPORTANT):
```text
sum = pref[r2][c2]
    - pref[r1-1][c2]
    - pref[r2][c1-1]
    + pref[r1-1][c1-1]
```
Interview Note:

If matrix range sum appears → prefix sum instantly.

🟢 DIFFERENCE ARRAY (ADVANCED & POWERFUL)
7️⃣ Story — Why Difference Array Exists

You are told:

Apply many range updates

Then return final array

Brute force:

Each update → O(n) ❌

Difference array:

“Mark start and end. Build later.”

8️⃣ Difference Array Idea

Original: arr = [0, 0, 0, 0, 0]

Update:
```text
add +5 from index 1 to 3
```
Difference Array:
```text
diff[1] += 5
diff[4] -= 5
```
Final array = prefix sum of Diff

```java
int[] diff = new int[n];

for (each update [l, r, val]) {
    diff[l] += val;
    if (r + 1 < n) diff[r + 1] -= val;
}

// build final array
int curr = 0;
for (int i = 0; i < n; i++) {
    curr += diff[i];
    arr[i] = curr;
}
```
🔟 Famous Difference Array Problems
🔹 LC 370 — Range Addition
🔹 LC 1109 — Corporate Flight Bookings ⭐
🔹 LC 1094 — Car Pooling

All same idea.

11️⃣ Interview Thinking Tricks 🧠

🔥 If many range updates → Difference array
🔥 If many range queries → Prefix sum
🔥 If subarray sum → Prefix + HashMap
🔥 If 0s & 1s balance → convert → prefix

12️⃣ One-Line Recognition Rules

Range sum → Prefix

Subarray count → Prefix + Map

Bulk updates → Difference

2D queries → 2D Prefix