package com.bsro.pojo.form;

import java.io.Serializable;

import list.Years;

import com.bfrc.pojo.UserVehicle;

public class UserForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long vehicleId;
	private Years vehicleYears;
	private com.bfrc.pojo.User user;
	private UserVehicle vehicle;
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Years getVehicleYears() {
		return vehicleYears;
	}
	public void setVehicleYears(Years vehicleYears) {
		this.vehicleYears = vehicleYears;
	}
	public com.bfrc.pojo.User getUser() {
		return user;
	}
	public void setUser(com.bfrc.pojo.User user) {
		this.user = user;
	}
	public UserVehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(UserVehicle vehicle) {
		this.vehicle = vehicle;
	}
	

}
