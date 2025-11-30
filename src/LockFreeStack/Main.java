package LockFreeStack;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
public class Main {
    public static void main(String[] args) throws Exception {
        final TreiberStack<Integer> stack = new TreiberStack<>();
        final int PRODUCERS = 4;
        final int CONSUMERS = 4;
        final int OPS_PER_PRODUCER = 10000;

        ExecutorService ex = Executors.newFixedThreadPool(PRODUCERS + CONSUMERS);
        AtomicInteger produced = new AtomicInteger();
        AtomicInteger consumed = new AtomicInteger();

        // producers
        for (int p = 0; p < PRODUCERS; p++) {
            ex.submit(() -> {
                for (int i = 0; i < OPS_PER_PRODUCER; i++) {
                    stack.push(produced.incrementAndGet());
                }
            });
        }

        // consumers
        for (int c = 0; c < CONSUMERS; c++) {
            ex.submit(() -> {
                while (consumed.get() < PRODUCERS * OPS_PER_PRODUCER) {
                    Integer v = stack.pop();
                    if (v != null) {
                        consumed.incrementAndGet();
                    } else {
                        // avoid busy spin too hot
                        try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                    }
                }
            });
        }

        ex.shutdown();
        ex.awaitTermination(60, TimeUnit.SECONDS);

        System.out.println("Produced: " + produced.get());
        System.out.println("Consumed: " + consumed.get());
        System.out.println("Stack empty: " + stack.isEmpty());
    }
}