package bsro.handler.appointment;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.json.JSONException;
import org.json.JSONObject;

import bsro.appointment.model.AppointmentData;
import bsro.dynamodb.mapper.appointment.Appointment;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bridgestone.bsro.aws.appointment.services.impl.AppointmentService;

public class BSROScheduleAppointmentFunctionHandler implements RequestHandler<Object, Object> {
	
	static final String GET_SERVICE = "services";
	static final String GET_METADATA = "metadata";
	static final String GET_DAY = "days";
	static final String GET_TIME = "times";
	static final String CREATE_BOOK = "book";
	
	public Object handleRequest(Object input, Context context) {
		
		HashMap<String, String> params = (HashMap<String, String>) input;
		String resourcePathParam = params.get("resourcePath").toString();
		
		String serviceFunction = resourcePathParam.substring(resourcePathParam.lastIndexOf("/") + 1);
		AppointmentService appointmentService = new AppointmentService();
		
		switch(serviceFunction) {
			case GET_SERVICE:
				return  appointmentService.getAppointmentService(input, context);
			case GET_METADATA:
				return  appointmentService.getAppointmentMetadata(input, context);
			case GET_DAY:
				return appointmentService.getAppointmentDays(input, context);
			case GET_TIME:
				return appointmentService.getAppointmentTimeSlots(input, context);
			case CREATE_BOOK:
				return CREATE_BOOK;
			default:
				return "Method not matched with the services";
		}
		
	}
	
