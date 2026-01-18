package SuperManProblem;

public class Demonstration {
    public static void main(String[] args) {

    }
}

class SupermanNaiveButCorrect{
    private static SupermanNaiveButCorrect superman = new SupermanNaiveButCorrect();

    private SupermanNaiveButCorrect(){

    }
    public static SupermanNaiveButCorrect getInstance(){
        return superman;
    }
    public void fly(){
        System.out.println("I am Flying");
    }
}
class SupermanWithFlaws{
    private static SupermanWithFlaws superman;
    private SupermanWithFlaws(){}
    public static SupermanWithFlaws getInstance(){
        if(superman == null){
            superman = new SupermanWithFlaws();// fail in multiThreaded Wnvironment
        }
        return superman;
    }
}

class SupermanCorrectButSlow{
    private static SupermanCorrectButSlow superman;

    private SupermanCorrectButSlow(){}

    public static SupermanCorrectButSlow getInstance(){
        synchronized (SupermanCorrectButSlow.class){
            if(superman == null){
                superman = new SupermanCorrectButSlow();
            }
        }
        return superman;
    }
}

class Superman {
    private static volatile Superman superman;

    private Superman() {
    }

    public static Superman getInstance() {
        if (superman == null) {
            synchronized (Superman.class) {
                if (superman == null) {
                    superman = new Superman();
                }
            }
        }
        return superman;
    }

    public void fly() {
        System.out.println("I am Superman & I can fly !");
    }
}