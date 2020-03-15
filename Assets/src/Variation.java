public abstract class Variation {
    private String type; // Type de variation, soit classe, soit méthode , soit un attribut

    /**
     * *
     * @return Une chaîne de caractère contenant le type de variation
     * @see Asset#getPossibleTypes()
     */
    public String getType(){
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type){
        this.type = type;
    }

}
