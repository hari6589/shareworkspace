package com.bfrc.dataaccess.svc.webdb;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */
public interface RealEstateService {
	
	public BSROWebServiceResponse getSurplusPropertyDetails(String siteName);

}
