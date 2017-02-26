/**
 * 
 */
package app.bsro.model.appointment;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author schowdhu
 *
 */

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AvailableSlot {
	
	private String locationId;
	private String selectedDate;
	private String startTime;
	private String employeeId;
	private String roomId;
	private String numAppts;
	private String numSlots;
	/**
	 * 
	 */
	public AvailableSlot() {
	}
	/**
	 * @param selectedDate
	 * @param startTime
	 * @param roomId
	 */
	public AvailableSlot(String selectedDate, String startTime, String employeeId, String roomId) {
		this.selectedDate = selectedDate;
		this.startTime = startTime;
		this.employeeId = employeeId;
		this.roomId = roomId;
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
	 * @return the selectedDate
	 */
	@JsonProperty("selectedDate")
	public String getSelectedDate() {
		return selectedDate;
	}
	/**
	 * @param selectedDate the selectedDate to set
	 */
	@JsonProperty("date")
	public void setDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}
	/**
	 * @return the startTime
	 */
	@JsonProperty("availableTime")
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	@JsonProperty("start_time")
	public void setStart_Time(String startTime) {
		this.startTime = startTime;
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
	@JsonProperty("employee_id")
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the roomId
	 */
	@JsonProperty("roomId")
	public String getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	@JsonProperty("room_id")
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	/**
	 * @return the numAppts
	 */
	@JsonIgnore
	public String getNumAppts() {
		return numAppts;
	}
	/**
	 * @param numAppts the numAppts to set
	 */
	@JsonProperty("num_appts")
	public void setNumAppts(String numAppts) {
		this.numAppts = numAppts;
	}
	/**
	 * @return the numSlots
	 */
	@JsonIgnore
	public String getNumSlots() {
		return numSlots;
	}
	/**
	 * @param numSlots the numSlots to set
	 */
	@JsonProperty("num_slots")
	public void setNumSlots(String numSlots) {
		this.numSlots = numSlots;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AvailableSlots [locationId=" + locationId + ", selectedDate="
				+ selectedDate + ", startTime=" + startTime + ", employeeId="
				+ employeeId + ", roomId=" + roomId + ", numAppts=" + numAppts
				+ ", numSlots=" + numSlots + "]";
	}
	
	
}
