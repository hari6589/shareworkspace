package com.scheduleappointment.model;

import java.util.Date;

public class AppointmentChoice {
	
	private String choice;
	private Date datetime;
	private String dropWaitOption;
	
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	public String getDropWaitOption() {
		return dropWaitOption;
	}
	public void setDropWaitOption(String dropWaitOption) {
		this.dropWaitOption = dropWaitOption;
	}
	@Override
	public String toString() {
		return "AppointmentChoice [choice=" + choice + ", datetime=" + datetime
				+ ", dropWaitOption=" + dropWaitOption + "]";
	}
	
}
