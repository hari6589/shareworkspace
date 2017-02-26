package com.bsro.api.rest.exception;

/**
 * Exception to catch an error from an external server connection, mainly from external web services
 * @author Brad Balmer
 *
 */
public class ExternalServerError extends Exception {

	private static final long serialVersionUID = 1L;

	public ExternalServerError() {
	}

	public ExternalServerError(String s) {
		super(s);
	}

	public ExternalServerError(Throwable t) {
		super(t);
	}

	public ExternalServerError(String s, Throwable t) {
		super(s, t);
	}

}
