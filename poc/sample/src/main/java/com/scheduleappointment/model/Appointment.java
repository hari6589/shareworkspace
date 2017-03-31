package com.scheduleappointment.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Appointment")
public class Appointment {
	private Long appointmentId;
	private Long customerId;
	private Long storeNumber;
	private Long vehicleYear;
	private String vehicleMake;
	private String vehicleModel;
	private String vehicleSubmodel;
	private int mileage;
	private String comments;
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
	private String createdDate;
	private String webSite;
	private String webSiteSource;
	private Long batteryQuoteId;
	private String phoneReminderInd;
	private String emailReminderInd;
	private String ecommRefNumber;
	private int choice;
	private String dateTime;
	private String dropWaitOption;
	private String pickupTime;
	private String dropOffTime;
	private String appointmentServices;
	private String appointmentServicesDesc;
	private Long employeeId;
	private Long roomId;
	private Long locationId;
	private Long appointmentStatusId;
	private String otherDetails;
	private String status;
	private String updateDate; // required?
	private String bookingConfirmationId;
	private String emailStatusMessage;
	private String emailTrackingNumber;
	
	@DynamoDBHashKey(attributeName="appointmentId")
	public Long getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	public Long getVehicleYear() {
		return vehicleYear;
	}
	public void setVehicleYear(Long vehicleYear) {
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
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getWebSiteSource() {
		return webSiteSource;
	}
	public void setWebSiteSource(String webSiteSource) {
		this.webSiteSource = webSiteSource;
	}
	public Long getBatteryQuoteId() {
		return batteryQuoteId;
	}
	public void setBatteryQuoteId(Long batteryQuoteId) {
		this.batteryQuoteId = batteryQuoteId;
	}
	public String getPhoneReminderInd() {
		return phoneReminderInd;
	}
	public void setPhoneReminderInd(String phoneReminderInd) {
		this.phoneReminderInd = phoneReminderInd;
	}
	public String getEmailReminderInd() {
		return emailReminderInd;
	}
	public void setEmailReminderInd(String emailReminderInd) {
		this.emailReminderInd = emailReminderInd;
	}
	public String getEcommRefNumber() {
		return ecommRefNumber;
	}
	public void setEcommRefNumber(String ecommRefNumber) {
		this.ecommRefNumber = ecommRefNumber;
	}
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getDropWaitOption() {
		return dropWaitOption;
	}
	public void setDropWaitOption(String dropWaitOption) {
		this.dropWaitOption = dropWaitOption;
	}
	public String getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	public String getDropOffTime() {
		return dropOffTime;
	}
	public void setDropOffTime(String dropOffTime) {
		this.dropOffTime = dropOffTime;
	}
	public String getAppointmentServices() {
		return appointmentServices;
	}
	public void setAppointmentServices(String appointmentServices) {
		this.appointmentServices = appointmentServices;
	}
	public String getAppointmentServicesDesc() {
		return appointmentServicesDesc;
	}
	public void setAppointmentServicesDesc(String appointmentServicesDesc) {
		this.appointmentServicesDesc = appointmentServicesDesc;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public Long getAppointmentStatusId() {
		return appointmentStatusId;
	}
	public void setAppointmentStatusId(Long appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getBookingConfirmationId() {
		return bookingConfirmationId;
	}
	public void setBookingConfirmationId(String bookingConfirmationId) {
		this.bookingConfirmationId = bookingConfirmationId;
	}
	public String getEmailStatusMessage() {
		return emailStatusMessage;
	}
	public void setEmailStatusMessage(String emailStatusMessage) {
		this.emailStatusMessage = emailStatusMessage;
	}
	public String getEmailTrackingNumber() {
		return emailTrackingNumber;
	}
	public void setEmailTrackingNumber(String emailTrackingNumber) {
		this.emailTrackingNumber = emailTrackingNumber;
	}	
}
