package com.bsro.api.rest.ws.mail;

import javax.ws.rs.POST;
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

@Path(MailWebService.URL)
public interface MailWebService {
	
	public static final String URL = "/mail";
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/quote/{siteName}/{source}/{quoteId}")
	public BSROWebServiceResponse postEmailQuote(@PathParam("source") final String source,
			@PathParam("siteName") final String siteName,
			@PathParam("quoteId") Long quoteId,
			@QueryParam("rebateId") String rebateId,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("emailAddress") String emailAddress,
			@Context HttpHeaders headers);

}
