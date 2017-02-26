package com.bfrc.framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bfrc.Config;
import com.bfrc.UserSessionData;
import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.IpAccessDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.pricing.PricingLocatorOperator;
import com.bfrc.pojo.admin.AdminIpAddress;
import com.bfrc.pojo.ext.CatalogGetQuoteDataBean;
import com.bfrc.pojo.survey.MindshareTiresurveyDetails;
import com.bfrc.pojo.tire.Fitment;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tire.TireSearchResults;
import com.bfrc.pojo.tire.jda2.TireGrouping;
import com.bfrc.security.Encode;
import com.bsro.service.inventory.StoreInventoryService;


public class TireSearchUtils {
	public final static java.lang.String VEHICLE_TABLE = "FITMENT";
	public final static java.lang.String PRODUCT_TABLE = "CONFIGURATION";
	public final static java.lang.String TABLESPACE = "BFS_TIRE_SELECTOR";
    public final static String ALL_SEASON_CATEGORY = "Passenger Tires";
    public final static String PERFORMANCE_CATEGORY = "Performance";
    public final static String TRUCK_CATEGORY = "Light Truck Tires";
    public final static String WINTER_CATEGORY = "Winter";
    public final static String PASS_SNOW_SEGMENT = "Snow Tires-PS";
    public final static String TRUCK_SNOW_SEGMENT = "Snow Tires-LT";
    
    public final static String[] SPEED = {"Q","R","S","T","U","H","V","Z","W","Y","(Y)"};
	public final static java.lang.String[] SPEED_VALUE = {"99","106","112","118","124","130","149","149+","168","186","186+"};
	
	public final static String WINTER_CONDITION=" (DISPLAY.TIREGROUP_ID=4) ";
    
	public final static int LIST_PERFORMANCE = 3;
	public final static int LIST_ALLSEASON = 1;
	public final static int LIST_WINTER = 4;
	public final static int LIST_TRUCK = 2;
	public final static int LIST_DRIVEGUARD = 5;
	
	public final static String NEW_LIST_FILTERED_CARMINIVAN= "CarMinivan";
	public final static String NEW_LIST_FILTERED_PERFORMANCE= "Performance";
	public final static String NEW_LIST_FILTERED_SUVCUV= "SUVCUV";
	public final static String NEW_LIST_FILTERED_LIGHTTRUCK= "LightTruck";
	public final static String NEW_LIST_FILTERED_WINTER= "Winter";
	
	public final static String NEW_LIST_CARMINIVAN= "Car & Minivan";
	public final static String NEW_LIST_PERFORMANCE= "Performance";
	public final static String NEW_LIST_SUVCUV= "SUV/CUV";
	public final static String NEW_LIST_LIGHTTRUCK= "Light Truck";
	public final static String NEW_LIST_WINTER= "Winter";
	
	
	public final static String ALL_SEASON_FRIENDLYID = "passenger";
    public final static String PERFORMANCE_FRIENDLYID = "performance";
    public final static String TRUCK_FRIENDLYID = "truck";
    public final static String WINTER_FRIENDLYID = "winter";
    
    public final static String SEARCH_BY_VEHICLE = "vehicle";
    public final static String SEARCH_BY_SIZE = "size";
    public final static String SEARCH_BY_ADVANCED = "advanced";
    
    public final static String INVENTORY_MAP = "InventoryMap";
    public final static String ACCENT_USER_IP = "Accent_User_IP";
	
	private static StoreDAO storeDAO;
	private static HttpServletRequest request;
	private static Config config;
	private static PricingLocatorOperator pricingLocator;
	private static VehicleDAO vehicleDAO;
	private static StoreInventoryService storeInventoryService;
	private static IpAccessDAO ipAccessDAO;

	public static HttpServletRequest getHttpServletRequest(){
    	return request;
    }
    public static void  setHttpServletRequest(HttpServletRequest httpServletRequest){
    	request = httpServletRequest;
    }
	private static void locateBeans(HttpSession httpSession){
		ServletContext ctx = httpSession.getServletContext();
		if(storeDAO == null)
		    storeDAO = (StoreDAO)Config.locate(ctx, "storeDAO");
		if(config == null)
			config = (Config)Config.locate(ctx, "config");
		if(vehicleDAO == null)
			vehicleDAO = (VehicleDAO)Config.locate(ctx, "vehicleDAO");
		if(pricingLocator == null)
			pricingLocator = (PricingLocatorOperator)Config.locate(ctx, "pricingLocator");
		if(storeInventoryService == null)
			storeInventoryService = (StoreInventoryService)Config.locate(ctx, "storeInventoryService");
		if(ipAccessDAO == null)
			ipAccessDAO = (IpAccessDAO)Config.locate(ctx, "ipAccessDAO");
	}
	private static void locateBeans(HttpServletRequest request){
		locateBeans(request.getSession());
	}
	
	static String getTireSelectorFields;

	public static String getTireSelectorFields() {
		if (getTireSelectorFields == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DISTINCT DISPLAY.ID AS displayId, \n");
			sb.append("     DISPLAY.GENERATE_CATALOG_PAGE as generateCatalogPage , \n");
			sb.append("     CONFIGURATION.LOAD_RANGE as loadRange, \n");
			sb.append("     CONFIGURATION.LOAD_INDEX as loadIndex, \n");
			sb.append("     DISPLAY.VALUE AS tireName, \n");
			sb.append("     BRAND.VALUE AS brand, \n");
			sb.append("     TIREGROUP_ID AS tireGroupId, \n");
			sb.append("     TIREGROUP_ID AS tireGroupId, \n");
			sb.append("     TIREGROUP.VALUE AS tireGroupName, \n");
			sb.append("     CLASS_ID AS tireClassId, \n");
			sb.append("     CLASS.VALUE AS tireClassName, \n");
			sb.append("     SEGMENT_ID AS tireSegmentId, \n");
			sb.append("     SEGMENT.VALUE AS tireSegmentName, \n");
			sb.append("     LABEL AS tireSize, \n");
			sb.append("     STANDARD_IND AS standardOptional, \n");
			sb.append("     FRB AS frontRearBoth, \n");
			sb.append("     VEHTYPE as vehtype, \n");
			sb.append("     SPEED.VALUE AS speedRating, \n");
			sb.append("     SIDEWALL.DESCRIPTION AS sidewallDescription, \n");
			sb.append("     MILEAGE.VALUE AS mileage, \n");
			sb.append("     TECHNOLOGY.VALUE AS technology, \n");
			sb.append("     WARRANTY.ID AS warrantyId, \n");
			sb.append("     WARRANTY.NAME AS warrantyName, \n");
			sb.append("     OEM_FLAG AS oemFlag, \n");
			sb.append("     CONFIGURATION.CROSS_SECTION AS crossSection, \n");
			sb.append("     CONFIGURATION.ASPECT AS aspect, \n");
			sb.append("     CONFIGURATION.RIM_SIZE AS rimSize, \n");
			sb.append("     DD as discontinued, \n");
			sb.append("     CONFIGURATION.SKU AS article  \n");
			sb.append("FROM  \n");
			sb.append("  BFS_TIRE_CATALOG_JDA.CONFIGURATION  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.DISPLAY ON (CONFIGURATION.DISPLAY_ID=DISPLAY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.CLASS ON (DISPLAY.CLASS_ID=CLASS.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SEGMENT ON (DISPLAY.SEGMENT_ID=SEGMENT.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIREGROUP ON (DISPLAY.TIREGROUP_ID=TIREGROUP.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIRE_WEBSOURCE ON (CONFIGURATION.SKU=TIRE_WEBSOURCE.SKU)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SPEED ON (CONFIGURATION.SPEED_ID=SPEED.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SIDEWALL ON (CONFIGURATION.SIDEWALL_ID=SIDEWALL.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.BRAND ON (DISPLAY.BRAND_ID=BRAND.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TECHNOLOGY ON (CONFIGURATION.TECHNOLOGY_ID=TECHNOLOGY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT_JOIN ON (DISPLAY.ID=FACT_JOIN.DISPLAY_ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT ON (FACT_JOIN.FACT_ID=FACT.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.WARRANTY ON (DISPLAY.WARRANTY_ID=WARRANTY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.MILEAGE ON (CONFIGURATION.MILEAGE_ID=MILEAGE.ID), \n");
			sb.append("BFS_TIRE_SELECTOR.FITMENT  \n");
			getTireSelectorFields = sb.toString();
		}
		return getTireSelectorFields;
	}
	
	static String getTireSelectorGroupFields;

	public static String getTireSelectorGroupFields() {
		if (getTireSelectorGroupFields == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DISTINCT TIREGROUP_ID as tireGroupId, TIREGROUP.VALUE as tireGroupName \n");
			sb.append("FROM  \n");
			sb.append("  BFS_TIRE_CATALOG_JDA.CONFIGURATION  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.DISPLAY ON (CONFIGURATION.DISPLAY_ID=DISPLAY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.CLASS ON (DISPLAY.CLASS_ID=CLASS.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SEGMENT ON (DISPLAY.SEGMENT_ID=SEGMENT.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIREGROUP ON (DISPLAY.TIREGROUP_ID=TIREGROUP.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIRE_WEBSOURCE ON (CONFIGURATION.SKU=TIRE_WEBSOURCE.SKU)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SPEED ON (CONFIGURATION.SPEED_ID=SPEED.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SIDEWALL ON (CONFIGURATION.SIDEWALL_ID=SIDEWALL.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.BRAND ON (DISPLAY.BRAND_ID=BRAND.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TECHNOLOGY ON (CONFIGURATION.TECHNOLOGY_ID=TECHNOLOGY.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT_JOIN ON (DISPLAY.ID=FACT_JOIN.DISPLAY_ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT ON (FACT_JOIN.FACT_ID=FACT.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.WARRANTY ON (DISPLAY.WARRANTY_ID=WARRANTY.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.MILEAGE ON (CONFIGURATION.MILEAGE_ID=MILEAGE.ID) \n");
			sb.append(", BFS_TIRE_SELECTOR.FITMENT  \n");
			getTireSelectorGroupFields = sb.toString();
		}
		return getTireSelectorGroupFields;
	}
	
	static String getTireSelectorClassFields;

