package com.bsro.core.svc;

import javax.ws.rs.core.HttpHeaders;

import com.bsro.core.model.Authenticate;

/**
 * Either authenticate a user's credentials or ensure that the request that is being handled by the application
 * contains the valid token parameters in the HTTP Header.
 * @author Brad Balmer
 *
 */
public interface AuthenticateService {
	
	/**
	 * Function to indicate if security, in general, is required for the project.
	 * @return
	 */
	public boolean isRequired();
	
	/**
	 * Inspects the HTTP Header of the request to ensure that the tokenId passed in 
	 * is valid.
	 * @param headers
	 * @return true if a valid token has been passed, false otherwise
	 */
	public boolean validateToken(HttpHeaders headers);
	
	/**
	 * Inspects the HTTP Header of the request to ensure that the user and tokenId passed in 
	 * is valid.
	 * @param headers
	 * @param serviceName
	 * @return true if a valid appName and tokenId combination has been passed, false otherwise
	 */
	public boolean validateAppNameAndToken(HttpHeaders headers, String serviceName);
}
