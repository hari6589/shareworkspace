package com.bridgestone.bsro.aem.voice.util;

import javax.servlet.http.HttpServletRequest;

public class VoiceRequest {
	
	
	private  String storeNumber;
	private String locationId;
	private String employeeId;
	private String appointmentStatusId;
	private String appointmentStatusDesc;
	private String vehicleYear;
	private String vehicleMake;
	private String vehicleModel;
	private String vehicleSubmodel;
	private String mileage;
	private String customerNotes;
	private String comments;
	private String customerFirstName;
	private String customerLastName;
	private String customerCity;
	private String customerDayTimePhone;
	private String customerEmailAddress;
	private String websiteName;
	private String appointmentType;
	private String quoteId;
	private Choice choice;
	private String selectedServices;
	
	public VoiceRequest(HttpServletRequest request, Choice choice, String selectedServices){
		this.storeNumber = request.getParameter("storeNumber");
		this.locationId = request.getParameter("locationId");
		this.employeeId = request.getParameter("employeeId");
		this.appointmentStatusId = request.getParameter("appointmentStatusId");
		this.appointmentStatusDesc = request.getParameter("appointmentStatusDesc");
		this.vehicleYear = request.getParameter("vehicleYear");
		this.vehicleMake = request.getParameter("vehicleMake");
		this.vehicleModel = request.getParameter("vehicleModel");
		this.vehicleSubmodel = request.getParameter("vehicleSubmodel");
		this.mileage = request.getParameter("mileage");
		this.customerNotes = request.getParameter("customerNotes");
		this.comments = request.getParameter("comments");
		this.customerFirstName = request.getParameter("customerFirstName");
		this.customerLastName = request.getParameter("customerLastName");
		this.customerCity = request.getParameter("customerCity");
		this.customerDayTimePhone = request.getParameter("customerDayTimePhone");
		this.customerEmailAddress = request.getParameter("customerEmailAddress");
		this.websiteName = request.getParameter("websiteName");
		this.appointmentType = request.getParameter("appointmentType");
		this.quoteId = request.getParameter("quoteId");
		this.choice = choice;
		this.selectedServices = selectedServices;
		
	}

	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(String appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	public String getAppointmentStatusDesc() {
		return appointmentStatusDesc;
	}

	public void setAppointmentStatusDesc(String appointmentStatusDesc) {
		this.appointmentStatusDesc = appointmentStatusDesc;
	}

	public String getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(String vehicleYear) {
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

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getCustomerNotes() {
		return customerNotes;
	}

	public void setCustomerNotes(String customerNotes) {
		this.customerNotes = customerNotes;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public String getCustomerDayTimePhone() {
		return customerDayTimePhone;
	}

	public void setCustomerDayTimePhone(String customerDayTimePhone) {
		this.customerDayTimePhone = customerDayTimePhone;
	}

	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public Choice getChoice() {
		return choice;
	}

	public void setChoice(Choice choice) {
		this.choice = choice;
	}

	public String getSelectedServices() {
		return selectedServices;
	}

	public void setSelectedServices(String selectedServices) {
		this.selectedServices = selectedServices;
	}
	
	
	
}
