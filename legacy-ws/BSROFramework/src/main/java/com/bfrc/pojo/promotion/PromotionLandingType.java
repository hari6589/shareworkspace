package com.bfrc.pojo.promotion;

public class PromotionLandingType implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5912611654856878198L;
	
	private long promoTypeId;
	private String promoName;
	private String promoType;
	private Boolean maintOffer;
	private Boolean repairOffer;
	private Boolean tireOffer;
	private String target;
	private String landingPageId;
	private String webSite;
	
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
	public Boolean getMaintOffer() {
		return maintOffer;
	}
	public void setMaintOffer(Boolean maintOffer) {
		this.maintOffer = maintOffer;
	}
	public Boolean getRepairOffer() {
		return repairOffer;
	}
	public void setRepairOffer(Boolean repairOffer) {
		this.repairOffer = repairOffer;
	}
	public Boolean getTireOffer() {
		return tireOffer;
	}
	public void setTireOffer(Boolean tireOffer) {
		this.tireOffer = tireOffer;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getLandingPageId() {
		return landingPageId;
	}
	public void setLandingPageId(String landingPageId) {
		this.landingPageId = landingPageId;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
