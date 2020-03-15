import java.util.ArrayList;

public class VariationNonClass extends Variation {
    private String name;
    private ArrayList<String> listValues=new ArrayList<>();

    public VariationNonClass(String nom, ArrayList<String> list){
        this.name = nom;
        listValues.addAll(list);
    }
}
