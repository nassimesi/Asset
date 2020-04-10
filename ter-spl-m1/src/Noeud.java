import java.io.IOException;
import java.util.ArrayList;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.path.CtRole;

public class Noeud extends Asset implements FeatureIdeUtils{
	
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
	

	public Noeud(Asset a, boolean isPackage, String clusterName, ArrayList<Asset> list) throws IOException {
		if (isPackage) {
			String name = "";
			System.out.println(a.getParent().getValueByRole(CtRole.NAME).toString()+"yaaw");
			CtElement c = a.getParent();
			while(!c.getValueByRole(CtRole.NAME).toString().equals("unnamed package")) {
				name = c.getValueByRole(CtRole.NAME).toString()+"\\"+name;
				//name.replaceFirst("", c.getValueByRole(CtRole.NAME).toString()+"\\");
				//name += c.getValueByRole(CtRole.NAME).toString()+"\\";
				c = c.getParent();
			}
			//name= ".\\" + name;
			System.out.println("the undertaker "+clusterName + name);
			FeatureIdeUtils.createPackageForClasses(clusterName+ name);
			Noeud b = new Noeud(a);
			String fileName = b.getNom().split("class ")[1].substring(0,b.getNom().split("class ")[1].length()-1).split(" ")[0];
			FeatureIdeUtils.createFilePackage(clusterName+name, fileName);
			FeatureIdeUtils.fillFile(clusterName+name+ fileName+".java", a.getNom(), "{\r\n");
			this.setNom(name);
			this.setValue(null);
			this.setType("package");
			this.setParent(null);
		}
		
		}
	
	
}
