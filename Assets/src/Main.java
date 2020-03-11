import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.declaration.CtFieldImpl;

public class Main {

    public static void main(String[] args) {
        CtClass l = Launcher.parseClass("class A {public int a=1; void m() { System.out.println(\"yeah\"+a);} }");
        System.out.println(((CtFieldImpl)(l.getDirectChildren().get(1))).getDefaultExpression().toString());

    }
}
