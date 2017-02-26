package com.bsro.api.rest.ws.rewards;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */

@Path(RewardsWebService.URL)
public interface RewardsWebService {
	
	public static final String URL = "/rewards";
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/pac/{tenDigitCode}")
	public BSROWebServiceResponse getPACUserDetails(@PathParam("tenDigitCode") final String tenDigitCode, @Context HttpHeaders headers);
	
	
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/pac/update/{tenDigitCode}")
	public BSROWebServiceResponse updatePACUserDetails(@PathParam("tenDigitCode") final String tenDigitCode, @QueryParam("firstName") final String firstName,
			@QueryParam("lastName") final String lastName, @QueryParam("email") final String email, @Context HttpHeaders headers);
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/pac/confirm/{tenDigitCode}")
	public BSROWebServiceResponse logRewards(@PathParam("tenDigitCode") final String tenDigitCode, 
			@QueryParam("confirmOptIn") final String confirmOptIn, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);

}
