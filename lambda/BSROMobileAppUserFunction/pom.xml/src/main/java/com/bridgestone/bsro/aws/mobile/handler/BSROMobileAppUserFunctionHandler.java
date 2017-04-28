package com.bridgestone.bsro.aws.mobile.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.bridgestone.bsro.aws.mobile.service.MobileAppUserService;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponseCode;

//public class BSROMobileAppUserFunctionHandler implements RequestHandler<Object, Object> {
public class BSROMobileAppUserFunctionHandler {
	
	static final String REGISTER = "register";
	static final String AUTHENTICATE = "authenticate";
	static final String CHECK_DATA = "check-data";
	static final String RESTORE = "restore";
	static final String BACKUP = "backup";
	static final String FORGOT_PASSWORD = "forgot_password";
	static final String UPDATE = "update";
	
	public static Properties properties = null;
	public static AWSCredentials awsCredentials = null;
	public static DynamoDBMapper dynamoDBMapper = null;
	
	//public BSROScheduleAppointmentFunctionHandler() {
	static {
		InputStream inputStream = BSROMobileAppUserFunctionHandler.class.getClassLoader().getResourceAsStream("application.properties");
		properties = new Properties();
		try {
			properties.load(inputStream);
			inputStream.close();
			awsCredentials = new BasicAWSCredentials(properties.getProperty("accessKey"), properties.getProperty("secretKey"));
			dynamoDBMapper = new DynamoDBMapper(new AmazonDynamoDBClient(awsCredentials));
		} catch (IOException e) {
			System.out.println("IOException  in BSROMobileAppUserFunctionHandler Constructor!");
		} catch (Exception e) {
			System.out.println("Exception  in BSROMobileAppUserFunctionHandler Constructor!");
		}
	}
	
	//@Override
	//public Object handleRequest(Object input, Context context) {
	public static void main(String[] args) {
		
		String email = "aravindhan.jayakumar@csscorp.com";
		//String password = "test123@";
		String password = "u0LzDig5";
		String appName = "FCAC";
		String serviceFunction = "update";
		String data = "";
		
		String oldEmail = "hari6589@gmail.com";
		String newEmail = "hari6589@gmail.com";
		String newPassword = "u0LzDig6"; // i/xQdP5xNaFkmyaRapxRIw==
		String oldPassword = "u0LzDig5";
		
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		MobileAppUserService mobileAppUserService = new MobileAppUserService();
		
		// Get Website Name
		
		try {
			
			switch(serviceFunction) {
				case REGISTER:
					System.out.println(mobileAppUserService.registerUser(dynamoDBMapper, appName, email, password));
					break;
				case AUTHENTICATE:
					System.out.println(mobileAppUserService.authenticateUser(dynamoDBMapper, appName, email, password));
					break;
				case CHECK_DATA:
					System.out.println(mobileAppUserService.doesUserDataExist(dynamoDBMapper, appName, email));
					break;
				case RESTORE:
					System.out.println(mobileAppUserService.restoreData(dynamoDBMapper, appName, email));
					break;
				case BACKUP:
					JSONObject jsonObject = new JSONObject();
					try {
						jsonObject.put("id", "1");
						jsonObject.put("name", "Hari");			
						jsonObject.put("price", "25.60");
						data = jsonObject.toString();
					} catch (JSONException e) {
						System.out.println("JSONException : " + e.getMessage());
					}
					System.out.println(mobileAppUserService.backupData(dynamoDBMapper, appName, email, data));
					break;
				case FORGOT_PASSWORD:
					System.out.println(mobileAppUserService.forgotPassword(dynamoDBMapper, appName, email));
					break;
				case UPDATE:
					System.out.println(mobileAppUserService.updateUser(dynamoDBMapper, appName, oldEmail, newEmail, oldPassword, newPassword));
					break;
				default:
					bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
					//return bsroWebServiceResponse;
					System.out.println("Default Section!");
					break;
			}
		} catch(Exception e) {
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
			bsroWebServiceResponse.setMessage(e.getMessage());
			//return bsroWebServiceResponse;
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
}
