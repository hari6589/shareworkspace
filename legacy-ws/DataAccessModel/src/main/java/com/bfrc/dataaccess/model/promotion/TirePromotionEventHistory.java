package com.bfrc.dataaccess.model.promotion;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

public class TirePromotionEventHistory {
	
    private Long eventHistoryId;
    private TirePromotionEvent tirePromotionEvent;
    private SourcePromotionType sourcePromotionType;
    private String promoName;
    private BigDecimal promoValue;
    private byte minQualifierQty;
    private Blob promoLargeImg;
    private Blob promoSmallImg;
    private String promoSmallImgText;
    private String promoSmallImgTooltip;
    private String promoDisplayName;
    private String promoTotalTag;
    private String promoUrlName;
    private String promoMainText;
    private String promoFooterText;
    private Blob promoPdf;
    private String promoPdfText;
    private String statusFlag;
    private String lastChangedBy;
    private Date lastChangedDate;
    
	public Long getEventHistoryId() {
		return eventHistoryId;
	}
	public void setEventHistoryId(Long eventHistoryId) {
		this.eventHistoryId = eventHistoryId;
	}
	public TirePromotionEvent getTirePromotionEvent() {
		return tirePromotionEvent;
	}
	public void setTirePromotionEvent(TirePromotionEvent tirePromotionEvent) {
		this.tirePromotionEvent = tirePromotionEvent;
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
	public BigDecimal getPromoValue() {
		return promoValue;
	}
	public void setPromoValue(BigDecimal promoValue) {
		this.promoValue = promoValue;
	}
	public byte getMinQualifierQty() {
		return minQualifierQty;
	}
	public void setMinQualifierQty(byte minQualifierQty) {
		this.minQualifierQty = minQualifierQty;
	}
	public Blob getPromoLargeImg() {
		return promoLargeImg;
	}
	public void setPromoLargeImg(Blob promoLargeImg) {
		this.promoLargeImg = promoLargeImg;
	}
	public Blob getPromoSmallImg() {
		return promoSmallImg;
	}
	public void setPromoSmallImg(Blob promoSmallImg) {
		this.promoSmallImg = promoSmallImg;
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
	public Blob getPromoPdf() {
		return promoPdf;
	}
	public void setPromoPdf(Blob promoPdf) {
		this.promoPdf = promoPdf;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventHistoryId == null) ? 0 : eventHistoryId.hashCode());
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
		TirePromotionEventHistory other = (TirePromotionEventHistory) obj;
		if (eventHistoryId == null) {
			if (other.eventHistoryId != null)
				return false;
		} else if (!eventHistoryId.equals(other.eventHistoryId))
			return false;
		return true;
	}
    
    
}
