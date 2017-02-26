package com.bsro.api.rest.ws.test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(TestWebService.URL)
public interface TestWebService {
	public static final String URL = "/test";
	
	/**
	 * Tests all of the web services to ensure that they are working
	 * @param headers
	 * @return
	 */
	@GET	
	@Produces(MediaType.TEXT_HTML)
	@Path("/")
	public Response testAll(@Context HttpHeaders headers, @Context HttpServletRequest request);

	 
	@GET	
	@Path("/appt")
	public Response testAppt(@Context HttpHeaders headers, @Context HttpServletRequest request);
	
}
