.\cluster4\States\public class EOperator{String add = "Addition.OperationLabel"
States.EOperator()spoon.support.reflect.code.CtBlockImpl@1
[public]goNext(Maths.Calculator){
    context.setState(new EOperand2());
}
[public]exec(){
    String input = null;
    System.out.println("> Please select an Operation [x] : ");
    System.out.println(("> [" + add) + "] for Addition ");
    input = sc.nextLine();
    if (input.equals(add)) {
        return new Addition();
    }
    return null;
}
String sub = "Subtraction.OperationLabel"
String mult = "Multiplication.OperationLabel"
[public]exec(){
    String input = null;
    System.out.println("> Please select an Operation [x] : ");
    System.out.println(("> [" + sub) + "] for Substraction ");
    System.out.println(("> [" + mult) + "] for Multiplication ");
    input = sc.nextLine();
    if (input.equals(sub)) {
        return new Subtraction();
    }
    if (input.equals(mult)) {
        return new Multiplication();
    }
    return null;
}
[public]exec(){
    String input = null;
    System.out.println("> Please select an Operation [x] : ");
    System.out.println(("> [" + sub) + "] for Substraction ");
    input = sc.nextLine();
    if (input.equals(sub)) {
        return new Subtraction();
    }
    return null;
}
[public]exec(){
    String input = null;
    System.out.println("> Please select an Operation [x] : ");
    System.out.println(("> [" + add) + "] for Addition ");
    System.out.println(("> [" + sub) + "] for Substraction ");
    System.out.println(("> [" + mult) + "] for Multiplication ");
    input = sc.nextLine();
    if (input.equals(add)) {
        return new Addition();
    }
    if (input.equals(sub)) {
        return new Subtraction();
    }
    if (input.equals(mult)) {
        return new Multiplication();
    }
    return null;
}
}