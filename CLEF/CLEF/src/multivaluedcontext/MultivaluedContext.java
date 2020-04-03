package multivaluedcontext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import main.testExtraction;

/**
 * This class represents a multivalued context.
 * 
 * A multivalued context has a list of objects and a list of characteristics (i.e., mulitvalued attributes).
 * A characteristic is either a binary attribute or a multivalued attribute.
 * Each object is described by a list of values: 
 * the value of index i corresponds to the characteristics of the same index in the list of characteristics.
 *  
 * @author Jessie Galasso Carbonnel
 *
 */
public class MultivaluedContext {
	
	/*
	 * ------ ATTRIBUTES ------
	 */
	public static final String RAC_EXPLORER_PATH = "../CLEF/CLEF/rcaexplore-20151012.jar";
	/**
	 * The name of the multivalued context.
	 */
	private String name;
	
	/**
	 * The list of objects' names.
	 */
	private ArrayList<String> objectNames;
	
	/**
	 * The list of characteristics' names.
	 */
	private ArrayList<Characteristic> characteristics;
	
	/**
	 * The list of objects, 
	 * an object being represented by the list of its values for each characteristics.
	 */
	private ArrayList<ArrayList<String>> objects;
	
	/**
	 * A binary context, containing only binary attributes.
	 */
	private MultivaluedContext binaryContext;

	
	/*
	 * ------ CONSTRUCTORS ------
	 */
	
	/**
	 * Creates an empty context.
	 */
	public MultivaluedContext() {
		
		this.name = name;
		this.objectNames = new ArrayList<String>();
		this.objects = new ArrayList<ArrayList<String>>();
		this.characteristics = new ArrayList<Characteristic>();
		
	}
	
