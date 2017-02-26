package com.bsro.oil.util;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





/**
 * Utility class to form URLs.  While these are trivial, the separation
 * allows for simplifying changes in the future 
 * 
 * @author Ramana
 *
 */
public class OilURLUtility implements OilDataConstants {

	
	
	private static final Log logger = LogFactory.getLog(OilURLUtility.class);
	
	
	/**
	 * URL to get all years for a given sector.  Currently, we are handling
	 * only the Cars portion, but using the sector it can be expanded
	 * 
	 * @param sector
	 * @return
	 */
	public static String getYearsUrl(String sectorId)
	{
		String url = "";
		if(useNewAPI){
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL_NEW;
			url = lofBaseWSURL + BROWSE;
		}else{
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL;
			url = lofBaseWSURL + BROWSE_BY_TYPE + sectorId;
		}
		
		if(logger.isDebugEnabled()){ logger.info("URL for retreiving Years: " + url); }
		
		return url;
		
	}
	
	/**
	 * URL to retrieve all "Make"s for a given year
	 * 
	 * @param sectorId
	 * @param yearId
	 * @return
	 */
	public static String getMakesUrl(String sectorId, String yearId)
	{
		String url = "";
		if(useNewAPI){
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL_NEW;
			url = lofBaseWSURL + BROWSE + PATH_SEPARATOR + yearId;
		}else{
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL;
			url = lofBaseWSURL + BROWSE_BY_TYPE + sectorId + PATH_SEPARATOR + yearId;
		}
		
		if(logger.isDebugEnabled()){ logger.info("URL for retreiving Manufacturer : " + url); }
		
		return url;
	}
	
	/**
	 * URL to get all the models for a given sector and a given year and Make
	 * @param sectorId
	 * @param yearId
	 * @param makeId
	 * @return
	 */
	public static String getModelsUrl(String sectorId, String yearId, String makeId)
	{
		String url = "";
		if(useNewAPI){
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL_NEW;
			url = lofBaseWSURL + BROWSE + PATH_SEPARATOR + yearId + PATH_SEPARATOR + makeId.toLowerCase();
		}else{
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL;
			url = lofBaseWSURL + BROWSE_BY_TYPE + sectorId + PATH_SEPARATOR + yearId + PATH_SEPARATOR + makeId.toLowerCase() + REGION_USA ;
		}
		if(logger.isDebugEnabled()){ logger.info("URL for retreiving Models : " + url); }
		
		return url;
	}
	
	/**
	 * URL to get all the products for a given vehicle guid
	 * @param guid
	*/
	
	public static String getProductsUrl(String guid)
	{
		String url = "";
		if(useNewAPI){
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL_NEW;
			url = lofBaseWSURL + EQUIPMENT + guid;

		}else{
			String lofBaseWSURL = OilDataConstants.LOF_WEBSERVICE_URL;
			url = lofBaseWSURL + VEHICLES + guid;
		}
		
		if(logger.isDebugEnabled()){ logger.info("URL for retreiving Years: " + url); }		
		return url;
		
	}

	
}
