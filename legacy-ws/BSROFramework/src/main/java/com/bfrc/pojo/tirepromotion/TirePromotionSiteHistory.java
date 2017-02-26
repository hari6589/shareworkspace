package com.bfrc.pojo.tirepromotion;
// Generated Jun 12, 2009 11:31:56 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * TirePromotionSiteHistory generated by hbm2java
 */
public class TirePromotionSiteHistory  implements java.io.Serializable {


     private long siteHistoryId;
     private TirePromotionEvent tirePromotionEvent;
     private String siteName;
     private String lastChangedBy;
     private Date lastChangedDate;

    public TirePromotionSiteHistory() {
    }

    public TirePromotionSiteHistory(long siteHistoryId, TirePromotionEvent tirePromotionEvent, String siteName, String lastChangedBy, Date lastChangedDate) {
       this.siteHistoryId = siteHistoryId;
       this.tirePromotionEvent = tirePromotionEvent;
       this.siteName = siteName;
       this.lastChangedBy = lastChangedBy;
       this.lastChangedDate = lastChangedDate;
    }
   
    public long getSiteHistoryId() {
        return this.siteHistoryId;
    }
    
    public void setSiteHistoryId(long siteHistoryId) {
        this.siteHistoryId = siteHistoryId;
    }
    public TirePromotionEvent getTirePromotionEvent() {
        return this.tirePromotionEvent;
    }
    
    public void setTirePromotionEvent(TirePromotionEvent tirePromotionEvent) {
        this.tirePromotionEvent = tirePromotionEvent;
    }
    public String getSiteName() {
        return this.siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
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




}


