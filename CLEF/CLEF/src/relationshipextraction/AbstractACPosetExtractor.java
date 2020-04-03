package relationshipextraction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

//import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

/**
 * This class groups attributes and methods which are commons to all variability relationship extractors based on AC-poset.
 * 
 * It allows to extract the set of attribute-concepts and the partial order between them.
 * 
 * @author Jessie Galasso Carbonnel
 *
 */
public abstract class AbstractACPosetExtractor {

	/*
	 * ---------- ATTRIBUTES ----------
	 */
	
	/**
	 * Map of concepts' attribute and their number from the AC-poset.
	 */
	private TreeMap<Integer, String> conceptAttributeMap;
	
	/**
	 * Map of concepts' objects and their number from the AC-poset.
	 */
	private TreeMap<Integer, String> conceptObjectMap;
	
	/**
	 * List of suborder relations from the AC-poset.
	 * Each index represents the number of a concept, and its associated ArrayList represents the list of all its direct super-concepts.
	 */
	private ArrayList<ArrayList<Integer>> suborder;
	
	/**
	 * Correspondences between the number of the concept and the number used in the dot file to represent the concept.
	 */
	private HashMap<String, String> correspondences;
	
	/**
	 * Path to the directory containing the .dot file representing the AC-poset.
	 */
	private String path;
	
	/**
	 * List of extracted variability relationships
	 */
	private ArrayList<String> variabilityRelationships;

	/**
	 * Complete suborder.
	 * Each index represents the number of a concept, and its associated ArrayList represents the list of all its super-concepts.
	 */
	private ArrayList<ArrayList<Integer>> completeSuborder;
	
	/*
	 * ---------- CONSTRUCTORS ----------
	 */
	
	public AbstractACPosetExtractor(String path) {
		
		this.path = path;
		variabilityRelationships = new ArrayList<>();
		
	}
	
	/*
	 * ---------- GETTERS AND SETTERS ----------
	 */
	
	public TreeMap<Integer, String> getConceptAttributeMap() {
		return this.conceptAttributeMap;
	}
	
	public TreeMap<Integer, String> getConceptObjectMap() {
		return this.conceptObjectMap;
	}
	
	public ArrayList<ArrayList<Integer>> getSuborder() {
		return this.suborder;
	}
	
	public ArrayList<ArrayList<Integer>> getCompleteSuborder() {
		return this.completeSuborder;
	}
	
	public void clearVariabilityRelationships() {
		this.variabilityRelationships.clear();
	}
	
	public ArrayList<String> getVariabilityRelationships() {
		return this.variabilityRelationships;
	}
	
	public void addVariabilityRelationship(String r) {
		this.variabilityRelationships.add(r);
	}
	
	/*
	 * ---------- METHODS ----------
	 */
	
