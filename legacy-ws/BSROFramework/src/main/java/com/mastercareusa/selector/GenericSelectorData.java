package com.mastercareusa.selector;

import java.sql.SQLException;

public interface GenericSelectorData {
	public GenericVehicle getVehicleById(Long vehicleId) throws SQLException;
}
