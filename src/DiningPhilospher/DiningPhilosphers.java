package DiningPhilospher;

import java.util.Random;
import java.util.concurrent.Semaphore;
class Demonstration{
    public static void main(String[] args) throws InterruptedException {
        DiningPhilosphers.runTest();
    }
}
public class DiningPhilosphers {
    private Semaphore[] forks = new Semaphore[5];
    private Semaphore maxDiners = new Semaphore(4);
    public DiningPhilosphers(){
        forks[0] = new Semaphore(1);
        forks[1] = new Semaphore(1);
        forks[2] = new Semaphore(1);
        forks[3] = new Semaphore(1);
        forks[4] = new Semaphore(1);
    }

    //Represents how a philospher lives his life
    public void lifeCycleOfPhilosper(int id) throws InterruptedException {
        while (true){
            contemplate();
            eat(id);
        }
    }

    void contemplate() throws InterruptedException {
        Thread.sleep(500);
    }
    void eat(int id) throws InterruptedException {
        maxDiners.acquire(); //if not used it might cause a deadlock as what if everyone acquire left fork at once
        forks[id].acquire();
        forks[(id+4)%5].acquire();

        System.out.println("Philospher "+ id +" is eating");

        forks[id].release();
        forks[(id+4)%5].release();
        maxDiners.release();
    }
    static void startPhilospher(DiningPhilosphers dp, int id){
        try{
            dp.lifeCycleOfPhilosper(id);
        }catch(InterruptedException e){

        }
    }
    public static void runTest() throws InterruptedException {
        final DiningPhilosphers dp = new DiningPhilosphers();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                startPhilospher(dp,0);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                startPhilospher(dp,1);

            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                startPhilospher(dp,2);
            }
        });
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                startPhilospher(dp,3);
            }
        });
        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                startPhilospher(dp,4);
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
    }
}

/*
class DiningPhilosophers2 {

    private static Random random = new Random(System.currentTimeMillis());

    private Semaphore[] forks = new Semaphore[5];

    public DiningPhilosophers2() {
        forks[0] = new Semaphore(1);
        forks[1] = new Semaphore(1);
        forks[2] = new Semaphore(1);
        forks[3] = new Semaphore(1);
        forks[4] = new Semaphore(1);
    }

    public void lifecycleOfPhilosopher(int id) throws InterruptedException {

        while (true) {
            contemplate();
            eat(id);
        }
    }

    void contemplate() throws InterruptedException {
        Thread.sleep(random.nextInt(500));
    }

    void eat(int id) throws InterruptedException {

        // We randomly selected the philosopher with
        // id 3 as left-handed. All others must be
        // right-handed to avoid a deadlock.
        if (id == 3) {
            acquireForkLeftHanded(3);
        } else {
            acquireForkForRightHanded(id);
        }

        System.out.println("Philosopher " + id + " is eating");
        forks[id].release();
        forks[(id + 1) % 5].release();
    }

    void acquireForkForRightHanded(int id) throws InterruptedException {
        forks[id].acquire();
        forks[(id + 1) % 5].acquire();
    }

    // Left-handed philosopher picks the left fork first and then
    // the right one.
    void acquireForkLeftHanded(int id) throws InterruptedException {
        forks[(id + 1) % 5].acquire();
        forks[id].acquire();
    }
}
*/