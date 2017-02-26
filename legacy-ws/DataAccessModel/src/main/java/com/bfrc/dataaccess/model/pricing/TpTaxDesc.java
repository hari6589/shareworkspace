package com.bfrc.dataaccess.model.pricing;

import java.io.Serializable;
import java.math.BigDecimal;

public class TpTaxDesc implements Serializable {

	private static final long serialVersionUID = 1L;
	private TpTaxDescId id;
	private long storeNumber;
	private long  counter;
	private String taxDesc;
	private BigDecimal laborRate;
	private BigDecimal materialRate;
	private long nDefault;
	private long nDefault2;

	public TpTaxDesc() {
	}
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TpTaxDesc other = (TpTaxDesc) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
