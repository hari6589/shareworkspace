package app.bsro.model.websiteuservehicle;

import java.io.Serializable;

public class UserSubvehicleTire implements Serializable {

	private static final long serialVersionUID = 1830575697343431403L;
	
	private String id;
	private String year;
	private String make;
	private String model;
	private String submodel;
	private Boolean hasTpms;
	
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
	public String getSubmodel() {
		return submodel;
	}
	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}

	public Boolean getHasTpms() {
		return hasTpms;
	}
	public void setHasTpms(Boolean hasTpms) {
		this.hasTpms = hasTpms;
	}


}
