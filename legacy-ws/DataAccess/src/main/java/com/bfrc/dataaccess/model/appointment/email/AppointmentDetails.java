/**
 * 
 */
package com.bfrc.dataaccess.model.appointment.email;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author schowdhu
 *
 */
public class AppointmentDetails {

	@JsonProperty("TRANSACTIONID")
	private String transactionId;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("APPOINTMENT_ID")
	private String appointmentId;
	
	@JsonProperty("STORE_NUMBER")
	private String storeNumber;
	
	@JsonProperty("STORE_NAME")
	private String storeName;
	
	@JsonProperty("STORE_ADDRESS")
	private String storeAddress;
	
	@JsonProperty("STORE_CITY")
	private String storeCity;
	
	@JsonProperty("STORE_STATE")
	private String storeState;
	
	@JsonProperty("STORE_ZIP")
	private String storeZip;
	
	@JsonProperty("STORE_PHONE")
	private String storePhone;
	
	@JsonProperty("STORE_FAX")
	private String storeFax;
	
	private String storeEmail;
	
	private String storeManagerFirstName;
	
	private String storeManagerLastName;
	
	@JsonProperty("VEHICLE_YEAR")
	private String vehicleYear;
	
	@JsonProperty("VEHICLE_MAKE")
	private String vehicleMake;
	
	@JsonProperty("VEHICLE_MODEL")
	private String vehicleModel;
	
	@JsonProperty("VEHICLE_SUBMODEL")
	private String vehicleSubmodel;
	
	@JsonProperty("MILEAGE")
	private String mileage;
	
	@JsonProperty("COMMENTS")
	private String comments;
	
	@JsonProperty("CUSTOMER_FIRST_NAME")
	private String customerFirstName;
	
	@JsonProperty("CUSTOMER_LAST_NAME")
	private String customerLastName;
	
	@JsonProperty("CUSTOMER_ADDRESS1")
	private String customerAddress1;
	
	@JsonProperty("CUSTOMER_ADDRESS2")
	private String customerAddress2;
	
	@JsonProperty("CUSTOMER_CITY")
	private String customerCity;
	
	@JsonProperty("CUSTOMER_STATE")
	private String customerState;
	
	@JsonProperty("CUSTOMER_ZIP_CODE")
	private String customerZipCode;
	
	@JsonProperty("CUSTOMER_DAYTIME_PHONE")
	private String customerDayTimePhone;
	
	@JsonProperty("CUSTOMER_EVENING_PHONE")
	private String customerEveningPhone;
	
	@JsonProperty("CUSTOMER_CELL_PHONE")
	private String customerCellPhone;
	
	@JsonProperty("CUSTOMER_EMAIL_ADDRESS")
	private String customerEmailAddress;
	
	@JsonProperty("CREATED_DATE")
	private String createdDate;
	
	@JsonProperty("WEBSITE_NAME")
	private String websiteName;
	
	@JsonProperty("APPOINTMENT_DATETIME")
	private String appointmentDateTime;
	
	@JsonProperty("PICKUP_DATETIME")
	private String pickUpDateTime;
	
	@JsonProperty("DROPOFF_DATETIME")
	private String dropOffDateTime;
	
	@JsonProperty("DROP_WAIT_OPTION")
	private String dropWaitOption;
	
	@JsonProperty("APPOINTMENTTYPE")
	private String appointmentType;
	
