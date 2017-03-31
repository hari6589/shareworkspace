package com.scheduleappointment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.scheduleappointment.model.Appointment;
import com.scheduleappointment.model.AppointmentData;

public class AppointmentWorkerService {
		
	public Object getAppointmentData() {
		
		String queueName = "MyFifoQueue.fifo";
		String messageBody = "";
		String queueUrl = "";
		
		AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
		AmazonSQSClient sqs = new AmazonSQSClient(credential);
        //sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
        sqs.setEndpoint("https://sqs.us-west-2.amazonaws.com");
        //sqs.setSignerRegionOverride("us-west-2");
        
        try {
        	queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
        } catch (QueueDoesNotExistException e) {
        	System.out.println("FIFO Queue not found!");
        }
        
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
        	messageBody = message.getBody();
        	System.out.println("Message");
            System.out.println("MessageId:     " + message.getMessageId());
            System.out.println("ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("Body:          " + message.getBody());
            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
            	System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
            String messageReceiptHandle = message.getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
            sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(queueUrl).withReceiptHandle(messageReceiptHandle));;
        }
        
        return messageBody;
	}
	
	public long getCustomer(String dayPhone, String emailAddress, String lastName) {
		System.out.println("Getting Customer..");
		String url = "https://sandbox-api.appointment-plus.com/Customers/GetCustomers"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		Long customerId = 0L;
		try {
			// Prepare url with query parameters
			url = url + getCustParamBuilder(dayPhone, emailAddress, lastName);
			httpPost = new HttpPost(url);
			
			// Set request configuration
			RequestConfig requestConfig = RequestConfig.custom()
				       .setConnectTimeout(30000).build();
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
				    System.out.println("JSON: " + json.toString());
				    System.out.println("outside before");
				    if (json.has("data")) {
				    	System.out.println("inside");
						//JSONObject data = json.optJSONObject("data");
						JSONArray dataList = json.optJSONArray("data");
						for(int i=0 ; i<dataList.length() ; i++){
	                        JSONObject jsonObj = dataList.getJSONObject(i);
	                        customerId = jsonObj.getLong("customer_id");
	                        if(customerId != null){
								break;
							}
						}
					}
				    System.out.println("outside after");
				}
			} else {
				System.out.println("Business Service Error");
			}
		} catch (JSONException ase) {
	    	System.out.println("JSONException Occured while saving Appointment : " + ase.getMessage());
		} catch (ClientProtocolException ase) {
	    	System.out.println("ClientProtocolException Occured while saving Appointment : " + ase.getMessage());
		} catch (ConnectTimeoutException ase) {
	    	System.out.println("ConnectTimeoutException Occured while saving Appointment : " + ase.getMessage());
	    } catch (AmazonServiceException ase) {
	    	System.out.println("Exception Occured while saving Appointment : " + ase.getMessage());
		} catch (AmazonClientException ace) {
			System.out.println("JSONException Occured while saving Appointment : " + ace.getMessage());
		} catch (Exception e) {
			System.out.println("Exception Occured while saving Appointment : " + e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				System.out.println("HTTPClient Connection is already Closed");
			}
		}
		return customerId;
	}

	public static String getCustParamBuilder(String dayPhone, String emailAddress, String lastName) throws UnsupportedEncodingException {
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		//String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		StringBuilder paramBuilder = new StringBuilder();
		
		paramBuilder.append(queryDeli).append("day_phone").append(nameValuePairDeli).append(dayPhone);
		paramBuilder.append(nameValueDeli).append("email").append(nameValuePairDeli).append(emailAddress);
		paramBuilder.append(nameValueDeli).append("last_name").append(nameValuePairDeli).append(lastName);
		paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
		paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
		paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
		
		return paramBuilder.toString();
	}
	
	public long createCustomer(Long locationId, String firstName, String lastName, String emailAddress, String dayTimePhone) {
		System.out.println("Creating Customer..");
		String url = "https://sandbox-api.appointment-plus.com/Customers/CreateCustomers"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		Long customerId = null;
		
		try {
			// Prepare url with query parameters
			url = url + createCustParamBuilder(locationId, firstName, lastName, dayTimePhone, emailAddress);
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
					responseBody = EntityUtils.toString(responseEntity);
				    JSONObject json = new JSONObject(responseBody);
					if(json.getString("data") != null) {
						responseBody = json.get("data").toString();
						json = new JSONObject(responseBody);
						customerId = json.getLong("customer_id");
					} else {
						// customer not created exception should be thrown
					}
				}
			} else {
				System.out.println("Business Service Error");
			}
			
			// close HttpClient connection
			httpClient.close();
