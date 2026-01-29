BIT MANIPULATION

This is not about tricks.
It’s about thinking in binary when constraints scream for it.

1️⃣ Story — Why Bit Manipulation Exists

Interview situation:

O(n²) is too slow

Memory is tight

Numbers repeat / appear odd times

Constraints say: 1 ≤ n ≤ 10^5

Bits say:

“Stop counting. Start XOR-ing.”

2️⃣ Core Bit Rules (MEMORIZE — NON-NEGOTIABLE)
🔹 XOR Truth
a ^ a = 0
a ^ 0 = a
XOR is commutative & associative
🔹 AND / OR
a & 1 → check last bit
a | 0 → unchanged
🔹 Shifts
1 << k  → 2^k
n >> 1  → divide by 2
3️⃣ Bit Pattern 1 — SINGLE NUMBER ⭐⭐⭐
🔹 LC 136 — Single Number

Problem:

Every element appears twice

One appears once

Insight:

Duplicate numbers cancel via XOR.

Java Code
```java
public int singleNumber(int[] nums) {
int ans = 0;
for (int n : nums) {
ans ^= n;
}
return ans;
}
```
Interview Trick 🧠

If frequency = 2 → XOR
If frequency = 3 → bit counting (next pattern)

4️⃣ Bit Pattern 2 — SINGLE NUMBER II (HARDER)
🔹 LC 137 — Single Number II

Others appear 3 times

One appears once

Core Idea:

Count bits at each position.

If count % 3 ≠ 0 → bit belongs to unique number.

Java Code
```java
public int singleNumber(int[] nums) {
    int ans = 0;

    for (int bit = 0; bit < 32; bit++) {
        int count = 0;
        for (int n : nums) {
            if (((n >> bit) & 1) == 1) {
                count++;
            }
        }
        if (count % 3 != 0) {
            ans |= (1 << bit);
        }
    }
    return ans;
}
```
Interview Trap ❌

❌ Using HashMap (extra space)

5️⃣ Bit Pattern 3 — TWO UNIQUE NUMBERS ⭐⭐⭐
🔹 LC 260 — Single Number III

Every number twice

Two numbers appear once

Insight Flow (VERY IMPORTANT)

XOR all → xor = a ^ b

Find rightmost set bit

Partition numbers

XOR separately

Java Code
```java

public int[] singleNumber(int[] nums) {
int xor = 0;
for (int n : nums) xor ^= n;


    int diffBit = xor & -xor; // rightmost set bit


    int a = 0, b = 0;
    for (int n : nums) {
        if ((n & diffBit) == 0) a ^= n;
        else b ^= n;
    }
    return new int[]{a, b};
}
```

6️⃣ Bit Pattern 4 — SUBSETS USING BITS
🔹 LC 78 — Subsets
Story:

For n elements → 2^n subsets
Each bit represents include/exclude.

Java Code
```java

public List<List<Integer>> subsets(int[] nums) {
List<List<Integer>> res = new ArrayList<>();
int n = nums.length;


    for (int mask = 0; mask < (1 << n); mask++) {
        List<Integer> curr = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                curr.add(nums[i]);
            }
        }
        res.add(curr);
    }
    return res;
}
```

Interview Note

If n ≤ 20 → bitmask approach acceptable.

7️⃣ Bit Pattern 5 — POWER OF TWO
🔹 LC 231 — Power of Two
Insight:

Power of two has only one set bit.

Code
```java
public boolean isPowerOfTwo(int n) {
return n > 0 && (n & (n - 1)) == 0;
}
```
8️⃣ Bit Pattern 6 — MAXIMUM XOR ⭐⭐⭐
🔹 LC 421 — Maximum XOR of Two Numbers
Insight:

Try building answer bit by bit (greedy + hash set)

Interview Tip

This problem separates strong vs average candidates.

9️⃣ Common Interview Traps ❌

❌ Forgetting sign bits
❌ Shifting negatives incorrectly
❌ Overusing HashMaps
❌ Missing XOR cancellation property

🔥 Sharp Thinking Tricks (MEMORIZE)

“Others twice” → XOR

“Others thrice” → bit count

“Two uniques” → split by set bit

“Subsets” → bitmask

“Power of two” → n & (n−1)

10️⃣ Must-Do Bit Problems

LC 136 — Single Number

LC 137 — Single Number II

LC 260 — Single Number III

LC 78 — Subsets

LC 231 — Power of Two

LC 421 — Maximum XOR