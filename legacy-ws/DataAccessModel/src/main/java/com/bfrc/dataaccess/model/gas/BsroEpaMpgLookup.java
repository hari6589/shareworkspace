package com.bfrc.dataaccess.model.gas;

import java.math.BigDecimal;

public class BsroEpaMpgLookup {

	private Long lookupId;
	private String modelYear;
	private String makeName;
	private String modelName;
	private String submodel;
	private Short mpgCity;
	private Short mpgHwy;
	private Short mpgCombined;
	private BigDecimal engineSize;
	private Integer cylinders;
	private String fuelTypeCd;
	private String transmissionCd;
	private String driveCd;
	private Integer annualCost;
	
	/*
	 * The following are NOT a part of the Hibernate mapping
	 */
	private String fuelType;
	private String transmission;
	private String drive;
	
	public Long getLookupId() {
		return lookupId;
	}
	public void setLookupId(Long lookupId) {
		this.lookupId = lookupId;
	}
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getMakeName() {
		return makeName;
	}
	public void setMakeName(String makeName) {
		this.makeName = makeName;
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
	public Short getMpgCity() {
		return mpgCity;
	}
	public void setMpgCity(Short mpgCity) {
		this.mpgCity = mpgCity;
	}
	public Short getMpgHwy() {
		return mpgHwy;
	}
	public void setMpgHwy(Short mpgHwy) {
		this.mpgHwy = mpgHwy;
	}
	public Short getMpgCombined() {
		return mpgCombined;
	}
	public void setMpgCombined(Short mpgCombined) {
		this.mpgCombined = mpgCombined;
	}
	public BigDecimal getEngineSize() {
		return engineSize;
	}
	public void setEngineSize(BigDecimal engineSize) {
		this.engineSize = engineSize;
	}
	public Integer getCylinders() {
		return cylinders;
	}
	public void setCylinders(Integer cylinders) {
		this.cylinders = cylinders;
	}
	public String getFuelTypeCd() {
		return fuelTypeCd;
	}
	public void setFuelTypeCd(String fuelCd) {
		this.fuelTypeCd = fuelCd;
	}
	public String getTransmissionCd() {
		return transmissionCd;
	}
	public void setTransmissionCd(String transmissionCd) {
		this.transmissionCd = transmissionCd;
	}
	public String getDriveCd() {
		return driveCd;
	}
	public void setDriveCd(String driveCd) {
		this.driveCd = driveCd;
	}
	public Integer getAnnualCost() {
		return annualCost;
	}
	public void setAnnualCost(Integer annualCost) {
		this.annualCost = annualCost;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getTransmission() {
		return transmission;
	}
	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
	public String getDrive() {
		return drive;
	}
	public void setDrive(String drive) {
		this.drive = drive;
	}
	
	@Override
	public String toString() {
		return "BsroEpaMpgLookup [lookupId=" + lookupId + ", modelYear="
				+ modelYear + ", makeName=" + makeName + ", modelName="
				+ modelName + ", submodel=" + submodel + ", mpgCity=" + mpgCity
				+ ", mpgHwy=" + mpgHwy + ", mpgCombined=" + mpgCombined
				+ ", engineSize=" + engineSize + ", cylinders=" + cylinders
				+ ", fuelTypeCd=" + fuelTypeCd + ", transmissionCd="
				+ transmissionCd + ", driveCd=" + driveCd + ", annualCost="
				+ annualCost + ", fuelType=" + fuelType + ", transmission="
				+ transmission + ", drive=" + drive + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lookupId == null) ? 0 : lookupId.hashCode());
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
		BsroEpaMpgLookup other = (BsroEpaMpgLookup) obj;
		if (lookupId == null) {
			if (other.lookupId != null)
				return false;
		} else if (!lookupId.equals(other.lookupId))
			return false;
		return true;
	}
}
