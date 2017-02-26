package com.bfrc.dataaccess.svc.webdb.system;

import com.bfrc.dataaccess.model.system.BsroWebServiceRequests;
import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestServiceImpl.WebServiceId;

public interface WebServiceRequestsService {

	/**
	 * Pulls the web service request for the id.
	 * @param id {@link WebServiceId}
	 * @return
	 */
	public BsroWebServiceRequests getRequest(WebServiceId id);
	
	/**
	 * Updates an existing record.  If the ID does not exist then nothing will happen.
	 * @param record
	 */
	public void save(BsroWebServiceRequests record);
	
}
