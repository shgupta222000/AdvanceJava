package LLDRevision;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParkingMain {
    /*public static void main(String[] args) throws InterruptedException {
        ParkingLot parkinglot = ParkingLot.getInstance("AmbienceMall",2,10);

        DisplayBoard displayBoard = new DisplayBoard();
        for(Level level : parkinglot.getLevels()){
            level.addObserver(displayBoard);
        }
        int totalCars =10;
        List<Thread> threads = new ArrayList<Thread>();
        for(int i=1;i<=totalCars;i++){
            final int carNo =i;

            Thread t = new Thread(()->{
                Vehicle car = new Car("CAR-" +carNo);
                boolean parked = parkinglot.ParkVehicle(car);
                if(!parked){
                    System.out.println(car.getLicensePlate()+" Parking Full");
                }
            });
            threads.add(t);
        }
        for(Thread t : threads){
            t.start();
        }
        for(Thread t : threads){
            t.join();
        }
        System.out.println(" All Parking Attempt Completed");
    }*/
        public static void main(String[] args) throws InterruptedException {

            // 1️⃣ Create Parking Lot (Singleton)
            ParkingLot parkingLot = ParkingLot.getInstance(
                    "Phoenix Mall Parking",
                    2,   // levels
                    10   // spots per level
            );

            // 2️⃣ Add observers to levels
            DisplayBoard board = new DisplayBoard();
            for (Level level : parkingLot.getLevels()) { // make levels package-private OR add getter
                level.addObserver(board);
            }

            // 3️⃣ Create Vehicles
            Vehicle car1 = new Car("KA-01-1234");
            Vehicle car2 = new Car("KA-02-5678");
            Vehicle bike1 = new Vehicle("KA-03-9999", VehicleType.BIKE);

            // 4️⃣ Park Vehicles
            parkingLot.ParkVehicle(car1);
            parkingLot.ParkVehicle(car2);
            parkingLot.ParkVehicle(bike1);

            // 5️⃣ Manually create a Ticket (entry)
            ParkingSpot spot = new ParkingSpot(1, VehicleType.CAR);
            spot.park(car1);

            Ticket ticket = new Ticket(
                    UUID.randomUUID().toString(),
                    car1,
                    spot
            );

            // Simulate parking time
            Thread.sleep(2000);

            // 6️⃣ Exit Gate with Hourly Strategy
            ExitGate weekdayExit = new ExitGate(new HourlyFeeStrategy());
            double amount = weekdayExit.processExit(ticket);

            System.out.println(
                    "Vehicle " + car1.getLicensePlate() +
                            " exited. Parking Fee: ₹" + amount
            );

            // 7️⃣ Weekend Strategy Test
            ExitGate weekendExit = new ExitGate(new WeekendFeeStrategy());
            double weekendAmount = weekendExit.processExit(ticket);

            System.out.println(
                    "Weekend Fee: ₹" + weekendAmount
            );
        }
}
