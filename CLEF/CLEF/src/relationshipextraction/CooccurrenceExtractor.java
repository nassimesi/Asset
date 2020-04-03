package relationshipextraction;

import java.util.Map.Entry;

/**
 * This class allows to extract all co-occurring elements from an AC-poset.
 * 
 * @author Jessie Galasso Carbonnel
 *
 */
public class CooccurrenceExtractor extends AbstractACPosetExtractor {
	
	/*
	 * ---------- CONSTRUCTORS ----------
	 */
	
	/**
	 * Creates a new instance of co-occurrence extractor
	 * 
	 * @param path the path of the directory containing the .dot file used to extract co-occurrences
	 */
	public CooccurrenceExtractor(String path) {		
		super(path);
	}
	
	/*
	 * ---------- OVERRIDE METHODS ----------
	 */
	
	/**
	 * This method uses the information extracted from the concepts of the lattice to detect co-occurences between attributes.
	 * It detects concepts introducing more than one attribute, and create a co-occurrence between each pair of attributes.
	 */
	public void computeRelationships(){
			
		// Extracts the information about attribute concepts' intent
		
		this.extractConcepts();
				
		this.clearVariabilityRelationships();
		
		for (Entry<Integer, String>  e : getConceptAttributeMap().entrySet()) {
				
			// Creative way to check if the intent of the current concept possesses more than one element.
			
			if (e.getValue().substring(0, e.getValue().length() - 1).contains(";")) {
																
				for (String att1 : e.getValue().split(";")) {
					
					for (String att2 : e.getValue().split(";")) {
										
						if (!att1.equals(att2)) {
				
							if (!getVariabilityRelationships().contains(att2 + " <-> "+ att1)) {
																	
								addVariabilityRelationship(att1 + " <-> "+ att2);
//								System.out.println(att1 + " <-> "+ att2);
								
							}  
						}
					}
				}	
			}
		}
		
		System.out.println("\n########### CO-OCCURRENCES  - [" + getVariabilityRelationships().size() + "] ###########\n");

	}
	
	@Override
	protected String getRelationshipType() {
		return "cooccurrences";
	}

}
