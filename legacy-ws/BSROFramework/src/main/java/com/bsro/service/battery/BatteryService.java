package com.bsro.service.battery;

import java.util.List;
import java.util.Map;

import app.bsro.model.battery.Battery;
import app.bsro.model.battery.BatteryQuote;

import com.bfrc.framework.dao.InterstateBatteryDAO;

public interface BatteryService {
	public static final String CURRENT_DONATION_NAME = "Round Up to Help Out";
	public static final String CURRENT_DONATION_ARTICLE = "7009019";

	public static final String MEALS_ON_WHEELS_DONATION_NAME = "Meals on Wheels Donation";
	public static final String MEALS_ON_WHEELS_DONATION_ARTICLE ="7003125";
	
	public List<Battery> getFeaturedBatteries(String siteName);
	
	public Map<String, String> getBatteryNamesByProduct();
	
	public Long saveQuote(String siteName, String storeNumber, String productCode, String donationName, String donationArticle,
			String zip, String year, String make, String model, String engine, String donationYOrN, String donationAmount, String donationAmountOther, String firstName, String lastName) throws Exception;
	
	public void updateQuote(Long quoteId, Short quantity, String batteryRebateId) throws Exception;
	
	public Battery getBatteryByProductCode(String productCode);
	
	public BatteryQuote retrieveQuote(String quoteId, String batteryRebateId);
	
	public List<Battery> findBatteriesByVehicle(String year, String make, String model, String engine);

	public void setInterstateBatteryDAO(InterstateBatteryDAO batteryDAO);
	
	public Map<String, String> getYearOptions() throws Exception;
	
	public Map<String, String> getMakeOptionsByYear(String year) throws Exception;
	
	public Map<String, String> getModelOptionsByYearAndMakeName(String year, String makeName) throws Exception;
	
	public Map<String, String> getEngineOptionsByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception;
}
