package relationshipextraction;

import java.util.ArrayList;

/**
 * This class allows to extract the non-redundant binary implications of the transitive reduction.
 * The extraction process is based on the AC-poset.
 * 
 * @author Jessie Galasso Carbonnel
 *
 */
public class BinaryImplicationExtractor extends AbstractACPosetExtractor {

	
	/*
	 * ------ CONSTRUCTORS ------
	 */
	
	public BinaryImplicationExtractor(String p) {
		super(p);
	}
	
	
	/*
	 * ---------- OVERRIDE METHODS ----------
	 */
	
	/**
	 * This method extracts non-redundant binary implications of the transitive reduction.
	 * The extraction process is based  on the concepts and the suborder of the AC-poset.
	 * 
	 * We process the 3 cases "feature -> attribute", "attribute -> feature" and "attribute -> attribute" separately, 
	 * as each case has its own redundancy elimination policy.
	 */
	public void computeRelationships() {

		int totalRedundant = 0;
		
		this.extractConcepts();
		this.extractCorrespondences();
		this.extractSuborder();
		
		this.clearVariabilityRelationships();
		
		
		for (int premiseIndex = 0 ; premiseIndex < getSuborder().size() ; premiseIndex++) {
			
			for (int conclusionIndex : getSuborder().get(premiseIndex)) {
					
				String premiseIntent = getConceptAttributeMap().get(premiseIndex);
				
				String conclusionIntent = getConceptAttributeMap().get(conclusionIndex);
				
				for (String p : premiseIntent.split(";")) {
					
					for (String c : conclusionIntent.split(";")) {
						totalRedundant++;
						
						/*
						 ******* Implication between two attribute values *******
						 *
						 * Redundancy elimination policy:
						 * 1) We do not keep implications between two values of the same attribute.
						 * 2) We do not keep implications having the conclusion-attribute in co-occurrence 
						 * 	  with the premise-attribute.
						 * 3) We do not keep implications having the premise-attribute in co-occurrence 
						 * 	  with the conclusion-attribute.
						 */
						if(p.contains(":=") && c.contains(":=")) {
							
							/*
							 * We do not extract implications between two values of the same attribute.
							 * this information can be found in the taxonomies. 
							 * TODO I think the second "if" takes into account the two cases... 
							 */
							if (!p.substring(0, p.indexOf(":")).equals(c.substring(0, c.indexOf(":")))) {
								
								/*
								 * If the attribute in conclusion is present in the concept introducing the premise,
								 * then it means that the premise is in co-occurrence with a value of this attribute.
								 * As a co-occurrence is a double implication, 
								 * and that we keep implications having the most specific conclusions,
								 * we do not extract the current implication.
								 */
								if (!premiseIntent.contains(c.substring(0, c.indexOf(":")))) {
									
									/*
									 * If the attribute in premise is present in the concept introducing the attribute in conclusion,
									 * then it means that the conclusion is in co-occurrence with a value of this attribute.
									 * As a co-occurrence is a double implication, 
									 * and that we keep implications having the most general premises,
									 * we do not extract the current implication.
									 */
									if (!conclusionIntent.contains(p.substring(0, p.indexOf(":")))) {
										
//										System.out.println(p + " -> " + c);	
										this.addVariabilityRelationship(p + " -> " + c);
									}
								}
																						
							}
						} 
						
						/*
						 ******* Implication between a feature and an attribute value *******
						 *
						 * Redundancy elimination policy:
						 * 1) We do not keep implications having the conclusion-attribute in co-occurrence 
						 * 	  with the premise-feature.
						 */
						else if (!p.contains(":=") && c.contains(":=")) {
							
							/*
							 * If the attribute in conclusion is present in the concept introducing the premise,
							 * then it means that the premise is in co-occurrence with a value of this attribute.
							 * As a co-occurrence is a double implication, 
							 * and that we keep implications having the most specific conclusions,
							 * we do not extract the current implication.
							 */
							if (!premiseIntent.contains(c.substring(0, c.indexOf(":")))) {
								
								//System.out.println(p + " -> " + c);	
								this.addVariabilityRelationship(p + " -> " + c);
							}
						}
						
						/*
						 ******* Implication between an attribute value and a feature *******
						 *
						 * Redundancy elimination policy:
						 * 1) We do not keep implications having the premise-attribute in co-occurrence 
						 * 	  with the conclusion-feature.
						 */
						else if (p.contains(":=") && !c.contains(":=")) {
							
							/*
							 * If the attribute in premise is present in the concept introducing the conclusion,
							 * then it means that the conclusion is in co-occurrence with a value of this attribute.
							 * As a co-occurrence is a double implication, 
							 * and that we keep implications having the most general premises,
							 * we do not extract the current implication.
							 */
							if (!conclusionIntent.contains(p.substring(0, p.indexOf(":")))) {
								
								//System.out.println(p + " -> " + c);	
								this.addVariabilityRelationship(p + " -> " + c);
							}
						}
						
						
						/*
						 ******* Implication between two features *******
						 *
						 * No redundancy elimination policy.
						 */
						else {
//							System.out.println(p + " -> " + c);	
							this.addVariabilityRelationship(p + " -> " + c);
	
						}
					
					}
				}
			
			}
		}
		
		System.out.println("\n########### BINARY IMPL. (TR)  - [" + getVariabilityRelationships().size() + " / " + totalRedundant + "] ###########\n");
		//System.out.println(getVariabilityRelationships());
	}
	
	@Override
	protected String getRelationshipType() {
		return "binary_implications_TR";
	}
	
}
