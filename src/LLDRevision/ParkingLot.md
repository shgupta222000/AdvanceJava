The Parking Lot
This is the "Hello World" of LLD, but most people overcomplicate it or miss the nuances of concurrency.

1. The Story
   Imagine you are standing at the entrance of a massive, multi-floor mall parking lot. There are bikes, cars, and large trucks. As a driver enters, they need a ticket that tells them exactly where to go. As they leave, they pay based on how long they stayed.

2. The Initial Intuition (The "Naive" Approach)
   If you were building this in 10 minutes, you'd probably just create a List<Slot>. When a car comes, you loop through the list, find the first isAvailable == true, and park it.

The Problem: * What if a Truck tries to park in a Bike slot?

What if two cars try to grab the last spot at the exact same millisecond? (Thread safety!)

What if the mall adds "Electric Charging" spots later?

3. Core Entities
   Before we code, we need to identify the "Actors" and "Objects."

Entity,Responsibility
ParkingLot,The Singleton manager. It knows about floors and gates.
Level/Floor,Manages a collection of slots.
ParkingSpot,"The actual space (Small, Medium, Large)."
Vehicle,"Abstract class (Car, Bike, Truck)."
Ticket,"Stores entry time, spot info, and vehicle info."

4. The "Why, Where, When" Thread
Why use Inheritance for Vehicles? Because a Car and a Truck both have a license plate, but they require different SpotTypes.

Where should the "Is Full" logic live? Usually at the Level or ParkingLot level to prevent scanning every single spot every time.

When do we worry about Thread Safety? The moment the assignTicket() method is called. In a real-world scenario, multiple entry gates are hitting the database/memory at once.

Next Step for You
To keep this "slow and mastery-focused," I want you to start by defining the Vehicle and ParkingSpot relationship.

Should a ParkingSpot "know" about the Vehicle parked in it, or should the Vehicle "know" its ParkingSpot? Why?

Once you give me your take on that, we will write the first block of code together and tackle the Thread Safety of the assignment logic.

parking spot should know vehicle 

Spot on. Having the ParkingSpot know which Vehicle is occupying it is the more natural and "Stateful" way to model this.

The Intuition
Think of the ParkingSpot as a Container. A container (the spot) can be empty or hold an object (the vehicle). If the vehicle "knew" its spot, the spot itself would still need a way to tell the rest of the system, "Hey, I'm busy!" By giving the ParkingSpot a reference to a Vehicle, we make the spot the Source of Truth for availability.

Step 1: Defining the Foundation
Let's start with the base classes. We need to handle different vehicle sizes to ensure a Truck doesn't squeeze into a Bike spot.

The Vehicle Hierarchy

```Java
public enum VehicleType {
BIKE, CAR, TRUCK
}

public abstract class Vehicle {
private String licensePlate;
private VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public VehicleType getType() { return type; }
}

public class Car extends Vehicle {
public Car(String licensePlate) {
super(licensePlate, VehicleType.CAR);
}
}
```
The Parking Spot (The Container)

```Java
public class ParkingSpot {
private final int spotNumber;
private final VehicleType supportedType;
private Vehicle parkedVehicle; // The "Knowing" part

    public ParkingSpot(int spotNumber, VehicleType supportedType) {
        this.spotNumber = spotNumber;
        this.supportedType = supportedType;
    }

    // Checking if a vehicle can actually fit here
    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public boolean canFitVehicle(Vehicle vehicle) {
        return isAvailable() && vehicle.getType() == supportedType;
    }

    public void park(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
    }

    public void unpark() {
        this.parkedVehicle = null;
    }
}
```
The Evolution: The Concurrency Problem
Now, let's look at the "Why" of Thread Safety.

Imagine a busy Saturday at the mall. There are two entry gates.

Gate A finds Spot #101 is empty.

Gate B finds Spot #101 is empty (at the exact same microsecond).

Gate A assigns Spot #101 to a Car.

Gate B assigns Spot #101 to another Car.

Result: A physical crash in the parking lot and a bug in your code.

How do we fix this?
In Java, we often use the synchronized keyword or Locks. However, locking the entire ParkingLot would be slow (only one car could enter the mall at a time).

