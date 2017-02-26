package com.bfrc.pojo.tire;

/**
 * Fitment generated by hbm2java
 */

public class Fitment  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Long carTireId;
	private Long acesVehicleId;
     //private Long vehCnfgId;//removed//
     private Long makeId;
     private Long modelId;
     private Long submodelId;
     private String makeName;
     private String modelYear;
     private String modelName;
     private String submodel;
     private Character standardInd;
     private Character frb;
     private String vehtype;
     private String speedRating;
     private String crossSection;
     private String aspect;
     private String rimSize;
     private String tireSize;
     private String loadIndex;
     private String loadRange;
	private String frontInf;
	private String rearInf;
	private Integer tpmsInd;
	private long aaiaId;
    private String notes;

    // Constructors

    /** default constructor */
    public Fitment() {
    }
    

	public Long getCarTireId() {
		return carTireId;
	}


	public void setCarTireId(Long carTireId) {
		this.carTireId = carTireId;
	}


	public Long getAcesVehicleId() {
		return acesVehicleId;
	}


	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}


	public Long getMakeId() {
		return makeId;
	}


	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}


	public Long getModelId() {
		return modelId;
	}


	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}


	public Long getSubmodelId() {
		return submodelId;
	}


	public void setSubmodelId(Long submodelId) {
		this.submodelId = submodelId;
	}


	public String getMakeName() {
		return makeName;
	}


	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}


	public String getModelYear() {
		return modelYear;
	}


	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}


	public String getModelName() {
		return modelName;
	}


	public void setModelName(String modelName) {
		this.modelName = modelName;
	}


	public String getSubmodel() {
		return submodel;
	}


	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}


	public Character getStandardInd() {
		return standardInd;
	}


	public void setStandardInd(Character standardInd) {
		this.standardInd = standardInd;
	}


	public Character getFrb() {
		return frb;
	}


	public void setFrb(Character frb) {
		this.frb = frb;
	}


	public String getVehtype() {
		return vehtype;
	}


	public void setVehtype(String vehtype) {
		this.vehtype = vehtype;
	}


	public String getSpeedRating() {
		return speedRating;
	}


	public void setSpeedRating(String speedRating) {
		this.speedRating = speedRating;
	}


	public String getCrossSection() {
		return crossSection;
	}


	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}


	public String getAspect() {
		return aspect;
	}


	public void setAspect(String aspect) {
		this.aspect = aspect;
	}


	public String getRimSize() {
		return rimSize;
	}


	public void setRimSize(String rimSize) {
		this.rimSize = rimSize;
	}


	public String getTireSize() {
		return tireSize;
	}


	public void setTireSize(String tireSize) {
		this.tireSize = tireSize;
	}


	public String getLoadIndex() {
		return loadIndex;
	}


	public void setLoadIndex(String loadIndex) {
		this.loadIndex = loadIndex;
	}


	public String getLoadRange() {
		return loadRange;
	}


	public void setLoadRange(String loadRange) {
		this.loadRange = loadRange;
	}


	public String getFrontInf() {
		return frontInf;
	}

	public void setFrontInf(String frontInf) {
		this.frontInf = frontInf;
	}

	public String getRearInf() {
		return rearInf;
	}

	public void setRearInf(String rearInf) {
		this.rearInf = rearInf;
	}

	public Integer getTpmsInd() {
		return tpmsInd;
	}

	public void setTpmsInd(Integer tpmsInd) {
		this.tpmsInd = tpmsInd;
	}

	public long getAaiaId() {
		return aaiaId;
	}

	public void setAaiaId(long aaiaId) {
		this.aaiaId = aaiaId;
	}
   
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

	public boolean isBack() {
	    if(frb == 'R' || frb == 'B')
	        return true;
	    return false;
	}
	public boolean isFront() {
	    if(frb == 'F' || frb == 'B')
	        return true;
	    return false;
	}
	
	public String getFitmentText() { 
		return getTireSize() + getLoadRange();
	}
	
	public boolean isTpms() {
		if(tpmsInd != null && tpmsInd.intValue() == 1){
			return true;
		}
		return false;
	}


	@Override
	public String toString() {
		return "Fitment [acesVehicleId=" + acesVehicleId + ", makeId=" + makeId
				+ ", modelId=" + modelId + ", submodelId=" + submodelId
				+ ", makeName=" + makeName + ", modelYear=" + modelYear
				+ ", modelName=" + modelName + ", submodel=" + submodel
				+ ", standardInd=" + standardInd + ", frb=" + frb
				+ ", vehtype=" + vehtype + ", speedRating=" + speedRating
				+ ", crossSection=" + crossSection + ", aspect=" + aspect
				+ ", rimSize=" + rimSize + ", tireSize=" + tireSize
				+ ", loadIndex=" + loadIndex + ", loadRange=" + loadRange
				+ ", frontInf=" + frontInf + ", rearInf=" + rearInf
				+ ", tpmsInd=" + tpmsInd + ", aaiaId=" + aaiaId + ", notes="
				+ notes + "]";
	}
	
}