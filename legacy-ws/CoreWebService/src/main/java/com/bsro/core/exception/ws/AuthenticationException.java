package com.bsro.core.exception.ws;

import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 * Thrown when a request either passes incorrect credentials that do not validate 
 * or when a request is attempted without the proper token.
 * @author Brad Balmer
 *
 */
public class AuthenticationException extends WebApplicationException {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(getClass().getName());
	public AuthenticationException() {}
	
	public AuthenticationException(Throwable t) {
		super(t);
	}
	
	@Override
	public Response getResponse() {
		ResponseBuilder response = Response.status(Status.UNAUTHORIZED);
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
