package com.bfrc.dataaccess.model.storeadmin;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import app.bsro.model.util.DateUtils;
import app.bsro.model.util.HTMLUtils;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties({"createdBy", "createdDate", "modifiedBy", "modifiedDate", "offers"})
public class StoreAdminPromotion implements java.io.Serializable {

	private long promotionId;
	private String friendlyId;
	private String promotionType;
	private String brand;
	private String title;
	private String description;
	private Long imageId;
	private String promoBannerURL;
	private Date onlineStartDate;
	private Date onlineEndDate;
	private Date startDate;
	private Date endDate;
	private String denyReason;
	private String requestReason;
	private Long articleNumber;
	private String createdBy;
	private String createdByEmail;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String status;
	private String disclaimer;
	private Set offers = new HashSet();

	public StoreAdminPromotion() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using = HTMLUtils.class)
	public String getDescription() {
		return description;
	}
	
	@JsonSerialize(using = HTMLUtils.class)
	public void setDescription(String description) {
		this.description = description;
	}

	public StoreAdminPromotion(long promotionId, Date createdDate) {
		this.setPromotionId(promotionId);
		this.createdDate = createdDate;
	}

	public String getFriendlyId() {
		return this.friendlyId;
	}

	public void setFriendlyId(String friendlyId) {
		this.friendlyId = friendlyId;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@JsonSerialize(using = DateUtils.class)
	public Date getOnlineStartDate() {
		return this.onlineStartDate;
	}

	public void setOnlineStartDate(Date onlineStartDate) {
		this.onlineStartDate = onlineStartDate;
	}
	
	@JsonSerialize(using = DateUtils.class)
	public Date getOnlineEndDate() {
		return this.onlineEndDate;
	}

	public void setOnlineEndDate(Date onlineEndDate) {
		this.onlineEndDate = onlineEndDate;
	}
	
	@JsonSerialize(using = DateUtils.class)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonSerialize(using = DateUtils.class)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getDenyReason() {
		return this.denyReason;
	}

	public void setDenyReason(String denyReason) {
		this.denyReason = denyReason;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getArticleNumber() {
		return this.articleNumber;
	}

	public void setArticleNumber(Long articleNumber) {
		this.articleNumber = articleNumber;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDisclaimer() {
		return this.disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public void setPromotionId(long promotionId) {
		this.promotionId = promotionId;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public long getPromotionId() {
		return promotionId;
	}

	public void setOffers(Set offers) {
		this.offers = offers;
	}

	public Set getOffers() {
		return offers;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getImageId() {
		return imageId;
	}
	
	public void setPromoBannerURL(String promoBannerURL) {
		this.promoBannerURL = promoBannerURL;
	}
	
	public String getPromoBannerURL() {
		return promoBannerURL;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

}
