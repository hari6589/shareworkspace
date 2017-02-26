package com.bfrc.dataaccess.model.customer;

import java.util.Date;

public class CustomerContactUsEmailLog {
	
    private long contactusId;
    private String emailFrom;
    private String emailTo;
    private String emailCc;
    private String emailBcc;
    private Integer feedbackId;
    private String companyName;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String state;
    private String zipCode;
    private String daytimePhone;
    private String eveningPhone;
    private String mobilePhone;
    private Short vehicleYear;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleSubmodel;
    private Long storeNumber;
    private String storeName;
    private String storeAddress;
    private String storeCity;
    private String storeState;
    private String storeZip;
    private String comments;
    private Integer siteId;
    private String userAgentInfo;
    private Date creationDate;
	public long getContactusId() {
		return contactusId;
	}
	public void setContactusId(long contactusId) {
		this.contactusId = contactusId;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
	public String getEmailBcc() {
		return emailBcc;
	}
	public void setEmailBcc(String emailBcc) {
		this.emailBcc = emailBcc;
	}
	public Integer getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStreetAddress1() {
		return streetAddress1;
	}
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}
	public String getStreetAddress2() {
		return streetAddress2;
	}
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getDaytimePhone() {
		return daytimePhone;
	}
	public void setDaytimePhone(String daytimePhone) {
		this.daytimePhone = daytimePhone;
	}
	public String getEveningPhone() {
		return eveningPhone;
	}
	public void setEveningPhone(String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public Short getVehicleYear() {
		return vehicleYear;
	}
	public void setVehicleYear(Short vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
	public String getVehicleMake() {
		return vehicleMake;
	}
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getVehicleSubmodel() {
		return vehicleSubmodel;
	}
	public void setVehicleSubmodel(String vehicleSubmodel) {
		this.vehicleSubmodel = vehicleSubmodel;
	}
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreCity() {
		return storeCity;
	}
	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	public String getStoreState() {
		return storeState;
	}
	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}
	public String getStoreZip() {
		return storeZip;
	}
	public void setStoreZip(String storeZip) {
		this.storeZip = storeZip;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getUserAgentInfo() {
		return userAgentInfo;
	}
	public void setUserAgentInfo(String userAgentInfo) {
		this.userAgentInfo = userAgentInfo;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@Override
	public String toString() {
		return "CustomerContactUsEmailLog [contactusId=" + contactusId
				+ ", emailFrom=" + emailFrom + ", emailTo=" + emailTo
				+ ", emailCc=" + emailCc + ", emailBcc=" + emailBcc
				+ ", feedbackId=" + feedbackId + ", companyName=" + companyName
				+ ", emailAddress=" + emailAddress + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", streetAddress1="
				+ streetAddress1 + ", streetAddress2=" + streetAddress2
				+ ", city=" + city + ", state=" + state + ", zipCode="
				+ zipCode + ", daytimePhone=" + daytimePhone
				+ ", eveningPhone=" + eveningPhone + ", mobilePhone="
				+ mobilePhone + ", vehicleYear=" + vehicleYear
				+ ", vehicleMake=" + vehicleMake + ", vehicleModel="
				+ vehicleModel + ", vehicleSubmodel=" + vehicleSubmodel
				+ ", storeNumber=" + storeNumber + ", storeName=" + storeName
				+ ", storeAddress=" + storeAddress + ", storeCity=" + storeCity
				+ ", storeState=" + storeState + ", storeZip=" + storeZip
				+ ", comments=" + comments + ", siteId=" + siteId
				+ ", userAgentInfo=" + userAgentInfo + ", creationDate="
				+ creationDate + "]";
	}
	
}
