package FizzBuzz;

public class MultithreadedFizzBuzz {
    private int n;
    private int num = 1;
    public MultithreadedFizzBuzz(int n) {
        this.n = n;
    }
    public synchronized void fizz() throws InterruptedException {
        while (num <= n){
            if(num%3==0 && num%5!=0){
                System.out.println("Fizz");
                num++;
                notifyAll();
            }else{
                wait();
            }
        }

    }
    public synchronized void buzz() throws InterruptedException {
        while(num <= n){
            if(num%3!=0 && num%5==0){
                System.out.println("Buzz");
                num++;
                notifyAll();
            }else{
                wait();
            }
        }
    }
    public synchronized void fizzbuzz() throws InterruptedException {
        while(num <= n){
            if(num%15==0){
                System.out.println("FizzBuzz");
                num++;
                notifyAll();
            }else {
                wait();
            }
        }
    }
    public synchronized void number() throws InterruptedException {
        while(num <= n){
            if(num%3!=0 && num%5!=0){
                System.out.println(num);
                num++;
                notifyAll();
            }
            else {
                wait();
            }
        }
    }
}
class FizzBuzzThread extends Thread{
    MultithreadedFizzBuzz obj;
    String method;
    public FizzBuzzThread(MultithreadedFizzBuzz obj, String method) {
        this.obj = obj;
        this.method = method;
    }
    @Override
    public void run() {
        if("Fizz".equals(method)){
            try {
                obj.fizz();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if("Buzz".equals(method)){
            try {
                obj.buzz();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if("FizzBuzz".equals(method)){
            try {
                obj.fizzbuzz();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if("Number".equals(method)){
            try {
                obj.number();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
