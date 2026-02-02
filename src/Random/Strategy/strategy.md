The Problem: "The Dynamic Pricing Engine"
Scenario: You are building a Pricing Engine for a Ride-Sharing app (like Uber or Lyft). The price of a ride depends on the "Mode" of the day:

Normal Mode: Flat rate of $10 per km.

Peak Hours: 2x multiplier on the flat rate.

Surge/Rainy Mode: 5x multiplier on the flat rate.

Discount Mode: 0.5x (50% off) for new users.

Phase 1: The "Junior" Approach (Avoid This)
Usually, candidates start by writing a PricingService with a massive switch statement.

```Java
public double calculatePrice(double km, String type) {
if (type.equals("NORMAL")) return km * 10;
else if (type.equals("PEAK")) return km * 10 * 2;
else if (type.equals("SURGE")) return km * 10 * 5;
// Adding a new type requires modifying this class! (Violates Open/Closed)
}
```
Phase 2: The Exercise (Your Turn)
Your Task: Refactor the logic above using the Strategy Pattern. Follow these steps:

Create a PricingStrategy interface with a method double getPrice(double km).

Implement two concrete strategies: RegularPricing and SurgePricing.

Create a RideContext class that holds a reference to the strategy and has a method double executeStrategy(double km).

🚀 Bonus "SDE-2" Challenge:
How would you handle the case where the RideContext needs to switch strategies dynamically mid-ride (e.g., the ride started during Normal hours but ended during Peak hours)?

How to format your answer:
Show the Interface.

Show one or two Concrete Classes.

Show the Context class.

Briefly explain how you handled the Bonus Challenge.

1. The Code Review (SDE-2 Perspective)Encapsulation: You included a getStrategy() getter. In a strict Strategy Pattern, the Context should hide the strategy from the outside world to maintain encapsulation. The client shouldn't necessarily "reach into" the Context once it's set.Data Types: For financial calculations (like pricing), never use int or double in a real interview. Use BigDecimal to avoid floating-point errors. For this exercise, double is fine, but mentioning BigDecimal to an interviewer earns you massive "Senior" points.Naming: Your Context class is named Context. In a real LLD interview, name it according to the domain (e.g., RidePricingEngine or Trip).2. The "Bonus Challenge" SolutionYou asked how to handle dynamic switches (e.g., Normal $\rightarrow$ Peak mid-ride).The Solution: Add a Setter Method in the Context.This allows the strategy to be swapped at runtime without recreating the Context object.Javapublic void setStrategy(PricingStrategy strategy) {
   this.strategy = strategy;
   }
   Interview Insight: If the pricing is based on segments (first 5km at Normal, next 5km at Peak), you could even pass a List<PricingStrategy> or use a Decorator Pattern on top of the Strategy, but usually, a simple setter is what the interviewer is looking for to see "Runtime Interchangeability."3. Interview "Tough" Questions on StrategyBefore we move to the next pattern, be ready for these "Curveballs":Question: "Where should the logic for 'Which Strategy to use' live? Inside the Context?"The SDE-2 Answer: "No. The Context should remain agnostic. The Client or a Factory should decide which strategy to inject into the Context. This keeps the Context lean."Question: "What if I have 50 strategies? Isn't that a lot of classes?"The SDE-2 Answer: "Yes, that's the trade-off (Class Explosion). If the strategies are simple, I'd use Lambdas/Functional Interfaces. If they are complex, the class overhead is a small price to pay for the testability and adherence to the Open/Closed Principle."

In your previous code, someone still has to write new NormalStrategy() or new PeakHourStrategy(). If that logic stays in your main business flow, you've just pushed the if-else mess to a different file.

To achieve true decoupling, we use a Strategy Factory to handle the creation, and we keep the strategies stateless so we can reuse them (Singleton-ish).

The Advanced Tweak: Strategy + Factory
1. The Strategy Map (The "Cache")
   Instead of creating a new object every time, we store them in a Map.

```Java
public class PricingStrategyFactory {
// Store strategies in a map for quick lookup
private static final Map<String, PricingStrategy> strategies = new HashMap<>();

    static {
        strategies.put("NORMAL", new NormalStrategy());
        strategies.put("PEAK", new PeakHourStrategy());
        // Adding a new strategy? Just add one line here or use Reflection
    }

    public static PricingStrategy getStrategy(String type) {
        return strategies.getOrDefault(type, new NormalStrategy());
    }
}
```
2. The Clean Client Code
   Now, look how clean your "Ride Booking" logic becomes. No new keywords, no switch statements.

```Java
public class RideService {
public void bookRide(int km, String rideType) {
// Factory decides WHICH strategy, Strategy decides HOW to calculate
PricingStrategy strategy = PricingStrategyFactory.getStrategy(rideType);

        Context pricingEngine = new Context(strategy);
        double price = pricingEngine.executeStrategy(km);
        
        System.out.println("Final Price: " + price);
    }
}
```
Why this wins the Interview:
Memory Efficiency: By caching strategies in the Factory, you aren't spamming the Heap with new objects (important for high-throughput systems like Uber).

Single Responsibility: The RideService only cares about booking, the Factory only cares about creation, and the Strategy only cares about the math.

Extensibility: If a new "Holiday Mode" comes out, you create the class and register it in the Factory. Zero changes to your RideService.

Quick Check: "The Trap"
An interviewer might ask: "What if the Strategy needs user-specific data (like a user's loyalty points) to calculate the price? Does your Factory still work?"

The SDE-2 Answer: "If the strategy needs state, it's no longer a Singleton. I would either pass the User object into the getPrice(int km, User user) method, or have the Factory return a new instance instead of a cached one."
