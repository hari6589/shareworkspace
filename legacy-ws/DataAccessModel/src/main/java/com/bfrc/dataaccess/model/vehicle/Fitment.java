package com.bfrc.dataaccess.model.vehicle;

public class Fitment {

	private Long carTireId;
	private Long acesVehicleId;
     private Long makeId;
     private Long modelId;
     private Long submodelId;
     private String makeName;
     private String modelYear;
     private String modelName;
     private String submodel;
     private String standardInd;
     private String frb;
     private String vehtype;
     private String speedRating;
     private String crossSection;
     private String aspect;
     private String rimSize;
     private String tireSize;
     private String loadIndex;
     private String loadRange;
	private String notes;
	private boolean tpmsInd;
	private String frontInf;
	private String rearInf;

	
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

	public String getStandardInd() {
		return standardInd;
	}

	public void setStandardInd(String standardInd) {
		this.standardInd = standardInd;
	}

	public String getFrb() {
		return frb;
	}

	public void setFrb(String frb) {
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	

	public boolean isTpmsInd() {
		return tpmsInd;
	}

	public void setTpmsInd(boolean tpmsInd) {
		this.tpmsInd = tpmsInd;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acesVehicleId == null) ? 0 : acesVehicleId.hashCode());
		result = prime * result + ((aspect == null) ? 0 : aspect.hashCode());
		result = prime * result
				+ ((crossSection == null) ? 0 : crossSection.hashCode());
		result = prime * result + ((frb == null) ? 0 : frb.hashCode());
		result = prime * result
				+ ((frontInf == null) ? 0 : frontInf.hashCode());
		result = prime * result
				+ ((loadIndex == null) ? 0 : loadIndex.hashCode());
		result = prime * result
				+ ((loadRange == null) ? 0 : loadRange.hashCode());
		result = prime * result + ((makeId == null) ? 0 : makeId.hashCode());
		result = prime * result
				+ ((makeName == null) ? 0 : makeName.hashCode());
		result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
		result = prime * result
				+ ((modelName == null) ? 0 : modelName.hashCode());
		result = prime * result
				+ ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((rearInf == null) ? 0 : rearInf.hashCode());
		result = prime * result + ((rimSize == null) ? 0 : rimSize.hashCode());
		result = prime * result
				+ ((speedRating == null) ? 0 : speedRating.hashCode());
		result = prime * result
				+ ((standardInd == null) ? 0 : standardInd.hashCode());
		result = prime * result
				+ ((submodel == null) ? 0 : submodel.hashCode());
		result = prime * result
				+ ((submodelId == null) ? 0 : submodelId.hashCode());
		result = prime * result
				+ ((tireSize == null) ? 0 : tireSize.hashCode());
		result = prime * result + (tpmsInd ? 1231 : 1237);
		result = prime * result + ((vehtype == null) ? 0 : vehtype.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fitment other = (Fitment) obj;
		if (acesVehicleId == null) {
			if (other.acesVehicleId != null)
				return false;
		} else if (!acesVehicleId.equals(other.acesVehicleId))
			return false;
		if (aspect == null) {
			if (other.aspect != null)
				return false;
		} else if (!aspect.equals(other.aspect))
			return false;
		if (crossSection == null) {
			if (other.crossSection != null)
				return false;
		} else if (!crossSection.equals(other.crossSection))
			return false;
		if (frb == null) {
			if (other.frb != null)
				return false;
		} else if (!frb.equals(other.frb))
			return false;
		if (frontInf == null) {
			if (other.frontInf != null)
				return false;
		} else if (!frontInf.equals(other.frontInf))
			return false;
		if (loadIndex == null) {
			if (other.loadIndex != null)
				return false;
		} else if (!loadIndex.equals(other.loadIndex))
			return false;
		if (loadRange == null) {
			if (other.loadRange != null)
				return false;
		} else if (!loadRange.equals(other.loadRange))
			return false;
		if (makeId == null) {
			if (other.makeId != null)
				return false;
		} else if (!makeId.equals(other.makeId))
			return false;
		if (makeName == null) {
			if (other.makeName != null)
				return false;
		} else if (!makeName.equals(other.makeName))
			return false;
		if (modelId == null) {
			if (other.modelId != null)
				return false;
		} else if (!modelId.equals(other.modelId))
			return false;
		if (modelName == null) {
			if (other.modelName != null)
				return false;
		} else if (!modelName.equals(other.modelName))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (rearInf == null) {
			if (other.rearInf != null)
				return false;
		} else if (!rearInf.equals(other.rearInf))
			return false;
		if (rimSize == null) {
			if (other.rimSize != null)
				return false;
		} else if (!rimSize.equals(other.rimSize))
			return false;
		if (speedRating == null) {
			if (other.speedRating != null)
				return false;
		} else if (!speedRating.equals(other.speedRating))
			return false;
		if (standardInd == null) {
			if (other.standardInd != null)
				return false;
		} else if (!standardInd.equals(other.standardInd))
			return false;
		if (submodel == null) {
			if (other.submodel != null)
				return false;
		} else if (!submodel.equals(other.submodel))
			return false;
		if (submodelId == null) {
			if (other.submodelId != null)
				return false;
		} else if (!submodelId.equals(other.submodelId))
			return false;
		if (tireSize == null) {
			if (other.tireSize != null)
				return false;
		} else if (!tireSize.equals(other.tireSize))
			return false;
		if (tpmsInd != other.tpmsInd)
			return false;
		if (vehtype == null) {
			if (other.vehtype != null)
				return false;
		} else if (!vehtype.equals(other.vehtype))
			return false;
		return true;
	}


}
