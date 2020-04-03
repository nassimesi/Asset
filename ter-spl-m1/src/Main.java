import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtFieldImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtFieldReferenceImpl;

import java.io.*;
import java.lang.reflect.Member;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.testExtraction;
import model.Cluster;
import model.VariabilityModel;
import model.VariabilityModelGenerator;
import multivaluedcontext.MultivaluedContext;
import relationshipextraction.AllBinaryImplicationExtractor;
import relationshipextraction.BinaryImplicationExtractor;
import relationshipextraction.CooccurrenceExtractor;
import relationshipextraction.MutexExtractor;

public class Main {

    /**
     * Une liste de listes, chaque liste contient les assets relative à un produits
     * C'est à dire une liste dont chaque élément contient tous les assets d'un produit
     */
    public static ArrayList<ArrayList<Asset>> assetByProduct = new ArrayList<>();

    /**
     * Comme son nom l'indique, générer la matrice Assets/produit nécessaire pour l'outil clef
     * @param allAssets tous les assets contenu dans tous les produits (non dupliqués)
     * @param numberProduct le nombre de produit, sera traité sans intervention de l'utilisateur
     * @throws IOException Exception realtive à l'ouverture, la création et l'accés aux fichiers
     */

    public static ArrayList<String>  generateCSVforCLEF(ArrayList<Asset> allAssets, int numberProduct) throws IOException {
        //La liste de chaîne de caractère contenant les identifiants
    	ArrayList<String> ids = new ArrayList<>();
    	//Le fichier de sortie, extension .csv
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("..\\CLEF\\CLEF\\data\\0_clean_PCMs\\out.csv"));
        StringBuilder line = new StringBuilder("");
        for (Asset e:allAssets
             ) {
            line.append(",").append(e.getId()); //création de la ligne 1, ajout de tous les assets
        }
        ids.addAll(Arrays.asList(line.substring(1).toString().split(",")));
        line.append("\n"); // fin de la ligne 1
        fileWriter.write(line.toString()); //Ecrire la première ligne dans le fichier .csv
        int i=1;
        for (i=1; i<assetByProduct.size()+1; i++) //pour chauqe produit
        {
            line = new StringBuilder("" + i + "/"); //première colonne de chaque ligne suivante indiquand l'id du produit
            for (Asset a:allAssets //pour chaque asset
                 ) {
            	
                //si le produit contient l'asset, on met X , sinon on passe en suivant (virgule généréer dans les deux cas)
                line.append(assetByProduct.get(i - 1).contains(a) ? ",X" : ",");
            }
            line.append("\n"); //fin de la ligne courante
            fileWriter.write(line.toString()); // Ecrire la ligne dans le ficheier
        }
            fileWriter.close(); //fermeture du fichier
            return ids;
    }

    /**
     * @see Asset#assetsFromAst(String)
     * @return la liste de tous les assets dans tous les produits non dupliqués
     * @throws IOException Exception realtive à l'ouverture, la création et l'accés aux fichiers
     */

    public static ArrayList<Asset> getAllAssets() throws IOException {
        //préparer l'environnement : la liste result pour stoquer le résultat
        ArrayList<Asset> result = new ArrayList<Asset>();
        // Un filtre sur les noms
        FilenameFilter filter1 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                //retourner tous les dossier dans le répertoire courant
                return new File(dir, name).isDirectory() && name.startsWith("0");
            }
        };
        //récupérer la liste des noms des dossiers en appliquant le filtre précédent
        String product[] = new File(".").list(filter1);
        int i = 0;
        for (i = 0; i < product.length; i++) { //pour chaque dossier (produit)
            //création d'une nouvelle liste d'asset
            assetByProduct.add(new ArrayList<>());
            final int k = i;
            //parcourir récursivement en respectant le prédicat qui suit
            Stream<Path> paths = Files.walk(Paths.get(product[i]));
            paths.filter(new Predicate<Path>() {
                @Override
                public boolean test(Path path) {
                    //returner que les fichiers java
                    return path.toString().matches(".*\\.java");
                }
            })
                    .forEach(s -> { //pour chaque élément du résultat

                        try {
                        	for (Asset e : Asset.assetsFromAst(s.toString()) //voir See Also
                            ) {
                        		assetByProduct.get(k).add(e); //ajouter chaque asset à la liste des asset du produit concerné
                        		if (!result.contains(e)) {
                        			result.add(e); // éviter les doublons
                        		}
                        		}
                        		
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });}
        //retourner tous les assets pris une seule fois
            return result;
    }


    /**
     * Le main, qui sert à faire tous les traitments et appeler des méthodes déja inmpléméntée
     * @see Variation#allVariation(ArrayList)
     * @see #generateCSVforCLEF(ArrayList, int)
     * @see Variation#listVariation
     * @param args liste des arguments en cas d'utilisation par commande
     * @throws IOException Exception realtive à l'ouverture, la création et l'accés aux fichiers
     */
    public static void main(String[] args) throws IOException {
        CtClass l = Launcher.parseClass("class A { public int a=1; public String b="+"hehe"+" ;  void m() {" +
                "a=a*5; ;" +
                " System.out.println(\"yeah\"+a);} }");

        // 1 - générer le fichier .csv
        ArrayList<Asset> allAssets = (ArrayList<Asset>) getAllAssets().clone();
        ArrayList<String> ids = generateCSVforCLEF(allAssets,assetByProduct.size());
        //  
        //testExtraction.run("E:\\GitLirmmProject\\CLEF\\CLEF\\data", "out.csv");
        String DATA_FOLDER = "../CLEF/CLEF/data/";
        System.out.println("oww " + Paths.get(".").toAbsolutePath());
        MultivaluedContext pm = new MultivaluedContext("out.csv");
		
        String path = DATA_FOLDER + pm.getName() + "/";
		
        long currentTime = java.lang.System.currentTimeMillis();
		pm.computeLattice();
		
		pm.printContext();
		BinaryImplicationExtractor bie = new BinaryImplicationExtractor(path);
		bie.computeRelationships();
		
		AllBinaryImplicationExtractor abie = new AllBinaryImplicationExtractor(path);
		abie.computeRelationships();
		
		CooccurrenceExtractor ce = new CooccurrenceExtractor(path);
		ce.computeRelationships();
		
		MutexExtractor me = new MutexExtractor(path);
		me.computeRelationships();
		
		bie.exportsInTextFile();
		ce.exportsInTextFile();
		abie.exportsInTextFile();
		me.exportsInTextFile();
		
		long timeToComputeACposet = java.lang.System.currentTimeMillis() - currentTime;

        System.out.println("oww " + Paths.get(".").toAbsolutePath());
		VariabilityModelGenerator model = new VariabilityModelGenerator(path+"variability/", path+"model/variabilityModel.dot");
		model.saveInFile();
		
		/**
		 * QUELQUES EXEMPLE SUR COMMENT UTILISER LE MODEL
		 * (voir package model)
		 * 
		 * **/
		
		// Exemple de comment recuperer le model 
		VariabilityModel variabilityModel = model.getVaribilityGraph();
		
		//get cluster 
		variabilityModel.getClusters();
		
		//print the cluster set 
		System.out.println("hahowa "+variabilityModel.getClusters().get(0).getAllIdentifiers());
		
		//get identifiers of a specific cluster
		ArrayList<Cluster> clusters = variabilityModel.getClusters();
		for(Cluster e:clusters){
			ArrayList<Asset> tmp = new ArrayList<Asset>();
			for(String a:e.getAllIdentifiers()){
				System.out.println("this is : "+a +" in list "+ ids.size());
				tmp.add(allAssets.get(ids.indexOf(a)));
			}
			System.out.println("wash a "+tmp.toString());
		}
		
		
		// get cluster variable 
		variabilityModel.getVariableClusters();
		
        //Récupérer tous les variations de facn brute
        Variation.allVariation(assetByProduct);
        //filter les variation : une variation avec un seul asset doit être supprimée parce que ce n'est plus un asset
        Variation.listVariation.removeIf(new Predicate<Variation>() {
            @Override
            public boolean test(Variation variation) {
                return variation.getRelatedAssets().size()<=1;
            }
        });
        //afficher toutes les variations
        System.out.println(allAssets.toString());
    }
}
