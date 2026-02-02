The Decorator Pattern (Structural)
The Decorator Pattern allows you to dynamically add behavior to an individual object without affecting the behavior of other objects from the same class.

The "Why"
Imagine a Coffee Shop (Starbucks). You have a Beverage base class.

The "Junior" Mistake: Creating classes like EspressoWithMilk, EspressoWithCaramel, LatteWithMilkAndCaramel. This leads to Class Explosion.

The SDE-2 Solution: Create a base beverage and "wrap" it with decorators (Milk, Caramel, Whip) at runtime.

Java Implementation: The Coffee System
1. The Component Interface
   Java
   public abstract class Beverage {
   String description = "Unknown Beverage";
   public String getDescription() { return description; }
   public abstract double cost();
   }
2. The Decorator (Abstract)
   The decorator is-a Beverage (inheritance) and has-a Beverage (composition).

Java
public abstract class AddonDecorator extends Beverage {
protected Beverage beverage; // The object being wrapped
public abstract String getDescription();
}
3. Concrete Decorator
   Java
   public class Milk extends AddonDecorator {
   public Milk(Beverage beverage) {
   this.beverage = beverage;
   }

   @Override
   public String getDescription() {
   return beverage.getDescription() + ", Milk";
   }

   @Override
   public double cost() {
   return beverage.cost() + 0.50; // Base cost + milk cost
   }
   }
   💡 SDE-2 Interview Tips: The "Deep Dive"
   Tip 1: Real World Example: If an interviewer asks where this is used in Java, point to Java I/O. BufferedInputStream(FileInputStream(File)) is the classic Decorator. FileInputStream is the base; Buffered is the decorator adding "buffering" capability.

Tip 2: Decorator vs. Proxy:

Decorator: Adds behavior (features).

Proxy: Controls access (security, logging, lazy loading). They look identical in code structure, but their intent is different.

Tip 3: The "Lego" Analogy: Explain that decorators are like Lego pieces you stack on top of a base. You can add as many as you want in any order.

Exercise 4: The File Compression & Encryption System
Scenario: You are building a FileStream system.

The base class is SimpleFileStream which just writes text.

You need a CompressionDecorator (adds [Compressed] to the string).

You need an EncryptionDecorator (adds [Encrypted] to the string).

Task:

Implement the structure so a user can do: new EncryptionDecorator(new CompressionDecorator(new SimpleFileStream())).

What happens if the order is changed? Does the pattern still work?

Follow-up Questions:

Q1: How do you handle a situation where a specific decorator (e.g., LuxuryPackaging) can only be added once, while others (e.g., ExtraSugar) can be added multiple times?

Q2: Does the Decorator pattern violate the Liskov Substitution Principle?


