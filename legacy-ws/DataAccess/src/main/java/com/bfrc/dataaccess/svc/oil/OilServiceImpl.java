package com.bfrc.dataaccess.svc.oil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.oil.OilChangePackage;
import app.bsro.model.oil.OilChangeQuote;
import app.bsro.model.oil.OilVehicle;

import com.bfrc.dataaccess.dao.oil.OilChangeStorePriceDAO;
import com.bfrc.dataaccess.dao.oil.OilDAO;
import com.bfrc.dataaccess.dao.oil.OilFilterPriceDAO;
import com.bfrc.dataaccess.dao.oil.OilStorePriceDAO;
import com.bfrc.dataaccess.dao.promotion.PromotionBusinessRulesDAO;
import com.bfrc.dataaccess.dao.quote.QuoteDAO;
import com.bfrc.dataaccess.dao.quote.QuoteItemTypeDAO;
import com.bfrc.dataaccess.dao.quote.QuoteTypeDAO;
import com.bfrc.dataaccess.model.oil.OatsOilRecommendationCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleMakeCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearToMake;
import com.bfrc.dataaccess.model.oil.Oil;
import com.bfrc.dataaccess.model.oil.OilChange;
import com.bfrc.dataaccess.model.oil.OilChangeStorePrice;
import com.bfrc.dataaccess.model.oil.OilChangeStorePriceId;
import com.bfrc.dataaccess.model.oil.OilFilter;
import com.bfrc.dataaccess.model.oil.OilFilterPrice;
import com.bfrc.dataaccess.model.oil.OilStorePrice;
import com.bfrc.dataaccess.model.oil.OilStorePriceId;
import com.bfrc.dataaccess.model.promotion.PromotionBusinessRules;
import com.bfrc.dataaccess.model.quote.Quote;
import com.bfrc.dataaccess.model.quote.QuoteItem;
import com.bfrc.dataaccess.model.quote.QuoteItemType;
import com.bfrc.dataaccess.model.quote.QuoteType;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.lookup.SeoVehicleData;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;
import com.bfrc.framework.dao.SeoVehicleDataDAO;

@Service
public class OilServiceImpl implements IOilService {

	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private OilDAO oilDao;
	@Autowired
	private OilStorePriceDAO oilStorePriceDao;
	@Autowired
	private OilChangeStorePriceDAO oilChangeStorePriceDao;
	@Autowired
	private OilFilterPriceDAO oilFilterPriceDao;
	@Autowired
	private QuoteDAO quoteDao;
	@Autowired
	private QuoteTypeDAO quoteTypeDao;
	@Autowired
	private QuoteItemTypeDAO quoteItemTypeDao;
	
	@Autowired
	private PromotionBusinessRulesDAO promotionBusinesRulesDAO;
	
	@Autowired
	private SeoVehicleDataDAO seoVehicleDataDAO;
	
	
	public PromotionBusinessRulesDAO getPromotionBusinesRulesDAO() {
		return promotionBusinesRulesDAO;
	}
	public void setPromotionBusinesRulesDAO(
			PromotionBusinessRulesDAO promotionBusinesRulesDAO) {
		this.promotionBusinesRulesDAO = promotionBusinesRulesDAO;
	}

	
	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	
	public OilDAO getOilDao() {
		return oilDao;
	}
	public void setOilDao(OilDAO oilDao) {
		this.oilDao = oilDao;
	}
	
	public OilStorePriceDAO getOilStorePriceDao() {
		return oilStorePriceDao;
	}
	public void setOilStorePriceDao(OilStorePriceDAO oilStorePriceDao) {
		this.oilStorePriceDao = oilStorePriceDao;
	}	
	
	public Oil getOilByArticleNumber(Long articleNumber) {
		return oilDao.get(articleNumber);
	}

	private List<Oil> getOilsByOATSName(String oatsName) {
		List<Oil> result = oilDao.findOilByOATSName(oatsName);
		
		return result;
	}
	
	public OilChangeStorePriceDAO getOilChangeStorePriceDao() {
		return oilChangeStorePriceDao;
	}
	
