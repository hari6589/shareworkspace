package com.bfrc.dataaccess.exception;

public class InvalidUserException extends Exception {

	private static final long serialVersionUID = 1265072354429826892L;

	public InvalidUserException() {
	}

	public InvalidUserException(String message) {
		super(message);
	}

	public InvalidUserException(Throwable cause) {
		super(cause);
	}

	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
	}

}
