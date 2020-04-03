import java.util.ArrayList;

import spoon.reflect.path.CtRole;

public class Noeud extends Asset{
	
	private ArrayList<Asset> children = new ArrayList<Asset>();
	
	public Noeud(Asset a){
		
		this.setNom(a.getNom());
		this.setValue(a.getValue());
		this.setType(a.getType());
		this.setParent(a.getParent());
		
	}


	public ArrayList<Asset> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Asset> children) {
		this.children = children;
	}
	
	public ArrayList<Asset> Allchildren (ArrayList<Asset> assets){
		ArrayList<Asset> tmp = new ArrayList<Asset>();
		for(int i=0; i<assets.size();i++) {
			System.out.println("test  "+assets.get(i).getParent().getValueByRole(CtRole.NAME).toString()+" and nom = "+this.getNom());
			if(assets.get(i).getParent().getValueByRole(CtRole.NAME).toString().equals(this.getNom().replace("public class ", ""))) 
				{
					children.add(assets.get(i));
				}
		}
		tmp.addAll(children);
		return tmp;
		
	}
	

	
	
}
