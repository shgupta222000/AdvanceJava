package LLDRevision.Elevator;

import java.util.List;

/*enum Direction{
    UP,
    DOWN,
    IDLE
}

public class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;
    private Door door= new Door();
    private boolean[] stops = new boolean[51];

    public void addStop(int floor){
        stops[floor] = true;
    }

    public void moveToFloor(int targetFloor){
        try{
            door.close();
            if(door.isClosed()){
                startMotor(targetFloor);
            }
        } catch (Exception e) {
            System.out.println("Safety Alert : "+e.getMessage());
        }
    }

    public void startMotor(int targetFloor){
        //physical movement logic
    }
    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean[] getStops() {
        return stops;
    }
}

class Dispatcher{
    private List<Elevator> elevators;

    private static volatile Dispatcher dispatcher;

    public synchronized void handleExternalRequest(int floor, Direction direction){
        Elevator bestElevator = findBestElevator(floor, direction);
        bestElevator.addStop(floor);
    }
     private Elevator findBestElevator(int floor, Direction direction){
        Elevator best = null;
        int minDistance = Integer.MAX_VALUE;

        for(Elevator e : elevators){
            int distance = calculateCost(e,floor,direction);
            if(distance < minDistance){
                minDistance = distance;
                best = e;
            }
        }
        return best;
     }
     private int calculateCost(Elevator elevator, int floor, Direction direction){
        // Logic Same direction = low cost , Opposite = high cost
         return Math.abs(elevator.getCurrentFloor()-floor);
     }
}

enum DoorState{
    OPEN,
    CLOSED,
    OBSTRUCTED,
    CLOSING
}

class Door{
    private DoorState state = DoorState.CLOSED;

    public void open(){
        state = DoorState.OPEN;
        System.out.println("Elevator is open");
    }
    public synchronized void close(){
        state = DoorState.CLOSING;
        if(sensorObstructed()){
            state = DoorState.OPEN;
            System.out.println("Objected detected in door");
        }
        state= DoorState.CLOSED;

        System.out.println("Door is securely closed.");
    }
    public boolean sensorObstructed(){
        //In real life , this reads hardware signal
        return false;
    }
    public boolean isClosed(){
        return state == DoorState.CLOSED;
    }
}*/

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

enum Direction {
    UP, DOWN, IDLE
}

enum DoorState {
    OPEN, CLOSED, OBSTRUCTED, CLOSING
}

class Door {
    private DoorState state = DoorState.CLOSED;

    public synchronized void open() {
        state = DoorState.OPEN;
        System.out.println("Door opened");
    }

    public synchronized void close() {
        state = DoorState.CLOSING;
        if (sensorObstructed()) {
            state = DoorState.OPEN;
            throw new RuntimeException("Object detected while closing door");
        }
        state = DoorState.CLOSED;
        System.out.println("Door closed");
    }

    public boolean sensorObstructed() {
        return false; // simulate hardware
    }

    public boolean isClosed() {
        return state == DoorState.CLOSED;
    }
}

class Elevator implements Runnable {
    private int id;
    private int currentFloor = 0;
    private Direction direction = Direction.IDLE;
    private Door door = new Door();
    private boolean[] stops = new boolean[51]; // 0 to 50 floors

    public Elevator(int id) {
        this.id = id;
    }

    public synchronized void addStop(int floor) {
        if (floor < 0 || floor >= stops.length)
            throw new IllegalArgumentException("Invalid floor");
        stops[floor] = true;
        System.out.println("Elevator " + id + " received request for floor " + floor);
    }

    @Override
    public void run() {
        while (true) {
            try {
                processStops();
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Elevator " + id + " error: " + e.getMessage());
            }
        }
    }

    private synchronized void processStops() {
        for (int i = 0; i < stops.length; i++) {
            if (stops[i]) {
                moveToFloor(i);
                stops[i] = false;
            }
        }
        direction = Direction.IDLE;
    }

    public void moveToFloor(int targetFloor) {
        try {
            door.close();
            if (door.isClosed()) {
                startMotor(targetFloor);
                door.open();
            }
        } catch (Exception e) {
            System.out.println("Safety Alert : " + e.getMessage());
        }
    }

    private void startMotor(int targetFloor) {
        System.out.println("Elevator " + id + " moving from " + currentFloor + " to " + targetFloor);

        if (targetFloor > currentFloor) direction = Direction.UP;
        else if (targetFloor < currentFloor) direction = Direction.DOWN;

        while (currentFloor != targetFloor) {
            if (direction == Direction.UP) currentFloor++;
            else currentFloor--;
            System.out.println("Elevator " + id + " at floor " + currentFloor);
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        System.out.println("Elevator " + id + " reached floor " + currentFloor);
    }

    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getDirection() { return direction; }
}

class Dispatcher {
    private List<Elevator> elevators;
    private static volatile Dispatcher dispatcher;

    private Dispatcher(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public static Dispatcher getInstance(List<Elevator> elevators) {
        if (dispatcher == null) {
            synchronized (Dispatcher.class) {
                if (dispatcher == null) {
                    dispatcher = new Dispatcher(elevators);
                }
            }
        }
        return dispatcher;
    }

    public synchronized void handleExternalRequest(int floor, Direction direction) {
        Elevator best = findBestElevator(floor, direction);
        best.addStop(floor);
    }

    private Elevator findBestElevator(int floor, Direction direction) {
        Elevator best = null;
        int minCost = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int cost = calculateCost(e, floor, direction);
            if (cost < minCost) {
                minCost = cost;
                best = e;
            }
        }
        return best;
    }

    private int calculateCost(Elevator e, int floor, Direction direction) {
        if (e.getDirection() == Direction.IDLE) return Math.abs(e.getCurrentFloor() - floor);
        if (e.getDirection() == direction) return Math.abs(e.getCurrentFloor() - floor);
        return Math.abs(e.getCurrentFloor() - floor) + 10; // penalty
    }
}

