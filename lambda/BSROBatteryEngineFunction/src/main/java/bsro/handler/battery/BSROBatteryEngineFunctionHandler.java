package bsro.handler.battery;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bsro.dynamodb.mapper.battery.engine.Battery;
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

public class BSROBatteryEngineFunctionHandler implements RequestHandler<Object, Object> {

    public Object handleRequest(Object input, Context context) {
    	context.getLogger().log("Input: " + input);
    	
    	HashMap<String, String> mapInput = (HashMap<String, String>) input;
		String year = mapInput.get("year");
		String make = mapInput.get("make");
		String model = mapInput.get("model");
		
        AWSCredentials creds = new BasicAWSCredentials("AKIAIWQOW2UUYPMOG2VQ","T3NHKV0FayMEeHHpwixdguEE7RIPyZywHdESE1ps");
        AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(creds);
        DynamoDBMapper mapper = new DynamoDBMapper(dyndbclient);
        
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue> ();
        eav.put(":s", new AttributeValue().withS(year+"-"+make.toLowerCase()+"-"+model.toLowerCase()));
        
        DynamoDBQueryExpression<Battery> queryExpression = new DynamoDBQueryExpression<Battery>()
        		.withIndexName("EngineSearch-index")
		        .withConsistentRead(false)
		        .withKeyConditionExpression("EngineSearch = :s")
		        .withExpressionAttributeValues(eav);

        List<Battery> engineList = mapper.query(Battery.class, queryExpression);
        Map<String, String> engine = new LinkedHashMap<String, String>();
		for(Battery battery : engineList){
			engine.put(String.valueOf(battery.getEngine()), String.valueOf(battery.getEngine()));
		}
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(engine);
      
        return response;
    }

}
