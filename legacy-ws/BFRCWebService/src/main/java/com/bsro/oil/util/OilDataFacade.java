package com.bsro.oil.util;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




/**
 * This is a FACADE class that decouples data from the User Interface
 * This class is intended to provide appropriate data to the UI, either through
 * CACHE or directly getting it from the Web Service provided by OATS
 * 
 * Currently, Cache is not implemented, but it can be added by simply changing this
 * class and without any changes to UI
 * 
 * @author Ramana
 *
 */

public class OilDataFacade implements OilDataConstants {

	private static final Log logger = LogFactory.getLog(OilDataFacade.class);
	
	public static String sectorId = SECTOR_CARS;

	
	/**
	 * Returns all years
	 * @return
	 */
	public static LinkedHashMap<String, String> getYears(String sectorId) throws IOException
	{
		LinkedHashMap<String, String> yearsList = new LinkedHashMap<String, String>();
		
		// -- Not implemented currently
		if (cacheEnabled)
		{
			// Get data from Cache.
			if (refreshCache)
			{
				// -- cache is enabled, but needs a refresh.  Invoke refresh and then get data
			}
			
			// --- get data from cache
		}
		
		// --- This means cache is not enabled or not available in the cache
		
		yearsList = formYears(sectorId);
	
		// --- Now add it to cache and return
		
		if (logger.isDebugEnabled()) {
			Iterator it = yearsList.entrySet().iterator();			
			while(it.hasNext()) {				
				Map.Entry<String,String> mapEntry  = (Map.Entry)it.next();				
				logger.debug("KEY--->" + mapEntry.getKey() + " VALUE--->" + mapEntry.getValue());			
			}
		}
		
		return yearsList;
	}

	/**
	 * Returns all years
	 * @return
	 */
	public static String getCapacityForVehicle(String guid) throws IOException
	{
		String engineCapacity = null;
		
		// -- Not implemented currently
		if (cacheEnabled)
		{
			// Get data from Cache.
			if (refreshCache)
			{
				// -- cache is enabled, but needs a refresh.  Invoke refresh and then get data
			}
			
			// --- get data from cache
		}
		
		// --- This means cache is not enabled or not available in the cache
		
		engineCapacity = formCapacityForVehicle(guid);
	
		// --- Now add it to cache and return
		
		
		return engineCapacity;
	}
	
	/**
	 * Returns all Makes for a given year
	 * @return
	 */
	public static LinkedHashMap<String, String> getMakes(String sectorId, String yearId) throws IOException
	{
		LinkedHashMap<String, String> makesList = new LinkedHashMap<String, String>();
		
		// -- Not implemented currently
		if (cacheEnabled)
		{
			// Get data from Cache.
			if (refreshCache)
			{
				// -- cache is enabled, but needs a refresh.  Invoke refresh and then get data
			}
			
			// --- get data from cache
		}
		
		// --- This means cache is not enabled or not available in the cache
		
		makesList = formMakes(sectorId, yearId);
		
		if (logger.isDebugEnabled()) {
			Iterator it = makesList.entrySet().iterator();			
			while(it.hasNext()) {				
				Map.Entry<String,String> mapEntry  = (Map.Entry)it.next();				
				logger.debug("KEY--->" + mapEntry.getKey() + " VALUE--->" + mapEntry.getValue());			
			}
		}

	
		// --- Now add it to cache and return
		
		return makesList;
		
		
	}

	
	/**
	 * Returns all models for a given year and make
	 * @return
	 */

