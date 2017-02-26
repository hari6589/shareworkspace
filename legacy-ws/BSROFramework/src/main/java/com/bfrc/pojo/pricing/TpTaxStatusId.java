package com.bfrc.pojo.pricing;

// Generated Jun 18, 2007 10:31:30 AM by Hibernate Tools 3.2.0.beta8

/**
 * TpTireFeeId generated by hbm2java
 */
public class TpTaxStatusId implements java.io.Serializable {

	// Fields    

	private long storeNumber;

	private long itaxCode;

	// Constructors

	/** default constructor */
	public TpTaxStatusId() {
	}

	/** full constructor */
	public TpTaxStatusId(long storeNumber, long itaxCode) {
		this.storeNumber = storeNumber;
		this.itaxCode = itaxCode;
	}

	// Property accessors
	public long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public long getItaxCode() {
		return this.itaxCode;
	}

	public void setItaxCode(long itaxCode) {
		this.itaxCode = itaxCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TpTaxStatusId))
			return false;
		TpTaxStatusId castOther = (TpTaxStatusId) other;

		return (this.getStoreNumber() == castOther.getStoreNumber())
				&& (this.getItaxCode() == castOther.getItaxCode());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getStoreNumber();
		result = 37 * result + (int) this.getItaxCode();
		return result;
	}

}