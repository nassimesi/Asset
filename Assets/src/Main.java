import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.code.CtAssignmentImpl;
import spoon.support.reflect.code.CtFieldReadImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.declaration.CtFieldImpl;

public class Main {

    public static void main(String[] args) {
        CtClass l = Launcher.parseClass("class A { public ArrayList<Integer> a=null; void m() {" +
                "a=new ArrayList<Integer>; a.add(5); a.add(10);" +
                " System.out.println(\"yeah\"+a);} }");
        //System.out.println(((CtFieldImpl)(l.getDirectChildren().get(1))).getDefaultExpression().toString());
        System.out.println(((CtInvocationImpl)l.getDirectChildren().get(2).getDirectChildren().get(1).getDirectChildren().get(2)).getDirectChildren().get(0).toString());
        System.out.println(l.getDirectChildren().get(2).getDirectChildren().get(0).getValueByRole(CtRole.TYPE).toString());
    }
}
