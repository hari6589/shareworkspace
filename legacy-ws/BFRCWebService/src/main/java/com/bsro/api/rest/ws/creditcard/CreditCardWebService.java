package com.bsro.api.rest.ws.creditcard;

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

@Path(CreditCardWebService.URL)
public interface CreditCardWebService {
	
	public static final String URL = "/creditcard";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{siteName}")
	public BSROWebServiceResponse getCrediCardDetails(@PathParam("siteName") final String siteName, @Context HttpHeaders headers);

}