//					
//				} catch (JSONException e) {
//					System.out.println("JSONException");
//					e.printStackTrace();
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
		return customerId;
	}

	public static String createCustParamBuilder(Long locationId, String firstName, String lastName, String dayTimePhone, String emailAddress) throws UnsupportedEncodingException {
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
	
	public Appointment bookAppointment(AppointmentData appointmentData, Long customerId) throws JSONException, ClientProtocolException, ConnectTimeoutException, Exception {
		System.out.println("Booking Appointment..");
		Appointment appointment = new Appointment();
		
		String url = "https://sandbox-api.appointment-plus.com/Appointments/CreateAppointments"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		String responseBody = "";
		
		// Prepare url with query parameters
		System.out.println(appointmentData.toString());
		System.out.println("URL Before : " + url);
		url = url + parameterBuilder(appointmentData, customerId);
		System.out.println("URL after : " + url);
		httpPost = new HttpPost(url);
		
		// Set request configuration
		RequestConfig requestConfig = RequestConfig.custom()
			       .setConnectTimeout(30000).build();
		httpPost.setConfig(requestConfig);
		
		// Method execution
		response = httpClient.execute(httpPost);
		
		System.out.println("URL: " + url);
		
		// Process response
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			responseEntity = response.getEntity();				
			if(responseEntity != null) {
				responseBody = EntityUtils.toString(responseEntity);
				System.out.println(responseBody);
			    JSONObject json = new JSONObject(responseBody);
			    responseBody = json.toString();
			    
			    if(json.getString("data") != null) {
					JSONObject responseData = json.optJSONObject("data");
					appointment = processResponse(appointmentData, responseData);
				}
			}
		} else {
			// Business service error should be thrown
		}
		
		// close HttpClient connection
		httpClient.close();
		
		return appointment;
	}
	
	public String parameterBuilder(AppointmentData appointmentData, Long customerId) {
		
		StringBuilder paramBuilder = new StringBuilder();
		
		try {
			System.out.println("Choice DateL: " + appointmentData.getChoice().getDatetime());
			// Simplify
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
			String dateStr = dateFormat.format(appointmentData.getChoice().getDatetime());

			try {
				Date choiceDateTime = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				System.out.println("ParseException : " + e);
			}
			String[] dateSplits = dateStr.split("\\s+");
			String selectedDate = dateSplits[0];
			String[] hourMinSplits = dateSplits[1].split(":");
			Integer selectedTime = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
			
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
			paramBuilder.append(nameValueDeli).append("customer_id").append(nameValuePairDeli).append(customerId);
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
			paramBuilder.append(nameValueDeli).append("override_aces_submodel").append(nameValuePairDeli).append("true");// check logic
			paramBuilder.append(nameValueDeli).append("aces_submodel").append(nameValuePairDeli).append(appointmentData.getVehicleSubmodel());//if available
			paramBuilder.append(nameValueDeli).append("odometer").append(nameValuePairDeli).append(appointmentData.getMileage());//if available
			
			paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
			paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
			paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
			
			System.out.println("Parameter Builder");
		} catch (IOException e) {
			System.out.println("IOException" + e);
		} catch (Exception e) {
			System.out.println("Exception" + e);
		}
		return paramBuilder.toString();
	}
	
	public Appointment processResponse(AppointmentData appointmentData, JSONObject responseData) {
		
		Appointment appointment = new Appointment();
		
		try {
			
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String timestampStr = dateFormat.format(now);
			
			appointment.setAppointmentId(Long.valueOf(timestampStr));
			appointment.setBookingConfirmationId(responseData.getString("appt_id"));
			appointment.setLocationId(responseData.getLong("c_id"));
			appointment.setCustomerId(responseData.getLong("customer_id"));
			appointment.setStoreNumber(appointmentData.getStoreNumber());
			appointment.setVehicleYear(appointmentData.getVehicleYear());
			appointment.setVehicleMake(appointmentData.getVehicleMake());
			appointment.setVehicleModel(appointmentData.getVehicleModel());
			appointment.setVehicleSubmodel(appointmentData.getVehicleSubmodel());
			appointment.setMileage(appointmentData.getMileage());
			appointment.setComments(appointmentData.getComments());
			appointment.setFirstName(appointmentData.getCustomerFirstName());
			appointment.setLastName(appointmentData.getCustomerLastName());
			appointment.setAddress1("");
			appointment.setAddress2("");
			appointment.setCity("");
			appointment.setState("");
			appointment.setZip("");
			appointment.setDaytimePhone("");
			appointment.setEveningPhone("");
			appointment.setCellPhone("");
			appointment.setEmailAddress(appointmentData.getCustomerEmailAddress());
			appointment.setEmailSignup("");
			appointment.setCreatedDate(null);
			appointment.setWebSite(appointmentData.getWebsiteName());
			appointment.setWebSiteSource("");
			//appointment.setAppointmentChoiceConfirmed("");
			appointment.setBatteryQuoteId(responseData.getLong("po_number"));
			appointment.setPhoneReminderInd("");
			appointment.setEmailReminderInd("");
			appointment.setEcommRefNumber(responseData.getString("vin"));
			appointment.setChoice(0);
			appointment.setDateTime(null);
			appointment.setDropWaitOption("");
			appointment.setPickupTime(null);
			appointment.setDropOffTime(null);
			appointment.setAppointmentServices(appointmentData.getSelectedServices());
			appointment.setEmployeeId(responseData.getLong("employee_id"));
			appointment.setAppointmentStatusId(responseData.getLong("status_id"));
//			if(responseData.getString("room_id") != "") {
//				appointment.setRoomId(Long.valueOf(responseData.getString("room_id")));
//			}
			appointment.setUpdateDate(null);//private Date UpdateDate; // required?
			if(responseData.getString("appt_id") != null && !responseData.getString("appt_id").equalsIgnoreCase("")) {
	    		appointment.setBookingConfirmationId(responseData.getString("appt_id"));
	    		appointment.setOtherDetails(""); // required?
				appointment.setStatus("S");
	    	} else {
	    		appointment.setStatus("R");
	    	}
			appointment.setBookingConfirmationId(responseData.getString("appt_id"));
			appointment.setStatus("SSSS");
			appointment.setEmailStatusMessage("");
			appointment.setEmailTrackingNumber("");
			
		} catch (JSONException e) {
			System.out.println("JSONException" + e);
		} catch (Exception e) {
			System.out.println("Exception" + e);
		}
		
		return appointment;
	}
}
