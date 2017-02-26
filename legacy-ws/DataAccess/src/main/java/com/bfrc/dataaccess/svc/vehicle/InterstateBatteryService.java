/**
 * 
 */
package com.bfrc.dataaccess.svc.vehicle;

import java.util.List;
import java.util.Map;


import app.bsro.model.battery.Battery;
import app.bsro.model.battery.BatteryAutomobile;
import app.bsro.model.battery.BatteryQuote;
import app.bsro.model.webservice.BSROWebServiceResponse;



/**
 * @author schowdhu
 *
 */
public interface InterstateBatteryService {
	
	public Map<String,String> getYears();
	
	public Map<String,String> getMakesByYear(String year);
	
	public Map<String,String> getModelsByYearAndMakeName(String year, String makeName);
	
	public Map<String,String> getEngineSizesByYearAndMakeAndModel(String year, String makeName, 
			String modelName);
	
	public List<Battery> getBatterySearchResults(String year,
			String makeName, String modelName, String engineSize);

	public BSROWebServiceResponse getBatteryQuote(String quoteId, String rebateId, String siteName);

	public BSROWebServiceResponse saveBatteryQuote(String siteName, String storeNumber,
			String productCode, String donationName, String donationArticle,
			String zip, String year, String make, String model, String engine,
			String donationYOrN, String donationAmount,
			String donationAmountOther, String firstName, String lastName);
	
	public Map<String, String> getBatteryLife(String zip, String siteName);
}
