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
        for (Asset e:allAssets
             ) {
            line.append(",").append(e.getId());
        }
        line.append("\n");
        fileWriter.write(line.toString());
        int i=1;
        for (i=1; i<assetByProduct.size()+1; i++)
        {
            line = new StringBuilder("" + i + "/");
            for (Asset a:allAssets
                 ) {
                line.append(assetByProduct.get(i - 1).contains(a) ? ",X" : ",");
            }
            line.append("\n");
            fileWriter.write(line.toString());}
        fileWriter.close();
    }

    public static ArrayList<Asset> getAllAssets() throws IOException {
        ArrayList<Asset> result = new ArrayList<>();
        FilenameFilter filter1 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                //return name.startsWith("Test");
                return new File(dir, name).isDirectory();
            }
        };
        String product[] = new File(".").list(filter1);
        int i = 0;
        for (i = 0; i < product.length; i++) {
            assetByProduct.add(new ArrayList<>());
            final int k = i;
            Stream<Path> paths = Files.walk(Paths.get(product[i]));
            paths.filter(new Predicate<Path>() {
                @Override
                public boolean test(Path path) {
                    return path.toString().matches(".*\\.java");
                }
            })
                    .forEach(s -> {

                        try {
                            for (Asset e : Asset.assetsFromAst(s.toString())
                            ) {
                                assetByProduct.get(k).add(e);
                                if (!result.contains(e)) result.add(e);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });}
            return result;

    }


    public static void main(String[] args) throws IOException {
        CtClass l = Launcher.parseClass("class A { public int a=1; public String b="+"hehe"+" ;  void m() {" +
                "a=a*5; ;" +
                " System.out.println(\"yeah\"+a);} }");

        generateCSVforCLEF(getAllAssets(),assetByProduct.size());
        Variation.allVariation(assetByProduct);
        Variation.listVariation.removeIf(new Predicate<Variation>() {
            @Override
            public boolean test(Variation variation) {
                return variation.getRelatedAssets().size()<=1;
            }
        });
        System.out.println(Variation.listVariation.toString());
    }
}
