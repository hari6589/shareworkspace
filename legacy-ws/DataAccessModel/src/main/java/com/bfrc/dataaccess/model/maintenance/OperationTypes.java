package com.bfrc.dataaccess.model.maintenance;

import java.util.HashSet;
import java.util.Set;

public class OperationTypes implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private long operationTypeId;
	private String operationType;
	private Set<Intervals> intervals = new HashSet<Intervals>(0);

	public OperationTypes() {}

	public OperationTypes(long operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	public OperationTypes(long operationTypeId, String operationType,
			Set<Intervals> intervals) {
		this.operationTypeId = operationTypeId;
		this.operationType = operationType;
		this.intervals = intervals;
	}

	public long getOperationTypeId() {
		return this.operationTypeId;
	}

	public void setOperationTypeId(long operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	public String getOperationType() {
		return this.operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Set<Intervals> getIntervals() {
		return this.intervals;
	}

	public void setIntervals(Set<Intervals> intervals) {
		this.intervals = intervals;
	}

	@Override
	public String toString() {
		return "OperationTypes [operationTypeId=" + operationTypeId
				+ ", operationType=" + operationType + "]";
	}

	
}
