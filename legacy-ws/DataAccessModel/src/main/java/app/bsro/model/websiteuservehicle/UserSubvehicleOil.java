package app.bsro.model.websiteuservehicle;

import java.io.Serializable;

public class UserSubvehicleOil implements Serializable {

	private static final long serialVersionUID = 7900952672071789357L;
	
	private String id;
	private String year;
	private String make;
	private String makeId;
	private String modelSubmodelEngine;
	
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
	public String getMakeId() {
		return makeId;
	}
	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}
	public String getModelSubmodelEngine() {
		return modelSubmodelEngine;
	}
	public void setModelSubmodelEngine(String modelSubmodelEngine) {
		this.modelSubmodelEngine = modelSubmodelEngine;
	}
}
