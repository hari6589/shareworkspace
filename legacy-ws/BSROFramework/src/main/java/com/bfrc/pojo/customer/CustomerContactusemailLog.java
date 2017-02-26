package com.bfrc.pojo.customer;
// Generated Mar 3, 2010 11:47:07 AM by Hibernate Tools 3.2.1.GA

//-- add server validations for phone numbers 04/23/2012--//
import java.util.Date;

import com.bfrc.framework.util.StringUtils;

/**
 * CustomerContactusemailLog generated by hbm2java
 */
public class CustomerContactusemailLog  implements java.io.Serializable {


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

    public CustomerContactusemailLog() {
    }

    public CustomerContactusemailLog(long contactusId) {
        this.contactusId = contactusId;
    }
    public CustomerContactusemailLog(long contactusId, String emailFrom, String emailTo, String emailCc, String emailBcc, Integer feedbackId, String companyName, String emailAddress, String firstName, String lastName, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String daytimePhone, String eveningPhone, String mobilePhone, Short vehicleYear, String vehicleMake, String vehicleModel, String vehicleSubmodel, Long storeNumber, String storeName, String storeAddress, String storeCity, String storeState, String storeZip, String comments, Integer siteId, String userAgentInfo, Date creationDate) {
       this.contactusId = contactusId;
       this.emailFrom = emailFrom;
       this.emailTo = emailTo;
       this.emailCc = emailCc;
       this.emailBcc = emailBcc;
       this.feedbackId = feedbackId;
       this.companyName = companyName;
       this.emailAddress = emailAddress;
       this.firstName = firstName;
       this.lastName = lastName;
       this.streetAddress1 = streetAddress1;
       this.streetAddress2 = streetAddress2;
       this.city = city;
       this.state = state;
       this.zipCode = zipCode;
       this.daytimePhone = daytimePhone;
       this.eveningPhone = eveningPhone;
       this.mobilePhone = mobilePhone;
       this.vehicleYear = vehicleYear;
       this.vehicleMake = vehicleMake;
       this.vehicleModel = vehicleModel;
       this.vehicleSubmodel = vehicleSubmodel;
       this.storeNumber = storeNumber;
       this.storeName = storeName;
       this.storeAddress = storeAddress;
       this.storeCity = storeCity;
       this.storeState = storeState;
       this.storeZip = storeZip;
       this.comments = comments;
       this.siteId = siteId;
       this.userAgentInfo = userAgentInfo;
       this.creationDate = creationDate;
    }
   
    public long getContactusId() {
        return this.contactusId;
    }
    
    public void setContactusId(long contactusId) {
        this.contactusId = contactusId;
    }
    public String getEmailFrom() {
        return this.emailFrom;
    }
    
    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
    public String getEmailTo() {
        return this.emailTo;
    }
    
    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
    public String getEmailCc() {
        return this.emailCc;
    }
    
    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }
    public String getEmailBcc() {
        return this.emailBcc;
    }
    
    public void setEmailBcc(String emailBcc) {
        this.emailBcc = emailBcc;
    }
    public Integer getFeedbackId() {
        return this.feedbackId;
    }
    
    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }
    public String getCompanyName() {
        return StringUtils.userNameFilter(this.companyName,250);
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getEmailAddress() {
        return this.emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public String getFirstName() {
    return StringUtils.userNameFilter(this.firstName,50);
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return StringUtils.userNameFilter(this.lastName,50);
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getStreetAddress1() {
        return StringUtils.userNameFilter(this.streetAddress1,100);
    }
    
    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }
    public String getStreetAddress2() {
        return StringUtils.truncate(this.streetAddress2,100);
    }
    
    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }
    public String getCity() {
        return StringUtils.truncate(this.city,50);
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return StringUtils.truncate(this.state,2);
    }
    
    public void setState(String state) {
        this.state = state;
    }
    public String getZipCode() {
        return StringUtils.truncate(this.zipCode,10);
    }
    
    public void setZipCode(String zipCode) {
    if(zipCode != null && zipCode.length() > 10)
    zipCode = zipCode.substring(0,10);
        this.zipCode = zipCode;
    }
    public String getDaytimePhone() {
    return StringUtils.truncate(this.daytimePhone,20);
    }
    
    public void setDaytimePhone(String daytimePhone) {
        this.daytimePhone = daytimePhone;
    }
    public String getEveningPhone() {
    return StringUtils.truncate(this.eveningPhone,20);
    }
    
    public void setEveningPhone(String eveningPhone) {
        this.eveningPhone = eveningPhone;
    }
    public String getMobilePhone() {
    return StringUtils.truncate(this.mobilePhone,20);
    }
    
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    public Short getVehicleYear() {
        return this.vehicleYear;
    }
    
    public void setVehicleYear(Short vehicleYear) {
        this.vehicleYear = vehicleYear;
    }
    public String getVehicleMake() {
        return this.vehicleMake;
    }
    
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }
    public String getVehicleModel() {
        return this.vehicleModel;
    }
    
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
    public String getVehicleSubmodel() {
        return this.vehicleSubmodel;
    }
    
    public void setVehicleSubmodel(String vehicleSubmodel) {
        this.vehicleSubmodel = vehicleSubmodel;
    }
    public Long getStoreNumber() {
        return this.storeNumber;
    }
    
    public void setStoreNumber(Long storeNumber) {
        this.storeNumber = storeNumber;
    }
    public String getStoreName() {
        return this.storeName;
    }
    
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getStoreAddress() {
        return this.storeAddress;
    }
    
    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
    public String getStoreCity() {
        return this.storeCity;
    }
    
    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }
    public String getStoreState() {
        return this.storeState;
    }
    
    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }
    public String getStoreZip() {
        return this.storeZip;
    }
    
    public void setStoreZip(String storeZip) {
        this.storeZip = storeZip;
    }
    public String getComments() {
    return StringUtils.truncate(this.comments,4000);
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Integer getSiteId() {
        return this.siteId;
    }
    
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
    public String getUserAgentInfo() {
        return this.userAgentInfo;
    }
    
    public void setUserAgentInfo(String userAgentInfo) {
        this.userAgentInfo = userAgentInfo;
    }
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }




}
