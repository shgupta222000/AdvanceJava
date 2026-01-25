package BuildH2O;

public class Main
{
    public static void main(String[] args) {

        H2OMachine molecule = new H2OMachine();

        Thread t1 = new H2OMachineThread(molecule,"H");
        Thread t2 = new H2OMachineThread(molecule,"O");
        Thread t3 = new H2OMachineThread(molecule,"H");
        Thread t4 = new H2OMachineThread(molecule,"O");

        t2.start();
        t1.start();
        t4.start();
        t3.start();
    }
}
