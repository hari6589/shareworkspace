package com.bfrc.dataaccess.svc.catalog;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */
public interface CatalogService {
	
	public BSROWebServiceResponse getTireProductDetails(String siteName, String brand, String tireName, String displayId);
	
	public BSROWebServiceResponse getTiresByVehicleType(String vehicleType, String siteName, String sortBy);

}
