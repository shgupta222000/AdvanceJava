package NumberSeries;

import java.util.concurrent.Semaphore;

public class PrintNumberSeries {
    private int n;
    Semaphore zeroSem, oddSem, evenSem;
    public PrintNumberSeries(int n) {
        this.n= n;
        zeroSem = new Semaphore(1);
        oddSem = new Semaphore(0);
        evenSem = new Semaphore(0);
    }
    public void printZero() throws InterruptedException {
        for(int i=0;i<n;++i){
            try {
                zeroSem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("0");

            if (i % 2 == 0) {
                oddSem.release();
            } else {
                evenSem.release();
            }
        }
    }
    public void printOdd() throws InterruptedException {
        for(int i=1;i<n;i+=2){
            try {
                oddSem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(i);
            zeroSem.release();
        }
    }
    public void printEven() throws InterruptedException {
        for(int i=2;i<n;i+=2){
            try {
                evenSem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(i);
            zeroSem.release();
        }
    }
}
class PrintNumberSeriesThread extends Thread {

    PrintNumberSeries zeo;
    String method;
    public PrintNumberSeriesThread(PrintNumberSeries zeo, String method) {
        this.zeo = zeo;
        this.method = method;
    }
    @Override
    public void run() {
        if("zero".equals(method)){
            try{
                zeo.printZero();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        else if("odd".equals(method)){
            try{
                zeo.printOdd();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }else if("even".equals(method)){
            try{
                zeo.printEven();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
