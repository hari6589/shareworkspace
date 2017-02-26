package com.bsro.api.rest.ws.pricing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author stallin sevugamoorthy
 *
 */
@Path(TirePricingWebService.URL)
public interface TirePricingWebService {
	public static final String URL = "/tires/pricing";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/results")
	public BSROWebServiceResponse getTirePricingResults(@QueryParam("siteName") final String siteName,
			@QueryParam("storeNumber") Long storeNumber, 
			@QueryParam("acesVehicleId") final Long acesVehicleId,
			@QueryParam("tpms") final Integer tpms,
			@QueryParam("cross") String cross,
			@QueryParam("aspect") String aspect,
			@QueryParam("rim") String rim,
			@Context HttpHeaders headers);
}
