import spoon.Launcher;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Filter;
import spoon.support.reflect.declaration.CtConstructorImpl;
import spoon.support.reflect.declaration.CtExecutableImpl;
import spoon.support.reflect.declaration.CtPackageImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Asset {
    /**
     * Variable qui sert à affecter un identifiant unique à  chaque instantiation de classe
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
     * type : le type de l'asset, si c'est un attribut, une mÃ©thode ou une classe
     * @see #Asset(String, String, String, CtElement)
     * @see #possibleTypes */
    private String type;
    /**
     * parent : Le parent de l'asset dans l'arbre AST
     */
    private CtElement parent;

    /**
     * id : L'identifiant de l'asset , qui le diffÃ©rencie des autres
     * @see #autoIncrement
     */
    private int id = 0;
    /**
     * possibleType : Les types possibles acceptÃ©s par les assets qu'on gÃ©nÃ¨re
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
     * attributeReference : la liste des rÃ©fÃ©rences vers tous les attributs de parent de l'asset (tous les assets qui ont "parent" comme parent)
     */
    private static List<CtFieldReference> attributeReferences = new ArrayList<>();
    /**
     * methodAttribute : uniquement modifiable pour une mÃ©thode, cela renvoie tous les attributs de la classe qui participent dand la mÃ©thode
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
     * Le constructuer de l'asset, permet de crÃ©er un asset  partir des paramÃ¨tres suivants
     * @param name la signature
     * @param value la valeur
     * @param type le type
     * @param parent le noeud parent dans l'arbre AST
     */
    public Asset(String name, String value, String type, CtElement parent){
            autoIncrement++; //identifiant unique Ã  chaque classe
            id = autoIncrement;
            nom = name;
            this.value = value;
            this.type = type;
            this.parent = parent;
                   if (nom.contains("String")) this.value = "\"" + value + "\""; // Ecrire les chaÃ®nes de caractÃ¨res entre guillements
       
    }

    /**
     * MÃ©thode pour l'affichage de la classe
     * @return une chaine de caractÃ¨re prÃ©sentant la classe, sera utile lors de l'affichage des liste ainsi que le dÃ©bougage
     */
    @Override
    public String toString() {
        return "id = "+id+"; nom = "+nom+"; parent = "+parent.getValueByRole(CtRole.NAME)+"\n "; // On affiche ce qui est essentiel : l'id et la signature
    }

    /**
     * Cette mÃ©thode est implÃ©mentÃ©e pour gÃ©nÃ©rer des assets Ã  partir d'un arbre AST
     * Sera utile pour les points de variation et la matrice CLEF
     * @param pathFile le chemin du fichier Ã  traiter (Ã  gÃ©nÃ©erer ses assets Ã  partir d'un AST)
     * @return Une liste de tous les assets de la classe (du fichier en paramÃ¨tre)
     * @throws IOException Exception d'entrÃ©e sortie ou de fichier non trouvÃ©
     */

    public static ArrayList<Asset> assetsFromAst(String pathFile) throws IOException {
        //Phase 1 : gÃ©nÃ©rer l'AST
        // Parse Class :  permet de transformer une clasee en un AST
        // lines : retourner toutes les lignes du fichier
        // collect : organisÃ© les lignes rÃ©cupÃ©rÃ©es par un sÃ©parateur de ligne
        CtClass ll = Launcher.parseClass(Files.lines(Paths.get(pathFile), StandardCharsets.UTF_8)
                .collect(Collectors.joining(System.lineSeparator())));

        // Liste temporaire des assets, initialement vide
        ArrayList<Asset> assetList = new ArrayList<>();
        // modifier : une des valuers suivante : public, private, protected, vide (Ã  tester et amÃ©liorer)
        String modifier=((ll.getModifiers().toString()).replace("[", "")).replace("]" ,"");
        //rÃ©cupÃ©rer la signature de la classe pour la retrancher pour avoir la valeur de la classe
        String regex = modifier + " class "+ll.getSimpleName().toString();
        // Valeur de la classe dans la variable "candidate"
        String candidate = ll.toString().substring(regex.length());
        //CrÃ©er l'asset correspondant Ã  la classe (neoud parent)
        assetList.add(new Asset(regex,candidate,"classe",ll.getParent()));
        // Initialiser la liste des attributs de la classe Ã  chaque appel de la mÃ©thode
        attributeReferences = new ArrayList<>();
        //récupérer la liste des attributs de la classe 
        ArrayList<CtField> attributs =  new ArrayList<CtField>();
        attributs.addAll(ll.getFields());
        //Ajouter leurs réferences à 
        for(CtField e:attributs) {
        	assetList.add(new Asset(e.getType().getSimpleName() +" "+ e.getSimpleName() , e.getAssignment().toString() , "Field" ,e.getParent()));
            attributeReferences.add(e.getReference());
        }
        // Récupérer la liste des constructeurs explicites
        ArrayList<CtConstructor> constructeurs =  new ArrayList<CtConstructor>(ll.getConstructors());
        
        for(int i=0; i<constructeurs.size(); i++) {
        	CtConstructor constructeur = constructeurs.get(i);  
        	if (!constructeur.isShadow()) 
        		 assetList.add(new Asset(constructeur.getSignature() , constructeur.getBody().getShortRepresentation() , "Constructor" ,constructeur.getParent())); 
        }
        
        // récupérer tous les méthodes 
        ArrayList<CtMethod> methodes =  new ArrayList<CtMethod>(ll.getAllMethods());
        for(CtMethod e:methodes) {
        	if(!e.isShadow())
        	{ 
        		Asset tmp = new Asset(e.prettyprint().substring(e.getModifiers().toString().length()-e.getModifiers().size()) , e.getBody() == null ?"":e.getBody().toString() , "Method" ,e.getParent());
        		assetList.add(tmp);
        		tmp.getMethodAttribute(e,"Method");
        	}
        }
        
        // retourner la liste des assets du fichier
        return assetList;
    }

    /**
     * Ajouter la référence de l'asset passé en parametre
     * @param testt l'élément à traiter
     * @param type le type de l'Ã©lÃ©ment
     */
    public static void addAtributesReference(CtElement testt, String type){
        //exécuter le corps de la méthode si et selement si le type corresond bien à un attribut
        if (type.equals("Field")){
            //ajouter la rÃ©fÃ©rence de l'attribut Ã  la liste globale des rÃ©fÃ©rences des attributs du fichier (classe)
            attributeReferences.add(((CtField)testt).getReference());
        }
    }

    /**
     * Stocker les attributs de la classe participant Ã  la mÃ©thode en paramÃ¨tre dans la variable
     * @see #methodAttribute
     * @param testt l'asset Ã  tester si c'est une mÃ©thode
     * @param type le type de l'asset
     */
    public  void getMethodAttribute(CtElement testt, String type){
        // Tester si l'Ã©lÃ©ment est une mÃ©thode
        if (type.equals("Method") || type.equals("Constructor")){
            //getElement : rÃ©cupÃ©rer les Ã©lÃ©ments puis filter
            CtExecutableImpl testtt = (CtExecutableImpl)testt;
        	
            methodAttribute =  testt.getElements(new Filter<CtElement>() {
                // RedÃ©finition du filtre
                @Override
                public boolean matches(CtElement ctElement) {
                    // RÃ©cupÃ©rer le type de la classe, utiliser la mÃªme technique que la ligne 181
                    //System.out.println(testt.getValueByRole(CtRole.NAME)+" sa3et "+add.first().getTypeDeclaration().getReference().toString());
                	Matcher m = Pattern.compile(".*\\.Ct(.*)ld.*Impl").matcher(ctElement.getClass().toString());
                    if (m.find()){
                        return m.group(1).equals("Fie"); //puisque le matcher reconnait ld, donc s'il est Ã©gal Ã  Fie, on constitue le mot "Field"
                    }
                    return false; //Si echec d'extraction , retourner faux
                }
            });
            // Faire une intersection entre les attributs rÃ©cupÃ©rÃ©s par le filtre prÃ©cÃ©dent , et les attributs de la classe
            methodAttribute.retainAll(attributeReferences);
        }
    }

    /**
     * RedÃ©finition de la mÃ©thode equals()
     * @param obj L'objet Ã  comparer, aprÃ©s utiliation de cast vers l'asset
     * @return vrai si l'Ã©lÃ©ment est Ã©gal Ã  notre sens
     *
     */

    public boolean equals(Object obj) {
        //cast vers le type Asset concernÃ©
        Asset objet=(Asset) obj;
        // 2 assets de type "classe" sont Ã©gaux si et seulement s'il ont le meme nom
        if (this.type.equals("classe") ){ //si c'est une classe , qui a parent null , comparaison spÃ©ciale
              if (objet.type.equals("classe") && this.getNom().equals(objet.getNom()))
        { //Comparaison type et nom de classe
            updateAssetIfEqual(objet);
        }
        }
        else if (!objet.type.equals("classe")) //sinon si l'objet Ã  comparer n'est pas une classe (qui a parent null)
        {
        // 2 assets sont Ã©gaux si et seulement s'il ont le meme nom, le meme type et la meme valuer et la meme classe parente
        // Phase une : mise Ã  jour de l'identifiant en cas d'Ã©galitÃ©
            if (this.parent.getValueByRole(CtRole.NAME).toString().equals(objet.parent.getValueByRole(CtRole.NAME).toString()))

            if ( (this.nom.equals(((Asset)obj).nom) && this.value.equals(((Asset)obj).value)))
            updateAssetIfEqual(((Asset)obj)); //mise Ã  jour des id en cas d'Ã©galitÃ©
        }
        // retourner si les identifiants sont Ã©gales
        return (this.id == objet.id);
    }

    /**
     * Mise Ã  jour de l'identifiant de l'asset courante par celui du paramÃ¨tre
     * @param a l'asset qui a l'id le plus Ã  jour
     */

    public void updateAssetIfEqual(Asset a){
        this.id = a.id;
    }


}
