package com.bfrc.dataaccess.model.promotion;

import java.sql.Blob;
import java.util.Date;

import app.bsro.model.promotions.PromotionType;

public class Promotion {
	
	private Long promotionId;
	private Date startDate;
	private Date expirationDate;
	private String description;
	private String tireId;
	private String tireId2;
	private int suvDisplay;
	private String url;
	private Boolean defaultSize;
	private Boolean newWindow;
	private Boolean maintOffer;
	private Boolean repairOffer;
	private Boolean tireOffer;
	private Long width;
	private Long height;
	private Long orderId;
	private String target;
	private Date createdDate;
	private String promoType;
	private Blob imageBlob;
	private Blob thumbBlob;
	private Blob flashIconBlob;
	private String webSite;
	private String landingPageId;
	private byte[] landingPageIcon;
	private String priceInfo;
	private String friendlyId;
	private String disclaimer;
	private Date modifiedDate;	
	private Date offerStartDate;
	private Date offerEndDate;
	private String offerDescription;
	private String imageFileId;
	private String title;
	private String price;
	private String promoDescription;
	private String brand;
	private String urlText;
	
	private Boolean homePageOffer;
	private Boolean offerWithoutPrice;
	private PromotionType promotionType;
	
	private String subtitleOne;
 	private String subtitleTwo;
 	private String invalidator;
 	private Long brandLogoId;
 	private Long brandImageId;
 	private String stackFriendlyId;
	
//	private boolean hasImage;
//	private boolean hasThumb;
//	private boolean hasFlashIcon;

	
	
	public String getSubtitleOne() {
		return subtitleOne;
	}
	public void setSubtitleOne(String subtitleOne) {
		this.subtitleOne = subtitleOne;
	}
	public String getSubtitleTwo() {
		return subtitleTwo;
	}
	public void setSubtitleTwo(String subtitleTwo) {
		this.subtitleTwo = subtitleTwo;
	}
	public String getInvalidator() {
		return invalidator;
	}
	public void setInvalidator(String invalidator) {
		this.invalidator = invalidator;
	}
	public Long getBrandLogoId() {
		return brandLogoId;
	}
	public void setBrandLogoId(Long brandLogoId) {
		this.brandLogoId = brandLogoId;
	}
	public Long getBrandImageId() {
		return brandImageId;
	}
	public void setBrandImageId(Long brandImageId) {
		this.brandImageId = brandImageId;
	}
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getDescription() {
		return description;
	}
	public String getDescription(boolean splitOnColon) {
		if(splitOnColon && this.description != null)
			return this.description.replaceFirst(":", ":<br />");
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTireId() {
		return tireId;
	}
	public void setTireId(String tireId) {
		this.tireId = tireId;
	}
	public String getTireId2() {
		return tireId2;
	}
	public void setTireId2(String tireId2) {
		this.tireId2 = tireId2;
	}
	public int getSuvDisplay() {
		return suvDisplay;
	}
	public void setSuvDisplay(int suvDisplay) {
		this.suvDisplay = suvDisplay;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getDefaultSize() {
		return defaultSize;
	}
	public void setDefaultSize(Boolean defaultSize) {
		this.defaultSize = defaultSize;
	}
	public Boolean getNewWindow() {
		return newWindow;
	}
	public void setNewWindow(Boolean newWindow) {
		this.newWindow = newWindow;
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
	public Long getWidth() {
		return width;
	}
	public void setWidth(Long width) {
		this.width = width;
	}
	public Long getHeight() {
		return height;
	}
	public void setHeight(Long height) {
		this.height = height;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getPromoType() {
		return promoType;
	}
	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}
	public Blob getImageBlob() {
		return imageBlob;
	}
	public void setImageBlob(Blob imageBlob) {
		this.imageBlob = imageBlob;
	}
	public Blob getThumbBlob() {
		return thumbBlob;
	}
	public void setThumbBlob(Blob thumbBlob) {
		this.thumbBlob = thumbBlob;
	}
	public Blob getFlashIconBlob() {
		return flashIconBlob;
	}
	public void setFlashIconBlob(Blob flashIconBlob) {
		this.flashIconBlob = flashIconBlob;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getLandingPageId() {
		return landingPageId;
	}
	public void setLandingPageId(String landingPageId) {
		this.landingPageId = landingPageId;
	}
	public byte[] getLandingPageIcon() {
		return landingPageIcon;
	}
	public void setLandingPageIcon(byte[] landingPageIcon) {
		this.landingPageIcon = landingPageIcon;
	}
	public String getPriceInfo() {
		return priceInfo;
	}
	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}
	public String getFriendlyId() {
		return friendlyId;
	}
	public void setFriendlyId(String friendlyId) {
		this.friendlyId = friendlyId;
	}
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public Date getOfferStartDate() {
		return offerStartDate;
	}
	public void setOfferStartDate(Date offerStartDate) {
		this.offerStartDate = offerStartDate;
	}
	public Date getOfferEndDate() {
		return offerEndDate;
	}
	public void setOfferEndDate(Date offerEndDate) {
		this.offerEndDate = offerEndDate;
	}
	public String getOfferDescription() {
		return offerDescription;
	}
	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}
	public String getImageFileId() {
		return imageFileId;
	}
	public void setImageFileId(String imageFileId) {
		this.imageFileId = imageFileId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPromoDescription() {
		return promoDescription;
	}
	public void setPromoDescription(String promoDescription) {
		this.promoDescription = promoDescription;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getUrlText() {
		return urlText;
	}
	public void setUrlText(String urlText) {
		this.urlText = urlText;
	}
	public Boolean getHomePageOffer() {
		return homePageOffer;
	}
	public void setHomePageOffer(Boolean homePageOffer) {
		this.homePageOffer = homePageOffer;
	}
	public Boolean getOfferWithoutPrice() {
		return offerWithoutPrice;
	}
	public void setOfferWithoutPrice(Boolean offerWithoutPrice) {
		this.offerWithoutPrice = offerWithoutPrice;
	}
	public PromotionType getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(PromotionType promotionType) {
		this.promotionType = promotionType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (promotionId ^ (promotionId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Promotion other = (Promotion) obj;
		if (promotionId != other.promotionId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Promotion [promotionId=" + promotionId + ", startDate="
				+ startDate + ", expirationDate=" + expirationDate
				+ ", description=" + description + ", url=" + url
				+ ", promoType=" + promoType + ", webSite=" + webSite
				+ ", disclaimer=" + disclaimer + "]";
	}
	
	public String getStackFriendlyId() {
		return stackFriendlyId;
	}

	public void setStackFriendlyId(String stackFriendlyId) {
		this.stackFriendlyId = stackFriendlyId;
	}
	
}
