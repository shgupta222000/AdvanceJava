🚀 QUESTION — Implement a Lock-Free Stack (Treiber Stack)

We’ll cover:
1.	What it is and why it matters
2.	Brute (synchronized) version — easy and correct
3.	Better: Treiber lock-free stack using AtomicReference (CAS)
4.	Best: ABA-safe version using AtomicStampedReference (stamp = version)
5.	Tests / driver to exercise concurrency
6.	Interview talking points and pitfalls

⸻

🧠 Quick concept — what & why

What: A stack with push() and pop() operations that multiple threads can use concurrently.

Why lock-free?
•	Avoids locks → higher throughput under contention
•	Threads don’t block each other (no mutex), so less risk of deadlock
•	Useful in low-latency systems, schedulers, or internal libraries

Key primitive: CAS (compare-and-set). In Java: AtomicReference.compareAndSet(expected, update).

Main caveat: ABA problem — a location goes A → B → A so a CAS that checks “A” may be tricked. We’ll show mitigation.