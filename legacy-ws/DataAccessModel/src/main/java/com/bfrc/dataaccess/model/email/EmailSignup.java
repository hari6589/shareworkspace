package com.bfrc.dataaccess.model.email;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import app.bsro.model.util.DateUtils;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class EmailSignup implements Serializable{
	
   
	private static final long serialVersionUID = 1L;
	private Long signupId;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String emailAddress;
    private Date createdDate;
    private String source;
    private String optinCode;
	private String confirmOptin;
    private String couponId;
    private String friendsEmail;
    private String phoneNumber;
    private String actionCode;
    private String accessCode;
    private Date modifiedDate;
    
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getSignupId() {
		return signupId;
	}
	public void setSignupId(Long signupId) {
		this.signupId = signupId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
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
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@JsonSerialize(using = DateUtils.class)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getOptinCode() {
		return optinCode;
	}
	public void setOptinCode(String optinCode) {
		this.optinCode = optinCode;
	}
	public String getConfirmOptin() {
		return confirmOptin;
	}
	public void setConfirmOptin(String confirmOptin) {
		this.confirmOptin = confirmOptin;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getFriendsEmail() {
		return friendsEmail;
	}
	public void setFriendsEmail(String friendsEmail) {
		this.friendsEmail = friendsEmail;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((signupId == null) ? 0 : signupId.hashCode());
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
		EmailSignup other = (EmailSignup) obj;
		if (signupId == null) {
			if (other.signupId != null)
				return false;
		} else if (!signupId.equals(other.signupId))
			return false;
		return true;
	}

    
}
