package com.bsro.service.maintenance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.maintenance.MaintenanceMilestones;
import app.bsro.model.maintenance.MileageTypes;
import app.bsro.model.maintenance.OperationTypes;
import app.bsro.model.maintenance.PeriodicMaintenanceMilestones;
import app.bsro.model.maintenance.ScheduledMaintenances;

import com.bfrc.framework.dao.VehicleDAO;
import com.bsro.pojo.maintenance.ScheduledMaintenanceDataBean;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.BSROWebserviceServiceImpl;
import com.mastercareusa.selector.GenericVehicle;

@Service
public class ScheduledMaintenanceServiceImpl extends BSROWebserviceServiceImpl implements ScheduledMaintenanceService {
	private final Log logger;
	
	private static final String MAINTENANCE_CATEGORIES = "categories";
	private static final String MAINTENANCE_MILEAGE_INTERVALS = "mileages";
	private static final String MAINTENANCE_SERVICE_TYPES = "types";
	
	private static final String MAINTENANCE_CATEGORY_REQUIRED = "required";
	private static final String MAINTENANCE_CATEGORY_SERVICE_CHECKS = "checks";
	private static final String MAINTENANCE_CATEGORY_PERIODIC = "periodic";
	private static final String MAINTENANCE_CATEGORY_MILESTONES = "milestones";
	
	private static final String PARAM_SERVICE_TYPE = "serviceType";
	
	private static final String PATH_WEBSERVICE_SCHEDULED_MAINTENANCE = "maintenance/scheduled";
	
	@Autowired
	private VehicleDAO vehicleDAO;
	
