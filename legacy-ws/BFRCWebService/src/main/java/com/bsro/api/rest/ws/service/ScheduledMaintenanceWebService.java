package com.bsro.api.rest.ws.service;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.maintenance.MaintenanceMilestones;
import app.bsro.model.maintenance.PeriodicMaintenanceMilestones;
import app.bsro.model.maintenance.ScheduledMaintenanceCategories;
import app.bsro.model.maintenance.ScheduledMaintenances;
import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * Retrieving the suggested scheduled maintenance for the acesVehicleId passed.  The acesVehicleId is the same id
 * that the website uses to identify a year/make/model of a vehicle.
 * @author Brad Balmer
 *
 */
@Path(ScheduledMaintenanceWebService.URL)
public interface ScheduledMaintenanceWebService {
	public static final String URL = "/maintenance/scheduled";

	/**
	 * Returns counts for the four categories to see if there is data to pull for them
	 * vehicle (acesVehicleId)<br/>
	 * <b>URL: </b>/{URI}/categories/{acesVehicleId}<br/>
	 * <b>Type: </b>GET
	 * @return {@link ScheduledMaintenanceCategories} populated Object in JSON.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/categories/{acesVehicleId}")
	public BSROWebServiceResponse getCategoryCounts(@PathParam("acesVehicleId") final Long vehicleId, @Context HttpHeaders headers);
	
	/**
	 * Returns all service types (NORMAL, SEVERE, etc) for this particular
	 * vehicle (acesVehicleId)<br/>
	 * <b>URL: </b>/{URI}/types/{acesVehicleId}<br/>
	 * <b>Type: </b>GET
	 * @return List<String> of service types
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/types/{acesVehicleId}")
	public BSROWebServiceResponse typeListByVehicle(@PathParam("acesVehicleId") final Long vehicleId, @Context HttpHeaders headers);
	
	/**
	 * Returns all mileage interval values (15,000, 30,000, etc) for this particular
	 * vehicle (acesVehicleId)<br/>
	 * <b>URL: </b>/{URI}mileages/{acesVehicleId}/{serviceType}<br/>
	 * <b>Type: </b>GET
	 * @return List<String> of mileages for this vehicle/service type combination
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mileages/{acesVehicleId}")
	public BSROWebServiceResponse mileageListByVehicle(
			@PathParam("acesVehicleId") final Long vehicleId, 
			@QueryParam("serviceType") final String serviceType, @Context HttpHeaders headers);
	
	/**
	 * Returns all Required Maintenance (Periodic Maintenance Schedule) for this particular
	 * vehicle (acesVehicleId)<br/>
	 * <b>URL: </b>/{URI}/required/{acesVehicleId}<br/>
	 * <b>Type: </b>GET
	 * @return {@link ScheduledMaintenances}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/required/{acesVehicleId}")
	public BSROWebServiceResponse requiredMaintenance(@PathParam("acesVehicleId") final Long vehicleId, @Context HttpHeaders headers);
	
	/**
	 * Returns all Additional Periodic Maintenance Schedules for this particular
	 * vehicle (acesVehicleId)<br/>
	 * <b>URL: </b>/{URI}/periodic/{acesVehicleId}<br/>
	 * <b>Type: </b>GET
	 * @return {@link PeriodicMaintenanceMilestones}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/periodic/{acesVehicleId}")
	public BSROWebServiceResponse periodicMaintenance(@PathParam("acesVehicleId") final Long vehicleId, @Context HttpHeaders headers);
	
	/**
	 * Returns all Interval Service Check-ins Schedules (Service Checks) for this particular
	 * vehicle (acesVehicleId)<br/>
	 * <b>URL: </b>/{URI}/checks/{acesVehicleId}<br/>
	 * <b>Type: </b>GET
	 * @return {@link ScheduledMaintenances}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/checks/{acesVehicleId}")
	public BSROWebServiceResponse serviceCheckMaintenance(@PathParam("acesVehicleId") final Long vehicleId, @Context HttpHeaders headers);
	
	/**
	 * Returns all Maintenance Milestone schedules for this particular vehicle (acesVehicleId)
	 * and service type (driving condition).<br/>
	 * <b>URL: </b>/{URI}/milestones/{acesVehicleId}/{serviceType}?mileage={String}&index={numeric}<br/>
	 * <b>Type: </b>GET
	 * @return {@link MaintenanceMilestones}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/milestones/{acesVehicleId}")
	public BSROWebServiceResponse milestoneMaintenance(
			@PathParam("acesVehicleId") final Long vehicleId, 
			@QueryParam("serviceType") final String serviceType,
			@QueryParam("mileage") final String mileageInterval,
			@QueryParam("index") @DefaultValue("-1") Integer indexId, @Context HttpHeaders headers);
	

}
