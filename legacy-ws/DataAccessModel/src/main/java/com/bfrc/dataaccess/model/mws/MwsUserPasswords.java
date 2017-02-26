/**
 * 
 */
package com.bfrc.dataaccess.model.mws;

import java.io.Serializable;
import java.util.Date;

/**
 * @author schowdhu
 *
 */
public class MwsUserPasswords implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long passwordId;
	private MwsUsers user;
	private String password;
	private String prevPasswords;
	private Date insertDate;
	private Date numberofAttempts;
	
	
	/**
	 * @return the passwordId
	 */
	public Long getPasswordId() {
		return passwordId;
	}
	/**
	 * @param passwordId the passwordId to set
	 */
	public void setPasswordId(Long passwordId) {
		this.passwordId = passwordId;
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
	 * @return the prevPasswords
	 */
	public String getPrevPasswords() {
		return prevPasswords;
	}
	/**
	 * @param prevPasswords the prevPasswords to set
	 */
	public void setPrevPasswords(String prevPasswords) {
		this.prevPasswords = prevPasswords;
	}
	/**
	 * @return the insertDate
	 */
	public Date getInsertDate() {
		return insertDate;
	}
	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	/**
	 * @return the numberofAttempts
	 */
	public Date getNumberofAttempts() {
		return numberofAttempts;
	}
	/**
	 * @param numberofAttempts the numberofAttempts to set
	 */
	public void setNumberofAttempts(Date numberofAttempts) {
		this.numberofAttempts = numberofAttempts;
	}
	
	
}
