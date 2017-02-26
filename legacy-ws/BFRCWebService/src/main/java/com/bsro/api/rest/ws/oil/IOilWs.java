package com.bsro.api.rest.ws.oil;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.oil.OilChangeQuote;
import app.bsro.model.oil.OilChangeSearchResult;

import com.bsro.core.exception.ws.ErrorMessageException;
import java.io.IOException;

/**
 * Services different aspects pertaining to oil.
 * Services include:
 * <ul>
 * <li>Allows a user to search for year, manufacturer, model, and oil types</li>
 * </ul>
 * @author Brad Balmer
 *
 */
@Path(IOilWs.URL)
public interface IOilWs {
	public static final String URL = "/oil";
	
	/**              
	 * Returns all Years available for products from OATS.<br/>
	 * <b>URL: </b>/{URI}/oil/years<br/>
	 * <b>Type: </b>GET<br/>
	 * <ul>Parameters - none
	 * 
	 * @return {@link Map<String,String>} a map of years with guid values
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/years")
	public LinkedHashMap<String,String> getYears(@Context HttpHeaders headers) throws IOException;
	
	/**
	 * Returns the manufacturers for the yearId passed in from OATS.
	 * <b>URL: </b>/{URI}/oil/manufacturers?yearId={string}<br/>
	 * <b>Type: </b>GET
	 * @return {@link Map<String,String>} a map of manufacturers with guid values
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/manufacturers")
	public LinkedHashMap<String,String> getManufacturers(@Context HttpHeaders headers,
		@QueryParam("yearId") final String year) throws IOException;
	/**
	 * Returns the models for the year/manufacturer passed in.
	 * <b>URL: </b>/{URI}/oil/models?yearId={string}&manufacturerId={string}<br/>
	 * <b>Type: </b>GET
	 * @return {@link Map<String,String>} a list of models with guid values
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/models")
	public LinkedHashMap<String,String> getModels(@Context HttpHeaders headers,
		@QueryParam("yearId") final String year,
		@QueryParam("manufacturerId") final String manufacturer) throws IOException;
	
	/**
	 * Returns the products (oils) for the equipmentID passed in.
	 * <b>URL: </b>/{URI}/oil/products?equipmentId={string}&storeNumber={long}<br/>
	 * <b>Type: </b>GET
	 * @return {@link Map<String,String>} a list of oil products
	 */
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/products")
	public OilChangeSearchResult getProducts(@Context HttpHeaders headers,
		@QueryParam("equipmentId") final String equipmentId, @QueryParam("storeNumber") final Long storeNumber, @QueryParam("isHighMileageVehicle") final Boolean isHighMileageVehicle) throws IOException;

	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/create-quote")
	public Long createOilChangeQuote(@Context HttpHeaders headers, @QueryParam("oilArticleNumber") final Long oilArticleNumber, @QueryParam("storeNumber") final Long storeNumber, @QueryParam("quarts") final BigDecimal quarts, @QueryParam("vehicleId") final String vehicleId, @QueryParam("vehicleYear") final Integer vehicleYear, @QueryParam("vehicleMake") final String vehicleMake, @QueryParam("vehicleModelSubmodelEngine") final String vehicleModelSubmodelEngine, @QueryParam("customerZip") final String customerZip, @QueryParam("siteName") final String siteName)  throws ErrorMessageException;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/get-quote")
	public OilChangeQuote getOilChangeQuote(@Context HttpHeaders headers, @QueryParam("oilChangeQuoteId") final Long oilChangeQuoteId, @QueryParam("siteName") final String siteName)  throws ErrorMessageException;
		
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/update-quote/first-name")
	public String updateOilChangeQuoteFirstName(@Context HttpHeaders headers, @QueryParam("oilChangeQuoteId") final Long oilChangeQuoteId, @QueryParam("siteName") final String siteName, @QueryParam("firstName") final String firstName) throws ErrorMessageException;	

	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/update-quote/last-name")
	public String updateOilChangeQuoteLastName(@Context HttpHeaders headers, @QueryParam("oilChangeQuoteId") final Long oilChangeQuoteId, @QueryParam("siteName") final String siteName, @QueryParam("lastName") final String lastName) throws ErrorMessageException;	
	
}
