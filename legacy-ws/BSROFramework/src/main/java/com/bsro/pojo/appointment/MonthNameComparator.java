package com.bsro.pojo.appointment;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MonthNameComparator implements Comparator<String> {
	
	List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September","October","November", "December");

	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		int o1Index = months.indexOf(o1);
		int o2Index = months.indexOf(o2);
		
		if(o1Index < o2Index) 
			return -1;
		else if (o1Index == o2Index) 
			return 0;
		else 
			return 1;
	}

}
