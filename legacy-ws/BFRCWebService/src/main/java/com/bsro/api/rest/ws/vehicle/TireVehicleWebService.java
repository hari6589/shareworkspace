package com.bsro.api.rest.ws.vehicle;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bsro.core.exception.ws.BSROWebServiceUnexpectedErrorResponseException;

@Path(TireVehicleWebService.URL)
public interface TireVehicleWebService {
	public static final String URL = "/vehicle/tire";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-submodel/years")
	public BSROWebServiceResponse getYearOptions(@Context HttpHeaders headers) throws BSROWebServiceUnexpectedErrorResponseException;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-submodel/makes")
	public BSROWebServiceResponse getMakeOptionsByYear(@Context HttpHeaders headers, @QueryParam("year") String year) 
			throws BSROWebServiceUnexpectedErrorResponseException ;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-submodel/models")
	public BSROWebServiceResponse getModelOptionsByYearAndMake(@Context HttpHeaders headers, 
			@QueryParam("year") String year, @QueryParam("make") String make) throws BSROWebServiceUnexpectedErrorResponseException ;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-submodel/submodels")
	public BSROWebServiceResponse getSubModelOptionsByYearAndMakeAndModel(@Context HttpHeaders headers, @QueryParam("year") String year, 
			@QueryParam("make") String make, @QueryParam("model") String model) throws BSROWebServiceUnexpectedErrorResponseException ;

	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/tire-pressure/get")
	public BSROWebServiceResponse getTirePressureDetails(@Context HttpHeaders headers, @Context UriInfo uriInfo) 
					throws BSROWebServiceUnexpectedErrorResponseException ;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/store-inventory")
	public BSROWebServiceResponse getStoreInventory(@Context HttpHeaders headers, @QueryParam("storeNumber") String storeNumber);
	
	
	/**
	 * get the alignment pricing by passing storeNumber.
	 * <b>URL: </b>/{URI}/vehicle/tire/repair-alignment-pricing/get<br/>
	 * <b>Type: </b>GET
	 * @param storeNumber 	- store number for which used to get alignment pricing
	 * 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/repair-alignment-pricing/get")
	public BSROWebServiceResponse getAlignmentPricing(@Context HttpHeaders headers, @QueryParam("acesVehicleId") String acesVehicleId ,@QueryParam("storeNumber") String storeNumber , @QueryParam("siteName") String siteName);
	
	/**
	 * get the alignment quote based on quoteId.
	 * <b>URL: </b>/{URI}/vehicle/tire/repair-alignment-quote/get<br/>
	 * <b>Type: </b>GET
	 * @param quoteId 	- based on quoteId we can read full quote
	 * @param siteName		- web site name (FCAC/TP/HTP/WW)
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/repair-alignment-quote/get")
	public BSROWebServiceResponse getAlignmentQuote(@Context HttpHeaders headers, @QueryParam("quoteId") String quoteId,@QueryParam("siteName") String siteName);
	
	/**
	 * Create a repair alignment quote for the store number and repair alignment article number and save the details in database.
	 * <b>URL: </b>/{URI}/vehicle/tire/repair-alignment-quote/save<br/>
	 * <b>Type: </b>POST
	 * @param storeNumber 	- store number for which the tire quote is created
	 * @param articleNumber	- tire article number which the boss prefers
	 * @param altype		- alignment type
	 * @param alpricingId		- alignment pricing id
	 * @param acesVehicleId	- ACES vehicle Id
	 * @param firstName		- customer first name
	 * @param lastName		- customer last name
	 * @param siteName		- web site name (FCAC/TP/HTP/WW)
	 * @param emailId		- customer mail id
	 * 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/repair-alignment-quote/save")
	public BSROWebServiceResponse createRepairAlignmentQuote(@Context HttpHeaders headers,
						@QueryParam("storeNumber") String storeNumber, 
						@QueryParam("articleNumber") String articleNumber, 
						@QueryParam("altype") String altype,
						@QueryParam("acesVehicleId") String acesVehicleId,
						@QueryParam("alpricingId") String alpricingId,
						@QueryParam("firstName") String firstName,
						@QueryParam("lastName") String lastName,
						@QueryParam("emailId") String emailId,
						@QueryParam("siteName") String siteName
						);

	
}

