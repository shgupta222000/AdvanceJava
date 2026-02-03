package LLDRevision.BMS;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

enum SeatStatus{
    AVAILABLE,
    LOCK_IN_PROGRESS,
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

    public void releaseLock() {
        this.status = SeatStatus.AVAILABLE;
        this.lockedByUserId = null;
        this.lockTimestamp = null;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public String getSeatId() {
        return seatId;
    }
}
class Show {
    private String showId;
    private List<Seat> seats;

    public Show(String showId, List<Seat> seats) {
        this.showId = showId;
        this.seats = seats;
    }

    public Seat getSeat(String seatId) {
        return seats.stream()
                .filter(seat -> seatId.equals(seat.getSeatId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }
}
class BookingResponse {
    private boolean success;
    private String message;
    private List<Seat> lockedSeats;

    public BookingResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.lockedSeats = new ArrayList<>();
    }

    public BookingResponse(boolean success, String message, List<Seat> lockedSeats) {
        this.success = success;
        this.message = message;
        this.lockedSeats = lockedSeats;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Seat> getLockedSeats() {
        return lockedSeats;
    }
}
 class BookingService {

    public BookingResponse reserveSeats(Show show, List<String> seatIds, String userId) {
        // 1. Sort seat IDs to prevent Deadlocks
        List<String> sortedSeatIds = seatIds.stream()
                .sorted()
                .collect(Collectors.toList());

        // 2. Attempt to lock all seats
        List<Seat> lockedSeats = new ArrayList<>();

        try {
            for (String id : sortedSeatIds) {
                Seat seat = show.getSeat(id);
                // We use a timeout or immediate check
                if (seat.lockSeat(userId)) {
                    lockedSeats.add(seat);
                } else {
                    // 3. Rollback: If even one seat fails, release all previously locked seats
                    unlockSeats(lockedSeats);
                    return new BookingResponse(false, "One or more seats are unavailable.");
                }
            }
        } catch (Exception e) {
            unlockSeats(lockedSeats);
            return new BookingResponse(false, "System error during booking.");
        }

        // 4. Create a temporary Booking/Transaction record
        return new BookingResponse(true, "Seats held for 10 minutes.", lockedSeats);
    }

    private void unlockSeats(List<Seat> seats) {
        for (Seat seat : seats) {
            seat.releaseLock(); // Sets status back to AVAILABLE
        }
    }
}