	public static String getTireSelectorClassFields() {
		if (getTireSelectorClassFields == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DISTINCT CLASS_ID as tireClassId, CLASS.VALUE as tireClassName \n");
			sb.append("FROM  \n");
			sb.append("  BFS_TIRE_CATALOG_JDA.CONFIGURATION  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.DISPLAY ON (CONFIGURATION.DISPLAY_ID=DISPLAY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.CLASS ON (DISPLAY.CLASS_ID=CLASS.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SEGMENT ON (DISPLAY.SEGMENT_ID=SEGMENT.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIREGROUP ON (DISPLAY.TIREGROUP_ID=TIREGROUP.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIRE_WEBSOURCE ON (CONFIGURATION.SKU=TIRE_WEBSOURCE.SKU)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SPEED ON (CONFIGURATION.SPEED_ID=SPEED.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SIDEWALL ON (CONFIGURATION.SIDEWALL_ID=SIDEWALL.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.BRAND ON (DISPLAY.BRAND_ID=BRAND.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TECHNOLOGY ON (CONFIGURATION.TECHNOLOGY_ID=TECHNOLOGY.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT_JOIN ON (DISPLAY.ID=FACT_JOIN.DISPLAY_ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT ON (FACT_JOIN.FACT_ID=FACT.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.WARRANTY ON (DISPLAY.WARRANTY_ID=WARRANTY.ID)  \n");
			//sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.MILEAGE ON (CONFIGURATION.MILEAGE_ID=MILEAGE.ID) \n");
			sb.append(",BFS_TIRE_SELECTOR.FITMENT  \n");
			getTireSelectorClassFields = sb.toString();
		}
		return getTireSelectorClassFields;
	}

	static String getSizeTireSelectorFields;

	public static String getSizeTireSelectorFields() {
		if (getSizeTireSelectorFields == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DISTINCT DISPLAY.ID AS displayId, \n");
			sb.append("     DISPLAY.GENERATE_CATALOG_PAGE as generateCatalogPage , \n");
			sb.append("     nvl(CONFIGURATION.LOAD_RANGE,'') as loadRange, \n");
			sb.append("     CONFIGURATION.LOAD_INDEX as loadIndex, \n");
			sb.append("     DISPLAY.VALUE AS tireName, \n");
			sb.append("     BRAND.VALUE AS brand, \n");
			sb.append("     TIREGROUP_ID AS tireGroupId, \n");
			sb.append("     TIREGROUP.VALUE AS tireGroupName, \n");
			sb.append("     CLASS_ID AS tireClassId, \n");
			sb.append("     CLASS.VALUE AS tireClassName, \n");
			sb.append("     SEGMENT_ID AS tireSegmentId, \n");
			sb.append("     SEGMENT.VALUE AS tireSegmentName, \n");
			sb.append("     LABEL AS tireSize, \n");
			sb.append("     nvl(SPEED.VALUE,'') AS speedRating, \n");
			sb.append("     SIDEWALL.DESCRIPTION AS sidewallDescription, \n");
			sb.append("     MILEAGE.VALUE AS mileage, \n");
			sb.append("     TECHNOLOGY.VALUE AS technology, \n");
			sb.append("     WARRANTY.ID AS warrantyId, \n");
			sb.append("     WARRANTY.NAME AS warrantyName, \n");
			sb.append("     OEM_FLAG AS oemFlag, \n");
			sb.append("     CONFIGURATION.CROSS_SECTION AS crossSection, \n");
			sb.append("     CONFIGURATION.ASPECT AS aspect, \n");
			sb.append("     CONFIGURATION.RIM_SIZE AS rimSize, \n");
			sb.append("     DD as discontinued, \n");
			sb.append("     CONFIGURATION.SKU AS article  \n");
			sb.append("FROM  \n");
			sb.append("  BFS_TIRE_CATALOG_JDA.CONFIGURATION  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.DISPLAY ON (CONFIGURATION.DISPLAY_ID=DISPLAY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.CLASS ON (DISPLAY.CLASS_ID=CLASS.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SEGMENT ON (DISPLAY.SEGMENT_ID=SEGMENT.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIREGROUP ON (DISPLAY.TIREGROUP_ID=TIREGROUP.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIRE_WEBSOURCE ON (CONFIGURATION.SKU=TIRE_WEBSOURCE.SKU)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SPEED ON (CONFIGURATION.SPEED_ID=SPEED.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SIDEWALL ON (CONFIGURATION.SIDEWALL_ID=SIDEWALL.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.BRAND ON (DISPLAY.BRAND_ID=BRAND.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TECHNOLOGY ON (CONFIGURATION.TECHNOLOGY_ID=TECHNOLOGY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT_JOIN ON (DISPLAY.ID=FACT_JOIN.DISPLAY_ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.FACT ON (FACT_JOIN.FACT_ID=FACT.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.WARRANTY ON (DISPLAY.WARRANTY_ID=WARRANTY.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.MILEAGE ON (CONFIGURATION.MILEAGE_ID=MILEAGE.ID) \n");
			// sb.append("BFS_TIRE_SELECTOR.FITMENT  \n");
			getSizeTireSelectorFields = sb.toString();
		}
		return getSizeTireSelectorFields;
	}

	// --- for cached articles and user parameters --//
	static String getFitmentSelectorFields;

	public static String getFitmentSelectorFields() {
		if (getFitmentSelectorFields == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DISTINCT nvl(SPEED_RATING,'') as speedRating, \n");
			sb.append("    CROSS_SECTION as crossSection, \n");
			sb.append("    ASPECT as aspect,\n");
			sb.append("    RIM_SIZE as rimSize,\n");
			sb.append("    STANDARD_IND as standardInd,\n");
			sb.append("    FRB as frb, \n");
			sb.append("    VEHTYPE as vehtype, \n");
			sb.append("    NOTES as notes, \n");
			sb.append("    TIRE_SIZE as tireSize, \n");
			sb.append("    nvl(LOAD_INDEX,0) as loadIndex, \n");
			sb.append("    nvl(LOAD_RANGE,'') as loadRange, \n");
			sb.append("    nvl(FRONT_INF,'0') as frontInf, \n");
			sb.append("    nvl(REAR_INF,'0') as rearInf, \n");
			sb.append("    ACES_VEHICLE_ID as acesVehicleId, \n");
			sb.append("    MAKE_ID as makeId, \n");
			sb.append("    MODEL_ID as modelId, \n");
			sb.append("    SUBMODEL_ID as submodelId, \n");
			sb.append("    MODEL_YEAR as modelYear, \n");
			sb.append("    MODEL_NAME as modelName, \n");
			sb.append("    SUBMODEL as submodel, \n");
			sb.append("    MAKE_NAME as makeName, \n");
			sb.append("    nvl(TPMS_IND, 0) as tpmsInd \n");
			sb.append("  FROM BFS_TIRE_SELECTOR.FITMENT \n");
			getFitmentSelectorFields = sb.toString();
		}
		return getFitmentSelectorFields;
	}

	// --- for cached articles and user parameters --//
	static String getDescriptionTireSelectorFields;

	public static String getDescriptionTireSelectorFields() {
		if (getDescriptionTireSelectorFields == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DISTINCT DISPLAY.ID AS displayId, \n");
			sb.append("     DISPLAY.GENERATE_CATALOG_PAGE as generateCatalogPage , \n");
			sb.append("     nvl(CONFIGURATION.LOAD_RANGE,'') as loadRange, \n");
			sb.append("     CONFIGURATION.LOAD_INDEX as loadIndex, \n");
			sb.append("     DISPLAY.VALUE AS tireName, \n");
			sb.append("     BRAND.VALUE AS brand, \n");
			sb.append("     TIREGROUP_ID AS tireGroupId, \n");
			sb.append("     TIREGROUP.VALUE AS tireGroupName, \n");
			sb.append("     CLASS_ID AS tireClassId, \n");
			sb.append("     CLASS.VALUE AS tireClassName, \n");
			sb.append("     LABEL AS tireSize, \n");
			sb.append("     nvl(SPEED.VALUE,'') AS speedRating, \n");
			sb.append("     SIDEWALL.DESCRIPTION AS sidewallDescription, \n");
			sb.append("     MILEAGE.VALUE AS mileage, \n");
			sb.append("     TECHNOLOGY.VALUE AS technology, \n");
			sb.append("     WARRANTY.ID AS warrantyId, \n");
			sb.append("     WARRANTY.NAME AS warrantyName, \n");
			sb.append("     OEM_FLAG AS oemFlag, \n");
			sb.append("     CONFIGURATION.CROSS_SECTION AS crossSection, \n");
			sb.append("     CONFIGURATION.ASPECT AS aspect, \n");
			sb.append("     CONFIGURATION.RIM_SIZE AS rimSize, \n");
			sb.append("     DD as discontinued, \n");
			sb.append("     CONFIGURATION.SKU AS article  \n");
			sb.append("FROM  \n");
			sb.append("  BFS_TIRE_CATALOG_JDA.CONFIGURATION \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.DISPLAY ON (CONFIGURATION.DISPLAY_ID=DISPLAY.ID) \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.CLASS ON (DISPLAY.CLASS_ID=CLASS.ID)  \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TIREGROUP ON (DISPLAY.TIREGROUP_ID=TIREGROUP.ID) \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SPEED ON (CONFIGURATION.SPEED_ID=SPEED.ID) \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.SIDEWALL ON (CONFIGURATION.SIDEWALL_ID=SIDEWALL.ID) \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.BRAND ON (DISPLAY.BRAND_ID=BRAND.ID) \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.TECHNOLOGY ON (CONFIGURATION.TECHNOLOGY_ID=TECHNOLOGY.ID) \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.WARRANTY ON (DISPLAY.WARRANTY_ID=WARRANTY.ID) \n");
			sb.append("  LEFT JOIN BFS_TIRE_CATALOG_JDA.MILEAGE ON (CONFIGURATION.MILEAGE_ID=MILEAGE.ID) \n");
			getDescriptionTireSelectorFields = sb.toString();
		}
		return getDescriptionTireSelectorFields;
	}

	static String getTireSelectorPriceQuery;

