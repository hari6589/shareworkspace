package com.bridgestone.bsro.aem.voice.util;

public class Choice {
	
	private String appointmentChoiceId;
	private String choice;
	private String datetime;
	private String dropWaitOption;
	private String appointmentId;
	private String pickUpTime;
	private String dropOffTime;

	public Choice() {
	}

	public Choice(String appointmentChoiceId, String choice, String datetime,
				  String dropWaitOption, String appointmentId, String pickUpTime, String dropOffTime) {
		this.appointmentChoiceId = appointmentChoiceId;
		this.choice = choice;
		this.datetime = datetime;
		this.dropWaitOption = dropWaitOption;
		this.appointmentId = appointmentId;
		this.pickUpTime = pickUpTime;
		this.dropOffTime = dropOffTime;
	}

	public String getAppointmentChoiceId() {
		return appointmentChoiceId;
	}

	public void setAppointmentChoiceId(String appointmentChoiceId) {
		this.appointmentChoiceId = appointmentChoiceId;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getDropWaitOption() {
		return dropWaitOption;
	}

	public void setDropWaitOption(String dropWaitOption) {
		this.dropWaitOption = dropWaitOption;
	}

	public String getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public String getDropOffTime() {
		return dropOffTime;
	}

	public void setDropOffTime(String dropOffTime) {
		this.dropOffTime = dropOffTime;
	}

}