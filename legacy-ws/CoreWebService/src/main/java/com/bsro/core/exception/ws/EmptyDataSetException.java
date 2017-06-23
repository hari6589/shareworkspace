package com.bsro.core.exception.ws;

import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 * Thrown when a request is successful but returns no data
 * @author Brad Balmer
 *
 */
public class EmptyDataSetException extends WebApplicationException {

	private static final long serialVersionUID = 1L;
	private String message;
	private Logger log = Logger.getLogger(getClass().getName());
	public EmptyDataSetException(){
		message = new String("");
	}
	public EmptyDataSetException(Throwable t) {
//		super(t);
		this.message = t.getMessage();
		log.info(t.getMessage());
	}
	
	public EmptyDataSetException(String msg) {
		this.message = msg;
		log.info(msg);
	}
	
	@Override
	public Response getResponse() {
		ResponseBuilder response = Response.status(Status.NO_CONTENT); //BAD REQUEST
		response.entity(message);
		log.info("Returning a NO_CONTENT ("+Status.NO_CONTENT.getStatusCode()+") - "+message);
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
