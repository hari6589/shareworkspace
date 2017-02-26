package com.bsro.databean.vehicle;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

public class TireVehicleMake {
	private Long makeId;
	private String name;
	private String friendlyName;
	
	@JsonSerialize(using=ToStringSerializer.class)
	public Long getMakeId() {
		return makeId;
	}
	public void setMakeId(Long makeId) {
		this.makeId = makeId;
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
