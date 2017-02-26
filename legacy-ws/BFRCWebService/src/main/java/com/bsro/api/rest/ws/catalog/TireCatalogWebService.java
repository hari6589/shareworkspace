package com.bsro.api.rest.ws.catalog;

import javax.ws.rs.GET;
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
@Path(TireCatalogWebService.URL)
public interface TireCatalogWebService {
	
	public static final String URL = "/tirecatalog";
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/{brand}/{tireName}")
	public BSROWebServiceResponse getTireProductDetails(@QueryParam("siteName") final String siteName, @PathParam("brand") final String brand, 
			@PathParam("tireName") final String tireName, @QueryParam("id") final String displayId, @Context HttpHeaders headers);

}
