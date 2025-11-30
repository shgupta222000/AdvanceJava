🎯 What Writer-Preference Means

✔ If a writer is waiting,

➡️ new readers cannot enter,
➡️ even if there is no active writer right now.

✔ Writers get exclusive access as soon as possible.

✔ Prevents writer starvation.

⸻

👌 Real-world example:

Imagine a database:
•	Many users are reading
•	Few admin actions update records

If readers never stop, admin updates will never happen.

Writer-preference RW lock solves this.

⸻

🧠 Core Design Rules

Readers can proceed only when:
•	no writer is active AND
•	no writer is waiting

Writers must wait until:
•	no readers are active
•	no writer is active

When unlocking:
•	If writers are waiting → wake writer first
•	Else → allow readers
