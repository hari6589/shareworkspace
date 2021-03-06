package com.bfrc.pojo.pricing;

// Generated Jun 18, 2007 10:31:30 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * TpSuppressionBrand generated by hbm2java
 */
public class TpSuppressionBrand implements java.io.Serializable {

	// Fields    

	private TpSuppressionBrandId id;

	private Long userId;

	private Date createdDate;

	// Constructors

	/** default constructor */
	public TpSuppressionBrand() {
	}

	/** minimal constructor */
	public TpSuppressionBrand(TpSuppressionBrandId id) {
		this.id = id;
	}

	/** full constructor */
	public TpSuppressionBrand(TpSuppressionBrandId id, Long userId,
			Date createdDate) {
		this.id = id;
		this.userId = userId;
		this.createdDate = createdDate;
	}

	// Property accessors
	public TpSuppressionBrandId getId() {
		return this.id;
	}

	public void setId(TpSuppressionBrandId id) {
		this.id = id;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
