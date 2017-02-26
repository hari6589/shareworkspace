package com.bfrc.pojo.pricing;

// Generated Jun 18, 2007 10:31:30 AM by Hibernate Tools 3.2.0.beta8

import java.math.BigDecimal;

/**
 * TpTireFee generated by hbm2java
 */
public class TpTaxDesc implements java.io.Serializable {

	// Fields    

	private TpTaxDescId id;
	
	private long storeNumber;
	
	private long  counter;
	
	private String taxDesc;
	
	private BigDecimal laborRate;
	
	private BigDecimal materialRate;
	
	private long nDefault;
	
	private long nDefault2;
	
	// Constructors

	/** default constructor */
	public TpTaxDesc() {
	}

	/** full constructor */
	public TpTaxDesc(TpTaxDescId id, String taxDesc, BigDecimal materialRate,
			long nDefault, long nDefault2) {
		this.id = id;
		this.taxDesc = taxDesc;
		this.materialRate = materialRate;
		this.nDefault = nDefault;
		this.nDefault2 = nDefault2;
	}

	/** full constructor */
	public TpTaxDesc(TpTaxDescId id, String taxDesc, BigDecimal materialRate, BigDecimal laborRate,
			long nDefault, long nDefault2) {
		this.id = id;
		this.taxDesc = taxDesc;
		this.materialRate = materialRate;
		this.laborRate = laborRate;
		this.nDefault = nDefault;
		this.nDefault2 = nDefault2;
	}
	
	// Property accessors
	public TpTaxDescId getId() {
		return this.id;
	}

	public void setId(TpTaxDescId id) {
		this.id = id;
	}

	public String getTaxDesc() {
		return this.taxDesc;
	}

	public void setTaxDesc(String taxDesc) {
		this.taxDesc = taxDesc;
	}

	public BigDecimal getLaborRate() {
		return this.laborRate;
	}

	public void setLaborRate(BigDecimal laborRate) {
		this.laborRate = laborRate;
	}

	public BigDecimal getMaterialRate() {
		return this.materialRate;
	}

	public void setMaterialRate(BigDecimal materialRate) {
		this.materialRate = materialRate;
	}

	public long getNdefault() {
		return this.nDefault;
	}

	public void setNdefault(long nDefault) {
		this.nDefault = nDefault;
	}
	
	public long getNdefault2() {
		return this.nDefault2;
	}

	public void setNdefault2(long nDefault2) {
		this.nDefault2 = nDefault2;
	}
	

}
