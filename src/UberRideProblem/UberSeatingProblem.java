package UberRideProblem;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
class Demonstration{
    public static void main(String[] args) throws InterruptedException {
        UberSeatingProblem.runTest();
    }
}
public class UberSeatingProblem {

    private int republicans = 0;
    private int democrats = 0;

    CyclicBarrier barrier = new CyclicBarrier(4);
    ReentrantLock lock = new ReentrantLock();
    Semaphore demsWaiting = new Semaphore(0);
    Semaphore repubsWaiting = new Semaphore(0);

    void drive(){
        System.out.println("Uber Ride on Its wayyy... with ride leader "+ Thread.currentThread().getName());
        System.out.flush();
    }
    void seatDemocrat() throws InterruptedException, BrokenBarrierException {
        boolean rideLeader = false;
        lock.lock();

        democrats++;
        if(democrats==4){
            //Seat all the democrats in the Uber ride.
            demsWaiting.release(3);
            democrats-=4;
            rideLeader=true;
        }else if(democrats == 2 && republicans>=2){
            demsWaiting.release(1);
            repubsWaiting.release(2);
            rideLeader=true;
            democrats -=2;
            republicans-=2;
        }else{
            lock.unlock();
            demsWaiting.acquire();
        }
        seated();
        barrier.await();

        if(rideLeader == true){
            drive();
            lock.unlock();
        }

    }
    void seated(){
        System.out.println(Thread.currentThread().getName() + " seated");
        System.out.flush();
    }
    void seatRepublican() throws InterruptedException, BrokenBarrierException {
        boolean rideLeader = false;
        lock.lock();
        republicans++;
        if(republicans==4){
            repubsWaiting.release(3);
            republicans-=4;
            rideLeader=true;
        }else if(republicans==2 && democrats>=2){
            repubsWaiting.release(1);
            demsWaiting.release(2);
            rideLeader=true;
            democrats-=2;
            republicans-=2;
        }else{
            lock.unlock();
            repubsWaiting.acquire();
        }
        seated();
        barrier.await();

        if(rideLeader == true){
            drive();
            lock.unlock();
        }
    }
    public static void runTest() throws InterruptedException{
        final UberSeatingProblem problem = new UberSeatingProblem();
        Set<Thread> allThreads = new HashSet<Thread>();

        for(int i=0;i<10;i++){
            Thread thread = new Thread(new Runnable(){
                public void run(){
                    try{
                        problem.seatDemocrat();
                    } catch (BrokenBarrierException e) {
                        System.out.println("We have a problem");
                    } catch (InterruptedException e) {
                        System.out.println("We have a problem");
                    }
                }
            });
            thread.setName("Democrat_"+(i+1));
            allThreads.add(thread);

            Thread.sleep(500);
        }

        for(int i=0;i<14;i++){
            Thread thread = new Thread(()->{
                try{
                    problem.seatRepublican();
                } catch (BrokenBarrierException e) {
                    System.out.println("We have a problem");
                } catch (InterruptedException e) {
                    System.out.println("We have a problem");
                }
            });
            thread.setName("Republican_"+(i+1));
            allThreads.add(thread);
            Thread.sleep(500);
        }

        for(Thread thread:allThreads){
            thread.start();
        }

        for(Thread thread:allThreads){
            thread.join();
        }
    }
}
