package com.bfrc.pojo.storeadmin;

import java.util.ArrayList;
import java.util.List;

import com.bfrc.framework.util.StringUtils;
import com.bfrc.security.Encode;

public class StoreAdminPromotionViewObject {
	
	private StoreAdminPromotion storePromo;
	private List<StoreAdminOfferViewObject> sortedOfferViewObjects = new ArrayList<StoreAdminOfferViewObject>();
	private String daysDisplay;
	private String lwTrackingTag;

	public StoreAdminPromotionViewObject() {
	}
	
	public StoreAdminPromotionViewObject(StoreAdminPromotion storeAdminPromotion, String lwTrackingTag) {
		setStorePromo(storeAdminPromotion);
		setLwTrackingTag(lwTrackingTag);
	}

	public String getLwTrackingTag() {
		return lwTrackingTag;
	}

	public void setLwTrackingTag(String lwTrackingTag) {
		this.lwTrackingTag = lwTrackingTag;
	}
	
	public StoreAdminPromotion getStorePromo() {
		return storePromo;
	}

	public void setStorePromo(StoreAdminPromotion storePromo) {
		this.storePromo = storePromo;
	}
	
	public String getFormattedDateRange(){
		return StringUtils.formatDateRangeShortMonth(storePromo.getStartDate(), storePromo.getEndDate());
	}

	public List<StoreAdminOfferViewObject> getSortedOfferViewObjects() {
		return sortedOfferViewObjects;
	}

	public void setSortedOfferViewObjects(List<StoreAdminOfferViewObject> sortedOfferViewObjects) {
		this.sortedOfferViewObjects = sortedOfferViewObjects;
	}

	public String getDaysDisplay() {
		return daysDisplay;
	}
	public void setDaysDisplay(String daysDisplay) {
		this.daysDisplay = daysDisplay;
	}
	public String getLwTag(){
		String desc = storePromo.getDescription() == null ? "" : Encode.html(storePromo.getDescription().replace("'","\\'").replace("%", "percent"));
		return lwTrackingTag.replace("_DESCRIPTION_", "View " + desc);
	}
	public String getLwTagPrint(){
		String desc = storePromo.getDescription() == null ? "" : Encode.html(storePromo.getDescription().replace("'","\\'").replace("%", "percent"));
		return lwTrackingTag.replace("_DESCRIPTION_", "Print " + desc);
	}	
}
