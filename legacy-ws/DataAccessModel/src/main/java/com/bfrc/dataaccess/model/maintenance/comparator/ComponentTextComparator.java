package com.bfrc.dataaccess.model.maintenance.comparator;

import java.util.Comparator;

import com.bfrc.dataaccess.model.maintenance.Intervals;

public class ComponentTextComparator implements Comparator<Intervals> {

	public int compare(Intervals a, Intervals b) {
		if (a == null || b == null)
			return 0;
		if (a.getComponents() == null
				|| a.getComponents().getComponent() == null
				|| b.getComponents() == null
				|| b.getComponents().getComponent() == null)
			return 0;

		return a.getComponents().getComponent()
				.compareTo(b.getComponents().getComponent());

	}
}
