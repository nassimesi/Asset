package util;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SortBySetSize implements Comparator<Set<String>> {

	@Override
	public int compare(Set<String> arg0, Set<String> arg1) {
		if (arg0.size() == arg1.size()) {
			return 0;
		} else {
			return (arg0.size() < arg1.size() ? -1 : 1);
		}
		
	}

}
