package bsro.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "c_id", "employee_id" })
public class Day {

	private String availableDate;
	private String locationId;
	private String employeeId;
	private String availableMonth;
	private String availableDay;
	private String availableYear;
	private String dayName;
	
	@JsonProperty("date")
	public String getAvailableDate() {
		return availableDate;
	}
	public void setAvailableDate(String availableDate) {
		this.availableDate = availableDate;
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
	
	@JsonProperty("month")
	public String getAvailableMonth() {
		return availableMonth;
	}
	public void setAvailableMonth(String availableMonth) {
		this.availableMonth = availableMonth;
	}
	
	@JsonProperty("day")
	public String getAvailableDay() {
		return availableDay;
	}
	public void setAvailableDay(String availableDay) {
		this.availableDay = availableDay;
	}
	
	@JsonProperty("year")
	public String getAvailableYear() {
		return availableYear;
	}
	public void setAvailableYear(String availableYear) {
		this.availableYear = availableYear;
	}
	
	@JsonProperty("day_name")
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	
}
