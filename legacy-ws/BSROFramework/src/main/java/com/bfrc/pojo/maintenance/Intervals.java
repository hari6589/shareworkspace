package com.bfrc.pojo.maintenance;

// Generated Jan 19, 2007 2:46:32 PM by Hibernate Tools 3.2.0.beta8

/**
 * Intervals generated by hbm2java
 */
public class Intervals implements java.io.Serializable {

	// Fields    

	private IntervalsId id;

	private OperationTypes operationTypes;

	private Components components;

	// Constructors

	/** default constructor */
	public Intervals() {
	}

	/** full constructor */
	public Intervals(IntervalsId id, OperationTypes operationTypes,
			Components components) {
		this.id = id;
		this.operationTypes = operationTypes;
		this.components = components;
	}

	// Property accessors
	public IntervalsId getId() {
		return this.id;
	}

	public void setId(IntervalsId id) {
		this.id = id;
	}

	public OperationTypes getOperationType() {
		return this.operationTypes;
	}

	public void setOperationType(OperationTypes operationTypes) {
		this.operationTypes = operationTypes;
	}

	public Components getComponent() {
		return this.components;
	}

	public void setComponent(Components components) {
		this.components = components;
	}

	public Components getComponents() {
		return this.components;
	}

	public void setComponents(Components components) {
		this.components = components;
	}

	public OperationTypes getOperationTypes() {
		return this.operationTypes;
	}

	public void setOperationTypes(OperationTypes operationTypes) {
		this.operationTypes = operationTypes;
	}

}
