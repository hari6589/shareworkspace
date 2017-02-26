/**
 * 
 */
package com.bsro.pojo.appointment;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author schowdhu
 *
 */
public class YearMonthComparator implements Comparator<String> {
	
	List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September","October","November", "December");

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String o1, String o2) {
		//compares two strings like 2013November and 2014Janurary
		//get the year
		String y1 = o1.substring(0, 4);
		String y2 = o2.substring(0, 4);

		if(new Integer(y1).intValue() < new Integer(y2).intValue()){
			return -1;
		}else if(new Integer(y1).intValue() > new Integer(y2).intValue()){
			return 1;
		}else{
			//get the months
			String m1 = o1.substring(4);
			String m2 = o2.substring(4);

			int o1Index = months.indexOf(m1);
			int o2Index = months.indexOf(m2);

			if(o1Index < o2Index) {
				return -1;
			}
			else if (o1Index == o2Index) {
				return 0;
			}else {
				return 1;
			}
		}		
	}
}
