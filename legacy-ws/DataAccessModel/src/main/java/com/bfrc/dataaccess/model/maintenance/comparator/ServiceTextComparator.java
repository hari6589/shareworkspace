package com.bfrc.dataaccess.model.maintenance.comparator;

import java.util.Comparator;

import com.bfrc.dataaccess.model.maintenance.Intervals;

public class ServiceTextComparator implements Comparator<Intervals> {

	public int compare(Intervals a, Intervals b) {

		if (a == null || b == null || a.getId() == null || b.getId() == null
				|| a.getId().getServiceType() == null
				|| b.getId().getServiceType() == null)
			return 0;

		return a.getId().getServiceType().compareTo(b.getId().getServiceType());
	}

}
