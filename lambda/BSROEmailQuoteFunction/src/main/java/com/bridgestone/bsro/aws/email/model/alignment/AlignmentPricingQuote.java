package com.bridgestone.bsro.aws.email.model.alignment;

import java.math.BigDecimal;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AlignmentPricingQuote  implements java.io.Serializable {


  private long alignmentQuoteId;
  private String firstName;
  private String lastName;
  private Long storeNumber;
  private Short vehicleYear;
  private String vehicleMake;
  private String vehicleModel;
  private String vehicleSubmodel;
  private String zip;
  private Short alignmentPricingId;
  private String pricingName;
  private Long article;
  private BigDecimal price;
  private String webSite;
  private BigDecimal mowaaAmount;
  private Date createdDate;
  private String donationName;
  private BigDecimal donationAmount;
  private Long donationArticle;
  private String emailAddress;
  
  
 public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public AlignmentPricingQuote() {
 }

	
 public AlignmentPricingQuote(long alignmentQuoteId) {
     this.alignmentQuoteId = alignmentQuoteId;
 }
 public AlignmentPricingQuote(long alignmentQuoteId, String firstName, String lastName, Long storeNumber, Short vehicleYear, String vehicleMake, String vehicleModel, String vehicleSubmodel, String zip, Short alignmentPricingId, String pricingName, Long article, BigDecimal price, String webSite, BigDecimal mowaaAmount, Date createdDate) {
    this.alignmentQuoteId = alignmentQuoteId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.storeNumber = storeNumber;
    this.vehicleYear = vehicleYear;
    this.vehicleMake = vehicleMake;
    this.vehicleModel = vehicleModel;
    this.vehicleSubmodel = vehicleSubmodel;
    this.zip = zip;
    this.alignmentPricingId = alignmentPricingId;
    this.pricingName = pricingName;
    this.article = article;
    this.price = price;
    this.webSite = webSite;
    this.mowaaAmount = mowaaAmount;
    this.createdDate = createdDate;
 }

 public long getAlignmentQuoteId() {
     return this.alignmentQuoteId;
 }
 
 public void setAlignmentQuoteId(long alignmentQuoteId) {
     this.alignmentQuoteId = alignmentQuoteId;
 }
 public String getFirstName() {
 	//return StringUtils.userNameFilter(this.firstName,50);
	 return this.firstName;
 }
 
 public void setFirstName(String firstName) {
     this.firstName = firstName;
 }
 public String getLastName() {
 	//return StringUtils.userNameFilter(this.lastName,80);
	 return this.lastName;
 }
 
 public void setLastName(String lastName) {
     this.lastName = lastName;
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
 	//return StringUtils.truncate(this.vehicleMake,30);
 	return this.vehicleMake;
 }
 
 public void setVehicleMake(String vehicleMake) {
     this.vehicleMake = vehicleMake;
 }
 public String getVehicleModel() {
 	//return StringUtils.truncate(this.vehicleModel,40);
	 return this.vehicleModel;
 }
 
 public void setVehicleModel(String vehicleModel) {
     this.vehicleModel = vehicleModel;
 }
 public String getVehicleSubmodel() {
     //return StringUtils.truncate(this.vehicleSubmodel,80);
	 return this.vehicleSubmodel;
 }
 
 public void setVehicleSubmodel(String vehicleSubmodel) {
     this.vehicleSubmodel = vehicleSubmodel;
 }
 public String getZip() {
 	//return StringUtils.truncate(this.zip,10);
 	return this.zip;
 }
 
 public void setZip(String zip) {
     this.zip = zip;
 }
 
 public Short getAlignmentPricingId() {
     return this.alignmentPricingId;
 }
 
 public void setAlignmentPricingId(Short alignmentPricingId) {
     this.alignmentPricingId = alignmentPricingId;
 }
 public String getPricingName() {
     return this.pricingName;
 }
 
 public void setPricingName(String pricingName) {
     this.pricingName = pricingName;
 }
 
 public Long getArticle() {
     return this.article;
 }
 
 public void setArticle(Long article) {
     this.article = article;
 }
 
 public BigDecimal getPrice() {
     return this.price;
 }
 
 public void setPrice(BigDecimal price) {
     this.price = price;
 }
 public String getWebSite() {
     return this.webSite;
 }
 
 public void setWebSite(String webSite) {
     this.webSite = webSite;
 }
 
 public BigDecimal getMowaaAmount() {
     return this.mowaaAmount;
 }
 
 public void setMowaaAmount(BigDecimal mowaaAmount) {
     this.mowaaAmount = mowaaAmount;
 }
 
 public Date getCreatedDate() {
     return this.createdDate;
 }
 
 public void setCreatedDate(Date createdDate) {
     this.createdDate = createdDate;
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

}


