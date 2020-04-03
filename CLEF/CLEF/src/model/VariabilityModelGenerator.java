package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.plaf.metal.MetalPopupMenuSeparatorUI;


public class VariabilityModelGenerator extends ModelGenerator {

	private final String path_cooccurrences = "cooccurrences.txt";
	private final String path_implications = "binary_implications_TR.txt";
	private final String path_mutex = "mutex.txt";

	protected String pathIn;
	protected File coocurencesFile;
	protected File mutexFile;
	protected File implicationsFile;

	protected VariabilityModel variabilityModel = new VariabilityModel();

	private ArrayList<ArrayList<String>> andGroups = new ArrayList<>();
	private ArrayList<Pair<String, String>> mutexEdges = new ArrayList<Pair<String,String>>();
	private ArrayList<Pair<String, String>> implyEdges = new ArrayList<Pair<String,String>>();


	/**
	 * @param pathIn the path of the folder contening the CLEF files for implication, mutex and co-occurence
	 * @param PathOut the path where you want the graph to be printed 
	 */
	public VariabilityModelGenerator(String pathIn, String PathOut) {
		this.pathIn = pathIn;
		this.pathOut = PathOut;
		coocurencesFile = new File(pathIn+path_cooccurrences);
		mutexFile = new File(pathIn+path_mutex);
		implicationsFile = new File(pathIn+path_implications);
		this.graphvizCode = ToStringGraphviz();
	}

	private String coocurenceParser() {
		String s="\n // CO-OCCURENCE GROUPS \n";

		// prendre les second S tant le 1er P est toujours le même
		// puis sauter toutes les lignes tant que les seconds n'est pas égals au dernier des second last(S)
		int i = 0;
		andGroups.add(new ArrayList<String>());


		final String regex = "(.*) <-> (.*)";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

		try  
		{  
			FileReader fr=new FileReader(coocurencesFile);   //reads the file  
			BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  

			Stream<String> temp = br.lines();
			ArrayList<String> entries = temp.collect(Collectors.toCollection(ArrayList::new)); 
			fr.close();    //closes the stream and release the resources  

			ArrayList<String> lastGroup = andGroups.get(andGroups.size()-1);

			for (String line : entries) {
				String first = null, second = null;
				Matcher matcher = pattern.matcher(line);
				if(matcher.find()) {	
					first = matcher.group(1);
					second = matcher.group(2);

					// on creer un nouveau groupe si on trouve un first qui n'appartient pas deja au groupe
					if(!lastGroup.contains(first)) {
						andGroups.add(new ArrayList<>());
						lastGroup = andGroups.get(andGroups.size()-1);
						lastGroup.add(first);
						lastGroup.add(second);
					}else {
						if(!lastGroup.contains(second)) {
							lastGroup.add(second);
						}

					}
				}
			}
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}  

		for (int t = 0; t<andGroups.size(); t++) {
			if(andGroups.get(t).isEmpty()) {
				andGroups.remove(t);
			}
		}

		for (ArrayList<String> andGroup : andGroups) {



			String style = " subgraph \"cluster_"+i+"\" {\n" + 
//					"  	style=filled;\n" +  //affiche les groupe AND avec une couleur de fond
					"		color=black;\n" + 
					"		node [style=filled,color=white]; \n";
			String label = "\n label = \"COOCURENCE_GRP_"+i+"\";\n" + 
					"	}";
			i++;
			String IDs = "";
			for (String string : andGroup) {

				IDs += quote+string+quote + "; ";
			}


			s+= style + IDs + label;
			s+= "\n";



		}

		return s;
	}



	private String implicationParser() {
		String s="\n // IMPLICATION \n" + 
				"//\"A\" -> \"B\" [lhead = cluster_0;color=\"blue\"]\n" ;

		final String regex = "(.*) -> (.*)";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		implyEdges = new ArrayList<>();


		try  
		{  
			FileReader fr=new FileReader(implicationsFile);   //reads the file  
			BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
			String line;  
			while((line=br.readLine())!=null)  
			{  
				final Matcher matcher = pattern.matcher(line);

				String first = null, second = null;

				if(matcher.find()) {				
					first = matcher.group(1);
					second = matcher.group(2);
				}else {
					System.err.println("Impossible to find pattern in line :" + line);
					break;
				}

				String linkToGroup = "";
				String head = "";
				String tail = "";
				String style = "color=\"blue\"]";


				//if the tail of the imply is a group's member 
				int iTail;
				for(iTail = 0; iTail < andGroups.size(); iTail++) {
					ArrayList<String> group = andGroups.get(iTail);
					if(group.contains(first)) {
						tail= " ltail = cluster_"+iTail+";";
						first = group.get(0); // first est remplace par la tete du groupe
						break;
					}
				}

				//if the head of the imply is a group's member 
				int iHead;
				for(iHead = 0; iHead < andGroups.size(); iHead++) {
					ArrayList<String> group = andGroups.get(iHead);
					if(group.contains(second)) {
						head= " lhead = cluster_"+iHead+";";
						second = group.get(0); // second est remplcé par la tete du groupe
						break;
					}
				}


				Pair<String, String> p = new Pair<String, String>(first, second);
				if(!implyEdges.contains(p)) {
					implyEdges.add(p);
					linkToGroup = "[" + tail + head + style;

					String quote = "\"";
					s+= quote+ first +quote + " -> " + quote+second+quote + linkToGroup ; 
					s+= "\n";

				}

			}  
			fr.close();    //closes the stream and release the resources  
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}  


		return s;
	}



