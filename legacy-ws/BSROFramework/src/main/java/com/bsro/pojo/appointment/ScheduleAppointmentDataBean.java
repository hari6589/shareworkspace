package com.bsro.pojo.appointment;

import java.util.HashMap;
import java.util.Map;

import com.bfrc.pojo.appointment.Appointment;

public class ScheduleAppointmentDataBean {
	Map<String, String> errors = null;
	Appointment appointment = null;
	
	public ScheduleAppointmentDataBean(){
		this.errors = new HashMap<String, String>();
		this.appointment = new Appointment();
	}
	public ScheduleAppointmentDataBean(Map<String, String> errs){
		this.errors = errs;
		this.appointment = new Appointment();
	}
	public ScheduleAppointmentDataBean(Map<String, String> errs, Appointment appt){
		this.errors = errs;
		this.appointment = appt;
	}
	public Map<String, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
}
