package com.bfrc.dataaccess.model.storeadmin;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class StoreAdminPromotionViewObject {
	
	private StoreAdminPromotion storePromo;
	private List<StoreAdminOfferViewObject> sortedOfferViewObjects = new ArrayList<StoreAdminOfferViewObject>();
	private String daysDisplay;
	private String lwTrackingTag;
	private String formattedDateRange;

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
		return formattedDateRange;
	}
	
	public void setFormattedDateRange(String formattedDateRange) {
		this.formattedDateRange = formattedDateRange;
	}

	public List<StoreAdminOfferViewObject> getSortedOfferViewObjects() {
		return sortedOfferViewObjects;
	}

	public void setSortedOfferViewObjects(List<StoreAdminOfferViewObject> sortedOfferViewObjects) {
		this.sortedOfferViewObjects = sortedOfferViewObjects;
	}
	
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
	public String getDaysDisplay() {
		return daysDisplay;
	}
	
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
	public void setDaysDisplay(String daysDisplay) {
		this.daysDisplay = daysDisplay;
	}
}
