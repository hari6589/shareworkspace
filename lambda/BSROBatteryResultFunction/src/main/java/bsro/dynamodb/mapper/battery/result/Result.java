package bsro.dynamodb.mapper.battery.result;

import java.math.BigDecimal;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

public class Result {
	
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
	public Long getTotalWarrantyMonths() {
		return totalWarrantyMonths;
	}
	public void setTotalWarrantyMonths(Long totalWarrantyMonths) {
		this.totalWarrantyMonths = totalWarrantyMonths;
	}
	public Long getReplacementWarrantyMonths() {
		return replacementWarrantyMonths;
	}
	public void setReplacementWarrantyMonths(Long replacementWarrantyMonths) {
		this.replacementWarrantyMonths = replacementWarrantyMonths;
	}
	public Long getPerformanceWarrantyMonths() {
		return performanceWarrantyMonths;
	}
	public void setPerformanceWarrantyMonths(Long performanceWarrantyMonths) {
		this.performanceWarrantyMonths = performanceWarrantyMonths;
	}
	public Long getColdCrankingAmps() {
		return coldCrankingAmps;
	}
	public void setColdCrankingAmps(Long coldCrankingAmps) {
		this.coldCrankingAmps = coldCrankingAmps;
	}
	public Long getReserveCapacityMinutes() {
		return reserveCapacityMinutes;
	}
	public void setReserveCapacityMinutes(Long reserveCapacityMinutes) {
		this.reserveCapacityMinutes = reserveCapacityMinutes;
	}
	public BigDecimal getTradeInCredit() {
		return tradeInCredit;
	}
	public void setTradeInCredit(BigDecimal tradeInCredit) {
		this.tradeInCredit = tradeInCredit;
	}
	public Long getDiscountArticle() {
		return discountArticle;
	}
	public void setDiscountArticle(Long discountArticle) {
		this.discountArticle = discountArticle;
	}
	public Boolean getHasPricing() {
		return hasPricing;
	}
	public void setHasPricing(Boolean hasPricing) {
		this.hasPricing = hasPricing;
	}
	public void setPartNumber(Long partNumber) {
		this.partNumber = partNumber;
	}
    public Long getPartNumber() {
		return partNumber;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	public BigDecimal getInstallationAmount() {
		return installationAmount;
	}
	public void setInstallationAmount(BigDecimal installationAmount) {
		this.installationAmount = installationAmount;
	}
	public BigDecimal getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}
	public String getSalesText() {
		return salesText;
	}
	public void setSalesText(String salesText) {
		this.salesText = salesText;
	}
	public BigDecimal getWebPrice() {
		return webPrice;
	}
	public void setWebPrice(BigDecimal webPrice) {
		this.webPrice = webPrice;
	}
}
