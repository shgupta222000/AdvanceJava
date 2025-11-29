
#  Reader-Writer Lock Implementation

✔ Reader-preference (many read lock operations in parallel)
✔ Writer-preference (writers should not starve)
✔ Fair (no starvation for readers or writers)

🧩 Problem Statement

Design and implement a Reader–Writer Lock with the following API:
```java
public class ReaderWriterLock {

    public void lockRead() throws InterruptedException;

    public void unlockRead();

    public void lockWrite() throws InterruptedException;

    public void unlockWrite();
}
```
#Requirements:
1.	Multiple readers must be allowed simultaneously.
2.	Only one writer at a time.
3.	While writer is active → no readers allowed.
4.	When writer is waiting →
▪ In the basic version, readers can still read (read preference model).
▪ In the writer-preference version, readers must wait.
5.	Avoid deadlocks.
6.	Avoid starvation (writer-preference version).
7.	Thread-safe.



🧠 Concept Explanation 


#Why Reader–Writer Lock Exists?

Sometimes:
•	Reads are very frequent
•	Writes are rare

Allowing multiple read threads at once massively improves throughput.

Example:
Database queries → many read operations, few updates.

Traditional lock (synchronized) blocks everyone:
•	Reader blocks reader
•	Reader blocks writer
•	Writer blocks reader

This is inefficient for read-heavy systems.

Reader–Writer Lock solves this.

-------------------------------------------------------------------------------------
🌟 Reader Preference Model

If no writer is writing, then:
•	All readers → allowed
•	Writers → must wait until ALL readers finish

Problem: Writer can starve
-------------------------------------------------------------------------------------
🌟 Writer Preference Model
If a writer is waiting, then:
•	New readers → must wait
•	Writer → allowed when current readers finish