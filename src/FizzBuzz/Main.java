package FizzBuzz;

public class Main {
    public static void main(String[] args) {
        MultithreadedFizzBuzz obj = new MultithreadedFizzBuzz(15);

        Thread t1 = new FizzBuzzThread(obj,"Fizz");
        Thread t2 = new FizzBuzzThread(obj,"Buzz");
        Thread t3 = new FizzBuzzThread(obj,"FizzBuzz");
        Thread t4 = new FizzBuzzThread(obj,"Number");

        t2.start();
        t1.start();
        t3.start();
        t4.start();
    }
}
