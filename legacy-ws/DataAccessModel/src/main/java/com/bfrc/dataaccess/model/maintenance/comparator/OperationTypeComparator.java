package com.bfrc.dataaccess.model.maintenance.comparator;

import java.util.Comparator;

import com.bfrc.dataaccess.model.maintenance.Intervals;

public class OperationTypeComparator implements Comparator<Intervals> {

	public int compare(Intervals a, Intervals b) {

		if (a == null || b == null || a.getOperationTypes() == null
				|| b.getOperationTypes() == null
				|| a.getOperationTypes().getOperationType() == null
				|| b.getOperationTypes().getOperationType() == null)
			return 0;
		
		return a.getOperationTypes().getOperationType().compareTo(b.getOperationTypes().getOperationType());
	}

}
