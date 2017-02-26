package com.bfrc.storelocator.util;

public class GeocodingException extends AddressNotFoundException {
	private String faultCode;
	public String getFaultCode() {
		return this.faultCode;
	}
	public GeocodingException(String message, String faultCode) {
		super();
		this.reason = message;
		this.faultCode = faultCode;
	}
	public GeocodingException(GeocodingException ex) {
		this(ex.getReason(), ex.getFaultCode());
	}
	
}
