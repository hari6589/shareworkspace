package com.bfrc.dataaccess.model.storeadmin;

// Generated Oct 19, 2011 7:57:34 PM by Hibernate Tools 3.2.1.GA

public class StoreAdminPromotionStoreJoinId implements java.io.Serializable {

	private long promoId;
	private long storeNumber;

	public StoreAdminPromotionStoreJoinId() {
	}

	public StoreAdminPromotionStoreJoinId(long promoId,
			long storeNumber) {
		this.setPromoId(promoId);
		this.storeNumber = storeNumber;
	}

	public long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof StoreAdminPromotionStoreJoinId))
			return false;
		StoreAdminPromotionStoreJoinId castOther = (StoreAdminPromotionStoreJoinId) other;

		return (this.getPromoId() == castOther.getPromoId())
				&& (this.getStoreNumber() == castOther.getStoreNumber());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getPromoId();
		result = 37 * result + (int)this.getStoreNumber();
		return result;
	}

	public void setPromoId(long promoId) {
		this.promoId = promoId;
	}

	public long getPromoId() {
		return promoId;
	}

}