	/**
	 * This methods extracts the concepts of the AC-poset
	 * from the .dot file in the directory indicated by the path.
	 * It extracts concept attribute intents and their numbers from the dot file,
	 * and stores them in a TreeMap where the key is the concept number, and the value is the concept intent.
	 * Ex: 5 = "elt1;elt2;elt3"
	 */
	public void extractConcepts() {
		
		try {
		
			this.conceptAttributeMap = new TreeMap<Integer, String>();
			this.conceptObjectMap = new TreeMap<>();
	
			//OLD JESSIE CODE
//			Files.lines(Paths.get(path + "FCA/AC-poset/step0-0.dot"))
//            .map(line -> line.split("\\r\\n|\\n|\\r")) 		
//            .flatMap(Arrays::stream) 					
//            .distinct()
//            .filter(c -> !c.contains("->") && c.matches("^[0-9].*$"))
//            .map(c -> c.replace("</td></tr><tr><td>", "|"))
//            .map(c-> c.replace("<br/>", "\\n"))
//            .map(c -> c.replace("shape=none,label=<<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" port=\"p\"><tr><td>Concept_ctx_", ""))
//            .map(c -> c.replace("</td></tr></table>>", ""))
//            .map(c -> c.replace("|\\n", "|"))
//            .map(c -> c.replace("\\n", ";"))
//            .map(c -> c.replaceAll("[0-9]+.\\[", "")) 
//          //  .map(c -> c.replaceAll(";\\|.*\\]", ""))
//         //   .map(c -> c.replaceAll("\\|\\|.*", " "))
//            //.map(c -> c.replaceAll("\\|", " "))
//            .map(c -> c.replaceAll(" ];$", ""))
//            .map(c -> c.replaceAll("];$", ""))
//          //  .forEach( System.out::println);
//            .forEach(c -> {conceptAttributeMap.put(
//           		Integer.valueOf(c.substring(0, c.indexOf("|"))), 
//            		c.substring(c.indexOf("|") + 1, c.indexOf(";|") + 1));
//            	conceptObjectMap.put(Integer.valueOf(c.substring(0, c.indexOf("|"))),
//            			c.substring(c.indexOf(";|") + 2, c.length()));	
//            });
			
			Files.lines(Paths.get(path + "FCA/AC-poset/step0-0.dot"))
            .map(line -> line.split("\\r\\n|\\n|\\r")) 		
            .flatMap(Arrays::stream) 					
            .distinct()
            .filter(c -> !c.contains("->") && c.matches("^[0-9].*$"))
            .map(c -> c.replace("shape=none,label=<<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" port=\"p\"><tr><td>Concept_ctx_", ""))
            .map(c -> c.replace("</td></tr><tr><td>", "|"))
            .map(c-> c.replace("<br/>", ";"))
            .map(c -> c.replace("</td></tr></table>>", ""))
            .map(c -> c.replaceAll(" ];$", ""))
            .map(c -> c.replaceAll("];$", ""))
            .map(c -> c.replaceAll("[0-9]+.\\[", "")) 
            .map(c -> c.replaceAll(" ", "")) 
//            .forEach( System.out::println);
            .forEach(c -> {conceptAttributeMap.put(
           		Integer.valueOf(c.substring(0, c.indexOf("|"))), 
            		c.substring(c.indexOf("|") + 1, c.indexOf(";|") + 1));
            	conceptObjectMap.put(Integer.valueOf(c.substring(0, c.indexOf("|"))),
            			c.substring(c.indexOf(";|") + 2, c.length()));	
            })
            ;
			
			
			
			System.out.println("## Number of concepts in AC-poset: "+ conceptAttributeMap.size());
			//System.out.println(conceptAttributeMap);
			//System.out.println(conceptObjectMap);
								
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method extracts the correspondences between the number of the concept (lectic order)
	 * and the number used in the .dot file to represent the concept.
	 */
	public void extractCorrespondences() {
		
		try {
					
			this.correspondences = new HashMap<>();			
			
			Files.lines(Paths.get(path + "FCA/AC-poset/step0-0.dot"))
            .map(line -> line.split("\\r\\n|\\n|\\r")) 		
            .flatMap(Arrays::stream) 					
            .distinct()
            .filter(c -> !c.contains("->") && c.matches("^[0-9].*$"))
            .map(c -> c.replace("</td></tr><tr><td>", "|"))
            .map(c-> c.replace("<br/>", "\\n"))
            .map(c -> c.replace("shape=none,label=<<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" port=\"p\"><tr><td>Concept_ctx_", ""))
            .map(c -> c.replace("</td></tr></table>>", ""))
            .map(c -> c.replace("|\\n", "|"))
            .map(c -> c.replace("\\n", ";"))
            .map(c -> c.replaceAll("\\[", "")) 
            .map(c -> c.replaceAll(";\\|.*\\]", ""))
            .map(c -> c.replaceAll("\\|.*", ""))
            .forEach(c -> correspondences.put(c.substring(0,c.indexOf(" ")), c.substring(c.indexOf(" ") + 1, c.length())));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method extracts the suborder between the concepts as indicated in the .dot file.
	 * Each element of the suborder represents an arrow in the AC-poset.
	 * Each concept is represented by an arbitrary number in the .dot file.
	 * We use the Map "correspondences" to represents the suborder with the real concepts' number. 
	 * 
	 * An ArrayList in suborder represents the direct super-concepts of the concept corresponding to the ArrayList index.
 	 * Ex: suborder.get(4) -> [6,7] means that concepts 6 and 7 are direct super-concepts of concept 4.
	 */
	public void extractSuborder() {
		
		try {
			
			this.suborder = new ArrayList<ArrayList<Integer>>();
			
			/*
			 * Easier to retrieve a String than a list of String.
			 */
			ArrayList<String> suborder2 = new ArrayList<>();
			
			Files.lines(Paths.get(path + "FCA/AC-poset/step0-0.dot"))
            .map(line -> line.split("\\r\\n|\\n|\\r")) 		// Stream<String[]>
            .flatMap(Arrays::stream) 					// Stream<String>
            .distinct()
            .filter(c -> c.contains("->"))
            .map(c -> c.replace(":p", ""))
            .map(c -> c.replace("\t",""))
            .map(c -> c.replace(" ", ""))
            .forEach(c -> suborder2.add(correspondences.get(c.substring(0,c.indexOf("-"))) +
            		" " +
            		correspondences.get(c.substring(c.indexOf(">") + 1, c.length()))));
			
			/*
			 * Sorting the suborder by sub-concept number.
			 */
			Collections.sort(suborder2, new Comparator<String>() {
				public int compare(String o1, String o2) {
					return Integer.parseInt(o1.substring(0, o1.indexOf(" "))) - 
							Integer.parseInt(o2.substring(0, o2.indexOf(" ")));
				}
			});

			/*
			 * Creating the list of super-concepts.
			 */
			int index = 0;
			for (int i = 0 ; i < this.conceptAttributeMap.size() ; i++) {
				this.suborder.add(new ArrayList<>());
				
				while (index < suborder2.size() && 
						suborder2.get(index).substring(0, suborder2.get(index).indexOf(" ")).equals(i+"")) {
					
					this.suborder.get(i).add(Integer.valueOf(
							suborder2.get(index).substring(suborder2.get(index).indexOf(" ") + 1, suborder2.get(index).length())));
					index++;
				}
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method computes the reverse complete suborder in the AC-poset.
	 * From bottom to top, each concept retrieves all sub-concepts of its own direct sub-concepts.
	 */
	public void computeReverseCompleteSuborder() {
		
		this.completeSuborder = new ArrayList<>();
	
		for (int conceptNumberInCS = 0 ; conceptNumberInCS < conceptAttributeMap.size() ; conceptNumberInCS++) {
			
			this.completeSuborder.add(new ArrayList<Integer>());
			
			for (int conceptNumberInS = 0 ; conceptNumberInS < conceptAttributeMap.size() ; conceptNumberInS++) {
				if (this.suborder.get(conceptNumberInS).contains(conceptNumberInCS)) {
					this.completeSuborder.get(conceptNumberInCS).add(conceptNumberInS);
				}
			}
		}
		
		for (int i = 0 ; i < conceptAttributeMap.size()  ; i++) {
			
			HashSet<Integer> toBeAdded = new HashSet<>();
			
			for (Integer subConcept : completeSuborder.get(i)) {
				toBeAdded.addAll(completeSuborder.get(subConcept));
			}
			completeSuborder.get(i).addAll(toBeAdded);
			toBeAdded.clear();
					}		
	}
	

	/**
	 * This method computes the complete suborder in the AC-poset.
	 * From top to bottom, each concept retrieves all super-concepts of its own direct super-concepts.
	 */
	public void computeCompleteSuborder() {
		
		this.completeSuborder = new ArrayList<>();
		this.completeSuborder.addAll(suborder);
		
		for (int i = conceptAttributeMap.size() - 1 ; i >= 0 ; i--) {
			
			HashSet<Integer> toBeAdded = new HashSet<>();
			
			for (Integer superConcept : completeSuborder.get(i)) {
				toBeAdded.addAll(completeSuborder.get(superConcept));
			}
			completeSuborder.get(i).addAll(toBeAdded);
			toBeAdded.clear();
			
			Collections.sort(completeSuborder.get(i));
		}		
	}
	
	/**
	 * This method exports all the extracted variability relationships in a text file.
	 * Depends on the relationship type defined in the class dynamic type.
	 */
	public void exportsInTextFile() {
		
		String filename = path + "variability/" + getRelationshipType() + ".txt";
		
		try {
		
			BufferedWriter f_rel = new BufferedWriter(new FileWriter (filename));
			
			for (String rel : variabilityRelationships) {
				f_rel.write(rel); 
				f_rel.newLine();
			}
			f_rel.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ---------- ABSTRACT METHODS ----------
	 */
	
	protected abstract String getRelationshipType();
	
	public abstract void computeRelationships();
	
}
