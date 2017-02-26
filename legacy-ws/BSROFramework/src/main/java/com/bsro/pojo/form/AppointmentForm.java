package com.bsro.pojo.form;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.bfrc.pojo.appointment.AppointmentCustomer;
import com.bfrc.pojo.appointment.AppointmentSchedule;
import com.bfrc.pojo.store.Store;


public class AppointmentForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The value of the simple storeNumber property. */
	private java.lang.Long storeNumber;
	
	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	private String acesVehicleId;

	/** The value of the simple vehicleYear property. */
	private java.lang.Long vehicleYear;

	/** The value of the simple vehicleMake property. */
	private java.lang.String vehicleMake;
	private java.lang.String vehicleMakeName;

	/** The value of the simple vehicleModel property. */
	private java.lang.String vehicleModel;
	private java.lang.String vehicleModelName;

	/** The value of the simple vehicleSubmodel property. */
	private java.lang.String vehicleSubmodel;

	/** The value of the simple mileage property. */
	private java.lang.Integer mileage;

	/** The value of the simple comments property. */
	private java.lang.String comments;
	
	/** The value of the simple comments property. */
	private java.lang.String customerComments;

	/** The value of the simple firstName property. */
	private java.lang.String firstName;

	/** The value of the simple lastName property. */
	private java.lang.String lastName;

	/** The value of the simple address1 property. */
	private java.lang.String address;

	//we don't have an address2 or address 1 on the form.
	//private java.lang.String address2;

	/** The value of the simple city property. */
	private java.lang.String city;

	/** The value of the simple state property. */
	private java.lang.String state;

	/** The value of the simple zip property. */
	private java.lang.String zip;

	/** The value of the simple daytimePhone property. */
	private java.lang.String daytimePhone;
	
	private String daytimePhoneExt;

	/** The value of the simple eveningPhone property. */
	private java.lang.String eveningPhone;

	/** The value of the simple cellPhone property. */
	private java.lang.String cellPhone;

	private String phoneType;

	/** The value of the simple email property. */
	private java.lang.String emailAddress;

	/** The value of the simple emailSignup property. */
	private java.lang.String emailSignup;

	/** The value of the simple createdDate property. */
	private java.util.Date createdDate;

	/** The value of the simple webSite property. */
	private java.lang.String webSite;

	/** The value of the simple quoteId property. */
	private java.lang.Long batteryQuoteId;

	private String[] maintenanceChoices;
	private String selectedChoices;
	
	private Map servicesMap;

	private String choice1Date;
	private String choice1Time;
	private String choice1Radio;

	private String choice2Date;
	private String choice2Radio;
	private String choice2Time;
	
	private String dropOffTime;
	private String pickUpTime;

	private String emailReminderOption;
	private String phoneReminderOption;
	
	private String noValidation;
	
	private SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM/dd/yyyy h:mma");	
	
	private AppointmentParsedComments parsedComments;

	private List<Store> storeList;
	private Map mappedDistance;

	private boolean hasStores = false;
	private boolean isMilitaryStore = false;
	private boolean isPilotStore;
	
	private String tpms;
	private String storeState;
	private String storeCity;

	private String enteredZip;
	private String enteredState;
	private String enteredCity;
	
	private boolean changedTpms;

	private Date testDate;
	private AppointmentSchedule appointmentSchedule;

	private int minTimeBetween1stAnd2ndAppointment;
	
	private int minTimeBetween2ndAnd3rdAppointment;

	private boolean allowTwoAppointmentsBeforeNoon;
	
	private List<Integer> daysForNoPriorToNoonAppointments;

	private boolean findStoreById = true;
	
	private String searchedStoresBy = null;
	
	private int minRequiredAppointments;
	
	private int numEligibleDays;
	
	private int minTimeBetweenPickUpAndDropOff;
	
	private AppointmentCustomer appointmentCustomer;
	
	private Long employeeId;
	
	private String ap_serviceIds;
	
	private String ap_serviceDescriptions;
	
	private Long locationId;
	
	private Long appointmentStatusId;
	
	private String appointmentStatus;
	
	private List<String> schedulerErrors;
	
	public void setChangedTpms(boolean changedTpms) {
		this.changedTpms = changedTpms;
	}
	
	public boolean getChangedTpms() {
		return this.changedTpms;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public java.lang.Long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(java.lang.Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getAcesVehicleId() {
		return acesVehicleId;
	}

	public void setAcesVehicleId(String acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}

	public java.lang.Long getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(java.lang.Long vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

	public java.lang.String getVehicleMake() {
		return vehicleMake;
	}

	public void setVehicleMake(java.lang.String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}

	public java.lang.String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(java.lang.String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public java.lang.String getVehicleSubmodel() {
		return vehicleSubmodel;
	}

	public void setVehicleSubmodel(java.lang.String vehicleSubmodel) {
		this.vehicleSubmodel = vehicleSubmodel;
	}

	public java.lang.Integer getMileage() {
		return mileage;
	}

	public void setMileage(java.lang.Integer mileage) {
		this.mileage = mileage;
	}

	public java.lang.String getComments() {
		return comments;
	}

	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	public java.lang.String getCustomerComments() {
		return customerComments;
	}

	public void setCustomerComments(java.lang.String customerComments) {
		this.customerComments = customerComments;
	}

	public java.lang.String getFirstName() {
		return firstName;
	}

	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	public java.lang.String getLastName() {
		return lastName;
	}

	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public java.lang.String getState() {
		return state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	public java.lang.String getZip() {
		return zip;
	}

	public void setZip(java.lang.String zip) {
		this.zip = zip;
	}

	public java.lang.String getDaytimePhone() {
		return daytimePhone;
	}

	public void setDaytimePhone(java.lang.String daytimePhone) {
		this.daytimePhone = daytimePhone;
	}

	public java.lang.String getEveningPhone() {
		return eveningPhone;
	}

	public void setEveningPhone(java.lang.String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}

	public java.lang.String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(java.lang.String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public java.lang.String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(java.lang.String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public java.lang.String getEmailSignup() {
		return emailSignup;
	}

	public void setEmailSignup(java.lang.String emailSignup) {
		this.emailSignup = emailSignup;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public java.lang.String getWebSite() {
		return webSite;
	}

	public void setWebSite(java.lang.String webSite) {
		this.webSite = webSite;
	}

	public java.lang.Long getBatteryQuoteId() {
		return batteryQuoteId;
	}

	public void setBatteryQuoteId(java.lang.Long batteryQuoteId) {
		this.batteryQuoteId = batteryQuoteId;
	}

	public String[] getMaintenanceChoices() {
		return maintenanceChoices;
	}

	public void setMaintenanceChoices(String[] maintenanceChoices) {
		this.maintenanceChoices = maintenanceChoices;
	}

	public String getSelectedChoices() {
		return selectedChoices;
	}

	public void setSelectedChoices(String selectedChoices) {
		this.selectedChoices = selectedChoices;
	}
	public Map getServicesMap() {
		return servicesMap;
	}

	public void setServicesMap(Map servicesMap) {
		this.servicesMap = servicesMap;
	}

	public String getChoice1Date() {
		return choice1Date;
	}

	public void setChoice1Date(String choice1Date) {
		this.choice1Date = choice1Date;
	}

	public String getChoice1Time() {
		return choice1Time;
	}

	public void setChoice1Time(String choice1Time) {
		this.choice1Time = choice1Time;
	}

	public String getChoice1Radio() {
		return choice1Radio;
	}

	public void setChoice1Radio(String choice1Radio) {
		this.choice1Radio = choice1Radio;
	}

	public String getChoice2Date() {
		return choice2Date;
	}

	public void setChoice2Date(String choice2Date) {
		this.choice2Date = choice2Date;
	}

	public String getChoice2Radio() {
		return choice2Radio;
	}

	public void setChoice2Radio(String choice2Radio) {
		this.choice2Radio = choice2Radio;
	}

	public String getChoice2Time() {
		return choice2Time;
	}

	public void setChoice2Time(String choice2Time) {
		this.choice2Time = choice2Time;
	}

	public String getDropOffTime() {
		return dropOffTime;
	}

	public void setDropOffTime(String dropOffTime) {
		this.dropOffTime = dropOffTime;
	}

	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public SimpleDateFormat getDatetimeFormat() {
		return datetimeFormat;
	}

	public void setDatetimeFormat(SimpleDateFormat datetimeFormat) {
		this.datetimeFormat = datetimeFormat;
	}

	public String getNoValidation() {
		return noValidation;
	}

	public void setNoValidation(String noValidation) {
		this.noValidation = noValidation;
	}

	public AppointmentParsedComments getParsedComments() {
		return parsedComments;
	}

	public void setParsedComments(AppointmentParsedComments parsedComments) {
		this.parsedComments = parsedComments;
	}
	
	
	public Map getSelectedMaintenanceChoicesAsMap(){		
		HashMap<String, String> maintChoices = new HashMap<String, String>();

		if (this.maintenanceChoices != null && this.maintenanceChoices.length >0){
			for(String choice : maintenanceChoices) {
				maintChoices.put(choice, choice);
			}
		}
		
		return maintChoices;
	}

	public String getDaytimePhoneExt() {
		return daytimePhoneExt;
	}

	public void setDaytimePhoneExt(String daytimePhoneExt) {
		this.daytimePhoneExt = daytimePhoneExt;
	}

	public java.lang.String getVehicleMakeName() {
		return vehicleMakeName;
	}

	public void setVehicleMakeName(java.lang.String vehicleMakeName) {
		this.vehicleMakeName = vehicleMakeName;
	}

	public java.lang.String getVehicleModelName() {
		return vehicleModelName;
	}

	public void setVehicleModelName(java.lang.String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}

	public void setStoreList(List<Store> l) {
		storeList = l;		
	}
	
	public List<Store> getStoreList() {
		return storeList;
	}
	
	public void setMappedDistance(Map mappedDistance) {
		this.mappedDistance = mappedDistance;
	}
	
	public Map getMappedDistance() {
		return this.mappedDistance;
	}
	
	public boolean getIsMilitaryStore(){
		return isMilitaryStore;
	}
	
	public boolean setIsMilitaryStore(boolean isMilitaryStore){
		return this.isMilitaryStore  = isMilitaryStore;
	}
	
	public boolean isPilotStore() {
		return isPilotStore;
	}
	
	public boolean getIsPilotStore() {
		return isPilotStore;
	}

	public void setPilotStore(boolean isPilotStore) {
		this.isPilotStore = isPilotStore;
	}

	public boolean getHasStores(){
		return hasStores;
	}
	
	public boolean setHasStores(boolean hasStores){
		return this.hasStores  = hasStores;
	}

	public String getTpms() {
		return tpms;
	}

	public void setTpms(String tpms) {
		this.tpms = tpms;
	}

	public String getStoreState() {
		return storeState;
	}

	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}

	public String getStoreCity() {
		return storeCity;
	}

	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	
	public String getEnteredZip() {
		return enteredZip;
	}

	public void setEnteredZip(String enteredZip) {
		this.enteredZip = enteredZip;
	}

	public AppointmentSchedule getAppointmentSchedule() {
		return appointmentSchedule;
	}

	public void setAppointmentSchedule(AppointmentSchedule appointmentSchedule) {
		this.appointmentSchedule = appointmentSchedule;
	}

	

	public int getMinTimeBetween1stAnd2ndAppointment() {
		return minTimeBetween1stAnd2ndAppointment;
	}

	public void setMinTimeBetween1stAnd2ndAppointment(
			int minTimeBetween1stAnd2ndAppointment) {
		this.minTimeBetween1stAnd2ndAppointment = minTimeBetween1stAnd2ndAppointment;
	}

	public int getMinTimeBetween2ndAnd3rdAppointment() {
		return minTimeBetween2ndAnd3rdAppointment;
	}

	public void setMinTimeBetween2ndAnd3rdAppointment(
			int minTimeBetween2ndAnd3rdAppointment) {
		this.minTimeBetween2ndAnd3rdAppointment = minTimeBetween2ndAnd3rdAppointment;
	}

	public boolean getAllowTwoAppointmentsBeforeNoon(){
		return allowTwoAppointmentsBeforeNoon;
	}
	
	public void setAllowTwoAppointmentsBeforeNoon(boolean allowTwoAppointmentsBeforeNoon){
		this.allowTwoAppointmentsBeforeNoon = allowTwoAppointmentsBeforeNoon;
	}

	public boolean isFindStoreById() {
		return findStoreById;
	}

	public void setFindStoreById(boolean findStoreById) {
		this.findStoreById = findStoreById;
	}

	public String getSearchedStoresBy() {
		return searchedStoresBy;
	}

	public void setSearchedStoresBy(String searchedStoresBy) {
		this.searchedStoresBy = searchedStoresBy;
	}

	public int getMinRequiredAppointments() {
		return minRequiredAppointments;
	}

	public void setMinRequiredAppointments(int minRequiredAppointments) {
		this.minRequiredAppointments = minRequiredAppointments;
	}

	public int getNumEligibleDays() {
		return numEligibleDays;
	}

	public void setNumEligibleDays(int numEligibleDays) {
		this.numEligibleDays = numEligibleDays;
	}

	public List<Integer> getDaysForNoPriorToNoonAppointments() {
		return daysForNoPriorToNoonAppointments;
	}

	public void setDaysForNoPriorToNoonAppointments(
			List<Integer> daysForNoPriorToNoonAppointments) {
		this.daysForNoPriorToNoonAppointments = daysForNoPriorToNoonAppointments;
	}

	public AppointmentCustomer getAppointmentCustomer() {
		return appointmentCustomer;
	}

	public void setAppointmentCustomer(AppointmentCustomer appointmentCustomer) {
		this.appointmentCustomer = appointmentCustomer;
	}

	public int getMinTimeBetweenPickUpAndDropOff() {
		return minTimeBetweenPickUpAndDropOff;
	}

	public void setMinTimeBetweenPickUpAndDropOff(int minTimeBetweenPickUpAndDropOff) {
		this.minTimeBetweenPickUpAndDropOff = minTimeBetweenPickUpAndDropOff;
	}

	public List<String> getSchedulerErrors() {
		return schedulerErrors;
	}

	public void setSchedulerErrors(List<String> schedulerErrors) {
		this.schedulerErrors = schedulerErrors;
	}

	/**
	 * @return the emailReminderOption
	 */
	public String getEmailReminderOption() {
		return emailReminderOption;
	}

	/**
	 * @param emailReminderOption the emailReminderOption to set
	 */
	public void setEmailReminderOption(String emailReminderOption) {
		this.emailReminderOption = emailReminderOption;
	}

	/**
	 * @return the phoneReminderOption
	 */
	public String getPhoneReminderOption() {
		return phoneReminderOption;
	}

	/**
	 * @param phoneReminderOption the phoneReminderOption to set
	 */
	public void setPhoneReminderOption(String phoneReminderOption) {
		this.phoneReminderOption = phoneReminderOption;
	}

	/**
	 * @return the employeeId
	 */
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
	 * @return the ap_serviceIds
	 */
	public String getAp_serviceIds() {
		return ap_serviceIds;
	}

	/**
	 * @param ap_serviceIds the ap_serviceIds to set
	 */
	public void setAp_serviceIds(String ap_serviceIds) {
		this.ap_serviceIds = ap_serviceIds;
	}

	/**
	 * @return the ap_serviceDescriptions
	 */
	public String getAp_serviceDescriptions() {
		return ap_serviceDescriptions;
	}

	/**
	 * @param ap_serviceDescriptions the ap_serviceDescriptions to set
	 */
	public void setAp_serviceDescriptions(String ap_serviceDescriptions) {
		this.ap_serviceDescriptions = ap_serviceDescriptions;
	}

	/**
	 * @return the locationId
	 */
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
	 * @return the appointmentStatusId
	 */
	public Long getAppointmentStatusId() {
		return appointmentStatusId;
	}

	/**
	 * @param appointmentStatusId the appointmentStatusId to set
	 */
	public void setAppointmentStatusId(Long appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	
	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	public String getEnteredState() {
		return enteredState;
	}

	public void setEnteredState(String enteredState) {
		this.enteredState = enteredState;
	}

	public String getEnteredCity() {
		return enteredCity;
	}

	public void setEnteredCity(String enteredCity) {
		this.enteredCity = enteredCity;
	}
}
