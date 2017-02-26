package com.bsro.service.oil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.oil.OilChangeQuote;
import app.bsro.model.oil.OilChangeSearchResult;

import com.bsro.exception.errors.ErrorMessageException;

import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.LOFBSROWebserviceServiceImpl;



@Service("oilService")
public class OilServiceImpl extends LOFBSROWebserviceServiceImpl implements OilService {

	private static final String PATH_OIL_BASE = "/oil";

	private static final String PATH_FIND_YEARS = "/years";

	private static final String PATH_FIND_MODELS = "/models";

	private static final String PATH_FIND_MANUFACTURERS = "/manufacturers";

	private static final String PATH_FIND_PRODUCTS = "/products";
	
	private static final String PATH_CREATE_QUOTE = "/create-quote";
	
	private static final String PATH_GET_QUOTE = "/get-quote";
	
	private static final String PATH_UPDATE_QUOTE_FIRST_NAME = "/update-quote/first-name";
	
	private static final String PATH_UPDATE_QUOTE_LAST_NAME = "/update-quote/last-name";
	
	
	private static final String PARAM_YEAR_ID = "yearId";

	private static final String PARAM_MANUFACTURER_ID = "manufacturerId";
	
	private static final String PARAM_EQUIPMENT_ID = "equipmentId";
	
	private static final String PARAM_STORE_NUMBER = "storeNumber";
	
	private static final String PARAM_IS_HIGH_MILEAGE_VEHICLE = "isHighMileageVehicle";

	private static final String PARAM_OIL_ARTICLE_NUMBER = "oilArticleNumber";
	
	private static final String PARAM_QUARTS = "quarts";
	
	private static final String PARAM_VEHICLE_ID = "vehicleId";
	
	private static final String PARAM_VEHICLE_YEAR = "vehicleYear";
	
	private static final String PARAM_VEHICLE_MAKE = "vehicleMake";
	
	private static final String PARAM_VEHICLE_MODEL_SUBMODEL_ENGINE = "vehicleModelSubmodelEngine";
	
	private static final String PARAM_CUSTOMER_ZIP = "customerZip";
	
	private static final String PARAM_SITE_NAME = "siteName";
	
	private static final String PARAM_OIL_CHANGE_QUOTE_ID = "oilChangeQuoteId";
	
	private static final String PARAM_FIRST_NAME = "firstName";
	
	private static final String PARAM_LAST_NAME = "lastName";
	
	
	private static final Map<String,String> MILEAGE_OPTIONS = new LinkedHashMap<String,String>();
	
