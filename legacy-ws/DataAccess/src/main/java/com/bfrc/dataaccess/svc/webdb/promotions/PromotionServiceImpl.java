package com.bfrc.dataaccess.svc.webdb.promotions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.PromotionDAO;
import com.bfrc.dataaccess.dao.generic.TirePromotionEventDAO;
import com.bfrc.dataaccess.model.promotion.Promotion;
import com.bfrc.dataaccess.model.promotion.TirePromotionEvent;
import com.bfrc.dataaccess.svc.webdb.PromotionsService;
import com.bfrc.dataaccess.util.DateUtils;
import com.bfrc.framework.dao.TirePromotionDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.pojo.promotion.PromotionType;
import com.bfrc.pojo.tirepromotion.TirePromotionArticle;
import com.bsro.service.promotion.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionsService {

	public static final String PUBLISHED = "P";
	private static double zeroDiscountPromoAmount = 0.0000001;
	@Autowired
	private PromotionDAO promotionDao;
	@Autowired
	private TirePromotionEventDAO tirePromotionEventDao;
	@Autowired
	private TirePromotionDAO tirePromotionDAO;
	@Autowired
	private PromotionService promotionService;
	@Autowired
	LocatorOperator locator;

	public Map<String, List> getSpecialOffers(String siteName, Long storeNumber,
			Date startDate, String friendlyId, String promoType, String promoCategory) {
		
		if (siteName != null && !siteName.isEmpty())
			locator.getConfig().setSiteName(siteName);
		
		List<TirePromotionEvent> tirePromotions = new ArrayList<TirePromotionEvent>();
		List<Promotion> coupons = new ArrayList<Promotion>();
		List<Promotion> promotions = new ArrayList<Promotion>();
		
		
		List<TirePromotionEvent> tmpTirePromotions = (((promoType == null || promoType.isEmpty()) && (promoCategory == null || promoCategory.isEmpty())) || "W".equalsIgnoreCase(promoType) || "tire".equalsIgnoreCase(promoCategory)) ? 
														getTirePromotions(siteName, storeNumber, formatFriendlyId(friendlyId)) : new ArrayList<TirePromotionEvent>();
		List<Promotion> tmpCoupons = getCoupons(siteName, startDate, friendlyId, promoType, promoCategory);
		List<Promotion> tmpPromotions = getPromotions(siteName, startDate, friendlyId, promoType);
		
		updateAttributeValues(tmpTirePromotions, tmpCoupons, siteName);
		updateAttributeValues(tmpTirePromotions, tmpPromotions, siteName);
		
		List<Long> promotionIds = new ArrayList<Long>();
		List<String> promotionNames = new ArrayList<String>();
		for(TirePromotionEvent t : tmpTirePromotions) {
			if("Y".equals(t.getFCACOffersFlag()) && !promotionIds.contains(t.getPromoId())) {
				promotionIds.add(t.getPromoId());
				promotionNames.add(StringUtils.trimToEmpty(t.getPromoDisplayName()));
				tirePromotions.add(t);
			}
		}
		
		Date now = new Date();
		for(Promotion c : tmpCoupons) {
			if(c.getExpirationDate() == null || c.getExpirationDate().after(now)) {
				if(!promotionNames.contains(StringUtils.trimToEmpty(c.getDescription()))) {
					coupons.add(c);
				}
			}
		}
		
		for(Promotion p : tmpPromotions) {
			if(p.getExpirationDate() == null || p.getExpirationDate().after(now)) {
				promotions.add(p);
			}
		}
		
		Map<String, List> loResults = new HashMap<String, List>();
		if (((promoType == null || promoType.isEmpty()) && (promoCategory == null || promoCategory.isEmpty())) || "W".equalsIgnoreCase(promoType) || "tire".equalsIgnoreCase(promoCategory))
			loResults.put("TIRES", tirePromotions);
		if (((promoType == null || promoType.isEmpty()) && (promoCategory == null || promoCategory.isEmpty())) || (promoType == null || !"W".equalsIgnoreCase(promoType)) && (promoCategory == null || !"tire".equalsIgnoreCase(promoCategory))) {
			loResults.put("COUPONS", coupons);
			loResults.put("PROMOTIONS", promotions);
		}
		return loResults;
	}
	
	@SuppressWarnings("rawtypes")
	private void updateAttributeValues(List<TirePromotionEvent> tmpTirePromotions,
			List<Promotion> tmpCoupons, String siteName) {
		Map imageMap = null;
		Map brandMap = null;
		List<TirePromotionEvent> deletePromoList = new ArrayList<TirePromotionEvent>();
		if (tmpCoupons != null) {
			imageMap = promotionService.getAllPromotionImages();
			brandMap = promotionService.getAllPromotionBrands();
		}
		for(TirePromotionEvent t : tmpTirePromotions) {
			boolean matched = false;
			for(Promotion c : tmpCoupons) {
				if (t.getPromoName().equalsIgnoreCase(c.getFriendlyId())) {
					matched = true;
					if (t.getOrderId() == null && c.getOrderId() != null) {
						t.setOrderId(c.getOrderId());
					}
					if (t.getPromotionType() == null) {
						PromotionType promotionType = promotionService.getPromotionTypeObjectByType(c.getPromoType());
						if (promotionType != null) {
							app.bsro.model.promotions.PromotionType promotype = new app.bsro.model.promotions.PromotionType();
							promotype.setName(promotionType.getPromoName());
							promotype.setTitle(promotionType.getOfferPageTitle());
							promotype.setType(promotionType.getPromoType());
							t.setPromotionType(promotype);
							c.setPromotionType(promotype);
						}
					}
					
					if (c.getOfferStartDate() != null) t.setOfferStartDate(c.getOfferStartDate());
					if (c.getOfferEndDate() != null) t.setOfferEndDate(c.getOfferEndDate());
					if (c.getUrl() != null) t.setRebateURL(c.getUrl());
					
					if(c.getTireOffer().booleanValue()) {
						if(c.getImageFileId() != null) {
							Object brandId = imageMap.get(c.getImageFileId());
							com.bfrc.pojo.tire.jda2.Brand brand = (com.bfrc.pojo.tire.jda2.Brand) brandMap.get(brandId+ "");
							if(brand != null) {
								t.setBrandName(brand.getValue());
							}
						}
					}
					if (t.getImageFileId() == null && c.getImageFileId() != null) {
						t.setImageFileId(c.getImageFileId());
					}
					t.setTitle(c.getTitle());
					t.setPrice(c.getPrice());
					t.setPromoDescription(c.getPromoDescription());
					t.setHomePageOffer(c.getHomePageOffer());					
				}
			}
			if (!matched) {
				deletePromoList.add(t);
			}
		}
		
		for(Promotion c : tmpCoupons) {
			if (c.getPromotionType() == null) {
				if ("O".equalsIgnoreCase(c.getPromoType())) {
					app.bsro.model.promotions.PromotionType promotype = new app.bsro.model.promotions.PromotionType();
					promotype.setName("email-coupons");
					promotype.setTitle("Email Coupons");
					promotype.setType("O");
					c.setPromotionType(promotype);
				} else {
					PromotionType promotionType = promotionService.getPromotionTypeObjectByType(c.getPromoType());
					if (promotionType != null) {
						app.bsro.model.promotions.PromotionType promotype = new app.bsro.model.promotions.PromotionType();
						promotype.setName(promotionType.getPromoName());
						promotype.setTitle(promotionType.getOfferPageTitle());
						promotype.setType(promotionType.getPromoType());
						c.setPromotionType(promotype);
					}
				}
			}
			if(c.getTireOffer() != null && c.getTireOffer().booleanValue()) {
				if(c.getImageFileId() != null) {
					Object brandId = imageMap.get(c.getImageFileId());
					com.bfrc.pojo.tire.jda2.Brand brand = (com.bfrc.pojo.tire.jda2.Brand) brandMap.get(brandId+ "");
					if(brand != null) {
						c.setBrand(brand.getValue());
					}
				}
			}
		}
		
		if (!deletePromoList.isEmpty()) {
			Iterator<TirePromotionEvent> it = tmpTirePromotions.iterator();
			while (it.hasNext()) {
				TirePromotionEvent tpe = (TirePromotionEvent) it.next();
				for(TirePromotionEvent t : deletePromoList) {
					if (tpe != null && tpe.getPromoName().equals(t.getPromoName())) {
						it.remove();
					}
				}
			}
		}
	}
	
	public List<Promotion> getOffersByLandingPage(String siteName, Date startDate, String landingPageId, String friendlyId, String promoType){
		List<Promotion> coupons = null;
		coupons = new ArrayList<Promotion>(promotionDao.findByLandingPageId(siteName, DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), landingPageId, formatFriendlyId(friendlyId), formatPromoType(promoType)));
		return cleanPromotionList(coupons);
	}
	
	public List<Promotion> findActiveRepairOffers(String siteName, Date startDate, String landingPageId, String friendlyId){
		List<Promotion> coupons = null;
		coupons = new ArrayList<Promotion>(promotionDao.findActiveRepairOffers(siteName, formatFriendlyId(friendlyId), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), landingPageId, formatFriendlyId(friendlyId)));
		return cleanPromotionList(coupons);
	}
	
	public List<Promotion> findActiveMaintenanceOffers(String siteName, Date startDate, String landingPageId, String friendlyId){
		List<Promotion> coupons = null;
		coupons = new ArrayList<Promotion>(promotionDao.findActiveMaintenanceOffers(siteName, formatFriendlyId(friendlyId), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), landingPageId, formatFriendlyId(friendlyId)));
		return cleanPromotionList(coupons);
	}
	
	private List<Promotion> findActiveServiceOffers(String siteName, Date startDate, String landingPageId, String friendlyId){
		List<Promotion> coupons = null;
		coupons = findActiveRepairOffers(siteName, startDate, landingPageId, friendlyId);
		coupons.addAll(findActiveMaintenanceOffers(siteName, startDate, landingPageId, friendlyId));
		return cleanPromotionList(coupons);
	}
	
	private List<TirePromotionEvent> getTirePromotions(String siteName, Long storeNumber, String friendlyId) {
		Collection<TirePromotionEvent> events = null;
		if(storeNumber == null || storeNumber.longValue() == 0)
			events = tirePromotionEventDao.findTirePromotionsBySiteAndStatus(siteName, PromotionServiceImpl.PUBLISHED, friendlyId);
		else
			events = tirePromotionEventDao.findTirePromotionsBySitesAndStoreNumber(siteName, storeNumber, friendlyId);

		return cleanTirePromotionEventList(events);
	}

	private List<Promotion> getCoupons(String siteName, Date startDate, String friendlyId, String promoType, String promoCategory) {
		List<Promotion> coupons = null;
		if (promoCategory != null && !promoCategory.isEmpty() && !"tire".equalsIgnoreCase(promoCategory)) {
			if ("repair".equalsIgnoreCase(promoCategory)) {
				coupons = findActiveRepairOffers(siteName, startDate, "offers", friendlyId);
			} else if ("maintenance".equalsIgnoreCase(promoCategory)) {
				coupons = findActiveMaintenanceOffers(siteName, startDate, "offers", friendlyId);
			} else if ("service".equalsIgnoreCase(promoCategory)) {
				coupons = findActiveServiceOffers(siteName, startDate, "offers", friendlyId);
			} else {
				coupons = new ArrayList<Promotion>();
			}
		} else if(friendlyId != null && !friendlyId.isEmpty()){
			coupons = new ArrayList(promotionDao.findBySiteStartExpirationAndFriendlyId(siteName, DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), formatFriendlyId(friendlyId)));
		} else{
			coupons = new ArrayList(promotionDao.findByLandingPageId(siteName, DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), "offers", formatFriendlyId(friendlyId), formatPromoType(promoType)));
		}
		return cleanPromotionList(coupons);
	}

	private List<Promotion> getPromotions(String siteName, Date startDate, String friendlyId, String promoType) {
		List<Promotion> promotions = new ArrayList(promotionDao.findBySiteStartExpirationAndType(siteName,formatFriendlyId(friendlyId), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), DateUtils.format(startDate, DateUtils.YYYYMMDDHH24MISS), "P", formatFriendlyId(friendlyId)));
		return cleanPromotionList(promotions);
	}
	
	private List<Promotion> cleanPromotionList(List<Promotion> promotions) {
		String IGNORE_DESCRIPTION = "banner:";
		String badCreditCardDescripton = "Credit now. Apply online.";
		Set<Promotion> distinctList = new HashSet<Promotion>();
		if (!promotions.isEmpty()) {
			for(Promotion p : promotions) {
				String desc = StringUtils.trimToEmpty(p.getDescription());
				boolean isBad = desc.startsWith(IGNORE_DESCRIPTION);
				if(!isBad) 
					isBad = desc.equals(badCreditCardDescripton);
	
				if(p != null && !isBad) {
					distinctList.add(p);
				}
			}
		}
		return new ArrayList<Promotion>(distinctList);
	}
	
	private List<TirePromotionEvent> cleanTirePromotionEventList(Collection<TirePromotionEvent> promotions) {
		String IGNORE_DESCRIPTION = "banner:";
		Set<TirePromotionEvent> distinctList = new HashSet<TirePromotionEvent>();
		for(TirePromotionEvent p : promotions) {
			String desc = StringUtils.trimToEmpty(p.getPromoDisplayName());
			boolean isBad = desc.startsWith(IGNORE_DESCRIPTION);
			if(p != null && !isBad) {
				distinctList.add(p);
			}
		}
		
		return new ArrayList<TirePromotionEvent>(distinctList);
	}
	
	private String formatFriendlyId(String friendlyId){
		if(friendlyId == null || friendlyId.isEmpty()){
			return "%";
		}
		return friendlyId;
	}
	
	private String formatPromoType(String promoType){
		if(promoType == null || promoType.isEmpty()){
			return "%";
		}
		return promoType.toUpperCase();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getOfferData(String siteName, String article, String rearArticle, String storeNumber, String qty, Map offersData) {
		Map m = new HashMap();
		//Map offersData = loadTirePromotionData(siteName, Long.parseLong(storeNumber), null);
		TirePromotionEvent promo = null;
		List rebateOffers = new ArrayList();
		List discountOffers = new ArrayList();
		List pid_list = new ArrayList();
		int count = Integer.parseInt(qty);
		String[] promoIds = null;
		String promoId = null;
		double discountAmount = 0.0d;
		double frontDiscountAmount = 0.0d;
		double frontDiscountAmountTotal = 0.0d;
		double rearDiscountAmount = 0.0d;
		double rearDiscountAmountTotal = 0.0d;
		if (offersData.containsKey(article)) {
			String ps = (String) offersData.get(article);
			promoIds = ps.split("\\|");
			for (int i = 0; i < promoIds.length; i++) {
				promo = (TirePromotionEvent) offersData.get("PROMO_" + promoIds[i]);
				promoId = String.valueOf(promo.getPromoId());
				if (('P' == promo.getSourcePromotionType().getPromoType().charAt(0)) || ('D' == promo.getSourcePromotionType().getPromoType().charAt(0))) {
					double this_frontDiscountAmount = 0;
					try {
						Collection<Object[]> tmpfrontDiscountAmount = tirePromotionEventDao.findDiscountAmountByStoreNumberMinQtyPromoId(
								Long.parseLong(article),
								Long.parseLong(storeNumber),
								(byte) count,
								promo.getPromoId());
						if (tmpfrontDiscountAmount != null)  {
							Iterator itr = tmpfrontDiscountAmount.iterator();
							Object[] fda = (Object[])itr.next();
							this_frontDiscountAmount = (new Double(fda[0].toString())).doubleValue();
						}
					} catch (Exception ex) {
					}
					if (this_frontDiscountAmount > 0.0) {
						frontDiscountAmount = this_frontDiscountAmount;
						discountAmount += this_frontDiscountAmount * count;
						frontDiscountAmountTotal += this_frontDiscountAmount * count;
						if (!pid_list.contains(promoId)) {
							discountOffers.add(promo);
							pid_list.add(promoId);
						}
					} else {
						frontDiscountAmount = zeroDiscountPromoAmount;
						discountAmount += zeroDiscountPromoAmount * count;
						frontDiscountAmountTotal += zeroDiscountPromoAmount * count;
						if (!pid_list.contains(promoId)) {
							discountOffers.add(promo);
							pid_list.add(promoId);
						}
					}
				} else {
					if (!pid_list.contains(promoId)) {
						rebateOffers.add(promo);
						pid_list.add(promoId);
					}
				}
			}
		}
		
		if (rearArticle != null) {
			if (offersData.containsKey(rearArticle)) {
				String ps = (String) offersData.get(rearArticle);
				promoIds = ps.split("\\|");
				for (int i = 0; i < promoIds.length; i++) {
					promo = (TirePromotionEvent) offersData.get("PROMO_" + promoIds[i]);
					promoId = String.valueOf(promo.getPromoId());
					if (('P' == promo.getSourcePromotionType().getPromoType().charAt(0)) || ('D' == promo.getSourcePromotionType().getPromoType().charAt(0))) {
						double this_rearDiscountAmount = 0;
						try {
							Collection<Object[]> tmpRearDiscountAmount = tirePromotionEventDao.findDiscountAmountByStoreNumberMinQtyPromoId(
									Long.parseLong(rearArticle),
									Long.parseLong(storeNumber),
									(byte) count,
									promo.getPromoId());
							if (tmpRearDiscountAmount != null)  {
								Iterator itr = tmpRearDiscountAmount.iterator();
								Object[] rda = (Object[])itr.next();
								this_rearDiscountAmount = (new Double(rda[0].toString())).doubleValue();
							}
						} catch (Exception ex) {
						}
						if (this_rearDiscountAmount > 0.0) {
							rearDiscountAmount = this_rearDiscountAmount;
							discountAmount += this_rearDiscountAmount * count;
							rearDiscountAmountTotal += this_rearDiscountAmount * count;
							if (!pid_list.contains(promoId)) {
								discountOffers.add(promo);
								pid_list.add(promoId);
							}
						}else{
							rearDiscountAmount = zeroDiscountPromoAmount;
							discountAmount += zeroDiscountPromoAmount * count;
							rearDiscountAmountTotal += zeroDiscountPromoAmount * count;
							if (!pid_list.contains(promoId)) {
								discountOffers.add(promo);
								pid_list.add(promoId);
							}
						}
					} else {
						if (!pid_list.contains(promoId)) {
							rebateOffers.add(promo);
							pid_list.add(promoId);
						}
					}
				}
			}
		}
		m.put("rebateOffers", rebateOffers);
		m.put("discountOffers", discountOffers);
		m.put("discountAmount", discountAmount);
		m.put("frontDiscountAmount", frontDiscountAmount);
		m.put("frontDiscountAmountTotal", frontDiscountAmountTotal);
		m.put("rearDiscountAmount", rearDiscountAmount);
		m.put("rearDiscountAmountTotal", rearDiscountAmountTotal);
		return m;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map loadTirePromotionData(String siteName, long storeNumber, String friendlyId) {
		Map data = new HashMap();
		List<TirePromotionEvent> tmpTirePromotions = getTirePromotions(siteName, storeNumber, formatFriendlyId(friendlyId));
		if(tmpTirePromotions != null && tmpTirePromotions.size() > 0){
			for(Iterator it = tmpTirePromotions.iterator(); it.hasNext();){
			    TirePromotionEvent promo = (TirePromotionEvent)it.next();
			    String  promoId = String.valueOf(promo.getPromoId());
			    List a  = tirePromotionDAO.getTirePromotionArticles(new Long(promoId));
			    if(a != null && a.size() > 0){
					for(Iterator ita = a.iterator(); ita.hasNext();){
						TirePromotionArticle art = (TirePromotionArticle)ita.next();
						String articleNum = String.valueOf(art.getId().getArticle());
						if(data.get(articleNum) == null){
						    data.put(String.valueOf(articleNum), String.valueOf(promoId));
						}else{
							String prevStr = (String)data.get(articleNum);
							data.put(String.valueOf(articleNum), prevStr+"|"+String.valueOf(promoId));
						}
					}
					data.put("PROMO_"+promoId,promo);
					
			    }
			    
			}
		}
		return data;
	}
}
