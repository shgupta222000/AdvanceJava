package LLDRevision.BMS;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Seat> seats = new ArrayList<>();
        seats.add(createSeat("A1"));
        seats.add(createSeat("A2"));
        seats.add(createSeat("A3"));

        Show show = new Show("SHOW_1", seats);
        BookingService bookingService = new BookingService();

        List<String> requestedSeats = List.of("A1", "A2");

        BookingResponse response =
                bookingService.reserveSeats(show, requestedSeats, "USER_101");

        System.out.println(response.getMessage());
        System.out.println("Booking success: " + response.isSuccess());
    }

    private static Seat createSeat(String seatId) {
        Seat seat = new Seat();
        seat.setSeatId(seatId);
        seat.setStatus(SeatStatus.AVAILABLE);
        return seat;
    }
}
