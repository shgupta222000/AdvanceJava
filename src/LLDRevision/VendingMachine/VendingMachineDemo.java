package LLDRevision.VendingMachine;

public class VendingMachineDemo {

    public static void main(String[] args) {

        // 1. Create Vending Machine
        VendingMachine machine = new VendingMachine();

        // 2. Setup Inventory
        Product coke = new Product(1,"chips",10);
        Product chips = new Product(2,"Coke",25);

        machine.getInventory().addProduct(101, coke, 2);
        machine.getInventory().addProduct(102, chips, 1);

        System.out.println("\n--- Scenario 1: Buy Coke ---");
        machine.insertCoin();
        machine.selectProduct(101);
        machine.dispense();

        System.out.println("\n--- Scenario 2: Buy Coke again ---");
        machine.insertCoin();
        machine.selectProduct(101);
        machine.dispense();

        System.out.println("\n--- Scenario 3: Coke Out of Stock ---");
        machine.insertCoin();
        machine.selectProduct(101);   // Out of stock
        machine.selectProduct(102);   // Pick another
        machine.dispense();

        System.out.println("\n--- Scenario 4: Invalid Flow ---");
        machine.pressButton();        // No coin
        machine.dispense();           // No payment
    }
}
