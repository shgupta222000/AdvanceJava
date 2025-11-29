package ProducerConsumer;

public class Producer implements Runnable {
    private final SharedBuffer sharedBuffer ;
    private final int id;
    public Producer(SharedBuffer sharedBuffer, int id){
        this.sharedBuffer = sharedBuffer;
        this.id = id;
    }
    @Override
    public void run() {
        int value=1;
        try{
            while(true){
                if(Thread.currentThread().isInterrupted()){
                    //System.out.println(Thread.currentThread().getName()+" Interrupted");
                    break;
                }
                sharedBuffer.produce(value++);
                Thread.sleep(100);
            }
        }catch(InterruptedException e){}
        System.out.println("Producer-"+id+" exiting");
    }


}
