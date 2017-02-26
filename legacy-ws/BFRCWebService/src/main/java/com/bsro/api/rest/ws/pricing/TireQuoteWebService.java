package com.bsro.api.rest.ws.pricing;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;

import app.bsro.model.tire.TireQuote;
import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * Controls the creation of tire quote or fetch existing quote.
 * 
 * @author stallin sevugamoorthy
 *
 */
@Path(TireQuoteWebService.URL)
public interface TireQuoteWebService {
	public static final String URL = "/tires/quote";
	
	/**
	 * Create a tire quote for the store number and tire article number and save the details in database.
	 * <b>URL: </b>/{URI}/tires/quote/create<br/>
	 * <b>Type: </b>POST
	 * @param storeNumber 	- store number for which the tire quote is created
	 * @param articleNumber	- tire article number which the boss prefers
	 * @param quantity		- total number of tires
	 * @param acesVehicleId	- ACES vehicle Id
	 * @param firstName		- customer first name
	 * @param lastName		- customer last name
	 * @param siteName		- web site name (FCAC/TP/HTP/WW)
	 * 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create")
	public BSROWebServiceResponse createQuote(@QueryParam("storeNumber") Long storeNumber, 
						@QueryParam("articleNumber") String articleNumber, 
						@QueryParam("quantity") String quantity,
						@QueryParam("acesVehicleId") final Long acesVehicleId,
						@QueryParam("tpms") final Integer tpms,
						@QueryParam("firstName") final String firstName,
						@QueryParam("lastName") final String lastName,
						@QueryParam("siteName") final String siteName,
						@QueryParam("emptyCart") final String emptyCart,
						@Context HttpHeaders headers);
	
	/**
	 * Get a tire quote from database for the quote ID passed.
	 * <b>URL: </b>/{URI}/tires/quote/get<br/>
	 * <b>Type: </b>GET
	 * @param code			- tire quote id. Flowing through email link.
	 * 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get")
	public BSROWebServiceResponse getQuote(@QueryParam("quoteId") Long quoteId, 
						@Context HttpHeaders headers);
	
	@POST	
	@Consumes
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	public BSROWebServiceResponse updateQuote(JsonNode hybrisQuote, @QueryParam("quoteId") Long quoteId, 
						@Context HttpHeaders headers);
	
	@GET
	@Produces("application/pdf")
	@Path("/pdf")
	public Response getPdf(@QueryParam("quoteId") Long quoteId, 
						@Context HttpHeaders headers);
}
