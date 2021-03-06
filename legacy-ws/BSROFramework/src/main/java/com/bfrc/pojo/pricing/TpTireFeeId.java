package com.bfrc.pojo.pricing;

// Generated Jun 18, 2007 10:31:30 AM by Hibernate Tools 3.2.0.beta8

/**
 * TpTireFeeId generated by hbm2java
 */
public class TpTireFeeId implements java.io.Serializable {

	// Fields    

	private long storeNumber;

	private long feeTypeCode;

	// Constructors

	/** default constructor */
	public TpTireFeeId() {
	}

	/** full constructor */
	public TpTireFeeId(long storeNumber, long feeTypeCode) {
		this.storeNumber = storeNumber;
		this.feeTypeCode = feeTypeCode;
	}

	// Property accessors
	public long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public long getFeeTypeCode() {
		return this.feeTypeCode;
	}

	public void setFeeTypeCode(long feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TpTireFeeId))
			return false;
		TpTireFeeId castOther = (TpTireFeeId) other;

		return (this.getStoreNumber() == castOther.getStoreNumber())
				&& (this.getFeeTypeCode() == castOther.getFeeTypeCode());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getStoreNumber();
		result = 37 * result + (int) this.getFeeTypeCode();
		return result;
	}

}
