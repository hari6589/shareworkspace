package com.bsro.databean.vehicle;

public class TireVehicle {
    private Long acesVehicleId;

    private String makeName;
    private Long makeId;
    private String modelName;
    private Long modelId;
    private String submodelName;
    private String year;
    private Integer hasTpms;
    
	
	public Long getAcesVehicleId() {
		return acesVehicleId;
	}
	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}
	public String getMakeName() {
		return makeName;
	}
	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	public Long getMakeId() {
		return makeId;
	}
	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public String getSubmodelName() {
		return submodelName;
	}
	public void setSubmodelName(String submodelName) {
		this.submodelName = submodelName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getHasTpms() {
		return hasTpms;
	}
	public void setHasTpms(Integer hasTpms) {
		this.hasTpms = hasTpms;
	}
}
