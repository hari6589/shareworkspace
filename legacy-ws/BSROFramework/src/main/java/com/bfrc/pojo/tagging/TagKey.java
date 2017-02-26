package com.bfrc.pojo.tagging;

import java.io.Serializable;
import java.util.Date;

public class TagKey implements Serializable{
	public static final int  RESULTS_PER_PAGE = 20;
	
	private Long tagKeyId;
	private String key;
	private String siteName;
	private Date createdDate;
	private Date updatedDate;
	public Long getTagKeyId() {
		return tagKeyId;
	}
	public void setTagKeyId(Long tagKeyId) {
		this.tagKeyId = tagKeyId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
