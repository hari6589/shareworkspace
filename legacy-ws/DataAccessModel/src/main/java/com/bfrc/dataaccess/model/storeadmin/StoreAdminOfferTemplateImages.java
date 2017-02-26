package com.bfrc.dataaccess.model.storeadmin;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ser.ToStringSerializer;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties({ "couponImage", "bannerImage"})
public class StoreAdminOfferTemplateImages implements java.io.Serializable {
	
	private long imageId;
	private Long templateId;
	private String siteName;
	private byte[] couponImage;
	Boolean hasCouponImage;
	private String couponImageURL;
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
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public long getImageId() {
		return this.imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
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
	
	public String getCouponImageURL() {
		return couponImageURL;
	}
	
	public void setCouponImageURL(String couponImageURL) {
		this.couponImageURL = couponImageURL;
	}
}
