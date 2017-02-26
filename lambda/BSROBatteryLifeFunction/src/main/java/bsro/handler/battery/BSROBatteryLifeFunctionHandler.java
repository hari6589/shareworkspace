package bsro.handler.battery;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import bsro.dynamodb.mapper.battery.engine.BatteryLife;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BSROBatteryLifeFunctionHandler implements RequestHandler<Object, Object> {

    public Object handleRequest(Object input, Context context) {
    	context.getLogger().log("Input: " + input);
    	
    	HashMap<String, String> mapInput = (HashMap<String, String>) input;
		String zip = mapInput.get("zip");
		
        AWSCredentials creds = new BasicAWSCredentials("AKIAIWQOW2UUYPMOG2VQ","T3NHKV0FayMEeHHpwixdguEE7RIPyZywHdESE1ps");
        AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(creds);
        DynamoDBMapper mapper = new DynamoDBMapper(dyndbclient);
        
        BSROWebServiceResponse response = new BSROWebServiceResponse();
        
        if(zip == null || zip.isEmpty() || zip.toString().length() < 5){
			response.setMessage("Invalid Zip Code");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
        
		String zip3 = zip.toString().substring(0,3);
        BatteryLife batterylife = mapper.load(BatteryLife.class, zip3, 
                new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT));
        
        long lifeMonth = 0;
		long lifeYear = 0;
		long lifeMonthMode = 0;
		Map<String, String> batteryLife = new LinkedHashMap<String, String>();
		
		if (batterylife != null) {
			lifeMonth = Math.round(batterylife.getBatteryLifeAverage().doubleValue());
			lifeYear = (long) (lifeMonth / 12);
			lifeMonthMode = lifeMonth % 12;
			batteryLife.put("lifeYear", String.valueOf(lifeYear));
			batteryLife.put("lifeMonth", String.valueOf(lifeMonthMode));
			batteryLife.put("lifeTotMonth", String.valueOf(lifeMonth));
		}
		
		if(batteryLife.isEmpty()){
			response.setMessage("NoDataFound");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
       
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(batteryLife);
      
        return response;
    }

}