	public static LinkedHashMap<String, String> getModels(String sectorId, String yearId, String makeId) throws IOException
	{
		LinkedHashMap<String, String> modelList = new LinkedHashMap<String, String>();
		
		// -- Not implemented currently
		if (cacheEnabled)
		{
			// Get data from Cache.
			if (refreshCache)
			{
				// -- cache is enabled, but needs a refresh.  Invoke refresh and then get data
			}
			
			// --- get data from cache
		}
		
		// --- This means cache is not enabled or not available in the cache
		
		modelList = formModels(sectorId, yearId, makeId);
	
		// --- Now add it to cache or have a thread kicked off to refresh the cache so that it is up to date
		
		if (logger.isDebugEnabled()) {
			Iterator it = modelList.entrySet().iterator();			
			while(it.hasNext()) {				
				Map.Entry<String,String> mapEntry  = (Map.Entry)it.next();				
				logger.debug("KEY--->" + mapEntry.getKey() + " VALUE--->" + mapEntry.getValue());			
			}
		}

		
		return modelList;
	}

	/**
	 * Returns all products for a given vehicle
	 * @return
	 */
	public static LinkedHashMap<String, String> getProducts(String guid) throws IOException
	{
		LinkedHashMap<String, String> productList = new LinkedHashMap<String, String>();
		
		// -- Not implemented currently
		if (cacheEnabled)
		{
			// Get data from Cache.
			if (refreshCache)
			{
				// -- cache is enabled, but needs a refresh.  Invoke refresh and then get data
			}
			
			// --- get data from cache
		}
		
		// --- This means cache is not enabled or not available in the cache
		
		productList = formProducts(guid);
		
	
		// --- Now add it to cache and return
		if (logger.isDebugEnabled()) {
			Iterator it = productList.entrySet().iterator();			
			while(it.hasNext()) {				
				Map.Entry<String,String> mapEntry  = (Map.Entry)it.next();				
				logger.debug("KEY--->" + mapEntry.getKey() + " VALUE--->" + mapEntry.getValue());			
			}
		}

		
		return productList;
	}
	
	// --- Methods to fetch data from web services and form 
	
	/**
	 * Forms a fresh data for years
	 *  
	 * @param sector
	 * @return
	 */
	public static LinkedHashMap<String, String> formYears(String sectorId)  throws IOException
	{
		LinkedHashMap<String, String> yearsList = new LinkedHashMap<String, String>();
		
		String url = OilURLUtility.getYearsUrl(sectorId);
		String xmlResponse = OilUtility.getWebServiceResponse(url);
		yearsList = OilDataParser.parseYears(xmlResponse);		
		
		return yearsList;
	}

	public static LinkedHashMap<String, String> formMakes(String sectorId, String yearId) throws IOException
	{
		LinkedHashMap<String, String> makesList = new LinkedHashMap<String, String>();
		
		String url = OilURLUtility.getMakesUrl(sectorId,yearId);
		String xmlResponse = OilUtility.getWebServiceResponse(url);
		makesList = OilDataParser.parseMakes(xmlResponse);		
		
		return makesList;
	}

	public static LinkedHashMap<String, String> formModels(String sectorId, String yearId, String makeId) throws IOException
	{
		LinkedHashMap<String, String> modelList = new LinkedHashMap<String, String>();
		
		String url = OilURLUtility.getModelsUrl(sectorId,yearId, makeId);
		String xmlResponse = OilUtility.getWebServiceResponse(url);
		modelList = OilDataParser.parseModels(xmlResponse,yearId);		
		
		return modelList;
	}

	public static LinkedHashMap<String, String> formProducts(String guid) throws IOException
	{
		LinkedHashMap<String, String> productList = new LinkedHashMap<String, String>();
		
		String url = OilURLUtility.getProductsUrl(guid);
		String xmlResponse = OilUtility.getWebServiceResponse(url);
		productList = OilDataParser.parseProducts(xmlResponse);	
		
		return productList;
	}
	

	public static String formCapacityForVehicle(String guid) throws IOException
	{
		String engineCapacity = null;
		
		String url = OilURLUtility.getProductsUrl(guid);
		String xmlResponse = OilUtility.getWebServiceResponse(url);
		engineCapacity = OilDataParser.parseCapacityForVehicle(xmlResponse);		
		return engineCapacity;
	}
	
	/**
	 * If cache is enabled but requires a refresh, this method is called to 
	 * refresh the entire data
	 * 
	 */
	public static void refreshCache()
	{
		
	}
	
}
