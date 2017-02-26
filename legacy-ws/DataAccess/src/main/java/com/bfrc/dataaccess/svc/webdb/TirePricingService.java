package com.bfrc.dataaccess.svc.webdb;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author stallin sevugamoorthy
 *
 */
public interface TirePricingService {
	
	public BSROWebServiceResponse getTirePricingResults(String siteName, Long storeNumber, Long acesVehicleId, 
			Integer tpms, String cross, String aspect, String rim);

}
