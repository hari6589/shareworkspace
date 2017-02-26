package com.bsro.service.promotion;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bfrc.pojo.promotion.AbstractPromotion;
import com.bfrc.pojo.promotion.Promotion;
import com.bfrc.pojo.promotion.PromotionImages;
import com.bfrc.pojo.promotion.PromotionType;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;

public interface PromotionService {

//	public  List<AbstractPromotion> getPromotionsByFriendlyIds(List<String> friendlyPromotionIds, boolean useEffectiveDates);

	public List<AbstractPromotion> getPromotionsByFriendlyIds(List<String> friendlyPromotionIds, Date targetDate);
	
	public AbstractPromotion getPromotionByFriendlyId(String friendlyid);

	public List<Promotion> getActivePromotionsByType(String promotionType, Date startDate);
	
//	public Map<String, Promotion> getActivePromotionsByTypeMap(String promotionType, Date startDate);

	public List<Promotion> getActivePromotionsByLandingPageId(String landingPageId, Date startDate);
	
	public String getDisclaimerByLandingPageId(String landingPageId);
	
	public List<TirePromotionEvent> getActiveTirePromotions(Date startDate);

	public List<Promotion> getActiveHomePageCoupons(Date startDate);
	
	public List<Promotion> getActiveHomePagePromotions(Date startDate);

	public void formatPromotion(AbstractPromotion promotion);

	public String getPromoTypeByName(String name);
	
	public PromotionType getPromotionTypeObjectByName(String name);

	public String getPromoTypeByType(String type);

	public PromotionType getPromotionTypeObjectByType(String type);
	
	public List<Promotion> removePromotionsWithoutImage(List<Promotion> promotionsToCheck);

	public List<Promotion> getActiveRepairOffers(Date startDate);

	public List<Promotion> getActiveMaintenanceOffers(Date startDate);

	public List<Promotion> getActiveTireCoupons(Date startDate);
	
	public List<Promotion> filterDuplicateOffers(List<Promotion> promotionsToCheck, int maxPromotionsReturned);

	public List<Promotion> filterDuplicateOffers(List<Promotion> promotionsToCheck);

	public PromotionImages getActivePromotionImagesById(Long id, Date startDate);
	
	public PromotionImages getActivePromotionImagesByFriendlyId(String friendlyId, Date startDate);
	
	public Map getAllPromotionImages();
	
	public Map getAllPromotionBrands();
}