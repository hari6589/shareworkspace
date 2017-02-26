/**
 * 
 */
package com.bfrc.dataaccess.model.vehicle;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"acesVehicleId","tireSize","speedRating",
	"standardIndicator","frontRearBoth","frontInflation","rearInflation"})	
public class TirePressure {
	
	private Long acesVehicleId;
	private String tireSize;
	private String speedRating;
	private String standardIndicator;
	private String frontRearBoth;
	private Integer frontInflation;
	private Integer rearInflation;
	
	
	public TirePressure() {
	}
	public Long getAcesVehicleId() {
		return acesVehicleId;
	}
	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}
	public String getTireSize() {
		return tireSize;
	}
	public void setTireSize(String tireSize) {
		this.tireSize = tireSize;
	}
	public String getSpeedRating() {
		return speedRating;
	}
	public void setSpeedRating(String speedRating) {
		this.speedRating = speedRating;
	}
	public String getStandardIndicator() {
		return standardIndicator;
	}
	public void setStandardIndicator(String standardIndicator) {
		this.standardIndicator = standardIndicator;
	}
	public String getFrontRearBoth() {
		return frontRearBoth;
	}
	public void setFrontRearBoth(String frontRearBoth) {
		this.frontRearBoth = frontRearBoth;
	}
	public Integer getFrontInflation() {
		return frontInflation;
	}
	public void setFrontInflation(Integer frontInflation) {
		this.frontInflation = frontInflation;
	}
	public Integer getRearInflation() {
		return rearInflation;
	}
	public void setRearInflation(Integer rearInflation) {
		this.rearInflation = rearInflation;
	}
}