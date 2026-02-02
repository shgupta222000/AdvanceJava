DYNAMIC PROGRAMMING (CORE INTERVIEW PATTERNS)

It’s systematic thinking + discipline.

1️⃣ DP Story — Why It Exists

Brute force recursion:

Repeats same work

Exponential time ❌

DP says:

“If you solve a subproblem once, store it.”

2️⃣ The ONLY 4 DP Questions You Must Answer (EVERY TIME)

Before coding, say this out loud:

State — What am I storing?

Transition — How do I build from smaller?

Base case

Answer location

🟢 DP PATTERN 1 — 1D DP (TAKE / NOT TAKE)
3️⃣ Story Example — Climbing Stairs
🔹 LC 70 — Climbing Stairs
State
dp[i] = number of ways to reach step i
Transition
dp[i] = dp[i-1] + dp[i-2]

```java
public int climbStairs(int n) {
    if (n <= 2) return n;

    int prev2 = 1, prev1 = 2;
    for (int i = 3; i <= n; i++) {
        int curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

🟢 DP PATTERN 2 — HOUSE ROBBER ⭐⭐⭐
🔹 LC 198 — House Robber
State
dp[i] = max money till index i
Transition
dp[i] = max(dp[i-1], nums[i] + dp[i-2])

```java
public int rob(int[] nums) {
    int prev2 = 0, prev1 = 0;

    for (int n : nums) {
        int curr = Math.max(prev1, prev2 + n);
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```