	public static Appointment bookAppointment(AppointmentData appointmentData, Long customerId) throws JSONException, ClientProtocolException, ConnectTimeoutException, Exception {
		
		Appointment appointment = new Appointment();
		
		String url = "https://sandbox-api.appointment-plus.com/Appointments/CreateAppointments"; // Move this uri to prop
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		HttpResponse response = null;
		
		String responseBody = "";
		
		// Prepare url with query parameters
		url = url + parameterBuilder(appointmentData, customerId);
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
				responseBody = EntityUtils.toString(responseEntity);
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
	
	public static BSROWebServiceResponse bookAppointmentWorker(AppointmentData appointmentData, Context context) {
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		CustomerService customerService = new CustomerService();
		
		try {
			// Get Customer ID
			Long customerId = customerService.getCustomer(appointmentData.getCustomerDayTimePhone(), appointmentData.getCustomerEmailAddress(), appointmentData.getCustomerLastName());
			
			if(customerId == null) {
				// Create a Customer and get ID
				customerId = customerService.createCustomer(appointmentData.getLocationId(), appointmentData.getCustomerFirstName(), 
						appointmentData.getCustomerLastName(), appointmentData.getCustomerEmailAddress(), 
						appointmentData.getCustomerDayTimePhone());
			}
			
			Appointment appointment;
			
				appointment = bookAppointment(appointmentData, customerId);
			
			bsroWebServiceResponse.setMessage("SUCCESS");
			bsroWebServiceResponse.setPayload(appointment);
					
			AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
	    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credential);
	    	amazonDynamoDBClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	    	
	        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
			dynamoDBMapper.save(appointment, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
			
        } catch (AmazonServiceException ase) {
        	context.getLogger().log("Exception Occured while saving Appointment : " + ase.getMessage());
        	bsroWebServiceResponse.setMessage("Exception Occured while saving Appointment : " + ase.getMessage());
        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return bsroWebServiceResponse;
        } catch (AmazonClientException ace) {
        	context.getLogger().log("Exception Occured while saving Appointment : " + ace.getMessage());
        	bsroWebServiceResponse.setMessage("Exception Occured while saving Appointment : " + ace.getMessage());
        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return bsroWebServiceResponse;
        } catch (ConnectTimeoutException e) {
        	context.getLogger().log("ConnectTimeoutException Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setMessage("ConnectTimeoutException Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
		} catch (ClientProtocolException e) {
			context.getLogger().log("ClientProtocolException Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setMessage("ClientProtocolException Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
		} catch (JSONException e) {
			context.getLogger().log("JSONException Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setMessage("JSONException Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
		} catch (Exception e) {
			context.getLogger().log("Exception Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setMessage("Exception Occured while saving Appointment : " + e.getMessage());
        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
		}
		return bsroWebServiceResponse;
	}
	
	public static String parameterBuilder(AppointmentData appointmentData, Long customerId) {
		
		StringBuilder paramBuilder = new StringBuilder();
		
		try {
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

		} catch (IOException e) {
			e.printStackTrace();
		}

		return paramBuilder.toString();
		
	}
	
	public static Appointment processResponse(AppointmentData appointmentData, JSONObject responseData) {
		
		Appointment appointment = new Appointment();
		
		try {
			appointment.setBookingConfirmationId(responseData.getString("appt_id"));
			appointment.setLocationId(responseData.getLong("c_id"));
			appointment.setCustomerId(responseData.getLong("customer_id"));
			appointment.setStoreNumber(appointmentData.getStoreNumber()); //private Long StoreNumber;
			appointment.setVehicleYear(appointmentData.getVehicleYear()); //private int VehicleYear;
			appointment.setVehicleMake(appointmentData.getVehicleMake());//private String VehicleMake;
			appointment.setVehicleModel(appointmentData.getVehicleModel());//private String VehicleModel;
			appointment.setVehicleSubmodel(appointmentData.getVehicleSubmodel());//private String VehicleSubmodel;
			appointment.setMileage(appointmentData.getMileage());//private int Mileage;
			appointment.setComments(appointmentData.getComments());//private String Comments;
			appointment.setFirstName(appointmentData.getCustomerFirstName());//private String FirstName;
			appointment.setLastName(appointmentData.getCustomerLastName());//private String LastName;
			appointment.setAddress1("");//private String Address1;///////////////////
			appointment.setAddress2("");//private String Address2;///////////////////
			appointment.setCity("");//private String City;///////////////////
			appointment.setState("");//private String State;///////////////////
			appointment.setZip("");//private String Zip;///////////////////
			appointment.setDaytimePhone("");//private String DaytimePhone;
			appointment.setEveningPhone("");//private String EveningPhone;
			appointment.setCellPhone("");//private String CellPhone;
			appointment.setEmailAddress(appointmentData.getCustomerEmailAddress());//private String EmailAddress;
			appointment.setEmailSignup("");//private String EmailSignup;
			appointment.setCreatedDate(null);//private Date CreatedDate;
			appointment.setWebSite(appointmentData.getWebsiteName());//private String WebSite;
			appointment.setWebSiteSource("");//private String WebSiteSource;
			appointment.setAppointmentChoiceConfirmed(0);//private int AppointmentChoiceConfirmed;
			appointment.setBatteryQuoteId(responseData.getLong("po_number"));//private int BatteryQuoteId;
			appointment.setPhoneReminderInd("");//private String PhoneReminderInd;
			appointment.setEmailReminderInd("");//private String EmailReminderInd;
			appointment.setEcommRefNumber(responseData.getString("vin"));//private String EcommRefNumber;
			appointment.setChoice(0);//private int Choice;
			appointment.setDateTime(null);//private String DateTime;
			appointment.setDropWaitOption("");//private String DropWaitOption;
			appointment.setPickupTime(null);//private Date PickupTime;
			appointment.setDropOffTime(null);//private Date DropOffTime;
			appointment.setAppointmentServices(appointmentData.getSelectedServices());//private String AppointmentServices;
			appointment.setEmployeeId(responseData.getLong("employee_id"));//private String EmployeeId;
			appointment.setAppointmentStatusId(responseData.getLong("status_id"));//private int AppointmentStatusId;
			appointment.setRoomId(responseData.getLong("room_id"));
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
			e.printStackTrace();
		}
		
		return appointment;
	}
	
}
