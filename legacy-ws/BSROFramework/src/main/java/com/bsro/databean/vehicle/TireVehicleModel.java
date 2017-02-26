package com.bsro.databean.vehicle;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TireVehicleModel {
	private Long modelId;
	private Long makeId;
	private String name;
	private String friendlyName;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getMakeId() {
		return makeId;
	}
	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
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


}
