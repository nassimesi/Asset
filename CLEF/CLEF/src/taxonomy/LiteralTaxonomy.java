package taxonomy;

import java.util.Collections;
import java.util.HashSet;

import multivaluedcontext.MultivaluedAttribute;
import util.SortBySetSize;
import util.Node;

/**
 * This class is a concrete taxonomy implementation for value of type "literal set".
 * 
 * @author Jessie Galasso Carbonnel
 *
 */
public class LiteralTaxonomy extends AbstractTaxonomy {

	/*
	 * ------ CONSTRUCTORS ------
	 */
	
	/**
	 * Creates a new taxonomy for literal values corresponding to the specified attribute.
	 * This constructor checks that the type of the {@link MultivaluedAttribute} corresponds to literals and throws an Exception if it is not.
	 * 
	 * @param attribute a {@link MultivaluedAttribute} having literal values.
	 * @throws Exception if the type of the specified {@link MultivaluedAttribute} is not "Literal Attribute".
	 */
	public LiteralTaxonomy(MultivaluedAttribute attribute) throws Exception {
		super(attribute);
		
		if (!attribute.getType().equals("Literal Attribute")) {
			throw new Exception ("[Wrong taxonomy instance] Trying to compute an ontology of literals for the attribute \"" + attribute.getName() + "\" of type " + attribute.getType());
		}
	}

	
	/*
	 * ------ INHERITED METHODS ------
	 */
	
	@Override
	/**
	 * This method computes the missing values necessary to build the ontology from the attribute values.
	 * As a product may have several values for an given attribute, an attribute value is actually a set of values.
	 * An ontology for literal values is thus a sub semi-lattice of value sets.
	 * A sub semi-lattice being closed for the intersection, we compute the intersection of each pair of value sets ;
	 * In this way, we find all the value sets necessary to build the sub semi-lattice, i.e., the ontology values.
	 */
	public void computeMissingTaxonomyValues() {
		
		for (String valueSet : this.attribute.getDistinctValues()) {
			HashSet<String> allValues = new HashSet<String>();
			for (String v : valueSet.split(";")) {
				allValues.add(v);
			}
			this.taxonomyValues.add(allValues);
		}
		
		HashSet<HashSet<String>> missingOntologyValues = new HashSet<HashSet<String>>();
		
		for (int i = 0 ; i < taxonomyValues.size()-1 ; i++) {
			
			for(int j = i+1 ; j < taxonomyValues.size() ; j++) {
				
				HashSet<String> intersection = new HashSet<String>();
				intersection.addAll(taxonomyValues.get(i));
				intersection.retainAll(taxonomyValues.get(j));

				if (intersection.size() != 0 && !taxonomyValues.contains(intersection)) {
					missingOntologyValues.add(intersection);
				}
			}
		}
				
		this.taxonomyValues.addAll(missingOntologyValues);
		
		Collections.sort(this.taxonomyValues, new SortBySetSize());
//		System.out.println("Sorted ontology values: " + this.ontologyValues);
		
	} 
	
	@Override
	/**
	 * This methods states if a node n1 subsumes the node n2 in the hierarchy.
	 * n1 subsumes n2 (n1 is a subnode of n2 in the hierarchy) if all values of n2 are values of n1.
	 * In other words, all values of n2 must be contained in n1.
	 * @return a boolean stating if n1 subsumes n2.
	 */
	public boolean subsume(Node n1, Node n2) {
		if (n2.getValue().contains("*") && n2.getValue().size() == 1) {
			return true;
		} else {
			return n1.getValue().containsAll(n2.getValue());		
		}
	}

}
