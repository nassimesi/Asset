package multivaluedcontext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a characteristic (i.e., a column) of a {@link multivaluedcontext.MultivaluedContext}.
 * 
 * A characteristic has a name and a list of values.
 * 
 * Each value corresponds to an object of the {@link multivaluedcontext.MultivaluedContext}. 
 * More precisely, the index of a value correspond to the object having the same index in the {@link multivaluedcontext.MultivaluedContext}.
 * 
 * We keep a set of distinct values to ease the characteristic management.
 * 
 * A characteristic is either a binary attribute ({@link multivaluedcontext.BinaryAttribute}) or a multivalued attribute ({@link multivaluedcontext.MultivaluedAttribute}) of type integer, double or literal.
 *  
 * @author Jessie Galasso Carbonnel
 *
 */
public abstract class Characteristic {

	/*
	 * ------- ATTRIBUTES ------
	 */
	
	/**
	 * Name of the characteristic.
	 */
	protected String name;
	
	/**
	 * List of values of the characteristic.
	 * The value at index i corresponds to the object at index i 
	 * in the object list of {@link multivaluedcontext.MultivaluedContext}.
	 */
	protected ArrayList<String> values = new ArrayList<String>();
	
	/**
	 * Set of distinct values of the characteristic.
	 */
	protected Set<String> distinctValues = new HashSet<String>();

	
	/*
	 * ------ CONSTRUCTORS ------
	 */
	
	/**
	 * Creates a characteristic with a name and a list of values.
	 * @param name a String representing the name of the characteristic.
	 * @param values an ArrayList of String representing the values of the characteristic in the multivalued context.
	 */
	public Characteristic(String name, ArrayList<String> values) {
		this.name = name;
		this.values.addAll(values);
		this.distinctValues.addAll(values);
		this.distinctValues.remove("*");
	}
	
	
	/*
	 * ------ GETTERS AND SETTERS ------
	 */

	/**
	 * Returns the name of the characteristic
	 * @return a String representing the name of the characteristic
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the list of values of the characteristic.
	 * @return an ArrayList of String representing all the values of the characteristic.
	 */
	public ArrayList<String> getValues() {
		return values;
	}

	/**
	 * Returns the list of values of the characteristic.
	 * @return an ArrayList of String representing the distinct values of the characteristic.
	 */
	public Set<String> getDistinctValues() {
		return distinctValues;
	}
	
	
	/*
	 * ------ ABSTRACT METHODS ------
	 */
	
	/**
	 * Applies binary scaling on the current characteristic, which produces a set of binary attributes({@link multivaluedcontext.BinaryAttribute}).
	 * These binary attributes are added to the specified multivalued context.
	 * The method returns the specified context extended with the new binary attributes.
	 * @param pm the instance of {@link MultivaluedContext} in which the instances of {@link BinaryAttribute} resulting from the binary scaled characteristic have to be added.
	 */
	public abstract MultivaluedContext scaling(MultivaluedContext pm);
	
	/**
	 * Returns the type of the characteristic amongst
	 * "Binary Attribute", "Integer Attribute", "Double Attribute" or "Literal Attribute".
	 * @return a String representing the type of the characteristic.
	 */
	public abstract String getType();
	

	/*
	 * ------ METHODS ------
	 */
	
	public String toString() {
		return "Name: " + this.name + " / Type: " + this.getType() + " / Values: " + this.values;
	}

}
