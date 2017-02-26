package com.bfrc.pojo.promotion;

public class PromotionType implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5912611654856878198L;
	
	private long promoTypeId;
	private String promoName;
	private String promoType;
	private String offerPageTitle;
	private String offerMetaDescription;
	private String offerMetaKeywords;
	private String description;
	
	public long getPromoTypeId() {
		return promoTypeId;
	}
	public void setPromoTypeId(long promoTypeId) {
		this.promoTypeId = promoTypeId;
	}
	public String getPromoName() {
		return promoName;
	}
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	public String getPromoType() {
		return promoType;
	}
	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}
	public String getOfferPageTitle() {
		return offerPageTitle;
	}
	public void setOfferPageTitle(String offerPageTitle) {
		this.offerPageTitle = offerPageTitle;
	}
	public String getOfferMetaDescription() {
		return offerMetaDescription;
	}
	public void setOfferMetaDescription(String offerMetaDescription) {
		this.offerMetaDescription = offerMetaDescription;
	}
	public String getOfferMetaKeywords() {
		return offerMetaKeywords;
	}
	public void setOfferMetaKeywords(String offerMetaKeywords) {
		this.offerMetaKeywords = offerMetaKeywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
