#Problem Statement

#Design and implement a Producer–Consumer system with:

✔ Multiple producers

✔ Multiple consumers

✔ Shared blocking queue

✔ Graceful shutdown (no deadlocks, no stuck threads)

✔ Consumers stop automatically when shutdown is triggered

✔ Producers stop producing after shutdown

✔ No item loss (unless intentionally discarded)
-------------------------------------------------------------------------------------------
🎯 Functional Requirements
1.	Producers keep producing items and pushing into the queue.
2.	Consumers keep consuming until:
•	queue is empty, AND
•	shutdown is triggered.
3.	Calling shutdown() should:
•	Stop producers from adding more items
•	Wake all consumers
•	Allow all threads to exit gracefully
4.	No busy-waiting.
-------------------------------------------------------------------------------------------
#Architecture Diagram
+----------------------+        +------------------------+
|     Producer-1       | ---->  |                        |
+----------------------+        |                        |
|                        |
+----------------------+        |  BoundedBlockingQueue  | ---> Consumers
|     Producer-2       | ---->  |
+----------------------+        |                        |
|                        |
+----------------------+        +------------------------+
|     Producer-N       |
+----------------------+

-------------------------------------------------------------------------------------------
#Implementation

Producers run in a loop generating items and enqueueing them. Consumers dequeue items and process them.
A shutdown flag must be shared.
Producers must stop producing when shutdown happens.
Consumers must wake up if waiting and exit cleanly when the queue is empty during shutdown.