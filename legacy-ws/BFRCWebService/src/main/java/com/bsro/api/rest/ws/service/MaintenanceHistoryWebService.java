package com.bsro.api.rest.ws.service;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import app.bsro.model.maintenance.MaintenanceSearchResults;
import app.bsro.model.scheduled.maintenance.JobResults;

/**
 * 
 * @author Brad Balmer
 *
 */
@Path(MaintenanceHistoryWebService.URL)
public interface MaintenanceHistoryWebService {
	public static final String URL = "/maintenance/history";

	/**
	 * Do we allow access to NCD?<br/>
	 * <b>URL: </b>/{URI}/maintenance/history/alive<br/>
	 * <b>Type: </b>GET
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/alive")
	public Response allowNcdAccess( @Context HttpHeaders headers );

	/**
	 * Queries the NCD system for vehicles that match a subset of the query parameters.<br/>
	 * <b>URL: </b>/{URI}/maintenance/history/search?phone={10-digit}&phone={10-digit}<br/>
	 * <i>or</i>
	 * <b>URL: </b>/{URI}/maintenance/history/search?storeId={numeric}&invoiceId={numeric}<br/>
	 * <b>Type: </b>GET
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search")
	public MaintenanceSearchResults searchForVehicles( @Context HttpHeaders headers, @Context UriInfo info,
			@QueryParam("filter") @DefaultValue("true") boolean filterResults);
	

	/**
	 * Returns ALL Purchase Records for the vehicle with the optional since date parameter<br/>
	 * <b>URL: </b>{URI}/maintenance/history/{vehicleId}?sinceDt={numeric(ms)}<br/>
	 * <b>Type: </b>GET
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{vehicleId}")
	public JobResults selectJobData(
			@Context HttpHeaders headers,
			@PathParam("vehicleId") final String vehicleId,
			@QueryParam("sinceDt") final Long since,
			@QueryParam("filter") @DefaultValue("true") boolean filterResults);
	

	/**
	 * Returns ALL Purchase Records for the multiple vehicle id values with the optional since date parameter<br/>
	 * <b>URL: </b>{URI}/maintenance/history/invoice?(vehicleId={vehicleId})*?sinceDt={numeric(ms)}<br/>
	 * <b>Type: </b>GET
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/invoice")
	public JobResults selectInvoiceData(@Context HttpHeaders headers, 
			@Context UriInfo info,
			@QueryParam("filter") @DefaultValue("true") boolean filterResults);
}
