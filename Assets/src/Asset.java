import spoon.Launcher;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.Filter;
import spoon.support.reflect.declaration.CtConstructorImpl;
import spoon.support.reflect.declaration.CtExecutableImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Asset {
    /**
     * Variable qui sert à affecter un identifiant unique à chaque instantiation de classe
     */
    private static int autoIncrement=0;
    /**
     * nom : c'est la signature de l'asset
     */
    private String nom;
    /**
     * Value :  c'est la valeur de l'asset
     */
    private String value;

    /**
     *
     * @return Le nom de l'asset , qui veut dire la signature
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @return le type de l'asset
     */

    public String getType() {
        return type;
    }

    /**
     * type : le type de l'asset, si c'est un attribut, une méthode ou une classe
     * @see #Asset(String, String, String, CtElement)
     * @see #possibleTypes */
    private String type;
    /**
     * parent : Le parent de l'asset dans l'arbre AST
     */
    private CtElement parent;

    /**
     * id : L'identifiant de l'asset , qui le différencie des autres
     * @see #autoIncrement
     */
    private int id = 0;
    /**
     * possibleType : Les types possibles acceptés par les assets qu'on génère
     */
    private static final ArrayList possibleTypes = new ArrayList<>((Arrays.asList("Field", "Method", "class", "Constructor")));


    /**
     * @return les types possibles
     * @see #possibleTypes
     * {
     */

    public static ArrayList getPossibleTypes() {
        return possibleTypes;
    }

    /**
     * attributeReference : la liste des références vers tous les attributs de parent de l'asset (tous les assets qui ont "parent" comme parent)
     */
    private static List<CtElement> attributeReferences = new ArrayList<>();
    /**
     * methodAttribute : uniquement modifiable pour une méthode, cela renvoie tous les attributs de la classe qui participent dand la méthode
     */
    private List<CtElement> methodAttribute = new ArrayList<>();

    /**
     *
     * @return l'identifiant de l'asset
     * @see #id
     */

    public int getId() {
        return id;
    }

    /**
     * Le constructuer de l'asset, permet de créer un asset  partir des paramètres suivants
     * @param name la signature
     * @param value la valeur
     * @param type le type
     * @param parent le noeud parent dans l'arbre AST
     */
    public Asset(String name, String value, String type, CtElement parent){
        if (possibleTypes.contains(type)) {
            autoIncrement++; //identifiant unique à chaque classe
            id = autoIncrement;
            nom = name;
            this.value = value;
            this.parent = parent;
            //conversion type en format personnalisé (pas vraiment nécessaire)
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
                    //Traitement particulier en ce qui suit, pour enlever les parenthèses et la duplication du nom de la classe
                    this.nom = name.substring(1 + parent.getValueByRole(CtRole.NAME).toString().length(), name.length() - 1);
                    break;
                }
                default:
                    this.type = type;
                    break;
            }
            if (nom.contains("String")) this.value = "\"" + value + "\""; // Ecrire les chaînes de caractères entre guillements
        }
    }

    /**
     * Méthode pour l'affichage de la classe
     * @return une chaine de caractère présentant la classe, sera utile lors de l'affichage des liste ainsi que le débougage
     */
    @Override
    public String toString() {
        return "id = "+id+"; nom = "+nom+";\n "; // On affiche ce qui est essentiel : l'id et la signature
    }

    /**
     * Cette méthode est implémentée pour générer des assets à partir d'un arbre AST
     * Sera utile pour les points de variation et la matrice CLEF
     * @param pathFile le chemin du fichier à traiter (à généerer ses assets à partir d'un AST)
     * @return Une liste de tous les assets de la classe (du fichier en paramètre)
     * @throws IOException Exception d'entrée sortie ou de fichier non trouvé
     */

    public static ArrayList<Asset> assetsFromAst(String pathFile) throws IOException {
        //Phase 1 : générer l'AST
        // Parse Class :  permet de transformer une clasee en un AST
        // lines : retourner toutes les lignes du fichier
        // collect : organisé les lignes récupérées par un séparateur de ligne
        CtClass ll = Launcher.parseClass(Files.lines(Paths.get(pathFile), StandardCharsets.UTF_8)
                .collect(Collectors.joining(System.lineSeparator())));

        // Initialiser la liste des attributs de la classe à chaque appel de la méthode
        attributeReferences = new ArrayList<>();
        // Liste temporaire des assets, initialement vide
        ArrayList<Asset> assetList = new ArrayList<>();
        //modifier : une des valuers suivante : public, private, protected, vide (à tester et améliorer)
        String modifier=((ll.getModifiers().toString()).replace("[", "")).replace("]" ,"");
        //récupérer la signature de la classe pour la retrancher pour avoir la valeur de la classe
        String regex = modifier+ " class "+ll.getValueByRole(CtRole.NAME).toString();
        // Valeur de la classe dans la variable "candidate"
        String candidate = ll.toString().substring(regex.length());
        //Créer l'asset correspondant à la classe (neoud parent)
        assetList.add(new Asset("Class "+ll.getValueByRole(CtRole.NAME),candidate,"class",null));
        //Variable contenant des fils de la classe (méthode et attributs)
        List<CtElement> chidrenList = ll.getDirectChildren();
        // parcourer tous les fils de l'arbre en largeur
        while (!chidrenList.isEmpty()){
            //Récupérer le premier élément
            CtElement e = chidrenList.remove(0);
            //Récupérer le type de l'asset à partir du nom de la classe de celui de l'AST, pas de moyen de le faire autrement
            //L'idée est d'utiliser un matcher
            // voir documentation de java.util.regex.Matcher;
            Matcher m = Pattern.compile(".*\\.Ct(.*)Impl").matcher(e.getClass().toString());

            if (m.find())
            {
                if (possibleTypes.contains(m.group(1)))
                {
                    //Cas à discuter avec le prof, si on utilise les constructeurs implicites ou pas
                    if (e.getClass().toString().contains("Constructor")) System.out.println("Is the constructor implicit ? "+((CtConstructorImpl)e).isImplicit());
                    //Générer les assets pour chaque fils du parent contenu dans un des types acceptés par la caf
                    Asset tmp = new Asset(e.getValueByRole(CtRole.TYPE).toString() + ((!m.group(1).equals("Field"))?"("+((CtExecutableImpl)e).getSignature()+")":" "+e.getValueByRole(CtRole.NAME).toString()), (e.getDirectChildren().size()>1 || e.getClass().toString().contains("Constructor"))?e.getDirectChildren().get(e.getDirectChildren().size()-1)+"":"non initialisé", m.group(1),e.getParent());
                    //Ajouter l'asset à la liste des assets du fichier en paramètre de la méthode assetFrom
                    assetList.add(tmp);
                    //Ajouter la référence de l'attrbut récupéré à la variable statique relative aux attributs du fichier en paramètre
                    Asset.addAtributesReference(e,m.group(1));
                    //Ajouter à la méthode référencée par tmp tous les attributs de la classe impliquée dedans
                    tmp.getMethodAttribute(e,m.group(1));
                }
            }
        }
        // retourner la liste des assets du fichier
        return assetList;
    }

    /**
     * Ajouter la référence de l'asset passé en parametre
     * @param testt l'élément à traiter
     * @param type le type de l'élément
     */
    public static void addAtributesReference(CtElement testt, String type){
        //exécuter le corps de la méthode si et selement si le type corresond bien à un attribut
        if (type.equals("Field")){
            //ajouter la référence de l'attribut à la liste globale des références des attributs du fichier (classe)
            attributeReferences.add(((CtField)testt).getReference());
        }
    }

    /**
     * Stocker les attributs de la classe participant à la méthode en paramètre dans la variable
     * @see #methodAttribute
     * @param testt l'asset à tester si c'est une méthode
     * @param type le type de l'asset
     */
    public  void getMethodAttribute(CtElement testt, String type){
        // Tester si l'élément est une méthode
        if (type.equals("Method") || type.equals("Constructor")){
            //getElement : récupérer les éléments puis filter
            methodAttribute =  testt.getElements(new Filter<CtElement>() {
                // Redéfinition du filtre
                @Override
                public boolean matches(CtElement ctElement) {
                    // Récupérer le type de la classe, utiliser la même technique que la ligne 181
                    Matcher m = Pattern.compile(".*\\.Ct(.*)ld.*Impl").matcher(ctElement.getClass().toString());
                    if (m.find()){
                        return m.group(1).equals("Fie"); //puisque le matcher reconnait ld, donc s'il est égal à Fie, on constitue le mot "Field"
                    }
                    return false; //Si echec d'extraction , retourner faux
                }
            });
            // Faire une intersection entre les attributs récupérés par le filtre précédent , et les attributs de la classe
            methodAttribute.retainAll(attributeReferences);
            }
    }

    /**
     * Redéfinition de la méthode equals()
     * @param obj L'objet à comparer, aprés utiliation de cast vers l'asset
     * @return vrai si l'élément est égal à notre sens
     *
     */

    public boolean equals(Object obj) {
        // 2 assets sont égaux si et seulement s'il ont le meme nom, le meme type et la meme valuer
        // Cas particulier : Pour les attributs, il suffit que le nom et le type seulement soit les mêmes
        // Phase une : mise à jour de l'identifiant en cas d'égalité
        if ((this.type.equals("attribut") && this.nom.equals(((Asset)obj).nom))
        || (this.nom.equals(((Asset)obj).nom) && this.value.equals(((Asset)obj).value)))
            updateAssetIfEqual(((Asset)obj));

        // retourner si les identifiants sont égales
        return (this.id == ((Asset)obj).id);
    }

    /**
     * Mise à jour de l'identifiant de l'asset courante par celui du paramètre
     * @param a l'asset qui a l'id le plus à jour
     */

    public void updateAssetIfEqual(Asset a){
        this.id = a.id;
    }


}