	public static String getTireSelectorPriceQuery() {
		if (getTireSelectorPriceQuery == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT \n")
					.append("   tm_Article as article, \n")
					.append("   tm_Description as description, \n")
					.append("   tm_Line as line, \n")
					.append("   tp_Retail_Price as retailPrice, \n")
					.append("   tp_OnSale as onSale, \n")
					.append("   tp_End_Date as endDate, \n")
					.append("   tm_Excise_Tax as exciseTax, \n")
					.append("   sl_Excise_Tax_Article as exciseTaxArticle, \n")
					.append("   CASE WHEN nvl(c.Percent,0) = 0 THEN nvl(c.Amount,0) ELSE (CASE WHEN nvl(c.Amount,0) <> 0 THEN (CASE WHEN nvl(c.Percent,0) * tp_Retail_Price / 100 < nvl(c.Amount,0) THEN nvl(c.Percent,0) * tp_Retail_Price / 100 ELSE nvl(c.Amount,0) END) ELSE nvl(c.Percent,0) * tp_Retail_Price / 100 END) END as tireFee, \n")
					.append("   nvl(c.Description,'') as feeDesc, \n")
					.append("   nvl(c.Article,0) as feeArticle, \n")
					.append("   hp_Wheel_Balance_Weight as wheelBalanceWeight, \n")
					.append("   '7018708' as wheelWgtArticle, \n")
					.append("   hp_Wheel_Balance_Labor as wheelBalanceLabor, \n")
					.append("   '7018716' as wheelBalArticle, \n")
					.append("  hp_Valve_Stem as valveStem, \n")
					.append("   '7015040' as valveStemArticle, \n")
					.append("   sl_Disposal_Price as disposalPrice, \n")
					.append("   '7075078' as disposalFeeArticle, \n")
					.append("   0 as tireInstallPrice, \n")
					.append("   '7015016' as tireInstallArticle  \n")
					.append(" FROM  \n")
					.append("   (SELECT e.Article as tm_article, \n")
					.append("		   e.Description as tm_description, \n")
					.append("		   e.Line as tm_line, \n")
					.append("		   nvl(a.Retail_Price,0) as tp_retail_price, \n")
					.append("		   a.OnSale as tp_onsale, \n")
					.append("		   a.End_Date as tp_end_date, \n")
					.append("		   nvl(e.Excise_Tax,0) as tm_excise_tax, \n")
					.append("		   b.Excise_Tax_Article as sl_excise_tax_article, \n")
					.append("		   nvl(d.Wheel_Balance_Weight,0) as hp_wheel_balance_weight, \n")
					.append("		   nvl(d.Wheel_Balance_Labor,0) as hp_wheel_balance_labor, \n")
					.append("		   nvl(d.Valve_Stem,0) as hp_valve_stem, \n")
					.append("		   nvl(b.Disposal_Price, 0) as sl_disposal_price, \n")
					.append("		   b.store_number as sl_store_number, \n")
					.append("		   e.fee_type_code as tm_fee_type_code  \n")
					.append("	FROM TP_Tire_Price a, \n")
					.append("	     TP_Hierarchy_Price d, \n")
					.append("	     TP_Tire_Master e,  \n")
					.append("	     TP_Store_List b  \n")
					.append("	WHERE a.Price_Book_ID = b.Price_Book_ID  \n")
					.append("	     AND a.Price_Book_ID = d.Price_Book_ID  \n")
					.append("	     AND e.article = a.article  \n")
					.append("	     AND d.Line=e.Line  \n")
					.append("	     AND a.Article in ( %%ARTICLES_LIST%%  )  \n")
					.append("         AND b.Store_Number=%%STORE_NUMBER%%)  \n")
					.append("   derived_price  \n")
					.append("left outer join  \n")
					.append("    TP_Tire_Fee c  \n")
					.append("on derived_price.sl_store_number = c.store_number  \n")
					.append("    and derived_price.tm_fee_type_code = c.fee_type_code  \n")
					.append("    and (nvl(c.amount,0) > 0 or nvl(c.percent,0) > 0) \n");
			getTireSelectorPriceQuery = sb.toString();
		}
		return getTireSelectorPriceQuery;
	}

	static String sizeQuery;

	public static String buildSizeQuery(Object cross, Object aspect, Object rim) {
		if (sizeQuery == null) {
			StringBuffer sb = new StringBuffer();
			sb.append(" AND (CROSS_SECTION='%%CROSS_SECTION%%') \n");
			sb.append(" AND (ASPECT='%%ASPECT%%') \n");
			sb.append(" AND (RIM_SIZE='%%RIM_SIZE%%') \n");
			sizeQuery = sb.toString();
		}
		if (StringUtils.isNullOrEmpty(cross)
				|| StringUtils.isNullOrEmpty(aspect)
				|| StringUtils.isNullOrEmpty(rim))
			return "";
		return sizeQuery
				.replace("%%CROSS_SECTION%%",
						Encode.escapeDb(cross.toString()))
				.replace("%%ASPECT%%", Encode.escapeDb(aspect.toString()))
				.replace("%%RIM_SIZE%%", Encode.escapeDb(rim.toString()));
	}
	/*
	public static String buildBasicQuery(UserSessionData userSessionData,TireSearchResults results){
		//locateBeans(userSessionData.getHttpServletRequest());
		//locateBeans(userSessionData.getHttpSession());
		locateBeans(getHttpServletRequest());
		StringBuffer sb = new StringBuffer();
		//Vehicle vehicle = userSessionData.getVehicle();
		List fitments = userSessionData.getFitments();
		Fitment fitment  = null;
		String vehType = null;
		if(fitments != null && fitments.size() > 0){
			fitment  = (Fitment)fitments.get(0);
			vehType = fitment.getVehtype();
		}
		//--**TIRE SELECTOR RULES(1) -1**--//
        if(!"advanced".equalsIgnoreCase(userSessionData.getUserRequest()) 
        		&& !ServerUtil.isNullOrEmpty(vehType)
        		) {
           
            if("PASS".equalsIgnoreCase(vehType)) {
                sb.append(" AND (TIREGROUP.VALUE<>\'" + TireSearchUtils.TRUCK_CATEGORY + "\') ");
                sb.append(" AND (SEGMENT.VALUE<>\'" + TireSearchUtils.TRUCK_SNOW_SEGMENT + "\') ");
            }
        } else {
        	if("advanced".equalsIgnoreCase(userSessionData.getUserRequest())){ 
                if(!ServerUtil.isNullOrEmpty(userSessionData.getCategory())){
                	sb.append(" AND (TIREGROUP.VALUE=\'" + Encode.escapeDb(userSessionData.getCategory()) + "\') ");
                }
                if(!ServerUtil.isNullOrEmpty(userSessionData.getSegment())){
                	sb.append(" AND (CLASS.VALUE=\'" + Encode.escapeDb(userSessionData.getSegment()) + "\') ");
                }
        	}
        }
        if(!ServerUtil.isNullOrEmpty(userSessionData.getSubmodel())){
        	String submodel = userSessionData.getSubmodel();
        	String baseId = userSessionData.getAcesVehicleId();
        	if(ServerUtil.isNullOrEmpty(baseId)){
        		//--- get base id from year, make, model, submodel --//
        		List<Fitment> fits = vehicleDAO.getFitmentsByNames(userSessionData.getYear(), userSessionData.getMake(), userSessionData.getModel(), userSessionData.getSubmodel());
        		if(fits != null){
        			baseId = ((Fitment)fits.get(0)).getAcesVehicleId()+"";
        			userSessionData.setAcesVehicleId(baseId);
        		}
        	}
        	sb.append(" AND ("+TireSearchUtils.VEHICLE_TABLE+".ACES_VEHICLE_ID=\'" + Encode.escapeDb(userSessionData.getAcesVehicleId()) + "\') ");
        	if(!"DEFAULT_SUBMODEL".equals(submodel)) {
        	    sb.append(" AND ("+TireSearchUtils.VEHICLE_TABLE+".SUBMODEL=\'" + Encode.escapeDb(submodel) + "\') ");
        	}
        	
        }
		return sb.toString();
	}
	*/
	public static String buildFitmentQuery(List<Fitment> fitments){
		//Store store = results.getStore(userSessionData.getStoreNumber());
		StringBuffer sb = new StringBuffer();
		sb.append(" AND ( ");
		//List fitments = userSessionData.getFitments();
		for(int i=0; i< fitments.size(); i++){
			Fitment fitment = (Fitment)fitments.get(i);
			if(i > 0){
				sb.append( " OR \n"  );
			}
		sb.append(buildFitmentBaseQuery(fitment));
		/*
		if("78".equals(aspect)) {
			Condition or = new Condition();
			or.setType(Condition.OR);
			Fitment[] sub = AlphaUtil.substitute(this);
			for(int i=0; i<sub.length; i++)
				or.addElement(sub[i].generateCondition(store));
			return or;
		}
		Condition out = generateBaseCondition(store);
		
	    out.addElement(new Terminal(FullResults.FRONT_REAR_FIELD + "=\'" + frb + "\'"));
	    out.addElement(new Terminal(FullResults.STD_OPT_FIELD + "=\'" + (standard ? "S" : "O") + "\'"));
	    */
		}
	    sb.append(" ) ");
	    return sb.toString();
	}
	
