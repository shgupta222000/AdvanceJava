The Movie Booking System (e.g., BookMyShow). The Hook: This is the ultimate "Concurrency" question.

How do you ensure two people don't book the exact same seat at the exact same time in a cinema with 500 people?

Would you like to start with the Story of how we handle "Seat Locking"?

That is the natural first instinct, but here is where Mastery begins. In a high-scale system like BookMyShow, "Seat Level Locking" in the way we locked a ParkingSpot or InventorySlot can actually lead to a terrible user experience.

1. The Story: The "Stale Seat" Nightmare
   Imagine you are booking a ticket for a blockbuster movie.

You click on Seat A10. It's green (Available).

You spend 2 minutes entering your credit card details.

You hit "Pay."

Error: "Sorry, this seat was booked by someone else 5 seconds ago."

If we only lock at the moment of assignment, we get frustrated users. If we lock the seat the moment you click it, and you walk away from your computer, that seat is "stuck" forever.

2. The Intuition: Optimistic vs. Pessimistic Locking
   In LLD interviews for booking systems, you must distinguish between these two:

Pessimistic Locking: You lock the seat in the database the moment the user selects it. No one else can even look at it. (Safe, but bad for performance).

Optimistic Locking: You allow multiple people to "select" it, but only the first one to successfully "update" the status to BOOKED wins. (Fast, but high failure rate at the finish line).

The Industry Standard: A hybrid approach called "Soft Locking" with an Expiry.

3. Core Entities
   To handle this, we need more than just a Seat class.

Entity,Responsibility
Show,A specific movie at a specific time in a specific Hall.
Seat,"Has a SeatStatus (AVAILABLE, LOCKED, BOOKED)."
Booking,"Links a User, a Show, and multiple Seats."
Payment,Handles the transaction.

4. Step 1: The "Soft Lock" (The Interview Winner)
   Instead of a simple boolean isOccupied, we introduce a Lock object or a status with a timestamp.

```java
public enum SeatStatus {
    AVAILABLE,
    LOCK_IN_PROGRESS, // Temporary hold
    BOOKED
}

public class Seat {
    private String seatId;
    private SeatStatus status;
    private LocalDateTime lockTimestamp;
    private String lockedByUserId;

    public synchronized boolean lockSeat(String userId) {
        if (status == SeatStatus.AVAILABLE) {
            this.status = SeatStatus.LOCK_IN_PROGRESS;
            this.lockedByUserId = userId;
            this.lockTimestamp = LocalDateTime.now();
            return true;
        }
        // Check if the existing lock has expired (e.g., after 10 mins)
        if (status == SeatStatus.LOCK_IN_PROGRESS && isLockExpired()) {
            this.status = SeatStatus.LOCK_IN_PROGRESS;
            this.lockedByUserId = userId;
            this.lockTimestamp = LocalDateTime.now();
            return true;
        }
        return false;
    }

    private boolean isLockExpired() {
        return Duration.between(lockTimestamp, LocalDateTime.now()).toMinutes() > 10;
    }
}
```
5. The "Why, Where, When" Thread
   Why 10 minutes? This is a business decision. It gives the user enough time to pay but prevents "denial of service" where seats are held indefinitely.

Where do we handle the "Double Booking"? In the BookingService. When the payment is successful, we check if the userId on the lock still matches the userId making the payment.

When do we use a Database Lock? In a distributed system (multiple servers), Java's synchronized won't work. We would use Redis Distributed Locks or SELECT FOR UPDATE in SQL.

6. The "Search" Problem
   If a cinema has 500 seats, and 10,000 people are trying to find available seats at once, how do we show the "Seat Map" quickly?

Mastery Concept: In-Memory Cache. The "Read" operations (showing the map) are served from a cache like Redis. The "Write" operations (locking/booking) go to the Database.

Your Mastery Challenge
Imagine the user selects 4 seats (A1, A2, A3, A4) for their family.

Should we lock them one by one, or lock all 4 at once? The Catch: What if Thread A locks A1 and A2, while Thread B locks A3 and A4, and they both need the other two to complete their family booking? (This is a classic Deadlock scenario).

How would you prevent this deadlock during seat selection?
