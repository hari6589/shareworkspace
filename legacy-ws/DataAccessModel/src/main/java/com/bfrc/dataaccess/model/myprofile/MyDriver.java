/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * @author schowdhu
 *
 */
@JsonIgnoreProperties({"user", "picture", "stores", "vehicleDrivers"})
@JsonPropertyOrder({"id", "emailAddress", "lastModifiedDate", "firstName", "middleInitial", "lastName"})
public class MyDriver  {
	
	private Long userId;
	
	private BFSUser user;
	
	private String emailReminders;
	
	private String emailAddress;

	private String preferredContactMethod;

	private String firstName;

	private String lastName;
	
	private String middleInitial;

	private String secondaryEmail;

	private String homePhone;

	private String cellPhone;

	private String workPhone;

	private String workPhoneExtn;

	private String address;

	private String city;
	
	private String state;
	
	private String zip;
	
	private String countryCode;
	
	private String notes;
	
	private String activeFlag;
	
	private Date lastModifiedDate;
	

	private Set<MyVehicle> vehicles = new HashSet<MyVehicle>();
	

	private Set<MyStore> stores = new HashSet<MyStore>();
	
	/**
	 * 
	 */
	public MyDriver() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BFSUser getUser() {
		return user;
	}

	public void setUser(BFSUser user) {
		this.user = user;
	}

	/**
	 * @return the emailReminders
	 */
	public String getEmailReminders() {
		return emailReminders;
	}
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @param emailReminders the emailReminders to set
	 */
	public void setEmailReminders(String emailReminders) {
		this.emailReminders = emailReminders;
	}
	/**
	 * @return the preferredContactMethod
	 */
	public String getPreferredContactMethod() {
		return preferredContactMethod;
	}
	/**
	 * @param preferredContactMethod the preferredContactMethod to set
	 */
	public void setPreferredContactMethod(String preferredContactMethod) {
		this.preferredContactMethod = preferredContactMethod;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleInitial
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}

	/**
	 * @param middleInitial the middleInitial to set
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	/**
	 * @return the secondaryEmail
	 */
	public String getSecondaryEmail() {
		return secondaryEmail;
	}
	/**
	 * @param secondaryEmail the secondaryEmail to set
	 */
	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}
	/**
	 * @return the homePhone
	 */
	public String getHomePhone() {
		return homePhone;
	}
	/**
	 * @param homePhone the homePhone to set
	 */
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}
	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	/**
	 * @return the workPhone
	 */
	public String getWorkPhone() {
		return workPhone;
	}
	/**
	 * @param workPhone the workPhone to set
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	/**
	 * @return the workPhoneExtn
	 */
	public String getWorkPhoneExtn() {
		return workPhoneExtn;
	}
	/**
	 * @param workPhoneExtn the workPhoneExtn to set
	 */
	public void setWorkPhoneExtn(String workPhoneExtn) {
		this.workPhoneExtn = workPhoneExtn;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the activeFlag
	 */
	public String getActiveFlag() {
		return activeFlag;
	}

	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
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
	
//	
//	/**
//	 * @return the vehicleDrivers
//	 */
//	public Set<MyVehicleDriver> getVehicleDrivers() {
//		return vehicleDrivers;
//	}
//
//	/**
//	 * @param vehicleDrivers the vehicles to set
//	 */
//	public void setVehicleDrivers(Set<MyVehicleDriver> vehicles) {
//		this.vehicleDrivers = vehicles;
//	}

	/**
	 * @return the vehicles
	 */
	@JsonIgnore
	public Set<MyVehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * @param vehicles the vehicles to set
	 */
	public void setVehicles(Set<MyVehicle> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * @return the stores
	 */
	@JsonIgnore
	public Set<MyStore> getStores() {
		return stores;
	}

	/**
	 * @param stores the stores to set
	 */
	public void setStores(Set<MyStore> stores) {
		this.stores = stores;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyDriver [userId=" + userId
				+ ", emailReminders=" + emailReminders
				+ ", preferredContactMethod=" + preferredContactMethod
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", middleInitial=" + middleInitial 
				+ ", secondaryEmail=" + secondaryEmail
				+ ", homePhone=" + homePhone + ", cellPhone=" + cellPhone
				+ ", workPhone=" + workPhone + ", workPhoneExtn="
				+ workPhoneExtn + ", address=" + address + ", city=" + city
				+ ", state=" + state + ", zip=" + zip + ", countryCode="
				+ countryCode + ", notes=" + notes + ", activeFlag="
				+ activeFlag + ", lastModifiedDate=" + lastModifiedDate
				+ ", stores=" + stores + "]";
	}


	public void copyFrom(MyDriver driver) {
		this.emailReminders = driver.getEmailReminders();
		this.preferredContactMethod = driver.getPreferredContactMethod();
		this.firstName = driver.getFirstName();
		this.lastName = driver.getLastName();
		this.middleInitial = driver.getMiddleInitial();
		this.secondaryEmail = driver.getSecondaryEmail();
		this.homePhone = driver.getHomePhone();
		this.cellPhone = driver.getCellPhone();
		this.workPhone = driver.getWorkPhone();
		this.workPhoneExtn = driver.getWorkPhoneExtn();
		this.address = driver.getAddress();
		this.city = driver.getCity();
		this.state = driver.getState();
		this.zip = driver.getZip();
		this.countryCode = driver.getCountryCode();
		this.notes = driver.getNotes();
		this.activeFlag = driver.getActiveFlag();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		MyDriver other = (MyDriver) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}
}
