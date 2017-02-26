package com.bfrc.pojo.store;

public class StoreSearch {
	private String searchEntered;
	private String zip;
	private String state;
	private String city;
	
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSearchEntered() {
		return searchEntered;
	}
	public void setSearchEntered(String searchEntered) {
		this.searchEntered = searchEntered;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
