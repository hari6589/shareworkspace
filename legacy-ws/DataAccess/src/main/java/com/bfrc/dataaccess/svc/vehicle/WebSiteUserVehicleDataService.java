package com.bfrc.dataaccess.svc.vehicle;

import java.io.IOException;
import java.util.List;

import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bfrc.dataaccess.model.vehicle.WebSiteUserSubvehicle;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserVehicle;

public interface WebSiteUserVehicleDataService {
	
	public UserVehicle createWebSiteUserVehicleFromTireData(Long webSiteUserId, String displayName, String year, String make, String model, String submodel) throws Throwable;

	public WebSiteUserVehicle updateWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle) throws IOException;
	public void deleteWebSiteUserVehicle(Long webSiteUserVehicleId) throws IOException;
	public UserVehicle fetchWebSiteUserVehicle(Long webSiteUserVehicleId) throws IOException;
	public List<UserVehicle> fetchWebSiteUserVehiclesByWebSiteUser(Long webSiteUserId) throws IOException;
	
	public WebSiteUserSubvehicle createWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle) throws IOException;
	public WebSiteUserSubvehicle updateWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle) throws IOException;
	public void deleteWebSiteUserSubvehicle(Long webSiteUserSubvehicleId) throws IOException;
	public WebSiteUserSubvehicle fetchWebSiteUserSubvehicle(Long webSiteUserSubvehicleId) throws IOException;	
}
