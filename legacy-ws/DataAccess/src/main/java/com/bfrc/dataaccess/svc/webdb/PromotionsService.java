package com.bfrc.dataaccess.svc.webdb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bfrc.dataaccess.model.promotion.Promotion;

public interface PromotionsService {
	public Map<String, List> getSpecialOffers(String siteName, Long storeNumber, Date startDate, String friendlyId, String promoType, String promoCategory);
	
	public List<Promotion> getOffersByLandingPage(String siteName, Date startDate, String landingPageId, String friendlyId, String promoType);
	
	public List<Promotion> findActiveRepairOffers(String siteName, Date startDate, String landingPageId, String friendlyId);
	
	public List<Promotion> findActiveMaintenanceOffers(String siteName, Date startDate, String landingPageId, String friendlyId);
	
	public Map getOfferData(String siteName, String article, String rearArticle, String storeNumber, String qty, Map offersData);
	
	public Map loadTirePromotionData(String siteName, long storeNumber, String friendlyId);
	
}
