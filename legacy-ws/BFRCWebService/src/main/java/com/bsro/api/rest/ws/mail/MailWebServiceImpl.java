package com.bsro.api.rest.ws.mail;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfrc.dataaccess.svc.mail.MailService;
import com.bfrc.framework.util.ServerUtil;
import com.bsro.core.security.RequireValidToken;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

/**
 * @author smoorthy
 *
 */

@Component
public class MailWebServiceImpl implements MailWebService {
	
	@Autowired
	MailService mailService;

	@Override
	@RequireValidToken
	public BSROWebServiceResponse postEmailQuote(String source, String siteName, Long quoteId,
			String rebateId, String firstName, String lastName, String emailAddress,
			HttpHeaders headers) {
		BSROWebServiceResponse response = null;
		
		if (ServerUtil.isNullOrEmpty(source) || ServerUtil.isNullOrEmpty(siteName) || ServerUtil.isNullOrEmpty(quoteId) || ServerUtil.isNullOrEmpty(emailAddress)) {
			String errmsg = "Missing required inputs [soure / site name / quote id / email address]";
			response = getValidationMessage(errmsg);
		} else if (!EmailValidator.getInstance().isValid(emailAddress)) {
			String errmsg = "Invalid email address.";
			response = getValidationMessage(errmsg);
		} else {
			response = mailService.postEmailQuote(source, siteName, quoteId, rebateId, firstName, lastName, emailAddress);
		}
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
