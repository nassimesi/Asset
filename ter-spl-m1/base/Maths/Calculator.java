
public class Calculator {

Integer accumulator = 0;

Operation operator = null;

AbstractState current = null;

Maths.Calculator(){
    current = new EOperand1();
};
public void setState(AbstractState state){
    current = state;
};
public void quitCalc(){
    System.exit(0);
};
public void run(){
    while (true) {
        accumulator = 0;
        Integer a = ((Integer) (current.exec()));
        goNext();
        Operation op = ((Operation) (current.exec()));
        goNext();
        Integer b = ((Integer) (current.exec()));
        accumulator += op.compute(a, b);
        System.out.println("res : " + accumulator);
    } 
};
private void goNext(){
    current.goNext(this);
};
public Operation getOperator(){
    return operator;
};
public class Calculator {

Integer accumulator = 0;

Operation operator = null;

AbstractState current = null;

Maths.Calculator(){
    current = new EOperand1();
};
public void setState(AbstractState state){
    current = state;
};
public void quitCalc(){
    System.exit(0);
};
public void run(){
    while (true) {
        accumulator = 0;
        Integer a = ((Integer) (current.exec()));
        goNext();
        Operation op = ((Operation) (current.exec()));
        goNext();
        Integer b = ((Integer) (current.exec()));
        accumulator += op.compute(a, b);
        System.out.println("res : " + accumulator);
    } 
};
private void goNext(){
    current.goNext(this);
};
public Operation getOperator(){
    return operator;
};
public class Calculator {

Integer accumulator = 0;

Operation operator = null;

AbstractState current = null;

Maths.Calculator(){
    current = new EOperand1();
};
public void setState(AbstractState state){
    current = state;
};
public void quitCalc(){
    System.exit(0);
};
public void run(){
    while (true) {
        accumulator = 0;
        Integer a = ((Integer) (current.exec()));
        goNext();
        Operation op = ((Operation) (current.exec()));
        goNext();
        Integer b = ((Integer) (current.exec()));
        accumulator += op.compute(a, b);
        System.out.println("res : " + accumulator);
    } 
};
private void goNext(){
    current.goNext(this);
};
public Operation getOperator(){
    return operator;
};}