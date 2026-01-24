BINARY SEARCH ON ANSWER (PARAMETRIC SEARCH)

1️⃣ Story — Why Binary Search on Answer Exists

Sometimes:

You are not given a sorted array

You are asked:

“What is the minimum / maximum value such that a condition holds?”

Brute force:

Try all answers → slow

Binary Search on Answer says:

“If feasibility changes monotonically, search the answer space.”

2️⃣ When to THINK of This (INTERVIEW TRIGGERS)

Immediately think Binary Search on Answer when you hear:

✅ Minimize / Maximize something
✅ “At least / at most”
✅ “Possible or not?”
✅ Large constraints
✅ Answer lies in a range

This is NOT normal binary search.

Core Mental Model (CRITICAL)

You define a function:
```java
isPossible(x)
```
Properties:

If x works → all larger (or smaller) also work

Search for boundary

Example:
```text
false false false true true true
```
Binary search finds first true.

```java
public int binarySearchAnswer(int low, int high) {

    int ans = -1;

    while (low <= high) {
        int mid = low + (high - low) / 2;

        if (isPossible(mid)) {
            ans = mid;
            high = mid - 1; // try better answer
        } else {
            low = mid + 1;
        }
    }
    return ans;
}
```
Famous LeetCode Problems (MUST MASTER)
🔹 LC 875 — Koko Eating Bananas

Story:

Eating speed = k

Is it possible to eat all piles in h hours?

```java
boolean isPossible(int k, int[] piles, int h) {
    int hours = 0;
    for (int p : piles) {
        hours += (p + k - 1) / k;
    }
    return hours <= h;
}
low = 1
high = max(piles)
```
LC 1011 — Capacity To Ship Packages

Search:min capacity

check Can we ship within D days?

LC 410 — Split Array Largest Sum

Search : answer = max subarray sum

check : Can we split into ≤ K parts?

Interview Traps (VERY COMMON)

❌ Binary searching the array instead of answer
❌ Wrong low / high bounds
❌ Incorrect monotonic condition
❌ Infinite loop (low < high misuse)

⃣Sharp Interview Thinking Tricks 🧠
🔥 Trick 1 — If brute force answer is O(n)

Check if it’s monotonic → binary search it

🔥 Trick 2 — Choose search space smartly

Minimum = max single element

Maximum = sum of all elements

🔥 Trick 3 — Answer often fits in long

Use long for safety.

One-Line Recognition Rule

If answer is numeric and feasibility is monotonic → Binary Search on Answer

Comparison with Normal Binary Search

| Normal BS      | BS on Answer       |
| -------------- | ------------------ |
| Search index   | Search value       |
| Array sorted   | Feasibility sorted |
| Direct compare | Custom check       |
