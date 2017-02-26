/**
 * 
 */
package com.bfrc.dataaccess.model.aces;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id", "modelName", "vehicleType"})
public class VehicleModel {
	
	private Long modelId;
	
	private String modelName;
	
	private VehicleType vehicleType;

	@JsonProperty("id")
	public Long getModelId() {
		return modelId;
	}

	@JsonProperty("id")
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	
}