	public static String buildFitmentBaseQuery(Fitment fitment){
		if("78".equals(fitment.getAspect())) {
			StringBuffer sub78 = new StringBuffer();
			
			Fitment[] sub = substitute(fitment);
			for(int i=0; i<sub.length; i++){
				if(i > 0){
					sub78.append(" OR ");
				}
				sub78.append(buildFitmentBaseQuery(sub[i]));
			}
			return sub78.toString();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" ( ");
		sb.append("("+PRODUCT_TABLE + ".CROSS_SECTION=\'" + fitment.getCrossSection() + "\') \n");
		sb.append(" AND ("+PRODUCT_TABLE + ".ASPECT=\'" + fitment.getAspect() + "\') \n");
		sb.append(" AND ("+PRODUCT_TABLE + ".RIM_SIZE=\'" + fitment.getRimSize() + "\') \n");
		StringBuffer winter = new StringBuffer();
		winter.append(" "+WINTER_CONDITION+" ");
		boolean addWinter = false;
		boolean extraLoad = false;
		
		String loadRange = fitment.getLoadRange();
		String loadIndex = fitment.getLoadIndex();
		String tireSize = fitment.getTireSize();
		//System.out.println("fitment.getLoadIndex() : "+fitment.getLoadIndex()+" fitment.getCrossSection() :"+fitment.getCrossSection()+"   fitment.getAspect(): "+fitment.getAspect()+" fitment.getRimSize(): "+fitment.getRimSize()+" tireSize :"+tireSize+"-----------------------------"+loadRange);
		if(!Util.isStanddardLoadRange(loadRange)) {
			StringBuffer or = new StringBuffer();
	        //--**TIRE SELECTOR RULES(4)**--//
			 //---Additional Rules from ROD --//
	        /*
	         If the vehicle type is SUV/LTR/VAN
	         And if size type is P-metric or Euro-Metric
	         And load range is XL or Reinforced 
	         Then we should display LTR tires of the same size in D or E ply only.
	         */
	        if("XL".equals(loadRange) || "RE".equals(loadRange) || "RF".equals(loadRange) ) {
	        	or.append(" (CONFIGURATION.LOAD_RANGE = 'XL') ");
	        	or.append(" OR (CONFIGURATION.LOAD_RANGE = 'RE') ");
	        	//System.out.println("================tireSize : "+tireSize+" fitment.getVehtype() "+fitment.getVehtype());
	        	if(com.bfrc.framework.util.Util.isPMetric(tireSize) 
	             || com.bfrc.framework.util.Util.isEuroMetric(tireSize)){
	        		if("TRK".equalsIgnoreCase(fitment.getVehtype()) || "VAN".equalsIgnoreCase(fitment.getVehtype()) ||"MVAN".equalsIgnoreCase(fitment.getVehtype()) ){
	        			or.append(" OR (CONFIGURATION.LOAD_RANGE = 'D') ");
	        			or.append(" OR (CONFIGURATION.LOAD_RANGE = 'E') ");
	        		}
	        		
	        	}
	        	extraLoad = true;
	        } else {
	        	//--**TIRE SELECTOR RULES(5)**--//
	        	//---------or.addElement(winter);
	        	if("|F|E|D|C|B|".indexOf("|"+loadRange+"|") >= 0){
	        		or.append("(CONFIGURATION.LOAD_RANGE <= 'F'  AND CONFIGURATION.LOAD_RANGE >= '"+loadRange+"') \n");
	        	}else{
	        		or = null;
	        	}
	        }
	        if(or != null)
	        	sb.append(" AND ("+or+")");
	    }
	    else {//--- LOAD RANGE is SL //CS
	    	//out.addElement(new Terminal("CONFIGURATION.LOAD_RANGE is null or (CONFIGURATION.LOAD_RANGE <= 'F'  AND CONFIGURATION.LOAD_RANGE >= 'B') "));
	    	sb.append(" AND ( CONFIGURATION.LOAD_RANGE is null or  CONFIGURATION.LOAD_RANGE >= 'B' ) \n");
	    }
		if(Util.isStanddardLoadRange(loadRange) && !com.bfrc.framework.util.StringUtils.isNullOrEmpty(loadIndex)) {
	    	//--**TIRE SELECTOR RULES(7)**--//
			if("TRK".equalsIgnoreCase(fitment.getVehtype()) || "VAN".equalsIgnoreCase(fitment.getVehtype()) ||"MVAN".equalsIgnoreCase(fitment.getVehtype()) ){
	    		//----(Rod) The Load Index Rules that you mentioned does not apply to LTR:  20110105--//
	    		addWinter = true;
	    		winter.append(" OR (CONFIGURATION.LOAD_INDEX >= 0) \n");
	    	}else{
	    	    addWinter = true;
	    	    winter.append(" OR (CONFIGURATION.LOAD_INDEX >= " + loadIndex+") \n");
	    	}
	        //snowTires.addElement(new Terminal(" CONFIGURATION.LOAD_INDEX >= " + loadIndex));
	    	//---------out.addElement(winter);
	    }
	    //--**TIRE SELECTOR RULES(13)**--//
	    if(Util.isLTMetric(tireSize) || com.bfrc.framework.util.Util.isFlotationMetric(tireSize)){
	    	if("XL".equals(loadRange)) {
	    		winter.append(" OR (CONFIGURATION.LOAD_RANGE = 'XL' or CONFIGURATION.LOAD_RANGE = 'RE') ");
	    		//snowTires.addElement(new Terminal(" (CONFIGURATION.LOAD_RANGE = 'XL' or CONFIGURATION.LOAD_RANGE = 'RE')"));
	        }else  if("RE".equals(loadRange)) {
	        	winter.append(" OR (CONFIGURATION.LOAD_RANGE = 'RE') \n");
	        	//snowTires.addElement(new Terminal("CONFIGURATION.LOAD_RANGE = 'RE'"));
	        } else if(Util.isStanddardLoadRange(loadRange)){//SL
	        	winter.append(" OR (CONFIGURATION.LOAD_RANGE is null or CONFIGURATION.LOAD_RANGE >= 'B') ");
	        	//snowTires.addElement(new Terminal("(CONFIGURATION.LOAD_RANGE is null or CONFIGURATION.LOAD_RANGE >= 'B')"));
	        }else{
	        	winter.append(" OR (CONFIGURATION.LOAD_RANGE >= '"+loadRange+"') ");
	        	//snowTires.addElement(new Terminal(" CONFIGURATION.LOAD_RANGE >= '"+loadRange+"' "));
	        }
	    	winter.append(" OR ((CONFIGURATION.LABEL NOT LIKE \'P%\'  AND  (CONFIGURATION.LABEL  LIKE \'LT%\' OR CONFIGURATION.LABEL  LIKE \'%.%\')) )");
	    	addWinter = true;
	    	//snowTires.addElement(new Terminal(" (CONFIGURATION.LABEL NOT LIKE \'P%\'  AND  (CONFIGURATION.LABEL  LIKE \'LT%\' OR CONFIGURATION.LABEL  LIKE \'%.%\'))"));
	    	//---------out.addElement(winter);
	    }
	    //--**TIRE SELECTOR RULES(12)**--//
	    if(Util.isPMetric(tireSize) || com.bfrc.framework.util.Util.isEuroMetric(tireSize)){
	    	sb.append(" AND ( CONFIGURATION.LABEL NOT LIKE \'%.%\') ");
	    }
	    //--**TIRE SELECTOR RULES(8)**--//
	    /*
	     * If the vehicle display requires LT-Metric or Flotation:             (13)  X
	          The load range must be >= the vehicle load range (can display any load index).
	          Do not show P-metric and Euro-metric tires 

	     */
	    if(Util.isLTMetric(tireSize) || Util.isFlotationMetric(tireSize)){
	    	if(!com.bfrc.framework.util.StringUtils.isNullOrEmpty(loadIndex)){
	    		sb.append(" AND (CONFIGURATION.LOAD_INDEX >= " + loadIndex+") \n");
	    	}
	    	sb.append(" AND (CONFIGURATION.LABEL NOT LIKE \'P%\'  AND  (CONFIGURATION.LABEL  LIKE \'LT%\' OR CONFIGURATION.LABEL  LIKE \'%.%\'))");
	    }
	    String fInflation = fitment.getFrontInf();
	    String rInflation = fitment.getRearInf();
	    int fInf = -1;
	    int rInf = -1;
	    if(!"RR".equals(fInflation))
	    	try {
	    		fInf = Integer.parseInt(fInflation);
	    	} catch(Exception ex) {
	    		//System.err.println("FRONT_INF not RR and not number: *" + fInflation + "*");
	    		//System.err.println("Fitment:" + fitment.toString());
	    	}
	    if(!"FF".equals(rInflation))
	    	try {
	    		rInf = Integer.parseInt(rInflation);
	    	} catch(Exception ex) {
	    		//System.err.println("REAR_INF not FF and not number: *" + rInflation + "*");
	    		//System.err.println("Fitment:" + fitment.toString());
	    	}
	    int mfgrInf = fInf;
	    if(rInf > fInf)
	    	mfgrInf = rInf;
	    // Euro-metric
	    //--**TIRE SELECTOR RULES(6)**--//
	    if(com.bfrc.framework.util.Util.isEuroMetric(tireSize)){
	    	sb.append(" AND (CONFIGURATION.LABEL NOT LIKE \'LT%\') \n");
			if((!extraLoad && mfgrInf > 35) || (extraLoad && mfgrInf > 41))
				sb.append(" AND (CONFIGURATION.LABEL NOT LIKE \'P%\') \n");
	    }
	    
	    if(addWinter)
	    	sb.append(" AND ("+winter+") \n");
	    sb.append(" AND (FRB=\'" + fitment.getFrb() + "\') \n");
	    sb.append(" AND (STANDARD_IND=\'" + ( 'S' == fitment.getStandardInd() ? "S" : "O") + "\') \n");
	    
	    //----------Make Speed Rating Condition ---//
	    String speedRating = fitment.getSpeedRating();
		if(speedRating == null || "NONE".equalsIgnoreCase(speedRating)){
			//-- no condition needed --//
		}else{
		
			
			StringBuffer speedCons = new StringBuffer();
			speedCons.append(WINTER_CONDITION);
			speedCons.append(" OR (");
			
			// Go through speed ratings in descending order, until reaching Q
			
			String[] speeds = SPEED;
			
			int numberOfSpeedRatingStepDown = 0;
			//-- The speed rating will be dropped one level if the vehicle type is Passenger and the Load Index is NULL or if the Load Index is under 100.  --//
			if("PASS".equals(fitment.getVehtype())
					&& (loadIndex == null || loadIndex.length() < 3)){
				numberOfSpeedRatingStepDown = 1;
			}
			//-- TIRE SELECTOR RULES and GENERAL CATALOG PAGE UPDATES by cs 20100527 --//
			//-- Drop one speed rating lower on LT or flotation sizes regardless of load index  --//
			if(Util.isFlotationMetric(tireSize) || Util.isLTMetric(tireSize)){
				numberOfSpeedRatingStepDown = 1;
			}
			/*	Exceptions to speed rating rules :
				Y, W, Z can't go down one level
				H  -> U is allowed 
				H -> T is allowed (technically two levels) 
				U -> T is allowed
				(Y) -> Y is allowed 
		    */
			if("W".equalsIgnoreCase(speedRating) || "Z".equalsIgnoreCase(speedRating)){
				numberOfSpeedRatingStepDown = 0;
			}
			if("H".equalsIgnoreCase(speedRating)){
				numberOfSpeedRatingStepDown = 2;
			}
			// For WI #927 New changes made to speed rating rules 
			//1. IF Speed Rating is '(Y)' step down 2 levels. (Y) -> W
			//2. IF Speed Rating is 'Y' step down 1 level. Y -> W
			
			if("(Y)".equalsIgnoreCase(speedRating)){
				numberOfSpeedRatingStepDown = 2;        //Changed for WI #927,to include W-rated tires
			}
			if("Y".equalsIgnoreCase(speedRating)){
				numberOfSpeedRatingStepDown = 1;       //Changed for WI #927,to include W-rated tires
			}			
			
			ServerUtil.debug("\t\t--> Car Speed: "+speedRating+" *** Speed Step down "+numberOfSpeedRatingStepDown+" step(s), ");
			ServerUtil.debug("\t\t-->2					class=com.mastercareusa.selector.QueryBuilderUtil     "
					+"    request.getState(\"vehtype\"): "+fitment.getVehtype()+
					"loadIndex : "+loadIndex);//cxsdebug				class=com.mastercareusa.selector.QueryBuilderUtil    speed : "+speed);//cxsdebug
			String speedRatings = "";
			int start = speeds.length-1;
			int end = 0;
			for(int i = speeds.length-1; i>=0; i--) {
				if(speedRating.equals(speeds[i])) {
					end = i-numberOfSpeedRatingStepDown;
					break;
				}
			}
			if(end < 0) end = 0;
			for(int i = start; i>=end; i--) {
				if(i== start)
					speedRatings = "'"+speeds[i]+"'";
				else 
					speedRatings += ", '"+speeds[i]+"'";
			}
			speedCons.append(" (SPEED.VALUE in (" + speedRatings + ")) \n");
			speedCons.append(" ) ");
			sb.append(" AND ("+speedCons+") ");
		}
		sb.append(" ) ");
		return sb.toString();
	}
	
