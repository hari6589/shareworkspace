package com.bfrc.dataaccess.svc.webdb.emailcollection;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */
public interface EmailCollectionService {
	
	public BSROWebServiceResponse getPACUserDetails(String tendigitcode);
	
	public BSROWebServiceResponse updatePACUserDetails(String tendigitcode, String firstName, String lastName, String email);
	
	public BSROWebServiceResponse logRewards(String tenDigitCode, String confirmOptIn, String siteName);

}
