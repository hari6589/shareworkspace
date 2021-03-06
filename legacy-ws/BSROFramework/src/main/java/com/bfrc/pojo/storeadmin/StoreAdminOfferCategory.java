package com.bfrc.pojo.storeadmin;

// Generated Oct 19, 2011 7:57:34 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;

/**
 * StoreAdminOfferCategory generated by hbm2java
 */
public class StoreAdminOfferCategory implements java.io.Serializable {

	private long categoryId;
	private String friendlyId;
	private String name;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public StoreAdminOfferCategory() {
	}

	public StoreAdminOfferCategory(long categoryId, Date createdDate) {
		this.categoryId = categoryId;
		this.createdDate = createdDate;
	}

	public StoreAdminOfferCategory(long categoryId, String friendlyId,
			String name, String createdBy, Date createdDate, String modifiedBy,
			Date modifiedDate) {
		this.categoryId = categoryId;
		this.friendlyId = friendlyId;
		this.name = name;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}

	public long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getFriendlyId() {
		return this.friendlyId;
	}

	public void setFriendlyId(String friendlyId) {
		this.friendlyId = friendlyId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public boolean equals(Object o) {

		if (o instanceof StoreAdminOfferCategory) {
			StoreAdminOfferCategory aoc = (StoreAdminOfferCategory) o;
			if (this.categoryId == aoc.getCategoryId())
				return true;
		}
		return false;
	}

}
