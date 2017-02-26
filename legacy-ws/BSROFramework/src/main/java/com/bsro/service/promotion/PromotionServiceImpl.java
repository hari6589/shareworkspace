package com.bsro.service.promotion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import com.bfrc.framework.dao.JDA2CatalogDAO;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.Config;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.dao.TirePromotionDAO;
import com.bfrc.pojo.promotion.AbstractPromotion;
import com.bfrc.pojo.promotion.Promotion;
import com.bfrc.pojo.promotion.PromotionImages;
import com.bfrc.pojo.promotion.PromotionType;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;

@Service("promotionService")
public class PromotionServiceImpl implements PromotionService {
	
	private String offerDateDisclaimerFormat = "MMMM d, yyyy";
	
	@Autowired
	private PromotionDAO promotionDAO;
	
	@Autowired
	private TirePromotionDAO tirePromotionDAO;
	
	@Autowired
	JDA2CatalogDAO jda2CatalogDAO;
	
	@Autowired
	private Config config;
	
	private final String DEFAULT_PROMO_TYPE = "get";
	//NOT COUPONS FOR HOME PAGE
	private final String HOME_PAGE_PROMO_TYPE = "X";
	
	private Map<String, PromotionType> promoTypeMap;
	
	private Map<String, PromotionType> promoTypeToNameMap;
	
	public void setConfig(Config config) {
		this.config = config;
	}

	private final Log logger = LogFactory.getLog(PromotionServiceImpl.class);
	
	@PostConstruct
	public void initialize(){
		setupPromoTypeMaps();
	}
	
	/* (non-Javadoc)
	 * @see com.bsro.service.promotion.PromotionService#getPromotionsByFriendlyIds(java.util.List)
	 */
	@Override
	public List<AbstractPromotion> getPromotionsByFriendlyIds(List<String> friendlyPromotionIds, Date targetDate){
		long startTime = System.currentTimeMillis();
		
		List<AbstractPromotion> promos = (List<AbstractPromotion>)promotionDAO.findPromotionsByFriendlyIds(friendlyPromotionIds, targetDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("Lookup took (in ms): " + elapsedTime); }
		return promos;
		
	}
	
