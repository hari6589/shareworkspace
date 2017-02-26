package bsro.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "c_id", "employee_id", "num_slots" })
public class Time {

	private String selectedDate;
	private String availableTime;
	private String locationId;
	private String employeeId;
	private String numberOfSlots;
	
	@JsonProperty("date")
	public String getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	@JsonProperty("start_time")
	public String getAvailableTime() {
		return availableTime;
	}
	public void setAvailableTime(String availableTime) {
		this.availableTime = availableTime;
	}
	
	@JsonProperty("c_id")
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	@JsonProperty("employee_id")
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	@JsonProperty("num_slots")
	public String getNumberOfSlots() {
		return numberOfSlots;
	}
	public void setNumberOfSlots(String numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}
	
}

