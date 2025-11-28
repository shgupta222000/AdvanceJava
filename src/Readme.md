# Concurrency & Multithreading — Super Simple  Explanation
Explanation: shows a simple variable write/read where Thread 2 may see a stale value without proper memory visibility guarantees.

```java
int x = 0;
// Thread 1 writes x = 5
// Thread 2 still sees x = 0 (cached copy)
```

# WEEK 1 — CONCURRENCY & MULTITHREADING

1. What is Concurrency? (Super Simple Explanation)

Concurrency = Doing multiple tasks “at the same time”, but not necessarily literally at the same moment.

Example:
You are cooking:
•	Boiling water
•	Frying vegetables
•	Cutting fruits

You switch between tasks, but you give the illusion you do them all together.

Similarly, a CPU switches between threads very fast → looks parallel.

⸻

2. What is Parallelism? (Different)

Parallelism = actually doing things at the same exact time (requires multiple CPU cores).
•	Concurrency = task switching
•	Parallelism = simultaneous execution

Java supports both, depending on hardware and your design.

⸻

🧠 3. Why do we need multithreading?

Real-world backend services need to:
•	Serve multiple users at the same time
•	Run background jobs
•	Handle I/O + computation together
•	Use all CPU cores
•	Improve performance and responsiveness

Examples:
•	Zomato receives 1000 order requests → threads handle each request
•	Kafka consumer threads reading messages
•	Databases use thread pools internally
•	Every Spring Boot Web Server uses a thread pool to serve HTTP requests

⸻

⚠️ 4. Why concurrency is HARD (and interviewers love it)?

Because threads cause race conditions, deadlocks, visibility issues, and order-of-execution problems.

Common issues:
1.	Two threads modifying same variable → wrong result
2.	Thread sees stale (old) value due to caching
3.	Deadlock → system freezes
4.	Livelock → threads keep reacting but don’t progress
5.	Starvation → some thread never gets CPU time

Companies want SDE-2 who can design safe high-performance concurrent code.

⸻

🔥 5. Java Memory Model (JMM) — Why it exists?

JMM defines how and when one thread sees changes made by another.

Without JMM, CPU caching would break everything:

Example:
int x = 0;
// Thread 1 writes x = 5
// Thread 2 still sees x = 0 (cached copy)
JMM solves:
•	Visibility
•	Ordering of operations
•	Happens-before relationships

⸻

🔑 6. Tools given by Java to handle concurrency

a) synchronized
•	Oldest, simplest lock
•	Only one thread can enter code block
•	Slow (kernel-level locking)
•	But correct and reliable

b) volatile
•	Ensures visibility (no stale values)
•	Does NOT ensure atomicity
•	Used for flags, stop signals, state visibility

c) Locks (ReentrantLock)
•	More powerful than synchronized
•	TryLock, fairness, conditions
•	Faster in high contention
•	Used in 90% production systems now

d) Condition variables

Used for waiting/wakeup like:
•	notFull
•	notEmpty
•	used in blocking queue implementations

e) Atomic classes (AtomicInteger, AtomicReference, etc.)
•	Lock-free thread-safe operations
•	Use CAS (compare-and-swap)
•	Very fast, used in high-performance systems

f) Thread Pools (ThreadPoolExecutor)
•	Core of Java concurrency
•	Used by Spring Boot, web servers, schedulers
•	Avoids creating/destroying threads repeatedly
•	Covers:
•	fixed thread pool
•	cached thread pool
•	scheduled pool
•	work stealing pool

g) ForkJoinPool

Used for parallel streams & recursive divide-and-conquer tasks.

⸻

🕸️ 7. The Big 5 Concurrency Problems Asked in Interviews

✔ Producer–Consumer

– Bounded Blocking Queue
– Wait/notify or Lock + Condition

✔ Reader–Writer Problem

– Ensure multiple readers + single writer

✔ Deadlock detection & prevention

– Ordering of locks
– TryLock approach
– Lock timeout

✔ Implement ThreadPool / Executor

– One of the most common SDE-2 interview questions

✔ Lock-free Data Structures

– AtomicReference
– CAS
– Treiber Stack

⸻

💡 8. How to talk about Concurrency in Interviews (Short Speech)

If an interviewer asks:

“What is concurrency?”

Say this:

“Concurrency is the ability of a program to deal with multiple tasks at once by interleaving execution. Java uses threads and provides tools like synchronized, volatile, Locks, atomic classes, and thread pools to write safe concurrent code. Concurrency is required to scale backend systems and fully utilize CPU cores.”

Perfect, crisp SDE-2 answer.

⸻

🌟 9. What you should MASTER in Week 1
•	Thread lifecycle
•	JMM (visibility + happens-before)
•	synchronized vs Lock vs CAS
•	wait/notify vs Condition
•	Deadlock patterns
•	ThreadPoolExecutor internals
•	Atomic operations
•	Blocking Queue implementation
•	CompletableFuture basics