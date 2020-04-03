package relationshipextraction;

import java.util.ArrayList;

public class AllBinaryImplicationExtractor extends AbstractACPosetExtractor {

	/*
	 * ------ CONSTRUCTORS ------
	 */
	
	public AllBinaryImplicationExtractor(String p) {
		super(p);
	}

	
	/*
	 * ------ OVERRIDE METHODS ------
	 */
	
	@Override
	protected String getRelationshipType() {
		return "binary_implications_TC";
	}

	@Override
	public void computeRelationships() {	
		
		this.extractConcepts();
		this.extractCorrespondences();
		this.extractSuborder();
		this.computeCompleteSuborder();
		
		int totalRedundant = 0;
		
		this.clearVariabilityRelationships();
		
		for (int premiseIndex = 0 ; premiseIndex < this.getCompleteSuborder().size() ; premiseIndex++) {
			
			String premiseIntent = getConceptAttributeMap().get(premiseIndex);

			for (String p : premiseIntent.split(";")) {
			
				ArrayList<String> alreadyKeptAttributeAsConclusion = new ArrayList<String>();
			
				for (Integer conclusionIndex : this.getCompleteSuborder().get(premiseIndex)) {
				
					String conclusionIntent = getConceptAttributeMap().get(conclusionIndex);
				
					for (String c : conclusionIntent.split(";")) {
					totalRedundant++;
						
						/*
						 ******* Implication between two attribute values *******
						 *
						 * The main goal is to keep an implication iff 
						 * it has the most general premise
						 * and the most specific conclusion.
						 *
						 * Redundancy elimination policy:
						 * 1) We do not keep implications between two values of the same attribute.
						 * 2) We do not keep implications having the conclusion-attribute in co-occurrence 
						 * 	  with the premise-attribute.
						 *    No need to store any value as we can test the concept introducing the premise-attribute
						 *    at each step.
						 * 3) We do not keep implications having the premise-attribute in co-occurrence 
						 * 	  with the conclusion-attribute. 
						 *    In this case, we store the conclusion-attribute 
						 *    to avoid extracting implications with more general conclusion-attribute.
						 * 4) We do not keep implications having the conclusion-attribute being in an implication
						 *    previously extracted with the same premise-attribute.
						 *    It means that the conclusion-attribute is not the most specific one.
						 *    In this case, we store the conclusion-attribute 
						 *    to avoid extracting implications with more general conclusion-attribute.
						 * 5) We do not keep implications having the premise-attribute introduced
						 *    in super concepts of the current concept, 
						 *    in sub-concepts of the concept introducing the conclusion-attribute.
						 *    It means that the premise-attribute is not the most general one.
						 *    In this case, we store the conclusion-attribute 
						 *    to avoid extracting implications with more specific premise-attribute.
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
										
										/*
										 * If the attribute in conclusion has already been saw
										 * then the current attribute value is more specific.
										 * We keep implications having the most specific conclusions,
										 * so we do not extract the current implication.
										 * Lectic order guarantees that if we have seen an attribute value before,
										 * It was not in a super-concept of the current concept,
										 * Therefore the value was more specific.
										 * 
										 * THIS TEST GUARANTEES THAT WE KEEP THE MOST SPECIFIC CONCLUSION.
										 */
										if(!alreadyKeptAttributeAsConclusion.contains(c.substring(0, c.indexOf(":")))) {
										
											alreadyKeptAttributeAsConclusion.add(c.substring(0, c.indexOf(":")));
											
											/*
											 * For all super-concepts of the concept introducing the premise 
											 * being sub-concepts of the concept introducing the conclusion.
											 * If one of these concepts introduces an attribute value 
											 * of the premise-attribute,
											 * then the premise-attribute value is not the more general one.
											 * We keep implications having the most general premises,
											 * so we do not extract the current implication.
											 * 
											 * THIS TEST GUARANTEES THAT WE KEEP THE MOST GENERAL PREMISE.
											 */
											boolean moreGeneral = true;
											for (Integer premiseSuperConcept : this.getCompleteSuborder().get(premiseIndex)) {
												
												if (this.getCompleteSuborder().get(premiseSuperConcept).contains(conclusionIndex)) {
													
													if (this.getConceptAttributeMap().get(premiseSuperConcept).contains(p.substring(0, p.indexOf(":")))) {
														moreGeneral = false;
													}
												}
											}
											
											if (moreGeneral) {
//												System.out.println(p + " -> " + c);	
												this.addVariabilityRelationship(p + " -> " + c);
											}	
										}
										
									} else {
										alreadyKeptAttributeAsConclusion.add(c.substring(0, c.indexOf(":")));
									}
								}
							}
						}
						
						/*
						 ******* Implication between a feature and an attribute value *******
						 *
						 * The main goal is to keep an implication iff 
						 * it has the most specific conclusion.
						 *
						 * Redundancy elimination policy:
						 * 1) We do not keep implications having the conclusion-attribute in co-occurrence 
						 * 	  with the premise-feature.
						 *    No need to store any value as we can test the concept introducing the premise-attribute
						 *    at each step.
						 * 2) We do not keep implications having the conclusion-attribute being in an implication
						 *    previously extracted with the same premise-feature.
						 *    It means that the conclusion-attribute is not the most specific one.
						 *    In this case, we store the conclusion-attribute 
						 *    to avoid extracting implications with more general conclusion-attribute.
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
								
								/*
								 * If the attribute in conclusion has already been saw
								 * then the current attribute value is more specific.
								 * We keep implications having the most specific conclusions,
								 * so we do not extract the current implication.
								 * Lectic order guarantees that if we have seen an attribute value before,
								 * It was not in a super-concept of the current concept,
								 * Therefore the value was more specific.
								 * 
								 * THIS TEST GUARANTEES THAT WE KEEP THE MOST SPECIFIC CONCLUSION.
								 */
								if(!alreadyKeptAttributeAsConclusion.contains(c.substring(0, c.indexOf(":")))) {
									
									alreadyKeptAttributeAsConclusion.add(c.substring(0, c.indexOf(":")));
									
//									System.out.println(p + " -> " + c);	
									this.addVariabilityRelationship(p + " -> " + c);									
								}
							}
							
						}
						
						/*
						 ******* Implication between an attribute value and a feature *******
						 *
						 * The main goal is to keep an implication iff 
						 * it has the most general premise
						 *
						 * Redundancy elimination policy:
						 * 1) We do not keep implications having the premise-attribute in co-occurrence 
						 * 	  with the conclusion-feature. 
						 * 2) We do not keep implications having the premise-attribute introduced
						 *    in super concepts of the current concept, 
						 *    in sub-concepts of the concept introducing the conclusion-attribute.
						 *    It means that the premise-attribute is not the most general one.
						 *    In this case, we store the conclusion-attribute 
						 *    to avoid extracting implications with more specific premise-attribute.
						 */
						else if(p.contains(":=") && !c.contains(":=")) {
							
							/*
							 * If the attribute in premise is present in the concept introducing the attribute in conclusion,
							 * then it means that the conclusion is in co-occurrence with a value of this attribute.
							 * As a co-occurrence is a double implication, 
							 * and that we keep implications having the most general premises,
							 * we do not extract the current implication.
							 */
							if (!conclusionIntent.contains(p.substring(0, p.indexOf(":")))) {
								
								/*
								 * For all super-concepts of the concept introducing the premise 
								 * being sub-concepts of the concept introducing the conclusion.
								 * If one of these concepts introduces an attribute value 
								 * of the premise-attribute,
								 * then the premise-attribute value is not the more general one.
								 * We keep implications having the most general premises,
								 * so we do not extract the current implication.
								 * 
								 * THIS TEST GUARANTEES THAT WE KEEP THE MOST GENERAL PREMISE.
								 */
								boolean moreGeneral = true;
								for (Integer premiseSuperConcept : this.getCompleteSuborder().get(premiseIndex)) {
									
									if (this.getCompleteSuborder().get(premiseSuperConcept).contains(conclusionIndex)) {
										
										if (this.getConceptAttributeMap().get(premiseSuperConcept).contains(p.substring(0, p.indexOf(":")))) {
											moreGeneral = false;
										}
									}
								}
								
								if (moreGeneral) {
//									System.out.println(p + " -> " + c);	
									this.addVariabilityRelationship(p + " -> " + c);
								}	
								
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
		System.out.println("\n########### BINARY IMPL. (TC)  - [" + getVariabilityRelationships().size() + " / "+ totalRedundant + "] ###########\n");
	}

}