	/**
	 * Creates a new context w.r.t the csv file specified in parameter.
	 * The specified csv file must be in the directory "data/0_clean_PCMs/".
	 * @param fileName the name of the csv file in the directory data/0_clean_PCMs.
	 */
	public MultivaluedContext(String fileName) {
		
		this.name = fileName.substring(0, fileName.length()-4);
		this.objectNames = new ArrayList<String>();
		this.objects = new ArrayList<ArrayList<String>>();
		this.characteristics = new ArrayList<Characteristic>();
		
		System.out.println("owwwwww " + Paths.get(".").toAbsolutePath());
		this.initFromCSVfile(fileName);
		
		binaryContext = new MultivaluedContext();
		binaryContext.setName("scaled_" + name);
		binaryContext.setObjectsNameList(this.objectNames);
		
		try {
			if(Files.notExists(Paths.get(testExtraction.DATA_FOLDER + name))){
				Files.createDirectory(Paths.get(testExtraction.DATA_FOLDER + name));
			}	
			
			if(Files.notExists(Paths.get(testExtraction.DATA_FOLDER + name + "/variability/"))){
				Files.createDirectory(Paths.get(testExtraction.DATA_FOLDER + name + "/variability/"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

	/*
	 * ------ GETTERS AND SETTERS  ------
	 */
	
	/**
	 * Initialises the name of the context.
	 * @param n a String representing the new name of the context.
	 */
	public void setName(String n) {
		this.name = n;
	}
	
	/**
	 * Returns the name of the context.
	 * @return a String representing the name of the context.
	 */
	public String getName() { 
		return this.name;
	}
	
	/**
	 * Initialises the objects' name list.
	 * Initialises the list of objects' values.
	 * @param list an ArrayList of String containing the objects' name list.
	 */
	public void setObjectsNameList(ArrayList<String> list) {
		this.objectNames.addAll(list);
		
		for (int i = 0 ; i < objectNames.size() ; i++) {
			objects.add(new ArrayList<String>());
		}
	}
	
	/**
	 * Returns an integer representing the number of objects in the context.
	 */
	public int getNumberOfObjects() {
		return objectNames.size();
	}
	
	/**
	 * Returns an integer representing the number of characteristics in the context.
	 */
	public int getNumberOfCharacteristics() {
		return characteristics.size();
	}
	
	/**
	 * Returns the names of the objects in the context.
	 * @return an arraylist containing the names of all objects of the context.
	 */
	public ArrayList<String> getObjectsNameList() {
		return objectNames;
	}
	
	/**
	 * Returns the characteristics of the context.
	 * @return an arraylist containing the characteristics of the context.
	 */
	public ArrayList<Characteristic> getCharacteristicList() {
		return characteristics;
	}
	
	/**
	 * Returns the list of objects in the context.
	 * @return an arraylist containing the lists of values representing each object.
	 */
	public ArrayList<ArrayList<String>> getObjects() {
		return this.objects;
	}
	
	
	/*
	 * ------ METHODS------
	 */

	
	/**
	 * Reads the specified csv file and initialises the context.
	 * Values in the csv file must be separated by a coma ','.
	 * @param contextName a String representing the name of the csv file from which initialise the context.
	 */
	private void initFromCSVfile(String contextName) {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(testExtraction.DATA_FOLDER+"0_clean_PCMs/" + contextName));
			
			String line = br.readLine();
			
			String charateristicList = line.substring(line.indexOf(",") + 1, line.length());
			System.out.println("Characteristic list: " + charateristicList);
						
			while ((line = br.readLine()) != null) {
				
				String currentProductName = line.substring(0, line.indexOf(","));			
				this.objectNames.add(currentProductName);
				
				String stringValueList = line.substring(line.indexOf(",") + 1, line.length());
				
				ArrayList<String> valueList = new ArrayList<String>();
								
				for (String value : stringValueList.split(",",-1)) {
					valueList.add(value);
				}
								
				this.objects.add(valueList);
			}
			
			br.close();
			
			int i = 0;
			for (String characteristicName : charateristicList.split(",")) {
				
				ArrayList<String> valueList = new ArrayList<String>();
				
				for (ArrayList<String> product : objects) {
					valueList.add(product.get(i));
				}
				
				HashSet<String> distinctValues = new HashSet<String>();
				distinctValues.addAll(valueList);
				distinctValues.remove("X"); distinctValues.remove(""); distinctValues.remove("*");
				
				Characteristic c;
				
				if (distinctValues.size() == 0) {
					c = new BinaryAttribute (characteristicName, valueList);
				} else {
					c = new MultivaluedAttribute (characteristicName, valueList);
				}
				
				this.characteristics.add(c); 
				
				i++;
			}
			
		} catch (Exception e) {
			System.err.println("Multivalued context cannot be initialised with the file " + contextName);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Applies binary scaling on the current multivalued context and returns the corresponding binary context.
	 * @return an instance of {@link MultivaluedContext} whose characteristics are all binary attributes.
	 */
	private MultivaluedContext computeScaledContext() {
		
		for (Characteristic c : this.characteristics) {
			binaryContext = c.scaling(binaryContext);
		}
		return binaryContext;
	}
	
	
	/**
	 * Prints the context's information.
	 */
	public void printContext() {
	
		System.out.println("############ " + this.name + " ############");
		
		System.out.println("############ Products ############");
		
		for (int i = 0 ; i < objectNames.size() ; i++) {
			
			System.out.println("# " + objectNames.get(i) + ": " + objects.get(i));
		}
		
		System.out.println("############ Characteristics ############");
		
		for (Characteristic c : this.characteristics) {
			System.out.println("# " + c);
		}
		
		System.out.println("####################################");
		
		System.out.println();
	}
	
	
	/**
	 * Prints the characteristics' information.
	 */
	public void printCharacteristicsInfo(){
		System.out.println("############ Characteristics ############");
		
		for (Characteristic c : this.characteristics) {
			HashSet<String> dv = new HashSet<>();
			for (String v : c.getDistinctValues()) {
				for (String v2 : v.split(";")) {
					dv.add(v2);
				}
			}
			ArrayList<String> dvsorted = new ArrayList<>();
			dvsorted.addAll(dv);
			Collections.sort(dvsorted);
			System.out.println("# " + c.name + ": " + dvsorted);
			System.out.println();
		}
	}
	
	
	/**
	 * Adds a new characteristic (and its values) to the context.
	 * @param c the new characteristic.
	 */
	public void addCharacteristic(Characteristic c){
		
		this.characteristics.add(c);
		
		int i = 0;
		for (ArrayList<String> product : this.objects) {
			
			product.add(c.getValues().get(i));
			i++;
			
		}
	}
	
	
	/**
	 * This methods exports the scaled context into an rcft file.
	 */
	private void exportInRcft() {
		
		String rcft = "FormalContext ctx\nalgo acposet\n| |"; 
		
		for (Characteristic c : this.binaryContext.getCharacteristicList()) {
			rcft += c.getName() + "|";
		}
		rcft += "\n";
		
		int productIndex = 0;
		for (String product : this.binaryContext.getObjectsNameList()) {
			
			rcft += "|" + product + "|";
			
			for (String value : this.binaryContext.getObjects().get(productIndex)) {
				
				rcft += value.toLowerCase() + "|";
			}
			rcft += "\n";
			productIndex++;
		}
		
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter (testExtraction.DATA_FOLDER + this.name + "/" + this.name + ".rcft"));
			file.write(rcft);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method computes the concept lattice and AC-poset of the rcft corresponding to the scaled context.
	 */
	public void computeLattice() {
		
		computeScaledContext();
		
		exportInRcft();
		
		try {
			
			String PCM_dir = testExtraction.DATA_FOLDER + name + "\\";
			if(Files.notExists(Paths.get(PCM_dir + "FCA/"))){
				Files.createDirectory(Paths.get(PCM_dir + "FCA\\"));
			}
			
			if(Files.notExists(Paths.get(PCM_dir + "FCA\\AC-poset"))){
				Files.createDirectory(Paths.get(PCM_dir + "FCA\\AC-poset"));
			}
			
			Runtime r = Runtime.getRuntime();
			System.out.println("java -jar "+RAC_EXPLORER_PATH+" auto " + PCM_dir + name + ".rcft " + PCM_dir + "FCA\\AC-poset\\");
//			Process p = r.exec("java -jar "+RAC_EXPLORER_PATH+" auto "+ PCM_dir + name+".rcft " + PCM_dir + "FCA/");
			//NICO
			Process p = r.exec("java -jar "+RAC_EXPLORER_PATH+" auto "+ PCM_dir + name+".rcft " + PCM_dir + "FCA\\AC-poset\\");

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((reader.readLine()) != null) {}
			
			p.waitFor();
			
			System.out.println("java -jar "+RAC_EXPLORER_PATH+" auto "+PCM_dir + name+".rcft "+ PCM_dir + "FCA\\AC-poset\\ --follow-path=trace1.csv");
			p = r.exec("java -jar "+RAC_EXPLORER_PATH+" auto "+PCM_dir + name+".rcft "+ PCM_dir + "FCA\\AC-poset\\ --follow-path=trace.csv");
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((reader.readLine()) != null) {}
			
			p.waitFor();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
