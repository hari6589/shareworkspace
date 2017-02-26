package com.scheduleappointment;

import java.io.IOException;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduleappointment.model.AppointmentData;

public class ScheduleAppointment {
	
	public static void main(String[] args) {
		
		String url = "https://sandbox-ws.appointment-plus.com/Appointments/CreateAppointments"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		try {
			// Prepare url with query parameters
			url = url + parameterBuilder();
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
					String responseBody = EntityUtils.toString(responseEntity);
				    //ObjectMapper objectMapper = new ObjectMapper();
				    JSONObject json = new JSONObject(responseBody);
				    if(json.getString("data") != null) {
						//JSONObject data = json.optJSONObject("data");
						
						//Appointment result = objectMapper.readValue(responseBody, Appointment.class);
						System.out.println("Response Body : " + responseBody);
					}
				}
			} else {
				System.out.println("Business Service Error");
			}
			
			// close HttpClient connection
			httpClient.close();
			
		} catch (JSONException e) {
			System.out.println("JSONException");
			e.printStackTrace();
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

	public static String parameterBuilder() {
		
		StringBuilder paramBuilder = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		AppointmentData appointmentData = null;
		
		String jsonData = getJsonData();
		
		try {
			appointmentData = mapper.readValue(jsonData, AppointmentData.class);
			System.out.println("Property : " + appointmentData.toString());
			
			//String locationId = "1581";			//c_id:1581
			//String employeeId = "12713";			//employee_id:12713
			//String customerId = "6962263";		//customer_id:6962263
			//String statusId = "4088";				//status_id:4088
			String selectedDate = "20170215";		//date:20170215
			String selectedTime = "944"; 			//start_time:944
			//String serviceId = "2745";			//service_id:2745
			//String quoteId = "1060318"; 			//po_number:1060318
			//String eComRefNumber = "1000ABC";		//vin:1000ABC
			//String dropWaitOption = "drop";		//other_vehicle:drop
			//String override = "false";			//override:false // If dropWaitOption is WAIT then override should be false
			//String acesYear = "2012";				//aces_year:2012
			//String acesMake = "Audi";				//aces_make:Audi
			//String acesModel = "A4";				//aces_model:A4
			String overrideAcesSubmodel = "true";	//override_aces_submodel:true
			//String acesSubmodel = "Base"; 		//aces_submodel:Base
			//String mileage = "2500"; 				//odometer:2500
			//String customerNotes = ""; 			//customer_notes:TestNote
			
			StringBuilder appointmentNotes = new StringBuilder();
			if(appointmentData.getCustomerNotes() != null && !appointmentData.getCustomerNotes().isEmpty()){
				appointmentNotes.append(appointmentData.getCustomerNotes());
				appointmentNotes.append(!appointmentData.getCustomerNotes().endsWith(".")? "." : "");
			}
			if(appointmentData.getComments() != null && !appointmentData.getComments().isEmpty()){
				appointmentNotes.append(appointmentData.getComments());
				appointmentNotes.append(!appointmentData.getComments().endsWith(".") ? "." : "");
			}
			
			String siteId = "appointplus846/776";
			String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
			//String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
			String responseType = "JSON";
			
			String queryDeli = "?";
			String nameValueDeli = "&";
			String nameValuePairDeli = "=";
			
			paramBuilder.append(queryDeli).append("c_id").append(nameValuePairDeli).append(appointmentData.getLocationId());
			paramBuilder.append(nameValueDeli).append("employee_id").append(nameValuePairDeli).append(appointmentData.getEmployeeId());
			paramBuilder.append(nameValueDeli).append("customer_id").append(nameValuePairDeli).append(6962263); // Customer ID // HARDCODED
			paramBuilder.append(nameValueDeli).append("status_id").append(nameValuePairDeli).append(appointmentData.getAppointmentStatusId()); // confirm
			paramBuilder.append(nameValueDeli).append("date").append(nameValuePairDeli).append(selectedDate);
			paramBuilder.append(nameValueDeli).append("start_time").append(nameValuePairDeli).append(selectedTime);
			paramBuilder.append(nameValueDeli).append("service_id").append(nameValuePairDeli).append(appointmentData.getSelectedServices()); // confirm
			paramBuilder.append(nameValueDeli).append("po_number").append(nameValuePairDeli).append(appointmentData.getQuoteId());
			paramBuilder.append(nameValueDeli).append("vin").append(nameValuePairDeli).append(appointmentData.geteCommRefNumber()); // check logic
			paramBuilder.append(nameValueDeli).append("customer_notes").append(nameValuePairDeli).append(appointmentNotes); // check logic // no data received in json
			paramBuilder.append(nameValueDeli).append("other_vehicle").append(nameValuePairDeli).append(appointmentData.getDropWaitOption());
			paramBuilder.append(nameValueDeli).append("override").append(nameValuePairDeli).append(appointmentData.getChoice().getDropWaitOption().equalsIgnoreCase("wait") ? false : true); //confirm
			paramBuilder.append(nameValueDeli).append("aces_year").append(nameValuePairDeli).append(appointmentData.getVehicleYear());
			paramBuilder.append(nameValueDeli).append("aces_make").append(nameValuePairDeli).append(appointmentData.getVehicleMake());
			paramBuilder.append(nameValueDeli).append("aces_model").append(nameValuePairDeli).append(appointmentData.getVehicleModel());
			paramBuilder.append(nameValueDeli).append("override_aces_submodel").append(nameValuePairDeli).append(overrideAcesSubmodel);// check logic
			paramBuilder.append(nameValueDeli).append("aces_submodel").append(nameValuePairDeli).append(appointmentData.getVehicleSubmodel());//if available
			paramBuilder.append(nameValueDeli).append("odometer").append(nameValuePairDeli).append(appointmentData.getMileage());//if available
			
			paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
			paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
			paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
			
		
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return paramBuilder.toString();
		
	}
	
	public static String getJsonData() {
		return "{"
				+ "\"storeNumber\":\"23817\","
				+ "\"locationId\":\"1581\","
				+ "\"employeeId\":\"12713\","
				+ "\"quoteId\":\"123456\","
				+ "\"appointmentStatusId\":\"4088\","
				+ "\"appointmentStatusDesc\":\"Scheduled\","
				+ "\"vehicleYear\":\"2010\","
				+ "\"vehicleMake\":\"Chevrolet\","
				+ "\"vehicleModel\":\"Colorado\","
				+ "\"vehicleSubmodel\":\"WT\","
				+ "\"mileage\":\"7500\","
				+ "\"customerFirstName\":\"Stallin\","
				+ "\"customerLastName\":\"Moorthy\","
				+ "\"customerDayTimePhone\":\"227-876-5678\","
				+ "\"customerEmailAddress\":\"test@bfrc.com\","
				+ "\"websiteName\":\"FCAC\","
				+ "\"appointmentType\":\"New\","
				+ "\"choice\":{"
					+ "\"choice\":\"1\","
					+ "\"datetime\":\"1455811200000\","
					+ "\"dropWaitOption\":\"drop\""
				+ "},"
				+ "\"selectedServices\":\"2751,2767\","
				+ "\"eCommRefNumber\":\"100123\""
				+ "}";
	}
	
	public static String getObjectData() {
		return "body={"
				+ "\"storeNumber\":\"23817\","
				+ "\"locationId\":\"1581\","
				+ "\"employeeId\":\"12713\","
				+ "\"quoteId\":\"123456\","
				+ "\"appointmentStatusId\":\"4088\","
				+ "\"appointmentStatusDesc\":\"Scheduled\","
				+ "\"vehicleYear\":\"2010\","
				+ "\"vehicleMake\":\"Chevrolet\","
				+ "\"vehicleModel\":\"Colorado\","
				+ "\"vehicleSubmodel\":\"WT\","
				+ "\"mileage\":\"7500\","
				+ "\"customerFirstName\":\"Stallin\","
				+ "\"customerLastName\":\"Moorthy\","
				+ "\"customerDayTimePhone\":\"227-876-5678\","
				+ "\"customerEmailAddress\":\"test@bfrc.com\","
				+ "\"websiteName\":\"FCAC\","
				+ "\"appointmentType\":\"New\","
				+ "\"choice\":{"
					+ "\"choice\":\"1\","
					+ "\"datetime\":\"1455811200000\","
					+ "\"dropWaitOption\":\"drop\""
				+ "},"
				+ "\"selectedServices\":\"2751,2767\","
				+ "\"eCommRefNumber\":\"100123\""
				+ "}";
	}
}
