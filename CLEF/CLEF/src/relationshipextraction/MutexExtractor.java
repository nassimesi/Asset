package relationshipextraction;

import java.util.ArrayList;
import java.util.HashSet;

public class MutexExtractor extends AbstractACPosetExtractor {

	/*
	 * ---------- ATTRIBUTES ----------
	 */
	
	ArrayList<HashSet<Integer>> underlyingMutexGraph;
	
	/*
	 * ---------- CONSTRUCTOR ----------
	 */
	
	public MutexExtractor(String p) {
		super(p);
	}

	
	/*
	 * ---------- OVERRIDE METHODS ----------
	 */
	
	@Override
	protected String getRelationshipType() {
		return "mutex";
	}

	@Override
	public void computeRelationships() {
			
		int totalRedundant = 0;
		
		this.extractConcepts();
		this.extractCorrespondences();
		this.extractSuborder();
		this.computeReverseCompleteSuborder();
		
		this.clearVariabilityRelationships();
		
		this.underlyingMutexGraph = new ArrayList<>();
		for (int i = 0 ; i < getConceptAttributeMap().size() ; i++) {
			this.underlyingMutexGraph.add(new HashSet<Integer>());
		}
		
		/*
		 * For each distinct concept pair
		 */
		for (int c1Index = this.getCompleteSuborder().size() - 1 ; c1Index >= 0 ; c1Index--) {
										
			for (int c2Index = c1Index - 1 ; c2Index >= 0 ; c2Index--) {
				
				if (!getCompleteSuborder().get(c1Index).contains(c2Index) &&
						!getCompleteSuborder().get(c2Index).contains(c1Index)) {
					
					ArrayList<Integer> intersection = new ArrayList<>();
					intersection.addAll(getCompleteSuborder().get(c1Index));
					intersection.retainAll(getCompleteSuborder().get(c2Index));
					
					if (intersection.isEmpty()) {
						
						//System.out.println(c1Index + " may be mutex with " + c2Index);
						ArrayList<String> intersection2 = new ArrayList<>();
						ArrayList<String> intersection3 = new ArrayList<>();
						for (String s : getConceptObjectMap().get(c1Index).split(";")) {
							intersection2.add(s);
						}
						for (String s : getConceptObjectMap().get(c2Index).split(";")) {
							intersection3.add(s);
						}
						intersection2.retainAll(intersection3);
						//System.out.println(" -- " + intersection2);						

						if (intersection2.isEmpty()) {
						
						/*
						 * We keep the information in the underlying mutex graph
						 */
						
						this.underlyingMutexGraph.get(c1Index).add(c2Index);
						this.underlyingMutexGraph.get(c2Index).add(c1Index);
						
						String c1Intent = getConceptAttributeMap().get(c1Index);
						String c2Intent = getConceptAttributeMap().get(c2Index);
							
						for (String c1 : c1Intent.split(";")) {

							for (String c2 : c2Intent.split(";")) {
								
								totalRedundant++;
								
								/*
								 * MUTEX BETWEEN TWO ATTRIBUTE VALUES
								 */
								
								if (c2.contains(":=") && c1.contains(":=")) {
									
									/*
									 * We do not extract mutex between two values of the same attribute.
									 */
									if (!c1.substring(0,c1.indexOf(":")).equals(c2.substring(0,c2.indexOf(":")))) {
										
										/*
										 * Test if there is a more general relationships already extracted.
										 */
										boolean isMostGeneral = true;
										for (Integer concept : underlyingMutexGraph.get(c1Index)) {
											
											if ((getCompleteSuborder().get(concept).contains(c2Index) && 
													getConceptAttributeMap().get(concept).contains(c2.substring(0, c2.indexOf(":"))))) {
												
												//System.out.println("Value of " + c2.substring(0, c2.indexOf(":")) + " already met in concept " + concept);
												isMostGeneral = false;

											}
										}
										
										for (Integer concept : underlyingMutexGraph.get(c2Index)) {
											
											if ((getCompleteSuborder().get(concept).contains(c1Index) && 
													getConceptAttributeMap().get(concept).contains(c1.substring(0, c1.indexOf(":"))))) {
												
												//System.out.println("Value of " + c1.substring(0, c1.indexOf(":")) + " already met in concept " + concept);
												isMostGeneral = false;
											}
										}
										
										if (isMostGeneral) {
//											System.out.println(c1 + " ->! " + c2);
											this.addVariabilityRelationship(c1 + " ->! " + c2);
										}

									}
										
								}
								
								/*
								 * MUTEX BETWEEN AN ATTRIBUTE VALUE AND A FEATURE
								 */
								
								else if (c2.contains(":=") || c1.contains(":=")) {
									
									String att;
									String feat;
									int attIndex, featIndex;
									
									if (c2.contains(":=")) {
										att = c2; attIndex = c2Index;
										feat = c1; featIndex = c1Index;
									} else {
										att = c1; attIndex = c1Index;
										feat = c2; featIndex = c2Index;
									}
									
									boolean isMostGeneral = true;
									for (Integer concept : underlyingMutexGraph.get(featIndex)) {
										
										if ((getCompleteSuborder().get(concept).contains(attIndex) && 
												getConceptAttributeMap().get(concept).contains(att.substring(0, att.indexOf(":"))))) {
											
											//System.out.println("Value of " + att.substring(0, att.indexOf(":")) + " already met in concept " + concept);
											isMostGeneral = false;

										}
									}
									
									if (isMostGeneral) {
//										System.out.println(c1 + " ->! " + c2);
										this.addVariabilityRelationship(c1 + " ->! " + c2);
									}
									
								}
								
								/*
								 * MUTEX BETWEEN TWO FEATURES
								 */
								
								else {
//									System.out.println(c1 + " ->! " + c2);
									this.addVariabilityRelationship(c1 + " ->! " + c2);
									
								}
								
								
							}
						}	
					}
				}
				}
			}
		}
		System.out.println("\n########### MUTEX  - [" + getVariabilityRelationships().size() + " / " + totalRedundant + "] ###########\n");

	}

}
