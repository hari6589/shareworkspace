package com.bsro.api.rest.ws.vehicle;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.bsro.core.exception.ws.BSROWebServiceUnexpectedErrorResponseException;

import app.bsro.model.webservice.BSROWebServiceRequest;
import app.bsro.model.webservice.BSROWebServiceResponse;

@Path(WebSiteUserVehicleWebService.PATH_USER_VEHICLE_BASE)
public interface WebSiteUserVehicleWebService {

	public static final String PATH_USER_VEHICLE_BASE = "/web-site-user-vehicle";

	public static final String PATH_CREATE_USER_VEHICLE_FROM_TIRE = "/create-user-vehicle/tire";

	public static final String PATH_UPDATE_USER_VEHICLE = "/update-user-vehicle";
	public static final String PATH_UPDATE_USER_SUBVEHICLE = "/update-user-subvehicle";

	public static final String PATH_DELETE_USER_VEHICLE = "/delete-user-vehicle";
	
	public static final String PATH_FETCH_USER_VEHICLE_BY_USER_ID = "/fetch-user-vehicle-by-user-id";
	public static final String PATH_FETCH_USER_VEHICLE_BY_ID = "/fetch-user-vehicle-by-id";

	public static final String PARAM_USER_VEHICLE_ID  = "userVehicleId";
	public static final String PARAM_USER_ID  = "userId";
	

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path(PATH_CREATE_USER_VEHICLE_FROM_TIRE)
	public BSROWebServiceResponse createWebSiteUserVehicleFromTireData(@Context HttpHeaders headers, @FormParam("userId") final Long webSiteUserId, @FormParam("displayName") final String displayName, @FormParam("year") final String year, @FormParam("make") final String make, @FormParam("model") final String model, @FormParam("submodel") final String submodel) throws BSROWebServiceUnexpectedErrorResponseException;
	
	/*
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(PATH_UPDATE_USER_VEHICLE)
	public BSROWebServiceResponse updateWebSiteUserVehicle(@Context HttpHeaders headers, final BSROWebServiceRequest request);
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path(PATH_DELETE_USER_VEHICLE)
	public void deleteWebSiteUserVehicle(@Context HttpHeaders headers, @FormParam(PARAM_USER_VEHICLE_ID) final Long webSiteUserVehicleId);
	*/
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path(PATH_FETCH_USER_VEHICLE_BY_USER_ID)
	public BSROWebServiceResponse fetchWebSiteUserVehiclesByWebSiteUser(@Context HttpHeaders headers, @FormParam(PARAM_USER_ID) final Long webSiteUserId);
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path(PATH_FETCH_USER_VEHICLE_BY_ID)
	public BSROWebServiceResponse fetchWebSiteUserVehiclesById(@Context HttpHeaders headers, @FormParam(PARAM_USER_VEHICLE_ID) final Long webSiteUserVehicleId);

}
