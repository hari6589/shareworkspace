package com.bsro.core.exception.ws;

/**
 * If your webservice throws this exception, an exception handler will automatically generate an appropriate.
 * BSROWebServiceResponse object.
 * 
 * @author mholmes
 *
 */
public class BSROWebServiceUnexpectedErrorResponseException extends Exception {
	private static final long serialVersionUID = 2770255107396089177L;
	
    public BSROWebServiceUnexpectedErrorResponseException() {
	super();
    }

    public BSROWebServiceUnexpectedErrorResponseException(String message) {
	super(message);
    }


    public BSROWebServiceUnexpectedErrorResponseException(String message, Throwable cause) {
        super(message, cause);
    }


    public BSROWebServiceUnexpectedErrorResponseException(Throwable cause) {
        super(cause);
    }
}
