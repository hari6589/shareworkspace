package com.bsro.api.rest.ws.rewards;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.svc.webdb.emailcollection.EmailCollectionService;
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
public class RewardsWebServiceImpl implements RewardsWebService {
	
	@Autowired
	EmailCollectionService emailCollectionService;

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getPACUserDetails(String tenDigitCode, HttpHeaders headers) {		
		BSROWebServiceResponse response = null;
		if(StringUtils.isNullOrEmpty(tenDigitCode)) {
			return getValidationMessage("Sorry, the 10 digit code you entered was not in our system. Please try again.");
		}
		
		response = emailCollectionService.getPACUserDetails(tenDigitCode);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse updatePACUserDetails(String tenDigitCode, String firstName, String lastName, String email, HttpHeaders headers) {
		BSROWebServiceResponse response = null;
		
		if(StringUtils.isNullOrEmpty(tenDigitCode)) {
			return getValidationMessage("Sorry, the 10 digit code you entered was not in our system. Please try again.");
		}
		
		if(StringUtils.isNullOrEmpty(email)) {
			return getValidationMessage("Invalid email address");
		}
		
		if(StringUtils.isNullOrEmpty(firstName) || StringUtils.isNullOrEmpty(lastName)) {
			return getValidationMessage("Invalid first and last name");
		}
		
		response = emailCollectionService.updatePACUserDetails(tenDigitCode, firstName, lastName, email);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse logRewards(String tenDigitCode, String confirmOptIn, String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = null;
		
		if(StringUtils.isNullOrEmpty(tenDigitCode)) {
			return getValidationMessage("Sorry, the 10 digit code you entered was not in our system. Please try again.");
		}
		
		if(StringUtils.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name");
		}
		
		response = emailCollectionService.logRewards(tenDigitCode, confirmOptIn, siteName);
		
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
