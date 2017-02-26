package com.bfrc.dataaccess.svc.geocode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import app.bsro.model.gas.stations.StationPrices;

import com.bfrc.dataaccess.http.HttpResponse;
import com.bfrc.dataaccess.http.HttpUtils;

@Service(value="googleGeocodeService")
@Deprecated
public class GoogleGeocodeServiceImpl implements GeocodeService {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	private static final String US_ZIP_PATTERN = "\\d{5}(-\\d{4})?";
	
	private static final String GOOGLE_GEOCODE_API_VERSION = "3";
	private static final String BSRO_GOOGLE_PRIVATE_KEY = "1DEeeAp-N64RwbEmH2DdJxKjcJI=";
	
    private static final String GOOGLE_GEOCODING_API_OK_STATUS = "OK"; // indicates that no errors occurred; the address was successfully parsed and at least one geocode was returned.
    private static final String GOOGLE_GEOCODING_API_ZERO_RESULTS_STATUS = "ZERO_RESULTS"; // indicates that the geocode was successful but returned no results. This may occur if the geocode was passed a non-existent address or a latlng in a remote location.
    private static final String GOOGLE_GEOCODING_API_OVER_QUERY_LIMIT_STATUS = "OVER_QUERY_LIMIT"; // indicates that you are over your quota.
    private static final String GOOGLE_GEOCODING_API_REQUEST_DENIED_STATUS = "REQUEST_DENIED"; // indicates that your request was denied, generally because of lack of a sensor parameter.
    private static final String GOOGLE_GEOCODING_API_INVALID_REQUEST_STATUS = "INVALID_REQUEST"; // generally indicates that the query (address or latlng) is missing.

    private static final String URL_ENCODED_COLON = "%3A";
    private static final String URL_ENCODED_PIPE = "%7C";
    
	private static final String NULL_TEXT = "null";
	
