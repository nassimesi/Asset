import com.sun.beans.finder.FieldFinder;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.FieldAccessFilter;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.declaration.CtConstructorImpl;
import spoon.support.reflect.declaration.CtExecutableImpl;
import spoon.support.reflect.reference.CtReferenceImpl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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
    private static final ArrayList possibleTypes = new ArrayList<>((Arrays.asList("Field", "Method", "class", "Constructor")));
    private Variation tmp=new Variation("","");// temporary variable for equal test


    /**
     *
     * @see #possibleTypes
     */

    public static ArrayList getPossibleTypes() {
        return possibleTypes;
    }

    private static List<CtElement> attributeReferences = new ArrayList<>();
    private List<CtElement> methodAttribute = new ArrayList<>();


    public int getId() {
        return id;
    }

    public Asset(String name, String value, String type, CtElement parent){
        if (possibleTypes.contains(type)) {
            autoIncrement++;
            id = autoIncrement;
            nom = name;
            this.value = value;
            this.parent = parent;
            switch (type) {
                case "Field": {
                    this.type = "attribut";
                    break;
                }
                case "Method": {
                    this.type = "méthode";
                    break;
                }
                case "class": {
                    this.type = "classe";
                    break;
                }
                case "Constructor": {
                    this.type = "méthode + constructeur";
                    this.nom = name.substring(1 + parent.getValueByRole(CtRole.NAME).toString().length(), name.length() - 1);
                    break;
                }
                default:
                    this.type = type;
                    break;
            }
            if (nom.contains("String")) this.value = "\"" + value + "\"";
        }
    }

    @Override
    public String toString() {
        return "id = "+id+"; nom = "+nom+";\n ";
    }

    public static ArrayList<Asset> assetsFromAst(String pathFile) throws IOException {
        CtClass ll = Launcher.parseClass(Files.lines(Paths.get(pathFile), StandardCharsets.UTF_8)
                .collect(Collectors.joining(System.lineSeparator())));

        ArrayList<Asset> assetList = new ArrayList<>();
        //System.out.println(ll.getClass().toString());
        String regex = " public class "+ll.getValueByRole(CtRole.NAME).toString();
        //System.out.println(regex);
        String candidate = ll.toString().substring(regex.length());
        //System.out.println("candidate ==== "+candidate);
        assetList.add(new Asset("Class "+ll.getValueByRole(CtRole.NAME),candidate,"class",null));
        List<CtElement> chidrenList = ll.getDirectChildren();
        //System.out.println(chidrenList.toString()+ "\n \n \n ************************************************************************* \n");

        //System.out.println(attributeList);
        while (!chidrenList.isEmpty()){
            CtElement e = chidrenList.remove(0);
            //if (!e.getValueByRole(CtRole.NAME).toString().equals("<init>"))
            {   Matcher m = Pattern.compile(".*\\.Ct(.*)Impl").matcher(e.getClass().toString());
                if (m.find())
                {
                    if (possibleTypes.contains(m.group(1))){
                        Asset tmp = new Asset(e.getValueByRole(CtRole.TYPE).toString() + ((!m.group(1).equals("Field"))?"("+((CtExecutableImpl)e).getSignature()+")":" "+e.getValueByRole(CtRole.NAME).toString()), e.getDirectChildren().size()>1?e.getDirectChildren().get(e.getDirectChildren().size()-1)+"":"non initialisé", m.group(1),e.getParent());
                        assetList.add(tmp);
                        Asset.addAtributesReference(e,m.group(1));
                        tmp.getMethodAttribute(e,m.group(1));
                    }

                }
            }
/*            else {
                String[] dd = e.getParent().toString().split("class "+e.getValueByRole(CtRole.TYPE).toString());
                assetList.add(new Asset("Class "+e.getValueByRole(CtRole.TYPE).toString(),dd[1],"class",null));
            }*/
        }
        //System.out.println(assetList.toString());

        return assetList;
    }
    public static void addAtributesReference(CtElement testt, String type){
        if (type.equals("Field")){attributeReferences.add(((CtField)testt).getReference());
        //    System.out.println("list = "+attributeReferences);
        }
    }
    public  void getMethodAttribute(CtElement testt, String type){
        if (type.equals("Method") || type.equals("Constructor")){
            methodAttribute =  testt.getElements(new Filter<CtElement>() {
                @Override
                public boolean matches(CtElement ctElement) {
                   // System.out.println("before"+ctElement.getClass().toString());
                    Matcher m = Pattern.compile(".*\\.Ct(.*)ld.*Impl").matcher(ctElement.getClass().toString());
                    if (m.find()){
                        return m.group(1).equals("Fie");
                    }
                    return false;
                }
            });
            //System.out.println(attributeReferences.toString()+"aaaaaand "+methodAttribute.toString());
            methodAttribute.retainAll(attributeReferences);

            //System.out.println("here is the list "+methodAttribute.toString());
            }

    }


    public boolean equals(Object obj) {
        if ((this.type.equals("attribut") && this.nom.equals(((Asset)obj).nom))
        || (this.nom.equals(((Asset)obj).nom) && this.value.equals(((Asset)obj).value)))
        updateAssetIfEqual(((Asset)obj));


        else if(this.nom.equals(((Asset)obj).nom)){
            tmp.setType(((Asset)obj).type);
            tmp.setName(((Asset)obj).nom);
            if (Variation.listVariation.contains(tmp)) {
                int index = Variation.listVariation.indexOf(tmp);
                Variation.listVariation.get(index).addAsset(this);
            }
            else {
                tmp = new Variation(((Asset)obj).nom,((Asset)obj).type);
                Variation.addVariation(tmp);

            }
        }
            //System.out.println("atletico madrid");
        return (this.id == ((Asset)obj).id);
    }

    public void updateAssetIfEqual(Asset a){
        this.id = a.id;
    }


}
