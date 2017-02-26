package com.bfrc.dataaccess.exception;

/**
 * Exception class for any HTTP error
 * @author Brad Balmer
 *
 */
public class HttpException extends Exception {

	private static final long serialVersionUID = 1L;

	public HttpException() {
	}

	public HttpException(String s) {
		super(s);
	}

	public HttpException(Throwable t) {
		super(t);
	}

	public HttpException(String s, Throwable t) {
		super(s, t);
	}

}
