package CountingSemaphore;

public class Demonstration {
    public static void main(String[] args) throws InterruptedException {
        final CountingSemaphore cs  = new CountingSemaphore(1);
        Thread t1 = new Thread(() -> {
            try{
                for(int i = 0; i < 5; i++){
                    cs.acquire();
                    System.out.println("Ping " +i);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try
                {
                for(int i = 0; i < 5; i++){
                    cs.release();
                    System.out.println("Pong " +i);
                }
                }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        });

        t2.start();
        t1.start();
        t1.join();
        t2.join();
    }
}

class CountingSemaphore{
    int usedPermits=0;
    int maxCount =0;

    public CountingSemaphore(int maxCount){
        this.maxCount=maxCount;
    }
    public synchronized void acquire() throws InterruptedException{
        while(usedPermits==maxCount){
            wait();
        }
        usedPermits++;
        notify();
    }
    public synchronized void release() throws InterruptedException {
        while(usedPermits==0){
            wait();
        }
        usedPermits--;
        notify();
    }
}