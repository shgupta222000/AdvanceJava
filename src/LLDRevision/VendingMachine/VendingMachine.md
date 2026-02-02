The Vending Machine. The Hook: A Vending Machine is a "State Machine." It behaves differently depending on whether it has money, is out of stock, or is currently dispensing.

Quick Question to start: If a Vending Machine is "Out of Stock," should the pressButton() method just have a big if-else block, or is there a cleaner way to handle behavior changes?

Moving the Vending Machine to an Idle State sounds simple, but in LLD, it’s the foundation of the State Design Pattern.

If we use if-else or switch statements to check the machine's state (e.g., if (state == HAS_MONEY)), our code becomes a "spaghetti" mess as we add more states like Dispensing or Maintenance.

Instead, we treat State as an object.

1. The Story
   Imagine the Vending Machine sitting in a quiet office corner. It’s in the Idle State.

If you press the "Dispense" button now, nothing happens.

If you try to select a product, it tells you "Insert Coin First."

The only valid action is Insert Coin.

Once you insert a coin, the machine transforms. It's the same physical box, but it now follows a different set of rules. This "transformation" is what we call a State Transition.

2. The Intuition: The State Pattern
   We define an interface called State that lists every possible action a user can take. Then, we create specific classes (like IdleState, HasMoneyState) that implement those actions differently.

3. Step 1: The State Interface
   Every state must handle these four core actions. If an action is invalid for that state (like dispensing while idle), the state itself handles the error message.

```java
public interface State {
    void insertCoin(VendingMachine machine);
    void pressButton(VendingMachine machine);
    void selectProduct(VendingMachine machine, int code);
    void dispense(VendingMachine machine);
}
```
4. Step 2: The Idle State (The "Beginning")
   In the Idle State, the machine is waiting. The only way to leave this state is by inserting money.

```java
public class IdleState implements State {

    public IdleState() {
        System.out.println("Machine is IDLE. Please insert a coin.");
    }

    @Override
    public void insertCoin(VendingMachine machine) {
        // The Transition logic
        System.out.println("Coin inserted!");
        machine.setCurrentState(new HasMoneyState()); 
    }

    @Override
    public void pressButton(VendingMachine machine) {
        System.out.println("Error: Insert coin first.");
    }

    @Override
    public void selectProduct(VendingMachine machine, int code) {
        System.out.println("Error: Insert coin first.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Error: Payment required.");
    }
}
```
5. The "Why, Where, When" Thread
   Why use the State Pattern? It follows the Single Responsibility Principle. The IdleState class only cares about what happens when the machine is idle. It doesn't need to know how the "Dispense" logic works.

Where do the states live? The VendingMachine (the Context) holds a reference to the CurrentState.

When does the transition happen? Inside the state methods. The IdleState "knows" that an insertCoin action should push the machine into the HasMoneyState.

6. The Context: The Vending Machine Class
   This class just delegates all the work to the current state object.

```java
public class VendingMachine {
    private State currentState;
    private Inventory inventory;
    private double balance;

    public VendingMachine() {
        // Machine starts in Idle State
        this.currentState = new IdleState();
        this.inventory = new Inventory();
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    // Delegation: The machine itself doesn't "know" what to do, 
    // it asks the state object.
    public void insertCoin() {
        currentState.insertCoin(this);
    }

    public void pressButton() {
        currentState.pressButton(this);
    }
    
    // ... other methods ...
}
```
Mastery Check: Thread Safety in Vending Machines
Imagine two people stand at the Vending Machine.

Person A inserts a coin. The state changes to HasMoneyState.

Person B (at the same time) tries to hit insertCoin.

If our setCurrentState or insertCoin methods aren't handled correctly, the machine might swallow a coin or skip a transition.

Next Step for You: In our ParkingLot, we used Level-level locks. For a Vending Machine, we only have one physical machine.

Should we synchronize the methods in the VendingMachine class, or should we make the State transitions themselves thread-safe? (Think about what happens if the state object is swapped while another thread is halfway through a method).

This is a sophisticated design choice. When we move from the "Naive" State Pattern to an "Interview Ready" one, we have to handle the Race Condition of Transitions.

If Thread A is currently executing selectProduct() and is halfway through transitioning the machine to DispensingState, we must prevent Thread B from calling insertCoin() and potentially overriding the state or messing with the balance.

1. The Strategy: Locking the Context
   In a Vending Machine, the Context (VendingMachine) is the "Source of Truth." To ensure no other thread interrupts a state transition, we must synchronize on the machine instance itself.

The "Why": If we synchronized inside the State classes, it wouldn't work because the currentState object is constantly being swapped out. Locking an object that is about to be replaced is like locking a door while someone is changing the locks.

2. Implementation: Thread-Safe Delegation
   We use the synchronized keyword on the context methods. This ensures that the entire "Action + Transition" becomes an Atomic Operation.

```java
public class VendingMachine {
    private State currentState;
    private Inventory inventory;
    private double balance;

    public VendingMachine() {
        this.currentState = new IdleState();
        this.inventory = new Inventory();
    }

    // This is the "Gatekeeper"
    // synchronized ensures Thread B waits until Thread A finishes the state logic
    public synchronized void insertCoin() {
        currentState.insertCoin(this);
    }

    public synchronized void selectProduct(int code) {
        currentState.selectProduct(this, code);
    }

    public synchronized void pressButton() {
        currentState.pressButton(this);
    }

    public synchronized void setCurrentState(State state) {
        this.currentState = state;
    }
    
    // Inventory and Balance also need thread-safe access
    public Inventory getInventory() { return inventory; }
    public void addBalance(double amount) { this.balance += amount; }
}
```
3. Deep Dive: Handling "Mid-Action" Interruptions
   What if dispense() takes 5 seconds (simulating a physical motor moving)? With synchronized, the machine is "Locked" for those 5 seconds. In a Vending Machine, this is actually correct. You don't want someone else inserting money while the mechanical arm is still dropping a soda.

