package com.bsro.oil.exception;

public class OilDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public OilDataException() {
	}

	public OilDataException(String s) {
		super(s);
	}

	public OilDataException(Throwable t) {
		super(t);
	}

	public OilDataException(String s, Throwable t) {
		super(s, t);
	}

}
