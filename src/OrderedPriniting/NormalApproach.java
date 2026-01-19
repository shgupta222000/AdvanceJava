package OrderedPriniting;

public class NormalApproach {
}
class OrderedPrinting {

    int count;

    public OrderedPrinting() {
        count = 1;
    }

    public void printFirst() throws InterruptedException {

        synchronized(this){
            System.out.println("First");
            count++;
            this.notifyAll();
        }
    }

    public void printSecond() throws InterruptedException {

        synchronized(this){
            while(count != 2){
                this.wait();
            }
            System.out.println("Second");
            count++;
            this.notifyAll();
        }

    }

    public void printThird() throws InterruptedException {

        synchronized(this){
            while(count != 3){
                this.wait();
            }
            System.out.println("Third");
        }

    }
}

class OrderedPrintingThread extends Thread
{
    private OrderedPrinting obj;
    private String method;

    public OrderedPrintingThread(OrderedPrinting obj, String method)
    {
        this.method = method;
        this.obj = obj;
    }

    public void run()
    {
        //for printing "First"
        if ("first".equals(method))
        {
            try
            {
                obj.printFirst();
            }
            catch(InterruptedException e)
            {

            }
        }
        //for printing "Second"
        else if ("second".equals(method))
        {
            try
            {
                obj.printSecond();
            }
            catch(InterruptedException e)
            {

            }
        }
        //for printing "Third"
        else if ("third".equals(method))
        {
            try
            {
                obj.printThird();
            }
            catch(InterruptedException e)
            {

            }
        }
    }
}