The Mastery Questions (For You)
Where should the Locking happen? Should we lock the individual ParkingSpot, the Level, or the whole ParkingLot?

The "When" of Data Consistency: If we have 10 floors, and Gate A is looking for a spot on Floor 1, and Gate B is looking for a spot on Floor 10, should they block each other?

How would you structure the Level class to find an available spot safely without making every driver wait in a single line?

Granularity of Locking. If Gate A and Gate B are looking at different floors, they shouldn't care about each other. If we lock the whole ParkingLot, we create a bottleneck. Your intuition is correct—we should lock at the level where the "search and grab" happens.

The Mastery Concept: Locking Strategy
To achieve high concurrency (letting many cars park at once), we use a Level-Level Lock.

Why not the Spot? If you lock the ParkingSpot, you first have to find it. By the time you find it, someone else might have grabbed it. You’d be locking 1000s of objects individually, which is messy.

Why not the ParkingLot? Too slow. It’s like having 10 entries to a stadium but only one person allowed through the turnstiles at a time across the whole city.

The Sweet Spot (The Level): Each Level (Floor) manages its own spots. Gate A can look at Floor 1 while Gate B looks at Floor 2 simultaneously.

Step 2: The Level Class with Thread Safety
We will use synchronized on the method that finds and sets the spot to ensure that the "Check then Act" operation is Atomic.

```Java
import java.util.ArrayList;
import java.util.List;

public class Level {
private final int floorNumber;
private final List<ParkingSpot> spots;

    public Level(int floorNumber, int numSpots) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        // For simplicity, let's say 20% are for Bikes, 80% for Cars
        for (int i = 0; i < numSpots; i++) {
            VehicleType type = (i < numSpots * 0.2) ? VehicleType.BIKE : VehicleType.CAR;
            spots.add(new ParkingSpot(i, type));
        }
    }

    // The "Mastery" Method: Thread Safe assignment
    public synchronized boolean parkVehicle(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.canFitVehicle(vehicle)) {
                spot.park(vehicle); // State change happens inside the lock
                return true;
            }
        }
        return false; // Floor is full for this vehicle type
    }

    public synchronized void unparkVehicle(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (!spot.isAvailable() && spot.getParkedVehicle().equals(vehicle)) {
                spot.unpark();
                break;
            }
        }
    }
}
```
The "Why, Where, When" Thread
Why synchronized here? Because if (spot.canFitVehicle) followed by spot.park() is a Check-then-Act race condition. Without the lock, two threads could both see canFitVehicle as true before either has finished calling park().

Where is the bottleneck? If a floor has 500 spots, the for loop inside a lock might be slow.

When to optimize? If we had thousands of spots per floor, we might use a Queue or Set of available spots instead of a list of all spots to avoid the O(N) search.

Interview Ready: The "Strategy" Pattern
In an interview, they might ask: "What if the parking fee changes? Cars pay $2/hr, but Trucks pay $5/hr?" If we put the payment logic inside the Vehicle or ParkingLot class, we violate the Open-Closed Principle (you’d have to modify existing code every time a price changes).

The Solution: The Strategy Pattern. We create a PaymentStrategy interface.

Next Step for You: How would you design the ParkingLot (Singleton) class to coordinate between these floors? Remember, it needs to handle a vehicle entering and find the first available floor.

Bonus Question: Why should the ParkingLot be a Singleton?

Spot on. If you have two instances of the ParkingLot class, you effectively have two "parallel universes" managing the same physical space. One instance thinks Spot A is free, while the other thinks it's occupied.

In LLD, the Singleton Pattern ensures there is a single, global point of access to the shared resource (the parking lot state).

Step 3: The Singleton ParkingLot
In an interview, don't just write a basic Singleton. Show them you understand Double-Checked Locking to ensure it’s thread-safe and performant.

