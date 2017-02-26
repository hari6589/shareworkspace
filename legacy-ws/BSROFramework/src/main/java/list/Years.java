package list;

import java.util.*;

import com.bfrc.Bean;

import com.bfrc.framework.dao.*;

public class Years extends LinkedHashMap implements Bean {

	VehicleDAO vehicleDAO;

	public VehicleDAO getVehicleDAO() {
		return this.vehicleDAO;
	}

	public void setVehicleDAO(VehicleDAO vehicleDAO) {
		this.vehicleDAO = vehicleDAO;
		initialize();
	}
	
	public void initialize() {
		List l = this.vehicleDAO.getModelYears(new HashMap());
		Iterator i = l.iterator();
		while(i.hasNext()) {
			Object o = i.next();
			this.put(o, o);
		}
	}
}
