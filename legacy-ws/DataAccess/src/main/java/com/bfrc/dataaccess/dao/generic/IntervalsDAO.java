package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.maintenance.Intervals;
import com.bfrc.dataaccess.model.maintenance.IntervalsId;

public interface IntervalsDAO extends IGenericOrmDAO<Intervals, IntervalsId> {

	public Collection<Intervals> findByVehicleWithMileage(Long acesVehicleId);
	public Collection<Intervals> findByVehicleAndServiceType(Long acesVehicleId, String serviceType);
	public Collection<Intervals> findByVehicleAndServiceTypeWithMileage(Long acesVehicleId, String serviceType);
	public Collection<String> findDistinctServiceTypesByVehicle(Long acesVehicleId);
	public Collection<String> findDistinctMileageByVehicle(Long acesVehicleId, String serviceType);
}
