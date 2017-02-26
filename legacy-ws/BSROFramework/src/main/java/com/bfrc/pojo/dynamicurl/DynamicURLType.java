package com.bfrc.pojo.dynamicurl;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class DynamicURLType implements java.io.Serializable {
	private Long dynamicURLTypeId;
	private String urlPrefix;
	
	public Long getDynamicURLTypeId() {
		return dynamicURLTypeId;
	}

	public void setDynamicURLTypeId(Long dynamicURLTypeId) {
		this.dynamicURLTypeId = dynamicURLTypeId;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public boolean equals(Object other) {
		if (!(other instanceof DynamicURLType))
			return false;
		DynamicURLType otherOfMyType = (DynamicURLType) other;
		return otherOfMyType.getDynamicURLTypeId().equals(this.getDynamicURLTypeId());
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.getDynamicURLTypeId()).toHashCode();
	}
}
