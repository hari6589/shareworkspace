package com.bfrc.dataaccess.exception;

public class InvalidEmailException extends Exception {

	private static final long serialVersionUID = -2355193544312459349L;

	public InvalidEmailException() {}

	public InvalidEmailException(String message) {
		super(message);
	}

	public InvalidEmailException(Throwable cause) {
		super(cause);
	}

	public InvalidEmailException(String message, Throwable cause) {
		super(message, cause);
	}

}
