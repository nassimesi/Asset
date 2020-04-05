.\cluster4\States\public class EOperand2{States.EOperand2()spoon.support.reflect.code.CtBlockImpl@1
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