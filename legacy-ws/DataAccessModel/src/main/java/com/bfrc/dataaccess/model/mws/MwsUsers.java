/**
 * 
 */
package com.bfrc.dataaccess.model.mws;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.bfrc.dataaccess.model.contact.WebSite;

/**
 * @author schowdhu
 *
 */
public class MwsUsers implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String email;
	private String previousEmail;
	private String password;
	private WebSite userType;
	private Date registerDate;
	private String status;
	private Integer unsuccessfulAttempts;
	
	private Set<MwsBackupData> mwsBackUpData = new HashSet<MwsBackupData>();
	
	public MwsUsers() {
	}
	
	/**
	 * 
	 * @param email
	 * @param previousEmail
	 * @param password
	 * @param userType
	 */
	public MwsUsers(String email, String previousEmail,
			String password, WebSite userType) {
		this.email = email;
		this.previousEmail = previousEmail;
		this.password = password;
		this.userType = userType;
		this.registerDate = new Date();
		this.status = MwsUserStatus.ACTIVE.getStatusCode();
		this.unsuccessfulAttempts = 0;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the previousEmail
	 */
	public String getPreviousEmail() {
		return previousEmail;
	}
	/**
	 * @param previousEmail the previousEmail to set
	 */
	public void setPreviousEmail(String previousEmail) {
		this.previousEmail = previousEmail;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userType
	 */
	public WebSite getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(WebSite userType) {
		this.userType = userType;
	}

	/**
	 * @return the registerDate
	 */
	public Date getRegisterDate() {
		return registerDate;
	}
	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	/**
	 * @return the mwsBackUpData
	 */
	public Set<MwsBackupData> getMwsBackUpData() {
		return mwsBackUpData;
	}

	/**
	 * @param mwsBackUpData the mwsBackUpData to set
	 */
	public void setMwsBackUpData(Set<MwsBackupData> mwsBackUpData) {
		this.mwsBackUpData = mwsBackUpData;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the unsuccessfulAttempts
	 */
	public Integer getUnsuccessfulAttempts() {
		return unsuccessfulAttempts;
	}

	/**
	 * @param unsuccessfulAttempts the unsuccessfulAttempts to set
	 */
	public void setUnsuccessfulAttempts(Integer unsuccessfulAttempts) {
		this.unsuccessfulAttempts = unsuccessfulAttempts;
	}	
}
