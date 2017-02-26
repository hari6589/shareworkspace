/**
 * 
 */
package app.bsro.model.battery;

/**
 * @author schowdhu
 *
 */
public class BatteryMaster {

	private String product;
	private String productCode;
	private Long partNumber;
	private Long totalWarranty;
	private Long replacementWarranty;
	private Long cca;
	private Long rcMinutes;
	
	
	
	public BatteryMaster() {
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
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * @return the partNumber
	 */
	public Long getPartNumber() {
		return partNumber;
	}
	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(Long partNumber) {
		this.partNumber = partNumber;
	}
	/**
	 * @return the totalWarranty
	 */
	public Long getTotalWarranty() {
		return totalWarranty;
	}
	/**
	 * @param totalWarranty the totalWarranty to set
	 */
	public void setTotalWarranty(Long totalWarranty) {
		this.totalWarranty = totalWarranty;
	}
	/**
	 * @return the replacementWarranty
	 */
	public Long getReplacementWarranty() {
		return replacementWarranty;
	}
	/**
	 * @param replacementWarranty the replacementWarranty to set
	 */
	public void setReplacementWarranty(Long replacementWarranty) {
		this.replacementWarranty = replacementWarranty;
	}
	/**
	 * @return the cca
	 */
	public Long getCca() {
		return cca;
	}
	/**
	 * @param cca the cca to set
	 */
	public void setCca(Long cca) {
		this.cca = cca;
	}
	/**
	 * @return the rcMinutes
	 */
	public Long getRcMinutes() {
		return rcMinutes;
	}
	/**
	 * @param rcMinutes the rcMinutes to set
	 */
	public void setRcMinutes(Long rcMinutes) {
		this.rcMinutes = rcMinutes;
	}
	
	
}
