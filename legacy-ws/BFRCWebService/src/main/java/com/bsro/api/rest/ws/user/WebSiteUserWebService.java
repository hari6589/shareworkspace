package com.bsro.api.rest.ws.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceRequest;
import app.bsro.model.webservice.BSROWebServiceResponse;

@Path(WebSiteUserWebService.URL)
public interface WebSiteUserWebService {
	public static final String URL = "/web-site-user";
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/auth-user")
	public BSROWebServiceResponse authenticateWebSiteUser(@Context HttpHeaders headers,
			@FormParam("email") final String email,
			@FormParam("password") final String password);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/get-user")
	public BSROWebServiceResponse getWebSiteUser(@Context HttpHeaders headers,
		@QueryParam("email") final String email);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/get-user-by-id")
	public BSROWebServiceResponse getWebSiteUser(@Context HttpHeaders headers, 
			@QueryParam("webSiteUserId") final Long webSiteUserId);
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/forgot-password")
	public BSROWebServiceResponse sendForgottenPasswordEmail(@Context HttpHeaders headers,
			@FormParam("email") final String email,
			@FormParam("emailLink") final String emailLink,
			@FormParam("fullSiteName") final String fullSiteName);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/create-login-user")
	public BSROWebServiceResponse createWebSiteUser(@Context HttpHeaders headers, 
			@FormParam("email") final String email,
			@FormParam("passwordOne") final String passwordOne,
			@FormParam("passwordTwo") final String passwordTwo);
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/create-anonymous-user")
	public BSROWebServiceResponse createWebSiteAnonymousUser(@Context HttpHeaders headers);

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/create-login-user-from-anonymous-user")
	public BSROWebServiceResponse createLoginUserFromAnonymousUser(@Context HttpHeaders headers,
			@FormParam("webSiteUserId") final Long webSiteUserId,
			@FormParam("email") final String email,
			@FormParam("passwordOne") final String passwordOne,
			@FormParam("passwordTwo") final String passwordTwo);
		
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/reset-password")
	public BSROWebServiceResponse resetPassword(@Context HttpHeaders headers,
			@FormParam("email") final String email,
			@FormParam("passwordOne") final String passwordOne,
			@FormParam("passwordTwo") final String passwordTwo,
			@FormParam("token") final String tokent);
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update-user")
	public BSROWebServiceResponse updateWebSiteUser(@Context HttpHeaders headers, final BSROWebServiceRequest request);

}