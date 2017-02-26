package com.bsro.exception.webservice;

import app.bsro.model.error.WebServiceExceptionError;

public class WebServiceExceptionErrorException extends RuntimeException {
	private WebServiceExceptionError webServiceExceptionError;
	
	public WebServiceExceptionErrorException(WebServiceExceptionError webServiceExceptionError) {
		super();
		this.webServiceExceptionError = webServiceExceptionError;
	}
	
	@Override
	public String getMessage() {
		StringBuffer message = null;
		if (webServiceExceptionError != null) {
			message = new StringBuffer("A web service returned the following exception information: ");
			message.append("\nmessage: ");
			message.append(webServiceExceptionError.getExceptionMessage());
			message.append("\nstack trace: ");
			message.append(webServiceExceptionError.getExceptionStackTrace());
		} else {
			message = new StringBuffer("This exception does not contain any web service exception information. This is presumably due to an error.");
		}
		return message.toString();
	}
	
	public WebServiceExceptionError getWebServiceExceptionError() {
		return webServiceExceptionError;
	}
}
