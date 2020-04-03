package util;

import java.util.Comparator;

public class SortStringAsInteger implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		int a0 = Integer.valueOf(arg0);
		int a1 = Integer.valueOf(arg1);
		
		return (a0 == a1 ? 0 : ((a0 < a1) ? -1 : 1));
	}

}
