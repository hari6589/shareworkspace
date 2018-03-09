package com.bridgestone.bsro.aws.mobile.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bridgestone.bsro.aws.mobile.service.MobileAppUserService;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponseCode;

public class BSROMobileAppUserFunctionHandler implements RequestHandler<Object, Object> {
//public class BSROMobileAppUserFunctionHandler {
	
	static final String REGISTER = "register";
	static final String AUTHENTICATE = "authenticate";
	static final String CHECK_DATA = "check-data";
	static final String RESTORE = "restore";
	static final String BACKUP = "backup";
	static final String FORGOT_PASSWORD = "forgot-password";
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
	public Object handleRequest(Object input, Context context) {
	//public static void main(String[] args) {
		
		HashMap<String, String> params = (HashMap<String, String>) input;
		String resourcePathParam = params.get("resourcePath").toString();
		System.out.println("Resource Path : " + resourcePathParam);
		String serviceFunction = resourcePathParam.substring(resourcePathParam.lastIndexOf("/") + 1);
		System.out.println("serviceFunction : " + serviceFunction);
		
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		MobileAppUserService mobileAppUserService = new MobileAppUserService();
		
		String email = "";
		String appName = "";
		String password = "";
		String data = "";
		
		try {
			
			switch(serviceFunction) {
				case REGISTER:
					//System.out.println(mobileAppUserService.registerUser(dynamoDBMapper, appName, email, password));
					//break;
					
					appName = params.get("appName");
					email = params.get("email");
					password = params.get("authString");
					return mobileAppUserService.registerUser(dynamoDBMapper, appName, email, password);
				case AUTHENTICATE:
					//System.out.println(mobileAppUserService.authenticateUser(dynamoDBMapper, appName, email, password));
					//break;
					
					appName = params.get("appName");
					email = params.get("email");
					password = params.get("authString");
					return mobileAppUserService.authenticateUser(dynamoDBMapper, appName, email, password);
				case CHECK_DATA:
					//System.out.println(mobileAppUserService.doesUserDataExist(dynamoDBMapper, appName, email));
					//break;
					
					appName = params.get("appName");
					email = params.get("email");
					return mobileAppUserService.doesUserDataExist(dynamoDBMapper, appName, email);
				case RESTORE:
					//System.out.println(mobileAppUserService.restoreData(dynamoDBMapper, appName, email));
					//break;
					
					appName = params.get("appName");
					email = params.get("email");
					return mobileAppUserService.restoreData(dynamoDBMapper, appName, email);
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
					
					//System.out.println(mobileAppUserService.backupData(dynamoDBMapper, appName, email, data));
					//break;
					
					appName = params.get("appName");
					email = params.get("email");
					System.out.println(data);
					data = String.valueOf(params.get("data"));
					System.out.println(data);
					return mobileAppUserService.backupData(dynamoDBMapper, appName, email, data);
					
				case FORGOT_PASSWORD:
					//System.out.println(mobileAppUserService.forgotPassword(dynamoDBMapper, appName, email));
					//break;
					
					appName = params.get("appName");
					email = params.get("email");
					return mobileAppUserService.forgotPassword(dynamoDBMapper, appName, email);
				case UPDATE:
					//System.out.println(mobileAppUserService.updateUser(dynamoDBMapper, appName, oldEmail, newEmail, oldPassword, newPassword));
					//break;
					
					appName = params.get("appName");
					String oldEmail = params.get("oEmail");
					String newEmail = params.get("nEmail");
					String oldPassword = params.get("oAuthString");
					String newPassword = params.get("nAuthString");
					return mobileAppUserService.updateUser(dynamoDBMapper, appName, oldEmail, newEmail, oldPassword, newPassword);
					
				default:
					bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
					System.out.println("Default Section!");
					return bsroWebServiceResponse;
			}
		} catch(Exception e) {
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
			bsroWebServiceResponse.setMessage(e.getMessage());
			System.out.println("Exception : " + e.getMessage());
			return bsroWebServiceResponse;
		}
	}
	
}
