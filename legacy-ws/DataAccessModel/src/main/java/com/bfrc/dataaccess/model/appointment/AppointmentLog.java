package com.bfrc.dataaccess.model.appointment;

public class AppointmentLog {
	private long appointmentLogId;
	private String logValue;
	public long getAppointmentLogId() {
		return appointmentLogId;
	}
	public void setAppointmentLogId(long appointmentLogId) {
		this.appointmentLogId = appointmentLogId;
	}
	public String getLogValue() {
		return logValue;
	}
	public void setLogValue(String logValue) {
		this.logValue = logValue;
	}
}
