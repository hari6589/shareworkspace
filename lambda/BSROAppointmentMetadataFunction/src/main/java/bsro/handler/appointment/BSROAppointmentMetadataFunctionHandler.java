package bsro.handler.appointment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Properties;

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
import bsro.webservice.error.Errors;

public class BSROAppointmentMetadataFunctionHandler implements RequestHandler<Object, Object> {
	
	public Object handleRequest(Object input, Context context) {
		
		// Extract input parameters
		HashMap<String, String> params = (HashMap<String, String>) input;
		
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		
		String storeNumberParam = "";
		String servicesParam = "";
		
		try {
			storeNumberParam = params.get("storeNumber").toString();
			servicesParam = params.get("services").toString();
		} catch(NullPointerException e) {
			context.getLogger().log("NullPointerException Occured while fetching Appointment Metadata : " + e.getMessage());
		}
		
		if(isNullOrEmpty(storeNumberParam)) {
			return getValidationMessage("Invalid Store Number");
		}
		if(isNullOrEmpty(servicesParam)) {
			return getValidationMessage("Invalid Services");
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		try {
			// Prepare url with query parameters
			String url = constructURL(storeNumberParam, servicesParam);
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
				    if(json.has("data")) {
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
		} catch (UnsupportedEncodingException e) {
			context.getLogger().log("BSROAppointmentMetadataFunctionHandler - UnsupportedEncodingException Occured while fetching Appointment Metadata : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("UnsupportedEncodingException : " + e.getMessage());
		} catch (IOException e) {
			context.getLogger().log("BSROAppointmentMetadataFunctionHandler - IOException Occured while fetching Appointment Metadata : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("IOException : " + e.getMessage());
		} catch (Exception e) {
			context.getLogger().log("BSROAppointmentMetadataFunctionHandler - Exception Occured while fetching Appointment Metadata : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("Exception : " + e.getMessage());
		}
		
		Properties prop = new Properties();
		InputStream inputStream = BSROAppointmentMetadataFunctionHandler.class.getClassLoader().getResourceAsStream("application.properties");
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			bsroWebServiceResponse.setMessage("IOException while getting property value");
		}
		bsroWebServiceResponse.setMessage(prop.getProperty("environment"));
		
		return bsroWebServiceResponse;
	}
	
	public String constructURL(String storeNumber, String services) throws UnsupportedEncodingException, IOException {
		
		Properties prop = new Properties();
		InputStream input = BSROAppointmentMetadataFunctionHandler.class.getClassLoader().getResourceAsStream("application.properties");
		prop.load(input);
		input.close();
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		String paddedStoreNumber = ("000000" + storeNumber);
		StringBuilder paramBuilder = new StringBuilder();
		
		paramBuilder.append(prop.getProperty("appointment.plus.url.base")+prop.getProperty("appointment.plus.url.metadata"));
		
		paramBuilder.append(queryDeli).append("store_number").append(nameValuePairDeli).append(paddedStoreNumber.substring(paddedStoreNumber.length()-6));
		paramBuilder.append(nameValueDeli).append("services").append(nameValuePairDeli).append(URLEncoder.encode(services,"UTF-8"));
		paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(prop.getProperty("siteId"));
		paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(prop.getProperty("apiKey"),"UTF-8"));
		paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(prop.getProperty("responseType"),"UTF-8"));
		
		return paramBuilder.toString();
	}
	
	public static boolean isNullOrEmpty(String str) {
        if (str == null || str.trim().equals("") || str.trim().equals("null")) {
            return true;
        } 
        return false;
    }
    
    private BSROWebServiceResponse getValidationMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}
}
