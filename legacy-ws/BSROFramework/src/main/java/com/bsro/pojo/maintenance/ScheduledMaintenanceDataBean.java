package com.bsro.pojo.maintenance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import app.bsro.model.maintenance.MaintenanceMilestones;
import app.bsro.model.maintenance.OperationTypes;
import app.bsro.model.maintenance.PeriodicMaintenanceMilestones;
import app.bsro.model.maintenance.ScheduledMaintenances;

import com.mastercareusa.selector.GenericVehicle;

public class ScheduledMaintenanceDataBean implements Serializable {
	private static final long serialVersionUID = -6604175608811200335L;
	private static final String SERVICE_TYPE_ALL = "ALL";
	private static final String SERVICE_TYPE_NORMAL = "NORMAL";
	
	GenericVehicle vehicle = null;
	
	List<String> maintenanceCategories = null;
	List<String> serviceTypes = null;
	
	ScheduledMaintenances requiredMaintenance = null;
	PeriodicMaintenanceMilestones periodicMaintenance = null;
	ScheduledMaintenances serviceCheckMaintenance = null;
	
	Map<String, MaintenanceMilestones> maintenanceMilestonesByService = null;
	Map<String, Map<Long, List<OperationTypes>>> maintenanceMilestoneServicesByServiceTypeAndMileage = null;
	Map<String, List<Long>> mileageIntervalsByService = null;
	Map<Long, List<String>> servicesByMileageInterval = null;
	List<Long> allMileageIntervals = null;

	String currentServiceType = null;

	Long previousPreviousMileageInterval = null;
	Long previousMileageInterval = null;
	Long currentMileageInterval = null;
	Long nextMileageInterval = null;
	Long nextNextMileageInterval = null;
	