	private static Hashtable conversion;
	static {
		conversion = new Hashtable();

		Vector e14 = new Vector(),
			f15 = new Vector(),
			g15 = new Vector(),
			h15 = new Vector(),
			l15 = new Vector();

		Fitment p19575 = new Fitment();
		p19575.setCrossSection("195");
		p19575.setAspect("75");
		Fitment p20570 = new Fitment();
		p20570.setCrossSection("205");
		p20570.setAspect("70");
		Fitment p20575 = new Fitment();
		p20575.setCrossSection("205");
		p20575.setAspect("75");
		Fitment p21565 = new Fitment();
		p21565.setCrossSection("215");
		p21565.setAspect("65");
		Fitment p21570 = new Fitment();
		p21570.setCrossSection("215");
		p21570.setAspect("70");
		Fitment p21575 = new Fitment();
		p21575.setCrossSection("215");
		p21575.setAspect("75");
		Fitment p22560 = new Fitment();
		p22560.setCrossSection("225");
		p22560.setAspect("60");
		Fitment p22575 = new Fitment();
		p22575.setCrossSection("225");
		p22575.setAspect("75");
		Fitment p23560 = new Fitment();
		p23560.setCrossSection("235");
		p23560.setAspect("60");
		Fitment p23570 = new Fitment();
		p23570.setCrossSection("235");
		p23570.setAspect("70");
		Fitment p23575 = new Fitment();
		p23575.setCrossSection("235");
		p23575.setAspect("75");
		Fitment p24550 = new Fitment();
		p24550.setCrossSection("245");
		p24550.setAspect("50");
		Fitment p24570 = new Fitment();
		p24570.setCrossSection("245");
		p24570.setAspect("70");
		Fitment p25555 = new Fitment();
		p25555.setCrossSection("255");
		p25555.setAspect("55");
		Fitment p25560 = new Fitment();
		p25560.setCrossSection("255");
		p25560.setAspect("60");
		Fitment p25565 = new Fitment();
		p25565.setCrossSection("255");
		p25565.setAspect("65");
		Fitment p26550 = new Fitment();
		p26550.setCrossSection("265");
		p26550.setAspect("50");
		Fitment p26560 = new Fitment();
		p26560.setCrossSection("265");
		p26560.setAspect("60");
		Fitment p27550 = new Fitment();
		p27550.setCrossSection("275");
		p27550.setAspect("50");
		Fitment p29550 = new Fitment();
		p29550.setCrossSection("295");
		p29550.setAspect("50");

		e14.addElement(p19575);
		e14.addElement(p20570);
		e14.addElement(p22560);
		e14.addElement(p24550);

		f15.addElement(p20575);
		f15.addElement(p21570);
		f15.addElement(p21565);
		f15.addElement(p23560);
		f15.addElement(p24550);

		g15.addElement(p21575);
		g15.addElement(p21570);
		g15.addElement(p23560);
		g15.addElement(p25555);
		g15.addElement(p26550);

		h15.addElement(p22575);
		h15.addElement(p23570);
		h15.addElement(p25560);
		h15.addElement(p27550);

		l15.addElement(p23575);
		l15.addElement(p24570);
		l15.addElement(p25565);
		l15.addElement(p26560);
		l15.addElement(p29550);

		conversion.put("E_14", e14);
		conversion.put("F_15", f15);
		conversion.put("G_15", g15);
		conversion.put("H_15", h15);
		conversion.put("L_15", l15);
    }

	public static Fitment[] substitute(Fitment alpha) {
		String letter = alpha.getCrossSection().trim();
		String rim = alpha.getRimSize().trim();
		Fitment[] out = new Fitment[0];
		Vector v = (Vector)conversion.get(letter + "_" + rim);
		if(v != null && !v.isEmpty()){
			int len = v.size();
			out = new Fitment[len];
			for(int i = 0;i < len;i++) {
				Fitment newFit = new Fitment(),
						curr = (Fitment)v.elementAt(i);
				newFit.setCrossSection(curr.getCrossSection());
				newFit.setAspect(curr.getAspect());

				newFit.setRimSize(rim);
				newFit.setSpeedRating(alpha.getSpeedRating());
				newFit.setStandardInd(alpha.getStandardInd());
				newFit.setFrb(alpha.getFrb());
				newFit.setTireSize(curr.getCrossSection() + "/" + curr.getAspect() + "R" + alpha.getRimSize());
				out[i] = newFit;
			}
		}
		
		return out;
	}
	
	public static void populatePrice(Tire tire, Tire price){
		 tire.setDescription(price.getDescription());
	     tire.setLine(price.getLine());
	     tire.setRetailPrice(price.getRetailPrice());
	     tire.setOnSale(price.getOnSale());
	     tire.setEndDate(price.getEndDate());
	     tire.setExciseTax(price.getExciseTax());
	     tire.setExciseTaxArticle(price.getExciseTaxArticle());
	     tire.setTireFee(price.getTireFee());
	     tire.setFeeDesc(price.getFeeDesc());
	     tire.setFeeArticle(price.getFeeArticle());
	     tire.setWheelBalanceWeight(price.getWheelBalanceWeight());
	     tire.setWheelWgtArticle(price.getWheelWgtArticle());
	     tire.setWheelBalanceLabor(price.getWheelBalanceLabor());
	     tire.setWheelBalArticle(price.getWheelBalArticle());
	     tire.setValveStem(price.getValveStem());
	     tire.setValveStemArticle(price.getValveStemArticle());
	     tire.setDisposalPrice(price.getDisposalPrice());
	     tire.setDisposalFeeArticle(price.getDisposalFeeArticle());
	     tire.setTireInstallPrice(price.getTireInstallPrice());
	     tire.setTireInstallArticle(price.getTireInstallArticle());
	}
	
	public static boolean hasDriveGuardProductTires(List<Tire> list) {
    	return getDriveGuardProductTires(list).size() > 0;
    }
	
	public static boolean hasPrimewellProductTires(List<Tire> list) {
    	return getPrimewellProductTires(list).size() > 0;
    }
    
    public static List<Tire> getDriveGuardProductTires(List<Tire> list) {
    	List<Tire> l = new ArrayList<Tire>();
    	if(list != null){
    		for(Tire tire : list){
    			if ("DRIVEGUARD".equalsIgnoreCase(tire.getTireName())) {
    				l.add(tire);
    			}
    		}
    	}
    	return l;
    }
    
    public static List<Tire> getPrimewellProductTires(List<Tire> list) {
    	List<Tire> l = new ArrayList<Tire>();
    	if(list != null){
    		for(Tire tire : list){
    			if ("PRIMEWELL".equalsIgnoreCase(tire.getBrand())) {
    				l.add(tire);
    			}
    		}
    	}
    	return l;
    }
    
    public static List<Tire> moveDriveGuardProductTiresToFront(List<Tire> list) {
    	return moveDriveGuardProductTiresToFront(list, "1");
    }
    
    public static List<Tire> moveDriveGuardProductTiresToFront(List<Tire> list, String tpms) {
    	List<Tire> driveGuardTires = new ArrayList<Tire>();
    	if(list != null){
    		if (StringUtils.isNullOrEmpty(tpms)) {
    			tpms = "1";
    		}
    		for(int i=0; list != null && i < list.size(); i++) {
    			Tire tire = list.get(i);
    			if ("DRIVEGUARD".equalsIgnoreCase(tire.getTireName()) && "1".equalsIgnoreCase(tpms)) {
    				driveGuardTires.add(list.remove(i));
    			} else if ("DRIVEGUARD".equalsIgnoreCase(tire.getTireName()) && "0".equalsIgnoreCase(tpms)) {
    				list.remove(i);
    			}
    		}
    	}
    	if (driveGuardTires.size() > 0) {
    		for(int j=0; j < driveGuardTires.size(); j++){
    			list.add(j, driveGuardTires.get(j));
    		}
    	}
    	return list;
    }
	
