package com.bsro.api.rest.exception;


public class WebServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public WebServiceException() {
	}

	public WebServiceException(String exception) {
		super(exception);
	}

	public WebServiceException(Throwable exception) {
		super(exception);
	}

	public WebServiceException(String exception, Throwable throwable) {
		super(exception, throwable);
	}

}
