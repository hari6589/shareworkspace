package app.bsro.model.websiteuservehicle;

import java.io.Serializable;

public class UserSubvehicleBattery implements Serializable {
	private static final long serialVersionUID = -1101917262749655654L;
	
	private String id;
	private String year;
	private String make;
	private String model;
	private String engine;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
}
