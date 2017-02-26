package com.bsro.api.rest.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Simple interface to return the current version of the web service.
 * @author Brad Balmer
 *
 */
@Path(VersionWebService.URL)
public interface VersionWebService {
	public static final String URL = "/version";
	
	/**
	 * Returns the current version of the webservice that is deployed<br/><br/>
	 * <b>Type: </b>GET
	 * <b>URL: </b>/{URI}/version<br/>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public String getVersion();
}
