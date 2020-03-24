import java.lang.reflect.Array;
import java.util.ArrayList;

public class Variation {
    private String type; // Type de variation, soit classe, soit méthode , soit un attribut

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name; // Le nom de la variante
    public static ArrayList<Variation> listVariation = new ArrayList<>();

    public ArrayList<Asset> getRelatedAssets() {
        return relatedAssets;
    }

    public static void allVariation(ArrayList<ArrayList<Asset>> a){
        for (int i=0; i<a.size(); i++){
            for (int j=0; j<a.get(i).size(); j++){
                    Variation tmp = new Variation(a.get(i).get(j).getNom(),a.get(i).get(j).getType());
                    tmp.setType(a.get(i).get(j).getType());
                    tmp.setName(a.get(i).get(j).getNom());
                    System.out.println("dkhol"+a.get(i).get(j).getNom());
                    int k=Variation.listVariation.indexOf(tmp);
                    System.out.println("tredouli madamti "+k);
                    if (k!=-1) {
                        System.out.println("hna");
                        Variation.listVariation.get(k).addAsset(a.get(i).get(j));
                    }
                    else {
                        System.out.println("khir me lhih");
                        Variation.addVariation(a.get(i).get(j).getNom(),a.get(i).get(j).getType());
                        Variation.listVariation.get(Variation.listVariation.size()-1).addAsset(a.get(i).get(j));
                    }
                }
            }
        }


    private ArrayList<Asset> relatedAssets;
    /**
     * *
     * @return Une chaîne de caractère contenant le type de variation
     * @see Asset#getPossibleTypes()
     */


    public String getType(){
        return type;
    }

    public Variation(String nom, String type){
        name = nom;
        this.type = type;
        this.relatedAssets = new ArrayList<>();
    }

    /**
     *
     * @param type
     */
    public void setType(String type){
        this.type = type;
    }

    public void addAsset(Asset e){
        if (!relatedAssets.contains(e)) relatedAssets.add(e);
    }

    public static void addVariation(String nom, String type){
         listVariation.add(new Variation(nom,type));
    }

    @Override
    public boolean equals(Object obj) {
        return (this.name.equals(((Variation)obj).name) && this.type.equals((((Variation)obj).type)));
    }

    @Override
    public String toString() {
        return "type = "+this.type +" and name = "+name+"and the related assets are :"+relatedAssets+"\n";
    }
}
