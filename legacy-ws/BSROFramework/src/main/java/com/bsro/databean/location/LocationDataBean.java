package com.bsro.databean.location;

import com.bsro.databean.BaseDataBean;

public class LocationDataBean extends BaseDataBean {
	private String city;
	private String state;
	private String urlFriendlyCity;
	private String urlFriendlyState;
	
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
	public String getUrlFriendlyCity() {
		return urlFriendlyCity;
	}
	public void setUrlFriendlyCity(String urlFriendlyCity) {
		this.urlFriendlyCity = urlFriendlyCity;
	}
	public String getUrlFriendlyState() {
		return urlFriendlyState;
	}
	public void setUrlFriendlyState(String urlFriendlyState) {
		this.urlFriendlyState = urlFriendlyState;
	}
}
