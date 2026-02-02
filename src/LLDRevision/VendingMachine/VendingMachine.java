package LLDRevision.VendingMachine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

interface State{
    public void insertCoin(VendingMachine machine);
    public void pressButton(VendingMachine machine);
    public void selectProduct(VendingMachine machine,int code);
    public void dispense(VendingMachine machine);

}
//Idle State Machine is waiting

class  IdleState implements State{

    public IdleState(){
        System.out.println(" Machine is IDLE. Insert the coin to proceed");
    }
    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println(" Coin Inserted");
        machine.setCurrentState(new HasMoneyState());
    }
    @Override
    public void pressButton(VendingMachine machine) {
        System.out.println("Error : Insert Coin First");
    }
    @Override
    public void selectProduct(VendingMachine machine,int code) {
        System.out.println("Error : Insert Coin First");
    }
    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Error : Payment required");
    }

}

public class VendingMachine {
    private State currentState;
    private Inventory inventory;
    private double balance;

    public VendingMachine(){
        currentState = new IdleState();
        inventory = new Inventory();
    }

    public synchronized void setCurrentState(State state){
        currentState = state;
    }
    public synchronized void insertCoin(){
        currentState.insertCoin(this);
    }
    public synchronized void pressButton(){
        currentState.pressButton(this);
    }
    public synchronized void selectProduct(int code){
        currentState.selectProduct(this, code);
    }
    public void dispense(){
        currentState.dispense(this);
    }

    public Inventory getInventory() {
        return inventory;
    }
    public void addBalance(double amount){
        this.balance += amount;
    }

    public void setBalance(int i) {
        this.balance = i;
    }
}

class HasMoneyState implements State{
    public HasMoneyState(){

    }
    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Coin Accepted. Balance update");
        machine.addBalance(1.0);
    }
    @Override
    public void pressButton(VendingMachine machine) {
        System.out.println("Error : Select Product First");
    }
    @Override
    public void selectProduct(VendingMachine machine,int code) {
        if(machine.getInventory().isAvailable(code)){
            System.out.println("Product Selected: "+ code);
            machine.setCurrentState(new DispensingState(code));
        }else{
            System.out.println("Product Not Available Ot of Stock ..!");
            // Transition back to Idle or stay here ..?
            // usually stay here so they can pick something else.
        }
    }
    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Error : Pay First ");
    }
}
class Inventory{
    private final Map<Integer, InventorySlot> slots = new ConcurrentHashMap<>();

    public void addProduct(int code, Product product, int quantity){
        slots.put(code, new InventorySlot(product, quantity));
    }
    public boolean isAvailable(int code){
        return slots.containsKey(code) && slots.get(code).getQuantity()>0;

    }
    public Product getProduct(int code){
        return slots.get(code).getProduct();
    }
    public void deductQuantity(int code){
        InventorySlot slot = slots.get(code);
        if(slot != null){
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
class Product{
    private int id ;
    private String name;
    private int price;
    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
}

class DispensingState implements State{
    private final int selectionCode;

    public DispensingState(int selectionCode) {
        this.selectionCode = selectionCode;
    }
    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Wait, currently dispensing");
    }

    @Override
    public void pressButton(VendingMachine machine) {
        System.out.println("Wait, currently dispensing");
    }

    @Override
    public void selectProduct(VendingMachine machine, int code) {
        System.out.println("Wait, currently dispensing");
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
}