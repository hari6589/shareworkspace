/**
 * 
 */
package com.bfrc.dataaccess.svc.geocode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import app.bsro.model.gas.stations.StationPrices;

import com.bfrc.dataaccess.http.HttpResponse;
import com.bfrc.dataaccess.http.HttpUtils;

/**
 * @author schowdhury
 *
 */
@Service(value="bingRestGeocodeService")
public class BingRestGeocodeServiceImpl implements GeocodeService {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	private static final String US_ZIP_PATTERN = "\\d{5}(-\\d{4})?";
	
	//TODO: needs to be set from a property file
	private String bingmapsApiKey = "AuANGjPKr0frVuERtSMaCrvNcP7cMUOZ4l2Qn1n_wrvoti1Sd7UpXuhS12v4xxVs";
	private String bingmapsGeoLocationApiUrl = "http://dev.virtualearth.net/REST/v1/Locations";
	private String bingmapsGeoLocationOutputType = "json";
	private String bingmapsGeoLocationApiVersion = "1";
	
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_OK 		= "200";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_BAD 		= "400";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_NOTFOUND = "404";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_UNAUTHORIZED = "401";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_VALID = "ValidCredentials";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_INVALID = "InvalidCredentials";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_NONE = "NoCredentials";
	
	private static final String NULL_TEXT = "null";

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.geocode.GeocodeService#geocode(com.bfrc.dataaccess.svc.geocode.GeoAddress, java.util.Map)
	 */
	public GeoLatLong geocode(GeoAddress geoAddress,Map<StationPrices.Params, Object> params) {
		if(geoAddress == null) return null;
			
		GeoLatLong loResult = null;
		
		try {
			String urlEncodedAddress = geoAddress.toEncodedUrlString();
			
			if(urlEncodedAddress == null) return null;
			
			String nonEncodedAddress = geoAddress.getCompositeString();
			
			HttpResponse response = null;
			
			boolean isAddressAZipCode = false;
			
			if (nonEncodedAddress != null) {
				isAddressAZipCode = isValidZipCode(nonEncodedAddress);
			}
			
			String formattedAddress = "";
			if(geoAddress.getAddress() != null && !geoAddress.getAddress().isEmpty()){
				formattedAddress += geoAddress.getAddress();
			}
			if(geoAddress.getCity() != null && !geoAddress.getCity().isEmpty()){
				if(formattedAddress.length()>0)
					formattedAddress += ",";
				formattedAddress += geoAddress.getCity();
			}
			if(geoAddress.getState() != null && !geoAddress.getState().isEmpty()){
				if(formattedAddress.length()>0)
					formattedAddress += ",";
				formattedAddress += ","+geoAddress.getState();
			}
			if(geoAddress.getZip() != null && !geoAddress.getZip().isEmpty()){
				if(formattedAddress.length()>0)
					formattedAddress += ",";
				formattedAddress += ","+geoAddress.getZip();
			}
			
			//TODO: we may need to use different url if we always use zip code here
			//      need to create different work item and handle this
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(bingmapsGeoLocationApiUrl+"?q=");
			urlBuilder.append(java.net.URLEncoder.encode(formattedAddress, "UTF-8"));
			urlBuilder.append("&o="+java.net.URLEncoder.encode(bingmapsGeoLocationOutputType, "UTF-8"));
			urlBuilder.append("&key="+bingmapsApiKey);
			
			
			String url = urlBuilder.toString();
			
			try {		
				if (logger.isLoggable(Level.INFO)) {
					logger.info("Bing Geolocation API (v"+bingmapsGeoLocationApiVersion+"): "+url);
				}
				response = HttpUtils.get(url, null);
			}catch(Throwable ex) {
				if (logger.isLoggable(Level.WARNING)) {
					String output = generateBingLocationGeoDataLogOutput(ex, "Error getting response from google", url, null);
					logger.warning(output);
				}
				return null;
			}

			
			try {
				loResult = parse(response.getResponseBody(), url);
			} catch (Throwable throwable) {
				if (logger.isLoggable(Level.WARNING)) {
					String output = generateBingLocationGeoDataLogOutput(throwable, "Error parsing response", url,  null);
					logger.warning(output);
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return loResult;
	}
	
	
	/**
	 * Parse the JSON coming back from Google into the Latitude and Longitude along with the countryCode.
	 * 
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	private GeoLatLong parse(String response, String url) throws JSONException {
		GeoLatLong geoLatLong = new GeoLatLong();
		String country="";
		String statusCode="";

		try {
			JSONObject json = new JSONObject(response);

			JSONArray resourceSets = null;
			JSONObject resource = null;
			JSONArray geocodePoints = null;
			String lat = null;
			String lng = null;

			statusCode = String.valueOf(json.getInt("statusCode"));

			String authResultsCode = json.getString("authenticationResultCode");
			
			if(BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_VALID.equalsIgnoreCase(authResultsCode)){

				if(BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_OK.equalsIgnoreCase(statusCode)) {

					resourceSets = json.getJSONArray("resourceSets");
					if (resourceSets.length() > 0 && resourceSets.getJSONObject(0).getJSONArray("resources").length() > 0) {

						resource = resourceSets.getJSONObject(0).getJSONArray("resources").getJSONObject(0);
						if(resource != null && resource.getJSONArray("geocodePoints").length() > 0){
							geocodePoints = resource.getJSONArray("geocodePoints").getJSONObject(0).getJSONArray("coordinates");
							lat = geocodePoints.getString(0);
							lng = geocodePoints.getString(1);
						}
						
						JSONObject addressJSON = resource.getJSONObject("address"); 
						country = addressJSON.getString("countryRegion");

						if("United States".equals(country) && lat != null && lng != null) {
							geoLatLong.setLongitude(lng);
							geoLatLong.setLatitude(lat);
							geoLatLong.setCountryCode("US");
							
							return geoLatLong;
						} else {
							if (logger.isLoggable(Level.INFO)) {
								String output = generateBingLocationGeoDataLogOutput(null, "Unable to find a US location, or missing location data", url, response);	
								logger.info(output);
							}
						}
					}
				}else {					
					if (BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_UNAUTHORIZED.equalsIgnoreCase(statusCode)) {
						// status code - 401
						if (logger.isLoggable(Level.WARNING)) {
							String output = generateBingLocationGeoDataLogOutput(null, "Uuathorized Url", url, response);					
							logger.warning(output);
						}
					}else if(BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_BAD.equalsIgnoreCase(statusCode)) {
						// status code - 404
						if (logger.isLoggable(Level.WARNING)) {
							String output = generateBingLocationGeoDataLogOutput(null, "Bad Request - check again", url, response);					
							logger.warning(output);
						}
					}else if(BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_NOTFOUND.equalsIgnoreCase(statusCode)) {
						// status code - 404
						if (logger.isLoggable(Level.WARNING)) {
							String output = generateBingLocationGeoDataLogOutput(null, "No Results Found", url, response);					
							logger.warning(output);
						}				
					}
				}
			}else{
				if(BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_NONE.equalsIgnoreCase(authResultsCode)){
					//missing credentials
					if (logger.isLoggable(Level.WARNING)) {
						String output = generateBingLocationGeoDataLogOutput(null, "No Bing maps key entered", url, response);					
						logger.warning(output);
					}	
				}
				else if(BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_INVALID.equalsIgnoreCase(authResultsCode)){
					// invalid credentials
					if (logger.isLoggable(Level.WARNING)) {
						String output = generateBingLocationGeoDataLogOutput(null, "Invalid Bing maps key entered", url, response);					
						logger.warning(output);
					}	
				}else{
					//other issues with credentials
					if (logger.isLoggable(Level.WARNING)) {
						String output = generateBingLocationGeoDataLogOutput(null, "Issue with Bing maps credentials", url, response);					
						logger.warning(output);
					}	
				}
			}	
				
		} catch (Throwable throwable) {
			if (logger.isLoggable(Level.WARNING)) {
				String output = generateBingLocationGeoDataLogOutput(throwable, "Exception", url, response);					
				logger.warning(output);
			}
		}

		return null;
	}
	
	private String generateBingLocationGeoDataLogOutput(Throwable throwable, String subject, String url, String response) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n=========================== ");
		if (subject != null) {
			sb.append(subject).append(": ");
		}
		sb.append("GeocodeOperator(v");
		sb.append(bingmapsGeoLocationApiVersion);
		sb.append(").getGoogleGeoData:");
		sb.append("\n\trequestUrl:");
		sb.append(url == null ? NULL_TEXT : url);
		sb.append("\n");
		sb.append("\nresponse:\n");
		sb.append(response);
		sb.append("\n");
		if (throwable != null) {
			sb.append(getStackTraceAsString(throwable));
		}
		return sb.toString();
	}

	private String getStackTraceAsString(Throwable throwable) {
	    StringWriter stackTrace = new StringWriter();
	    throwable.printStackTrace(new PrintWriter(stackTrace));
	    return stackTrace.toString();
	}
	
   private boolean isValidZipCode(String str) {
	   if(str == null) return false;
       return str.matches(US_ZIP_PATTERN);
   }
}
