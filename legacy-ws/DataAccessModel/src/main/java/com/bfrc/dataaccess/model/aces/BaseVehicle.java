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
@JsonPropertyOrder({"id", "year", "make", "model"})
public class BaseVehicle {
	
	private Long baseVehicleId;
	
	private Integer year;
	
	private VehicleMake make;
	
	private VehicleModel model;

	@JsonProperty("id")
	public Long getBaseVehicleId() {
		return baseVehicleId;
	}
	@JsonProperty("id")
	public void setBaseVehicleId(Long baseVehicleId) {
		this.baseVehicleId = baseVehicleId;
	}


	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public VehicleMake getMake() {
		return make;
	}

	public void setMake(VehicleMake make) {
		this.make = make;
	}

	public VehicleModel getModel() {
		return model;
	}

	public void setModel(VehicleModel model) {
		this.model = model;
	}
	@Override
	public String toString() {
		return "BaseVehicle [baseVehicleId=" + baseVehicleId + ", year=" + year
				+ ", make=" + make + ", model=" + model + "]";
	}
	
}
