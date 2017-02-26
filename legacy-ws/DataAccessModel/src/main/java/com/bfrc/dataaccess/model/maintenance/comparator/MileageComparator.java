package com.bfrc.dataaccess.model.maintenance.comparator;

import java.util.Comparator;

import com.bfrc.dataaccess.model.maintenance.Intervals;

public class MileageComparator implements Comparator<Intervals> {

	public int compare(Intervals a, Intervals b) {

		if (a == null || b == null || a.getId() == null || b.getId() == null
				|| a.getId().getMileageInterval() == null
				|| b.	getId().getMileageInterval() == null)
			return 0;

		return a.getId().getMileageInterval()
				.compareTo(b.getId().getMileageInterval());
	}

}
