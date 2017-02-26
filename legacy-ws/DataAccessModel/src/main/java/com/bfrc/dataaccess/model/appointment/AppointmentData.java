/**
 * Note: This will be replaced with the com.bfrc.dataaccess.model.appointment.Appointment pojo 
 * once we completely move out from MAI
 */
package com.bfrc.dataaccess.model.appointment;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AppointmentData {

	private Long appointmentId;

	private Long storeNumber;

	private Long locationId;
	
	private Long employeeId;
	
	private Long roomId;
	
	private Long appointmentStatusId;
	
	private String appointmentStatusDesc;
	
	private String vehicleYear;

	private String vehicleMake;

	private String vehicleModel;

	private String vehicleSubmodel;
	
	private String vehicleMileage;

	private String mileage;
	
	private String customerNotes;

	private String comments;
	
	private Long customerId;

	private String customerFirstName;

	private String customerLastName;

	private String customerAddress1;

	private String customerAddress2;

	private String customerCity;

	private String customerState;

	private String customerZipCode;

	private String customerDayTimePhone;

	private String customerEveningPhone;

	private String customerCellPhone;

	private String customerEmailAddress;

	private String websiteName;
	
	private String webSiteSource;

	private String appointmentType;
	
	private String tpmsFlag;
	
	private String quoteId;
	
	private AppointmentChoice choice;
	
	private String selectedServices;
	
	private String emailSignup;
	
	private String eCommRefNumber;

	/**
	 * 
	 */
	public AppointmentData() {
	}

	/**
	 * @return the appointmentId
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getAppointmentId() {
		return appointmentId;
	}

	/**
	 * @param appointmentId the appointmentId to set
	 */
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	/**
	 * @return the storeNumber
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getStoreNumber() {
		return storeNumber;
	}

	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**
	 * @return the locationId
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the employeeId
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the roomId
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	/**
	 * @return the appointmentStatusId
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getAppointmentStatusId() {
		return appointmentStatusId;
	}

	/**
	 * @param appointmentStatusId the appointmentStatusId to set
	 */
	public void setAppointmentStatusId(Long appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	/**
	 * @return the appointmentStatusDesc
	 */
	public String getAppointmentStatusDesc() {
		return appointmentStatusDesc;
	}

	/**
	 * @param appointmentStatusDesc the appointmentStatusDesc to set
	 */
	public void setAppointmentStatusDesc(String appointmentStatusDesc) {
		this.appointmentStatusDesc = appointmentStatusDesc;
	}

	/**
	 * @return the vehicleYear
	 */
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
	 * @return the vehicleMileage
	 */
	public String getVehicleMileage() {
		return vehicleMileage;
	}

	/**
	 * @param vehicleMileage the vehicleMileage to set
	 */
	public void setVehicleMileage(String vehicleMileage) {
		this.vehicleMileage = vehicleMileage;
	}

	/**
	 * @return the mileage
	 */
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
	 * @return the customerNotes
	 */
	public String getCustomerNotes() {
		return customerNotes;
	}

	/**
	 * @param customerNotes the customerNotes to set
	 */
	public void setCustomerNotes(String customerNotes) {
		this.customerNotes = customerNotes;
	}

	/**
	 * @return the comments
	 */
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
	 * @return the customerId
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerFirstName
	 */
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
	 * @return the websiteName
	 */
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
	 * @return the webSiteSource
	 */
	public String getWebSiteSource() {
		return webSiteSource;
	}

	/**
	 * @param webSiteSource the webSiteSource to set
	 */
	public void setWebSiteSource(String webSiteSource) {
		this.webSiteSource = webSiteSource;
	}

	/**
	 * @return the appointmentType
	 */
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
	 * @return the tpmsFlag
	 */
	public String getTpmsFlag() {
		return tpmsFlag;
	}

	/**
	 * @param tpmsFlag the tpmsFlag to set
	 */
	public void setTpmsFlag(String tpmsFlag) {
		this.tpmsFlag = tpmsFlag;
	}

	/**
	 * @return the quoteId
	 */
	public String getQuoteId() {
		return quoteId;
	}

	/**
	 * @param quoteId the quoteId to set
	 */
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * @return the choice
	 */
	public AppointmentChoice getChoice() {
		return choice;
	}

	/**
	 * @param choice the choice to set
	 */
	public void setChoice(AppointmentChoice choice) {
		this.choice = choice;
	}

	/**
	 * @return the selectedServices
	 */
	public String getSelectedServices() {
		return selectedServices;
	}

	/**
	 * @param selectedServices the selectedServices to set
	 */
	public void setSelectedServices(String selectedServices) {
		this.selectedServices = selectedServices;
	}

	/**
	 * @return the emailSignup
	 */
	public String getEmailSignup() {
		return emailSignup;
	}

	/**
	 * @param emailSignup the emailSignup to set
	 */
	public void setEmailSignup(String emailSignup) {
		this.emailSignup = emailSignup;
	}
	
	public String geteCommRefNumber() {
		return eCommRefNumber;
	}

	public void seteCommRefNumber(String eCommRefNumber) {
		this.eCommRefNumber = eCommRefNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppointmentData [appointmentId=" + appointmentId
				+ ", storeNumber=" + storeNumber + ", locationId=" + locationId
				+ ", employeeId=" + employeeId + ", roomId=" + roomId
				+ ", appointmentStatusId=" + appointmentStatusId
				+ ", appointmentStatusDesc=" + appointmentStatusDesc
				+ ", vehicleYear=" + vehicleYear + ", vehicleMake="
				+ vehicleMake + ", vehicleModel=" + vehicleModel
				+ ", vehicleSubmodel=" + vehicleSubmodel + ", vehicleMileage="
				+ vehicleMileage + ", mileage=" + mileage + ", customerNotes="
				+ customerNotes + ", comments=" + comments + ", customerId="
				+ customerId + ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName
				+ ", customerAddress1=" + customerAddress1
				+ ", customerAddress2=" + customerAddress2 + ", customerCity="
				+ customerCity + ", customerState=" + customerState
				+ ", customerZipCode=" + customerZipCode
				+ ", customerDayTimePhone=" + customerDayTimePhone
				+ ", customerEveningPhone=" + customerEveningPhone
				+ ", customerCellPhone=" + customerCellPhone
				+ ", customerEmailAddress=" + customerEmailAddress
				+ ", websiteName=" + websiteName + ", webSiteSource="
				+ webSiteSource + ", appointmentType=" + appointmentType
				+ ", tpmsFlag=" + tpmsFlag + ", quoteId=" + quoteId
				+ ", choice=" + choice + ", selectedServices="
				+ ", emailSignup=" + emailSignup 
				+ selectedServices + "]";
	}
	
	
}
