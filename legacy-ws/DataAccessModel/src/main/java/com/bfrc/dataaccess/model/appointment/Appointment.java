package com.bfrc.dataaccess.model.appointment;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.bfrc.dataaccess.model.vehicle.UserVehicle;

public class Appointment {

	private Logger log = Logger.getLogger(getClass().getName());
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMMM-d h:mma");
	private String[] maintenanceChoices;
	private UserVehicle vehicle;
	private Set<AppointmentService> services = new HashSet<AppointmentService>();
	private List<AppointmentChoice> choices = new ArrayList<AppointmentChoice>();
	
    private int hashValue = 0;
    private Long appointmentId;
    private Long storeNumber;
    private Long vehicleId;
    private Long vehicleYear;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleSubmodel;
    private Integer mileage;
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
    private java.util.Date createdDate;
    private String webSite;
    private String webSiteSource;
    private String emailReminder;
    private String phoneReminder;
   
    private Long batteryQuoteId;
    private String eCommRefNumber;
    
    public Appointment() {}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public Long getAppointmentId() {
		return this.appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public java.util.Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDaytimePhone() {
		return this.daytimePhone;
	}

	public void setDaytimePhone(String daytimePhone) {
		this.daytimePhone = daytimePhone;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailSignup() {
		return this.emailSignup;
	}

	public void setEmailSignup(String emailSignup) {
		this.emailSignup = emailSignup;
	}

	public String getEveningPhone() {
		return this.eveningPhone;
	}

	public void setEveningPhone(String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}

	public String getFirstName() {
		return StringUtils.abbreviate(this.firstName,50);
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getHashValue() {
		return this.hashValue;
	}

	public void setHashValue(int hashValue) {
		this.hashValue = hashValue;
	}

	public String getLastName() {
		return StringUtils.abbreviate(this.lastName,80);
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getMileage() {
		return this.mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleMake() {
		return StringUtils.abbreviate(this.vehicleMake,30);
	}

	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}

	public String getVehicleModel() {
		return StringUtils.abbreviate(this.vehicleModel,40);
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getVehicleSubmodel() {
		return StringUtils.abbreviate(this.vehicleSubmodel,80);
	}

	public void setVehicleSubmodel(String vehicleSubmodel) {
		this.vehicleSubmodel = vehicleSubmodel;
	}

	public Long getVehicleYear() {
		return this.vehicleYear;
	}

	public void setVehicleYear(Long vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

	public String getZip() {
		return StringUtils.abbreviate(this.zip,10);
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public UserVehicle getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(UserVehicle vehicle) {
		this.vehicle = vehicle;
		if(vehicle != null) {
			Long year = 0l;
			try { 
				year = new Long(vehicle.getYear()); 
			} catch(Exception E) { 
				log.severe("Could not convert "+vehicle.getYear()+" to a Long for vehicle "+vehicle.getAcesVehicleId()); 
			} 
			setVehicleYear(year);
			setVehicleMake(vehicle.getMake());
			setVehicleModel(vehicle.getModel());
			setVehicleSubmodel(vehicle.getSubmodel());
		}
	}

	public String[] getMaintenanceChoices() {
		return this.maintenanceChoices;
	}

	public void setMaintenanceChoices(String[] maintenanceChoices) {
		this.maintenanceChoices = maintenanceChoices;
	}

	public void setChoices(List<AppointmentChoice> choices) {
		this.choices = choices;
	}
	public List<AppointmentChoice> getChoices() {
		return choices;
	}
	public void setServices(Set<AppointmentService> services) {
		this.services = services;
	}
	public Set<AppointmentService> getServices() {
		return services;
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

	public String toEmail(List<AppointmentChoice> l) { return toEmail(l, null); }
	
	public String toEmail(List<AppointmentChoice> l, String serviceDesc) {
			StringWriter out = new StringWriter();
			if(this.firstName != null)
				out.append("First Name=").append(this.firstName).append("\n");
			if(this.lastName != null)
				out.append("Last Name=").append(this.lastName).append("\n");
			if(this.address1 != null)
				out.append("Address=").append(this.address1).append("\n");
			if(this.address2 != null && !"".equals(this.address2))
				out.append(this.address2).append("\n");
			if(this.city != null)
				out.append("City=").append(this.city).append("\n");
			if(this.state != null)
				out.append("State=").append(this.state).append("\n");
			if(this.zip != null)
				out.append("ZIP Code=").append(this.zip).append("\n");
			if(this.emailAddress != null)
				out.append("E-mail=").append(this.emailAddress).append("\n");
			if(this.daytimePhone != null && !"".equals(this.daytimePhone))
				out.append("Daytime Phone=").append(this.daytimePhone).append("\n");
			if(this.eveningPhone != null && !"".equals(this.eveningPhone))
				out.append("Evening Phone=").append(this.eveningPhone).append("\n");
			if(this.cellPhone != null && !"".equals(this.cellPhone))
				out.append("Cell Phone=").append(this.cellPhone).append("\n");
			
			if(this.vehicle != null)
				out.append("Vehicle=").append(this.vehicle.getYear()).append(" ").append(this.vehicle.getMake()).append(" " +
				this.vehicle.getModel()).append(" ").append(this.vehicle.getSubmodel()).append("\n");
			if(this.mileage != null)
				out.append("Mileage=").append(this.mileage.toString()).append("\n");
			if(this.maintenanceChoices != null && this.maintenanceChoices.length > 0) {
				out.append("Services=");
				for(int i=0;i<this.maintenanceChoices.length;i++) {
					if(i>0)
						out.append(", ");
					String choice = this.maintenanceChoices[i];
					try {
						Integer id = Integer.valueOf(choice);
						if(serviceDesc != null) {
							choice = serviceDesc;
//							AppointmentServiceDesc appointmentServiceDesc = dao.get(id);
//							if(appointmentServiceDesc != null)
//								choice = appointmentServiceDesc.getServiceDesc();
						}
					} catch(Exception ex) {}
					out.append(choice);
				}
				out.append("\n");
			}
			if(this.comments != null && !"".equals(this.comments))
				out.append("Comment=").append(this.comments).append("\n");

			if(l != null && l.size() > 0) {
				int idx = 0;
				for(AppointmentChoice choice : l) {
					out.append("Choice ").append(new Integer(idx+1).toString()).append("=").append(this.sdf.format(choice.getDatetime())).append("\n");
					if("drop".equals(choice.getDropWaitOption()))
						out.append("Leave vehicle");
					else out.append("Wait for service to be complete");
					out.append("\n");
				}
			}
			return out.toString();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appointmentId == null) ? 0 : appointmentId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Appointment other = (Appointment) obj;
		if (appointmentId == null) {
			if (other.appointmentId != null)
				return false;
		} else if (!appointmentId.equals(other.appointmentId))
			return false;
		return true;
	}
	
	public void addService(AppointmentService service) {
		if(services == null) services = new HashSet<AppointmentService>();
		services.add(service);
	}
	
	public void addChoice(AppointmentChoice choice) {
		if(choices == null) choices = new ArrayList<AppointmentChoice>();
		choices.add(choice);
	}
	
	public String geteCommRefNumber() {
		return eCommRefNumber;
	}

	public void seteCommRefNumber(String eCommRefNumber) {
		this.eCommRefNumber = eCommRefNumber;
	}

	@Override
	public String toString() {
		return "Appointment [vehicle=" + vehicle + ", appointmentId="
				+ appointmentId + ", storeNumber=" + storeNumber
				+ ", vehicleYear=" + vehicleYear + ", vehicleMake="
				+ vehicleMake + ", vehicleModel=" + vehicleModel
				+ ", vehicleSubmodel=" + vehicleSubmodel + ", mileage="
				+ mileage + ", comments=" + comments + ", firstName="
				+ firstName + ", lastName=" + lastName + ", address1="
				+ address1 + ", address2=" + address2 + ", city=" + city
				+ ", state=" + state + ", zip=" + zip + ", daytimePhone="
				+ daytimePhone + ", eveningPhone=" + eveningPhone
				+ ", cellPhone=" + cellPhone + ", emailAddress=" + emailAddress
				+ ", emailSignup=" + emailSignup + ", createdDate="
				+ createdDate + ", webSite=" + webSite + ", batteryQuoteId="
				+ batteryQuoteId + "]";
	}
	
	
	
}
