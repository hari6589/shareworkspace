package app.bsro.model.appointment;

import java.util.List;

/**
 * This class is used to pass data to the Appointment web service to create an appointment.
 * @author Brad Balmer
 *
 */
public class CreateAppointment {

	private String appointmentId;
	private String storeId;
	private String acesVehicleId;
	private String year;
	private String make;
	private String model;
	private String submodel;
	private String mileage;
	private List<String>services;
	private String comments;
	private List<AppointmentChoice> choices;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String email;
	private String dayPhone;
	private String dayPhoneExt;
	private String eveningPhone;
	private String eveningPhoneExt;
	private String cellPhone;
	private String rcvPromos;
	private String dropWait;
	private String batteryQuoteId;
	private String emailReminder;
	private String phoneReminder;

	/**
	 * This appName should not be used now.  It is reserved for future use.
	 */
	private String appName;
	
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getAppointmentId() {
		return appointmentId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	public String getAcesVehicleId() {
		return acesVehicleId;
	}
	public void setAcesVehicleId(String acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public List<String> getServices() {
		return services;
	}
	public void setServices(List<String> services) {
		this.services = services;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<AppointmentChoice> getChoices() {
		return choices;
	}
	public void setChoices(List<AppointmentChoice> choices) {
		this.choices = choices;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDayPhone() {
		return dayPhone;
	}
	public void setDayPhone(String dayPhone) {
		this.dayPhone = dayPhone;
	}
	public String getDayPhoneExt() {
		return dayPhoneExt;
	}
	public void setDayPhoneExt(String dayPhoneExt) {
		this.dayPhoneExt = dayPhoneExt;
	}
	public String getEveningPhone() {
		return eveningPhone;
	}
	public void setEveningPhone(String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}
	public String getEveningPhoneExt() {
		return eveningPhoneExt;
	}
	public void setEveningPhoneExt(String eveningPhoneExt) {
		this.eveningPhoneExt = eveningPhoneExt;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getRcvPromos() {
		return rcvPromos;
	}
	public void setRcvPromos(String rcvPromos) {
		this.rcvPromos = rcvPromos;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}
	public String getSubmodel() {
		return submodel;
	}

	/**
	 * Set value to either 'drop' or 'wait'.
	 * @param dropWait
	 */
	public void setDropWait(String dropWait) {
		this.dropWait = dropWait;
	}
	public String getDropWait() {
		return dropWait;
	}
	
	/**
	 * Do Not Set this value
	 * @param applicationName
	 */
	public void setAppName(String applicationName) {
		this.appName = applicationName;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public String getBatteryQuoteId() {
		return batteryQuoteId;
	}
	public void setBatteryQuoteId(String batteryQuoteId) {
		this.batteryQuoteId = batteryQuoteId;
	}
	
	public String getEmailReminder() {
		return emailReminder;
	}
	public void setEmailReminder(String emailReminder) {
		this.emailReminder = emailReminder;
	}
	public String getPhoneReminder() {
		return phoneReminder;
	}
	public void setPhoneReminder(String phoneReminder) {
		this.phoneReminder = phoneReminder;
	}
	@Override
	public String toString() {
		return "CreateAppointment [appointmentId=" + appointmentId
				+ ", storeId=" + storeId 
				+ ", year="+ year + ", make=" + make + ", model ="+ model + ", submodel = "+submodel
				+ ", mileage=" + mileage + ", services=" + services
				+ ", comments=" + comments + ", choices=" + choices
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", email=" + email + ", dayPhone=" + dayPhone
				+ ", dayPhoneExt=" + dayPhoneExt + ", eveningPhone="
				+ eveningPhone + ", eveningPhoneExt=" + eveningPhoneExt
				+ ", cellPhone=" + cellPhone + ", rcvPromos=" + rcvPromos
				+ ", applicationName=" + appName + ", batteryQuoteId " + batteryQuoteId + "]";
	}

}
