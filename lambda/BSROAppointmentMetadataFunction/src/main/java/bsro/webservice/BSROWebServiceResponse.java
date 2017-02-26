package bsro.webservice;

import bsro.webservice.error.Errors;

public class BSROWebServiceResponse {

	private Object payload;
	private String statusCode;
	private String message;
	private Errors errors;
	
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Errors getErrors() {
		return errors;
	}
	public void setErrors(Errors errors) {
		this.errors = errors;
	}
	
	@Override
	public String toString() {
		return "BSROWebServiceResponse [payload=" + payload + ", statusCode="
				+ statusCode + ", message=" + message + ", errors=" + errors
				+ "]";
	}
	
}
