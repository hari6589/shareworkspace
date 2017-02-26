package com.bsro.api.rest.ws.creditcard;

import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.svc.webdb.CreditCardService;
import com.bfrc.framework.util.StringUtils;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import com.bsro.core.security.RequireValidToken;

/**
 * @author smoorthy
 *
 */

@Component
public class CreditCardWebServiceImpl implements CreditCardWebService {
	
	@Autowired
	CreditCardService creditCardService;
	
	private Logger log = Logger.getLogger(getClass().getName());

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getCrediCardDetails(String siteName,
			HttpHeaders headers) {
		log.fine("Returning card details for site "+siteName);
		BSROWebServiceResponse response = null;
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name");
		}
		
		response = creditCardService.getCreditCardDetails(siteName);
		
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
