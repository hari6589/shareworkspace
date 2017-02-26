package com.bsro.api.rest.ws.gas;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.gas.mpg.EpaMpgSearchResult;
import app.bsro.model.gas.stations.StationPrices;

/**
 * Services different aspects pertaining to gasoline.
 * Services include:
 * <ul>
 * <li>Allows a user to search for service stations within a predefined area for the cheapest gas</li>
 * <li>Allows a user to find the EPA data for particular MPG data for a vehicle</li>
 * </ul>
 * @author Brad Balmer
 *
 */
@Path(GasolineWebService.URL)
public interface GasolineWebService {
	public static final String URL = "/gas";
	
	/**              
	 * Returns all service stations within a predefined radius order by the cheapest gasoline prices.<br/>
	 * <b>URL: </b>/{URI}/gas/find/price?address={string}&geoPoint={string}&radius={numeric}<br/>
	 * <b>Type: </b>GET<br/>
	 * <ul>Parameters - One of address or geoPoint is required
	 * <li>address - [optional] any combination of city, state and zip code</li>
	 * <li>geoPoint - [optional] comma separated Longitude,Latitude value</li>
	 * <li>radius - [optional] mileage radius to return.
	 * @return {@link StationPrices} a list of StationPrice values
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/find/price")
	public StationPrices findCheapGas(@Context HttpHeaders headers,
		@QueryParam("address") final String address, 
		@QueryParam("geoPoint") final String geoPoint,
		@QueryParam("radius") final String radius,
		@QueryParam("maxCount") final String maxCount,
		@QueryParam("grade") @DefaultValue("U") final String grade);
	
}
