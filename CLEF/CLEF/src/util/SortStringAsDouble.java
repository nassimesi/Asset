package util;

import java.util.Comparator;

public class SortStringAsDouble implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		double a0 = Double.valueOf(arg0);
		double a1 = Double.valueOf(arg1);
		
		return (a0 == a1 ? 0 : ((a0 < a1) ? -1 : 1));
	}

}
