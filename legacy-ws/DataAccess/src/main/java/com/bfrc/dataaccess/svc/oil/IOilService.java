package com.bfrc.dataaccess.svc.oil;

import java.math.BigDecimal;

import app.bsro.model.oil.OilChangePackage;
import app.bsro.model.oil.OilChangeQuote;

import com.bfrc.dataaccess.model.oil.OatsOilRecommendationCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleMakeCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearToMake;
import com.bfrc.dataaccess.model.oil.Oil;
import com.bfrc.dataaccess.model.quote.Quote;
import com.bfrc.pojo.lookup.SeoVehicleData;

public interface IOilService {
	public static final String QUOTE_TYPE_OIL_CHANGE = "OIL_CHANGE";
	
	public static final String QUOTE_ITEM_TYPE_BASE_OIL = "BASE_OIL";
	
	public static final String QUOTE_ITEM_TYPE_ADDITIONAL_OIL = "ADDITIONAL_OIL";
	
	public static final String QUOTE_ITEM_TYPE_OIL_CHANGE = "OIL_CHANGE";
	
	public static final String QUOTE_ITEM_TYPE_OIL_FILTER = "OIL_FILTER";
	
	public static final String QUOTE_ITEM_TYPE_DONATION = "DONATION";
	
	public Oil getOilByArticleNumber(Long articleNumber);
	
	public OilChangePackage getOilChangeSearchResultByStoreNumberOatsNameAndQuarts(Long storeNumber, String oatsName, BigDecimal quarts);
	
	public OilChangePackage getHighMileageOilChangePackageByStoreNumberAndViscosityAndQuarts(Long storeNumber, String viscosity, BigDecimal quarts);
	
	public OilChangeQuote getOilChangeQuote(Long oilChangeQuoteId, String siteName);
	
	public Long createOilChangeQuote(Long oilArticleNumber, Long storeNumber, BigDecimal quarts, String vehicleId, Integer vehicleYear, String vehicleMake, String vehicleModelSubmodelEngine, String customerZip, String webSite);

	public void updateOilChangeQuoteFirstName(Long oilChangeQuoteId,String siteName,String firstName);

	public void updateOilChangeQuoteLastName(Long oilChangeQuoteId, String siteName, String lastName);

	
	
	public void saveOatsVehicleYearCache(OatsVehicleYearCache oatsVehicleYearCache);
	
	public void saveOatsVehicleYearToMake(OatsVehicleYearToMake oatsVehicleYearToMake);
	
	public void saveOatsVehicleMakeCache(OatsVehicleMakeCache oatsVehicleMakeCache);
	
	public void saveOatsVehicleCache(OatsVehicleCache oatsVehicleCache);
	
	public void saveOatsOilRecommendationCache(OatsOilRecommendationCache oatsOilRecommendationCache);
	
	public SeoVehicleData getSEOVehicleDataBean(String seoContent, String siteName);
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String seoContent, String siteName);
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String seoContent, String siteName);
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String year, String seoContent, String siteName);
	
}
