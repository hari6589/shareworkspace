package com.bsro.service.maintenance;

import java.io.IOException;
import java.util.List;

import com.bsro.pojo.maintenance.ScheduledMaintenanceDataBean;
import com.mastercareusa.selector.GenericVehicle;

import app.bsro.model.maintenance.MaintenanceMilestones;
import app.bsro.model.maintenance.PeriodicMaintenanceMilestones;
import app.bsro.model.maintenance.ScheduledMaintenances;

public interface ScheduledMaintenanceService {
	
	public ScheduledMaintenanceDataBean getAllMaintenanceData(Long vehicleId, GenericVehicle vehicle, Long mileage, String serviceType) throws IOException;
	
	public ScheduledMaintenances getRequiredMaintenance(Long vehicleId) throws IOException;
	
	public ScheduledMaintenances getServiceCheckMaintenance(Long vehicleId) throws IOException;

	public PeriodicMaintenanceMilestones getPeriodicMaintenance(Long vehicleId) throws IOException;
	
	public MaintenanceMilestones getMaintenanceMilestone(Long vehicleId, String vehicleServiceType) throws IOException;
	
	public List<String> getMaintenanceCategories(Long vehicleId) throws IOException;
	
	public List<Long> getMaintenanceMileageIntervals(Long vehicleId, String vehicleServiceType) throws IOException;
	
	public List<String> getServiceTypes(Long vehicleId) throws IOException;
	
	public List<String> getSubmodelNamesByAcesVehicleId(Long acesVehicleId);
}
