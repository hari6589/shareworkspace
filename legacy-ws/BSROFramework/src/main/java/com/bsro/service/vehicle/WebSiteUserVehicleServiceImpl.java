package com.bsro.service.vehicle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bsro.errors.Errors;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.rebrand.BSROWebserviceServiceImpl;

@Service("webSiteUserVehicleService")
public class WebSiteUserVehicleServiceImpl extends BSROWebserviceServiceImpl implements WebSiteUserVehicleService {
	public static final String PATH_USER_VEHICLE_BASE = "/web-site-user-vehicle";
	public static final String PATH_CREATE_USER_VEHICLE_FROM_TIRE = "/create-user-vehicle/tire";
	//public static final String PATH_CREATE_USER_SUBVEHICLE = "/create-user-subvehicle";
	//public static final String PATH_UPDATE_USER_VEHICLE = "/update-user-vehicle";
	//public static final String PATH_UPDATE_USER_SUBVEHICLE = "/update-user-subvehicle";
	public static final String PATH_DELETE_USER_VEHICLE = "/delete-user-vehicle";
	public static final String PATH_FETCH_USER_VEHICLE_BY_USER_ID = "/fetch-user-vehicle-by-user-id";
	public static final String PATH_FETCH_USER_VEHICLE_BY_ID = "/fetch-user-vehicle-by-id";
	
	public static final String PARAM_USER_VEHICLE_ID  = "userVehicleId";
	public static final String PARAM_USER_ID  = "userId";

	public static final String PARAM_YEAR = "year";
	public static final String PARAM_MAKE = "make";
	public static final String PARAM_MODEL = "model";
	public static final String PARAM_SUBMODEL = "submodel";
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}


	@Override
	public UserVehicle createWebSiteUserVehicleFromTireData(Long webSiteUserId, String year, String make, String model, String submodel, Errors errors) throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put(PARAM_USER_ID, Long.toString(webSiteUserId));
		parameters.put(PARAM_YEAR, year);
		parameters.put(PARAM_MAKE, make);
		parameters.put(PARAM_MODEL, model);
		parameters.put(PARAM_SUBMODEL, submodel);
		
		return postWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_VEHICLE_BASE + PATH_CREATE_USER_VEHICLE_FROM_TIRE, 
				parameters, 
				UserVehicle.class,
				errors);
	}
/*
	@Override
	public WebSiteUserVehicle updateWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle, Errors errors) throws IOException {
		return postJSONToWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_VEHICLE_BASE + PATH_UPDATE_USER_VEHICLE, 
				webSiteUserVehicle, 
				WebSiteUserVehicle.class,
				errors);
	}

	@Override
	public void deleteWebSiteUserVehicle(Long webSiteUserVehicleId, Errors errors) throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_USER_VEHICLE_ID, webSiteUserVehicleId.toString());
		postWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_VEHICLE_BASE + PATH_DELETE_USER_VEHICLE, 
				parameters, 
				Void.class,
				errors);
	}
*/

	@SuppressWarnings("unchecked")
	@Override
	public List<UserVehicle> fetchWebSiteUserVehiclesByWebSiteUser(Long webSiteUserId, Errors errors) throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_USER_ID,webSiteUserId.toString());
		
		//TODO if this fails you need to use "new TypeReference<List<WebSiteUserVehicle>>() {}" instead of List.class for the return object
		return (List<UserVehicle>) postWebservice(PATH_WEBSERVICE_BASE + PATH_USER_VEHICLE_BASE + PATH_FETCH_USER_VEHICLE_BY_USER_ID, 
				parameters,  
				 new TypeReference<List<UserVehicle>>() {}, 
				errors);
	}
/*
	@Override
	public WebSiteUserSubvehicle createWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle, Errors errors) throws IOException {
		return postJSONToWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_VEHICLE_BASE + PATH_CREATE_USER_SUBVEHICLE, 
				webSiteUserSubvehicle, 
				WebSiteUserSubvehicle.class,
				errors);
	}

	@Override
	public WebSiteUserSubvehicle updateWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle, Errors errors) throws IOException {
		return postJSONToWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_VEHICLE_BASE + PATH_UPDATE_USER_SUBVEHICLE, 
				webSiteUserSubvehicle, 
				WebSiteUserSubvehicle.class,
				errors);
	}

	public WebSiteUserVehicle fetchWebSiteUserVehicleById(Long webSiteUserVehicleId, Errors errors) throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_USER_VEHICLE_ID,webSiteUserVehicleId.toString());
		
		return postWebservice(PATH_WEBSERVICE_BASE + PATH_USER_VEHICLE_BASE + PATH_FETCH_USER_VEHICLE_BY_ID, 
				parameters,  
				WebSiteUserVehicle.class, 
				errors);
	}
	*/
}
