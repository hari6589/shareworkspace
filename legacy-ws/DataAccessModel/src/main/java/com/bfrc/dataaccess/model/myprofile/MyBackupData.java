/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.sql.Clob;
import java.util.Date;

/**
 * @author schowdhu
 *
 */
public class MyBackupData {
	
	private Long myBackupDataId;
	private BFSUser user;
	private Clob jsonData;
	private String deleteFlag;
	private String descFlag;
	private Date lastModifiedDate;
	
	
	public MyBackupData() {
	}
	
	/**
	 * 
	 * @param user
	 * @param jsonData
	 * @param lastModifiedDesc
	 */
	public MyBackupData(BFSUser user, Clob jsonData,
			String descFlag) {
		this.user = user;
		this.jsonData = jsonData;
		this.descFlag = descFlag;
		this.lastModifiedDate = new Date();
	}
	
	/**
	 * @param user
	 * @param jsonData
	 * @param deleteFlag
	 * @param lastModifiedDesc
	 */
	public MyBackupData(BFSUser user, Clob jsonData, 
			String deleteFlag, String descFlag) {
		this.user = user;
		this.jsonData = jsonData;
		this.deleteFlag = deleteFlag;
		this.descFlag = descFlag;
		this.lastModifiedDate = new Date();
	}

	/**
	 * @return the myBackupDataId
	 */
	public Long getMyBackupDataId() {
		return myBackupDataId;
	}
	/**
	 * @param myBackupDataId the myBackupDataId to set
	 */
	public void setMyBackupDataId(Long myBackupDataId) {
		this.myBackupDataId = myBackupDataId;
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
	 * @return the jsonData
	 */
	public Clob getJsonData() {
		return jsonData;
	}
	/**
	 * @param jsonData the jsonData to set
	 */
	public void setJsonData(Clob jsonData) {
		this.jsonData = jsonData;
	}
	/**
	 * @return the deleteFlag
	 */
	public String getDeleteFlag() {
		return deleteFlag;
	}
	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/**
	 * @return the descFlag
	 */
	public String getDescFlag() {
		return descFlag;
	}
	/**
	 * @param descFlag the descFlag to set
	 */
	public void setDescFlag(String descFlag) {
		this.descFlag = descFlag;
	}
	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyBackupData [myBackupDataId=" + myBackupDataId + ", user="
				+ user + ", jsonData=" + jsonData + ", deleteFlag="
				+ deleteFlag + ", descFlag=" + descFlag + ", lastModifiedDate="
				+ lastModifiedDate + "]";
	}

}
