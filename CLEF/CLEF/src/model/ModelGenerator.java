package model;

import java.io.IOException;


public abstract class ModelGenerator {

	protected String pathOut;
	protected String graphvizCode = null;
	
	// For graphviz
	protected String graphDeclaration = "digraph G {\n" + 
			"  compound = true;\n" + 
			"  rankdir = \"LR\"; \n" + 
			"  nodesep= 0.5;\n" + 
			"  ranksep = 5; \n";
	
	protected final String quote = "\"";
	
	public abstract String ToStringGraphviz();

	public void saveInFile() {
		try {
			FileManager.saveInFile(this.ToStringGraphviz(), this.pathOut);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String colorGenerator(String seed) {
		return String.format("#%X", seed.hashCode());
	}
	
	/**
	 * Transform a text into a node for graphivz
	 * like \"Text\"
	 * */
	protected String node(String text) {
		return quote + text + quote;
	}
	
	/**
	 * Transform a text into a node for graphivz
	 * like \"Text\"
	 * */
	protected String node(int text) {
		return node(text+"");
	}
	
	
	protected String shapeLegend() {
		String s = "";
		String style = " subgraph \"cluster_"+-1+"\" {\n" + 
				"		color=black;\n" + 
				"\n";
		String label = "\n label = \"LEGEND\";\n" + 
				"	}";
		
		String content = "Compilation_Unit [shape=\"note\"] "
				+ "Type [shape=\"box3d\"] "
				+ "Method [shape=\"Msquare\"] "
				+ "Field [shape=\"Mcircle\"] "
				+ "Import [shape=\"lpromoter\"] "
				+ "Package [shape=\"folder\"] "
				+ "\n";
		s += style + content + label;
		
		return s;
	}
	
}
