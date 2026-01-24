package NumberSeries;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        PrintNumberSeries series = new PrintNumberSeries(5);

        PrintNumberSeriesThread t1 = new PrintNumberSeriesThread(series,"zero");
        PrintNumberSeriesThread t2 = new PrintNumberSeriesThread(series,"odd");
        PrintNumberSeriesThread t3 = new PrintNumberSeriesThread(series,"even");
        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
