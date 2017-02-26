package com.bfrc.pojo.promotion;

// Generated Jun 14, 2007 11:50:22 AM by Hibernate Tools 3.2.0.beta8

/**
 * TpPromotionDisplayId generated by hbm2java
 */
public class TpPromotionDisplayId implements java.io.Serializable {

	// Fields    

	private long promotionId;

	private long displayId;

	// Constructors

	/** default constructor */
	public TpPromotionDisplayId() {
	}

	/** full constructor */
	public TpPromotionDisplayId(long promotionId, long displayId) {
		this.promotionId = promotionId;
		this.displayId = displayId;
	}

	// Property accessors
	public long getPromotionId() {
		return this.promotionId;
	}

	public void setPromotionId(long promotionId) {
		this.promotionId = promotionId;
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
		if (!(other instanceof TpPromotionDisplayId))
			return false;
		TpPromotionDisplayId castOther = (TpPromotionDisplayId) other;

		return (this.getPromotionId() == castOther.getPromotionId())
				&& (this.getDisplayId() == castOther.getDisplayId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getPromotionId();
		result = 37 * result + (int) this.getDisplayId();
		return result;
	}

}