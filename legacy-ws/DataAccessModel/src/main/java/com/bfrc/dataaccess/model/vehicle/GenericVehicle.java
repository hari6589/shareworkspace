package com.bfrc.dataaccess.model.vehicle;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GenericVehicle {
    private String year;
    private String make;
    private String model;
    private String submodel;
    private String engine;
    
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
}
