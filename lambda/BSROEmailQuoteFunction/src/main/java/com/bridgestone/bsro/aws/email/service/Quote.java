package com.bridgestone.bsro.aws.email.service;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.bridgestone.bsro.aws.email.model.alignment.AlignmentPricingQuote;
import com.bridgestone.bsro.aws.email.model.battery.BatteryQuote;
import com.bridgestone.bsro.aws.email.model.tire.Store;
import com.bridgestone.bsro.aws.email.model.tire.TireQuote;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Quote {
	
	public static AWSCredentials awsCredentials = null;
	public static DynamoDBMapper dynamoDBMapper = null;
	public static DynamoDB dynamoDB = null;
	
	public Quote() {
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
		
		Table table = dynamoDB.getTable("TireQuote");
		System.out.println("Quote Id: " + quoteId);
		GetItemSpec spec = new GetItemSpec()
			.withPrimaryKey("quoteId", Long.parseLong(quoteId))
			.withConsistentRead(true);
		Item item = table.getItem(spec);
		
		if(item.hasAttribute("payload")) {
			JSONObject jsonObj = new JSONObject(item.getJSON("payload"));
			System.out.println(">>" + jsonObj.getString("payload"));
			ObjectMapper mapper = new ObjectMapper();
			tireQuote = mapper.readValue(jsonObj.getString("payload"), TireQuote.class);
		}
		
		/*
		String jsonStr = "{\"storeInventory\":{\"storeNumber\":\"23817\",\"articleNumber\":\"97725\",\"quantityOnOrder\":\"0\",\"quantityOnHand\":\"4\",\"inventoryId\":10318146},\"storeNumber\":\"23817\",\"createdDate\":\"2015-12-30T11:23:16.000+05:30\",\"quantity\":\"4\",\"quoteItem\":{\"totalTirePrice\":\"535.96\",\"unitPrice\":\"133.99\",\"scrapTireRecyclingCharge\":\"12.0\",\"total\":\"560.76\",\"wheelBalance\":\"59.96\",\"tpmsValveServiceKitLabor\":\"8.0\",\"shopSupplies\":\"4.08\",\"tpmsValveServiceKit\":\"27.96\",\"stateEnvironmentalFee\":\"10.0\",\"tax\":\"36.8\",\"totalUnits\":\"4\"},\"vehicleFitment\":{\"year\":\"2010\",\"submodel\":\"WT\",\"acesVehicleId\":\"143886\",\"model\":\"Colorado\",\"tpmsInd\":\"true\",\"make\":\"Chevrolet\"},\"tireQuoteId\":\"1061397\",\"tire\":{\"description\":\"DESTINATION LE 2 P-METRIC P215/70R16 99H\",\"tireBrandImage\":\"Destination_logo.png\",\"loadIndexPounds\":\"1709\",\"warrantyName\":\"Gold Pledge Limited Warranty\",\"standardOptional\":\"S\",\"loadIndex\":\"99\",\"frb\":\"B\",\"speedRating\":\"H\",\"brand\":\"Firestone\",\"mileage\":\"60000\",\"tireName\":\"Destination LE2 \",\"tireBrandName\":\"Destination\",\"tireImage\":\"DestinationLE2.png\",\"discontinued\":\"N\",\"generateCatalogPage\":\"Y\",\"technology\":\"NONE\",\"article\":\"97725\",\"speedRatingMPH\":\"130\",\"notBrandedProduct\":\"false\",\"tireClassName\":\"CUV/SUV Highway All-Season\",\"tireType\":\"SUV/CUV\",\"tireGroupName\":\"Light Truck Tires\",\"tireSize\":\"P215/70R16\",\"retailPrice\":\"133.99\",\"sidewallDescription\":\"Black Letter/Black Wall\"}}";
		ObjectMapper mapper = new ObjectMapper();
		tireQuote = mapper.readValue(jsonStr, TireQuote.class);
		*/
		
		return tireQuote;
	}
	
	public BatteryQuote getBatteryQuote(String quoteId) throws JSONException, JsonParseException, JsonMappingException, IOException {
		/*
		Table table = dynamoDB.getTable("InterstateBatteryQuote");
		System.out.println("Quote Id: " + quoteId);
		GetItemSpec spec = new GetItemSpec()
			.withPrimaryKey("quoteId", Long.parseLong(quoteId))
			.withConsistentRead(true);
		Item item = table.getItem(spec);
		System.out.println("To JSON: " + item.toJSON());
		ObjectMapper mapper = new ObjectMapper();
		BatteryQuote batteryQuote = mapper.readValue(item.toJSON(), BatteryQuote.class);
		*/
		
		String jsonStr = "{\"batteryQuoteId\":\"9038\",\"storeNumber\":\"11940\",\"zip\":\"85308\",\"createdDate\":\"2016-06-01T12:48:52.000-05:00\",\"donationName\":\"0\",\"donationAmount\":\"0.0\",\"donationArticle\":\"\",\"quantity\":\"1\",\"priceForQuantity\":\"99.99\",\"installationForQuantity\":\"14.99\",\"subtotal\":\"114.98\",\"total\":\"114.98\",\"isEligibleForBatteryRebate\":\"false\",\"battery\":{\"productName\":\"Mega-Tron II\",\"productCode\":\"MT-24F\",\"product\":\"MT\",\"partNumber\":\"7005423\",\"totalWarrantyMonths\":\"60\",\"replacementWarrantyMonths\":\"24\",\"performanceWarrantyMonths\":\"60\",\"coldCrankingAmps\":\"600\",\"reserveCapacityMinutes\":\"110\",\"webPrice\":\"99.99\",\"tradeInCredit\":\"20\",\"installationAmount\":\"14.99\",\"salesText\":\"Get long life and premium performance with the Maintenance-Free Mega-Tron II, backed by 24 months free replacement and a 5-year performance warranty.\",\"regularPrice\":\"119.99\",\"discountAmount\":\"0\",\"hasPricing\":\"true\",\"expirayDate\":null},\"vehicle\":{\"year\":\"2012\",\"make\":\"Acura\",\"model\":\"MDX\",\"engine\":\"V6/3.7L\"}}";
		ObjectMapper mapper = new ObjectMapper();
		BatteryQuote batteryQuote = mapper.readValue(jsonStr, BatteryQuote.class);
		
		return batteryQuote;
	}
	
	public AlignmentPricingQuote getAlignmentPricingQuote(String quoteId) throws JSONException, JsonParseException, JsonMappingException, IOException {
		String batteryStr = "{\"alignmentQuoteId\":\"4835\",\"storeNumber\":\"23817\",\"vehicleYear\":\"2010\",\"vehicleMake\":\"Hyundai\",\"vehicleModel\":\"Accent\",\"vehicleSubmodel\":\"GLS\",\"zip\":\"60139-2166\",\"alignmentPricingId\":\"0\",\"pricingName\":\"Standard Pricing\",\"article\":\"7009500\",\"price\":\"79.99\",\"webSite\":\"FCAC\",\"createdDate\":\"2016-01-20T07:57:53.000-06:00\"}";
		ObjectMapper mapper = new ObjectMapper();
		AlignmentPricingQuote alignmentPricingQuote = mapper.readValue(batteryStr, AlignmentPricingQuote.class);
		return alignmentPricingQuote;
	}
	public Store getStoreDetail(String storeNumber) throws JsonParseException, JsonMappingException, IOException {
		String storeStr = "{\"storeNumber\":\"11940\",\"areaId\":\"PXPR\",\"areaName\":\"PEORIA AREA\",\"regionId\":\"PX02\",\"regionName\":\"Southwest \",\"divisionId\":\"SD\",\"divisionName\":\"South \",\"storeName\":\"Firestone Complete Auto Care\",\"address\":\"3540 W Bell Rd\",\"city\":\"Glendale\",\"state\":\"AZ\",\"zip\":\"85308-4314\",\"phone\":\"(602) 904-8002\",\"trackingPhone\":\"(602) 904-8002\",\"managerName\":\"Daniel Debusk\",\"managerEmailAddress\":\"011940@bfrc.com\",\"storeType\":\"FCAC\",\"activeFlag\":\"1\",\"latitude\":\"33.639645\",\"longitude\":\"-112.1358\",\"onlineAppointmentActiveFlag\":\"0\",\"tirePricingActiveFlag\":\"1\",\"fax\":\"(602) 938-0554\",\"localPageURL\":\"http://firestone.staging.getlocal.co/location/11940\",\"eCommActiveFlag\":\"1\",\"hours\":[{\"weekDay\":\"SUN\",\"openTime\":\"08:00\",\"closeTime\":\"17:00\"},{\"weekDay\":\"MON\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"TUE\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"WED\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"THU\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"FRI\",\"openTime\":\"07:00\",\"closeTime\":\"19:00\"},{\"weekDay\":\"SAT\",\"openTime\":\"07:00\",\"closeTime\":\"18:00\"}],\"holidays\":[{\"year\":\"2017\",\"month\":\"4\",\"day\":\"16\",\"holidayId\":\"6\",\"description\":\"Easter\"}]}";
		//ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ObjectMapper mapper = new ObjectMapper();
		Store store = mapper.readValue(storeStr, Store.class);
		//store.setStoreHour(getStoreHourHTML(store.getHours(), true));
		return store;
	}
	
	/*
	public static final String DAY_ABBREV[] = {
        "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"
    };
	
	public String getStoreHourHTML(Collection<StoreHour> storeHours, boolean isFormatted) {
		String out = "";
		if (storeHours == null || storeHours.isEmpty())
			return out;
		for (int i=0; i<7; i++) {
			StoreHour curr = getStoreHour(storeHours, DAY_ABBREV[i]);
			if (curr == null)
				continue;
			for (int j=i+1; j<=7; j++) {
				StoreHour test = null;
				if (j<7)
					test = getStoreHour(storeHours, DAY_ABBREV[j]);
				if (test != null && test.equals(curr))
                    continue;
				if (isFormatted)
					out = out + getHourFormattedHTML(i, j - 1, curr);
				else
					out = out + getHourHTML(i, j - 1, curr);
				i = j - 1;
				break;
			}		
		}
		return out;		
	}
	
	public StoreHour getStoreHour(Collection<StoreHour> l, String day) {
		System.out.println("getStoreHour");
		StoreHour hour = null;
		
		System.out.println(l.size() + " " + day);
		for (StoreHour temp: l) {
			System.out.println(temp.getId().getWeekDay());
		}
		
		for (StoreHour temp: l) {
			if (temp != null && temp.getId().getWeekDay().equals(day)) {
				hour = temp;
				break;
			}
		}
		return hour;
	}
	
	public String getHourFormattedHTML(int begin, int end, StoreHour hour) {
		if(hour == null || hour.getOpenTime() == null || hour.getCloseTime() == null)
            return "";
        String out = "<b>" + DAY_ABBREV[begin];
        if(end > begin)
        {
            out = out + "&#45;" + DAY_ABBREV[end];            
        }
        return out + ":</b> " + format(hour.getOpenTime()) + "&#45;" + format(hour.getCloseTime()) + "<br />";
	}
	
	public String getHourHTML(int begin, int end, StoreHour hour) {
		if(hour == null || hour.getOpenTime() == null || hour.getCloseTime() == null)
            return "";
        String out = DAY_ABBREV[begin];
        if(end > begin)
        {
            out = out + "&#45;" + DAY_ABBREV[end];            
        }
        return out + " " + format(hour.getOpenTime()) + "&#45;" + format(hour.getCloseTime()) + "<br />";
	}
	
	public static String format(String time) {
        String temp = pad(time);
        int hour = Integer.valueOf(temp.substring(0, 2)).intValue();
        String m = "am";
        if(hour > 11)
        {
            m = "pm";
            if(hour > 12)
                hour -= 12;
        }
        String out = Integer.toString(hour) + ":" + temp.substring(3, 5);
        return out + m;
    }
	
	public static String pad(String param) {
        if(param == null)
            return null;
        String temp = param.trim();
        if(temp.length() < 5)
            return "0" + temp;
        return temp;
    }*/
}
