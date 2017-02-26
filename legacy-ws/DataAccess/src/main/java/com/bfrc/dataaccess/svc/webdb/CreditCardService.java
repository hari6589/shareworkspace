package com.bfrc.dataaccess.svc.webdb;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */
public interface CreditCardService {
	
	public BSROWebServiceResponse getCreditCardDetails(String siteName);

}
