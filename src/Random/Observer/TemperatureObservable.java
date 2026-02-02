//package Random.Observer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//interface NotificationObserver {
//    void update();
//}
//
//interface TemperatureObservable{
//    void addObserver(NotificationObserver o);
//    void removeObserver(NotificationObserver o);
//    void notifyObservers();
//
//}
//
//class PhoneDisplay implements TemperatureObservable{
//    List<NotificationObserver> observers = new ArrayList<>();
//    int temperature;
//
//    @Override
//    public void addObserver(NotificationObserver o) {
//        observers.add(o);
//
//    }
//
//    @Override
//    public void removeObserver(NotificationObserver o) {
//        observers.remove(o);
//    }
//
//    @Override
//    public void notifyObservers() {
//        for(NotificationObserver o : observers){
//            o.update();
//        }
//    }
//
//    public void setDisplay(int temp){
//        if(temp>30){
//            notifyObservers();
//        }
//        temperature = temp;
//    }
//    public int getDisplay(){
//        return temperature;
//    }
//}
//
//class WindowDisplay implements TemperatureObservable{
//    List<NotificationObserver> observers = new ArrayList<>();
//    int temperature;
//    @Override
//    public void addObserver(NotificationObserver o) {
//        observers.add(o);
//    }
//
//    @Override
//    public void removeObserver(NotificationObserver o) {
//        observers.remove(o);
//    }
//
//    @Override
//    public void notifyObservers() {
//        for(NotificationObserver o : observers){
//            o.update();
//        }
//    }
//    public void setDisplay(int temp){
//        if(temp>30){
//            notifyObservers();
//        }
//        temperature = temp;
//
//    }
//    public int getDisplay(){
//        return temperature;
//    }
//
//}

package Random.Observer;
import java.util.ArrayList;
import java.util.List;

// 1. The Watcher Interface
interface NotificationObserver {
    void update(int temp); // Passing temp here is the "Push" model
}

// 2. The Subject (The Source of Truth)
class WeatherStation {
    private List<NotificationObserver> observers = new ArrayList<>();
    private int temperature;

    public void addObserver(NotificationObserver o) { observers.add(o); }

    public void setTemperature(int newTemp) {
        this.temperature = newTemp;
        notifyObservers();
    }

    private void notifyObservers() {
        for (NotificationObserver o : observers) {
            o.update(temperature); // Tell everyone the new temp
        }
    }
}

// 3. Concrete Observer with Logic
class PhoneDisplay implements NotificationObserver {
    @Override
    public void update(int temp) {
        if (temp > 30) {
            System.out.println("Phone Display: Warning! High Temp: " + temp);
        } else {
            System.out.println("Phone Display: Temperature updated to " + temp);
        }
    }
}

class WindowDisplay implements NotificationObserver {
    @Override
    public void update(int temp) {
        System.out.println("Window Display shows: " + temp + "°C");
    }
}

/*
During an interview, once you write this, the interviewer will probe for weaknesses:

The "Filter" Logic Location: * Question: "Should the if (temp > 30) be in the WeatherStation or the PhoneDisplay?"

SDE-2 Answer: "It belongs in the Observer (PhoneDisplay). If you put it in the WeatherStation, the Station needs to know the specific business rules of every display. This breaks Encapsulation. The Station should just broadcast; the Observers decide if they care."

Memory Leaks:

Question: "What happens if a user closes the Phone app but the WeatherStation still has a reference to it in the List?"

SDE-2 Answer: "This causes a Memory Leak. To prevent this, we must ensure removeObserver is called when the object is destroyed. In Java, we could also use WeakReference for the observer list so the Garbage Collector can reclaim them if they aren't used elsewhere."

Thread Safety (Crucial):

Tip: If setTemperature is called from one thread and addObserver from another, your ArrayList will crash. Always mention CopyOnWriteArrayList for the observer list in a high-concurrency LLD round.
 */