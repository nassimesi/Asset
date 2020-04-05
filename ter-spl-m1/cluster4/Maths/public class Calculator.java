.\cluster4\Maths\public class Calculator{Integer accumulator = 0
Operation operator = null
AbstractState current = null
Maths.Calculator()spoon.support.reflect.code.CtBlockImpl@1
[public]setState(States.AbstractState){
    current = state;
}
[public]quitCalc(){
    System.exit(0);
}
[public]run(){
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
}
[private]goNext(){
    current.goNext(this);
}
[public]getOperator(){
    return operator;
}
}