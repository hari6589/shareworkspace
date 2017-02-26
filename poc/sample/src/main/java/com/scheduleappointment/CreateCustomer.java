package com.scheduleappointment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appointmentplus.model.Day;

public class CreateCustomer {
	public static void main(String[] args) {
		
		String locationId = "1581";
		String firstName = "ATest";
		String lastName = "Atest";
		String emailAddress = "atest@agmail.com";
		String dayTimePhone = "9566765497";
		
		String url = "https://sandbox-ws.appointment-plus.com/Customers/CreateCustomers"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		try {
			// Prepare url with query parameters
			url = url + parameterBuilder(locationId, firstName, lastName, dayTimePhone, emailAddress);
			httpPost = new HttpPost(url);
			
			// Set request configuration
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000)
				       .setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
			httpPost.setConfig(requestConfig);
			
			// Method execution
			response = httpClient.execute(httpPost);
			
			System.out.println("URL: " + url);
			
			// Process response
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				responseEntity = response.getEntity();				
				if(responseEntity != null) {
					String responseBody="";
					ObjectMapper objectMapper = new ObjectMapper();
					responseBody = EntityUtils.toString(responseEntity);
					System.out.println("ResponseBody : " + responseBody);
				    JSONObject json = new JSONObject(responseBody);
					if(json.getString("data") != null) {
						responseBody = json.get("data").toString();
					}
					json = new JSONObject(responseBody);
					json.get("customer_id");
					System.out.println("Customer Id :" + json.get("customer_id"));
					}
			} else {
				System.out.println("Business Service Error");
			}
			
			// close HttpClient connection
			httpClient.close();
//			
//		} catch (JSONException e) {
//			System.out.println("JSONException");
//			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException");
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			System.out.println("ConnectTimeoutException");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unknown Exception");
			e.printStackTrace();
		}

	}

	public static String parameterBuilder(String locationId, String firstName, String lastName, String dayTimePhone, String emailAddress) throws UnsupportedEncodingException {
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		//String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		StringBuilder paramBuilder = new StringBuilder();
		
		paramBuilder.append(queryDeli).append("c_id").append(nameValuePairDeli).append(locationId);
		paramBuilder.append(nameValueDeli).append("first_name").append(nameValuePairDeli).append(firstName);
		paramBuilder.append(nameValueDeli).append("last_name").append(nameValuePairDeli).append(lastName);
		paramBuilder.append(nameValueDeli).append("email").append(nameValuePairDeli).append(emailAddress);
		paramBuilder.append(nameValueDeli).append("day_phone").append(nameValuePairDeli).append(dayTimePhone);
		paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
		paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
		paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
		
		return paramBuilder.toString();
	}
}
