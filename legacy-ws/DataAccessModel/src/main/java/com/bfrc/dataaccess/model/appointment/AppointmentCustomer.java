/**
 * 
 */
package com.bfrc.dataaccess.model.appointment;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smoorthy
 *
 */
public class AppointmentCustomer implements Serializable {
	
	private static final long serialVersionUID = 2058349727284002603L;
	
	private Long appointmentCustomerId;
	private Long appointmentId;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String daytimePhone;
	private String eveningPhone;
	private String cellPhone;
	private String emailAddress;
	private String emailSignup;
	private String webSite;
	private Date createdDate;
	
	public AppointmentCustomer() {
	}

	public Long getAppointmentCustomerId() {
		return appointmentCustomerId;
	}

	public void setAppointmentCustomerId(Long appointmentCustomerId) {
		this.appointmentCustomerId = appointmentCustomerId;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
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

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailSignup() {
		return emailSignup;
	}

	public void setEmailSignup(String emailSignup) {
		this.emailSignup = emailSignup;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "AppointmentCustomer [appointmentCustomerId="
				+ appointmentCustomerId + ", appointmentId=" + appointmentId
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", daytimePhone=" + daytimePhone + ", eveningPhone="
				+ eveningPhone + ", cellPhone=" + cellPhone + ", emailAddress="
				+ emailAddress + ", emailSignup=" + emailSignup + ", webSite="
				+ webSite + ", createdDate=" + createdDate + "]";
	}
	
}
