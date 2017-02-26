package com.bsro.databean.vehicle;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TireVehicleSubModel {
	private String name;
	private String friendlyName;
	// this is the PARENT's ID...there is no "submodel id"
	private Long modelId;
	private Long submodelId;
	private Long acesVehicleId;
	private Integer tpmsInd;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getSubmodelId() {
		return submodelId;
	}

	public void setSubmodelId(Long submodelId) {
		this.submodelId = submodelId;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getAcesVehicleId() {
		return acesVehicleId;
	}

	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}
	
	@JsonSerialize(using=ToStringSerializer.class)
	public Integer getTpmsInd() {
		return tpmsInd;
	}

	public void setTpmsInd(Integer tpmsInd) {
		this.tpmsInd = tpmsInd;
	}
}
