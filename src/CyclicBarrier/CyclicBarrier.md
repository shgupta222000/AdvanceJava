🚀 QUESTION 5 — Implement Your Own CyclicBarrier

(From scratch: No using java.util.concurrent.CyclicBarrier)

⸻

🎯 What is a CyclicBarrier? (Easy Explanation)

A CyclicBarrier is a synchronization aid that:
•	Lets N threads wait until all N have reached a common point.
•	Once all N threads arrive → the barrier trips, all threads proceed.
•	The barrier then resets and can be used again (cyclic behavior).

Real-world analogy:

A group of 5 friends decide to start a game only when all 5 join.
After the game ends, they can repeat again.

⸻

🧠 Concepts You Must Show in Interview

✔ Counting how many threads arrived

✔ Making threads wait

✔ Detecting when last thread arrives

✔ Running an optional barrier action (like a callback)

✔ Resetting for the next cycle

✔ Correct signaling

✔ Handling broken barrier if a thread is interrupted

✔ Avoiding deadlocks