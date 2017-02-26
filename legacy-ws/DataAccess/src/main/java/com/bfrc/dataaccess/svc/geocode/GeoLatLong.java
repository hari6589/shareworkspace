package com.bfrc.dataaccess.svc.geocode;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class GeoLatLong {

	private String latitude;
	private String longitude;
	private String countryCode;
	private Calendar created;
	
	public GeoLatLong() {
		this.created = GregorianCalendar.getInstance();
	}
	
	public boolean isSet() {
		return (latitude != null) && (longitude != null);
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public Calendar getCreated() {
		return created;
	}
}
