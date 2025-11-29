package ProducerConsumer;

public class Consumer implements Runnable {
    private final SharedBuffer sharedBuffer ;
    private final int id;
    public Consumer(SharedBuffer sharedBuffer, int id){
        this.sharedBuffer = sharedBuffer;
        this.id = id;
    }
    @Override
    public void run() {
        try {
            while (true) {
                Integer item = sharedBuffer.consume();

                if (item == null) break;

                Thread.sleep(150);
            }
        } catch (InterruptedException e) {
            // exit gracefully
        }
        System.out.println("Consumer-" + id + " exiting.");
    }

}
