package com.bfrc.pojo.survey;
// Generated Jan 23, 2012 11:49:47 AM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * MindshareTiresurveyDetails generated by hbm2java
 */
public class MindshareTiresurveyDetails  implements java.io.Serializable {


     private String salesLine;
     private String brandName;
     private BigDecimal percentWillPurchaseAgain;
     private BigDecimal dryTraction;
     private BigDecimal wetTraction;
     private BigDecimal tractionInSnowIce;
     private BigDecimal tireStability;
     private BigDecimal noiseLevel;
     private BigDecimal rideComfort;
     private BigDecimal tireWear;
     private Integer numberOfComments;
     private Integer numberOfSurveys;
     
     private long id;

    public MindshareTiresurveyDetails() {
    }

	
    public MindshareTiresurveyDetails(String salesLine) {
        this.salesLine = salesLine;
    }
    public MindshareTiresurveyDetails(String salesLine, String brandName, BigDecimal percentWillPurchaseAgain, BigDecimal dryTraction, BigDecimal wetTraction, BigDecimal tractionInSnowIce, BigDecimal tireStability, BigDecimal noiseLevel, BigDecimal rideComfort, BigDecimal tireWear, Integer numberOfComments, Integer numberOfSurveys) {
       this.salesLine = salesLine;
       this.brandName = brandName;
       this.percentWillPurchaseAgain = percentWillPurchaseAgain;
       this.dryTraction = dryTraction;
       this.wetTraction = wetTraction;
       this.tractionInSnowIce = tractionInSnowIce;
       this.tireStability = tireStability;
       this.noiseLevel = noiseLevel;
       this.rideComfort = rideComfort;
       this.tireWear = tireWear;
       this.numberOfComments = numberOfComments;
       this.numberOfSurveys = numberOfSurveys;
    }
   
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getSalesLine() {
        return this.salesLine;
    }
    
    public void setSalesLine(String salesLine) {
        this.salesLine = salesLine;
    }
    public String getBrandName() {
        return this.brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public BigDecimal getPercentWillPurchaseAgain() {
        return this.percentWillPurchaseAgain;
    }
    
    public void setPercentWillPurchaseAgain(BigDecimal percentWillPurchaseAgain) {
        this.percentWillPurchaseAgain = percentWillPurchaseAgain;
    }
    public BigDecimal getDryTraction() {
        return this.dryTraction;
    }
    
    public void setDryTraction(BigDecimal dryTraction) {
        this.dryTraction = dryTraction;
    }
    public BigDecimal getWetTraction() {
        return this.wetTraction;
    }
    
    public void setWetTraction(BigDecimal wetTraction) {
        this.wetTraction = wetTraction;
    }
    public BigDecimal getTractionInSnowIce() {
        return this.tractionInSnowIce;
    }
    
    public void setTractionInSnowIce(BigDecimal tractionInSnowIce) {
        this.tractionInSnowIce = tractionInSnowIce;
    }
    public BigDecimal getTireStability() {
        return this.tireStability;
    }
    
    public void setTireStability(BigDecimal tireStability) {
        this.tireStability = tireStability;
    }
    public BigDecimal getNoiseLevel() {
        return this.noiseLevel;
    }
    
    public void setNoiseLevel(BigDecimal noiseLevel) {
        this.noiseLevel = noiseLevel;
    }
    public BigDecimal getRideComfort() {
        return this.rideComfort;
    }
    
    public void setRideComfort(BigDecimal rideComfort) {
        this.rideComfort = rideComfort;
    }
    public BigDecimal getTireWear() {
        return this.tireWear;
    }
    
    public void setTireWear(BigDecimal tireWear) {
        this.tireWear = tireWear;
    }
    public Integer getNumberOfComments() {
        return this.numberOfComments;
    }
    
    public void setNumberOfComments(Integer numberOfComments) {
        this.numberOfComments = numberOfComments;
    }
    public Integer getNumberOfSurveys() {
        return this.numberOfSurveys;
    }
    
    public void setNumberOfSurveys(Integer numberOfSurveys) {
        this.numberOfSurveys = numberOfSurveys;
    }




}


