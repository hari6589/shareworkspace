package com.bfrc.pojo.dynamicurl;

import org.apache.commons.lang.builder.HashCodeBuilder;


public class DynamicURLComponent implements java.io.Serializable {
	private Long dynamicURLComponentId;
	private Long dynamicURLId;
	private Long dynamicURLComponentTypeId;
	private String value;
	private String urlText;
	private Long sequence;
	
	public Long getDynamicURLComponentId() {
		return dynamicURLComponentId;
	}
	public void setDynamicURLComponentId(Long dynamicURLComponentId) {
		this.dynamicURLComponentId = dynamicURLComponentId;
	}
	public Long getDynamicURLId() {
		return dynamicURLId;
	}
	public void setDynamicURLId(Long dynamicURLId) {
		this.dynamicURLId = dynamicURLId;
	}
	public Long getDynamicURLComponentTypeId() {
		return dynamicURLComponentTypeId;
	}
	public void setDynamicURLComponentTypeId(Long dynamicURLComponentTypeId) {
		this.dynamicURLComponentTypeId = dynamicURLComponentTypeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUrlText() {
		return urlText;
	}
	public void setUrlText(String urlText) {
		this.urlText = urlText;
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof DynamicURLComponent))
			return false;
		DynamicURLComponent otherOfMyType = (DynamicURLComponent) other;
		return otherOfMyType.getDynamicURLComponentId().equals(this.getDynamicURLComponentId());
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.getDynamicURLComponentId()).toHashCode();
	}
}
