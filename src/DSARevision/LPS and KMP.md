KMP Knuth Morris Pratt Algorithm

Story: Why it Exists?
Problem:

Find pattern P inside text T

Brute force:
	•	On mismatch → restart pattern from beginning
	•	Wastes work

KMP says:

“Don’t recheck what you already know matches.”

⸻

2️⃣ When to THINK of KMP (INTERVIEW TRIGGERS)

Immediately think KMP when you hear:

✅ Substring search
✅ Pattern matching
✅ Repeated patterns
✅ Avoid O(n·m)
✅ Long strings

3️⃣ Core Mental Model (NO JARGON)

Key idea:

Pattern matches itself.

Example:
```text
Pattern = "abab"
```
When mismatch happens:
•	You don’t start from 0
•	You jump to the longest prefix which is also suffix

This information is stored in LPS array of pattern.

4️⃣ LPS Array (HEART OF KMP)

LPS[i] means:

Length of longest proper prefix which is also suffix
for pattern[0..i]

Example:
```text
Pattern = a b a b
Index     0 1 2 3
LPS       0 0 1 2
```
Building LPS (STEP-BY-STEP)
```java
public int[] buildLPS(String pattern) {
    int m = pattern.length();
    int[] lps = new int[m];
    int len = 0; // length of previous longest prefix suffix
    int i = 1;  
    while(i < m){
        if(pattern.charAt(i) == pattern.charAt(len)){
            len++;
            lps[i] = len;
            i++;
        } else {
            if(len != 0){
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
    }
    return lps;
}
```
⸻
5️⃣ KMP Search Algorithm (USING LPS)
```java
public int KMPSearch(String text, String pattern){
    int i=0,j=0;
    int n= text.length();
    int m = pattern.length();
    while(i<n){
        if(text.charAt(i)==pattern.charAt(j)){
            i++;
            j++;
        }
        if(j==pattern.length())return i-j; // match found
        else if(i<n && text.charAt(i)!=pattern.charAt(j)){
            if(j!=0){
                j=lps[j-1];
            }else{
                i++;
            }
        }
    }
    return -1;
}
```

7️⃣ Time & Space (INTERVIEW MUST)
•	Time: O(n + m)
•	Space: O(m)

⸻

8️⃣ Famous LeetCode Problems

🔹 LC 28 — Find the Index of First Occurrence

Classic KMP usage.

⸻

🔹 LC 459 — Repeated Substring Pattern
Trick
```text
lps[n-1] > 0 && n % (n - lps[n-1]) == 0
```
🔹 LC 1392 — Longest Happy Prefix

Direct LPS usage.

⸻

9️⃣ Interview Traps (VERY COMMON)

❌ Misunderstanding LPS
❌ Restarting j = 0 always
❌ Off-by-one errors
❌ Overengineering

🔥 Sharp Interview Thinking Tricks

🧠 Trick 1 — Pattern inside pattern

If problem talks about:
•	Prefix == suffix
•	Repeated patterns

👉 LPS instantly.

⸻

🧠 Trick 2 — Avoid Hash Collision

If Rabin–Karp scares you → KMP is safe.

⸻

🧠 Trick 3 — Build LPS ONCE

Reuse it for multiple searches.

KMP = smart skipping using pattern’s own structure

What interviewers may ask next 👀
•	Write KMP to count occurrences
•	Modify KMP to return all indices
•	Explain why KMP is O(n + m)
•	Explain overlapping matches