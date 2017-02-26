package com.bsro.api.rest.ws.store;

/**
 * @author smoorthy
 *
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import javax.servlet.http.HttpServletRequest;

import app.bsro.model.webservice.BSROWebServiceResponse;

@Path(StoresWebService.URL)
public interface StoresWebService {
	public static final String URL = "/store";
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/info")
	public BSROWebServiceResponse getStoreDetails(@QueryParam("storeNumber") final Long storeNumber, @Context HttpHeaders headers);
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/info/{storeType}")
	public BSROWebServiceResponse getStoreDetailsByType(@PathParam("storeType") final String storeType, @Context HttpHeaders headers);
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/locator/geo/{lat}/{lng}/{siteName}")
	public BSROWebServiceResponse getStoreLocationByGeoPoint(@PathParam("lat") final String lat, @PathParam("lng") final String lng, 
			@PathParam("siteName") final String siteName, @QueryParam("storeCount") final Integer storeCount, @Context HttpHeaders headers);
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/locator/state/{state}/{siteName}")
	public BSROWebServiceResponse getStoreLocationByState(@PathParam("state") final String state, @PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/locator/zip/{zip}/{siteName}")
	public BSROWebServiceResponse getStoreLocationByZip(@PathParam("zip") final String zip, @PathParam("siteName") final String siteName, 
			@QueryParam("storeCount") final Integer storeCount, @Context HttpHeaders headers, @Context HttpServletRequest request);
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/locator/address/{siteName}")
	public BSROWebServiceResponse getStoreLocationByAddress(@QueryParam("address") final String address, @QueryParam("city") final String city,
			@QueryParam("state") final String state, @QueryParam("zip") final String zip, @PathParam("siteName") final String siteName, 
			@QueryParam("storeCount") final Integer storeCount, @Context HttpHeaders headers, @Context HttpServletRequest request);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/services/{siteName}")
	public BSROWebServiceResponse getStoreFlagsBySite(@PathParam("siteName") final String siteName, @Context HttpHeaders headers, 
			@Context HttpServletRequest request);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/services")
	public BSROWebServiceResponse getStoreFlagsByStoreNumber(@QueryParam("storeNumber") final String storeNumber, @Context HttpHeaders headers, 
			@Context HttpServletRequest request);
}
