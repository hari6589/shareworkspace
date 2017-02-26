package app.bsro.model.battery;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import app.bsro.model.util.DateUtils;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Battery {
	private String productName;
    private String productCode;
    private String product;
    private String productOption;
    private Long partNumber;
    private Long totalWarrantyMonths;
    private Long replacementWarrantyMonths;
    private Long performanceWarrantyMonths;
    private Long coldCrankingAmps;
    private Long reserveCapacityMinutes;
    private BigDecimal webPrice;
    private BigDecimal tradeInCredit;
    private BigDecimal installationAmount;
    private String salesText;
    private BigDecimal regularPrice;
    private Long discountArticle;
    private BigDecimal discountAmount;
    private Boolean hasPricing;
    
    // for showing rebate information
    private String rebateId;
    private String promoText;
    private Date expirayDate;
    
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getProductOption() {
		return productOption;
	}
	public void setProductOption(String productOption) {
		this.productOption = productOption;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(Long partNumber) {
		this.partNumber = partNumber;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getTotalWarrantyMonths() {
		return totalWarrantyMonths;
	}
	public void setTotalWarrantyMonths(Long totalWarrantyMonths) {
		this.totalWarrantyMonths = totalWarrantyMonths;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getReplacementWarrantyMonths() {
		return replacementWarrantyMonths;
	}
	public void setReplacementWarrantyMonths(Long replacementWarrantyMonths) {
		this.replacementWarrantyMonths = replacementWarrantyMonths;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getColdCrankingAmps() {
		return coldCrankingAmps;
	}
	public void setColdCrankingAmps(Long coldCrankingAmps) {
		this.coldCrankingAmps = coldCrankingAmps;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getReserveCapacityMinutes() {
		return reserveCapacityMinutes;
	}
	public void setReserveCapacityMinutes(Long reserveCapacityMinutes) {
		this.reserveCapacityMinutes = reserveCapacityMinutes;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getWebPrice() {
		return webPrice;
	}
	public void setWebPrice(BigDecimal webPrice) {
		this.webPrice = webPrice;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getTradeInCredit() {
		return tradeInCredit;
	}
	public void setTradeInCredit(BigDecimal tradeInCredit) {
		this.tradeInCredit = tradeInCredit;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getInstallationAmount() {
		return installationAmount;
	}
	public void setInstallationAmount(BigDecimal installationAmount) {
		this.installationAmount = installationAmount;
	}
	public String getSalesText() {
		return salesText;
	}
	public void setSalesText(String salesText) {
		this.salesText = salesText;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getDiscountArticle() {
		return discountArticle;
	}
	public void setDiscountArticle(Long discountArticle) {
		this.discountArticle = discountArticle;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getPerformanceWarrantyMonths() {
		return performanceWarrantyMonths;
	}
	public void setPerformanceWarrantyMonths(Long performanceWarrantyMonths) {
		this.performanceWarrantyMonths = performanceWarrantyMonths;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Boolean getHasPricing() {
		return hasPricing;
	}
	public void setHasPricing(Boolean hasPricing) {
		this.hasPricing = hasPricing;
	}
	@JsonIgnore
    public String getPerformanceWarrantyYearsOrMonths() {
    	String warranty;
    	
    	if(this.performanceWarrantyMonths == null) {
    		warranty = "";
    	}
    	else if(this.performanceWarrantyMonths%12 == 0) {
    		warranty = this.performanceWarrantyMonths/12 + "-Year";
    	}
    	else {
    		warranty = this.performanceWarrantyMonths + "-Month";
    	}
    	
    	return warranty;
    }
	@Override
	public String toString() {
		return "Battery [productName=" + productName + ", productCode="
				+ productCode + ", product=" + product + ", productOption="
				+ productOption + ", partNumber=" + partNumber
				+ ", totalWarrantyMonths=" + totalWarrantyMonths
				+ ", replacementWarrantyMonths=" + replacementWarrantyMonths
				+ ", performanceWarrantyMonths=" + performanceWarrantyMonths
				+ ", coldCrankingAmps=" + coldCrankingAmps
				+ ", reserveCapacityMinutes=" + reserveCapacityMinutes
				+ ", webPrice=" + webPrice + ", tradeInCredit=" + tradeInCredit
				+ ", installationAmount=" + installationAmount + ", salesText="
				+ salesText + ", regularPrice=" + regularPrice
				+ ", discountArticle=" + discountArticle + ", discountAmount="
				+ discountAmount + ", hasPricing=" + hasPricing + ", rebateId=" +rebateId+ ", promoText ="
				+ promoText +", expirayDate=" +expirayDate+"]";
	}
	
	
	public String getRebateId() {
		return rebateId;
	}
	public void setRebateId(String rebateId) {
		this.rebateId = rebateId;
	}
	public String getPromoText() {
		return promoText;
	}
	public void setPromoText(String promoText) {
		this.promoText = promoText;
	}
	
	@JsonSerialize(using = DateUtils.class)
	public Date getExpirayDate() {
		return expirayDate;
	}
	
	public void setExpirayDate(Date expirayDate) {
		this.expirayDate = expirayDate;
	}

}
