package com.bfrc.dataaccess.svc.webdb;

import java.util.List;

import com.bfrc.dataaccess.model.maintenance.Intervals;

public interface IntervalsService {
	
	public List<String> getDistinctServiceTypes(Long acesVehicleId);
	public List<String> getDistinctServiceTypesInSvcType(Long acesVehicleId, List<String> serviceTypes);
	public List<String> getDistinctMileageValues(Long acesVehicleId, String serviceType);
	public List<Intervals> listRequiredMaintenance(Long acesVehicleId);
	public List<Intervals> listPeriodicMaintenance(Long acesVehicleId);
	public List<Intervals> listIntervalMaintenance(Long acesVehicleId);
	public List<Intervals> listMaintenanceMilestones(Long acesVehicleId, String serviceType);
	public List<Intervals> listMaintenanceMilestones(Long acesVehicleId);
	
}
