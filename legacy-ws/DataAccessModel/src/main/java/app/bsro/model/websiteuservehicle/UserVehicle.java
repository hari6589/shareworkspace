package app.bsro.model.websiteuservehicle;

import java.io.Serializable;

public class UserVehicle implements Serializable {

	private static final long serialVersionUID = 1148677374045714853L;
	
	private Long id;
	private String displayName;
	private String fullName;
	private String year;
	private String make;
	private String model;
	private String submodel;
	private String engine;
	private UserSubvehicleTire tire;
	private UserSubvehicleOil oil;
	private UserSubvehicleBattery battery;
	

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public UserSubvehicleTire getTire() {
		return tire;
	}
	public void setTire(UserSubvehicleTire tire) {
		this.tire = tire;
	}
	public UserSubvehicleOil getOil() {
		return oil;
	}
	public void setOil(UserSubvehicleOil oil) {
		this.oil = oil;
	}
	public UserSubvehicleBattery getBattery() {
		return battery;
	}
	public void setBattery(UserSubvehicleBattery battery) {
		this.battery = battery;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSubmodel() {
		return submodel;
	}
	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	
}
