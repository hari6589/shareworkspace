/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.bfrc.dataaccess.model.appointment.Appointment;

/**
 * @author schowdhury
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"lastModifiedDate", "vehicle", "user"})
public class MyAppointment {

	private Long myAppointmentId;	
	private MyVehicle vehicle;
	private Appointment appointment;	
	private BFSUser user;
	private Date lastModifiedDate;
	

	/**
	 * @param vehicle
	 * @param appointment
	 * @param user
	 * @param lastModifiedDate
	 */
	public MyAppointment(MyVehicle vehicle, Appointment appointment,
			BFSUser user) {
		this.vehicle = vehicle;
		this.appointment = appointment;
		this.user = user;
		this.lastModifiedDate = new Date();
	}
	/**
	 * @return the myAppointmentId
	 */
	@JsonProperty("id")
	public Long getMyAppointmentId() {
		return myAppointmentId;
	}
	/**
	 * @param myAppointmentId the myAppointmentId to set
	 */
	@JsonProperty("id")
	public void setMyAppointmentId(Long myAppointmentId) {
		this.myAppointmentId = myAppointmentId;
	}
	
	/**
	 * @return the vehicle
	 */
	public MyVehicle getVehicle() {
		return vehicle;
	}
	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicle(MyVehicle vehicle) {
		this.vehicle = vehicle;
	}
	/**
	 * @return the appointment
	 */
	public Appointment getAppointment() {
		return appointment;
	}
	/**
	 * @param appointment the appointment to set
	 */
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	/**
	 * @return the user
	 */
	public BFSUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */

	public void setUser(BFSUser user) {
		this.user = user;
	}
	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyAppointment [myAppointmentId=" + myAppointmentId
				+ ", vehicle=" + vehicle + ", appointment=" + appointment
				+ ", user=" + user + ", lastModifiedDate=" + lastModifiedDate
				+ "]";
	}

}