	public void setOilChangeStorePriceDao(OilChangeStorePriceDAO oilChangeStorePriceDao) {
		this.oilChangeStorePriceDao = oilChangeStorePriceDao;
	}
	
	public OilFilterPriceDAO getOilFilterPriceDao() {
		return oilFilterPriceDao;
	}
	
	public void setOilFilterPriceDao(OilFilterPriceDAO oilFilterPriceDao) {
		this.oilFilterPriceDao = oilFilterPriceDao;
	}
	
	public QuoteDAO getQuoteDao() {
		return quoteDao;
	}
	
	public void setQuoteDao(QuoteDAO quoteDao) {
		this.quoteDao = quoteDao;
	}
	
	public QuoteTypeDAO getQuoteTypeDao() {
		return quoteTypeDao;
	}
	
	public void setQuoteTypeDao(QuoteTypeDAO quoteItemDao) {
		this.quoteTypeDao = quoteItemDao;
	}
	
	public QuoteItemTypeDAO getQuoteItemTypeDao() {
		return quoteItemTypeDao;
	}
	
	public void setQuoteItemTypeDao(QuoteItemTypeDAO quoteItemTypeDao) {
		this.quoteItemTypeDao = quoteItemTypeDao;
	}	

	public OilChangePackage getHighMileageOilChangePackageByStoreNumberAndViscosityAndQuarts(Long storeNumber, String viscosity, BigDecimal quarts) {
		
		
		Oil highMileageOil = oilDao.findHighMileageOilByViscosity(viscosity);
		if (highMileageOil != null) {
			return getOilChangePackageForBaseOil(highMileageOil, storeNumber, quarts);
		} else {
			return null;
		}
	}
	
	public OilChangePackage getOilChangeSearchResultByStoreNumberOatsNameAndQuarts(Long storeNumber, String oatsName, BigDecimal quarts) {
		
		
		
		List<Oil> oils = getOilsByOATSName(oatsName.trim());
		
		
		
		Oil oil = null;
		
		if (oils != null && !oils.isEmpty()) {
			oil = oils.get(0);
		} else {
			logger.severe("Could not find a match for OATS oil name "+oatsName);
		}
		
		return getOilChangePackageForBaseOil(oil, storeNumber, quarts);
	}
	
	public void updateOilChangeQuoteFirstName(Long oilChangeQuoteId,String siteName,String firstName) {
		Quote quote = quoteDao.findQuoteByIdAndSite(oilChangeQuoteId, siteName);
		quote.setFirstName(firstName);
		quoteDao.update(quote);
	}

	public void updateOilChangeQuoteLastName(Long oilChangeQuoteId, String siteName, String lastName) {
		Quote quote = quoteDao.findQuoteByIdAndSite(oilChangeQuoteId, siteName);
		quote.setLastName(lastName);
		quoteDao.update(quote);
	}

	
	
