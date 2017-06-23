package com.bsro.core.exception.ws;

import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;


/**
 * Exception thrown when an internal error has occurred within the web service's control.
 * @author Brad Balmer
 *
 */
public class ServerProcessingException extends WebApplicationException {

	private static final long serialVersionUID = 1L;
	private String message;
	private Logger log = Logger.getLogger(getClass().getName());
	
	public ServerProcessingException(){}
	
	public ServerProcessingException(Throwable t) {
		//super(t);
		this.message = t.getMessage();
		log.severe(t.getMessage());
	}
	
	public ServerProcessingException(String msg) {
		this.message = msg;
		log.severe(msg);
	}
	
	@Override
	public Response getResponse() {
		ResponseBuilder response = Response.status(Status.INTERNAL_SERVER_ERROR);
		response.entity(message);
		log.severe("Returning a INTERNAL_SERVER_ERROR ("+Status.INTERNAL_SERVER_ERROR.getStatusCode()+") - "+message);
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
