/**
 * 
 */
package com.bsro.api.rest.ws.vehicle;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.battery.Battery;
import app.bsro.model.battery.BatteryQuote;
import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.svc.vehicle.InterstateBatteryService;
import com.bsro.core.exception.ws.BSROWebServiceUnexpectedErrorResponseException;
import com.bsro.core.security.RequireValidAppNameAndToken;
import com.bsro.core.security.RequireValidToken;

import java.util.LinkedHashMap;
import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.interstatebattery.BatteryLifeDuration;

/**
 * @author schowdhu
 *
 */
@Component
public class BatteryVehicleWebServiceImpl implements BatteryVehicleWebService {
	
	@Autowired
	InterstateBatteryService interstateBatteryService;
	
	private static final String INVALID_ZIPCODE = "Invalid Zip Code";
	private static final String NO_DATA_FOUND = "NoDataFound";

	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-engine/years")
	public BSROWebServiceResponse getYearOptions(HttpHeaders headers)
			throws BSROWebServiceUnexpectedErrorResponseException {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Map<String, String> dropdownMap = interstateBatteryService.getYears();
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(dropdownMap);
		return response;
	}


	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-engine/makes")
	public BSROWebServiceResponse getMakeOptionsByYear(HttpHeaders headers,String year) 
			throws BSROWebServiceUnexpectedErrorResponseException {

		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Map<String, String> dropdownMap = interstateBatteryService.getMakesByYear(year);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(dropdownMap);
		return response;
	}

	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-engine/models")
	public BSROWebServiceResponse getModelOptionsByYearAndMake(
			HttpHeaders headers, String year, String make)
			throws BSROWebServiceUnexpectedErrorResponseException {

		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Map<String, String> dropdownMap = interstateBatteryService.getModelsByYearAndMakeName(year, make);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(dropdownMap);
		return response;
	}


	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-engine/engine-sizes")
	public BSROWebServiceResponse getEngineOptionsByYearAndMakeAndModel(
			HttpHeaders headers, String year, String make, String model)
			throws BSROWebServiceUnexpectedErrorResponseException {

		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Map<String, String> dropdownMap = interstateBatteryService.getEngineSizesByYearAndMakeAndModel(year, make, model);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(dropdownMap);
		return response;
	}
	
	@Override
	@RequireValidAppNameAndToken(URL+"/get/search-results")
    public BSROWebServiceResponse getBatteryListByYearMakeModelAndEngineSize(HttpHeaders headers, String year, 
    		String make, String model, String engineSize) throws BSROWebServiceUnexpectedErrorResponseException {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if(StringUtils.isNullOrEmpty(year)) {
			return getValidationMessage("Invalid vehicle year");
		}
		if(StringUtils.isNullOrEmpty(make)) {
			return getValidationMessage("Invalid vehicle make");
		}
		if(StringUtils.isNullOrEmpty(model)) {
			return getValidationMessage("Invalid vehicle model");
		}
		if(StringUtils.isNullOrEmpty(engineSize)) {
			return getValidationMessage("Invalid engine size");
		}
		
		List<Battery> searchResults = 
				interstateBatteryService.getBatterySearchResults(year, make, model, engineSize);
		if(searchResults == null || searchResults.isEmpty()){
			response.setMessage(NO_DATA_FOUND);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(searchResults);
		return response;
	}


	@Override
	@RequireValidToken
	public BSROWebServiceResponse getBatteryQuoteByQuoteId(HttpHeaders headers, String quoteId, String rebateId, String siteName)
			throws BSROWebServiceUnexpectedErrorResponseException 
	{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if(!ServerUtil.isNullOrEmpty(quoteId) && !ServerUtil.isNullOrEmpty(siteName))
			{
			response = interstateBatteryService.getBatteryQuote(quoteId,rebateId,siteName);
			
			return response;
			}
		else
			{
			response.setMessage("quoteId and siteName should not empty or null");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
			}
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveBatteryQuote(HttpHeaders headers, String siteName, String storeNumber,  String productCode,  String zip,  String year, String make, String model,String engine) 
			throws BSROWebServiceUnexpectedErrorResponseException 
			{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		String donationName =null, donationArticle ="0",
					donationYOrN ="N", donationAmount ="0", donationAmountOther = null, 
					firstName =null, lastName =null;
		
			if (ServerUtil.isNullOrEmpty(storeNumber) || ServerUtil.isNullOrEmpty(productCode) || ServerUtil.isNullOrEmpty(year) || ServerUtil.isNullOrEmpty(model) || ServerUtil.isNullOrEmpty(make) || ServerUtil.isNullOrEmpty(engine) || ServerUtil.isNullOrEmpty(zip)) 
				{
					String errmsg = getErrorMessage(storeNumber,productCode,year,make,model,engine,zip);
					Errors errors = new Errors();
					errors.getGlobalErrors().add(errmsg);
					response.setErrors(errors);
					response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
					response.setPayload(null);
					return response;	
				}
				else
				{
					response = interstateBatteryService.saveBatteryQuote(siteName,storeNumber,productCode,donationName,donationArticle,zip,year,make,model,engine,donationYOrN,donationAmount,donationAmountOther,firstName,lastName);		
				}
				
			return response;
		}

	@Override
	@RequireValidToken
    public BSROWebServiceResponse getBatteryLife(HttpHeaders headers, String zip, String siteName) throws BSROWebServiceUnexpectedErrorResponseException {
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if(zip == null || zip.isEmpty()){
			response.setMessage(INVALID_ZIPCODE);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
		
		Map<String, String> batteryLife = interstateBatteryService.getBatteryLife(zip, siteName);
		
		if(batteryLife.isEmpty()){
			response.setMessage(NO_DATA_FOUND);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(batteryLife);
		return response;
	}
	
	private String getErrorMessage(String storeNumber, String productCode, String year, String make, String model, String engine, String zip) {
		StringBuffer buf = new StringBuffer();
		String errmsg = null;
		int errFieldsCount = 0;
		if (ServerUtil.isNullOrEmpty(storeNumber)) {
				buf.append("store number");
				++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(productCode)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("productCode");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(year)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle year");
			++errFieldsCount;
		}
		
		if (ServerUtil.isNullOrEmpty(make)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle make");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(model)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle model");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(engine)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle engine");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(zip)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Zip code");
			++errFieldsCount;
		}
		
		errmsg = (errFieldsCount == 1) ? "Missing parameter value for field " : "Missing parameter value for fields ";
		errmsg += buf.toString();
		return errmsg;
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
