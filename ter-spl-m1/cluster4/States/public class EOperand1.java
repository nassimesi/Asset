.\cluster4\States\public class EOperand1{States.EOperand1()spoon.support.reflect.code.CtBlockImpl@1
[public]goNext(Maths.Calculator){
    context.setState(new EOperator());
}
[public]exec(){
    String input = null;
    System.out.print("> Please give number : ");
    input = sc.nextLine();
    return Integer.valueOf(input);
}
}