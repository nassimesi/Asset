package main;

import java.io.File;

import model.VariabilityModel;
import model.VariabilityModelGenerator;
import multivaluedcontext.MultivaluedContext;
import relationshipextraction.AllBinaryImplicationExtractor;
import relationshipextraction.BinaryImplicationExtractor;
import relationshipextraction.CooccurrenceExtractor;
import relationshipextraction.MutexExtractor;

public class testExtraction {
	public static String DATA_FOLDER = "..\\CLEF\\CLEF\\data\\";

	public static void main(String[] args) {
		final String path = "out.csv";
		run(DATA_FOLDER,path);
	}
	
	/**
	 * 
	 * @param dataFolder absolute path to the data folder in CLEF project
	 * @param csvFile name of the csv file to analyseÒ
	 */
	public static void run(String dataFolder, String csvFile) {
		DATA_FOLDER = dataFolder;
		long currentTime = java.lang.System.currentTimeMillis();

		System.out.println("oww "+new File("test").getParent());
		MultivaluedContext pm = new MultivaluedContext(csvFile);
		///Users/nicolas/Logiciels/Projets/CLEF/CLEF/data/0_clean_PCMs/counterv1.csv
		
		String path = DATA_FOLDER + pm.getName() + "\\";
		
		System.out.println();
		
		pm.computeLattice();
		
		pm.printContext();
		
		long timeToComputeACposet = java.lang.System.currentTimeMillis() - currentTime;
		
		BinaryImplicationExtractor bie = new BinaryImplicationExtractor(path);
		bie.computeRelationships();
		
		AllBinaryImplicationExtractor abie = new AllBinaryImplicationExtractor(path);
		abie.computeRelationships();
		
		CooccurrenceExtractor ce = new CooccurrenceExtractor(path);
		ce.computeRelationships();
		
		MutexExtractor me = new MutexExtractor(path);
		me.computeRelationships();
		
		bie.exportsInTextFile();
		ce.exportsInTextFile();
		abie.exportsInTextFile();
		me.exportsInTextFile();
		
		long timeToComputeRelationships = java.lang.System.currentTimeMillis() - currentTime - timeToComputeACposet;

		System.out.println("Time to compute the AC-poset: " + timeToComputeACposet);
		System.out.println("Time to compute the relationships: " + (timeToComputeRelationships));
		
		
//		VariabilityModelGenerator model = new VariabilityModelGenerator(path+"variability/", path+"model/variabilityModel.dot");
//		model.saveInFile();
//		
//		/**
//		 * QUELQUES EXEMPLE SUR COMMENT UTILISER LE MODEL
//		 * (voir package model)
//		 * 
//		 * **/
//		
//		// Exemple de comment recuperer le model 
//		VariabilityModel variabilityModel = model.getVaribilityGraph();
//		
//		//get cluster 
//		variabilityModel.getClusters();
//		
//		// get cluster variable 
//		variabilityModel.getVariableClusters();
//		
		
	}
	
	@Deprecated
	public static void run(String csvFile) {
		long currentTime = java.lang.System.currentTimeMillis();

		MultivaluedContext pm = new MultivaluedContext("PCM.csv");
		///Users/nicolas/Logiciels/Projets/CLEF/CLEF/data/0_clean_PCMs/counterv1.csv
		
		String path = DATA_FOLDER + pm.getName() + "/";
		
		pm.computeLattice();
		
		pm.printContext();
		
		long timeToComputeACposet = java.lang.System.currentTimeMillis() - currentTime;
		
		BinaryImplicationExtractor bie = new BinaryImplicationExtractor(path);
		bie.computeRelationships();
		
		AllBinaryImplicationExtractor abie = new AllBinaryImplicationExtractor(path);
		abie.computeRelationships();
		
		CooccurrenceExtractor ce = new CooccurrenceExtractor(path);
		ce.computeRelationships();
		
		MutexExtractor me = new MutexExtractor(path);
		me.computeRelationships();
		
		bie.exportsInTextFile();
		ce.exportsInTextFile();
		abie.exportsInTextFile();
		me.exportsInTextFile();
		
		long timeToComputeRelationships = java.lang.System.currentTimeMillis() - currentTime - timeToComputeACposet;

		System.out.println("Time to compute the AC-poset: " + timeToComputeACposet);
		System.out.println("Time to compute the relationships: " + (timeToComputeRelationships));
	}

}
