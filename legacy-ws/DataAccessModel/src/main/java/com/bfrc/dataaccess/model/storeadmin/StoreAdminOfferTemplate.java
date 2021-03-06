package com.bfrc.dataaccess.model.storeadmin;

// Generated Oct 19, 2011 7:57:34 PM by Hibernate Tools 3.2.1.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * StoreAdminOfferTemplate generated by hbm2java
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties({"imageFcac", "imageTp", "imageEt", "startDate", "endDate", "createdBy", "createdDate", "modifiedBy", "modifiedDate", "bannerImageFcac",
	"bannerImageTp", "bannerImageEt", "hasImageEt", "images"})
public class StoreAdminOfferTemplate implements java.io.Serializable {

	private static final long serialVersionUID = 7141918401026483587L;
	
	private long templateId;
	private String friendlyId;
	private Long categoryId;
	private String name;
	private byte[] imageFcac;
	Boolean hasImageFcac;
	private byte[] imageTp;
	Boolean hasImageTp;
	private byte[] imageEt;
	Boolean hasImageEt;
	private byte[] bannerImageFcac;
	private byte[] bannerImageTp;
	private byte[] bannerImageEt;
	private BigDecimal price;
	private BigDecimal percentOff;
	private Date startDate;
	private Date endDate;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private Long priorityOrder;
	private String subtitle;
	private String shortDescription;
	private String priceDisclaimer;
	private String priceHtml;
	private String buttonText;
	private Set images = new HashSet();

	public StoreAdminOfferTemplate() {
	}

	public StoreAdminOfferTemplate(long templateId, Date createdDate) {
		this.templateId = templateId;
		this.createdDate = createdDate;
	}

	public StoreAdminOfferTemplate(long templateId, String friendlyId, Long categoryId, String name, byte[] imageFcac, byte[] imageTp,
			byte[] imageEt, BigDecimal price, BigDecimal percentOff, Date startDate, Date endDate, String createdBy, Date createdDate,
			String modifiedBy, Date modifiedDate, Long priorityOrder) {
		this.templateId = templateId;
		this.friendlyId = friendlyId;
		this.categoryId = categoryId;
		this.name = name;
		this.imageFcac = imageFcac;
		this.imageTp = imageTp;
		this.imageEt = imageEt;
		this.price = price;
		this.percentOff = percentOff;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.priorityOrder = priorityOrder;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getFriendlyId() {
		return this.friendlyId;
	}

	public void setFriendlyId(String friendlyId) {
		this.friendlyId = friendlyId;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getImageFcac() {
		return this.imageFcac;
	}

	public void setImageFcac(byte[] imageFcac) {
		this.imageFcac = imageFcac;
	}

	public byte[] getImageTp() {
		return this.imageTp;
	}

	public void setImageTp(byte[] imageTp) {
		this.imageTp = imageTp;
	}

	public byte[] getImageEt() {
		return this.imageEt;
	}

	public void setImageEt(byte[] imageEt) {
		this.imageEt = imageEt;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPercentOff() {
		return this.percentOff;
	}

	public void setPercentOff(BigDecimal percentOff) {
		this.percentOff = percentOff;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getPriorityOrder() {
		return this.priorityOrder;
	}

	public void setPriorityOrder(Long priorityOrder) {
		this.priorityOrder = priorityOrder;
	}

	public byte[] getBannerImageFcac() {
		return bannerImageFcac;
	}

	public void setBannerImageFcac(byte[] bannerImageFcac) {
		this.bannerImageFcac = bannerImageFcac;
	}

	public byte[] getBannerImageTp() {
		return bannerImageTp;
	}

	public void setBannerImageTp(byte[] bannerImageTp) {
		this.bannerImageTp = bannerImageTp;
	}

	public byte[] getBannerImageEt() {
		return bannerImageEt;
	}

	public void setBannerImageEt(byte[] bannerImageEt) {
		this.bannerImageEt = bannerImageEt;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getPriceDisclaimer() {
		return priceDisclaimer;
	}

	public void setPriceDisclaimer(String priceDisclaimer) {
		this.priceDisclaimer = priceDisclaimer;
	}

	public String getPriceHtml() {
		return priceHtml;
	}

	public void setPriceHtml(String priceHtml) {
		this.priceHtml = priceHtml;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public Boolean getHasImageFcac() {
		return hasImageFcac;
	}

	public void setHasImageFcac(Boolean hasImageFcac) {
		this.hasImageFcac = hasImageFcac;
	}

	public Boolean getHasImageTp() {
		return hasImageTp;
	}

	public void setHasImageTp(Boolean hasImageTp) {
		this.hasImageTp = hasImageTp;
	}

	public Boolean getHasImageEt() {
		return hasImageEt;
	}

	public void setHasImageEt(Boolean hasImageEt) {
		this.hasImageEt = hasImageEt;
	}
	
	public void setImages(Set images) {
		this.images = images;
	}

	public Set getImages() {
		return images;
	}

}