	public GenericVehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(GenericVehicle vehicle) {
		this.vehicle = vehicle;
	}
	public List<String> getMaintenanceCategories() {
		return maintenanceCategories;
	}
	public void setMaintenanceCategories(List<String> maintenanceCategories) {
		this.maintenanceCategories = maintenanceCategories;
	}
	public List<String> getServiceTypes() {
		return serviceTypes;
	}
	public void setServiceTypes(List<String> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
	public ScheduledMaintenances getRequiredMaintenance() {
		return requiredMaintenance;
	}
	public void setRequiredMaintenance(ScheduledMaintenances requiredMaintenance) {
		this.requiredMaintenance = requiredMaintenance;
	}
	public PeriodicMaintenanceMilestones getPeriodicMaintenance() {
		return periodicMaintenance;
	}
	public void setPeriodicMaintenance(
			PeriodicMaintenanceMilestones periodicMaintenance) {
		this.periodicMaintenance = periodicMaintenance;
	}
	public ScheduledMaintenances getServiceCheckMaintenance() {
		return serviceCheckMaintenance;
	}
	public void setServiceCheckMaintenance(
			ScheduledMaintenances serviceCheckMaintenance) {
		this.serviceCheckMaintenance = serviceCheckMaintenance;
	}
	public Map<String, MaintenanceMilestones> getMaintenanceMilestonesByService() {
		return maintenanceMilestonesByService;
	}
	public void setMaintenanceMilestonesByService(
			Map<String, MaintenanceMilestones> maintenanceMilestonesByService) {
		this.maintenanceMilestonesByService = maintenanceMilestonesByService;
	}
	public Map<String, Map<Long, List<OperationTypes>>> getMaintenanceMilestoneServicesByServiceTypeAndMileage() {
		return maintenanceMilestoneServicesByServiceTypeAndMileage;
	}
	public void setMaintenanceMilestoneServicesByServiceTypeAndMileage(
			Map<String, Map<Long, List<OperationTypes>>> maintenanceMilestoneServicesByServiceTypeAndMileage) {
		this.maintenanceMilestoneServicesByServiceTypeAndMileage = maintenanceMilestoneServicesByServiceTypeAndMileage;
	}
	public Map<String, List<Long>> getMileageIntervalsByService() {
		return mileageIntervalsByService;
	}
	public void setMileageIntervalsByService(
			Map<String, List<Long>> mileageIntervalsByService) {
		this.mileageIntervalsByService = mileageIntervalsByService;
	}
	public Map<Long, List<String>> getServicesByMileageInterval() {
		return servicesByMileageInterval;
	}
	public void setServicesByMileageInterval(
			Map<Long, List<String>> servicesByMileageInterval) {
		this.servicesByMileageInterval = servicesByMileageInterval;
	}
	public List<Long> getAllMileageIntervals() {
		return allMileageIntervals;
	}
	public void setAllMileageIntervals(List<Long> allMileageIntervals) {
		this.allMileageIntervals = allMileageIntervals;
	}
	public String getCurrentServiceType() {
		return currentServiceType;
	}
	public void setCurrentServiceType(String currentServiceType) {
		this.currentServiceType = currentServiceType;
	}
	public Long getPreviousPreviousMileageInterval() {
		return previousPreviousMileageInterval;
	}
	public void setPreviousPreviousMileageInterval(
			Long previousPreviousMileageInterval) {
		this.previousPreviousMileageInterval = previousPreviousMileageInterval;
	}
	public Long getPreviousMileageInterval() {
		return previousMileageInterval;
	}
	public void setPreviousMileageInterval(Long previousMileageInterval) {
		this.previousMileageInterval = previousMileageInterval;
	}
	public Long getCurrentMileageInterval() {
		return currentMileageInterval;
	}
	public void setCurrentMileageInterval(Long currentMileageInterval) {
		this.currentMileageInterval = currentMileageInterval;
	}
	public Long getNextMileageInterval() {
		return nextMileageInterval;
	}
	public void setNextMileageInterval(Long nextMileageInterval) {
		this.nextMileageInterval = nextMileageInterval;
	}
	public Long getNextNextMileageInterval() {
		return nextNextMileageInterval;
	}
	public void setNextNextMileageInterval(Long nextNextMileageInterval) {
		this.nextNextMileageInterval = nextNextMileageInterval;
	}
	
	public List<Long> getMileageIntervalsForCurrentService() {
		if (mileageIntervalsByService != null && currentServiceType != null) {
			return mileageIntervalsByService.get(currentServiceType);
		} else {
			return null;
		}
	}
	
	/**
	 * @TODO: This doesn't work yet
	 * @param selectedMileageInterval
	 * @param selectedServiceType
	 */
	public void setNewCurrentMileageAndServiceType(Long selectedMileageInterval, String selectedServiceType) {
		setPreviousPreviousMileageInterval(null);
		setPreviousMileageInterval(null);
		setCurrentMileageInterval(null);
		setNextMileageInterval(null);
		setNextNextMileageInterval(null);
		setCurrentServiceType(null);
		
		// If nothing is specified, look for one of two most "general" types and favor those
		if ((selectedServiceType == null || selectedServiceType.isEmpty()) && serviceTypes != null && !serviceTypes.isEmpty()) {
			for (String serviceType : serviceTypes) {
				// NORMAL is more common, so we'll favor that type
				if (serviceType.equals(SERVICE_TYPE_NORMAL)) {
					selectedServiceType = serviceType;
					break;
				} else if (serviceType.equals(SERVICE_TYPE_ALL)) {
					selectedServiceType = serviceType;
					break;
				}
			}
		}
		
		if (serviceTypes != null && !serviceTypes.isEmpty() && allMileageIntervals != null && !allMileageIntervals.isEmpty()) {
			if (selectedMileageInterval == null) {				
				if (selectedServiceType == null) {
					// If there is no selectedMileageInterval, and no selectedServiceType npick the first mileage interval of the first service type
					setCurrentServiceType(serviceTypes.get(0));
				} else {
					// If there is no selectedMileageInterval, but there is a selectedServiceType, pick the first mileage interval of the selected service type
					if (mileageIntervalsByService.containsKey(selectedServiceType)) {
						// ...but only if it's a valid choice
						setCurrentServiceType(selectedServiceType);
					} else {
						// ...otherwise, pick the first one again
						setCurrentServiceType(serviceTypes.get(0));
					}
				}
				
				if (mileageIntervalsByService.containsKey(getCurrentServiceType()) && mileageIntervalsByService.get(getCurrentServiceType()) != null && !mileageIntervalsByService.get(getCurrentServiceType()).isEmpty()) {
					currentMileageInterval =  mileageIntervalsByService.get(getCurrentServiceType()).get(0);
				}
			} else {			
				if (selectedServiceType == null || !mileageIntervalsByService.containsKey(selectedServiceType)) {
					for (int i = 0; i < allMileageIntervals.size(); i++) {
						currentMileageInterval = allMileageIntervals.get(i);
						if (selectedMileageInterval.equals(currentMileageInterval)) {
							break;
						} else if (selectedMileageInterval > currentMileageInterval || i == 0) {
							if (i == 0 && selectedMileageInterval < currentMileageInterval) {
								currentMileageInterval = allMileageIntervals.get(0);
								break;
							}
							if (i < allMileageIntervals.size() - 1 && selectedMileageInterval < allMileageIntervals.get(i+1)) {
								currentMileageInterval = allMileageIntervals.get(i+1);
								break;
							}
						}
					}
																	
					// get the first service type that has that mileage interval
					if (servicesByMileageInterval.containsKey(currentMileageInterval) && servicesByMileageInterval.get(currentMileageInterval) != null && !servicesByMileageInterval.get(currentMileageInterval).isEmpty()) {
						setCurrentServiceType(servicesByMileageInterval.get(currentMileageInterval).get(0));
					}
				} else {
					List<Long> selectedMileageIntervals = mileageIntervalsByService.get(selectedServiceType);
					for (int i = 0; i < selectedMileageIntervals.size(); i++) {
						currentMileageInterval = selectedMileageIntervals.get(i);
						if (selectedMileageInterval.equals(currentMileageInterval)) {
							break;
						} else if (selectedMileageInterval > currentMileageInterval || i == 0) {
							if (i == 0 && selectedMileageInterval < currentMileageInterval) {
								currentMileageInterval = selectedMileageIntervals.get(0);
								break;
							}
							if (i < selectedMileageIntervals.size() - 1 && selectedMileageInterval < selectedMileageIntervals.get(i+1)) {
								currentMileageInterval = selectedMileageIntervals.get(i+1);
								break;
							}
						}
					}
					setCurrentServiceType(selectedServiceType);
				}
			}
			
			List<Long> mileageIntervalsForCurrentService = getMileageIntervalsForCurrentService();
			
			for (int i = 0; i < mileageIntervalsForCurrentService.size(); i++) {
				if (currentMileageInterval.equals(mileageIntervalsForCurrentService.get(i))) {
					if (i == 0) {
						if (mileageIntervalsForCurrentService.size() > 1) {
							setNextMileageInterval(mileageIntervalsForCurrentService.get(1));
							if (mileageIntervalsForCurrentService.size() > 2) {
								setNextNextMileageInterval(mileageIntervalsForCurrentService.get(2));
							}
						}
					} else if (i == mileageIntervalsForCurrentService.size() - 1) {
						if (mileageIntervalsForCurrentService.size() > 1) {
							setPreviousMileageInterval(mileageIntervalsForCurrentService.get(i-1));
							 if (mileageIntervalsForCurrentService.size() > 2) {
								 setPreviousPreviousMileageInterval(mileageIntervalsForCurrentService.get(i-2));
								}
						}
					} else {
						setPreviousMileageInterval(mileageIntervalsForCurrentService.get(i-1));
						if (mileageIntervalsForCurrentService.size() > 2) {
							setNextMileageInterval(mileageIntervalsForCurrentService.get(i+1));
						}
					}
				}
			}
		}
	}
}
