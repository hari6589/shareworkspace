package com.bsro.api.rest.ws.realestate;

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

@Path(RealEstateWebService.URL)
public interface RealEstateWebService {
	
	public static final String URL = "/realestate";
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/surplusproperty/{siteName}")
	public BSROWebServiceResponse getSurplusPropertyDetails(@PathParam("siteName") final String siteName, @Context HttpHeaders headers);

}
