/**
 * 
 */
package com.bfrc.dataaccess.model.mws;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

/**
 * @author schowdhu
 *
 */
public class MwsBackupData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long backupId;
	private MwsUsers user;
	private String module;
	private Clob jsonData;
	private String deleteFlag;
	private String lastModifiedDesc;
	private Date lastModifiedDate;
	
	public MwsBackupData() {
	}
	
	/**
	 * 
	 * @param user
	 * @param jsonData
	 * @param lastModifiedDesc
	 */
	public MwsBackupData(MwsUsers user, Clob jsonData,
			String lastModifiedDesc) {
		this.user = user;
		this.jsonData = jsonData;
		this.lastModifiedDesc = lastModifiedDesc;
		this.lastModifiedDate = new Date();
	}
	
	/**
	 * @param user
	 * @param jsonData
	 * @param deleteFlag
	 * @param lastModifiedDesc
	 */
	public MwsBackupData(MwsUsers user, Clob jsonData, 
			String deleteFlag, String lastModifiedDesc) {
		this.user = user;
		this.jsonData = jsonData;
		this.deleteFlag = deleteFlag;
		this.lastModifiedDesc = lastModifiedDesc;
		this.lastModifiedDate = new Date();
	}


	/**
	 * @param user
	 * @param module
	 * @param jsonData
	 * @param deleteFlag
	 * @param lastModifiedDesc
	 */
	public MwsBackupData(MwsUsers user, String module,
			Clob jsonData, String deleteFlag, String lastModifiedDesc) {
		this.user = user;
		this.module = module;
		this.jsonData = jsonData;
		this.deleteFlag = deleteFlag;
		this.lastModifiedDesc = lastModifiedDesc;
		this.lastModifiedDate = new Date();
	}

	/**
	 * @return the backupId
	 */
	public Long getBackupId() {
		return backupId;
	}

	/**
	 * @param backupId the backupId to set
	 */
	public void setBackupId(Long backupId) {
		this.backupId = backupId;
	}

	/**
	 * @return the user
	 */
	public MwsUsers getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(MwsUsers user) {
		this.user = user;
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
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
	 * @return the lastModifiedDesc
	 */
	public String getLastModifiedDesc() {
		return lastModifiedDesc;
	}

	/**
	 * @param lastModifiedDesc the lastModifiedDesc to set
	 */
	public void setLastModifiedDesc(String lastModifiedDesc) {
		this.lastModifiedDesc = lastModifiedDesc;
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

}
