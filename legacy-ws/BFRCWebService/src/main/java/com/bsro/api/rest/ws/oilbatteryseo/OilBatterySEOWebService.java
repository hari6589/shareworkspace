package com.bsro.api.rest.ws.oilbatteryseo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import app.bsro.model.webservice.BSROWebServiceResponse;


@Path(OilBatterySEOWebService.URL)
public interface OilBatterySEOWebService {
	public static final String URL = "/vehicle/seo";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/")
	public BSROWebServiceResponse getSeoContent(@QueryParam("seoContent") final String seoContent, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{friendlyMakeName}")
	public BSROWebServiceResponse getSeoContentByMake(@PathParam("friendlyMakeName") final String friendlyMakeName, @QueryParam("seoContent") final String seoContent, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{friendlyMakeName}/{friendlyModelName}")
	public BSROWebServiceResponse getSeoContentByMakeModelNames(@PathParam("friendlyMakeName") final String friendlyMakeName, @PathParam("friendlyModelName") final String friendlyModelName, @QueryParam("seoContent") final String seoContent, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{friendlyMakeName}/{friendlyModelName}/{year}")
	public BSROWebServiceResponse getSeoContentByYearMakeModelNames(@PathParam("friendlyMakeName") final String friendlyMakeName, @PathParam("friendlyModelName") final String friendlyModelName, @PathParam("year") final String year, @QueryParam("seoContent") final String seoContent, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
}