	static {
		MILEAGE_OPTIONS.put("0", "0-74,999");
		MILEAGE_OPTIONS.put("1", "75,000+");
	}
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}

	@Override
	public Map<String, String> getYears() throws IOException {
		  Map<String, String> parameters = new LinkedHashMap<String, String>();
		  return (LinkedHashMap<String, String>) getWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_FIND_YEARS, parameters, LinkedHashMap.class);
		
	}

	@Override
	public Map<String, String> getManufacturers(String yearId) throws IOException {
		Map<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put(PARAM_YEAR_ID, yearId);
		return (LinkedHashMap<String, String>) getWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_FIND_MANUFACTURERS, parameters, LinkedHashMap.class);
		
		
		
	}

	@Override
	public Map<String, String> getModels(String yearId, String manufacturerId) throws IOException {
		
		
		
		

		 Map<String, String> parameters = new LinkedHashMap<String, String>();

		parameters.put(PARAM_YEAR_ID, yearId);
		parameters.put(PARAM_MANUFACTURER_ID, manufacturerId);

		return (LinkedHashMap<String, String>) getWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_FIND_MODELS, parameters, LinkedHashMap.class);
		
		
	}

	@Override
	public Map<String, String> getMileages() {		
		return MILEAGE_OPTIONS;
	}
	
	@Override
	public OilChangeSearchResult getProducts(String equipmentId, Long storeNumber, Boolean isHighMileageVehicle) throws IOException {

		Map<String, String> parameters = new HashMap<String, String>();
		
		

		parameters.put(PARAM_EQUIPMENT_ID, equipmentId);
		parameters.put(PARAM_STORE_NUMBER, storeNumber.toString());
		parameters.put(PARAM_IS_HIGH_MILEAGE_VEHICLE, isHighMileageVehicle.toString());

		return (OilChangeSearchResult) getWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_FIND_PRODUCTS, parameters, OilChangeSearchResult.class);
	}

	@Override
	 public Long createOilChangeQuote(Long oilArticleNumber, Long storeNumber, BigDecimal quarts, String vehicleId, Integer vehicleYear, String vehicleMake, String vehicleModelSubmodelEngine, String customerZip,String siteName, Boolean isHighMileage) throws IOException, ErrorMessageException {
		Map<String, String> parameters = new HashMap<String, String>();

		if (oilArticleNumber != null) {
			parameters.put(PARAM_OIL_ARTICLE_NUMBER, Long.toString(oilArticleNumber));
		}
		if (storeNumber != null) {
			parameters.put(PARAM_STORE_NUMBER, Long.toString(storeNumber));
		}
		if (quarts != null) {
			parameters.put(PARAM_QUARTS, quarts.toString());
		}
		if (isHighMileage != null && isHighMileage) {
			parameters.put(PARAM_IS_HIGH_MILEAGE_VEHICLE, isHighMileage.toString());
		}
		parameters.put(PARAM_VEHICLE_ID, vehicleId);
		if (vehicleYear != null) {
			parameters.put(PARAM_VEHICLE_YEAR, Integer.toString(vehicleYear));
		}
		parameters.put(PARAM_VEHICLE_MAKE, vehicleMake);
		parameters.put(PARAM_VEHICLE_MODEL_SUBMODEL_ENGINE, vehicleModelSubmodelEngine);
		parameters.put(PARAM_CUSTOMER_ZIP, customerZip);
		parameters.put(PARAM_SITE_NAME, siteName);
		
		return (Long) postWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_CREATE_QUOTE, parameters, Long.class, true);
	 }

	public OilChangeQuote getOilChangeQuote(Long oilChangeQuoteId, String siteName) throws IOException, ErrorMessageException {
		Map<String, String> parameters = new HashMap<String, String>();

		if (oilChangeQuoteId != null) {
			parameters.put(PARAM_OIL_CHANGE_QUOTE_ID, Long.toString(oilChangeQuoteId));
			parameters.put(PARAM_SITE_NAME, siteName);
		}
		
		return (OilChangeQuote) getWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_GET_QUOTE, parameters, OilChangeQuote.class, true);
	}
	
	public Boolean updateOilChangeQuoteFirstName(Long oilChangeQuoteId, String siteName, String firstName) throws IOException, ErrorMessageException {
		Map<String, String> parameters = new HashMap<String, String>();

		if (oilChangeQuoteId != null) {
			parameters.put(PARAM_OIL_CHANGE_QUOTE_ID, Long.toString(oilChangeQuoteId));
			parameters.put(PARAM_SITE_NAME, siteName);
			parameters.put(PARAM_FIRST_NAME, firstName);
		}
		
		String response = postWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_UPDATE_QUOTE_FIRST_NAME, parameters, String.class, true);
		
		return Boolean.parseBoolean(response);
	}

	public Boolean updateOilChangeQuoteLastName(Long oilChangeQuoteId, String siteName, String lastName) throws IOException, ErrorMessageException {
		Map<String, String> parameters = new HashMap<String, String>();

		if (oilChangeQuoteId != null) {
			parameters.put(PARAM_OIL_CHANGE_QUOTE_ID, Long.toString(oilChangeQuoteId));
			parameters.put(PARAM_SITE_NAME, siteName);
			parameters.put(PARAM_LAST_NAME, lastName);
		}
		
		String response = postWebservice(PATH_WEBSERVICE_BASE + PATH_OIL_BASE + PATH_UPDATE_QUOTE_LAST_NAME, parameters, String.class, true);
		return Boolean.parseBoolean(response);
	}

}
