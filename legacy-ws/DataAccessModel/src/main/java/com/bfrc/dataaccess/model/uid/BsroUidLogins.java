package com.bfrc.dataaccess.model.uid;

import java.util.Date;

public class BsroUidLogins {

	private String userName;
	private String password;
	private Integer userId;
	private String emailAddress;
	private String activeCd;
	private String emailConfirmCd;
	private Date emailConfirmDt;
	private Date creationDt;
	private Date lastLoginDt;
	private String tosCd;
	private String passwordResetCd;
	
	private BsroUids bsroUids;
	
	public BsroUidLogins(){}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getActiveCd() {
		return activeCd;
	}

	public void setActiveCd(String activeCd) {
		this.activeCd = activeCd;
	}

	public String getEmailConfirmCd() {
		return emailConfirmCd;
	}

	public void setEmailConfirmCd(String emailConfirmCd) {
		this.emailConfirmCd = emailConfirmCd;
	}

	public Date getEmailConfirmDt() {
		return emailConfirmDt;
	}

	public void setEmailConfirmDt(Date emailConfirmDt) {
		this.emailConfirmDt = emailConfirmDt;
	}

	public Date getCreationDt() {
		return creationDt;
	}

	public void setCreationDt(Date creationDt) {
		this.creationDt = creationDt;
	}

	public Date getLastLoginDt() {
		return lastLoginDt;
	}

	public void setLastLoginDt(Date lastLoginDt) {
		this.lastLoginDt = lastLoginDt;
	}

	public String getTosCd() {
		return tosCd;
	}

	public void setTosCd(String tosCd) {
		this.tosCd = tosCd;
	}

	public String getPasswordResetCd() {
		return passwordResetCd;
	}

	public void setPasswordResetCd(String passwordResetCd) {
		this.passwordResetCd = passwordResetCd;
	}
	
	public void setBsroUids(BsroUids bsroUids) {
		this.bsroUids = bsroUids;
	}
	
	public BsroUids getBsroUids() {
		return bsroUids;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		BsroUidLogins other = (BsroUidLogins) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BsroUidLogins [userName=" + userName + ", password=" + password
				+ ", userId=" + userId + ", emailAddress=" + emailAddress
				+ ", activeCd=" + activeCd + ", emailConfirmCd="
				+ emailConfirmCd + ", emailConfirmDt=" + emailConfirmDt
				+ ", creationDt=" + creationDt + ", lastLoginDt=" + lastLoginDt
				+ ", tosCd=" + tosCd + ", passwordResetCd=" + passwordResetCd
				+ "]";
	}
	
	
}
