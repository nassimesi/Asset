package taxonomy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import multivaluedcontext.MultivaluedAttribute;
import util.Node;
import util.SortStringAsDouble;

public class DefaultDoubleTaxonomy extends AbstractTaxonomy {

	/*
	 * ------ CONSTRUCTORS ------
	 */
	
	/**
	 * Creates a new taxonomy for double values corresponding to the specified attribute.
	 * This constructor checks that the type of the attribute corresponds to double and throws an Exception if it is not.
	 * 
	 * @param attribute a {@link MultivaluedAttribute} having double values.
	 * @throws Exception if the type of the specified {@link MultivaluedAttribute} is not "Double Attribute".
	 */
	public DefaultDoubleTaxonomy(MultivaluedAttribute attribute) throws Exception {
		super(attribute);
		
		if (!attribute.getType().equals("Double Attribute")) {
			throw new Exception ("[Wrong taxonomy instance] Trying to compute an ontology of doubles the attribute \"" + attribute.getName() + "\" of type " + attribute.getType());
		}
	}

	/**
	 * Computes the values that are not in the {@link MultivaluedAttribute} 
	 * but that are necessary to complete the taxonomy (sub semi lattice) of values.
	 */
	@Override
	public void computeMissingTaxonomyValues() {
		
		ArrayList<String> distinctValues = new ArrayList<String>();
		distinctValues.addAll(this.attribute.getDistinctValues());
		Collections.sort(distinctValues, new SortStringAsDouble());
		
		/*
		 * Struges rule to determine a range and a number of class intervals
		 */
		Double min = Double.parseDouble(distinctValues.get(0));
		Double max = Double.parseDouble(distinctValues.get(distinctValues.size() - 1));
		int nbClassInterval = (int) Math.ceil(1 + 3.3 * Math.log10(distinctValues.size()));
		double range = (max - min) / nbClassInterval;
		range = BigDecimal.valueOf(range)
			    .setScale(1, RoundingMode.HALF_UP)
			    .doubleValue();
		
		distinctValues.clear();
		double minnrange = min;
		for (int i = 0 ; i < nbClassInterval + 1; i++) {
			distinctValues.add("[" + minnrange + "-" + (minnrange) + "]");
			minnrange += range;
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
	/**
	 * Returns true if n1 subsumes n2 in the sub semi-lattice, else false.
	 */
	public boolean subsume(Node n1, Node n2) {
		
		if (n2.getValue().contains("*")) {
			return true;
		} else if (n1.getValue().contains("*")) {
			return false;
		} else {
		
			double leftN1 = Double.valueOf(n1.toString().substring(2,n1.toString().indexOf("-")));
			double leftN2 = Double.valueOf(n2.toString().substring(2,n2.toString().indexOf("-")));
			
			double rightN1 = Double.valueOf(n1.toString().substring(n1.toString().indexOf("-") + 1, n1.toString().length() - 2));
			double rightN2 = Double.valueOf(n2.toString().substring(n2.toString().indexOf("-") + 1, n2.toString().length() - 2));
					
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
			
			for (int i = getTaxonomyValues().size() -1 ; i >= 0 ; i--) {
				HashSet<String> valueSet = getTaxonomyValues().get(i);
				double valueG = Double.valueOf(valueSet.toString().substring(2, valueSet.toString().indexOf("-")));
				double valueD = Double.valueOf(valueSet.toString().substring(valueSet.toString().indexOf("-")+1, valueSet.toString().length()-2));
			
				if (valueG <= Double.valueOf(valueAsString) && valueD >= Double.valueOf(valueAsString)){

					valueAsString = "[" + valueG + "-" + valueD + "]";
					HashSet<String> valueAsParameter = new HashSet<String>();
					valueAsParameter.add(valueAsString);
					
					return super.getNodeOfValues(valueAsParameter);
				}
			}
		}
			
			return super.getNodeOfValues(values);
	}

}