	@JsonProperty("SERVICES_SELECTED")
	private String selectedServices;
	
	
	/**
	 * @return the transactionId
	 */
	@JsonProperty("TRANSACTIONID")
	public String getTransactionId() {
		return transactionId;
	}
	
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the email
	 */
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the appointmentId
	 */
	@JsonProperty("APPOINTMENT_ID")
	public String getAppointmentId() {
		return appointmentId;
	}
	/**
	 * @param appointmentId the appointmentId to set
	 */
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}
	/**
	 * @return the storeNumber
	 */
	@JsonProperty("STORE_NUMBER")
	public String getStoreNumber() {
		return storeNumber;
	}
	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	/**
	 * @return the storeName
	 */
	@JsonProperty("STORE_NAME")
	public String getStoreName() {
		return storeName;
	}
	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**
	 * @return the storeAddress
	 */
	@JsonProperty("STORE_ADDRESS")
	public String getStoreAddress() {
		return storeAddress;
	}
	/**
	 * @param storeAddress the storeAddress to set
	 */
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	/**
	 * @return the storeCity
	 */
	@JsonProperty("STORE_CITY")
	public String getStoreCity() {
		return storeCity;
	}
	/**
	 * @param storeCity the storeCity to set
	 */
	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	
	/**
	 * @return the storeState
	 */
	@JsonProperty("STORE_STATE")
	public String getStoreState() {
		return storeState;
	}
	/**
	 * @param storeState the storeState to set
	 */
	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}
	/**
	 * @return the storeZip
	 */
	@JsonProperty("STORE_ZIP")
	public String getStoreZip() {
		return storeZip;
	}
	/**
	 * @param storeZip the storeZip to set
	 */
	public void setStoreZip(String storeZip) {
		this.storeZip = storeZip;
	}
	/**
	 * @return the storePhone
	 */
	@JsonProperty("STORE_PHONE")
	public String getStorePhone() {
		return storePhone;
	}
	/**
	 * @param storePhone the storePhone to set
	 */
	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}
	/**
	 * @return the storeFax
	 */
	@JsonProperty("STORE_FAX")
	public String getStoreFax() {
		return storeFax;
	}
	/**
	 * @param storeFax the storeFax to set
	 */
	public void setStoreFax(String storeFax) {
		this.storeFax = storeFax;
	}

	/**
	 * @return the storeEmail
	 */
	public String getStoreEmail() {
		return storeEmail;
	}

	/**
	 * @param storeEmail the storeEmail to set
	 */
	public void setStoreEmail(String storeEmail) {
		this.storeEmail = storeEmail;
	}

	/**
	 * @return the storeManagerFirstName
	 */
	public String getStoreManagerFirstName() {
		return storeManagerFirstName;
	}

	/**
	 * @param storeManagerFirstName the storeManagerFirstName to set
	 */
	public void setStoreManagerFirstName(String storeManagerFirstName) {
		this.storeManagerFirstName = storeManagerFirstName;
	}

	/**
	 * @return the storeManagerLastName
	 */
	public String getStoreManagerLastName() {
		return storeManagerLastName;
	}

	/**
	 * @param storeManagerLastName the storeManagerLastName to set
	 */
	public void setStoreManagerLastName(String storeManagerLastName) {
		this.storeManagerLastName = storeManagerLastName;
	}

	/**
	 * @return the vehicleYear
	 */
	@JsonProperty("VEHICLE_YEAR")
	public String getVehicleYear() {
		return vehicleYear;
	}
	/**
	 * @param vehicleYear the vehicleYear to set
	 */
	public void setVehicleYear(String vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
	/**
	 * @return the vehicleMake
	 */
	@JsonProperty("VEHICLE_MAKE")
	public String getVehicleMake() {
		return vehicleMake;
	}
	/**
	 * @param vehicleMake the vehicleMake to set
	 */
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}
	/**
	 * @return the vehicleModel
	 */
	@JsonProperty("VEHICLE_MODEL")
	public String getVehicleModel() {
		return vehicleModel;
	}
	/**
	 * @param vehicleModel the vehicleModel to set
	 */
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	/**
	 * @return the vehicleSubmodel
	 */
	@JsonProperty("VEHICLE_SUBMODEL")
	public String getVehicleSubmodel() {
		return vehicleSubmodel;
	}
	/**
	 * @param vehicleSubmodel the vehicleSubmodel to set
	 */
	public void setVehicleSubmodel(String vehicleSubmodel) {
		this.vehicleSubmodel = vehicleSubmodel;
	}
	/**
	 * @return the mileage
	 */
	@JsonProperty("MILEAGE")
	public String getMileage() {
		return mileage;
	}
	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	/**
	 * @return the comments
	 */
	@JsonProperty("COMMENTS")
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the customerFirstName
	 */
	@JsonProperty("CUSTOMER_FIRST_NAME")
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	/**
	 * @param customerFirstName the customerFirstName to set
	 */
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	/**
	 * @return the customerLastName
	 */
	@JsonProperty("CUSTOMER_LAST_NAME")
	public String getCustomerLastName() {
		return customerLastName;
	}
	/**
	 * @param customerLastName the customerLastName to set
	 */
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	/**
	 * @return the customerAddress1
	 */
	@JsonProperty("CUSTOMER_ADDRESS1")
	public String getCustomerAddress1() {
		return customerAddress1;
	}
	/**
	 * @param customerAddress1 the customerAddress1 to set
	 */
	public void setCustomerAddress1(String customerAddress1) {
		this.customerAddress1 = customerAddress1;
	}
	/**
	 * @return the customerAddress2
	 */
	@JsonProperty("CUSTOMER_ADDRESS2")
	public String getCustomerAddress2() {
		return customerAddress2;
	}
	/**
	 * @param customerAddress2 the customerAddress2 to set
	 */
	public void setCustomerAddress2(String customerAddress2) {
		this.customerAddress2 = customerAddress2;
	}
	/**
	 * @return the customerCity
	 */
	@JsonProperty("CUSTOMER_CITY")
	public String getCustomerCity() {
		return customerCity;
	}
	/**
	 * @param customerCity the customerCity to set
	 */
	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
	/**
	 * @return the customerState
	 */
	@JsonProperty("CUSTOMER_STATE")
	public String getCustomerState() {
		return customerState;
	}
	/**
	 * @param customerState the customerState to set
	 */
	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}
	/**
	 * @return the customerZipCode
	 */
	@JsonProperty("CUSTOMER_ZIP_CODE")
	public String getCustomerZipCode() {
		return customerZipCode;
	}
	/**
	 * @param customerZipCode the customerZipCode to set
	 */
	public void setCustomerZipCode(String customerZipCode) {
		this.customerZipCode = customerZipCode;
	}
	/**
	 * @return the customerDayTimePhone
	 */
	@JsonProperty("CUSTOMER_DAYTIME_PHONE")
	public String getCustomerDayTimePhone() {
		return customerDayTimePhone;
	}
	/**
	 * @param customerDayTimePhone the customerDayTimePhone to set
	 */
	public void setCustomerDayTimePhone(String customerDayTimePhone) {
		this.customerDayTimePhone = customerDayTimePhone;
	}
	/**
	 * @return the customerEveningPhone
	 */
	@JsonProperty("CUSTOMER_EVENING_PHONE")
	public String getCustomerEveningPhone() {
		return customerEveningPhone;
	}
	/**
	 * @param customerEveningPhone the customerEveningPhone to set
	 */
	public void setCustomerEveningPhone(String customerEveningPhone) {
		this.customerEveningPhone = customerEveningPhone;
	}
	/**
	 * @return the customerCellPhone
	 */
	@JsonProperty("CUSTOMER_CELL_PHONE")
	public String getCustomerCellPhone() {
		return customerCellPhone;
	}
	/**
	 * @param customerCellPhone the customerCellPhone to set
	 */
	public void setCustomerCellPhone(String customerCellPhone) {
		this.customerCellPhone = customerCellPhone;
	}
	/**
	 * @return the customerEmailAddress
	 */
	@JsonProperty("CUSTOMER_EMAIL_ADDRESS")
	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}
	/**
	 * @param customerEmailAddress the customerEmailAddress to set
	 */
	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}
	/**
	 * @return the createdDate
	 */
	@JsonProperty("CREATED_DATE")
	public String getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the websiteName
	 */
	@JsonProperty("WEBSITE_NAME")
	public String getWebsiteName() {
		return websiteName;
	}
	/**
	 * @param websiteName the websiteName to set
	 */
	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}
	/**
	 * @return the appointmentDateTime
	 */
	@JsonProperty("APPOINTMENT_DATETIME")
	public String getAppointmentDateTime() {
		return appointmentDateTime;
	}
	/**
	 * @param appointmentDateTime the appointmentDateTime to set
	 */
	public void setAppointmentDateTime(String appointmentDateTime) {
		this.appointmentDateTime = appointmentDateTime;
	}
	/**
	 * @return the pickUpDateTime
	 */
	@JsonProperty("PICKUP_DATETIME")
	public String getPickUpDateTime() {
		return pickUpDateTime;
	}
	/**
	 * @param pickUpDateTime the pickUpDateTime to set
	 */
	public void setPickUpDateTime(String pickUpDateTime) {
		this.pickUpDateTime = pickUpDateTime;
	}
	/**
	 * @return the dropOffDateTime
	 */
	@JsonProperty("DROPOFF_DATETIME")
	public String getDropOffDateTime() {
		return dropOffDateTime;
	}
	/**
	 * @param dropOffDateTime the dropOffDateTime to set
	 */
	public void setDropOffDateTime(String dropOffDateTime) {
		this.dropOffDateTime = dropOffDateTime;
	}
	/**
	 * @return the dropWaitOption
	 */
	@JsonProperty("DROP_WAIT_OPTION")
	public String getDropWaitOption() {
		return dropWaitOption;
	}
	/**
	 * @param dropWaitOption the dropWaitOption to set
	 */
	public void setDropWaitOption(String dropWaitOption) {
		this.dropWaitOption = dropWaitOption;
	}

	/**
	 * @return the appointmentType
	 */
	@JsonProperty("APPOINTMENTTYPE")
	public String getAppointmentType() {
		return appointmentType;
	}
	/**
	 * @param appointmentType the appointmentType to set
	 */
	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}
	/**
	 * @return the selectedServices
	 */
	@JsonProperty("SERVICES_SELECTED")
	public String getSelectedServices() {
		return selectedServices;
	}
	/**
	 * @param selectedServices the selectedServices to set
	 */
	public void setSelectedServices(String selectedServices) {
		this.selectedServices = selectedServices;
	}
}