	public OilChangeQuote getOilChangeQuote(Long oilChangeQuoteId, String siteName) {
		Quote quote = quoteDao.findQuoteByIdAndSite(oilChangeQuoteId, siteName);

		OilChangeQuote oilChangeQuote = new OilChangeQuote();
		
		oilChangeQuote.setOilChangeQuoteId(quote.getQuoteId());
		oilChangeQuote.setStoreNumber(quote.getStoreNumber());
		oilChangeQuote.setCreatedDate(quote.getCreatedDate());
		
		oilChangeQuote.setFirstName(quote.getFirstName());
		oilChangeQuote.setLastName(quote.getLastName());
		
		OilVehicle oilVehicle = new OilVehicle();
		oilVehicle.setYear(Integer.toString(quote.getVehicleYear()));
		oilVehicle.setMake(quote.getVehicleMake());
		
		
		oilVehicle.setModelAndSubmodelAndEngine(quote.getVehicleModelSubmodelEngine());
		oilVehicle.setVehicleId(quote.getVehicleId());
		
		oilChangeQuote.setVehicle(oilVehicle);
		
		// No null checks here, because none of these should ever be null. If they are null, that's a data error and we can't present pricing.
		BigDecimal additionalQuarts = null;
		BigDecimal additionalOilSubtotal = new BigDecimal(0.00);
		
		BigDecimal baseOilOilChangeAndOilFilterSubtotal = new BigDecimal(0.00);
		BigDecimal baseOilOilChangeOilFilterAndAdditionalOilSubtotal = new BigDecimal(0.00);
		
		if (quote.getQuoteItems() != null && !quote.getQuoteItems().isEmpty()) {
			for (QuoteItem quoteItem : quote.getQuoteItems()) {
				if (quoteItem.getQuoteItemType().getFriendlyId().equals(QUOTE_ITEM_TYPE_BASE_OIL)) {
					oilChangeQuote.setBaseOil(oilDao.get(quoteItem.getArticleNumber()));
					baseOilOilChangeAndOilFilterSubtotal = baseOilOilChangeAndOilFilterSubtotal.add(quoteItem.getPricePerUnit().multiply(new BigDecimal(quoteItem.getQuantity())));
					
					oilChangeQuote.setOilType(oilDao.getOilType(oilChangeQuote.getBaseOil().getOilTypeId()));
					
					
					
				} else if (quoteItem.getQuoteItemType().getFriendlyId().equals(QUOTE_TYPE_OIL_CHANGE)) {
					oilChangeQuote.setOilChange(oilDao.getOilChange(quoteItem.getArticleNumber()));
					baseOilOilChangeAndOilFilterSubtotal = baseOilOilChangeAndOilFilterSubtotal.add(quoteItem.getPricePerUnit().multiply(new BigDecimal(quoteItem.getQuantity())));
			
		
				} else if (quoteItem.getQuoteItemType().getFriendlyId().equals(QUOTE_ITEM_TYPE_OIL_FILTER)) {
					// there is only one...for now
					oilChangeQuote.setOilFilter(oilDao.getOilFilter());
					baseOilOilChangeAndOilFilterSubtotal = baseOilOilChangeAndOilFilterSubtotal.add(quoteItem.getPricePerUnit().multiply(new BigDecimal(quoteItem.getQuantity())));
		
				
				} else if (quoteItem.getQuoteItemType().getFriendlyId().equals(QUOTE_ITEM_TYPE_ADDITIONAL_OIL)) {
					oilChangeQuote.setAdditionalOil(oilDao.get(quoteItem.getArticleNumber()));
					additionalOilSubtotal = quoteItem.getPricePerUnit().multiply(new BigDecimal(quoteItem.getQuantity()));
					additionalQuarts = oilChangeQuote.getAdditionalOil().getMaximumQuarts().multiply(new BigDecimal(quoteItem.getQuantity()));
					oilChangeQuote.setAdditionalQuarts(additionalQuarts);
				} 
			}
		}

		if (oilChangeQuote.getBaseOil() == null || oilChangeQuote.getOilChange() == null || oilChangeQuote.getOilFilter() == null) {
			throw new IllegalStateException("Quote "+oilChangeQuoteId+" does not contain all of the required oil change items");
		}
		
		baseOilOilChangeOilFilterAndAdditionalOilSubtotal = baseOilOilChangeAndOilFilterSubtotal.add(additionalOilSubtotal);
		
		oilChangeQuote.setBaseOilOilChangeAndOilFilterSubtotal(baseOilOilChangeAndOilFilterSubtotal);
		oilChangeQuote.setAdditionalOilSubtotal(additionalOilSubtotal);
		oilChangeQuote.setBaseOilOilChangeOilFilterAndAdditionalOilSubtotal(baseOilOilChangeOilFilterAndAdditionalOilSubtotal);
		
		PromotionBusinessRules promoBusinessRules = null;
		
		if ( promotionBusinesRulesDAO.findOilPromotionByPriceType(oilChangeQuote.getOilType().getOilTypeFriendlyId()) != null) {
			 promoBusinessRules = promotionBusinesRulesDAO.findOilPromotionByPriceType(oilChangeQuote.getOilType().getOilTypeFriendlyId());
		}
		
		BigDecimal priceBeforePercentDiscount = oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal();
		
		
		
		if(promoBusinessRules != null && promoBusinessRules.getPriceModificationType() != null) {
			if( promoBusinessRules.getPriceModificationType().equalsIgnoreCase("PERCENT_DISCOUNT")) {
				BigDecimal PROMOTIONAL_PERCENT_DISCOUNT =	promoBusinessRules.getPercentDiscount();
				if (PROMOTIONAL_PERCENT_DISCOUNT != null) {
					BigDecimal priceAfterPercentDiscount = priceBeforePercentDiscount.subtract(priceBeforePercentDiscount.multiply(PROMOTIONAL_PERCENT_DISCOUNT).divide(ONE_HUNDRED));
					oilChangeQuote.setBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion(priceAfterPercentDiscount);
					BigDecimal amountSaved = priceBeforePercentDiscount.subtract(priceAfterPercentDiscount);
					
					oilChangeQuote.setAmountSavedWithPromotion(amountSaved);
					oilChangeQuote.setPercentSavedWithPromotion(PROMOTIONAL_PERCENT_DISCOUNT);
				}
				
			} else if( promoBusinessRules.getPriceModificationType().equalsIgnoreCase("AMOUNT_DISCOUNT")){
			
					BigDecimal PROMOTIONAL_AMOUNT_DISCOUNT =	promoBusinessRules.getAmountDiscount();
				
					if (PROMOTIONAL_AMOUNT_DISCOUNT != null) {
						oilChangeQuote.setBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(PROMOTIONAL_AMOUNT_DISCOUNT));
						BigDecimal amountSaved = oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion());
						BigDecimal amountWithPromotionDividedByOriginalAmount = oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion().divide(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal(), RoundingMode.HALF_DOWN);
						BigDecimal percentPaid = amountWithPromotionDividedByOriginalAmount.multiply(ONE_HUNDRED);
						BigDecimal percentSaved = ONE_HUNDRED.subtract(percentPaid);
						
						oilChangeQuote.setAmountSavedWithPromotion(amountSaved);
						oilChangeQuote.setPercentSavedWithPromotion(percentSaved);
					}
				
			} else if(promoBusinessRules.getPriceModificationType().equalsIgnoreCase("SPECIFIC_PRICE")){
				BigDecimal PROMOTIONAL_AMOUNT_SPECIFIC_PRICE =	promoBusinessRules.getSpecificPrice();
				if (PROMOTIONAL_AMOUNT_SPECIFIC_PRICE != null) {
					oilChangeQuote.setBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(PROMOTIONAL_AMOUNT_SPECIFIC_PRICE));
					BigDecimal amountSaved = oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion());
					BigDecimal amountWithPromotionDividedByOriginalAmount = oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion().divide(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal(), RoundingMode.HALF_DOWN);
					BigDecimal percentPaid = amountWithPromotionDividedByOriginalAmount.multiply(ONE_HUNDRED);
					BigDecimal percentSaved = ONE_HUNDRED.subtract(percentPaid);
					
