package model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {


	

	// save the  SPL prettyprint code  into files  at [SPL path]/PrettyPrint/[level]
	public static void saveInFile (String text, String path ) throws IOException {
		File file =  new File (path) ;
		file.getParentFile().mkdirs(); 
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write(text);
		writer.close();
	}

	public static void copyFiles (String path_src, String path_dst ) throws IOException {
		// we assume that src file exist
		File src =  new File (path_src) ;

		//dst path may not exist
		File dst = new File(path_dst);
		dst.getParentFile().mkdirs(); 
		//copy
		Files.copy(src.toPath(), dst.toPath());
	}

	public static String LoadFile (String path ) throws IOException {
		File file =  new File (path) ;
		String s = "";
		int i;
		FileReader reader = new FileReader(file);
		while ((i=reader.read())!=-1) 
			s+=(char)i; 
		reader.close();
		return s;
	}

	
}
