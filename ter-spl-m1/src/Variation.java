import java.lang.reflect.Array;
import java.util.ArrayList;

public class Variation {
    private String type; // Type de variation, soit classe, soit méthode , soit un attribut

    /**
     * un setteur de la signature
     * @param name le nom à mettre à jour
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * un getteur de la signature
     * @return la signature de la variation
     */
    public String getName() {
        return name;
    }

    /**
     * La signature de la variante
     */
    private String name;
    /**
     * La liste des variation , une variable statique relative à tous les produits
     */
    public static ArrayList<Variation> listVariation = new ArrayList<>();

    /**
     *
     * @return les assets qui ont la meme signature que la variation
     */
    public ArrayList<Asset> getRelatedAssets() {
        return relatedAssets;
    }

    /**
     * Donner tous les variations possibles dans cette ligne de produit , résultat non retourné mais stoqué (voir See Also)
     * @see #listVariation
     * @param a la liste des assets par produit
     */

    public static void allVariation(ArrayList<ArrayList<Asset>> a){
        for (int i=0; i<a.size(); i++){ //pour chaque produit
            for (int j=0; j<a.get(i).size(); j++){ //pour chaque asset
                    // la variable tmp sera une variation avec le le type et la signature de l'asset courant (indices i et j)
                    Variation tmp = new Variation(a.get(i).get(j).getNom(),a.get(i).get(j).getType());
                    tmp.setType(a.get(i).get(j).getType());
                    tmp.setName(a.get(i).get(j).getNom());
                    //rechercher si tmp existe déja dans la liste des variation, k étant l'indice résultat de cette recherche
                    int k=Variation.listVariation.indexOf(tmp);
                    if (k!=-1) { //Si une variation avec meme non existe
                        //Ajouter l'asset courant à la liste des assets relative à la variation
                        Variation.listVariation.get(k).addAsset(a.get(i).get(j));
                    }
                    else {
                        //Créer une nouvelle variation avec le nom et type
                        Variation.addVariation(a.get(i).get(j).getNom(),a.get(i).get(j).getType());
                        //Ajouter l'asset courant à cette variation créée
                        Variation.listVariation.get(Variation.listVariation.size()-1).addAsset(a.get(i).get(j));
                    }
                }
            }
        }

    /**
     * La liste des assets portant la meme signature, et ayant le même type que celui indiqué dans la variation
     */

    private ArrayList<Asset> relatedAssets;
    /**
     * *
     * @return Une chaîne de caractère contenant le type de variation
     * @see Asset#getPossibleTypes()
     */


    public String getType(){
        return type;
    }

    /**
     * Le constructeur de la calsse variation, outil lors de la création des variations dans la méthode (See Also)
     * @see #allVariation(ArrayList)
     * @param nom la signature
     * @param type le type
     */

    public Variation(String nom, String type){
        name = nom;
        this.type = type;
        //nouvelle liste pour contenir d'éventuels assets
        this.relatedAssets = new ArrayList<>();
    }

    /**
     * Mettre à jour le type
     * @param type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Ajouter un asset à une variation, si elle n'existe pas déjà dans la liste des assets
     * @param e l'asset à ajouter
     */
    public void addAsset(Asset e){
        if (!relatedAssets.contains(e)) relatedAssets.add(e);
    }

    /**
     * Ajouter une variation à la liste globale des variations
     * @param nom la signature
     * @param type le type
     */

    public static void addVariation(String nom, String type){
         listVariation.add(new Variation(nom,type));
    }

    /**
     * Redéfintion de la méthode equals, pour la comparaison
     * @param obj l'objet à comparer (va être casté en Variation)
     * @return vrai si l'objet est égal sémantiquement à cette variation (même signature et même type)
     */
    @Override
    public boolean equals(Object obj) {
        return (this.name.equals(((Variation)obj).name) && this.type.equals((((Variation)obj).type)));
    }

    /**
     * Affichage de variation
     * @return une chaîne de caractères représentant l'affichage de cette variation
     */

    @Override
    public String toString() {
        return "type = "+this.type +" and name = "+name+"and the related assets are :"+relatedAssets+"\n";
    }
}