	public GeoLatLong geocode(GeoAddress geoAddress, Map<StationPrices.Params, Object> params) {

		if(geoAddress == null) return null;
		
		String urlEncodedAddress = geoAddress.toEncodedUrlString();
		
		if(urlEncodedAddress == null) return null;
		
		String nonEncodedAddress = geoAddress.getCompositeString();
		
		HttpResponse response = null;
		
		boolean isAddressAZipCode = false;
		
		if (nonEncodedAddress != null) {
			isAddressAZipCode = isValidZipCode(nonEncodedAddress);
		}
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://maps.googleapis.com/maps/api/geocode/json?address=");
		urlBuilder.append(urlEncodedAddress);
		urlBuilder.append("&components=country");
		urlBuilder.append(URL_ENCODED_COLON);
		urlBuilder.append("US");
		if (isAddressAZipCode) {
			// The old Google Geocoding API would not return results for an invalid zip code, such as "00000"
			// What this component filter does is tell the v3 API, "only return matches that ARE the given zip code"
			// It appears to handle both 5-digit and zip+4 digit zip codes gracefully, even though it only ever returns 5-digit zip codes
			urlBuilder.append(URL_ENCODED_PIPE);
			urlBuilder.append("postal_code");
			urlBuilder.append(URL_ENCODED_COLON);
			urlBuilder.append(urlEncodedAddress);
		}
		urlBuilder.append("&sensor=false&client=gme-bfrc");
		
		String url = urlBuilder.toString();
		String signedUrl = null;
		try {		
			signedUrl = signUrl(BSRO_GOOGLE_PRIVATE_KEY, url);
		}catch(Throwable throwable) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.severe("Failed to create and sign Google API URL: "+url);
			}
			throwable.printStackTrace();
			return null;
		}
		
		try {		
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Google Geocoding API (v"+GOOGLE_GEOCODE_API_VERSION+"): "+signedUrl);
			}
			response = HttpUtils.get(signedUrl, null);
		}catch(Throwable ex) {
			if (logger.isLoggable(Level.WARNING)) {
				String output = generateGetGoogleGeoDataLogOutput(ex, "Error getting response from google", url, signedUrl, null);
				logger.warning(output);
			}
			return null;
		}
		
		GeoLatLong loResult = null;
		
		try {
			loResult = parse(response.getResponseBody(), url, signedUrl);
		} catch (Throwable throwable) {
			if (logger.isLoggable(Level.WARNING)) {
				String output = generateGetGoogleGeoDataLogOutput(throwable, "Error parsing response", url, signedUrl, null);
				logger.warning(output);
			}
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
	private GeoLatLong parse(String response, String url, String signedUrl) throws JSONException {
		GeoLatLong geoLatLong = new GeoLatLong();
		String country="";
		String statusCode="";

		try {
			JSONObject json = new JSONObject(response);

			JSONArray results = null;
			JSONObject firstResult = null;
			JSONObject firstResultLocation = null;
			String lat = null;
			String lng = null;

			statusCode = (String) json.get("status");

			if(GOOGLE_GEOCODING_API_OK_STATUS.equalsIgnoreCase(statusCode)) {
				statusCode = "200";

				results = json.getJSONArray("results");
				if (results.length() > 0) {
					firstResult = results.getJSONObject(0);
					firstResultLocation = firstResult.getJSONObject("geometry").getJSONObject("location");
					lat = firstResultLocation.getString("lat");
					lng = firstResultLocation.getString("lng");

					JSONArray addressComponents = firstResult.getJSONArray("address_components"); 

					for(int i=0; i < addressComponents.length(); i++) {
						JSONObject addressComponent = addressComponents.getJSONObject(i);
						String addressComponentShortName = addressComponent.getString("short_name");
						if(addressComponentShortName.equals("US")) {
							country = addressComponentShortName;
						}
					}

					if("US".equals(country) && lat != null && lng != null && !lat.isEmpty() && !lng.isEmpty()) {
						geoLatLong.setLongitude(lng);
						geoLatLong.setLatitude(lat);
						geoLatLong.setCountryCode(StringUtils.trimToEmpty(country));

						return geoLatLong;
					} else {
						if (logger.isLoggable(Level.INFO)) {
							String output = generateGetGoogleGeoDataLogOutput(null, "Unable to find a US location, or missing location data", url, signedUrl, response);	
							logger.info(output);
						}
					}
				}

			} else if (!GOOGLE_GEOCODING_API_ZERO_RESULTS_STATUS.equalsIgnoreCase(statusCode)) {
				// If there are zero results, that's not a cause for alarm
				// Any other response should at least be logged, however
				if (logger.isLoggable(Level.WARNING)) {
					String output = generateGetGoogleGeoDataLogOutput(null, "Unexpected status", url, signedUrl, response);					
					logger.warning(output);
				}
			}				
		} catch (Throwable throwable) {
			if (logger.isLoggable(Level.WARNING)) {
				String output = generateGetGoogleGeoDataLogOutput(throwable, "Exception", url, signedUrl, response);					
				logger.warning(output);
			}
		}

		return null;
	}

	private String generateGetGoogleGeoDataLogOutput(Throwable throwable, String subject, String url, String signedUrl, String response) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n=========================== ");
		if (subject != null) {
			sb.append(subject).append(": ");
		}
		sb.append("GeocodeOperator(v");
		sb.append(GOOGLE_GEOCODE_API_VERSION);
		sb.append(").getGoogleGeoData:");
		sb.append("\n\trequestUrl:");
		sb.append(url == null ? NULL_TEXT : url);
		sb.append("\n\tsignedUrl:");
		sb.append(signedUrl == null ? NULL_TEXT : signedUrl);
		sb.append("\n");
		sb.append("\nresponse:\n");
		sb.append(response);
		sb.append("\n");
		if (throwable != null) {
			sb.append(getStackTraceAsString(throwable));
		}
		return sb.toString();
	}
	
	private static String signUrl(String keyString, String urlString) throws Exception {
		if(StringUtils.isBlank(urlString) || StringUtils.isBlank(keyString)) return null;
		else {
			if(urlString.indexOf("http://")<0 && urlString.indexOf("https://")<0)
				return null;
		}
		URL url = new URL(urlString);
		String path = url.getPath();
		String query = url.getQuery();
		String resource = path + '?' + query;
		byte[] key;
		keyString = keyString.replace('-', '+');
	    keyString = keyString.replace('_', '/');
	    
	    key = Base64.decodeBase64(keyString);

	    // Get an HMAC-SHA1 signing key from the raw key bytes
	    SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
	    // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(sha1Key);
	    // compute the binary signature for the request
	    byte[] sigBytes = mac.doFinal(resource.getBytes());		
	    // base 64 encode the binary signature
	    String signature = Base64.encodeBase64URLSafeString(sigBytes);
	    // convert the signature to 'web safe' base 64
	    signature = signature.replace('+', '-');
	    signature = signature.replace('/', '_');

	    return url.getProtocol() + "://" + url.getHost() + resource + "&signature=" + signature;
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
