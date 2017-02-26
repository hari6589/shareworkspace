package com.bsro.oil.util;

public interface OilDataConstants {

	
	// Check if cache needs refreshing
	public static boolean refreshCache	= false;
	public static boolean cacheEnabled	= false;
	
	//switch over to the new OATS API
	public static boolean useNewAPI = true;//TODO:should be configured through a property file.

	// -- this might already be defined somewhere, so use that instead and 
	// remove this
	public static final String SECTOR_CARS	= "cars";
	public static final String LOF_WEBSERVICE_URL = "http://firestone.api.earlweb.com";
	
	public static final String LOF_WEBSERVICE_URL_NEW ="http://firestone.phoenix.earlweb.net";
	public static final String BROWSE = "/browse";
	
	public static final String BROWSE_BY_TYPE = "/browse/retail/";
	
	public static final String VEHICLES = "/vehicles/";
	public static final String EQUIPMENT = "/equipment/";
	
	public static final String PATH_SEPARATOR ="/";
	
	public static final String REGION_USA ="_(us)";
	
	public static final String YEARS_EXPRESSION = "//index[@type='year']/item";
	public static final String MAKE_EXPRESSION = "//index[@type='manufacturer']/item";
	public static final String  MODEL_EXPRESSION = "/response/vehicles/vehicle[yearfrom/text()=";
	public static final String  MODEL_EXPRESSION_NEW = "/response/index/equipment";
	
	public static final String  PRODUCT_EXPRESSION = "/response/vehicle/application[application='Engine']/product";
	public static final String  CAPACITY_EXPRESSION = "/response/vehicle/application[application='Engine']/capacityto";
	public static final String  ENGINE_EXPRESSION = "/response/equipment/application[name='Engine']";
	public static final String  PRODUCT_EXPRESSION_NEW = "/response/equipment/application[name='Engine']/product";
	public static final String  CAPACITY_UNIT_EXPRESSION = "/response/equipment/capacity_unit/text()";
	
	public static final String PRODUCT = "product";
	public static final String TIER_ORDER = "tierorder";
	public static final String TIER = "tier";
	public static final String  CAPACITY_US_GALLONS = "us-gallons";
	public static final String  DISPLAY_CAPACITY_NODE = "display_capacity";
	
	public static final String  MODEL_GUID = "guid";
	public static final String  MODEL = "model";
	
	public static final String  MODEL_HREF = "href";
	
	public static final String OPEN_PARENTHESIS = "(";
	public static final String CLOSE_PARENTHESIS = ")";

}
