package com.bfrc.dataaccess.exception;

/** 
 * Catch all exception thrown when there is an error creating an appointment.
 * @author Brad Balmer
 *
 */
public class InvalidAppointmentException extends Exception {

	private static final long serialVersionUID = 2196029882703308114L;

	public InvalidAppointmentException() {}

	public InvalidAppointmentException(String message) {
		super(message);
	}

	public InvalidAppointmentException(Throwable cause) {
		super(cause);
	}

	public InvalidAppointmentException(String message, Throwable cause) {
		super(message, cause);
	}

}
