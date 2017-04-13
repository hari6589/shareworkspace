package com.bridgestone.bsro.aws.email.model.battery;

import java.util.Date;

public class BatteryQuote {
    private long batteryQuoteId;
    private Long storeNumber;
    private String zip;
    private Date createdDate;
    private String firstName;
    private String lastName;
    private String donationName;
    private Double donationAmount;
    private String donationArticle;
    private Short quantity;
    private Double priceForQuantity;
    private Double installationForQuantity;
    private Double subtotal;
    private Double total;
    private Boolean isEligibleForBatteryRebate;
    private Double batteryRebateAmount;
    private Date batteryRebateExpirationDate;
    private Double totalAfterRebate;
    
    private Battery battery;
    private GenericVehicle vehicle;
    
	public long getBatteryQuoteId() {
		return batteryQuoteId;
	}
	public void setBatteryQuoteId(long batteryQuoteId) {
		this.batteryQuoteId = batteryQuoteId;
	}
	
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDonationName() {
		return donationName;
	}
	public void setDonationName(String donationName) {
		this.donationName = donationName;
	}
	
	public Double getDonationAmount() {
		return donationAmount;
	}
	public void setDonationAmount(Double donationAmount) {
		this.donationAmount = donationAmount;
	}
	
	public String getDonationArticle() {
		return donationArticle;
	}
	public void setDonationArticle(String donationArticle) {
		this.donationArticle = donationArticle;
	}
	
	public Double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}
	
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Double getTotalAfterRebate() {
		return totalAfterRebate;
	}
	public void setTotalAfterRebate(Double totalAfterRebate) {
		this.totalAfterRebate = totalAfterRebate;
	}
	
	public Battery getBattery() {
		return battery;
	}
	public void setBattery(Battery battery) {
		this.battery = battery;
	}
	
	public GenericVehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(GenericVehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public Double getBatteryRebateAmount() {
		return batteryRebateAmount;
	}
	public void setBatteryRebateAmount(Double batteryRebateAmount) {
		this.batteryRebateAmount = batteryRebateAmount;
	}
	
	public Date getBatteryRebateExpirationDate() {
		return batteryRebateExpirationDate;
	}
	public void setBatteryRebateExpirationDate(Date batteryRebateExpirationDate) {
		this.batteryRebateExpirationDate = batteryRebateExpirationDate;
	}
	
	public Boolean getIsEligibleForBatteryRebate() {
		return isEligibleForBatteryRebate;
	}
	public void setIsEligibleForBatteryRebate(Boolean isEligibleForBatteryRebate) {
		this.isEligibleForBatteryRebate = isEligibleForBatteryRebate;
	}
	
	public Short getQuantity() {
		return quantity;
	}
	public void setQuantity(Short quantity) {
		this.quantity = quantity;
	}
	
	public Double getPriceForQuantity() {
		return priceForQuantity;
	}
	public void setPriceForQuantity(Double priceForQuantity) {
		this.priceForQuantity = priceForQuantity;
	}
	
	public Double getInstallationForQuantity() {
		return installationForQuantity;
	}
	public void setInstallationForQuantity(Double installationForQuantity) {
		this.installationForQuantity = installationForQuantity;
	}
	@Override
	public String toString() {
		return "BatteryQuote [batteryQuoteId=" + batteryQuoteId
				+ ", storeNumber=" + storeNumber + ", zip=" + zip
				+ ", createdDate=" + createdDate + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", donationName=" + donationName
				+ ", donationAmount=" + donationAmount + ", donationArticle="
				+ donationArticle + ", quantity=" + quantity
				+ ", priceForQuantity=" + priceForQuantity
				+ ", installationForQuantity=" + installationForQuantity
				+ ", subtotal=" + subtotal + ", total=" + total
				+ ", isEligibleForBatteryRebate=" + isEligibleForBatteryRebate
				+ ", batteryRebateAmount=" + batteryRebateAmount
				+ ", batteryRebateExpirationDate="
				+ batteryRebateExpirationDate + ", totalAfterRebate="
				+ totalAfterRebate + ", battery=" + battery + ", vehicle="
				+ vehicle + "]";
	}
	
	
}
