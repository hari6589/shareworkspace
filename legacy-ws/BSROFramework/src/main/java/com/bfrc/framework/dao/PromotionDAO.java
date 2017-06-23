package com.bfrc.framework.dao;

import java.util.List;
import java.util.*;
import java.util.Date;

import com.bfrc.pojo.promotion.*;

public interface PromotionDAO {
	static final int MAINT = 0;
	static final int REPAIR = 1;
	static final int TIRE = 2;
	Promotion getPromotion(long id);
	Promotion getPromotionByFriendlyId(String friendlyId);
	PromotionImages getPromotionImages(long id);
	PromotionImages getPromotionImagesByLandingPageID(Object id);
	List getPromotions(Date beginDate);
	List getCoupons(Date beginDate);
	List getPromotions();
	List getCoupons();
	List getAllCouponsAndPromos();
	List getAllCouponsAndPromos(int type);
	List getAllCouponsAndPromos(boolean filterByDate);
	void createPromotion(PromotionImages p) throws Exception;
	void deletePromotion(Integer id);
	Long getMaxOrderId(long curr);
	byte[] resizeImage(byte[] in, int width, int height) throws Exception;
	void updatePromotion(PromotionImages p) throws Exception;
	List getAllSitesCouponsAndPromos();
	List getAllSitesCouponsAndPromos(boolean filterByDate);
	PromotionImages getPromotionImagesById(Object id);
	PromotionImages getPromotionImagesById(Object id,boolean checkDates);
	
	List findPromotionsByIds(String pipeDelimitedIds);
	List findPromotionsByIds(List friendlyIds);
	List findPromotionsByFriendlyIds(String pipeDelimitedIds);
	List findPromotionsByFriendlyIds(List friendlyIds);
	
	List findPromotionsByIds(String pipeDelimitedIds,boolean checkDates);
	List findPromotionsByIds(List friendlyIds,boolean checkDates);
	List findPromotionsByFriendlyIds(String pipeDelimitedIds,boolean checkDates);
	List findPromotionsByFriendlyIds(List friendlyIds,boolean checkDates);
	
	List<AbstractPromotion> findPromotionsByFriendlyIds(List<String> friendlyPromotionIds, Date targetDate);
	
	List<Promotion> getActivePromotionsByType(String site, String promotionType, Date startDate);
	List<Promotion> getActivePromotionsByLandingPageId(String site, String landingPageId, Date startDate);
	String getDisclaimerByLandingPageId(String siteName, String landingPageId);
	List<Promotion> getHomePageCoupons(String site, Date startDate);
	
	List<PromotionType> getPromotionTypes();
	
	List<Object[]> checkPromotionsForImage(List<Long> promoIds);
	List<Promotion> getActiveRepairOffers(String siteName, Date startDate);
	List<Promotion> getActiveMaintenanceOffers(String siteName, Date startDate);
	List<Promotion> getActiveRepairAndHomeOffers(String siteName, Date startDate);
	List<Promotion> getActiveMaintenanceAndHomeOffers(String siteName, Date startDate);
	List<Promotion> getActiveTireCoupons(String siteName, Date startDate);
	PromotionImages getActivePromotionImagesById(String siteName, long id, Date startDate);
	PromotionImages getActivePromotionImagesByFriendlyId(String siteName, String friendlyId, Date startDate);
	
	PromotionLandingType findPromotionLandingType(String webSite,String promoName);
	List<PromotionLandingType> getPromotionLandingTypes();
	public Object[] findOilPromotionByPriceType(String friendlyId);
	int updatePromotionDisabled(Long promotionId, boolean disabled);
	int updatePromotionApproved(Long promotionId, boolean approved);	
	List<String> getPromotionImageFileIds();
	int updatePromotionStartDate(Long promotionId, String startDate);
	public Map<Long, PromotionBrand> getPromotionBrandsDetails();	
}
