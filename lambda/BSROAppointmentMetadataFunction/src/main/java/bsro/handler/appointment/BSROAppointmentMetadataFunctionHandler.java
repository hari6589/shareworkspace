package bsro.handler.appointment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

import bsro.appointment.model.Metadata;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

public class BSROAppointmentMetadataFunctionHandler implements RequestHandler<Object, Object> {
	
	public Object handleRequest(Object input, Context context) {
		
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		
		// Extract input parameters
		HashMap<String, String> params = (HashMap<String, String>) input;
		
		String storeNumberParam = params.get("storeNumber").toString();
		String servicesParam = params.get("services").toString();
		
		String url = "https://sandbox-api.appointment-plus.com/Bridgestone/Rules"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		try {
			// Prepare url with query parameters
			url = url + parameterBuilder(storeNumberParam, servicesParam);
			httpPost = new HttpPost(url);
			
			// Set request configuration
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000)
				       .setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
			httpPost.setConfig(requestConfig);
			
			// Method execution
			response = httpClient.execute(httpPost);
			
			// Process response
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				responseEntity = response.getEntity();				
				if(responseEntity != null) {
					String responseBody = "";
				    ObjectMapper objectMapper = new ObjectMapper();
				    JSONObject json = new JSONObject(EntityUtils.toString(responseEntity));
				    if(json.getString("data") != null) {
						responseBody = json.get("data").toString();
						Metadata result = objectMapper.readValue(responseBody, Metadata.class);
					    bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
					    bsroWebServiceResponse.setPayload(result);
					}
				}
			} else {
				context.getLogger().log("BSROAppointmentMetadataFunctionHandler - HTTP Status code is not 200");
				bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			}
			
			// close HttpClient connection
			httpClient.close();
			
		} catch (JSONException e) {
			context.getLogger().log("BSROAppointmentMetadataFunctionHandler - JSONException Occured while fetching Appointment Metadata : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("JSONException : " + e.getMessage());
		} catch (ClientProtocolException e) {
			context.getLogger().log("BSROAppointmentMetadataFunctionHandler - ClientProtocolException Occured while fetching Appointment Metadata : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("ClientProtocolException : " + e.getMessage());
		} catch (ConnectTimeoutException e) {
			context.getLogger().log("BSROAppointmentMetadataFunctionHandler - ConnectTimeoutException Occured while fetching Appointment Metadata : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("ConnectTimeoutException : " + e.getMessage());
		} catch (Exception e) {
			context.getLogger().log("BSROAppointmentMetadataFunctionHandler - Exception Occured while fetching Appointment Metadata : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("Exception : " + e.getMessage());
		}
		return bsroWebServiceResponse;
	}
	
	public String parameterBuilder(String storeNumber, String services) throws UnsupportedEncodingException {
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		//String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		String paddedStoreNumber = ("000000" + storeNumber);
		StringBuilder paramBuilder = new StringBuilder();
		
		paramBuilder.append(queryDeli).append("store_number").append(nameValuePairDeli).append(paddedStoreNumber.substring(paddedStoreNumber.length()-6));
		paramBuilder.append(nameValueDeli).append("services").append(nameValuePairDeli).append(services);
		paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
		paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
		paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
		
		return paramBuilder.toString();
	}
}
