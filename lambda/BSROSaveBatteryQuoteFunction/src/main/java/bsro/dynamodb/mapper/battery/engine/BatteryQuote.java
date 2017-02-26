package bsro.dynamodb.mapper.battery.engine;

import java.math.BigDecimal;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="BatteryQuote")
public class BatteryQuote {
	
	private long quoteId;
	private Long storeNumber;
    private String zip;
    private String createdDate;
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
    private String batteryRebateExpirationDate;
    private Double totalAfterRebate;

    public Battery battery;
    public Vehicle vehicle;

	@DynamoDBHashKey(attributeName="QuoteId")
    public long getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(long quoteId) {
		this.quoteId = quoteId;
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

	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
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

	public Boolean getIsEligibleForBatteryRebate() {
		return isEligibleForBatteryRebate;
	}
	public void setIsEligibleForBatteryRebate(Boolean isEligibleForBatteryRebate) {
		this.isEligibleForBatteryRebate = isEligibleForBatteryRebate;
	}

	public Double getBatteryRebateAmount() {
		return batteryRebateAmount;
	}
	public void setBatteryRebateAmount(Double batteryRebateAmount) {
		this.batteryRebateAmount = batteryRebateAmount;
	}

	public String getBatteryRebateExpirationDate() {
		return batteryRebateExpirationDate;
	}
	public void setBatteryRebateExpirationDate(String batteryRebateExpirationDate) {
		this.batteryRebateExpirationDate = batteryRebateExpirationDate;
	}
	
	public Double getTotalAfterRebate() {
		return totalAfterRebate;
	}
	public void setTotalAfterRebate(Double totalAfterRebate) {
		this.totalAfterRebate = totalAfterRebate;
	}

	@DynamoDBAttribute(attributeName="battery")
    public Battery getBattery() {
		return battery;
	}
	public void setBattery(Battery battery) {
		this.battery = battery;
	}
	
	@DynamoDBAttribute(attributeName="vehicle")
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@DynamoDBDocument
    public static class Battery {
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
		public Long getPartNumber() {
			return partNumber;
		}
		public void setPartNumber(Long partNumber) {
			this.partNumber = partNumber;
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
		public BigDecimal getWebPrice() {
			return webPrice;
		}
		public void setWebPrice(BigDecimal webPrice) {
			this.webPrice = webPrice;
		}
		public BigDecimal getTradeInCredit() {
			return tradeInCredit;
		}
		public void setTradeInCredit(BigDecimal tradeInCredit) {
			this.tradeInCredit = tradeInCredit;
		}
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
		public BigDecimal getRegularPrice() {
			return regularPrice;
		}
		public void setRegularPrice(BigDecimal regularPrice) {
			this.regularPrice = regularPrice;
		}
		public Long getDiscountArticle() {
			return discountArticle;
		}
		public void setDiscountArticle(Long discountArticle) {
			this.discountArticle = discountArticle;
		}
		public BigDecimal getDiscountAmount() {
			return discountAmount;
		}
		public void setDiscountAmount(BigDecimal discountAmount) {
			this.discountAmount = discountAmount;
		}
		public Boolean getHasPricing() {
			return hasPricing;
		}
		public void setHasPricing(Boolean hasPricing) {
			this.hasPricing = hasPricing;
		}
    }
    
    @DynamoDBDocument
    public static class Vehicle {
    	 private String year;
    	 private String make;
    	 private String model;
    	 private String engine;
    	 
    	 public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		public String getMake() {
			return make;
		}
		public void setMake(String make) {
			this.make = make;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getEngine() {
			return engine;
		}
		public void setEngine(String engine) {
			this.engine = engine;
		}
    }
}
