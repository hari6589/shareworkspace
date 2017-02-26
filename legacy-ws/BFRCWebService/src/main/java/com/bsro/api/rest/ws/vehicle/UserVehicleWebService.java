package com.bsro.api.rest.ws.vehicle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.bfrc.dataaccess.model.VehicleSearchResult;

@Path(UserVehicleWebService.URL)
public interface UserVehicleWebService {
	public static final String URL = "/vehicle";

	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/filter")
	public VehicleSearchResult filter( 		
			@QueryParam("year") String year,
			@QueryParam("make") Long makeId,
			@QueryParam("model") Long modelId,
			@Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/search")
	public Long search( 		
			@PathParam("year") String year,
			@PathParam("makeId") Long makeId,
			@PathParam("modelId") Long modelId,
			@Context HttpHeaders headers);
}
