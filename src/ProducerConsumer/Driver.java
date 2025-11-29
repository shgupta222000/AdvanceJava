package ProducerConsumer;

public class Driver {
    public static void main(String[] args) throws InterruptedException {

        SharedBuffer buffer = new SharedBuffer(5);

        Thread p1 = new Thread(new Producer(buffer, 1), "Producer-1");
        Thread p2 = new Thread(new Producer(buffer, 2), "Producer-2");

        Thread c1 = new Thread(new Consumer(buffer, 1), "Consumer-1");
        Thread c2 = new Thread(new Consumer(buffer, 2), "Consumer-2");

        p1.start();
        p2.start();
        c1.start();
        c2.start();

        Thread.sleep(3000);

        System.out.println("\n*** INITIATING SHUTDOWN ***\n");
        buffer.shutdown();
        // Interrupt producers and consumers to wake them if they are waiting
        p1.interrupt();
        p2.interrupt();
        c1.interrupt();
        c2.interrupt();
        p1.join();
        p2.join();
        c1.join();
        c2.join();

        System.out.println("\nAll producers and consumers exited cleanly.");
    }
}
