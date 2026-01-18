package Barrier;
class Demonstration{
    public static void main(String[] args) throws InterruptedException {
        Barrier.runTest();
    }
}
public class Barrier {
    int count =0;
    int totalThreads;
    int released =0;
    public Barrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }
    public synchronized void await() throws InterruptedException {
        count++;
        if(count==totalThreads){
            notifyAll();
            released =totalThreads;
        }else{
            while(count<totalThreads){
                wait();
            }
        }
        released--;
        if (released==0){
            count=0;
        }
    }
    public static void runTest() throws InterruptedException {
        Barrier barrier = new Barrier(3);
        Thread p1 = new Thread(()->{
            try{
                System.out.println("Thread 1");
                barrier.await();
                System.out.println("Thread 1");
                barrier.await();
                System.out.println("Thread 1");
                barrier.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        });
        Thread p2 = new Thread(()->{
            try{
                Thread.sleep(500);
                System.out.println("Thread 2");
                barrier.await();
                Thread.sleep(500);
                System.out.println("Thread 2");
                barrier.await();
                Thread.sleep(500);
                System.out.println("Thread 2");
                barrier.await();

            }catch(InterruptedException e){
                e.printStackTrace();
            }
        });

        Thread p3 = new Thread(()->{
            try {
                Thread.sleep(1500);
                System.out.println("Thread 3");
                barrier.await();
                Thread.sleep(1500);
                System.out.println("Thread 3");
                barrier.await();
                Thread.sleep(1500);
                System.out.println("Thread 3");
                barrier.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        });

        p1.start();
        p2.start();
        p3.start();
        p1.join();
        p2.join();
        p3.join();
    }
}
