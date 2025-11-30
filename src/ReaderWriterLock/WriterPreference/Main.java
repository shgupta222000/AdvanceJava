package ReaderWriterLock.WriterPreference;

public class Main {
    public static void main(String[] args) {

        ReaderWriterLockWriterPreference rwLock = new ReaderWriterLockWriterPreference();

        Runnable readTask = () -> {
            try {
                rwLock.lockRead();
                System.out.println(Thread.currentThread().getName() + " reading...");
                Thread.sleep(500);
                rwLock.unlockRead();
            } catch (InterruptedException e) {}
        };

        Runnable writeTask = () -> {
            try {
                rwLock.lockWrite();
                System.out.println("    " + Thread.currentThread().getName() + " WRITING...");
                Thread.sleep(1000);
                rwLock.unlockWrite();
            } catch (InterruptedException e) {}
        };

        // 3 readers first
        for (int i = 1; i <= 3; i++) {
            new Thread(readTask, "Reader-" + i).start();
        }

        // Writer arrives
        new Thread(writeTask, "Writer-1").start();

        // These readers MUST wait until writer finishes
        for (int i = 4; i <= 6; i++) {
            new Thread(readTask, "Reader-" + i).start();
        }
    }
}
