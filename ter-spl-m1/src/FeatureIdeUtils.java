import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.sun.org.apache.xerces.internal.util.URI;

import spoon.reflect.path.CtRole;

public interface FeatureIdeUtils {
	/**
	 * Cr�er les packages dans un dossier pour chaque cluster
	 * @param clusterName le nom qui va �tre attribut � chaque cluster (A g�n�rer dans le main)
	 * @param e l'asset qui a comme parent un package (la classe)
	 */
	public static void createPackageForClasses(String clusterName) {
			System.out.println("the folder is "+clusterName);
				new File(clusterName).mkdirs();
		
	};
	
	public static void createFilePackage(String packageName,String value) throws IOException {
		//System.out.println("edition khrat "+e.getType().equals("classe"));
		System.out.println("the name = "+value);
		//if(e.getType().equals("classe")) {
			String values = value.replace('.', '\\');
			File test2 = new File(packageName);
			test2.mkdirs();
			//FileWriter test = new FileWriter(packageName+value+".java");
			//test.write(packageName+e.getNom()+ "{");
//			for(Asset a:e.Allchildren(b)) {
//				System.out.println("yaw rahi mranka "+a.getNom()+"\n");
//				test.write(a.getNom()+(a.getType().equals("Field") && a.getValue()!=null ?" = ":"")+(a.getValue()!=null ?a.getValue() :"")+"\n");
//			}
//			test.write("}");
//			test.close();
	//	}
		
	}
	public static void fillFile(String fileName, String name, String value) throws IOException {
		Path path = Paths.get(fileName);
		String all = new String();
		//BufferedInputStream reader = new BufferedInputStream(new FileInputStream(fileName) );
		if (path.toFile().exists()) { 
			for(String s:Files.readAllLines(path,StandardCharsets.UTF_8))
			all += s+"\n" ;
			}
		//FileReader in = new FileReader(new File(fileName));
		if(all!= null) System.out.println(path + " the size of "+ all+"");
System.out.println(name + " :; "+ value);
		Path path2 = Files.createTempFile(path.getParent(),"test-file", ".java");
		Files.write(path2, ((all+"").substring(0, Math.max((all+"").length()-2, 0)) +  "\n" +  name + value+ "}").getBytes());
		Files.copy(path2, path, StandardCopyOption.REPLACE_EXISTING);
		Files.deleteIfExists(path2);
		//FileWriter test = new FileWriter(fileName,true);
		//BufferedWriter out = new BufferedWriter(test);
//		test.write(all.toString().replaceAll(", ", " ").substring(1, all.toString().replaceAll(", ", " ").length()-1) +  "\n" +  name + (name.endsWith(")")?"":" = ")+ value+ (name.endsWith(")")?"":";")+"\n }");
		//out.write(all.toString().replaceAll(", ", " ").substring(1, all.toString().replaceAll(", ", " ").length()-1) +  "\n" +  name + (name.endsWith(")")?"":" = ")+ value+ (name.endsWith(")")?"":";")+"\n }");
		//out.flush();
		//out.close();
		//test.flush();
	//	test.close();
		//all = Files.readAllLines(Paths.get(fileName),StandardCharsets.UTF_8);
		//System.out.println("all here "+all);
		//test.close();
	}
	
}
