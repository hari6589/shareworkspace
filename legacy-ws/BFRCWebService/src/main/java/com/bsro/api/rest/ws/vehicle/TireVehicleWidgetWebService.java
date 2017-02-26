/**
 * 
 */
package com.bsro.api.rest.ws.vehicle;

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
@Path(TireVehicleWidgetWebService.URL)
public interface TireVehicleWidgetWebService {
	
	public static final String URL = "/widget/";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/by-vehicle")
	public BSROWebServiceResponse getYearOptions(@Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/by-vehicle/{year}")
	public BSROWebServiceResponse getMakeOptions(@PathParam("year") final String year, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/by-vehicle/{year}/{makeId}")
	public BSROWebServiceResponse getModelOptions(@PathParam("year") final String year, @PathParam("makeId") final String makeId, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/by-vehicle/{year}/{makeId}/{modelId}")
	public BSROWebServiceResponse getSubmodelOptions(@PathParam("year") final String year, @PathParam("makeId") final String makeId, @PathParam("modelId") final String modelId, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/by-size")
	public BSROWebServiceResponse getCrossSectionOptions(@Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/by-size/{cross}")
	public BSROWebServiceResponse getAspectRatioOptions(@PathParam("cross") final String cross, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/by-size/{cross}/{aspectRatio}")
	public BSROWebServiceResponse getRimOptions(@PathParam("cross") final String cross, @PathParam("aspectRatio") final String aspectRatio, @Context HttpHeaders headers);

}
