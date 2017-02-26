/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhury
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"myVehicle"})
@JsonPropertyOrder({"id", "mileage", "condition", "title", "type"})
public class MyMaintenanceServicePerformed {

	private Long myMaintServicePerformedId;
	private MyVehicle myVehicle;
	
	private String complete;
	
	private Integer type;
	
	private Long mileage;
	
	private String condition;
	
	private String title;
	
	/**
	 * 
	 */
	public MyMaintenanceServicePerformed() {
	}
	/**
	 * @return the myMaintServicePerformedId
	 */
	@JsonProperty("id")
	public Long getMyMaintServicePerformedId() {
		return myMaintServicePerformedId;
	}
	/**
	 * @param myMaintServicePerformedId the myMaintServicePerformedId to set
	 */
	@JsonProperty("id")
	public void setMyMaintServicePerformedId(Long myMaintServicePerformedId) {
		this.myMaintServicePerformedId = myMaintServicePerformedId;
	}

	/**
	 * @return the myVehicle
	 */
	public MyVehicle getMyVehicle() {
		return myVehicle;
	}
	/**
	 * @param myVehicle the myVehicle to set
	 */
	public void setMyVehicle(MyVehicle myVehicle) {
		this.myVehicle = myVehicle;
	}
	/**
	 * @return the complete
	 */
	public String getComplete() {
		return complete;
	}
	/**
	 * @param complete the complete to set
	 */
	public void setComplete(String complete) {
		this.complete = complete;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the mileage
	 */
	public Long getMileage() {
		return mileage;
	}
	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}
	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "MyMaintenanceServicePerformed [myMaintServicePerformedId="
				+ myMaintServicePerformedId 
				+ ", complete=" + complete + ", type=" + type + ", mileage="
				+ mileage + ", condition=" + condition + ", title=" + title
				+ "]";
	}
	
	
}
