package com.bsro.pojo.appointment;

import java.util.Comparator;

public class DateStringComparator implements Comparator<String> {
	
	@Override
	public int compare(String o1, String o2) {
		int o1Int = Integer.parseInt(o1);
		int o2Int = Integer.parseInt(o2);
		
		if(o1Int < o2Int) 
			return -1;
		else if (o1Int == o2Int) 
			return 0;
		else 
			return 1;
	}

}
