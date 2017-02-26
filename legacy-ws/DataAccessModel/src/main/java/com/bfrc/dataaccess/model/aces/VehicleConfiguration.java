package com.bfrc.dataaccess.model.aces;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.bfrc.dataaccess.model.vehicle.Fitment;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"vehicle","lastUpdateDate"})
@JsonPropertyOrder({"id","bodyNumDoors"})
public class VehicleConfiguration {
	private Long vehicleConfigId;
	private Fitment fitment;
	private String bodyNumDoors;
	private String brakeAbs;
	private String brakeSystemName;
	private String frontBrakeType;
	private String rearBrakeType;
	private String driveType;
	private String engineDisplacementLiters;
	private String engineDisplacementCC;
	private String engineNumCylinders;
	private String engineBlockType;
	private String valvesPerEngine;
	private String engineFuelType;	
	private String engineFuelControlType;
	private String engineFuelSystemDesign;
	private String engineHorsepower;
	private String frontSpringType;
	private String rearSpringType;
	private String steeringSystemName;
	private String transmissionControlType;
	private String transmissionNumSpeeds;
	private Date lastUpdateDate;
	
	public VehicleConfiguration() {
	}
	@JsonProperty("id")
	public Long getVehicleConfigId() {
		return vehicleConfigId;
	}
	@JsonProperty("id")
	public void setVehicleConfigId(Long vehicleConfigId) {
		this.vehicleConfigId = vehicleConfigId;
	}
	
	
	public String getBodyNumDoors() {
		return bodyNumDoors;
	}
	public void setBodyNumDoors(String bodyNumDoors) {
		this.bodyNumDoors = bodyNumDoors;
	}
	
	public String getBrakeAbs() {
		return brakeAbs;
	}
	public void setBrakeAbs(String brakeAbs) {
		this.brakeAbs = brakeAbs;
	}
	public String getBrakeSystemName() {
		return brakeSystemName;
	}
	public void setBrakeSystemName(String brakeSystemName) {
		this.brakeSystemName = brakeSystemName;
	}
	public String getFrontBrakeType() {
		return frontBrakeType;
	}
	public void setFrontBrakeType(String frontBrakeType) {
		this.frontBrakeType = frontBrakeType;
	}
	public String getRearBrakeType() {
		return rearBrakeType;
	}
	public void setRearBrakeType(String rearBrakeType) {
		this.rearBrakeType = rearBrakeType;
	}
	
	public String getDriveType() {
		return driveType;
	}
	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}
	public Fitment getFitment() {
		return fitment;
	}
	public void setFitment(Fitment fitment) {
		this.fitment = fitment;
	}
	public String getEngineDisplacementLiters() {
		return engineDisplacementLiters;
	}
	public void setEngineDisplacementLiters(String engineDisplacementLiters) {
		this.engineDisplacementLiters = engineDisplacementLiters;
	}
	
	public String getEngineDisplacementCC() {
		return engineDisplacementCC;
	}
	public void setEngineDisplacementCC(String engineDisplacementCC) {
		this.engineDisplacementCC = engineDisplacementCC;
	}
	public String getEngineNumCylinders() {
		return engineNumCylinders;
	}
	public void setEngineNumCylinders(String engineNumCylinders) {
		this.engineNumCylinders = engineNumCylinders;
	}
	public String getEngineBlockType() {
		return engineBlockType;
	}
	public void setEngineBlockType(String engineBlockType) {
		this.engineBlockType = engineBlockType;
	}
	public String getValvesPerEngine() {
		return valvesPerEngine;
	}
	public void setValvesPerEngine(String valvesPerEngine) {
		this.valvesPerEngine = valvesPerEngine;
	}
	public String getEngineFuelType() {
		return engineFuelType;
	}
	public void setEngineFuelType(String engineFuelType) {
		this.engineFuelType = engineFuelType;
	}
	
	public String getEngineFuelControlType() {
		return engineFuelControlType;
	}
	public void setEngineFuelControlType(String engineFuelControlType) {
		this.engineFuelControlType = engineFuelControlType;
	}
	public String getEngineFuelSystemDesign() {
		return engineFuelSystemDesign;
	}
	public void setEngineFuelSystemDesign(String engineFuelSystemDesign) {
		this.engineFuelSystemDesign = engineFuelSystemDesign;
	}
	public String getEngineHorsepower() {
		return engineHorsepower;
	}
	public void setEngineHorsepower(String engineHorsepower) {
		this.engineHorsepower = engineHorsepower;
	}
	public String getFrontSpringType() {
		return frontSpringType;
	}
	public void setFrontSpringType(String frontSpringType) {
		this.frontSpringType = frontSpringType;
	}
	public String getRearSpringType() {
		return rearSpringType;
	}
	public void setRearSpringType(String rearSpringType) {
		this.rearSpringType = rearSpringType;
	}
	public String getSteeringSystemName() {
		return steeringSystemName;
	}
	public void setSteeringSystemName(String steeringSystemName) {
		this.steeringSystemName = steeringSystemName;
	}
	public String getTransmissionControlType() {
		return transmissionControlType;
	}
	public void setTransmissionControlType(String transmissionControlType) {
		this.transmissionControlType = transmissionControlType;
	}
	public String getTransmissionNumSpeeds() {
		return transmissionNumSpeeds;
	}
	public void setTransmissionNumSpeeds(String transmissionNumSpeeds) {
		this.transmissionNumSpeeds = transmissionNumSpeeds;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
}