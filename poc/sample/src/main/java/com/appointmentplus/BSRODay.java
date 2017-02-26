package com.appointmentplus;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.appointmentplus.model.Day;

public class BSRODay {
	public static void main(String[] args) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject json = new JSONObject();
		HttpEntity responseEntity = null;
		
		String responseBody="";
		
		String numberOfDays = "30";
		String startDate = "20170207";
		String employeeId = "12709";
		String locationId = "1581";
				
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		//String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String url = "https://sandbox-ws.appointment-plus.com/Staff/GetOpenDates";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
	    
		
		try {
			
			StringBuilder paramBuilder = new StringBuilder(); 
			paramBuilder.append(queryDeli).append("num_days").append(nameValuePairDeli).append(numberOfDays);
			paramBuilder.append(nameValueDeli).append("start_date").append(nameValuePairDeli).append(startDate);
			paramBuilder.append(nameValueDeli).append("c_id").append(nameValuePairDeli).append(locationId);
			paramBuilder.append(nameValueDeli).append("employee_id").append(nameValuePairDeli).append(employeeId);
			paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
			paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
			paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
			String finalUrl = url + paramBuilder.toString();
			
			HttpPost httppost = new HttpPost(finalUrl);
			
			RequestConfig requestConfig = RequestConfig.custom()
				       //.setSocketTimeout(5000)
				       .setConnectTimeout(10000)
				       //.setConnectionRequestTimeout(5000)
				       .build();
			
			httppost.setConfig(requestConfig);
			
			HttpResponse response = httpClient.execute(httppost); // Execute Method
			int statusCode = response.getStatusLine().getStatusCode();
	 
			if (statusCode != HttpStatus.SC_OK) {
				throw new IllegalStateException("Method failed: " + response.getStatusLine());
			}
	 
	       	responseEntity = response.getEntity();
			
			if(responseEntity != null) {
			    responseBody = EntityUtils.toString(responseEntity);
			    json = new JSONObject(responseBody);
				if(json.getString("data") != null) {
					responseBody = json.get("data").toString();
				}
				System.out.println("responseBody:" + responseBody);
				//day = objectMapper.readValue(responseBody, Day.class);
				Day myObjects[] = objectMapper.readValue(responseBody, Day[].class);
				//System.out.println(myObjects);
				System.out.println("myObjects: " + objectMapper.writeValueAsString(myObjects));
			    //res = objectMapper.writeValueAsString(myObjects);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
