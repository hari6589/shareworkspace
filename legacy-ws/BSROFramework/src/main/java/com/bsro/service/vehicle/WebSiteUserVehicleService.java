package com.bsro.service.vehicle;

import java.io.IOException;
import java.util.List;

import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bsro.errors.Errors;

public interface WebSiteUserVehicleService {	
	public UserVehicle createWebSiteUserVehicleFromTireData(Long webSiteUserId, String year, String make, String model, String submodel, Errors errors) throws IOException;
	/*
	public WebSiteUserVehicle updateWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle, Errors errors) throws IOException;
	public void deleteWebSiteUserVehicle(Long webSiteUserVehicleId, Errors errors) throws IOException;
	*/
	public List<UserVehicle> fetchWebSiteUserVehiclesByWebSiteUser(Long webSiteUserId, Errors errors) throws IOException;
	/*
	public WebSiteUserSubvehicle createWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle, Errors errors) throws IOException;
	public WebSiteUserSubvehicle updateWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle, Errors errors) throws IOException;
	public WebSiteUserVehicle fetchWebSiteUserVehicleById(Long webSiteUserVehicleId, Errors errors) throws IOException;
	*/
}
