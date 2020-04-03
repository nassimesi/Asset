package model;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Node {

	private String identifier;
	
	private ArrayList<Node> mutex = null;
	private ArrayList<Node> implies = null;
	
	public Node(String id) {
		mutex = new ArrayList<>();
		implies = new ArrayList<>();
		identifier = id;
	}
	
	
	/**
	 * @return the unique identifier of a Node
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	
	public void addMutex(Node n){
		if(!mutex.contains(n)) {
			mutex.add(n);
		}
 	}
	
	public void addImplies(Node n){
		if(!implies.contains(n)) {
			implies.add(n);
		}
 	}
	
	public ArrayList<Node> getImplies() {
		return implies;
	}
	
	public ArrayList<Node> getMutex() {
		return mutex;
	}
	
	public ArrayList<String> getAllIdentifiers(){
		return new ArrayList<String>(Arrays.asList(getIdentifier()));
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return identifier.equals(((Node)obj).identifier);
	}
	
	public Boolean compareIdentifier(String id) {
		return this.identifier.equals(id);
	}
	
}
