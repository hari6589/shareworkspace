package samples;

public class App {
	private Long storeNumber;
	private String locationId;
	private String employeeId;
	private String appointmentStatusId;
	private String appointmentStatusDesc;
	private String vehicleYear;
	private String vehicleMake;
	private String vehicleModel;
	private String vehicleSubmodel;
	private String mileage;
	private String customerFirstName;
	private String customerLastName;
	private String customerDayTimePhone;
	private String customerEmailAddress;
	private String websiteName;
	private String appointmentType;
	private SubApp choice;
	private String selectedServices;
	private String test;
	
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
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
	public SubApp getChoice() {
		return choice;
	}
	public void setChoice(SubApp choice) {
		this.choice = choice;
	}
	public String getSelectedServices() {
		return selectedServices;
	}
	public void setSelectedServices(String selectedServices) {
		this.selectedServices = selectedServices;
	}
	@Override
	public String toString() {
		return "App [storeNumber=" + storeNumber + ", locationId=" + locationId
				+ ", employeeId=" + employeeId + ", appointmentStatusId="
				+ appointmentStatusId + ", appointmentStatusDesc="
				+ appointmentStatusDesc + ", vehicleYear=" + vehicleYear
				+ ", vehicleMake=" + vehicleMake + ", vehicleModel="
				+ vehicleModel + ", vehicleSubmodel=" + vehicleSubmodel
				+ ", mileage=" + mileage + ", customerFirstName="
				+ customerFirstName + ", customerLastName=" + customerLastName
				+ ", customerDayTimePhone=" + customerDayTimePhone
				+ ", customerEmailAddress=" + customerEmailAddress
				+ ", websiteName=" + websiteName + ", appointmentType="
				+ appointmentType + ", choice=" + choice
				+ ", selectedServices=" + selectedServices + ", test=" + test
				+ "]";
	}
		
}
