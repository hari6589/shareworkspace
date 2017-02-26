package bsro.handler.battery;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bsro.dynamodb.mapper.battery.make.Battery;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BSROBatteryMakeFunctionHandler implements RequestHandler<Object, Object> {

	    public Object handleRequest(Object input, Context context) {
	    	context.getLogger().log("Input: " + input);
	    	
	    	HashMap<String, String> mapInput = (HashMap<String, String>) input;
			String year = mapInput.get("year");
			
	        AWSCredentials creds = new BasicAWSCredentials("AKIAIWQOW2UUYPMOG2VQ","T3NHKV0FayMEeHHpwixdguEE7RIPyZywHdESE1ps");
	        AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(creds);
	        DynamoDBMapper mapper = new DynamoDBMapper(dyndbclient);
	        
	        HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
	        eav.put(":n", new AttributeValue().withN(year));

	        DynamoDBQueryExpression<Battery> queryExpression = new DynamoDBQueryExpression<Battery>()
	        	.withIndexName("MakeSearch-index")
	            .withConsistentRead(false)
	            .withKeyConditionExpression("ModelYear = :n")
	            .withExpressionAttributeValues(eav);

	        List<Battery> makeList = mapper.query(Battery.class, queryExpression);
	        Map<String, String> make = new LinkedHashMap<String, String>();
	        for(Battery battery : makeList){
	        	make.put(String.valueOf(battery.getMake()), String.valueOf(battery.getMake()));
			}
			BSROWebServiceResponse response = new BSROWebServiceResponse();
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(make);
	        
	        return response;
	    }

}
