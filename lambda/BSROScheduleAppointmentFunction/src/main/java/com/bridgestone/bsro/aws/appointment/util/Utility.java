package com.bridgestone.bsro.aws.appointment.util;

import bsro.webservice.BSROWebServiceResponse;
import bsro.webservice.BSROWebServiceResponseCode;
import bsro.webservice.error.Errors;

public class Utility {
	
	public boolean isNullOrEmpty(String str) {
        if (str == null || str.trim().equals("") || str.trim().equals("null")) {
            return true;
        } 
        return false;
    }
    
    public BSROWebServiceResponse getValidationMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}
    
}
