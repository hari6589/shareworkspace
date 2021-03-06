package com.bfrc.pojo.tirepromotion;
// Generated Jun 12, 2009 11:31:56 AM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TirePromotionEvent generated by hbm2java
 */
public class TirePromotionEvent  implements java.io.Serializable {


     private long promoId;
     private SourcePromotionType sourcePromotionType;
     private String promoName;
     private BigDecimal promoValue;
     private byte minQualifierQty;
     //private Blob promoLargeImg;
     private byte[] promoLargeImg;
     //private Blob promoSmallImg;
     private byte[] promoSmallImg;
     private String promoSmallImgText;
     private String promoSmallImgTooltip;
     private String promoDisplayName;
     private String promoTotalTag;
     private String promoUrlName;
     private String promoMainText;
     private String promoFooterText;
     //private Blob promoPdf;
     private byte[] promoPdf;
     private String promoPdfText;
     private char statusFlag;
     private String lastChangedBy;
     private Date lastChangedDate;
     private Set tirePromotionEventHistories = new HashSet(0);
     private Set tirePromotionSiteHistories = new HashSet(0);
     private char FCACOffersFlag;
     private char ETOffersFlag;

    public TirePromotionEvent() {
    }

	
    public TirePromotionEvent(long promoId, SourcePromotionType sourcePromotionType, String promoName, BigDecimal promoValue, byte minQualifierQty, byte[] promoLargeImg, String promoDisplayName, String promoMainText, String promoFooterText, char statusFlag, String lastChangedBy, Date lastChangedDate) {
        this.promoId = promoId;
        this.sourcePromotionType = sourcePromotionType;
        this.promoName = promoName;
        this.promoValue = promoValue;
        this.minQualifierQty = minQualifierQty;
        this.promoLargeImg = promoLargeImg;
        this.promoDisplayName = promoDisplayName;
        this.promoMainText = promoMainText;
        this.promoFooterText = promoFooterText;
        this.statusFlag = statusFlag;
        this.lastChangedBy = lastChangedBy;
        this.lastChangedDate = lastChangedDate;
    }
    public TirePromotionEvent(long promoId, SourcePromotionType sourcePromotionType, String promoName, BigDecimal promoValue, byte minQualifierQty, byte[] promoLargeImg, byte[] promoSmallImg, String promoSmallImgText, String promoSmallImgTooltip, String promoDisplayName, String promoTotalTag, String promoUrlName, String promoMainText, String promoFooterText, byte[] promoPdf, String promoPdfText, char statusFlag, String lastChangedBy, Date lastChangedDate, Set tirePromotionEventHistories, Set tirePromotionSiteHistories) {
       this.promoId = promoId;
       this.sourcePromotionType = sourcePromotionType;
       this.promoName = promoName;
       this.promoValue = promoValue;
       this.minQualifierQty = minQualifierQty;
       this.promoLargeImg = promoLargeImg;
       this.promoSmallImg = promoSmallImg;
       this.promoSmallImgText = promoSmallImgText;
       this.promoSmallImgTooltip = promoSmallImgTooltip;
       this.promoDisplayName = promoDisplayName;
       this.promoTotalTag = promoTotalTag;
       this.promoUrlName = promoUrlName;
       this.promoMainText = promoMainText;
       this.promoFooterText = promoFooterText;
       this.promoPdf = promoPdf;
       this.promoPdfText = promoPdfText;
       this.statusFlag = statusFlag;
       this.lastChangedBy = lastChangedBy;
       this.lastChangedDate = lastChangedDate;
       this.tirePromotionEventHistories = tirePromotionEventHistories;
       this.tirePromotionSiteHistories = tirePromotionSiteHistories;
    }
   
    public long getPromoId() {
        return this.promoId;
    }
    
    public void setPromoId(long promoId) {
        this.promoId = promoId;
    }
    public SourcePromotionType getSourcePromotionType() {
        return this.sourcePromotionType;
    }
    
