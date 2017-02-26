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

import bsro.appointment.model.Day;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

public class BSROAppointmentDayFunctionHandler implements RequestHandler<Object, Object> {
	
	public Object handleRequest(Object input, Context context) {
		
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
			url = url + parameterBuilder(locationIdParam, startDateParam, numDaysParam, employeeIdParam); //Append query parameters
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
						Day result[] = objectMapper.readValue(responseBody, Day[].class);
						bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
					    bsroWebServiceResponse.setPayload(result);
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
	
	public String parameterBuilder(String locationId, String startDate, String numDays, String employeeId) throws UnsupportedEncodingException {
		
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
}
