package com.bsro.pojo.form;

import java.io.Serializable;

import com.bfrc.pojo.UserVehicle;

public class ContactUsForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nature;
	private String firstName;
	private String lastName;
	private String address, address2, city, state, zip;
	private String dayPhone0, dayPhone1, dayPhone2;
	private String eveningPhone0, eveningPhone1, eveningPhone2;
	private String cellPhone0, cellPhone1, cellPhone2;
	private String emailAddress, confirmEmailAddress;
	private Long storeNumber;
	private String message, comments;
	private String storeState;
	private String storeCity;
	private UserVehicle vehicle;
	//ETIRE phone fields
	private String phone0, phone1, phone2;
	
	public String getPhone() {
		if(phone0 != null && phone1 != null && phone2 != null)
			return phone0+"-"+phone1+"-"+phone2;
		return "";
	}
	
	public UserVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(UserVehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getDayPhone() {
		if(dayPhone0 != null && dayPhone1 != null && dayPhone2 != null)
			return dayPhone0+"-"+dayPhone1+"-"+dayPhone2;
		return "";
	}
	
	public String getEveningPhone() {
		if(eveningPhone0 != null && eveningPhone1 != null && eveningPhone2 != null)
			return eveningPhone0+"-"+eveningPhone1+"-"+eveningPhone2;
		return "";
	}

	public String getCellPhone() {
		if(cellPhone0 != null && cellPhone1 != null && cellPhone2 != null)
			return cellPhone0+"-"+cellPhone1+"-"+cellPhone2;
		return "";
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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

	public String getDayPhone0() {
		return dayPhone0;
	}

	public void setDayPhone0(String dayPhone0) {
		this.dayPhone0 = dayPhone0;
	}

	public String getDayPhone1() {
		return dayPhone1;
	}

	public void setDayPhone1(String dayPhone1) {
		this.dayPhone1 = dayPhone1;
	}

	public String getDayPhone2() {
		return dayPhone2;
	}

	public void setDayPhone2(String dayPhone2) {
		this.dayPhone2 = dayPhone2;
	}

	public String getEveningPhone0() {
		return eveningPhone0;
	}

	public void setEveningPhone0(String eveningPhone0) {
		this.eveningPhone0 = eveningPhone0;
	}

	public String getEveningPhone1() {
		return eveningPhone1;
	}

	public void setEveningPhone1(String eveningPhone1) {
		this.eveningPhone1 = eveningPhone1;
	}

	public String getEveningPhone2() {
		return eveningPhone2;
	}

	public void setEveningPhone2(String eveningPhone2) {
		this.eveningPhone2 = eveningPhone2;
	}

	public String getCellPhone0() {
		return cellPhone0;
	}

	public void setCellPhone0(String cellPhone0) {
		this.cellPhone0 = cellPhone0;
	}

	public String getCellPhone1() {
		return cellPhone1;
	}

	public void setCellPhone1(String cellPhone1) {
		this.cellPhone1 = cellPhone1;
	}

	public String getCellPhone2() {
		return cellPhone2;
	}

	public void setCellPhone2(String cellPhone2) {
		this.cellPhone2 = cellPhone2;
	}

	public String getConfirmEmailAddress() {
		return confirmEmailAddress;
	}

	public void setConfirmEmailAddress(String confirmEmailAddress) {
		this.confirmEmailAddress = confirmEmailAddress;
	}

	public String getPhone0() {
		return phone0;
	}

	public void setPhone0(String phone0) {
		this.phone0 = phone0;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	

}
