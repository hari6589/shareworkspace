/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.List;

import com.bfrc.dataaccess.model.appointment.Appointment;

/**
 * @author schowdhu
 *
 */
public class MyAppointments {
	
	private List<Appointment> appointments;

	/**
	 * 
	 */
	public MyAppointments() {
	}

	/**
	 * @param appointments
	 */
	public MyAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	/**
	 * @return the appointments
	 */
	public List<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * @param appointments the appointments to set
	 */
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	
	

}
