✨ Concept (very short)

A Thread Pool maintains a fixed set of worker threads that fetch tasks from a shared queue and execute them.
Benefits: avoid thread-creation overhead, control concurrency, implement backpressure (bounded queue), provide graceful shutdown for services.

Interviewers expect:
•	Correct task queue synchronization (no races)
•	Proper shutdown semantics (finish queued tasks, or cancel)
•	Handling interrupts
•	Rejection when pool is shutdown
•	submit returning Future (so caller can get result / cancel)

-----------------------------------------------------------------------------------
✅ Key correctness notes & interview talking points
1.	Why lock+condition and not synchronized?
ReentrantLock gives clearer separation (two Conditions) and finer control; it reads well in production code.
2.	Blocking on enqueue when queue is full — This is a backpressure strategy. Alternative policies: reject, drop, or block with timeout.
3.	execute blocks; submit wraps Callable in FutureTask — FutureTask implements Runnable and Future, so reusing it is convenient.
4.	Shutdown semantics:
•	shutdown(): stops accepting new tasks (isShutdown=true), workers finish queued tasks then exit.
•	shutdownNow(): attempts to stop immediately: clear pending tasks, interrupt workers. Returns pending tasks.
5.	Worker loop chooses tasks under lock and executes outside the lock to avoid holding lock during execution.
6.	Interrupt handling: Workers check isStopped and exit if interrupted during await. Calling thread.interrupt() helps wake workers sleeping in await().
7.	Exception handling: Worker catches RuntimeException from tasks to avoid worker death.
8.	Complexity: enqueue/dequeue are O(1) amortized. Worker creation cost is O(poolSize) once.