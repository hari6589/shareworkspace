package bsro.handler.battery;

import java.util.HashMap;

import bsro.dynamodb.mapper.battery.engine.BatteryQuote;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;
import bsro.webservice.error.Errors;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BSROGetBatteryQuoteFunctionHandler implements RequestHandler<Object, Object> {

    public Object handleRequest(Object input, Context context) {
    	context.getLogger().log("Battery Quote Input: " + input);
    	
    	HashMap<String, String> mapInput = (HashMap<String, String>) input;
		String quoteId = mapInput.get("quoteId");
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if(isNullOrEmpty(quoteId)){
			response.setMessage("quoteId should not empty or null");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
	    }
		
        AWSCredentials creds = new BasicAWSCredentials("AKIAIWQOW2UUYPMOG2VQ","T3NHKV0FayMEeHHpwixdguEE7RIPyZywHdESE1ps");
        AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(creds);
        DynamoDBMapper mapper = new DynamoDBMapper(dyndbclient);
        BatteryQuote batteryQuote = null;
        
        try {
        	batteryQuote = mapper.load(BatteryQuote.class, Long.parseLong(quoteId), 
                new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT));
        } catch (AmazonServiceException ase) {
        	context.getLogger().log("Exception Occured while fetching Battery quote : " + ase.getMessage());
            response.setMessage("Exception Occured while fetching Battery quote : " + ase.getMessage());
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
        } catch (AmazonClientException ace) {
        	context.getLogger().log("Exception Occured while fetching Battery quote : " + ace.getMessage());
        	response.setMessage("Exception Occured while fetching Battery quote : " + ace.getMessage());
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
        }
        
        if(batteryQuote==null){
        	response.setMessage("NoDataFound");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
        }
        
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(batteryQuote);
      
        return response;
    }
    
    private BSROWebServiceResponse getValidationMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}
    
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.trim().equals("") || str.trim().equals("null")) {
            return true;
        } 
        return false;
    }
}
