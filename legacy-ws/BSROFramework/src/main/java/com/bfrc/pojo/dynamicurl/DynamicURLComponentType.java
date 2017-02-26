package com.bfrc.pojo.dynamicurl;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class DynamicURLComponentType implements java.io.Serializable {
	private Long dynamicURLComponentTypeId;
	private String name;
	private String variableName;
	
	public Long getDynamicURLComponentTypeId() {
		return dynamicURLComponentTypeId;
	}
	public void setDynamicURLComponentTypeId(Long dynamicURLComponentTypeId) {
		this.dynamicURLComponentTypeId = dynamicURLComponentTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof DynamicURLComponentType))
			return false;
		DynamicURLComponentType otherOfMyType = (DynamicURLComponentType) other;
		return otherOfMyType.getDynamicURLComponentTypeId().equals(this.getDynamicURLComponentTypeId());
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.getDynamicURLComponentTypeId()).toHashCode();
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
}