	public static List sortTireList(List<Tire> list, String sortBy){
		return sortTireList(list, sortBy, false, null);
	}
	public static List sortTireList(List<Tire> list, String sortBy, boolean desc){
		return sortTireList(list, sortBy, desc, null);
	}
	public static List sortTireList(List<Tire> list, String sortBy, boolean desc, Map mappedSurveyData){
		return sortTireList(list, sortBy, desc, mappedSurveyData, null);
	}
	public static List sortTireList(List<Tire> list, String sortBy, boolean desc, Map mappedSurveyData, Map mappedGroupings){
		if(mappedSurveyData == null)
			mappedSurveyData = new HashMap();
		if(mappedGroupings == null)
			mappedGroupings = new HashMap();
		List<Tire> sortedList = new ArrayList<Tire>();
        List<String> sortTempData = new ArrayList<String>();
        Map<String, Tire> sortTempDataMap = new HashMap<String, Tire>();
        java.text.DecimalFormat df8_2 = new java.text.DecimalFormat("00000.00");
        if(list != null){
        	for(Tire tire: list){
        		String article_id = String.valueOf(tire.getArticle());
        		Tire rearTire = tire.getRearTire();
        		double price = tire.getSalePrice() > 0 ? tire.getSalePrice() : tire.getRetailPrice();
        		String tire_brand = tire.getBrand() == null ? "" : tire.getBrand();
        		String warrantyMileage = tire.getMileage() == null ? "" : tire.getMileage();
        		//String warrantyId = String.valueOf(tire.getWarrantyId());
        		article_id = rearTire == null ? article_id : article_id+"_"+rearTire.getArticle();
        		sortTempDataMap.put(article_id,tire);
        		double rearPrice = 0;
        		if(rearTire != null){
        			if(rearTire.getSalePrice() <= 0){
        				rearPrice = 9999;
        				if(desc){
        					rearPrice = 0;
                        }
        			}
        		}
        		if(price <=0 ){
                	price = 9999;
                    if(desc){
                    	price = 0;
                    }
                }
        		//-- check if matched set and only one has pricing --//
        		if(rearTire != null){
        			price = tire.getSalePrice() > 0 ? tire.getSalePrice() : tire.getRetailPrice();
        			rearPrice = rearTire.getSalePrice() > 0 ? rearTire.getSalePrice() : rearTire.getRetailPrice();
        			if(price > 0 && rearPrice <= 0){//front price only
        				rearPrice = 4999;
        				if(desc){//make it bigger than no price and smaller than normal price
        					price = price/6;
        					rearPrice = 0;
                        }
        			}else if(price <= 0 && rearPrice > 0){//rear price only
        				price = 4999;
        				if(desc){//make it bigger than no price and smaller than normal price
        					price = 0;
        					rearPrice = rearPrice/6;
                        }
        			}else if(price > 0 && rearPrice > 0){//rear price only
        				
        			}else{//both has no pricing
        				price = 4999;
                        if(desc){
                        	price = 0;
                        }
                        rearPrice = 4999;
        				if(desc){
        					rearPrice = 0;
                        }
        			}
        		}
                String fprice = df8_2.format(price+rearPrice);
        		if("price".equalsIgnoreCase(sortBy)){
                    sortTempData.add(fprice+"|"+article_id);
                }else if("brand".equalsIgnoreCase(sortBy)){
                    sortTempData.add(tire_brand.toLowerCase()+"|"+fprice+"|"+article_id);
                }else if("warranty".equalsIgnoreCase(sortBy)){
                	fprice = df8_2.format(9999 - price);
                    if( warrantyMileage.toLowerCase().indexOf("no") >= 0){
                        sortTempData.add("000000|"+fprice+"|"+article_id);
                    }else{
                        sortTempData.add(warrantyMileage.toLowerCase()+"|"+fprice+"|"+article_id);
                    }
                }else if("class".equalsIgnoreCase(sortBy)){
                	TireGrouping tg = (TireGrouping)mappedGroupings.get(tire.getDisplayId());
                	String tire_class = tg!=null ? tg.getClass_() : tire.getTireClassName()==null ? "" : tire.getTireClassName();
                    sortTempData.add(tire_class.toLowerCase()+"|"+fprice+"|"+article_id);
                }else if("wet".equalsIgnoreCase(sortBy)){
                	desc = true;
                	MindshareTiresurveyDetails detail = (MindshareTiresurveyDetails)mappedSurveyData.get(tire.getDisplayId());
                	double d = detail == null || detail.getWetTraction() == null ? 0 : detail.getWetTraction().doubleValue();
                	String fd = df8_2.format(d);
                    sortTempData.add(fd+"|"+article_id);
                }else if("dry".equalsIgnoreCase(sortBy)){
                	desc = true;
                	MindshareTiresurveyDetails detail = (MindshareTiresurveyDetails)mappedSurveyData.get(tire.getDisplayId());
                	double d = detail == null || (detail.getTireStability() == null || detail.getDryTraction() == null)  ? 0 : (detail.getTireStability().doubleValue()+detail.getDryTraction().doubleValue())/2;
                	String fd = df8_2.format(d);
                    sortTempData.add(fd+"|"+article_id);
                }else if("comfort".equalsIgnoreCase(sortBy)){
                	desc = true;
                	MindshareTiresurveyDetails detail = (MindshareTiresurveyDetails)mappedSurveyData.get(tire.getDisplayId());
                	double d = detail == null || (detail.getRideComfort() == null || detail.getNoiseLevel() == null)  ? 0 : (detail.getRideComfort().doubleValue()+detail.getNoiseLevel().doubleValue())/2;
                	String fd = df8_2.format(d);
                    sortTempData.add(fd+"|"+article_id);
                }else if("winter/snow".equalsIgnoreCase(sortBy)){
                	desc = true;
                	MindshareTiresurveyDetails detail = (MindshareTiresurveyDetails)mappedSurveyData.get(tire.getDisplayId());
                	double d = detail == null || detail.getTractionInSnowIce() == null ? 0 : detail.getTractionInSnowIce().doubleValue();
                	String fd = df8_2.format(d);
                    sortTempData.add(fd+"|"+article_id);
                }else if("treadwear".equalsIgnoreCase(sortBy)){
                	desc = true;
                	MindshareTiresurveyDetails detail = (MindshareTiresurveyDetails)mappedSurveyData.get(tire.getDisplayId());
                	double d = detail == null || detail.getTireWear() == null ? 0 : detail.getTireWear().doubleValue();
                	String fd = df8_2.format(d);
                    sortTempData.add(fd+"|"+article_id);
                }
        	}
        	Collections.sort(sortTempData);
        	if(("price".equalsIgnoreCase(sortBy) || "warranty".equalsIgnoreCase(sortBy)) && desc){
                for(int i=(sortTempData.size()-1); i>=0;i--){
                    String s = (String)sortTempData.get(i) == null ? "" : (String)sortTempData.get(i);
                    String[] tokens =  s.split("\\|");
                    if(tokens != null && tokens.length > 1){
                        String article = tokens[1];
                        if(tokens.length == 3)
                        	article = tokens[2];
                        sortedList.add(sortTempDataMap.get(article));
                    }else{
                      //System.err.println("tirequote :: list :: Out of Bound Error");
                      System.err.println("\t\t Sort String is : "+s+" Sort by :"+sortBy);
                    }
                }
            }else{
            	if(desc){
            		for(int i=(sortTempData.size()-1); i>=0;i--){
	                	String s = (String)sortTempData.get(i) == null ? "" : (String)sortTempData.get(i);
	                    String[] tokens =  s.split("\\|");
	                    if(tokens != null && tokens.length > 1){
	                        String article = tokens[1];
	                        if(tokens.length == 3)
	                        	article = tokens[2];
	                        sortedList.add(sortTempDataMap.get(article));
	                    }else{
	                      //System.err.println("tirequote :: list :: Out of Bound Error");
	                      System.err.println("\t\t Sort String is : "+s+" Sort by :"+sortBy);
	                    }
	                }
            	}else{
	                for(int i=0; i<sortTempData.size();i++){
	                	String s = (String)sortTempData.get(i) == null ? "" : (String)sortTempData.get(i);
	                    String[] tokens =  s.split("\\|");
	                    if(tokens != null && tokens.length > 1){
	                        String article = tokens[1];
	                        if(tokens.length == 3)
	                        	article = tokens[2];
	                        sortedList.add(sortTempDataMap.get(article));
	                    }else{
	                      //System.err.println("tirequote :: list :: Out of Bound Error");
	                      System.err.println("\t\t Sort String is : "+s+" Sort by :"+sortBy);
	                    }
	                }
            	}
            }
        }
		return sortedList;
	}
	
	public static Map<String, Tire> getMappedTires(List<Tire> tires){
    	Map<String, Tire> m = new LinkedHashMap<String, Tire>();
    	if(tires != null){
	    	for(Tire tire : tires){
	    		m.put(String.valueOf(tire.getArticle()), tire);
	    	}
    	}
    	return m;
    }
	
	public static List<Tire> getMacthedSetTires(List<Tire> tires){
    	Map<String, Tire> m = new LinkedHashMap<String, Tire>();
    	if(tires != null){
    		List tr = new ArrayList();
    	    for(int i=0; i< tires.size(); i++){
    	        Tire tire =(Tire)tires.get(i);
    	        if(tire.getRearArticle() > 0){
    	            tr.add(tire);
    	        }
    	    }
    	return tr;
    	}
    	return null;
    }
	
	public static List<Tire> getFrontTires(List<Tire> tires){
    	if(tires != null){
    		List<Tire> tl = new ArrayList<Tire>();
    	    for(int i=0; i< tires.size(); i++){
    	        Tire tire =(Tire)tires.get(i);
    	        if("Front".equalsIgnoreCase(tire.getFrontRearBothDisplay())){
    	        	tl.add(tire);
    	        }
    	    }
    	    return tl;
    	}
    	return null;
    }
	
	public static List<Tire> getRearTires(List<Tire> tires){
    	if(tires != null){
    		List<Tire> tl = new ArrayList<Tire>();
    	    for(int i=0; i< tires.size(); i++){
    	        Tire tire =(Tire)tires.get(i);
    	        if("Rear".equalsIgnoreCase(tire.getFrontRearBothDisplay())){
    	        	tl.add(tire);
    	        }
    	    }
    	    return tl;
    	}
    	return null;
    }
	
	public static List<Tire> DedupTires(List<Tire> tires){
		List<Tire> uniqueTires = new ArrayList<Tire>();
		
		if(tires != null){
			//-- dedup tires --//
			java.util.Map<Long, Tire> m = new LinkedHashMap<Long, Tire>();
			for(Tire tire: tires){
				Long article = new Long(tire.getArticle());
				
				if(!m.containsKey(article)){
					m.put(article,tire);
				}else{
					//if duplicate article, like 2003GMCSierraC3500RWD
					//then set the FRB to B
					String existStdOpt = m.get(article).getStandardOptional();
					String currStdOpt = tire.getStandardOptional();
					if (existStdOpt.equals(currStdOpt)) {
						m.get(article).setFrontRearBoth("B");
					} else {
						if ("S".equals(currStdOpt)) {
							m.put(article,tire);
						}
					}
				}
			}
			uniqueTires.addAll(m.values());
		}
		return uniqueTires;
	}
	
	@SuppressWarnings("unchecked")
    public static Map<String, List<Tire>> getGroupedTires(List<Tire> tires){
    	Map<String, List<Tire>> m = new LinkedHashMap<String, List<Tire>>();
    	if(tires != null){
	    	for(Tire tire : tires){
	    		String strGroupId = String.valueOf(tire.getTiregroupId());
	    		if(m.get(strGroupId) == null){
	    			m.put(strGroupId, (new ArrayList<Tire>()));
	    		}
	    		((List<Tire>)m.get(strGroupId)).add(tire);
	    	}
    	}
    	return m;
    }
	
	 public static Map<String, List<Tire>> getGroupedTiresWithDriveGuardProductOnTop(List<Tire> tires){
		 return  getGroupedTiresWithDriveGuardProductOnTop(tires,"1");
	 }
	 
	@SuppressWarnings("unchecked")
    public static Map<String, List<Tire>> getGroupedTiresWithDriveGuardProductOnTop(List<Tire> tires, String tpms){
    	Map<String, List<Tire>> m = new LinkedHashMap<String, List<Tire>>();
    	String driveGuardGroupID = "5";
    	if(tires != null){
    		if(ServerUtil.isNullOrEmpty(tpms)){
    			tpms = "1";
    		}
	    	for(Tire tire : tires){
	    		if ("DRIVEGUARD".equalsIgnoreCase(tire.getTireName())) {
	    			if(m.get(driveGuardGroupID) == null){
		    			m.put(driveGuardGroupID, (new ArrayList<Tire>()));
		    		}
	    			if("1".equalsIgnoreCase(tpms)){
	    			((List<Tire>)m.get(driveGuardGroupID)).add(tire);
	    			}
	    		} else {
		    		String strGroupId = String.valueOf(tire.getTiregroupId());
		    		if(m.get(strGroupId) == null){
		    			m.put(strGroupId, (new ArrayList<Tire>()));
		    		}
		    		((List<Tire>)m.get(strGroupId)).add(tire);
	    		}
	    	}
    	}
    	return m;
    }
	
	public static String displayStandard(Fitment param) {
		String pos = "";
		if(!param.isFront())
			pos = "(Rear)";
		else if (!param.isBack())
			pos = "(Front)";
		// If this is an alphanumeric size, expand it first
		String aspect = param.getAspect(), out = "";
		if("78".equals(aspect)) {
			Fitment[] tires = substitute(param);
			for(int i = 0;i < tires.length;i++)
				out += displayStandard(tires[i]);
			}
		// Otherwise format it
		else {
	            String speed = param.getSpeedRating();
			if(speed == null || speed.equals("NONE"))
				speed = "None";
			out += displayStandard(param.getFitmentText(), speed, pos, param.getLoadIndex(), param.getLoadRange());
			}
		return out;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/24/2001 %r)
	 * @return java.lang.String
	 */
	public static String displayStandard(String size, String speed, String pos) {
	    return displayStandard(size, speed, pos, null, null);
	}
	public static String displayStandard(String size, String speed, String pos, String loadIndex, String loadRange) {
		String out = "<span class=\"redbold\">Standard Size:</span> " + size + " " + pos + "<br />"
			+ "<span class=\"redbold\">Standard Speed Rating:</span> " + speed + " " + pos + "<br />";
	        if(!Util.isStanddardLoadRange(loadRange))
	            out += "<span class=\"redbold\">Standard Load Range:</span> " + loadRange + " " + pos + "<br />";
	        else if(!com.bfrc.framework.util.StringUtils.isNullOrEmpty(loadIndex))
	            out += "<span class=\"redbold\">Standard Load Index:</span> " + loadIndex + " " + pos + "<br />";
	        return out + "<br />";
	}
	
