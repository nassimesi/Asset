import spoon.reflect.declaration.CtElement;

public class Asset {
    private static int autoIncrement=0;
    private String nom;
    private String value;
    private String type;
    private CtElement parent;
    private int id = 0;

    public Asset(){
        autoIncrement++;
        id = autoIncrement;
        
    }

}
