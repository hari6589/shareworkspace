package com.bfrc.dataaccess.dao;

import java.util.List;

import com.bfrc.dataaccess.model.vehicle.WebSiteUserSubvehicle;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserVehicle;


public interface WebSiteUserVehicleDAO {

	WebSiteUserVehicle createWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle);
	WebSiteUserVehicle updateWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle);
	void deleteWebSiteUserVehicle(Long webSiteUserVehicleId);
	WebSiteUserVehicle fetchWebSiteUserVehicle(Long webSiteUserVehicleId);
	List<WebSiteUserVehicle> fetchWebSiteUserVehiclesByWebSiteUser(Long webSiteUserId);
	
	WebSiteUserSubvehicle createWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle);
	WebSiteUserSubvehicle updateWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle);
	void deleteWebSiteUserSubvehicle(Long webSiteUserSubvehicleId);
	WebSiteUserSubvehicle fetchWebSiteUserSubvehicle(Long webSiteUserSubvehicleId);
	
}
