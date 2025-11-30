package CyclicBarrier;

public class Main {
    public static void main(String[] args) {

        MyCyclicBarrier barrier = new MyCyclicBarrier(3,
                () -> System.out.println(">>> Barrier Action Executed <<<")
        );

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " reached barrier");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " passed barrier");

                // second phase
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " reached barrier second time");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " passed barrier second time");

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(task, "Thread-1").start();
        new Thread(task, "Thread-2").start();
        new Thread(task, "Thread-3").start();
    }
}
