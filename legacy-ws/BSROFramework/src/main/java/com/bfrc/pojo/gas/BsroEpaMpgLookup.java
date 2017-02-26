package com.bfrc.pojo.gas;
// Generated Apr 3, 2011 11:23:40 AM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * BsroEpaMpgLookup generated by hbm2java
 */
public class BsroEpaMpgLookup  implements java.io.Serializable {

     /**
	 * 
	 */
	private static final long serialVersionUID = 8974892654385201433L;
	private long lookupId;
     private String modelYear;
     private String makeName;
     private String modelName;
     private String submodel;
     private Short mpgCity;
     private Short mpgHwy;
     private Short mpgCombined;
     private BigDecimal engineSize;
     private Short cylinders;
     private String fuelType;
     private String transmission;
     private String drive;
     private Integer annualCost;

    public BsroEpaMpgLookup() {
    }

	
    public BsroEpaMpgLookup(long lookupId, String modelYear, String makeName, String modelName) {
        this.lookupId = lookupId;
        this.modelYear = modelYear;
        this.makeName = makeName;
        this.modelName = modelName;
    }
    public BsroEpaMpgLookup(long lookupId, String modelYear, String makeName, String modelName, String submodel, Short mpgCity, Short mpgHwy, Short mpgCombined, BigDecimal engineSize, Short cylinders, String fuelType, String transmission, String drive, Integer annualCost) {
       this.lookupId = lookupId;
       this.modelYear = modelYear;
       this.makeName = makeName;
       this.modelName = modelName;
       this.submodel = submodel;
       this.mpgCity = mpgCity;
       this.mpgHwy = mpgHwy;
       this.mpgCombined = mpgCombined;
       this.engineSize = engineSize;
       this.cylinders = cylinders;
       this.fuelType = fuelType;
       this.transmission = transmission;
       this.drive = drive;
       this.annualCost = annualCost;
    }
   
    public long getLookupId() {
        return this.lookupId;
    }
    
    public void setLookupId(long lookupId) {
        this.lookupId = lookupId;
    }
    public String getModelYear() {
        return this.modelYear;
    }
    
    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }
    public String getMakeName() {
        return this.makeName;
    }
    
    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }
    public String getModelName() {
        return this.modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getSubmodel() {
        return this.submodel;
    }
    
    public void setSubmodel(String submodel) {
        this.submodel = submodel;
    }
    public Short getMpgCity() {
        return this.mpgCity;
    }
    
    public void setMpgCity(Short mpgCity) {
        this.mpgCity = mpgCity;
    }
    public Short getMpgHwy() {
        return this.mpgHwy;
    }
    
    public void setMpgHwy(Short mpgHwy) {
        this.mpgHwy = mpgHwy;
    }
    public Short getMpgCombined() {
        return this.mpgCombined;
    }
    
    public void setMpgCombined(Short mpgCombined) {
        this.mpgCombined = mpgCombined;
    }
    public BigDecimal getEngineSize() {
        return this.engineSize;
    }
    
    public void setEngineSize(BigDecimal engineSize) {
        this.engineSize = engineSize;
    }
    public Short getCylinders() {
        return this.cylinders;
    }
    
    public void setCylinders(Short cylinders) {
        this.cylinders = cylinders;
    }
    public String getFuelType() {
        return this.fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    public String getTransmission() {
        return this.transmission;
    }
    
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    public String getDrive() {
        return this.drive;
    }
    
    public void setDrive(String drive) {
        this.drive = drive;
    }
    public Integer getAnnualCost() {
        return this.annualCost;
    }
    
    public void setAnnualCost(Integer annualCost) {
        this.annualCost = annualCost;
    }
}

