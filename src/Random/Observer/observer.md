The Observer Pattern (The "Event" King)
The Observer Pattern defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified automatically.

Interview Scenarios for Observer:

Notify me when a PS5 is "Back in Stock" on Amazon.

Update all dashboards when the Bitcoin price changes.

Pub-Sub systems (Kafka/RabbitMQ basics).

The Components:
Subject (The Observable): The one being watched (e.g., The Product).

Observer: The watchers (e.g., The Users).

ou aren't just notifying a list; you're managing subscriptions, handling concurrency, and ensuring thread safety.

2. The Observer Pattern (Behavioral)
   The core idea is Decoupling: The "Subject" (the thing changing) shouldn't care who is watching it. It just screams, "Hey, I changed!" to anyone who signed up.

The Scenario: "Amazon Back-in-Stock Notifier"
We have a Product (iPhone 15). Multiple Users want an email or SMS when the stockCount goes from 0 to 1.

Step 1: Define the Interfaces
We need an interface for the Observer (the listener) and the Subject (the source).

```Java
// The Watcher
interface NotificationAlertObserver {
void update();
}

// The Source
interface StockObservable {
void add(NotificationAlertObserver observer);
void remove(NotificationAlertObserver observer);
void notifySubscribers();
void setStockCount(int newStockAdded);
int getStockCount();
}
```
Step 2: Implementation of the Subject
The IphoneObservable manages the list of people to notify.

```Java
public class IphoneObservable implements StockObservable {
private List<NotificationAlertObserver> observerList = new ArrayList<>();
private int stockCount = 0;

    @Override
    public void add(NotificationAlertObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void remove(NotificationAlertObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifySubscribers() {
        for (NotificationAlertObserver observer : observerList) {
            observer.update();
        }
    }

    @Override
    public void setStockCount(int newStockAdded) {
        if (stockCount == 0 && newStockAdded > 0) {
            notifySubscribers();
        }
        stockCount = stockCount + newStockAdded;
    }

    @Override
    public int getStockCount() {
        return stockCount;
    }
}
```
Step 3: Implementation of the Observers
Different ways to notify users (Email, Mobile).

```Java
public class EmailAlertObserver implements NotificationAlertObserver {
private String emailId;
private StockObservable observable; // Reference to get details if needed

    public EmailAlertObserver(String emailId, StockObservable observable) {
        this.emailId = emailId;
        this.observable = observable;
    }

    @Override
    public void update() {
        sendMail(emailId, "Product is in stock! Hurry!");
    }

    private void sendMail(String emailId, String msg) {
        System.out.println("Mail sent to: " + emailId);
    }
}
```
💡 SDE-2 Interview Tips: The "Deep Dive"
When you present this, an interviewer at a top company will push you on scalability and reliability. Be ready with these points:

1. Push vs. Pull Model
   Push: The Subject sends the data inside the update(data) method. (Fast, but the Observer might get data it doesn't need).

Pull: The Subject just says "I changed," and the Observer calls observable.getDetails() to get exactly what it wants. (Flexible, but requires the Observer to have a reference to the Subject).

Verdict: Mention you chose the Pull model for better decoupling.

2. Thread Safety (The "Pro" Move)
   In a real system, multiple users might subscribe/unsubscribe at the same time.

Trick: Use CopyOnWriteArrayList for the observerList. It allows you to iterate (notify) while others are adding/removing without throwing a ConcurrentModificationException.

3. Handling Failure
   Question: "What if one Observer's update() method throws an exception or takes 10 seconds to run?"

The SDE-2 Answer: "I should never run the update() logic synchronously on the main thread. In a production system, the notifySubscribers() method would push the events into a Message Queue (like Kafka or RabbitMQ) or use an ExecutorService to handle notifications asynchronously. This ensures one slow subscriber doesn't crash the whole system."

Exercise Time!
The Task: Modify the implementation so that we have a "Weather Station".

The WeatherStation (Subject) tracks temperature.

Two Observers: PhoneDisplay and WindowDisplay.

The Twist: The PhoneDisplay should only be notified if the temperature is above 30°C.

How would you modify the notifySubscribers() or update() logic to handle this specific filtering? Post your logic or code!