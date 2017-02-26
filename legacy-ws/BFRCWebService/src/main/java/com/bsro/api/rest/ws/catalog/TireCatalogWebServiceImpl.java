package com.bsro.api.rest.ws.catalog;

import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.svc.catalog.CatalogService;
import com.bfrc.framework.util.StringUtils;
import com.bsro.core.security.RequireValidToken;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

/**
 * @author smoorthy
 *
 */

@Component
public class TireCatalogWebServiceImpl implements TireCatalogWebService {
	
	@Autowired
	CatalogService catalogService;
	
	private Logger log = Logger.getLogger(getClass().getName());

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getTireProductDetails(String siteName, String brand,
			String tireName, String displayId, HttpHeaders headers) {
		log.fine("Returning product details for site "+siteName+", brand "+brand+" and tire "+tireName);
		
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(siteName)) {
			response = getValidationMessage("Invalid site name");
			return response;
		}
		
		if(StringUtils.isNullOrEmpty(brand) && StringUtils.isNullOrEmpty(displayId)) {
			response = getValidationMessage("Input required for either brand name or display Id.");
			return response;
		}
		
		if(StringUtils.isNullOrEmpty(tireName) && StringUtils.isNullOrEmpty(displayId)) {
			response = getValidationMessage("Input required for either tire name or display Id.");
			return response;
		}
		
		response = catalogService.getTireProductDetails(siteName, brand, tireName, displayId);
		
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
