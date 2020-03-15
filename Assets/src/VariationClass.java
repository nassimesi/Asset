import java.util.ArrayList;
import java.util.List;

public class VariationClass extends Variation{
    private ArrayList<Asset> listVarianteMethod=new ArrayList<>();

    /**
     * La méthode est un constructeur qui permet de stoquer
     * la liste des méthodes
     * @param list la liste en paramètre générée dans La classe Main
     *
     */
    public VariationClass(ArrayList<Asset> list){
        setType("class");
        listVarianteMethod.addAll(list);
    }


}