	public ScheduledMaintenanceServiceImpl() {
		this.logger = LogFactory.getLog(ScheduledMaintenanceServiceImpl.class);
	}

	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}

	public ScheduledMaintenanceDataBean getAllMaintenanceData(Long vehicleId, GenericVehicle vehicle, Long mileage, String alphanumericSubmodelName) throws IOException {
		ScheduledMaintenanceDataBean scheduledMaintenanceDataBean = new ScheduledMaintenanceDataBean();
		
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
		
		List<String> serviceTypesWithNoMaintenanceMilestones =  null;
		
		maintenanceCategories = getMaintenanceCategories(vehicleId);
		serviceTypes = getServiceTypes(vehicleId);
		
		if (maintenanceCategories != null) {
			for (String category : maintenanceCategories) {
				if (category.equals(MAINTENANCE_CATEGORY_REQUIRED)) {
					requiredMaintenance = getRequiredMaintenance(vehicleId);
				} else if (category.equals(MAINTENANCE_CATEGORY_SERVICE_CHECKS)) {
					serviceCheckMaintenance = getServiceCheckMaintenance(vehicleId);
				} else if (category.equals(MAINTENANCE_CATEGORY_PERIODIC)) {
					periodicMaintenance = getPeriodicMaintenance(vehicleId);
				} else if (category.equals(MAINTENANCE_CATEGORY_MILESTONES) && serviceTypes != null) {
					maintenanceMilestonesByService = new HashMap<String, MaintenanceMilestones>();
					mileageIntervalsByService = new HashMap<String, List<Long>>();
					servicesByMileageInterval = new HashMap<Long, List<String>>();
					maintenanceMilestoneServicesByServiceTypeAndMileage = new LinkedHashMap<String, Map<Long, List<OperationTypes>>>();
					
					for (String serviceType : serviceTypes) {
						MaintenanceMilestones maintenanceMilestone = getMaintenanceMilestone(vehicleId, serviceType);
						if (maintenanceMilestone != null) {
							maintenanceMilestonesByService.put(serviceType, maintenanceMilestone);
							
							List<Long> mileageIntervals = getMaintenanceMileageIntervals(vehicleId, serviceType);
							
							if (mileageIntervals != null) {
							mileageIntervalsByService.put(serviceType, mileageIntervals);
							
								for (Long mileageInterval : mileageIntervals) {
									if (!servicesByMileageInterval.containsKey(mileageInterval)) {
										servicesByMileageInterval.put(mileageInterval, new ArrayList<String>());
									}
									List<String> servicesForMileageInterval = servicesByMileageInterval.get(mileageInterval);
									servicesForMileageInterval.add(serviceType);
									Collections.sort(servicesForMileageInterval);
									servicesByMileageInterval.put(mileageInterval, servicesForMileageInterval);
								}
							}
							
							Map<Long, List<OperationTypes>> operationsByMileage = new LinkedHashMap<Long, List<OperationTypes>>();
							List<MileageTypes> mileageTypes = maintenanceMilestone.getData();
							for (MileageTypes mileageType : mileageTypes) {
								Long mileageInterval = Long.valueOf(mileageType.getMileage());
								operationsByMileage.put(mileageInterval, mileageType.getServices());
							}
							maintenanceMilestoneServicesByServiceTypeAndMileage.put(serviceType, operationsByMileage);
						} else {
							if (serviceTypesWithNoMaintenanceMilestones == null) {
								serviceTypesWithNoMaintenanceMilestones = new ArrayList<String>();
							}
							serviceTypesWithNoMaintenanceMilestones.add(serviceType);
						}
					}
					
					// clean up empty service types
					for (int i = serviceTypes.size() - 1; i >= 0; i--) {
						if (serviceTypesWithNoMaintenanceMilestones != null && serviceTypesWithNoMaintenanceMilestones.contains(serviceTypes.get(i))) {
							serviceTypes.remove(i);
						}
					}
					
					Set<Long> uniqueMileageIntervalSet = servicesByMileageInterval.keySet();
					allMileageIntervals = new ArrayList<Long>(uniqueMileageIntervalSet);
					Collections.sort(allMileageIntervals);
				}
			}
		}
		
		scheduledMaintenanceDataBean.setVehicle(vehicle);
		scheduledMaintenanceDataBean.setMaintenanceCategories(maintenanceCategories);
		scheduledMaintenanceDataBean.setServiceTypes(serviceTypes);
		scheduledMaintenanceDataBean.setRequiredMaintenance(requiredMaintenance);
		scheduledMaintenanceDataBean.setPeriodicMaintenance(periodicMaintenance);
		scheduledMaintenanceDataBean.setServiceCheckMaintenance(serviceCheckMaintenance);
		scheduledMaintenanceDataBean.setMaintenanceMilestonesByService(maintenanceMilestonesByService);
		scheduledMaintenanceDataBean.setMaintenanceMilestoneServicesByServiceTypeAndMileage(maintenanceMilestoneServicesByServiceTypeAndMileage);
		scheduledMaintenanceDataBean.setMileageIntervalsByService(mileageIntervalsByService);
		scheduledMaintenanceDataBean.setServicesByMileageInterval(servicesByMileageInterval);
		scheduledMaintenanceDataBean.setAllMileageIntervals(allMileageIntervals);
		
		scheduledMaintenanceDataBean.setNewCurrentMileageAndServiceType(mileage, null);
		
		return scheduledMaintenanceDataBean;
	}
	
	public ScheduledMaintenances getRequiredMaintenance(Long vehicleId) throws IOException {
		String webservicePath = assembleWebservicePath(MAINTENANCE_CATEGORY_REQUIRED, vehicleId);
		
		return getWebserviceWithTokenOnly(webservicePath, null, ScheduledMaintenances.class);
	}
	
	public ScheduledMaintenances getServiceCheckMaintenance(Long vehicleId) throws IOException {
		String webservicePath = assembleWebservicePath(MAINTENANCE_CATEGORY_SERVICE_CHECKS, vehicleId);
		
		return getWebserviceWithTokenOnly(webservicePath, null, ScheduledMaintenances.class);
	}

	public PeriodicMaintenanceMilestones getPeriodicMaintenance(Long vehicleId) throws IOException {
		String webservicePath = assembleWebservicePath(MAINTENANCE_CATEGORY_PERIODIC, vehicleId);
		
		return getWebserviceWithTokenOnly(webservicePath, null, PeriodicMaintenanceMilestones.class);
	}
	
	public MaintenanceMilestones getMaintenanceMilestone(Long vehicleId, String vehicleServiceType) throws IOException {
		String webservicePath = assembleWebservicePath(MAINTENANCE_CATEGORY_MILESTONES, vehicleId);
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_SERVICE_TYPE, vehicleServiceType);

		return getWebserviceWithTokenOnly(webservicePath, parameters, MaintenanceMilestones.class);
	}
	
	public List<String> getMaintenanceCategories(Long vehicleId) throws IOException {
		String webservicePath = assembleWebservicePath(MAINTENANCE_CATEGORIES, vehicleId);
		
		Map<String, Integer> categories = getWebserviceWithTokenOnly(webservicePath, null, new TypeReference<Map<String, Integer>>() {});

		if (categories != null ) {
			String[] myStringArray = new String[categories.size()];
		
			myStringArray = categories.keySet().toArray(myStringArray);
	
			return Arrays.asList(myStringArray);
		}
		return null;
	}
	
	public List<Long> getMaintenanceMileageIntervals(Long vehicleId, String vehicleServiceType) throws IOException {
		String webservicePath = assembleWebservicePath(MAINTENANCE_MILEAGE_INTERVALS, vehicleId);
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_SERVICE_TYPE, vehicleServiceType);

		return getWebserviceWithTokenOnly(webservicePath, parameters, new TypeReference<List<Long>>() {});
	}
	
	public List<String> getServiceTypes(Long vehicleId) throws IOException {
		String webservicePath = assembleWebservicePath(MAINTENANCE_SERVICE_TYPES, vehicleId);

		List<String> serviceTypes = getWebserviceWithTokenOnly(webservicePath, null, new TypeReference<List<String>>() {});
		
		if (serviceTypes != null) {
			Collections.sort(serviceTypes);
		}
			
		return serviceTypes;
	}
	
	public List<String> getSubmodelNamesByAcesVehicleId(Long acesVehicleId) {
		return vehicleDAO.getSubmodelNameListByAcesVehicleId(acesVehicleId);
	}
	
	/**
	 * vehicleId is actually part of each path, so we assemble it here
	 * 
	 * @param webservice
	 * @param vehicleId
	 * @return
	 */
	private String assembleWebservicePath(String webservice, Long vehicleId) {
		StringBuilder webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_SCHEDULED_MAINTENANCE).append(PATH_DELIMITER).append(webservice).append(PATH_DELIMITER).append(vehicleId);
		return webservicePath.toString();
	}
}
