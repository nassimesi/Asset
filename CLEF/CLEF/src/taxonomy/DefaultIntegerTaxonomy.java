package taxonomy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import multivaluedcontext.MultivaluedAttribute;
import util.Node;
import util.SortStringAsInteger;

public class DefaultIntegerTaxonomy extends AbstractTaxonomy {

	/*
	 * ------ CONSTRUCTORS ------
	 */
	 
	/**
	 * Creates a new taxonomy for integer values corresponding to the specified attribute.
	 * This constructor checks that the type of the {@link MultivaluedAttribute} corresponds to integers and throws an Exception if it is not.
	 * 
	 * @param attribute a {@link MultivaluedAttribute} having integer values.
	 * @throws Exception if the type of the specified {@link MultivaluedAttribute} is not "Integer Attribute".
	 */
	public DefaultIntegerTaxonomy(MultivaluedAttribute attribute) throws Exception {
		super(attribute);
		
		if (!attribute.getType().equals("Integer Attribute")) {
			throw new Exception ("[Wrong taxonomy instance] Trying to compute an ontology of integer for the attribute \"" + attribute.getName() + "\" of type " + attribute.getType());
		}
		
	}
	
	/*
	 * ------ INHERITED METHODS ------
	 */

	/**
	 * Computes the values that are not in the {@link MultivaluedAttribute} 
	 * but that are necessary to complete the taxonomy (sub semi lattice) of values.
	 */
	@Override
	public void computeMissingTaxonomyValues() {
		
		ArrayList<String> distinctValues = new ArrayList<String>();
		distinctValues.addAll(this.attribute.getDistinctValues());
		Collections.sort(distinctValues, new SortStringAsInteger());
				
		for (int i = 0 ; i < distinctValues.size() ; i++) {
			String newValueInterval = "[" + distinctValues.get(i) + "-" + distinctValues.get(i)+ "]";
			distinctValues.set(i, newValueInterval);
		}
				
		ArrayList<String> stepN = new ArrayList<String>();
		stepN.addAll(distinctValues);
				
		ArrayList<String> stepN1 = new ArrayList<String>();
		
		while (stepN.size() > 1) {
			
			for (int i = 0 ; i < stepN.size() - 1 ; i++) {
				
				String inter1 = stepN.get(i);
				String inter2 = stepN.get(i+1);
				
				String left = inter1.substring(inter1.indexOf("["), inter1.indexOf("-"));
				String right = inter2.substring(inter2.indexOf("-"), inter2.indexOf("]")+1);
				
				stepN1.add(left + right);				
			}
			
			distinctValues.addAll(stepN1);
			stepN.clear();
			stepN.addAll(stepN1);
			stepN1.clear();
		}
				
		for (String value : distinctValues) {
			HashSet<String> valueSet = new HashSet<String>();
			valueSet.add(value);
			
			taxonomyValues.add(valueSet);
		}
		Collections.reverse(taxonomyValues);
	}

	@Override
	public boolean subsume(Node n1, Node n2) {
	
		if (n2.getValue().contains("*")) {
			return true;
		} else if (n1.getValue().contains("*")) {
			return false;
		} else {
		
			int leftN1 = Integer.valueOf(n1.toString().substring(2,n1.toString().indexOf("-")));
			int leftN2 = Integer.valueOf(n2.toString().substring(2,n2.toString().indexOf("-")));
			
			int rightN1 = Integer.valueOf(n1.toString().substring(n1.toString().indexOf("-") + 1, n1.toString().length() - 2));
			int rightN2 = Integer.valueOf(n2.toString().substring(n2.toString().indexOf("-") + 1, n2.toString().length() - 2));
					
			if ((leftN1 >= leftN2) && (rightN1 <= rightN2)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public Node getNodeOfValues(HashSet<String> values) {
		
		
		String valueAsString = "";
		
		for (String value : values) {
			valueAsString += value;
		}
		
		if (!valueAsString.contains("-") && !valueAsString.equals("*")) {
			
			valueAsString = "[" + valueAsString + "-" + valueAsString + "]";
			
			HashSet<String> valueAsParameter = new HashSet<String>();
			valueAsParameter.add(valueAsString);
			
			return super.getNodeOfValues(valueAsParameter);
			
		} else {
			
			return super.getNodeOfValues(values);
			
		}	
	}
	
}
