package com.bsro.api.rest.ws.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.ValidationErrorList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.store.StoreFlags;
import com.bfrc.dataaccess.model.store.StoreLocation;
import com.bfrc.dataaccess.svc.webdb.StoreService;
import com.bfrc.framework.util.StringUtils;
import com.bsro.core.security.RequireValidToken;
import com.bsro.core.exception.ws.InvalidArgumentException;

/**
 * @author smoorthy
 *
 */
@Component
public class StoresWebServiceImpl implements StoresWebService {
	
	@Autowired
	StoreService storeService;
	
	private Logger log = Logger.getLogger(getClass().getName());

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreDetails(Long storeNumber, HttpHeaders headers) {
		log.fine("Returning details for store "+storeNumber);
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(storeNumber)) {
			return getValidationMessage("Invalid store number");
		}
		
		Store store = storeService.findStoreById(storeNumber);
		if (StringUtils.isNullOrEmpty(store)) {
			return getValidationMessage("No store details found for store number "+storeNumber);
		}
		
		response = new BSROWebServiceResponse();
		response.setPayload(store);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreDetailsByType(String storeType, HttpHeaders headers) {
		log.fine("Returning details for store type "+storeType);
		BSROWebServiceResponse response = null;
		if (StringUtils.isNullOrEmpty(storeType)) {
			return getValidationMessage("Invalid store type");
		}
		
		List<Store> stores = storeService.findStoresByType(storeType);
		if (StringUtils.isNullOrEmpty(stores) || stores.isEmpty()) {
			return getValidationMessage("No store details found for store type "+storeType);
		}
		
		response = new BSROWebServiceResponse();
		response.setPayload(stores);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreLocationByGeoPoint(String lat, String lng, String siteName, Integer storeCount, HttpHeaders headers) {
		log.fine("Returning store location details for lat: "+lat+", lng: "+lng+", siteName: "+siteName);
		BSROWebServiceResponse response = null;
		
		if (StringUtils.isNullOrEmpty(lat) || StringUtils.isNullOrEmpty(lng) || StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Missing input parameters.");
		}
		
		List<StoreLocation> storeLocations = storeService.findClosestStoresByGeoPoints(lat, lng, siteName, storeCount);
		if (StringUtils.isNullOrEmpty(storeLocations) || storeLocations.isEmpty()) {
			return getValidationMessage("No stores found for lat: "+lat+", lng: "+lng+", siteName: "+siteName);
		}
		
		response = new BSROWebServiceResponse();
		response.setPayload(storeLocations);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreLocationByState(String state, String siteName, HttpHeaders headers) {
		log.fine("Returning store location details for state: "+state+", siteName: "+siteName);
		BSROWebServiceResponse response = null;
		
		if (StringUtils.isNullOrEmpty(state) || StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Missing input parameters {state/sitename}.");
		}
		
		String inputState = state;
		state = storeService.getAbbreviateStateName(state);
		if (StringUtils.isNullOrEmpty(state)) {
			return getValidationMessage("Invalid state name: "+inputState);
		}
		
		List<StoreLocation> storeLocations = storeService.findClosestStoresByState(state, siteName);
		if (StringUtils.isNullOrEmpty(storeLocations) || storeLocations.isEmpty()) {
			return getValidationMessage("No stores found for state: "+inputState+", siteName: "+siteName);
		}
		
		response = new BSROWebServiceResponse();
		response.setPayload(storeLocations);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreLocationByZip(String zip, String siteName, Integer storeCount, HttpHeaders headers, HttpServletRequest request) {
		log.fine("Returning store location details for zip: "+zip+", siteName: "+siteName);
		BSROWebServiceResponse response = null;
		
		if (StringUtils.isNullOrEmpty(zip) || StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Missing input parameters.");
		}
		
		List<StoreLocation> storeLocations = storeService.findClosestStoresByZip(zip, siteName, storeCount, request.getRemoteAddr());
		if (StringUtils.isNullOrEmpty(storeLocations) || storeLocations.isEmpty()) {
			return getValidationMessage("No stores found for zip: "+zip+", siteName: "+siteName);
		}
		
		response = new BSROWebServiceResponse();
		response.setPayload(storeLocations);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreLocationByAddress(String address, String city, String state, String zip, 
			String siteName, Integer storeCount, HttpHeaders headers, HttpServletRequest request) {
		log.fine("Returning store location details for address: "+address+", city: "+city+", state: "+state+"zip: "+zip+", siteName: "+siteName);
		BSROWebServiceResponse response = null;
		
		if (StringUtils.isNullOrEmpty(address) && StringUtils.isNullOrEmpty(city) && StringUtils.isNullOrEmpty(state) && StringUtils.isNullOrEmpty(zip) && StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Missing input parameters. Either zip code or state is required.");
		}
		
		String inputState = state;
		state = storeService.getAbbreviateStateName(state);
		if (StringUtils.isNullOrEmpty(state) && StringUtils.isNullOrEmpty(zip)) {
			return getValidationMessage("Missing input parameters. Either zip code or state is required. Invalid state "+inputState);
		}
		
		List<StoreLocation> storeLocations = storeService.findClosestStoresByAddress(address, city, state, zip, siteName, storeCount, request.getRemoteAddr());
		
		if (StringUtils.isNullOrEmpty(storeLocations) || storeLocations.isEmpty()) {
			return getValidationMessage("No stores found for address: "+address+", city: "+city+", state: "+inputState+", zip: "+zip+", siteName: "+siteName);
		}
		
		response = new BSROWebServiceResponse();
		response.setPayload(storeLocations);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		
		return response;
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
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreFlagsBySite(String siteName, HttpHeaders headers, HttpServletRequest request) {
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Missing input parameters {sitename}.");
		}
		
		List<StoreFlags> storeFlagsList = storeService.findStoreFlagsByType(siteName);
		if (StringUtils.isNullOrEmpty(storeFlagsList) || storeFlagsList.isEmpty()) {
			return getValidationMessage("No store details found for store type "+siteName);
		}
		
		response = new BSROWebServiceResponse();
		response.setPayload(storeFlagsList);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreFlagsByStoreNumber(String storeNumber, HttpHeaders headers, HttpServletRequest request) {		
		log.fine("Returning details for store "+storeNumber);
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(storeNumber)) {
			return getValidationMessage("Invalid store number");
		}
		
		StoreFlags storeFlags = storeService.findStoreFlagsByNumber(storeNumber);
		if (StringUtils.isNullOrEmpty(storeFlags)) {
			return getValidationMessage("No store details found for store number "+storeNumber);
		}
		
		response = new BSROWebServiceResponse();		
		response.setPayload(storeFlags);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

}
