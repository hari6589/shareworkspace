package com.bsro.api.rest.ws.promotions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;

@Path(PromotionsWebService.URL)
public interface PromotionsWebService {
	public static final String URL = "/promotions";
	
	/**
	 * Returns ALL special offers for a particular site with an optional friendlyId<br/>
	 * <b>URL: </b>{URI}/promotions/specialoffers/{siteName}<br/>
	 * <b>Type: </b>GET
	 * 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/specialoffers/{siteName}")
	public BSROWebServiceResponse getSpecialOffers(@PathParam("siteName") final String siteName, 
			@QueryParam("storeNumber") final Long storeNumber, @QueryParam("friendlyId") String friendlyId,
			@QueryParam("promoType") String promoType, @QueryParam("promoCategory") String promoCategory, @Context HttpHeaders headers);
	
	/**
	 * Returns ALL promotions for a particular type(Ex- AllState) for a particular site with an optional friendlyId<br/>
	 * <b>URL: </b>{URI}/promotions/offers/{siteName}/{promotionType}<br/>
	 * <b>Type: </b>GET
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/offers/{siteName}/{landingPage}")
	public BSROWebServiceResponse getPromotionsbyLandingPage(@PathParam("siteName") final String siteName, 
			@PathParam("landingPage") final String landingPageId, @QueryParam("friendlyId") String friendlyId,
			@QueryParam("source") String source, @Context HttpHeaders headers);

}
