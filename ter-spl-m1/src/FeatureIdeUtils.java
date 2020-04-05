import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import spoon.reflect.path.CtRole;

public interface FeatureIdeUtils {
	/**
	 * Créer les packages dans un dossier pour chaque cluster
	 * @param clusterName le nom qui va être attribut à chaque cluster (A générer dans le main)
	 * @param e l'asset qui a comme parent un package (la classe)
	 */
	public static void createPackageForClasses(String clusterName) {
			System.out.println("the folder is "+clusterName);
				new File(clusterName).mkdirs();
		
	};
	
	public static void createFilePackage(String packageName,Noeud e) throws IOException {
		System.out.println("edition khrat "+e.getType().equals("classe"));
		if(e.getType().equals("classe")) {
			FileWriter test = new FileWriter(packageName+e.getNom()+".java");
			test.write(packageName+e.getNom()+ "{");
			for(Asset a:e.Allchildren(Main.getAllAssets())) {
				System.out.println("yaw rahi mranka "+a.getNom()+"\n");
				test.write(a.getNom()+(a.getType().equals("Field") && a.getValue()!=null ?" = ":"")+(a.getValue()!=null ?a.getValue() :"")+"\n");
			}
			test.write("}");
			test.close();
		}
		
	}
	
}
