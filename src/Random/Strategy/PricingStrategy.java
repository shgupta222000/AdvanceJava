package Random.Strategy;

public interface PricingStrategy {
    int getPrice(int km);
}
class NormalStrategy implements PricingStrategy {
    @Override
    public int getPrice(int km) {
        return km*10;
    }
}
class PeakHourStrategy implements PricingStrategy {
    @Override
    public int getPrice(int km) {
        return 2*km*10;
    }
}
class Context {
    private PricingStrategy strategy;

    public Context(PricingStrategy strategy) {
        this.strategy = strategy;
    }
    public PricingStrategy getStrategy() {
        return strategy;
    }
    public double executeStrategy(int km) {
        return strategy.getPrice(km);
    }
}