package app.bsro.model.webservice;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import app.bsro.model.error.Errors;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@JsonPropertyOrder({"statusCode"})
public class BSROWebServiceResponse implements Serializable {
	
	@Override
	public String toString() {
		return "BSROWebServiceResponse [payload=" + payload + ", statusCode="
				+ statusCode + ", errors=" + errors + ", message=" + message+ "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//This is the POJO to be transformed to JSON
//	@JsonUnwrapped 
	private Object payload;
	
	//This status code is an easy way of determining if 
	//an error has occurred
	private String statusCode;
	
	private String message;
	
	//This is the list of errors that have occurred.
	//These could be validation errors or actual exceptions
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

	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
}
