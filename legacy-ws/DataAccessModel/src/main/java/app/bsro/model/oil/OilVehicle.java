package app.bsro.model.oil;

import java.io.Serializable;

public class OilVehicle implements Serializable {
	private static final long serialVersionUID = 5801712016345849395L;
	
	private String vehicleId;
    private String year;
    private String make;
    private String modelAndSubmodelAndEngine;
    
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
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
	public String getModelAndSubmodelAndEngine() {
		return modelAndSubmodelAndEngine;
	}
	public void setModelAndSubmodelAndEngine(String modelAndSubmodelAndEngine) {
		this.modelAndSubmodelAndEngine = modelAndSubmodelAndEngine;
	}

}
