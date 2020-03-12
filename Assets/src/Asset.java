import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.declaration.CtConstructorImpl;
import spoon.support.reflect.declaration.CtExecutableImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Asset {
    private static int autoIncrement=0;
    private String nom;
    private String value;
    private String type;
    private CtElement parent;
    private int id = 0;

    public Asset(String name, String value, String type, CtElement parent){
        autoIncrement++;
        id = autoIncrement;
        nom = name; this.value = value;  this.parent = parent;
        switch (type){
            case "Field":
                {this.type="attribut";break;}
            case "Method":
                {
                    this.type="méthode";
                    break;
                }
            case "class":
            {this.type="classe";break;}
            case "Constructor":
            {
                this.type="méthode + constructeur";
                this.nom = name.substring(1+parent.getValueByRole(CtRole.NAME).toString().length(),name.length()-1);
                break;
            }
            default:
                this.type = type;
                break;
        }
        if (nom.contains("String")) this.value = "\""+value+"\"";

    }

    @Override
    public String toString() {
        return "name = "+nom+";\n id = "+id+";\n type = "+type+";\n valeur = "+value+"\n";
    }

    public static void assetsFromAst(String pathFile) throws IOException {
        CtClass ll = Launcher.parseClass(Files.lines(Paths.get(pathFile), StandardCharsets.UTF_8)
                .collect(Collectors.joining(System.lineSeparator())));

        ArrayList<Asset> assetList = new ArrayList<>();
        System.out.println(ll.getClass().toString());
        String regex = " public class "+ll.getValueByRole(CtRole.NAME).toString();
        System.out.println(regex);
        String candidate = ll.toString().substring(regex.length());
        System.out.println("candidate ==== "+candidate);
        assetList.add(new Asset("Class "+ll.getValueByRole(CtRole.NAME),candidate,"class",null));
        List<CtElement> chidrenList = ll.getDirectChildren();
        System.out.println(chidrenList.toString()+ "\n \n \n ************************************************************************* \n");

        //System.out.println(attributeList);
        for (CtElement e:chidrenList
        ) {
            //if (!e.getValueByRole(CtRole.NAME).toString().equals("<init>"))
            {   Matcher m = Pattern.compile(".*\\.Ct(.*)Impl").matcher(e.getClass().toString());
                if (m.find())
                    assetList.add(new Asset(e.getValueByRole(CtRole.TYPE).toString() + ((!m.group(1).equals("Field"))?"("+((CtExecutableImpl)e).getSignature()+")":" "+e.getValueByRole(CtRole.NAME).toString()), e.getDirectChildren().size()>1?e.getDirectChildren().get(e.getDirectChildren().size()-1)+"":"non initialisé", m.group(1),e.getParent()));

            }
/*            else {
                String[] dd = e.getParent().toString().split("class "+e.getValueByRole(CtRole.TYPE).toString());
                assetList.add(new Asset("Class "+e.getValueByRole(CtRole.TYPE).toString(),dd[1],"class",null));
            }*/
        }
        System.out.println(assetList.toString());

    }
}
