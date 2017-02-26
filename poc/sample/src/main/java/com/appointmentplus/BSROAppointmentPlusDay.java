package com.appointmentplus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
//
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.appointmentplus.model.Day;

public class BSROAppointmentPlusDay {

	public static void main(String[] args) {
		
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		ObjectMapper objectMapper = new ObjectMapper();
		Day day = null;
		Object res = null;
		JSONObject json = new JSONObject();
		HttpEntity responseEntity = null;
		
		String responseBody="";
		String numberOfDays = "30";
		String startDate = "20170207";
		String employeeId = "12709";
		String locationId = "1581";
				
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		
		//String url = "https://sandbox-ws.appointment-plus.com/Bridgestone/Rules?store_number=011940&services=Tire%20Replacement&site_id=appointplus846/776&api_key=b18371f34b0931963f62add253820169cfa05cf7&response_type=JSON";
		String url = "https://sandbox-ws.appointment-plus.com/Staff/GetOpenDates";
		
		try {
			
			httpPost = new HttpPost(url);
			
			final HttpParams httpParams = new BasicHttpParams();
			httpClient = new DefaultHttpClient(httpParams);
			
			StringBuilder paramBuilder = new StringBuilder(); 
			paramBuilder.append(queryDeli).append("num_days").append(nameValuePairDeli).append(numberOfDays);
			paramBuilder.append(nameValueDeli).append("start_date").append(nameValuePairDeli).append(startDate);
			paramBuilder.append(nameValueDeli).append("c_id").append(nameValuePairDeli).append(locationId);
			paramBuilder.append(nameValueDeli).append("employee_id").append(nameValuePairDeli).append(employeeId);
			paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
			paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
			paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
			
			String finalUrl = httpPost.getURI().toString() + paramBuilder.toString();
			System.out.println("finalUrl : " + finalUrl);
			
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			
			HttpResponse response;
			response = httpClient.execute(httpPost);
			responseEntity = response.getEntity();
			
			try {
				if(responseEntity != null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    json = new JSONObject(responseBody);
					if(json.getString("data") != null) {
						responseBody = json.get("data").toString();
					}
					System.out.println("responseBody:" + responseBody);
					//day = objectMapper.readValue(responseBody, Day.class);
					Day myObjects[] = objectMapper.readValue(responseBody, Day[].class);
					System.out.println("myObjects: " + objectMapper.writeValueAsString(myObjects));
				    res = objectMapper.writeValueAsString(myObjects);
					httpClient.getConnectionManager().shutdown();
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API : " + e);
				e.printStackTrace();
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//System.out.println("res: " + res);
	}
	
}
