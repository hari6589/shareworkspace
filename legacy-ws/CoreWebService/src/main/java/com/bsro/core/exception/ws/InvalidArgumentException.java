package com.bsro.core.exception.ws;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.owasp.esapi.ValidationErrorList;
import org.owasp.esapi.errors.ValidationException;

import com.bsro.core.validation.Error;
import com.bsro.core.validation.ErrorList;

/**
 * Exception thrown when invalid arguments (path or query parameters) are submitted.
 * @author Brad Balmer
 *
 */
public class InvalidArgumentException extends WebApplicationException {

	private static final long serialVersionUID = 1L;
	private String message;
	private final String DEFAULT_ERROR_MSG = "Unspecified errors thrown by the application.";

	private Logger log = Logger.getLogger(getClass().getName());
	public InvalidArgumentException(){}
	public InvalidArgumentException(Throwable t) {
		//super(t);
		this.message = t.getMessage();
		log.severe(t.getMessage());
	}
	
	public InvalidArgumentException(String msg) {
		this.message = msg;
		log.severe(msg);
	}
	
	public InvalidArgumentException(ErrorList errorList) {
		if(errorList == null) {
			this.message = DEFAULT_ERROR_MSG;
		} else {
			List<Error> ves = errorList.getErrors();
			JSONArray errs = new JSONArray();
			if(ves == null || ves.size() == 0) {
				this.message = DEFAULT_ERROR_MSG;
			} else {
				this.message = "Invalid Argument Errors: ";
				
				for(Error ex : ves) {
					try {
						JSONObject message = new JSONObject();
						message.put("field", ex.getField());
						message.put("message", ex.getMessage());
						errs.put(message);
					}catch(Exception E){
						log.severe("Error creating JSON Response: "+E.getMessage());
					}
				}
			}
			
			this.message = errs.toString();
		}
		
		log.severe(this.message);
	}
	
	public InvalidArgumentException(ValidationErrorList errors) {
		if(errors == null) {
			this.message = DEFAULT_ERROR_MSG;
		} else {
			List<ValidationException> ves = errors.errors();
			JSONArray errs = new JSONArray();
			if(ves == null || ves.size() == 0) {
				this.message = DEFAULT_ERROR_MSG;
			} else {
				this.message = "Invalid Argument Errors: ";
				
				for(ValidationException ex : ves) {
					try {
						JSONObject message = new JSONObject();
						message.put("message", ex.getMessage());
						errs.put(message);
					}catch(Exception E){
						log.severe("Error creating JSON Response: "+E.getMessage());
					}
				}
			}
			
			this.message = errs.toString();
		}
		
		log.severe(this.message);
	}

	
	@Override
	public Response getResponse() {
		ResponseBuilder response = Response.status(Status.BAD_REQUEST); //BAD REQUEST
		response.entity(message);
		
		log.severe("Returning a BAD_REQUEST ("+Status.BAD_REQUEST.getStatusCode()+") - "+message);
		return response.build();
	}
	
	/**
	 * This us used to STOP RestEasy from printing a stack trace for this exception
	 */
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
