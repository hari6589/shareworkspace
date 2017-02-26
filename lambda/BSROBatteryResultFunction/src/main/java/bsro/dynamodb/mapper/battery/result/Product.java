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

@DynamoDBTable(tableName="BatteryProduct")
public class Product {
	
	private String productType;
	private String productCode;
	private String productName;
	private Long cca;
	private BigDecimal discountAmount;
	private BigDecimal installationAmount;
	private Long partNumber;
	private Long rcMinutes;
	private BigDecimal regularPrice;
	private Long replacementWarranty;
	private String salesText;
	private Long totalWarranty;
	private BigDecimal tradeinCredit;
	private BigDecimal webPrice;

	@DynamoDBHashKey(attributeName="ProductCode")
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	@DynamoDBAttribute(attributeName="ProductType")  
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	@DynamoDBAttribute(attributeName="ProductName")
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@DynamoDBAttribute(attributeName="CCA")
	public Long getCca() {
		return cca;
	}
	public void setCca(Long cca) {
		this.cca = cca;
	}
	
	@DynamoDBAttribute(attributeName="DiscountAmount")
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	@DynamoDBAttribute(attributeName="InstallationAmount")
	public BigDecimal getInstallationAmount() {
		return installationAmount;
	}
	public void setInstallationAmount(BigDecimal installationAmount) {
		this.installationAmount = installationAmount;
	}
	
	@DynamoDBAttribute(attributeName="PartNumber")
	public Long getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(Long partNumber) {
		this.partNumber = partNumber;
	}
	
	@DynamoDBAttribute(attributeName="RCMinutes")
	public Long getRcMinutes() {
		return rcMinutes;
	}
	public void setRcMinutes(Long rcMinutes) {
		this.rcMinutes = rcMinutes;
	}
	
	@DynamoDBAttribute(attributeName="RegularPrice")
	public BigDecimal getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}
	
	@DynamoDBAttribute(attributeName="ReplacementWarranty")
	public Long getReplacementWarranty() {
		return replacementWarranty;
	}
	public void setReplacementWarranty(Long replacementWarranty) {
		this.replacementWarranty = replacementWarranty;
	}
	
	@DynamoDBAttribute(attributeName="SalesText")
	public String getSalesText() {
		return salesText;
	}
	public void setSalesText(String salesText) {
		this.salesText = salesText;
	}
	
	@DynamoDBAttribute(attributeName="TotalWarranty")
	public Long getTotalWarranty() {
		return totalWarranty;
	}
	public void setTotalWarranty(Long totalWarranty) {
		this.totalWarranty = totalWarranty;
	}
	
	@DynamoDBAttribute(attributeName="TradeinCredit")
	public BigDecimal getTradeinCredit() {
		return tradeinCredit;
	}
	public void setTradeinCredit(BigDecimal tradeinCredit) {
		this.tradeinCredit = tradeinCredit;
	}
	
	@DynamoDBAttribute(attributeName="WebPrice")
	public BigDecimal getWebPrice() {
		return webPrice;
	}
	public void setWebPrice(BigDecimal webPrice) {
		this.webPrice = webPrice;
	}
}
