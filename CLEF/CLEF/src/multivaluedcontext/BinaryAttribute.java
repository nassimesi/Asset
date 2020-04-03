package multivaluedcontext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * This class represents a binary attribute (boolean characteristic).
 * 
 * It has a name and a list of values.
 * These values may be "X" or "".
 *  
 * @author Jessie Galasso Carbonnel
 *
 */
public class BinaryAttribute extends Characteristic {

	/*
	 * ------ CONSTRUCTORS ------
	 */
	
	/**
	 * Creates a binary attribute with a name and a list of values.
	 * This constructor verifies that the possible values may only be "X" or ""
	 * and throws Exception if there is more than two distinct values
	 * and when there are other values than "X" and "".
	 * 
	 * @param name a String representing the name of the binary attribute.
	 * @param values an ArrayList of String representing the values of the binary attribute in the {@link MultivaluedContext}.
	 * @throws Exception an Exception about too many distinct values or non-boolean values.
	 */
	public BinaryAttribute(String name, ArrayList<String> values) throws Exception {
		super(name, values);
		
		if (this.distinctValues.size() > 3) {
			throw new Exception("[Binary Attribute Bad Format] \"" + name + "\" has too many distinct values: " + distinctValues + " (at most 2 expected)");
		}

		Set<String> dv = new HashSet<String>();
		dv.addAll(this.distinctValues);
		dv.remove("X");
		dv.remove("");
		dv.remove("*");

		if (dv.size() != 0) {
			throw new Exception("[Binary Attribute Bad Format] \"" + name + "\" has non-boolean values: " + dv + " (should only contain \"X\" and/or \"\")");
		}		
	}
	
	/*
	 * ------ INHERITED METHODS ------
	 */

	@Override
	/**
	 * Applies binary scaling on the current binary attribute and adds the resulting binary attributes to the specified MultivaluedContext.
	 * As it is already a binary characteristic, just adds the current binary attribute to the specified MultivaluedContext.
	 * The method returns the specified context extended with the new binary attribute.
	 * @param pm the instance of MultivaluedContext in which the binary attribute has to be added.
	 */
	public MultivaluedContext scaling(MultivaluedContext pm) {
		try {
			if (this.values.contains("*")) {
				ArrayList<String> values2 = new ArrayList<String>();
				for (String v : values) {
					if (v.equals("*")) {
						values2.add("");
					} else {
						values2.add(v);
					}
				}
				pm.addCharacteristic(new BinaryAttribute(this.name, values2));
			} else {
				pm.addCharacteristic(new BinaryAttribute(this.name, this.values));
			}
			return pm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	/**
	 * Returns the type of the binary attribute.
	 * @return a String representing the type of the characteristics (here "Binary Attribute").
	 */
	public String getType() {
		return CharacteristicType.featureType.getName();
	}

}
