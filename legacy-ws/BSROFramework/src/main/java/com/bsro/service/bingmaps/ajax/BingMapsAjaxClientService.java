/**
 * 
 */
package com.bsro.service.bingmaps.ajax;

/**
 * @author schowdhury
 *
 */
public interface BingMapsAjaxClientService {
	
	public static final String	SPRING_KEY	= "bing.maps.ajax";
	
	/**
	 * Main public interface with the service. This returns the fully built URL to call Bing maps API.
	 * The service does cache the value and regenerates if any of the fields have been updated.
	 * 
	 * @return String value of the URL to call Bing maps API
	 */
	public String getEmbedScript();

}
