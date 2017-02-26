package com.bfrc.dataaccess.svc.webdb;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */
public interface LocatorService {
	
	public BSROWebServiceResponse getState();
	
	public BSROWebServiceResponse getStateBySite(final String siteName);
	
	public BSROWebServiceResponse getCityByStateAndSite(final String state, final String siteName);
	
	public BSROWebServiceResponse getStoresByStateAndCity(final String state, final String city, final String siteName);

}
