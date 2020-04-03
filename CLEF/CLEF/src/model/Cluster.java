package model;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents a set of singleton. 
 * Used to model the AND groups in our graph
 * @author nicolas
 *
 */
public class Cluster extends Node{
	
	ArrayList<Singleton> components = new ArrayList<>(); 
	Singleton head;
	

	/**
	 * The constructor return a cluster composed of one element made out of the id passed in paramter.
	 * @param id the identifier of the first element of this cluster, also refered to as the head
	 */
	public Cluster(String id) {
		super(id);
		components.add(new Singleton(id));
		head = components.get(0);
		// TODO Auto-generated constructor stub
	}
	
	public Cluster(ArrayList<String> ids) {
		super(ids.get(0));
		for (String string : ids) {
			components.add(new Singleton(string));
		}
		head = components.get(0);
	}
	
	@Override
	public Boolean compareIdentifier(String id) {
		for (Singleton singleton : components) {
			if(singleton.compareIdentifier(id)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ArrayList<String> getAllIdentifiers() {
		ArrayList<String> allIds = new ArrayList<>();
		for (Singleton s : components) {
			allIds.add(s.getIdentifier());
		}
		return allIds;
	}

}
