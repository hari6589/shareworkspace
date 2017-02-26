package com.bfrc.pojo.storeadmin;

public class StoreAdminOfferTemplateImages implements java.io.Serializable {
	
	private long imageId;
	private Long templateId;
	private String siteName;
	private byte[] couponImage;
	Boolean hasCouponImage;
	private byte[] bannerImage;
	Boolean hasBannerImage;
	
	public StoreAdminOfferTemplateImages() {
	}
	
	public StoreAdminOfferTemplateImages(long imageId) {
		this.imageId = imageId;
	}
	
	public StoreAdminOfferTemplateImages(long imageId, Long templateId, String siteName, byte[] couponImage, byte[] bannerImage) {
		this.imageId = imageId;
		this.templateId = templateId;
		this.siteName = siteName;
		this.couponImage = couponImage;
		this.bannerImage = bannerImage;
	}
	
	public long getImageId() {
		return this.imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	
	public Long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	
	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	public byte[] getCouponImage() {
		return this.couponImage;
	}

	public void setCouponImage(byte[] couponImage) {
		this.couponImage = couponImage;
	}
	
	public byte[] getBannerImage() {
		return this.bannerImage;
	}

	public void setBannerImage(byte[] bannerImage) {
		this.bannerImage = bannerImage;
	}
	
	public Boolean getHasCouponImage() {
		return hasCouponImage;
	}

	public void setHasCouponImage(Boolean hasCouponImage) {
		this.hasCouponImage = hasCouponImage;
	}
	
	public Boolean getHasBannerImage() {
		return hasBannerImage;
	}

	public void setHasBannerImage(Boolean hasBannerImage) {
		this.hasBannerImage = hasBannerImage;
	}
}
