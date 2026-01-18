package TokenBucketFilter;

import java.util.HashSet;
import java.util.Set;

public class TokenBucketFilter {
    private int maxTokens;
    long possibleTokens=0;
    private long lastRequestTime = System.currentTimeMillis();
    public TokenBucketFilter(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public synchronized void getTokens() throws InterruptedException {
        possibleTokens += (System.currentTimeMillis() - lastRequestTime)/1000;
        if(possibleTokens >= maxTokens){
            possibleTokens = maxTokens;
        }
        if(possibleTokens == 0){
            Thread.sleep(1000);
        }else{
            possibleTokens--;
        }
        lastRequestTime = System.currentTimeMillis();
        System.out.println("Granting"+ Thread.currentThread().getName()+" token at "+System.currentTimeMillis()/1000);
    }

    public static void runTestMAxTokenIsTen() throws InterruptedException {
        Set<Thread> allThreads = new HashSet<Thread>();

        final TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(1);
        Thread.sleep(10000);
        for(int i=0;i<12;i++){
            Thread thread = new Thread( new Runnable() {
                public void run() {
                    try {
                        tokenBucketFilter.getTokens();
                    } catch (InterruptedException e) {
                        System.out.println(" We have a problem");
                    }
                }
            });
            thread.setName("Thread_" + (i+1));
            allThreads.add(thread);
        }
        for(Thread thread : allThreads){
            thread.start();
        }
        for(Thread thread : allThreads){
            thread.join();
        }

    }
}
