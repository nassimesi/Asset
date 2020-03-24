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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static ArrayList<ArrayList<Asset>> assetByProduct = new ArrayList<>();

    public static void  generateCSVforCLEF(ArrayList<Asset> allAssets, int numberProduct) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("out.csv"));
        StringBuilder line = new StringBuilder("");
        System.out.println("before or after atletico");
        for (Asset e:allAssets
             ) {
            line.append(",").append(e.getId());
        }
        line.append("\n");
        System.out.println(line);
        fileWriter.write(line.toString());
        int i=1;
        for (i=1; i<assetByProduct.size()+1; i++)
        {
            line = new StringBuilder("" + i + "/");
            for (Asset a:allAssets
                 ) {
                System.out.println("the asset id of the "+i+" st is"+a.getId()+"and the boolean is "+assetByProduct.get(i - 1).contains(a));
                line.append(assetByProduct.get(i - 1).contains(a) ? ",X" : ",");
            }
           // System.out.println("here sur after atletico");
            //allAssets = new ArrayList<>(new HashSet<>(allAssets));
            //System.out.println("remontada"+allAssets.toString()+"with length"+allAssets.size());
            line.append("\n");
            fileWriter.write(line.toString());}
        fileWriter.close();
    }

    public static ArrayList<Asset> getAllAssets() throws IOException {
        ArrayList<Asset> result = new ArrayList<>();
        FilenameFilter filter1 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                System.out.println("dirouli j'aime" + name);
                //return name.startsWith("Test");
                return new File(dir, name).isDirectory();
            }
        };
        String product[] = new File(".").list(filter1);
        System.out.println("hello world" + product.length);
        int i = 0;
        for (i = 0; i < product.length; i++) {
            assetByProduct.add(new ArrayList<>());
            final int k = i;
            Stream<Path> paths = Files.walk(Paths.get(product[i]));
            paths.filter(new Predicate<Path>() {
                @Override
                public boolean test(Path path) {
                    System.out.println("rani hna" + path.toString());
                    return path.toString().matches(".*\\.java");
                }
            })
                    .forEach(s -> {

                        System.out.println("aya bele3" + s);
                        try {
                            for (Asset e : Asset.assetsFromAst(s.toString())
                            ) {
                                //   System.out.println("element added" + e + "for src/Test"+(i+1)+".java");
                                assetByProduct.get(k).add(e);
                                if (!result.contains(e)) result.add(e);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // System.out.println("ha rakoum 300 a "+assetByProduct.get(i).size());
                    });}
            System.out.println("here is the resultSet" + result.toString());
            return result;

    }


    public static void main(String[] args) throws IOException {
        CtClass l = Launcher.parseClass("class A { public int a=1; public String b="+"hehe"+" ;  void m() {" +
                "a=a*5; ;" +
                " System.out.println(\"yeah\"+a);} }");

        generateCSVforCLEF(getAllAssets(),assetByProduct.size());
        System.out.println(Variation.listVariation.toString());
        Variation.allVariation(assetByProduct);
        Variation.listVariation.removeIf(new Predicate<Variation>() {
            @Override
            public boolean test(Variation variation) {
                return variation.getRelatedAssets().size()<=1;
            }
        });
        System.out.println(Variation.listVariation.toString());
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
