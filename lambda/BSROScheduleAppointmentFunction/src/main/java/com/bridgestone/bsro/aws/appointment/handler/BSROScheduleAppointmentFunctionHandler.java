package com.bridgestone.bsro.aws.appointment.handler;

import java.util.HashMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bridgestone.bsro.aws.appointment.services.impl.AppointmentServiceImpl;
import com.bridgestone.bsro.aws.appointment.util.AuthenticateService;
import com.bridgestone.bsro.aws.appointment.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.appointment.webservice.BSROWebServiceResponseCode;

public class BSROScheduleAppointmentFunctionHandler implements RequestHandler<Object, Object> {
	
	static final String GET_SERVICE = "services";
	static final String GET_METADATA = "metadata";
	static final String GET_DAY = "days";
	static final String GET_TIME = "times";
	static final String CREATE_BOOK = "book";
	
	public Object handleRequest(Object input, Context context) {

		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		try {
			HashMap<String, String> params = (HashMap<String, String>) input;
			String tokenId = params.get("tokenId");
			String appName = params.get("appName");
			
	    	AuthenticateService security = new AuthenticateService();
	    	if(!security.authenticateService(tokenId,appName)){
	        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNAUTHORIZED_ERROR.toString());
	        	return bsroWebServiceResponse;
	        }
	    	
			String resourcePathParam = params.get("resourcePath").toString();
			
			String serviceFunction = resourcePathParam.substring(resourcePathParam.lastIndexOf("/") + 1);
			AppointmentServiceImpl appointmentService = new AppointmentServiceImpl();
			
			switch(serviceFunction) {
				case GET_SERVICE:
					return appointmentService.getAppointmentService(input, context);
				case GET_METADATA:
					return appointmentService.getAppointmentMetadata(input, context);
				case GET_DAY:
					return appointmentService.getAppointmentDays(input, context);
				case GET_TIME:
					return appointmentService.getAppointmentTimeSlots(input, context);
				case CREATE_BOOK:
					return appointmentService.bookAppointment(input, context);
				default:
					bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
					return bsroWebServiceResponse;
			}
		} catch(Exception e) {
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
			bsroWebServiceResponse.setMessage(input.toString() + " " + e.getMessage());
			return bsroWebServiceResponse;
		}
	}
}
