/**
 * 
 */
package com.bsro.api.rest.ws.ipinfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * The service returns gets the current Zip code of the user based </br>
 * on the machine IP address
 * 
 * @author schowdhury
 *
 */
@Path(IPInfoWebService.URL)
public interface IPInfoWebService {

	public static final String URL = "/ipinfo";  
	
	/**              
	 * Returns all Years available for products from OATS.<br/>
	 * <b>URL: </b>/{URI}/ipinfo/ip<br/>
	 * <b>Type: </b>GET<br/>
	 * <ul>Parameters - IP address of the user's machine
	 * 
	 * @return zip code string of the user's location
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ip")
	public BSROWebServiceResponse getZipByIp(@QueryParam("ipAddress") String ipAddress, 
											@Context HttpHeaders headers);
}

