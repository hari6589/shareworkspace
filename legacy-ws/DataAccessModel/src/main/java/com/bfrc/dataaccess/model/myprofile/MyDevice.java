/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"user","lastModifiedDate", "backupdata"})
@JsonPropertyOrder({"myDeviceId", "identifier", "mfgName", "deviceModel", "deviceType", "status"})
public class MyDevice {
	
	private Long myDeviceId;
	private BFSUser user;
	private MyBackupData backupData;
	private String mfgName;
	private String deviceModel;
	private String deviceType;
	private String syncFlag;
	private Date lastSyncDate;
	private Date lastBackupDate;
	private String identifier;
	private StatusType activeFlag;

	public MyDevice() {
	}
	/**
	 * @return the myDeviceId
	 */
	@JsonProperty("id")
	public Long getMyDeviceId() {
		return myDeviceId;
	}
	/**
	 * @param myDeviceId the myDeviceId to set
	 */
	@JsonProperty("id")
	public void setMyDeviceId(Long myDeviceId) {
		this.myDeviceId = myDeviceId;
	}
	/**
	 * @return the user
	 */
	public BFSUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(BFSUser user) {
		this.user = user;
	}
	/**
	 * @return the backupData
	 */
	@JsonIgnore
	public MyBackupData getBackupData() {
		return backupData;
	}
	/**
	 * @param backupData the backupData to set
	 */
	@JsonIgnore
	public void setBackupData(MyBackupData backupData) {
		this.backupData = backupData;
	}
	/**
	 * @return the mfgName
	 */
	public String getMfgName() {
		return mfgName;
	}
	/**
	 * @param mfgName the mfgName to set
	 */
	public void setMfgName(String mfgName) {
		this.mfgName = mfgName;
	}
	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}
	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the syncFlag
	 */
	public String getSyncFlag() {
		return syncFlag;
	}
	/**
	 * @param syncFlag the syncFlag to set
	 */
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	/**
	 * @return the lastSyncDate
	 */
	public Date getLastSyncDate() {
		return lastSyncDate;
	}
	/**
	 * @param lastSyncDate the lastSyncDate to set
	 */
	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	/**
	 * @return the lastBackupDate
	 */
	public Date getLastBackupDate() {
		return lastBackupDate;
	}
	/**
	 * @param lastBackupDate the lastBackupDate to set
	 */
	public void setLastBackupDate(Date lastBackupDate) {
		this.lastBackupDate = lastBackupDate;
	}
	/**
	 * @return the activeFlag
	 */
	@JsonProperty("status")
	public StatusType getActiveFlag() {
		return activeFlag;
	}
	/**
	 * @param activeFlag the activeFlag to set
	 */
	@JsonProperty("status")
	public void setActiveFlag(StatusType activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	@Override
	public String toString() {
		return "MyDevice [myDeviceId=" + myDeviceId 
				+ ", backupData=" + backupData + ", mfgName=" + mfgName
				+ ", deviceModel=" + deviceModel + ", deviceType=" + deviceType
				+ ", syncFlag=" + syncFlag + ", lastSyncDate=" + lastSyncDate
				+ ", lastBackupDate=" + lastBackupDate + ", identifier="
				+ identifier + ", activeFlag=" + activeFlag + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activeFlag == null) ? 0 : activeFlag.hashCode());
		result = prime * result
				+ ((deviceModel == null) ? 0 : deviceModel.hashCode());
		result = prime * result
				+ ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result
				+ ((lastBackupDate == null) ? 0 : lastBackupDate.hashCode());
		result = prime * result
				+ ((lastSyncDate == null) ? 0 : lastSyncDate.hashCode());
		result = prime * result + ((mfgName == null) ? 0 : mfgName.hashCode());
		result = prime * result
				+ ((syncFlag == null) ? 0 : syncFlag.hashCode());
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
		MyDevice other = (MyDevice) obj;
		if (activeFlag != other.activeFlag)
			return false;
		if (deviceModel == null) {
			if (other.deviceModel != null)
				return false;
		} else if (!deviceModel.equals(other.deviceModel))
			return false;
		if (deviceType == null) {
			if (other.deviceType != null)
				return false;
		} else if (!deviceType.equals(other.deviceType))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (lastBackupDate == null) {
			if (other.lastBackupDate != null)
				return false;
		} else if (!lastBackupDate.equals(other.lastBackupDate))
			return false;
		if (lastSyncDate == null) {
			if (other.lastSyncDate != null)
				return false;
		} else if (!lastSyncDate.equals(other.lastSyncDate))
			return false;
		if (mfgName == null) {
			if (other.mfgName != null)
				return false;
		} else if (!mfgName.equals(other.mfgName))
			return false;
		if (syncFlag == null) {
			if (other.syncFlag != null)
				return false;
		} else if (!syncFlag.equals(other.syncFlag))
			return false;
		return true;
	}
	
	

}
