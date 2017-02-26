package bsro.handler.battery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import bsro.dynamodb.mapper.battery.result.Product;
import bsro.dynamodb.mapper.battery.result.Battery;
import bsro.dynamodb.mapper.battery.result.Result;
import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;
import bsro.webservice.error.Errors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BSROBatteryResultFunctionHandler implements RequestHandler<Object, Object> {

    public Object handleRequest(Object input, Context context) {
    	context.getLogger().log("Input: " + input);
    	
    	HashMap<String, String> mapInput = (HashMap<String, String>) input;
		String year = mapInput.get("year");
		String make = mapInput.get("make");
		String model = mapInput.get("model");
		String engine = mapInput.get("engine");
		
		if(isNullOrEmpty(year)) {
			return getValidationMessage("Invalid vehicle year");
		}
		if(isNullOrEmpty(make)) {
			return getValidationMessage("Invalid vehicle make");
		}
		if(isNullOrEmpty(model)) {
			return getValidationMessage("Invalid vehicle model");
		}
		if(isNullOrEmpty(engine)) {
			return getValidationMessage("Invalid engine size");
		}
		
        AWSCredentials creds = new BasicAWSCredentials("AKIAIWQOW2UUYPMOG2VQ","T3NHKV0FayMEeHHpwixdguEE7RIPyZywHdESE1ps");
        AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(creds);
        DynamoDBMapper mapper = new DynamoDBMapper(dyndbclient);
        
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue> ();
        eav.put(":s", new AttributeValue().withS(year+"-"+make.toLowerCase()+"-"+model.toLowerCase()+"-"+engine.toLowerCase()));
        
        DynamoDBQueryExpression<Battery> queryExpression = new DynamoDBQueryExpression<Battery>()
        		.withIndexName("SearchResult-index")
		        .withConsistentRead(false)
		        .withKeyConditionExpression("SearchResult = :s")
		        .withExpressionAttributeValues(eav);

        List<Battery> batteryList = mapper.query(Battery.class, queryExpression);
        List<Product> productList = getProductList(mapper,batteryList);
        
        Map<String, Product> productCodeMap = new HashMap<String, Product> ();
    	for(Product p : productList){
    		productCodeMap.put(p.getProductCode(), p);
    	}
    	List<Result> results = getProductResult(batteryList,productCodeMap);
    	
    	BSROWebServiceResponse response = new BSROWebServiceResponse();
    	if(results == null || results.isEmpty()){
			response.setMessage("NoDataFound");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(results);
      
        return response;
    }
    
    public List<Product> getProductList(DynamoDBMapper mapper, List<Battery> batteryList){
    	 Map<String, AttributeValue> productCode = new HashMap<String, AttributeValue> ();
    	 Set<String> productExpression = new HashSet<String>();
    	 Set<String> productCheck = new HashSet<String>();
    	 int count=1;
    	 for(Battery battery : batteryList){
    		if(!productCheck.contains(battery.getProductCode())){
    			productCode.put(String.valueOf(":s"+count), new AttributeValue().withS(battery.getProductCode()));
    			productCheck.add(String.valueOf(battery.getProductCode()));
    			productExpression.add("ProductCode = "+String.valueOf(":s"+count));
    			count++;
    		}
    	}
    	DynamoDBQueryExpression<Product> queryExpression = new DynamoDBQueryExpression<Product>()
        		.withConsistentRead(false)
		        .withKeyConditionExpression(productExpression.toString().replace("[", "").replace("]", "").replace(",", " or "))
		        .withExpressionAttributeValues(productCode);
    	List<Product> productList = mapper.query(Product.class, queryExpression);
    	
    	return productList;
    }
    
    public List<Result> getProductResult(List<Battery> batteryList,Map<String, Product> productCodeMap){
    	List<Result> result =  new ArrayList<Result>();
    	Map<String, List<Battery>> productTypeGrouping = new HashMap<String, List<Battery>> ();
    	Map<String, String> productTypeCCA = new HashMap<String, String> ();
    	
    	for(Battery b : batteryList){
    		if(productCodeMap.containsKey(b.getProductCode())){
    			String productType = b.getProductCode().split("-")[0];
    			if(!productTypeGrouping.containsKey(productType)){
    				List<Battery> batteriesForProdtype =  new ArrayList<Battery>();
    				batteriesForProdtype.add(b);
    				productTypeGrouping.put(productType, batteriesForProdtype);
    				productTypeCCA.put(productType, b.getCca());
    			}else if(productTypeCCA.get(productType)!=null && productTypeCCA.get(productType).equals(b.getCca())){
    				productTypeGrouping.get(productType).add(b);
    			}
    		}
    	}
    	
    	for (Entry<String, List<Battery>> entry : productTypeGrouping.entrySet()) {
    	    if(entry.getValue().size()>1){
    	    	Random rand = new Random();
    	    	int r = rand.nextInt(entry.getValue().size());
    	    	Result resultObject = getResultObject(entry.getValue().get(r),productCodeMap.get(entry.getValue().get(r).getProductCode()));
    	    	result.add(resultObject);
    	    }else if(entry.getValue().size()>0){
    	    	Result resultObject = getResultObject(entry.getValue().get(0),productCodeMap.get(entry.getValue().get(0).getProductCode()));
    	    	result.add(resultObject);
    	    }
    	}
		return result;
   }
    
    public Result getResultObject(Battery battery,Product product){
    	Result result = new Result();
    		result.setProductCode(product.getProductCode());
    		result.setProductName(product.getProductName());
	    	result.setProduct(product.getProductType());
	    	result.setPartNumber(product.getPartNumber());
	    	result.setTotalWarrantyMonths(product.getTotalWarranty());
	    	result.setReplacementWarrantyMonths(product.getReplacementWarranty());
	    	result.setPerformanceWarrantyMonths(product.getTotalWarranty());
	    	result.setColdCrankingAmps(product.getCca());
	    	result.setReserveCapacityMinutes(product.getRcMinutes());
	    	result.setWebPrice(product.getWebPrice());
	    	result.setTradeInCredit(product.getTradeinCredit());
	    	result.setInstallationAmount(product.getInstallationAmount());
	    	result.setSalesText(product.getSalesText());
	    	result.setRegularPrice(product.getRegularPrice());
	    	result.setDiscountAmount(product.getDiscountAmount());
	    	result.setProductOption(battery.getProductOption());
		return result;
   }
    
    public static boolean isNullOrEmpty(String str) {
        
        if (str == null || str.trim().equals("") || str.trim().equals("null")) {
            return true;
        } 
        return false;
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

}
