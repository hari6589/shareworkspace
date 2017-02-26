package bsro.handler.appointment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import bsro.appointment.model.Time;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class BSROAppointmentTimeFunctionHandler implements RequestHandler<Object, Object> {
	public Object handleRequest(Object input, Context context) {
		
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
			url = url + parameterBuilder(locationIdParam, selectedDateParam, serviceIdsParam, employeeIdParam); //Append query parameters
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
					Time result[]  = objectMapper.readValue(responseBody, Time[].class);
					for(int i=0; i < result.length; i++) {
						Time time = (Time) result[i];
						String startTime = time.getAvailableTime();
						time.setAvailableTime(minutesToHours(startTime));
						result[i] = time;
					}
					bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
				    bsroWebServiceResponse.setPayload(result);
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
	
	public String parameterBuilder(String locationId, String selectedDate, String serviceIds, String employeeId) throws UnsupportedEncodingException, ParseException {
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
