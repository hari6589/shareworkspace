package com.bsro.service.gas;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import app.bsro.model.gas.stations.StationPrice;

import com.bfrc.UserSessionData;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.UserProfileUtils;

/**
 * This should be moved to framework asap. It's here for the moment in case we need to push this out quickly in a patch not involving the framework.
 * 
 * @author mholmes
 *
 */
public class GasUtils {
	/**
	 * Just the ones relevant to "get directions"
	 */
	private static Map<String, String> oldWebServiceToNewWebServiceDirectionsFieldsMapper = new HashMap<String, String>();

	static {
		oldWebServiceToNewWebServiceDirectionsFieldsMapper.put("Brand_Name", "brandName");
		oldWebServiceToNewWebServiceDirectionsFieldsMapper.put("Address1", "address1");
		oldWebServiceToNewWebServiceDirectionsFieldsMapper.put("City", "city");
		oldWebServiceToNewWebServiceDirectionsFieldsMapper.put("State", "state");
		oldWebServiceToNewWebServiceDirectionsFieldsMapper.put("Zip", "zip");
		oldWebServiceToNewWebServiceDirectionsFieldsMapper.put("Phone", "phone");
	}


	/**
	 * Since we changed webservices, URLs with the old Base64-encoded JSON string don't work. So, we handle those here by parsing out the necessary properties.
	 * 
	 * We do the same with the new webservice, so we won't have the same backward-compatibility problems in the future and won't be so bound to specific implementations.
	 * 
	 * This will detect and translate them to the extent necessary for the get directions page.
	 * 
	 * @param request
	 * @param storeJSONString
	 * @throws JSONException
	 */
	public static void initializeStationDataFromBase64EncodedJSONParameter (HttpServletRequest request, String storeJSONString) throws JSONException {
		JSONObject storeJSONObject = null;
		storeJSONString = StringUtils.base64Decode(storeJSONString);

		storeJSONObject = new JSONObject(storeJSONString);
		
        String[] keys = JSONObject.getNames(storeJSONObject);

	    // Translate from old keys to new keys
        for (String key : keys) {
        	if (oldWebServiceToNewWebServiceDirectionsFieldsMapper.containsKey(key)) {
        		storeJSONString = storeJSONString.replaceFirst("\""+key+"\":", "\""+oldWebServiceToNewWebServiceDirectionsFieldsMapper.get(key)+"\":");
        	}
        }
        // Refresh object with renamed keys
		storeJSONObject = new JSONObject(storeJSONString);
        
        //Pull out the necessary properties and store them
    	StationPrice station = new StationPrice();
    	
    	station.setBrandName(storeJSONObject.getString("brandName"));
    	station.setAddress1(storeJSONObject.getString("address1"));
    	station.setCity(storeJSONObject.getString("city"));
    	station.setState(storeJSONObject.getString("state"));
    	station.setZip(storeJSONObject.getString("zip"));
    	station.setPhone(storeJSONObject.getString("phone"));
    	
    	request.setAttribute("station", station);
	}
	
	/**
	 * Accepts just the parameters we need, not tied to a particular JSON implementation
	 * 
	 * @param request
	 * @param useCachedSessionData
	 */
	public static void initializeStationDataFromBasicParameters(HttpServletRequest request, boolean useCachedSessionData) {
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(request.getSession());
		
		StationPrice station = null;
		
		if (!useCachedSessionData) {	
			station = new StationPrice();
			station.setBrandName(request.getParameter("brandName"));
			station.setStationName(request.getParameter("stationName"));
			station.setAddress1(request.getParameter("stationAddress"));
			station.setCity(request.getParameter("city"));
			station.setState(request.getParameter("state"));
			station.setZip(request.getParameter("zip"));
			station.setPhone(request.getParameter("phone"));

			userSessionData.put("cache.print.gas.store", station);
		} else {
			station = (StationPrice)userSessionData.get("cache.print.gas.store");
			request.setAttribute("station", station);
		}
		
		request.setAttribute("station", station);
	}
}
