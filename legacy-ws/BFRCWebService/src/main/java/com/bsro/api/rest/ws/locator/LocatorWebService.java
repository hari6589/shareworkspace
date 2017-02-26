package com.bsro.api.rest.ws.locator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */

@Path(LocatorWebService.URL)
public interface LocatorWebService {
	
	public static final String URL = "/locator";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-state")
	public BSROWebServiceResponse getState(@Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-state/{siteName}")
	public BSROWebServiceResponse getStateBySite(@PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-city/{state}/{siteName}")
	public BSROWebServiceResponse getCityByStateAndSite(@PathParam("state") final String state, @PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-stores/{state}/{city}/{siteName}")
	public BSROWebServiceResponse getStoresByCityState(@PathParam("state") final String state, @PathParam("city") final String city, 
			@PathParam("siteName") final String siteName, @Context HttpHeaders headers);

}
