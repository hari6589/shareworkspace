package com.bfrc.framework.dao.fleetcare;


import com.bfrc.framework.dao.*;
import com.bfrc.framework.businessdata.BusinessOperatorSupport;

public class FleetCareOperator extends BusinessOperatorSupport {

	private FleetCareDAO fleetCareDAO;
	
	public Object operate(Object o) throws Exception {
		
		return SUCCESS;
	}

	public FleetCareDAO getFleetCareDAO() {
		return this.fleetCareDAO;
	}

	public void setFleetCareDAO(FleetCareDAO fleetCareDAO) {
		this.fleetCareDAO = fleetCareDAO;
	}

}