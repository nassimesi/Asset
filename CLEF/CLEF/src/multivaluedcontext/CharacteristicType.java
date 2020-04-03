package multivaluedcontext;

public enum CharacteristicType {
	featureType("Binary Attribute"), integerType("Integer Attribute"), doubleType("Double Attribute"), literalType("Literal Attribute");

	private String name;
	
	private CharacteristicType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
