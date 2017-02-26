package com.bridgestone.bsro.aws.appointment.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import org.json.JSONException;
import org.json.JSONObject;

import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

import com.bridgestone.bsro.aws.appointment.model.Day;
import com.bridgestone.bsro.aws.appointment.model.Metadata;
import com.bridgestone.bsro.aws.appointment.model.Service;
import com.bridgestone.bsro.aws.appointment.model.Time;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;

public class AppointmentService {
	public Object getAppointmentService(Object input, Context context) {
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		List<Service> services = new ArrayList<Service>();
		try {
			// Authenticate DynamoDB with AWS
			AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
	    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credential);
	    	amazonDynamoDBClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	    	
	    	DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
	    	DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
	    	
	    	// Fetch services list from DynamoDB.
	    	// 'scan' returns fixed size of list only, to sort the list it should be dynamic size
	    	services = new ArrayList<Service>(dynamoDBMapper.scan(Service.class, dynamoDBScanExpression));
	    	
	    	Collections.sort(services);	    	
	    		
		} catch (AmazonClientException ace) {
			context.getLogger().log("AmazonClientException Occured while fetching Appointment Services : " + ace.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("AmazonClientException : " + ace.getMessage());
		    return "AmazonClientException";
		} catch (Exception e) {
			context.getLogger().log("Exception Occured while fetching Appointment Services : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("Exception : " + e.getMessage());
			return "Exception : " + e;
		}
        return services;
	}
	
	public BSROWebServiceResponse getAppointmentMetadata(Object input, Context context) {
		
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
			url = url + getMatadataParamBuilder(storeNumberParam, servicesParam);
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
	
	public String getMatadataParamBuilder(String storeNumber, String services) throws UnsupportedEncodingException {
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
	
	public BSROWebServiceResponse getAppointmentDays(Object input, Context context) {
		
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		
		// Extract input parameters
		HashMap<String, String> props = (HashMap<String, String>) input;
		String locationIdParam = props.get("locationId").toString();
		String startDateParam = props.get("startDate").toString();
		String numDaysParam = props.get("numDays").toString();
		String employeeIdParam = props.get("employeeId").toString();
		
		String url = "https://sandbox-api.appointment-plus.com/Staff/GetOpenDates"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		try {
			// Prepare url with query parameters
			url = url + getDaysParamBuilder(locationIdParam, startDateParam, numDaysParam, employeeIdParam); //Append query parameters
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
					ObjectMapper objectMapper = new ObjectMapper();
					String responseBody = "";
				    JSONObject json = new JSONObject(EntityUtils.toString(responseEntity));
					if(json.getString("data") != null) {
						responseBody = json.get("data").toString();
						Day days[] = objectMapper.readValue(responseBody, Day[].class);
						bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
					    bsroWebServiceResponse.setPayload(days);
					}
				}
			} else {
				context.getLogger().log("BSROAppointmentDayFunctionHandler - HTTP Status code is not 200");
				bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			}
			
			// close HttpClient connection
			httpClient.close();
			
		} catch (JSONException e) {
			context.getLogger().log("BSROAppointmentDayFunctionHandler - JSONException Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("JSONException : " + e.getMessage());
		} catch (ClientProtocolException e) {
			context.getLogger().log("BSROAppointmentDayFunctionHandler - ClientProtocolException Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("ClientProtocolException : " + e.getMessage());
		} catch (ConnectTimeoutException e) {
			context.getLogger().log("BSROAppointmentDayFunctionHandler - ConnectTimeoutException Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("ConnectTimeoutException : " + e.getMessage());
		} catch (Exception e) {
			context.getLogger().log("BSROAppointmentDayFunctionHandler - Exception Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("Exception : " + e.getMessage());
		}
		return bsroWebServiceResponse;
	}
	
	public String getDaysParamBuilder(String locationId, String startDate, String numDays, String employeeId) throws UnsupportedEncodingException {
		
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		//String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		StringBuilder paramBuilder = new StringBuilder();
		
		paramBuilder.append(queryDeli).append("num_days").append(nameValuePairDeli).append(numDays);
		paramBuilder.append(nameValueDeli).append("start_date").append(nameValuePairDeli).append(startDate);
		paramBuilder.append(nameValueDeli).append("c_id").append(nameValuePairDeli).append(locationId);
		paramBuilder.append(nameValueDeli).append("employee_id").append(nameValuePairDeli).append(employeeId);
		
		paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
		paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
		paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
		
		return paramBuilder.toString();
	}
	
	public Object getAppointmentTimeSlots(Object input, Context context) {
		
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		
		// Extract input parameters
	    HashMap<String, String> props = (HashMap<String, String>) input;
		String locationIdParam = props.get("locationId").toString();
		String selectedDateParam = props.get("selectedDate").toString();
		String serviceIdsParam = props.get("serviceIds").toString();
		String employeeIdParam = props.get("employeeId").toString();
			
		String url = "https://sandbox-api.appointment-plus.com/Bridgestone/GetOpenSlots"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		try {
			// Prepare url with query parameters
			url = url + getTimeSlotsParamBuilder(locationIdParam, selectedDateParam, serviceIdsParam, employeeIdParam); //Append query parameters
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
					ObjectMapper objectMapper = new ObjectMapper();
					String responseBody = "";
				    JSONObject json = new JSONObject(EntityUtils.toString(responseEntity));
					if(json.getString("data") != null) {
						responseBody = json.get("data").toString();
					}
					Time timeSlots[]  = objectMapper.readValue(responseBody, Time[].class);
					for(int i=0; i < timeSlots.length; i++) {
						Time time = (Time) timeSlots[i];
						String startTime = time.getAvailableTime();
						time.setAvailableTime(minutesToHours(startTime));
						timeSlots[i] = time;
					}
					bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
				    bsroWebServiceResponse.setPayload(timeSlots);
				}
			} else {
				context.getLogger().log("BSROAppointmentTimeFunctionHandler - HTTP Status code is not 200");
				bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			}

			// close HttpClient connection
			httpClient.close();
						
		} catch (JSONException e) {
			context.getLogger().log("BSROAppointmentTimeFunctionHandler - JSONException Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("JSONException : " + e.getMessage());
		} catch (ClientProtocolException e) {
			context.getLogger().log("BSROAppointmentTimeFunctionHandler - ClientProtocolException Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("ClientProtocolException : " + e.getMessage());
		} catch (ConnectTimeoutException e) {
			context.getLogger().log("BSROAppointmentTimeFunctionHandler - ConnectTimeoutException Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("ConnectTimeoutException : " + e.getMessage());
		} catch (ParseException e) {
			context.getLogger().log("BSROAppointmentTimeFunctionHandler - ParseException Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("ParseException : " + e.getMessage());
		} catch (Exception e) {
			context.getLogger().log("BSROAppointmentTimeFunctionHandler - Exception Occured while fetching Appointment Time Slots : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("Exception : " + e.getMessage());
		}
		return bsroWebServiceResponse;
	}
	
	public String getTimeSlotsParamBuilder(String locationId, String selectedDate, String serviceIds, String employeeId) throws UnsupportedEncodingException, ParseException {
		Integer numberOfDays = 1; // Always get the selected date's time slots only
		String primaryServiceId = "";
		String secondaryServiceIds = "";
		String duplicateFlag = "no"; // Avoid duplicates
		
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		//String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		int firstDelimIndex = serviceIds.indexOf(",");
		if(firstDelimIndex != -1) {
			primaryServiceId = serviceIds.substring(0, firstDelimIndex);
			secondaryServiceIds = serviceIds.substring(firstDelimIndex+1);
		}
		
		StringBuilder paramBuilder = new StringBuilder();
		
		paramBuilder.append(queryDeli).append("num_days").append(nameValuePairDeli).append(String.valueOf(numberOfDays));
		paramBuilder.append(nameValueDeli).append("c_id").append(nameValuePairDeli).append(locationId);
		paramBuilder.append(nameValueDeli).append("employee_id").append(nameValuePairDeli).append(employeeId);
		paramBuilder.append(nameValueDeli).append("start_date").append(nameValuePairDeli).append(selectedDate);
		paramBuilder.append(nameValueDeli).append("start_time").append(nameValuePairDeli).append(String.valueOf(getStartTime(selectedDate)));
		paramBuilder.append(nameValueDeli).append("service").append(nameValuePairDeli).append(primaryServiceId); //Primary service
		paramBuilder.append(nameValueDeli).append("addons").append(nameValuePairDeli).append(secondaryServiceIds); //Additional services
		paramBuilder.append(nameValueDeli).append("show_duplicates").append(nameValuePairDeli).append(duplicateFlag);
		
		paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
		paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
		paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
		
		return paramBuilder.toString();
	}
	
	public Integer getStartTime(String selectedDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Integer startTimeInMins = 0;

		Date apptDate = dateFormat.parse(selectedDate); //Start from midnight, get slots for entire day if selected day is future
		
		Calendar today = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(apptDate);
		if(calendar.before(today)){
			SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
			String currentHourMin = hmFormat.format(today.getTime());
			String[] hourMinSplits = currentHourMin.split(":");
			startTimeInMins = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
		}
		
		return startTimeInMins;
	}
	
	public String minutesToHours(String startTimeStr) throws ParseException {
		String time = "";
		
		int startTime = Integer.parseInt(startTimeStr);
		int hours = Math.abs(new Integer(startTime/60));
		int minutes = startTime%60;
		
		String paddedMinutes = ("00"+minutes).substring(("00"+minutes).length()-2);
		String meridian = (hours > 11) ? "pm" : "am";
		
		hours = (hours >= 13) ? hours - 12 : hours; 
		time = hours + ":" + paddedMinutes + meridian;
		
		return time;
	}
	
}
