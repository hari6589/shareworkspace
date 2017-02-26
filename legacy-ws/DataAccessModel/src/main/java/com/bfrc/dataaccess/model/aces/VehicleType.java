package com.bfrc.dataaccess.model.aces;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id", "vehicleType"})
public class VehicleType {
	
	private Long vehicleTypeId;
	
	private String vehicleType;
	
	private VehicleTypeGroup vehicleTypeGroup;

	@JsonProperty("id")
	public Long getVehicleTypeId() {
		return vehicleTypeId;
	}

	@JsonProperty("id")
	public void setVehicleTypeId(Long vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleTypeGroup getVehicleTypeGroup() {
		return vehicleTypeGroup;
	}

	public void setVehicleTypeGroup(VehicleTypeGroup vehicleTypeGroup) {
		this.vehicleTypeGroup = vehicleTypeGroup;
	}
	
	
}
