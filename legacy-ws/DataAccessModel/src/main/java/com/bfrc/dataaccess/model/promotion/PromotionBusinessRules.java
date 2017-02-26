package com.bfrc.dataaccess.model.promotion;

import java.math.BigDecimal;
public class PromotionBusinessRules implements java.io.Serializable {
	
	private static final long serialVersionUID = 5687870305212993107L;
	
	private String productType;
	private String productSubType;
	private String priceModificationType;
	
	private BigDecimal percentDiscount;
	private BigDecimal amountDiscount;
	private BigDecimal specificPrice;
	public BigDecimal getSpecificPrice() {
		return specificPrice;
	}
	public void setSpecificPrice(BigDecimal specificPrice) {
		this.specificPrice = specificPrice;
	}
	private BigDecimal isRebate;
	

	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductSubType() {
		return productSubType;
	}
	public void setProductSubType(String productSubType) {
		this.productSubType = productSubType;
	}
	public String getPriceModificationType() {
		return priceModificationType;
	}
	public void setPriceModificationType(String priceModificationType) {
		this.priceModificationType = priceModificationType;
	}
	public BigDecimal getPercentDiscount() {
		return percentDiscount;
	}
	public void setPercentDiscount(BigDecimal percentDiscount) {
		this.percentDiscount = percentDiscount;
	}
	public BigDecimal getAmountDiscount() {
		return amountDiscount;
	}
	public void setAmountDiscount(BigDecimal amountDiscount) {
		this.amountDiscount = amountDiscount;
	}
	public BigDecimal getIsRebate() {
		return isRebate;
	}
	public void setIsRebate(BigDecimal isRebate) {
		this.isRebate = isRebate;
	}
}
