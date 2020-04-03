package model;

import java.util.ArrayList;

public class VariabilityModel {

	private ArrayList<Node> nodes = new ArrayList<>();
	
	/**
	 * Get all cluster, meaning all the groups of nodes that 
	 * represent artifact that always appears together
	 * @return all the cluster for the model
	 */
	public ArrayList<Cluster> getClusters() {
		ArrayList<Cluster> clusters = new ArrayList<>();
		for (Node node : nodes) {
			if(node instanceof Cluster) {
				clusters.add((Cluster)node);
			}
		}
		return clusters;
	}
	
	
	public ArrayList<Cluster> getVariableClusters() {
		ArrayList<Cluster> clusters = getClusters();
		for(int i =0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			if(c.getImplies().isEmpty() && c.getMutex().isEmpty()) {
				clusters.remove(i);
				break;
			}
		}
		return clusters;
	} 
	
	public Node getNodeWithHeadId(String ID) {
		for (Node node : nodes) {
			if(node.getIdentifier().equals(ID)) {
				return node;
			}
		}
		return null;
	}
	
	public Node getNodeWhereId(String ID) {
		for (Node node : nodes) {
			for (String ids : node.getAllIdentifiers()) {
				if(ids.equals(ID)){
					return node;
				}
			}
		}
		return null;
	}
	
	public ArrayList<Node> getNodes(){
		return nodes;
	}
	
	
	
	public void addNode(Node n) {
		Node temp = this.getNodeWithHeadId(n.getIdentifier());
		if(temp == null) {
			this.nodes.add(n);
		}
	}
	

	
}
