/**
 * 
 */
package com.bfrc.dataaccess.model.store;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author smoorthy
 *
 */
public class WeekDayComparator implements Comparator<StoreHour> {
	
	List<String> orderedWeekdays = Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");
	private Map<String, Integer> sortOrder = new HashMap<String, Integer>();
	
	public WeekDayComparator()
	{
		for (int i = 0; i < orderedWeekdays.size(); i++)
		{
			String weekday = orderedWeekdays.get(i);
			sortOrder.put(weekday, i);
		}
	}
	
	/*public WeekDayComparator(Map<String, Integer> sortOrder)
	{
		this.sortOrder = sortOrder;
	}*/

	public int compare(StoreHour i1, StoreHour i2)
	{
		Integer weekdayPos1 = sortOrder.get(i1.getId().getWeekDay());
		if (weekdayPos1 == null)
		{
			throw new IllegalArgumentException("Bad weekday encountered: " + i1.getId().getWeekDay());
		}
		Integer weekdayPos2 = sortOrder.get(i2.getId().getWeekDay());
		if (weekdayPos2 == null)
		{
			throw new IllegalArgumentException("Bad weekday encountered: " + i2.getId().getWeekDay());
		}
		return weekdayPos1.compareTo(weekdayPos2);
	}
}
