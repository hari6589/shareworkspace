package com.bfrc.pojo.tagging;

import java.io.Serializable;
import java.util.Date;

public class TagVariable implements Serializable {
	
	private Long tagVariableId;
	private TagKey tagKey;
	private String type;
	private String requestAttributeName;
	private String variableValueName;
	private Date createdDate;
	private Date updatedDate;
	public String getVariableValueName() {
		return variableValueName;
	}
	public void setVariableValueName(String variableValueName) {
		this.variableValueName = variableValueName;
	}
	public Long getTagVariableId() {
		return tagVariableId;
	}
	public void setTagVariableId(Long tagVariableId) {
		this.tagVariableId = tagVariableId;
	}
	public TagKey getTagKey() {
		return tagKey;
	}
	public void setTagKey(TagKey tagKey) {
		this.tagKey = tagKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRequestAttributeName() {
		return requestAttributeName;
	}
	public void setRequestAttributeName(String requestAttributeName) {
		this.requestAttributeName = requestAttributeName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
