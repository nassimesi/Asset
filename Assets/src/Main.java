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

import java.io.*;
import java.lang.reflect.Member;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static ArrayList<ArrayList<Asset>> assetByProduct = new ArrayList<>();

    public static void  generateCSVforCLEF(ArrayList<Asset> allAssets, int numberProduct) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("out.csv"));
        StringBuilder line = new StringBuilder(",");
        System.out.println("before or after atletico");
        for (Asset e:allAssets
             ) {
            line.append("").append(e.getId()).append(",");
        }
        line.append("\n");
        System.out.println(line);
        fileWriter.write(line.toString());
        int i=1;
        for (i=1; i<assetByProduct.size()+1; i++)
        {
            line = new StringBuilder("" + i + "/,");
            for (Asset a:allAssets
                 ) {
                line.append(assetByProduct.get(i - 1).contains(a) ? "X," : ",");
            }
            System.out.println("here sur after atletico");
            allAssets = new ArrayList<>(new HashSet<>(allAssets));
            System.out.println("remontada"+allAssets.toString()+"with length"+allAssets.size());
            line.append("\n");
            fileWriter.write(line.toString());}
        fileWriter.close();
    }

    public static ArrayList<Asset> getAllAssets() throws IOException {
        ArrayList<Asset> result = new ArrayList<>();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                //System.out.println("dirouli j'aime"+name);
                return name.startsWith("Test");
            }
        };
        File product[] = new File("C:\\Users\\L\\Desktop\\Assets\\src").listFiles(filter);
        System.out.println("hello world"+product.length);
        HashSet resultSet = new HashSet<Asset>(result);
        for (int i=0;i<product.length; i++){
            assetByProduct.add(new ArrayList<>());
            assetByProduct.get(i).addAll(Asset.assetsFromAst("src/Test"+(i+1)+".java"));
            System.out.println("ha rakoum 300 a ");
            resultSet.addAll(new HashSet<>(assetByProduct.get(i)));
        }
        resultSet = new HashSet(resultSet);
        result = new ArrayList<>(resultSet);
        System.out.println("here is the resultSet"+resultSet.toString());
        return result;
    }




    public static void main(String[] args) throws IOException {
        CtClass l = Launcher.parseClass("class A { public int a=1; public String b="+"hehe"+" ;  void m() {" +
                "a=a*5; ;" +
                " System.out.println(\"yeah\"+a);} }");

        generateCSVforCLEF(getAllAssets(),assetByProduct.size());
        //System.out.println(Asset.assetsFromAst("src/Test.java").toString());

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
