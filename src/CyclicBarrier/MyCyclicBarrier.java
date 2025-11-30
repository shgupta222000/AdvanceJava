package CyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCyclicBarrier {

    private final int parties;           // Number of threads required to trip the barrier
    private final Runnable barrierAction;

    private final Lock lock = new ReentrantLock();
    private final Condition trip = lock.newCondition();

    private int waiting = 0;             // Number of threads currently waiting
    private int generation = 0;          // Barrier cycle id (increments each trip)
    private boolean broken = false;

    public MyCyclicBarrier(int parties, Runnable action) {
        if (parties <= 0) throw new IllegalArgumentException();
        this.parties = parties;
        this.barrierAction = action;
    }

    public MyCyclicBarrier(int parties) {
        this(parties, null);
    }

    public int await() throws InterruptedException, BrokenBarrierException {
        lock.lock();
        try {
            int arrivalGeneration = generation;

            if (broken) {
                throw new BrokenBarrierException();
            }

            waiting++;

            if (waiting == parties) {
                // Last thread arrives

                if (barrierAction != null) {
                    barrierAction.run();
                }

                // Reset for next cycle
                nextGeneration();
                return 0; // For the last arriving thread
            }

            // Not last thread → wait
            while (arrivalGeneration == generation && !broken) {
                trip.await();
            }

            if (broken) {
                throw new BrokenBarrierException();
            }

            return waiting;

        } finally {
            lock.unlock();
        }
    }

    private void nextGeneration() {
        trip.signalAll();
        waiting = 0;
        generation++;
    }

    private void breakBarrier() {
        broken = true;
        trip.signalAll();
    }

    public boolean isBroken() {
        return broken;
    }
}
/*
	Each barrier cycle has a generation number.
	•	When threads arrive:
	•	waiting++
	•	if waiting == parties → last thread arrived
	•	Last thread runs:
	•	optional barrierAction
	•	resets the barrier for next use
	•	wakes all waiting threads with signalAll
	•	Other threads wait in loop:
	while (arrivalGeneration == generation) await();
 */