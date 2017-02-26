package com.bsro.api.rest.ws.pricing;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.svc.webdb.TirePricingService;
import com.bfrc.framework.util.StringUtils;
import com.bsro.core.security.RequireValidToken;

/**
 * @author stallin sevugamoorthy
 *
 */
@Component
public class TirePricingWebServiceImpl implements TirePricingWebService {
	
	@Autowired
	TirePricingService tirePricingService;

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getTirePricingResults(String siteName,
			Long storeNumber, Long acesVehicleId, Integer tpms, String cross, String aspect,
			String rim, HttpHeaders headers) {
		BSROWebServiceResponse res = null;
		
		if(StringUtils.isNullOrEmpty(storeNumber)) {
			return getValidationMessage("Invalid store number");
		}
		if(StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name");
		}
		if (StringUtils.isNullOrEmpty(acesVehicleId) && (StringUtils.isNullOrEmpty(cross) || StringUtils.isNullOrEmpty(aspect) || StringUtils.isNullOrEmpty(rim))) {
			return getValidationMessage("Missing input values for acesVehicleId or cross/aspect/rim.");
		}
		
		res = tirePricingService.getTirePricingResults(siteName, storeNumber, acesVehicleId, tpms, cross, aspect, rim);
		
		return res;
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
