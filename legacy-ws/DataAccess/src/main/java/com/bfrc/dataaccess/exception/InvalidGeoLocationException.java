package com.bfrc.dataaccess.exception;

public class InvalidGeoLocationException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidGeoLocationException() {
	}

	public InvalidGeoLocationException(String arg0) {
		super(arg0);
	}

	public InvalidGeoLocationException(Throwable arg0) {
		super(arg0);
	}

	public InvalidGeoLocationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
