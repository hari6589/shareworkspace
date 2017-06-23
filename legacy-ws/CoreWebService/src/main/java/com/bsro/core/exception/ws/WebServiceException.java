package com.bsro.core.exception.ws;

import java.io.PrintWriter;
import java.io.StringWriter;

public class WebServiceException extends RuntimeException {

	private String internalMessage = null;
	
	private static final long serialVersionUID = 1L;

	public WebServiceException() {
		super();
	}

	public WebServiceException(String message, Throwable cause) {
		super(message, cause);
		this.internalMessage = message;
	}

	public WebServiceException(Throwable cause) {
		super(cause);
	}

	public WebServiceException(String msg) {
		super(msg);
		this.internalMessage = internalMessage;
	}

	public String getInternalErrorMessage() {
		Throwable cause = this.getCause();
		if (cause != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			cause.printStackTrace(pw);
			return sw.toString();
		}
		return null;
	}

	public String getInternalMessage() {
		return internalMessage;
	}

	public void setInternalMessage(String message) {
		this.internalMessage = message;
	}
}
