package com.bfrc.framework.dao.vehicle;


import java.util.List;
import java.util.Map;

import com.bfrc.framework.businessdata.BusinessOperatorSupport;
import com.bfrc.framework.dao.VehicleDAO;

public class ListVehiclesOperator extends BusinessOperatorSupport {
	
	private VehicleDAO vehicleDAO;
	
	public Object operate(Object o) throws Exception {
		Map m = (Map)o;
		List l = null;
		if(m.containsKey("modelName")) {
			l = this.vehicleDAO.getSubmodels(m);
		} else if(m.containsKey("makeName")) {
			l = this.vehicleDAO.getModelNames(m);
		} else if(m.containsKey("modelYear")) {
			l = this.vehicleDAO.getMakeNames(m);
		} else {
			l = this.vehicleDAO.getModelYears(m);
		}
		m.put(RESULT, l);
		return "dropdown";
	}
	public VehicleDAO getVehicleDAO() {
		return this.vehicleDAO;
	}
	public void setVehicleDAO(VehicleDAO vehicleDAO) {
		this.vehicleDAO = vehicleDAO;
	}

}
