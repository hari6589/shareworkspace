/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.bfrc.dataaccess.model.contact.WebSite;

/**
 * @author schowdhu
 *
 */

@JsonIgnoreProperties({ "userType", 
		"previousEmail","registerDate","unsuccessfulAttempts"})
@JsonPropertyOrder({"id", "email", "lastModifiedDate", "authString", "registerDateTime", "status"})
public class BFSUser {

	private Long userId;
	
	private String email;

	private WebSite userType;

	private String password;
	
	@Temporal(TemporalType.TIME)
	private Calendar registerDate;

	private String previousEmail;
	
	private String status;
	
	private Date lastModifiedDate;
	
	private Integer unsuccessfulAttempts;
	
	private MyDriver driver;

	//@JsonManagedReference
	private Set<MyBackupData> myBackupData = new HashSet<MyBackupData>();

	private Set<MyDevice> devices = new HashSet<MyDevice>();

	private List<MyProductQuote> productQuotes = new ArrayList<MyProductQuote>();

	private Set<MyPromotion> promotions = new HashSet<MyPromotion>();
	
	private SimpleDateFormat dformat = new SimpleDateFormat("MMddyyyy-HH:mm");
	/**
	 * 
	 */
	public BFSUser() {
	}

	/**
	 * 
	 * @param email
	 * @param previousEmail
	 * @param password
	 * @param userType
	 */
	public BFSUser(String email, String previousEmail,
			String password, WebSite userType) {
		this.email = email;
		this.previousEmail = previousEmail;
		this.password = password;
		this.userType = userType;
		this.registerDate = Calendar.getInstance();
		this.status = StatusType.ACTIVE.getValue();
		this.unsuccessfulAttempts = 0;
	}
	
	/**
	 * @return the userId
	 */
	@JsonProperty("id")
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	@JsonProperty("id")
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
	 * @return the password
	 */
	@JsonProperty("authString")
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	@JsonProperty("authString")
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the registerDate
	 */
	public Calendar getRegisterDate() {
		return registerDate;
	}
	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(Calendar registerDate) {
		this.registerDate = registerDate;
	}
	
	/**
	 * @return the registerDateStr
	 */
	@JsonProperty("registerDateTime")
	public String getRegisterDateStr() {	
		String registerDateStr = dformat.format(registerDate.getTime());
		return registerDateStr;
	}
	/**
	 * @param registerDateStr the registerDateStr to set
	 */
	@JsonProperty("registerDateTime")
	public void setRegisterDateStr(String registerDateStr) {
		Date date=null;
		try {
			date = dformat.parse(registerDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		this.registerDate = cal;
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
	/**
	 * @return the myBackupData
	 */
	@JsonIgnore
	public Set<MyBackupData> getMyBackupData() {
		return myBackupData;
	}
	/**
	 * @param myBackupData the myBackupData to set
	 */
	@JsonIgnore
	public void setMyBackupData(Set<MyBackupData> myBackupData) {
		this.myBackupData = myBackupData;
	}

	/**
	 * @return the devices
	 */
	@JsonIgnore
	public Set<MyDevice> getDevices() {
		return devices;
	}
	/**
	 * @param devices the devices to set
	 */
	@JsonIgnore
	public void setDevices(Set<MyDevice> devices) {
		this.devices = devices;
	}

	/**
	 * @return the productQuotes
	 */
	@JsonIgnore
	public List<MyProductQuote> getProductQuotes() {
		return productQuotes;
	}
	/**
	 * @param productQuotes the productQuotes to set
	 */
	@JsonIgnore
	public void setProductQuotes(List<MyProductQuote> productQuotes) {
		this.productQuotes = productQuotes;
	}
	/**
	 * @return the promotions
	 */
	@JsonIgnore
	public Set<MyPromotion> getPromotions() {
		return promotions;
	}
	/**
	 * @param promotions the promotions to set
	 */
	@JsonIgnore
	public void setPromotions(Set<MyPromotion> promotions) {
		this.promotions = promotions;
	}

	public MyDriver getDriver() {
		return driver;
	}

	public void setDriver(MyDriver driver) {
		this.driver = driver;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BFSUser [userId=" + userId + ", email=" + email + ", userType="
				+ userType + ", password=" + password + ", registerDate="
				+ registerDate + ", previousEmail=" + previousEmail
				+ ", status=" + status + ", lastModifiedDate="
				+ lastModifiedDate + ", unsuccessfulAttempts="
				+ unsuccessfulAttempts + ", promotions=" + promotions + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime
				* result
				+ ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((previousEmail == null) ? 0 : previousEmail.hashCode());
		result = prime * result
				+ ((registerDate == null) ? 0 : registerDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime
				* result
				+ ((unsuccessfulAttempts == null) ? 0 : unsuccessfulAttempts
						.hashCode());
		result = prime * result
				+ ((userType == null) ? 0 : userType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BFSUser other = (BFSUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (previousEmail == null) {
			if (other.previousEmail != null)
				return false;
		} else if (!previousEmail.equals(other.previousEmail))
			return false;
		if (registerDate == null) {
			if (other.registerDate != null)
				return false;
		} else if (!registerDate.equals(other.registerDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (unsuccessfulAttempts == null) {
			if (other.unsuccessfulAttempts != null)
				return false;
		} else if (!unsuccessfulAttempts.equals(other.unsuccessfulAttempts))
			return false;
		if (userType == null) {
			if (other.userType != null)
				return false;
		} else if (!userType.equals(other.userType))
			return false;
		return true;
	}
	
	public void copyFrom(BFSUser user){
		this.userType = user.getUserType();
		this.previousEmail = user.getPreviousEmail();
		this.registerDate = user.getRegisterDate();
		this.unsuccessfulAttempts = user.getUnsuccessfulAttempts();
		if(this.status == null || this.status.isEmpty()){
			this.status = user.getStatus();
		}
	}

}
