package com.bfrc.dataaccess.model.maintenance.comparator;

import java.util.Comparator;

import com.bfrc.dataaccess.model.maintenance.Intervals;

public class TimeIntervalComparator implements Comparator<Intervals> {

	public int compare(Intervals a, Intervals b) {
		if (a == null || b == null)
			return 0;
		if (a.getTimeInterval() == null || b.getTimeInterval() == null)
			return 0;
		return a.getTimeInterval().compareTo(b.getTimeInterval());
	}

}
