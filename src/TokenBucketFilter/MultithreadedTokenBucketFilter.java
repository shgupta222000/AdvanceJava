package TokenBucketFilter;

public class MultithreadedTokenBucketFilter {
    private long possibleTokens = 0;
    private final int maxTokens;
    private  final int ONE_SECONDS = 1000;
    public MultithreadedTokenBucketFilter(int maxTokens) {
        this.maxTokens = maxTokens;
    }
    private void demonThread(){
        while(true){
            synchronized(this){
                if(possibleTokens<maxTokens){
                    possibleTokens++;
                }
                this.notify();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void getToken() throws InterruptedException {
        synchronized (this) {
            while(possibleTokens==0){
                this.wait();
            }
            possibleTokens--;
        }
        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis()/1000);
    }
}