	public static boolean isStandardSpeedFor(String speed, String standard) {
		if(standard == null || "NONE".equals(standard))
			return true;
		boolean out = false;
		boolean append = false;
		for(int i=0;i<SPEED.length;i++) {
			if(SPEED[i].equals(standard))
				append = true;
			if(append && SPEED[i].equals(speed))
				out = true;
		}
		return out;
	}
	
	private static Map mappedSpeedRatingAndSpeedValue;
	static {
		mappedSpeedRatingAndSpeedValue = new LinkedHashMap();
		for(int i=0; i< SPEED.length; i++){
			mappedSpeedRatingAndSpeedValue.put(SPEED[i], SPEED_VALUE[i]);
		}
	}
	
	public static Map getMappedSpeedRatingAndSpeedValue(){
		return mappedSpeedRatingAndSpeedValue;
	}
	
	private static Map mappedLoadIndexAndLbs;
	
	static {
		mappedLoadIndexAndLbs = new LinkedHashMap();
        mappedLoadIndexAndLbs.put("0","99");
        mappedLoadIndexAndLbs.put("50","419");
        mappedLoadIndexAndLbs.put("100","1764");
        mappedLoadIndexAndLbs.put("1","102");
        mappedLoadIndexAndLbs.put("51","430");
        mappedLoadIndexAndLbs.put("101","1819");
        mappedLoadIndexAndLbs.put("2","105");
        mappedLoadIndexAndLbs.put("52","441");
        mappedLoadIndexAndLbs.put("102","1874");
        mappedLoadIndexAndLbs.put("3","107");
        mappedLoadIndexAndLbs.put("53","454");
        mappedLoadIndexAndLbs.put("103","1929");
        mappedLoadIndexAndLbs.put("4","110");
        mappedLoadIndexAndLbs.put("54","467");
        mappedLoadIndexAndLbs.put("104","1984");
        mappedLoadIndexAndLbs.put("5","114");
        mappedLoadIndexAndLbs.put("55","481");
        mappedLoadIndexAndLbs.put("105","2039");
        mappedLoadIndexAndLbs.put("6","117");
        mappedLoadIndexAndLbs.put("56","494");
        mappedLoadIndexAndLbs.put("106","2094");
        mappedLoadIndexAndLbs.put("7","120");
        mappedLoadIndexAndLbs.put("57","507");
        mappedLoadIndexAndLbs.put("107","2149");
        mappedLoadIndexAndLbs.put("8","123");
        mappedLoadIndexAndLbs.put("58","520");
        mappedLoadIndexAndLbs.put("108","2205");
        mappedLoadIndexAndLbs.put("9","128");
        mappedLoadIndexAndLbs.put("59","536");
        mappedLoadIndexAndLbs.put("109","2271");
        mappedLoadIndexAndLbs.put("10","132");
        mappedLoadIndexAndLbs.put("60","551");
        mappedLoadIndexAndLbs.put("110","2337");
        mappedLoadIndexAndLbs.put("11","136");
        mappedLoadIndexAndLbs.put("61","567");
        mappedLoadIndexAndLbs.put("111","2403");
        mappedLoadIndexAndLbs.put("12","139");
        mappedLoadIndexAndLbs.put("62","584");
        mappedLoadIndexAndLbs.put("112","2469");
        mappedLoadIndexAndLbs.put("13","143");
        mappedLoadIndexAndLbs.put("63","600");
        mappedLoadIndexAndLbs.put("113","2535");
        mappedLoadIndexAndLbs.put("14","148");
        mappedLoadIndexAndLbs.put("64","617");
        mappedLoadIndexAndLbs.put("114","2601");
        mappedLoadIndexAndLbs.put("15","152");
        mappedLoadIndexAndLbs.put("65","639");
        mappedLoadIndexAndLbs.put("115","2679");
        mappedLoadIndexAndLbs.put("16","157");
        mappedLoadIndexAndLbs.put("66","639");
        mappedLoadIndexAndLbs.put("116","2756");
        mappedLoadIndexAndLbs.put("17","161");
        mappedLoadIndexAndLbs.put("67","677");
        mappedLoadIndexAndLbs.put("117","2833");
        mappedLoadIndexAndLbs.put("18","165");
        mappedLoadIndexAndLbs.put("68","694");
        mappedLoadIndexAndLbs.put("118","2910");
        mappedLoadIndexAndLbs.put("19","171");
        mappedLoadIndexAndLbs.put("69","716");
        mappedLoadIndexAndLbs.put("119","2998");
        mappedLoadIndexAndLbs.put("20","176");
        mappedLoadIndexAndLbs.put("70","739");
        mappedLoadIndexAndLbs.put("120","3086");
        mappedLoadIndexAndLbs.put("21","182");
        mappedLoadIndexAndLbs.put("71","761");
        mappedLoadIndexAndLbs.put("121","3197");
        mappedLoadIndexAndLbs.put("22","187");
        mappedLoadIndexAndLbs.put("72","783");
        mappedLoadIndexAndLbs.put("122","3307");
        mappedLoadIndexAndLbs.put("23","193");
        mappedLoadIndexAndLbs.put("73","805");
        mappedLoadIndexAndLbs.put("123","3417");
        mappedLoadIndexAndLbs.put("24","198");
        mappedLoadIndexAndLbs.put("74","827");
        mappedLoadIndexAndLbs.put("124","3527");
        mappedLoadIndexAndLbs.put("25","204");
        mappedLoadIndexAndLbs.put("75","852");
        mappedLoadIndexAndLbs.put("125","3638");
        mappedLoadIndexAndLbs.put("26","209");
        mappedLoadIndexAndLbs.put("76","882");
        mappedLoadIndexAndLbs.put("126","3748");
        mappedLoadIndexAndLbs.put("27","215");
        mappedLoadIndexAndLbs.put("77","908");
        mappedLoadIndexAndLbs.put("127","3858");
        mappedLoadIndexAndLbs.put("28","220");
        mappedLoadIndexAndLbs.put("78","937");
        mappedLoadIndexAndLbs.put("128","3968");
        mappedLoadIndexAndLbs.put("29","227");
        mappedLoadIndexAndLbs.put("79","963");
        mappedLoadIndexAndLbs.put("129","4079");
        mappedLoadIndexAndLbs.put("30","234");
        mappedLoadIndexAndLbs.put("80","992");
        mappedLoadIndexAndLbs.put("130","4189");
        mappedLoadIndexAndLbs.put("31","240");
        mappedLoadIndexAndLbs.put("81","1019");
        mappedLoadIndexAndLbs.put("131","4289");
        mappedLoadIndexAndLbs.put("32","247");
        mappedLoadIndexAndLbs.put("82","1047");
        mappedLoadIndexAndLbs.put("132","4409");
        mappedLoadIndexAndLbs.put("33","254");
        mappedLoadIndexAndLbs.put("83","1074");
        mappedLoadIndexAndLbs.put("133","4541");
        mappedLoadIndexAndLbs.put("34","260");
        mappedLoadIndexAndLbs.put("84","1102");
        mappedLoadIndexAndLbs.put("134","4674");
        mappedLoadIndexAndLbs.put("35","267");
        mappedLoadIndexAndLbs.put("85","1135");
        mappedLoadIndexAndLbs.put("135","4806");
        mappedLoadIndexAndLbs.put("36","276");
        mappedLoadIndexAndLbs.put("86","1168");
        mappedLoadIndexAndLbs.put("136","4938");
        mappedLoadIndexAndLbs.put("37","282");
        mappedLoadIndexAndLbs.put("87","1201");
        mappedLoadIndexAndLbs.put("137","5071");
        mappedLoadIndexAndLbs.put("38","291");
        mappedLoadIndexAndLbs.put("88","1235");
        mappedLoadIndexAndLbs.put("138","5203");
        mappedLoadIndexAndLbs.put("39","300");
        mappedLoadIndexAndLbs.put("89","1279");
        mappedLoadIndexAndLbs.put("139","5357");
        mappedLoadIndexAndLbs.put("40","309");
        mappedLoadIndexAndLbs.put("90","1323");
        mappedLoadIndexAndLbs.put("140","5512");
        mappedLoadIndexAndLbs.put("41","320");
        mappedLoadIndexAndLbs.put("91","1356");
        mappedLoadIndexAndLbs.put("141","5677");
        mappedLoadIndexAndLbs.put("42","331");
        mappedLoadIndexAndLbs.put("92","1389");
        mappedLoadIndexAndLbs.put("142","5842");
        mappedLoadIndexAndLbs.put("43","342");
        mappedLoadIndexAndLbs.put("93","1433");
        mappedLoadIndexAndLbs.put("143","6008");
        mappedLoadIndexAndLbs.put("44","353");
        mappedLoadIndexAndLbs.put("94","1477");
        mappedLoadIndexAndLbs.put("144","6173");
        mappedLoadIndexAndLbs.put("45","364");
        mappedLoadIndexAndLbs.put("95","1521");
        mappedLoadIndexAndLbs.put("145","6393");
        mappedLoadIndexAndLbs.put("46","375");
        mappedLoadIndexAndLbs.put("96","1565");
        mappedLoadIndexAndLbs.put("146","6614");
        mappedLoadIndexAndLbs.put("47","386");
        mappedLoadIndexAndLbs.put("97","1609");
        mappedLoadIndexAndLbs.put("147","6779");
        mappedLoadIndexAndLbs.put("48","397");
        mappedLoadIndexAndLbs.put("98","1653");
        mappedLoadIndexAndLbs.put("148","6844");
        mappedLoadIndexAndLbs.put("49","408");
        mappedLoadIndexAndLbs.put("99","1709");
        mappedLoadIndexAndLbs.put("149","7165");
        mappedLoadIndexAndLbs.put("150","7385");
	}
	
	public static Map getMappedLoadIndexAndLoadIndexValue(){
		return mappedLoadIndexAndLbs;
	}
	
