/**
 * 
 */
package com.bfrc.dataaccess.model.notification;

import java.io.Serializable;
import java.util.Date;

/**
 * @author schowdhu
 *
 */
public class DeviceMessage implements Serializable {

	private static final long serialVersionUID = -1657933589382756572L;
	
	private Long deviceMessageId;
	private String deviceUUID;
	private String readFlag;
	private String pinnedFlag;
	private String deleteFlag;
	private Date lastReadDate;
	private Date lastUpdateDate;
	private ServiceNotification serviceNotification;
	

	public DeviceMessage() {
	}
	public Long getDeviceMessageId() {
		return deviceMessageId;
	}
	public void setDeviceMessageId(Long deviceMessageId) {
		this.deviceMessageId = deviceMessageId;
	}
	public String getDeviceUUID() {
		return deviceUUID;
	}
	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}
	public String getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}
	public String getPinnedFlag() {
		return pinnedFlag;
	}
	public void setPinnedFlag(String pinnedFlag) {
		this.pinnedFlag = pinnedFlag;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public Date getLastReadDate() {
		return lastReadDate;
	}
	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	

	
	public ServiceNotification getServiceNotification() {
		return serviceNotification;
	}
	public void setServiceNotification(ServiceNotification serviceNotification) {
		this.serviceNotification = serviceNotification;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deleteFlag == null) ? 0 : deleteFlag.hashCode());
		result = prime * result
				+ ((deviceUUID == null) ? 0 : deviceUUID.hashCode());
		result = prime * result
				+ ((lastReadDate == null) ? 0 : lastReadDate.hashCode());
		result = prime * result
				+ ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result
				+ ((pinnedFlag == null) ? 0 : pinnedFlag.hashCode());
		result = prime * result
				+ ((readFlag == null) ? 0 : readFlag.hashCode());
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
		DeviceMessage other = (DeviceMessage) obj;
		if (deleteFlag == null) {
			if (other.deleteFlag != null)
				return false;
		} else if (!deleteFlag.equals(other.deleteFlag))
			return false;
		if (deviceUUID == null) {
			if (other.deviceUUID != null)
				return false;
		} else if (!deviceUUID.equals(other.deviceUUID))
			return false;
		if (lastReadDate == null) {
			if (other.lastReadDate != null)
				return false;
		} else if (!lastReadDate.equals(other.lastReadDate))
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (pinnedFlag == null) {
			if (other.pinnedFlag != null)
				return false;
		} else if (!pinnedFlag.equals(other.pinnedFlag))
			return false;
		if (readFlag == null) {
			if (other.readFlag != null)
				return false;
		} else if (!readFlag.equals(other.readFlag))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DeviceMessage [deviceMessageId=" + deviceMessageId
				+ ", deviceUUID=" + deviceUUID + ", readFlag=" + readFlag
				+ ", pinnedFlag=" + pinnedFlag + ", deleteFlag=" + deleteFlag
				+ ", lastReadDate=" + lastReadDate + ", lastUpdateDate="
				+ lastUpdateDate + "]";
	}
	
}
