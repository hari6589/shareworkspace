package com.bfrc.pojo.realestate;
// Generated May 20, 2010 2:51:11 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ser.ToStringSerializer;

/**
 * RealestateSurplusProperty generated by hbm2java
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties({"thumbImage","largeImage", "status", "createdDate", "modifiedDate", "webSite"})
public class RealestateSurplusProperty  implements java.io.Serializable {


     private long propertyId;
     private byte[] thumbImage;
     private byte[] largeImage;
     private String marketType;
     private String propertyType;
     private String area;
     private String subdivision;
     private BigDecimal askingPrice;
     private String askingPriceNote;
     private String address;
     private String city;
     private String state;
     private String zip;
     private String contact;
     private Boolean status;
     private Date createdDate;
     private Date modifiedDate;
     private String webSite;
     private String thumbImageURL;
     private String masterImageURL;

    public RealestateSurplusProperty() {
    }

	
    public RealestateSurplusProperty(long propertyId) {
        this.propertyId = propertyId;
    }
    public RealestateSurplusProperty(long propertyId, byte[] thumbImage, byte[] largeImage, String marketType, String propertyType, String area, String subdivision, BigDecimal askingPrice, String askingPriceNote, String address, String city, String state, String zip, String contact, Boolean status, Date createdDate, Date modifiedDate, String webSite) {
       this.propertyId = propertyId;
       this.thumbImage = thumbImage;
       this.largeImage = largeImage;
       this.marketType = marketType;
       this.propertyType = propertyType;
       this.area = area;
       this.subdivision = subdivision;
       this.askingPrice = askingPrice;
       this.askingPriceNote = askingPriceNote;
       this.address = address;
       this.city = city;
       this.state = state;
       this.zip = zip;
       this.contact = contact;
       this.status = status;
       this.createdDate = createdDate;
       this.modifiedDate = modifiedDate;
       this.webSite = webSite;
    }
   
    @JsonSerialize(using=ToStringSerializer.class)
    public long getPropertyId() {
        return this.propertyId;
    }
    
    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
    public byte[] getThumbImage() {
        return this.thumbImage;
    }
    
    public void setThumbImage(byte[] thumbImage) {
        this.thumbImage = thumbImage;
    }
    public byte[] getLargeImage() {
        return this.largeImage;
    }
    
    public void setLargeImage(byte[] largeImage) {
        this.largeImage = largeImage;
    }
    public String getMarketType() {
        return this.marketType;
    }
    
    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }
    public String getPropertyType() {
        return this.propertyType;
    }
    
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
    public String getArea() {
        return this.area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }
    public String getSubdivision() {
        return this.subdivision;
    }
    
    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }
    
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
    public BigDecimal getAskingPrice() {
        return this.askingPrice;
    }
    
    public void setAskingPrice(BigDecimal askingPrice) {
        this.askingPrice = askingPrice;
    }
    public String getAskingPriceNote() {
        return this.askingPriceNote;
    }
    
    public void setAskingPriceNote(String askingPriceNote) {
        this.askingPriceNote = askingPriceNote;
    }
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    public String getZip() {
        return this.zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getContact() {
        return this.contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    public Boolean getStatus() {
        return this.status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public Date getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public String getWebSite() {
        return this.webSite;
    }
    
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
    
    public String getThumbImageURL() {
    	return this.thumbImageURL;
    }

    public void setThumbImageURL(String thumbImageURL) {
    	this.thumbImageURL = thumbImageURL;
    }

    public String getMasterImageURL() {
    	return this.masterImageURL;
    }
    
    public void setMasterImageURL(String masterImageURL) {
    	this.masterImageURL = masterImageURL;
    }
}


