package com.bfrc.dataaccess.model.promotion;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonUnwrapped;

import app.bsro.model.promotions.PromotionType;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties({"promoMainText", "promoFooterText", "promoPdfText", "lastChangedBy", "lastChangedDate", 
	"FCACOffersFlag", "fcacoffersFlag", "ETOffersFlag", "etoffersFlag"})
public class TirePromotionEvent {
	
    private Long promoId;
    
    @JsonUnwrapped
    private SourcePromotionType sourcePromotionType;
    
    private String promoName;
    private BigDecimal promoValue;
    private byte minQualifierQty;
    private String promoSmallImgText;
    private String promoSmallImgTooltip;
    private String promoDisplayName;
    private String promoTotalTag;
    private String promoUrlName;
    private String promoMainText;
    private String promoFooterText;
    private String promoPdfText;
    private String statusFlag;
    private String lastChangedBy;
    private Date lastChangedDate;
    private String FCACOffersFlag;
    private String ETOffersFlag;
    private Long orderId;
    private String imageFileId;
    private String title;
	private String price;
	private String promoDescription;
	private String brandName;
	private Date offerStartDate;
	private Date offerEndDate;
	private String rebateURL;
	
	private Boolean homePageOffer;
	private Boolean offerWithoutPrice;
	private PromotionType promotionType;
	private String stackFriendlyId;
	
//    private Set<TirePromotionEventHistory> tirePromotionEventHistories = new HashSet<TirePromotionEventHistory>(0);
//    private Set<TirePromotionSiteHistory> tirePromotionSiteHistories = new HashSet<TirePromotionSiteHistory>(0);
    
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getPromoId() {
		return promoId;
	}
	public void setPromoId(Long promoId) {
		this.promoId = promoId;
	}
	
	public SourcePromotionType getSourcePromotionType() {
		return sourcePromotionType;
	}
	
	public void setSourcePromotionType(SourcePromotionType sourcePromotionType) {
		this.sourcePromotionType = sourcePromotionType;
	}
	public String getPromoName() {
		return promoName;
	}
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getPromoValue() {
		return promoValue;
	}
	public void setPromoValue(BigDecimal promoValue) {
		this.promoValue = promoValue;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public byte getMinQualifierQty() {
		return minQualifierQty;
	}
	public void setMinQualifierQty(byte minQualifierQty) {
		this.minQualifierQty = minQualifierQty;
	}

	public String getPromoSmallImgText() {
		return promoSmallImgText;
	}
	public void setPromoSmallImgText(String promoSmallImgText) {
		this.promoSmallImgText = promoSmallImgText;
	}
	public String getPromoSmallImgTooltip() {
		return promoSmallImgTooltip;
	}
	public void setPromoSmallImgTooltip(String promoSmallImgTooltip) {
		this.promoSmallImgTooltip = promoSmallImgTooltip;
	}
	public String getPromoDisplayName() {
		return promoDisplayName;
	}
	public void setPromoDisplayName(String promoDisplayName) {
		this.promoDisplayName = promoDisplayName;
	}
	public String getPromoTotalTag() {
		return promoTotalTag;
	}
	public void setPromoTotalTag(String promoTotalTag) {
		this.promoTotalTag = promoTotalTag;
	}
	public String getPromoUrlName() {
		return promoUrlName;
	}
	public void setPromoUrlName(String promoUrlName) {
		this.promoUrlName = promoUrlName;
	}
	
	public String getPromoMainText() {
		return promoMainText;
	}
	
	public void setPromoMainText(String promoMainText) {
		this.promoMainText = promoMainText;
	}
	
	public String getPromoFooterText() {
		return promoFooterText;
	}
	
	public void setPromoFooterText(String promoFooterText) {
		this.promoFooterText = promoFooterText;
	}
	
	public String getPromoPdfText() {
		return promoPdfText;
	}
	
	public void setPromoPdfText(String promoPdfText) {
		this.promoPdfText = promoPdfText;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	
	public String getLastChangedBy() {
		return lastChangedBy;
	}
	
	public void setLastChangedBy(String lastChangedBy) {
		this.lastChangedBy = lastChangedBy;
	}
	
	public Date getLastChangedDate() {
		return lastChangedDate;
	}
	
	public void setLastChangedDate(Date lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}
	
	public String getFCACOffersFlag() {
		return FCACOffersFlag;
	}
	
	public void setFCACOffersFlag(String fCACOffersFlag) {
		FCACOffersFlag = fCACOffersFlag;
	}
	
	public String getETOffersFlag() {
		return ETOffersFlag;
	}
	
	public void setETOffersFlag(String eTOffersFlag) {
		ETOffersFlag = eTOffersFlag;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
	public String getRebateURL() {
		return rebateURL;
	}
	public void setRebateURL(String rebateURL) {
		this.rebateURL = rebateURL;
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
	
	//	public Set<TirePromotionEventHistory> getTirePromotionEventHistories() {
//		return tirePromotionEventHistories;
//	}
//	public void setTirePromotionEventHistories(
//			Set<TirePromotionEventHistory> tirePromotionEventHistories) {
//		this.tirePromotionEventHistories = tirePromotionEventHistories;
//	}
//	public Set<TirePromotionSiteHistory> getTirePromotionSiteHistories() {
//		return tirePromotionSiteHistories;
//	}
//	public void setTirePromotionSiteHistories(
//			Set<TirePromotionSiteHistory> tirePromotionSiteHistories) {
//		this.tirePromotionSiteHistories = tirePromotionSiteHistories;
//	}
//	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (promoId ^ (promoId >>> 32));
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
		TirePromotionEvent other = (TirePromotionEvent) obj;
		if (promoId != other.promoId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TirePromotionEvent [promoId=" + promoId
				+ ", sourcePromotionType=" + sourcePromotionType
				+ ", promoName=" + promoName + ", promoValue=" + promoValue
				+ ", promoDisplayName=" + promoDisplayName + ", promoMainText="
				+ promoMainText + ", promoFooterText=" + promoFooterText
				+ ", statusFlag=" + statusFlag + ", orderId="+ orderId +"]";
	}
	
	public String getStackFriendlyId() {
		return stackFriendlyId;
	}

	public void setStackFriendlyId(String stackFriendlyId) {
		this.stackFriendlyId = stackFriendlyId;
	}
	

}
