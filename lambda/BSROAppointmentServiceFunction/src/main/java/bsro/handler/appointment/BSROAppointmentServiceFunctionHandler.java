package bsro.handler.appointment;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bsro.dynamodb.mapper.appointment.service.Service;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

public class BSROAppointmentServiceFunctionHandler implements RequestHandler<Object, Object> {
	public Object handleRequest(Object input, Context context) {
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
			context.getLogger().log("BSROAppointmentServiceFunctionHandler - AmazonClientException Occured while fetching Appointment Services : " + ace.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			bsroWebServiceResponse.setMessage("AmazonClientException : " + ace.getMessage());
		    return "AmazonClientException";
		} catch (Exception e) {
			context.getLogger().log("BSROAppointmentServiceFunctionHandler - Exception Occured while fetching Appointment Services : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
			bsroWebServiceResponse.setMessage("Exception : " + e.getMessage());
			return "Exception : " + e;
		}
        return services;
	}
}
