package LLDRevision.Elevator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElevatorSystemMain {

    public static void main(String[] args) {

        Elevator e1 = new Elevator(1);
        Elevator e2 = new Elevator(2);

        List<Elevator> elevators = List.of(e1, e2);

        Dispatcher dispatcher = Dispatcher.getInstance(elevators);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(e1);
        executor.submit(e2);

        // Simulate requests
        dispatcher.handleExternalRequest(10, Direction.UP);
        dispatcher.handleExternalRequest(3, Direction.DOWN);
        dispatcher.handleExternalRequest(7, Direction.UP);
    }
}
