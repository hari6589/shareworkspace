/**
 * 
 */
package app.bsro.model.appointment;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * @author schowdhu
 *
 */
@JsonPropertyOrder({"availableDate", "availableDay", "availableMonth", "availableYear", "dayName"})
public class AppointmentOpenDate {
	
	private String locationId;
	private String employeeId;
	private String dayName;
	private String availableDate;
	private String availableMonth;
	private String availableDay;
	private String availableYear;
	/**
	 * 
	 */
	public AppointmentOpenDate() {
	}
	
	
	/**
	 * @param availableDate
	 * @param availableMonth
	 * @param availableDay
	 * @param availableYear
	 */
	public AppointmentOpenDate(String availableDate, String availableMonth,
			String availableDay, String availableYear) {
		this.availableDate = availableDate;
		this.availableMonth = availableMonth;
		this.availableDay = availableDay;
		this.availableYear = availableYear;
	}


	/**
	 * @return the locationId
	 */
	@JsonIgnore
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	@JsonProperty("c_id")
	public void setC_Id(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the employeeId
	 */
	@JsonIgnore
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployee_Id(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the dayName
	 */
	@JsonProperty("dayName")
	public String getDayName() {
		return dayName;
	}

	/**
	 * @param dayName the dayName to set
	 */
	public void setDay_Name(String dayName) {
		this.dayName = dayName;
	}

	/**
	 * @return the availableDate
	 */
	@JsonProperty("availableDate")
	public String getAvailableDate() {
		return availableDate;
	}
	/**
	 * @param availableDate the availableDate to set
	 */
	@JsonProperty("date")
	public void setDate(String availableDate) {
		this.availableDate = availableDate;
	}
	/**
	 * @return the availableMonth
	 */
	@JsonProperty("availableMonth")
	public String getAvailableMonth() {
		return availableMonth;
	}
	/**
	 * @param availableMonth the availableMonth to set
	 */
	@JsonProperty("month")
	public void setMonth(String availableMonth) {
		this.availableMonth = availableMonth;
	}
	/**
	 * @return the availableDay
	 */
	@JsonProperty("availableDay")
	public String getAvailableDay() {
		return availableDay;
	}
	/**
	 * @param availableDay the availableDay to set
	 */
	@JsonProperty("day")
	public void setDay(String availableDay) {
		this.availableDay = availableDay;
	}
	/**
	 * @return the availableYear
	 */
	@JsonProperty("availableYear")
	public String getAvailableYear() {
		return availableYear;
	}
	/**
	 * @param availableYear the availableYear to set
	 */
	@JsonProperty("year")
	public void setYear(String availableYear) {
		this.availableYear = availableYear;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppointmentOpenDate [locationId=" + locationId
				+ ", employeeId=" + employeeId + ", dayName=" + dayName
				+ ", availableDate=" + availableDate + ", availableMonth="
				+ availableMonth + ", availableDay=" + availableDay
				+ ", availableYear=" + availableYear + "]";
	}
	
	
	
	
}
