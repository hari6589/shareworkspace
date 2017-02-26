/**
 * 
 */
package app.bsro.model.battery;

import java.math.BigDecimal;

/**
 * @author schowdhu
 *
 */
public class BatteryWebPrice {
	private String product;
    private BigDecimal webPrice;
    private BigDecimal tradeinCredit;
    private BigDecimal installationAmt;
    private String salesText;
    private byte[] batteryImage;
    private BigDecimal regularPrice;
    private Long discountArticle;
    private BigDecimal discountAmount;
    
	public BatteryWebPrice() {
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the webPrice
	 */
	public BigDecimal getWebPrice() {
		return webPrice;
	}

	/**
	 * @param webPrice the webPrice to set
	 */
	public void setWebPrice(BigDecimal webPrice) {
		this.webPrice = webPrice;
	}

	/**
	 * @return the tradeinCredit
	 */
	public BigDecimal getTradeinCredit() {
		return tradeinCredit;
	}

	/**
	 * @param tradeinCredit the tradeinCredit to set
	 */
	public void setTradeinCredit(BigDecimal tradeinCredit) {
		this.tradeinCredit = tradeinCredit;
	}

	/**
	 * @return the installationAmt
	 */
	public BigDecimal getInstallationAmt() {
		return installationAmt;
	}

	/**
	 * @param installationAmt the installationAmt to set
	 */
	public void setInstallationAmt(BigDecimal installationAmt) {
		this.installationAmt = installationAmt;
	}

	/**
	 * @return the salesText
	 */
	public String getSalesText() {
		return salesText;
	}

	/**
	 * @param salesText the salesText to set
	 */
	public void setSalesText(String salesText) {
		this.salesText = salesText;
	}

	/**
	 * @return the batteryImage
	 */
	public byte[] getBatteryImage() {
		return batteryImage;
	}

	/**
	 * @param batteryImage the batteryImage to set
	 */
	public void setBatteryImage(byte[] batteryImage) {
		this.batteryImage = batteryImage;
	}

	/**
	 * @return the regularPrice
	 */
	public BigDecimal getRegularPrice() {
		return regularPrice;
	}

	/**
	 * @param regularPrice the regularPrice to set
	 */
	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}

	/**
	 * @return the discountArticle
	 */
	public Long getDiscountArticle() {
		return discountArticle;
	}

	/**
	 * @param discountArticle the discountArticle to set
	 */
	public void setDiscountArticle(Long discountArticle) {
		this.discountArticle = discountArticle;
	}

	/**
	 * @return the discountAmount
	 */
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	/**
	 * @param discountAmount the discountAmount to set
	 */
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
    
    
}
