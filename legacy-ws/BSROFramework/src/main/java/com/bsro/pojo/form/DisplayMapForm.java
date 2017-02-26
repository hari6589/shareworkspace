package com.bsro.pojo.form;

import java.io.Serializable;

public class DisplayMapForm implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** The value of the simple storeNumber property. */
	private Long storeNumber;
	private String address, city, state, zip, navZip, geoPoint;
	private String eventSource;
	
	public Long getStoreNumber() {
		return storeNumber;
	}
	
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getGeoPoint() {
		return geoPoint;
	}
	
	public void setGeoPoint(String geoPoint) {
		this.geoPoint = geoPoint;
	}
	
	public String getNavZip() {
		return navZip;
	}
	
	public void setNavZip(String navZip) {
		this.navZip = navZip;
	}
	
	public String getEventSource() {
		return eventSource;
	}
	
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
}
