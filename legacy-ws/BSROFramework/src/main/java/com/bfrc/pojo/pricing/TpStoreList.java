package com.bfrc.pojo.pricing;

// Generated Jun 18, 2007 10:31:30 AM by Hibernate Tools 3.2.0.beta8

import java.math.BigDecimal;

/**
 * TpStoreList generated by hbm2java
 */
public class TpStoreList implements java.io.Serializable {

	// Fields    

	private TpStoreListId id;

	private BigDecimal disposalPrice;

	private long exciseTaxArticle;

	// Constructors

	/** default constructor */
	public TpStoreList() {
	}

	/** full constructor */
	public TpStoreList(TpStoreListId id, BigDecimal disposalPrice,
			long exciseTaxArticle) {
		this.id = id;
		this.disposalPrice = disposalPrice;
		this.exciseTaxArticle = exciseTaxArticle;
	}

	// Property accessors
	public TpStoreListId getId() {
		return this.id;
	}

	public void setId(TpStoreListId id) {
		this.id = id;
	}

	public BigDecimal getDisposalPrice() {
		return this.disposalPrice;
	}

	public void setDisposalPrice(BigDecimal disposalPrice) {
		this.disposalPrice = disposalPrice;
	}

	public long getExciseTaxArticle() {
		return this.exciseTaxArticle;
	}

	public void setExciseTaxArticle(long exciseTaxArticle) {
		this.exciseTaxArticle = exciseTaxArticle;
	}

}