					oilChangeQuote.setAmountSavedWithPromotion(amountSaved);
					oilChangeQuote.setPercentSavedWithPromotion(percentSaved);
				}
			}
		} else {
			oilChangeQuote.setBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion(priceBeforePercentDiscount);
		}
		if(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion() != null){
			oilChangeQuote.setBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion());
		}
		
		oilChangeQuote.setTotal(oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion());
		return oilChangeQuote;
	}
	
	public Long createOilChangeQuote(Long oilArticleNumber, Long storeNumber, BigDecimal quarts, String vehicleId, Integer vehicleYear, String vehicleMake, String vehicleModelSubmodelEngine, String customerZip, String webSite) {
		OilChangePackage oilChangePackage = getOilChangePackageByOilArticleNumberStoreNumberAndQuarts(oilArticleNumber, storeNumber, quarts);
		
		QuoteType oilQuoteType = quoteTypeDao.findQuoteTypeByFriendlyId(QUOTE_TYPE_OIL_CHANGE);
		
		Quote quote = new Quote();
		quote.setQuoteType(oilQuoteType);
		quote.setStoreNumber(storeNumber);
		quote.setVehicleId(vehicleId);
		quote.setVehicleYear(vehicleYear);
		quote.setVehicleMake(vehicleMake);
		quote.setVehicleModelSubmodelEngine(vehicleModelSubmodelEngine);
		quote.setZip(customerZip);
		quote.setWebSite(webSite);
		quote.setCreatedDate(Calendar.getInstance());
		
		int sequence = 1;
		
		QuoteItemType baseOilItemType = quoteItemTypeDao.findQuoteItemTypeByFriendlyId(QUOTE_ITEM_TYPE_BASE_OIL);
		
		QuoteItem baseOil = new QuoteItem();
		baseOil.setQuote(quote);
		baseOil.setQuoteItemType(baseOilItemType);
		baseOil.setArticleNumber(oilChangePackage.getBaseOil().getArticleNumber());
		baseOil.setName(oilChangePackage.getBaseOil().getArticleName());
		baseOil.setPricePerUnit(oilChangePackage.getBaseOilPrice().getPrice());
		baseOil.setQuantity(1);
		baseOil.setSequence(sequence);
		quote.getQuoteItems().add(baseOil);
		
		sequence++;
		
		if (oilChangePackage.getAdditionalOil() != null) {
			QuoteItemType additionalOilItemType = quoteItemTypeDao.findQuoteItemTypeByFriendlyId(QUOTE_ITEM_TYPE_ADDITIONAL_OIL);
			QuoteItem additionalOil = new QuoteItem();
			additionalOil.setQuote(quote);
			additionalOil.setQuoteItemType(additionalOilItemType);
			additionalOil.setArticleNumber(oilChangePackage.getAdditionalOil().getArticleNumber());
			additionalOil.setName(oilChangePackage.getAdditionalOil().getArticleName());
			additionalOil.setPricePerUnit(oilChangePackage.getAdditionalOilPrice().getPrice());
			additionalOil.setQuantity(oilChangePackage.getAdditionalOilQuantity());
			additionalOil.setSequence(sequence);
			quote.getQuoteItems().add(additionalOil);
			sequence++;
		}
		
		QuoteItemType oilChangeItemType = quoteItemTypeDao.findQuoteItemTypeByFriendlyId(QUOTE_ITEM_TYPE_OIL_CHANGE);
		QuoteItem oilChange = new QuoteItem();
		oilChange.setQuote(quote);
		oilChange.setQuoteItemType(oilChangeItemType);
		oilChange.setArticleNumber(oilChangePackage.getOilChange().getArticleNumber());
		oilChange.setName(oilChangePackage.getOilChange().getArticleName());
		oilChange.setPricePerUnit(oilChangePackage.getOilChangePrice().getPrice());
		oilChange.setQuantity(1);
		oilChange.setSequence(sequence);
		quote.getQuoteItems().add(oilChange);
		sequence++;
		
		
		QuoteItemType oilFilterItemType = quoteItemTypeDao.findQuoteItemTypeByFriendlyId(QUOTE_ITEM_TYPE_OIL_FILTER);
		QuoteItem oilFilter = new QuoteItem();
		oilFilter.setQuote(quote);
		oilFilter.setQuoteItemType(oilFilterItemType);
		oilFilter.setName(oilChangePackage.getOilFilter().getDescription());
		oilFilter.setPricePerUnit(oilChangePackage.getOilFilterPrice().getPrice());
		oilFilter.setQuantity(1);
		oilFilter.setSequence(sequence);
		quote.getQuoteItems().add(oilFilter);
		
		quoteDao.save(quote);
		
		return quote.getQuoteId();
	}
	
	private OilChangePackage getOilChangePackageByOilArticleNumberStoreNumberAndQuarts(Long oilArticleNumber, Long storeNumber, BigDecimal quarts) {		
		Oil oil = oilDao.get(oilArticleNumber);
		
		return getOilChangePackageForBaseOil(oil, storeNumber, quarts);
	}	
	
	private OilChangePackage getOilChangePackageForBaseOil(Oil oil, Long storeNumber, BigDecimal quarts) {
		
		OilChangePackage oilChangePackage = null;
		
		if (oil != null) {

			oilChangePackage = new OilChangePackage();
			
			oilChangePackage.setBaseOil(oil);
			
			
			
			oilChangePackage.setOilType(oilDao.getOilType(oilChangePackage.getBaseOil().getOilTypeId()));
			
			Oil additionalOil = null;
			
			// if the maximum quarts for this increment of oil is insufficient, get additional quarts
			if (oilChangePackage.getBaseOil().getMaximumQuarts().compareTo(quarts) < 0) {
				additionalOil = oilDao.findAdditionalOilByType(oilChangePackage.getBaseOil().getOilTypeId());
				
				BigDecimal additionalQuartsNeeded = quarts.subtract(oilChangePackage.getBaseOil().getMaximumQuarts());

				oilChangePackage.setAdditionalQuarts(additionalQuartsNeeded);
				
				BigDecimal additionalQuartsAccumulated = new BigDecimal(0.00);
				
				Integer additionalOilQuantity = 0;
				
				// TODO: Don't do this if additionalOil.maximumQuarts is equal to or less than 0
				while (additionalQuartsAccumulated.compareTo(additionalQuartsNeeded) < 0) {
					additionalQuartsAccumulated = additionalQuartsAccumulated.add(additionalOil.getMaximumQuarts());
					additionalOilQuantity++;
				}
				
				oilChangePackage.setAdditionalOil(additionalOil);
				oilChangePackage.setAdditionalOilQuantity(additionalOilQuantity);	
			}
			
			OilChange oilChange = oilDao.findOilChangeByOilType(oilChangePackage.getBaseOil().getOilTypeId());
			
			oilChangePackage.setOilChange(oilChange);
			
			OilFilter oilFilter = oilDao.getOilFilter();
			
			oilChangePackage.setOilFilter(oilFilter);
	
		
		if (oilChangePackage.getBaseOil() != null) {
			OilStorePriceId id = new OilStorePriceId(oilChangePackage.getBaseOil().getArticleNumber(), storeNumber);
			
			OilStorePrice oilStorePrice = oilStorePriceDao.get(id);
			oilChangePackage.setBaseOilPrice(oilStorePrice);
		}
		
		if (oilChangePackage.getAdditionalOil() != null) {
			OilStorePriceId id = new OilStorePriceId(oilChangePackage.getAdditionalOil().getArticleNumber(), storeNumber);
			OilStorePrice oilStorePrice = oilStorePriceDao.get(id);
			oilChangePackage.setAdditionalOilPrice(oilStorePrice);
		}
		if (oilChangePackage.getOilChange() != null) {
			
			OilChangeStorePriceId id = new OilChangeStorePriceId(oilChangePackage.getOilChange().getArticleNumber(), storeNumber);
			OilChangeStorePrice oilChangeStorePrice = oilChangeStorePriceDao.get(id);
			oilChangePackage.setOilChangePrice(oilChangeStorePrice);
		}
		
		if (oilChangePackage.getOilFilter() != null) {
			OilFilterPrice oilFilterPrice = oilFilterPriceDao.get(oilChangePackage.getOilFilter().getOilFilterId());
			oilChangePackage.setOilFilterPrice(oilFilterPrice);
		}
		
		// No null checks here, because none of these should ever be null. If they are null, that's a data error and we can't present pricing.
		BigDecimal baseOilOilChangeAndOilFilterSubtotal = new BigDecimal(0.00);
		baseOilOilChangeAndOilFilterSubtotal = baseOilOilChangeAndOilFilterSubtotal.add(oilChangePackage.getBaseOilPrice().getPrice());
		baseOilOilChangeAndOilFilterSubtotal = baseOilOilChangeAndOilFilterSubtotal.add(oilChangePackage.getOilChangePrice().getPrice());
		baseOilOilChangeAndOilFilterSubtotal = baseOilOilChangeAndOilFilterSubtotal.add(oilChangePackage.getOilFilterPrice().getPrice());
		oilChangePackage.setBaseOilOilChangeAndOilFilterSubtotal(baseOilOilChangeAndOilFilterSubtotal);
		if (oilChangePackage.getAdditionalOilPrice() != null && oilChangePackage.getAdditionalOilQuantity() != null) {
			oilChangePackage.setAdditionalOilSubtotal(oilChangePackage.getAdditionalOilPrice().getPrice().multiply(new BigDecimal(oilChangePackage.getAdditionalOilQuantity())));
		} else {
			oilChangePackage.setAdditionalOilSubtotal(new BigDecimal(0.00));
		}
		oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilSubtotal(oilChangePackage.getBaseOilOilChangeAndOilFilterSubtotal().add(oilChangePackage.getAdditionalOilSubtotal()));
		}
		
		
		return oilChangePackage;
	}
	
	public void saveOatsVehicleYearCache(OatsVehicleYearCache oatsVehicleYearCache) {
		 oilDao.saveOatsVehicleYearCache(oatsVehicleYearCache);		
	}
	
	public void saveOatsVehicleYearToMake(OatsVehicleYearToMake oatsVehicleYearToMake) {
		oilDao.saveOatsVehicleYearToMake(oatsVehicleYearToMake);
	}

	public void saveOatsVehicleMakeCache(OatsVehicleMakeCache oatsVehicleMakeCache) {
		oilDao.saveOatsVehicleMakeCache(oatsVehicleMakeCache);
	}

	public void saveOatsVehicleCache(OatsVehicleCache oatsVehicleCache) {
		oilDao.saveOatsVehicleCache(oatsVehicleCache);
	}
	
	public void saveOatsOilRecommendationCache(OatsOilRecommendationCache oatsOilRecommendationCache) {
		oilDao.saveOatsOilRecommendationCache(oatsOilRecommendationCache);
	}
	
	public SeoVehicleData getSEOVehicleDataBean(String seoContent, String siteName)  {		
		
		SeoVehicleData seoVehicleData  = null;
		   if(seoContent.equalsIgnoreCase("oil"))
		   {
			    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "OIL_VEHICLE", "/maintain/oil/vehicle/");
		   }
		   else if(seoContent.equalsIgnoreCase("battery"))
		   {
			    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "BATTERY_VEHICLE", "/maintain/battery/vehicle/");
		   }
			
			return seoVehicleData;
		}
	
