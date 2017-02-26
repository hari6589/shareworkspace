package com.bfrc.pojo.pricing;

// Generated Jun 18, 2007 10:31:30 AM by Hibernate Tools 3.2.0.beta8

/**
 * TpSuppressionDisplayId generated by hbm2java
 */
public class TpSuppressionDisplayId implements java.io.Serializable {

	// Fields    

	private long suppressionId;

	private long displayId;

	// Constructors

	/** default constructor */
	public TpSuppressionDisplayId() {
	}

	/** full constructor */
	public TpSuppressionDisplayId(long suppressionId, long displayId) {
		this.suppressionId = suppressionId;
		this.displayId = displayId;
	}

	// Property accessors
	public long getSuppressionId() {
		return this.suppressionId;
	}

	public void setSuppressionId(long suppressionId) {
		this.suppressionId = suppressionId;
	}

	public long getDisplayId() {
		return this.displayId;
	}

	public void setDisplayId(long displayId) {
		this.displayId = displayId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TpSuppressionDisplayId))
			return false;
		TpSuppressionDisplayId castOther = (TpSuppressionDisplayId) other;

		return (this.getSuppressionId() == castOther.getSuppressionId())
				&& (this.getDisplayId() == castOther.getDisplayId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getSuppressionId();
		result = 37 * result + (int) this.getDisplayId();
		return result;
	}

}
