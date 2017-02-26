/**
 * 
 */
package com.bsro.api.rest.ws.vehicle;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bsro.core.exception.ws.BSROWebServiceUnexpectedErrorResponseException;

@Path(BatteryVehicleWebService.URL)
public interface BatteryVehicleWebService {
public static final String URL = "/vehicle/battery";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-engine/years")
	public BSROWebServiceResponse getYearOptions(@Context HttpHeaders headers) 
			throws BSROWebServiceUnexpectedErrorResponseException;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-engine/makes")
	public BSROWebServiceResponse getMakeOptionsByYear(@Context HttpHeaders headers, 
			@QueryParam("year") String year) throws BSROWebServiceUnexpectedErrorResponseException ;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-engine/models")
	public BSROWebServiceResponse getModelOptionsByYearAndMake(@Context HttpHeaders headers, @QueryParam("year") String year, 
			@QueryParam("make") String make) throws BSROWebServiceUnexpectedErrorResponseException ;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/options/year-make-model-engine/engine-sizes")
	public BSROWebServiceResponse getEngineOptionsByYearAndMakeAndModel(@Context HttpHeaders headers, 
			@QueryParam("year") String year, @QueryParam("make") String make, 
			@QueryParam("model") String model) throws BSROWebServiceUnexpectedErrorResponseException ;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/get/search-results")
	public BSROWebServiceResponse getBatteryListByYearMakeModelAndEngineSize(@Context HttpHeaders headers, 
			@QueryParam("year") String year, @QueryParam("make") String make, 
			@QueryParam("model") String model, @QueryParam("engine") String engineSize) throws BSROWebServiceUnexpectedErrorResponseException ;
	
	
	/**
	 * Read the battery quote details based on quoteId.
	 * <b>URL: </b>/{URI}/vehicle/battery/get/battery-quote<br/>
	 * <b>Type: </b>GET
	 * @param siteName		- web site name (FCAC/TP/HTP/WW)
	 * @param quoteId		- battery quoteId
	 * @param rebateId		- if any Offer applicable 
	 * 
	 */
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/get/battery-quote")
	public BSROWebServiceResponse getBatteryQuoteByQuoteId(@Context HttpHeaders headers, 
			@QueryParam("quoteId") String quoteId,@QueryParam("rebateId") String rebateId,@QueryParam("siteName") String siteName) throws BSROWebServiceUnexpectedErrorResponseException ;
	
	
	/**
	 * Read the battery quote details based on quoteId.
	 * <b>URL: </b>/{URI}/vehicle/battery/get/battery-quote<br/>
	 * <b>Type: </b>POST
	 * @param siteName		- web site name (FCAC/TP/HTP/WW)
	 * @param storeNumber 	- store number for which the battery quote is created
	 * @param productCode	- battery productcode to save quote
	 * @param zip			- zip code for which the battery quote is created
	 * @param year			- vehicle year
	 * @param make			- vehicle make
	 * @param model			- vehicle model
	 * @param engine		- vehicle engine
	 * 
	 */
	
	@POST
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/save/battery-quote")
	public BSROWebServiceResponse saveBatteryQuote(@Context HttpHeaders headers, 
			@QueryParam("siteName") String siteName,@QueryParam("storeNumber") String storeNumber, @QueryParam("productCode") String productCode, @QueryParam("zip") String zip, @QueryParam("year") String year,@QueryParam("make") String make, @QueryParam("model") String model,
			@QueryParam("engine") String engine) throws BSROWebServiceUnexpectedErrorResponseException ;
	
	/**
	 * <p>
	 * This service method: Accepts Zip code and provides Battery's life.
     * </p>
     * 
	 * <b>URL: </b>/{URI}/vehicle/battery/get/battery-life<br/>
	 * <b>Type: </b>GET
	 * @param zip - Zip code, to get the life of battery according to the location
	 * @param siteName - Name of the site where request received from
	 * @return 1) lifeYear - Life of battery in years <br/>
	 *         2) lifeMonth - Life of Months which are more than the above year.
	 *         3) lifeTotMonth - Life of battery in Months
	 *         
	 *         If the Battery life is 3 Years & 4 Months then the result will be: lifeYear=3, lifeMonth=4 and lifeTotMonth=40
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/get/battery-life")
	public BSROWebServiceResponse getBatteryLife(@Context HttpHeaders headers, 
			@QueryParam("zip") String zip, @QueryParam("siteName") String siteName) throws BSROWebServiceUnexpectedErrorResponseException ;
}