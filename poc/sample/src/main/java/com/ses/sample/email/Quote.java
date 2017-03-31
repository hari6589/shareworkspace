package com.ses.sample.email;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import tirequote.Store;
import tirequote.TireQuote;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Quote {
	
	public static AWSCredentials awsCredentials = null;
	public static DynamoDBMapper dynamoDBMapper = null;
	public static DynamoDB dynamoDB = null;
	
	Quote() {
		try {
			awsCredentials = new BasicAWSCredentials("AKIAIO6KVA4ID6VFURAA", "aaSNiXJVyTafsNBJfuLXp0+/KeiPbUUeel54XcKF");
			AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(awsCredentials);
		    dynamoDB = new DynamoDB(dyndbclient);
		} catch (Exception e) {
			System.out.println("Exception  in BSROScheduleAppointmentFunctionHandler Constructor!");
		}
	}
	
	public TireQuote getTireQuote(String quoteId) throws JSONException, JsonParseException, JsonMappingException, IOException {
		TireQuote tireQuote = null;
		/*
		Table table = dynamoDB.getTable("TireQuote");
		GetItemSpec spec = new GetItemSpec()
			.withPrimaryKey("quoteId", Long.parseLong(quoteId))
			.withConsistentRead(true);
		Item item = table.getItem(spec);
		
		if(item.hasAttribute("payload")) {
			ObjectMapper mapper = new ObjectMapper();
			tireQuote = mapper.readValue(jsonStr, TireQuote.class);
		}*/
		
		String jsonStr = "{\"storeInventory\":{\"storeNumber\":\"23817\",\"articleNumber\":\"97725\",\"quantityOnOrder\":\"0\",\"quantityOnHand\":\"4\",\"inventoryId\":10318146},\"storeNumber\":\"23817\",\"createdDate\":\"2015-12-30T11:23:16.000+05:30\",\"quantity\":\"4\",\"quoteItem\":{\"totalTirePrice\":\"535.96\",\"unitPrice\":\"133.99\",\"scrapTireRecyclingCharge\":\"12.0\",\"total\":\"560.76\",\"wheelBalance\":\"59.96\",\"tpmsValveServiceKitLabor\":\"8.0\",\"shopSupplies\":\"4.08\",\"tpmsValveServiceKit\":\"27.96\",\"stateEnvironmentalFee\":\"10.0\",\"tax\":\"36.8\",\"totalUnits\":\"4\"},\"vehicleFitment\":{\"year\":\"2010\",\"submodel\":\"WT\",\"acesVehicleId\":\"143886\",\"model\":\"Colorado\",\"tpmsInd\":\"true\",\"make\":\"Chevrolet\"},\"tireQuoteId\":\"1061397\",\"tire\":{\"description\":\"DESTINATION LE 2 P-METRIC P215/70R16 99H\",\"tireBrandImage\":\"Destination_logo.png\",\"loadIndexPounds\":\"1709\",\"warrantyName\":\"Gold Pledge Limited Warranty\",\"standardOptional\":\"S\",\"loadIndex\":\"99\",\"frb\":\"B\",\"speedRating\":\"H\",\"brand\":\"Firestone\",\"mileage\":\"60000\",\"tireName\":\"Destination LE2 \",\"tireBrandName\":\"Destination\",\"tireImage\":\"DestinationLE2.png\",\"discontinued\":\"N\",\"generateCatalogPage\":\"Y\",\"technology\":\"NONE\",\"article\":\"97725\",\"speedRatingMPH\":\"130\",\"notBrandedProduct\":\"false\",\"tireClassName\":\"CUV/SUV Highway All-Season\",\"tireType\":\"SUV/CUV\",\"tireGroupName\":\"Light Truck Tires\",\"tireSize\":\"P215/70R16\",\"retailPrice\":\"133.99\",\"sidewallDescription\":\"Black Letter/Black Wall\"}}";
		ObjectMapper mapper = new ObjectMapper();
		tireQuote = mapper.readValue(jsonStr, TireQuote.class);
		
		return tireQuote;
	}
	
	public Store getStoreDetail(String storeNumber) throws JsonParseException, JsonMappingException, IOException {
		String storeStr = "{\"storeNumber\":\"11940\",\"areaId\":\"PXPR\",\"areaName\":\"PEORIA AREA\",\"regionId\":\"PX02\",\"regionName\":\"Southwest \",\"divisionId\":\"SD\",\"divisionName\":\"South \",\"storeName\":\"Firestone Complete Auto Care\",\"address\":\"3540 W Bell Rd\",\"city\":\"Glendale\",\"state\":\"AZ\",\"zip\":\"85308-4314\",\"phone\":\"(602) 904-8002\",\"trackingPhone\":\"(602) 904-8002\",\"managerName\":\"Daniel Debusk\",\"managerEmailAddress\":\"011940@bfrc.com\",\"storeType\":\"FCAC\",\"activeFlag\":\"1\",\"latitude\":\"33.639645\",\"longitude\":\"-112.1358\",\"onlineAppointmentActiveFlag\":\"0\",\"tirePricingActiveFlag\":\"1\",\"fax\":\"(602) 938-0554\",\"localPageURL\":\"http://firestone.staging.getlocal.co/location/11940\",\"eCommActiveFlag\":\"1\",\"hours\":[{\"weekDay\":\"SUN\",\"openTime\":\"08:00\",\"closeTime\":\"17:00\"},{\"weekDay\":\"MON\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"TUE\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"WED\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"THU\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"FRI\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"SAT\",\"openTime\":\"07:00\",\"closeTime\":\"18:00\"}],\"holidays\":[{\"year\":\"2017\",\"month\":\"4\",\"day\":\"16\",\"holidayId\":\"6\",\"description\":\"Easter\"}]}";
		ObjectMapper mapper = new ObjectMapper();
		Store store = mapper.readValue(storeStr, Store.class);
		return store;
	}
}
