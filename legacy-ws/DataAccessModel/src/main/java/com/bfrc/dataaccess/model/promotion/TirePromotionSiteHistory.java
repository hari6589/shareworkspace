package com.bfrc.dataaccess.model.promotion;

import java.util.Date;

public class TirePromotionSiteHistory {
	
    private Long siteHistoryId;
    private TirePromotionEvent tirePromotionEvent;
    private String siteName;
    private String lastChangedBy;
    private Date lastChangedDate;
    
	public Long getSiteHistoryId() {
		return siteHistoryId;
	}
	public void setSiteHistoryId(Long siteHistoryId) {
		this.siteHistoryId = siteHistoryId;
	}
	public TirePromotionEvent getTirePromotionEvent() {
		return tirePromotionEvent;
	}
	public void setTirePromotionEvent(TirePromotionEvent tirePromotionEvent) {
		this.tirePromotionEvent = tirePromotionEvent;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
				+ ((siteHistoryId == null) ? 0 : siteHistoryId.hashCode());
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
		TirePromotionSiteHistory other = (TirePromotionSiteHistory) obj;
		if (siteHistoryId == null) {
			if (other.siteHistoryId != null)
				return false;
		} else if (!siteHistoryId.equals(other.siteHistoryId))
			return false;
		return true;
	}
    
    
}
