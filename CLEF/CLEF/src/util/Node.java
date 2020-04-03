package util;

import java.util.ArrayList;
import java.util.HashSet;

public class Node {
	
	private ArrayList<Node> parents;
	
	private ArrayList<Node> children;
	
	private HashSet<String> value;

	public Node() {
		parents = new ArrayList<Node>();
		children = new ArrayList<Node>(); 
		value = new HashSet<String>();
	}
	
	public void addChild(Node n) {
		if (!children.contains(n)) {
			children.add(n);
		}
	}
	
	public void removeChild(Node n) {
		children.remove(n);
	}
	
	public void addParent(Node n) {
		parents.add(n);
	}
	
	public ArrayList<Node> getParents() {
		return parents;
	}
	
	public void setValue(HashSet<String> v) {
		value.addAll(v);
	}
	
	public HashSet<String> getValue() {
		return value;
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public String toString() {
		return value.toString();
	}
}