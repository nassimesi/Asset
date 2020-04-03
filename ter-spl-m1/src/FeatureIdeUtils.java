import java.io.File;
import java.util.Arrays;

import spoon.reflect.path.CtRole;

public abstract class FeatureIdeUtils {
	public static void createPackageForClasses(String clusterName,Asset e) {
		if (e.getType().equals("classe")) {
			String path = e.getParent().prettyprint()+""; 
			System.out.println("list of folders "+Arrays.toString(new File(".").listFiles()));
			if(!Arrays.asList(new File(".").listFiles()).contains(new File(".\\"+e.getParent().getValueByRole(CtRole.NAME)+""))) {
				System.out.println("3la chati2a lmatbakh"+e.getParent().getValueByRole(CtRole.NAME).toString().equals("unnamed package"));
				new File(clusterName+(e.getParent().getValueByRole(CtRole.NAME).toString().equals("unnamed package")?"source":(e.getParent().getValueByRole(CtRole.NAME)+""))).mkdirs();
			}
		}
	};
}
