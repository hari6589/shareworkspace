package com.bfrc.dataaccess.model.vehicle;

import java.util.Set;

/**
 * User generated by hbm2java
 */

public class WebSiteUserVehicle implements java.io.Serializable {

	public static final String VEHICLE_TYPE_BATTERY = "battery";
	public static final String VEHICLE_TYPE_OIL = "oil";
	public static final String VEHICLE_TYPE_TIRE = "tire";

	private Long webSiteUserVehicleId;
	private Long webSiteUserId;
	private String displayName;
	private Integer mileage;
	private Integer annualMileage;
	private String vin;
	private Set<WebSiteUserSubvehicle> webSiteUserSubvehicles;

	public Set<WebSiteUserSubvehicle> getWebSiteUserSubvehicles() {
		return webSiteUserSubvehicles;
	}

	public void setWebSiteUserSubvehicles(Set<WebSiteUserSubvehicle> webSiteUserSubvehicles) {
		this.webSiteUserSubvehicles = webSiteUserSubvehicles;
	}

	public Long getWebSiteUserVehicleId() {
		return webSiteUserVehicleId;
	}

	public void setWebSiteUserVehicleId(Long webSiteUserVehicleId) {
		this.webSiteUserVehicleId = webSiteUserVehicleId;
	}

	public Long getWebSiteUserId() {
		return webSiteUserId;
	}

	public void setWebSiteUserId(Long webSiteUserId) {
		this.webSiteUserId = webSiteUserId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getMileage() {
		return mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public Integer getAnnualMileage() {
		return annualMileage;
	}

	public void setAnnualMileage(Integer annualMileage) {
		this.annualMileage = annualMileage;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

}