public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String seoContent, String siteName)  {		
			
	SeoVehicleData seoVehicleData  = null;
	   if(seoContent.equalsIgnoreCase("oil"))
	   {
		    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "OIL_VEHICLE_MAKE", "/maintain/oil/vehicle/"+friendlyMake+"/");
	   }
	   else if(seoContent.equalsIgnoreCase("battery"))
	   {
		    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "BATTERY_VEHICLE_MAKE", "/maintain/batteries/vehicle/"+friendlyMake+"/");
	   }
		
			if (seoVehicleData == null) {
				FriendlyVehicleDataBean friendlyVehicleDataBean = generateFriendlyVehicleDataBean(friendlyMake, null, null, null);
				 if(seoContent.equalsIgnoreCase("oil"))
				   {
					 seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "OIL_VEHICLE_MAKE", "/maintain/oil/vehicle/generic-make/");
				   }
				   else if(seoContent.equalsIgnoreCase("battery"))
				   {
					 seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "BATTERY_VEHICLE_MAKE", "/maintain/batteries/vehicle/generic-make/");
				   }
				
				seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
			}
		
		return seoVehicleData;
	}
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String seoContent, String siteName)  {		
		
		SeoVehicleData seoVehicleData  = null;
		   if(seoContent.equalsIgnoreCase("oil"))
		   {
			    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "OIL_VEHICLE_MAKE_MODEL", "/maintain/oil/vehicle/"+friendlyMake+"/"+friendlyModel+"/");
		   }
		   else if(seoContent.equalsIgnoreCase("battery"))
		   {
			    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "BATTERY_VEHICLE_MAKE_MODEL", "/maintain/batteries/vehicle/"+friendlyMake+"/"+friendlyModel+"/");
		   }
			
				if (seoVehicleData == null) {
					FriendlyVehicleDataBean friendlyVehicleDataBean = generateFriendlyVehicleDataBean(friendlyMake, friendlyModel, null, null);
					 if(seoContent.equalsIgnoreCase("oil"))
					   {
						 seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "OIL_VEHICLE_MAKE_MODEL", "/maintain/oil/vehicle/generic-make/generic-model/");
					   }
					   else if(seoContent.equalsIgnoreCase("battery"))
					   {
						 seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "BATTERY_VEHICLE_MAKE_MODEL", "/maintain/batteries/vehicle/generic-make/generic-model/");
					   }
					
					seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
				}
			
			return seoVehicleData;
	}
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String year, String seoContent, String siteName)  {		
		
		SeoVehicleData seoVehicleData  = null;
		   if(seoContent.equalsIgnoreCase("oil"))
		   {
			    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "OIL_VEHICLE_MAKE_MODEL_YEAR", "/maintain/oil/vehicle/"+friendlyMake+"/"+friendlyModel+"/"+year+"/");
		   }
		   else if(seoContent.equalsIgnoreCase("battery"))
		   {
			    seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "BATTERY_VEHICLE_MAKE_MODEL_YEAR", "/maintain/batteries/vehicle/"+friendlyMake+"/"+friendlyModel+"/"+year+"/");
		   }
			
				if (seoVehicleData == null) {
					FriendlyVehicleDataBean friendlyVehicleDataBean = generateFriendlyVehicleDataBean(friendlyMake, friendlyModel, year, null);
					 if(seoContent.equalsIgnoreCase("oil"))
					   {
						 seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "OIL_VEHICLE_MAKE_MODEL_YEAR", "/maintain/oil/vehicle/generic-make/generic-model/generic-year/");
					   }
					   else if(seoContent.equalsIgnoreCase("battery"))
					   {
						 seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "BATTERY_VEHICLE_MAKE_MODEL_YEAR", "/maintain/batteries/vehicle/generic-make/generic-model/generic-year/");
					   }
					
					seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
				}
			
			return seoVehicleData;
	}
	
		private FriendlyVehicleDataBean generateFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel){
				FriendlyVehicleDataBean friendlyVehicleDataBean = new FriendlyVehicleDataBean();
				friendlyVehicleDataBean.setMakeName(friendlyMake);
				friendlyVehicleDataBean.setMakeFriendlyName(friendlyMake);
				friendlyVehicleDataBean.setModelName(friendlyModel);
				friendlyVehicleDataBean.setModelFriendlyName(friendlyModel);
				friendlyVehicleDataBean.setSubmodelName(friendlySubmodel);
				friendlyVehicleDataBean.setSubmodelFriendlyName(friendlySubmodel);
				friendlyVehicleDataBean.setYear(year);
				return friendlyVehicleDataBean;
			}
			
			private SeoVehicleData formatSeoVehicleData(SeoVehicleData seoVehicleData, FriendlyVehicleDataBean friendlyVehicleDataBean) {
				if (seoVehicleData != null && friendlyVehicleDataBean != null) {
					if (!StringUtils.isNullOrEmpty(friendlyVehicleDataBean.getYear())) {
						formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 1);
						formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 2);
						formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 3);
					} else if (!StringUtils.isNullOrEmpty(friendlyVehicleDataBean.getModelName())) {
						formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 1);
						formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 2);
					} else if (!StringUtils.isNullOrEmpty(friendlyVehicleDataBean.getMakeName())) {
						formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 1);
					}
					return seoVehicleData;
				}
				return null;
			}
			
			private SeoVehicleData formatSeoVehicleData(SeoVehicleData seoVehicleData, FriendlyVehicleDataBean friendlyVehicleDataBean, int fcode) {
				switch (fcode) {
					case 1:
						seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-make", friendlyVehicleDataBean.getMakeFriendlyName()));
						seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Make}", friendlyVehicleDataBean.getMakeName()));
						break;
					case 2:
						seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-model", friendlyVehicleDataBean.getModelFriendlyName()));
						seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Model}", friendlyVehicleDataBean.getModelName()));
						seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Model}", friendlyVehicleDataBean.getModelName()));
						break;
					case 3:
						seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-year", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Year}", friendlyVehicleDataBean.getYear()));
						seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Year}", friendlyVehicleDataBean.getYear()));
						break;
					default:
						break;
				}
				
				return seoVehicleData;
			}
	
}
