package com.bfrc.pojo.dynamicurl;

import java.util.Calendar;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class DynamicURL implements java.io.Serializable {
	private Long dynamicURLId;
	private String siteName;
	private Long dynamicURLTypeId;
	private Long creatorUserId;
	private Calendar createdDate;
	
	public Long getDynamicURLId() {
		return dynamicURLId;
	}
	public void setDynamicURLId(Long dynamicURLId) {
		this.dynamicURLId = dynamicURLId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public Long getDynamicURLTypeId() {
		return dynamicURLTypeId;
	}
	public void setDynamicURLTypeId(Long dynamicURLTypeId) {
		this.dynamicURLTypeId = dynamicURLTypeId;
	}
	public Long getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof DynamicURL))
			return false;
		DynamicURL otherOfMyType = (DynamicURL) other;
		return otherOfMyType.getDynamicURLId().equals(this.getDynamicURLId());
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.getDynamicURLId()).toHashCode();
	}


}
