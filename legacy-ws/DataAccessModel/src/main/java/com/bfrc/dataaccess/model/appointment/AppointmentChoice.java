package com.bfrc.dataaccess.model.appointment;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

/**
 * A class that represents a row in the APPOINTMENT_CHOICE table. 
 */

public class AppointmentChoice implements Serializable {
	private static final long serialVersionUID = -2294562671217264841L;
	private Long appointmentChoiceId;
    private Integer choice;
    private Date datetime;
    private String dropWaitOption;
    private Long appointmentId;
    private Date pickUpTime;
    private Date dropOffTime;
    
    public AppointmentChoice(){}
    
    public AppointmentChoice(Long appointmentChoiceId, Integer choice,
			Date datetime, String dropWaitOption, Long appointmentId) {
		this.appointmentChoiceId = appointmentChoiceId;
		this.choice = choice;
		this.datetime = datetime;
		this.dropWaitOption = dropWaitOption;
		this.appointmentId = appointmentId;
	}
    
	public AppointmentChoice(Long appointmentChoiceId, Integer choice,
			Date datetime, String dropWaitOption, Long appointmentId,
			Date pickUpTime, Date dropOffTime) {
		this.appointmentChoiceId = appointmentChoiceId;
		this.choice = choice;
		this.datetime = datetime;
		this.dropWaitOption = dropWaitOption;
		this.appointmentId = appointmentId;
		this.pickUpTime = pickUpTime;
		this.dropOffTime = dropOffTime;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getAppointmentChoiceId() {
		return appointmentChoiceId;
	}
	public void setAppointmentChoiceId(Long appointmentChoiceId) {
		this.appointmentChoiceId = appointmentChoiceId;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Integer getChoice() {
		return choice;
	}
	public void setChoice(Integer choice) {
		this.choice = choice;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getDropWaitOption() {
		return dropWaitOption;
	}
	public void setDropWaitOption(String dropWaitOption) {
		this.dropWaitOption = dropWaitOption;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Date getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(Date pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public Date getDropOffTime() {
		return dropOffTime;
	}

	public void setDropOffTime(Date dropOffTime) {
		this.dropOffTime = dropOffTime;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((appointmentChoiceId == null) ? 0 : appointmentChoiceId
						.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppointmentChoice other = (AppointmentChoice) obj;
		if (appointmentChoiceId == null) {
			if (other.appointmentChoiceId != null)
				return false;
		} else if (!appointmentChoiceId.equals(other.appointmentChoiceId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppointmentChoice [appointmentChoiceId=" + appointmentChoiceId
				+ ", choice=" + choice + ", datetime=" + datetime
				+ ", dropWaitOption=" + dropWaitOption + ", appointmentId="
				+ appointmentId + ", pickUpTime=" + pickUpTime
				+ ", dropOffTime=" + dropOffTime + "]";
	}    
}