	public static boolean isValidArticle(String article){
		if(StringUtils.isNullOrEmpty(article))
			return false;
		return isValidArticles(new String[]{article});
	}
	public static boolean isValidArticles(List articles){
        if(articles == null || articles.size() == 0)
        	return false;
		return isValidArticles((String[])articles.toArray());
	}
	public static boolean isValidArticles(String[] articleArray){
		if(articleArray != null && articleArray.length > 0) {
			for(int i=0; i<articleArray.length; i++){
				if(ServerUtil.isNullOrEmpty(articleArray[i])){
					return false;
				}else{
					try{
						String article = articleArray[i].trim();
						String[] tokens = article.split("_");
						for(int j=0; j< tokens.length; j++){
						   Integer.parseInt(tokens[j]);
						}
					}catch(Exception ex){
						return false;
					}
					
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	public static String getFriendlyTireName(String tireName){
		if(tireName == null)
			return null;
		return tireName.replaceAll(StringUtils.NAME_FILTER_REGX, "");
    }
	
	public static String CATALOG_GETQUOTE_CODE_NOPRICE="NP";
	public static String CATALOG_GETQUOTE_CODE_NOMATCH="NM";
	public static String CATALOG_GETQUOTE_CODE_MULTIPLEMATCH="MM";
	public static String CATALOG_GETQUOTE_CODE_TOQUOTE="TQ";
	
	public static CatalogGetQuoteDataBean processCatalogGetQuote(HttpServletRequest request, TireSearchResults results){
		String _checkTireId = request.getParameter("_checkTireId");
		UserSessionData userData = UserProfileUtils.getUserSessionData(request.getSession());
		String method = request.getParameter("method") == null ? userData.getUserRequest() : request.getParameter("method");
		boolean bySize = "size".equals(method);
        if(StringUtils.isNullOrEmpty(_checkTireId)){
        	return null;
        }
        try{
			String frb = request.getParameter("frbKey");
		    if(StringUtils.isNullOrEmpty(frb))
		        frb = request.getParameter("FRB");
		    String stdopt = request.getParameter("stdopt");
	        if(StringUtils.isNullOrEmpty(stdopt))
	            stdopt = request.getParameter("STDOPT");
			CatalogGetQuoteDataBean bean = new CatalogGetQuoteDataBean();
			ServletContext ctx = request.getSession().getServletContext();
			CatalogDAO catalogDAO = (CatalogDAO)Config.locate(ctx, "catalogDAO");
	        if(!StringUtils.isNullOrEmpty(_checkTireId)){
	            String responseMessage="";
	            com.bfrc.pojo.tire.jda.Display tireCatalog=null;
	            List passedTires =null;
	            try{
	            	passedTires = catalogDAO.getProductsInLine(Long.parseLong(_checkTireId));
	            	tireCatalog = catalogDAO.getProductLine(Long.parseLong(_checkTireId));
	            }catch(NumberFormatException nfe){
	            	Util.debug("Bad tire ID passed");
	            	return null;
	            }
	            bean.setPassedTireCatalog(tireCatalog);
	            List passedArticles = new ArrayList();
	            for(int i=0; passedTires != null && i<passedTires.size(); i++){
	               com.bfrc.pojo.tire.jda.Configuration tire = (com.bfrc.pojo.tire.jda.Configuration)passedTires.get(i);
	               passedArticles.add(tire.getSku());
	            }
	            //-- the target tires to check against --//
	            List tires = results.getTires();
	            boolean MacthedSetSelected = false;
	            //-- use tires based on user selections
	            if("S".equals(stdopt)){
	                //-- if select Standard Tires then just run against standard tires --//
	                tires = results.getStandardSizeTires();
	            }else if("O".equals(stdopt)){
	                //-- if select Optional Tires then just run against Optional tires --//
	                tires = results.getOptionalSizeTires();
	            }
	            if(!bySize && results.hasOptionalSizeTires() && results.getStandardSizeTires().size() >0 && stdopt == null){
	            	return null;
	            }
	            if (results.isMatchedSet() && "F".equals(frb)) {
	                //-- if select Front Tires then just run against Front tires --//
	                tires = results.getFrontTires();
	            }else if (results.isMatchedSet() && "R".equals(frb)) {
	                //-- if select Rear Tires then just run against Rear tires --//
	                tires = results.getRearTires();
	            }else if (results.isMatchedSet() && "B".equals(frb)) {
	                //-- if select Matched Tires then just run against Matched tires --//
	                tires = results.getMatchedSetTires();
	                MacthedSetSelected = true;
	            }else if (results.isMatchedSet() && frb == null) {
	                return null;
	            }
	            java.util.Map mappedTires = results.getMappedTires();
	            List articlesInCatalog = new ArrayList();
	            List articlesInCatalogRear = new ArrayList();
	            boolean containMachedSet = false;
	            for(int i=0; tires != null && i<tires.size(); i++){
	               Tire tire = (Tire)tires.get(i);
	               if(MacthedSetSelected){
	                   if(passedArticles.contains(tire.getArticle()) && passedArticles.contains(tire.getRearArticle())){
	                       articlesInCatalog.add(tire.getArticle());
	                       articlesInCatalogRear.add(tire.getRearArticle());
	                   }
	               }else{
	                   if(passedArticles.contains(tire.getArticle())){
	                       articlesInCatalog.add(tire.getArticle());
	                   }
	               }
	            }
	            bean.setArticlesInCatalog(articlesInCatalog);
	            bean.setArticlesInCatalogRear(articlesInCatalogRear);
	            if(articlesInCatalog.size() == 1){
	                if(MacthedSetSelected){
	                    Tire tire = (Tire)mappedTires.get(String.valueOf(articlesInCatalog.get(0)));
	                    Tire rearTire = (Tire)mappedTires.get(String.valueOf(articlesInCatalogRear.get(0)));
	                    ///tire_pricing/quote.jsp?article=105001_123701&qty=2&firstName=First%20Name&lastName=Last%20Name
	                    if(tire.getRetailPrice() <= 0 || rearTire.getRetailPrice() <= 0){
	                    	bean.setMessageCode(CATALOG_GETQUOTE_CODE_NOPRICE);
	                    }else{
	                         bean.setMessageCode(CATALOG_GETQUOTE_CODE_TOQUOTE);
	                    }
	                }else{
	                    Tire tire = (Tire)mappedTires.get(String.valueOf(articlesInCatalog.get(0)));
	                    ///tire_pricing/quote.jsp?article=105001_123701&qty=2&firstName=First%20Name&lastName=Last%20Name
	                    if(tire.getRetailPrice() <= 0){
	                    	bean.setMessageCode(CATALOG_GETQUOTE_CODE_NOPRICE);
	                    }else{
	                    	bean.setMessageCode(CATALOG_GETQUOTE_CODE_TOQUOTE);
	                    }
	                }
	            }else if(articlesInCatalog.size() == 0){
	            	bean.setMessageCode(CATALOG_GETQUOTE_CODE_NOMATCH);
	            }else if(articlesInCatalog.size() > 1){
	            	bean.setMessageCode(CATALOG_GETQUOTE_CODE_MULTIPLEMATCH);
	            }
	        }
			return bean;
        }catch(Exception ex){
        	ex.printStackTrace();
        	return null;
        }
	}
	
	public static String getNewFilteredName(String name){
		if(NEW_LIST_FILTERED_CARMINIVAN.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_FILTERED_CARMINIVAN;
		if(NEW_LIST_FILTERED_PERFORMANCE.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_FILTERED_PERFORMANCE;
		if(NEW_LIST_FILTERED_SUVCUV.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_FILTERED_SUVCUV;
		if(NEW_LIST_FILTERED_LIGHTTRUCK.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_FILTERED_LIGHTTRUCK;
		if(NEW_LIST_FILTERED_WINTER.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_FILTERED_WINTER;
		return null;
	}
	
	public static String getNewTireTypeName(String name){
		if(NEW_LIST_FILTERED_CARMINIVAN.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_CARMINIVAN;
		if(NEW_LIST_FILTERED_PERFORMANCE.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_PERFORMANCE;
		if(NEW_LIST_FILTERED_SUVCUV.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_SUVCUV;
		if(NEW_LIST_FILTERED_LIGHTTRUCK.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_LIGHTTRUCK;
		if(NEW_LIST_FILTERED_WINTER.equalsIgnoreCase(StringUtils.nameFilter(name)))
			return NEW_LIST_WINTER;
		if("All-Season".equalsIgnoreCase(name)) 
				return "All-Season";
		if("Summer".equalsIgnoreCase(name))
			return "Summer";
		return "";
	}
	
	public static boolean isUserApprovedtoViewInventory(HttpServletRequest req){
		locateBeans(req);
		String userIP = req.getRemoteAddr();
		Boolean approvedUser = (Boolean)req.getSession().getAttribute(ACCENT_USER_IP);
		if(approvedUser != null)
			return approvedUser;
		
		AdminIpAddress ipAddress = ipAccessDAO.checkIpAddress(userIP);
		
		if(ipAddress != null && ACCENT_USER_IP.equalsIgnoreCase(ipAddress.getDescription())){
			req.getSession().setAttribute(ACCENT_USER_IP, true);
			return true;
		}else
			req.getSession().setAttribute(ACCENT_USER_IP, false);
		
		return false;
	}
	
	public static String getTireQuantityOnHand(HttpServletRequest req, Long storeNumber, Long articleNumber){
		locateBeans(req);
    	Map<Long,StoreInventory> inventoryMap = (Map<Long,StoreInventory>)req.getSession().getAttribute(INVENTORY_MAP +"_"+ storeNumber);
    	//check again whether user is approved to view inventory
    	Boolean approvedUser = (Boolean)req.getSession().getAttribute(ACCENT_USER_IP);
    	if(approvedUser != null && !approvedUser)
    		return "";
    	if(inventoryMap == null || inventoryMap.isEmpty()){
    		inventoryMap = storeInventoryService.getInventoryInStore(storeNumber);
    		req.getSession().setAttribute(INVENTORY_MAP +"_"+ storeNumber, inventoryMap);
    	}
    	
    	if(inventoryMap != null && !inventoryMap.isEmpty()){
    		if(inventoryMap.containsKey(articleNumber) && inventoryMap.get(articleNumber) != null){
    			return String.valueOf(inventoryMap.get(articleNumber).getQuantityOnHand());
    		}
    		else
    			return "0";
    	}
    	return "0";
    }
	
	public static String getTireQuantityOnOrder(HttpServletRequest req, Long storeNumber, Long articleNumber){
		locateBeans(req);
    	Map<Long,StoreInventory> inventoryMap = (Map<Long,StoreInventory>)req.getSession().getAttribute(INVENTORY_MAP +"_"+ storeNumber);
    	//check again whether user is approved to view inventory
    	Boolean approvedUser = (Boolean)req.getSession().getAttribute(ACCENT_USER_IP);
    	if(approvedUser != null && !approvedUser)
    		return "";
    	if(inventoryMap == null || inventoryMap.isEmpty()){
    		inventoryMap = storeInventoryService.getInventoryInStore(storeNumber);
    		req.getSession().setAttribute(INVENTORY_MAP +"_"+ storeNumber, inventoryMap);
    	}
    	
    	if(inventoryMap != null && !inventoryMap.isEmpty()){
    		if(inventoryMap.containsKey(articleNumber) && inventoryMap.get(articleNumber) != null){
    			return String.valueOf(inventoryMap.get(articleNumber).getQuantityOnOrder());
    		}
    		else
    			return "0";
    	}
    	return "0";
    }
	
}