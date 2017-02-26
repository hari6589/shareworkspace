package com.bsro.databean.dynamicurl;

import java.util.Calendar;

public class DynamicURLWithPath {
	private Long dynamicURLId;
	private String siteName;
	private String urlPrefix;
	private String path;
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
	public String getUrlPrefix() {
		return urlPrefix;
	}
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}	
	
}
