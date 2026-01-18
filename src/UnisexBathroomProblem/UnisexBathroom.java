package UnisexBathroomProblem;

public class UnisexBathroom {
    String WOMEN= "women";
    String MEN = "men";
    String NONE = "none";
    String inUseBy= NONE;
    int empsInBathroom=0;

    void useBathroom( String name) throws InterruptedException {
        System.out.println(name +" Using Bathroom. Current employees in bathroom = "+ empsInBathroom);
        Thread.sleep(10000);
        System.out.println(name + " done using bathroom");
    }
    void maleUseBathroom(String name) throws InterruptedException {
        synchronized (this){
            while(inUseBy.equals(WOMEN)){
                this.wait();
            }
            empsInBathroom++;
            inUseBy= MEN;
        }
        useBathroom(name);

        synchronized (this){
            empsInBathroom--;

            if(empsInBathroom==0){
                inUseBy= NONE;
            }
            this.notifyAll();
        }
    }
    void femaleUseBathroom(String name) throws InterruptedException {
        synchronized (this){
            while(inUseBy.equals(MEN)){
                this.wait();
            }
            empsInBathroom++;
            inUseBy= WOMEN;
        }
        useBathroom(name);
        synchronized (this){
            empsInBathroom--;
            if(empsInBathroom==0){
                inUseBy= NONE;
            }
            this.notifyAll();
        }
    }
    public static void runTest() throws InterruptedException {
        final UnisexBathroom unisexBathroom = new UnisexBathroom();

        Thread female1 = new Thread(new Runnable() {
            public void run() {
                try{
                    unisexBathroom.femaleUseBathroom("Lisa");
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread male1 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Mohan");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread male2 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Anil");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread male3 = new Thread(new Runnable() {
            public void run() {
                try{
                    unisexBathroom.maleUseBathroom("Sumit");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        Thread male4 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Wentao");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        female1.start();
        male1.start();
        male2.start();
        male3.start();
        male4.start();

        female1.join();
        male1.join();
        male2.join();
        male3.join();
        male4.join();
    }
}
class Demonstration{
    public static void main(String[] args) throws InterruptedException {
        UnisexBathroom.runTest();
    }
}