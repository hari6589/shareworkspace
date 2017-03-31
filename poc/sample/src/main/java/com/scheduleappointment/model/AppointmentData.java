package com.scheduleappointment.model;

public class AppointmentData {
	private Long storeNumber;
	private Long locationId;
	private String employeeId;
	private Long quoteId;
	private String appointmentStatusId;
	private String appointmentStatusDesc;
	private Long vehicleYear;
	private String vehicleMake;
	private String vehicleModel;
	private String vehicleSubmodel;
	private int mileage;
	private String customerFirstName;
	private String customerLastName;
	private String customerDayTimePhone;
	private String customerEmailAddress;
	private String customerNotes;
	private String comments;
	private String dropWaitOption;
	private String websiteName;
	private String appointmentType;
	private String selectedServices;
	private String eCommRefNumber;
	private AppointmentChoice choice;
	
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Long getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
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
	public String getDropWaitOption() {
		return dropWaitOption;
	}
	public void setDropWaitOption(String dropWaitOption) {
		this.dropWaitOption = dropWaitOption;
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
	public AppointmentChoice getChoice() {
		return choice;
	}
	public void setChoice(AppointmentChoice choice) {
		this.choice = choice;
	}
	public String getSelectedServices() {
		return selectedServices;
	}
	public void setSelectedServices(String selectedServices) {
		this.selectedServices = selectedServices;
	}
	public void seteCommRefNumber(String eCommRefNumber) {
		this.eCommRefNumber = eCommRefNumber;
	}
	public String geteCommRefNumber() {
		return this.eCommRefNumber;
	}
	@Override
	public String toString() {
		return "AppointmentData [storeNumber=" + storeNumber + ", locationId="
				+ locationId + ", employeeId=" + employeeId + ", quoteId="
				+ quoteId + ", appointmentStatusId=" + appointmentStatusId
				+ ", appointmentStatusDesc=" + appointmentStatusDesc
				+ ", vehicleYear=" + vehicleYear + ", vehicleMake="
				+ vehicleMake + ", vehicleModel=" + vehicleModel
				+ ", vehicleSubmodel=" + vehicleSubmodel + ", mileage="
				+ mileage + ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName
				+ ", customerDayTimePhone=" + customerDayTimePhone
				+ ", customerEmailAddress=" + customerEmailAddress
				+ ", customerNotes=" + customerNotes + ", comments=" + comments
				+ ", dropWaitOption=" + dropWaitOption + ", websiteName="
				+ websiteName + ", appointmentType=" + appointmentType
				+ ", selectedServices=" + selectedServices
				+ ", eCommRefNumber=" + eCommRefNumber + ", choice=" + choice
				+ "]";
	}
	
}
