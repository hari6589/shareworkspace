package com.bfrc.dataaccess.model.aces;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"acesId", "baseVehicle", "submodel"})
public class Vehicle {
	
	private Long acesVehicleId;
	
	private BaseVehicle baseVehicle;
	
	private VehicleSubmodel submodel;

	@JsonProperty("acesId")
	public Long getAcesVehicleId() {
		return acesVehicleId;
	}

	@JsonProperty("acesId")
	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}

	public BaseVehicle getBaseVehicle() {
		return baseVehicle;
	}

	public void setBaseVehicle(BaseVehicle baseVehicle) {
		this.baseVehicle = baseVehicle;
	}

	public VehicleSubmodel getSubmodel() {
		return submodel;
	}

	public void setSubmodel(VehicleSubmodel submodel) {
		this.submodel = submodel;
	}
	
}