4. The "Why, Where, When" Thread
   Why volatile for currentState? Even with synchronization, it's good practice to mark private volatile State currentState;. This ensures that as soon as one thread updates the state, all other threads see the new state immediately (visibility).

Where should the logic for "Insufficient Funds" live? It should live inside the selectProduct method of the HasMoneyState.

When do we use a ReentrantLock instead? If we wanted to implement a "Timeout"—for example, if a user doesn't pick a product within 30 seconds, the machine should automatically return the coin and go back to Idle.

5. Transitioning to "HasMoneyState"
   Let's look at how the HasMoneyState handles a transition safely.

```java
public class HasMoneyState implements State {
    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Coin accepted. Balance updated.");
        machine.addBalance(1.0); 
        // We stay in HasMoneyState
    }

    @Override
    public void selectProduct(VendingMachine machine, int code) {
        if (machine.getInventory().isAvailable(code)) {
            System.out.println("Product selected: " + code);
            machine.setCurrentState(new DispensingState(code));
        } else {
            System.out.println("Out of stock!");
            // Transition back to Idle or stay here? 
            // Usually stay here so they can pick something else.
        }
    }
    // ... other methods ...
}
```
Mastery Challenge: The Inventory
We've handled the states, but what about the Inventory? If the Vending Machine has multiple slots, and two different threads (maybe an admin refilling and a user buying) access the inventory:

How would you design the Inventory class to be thread-safe without locking the whole Vending Machine? (Hint: Think about ConcurrentHashMap vs a simple Map).

Would you like me to show you the Inventory implementation next, or should we refine the "Dispensing" state logic first?

To make the Inventory interview-ready, we need to move beyond a simple list. In a real-world vending machine, you have "Slots" (like A1, B2). If an admin is refilling Slot B2, it shouldn't stop a customer from buying a Coke from Slot A1.

1. The Intuition: Fine-Grained Locking
   If we synchronize the entire Inventory class, we create a bottleneck. Instead, we want Fine-Grained Locking. We can achieve this by using a ConcurrentHashMap or by locking individual "Product Slots."

2. The Core Entities
   Product: The item (Coke, Pepsi).

InventorySlot: A container for a specific product and its count.

Inventory: The collection of these slots.

3. Implementation: The Thread-Safe Inventory

```java
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {
    // Map of Product Code -> InventorySlot
    private final Map<Integer, InventorySlot> slots = new ConcurrentHashMap<>();

    public void addProduct(int code, Product product, int quantity) {
        slots.put(code, new InventorySlot(product, quantity));
    }

    public boolean isAvailable(int code) {
        return slots.containsKey(code) && slots.get(code).getQuantity() > 0;
    }

    public Product getProduct(int code) {
        return slots.get(code).getProduct();
    }

    public void deductQuantity(int code) {
        InventorySlot slot = slots.get(code);
        if (slot != null) {
            slot.decrement();
        }
    }
}

class InventorySlot {
    private final Product product;
    private int quantity;

    public InventorySlot(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Thread-safe decrement for the specific slot
    public synchronized void decrement() {
        if (quantity > 0) {
            quantity--;
        }
    }

    public synchronized int getQuantity() { return quantity; }
    public Product getProduct() { return product; }
}
```
4. The "Why, Where, When" Thread
   Why ConcurrentHashMap? It allows multiple threads to read and even write to different parts of the map simultaneously without blocking each other. This is much faster than a Collections.synchronizedMap.

Where did the locking go? Notice we moved the synchronized keyword to the InventorySlot level.

Scenario: Thread A is buying from Slot 1. Thread B is buying from Slot 2.

Result: They run in parallel. Only if Thread A and Thread C both try to buy from Slot 1 will one have to wait.

When to use an AtomicInteger? If the only thing you are doing is decreasing the count, an AtomicInteger for quantity would be even faster than synchronized, as it uses low-level CPU instructions (Compare-And-Swap) instead of OS-level locks.

5. Connecting it to the State Pattern
   Now, let's see how our DispensingState uses this inventory safely.

```java
public class DispensingState implements State {
    private final int selectionCode;

    public DispensingState(int code) {
        this.selectionCode = code;
    }

    @Override
    public void dispense(VendingMachine machine) {
        // 1. Deduct from inventory
        machine.getInventory().deductQuantity(selectionCode);
        
        // 2. Release product (physical simulation)
        System.out.println("Dispensing product " + selectionCode);

        // 3. Reset machine
        machine.setBalance(0); 
        machine.setCurrentState(new IdleState());
    }

    // Other methods (insertCoin, selectProduct) throw "Wait, currently dispensing" errors
}
```
Mastery Summary
We have combined two types of thread safety:

Context Locking: We lock the VendingMachine during state transitions so the "Flow" isn't interrupted.

Resource Locking: We lock the InventorySlot so the "Count" remains accurate even during high-volume refills/purchases.

