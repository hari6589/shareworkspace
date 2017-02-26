package com.bsro.api.rest.ws.vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bfrc.dataaccess.model.vehicle.WebSiteUserVehicle;
import com.bfrc.dataaccess.svc.vehicle.WebSiteUserVehicleDataService;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.util.ValidatorUtils;
import com.bsro.core.exception.ws.BSROWebServiceUnexpectedErrorResponseException;
import com.bsro.core.security.RequireValidAppNameAndToken;

@Component
public class WebSiteUserVehicleWebServiceImpl implements WebSiteUserVehicleWebService {

	private Logger log = Logger.getLogger(getClass().getName());

	@Autowired
	private WebSiteUserVehicleDataService webSiteUserVehicleDataService;

	@Override
	@RequireValidAppNameAndToken(PATH_USER_VEHICLE_BASE+WebSiteUserVehicleWebService.PATH_CREATE_USER_VEHICLE_FROM_TIRE)
	public BSROWebServiceResponse createWebSiteUserVehicleFromTireData(HttpHeaders headers, final Long webSiteUserId, final String displayName, final String year, final String make, final String model, String submodel) throws BSROWebServiceUnexpectedErrorResponseException {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		UserVehicle userVehicle = null;
		
		try {
			userVehicle = webSiteUserVehicleDataService.createWebSiteUserVehicleFromTireData(webSiteUserId, displayName, year, make, model, submodel);
			response.setPayload(userVehicle);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		} catch(Throwable e){
			BSROWebServiceUnexpectedErrorResponseException exception = new BSROWebServiceUnexpectedErrorResponseException(e);
			throw exception;
		}

		return response;
	}

	/*
	@Override
	@RequireValidAppNameAndToken(PATH_USER_VEHICLE_BASE+WebSiteUserVehicleWebService.PATH_UPDATE_USER_VEHICLE)
	public BSROWebServiceResponse updateWebSiteUserVehicle(HttpHeaders headers, final BSROWebServiceRequest request) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		response.setErrors(errors);
		ObjectMapper mapper = new ObjectMapper();
		WebSiteUserVehicle webSiteUserVehicle = mapper.convertValue(request.getRequestPayload(), WebSiteUserVehicle.class);

		validateWebSiteUserVehicle(webSiteUserVehicle, false, errors);
		try {
			if(errors.getFieldErrors().isEmpty() && errors.getGlobalErrors().isEmpty()){
				response.setPayload(webSiteUserVehicleDataService.updateWebSiteUserVehicle(webSiteUserVehicle));
			}
		} catch(Throwable e) {
			log.log(Level.SEVERE, "An exception has occurred while updating a vehicle.", e);
			errors.getGlobalErrors().add("An exception has occurred while updating a vehicle.");
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		return response;
	}

	@Override
	@RequireValidAppNameAndToken(PATH_USER_VEHICLE_BASE+WebSiteUserVehicleWebService.PATH_DELETE_USER_VEHICLE)
	public void deleteWebSiteUserVehicle(HttpHeaders headers, Long webSiteUserVehicleId) {
		try {
			webSiteUserVehicleDataService.deleteWebSiteUserVehicle(webSiteUserVehicleId);
		} catch(Throwable e) {
			log.log(Level.SEVERE, "An exception has occurred while deleting a vehicle.", e);
		}
	}
	*/

	@Override
	@RequireValidAppNameAndToken(PATH_USER_VEHICLE_BASE+WebSiteUserVehicleWebService.PATH_FETCH_USER_VEHICLE_BY_USER_ID)
	public BSROWebServiceResponse fetchWebSiteUserVehiclesByWebSiteUser(HttpHeaders headers, Long webSiteUserId) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		response.setErrors(errors);
		try {
			if(errors.getFieldErrors().isEmpty() && errors.getGlobalErrors().isEmpty()){
				response.setPayload(webSiteUserVehicleDataService.fetchWebSiteUserVehiclesByWebSiteUser(webSiteUserId));
			}
		} catch(Throwable e) {
			log.log(Level.SEVERE, "An exception has occurred while fetching vehicles for a user.", e);
			errors.getGlobalErrors().add("An exception has occurred while fetching vehicles for a user.");
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		return response;
	}

	@Override
	@RequireValidAppNameAndToken(PATH_USER_VEHICLE_BASE+WebSiteUserVehicleWebService.PATH_FETCH_USER_VEHICLE_BY_ID)
	public BSROWebServiceResponse fetchWebSiteUserVehiclesById(HttpHeaders headers, Long webSiteUserVehicleId) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		response.setErrors(errors);
		try {
			if(errors.getFieldErrors().isEmpty() && errors.getGlobalErrors().isEmpty())
				response.setPayload(webSiteUserVehicleDataService.fetchWebSiteUserVehicle(webSiteUserVehicleId));
		} catch (IOException e) {
			log.log(Level.SEVERE, "An exception has occurred while fetching a vehicles by id.", e);
			errors.getGlobalErrors().add("An exception has occurred while fetching a vehicles by id.");
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		return response;
	}

	private void validateWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle, boolean create, Errors errors) {
		if(!create && webSiteUserVehicle.getWebSiteUserVehicleId() == null) {
			errors.getGlobalErrors().add(ValidationConstants.replaceParams(ValidationConstants.UPDATE_VALIDATION, "vehicle"));
		}
		if(StringUtils.isNotEmpty(webSiteUserVehicle.getDisplayName()) && !ValidatorUtils.isFirstNameValid(webSiteUserVehicle.getDisplayName())) {
			errors.getFieldErrors().put("name", new ArrayList<String>(Arrays.asList(ValidationConstants.replaceParams(ValidationConstants.REQUIRED_VALIDATION, "name"))));
		}
		if( webSiteUserVehicle.getWebSiteUserId() == null) {
			errors.getGlobalErrors().add(ValidationConstants.replaceParams(ValidationConstants.SELECT_VALIDATION, "web site user"));
		}
	}

}