	private String mutexParser() {
		String s="\n // MUTEX \n" + 
				"//\"A\" ->! \"C\" [arrowhead=invdot;color=\"red\";] \n" ;

		final String regex = "(.*) ->! (.*)";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

		mutexEdges = new ArrayList<>();



		try  
		{  
			FileReader fr=new FileReader(mutexFile);   //reads the file  
			BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
			String line;  
			while((line=br.readLine())!=null)  
			{  
				final Matcher matcher = pattern.matcher(line);

				String first = null, second = null;

				if (matcher.find()) {				
					first = matcher.group(1);
					second = matcher.group(2);
				}else {
					System.err.println("Impossible to find pattern in line :" + line);
					break;
				}

				//				Singleton s1 = new Singleton(first);
				//				Singleton s2 = new Singleton(second);
				//
				//				s1.addMutex(s2);

				String linkToGroup = "";
				String head = "";
				String tail = "";
				String style = "color=\"blue\"]";


				//tail 
				int iTail;
				for(iTail = 0; iTail < andGroups.size(); iTail++) {
					ArrayList<String> group = andGroups.get(iTail);
					if(group.contains(first)) {
						tail= " ltail = cluster_"+iTail+";";
						first = group.get(0); // first est remplace par la tete du groupe
						break;
					}
				}

				//head
				int iHead;
				for(iHead = 0; iHead < andGroups.size(); iHead++) {
					ArrayList<String> group = andGroups.get(iHead);
					if(group.contains(second)) {
						head= " lhead = cluster_"+iHead+";";
						second = group.get(0); // second est remplcé par la tete du groupe
						break;
					}
				}


				Pair<String, String> p = new Pair<String, String>(first, second);
				if(!mutexEdges.contains(p)) {
					mutexEdges.add(p);

					String parameters = " ["+tail + head + " arrowhead=normalicurvecurve;color=\"red\";]";
					s+= quote+ first +quote + " -> " + quote+second+quote+parameters  ; //TODO: if in cluster change color arrow 
					s+= "\n";

				}	

			}  
			fr.close();    //closes the stream and release the resources  
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}  



		return s;
	}

	public VariabilityModel getVaribilityGraph() {
		variabilityModel = new VariabilityModel();

		for (ArrayList<String> andGroup : andGroups) {
			// we add the cluster as node in the graph 
			variabilityModel.addNode(new Cluster(andGroup));
		}

		for (Pair<String, String> pair : implyEdges) {
			String tail = pair.x;
			String head = pair.y;

			Node n1 = variabilityModel.getNodeWithHeadId(tail);
			Node n2 = variabilityModel.getNodeWithHeadId(head);

			if(n1 == null) {
				n1 = new Singleton(tail);
			}
			if(n2 == null) {
				n2 = new Singleton(head);
			}
			n1.addImplies(n2);
			variabilityModel.addNode(n1);
			variabilityModel.addNode(n2);

		}

		for (Pair<String, String> pair : mutexEdges) {
			String tail = pair.x;
			String head = pair.y;

			Node n1 = variabilityModel.getNodeWithHeadId(tail);
			Node n2 = variabilityModel.getNodeWithHeadId(head);

			if(n1 == null) {
				n1 = new Singleton(tail);
			}
			if(n2 == null) {
				n2 = new Singleton(head);
			}
			n1.addMutex(n2);
			variabilityModel.addNode(n1);
			variabilityModel.addNode(n2);
		}

		return variabilityModel;

	}
	
	
	

	
	
	
	
	
	@Override
	public String ToStringGraphviz() {
		if(graphvizCode != null) {
			return graphvizCode;
		}

		graphvizCode = graphDeclaration;
	
		graphvizCode+= coocurenceParser();
		graphvizCode+= implicationParser();
		graphvizCode+= mutexParser();
		
		graphvizCode+= "}";

		return graphvizCode;
	}

	

}