```Java
public class ParkingLot {
private String name;
private List<Level> levels;
private static volatile ParkingLot instance; // volatile ensures visibility across threads

    private ParkingLot(String name, int numLevels, int spotsPerLevel) {
        this.name = name;
        this.levels = new ArrayList<>();
        for (int i = 0; i < numLevels; i++) {
            levels.add(new Level(i, spotsPerLevel));
        }
    }

    public static ParkingLot getInstance(String name, int numLevels, int spotsPerLevel) {
        if (instance == null) { // First check
            synchronized (ParkingLot.class) {
                if (instance == null) { // Second check
                    instance = new ParkingLot(name, numLevels, spotsPerLevel);
                }
            }
        }
        return instance;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        for (Level level : levels) {
            if (level.parkVehicle(vehicle)) {
                System.out.println(vehicle.getType() + " parked successfully.");
                return true;
            }
        }
        return false;
    }
}
```
The Mastery Deep Dive: Why volatile?
You’ll often get asked this in senior interviews. The "Why": Without volatile, a thread might see a half-initialized instance because of how the CPU reorders instructions. volatile ensures that the write to the instance variable happens only after the ParkingLot object is fully constructed.

Step 4: Adding the "Story" – The Entry/Exit Gate
Now we need a way for the user to interact. We don't want the user calling ParkingLot.parkVehicle() directly. We need an EntryGate and an ExitGate.

Why separate Gate classes?

Separation of Concerns: The ParkingLot manages the state of the floors. The EntryGate manages the process of entering (issuing tickets, opening the barrier).

Scalability: You can have 10 EntryGate objects all pointing to the same ParkingLot singleton.

Let's talk about the Ticket
When a car enters, we shouldn't just say "Go to Spot 10". We need to give them a Ticket.

Mastery Question for you: What information should the Ticket store? If we want to calculate the price at the exit, does the Ticket need to store the Spot or just the Entry Time?

And a tricky one: If a user loses their ticket, how would our system find their car?

In a real-world system, the License Plate Number becomes the unique identifier that links the physical car to the digital record.1. The Ticket DesignThe Ticket is a "Value Object." Once it's created, its core details (like entry time) shouldn't change.Javaimport java.time.LocalDateTime;

```Java
import java.util.UUID;

public class Ticket {
private final String ticketId;
private final LocalDateTime entryTime;
private final Vehicle vehicle;
private final ParkingSpot spot;

    public Ticket(Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = UUID.randomUUID().toString();
        this.entryTime = LocalDateTime.now();
        this.vehicle = vehicle;
        this.spot = spot;
    }

    // Getters only - Ticket is mostly immutable
    public String getTicketId() { return ticketId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
}
```
2. Solving the "Lost Ticket" MysteryYou suggested searching by vehicle number. This brings up an interesting architectural choice: Where do we store the "Active Tickets"?If we only store the ticket in the driver's hand, and they lose it, the data is gone. To solve this, the ParkingLot (or a TicketManager) should maintain a Map<String, Ticket> where the key is the License Plate.Why a Map?Speed: Searching $O(1)$ instead of looping through every floor ($O(N)$) to find where a specific license plate is parked.Efficiency: During the Exit process, we just ask for the plate, pull the ticket, calculate the time, and we're done.3. The "Strategy Pattern" for PricingLet’s make this "Interview Ready." The interviewer says: "Next week, we want to charge double on Sundays." If you put if(Sunday) price * 2 inside your code, you fail the Open-Closed Principle. Instead, we use a Strategy Pattern.
```Java 
public interface ParkingFeeStrategy {
   double calculateFee(long hours);
   }

public class HourlyFeeStrategy implements ParkingFeeStrategy {
@Override
public double calculateFee(long hours) {
return hours * 2.0; // $2 per hour
}
}

public class WeekendFeeStrategy implements ParkingFeeStrategy {
@Override
public double calculateFee(long hours) {
return hours * 5.0; // $5 per hour on weekends
}
}
```
4. The Exit Gate LogicThis is where everything comes together: The Ticket, the Strategy, and the Thread Safety.
```Java
public class ExitGate {
    private ParkingFeeStrategy feeStrategy;

   public ExitGate(ParkingFeeStrategy strategy) {
   this.feeStrategy = strategy;
   }

   public double processExit(Ticket ticket) {
   // 1. Calculate time stayed
   long hours = calculateHours(ticket.getEntryTime(), LocalDateTime.now());

        // 2. Apply strategy
        double amount = feeStrategy.calculateFee(hours);
        
        // 3. Free the spot
        ticket.getSpot().unpark();
        
        return amount;
   }

   private long calculateHours(LocalDateTime start, LocalDateTime end) {
   // Simplified logic: assume at least 1 hour
   return Math.max(1, java.time.Duration.between(start, end).toHours());
   }
   }
``` 
The Mastery Review: Why, Where, WhenWhy use UUID for Ticket ID? 

