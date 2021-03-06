package com.bfrc.pojo.interstatebattery;
// Generated Jan 20, 2010 11:12:56 AM by Hibernate Tools 3.2.1.GA

import com.bfrc.framework.util.StringUtils;
import java.math.BigDecimal;
import java.util.Date;

/**
 * InterstateBatteryQuote generated by hbm2java
 */
public class InterstateBatteryQuote  implements java.io.Serializable {


     private long batteryQuoteId;
     private Long storeNumber;
     private Short vehicleYear;
     private String vehicleMake;
     private String vehicleModel;
     private String vehicleSubmodel;
     private String vehicleEngine;
     private String zip;
     private String productCode;
     private Long partNumber;
     private Long totalWarranty;
     private Long replacementWarranty;
     private Long cca;
     private Long rcMinutes;
     private String product;
     private BigDecimal webPrice;
     private BigDecimal tradeinCredit;
     private BigDecimal installationAmt;
     private String webSite;
     private Date createdDate;
   //mowaa donation cxs
     private BigDecimal mowaaAmount;
     private String firstName;
     private String lastName;
     private String donationName;
     private BigDecimal donationAmount;
     private Long donationArticle;
     private BigDecimal regularPrice;
     private Long discountArticle;
     private BigDecimal discountAmount;
     private String emailAddress;
     private Short quantity;
     private BigDecimal totalPrice;
     
	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public InterstateBatteryQuote() {
    }

	
    public InterstateBatteryQuote(long batteryQuoteId) {
        this.batteryQuoteId = batteryQuoteId;
    }
    public InterstateBatteryQuote(long batteryQuoteId, Long storeNumber, Short vehicleYear, String vehicleMake, String vehicleModel, String vehicleSubmodel, String vehicleEngine, String zip, String productCode, Long partNumber, Long totalWarranty, Long replacementWarranty, Long cca, Long rcMinutes, String product, BigDecimal webPrice, BigDecimal tradeinCredit, BigDecimal installationAmt, String webSite, Short quantity, BigDecimal totalPrice) {
       this.batteryQuoteId = batteryQuoteId;
       this.storeNumber = storeNumber;
       this.vehicleYear = vehicleYear;
       this.vehicleMake = vehicleMake;
       this.vehicleModel = vehicleModel;
       this.vehicleSubmodel = vehicleSubmodel;
       this.vehicleEngine = vehicleEngine;
       this.zip = zip;
       this.productCode = productCode;
       this.partNumber = partNumber;
       this.totalWarranty = totalWarranty;
       this.replacementWarranty = replacementWarranty;
       this.cca = cca;
       this.rcMinutes = rcMinutes;
       this.product = product;
       this.webPrice = webPrice;
       this.tradeinCredit = tradeinCredit;
       this.installationAmt = installationAmt;
       this.webSite = webSite;
       this.quantity = quantity;
       this.totalPrice = totalPrice;
    }
   
    public long getBatteryQuoteId() {
        return this.batteryQuoteId;
    }
    
    public void setBatteryQuoteId(long batteryQuoteId) {
        this.batteryQuoteId = batteryQuoteId;
    }
    public Long getStoreNumber() {
        return this.storeNumber;
    }
    
    public void setStoreNumber(Long storeNumber) {
        this.storeNumber = storeNumber;
    }
    public Short getVehicleYear() {
        return this.vehicleYear;
    }
    
    public void setVehicleYear(Short vehicleYear) {
        this.vehicleYear = vehicleYear;
    }
    public String getVehicleMake() {
        return this.vehicleMake;
    }
    
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }
    public String getVehicleModel() {
        return this.vehicleModel;
    }
    
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
    public String getVehicleSubmodel() {
        return this.vehicleSubmodel;
    }
    
    public void setVehicleSubmodel(String vehicleSubmodel) {
        this.vehicleSubmodel = vehicleSubmodel;
    }
    public String getVehicleEngine() {
        return this.vehicleEngine;
    }
    
    public void setVehicleEngine(String vehicleEngine) {
        this.vehicleEngine = vehicleEngine;
    }
    public String getZip() {
        return this.zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getProductCode() {
        return this.productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public Long getPartNumber() {
        return this.partNumber;
    }
    
    public void setPartNumber(Long partNumber) {
        this.partNumber = partNumber;
    }
    public Long getTotalWarranty() {
        return this.totalWarranty;
    }
    
    public void setTotalWarranty(Long totalWarranty) {
        this.totalWarranty = totalWarranty;
    }
    public Long getReplacementWarranty() {
        return this.replacementWarranty;
    }
    
    public void setReplacementWarranty(Long replacementWarranty) {
        this.replacementWarranty = replacementWarranty;
    }
    public Long getCca() {
        return this.cca;
    }
    
    public void setCca(Long cca) {
        this.cca = cca;
    }
    public Long getRcMinutes() {
        return this.rcMinutes;
    }
    
    public void setRcMinutes(Long rcMinutes) {
        this.rcMinutes = rcMinutes;
    }
    public String getProduct() {
        return this.product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    public BigDecimal getWebPrice() {
        return this.webPrice;
    }
    
    public void setWebPrice(BigDecimal webPrice) {
        this.webPrice = webPrice;
    }
    public BigDecimal getTradeinCredit() {
        return this.tradeinCredit;
    }
    
    public void setTradeinCredit(BigDecimal tradeinCredit) {
        this.tradeinCredit = tradeinCredit;
    }
    public BigDecimal getInstallationAmt() {
        return this.installationAmt;
    }
    
    public void setInstallationAmt(BigDecimal installationAmt) {
        this.installationAmt = installationAmt;
    }
    public String getWebSite() {
        return this.webSite;
    }
    
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public BigDecimal getMowaaAmount() {
		return this.mowaaAmount;
	}

	public void setMowaaAmount(BigDecimal mowaaAmount) {
		this.mowaaAmount = mowaaAmount;
	}
	public String getFirstName() {
    	return StringUtils.userNameFilter(this.firstName,50);
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
    	return StringUtils.userNameFilter(this.lastName,80);
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getDonationName() {
        return this.donationName;
    }
    
    public void setDonationName(String donationName) {
        this.donationName = donationName;
    }
    public BigDecimal getDonationAmount() {
        return this.donationAmount;
    }
    
    public void setDonationAmount(BigDecimal donationAmount) {
        this.donationAmount = donationAmount;
    }
    
    public Long getDonationArticle() {
        return this.donationArticle;
    }
    
    public void setDonationArticle(Long donationArticle) {
        this.donationArticle = donationArticle;
    }
    public BigDecimal getRegularPrice() {
        return this.regularPrice;
    }
    
    public void setRegularPrice(BigDecimal regularPrice) {
        this.regularPrice = regularPrice;
    }
    public Long getDiscountArticle() {
        return this.discountArticle;
    }
    
    public void setDiscountArticle(Long discountArticle) {
        this.discountArticle = discountArticle;
    }
    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

	public Short getQuantity() {
		return quantity;
	}

	public void setQuantity(Short quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
    
}