    public void setSourcePromotionType(SourcePromotionType sourcePromotionType) {
        this.sourcePromotionType = sourcePromotionType;
    }
    public String getPromoName() {
        return this.promoName;
    }
    
    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }
    public BigDecimal getPromoValue() {//default to zero
        if(this.promoValue == null) 
           this.promoValue = new BigDecimal(0.0);
        return this.promoValue;
    }
    
    public void setPromoValue(BigDecimal promoValue) {
        this.promoValue = promoValue;
    }
    public byte getMinQualifierQty() {
        return this.minQualifierQty;
    }
    
    public void setMinQualifierQty(byte minQualifierQty) {
        this.minQualifierQty = minQualifierQty;
    }
    public byte[] getPromoLargeImg() {
        return this.promoLargeImg;
    }
    
    public void setPromoLargeImg(byte[] promoLargeImg) {
        this.promoLargeImg = promoLargeImg;
    }
    public byte[] getPromoSmallImg() {
        return this.promoSmallImg;
    }
    
    public void setPromoSmallImg(byte[] promoSmallImg) {
        this.promoSmallImg = promoSmallImg;
    }
    public String getPromoSmallImgText() {
        return this.promoSmallImgText;
    }
    
    public void setPromoSmallImgText(String promoSmallImgText) {
        this.promoSmallImgText = promoSmallImgText;
    }
    public String getPromoSmallImgTooltip() {
        return this.promoSmallImgTooltip;
    }
    
    public void setPromoSmallImgTooltip(String promoSmallImgTooltip) {
        this.promoSmallImgTooltip = promoSmallImgTooltip;
    }
    public String getPromoDisplayName() {
        return this.promoDisplayName;
    }
    
    public void setPromoDisplayName(String promoDisplayName) {
        this.promoDisplayName = promoDisplayName;
    }
    public String getPromoTotalTag() {
        return this.promoTotalTag;
    }
    
    public void setPromoTotalTag(String promoTotalTag) {
        this.promoTotalTag = promoTotalTag;
    }
    public String getPromoUrlName() {
        return this.promoUrlName;
    }
    
    public void setPromoUrlName(String promoUrlName) {
        this.promoUrlName = promoUrlName;
    }
    public String getPromoMainText() {
        return this.promoMainText;
    }
    
    public void setPromoMainText(String promoMainText) {
        this.promoMainText = promoMainText;
    }
    public String getPromoFooterText() {
        return this.promoFooterText;
    }
    
    public void setPromoFooterText(String promoFooterText) {
        this.promoFooterText = promoFooterText;
    }
    public byte[] getPromoPdf() {
        return this.promoPdf;
    }
    
    public void setPromoPdf(byte[] promoPdf) {
        this.promoPdf = promoPdf;
    }
    public String getPromoPdfText() {
        return this.promoPdfText;
    }
    
    public void setPromoPdfText(String promoPdfText) {
        this.promoPdfText = promoPdfText;
    }
    public char getStatusFlag() {
        return this.statusFlag;
    }
    
    public void setStatusFlag(char statusFlag) {
        this.statusFlag = statusFlag;
    }
    public String getLastChangedBy() {
        return this.lastChangedBy;
    }
    
    public void setLastChangedBy(String lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
    }
    public Date getLastChangedDate() {
        return this.lastChangedDate;
    }
    
    public void setLastChangedDate(Date lastChangedDate) {
        this.lastChangedDate = lastChangedDate;
    }
    public Set getTirePromotionEventHistories() {
        return this.tirePromotionEventHistories;
    }
    
    public void setTirePromotionEventHistories(Set tirePromotionEventHistories) {
        this.tirePromotionEventHistories = tirePromotionEventHistories;
    }
    public Set getTirePromotionSiteHistories() {
        return this.tirePromotionSiteHistories;
    }
    
    public void setTirePromotionSiteHistories(Set tirePromotionSiteHistories) {
        this.tirePromotionSiteHistories = tirePromotionSiteHistories;
    }

    public char getFCACOffersFlag() {
    	return this.FCACOffersFlag;
    }

    public void setFCACOffersFlag(char FCACOffersFlag) {
    	this.FCACOffersFlag = FCACOffersFlag;
    }
    
    public char getETOffersFlag() {
    	return this.ETOffersFlag;
    }

    public void setETOffersFlag(char ETOffersFlag) {
    	this.ETOffersFlag = ETOffersFlag;
    }
}


