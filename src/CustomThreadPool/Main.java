package CustomThreadPool;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        SimpleThreadPool pool = new SimpleThreadPool(3, 5);

        // submit some Runnable tasks
        for (int i = 1; i <= 10; i++) {
            final int id = i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " executing task " + id);
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            });
        }

        // submit Callable tasks
        Future<Integer> f = pool.submit(() -> {
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName() + " callable running");
            return 42;
        });

        System.out.println("Callable result: " + f.get()); // blocks until done

        // orderly shutdown
        pool.shutdown();
        boolean terminated = pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Terminated gracefully: " + terminated);

        // Uncomment to test shutdownNow()
        // SimpleThreadPool pool2 = new SimpleThreadPool(2, 2);
        // ... submit tasks ...
        // List<Runnable> pending = pool2.shutdownNow();
    }
}
