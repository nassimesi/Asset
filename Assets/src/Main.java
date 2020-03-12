import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtFieldImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtFieldReferenceImpl;

import java.io.IOException;
import java.lang.reflect.Member;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws IOException {
        CtClass l = Launcher.parseClass("class A { public int a=1; public String b="+"hehe"+" ;  void m() {" +
                "a=a*5; ;" +
                " System.out.println(\"yeah\"+a);} }");

        Asset.assetsFromAst("src/Asset.java");

        //        CtBlockImpl bb = (CtBlockImpl) l.getDirectChildren().get(2).getDirectChildren().get(1);
//
//        System.out.println(bb.getValueByRole(CtRole.STATEMENT).toString());
//        CtStatement aa = bb.getStatement(0);
//        CtStatement yy = bb.getStatement(0);
//        aa.partiallyEvaluate();
//        System.out.println(((CtBinaryOperatorImpl)aa.getDirectChildren().get(2)).getRightHandOperand().toString());




        //bb.getStatement(1).partiallyEvaluate();
        //bb.getStatement(2).partiallyEvaluate();
        //CtFieldReadImpl dd = (CtFieldReadImpl) aa.getDirectChildren().get(0);
        //System.out.println(dd.getDirectChildren().get(1));
        //System.out.println(((CtFieldImpl)((CtFieldReferenceImpl)dd.getDirectChildren().get(1)).getFieldDeclaration()).getAssignment().toString());
        //System.out.println(dd.getVariable().getDeclaration().getDefaultExpression());
        //String cc = bb.getLabel();
        //CtMethodImpl test = new CtMethodImpl();
        //System.out.println(cc);
        //System.out.println(((CtFieldImpl)(l.getDirectChildren().get(1))).getDefaultExpression().toString());
        //System.out.println(((CtFieldReadImpl)((CtInvocationImpl)bb).getDirectChildren().get(0)).get.getClass().toString());
        //System.out.println(l.getDirectChildren().get(2).getDirectChildren().get(0).getValueByRole(CtRole.TYPE).toString());
    }
}
