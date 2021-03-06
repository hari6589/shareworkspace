/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Oct 28 09:57:01 CDT 2005 by MyEclipse Hibernate Tool.
 */
package com.bfrc.pojo.appointment;

import com.bfrc.Bean;
import com.bfrc.framework.util.StringUtils;

/**
 * A class that represents a row in the APPOINTMENT table. 
 * You can customize the behavior of this class by editing the class, {@link EmailSignup()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public class AppointmentCustomer implements Bean
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.Long appointmentCustomerId;
    
    /** The composite primary key value. */
    private java.lang.Long appointmentId;

    /** The value of the simple firstName property. */
    private java.lang.String firstName;

    /** The value of the simple lastName property. */
    private java.lang.String lastName;

    /** The value of the simple address1 property. */
    private java.lang.String address1;

    /** The value of the simple address2 property. */
    private java.lang.String address2;

    /** The value of the simple city property. */
    private java.lang.String city;

    /** The value of the simple state property. */
    private java.lang.String state;

    /** The value of the simple zip property. */
    private java.lang.String zip;

    /** The value of the simple daytimePhone property. */
    private java.lang.String daytimePhone;

    /** The value of the simple eveningPhone property. */
    private java.lang.String eveningPhone;

    /** The value of the simple cellPhone property. */
    private java.lang.String cellPhone;

    /** The value of the simple email property. */
    private java.lang.String emailAddress;

    /** The value of the simple emailSignup property. */
    private java.lang.String emailSignup;

    /** The value of the simple webSite property. */
    private java.lang.String webSite;

    /** The value of the simple createdDate property. */
    private java.util.Date createdDate;
    
    /**
     * Simple constructor of AbstractEmailSignup instances.
     */
    public AppointmentCustomer() {
    }

	/**
	 * @return Returns the address1.
	 */
	public java.lang.String getAddress1() {
		return StringUtils.truncate(this.address1,50);
	}
	/**
	 * @param address1 The address1 to set.
	 */
	public void setAddress1(java.lang.String address1) {
		this.address1 = address1;
	}
	/**
	 * @return Returns the address2.
	 */
	public java.lang.String getAddress2() {
		return StringUtils.truncate(this.address2,50);
	}
	/**
	 * @param address2 The address2 to set.
	 */
	public void setAddress2(java.lang.String address2) {
		this.address2 = address2;
	}
	/**
	 * @return Returns the appointmentCustomerId.
	 */
	public java.lang.Long getAppointmentCustomerId() {
		return this.appointmentCustomerId;
	}
	/**
	 * @param appointmentCustomerId The appointmentCustomerId to set.
	 */
	public void setAppointmentCustomerId(java.lang.Long appointmentCustomerId) {
		this.appointmentCustomerId = appointmentCustomerId;
	}
	/**
	 * @return Returns the appointmentId.
	 */
	public java.lang.Long getAppointmentId() {
		return this.appointmentId;
	}
	/**
	 * @param appointmentId The appointmentId to set.
	 */
	public void setAppointmentId(java.lang.Long appointmentId) {
		this.appointmentId = appointmentId;
	}
	/**
	 * @return Returns the cellPhone.
	 */
	public java.lang.String getCellPhone() {
		return StringUtils.truncate(this.cellPhone,25);
	}
	/**
	 * @param cellPhone The cellPhone to set.
	 */
	public void setCellPhone(java.lang.String cellPhone) {
		this.cellPhone = cellPhone;
	}
	/**
	 * @return Returns the city.
	 */
	public java.lang.String getCity() {
		return StringUtils.truncate(this.city,50);
	}
	/**
	 * @param city The city to set.
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	/**
	 * @return Returns the createdDate.
	 */
	public java.util.Date getCreatedDate() {
		return this.createdDate;
	}
	/**
	 * @param createdDate The createdDate to set.
	 */
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return Returns the daytimePhone.
	 */
	public java.lang.String getDaytimePhone() {
		return this.daytimePhone;
	}
	/**
	 * @param daytimePhone The daytimePhone to set.
	 */
	public void setDaytimePhone(java.lang.String daytimePhone) {
		this.daytimePhone = daytimePhone;
	}
	/**
	 * @return Returns the emailAddress.
	 */
	public java.lang.String getEmailAddress() {
		return StringUtils.truncate(this.emailAddress,255);
	}
	/**
	 * @param emailAddress The emailAddress to set.
	 */
	public void setEmailAddress(java.lang.String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return Returns the emailSignup.
	 */
	public java.lang.String getEmailSignup() {
		return this.emailSignup;
	}
	/**
	 * @param emailSignup The emailSignup to set.
	 */
	public void setEmailSignup(java.lang.String emailSignup) {
		this.emailSignup = emailSignup;
	}
	/**
	 * @return Returns the eveningPhone.
	 */
	public java.lang.String getEveningPhone() {
		return this.eveningPhone;
	}
	/**
	 * @param eveningPhone The eveningPhone to set.
	 */
	public void setEveningPhone(java.lang.String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}
	/**
	 * @return Returns the firstName.
	 */
	public java.lang.String getFirstName() {
		return StringUtils.userNameFilter(this.firstName,50);
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the hashValue.
	 */
	public int getHashValue() {
		return this.hashValue;
	}
	/**
	 * @param hashValue The hashValue to set.
	 */
	public void setHashValue(int hashValue) {
		this.hashValue = hashValue;
	}
	/**
	 * @return Returns the lastName.
	 */
	public java.lang.String getLastName() {
		return StringUtils.userNameFilter(this.lastName,80);
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the state.
	 */
	public java.lang.String getState() {
		return StringUtils.truncate(this.state,2);
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(java.lang.String state) {
		this.state = state;
	}
	/**
	 * @return Returns the zip.
	 */
	public java.lang.String getZip() {
		return StringUtils.truncate(this.zip,10);
	}
	/**
	 * @param zip The zip to set.
	 */
	public void setZip(java.lang.String zip) {
		this.zip = zip;
	}

	public java.lang.String getWebSite() {
		return webSite;
	}
	
	public void setWebSite(java.lang.String webSite) {
		this.webSite = webSite;
	}
	
}
