package com.bsro.api.rest.ws.locator;

import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.svc.webdb.LocatorService;
import com.bfrc.framework.util.StringUtils;
import com.bsro.core.security.RequireValidToken;

/**
 * @author smoorthy
 *
 */

@Component
public class LocatorWebServiceImpl implements LocatorWebService {
	
	@Autowired
	LocatorService locatorService;
	
	private Logger log = Logger.getLogger(getClass().getName());

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getState(HttpHeaders headers) {
		log.fine("Returning all state names");
		
		BSROWebServiceResponse response = locatorService.getState();
				
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStateBySite(String siteName,
			HttpHeaders headers) {
		log.fine("Returning state name for site "+siteName);
		
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name");
		}
		
		response = locatorService.getStateBySite(siteName);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getCityByStateAndSite(String state,
			String siteName, HttpHeaders headers) {
		log.fine("Returning city name for state "+state+" & site "+siteName);
		
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name");
		}
		
		if(StringUtils.isNullOrEmpty(state)) {
			return getValidationMessage("Invalid state");
		}
		
		response = locatorService.getCityByStateAndSite(state, siteName);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoresByCityState(String state,
			String city, String siteName, HttpHeaders headers) {
		log.fine("Returning stores for state "+state+", city "+city+" & site "+siteName);
		
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name");
		}
		
		if(StringUtils.isNullOrEmpty(state)) {
			return getValidationMessage("Invalid state");
		}
		
		if(StringUtils.isNullOrEmpty(city)) {
			return getValidationMessage("Invalid city");
		}
		
		response = locatorService.getStoresByStateAndCity(state, city, siteName);
		
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
}