	public AbstractPromotion getPromotionByFriendlyId(String friendlyid){
		long startTime = System.currentTimeMillis();
		
		AbstractPromotion promotion = promotionDAO.getPromotionByFriendlyId(friendlyid);	
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("Lookup took (in ms): " + elapsedTime);}
		return promotion;
		
	}

	public void setPromotionDAO(PromotionDAO promotionDAO) {
		this.promotionDAO = promotionDAO;
	}
	
	public List<Promotion> getActivePromotionsByType(String promotionType, Date startDate) {
		long startTime = System.currentTimeMillis();
		
		List<Promotion> promotions = promotionDAO.getActivePromotionsByType(config.getSiteName(), promotionType, startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("Lookup took (in ms): " + elapsedTime);}
		return promotions;
	}

	@Override
	public List<Promotion> getActivePromotionsByLandingPageId(
			String landingPageId, Date startDate) {
		long startTime = System.currentTimeMillis();
		
		List<Promotion> activePromos = promotionDAO.getActivePromotionsByLandingPageId(config.getSiteName(),landingPageId, startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActivePromotionsByLandingPageId took (in ms): " + elapsedTime);}
		
		return activePromos;
	}
	
	@Override
	public String getDisclaimerByLandingPageId(String landingPageId){
		return promotionDAO.getDisclaimerByLandingPageId(config.getSiteName(),landingPageId);
	}
	
	public List<TirePromotionEvent> getActiveTirePromotions(Date startDate){
		long startTime = System.currentTimeMillis();
		
		List <TirePromotionEvent> promos = tirePromotionDAO.getPublishedTirePromotionsBySite(config.getSiteName(), startDate);
		
		List <TirePromotionEvent> finalList = new ArrayList<TirePromotionEvent>();
		for(TirePromotionEvent p : promos){
			if(config.getSiteName().equalsIgnoreCase(Config.FCAC) && "Y".equals(String.valueOf(p.getFCACOffersFlag())) ){
				finalList.add(p);
			}
		}
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ 
			logger.info("getActiveTirePromotions took (in ms): " + elapsedTime);
			logger.info("found tire promotions: " + promos.size());
		}
		
		
		return finalList;
		
	}
	
	@Override
	public List<Promotion> getActiveHomePageCoupons(Date startDate) {
		long startTime = System.currentTimeMillis();
		
		//this gets PROMOTIONS that have a home_page_offer of 1
		List<Promotion> activePromos = promotionDAO.getHomePageCoupons(config.getSiteName(), startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActiveHomePageCoupons took (in ms): " + elapsedTime);}
		
		return activePromos;
	}
	
	@Override
	public List<Promotion> getActiveHomePagePromotions(Date startDate) {
		long startTime = System.currentTimeMillis();
		
		//not putting the HOME_PAGE_PROMO_TYPE into the
		//PROMO_TYPE table so that we do not get a landing page.
		List<Promotion> activePromos = promotionDAO.getActivePromotionsByType(config.getSiteName(), HOME_PAGE_PROMO_TYPE, startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActiveHomePagePromotions took (in ms): " + elapsedTime);}
		
		return activePromos;
	}
	
	@Override
	public void formatPromotion(AbstractPromotion promotion){
			
		if(promotion != null){
			String promotionTypeUrl = "offers/" + getPromoTypeByType(String.valueOf(promotion.getPromoType()));
			promotion.setPromotionTypeUrl(promotionTypeUrl);
			
			SimpleDateFormat sdf = new SimpleDateFormat(offerDateDisclaimerFormat);
			if(promotion.getOfferStart() != null) promotion.setOfferStartDateFormatted(sdf.format(promotion.getOfferStart()));
			if(promotion.getOfferEnd() != null) promotion.setOfferEndDateFormatted(sdf.format(promotion.getOfferEnd()));
		}
		
	}
	
	protected void setupPromoTypeMaps() {
		List<PromotionType> promoTypes = promotionDAO.getPromotionTypes();
		promoTypeMap = new HashMap<String, PromotionType>();
		promoTypeToNameMap = new HashMap<String, PromotionType>();
		
		for(PromotionType promoType : promoTypes) {
			promoTypeMap.put(promoType.getPromoName(), promoType);
			promoTypeToNameMap.put(promoType.getPromoType(), promoType);
			
			if("Z".equalsIgnoreCase(promoType.getPromoType())) {
				promoTypeMap.put(DEFAULT_PROMO_TYPE, promoType);
				promoTypeToNameMap.put(DEFAULT_PROMO_TYPE, promoType);
			}
		}
	}

	public String getPromoTypeByName(String name) {
		String value = null;
		if(StringUtils.isBlank(name)) {
			name = DEFAULT_PROMO_TYPE;
		}
		
		if(promoTypeMap.containsKey(name)) {
			value = promoTypeMap.get(name).getPromoType();
		}
		else {
			value = DEFAULT_PROMO_TYPE;
		}
		
		return value;
	}
	
	public PromotionType getPromotionTypeObjectByName(String name) {
		PromotionType promoType;
		
		if(promoTypeMap.containsKey(name)) {
			promoType = promoTypeMap.get(name);
		}
		else {
			promoType = promoTypeMap.get(DEFAULT_PROMO_TYPE);
		}
		
		return promoType;
	}


	public String getPromoTypeByType(String type) {
		String value = null;
		if(StringUtils.isBlank(type)) {
			type = DEFAULT_PROMO_TYPE;
		}
		
		if(promoTypeToNameMap.containsKey(type)) {
			value = promoTypeToNameMap.get(type).getPromoName();
		}
		else {
			value = DEFAULT_PROMO_TYPE;
		}
		
		return value;
	}

	public PromotionType getPromotionTypeObjectByType(String type) {
		PromotionType promoType;
		if(promoTypeToNameMap.containsKey(type)) {
			promoType = promoTypeToNameMap.get(type);
		}
		else {
			promoType = promoTypeToNameMap.get(DEFAULT_PROMO_TYPE);
		}
		
		return promoType;
	}
	
	public List<Promotion> removePromotionsWithoutImage(List<Promotion> promotionsToCheck){
		long startTime = System.currentTimeMillis();
		
		if(promotionsToCheck != null && !promotionsToCheck.isEmpty()){
			Map<Long, Promotion> idsToCheck = new HashMap<Long, Promotion>();
			for(Promotion temp : promotionsToCheck){
				idsToCheck.put(new Long(temp.getPromotionId()), temp);
			}
			
			List<Long> justIds = new ArrayList<Long>(idsToCheck.keySet());
			List<Object[]> idsAndImageFlag = promotionDAO.checkPromotionsForImage(justIds);
			
			if(idsAndImageFlag != null){
				//[0] is promotionId is long, [1] is boolean.
				for(Object[] array: idsAndImageFlag){
					Long foundId = (Long) array [0];
					boolean hasImage = (Boolean) array[1];
					//if we don't have an image, thats when we should remove it for display
					if(!hasImage){
						if(idsToCheck.containsKey(foundId)){
							Promotion p = idsToCheck.get(foundId);
							promotionsToCheck.remove(p);
						}
					}
				}
			}
		}
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("removePromotionsWithoutImage took (in ms): " + elapsedTime);}
		return promotionsToCheck;
		
	}

	@Override
	public List<Promotion> getActiveRepairOffers(Date startDate) {
		long startTime = System.currentTimeMillis();
		
		List<Promotion> activePromos = promotionDAO.getActiveRepairOffers(config.getSiteName(), startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActiveRepairOffers took (in ms): " + elapsedTime);}
		
		return activePromos;
	}

	@Override
	public List<Promotion> getActiveMaintenanceOffers(Date startDate) {
		long startTime = System.currentTimeMillis();
		
		List<Promotion> activePromos = promotionDAO.getActiveMaintenanceOffers(config.getSiteName(), startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActiveMaintenanceOffers took (in ms): " + elapsedTime);}
		
		return activePromos;
	}

	@Override
	public List<Promotion> getActiveTireCoupons(Date startDate) {
		long startTime = System.currentTimeMillis();
		
		List<Promotion> activePromos = promotionDAO.getActiveTireCoupons(config.getSiteName(), startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActiveTireCoupons took (in ms): " + elapsedTime);}
		
		return activePromos;
	}
	
	public List<Promotion> filterDuplicateOffers(List<Promotion> promotionsToCheck, int maxPromotionsReturned) {
		// remove duplicate promotions by description
		List<Promotion> uniquePromotions = new ArrayList<Promotion>();
		Set<String> ds = new HashSet<String>();
		if(promotionsToCheck != null){
			for( Promotion p : promotionsToCheck ) {
			    if( "offers".equalsIgnoreCase(p.getLandingPageId()) && ds.add(p.getDescription()) ) {
			    	formatPromotion(p);
			    	uniquePromotions.add(p);
			    }
			}
		}
		
		if(maxPromotionsReturned>0 && uniquePromotions.size() > maxPromotionsReturned){
			uniquePromotions = uniquePromotions.subList(0, maxPromotionsReturned);
		}
		
		return uniquePromotions;
	}
	
	public List<Promotion> filterDuplicateOffers(List<Promotion> promotionsToCheck) {
		return filterDuplicateOffers(promotionsToCheck,-1);
	}
		
	public PromotionImages getActivePromotionImagesById(Long id, Date startDate){
		long startTime = System.currentTimeMillis();
		
		PromotionImages activePromo = promotionDAO.getActivePromotionImagesById(config.getSiteName(), id, startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActivePromotionImagesById took (in ms): " + elapsedTime);}
		
		return activePromo;
	}
	
	public PromotionImages getActivePromotionImagesByFriendlyId(String friendlyId, Date startDate){
		long startTime = System.currentTimeMillis();
		
		PromotionImages activePromo = promotionDAO.getActivePromotionImagesByFriendlyId(config.getSiteName(), friendlyId, startDate);
		
		long elapsedTime = (System.currentTimeMillis() - startTime);
		if(logger.isInfoEnabled()){ logger.info("getActivePromotionImagesByFriendlyId took (in ms): " + elapsedTime);}
		
		return activePromo;
	}
	
	// used to fetch all promoImages
	public Map getAllPromotionImages()
	{
		List allTires = jda2CatalogDAO.getAllDisplays();
		Map map = new HashMap();
		for (int i = 0; i < allTires.size(); i++) {
			com.bfrc.pojo.tire.jda2.Display tire = (com.bfrc.pojo.tire.jda2.Display) allTires.get(i);
			if(tire.getValue() != null )
			{
				map.put(tire.getValue().replaceAll("[^a-zA-Z0-9]+",""),tire.getBrandId());
			}
			
		}
		
		//Map tireCouponsBrand = promotionDAO.getPromotionImageBrandId();
		return map;
	}
	
	// used to fetch all promoImages
		public Map getAllPromotionBrands()
		{
			Map mappedBrands = jda2CatalogDAO.getMappedBrands();
			
			return mappedBrands;
		}

}