To ensure uniqueness across multiple entry gates without them needing to talk to each other to "pick the next number.

"Where does the Fee Strategy live? It’s passed into the ExitGate. This allows different gates (e.g., VIP Exit vs. Regular Exit) to have different pricing rules.

When does the spot become available? ONLY after the unpark() method is called at the exit.Next Step for YouWe have a working flow! But there is one more "Senior Level" nuance: The Observer Pattern.When a spot becomes free, how does the "Big Electronic Display" at the mall entrance know to update the "Available Spots" count?Should the ParkingLot constantly poll the floors, or should the Level "tell" the display when something changes? What do you think?

If the ParkingLot constantly asks the Level, "Are you full yet? How about now?", that's Polling, which is a waste of CPU cycles. Instead, we use the Observer Pattern (or Pub-Sub), where the Level acts as the Subject and the DisplayBoard acts as the Observer.The moment a state changes (a car parks or unparks), the Level "pushes" that update.1. The Intuition: The "Push" ModelThink of it like a YouTube subscription. You don't check a creator's channel every hour; you get a notification the second a video drops.Why this is Interview-Ready: It shows you understand Decoupling. The Level doesn't need to know how the DisplayBoard works (whether it's an LCD screen, a mobile app, or a web dashboard). It just knows it needs to notify its subscribers.2. Implementing the Observer PatternJavaimport java.util.ArrayList;

```Java
import java.util.List;

// The Observer Interface
interface Observer {
void update(int levelId, int availableSpots);
}

// The Subject (The Level)
public class Level {
private final int floorNumber;
private final List<ParkingSpot> spots;
private final List<Observer> observers = new ArrayList<>();
private int availableCount;

    public Level(int floorNumber, int numSpots) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        // Initialize spots...
        this.availableCount = numSpots; 
    }

    public void addObserver(Observer res) { observers.add(res); }

    private void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(floorNumber, availableCount);
        }
    }

    public synchronized boolean parkVehicle(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.canFitVehicle(vehicle)) {
                spot.park(vehicle);
                availableCount--; // State Change
                notifyObservers(); // Automatic Push
                return true;
            }
        }
        return false;
    }

    public synchronized void unparkVehicle(Vehicle vehicle) {
        // ... logic to find spot ...
        // spot.unpark();
        availableCount++; // State Change
        notifyObservers(); // Automatic Push
    }
}
```
3. The "Why, Where, When" ThreadWhy notify inside the synchronized block? To ensure the display never shows a "stale" number. If you notify outside the lock, another thread could change the count before the display is updated.Where is the DisplayBoard initialized? Usually in the ParkingLot constructor or a Main configuration class, then attached to each level.When would this fail? If the update() method in the Observer is slow (e.g., it makes a network call), it will hold up the lock and slow down the whole parking process. In a real senior-level system, the notification would be sent to an Asynchronous Queue.4. Final Review: The Full ArchitectureWe have built a system that is:Thread-Safe: Uses Level-level locking and Singleton with Double-Checked Locking.Extensible: Uses the Strategy Pattern for fees (Open-Closed Principle).Decoupled: Uses the Observer Pattern for displays.Organized: Follows SOLID principles (Single Responsibility for Gates, Levels, and Spots).Mastery Summary of The Parking LotProblemDesign SolutionGlobal AccessSingleton PatternRace ConditionsLevel-level SynchronizationDifferent Vehicle SizesInheritance + EnumsChanging Pricing RulesStrategy PatternReal-time UpdatesObserver Pattern