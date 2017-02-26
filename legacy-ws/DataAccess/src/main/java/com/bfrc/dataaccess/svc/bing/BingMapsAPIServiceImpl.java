/**
 * 
 */
package com.bfrc.dataaccess.svc.bing;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author schowdhu
 *
 */
public class BingMapsAPIServiceImpl implements BingMapsAPI {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	public static final String bingMapRoutesUri = "http://dev.virtualearth.net/REST/V1/Routes/Driving?o=json&amp;optmz=distance&amp;du=mi";
	//FCAC Geolocation 
	public static final String bingMapsKey = "AuANGjPKr0frVuERtSMaCrvNcP7cMUOZ4l2Qn1n_wrvoti1Sd7UpXuhS12v4xxVs";

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.bing.BingMapsAPI#getDistanceBetweenGeoLocations(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public BigDecimal getDistanceBetweenAddress(String fromAddress,
			String fromCity, String fromState, String fromZip,
			String toAddress, String toCity, String toState, String toZip) {
		
		StringBuilder fromLoc = new StringBuilder(fromAddress);
		if(fromAddress != null && !fromAddress.isEmpty())
			fromLoc.append(",");
		
		fromLoc.append(fromCity);
		if(fromCity != null && !fromCity.isEmpty())
			fromLoc.append(",");
		
		fromLoc.append(fromState);
		if(fromState != null && !fromState.isEmpty())
			fromLoc.append(",");
		
		fromLoc.append(fromZip);
		if(fromZip != null && !fromZip.isEmpty())
			fromLoc.append(",");
		
		StringBuilder toLoc = new StringBuilder(toAddress);
		if(toAddress != null && !toAddress.isEmpty())
			toLoc.append(",");
		
		toLoc.append(toCity);
		if(toCity != null && !toCity.isEmpty())
			toLoc.append(",");
		
		toLoc.append(toState);
		if(toState != null && !toState.isEmpty())
			toLoc.append(",");
		
		toLoc.append(toZip);
		
		StringBuilder url = new StringBuilder(bingMapRoutesUri);
		try {
			url.append("&wp.0=").append(java.net.URLEncoder.encode(fromLoc.toString(), "UTF-8"));
			url.append("&wp.1=").append(java.net.URLEncoder.encode(toLoc.toString(), "UTF-8"));
			url.append("&key=").append(bingMapsKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				
		String distance = callBingMapsAPI(url.toString());
		if(distance!=null && !distance.isEmpty())
			return new BigDecimal(distance);
		
		return null;
	}

	public BigDecimal getDistanceFromStore(
			String fromAddress, String fromCity, String fromState, String fromZip, 
			String storeLat, String storeLong){
		
		StringBuilder fromLoc = null;
		if(fromAddress != null && !fromAddress.isEmpty())
		{
			fromLoc = new StringBuilder(fromAddress);
			fromLoc.append(",");
		
		
		fromLoc.append(fromCity);
		if(fromCity != null && !fromCity.isEmpty())
			fromLoc.append(",");
		
		fromLoc.append(fromState);
		if(fromState != null && !fromState.isEmpty())
			fromLoc.append(",");
		
		fromLoc.append(fromZip);
		
		StringBuilder url = new StringBuilder(bingMapRoutesUri);
		try {
			url.append("&wp.0=").append(java.net.URLEncoder.encode(fromLoc.toString(), "UTF-8"));
			url.append("&wp.1=").append(storeLat).append(",").append(storeLong);
			url.append("&key=").append(bingMapsKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String distance = callBingMapsAPI(url.toString());
		if(distance!=null && !distance.isEmpty())
			return new BigDecimal(distance);
		}
		return null;
		
	}
	
	private String callBingMapsAPI(String url){
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		HttpResponse httpResponse = null;
		HttpEntity responseEntity = null;
		String responseBody = "";
		String distance = "";
		try {
			httpClient = new DefaultHttpClient();
			httpGet = new HttpGet(url.toString());

			httpResponse = httpClient.execute(httpGet);
			responseEntity = httpResponse.getEntity();
			responseBody = EntityUtils.toString(responseEntity);
			httpClient.getConnectionManager().shutdown();
			logger.info(responseBody);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			httpClient.getConnectionManager().shutdown();
		} catch (ParseException e) {
			e.printStackTrace();
			httpClient.getConnectionManager().shutdown();
		} catch (IOException e) {
			e.printStackTrace();
			httpClient.getConnectionManager().shutdown();
		}
		JSONObject jsonRootObj = null;
		
		try {
			//parse through the JSON and retrieve distance
			jsonRootObj = new JSONObject(responseBody);
			if(jsonRootObj != null && jsonRootObj.getJSONArray("resourceSets") != null){
				JSONObject resourceSetObj = jsonRootObj.getJSONArray("resourceSets").getJSONObject(0);
				if(resourceSetObj != null && resourceSetObj.getJSONArray("resources")!=null){
					JSONObject resourceObj = resourceSetObj.getJSONArray("resources").getJSONObject(0);
					if(resourceObj != null){
						//System.out.println("resourceObj====>"+resourceObj.toString(1));
						distance = resourceObj.getString("travelDistance");
					}
				}
			}
		} catch (ParseException e) {
			System.err.println("ParseException is thrown in method: callBingMapsAPI");
			e.printStackTrace();
		} catch (JSONException e) {
			System.err.println("JSONException is thrown in method: callBingMapsAPI");
			e.printStackTrace();
		} 
		
		logger.info("distance=="+distance);
		return distance;
	}
}
