package com.bsro.api.rest.ws.system;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import com.bfrc.dataaccess.model.system.BsroWebServiceRequests;

@Path(SystemRequestWebService.URL)
public interface SystemRequestWebService {
	public static final String URL = "/system";
	

	/**
	 * Tests all of the web services to ensure that they are working
	 * @param headers
	 * @return
	 */
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/toggle")
	public BsroWebServiceRequests toggle(@Context HttpHeaders headers,
		@QueryParam("code") final String serviceCd);
	
	
	/**
	 * Tests all of the web services to ensure that they are working
	 * @param headers
	 * @return
	 */
	@GET	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/save")
	public BsroWebServiceRequests save(
		@Context HttpHeaders headers,
		@QueryParam("code") final String serviceCd,
		@QueryParam("msg") final String serviceMsg);
	
	
	@GET	
	@Produces(MediaType.TEXT_HTML)
	@Path("/list")
	public String get(@Context HttpHeaders headers);
	
}
