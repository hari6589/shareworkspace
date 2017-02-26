package bsro.handler.battery;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bsro.dynamodb.mapper.battery.year.Battery;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BSROBatteryYearFunctionHandler implements RequestHandler<Object, Object> {

	 public Object handleRequest(Object input, Context context) {
	        context.getLogger().log("Input: " + input);

	        AWSCredentials creds = new BasicAWSCredentials("AKIAIWQOW2UUYPMOG2VQ","T3NHKV0FayMEeHHpwixdguEE7RIPyZywHdESE1ps");
	        AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(creds);
	        DynamoDBMapper mapper = new DynamoDBMapper(dyndbclient);
	        
	        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
			List<Battery> yearList = mapper.scan(Battery.class,scanExpression);
			Map<String, String> year = new LinkedHashMap<String, String>();
			for(Battery battery : yearList){
				year.put(String.valueOf(battery.getYear()), String.valueOf(battery.getYear()));
			}
			BSROWebServiceResponse response = new BSROWebServiceResponse();
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(year);
	        return response;
	    }

}
