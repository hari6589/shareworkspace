/**
 * 
 */
package com.bsro.api.rest.ws.tires;

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

@Path(TiresWebService.URL)
public interface TiresWebService {
	
	public static final String URL = "/tires";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle")
	public BSROWebServiceResponse getMakeOptions(@QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/{makeName}")
	public BSROWebServiceResponse getModelOptionsByMake(@PathParam("makeName") final String makeName, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/seo/{makeName}")
	public BSROWebServiceResponse getSeoContentByMake(@PathParam("makeName") final String makeName, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/{makeName}/{modelName}")
	public BSROWebServiceResponse getYearOptionsByMakeModelNames(@PathParam("makeName") final String makeName, @PathParam("modelName") final String modelName, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/seo/{makeName}/{modelName}")
	public BSROWebServiceResponse getSeoContentByMakeModelNames(@PathParam("makeName") final String makeName, @PathParam("modelName") final String modelName, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/{makeName}/{modelName}/{year}")
	public BSROWebServiceResponse getSubmodelOptionsByYearMakeModelNames(@PathParam("makeName") final String makeName, @PathParam("modelName") final String modelName, @PathParam("year") final String year, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/seo/{makeName}/{modelName}/{year}")
	public BSROWebServiceResponse getSeoContentByYearMakeModelNames(@PathParam("makeName") final String makeName, @PathParam("modelName") final String modelName, @PathParam("year") final String year, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/{makeName}/{modelName}/{year}/{trim}")
	public BSROWebServiceResponse getSubmodelOptionByYearMakeModelSubmodelNames(@PathParam("makeName") final String makeName, @PathParam("modelName") final String modelName, @PathParam("year") final String year, @PathParam("trim") final String trim, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/seo/{makeName}/{modelName}/{year}/{trim}")
	public BSROWebServiceResponse getSeoContentByYearMakeModelSubmodelNames(@PathParam("makeName") final String makeName, @PathParam("modelName") final String modelName, @PathParam("year") final String year, @PathParam("trim") final String trim, @QueryParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/vehicle/seo/{siteName}/{acesVehicleId}")
	public BSROWebServiceResponse getSEODataByVehicle(@PathParam("acesVehicleId") final Long acesVehicleId, @PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/size/seo/{siteName}/{cross}/{aspect}/{rim}")
	public BSROWebServiceResponse getSEODataBySize(@PathParam("cross") final String cross, @PathParam("aspect") final String aspect, @PathParam("rim") final String rim, @PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/size/{siteName}")
	public BSROWebServiceResponse getRimDiameters(@PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/size/{rimSize}/{siteName}")
	public BSROWebServiceResponse getTireSizes(@PathParam("rimSize") final String rimSize, @PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/size/seo/{rimSize}/{siteName}")
	public BSROWebServiceResponse getSeoContentByRimSize(@PathParam("rimSize") final String rimSize, @PathParam("siteName") final String siteName, @Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{category}/{siteName}")
	public BSROWebServiceResponse getTiresByVehicleType(@PathParam("category") final String category, 
			@PathParam("siteName") final String siteName, @QueryParam("sortBy") String sortBy, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/make-model-list/{category}")
	public BSROWebServiceResponse getMakeModelsByVehicleType(@PathParam("category") final String category, @Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/tire-pressure/{acesVehicleId}")
	public BSROWebServiceResponse getTirePressureDetails(@PathParam("acesVehicleId") final Long acesVehicleId, @Context HttpHeaders headers);

}
