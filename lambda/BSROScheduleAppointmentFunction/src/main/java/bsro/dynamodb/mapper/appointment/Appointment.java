package bsro.dynamodb.mapper.appointment;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Appointment")
public class Appointment {
	private Long AppointmentId;
	private Long customerId;
	private Long StoreNumber;
	private Long VehicleYear;
	private String VehicleMake;
	private String VehicleModel;
	private String VehicleSubmodel;
	private int Mileage;
	private String Comments;
	private String FirstName;
	private String LastName;
	private String Address1;
	private String Address2;
	private String City;
	private String State;
	private String Zip;
	private String DaytimePhone;
	private String EveningPhone;
	private String CellPhone;
	private String EmailAddress;
	private String EmailSignup;
	private Date CreatedDate;
	private String WebSite;
	private String WebSiteSource;
	private int AppointmentChoiceConfirmed;
	private Long BatteryQuoteId;
	private String PhoneReminderInd;
	private String EmailReminderInd;
	private String EcommRefNumber;
	private int Choice;
	private Date DateTime;
	private String DropWaitOption;
	private Date PickupTime;
	private Date DropOffTime;
	private String AppointmentServices;
	private Long EmployeeId;
	private Long roomId;
	private Long LocationId;
	private Long AppointmentStatusId;
	private String OtherDetails;
	private String Status;
	private Date UpdateDate; // required?
	private String BookingConfirmationId;
	private String EmailStatusMessage;
	private String EmailTrackingNumber;

	@DynamoDBHashKey(attributeName="AppointmentId")
	public Long getAppointmentId() {
		return AppointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		AppointmentId = appointmentId;
	}
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public Long getStoreNumber() {
		return StoreNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		StoreNumber = storeNumber;
	}
	
	public Long getVehicleYear() {
		return VehicleYear;
	}
	public void setVehicleYear(Long vehicleYear) {
		VehicleYear = vehicleYear;
	}
	
	public String getVehicleMake() {
		return VehicleMake;
	}
	public void setVehicleMake(String vehicleMake) {
		VehicleMake = vehicleMake;
	}
	
	public String getVehicleModel() {
		return VehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		VehicleModel = vehicleModel;
	}
	
	public String getVehicleSubmodel() {
		return VehicleSubmodel;
	}
	public void setVehicleSubmodel(String vehicleSubmodel) {
		VehicleSubmodel = vehicleSubmodel;
	}
	
	public int getMileage() {
		return Mileage;
	}
	public void setMileage(int mileage) {
		Mileage = mileage;
	}
	
	public String getComments() {
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
	}
	
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	public String getAddress1() {
		return Address1;
	}
	public void setAddress1(String address1) {
		Address1 = address1;
	}
	
	public String getAddress2() {
		return Address2;
	}
	public void setAddress2(String address2) {
		Address2 = address2;
	}
	
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	
	public String getZip() {
		return Zip;
	}
	public void setZip(String zip) {
		Zip = zip;
	}
	public String getDaytimePhone() {
		return DaytimePhone;
	}
	
	public void setDaytimePhone(String daytimePhone) {
		DaytimePhone = daytimePhone;
	}
	public String getEveningPhone() {
		return EveningPhone;
	}
	
	public void setEveningPhone(String eveningPhone) {
		EveningPhone = eveningPhone;
	}
	public String getCellPhone() {
		return CellPhone;
	}
	
	public void setCellPhone(String cellPhone) {
		CellPhone = cellPhone;
	}
	public String getEmailAddress() {
		return EmailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress;
	}
	
	public String getEmailSignup() {
		return EmailSignup;
	}
	public void setEmailSignup(String emailSignup) {
		EmailSignup = emailSignup;
	}
	
	public Date getCreatedDate() {
		return CreatedDate;
	}
	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}
	
	public String getWebSite() {
		return WebSite;
	}
	public void setWebSite(String webSite) {
		WebSite = webSite;
	}
	
	public String getWebSiteSource() {
		return WebSiteSource;
	}
	public void setWebSiteSource(String webSiteSource) {
		WebSiteSource = webSiteSource;
	}
	
	public int getAppointmentChoiceConfirmed() {
		return AppointmentChoiceConfirmed;
	}
	public void setAppointmentChoiceConfirmed(int appointmentChoiceConfirmed) {
		AppointmentChoiceConfirmed = appointmentChoiceConfirmed;
	}
	
	public Long getBatteryQuoteId() {
		return BatteryQuoteId;
	}
	public void setBatteryQuoteId(Long batteryQuoteId) {
		BatteryQuoteId = batteryQuoteId;
	}

	public String getPhoneReminderInd() {
		return PhoneReminderInd;
	}
	public void setPhoneReminderInd(String phoneReminderInd) {
		PhoneReminderInd = phoneReminderInd;
	}
	
	public String getEmailReminderInd() {
		return EmailReminderInd;
	}
	public void setEmailReminderInd(String emailReminderInd) {
		EmailReminderInd = emailReminderInd;
	}
	
	public String getEcommRefNumber() {
		return EcommRefNumber;
	}
	public void setEcommRefNumber(String ecommRefNumber) {
		EcommRefNumber = ecommRefNumber;
	}
	
	public int getChoice() {
		return Choice;
	}
	public void setChoice(int choice) {
		Choice = choice;
	}
		
	public Date getDateTime() {
		return DateTime;
	}
	public void setDateTime(Date dateTime) {
		DateTime = dateTime;
	}
	
	public String getDropWaitOption() {
		return DropWaitOption;
	}
	public void setDropWaitOption(String dropWaitOption) {
		DropWaitOption = dropWaitOption;
	}
	
	public Date getPickupTime() {
		return PickupTime;
	}
	public void setPickupTime(Date pickupTime) {
		PickupTime = pickupTime;
	}
	
	public Date getDropOffTime() {
		return DropOffTime;
	}
	public void setDropOffTime(Date dropOffTime) {
		DropOffTime = dropOffTime;
	}
	
	public String getAppointmentServices() {
		return AppointmentServices;
	}
	public void setAppointmentServices(String appointmentServices) {
		AppointmentServices = appointmentServices;
	}
	
	public Long getEmployeeId() {
		return EmployeeId;
	}
	public void setEmployeeId(Long employeeId) {
		EmployeeId = employeeId;
	}
	
	public Long getLocationId() {
		return LocationId;
	}
	public void setLocationId(Long locationId) {
		LocationId = locationId;
	}
		
	public Long getAppointmentStatusId() {
		return AppointmentStatusId;
	}
	public void setAppointmentStatusId(Long appointmentStatusId) {
		AppointmentStatusId = appointmentStatusId;
	}
	
	public String getOtherDetails() {
		return OtherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		OtherDetails = otherDetails;
	}
	
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	public Date getUpdateDate() {
		return UpdateDate;
	}
	public void setUpdateDate(Date updateDate) {
		UpdateDate = updateDate;
	}
	
	public String getBookingConfirmationId() {
		return BookingConfirmationId;
	}
	public void setBookingConfirmationId(String bookingConfirmationId) {
		BookingConfirmationId = bookingConfirmationId;
	}
	
	public String getEmailStatusMessage() {
		return EmailStatusMessage;
	}
	public void setEmailStatusMessage(String emailStatusMessage) {
		EmailStatusMessage = emailStatusMessage;
	}
	
	public String getEmailTrackingNumber() {
		return EmailTrackingNumber;
	}
	public void setEmailTrackingNumber(String emailTrackingNumber) {
		EmailTrackingNumber = emailTrackingNumber;
	}
	
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	
}
