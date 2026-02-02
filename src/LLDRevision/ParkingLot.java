package LLDRevision;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

enum VehicleType {
    CAR,
    TRUCK,
    BIKE
}
class Vehicle{
    private String licensePlate;
    private VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public VehicleType getType() {
        return type;
    }
}

class Car extends Vehicle{
    private String licensePlate;
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}

class Truck extends Vehicle{
    private String licensePlate;
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}

class ParkingSpot{
    private int spotNumber;
    private VehicleType type;
    private Vehicle parkedVehicle;

    public ParkingSpot(int spotNumber, VehicleType type) {
        this.spotNumber = spotNumber;
        this.type = type;
    }
    public boolean isAvailable(){
        return parkedVehicle == null;
    }
    public boolean canFitVehicle(Vehicle vehicle){
        return isAvailable() && vehicle.getType() == type;
    }
    public void park(Vehicle vehicle){
        parkedVehicle = vehicle;
    }
    public void unPark(){
        parkedVehicle = null;
    }
    public int getSpotNumber() {
        return spotNumber;
    }
    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }
}

class Level{
    private int floorNumber;
    private final List<ParkingSpot> spots;
    private final List<Observer> observer = new ArrayList<>();
    private int availableCount;
    public Level(int floorNumber, int numSpots){
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        this.availableCount = numSpots;
        for(int i=0;i<numSpots;i++){
            VehicleType type = (i<0.2*numSpots)?VehicleType.BIKE:VehicleType.CAR;
            spots.add(new ParkingSpot(i,type));
        }
    }
    public void addObserver(Observer res){
        observer.add(res);
    }
    private void notifyObservers(){
        for(Observer obs:observer){
            obs.update(floorNumber,availableCount);
        }
    }
    public synchronized boolean parkVehicle(Vehicle vehicle){
        for(ParkingSpot spot : spots){
            if(spot.canFitVehicle(vehicle)){
                spot.park(vehicle);
                availableCount--;
                notifyObservers();
                return true;
            }
        }
        return false;
    }

    public synchronized void unParkVehicle(Vehicle vehicle){
        for(ParkingSpot spot : spots){
            if(!spot.isAvailable() && spot.getParkedVehicle().equals(vehicle)){
                spot.unPark();
                availableCount++;
                notifyObservers();
                break;
            }
        }
    }
}
public class ParkingLot {
    private String name;
    private List<Level>levels;
    private static volatile ParkingLot instance; // volatile ensures visibility across Threads

    private ParkingLot(String name, int numLevels, int spotsPerLevel){
        this.name = name;
        this.levels = new ArrayList<>();
        for(int i=0;i<numLevels;i++){
            levels.add(new Level(i,spotsPerLevel));
        }
    }

    public static ParkingLot getInstance(String name, int numLevels, int spotsPerLevel){
        if(instance==null){
            synchronized (ParkingLot.class){
                if(instance==null){
                    instance = new ParkingLot(name,numLevels,spotsPerLevel);
                }
            }
        }
        return instance;
    }

    public boolean ParkVehicle(Vehicle vehicle){
        for(Level level : levels){
            if(level.parkVehicle(vehicle)){
                System.out.println(vehicle.getLicensePlate() + " Parked Successfully");
                return true;
            }
        }
        return false;
    }

    public List<Level> getLevels() {
        return levels;
    }
}

class Ticket{
    private final String TicketId;
    private final LocalDateTime entryTime;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;

    public Ticket(String TicketId, Vehicle vehicle, ParkingSpot parkingSpot){
        this.TicketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now();
    }

    public String getTicketId() {
        return TicketId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return parkingSpot;
    }
}
interface ParkingFeeStrategy{
    double calculateFee(long hours);
}
class HourlyFeeStrategy implements ParkingFeeStrategy{
    @Override
    public double calculateFee(long hours) {
        return hours*2.0;
    }
}

class WeekendFeeStrategy implements ParkingFeeStrategy{
    @Override
    public double calculateFee(long hours) {
        return hours*5.0;
    }
}

class ExitGate{
    private ParkingFeeStrategy feeStrategy;

    public ExitGate(ParkingFeeStrategy feeStrategy){
        this.feeStrategy = feeStrategy;
    }
    public double processExit(Ticket ticket){
        long hours =calculateHours(ticket.getEntryTime(), LocalDateTime.now());

        double amount = feeStrategy.calculateFee(hours);

        ticket.getSpot().unPark();
        return amount;
    }
    private  long calculateHours(LocalDateTime entryTime, LocalDateTime exitTime){
        return Math.max(1, java.time.Duration.between(entryTime, exitTime).toHours());
    }
}

interface Observer{
    void update(int levelId, int availableSpots);
}
class DisplayBoard implements Observer {
    @Override
    public void update(int levelId, int availableSpots) {
        System.out.println(
                "Level " + levelId + " | Available Spots: " + availableSpots
        );
    }
}