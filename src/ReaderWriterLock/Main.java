package ReaderWriterLock;


public class Main {
    public static void main(String[] args) {

        ReaderWriterLock rwLock = new ReaderWriterLock();

        // Reader task
        Runnable readTask = () -> {
            try {
                rwLock.lockRead();
                System.out.println(Thread.currentThread().getName() + " reading...");
                Thread.sleep(500);
                rwLock.unlockRead();
            } catch (InterruptedException e) {}
        };

        // Writer task
        Runnable writeTask = () -> {
            try {
                rwLock.lockWrite();
                System.out.println("    " + Thread.currentThread().getName() + " WRITING...");
                Thread.sleep(1000);
                rwLock.unlockWrite();
            } catch (InterruptedException e) {}
        };

        for (int i = 1; i <= 3; i++) {
            new Thread(readTask, "Reader-" + i).start();
        }

        new Thread(writeTask, "Writer-1").start();

        for (int i = 4; i <= 6; i++) {
            new Thread(readTask, "Reader-" + i).start();
        }
    }
}