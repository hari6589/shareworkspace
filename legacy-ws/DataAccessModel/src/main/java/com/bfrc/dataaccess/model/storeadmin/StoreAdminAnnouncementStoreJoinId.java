package com.bfrc.dataaccess.model.storeadmin;

// Generated Oct 19, 2011 7:57:34 PM by Hibernate Tools 3.2.1.GA

public class StoreAdminAnnouncementStoreJoinId implements java.io.Serializable {

	private static final long serialVersionUID = -4169796920349213251L;
	private long announcementId;
	private long storeNumber;

	public StoreAdminAnnouncementStoreJoinId() {
	}

	public StoreAdminAnnouncementStoreJoinId(long announcementId,
			long storeNumber) {
		this.setAnnouncementId(announcementId);
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
		if (!(other instanceof StoreAdminAnnouncementStoreJoinId))
			return false;
		StoreAdminAnnouncementStoreJoinId castOther = (StoreAdminAnnouncementStoreJoinId) other;

		return (this.getAnnouncementId() == castOther.getAnnouncementId())
				&& (this.getStoreNumber() == castOther.getStoreNumber());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result +  (int)this.getAnnouncementId();
		result = 37 * result + (int)this.getStoreNumber();
		return result;
	}

	public void setAnnouncementId(long announcementId) {
		this.announcementId = announcementId;
	}

	public long getAnnouncementId() {
		return announcementId;
	}

}
