package com.bfrc.dataaccess.model.aces;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id", "vehicleTypeGroup"})
public class VehicleTypeGroup {
	
	private Long vehicleTypeGroupId;
	private String vehicleTypeGroup;
	
	@JsonProperty("id")
	public Long getVehicleTypeGroupId() {
		return vehicleTypeGroupId;
	}
	@JsonProperty("id")
	public void setVehicleTypeGroupId(Long vehicleTypeGroupId) {
		this.vehicleTypeGroupId = vehicleTypeGroupId;
	}
	public String getVehicleTypeGroup() {
		return vehicleTypeGroup;
	}
	public void setVehicleTypeGroup(String vehicleTypeGroup) {
		this.vehicleTypeGroup = vehicleTypeGroup;
	}

}
