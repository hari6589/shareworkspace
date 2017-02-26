package com.bsro.controller.maintenance;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.bsro.model.maintenance.OperationTypes;

import com.bfrc.UserSessionData;
import com.bfrc.framework.util.UserProfileUtils;
import com.bsro.pojo.maintenance.ScheduledMaintenanceDataBean;
import com.bsro.service.maintenance.ScheduledMaintenanceService;
import com.mastercareusa.selector.GenericSelectorData;
import com.mastercareusa.selector.GenericVehicle;

@Controller
public class ScheduledMaintenanceBaseController {
	@Autowired
	private GenericSelectorData selectorData; 

	@Autowired
	private ScheduledMaintenanceService scheduledMaintenanceService;
		
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	}
	
	@RequestMapping(value = "/vehicle-scheduled-maintenance/{vehicleIdString}/listServices.htm", method = RequestMethod.GET)
	public String loadAllVehicleScheduledMaintenanceData(HttpServletRequest request, HttpSession session, @PathVariable String vehicleIdString, Model model, @RequestParam(value="showAll", required=false) String showAll) throws IOException {
		return loadAllVehicleScheduledMaintenanceData(request, session, vehicleIdString, null, null, model, showAll);
	}
	
	@RequestMapping(value = "/vehicle-scheduled-maintenance/{vehicleIdString}/{vehicleDescriptionString}/listServices.htm", method = RequestMethod.GET)
	public String loadAllVehicleScheduledMaintenanceData(HttpServletRequest request, HttpSession session, @PathVariable String vehicleIdString, @PathVariable String vehicleDescriptionString, Model model, @RequestParam(value="showAll", required=false) String showAll) throws IOException {
		return loadAllVehicleScheduledMaintenanceData(request, session, vehicleIdString, null, vehicleDescriptionString, model, showAll);
	}
		
	@RequestMapping(value = "/vehicle-scheduled-maintenance/{vehicleIdString}/{mileageString}/{vehicleDescriptionString}/listServices.htm", method = RequestMethod.GET)
	public String loadAllVehicleScheduledMaintenanceData(HttpServletRequest request, HttpSession session, @PathVariable String vehicleIdString, @PathVariable String mileageString, @PathVariable String vehicleDescriptionString, Model model, @RequestParam(value="showAll", required=false) String showAll) throws IOException {
		String year = null;
		String makeName = null;
		String modelName = null;
		String alphanumericSubmodelName = null;

		Long vehicleId = null;
		Long mileage = null;
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		
		if (vehicleDescriptionString != null) {
			String[] vehicleParams = vehicleDescriptionString.split("-");
			if (vehicleParams.length > 0) {
				year = vehicleParams[0];
				model.addAttribute("vehicleYear", year);
				userSessionData.setYear(year);
			}
			
			if(vehicleParams.length > 1) {
				makeName = vehicleParams[1];
				model.addAttribute("vehicleMake", makeName);
				userSessionData.setMake(makeName);
			}
			
			if(vehicleParams.length > 2){
				modelName = vehicleParams[2];
				model.addAttribute("vehicleModel", modelName);
				userSessionData.setModel(modelName);
			}

			if (vehicleParams.length > 3) {
				alphanumericSubmodelName = vehicleParams[3];
				model.addAttribute("vehicleSubmodel", alphanumericSubmodelName);
				userSessionData.setSubmodel(alphanumericSubmodelName);
			}
		}

		try {
			mileage = Long.valueOf(mileageString);
		} catch (Throwable myThrowable) {
			Exception myException = new Exception("Invalid URL, sent mileage as "+mileageString+" from URL "+request.getRequestURI()+" from referrer "+request.getHeader("referer")+". This is not a fatal error, mileage will just be ignored.", myThrowable);
			//myException.printStackTrace(System.out);
			mileage = null;
		}
		
		ScheduledMaintenanceDataBean scheduledMaintenanceDataBean = null;
				
		GenericVehicle vehicle = null;
		
		try {
			vehicleId = Long.valueOf(vehicleIdString);
			vehicle = retrieveVehicleData(session, vehicleId, alphanumericSubmodelName);
		} catch (Throwable myThrowable) {
			Exception myException = new Exception("Problem retrieving data for acesVehicleId "+vehicleIdString+", submodel "+alphanumericSubmodelName+" from URL "+request.getRequestURI()+" from referrer "+request.getHeader("referer"), myThrowable);
			//myException.printStackTrace(System.out);
			return "maintenance/vehicleNotFound";
		}
		
		
		try {
			scheduledMaintenanceDataBean = scheduledMaintenanceService.getAllMaintenanceData(vehicleId, vehicle, mileage, null);
		} catch (IOException myIOException) {
			// should already have been logged
			return "maintenance/servicesWebserviceNotAvailable";
		}

		model.addAttribute("maintenanceCategories", scheduledMaintenanceDataBean.getMaintenanceCategories());
		
		model.addAttribute("serviceTypes", scheduledMaintenanceDataBean.getServiceTypes());
		model.addAttribute("mileageIntervalsByService", scheduledMaintenanceDataBean.getMileageIntervalsByService());
		
		ObjectMapper JSONMapper = new ObjectMapper();
		String mileageIntervalsByServiceAsJSON = JSONMapper.writeValueAsString(scheduledMaintenanceDataBean.getMileageIntervalsByService());
		model.addAttribute("mileageIntervalsByServiceAsJSON", mileageIntervalsByServiceAsJSON);
		
		model.addAttribute("servicesByMileageInterval", scheduledMaintenanceDataBean.getServicesByMileageInterval());
		model.addAttribute("allMileageIntervals", scheduledMaintenanceDataBean.getAllMileageIntervals());
		
		model.addAttribute("requiredMaintenance", scheduledMaintenanceDataBean.getRequiredMaintenance());
		model.addAttribute("periodicMaintenance", scheduledMaintenanceDataBean.getPeriodicMaintenance());
		model.addAttribute("serviceCheckMaintenance", scheduledMaintenanceDataBean.getServiceCheckMaintenance());
		model.addAttribute("maintenanceMilestonesByService", scheduledMaintenanceDataBean.getMaintenanceMilestonesByService());
		
		Boolean hasService = false;
		
		if (scheduledMaintenanceDataBean.getRequiredMaintenance() != null || scheduledMaintenanceDataBean.getPeriodicMaintenance() != null || scheduledMaintenanceDataBean.getServiceCheckMaintenance() != null || scheduledMaintenanceDataBean.getMaintenanceMilestonesByService() != null) {
			hasService = true;
		}
		
		// These should probably all go in one bean...
		model.addAttribute("hasService", hasService);
		model.addAttribute("vehicle", scheduledMaintenanceDataBean.getVehicle());
		
		model.addAttribute("mileage", mileage);

		model.addAttribute("currentServiceType", scheduledMaintenanceDataBean.getCurrentServiceType());
		
		if (scheduledMaintenanceDataBean.getPreviousPreviousMileageInterval() != null) {
			model.addAttribute("previousPreviousMileageInterval", scheduledMaintenanceDataBean.getPreviousPreviousMileageInterval());
		}
		
		if (scheduledMaintenanceDataBean.getPreviousMileageInterval() != null) {
			model.addAttribute("previousMileageInterval", scheduledMaintenanceDataBean.getPreviousMileageInterval());
		}
		
		model.addAttribute("currentMileageInterval", scheduledMaintenanceDataBean.getCurrentMileageInterval());
		
		if (scheduledMaintenanceDataBean.getNextMileageInterval() != null) {
			model.addAttribute("nextMileageInterval", scheduledMaintenanceDataBean.getNextMileageInterval());
		}
		
		if (scheduledMaintenanceDataBean.getNextNextMileageInterval() != null) {
			model.addAttribute("nextNextMileageInterval", scheduledMaintenanceDataBean.getNextNextMileageInterval());
		}
		
		model.addAttribute("mileageIntervalsForCurrentService", scheduledMaintenanceDataBean.getMileageIntervalsForCurrentService());
		
		if (showAll == null) {
			return "maintenance/listServices";
		} else {
			return "maintenance/listAllServices";
		}
	}
	
	/**
	 * This takes the vehicleId as a string because if we make it a long an invalid value (like "xyz") will cause the mapping to fail (as in, page not found) and never even reach here.
	 * 
	 * @param session
	 * @param vehicleIdString
	 * @param serviceType
	 * @param mileageInterval
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/maintenance/{vehicleIdString}/milestoneData.htm")
	public String retrieveMilestoneDataByServiceAndMileageAsJSON(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String vehicleIdString, @RequestParam(value="serviceType") String serviceType, @RequestParam(value="mileageInterval") String mileageIntervalString, Model model) throws IOException {		
		Long mileageInterval = null;
		Long vehicleId = null;
		
		try {
			mileageInterval = Long.valueOf(mileageIntervalString);
		} catch (Throwable myThrowable) {
			Exception myException = new Exception("Invalid parameter, sent mileage as "+mileageIntervalString+" from URL "+request.getRequestURI()+" from referrer "+request.getHeader("referer")+". This is not a fatal error, mileage will just be ignored.", myThrowable);
			myException.printStackTrace(System.out);
			mileageInterval = null;
		}
		
		ScheduledMaintenanceDataBean scheduledMaintenanceDataBean = null;
		

		GenericVehicle vehicle = null;
		
		try {
			vehicleId = Long.valueOf(vehicleIdString);
			vehicle = retrieveVehicleData(session, vehicleId, null);
		} catch (Throwable myThrowable) {
			Exception myException = new Exception("Problem retrieving data for acesVehicleId "+vehicleIdString+" from URL "+request.getRequestURI()+" from referrer "+request.getHeader("referer"), myThrowable);
			myException.printStackTrace(System.out);
			return handleAjaxError(response);
		}
		
		
		try {
			scheduledMaintenanceDataBean = scheduledMaintenanceService.getAllMaintenanceData(vehicleId, vehicle, mileageInterval, null);
		} catch (IOException myIOException) {
			return handleAjaxError(response);
		}
		
		if (!scheduledMaintenanceDataBean.getMileageIntervalsByService().containsKey(serviceType)) {
			Exception myException = new Exception("Problem retrieving data for serviceType "+serviceType+" from URL "+request.getRequestURI()+"?"+request.getQueryString()+" from referrer "+request.getHeader("referer"));
			myException.printStackTrace(System.out);
			return handleAjaxError(response);
		}
			
		try {
			scheduledMaintenanceDataBean.setNewCurrentMileageAndServiceType(mileageInterval, serviceType);
			
			Map<String, Map<Long, List<OperationTypes>>> maintenanceMilestoneServicesByServiceTypeAndMileage = scheduledMaintenanceDataBean.getMaintenanceMilestoneServicesByServiceTypeAndMileage();
			Map<Long, List<OperationTypes>> milestoneServicesByMileage = maintenanceMilestoneServicesByServiceTypeAndMileage.get(serviceType);
			Map<Long, List<OperationTypes>> selectedMilestoneServicesByMileage = new LinkedHashMap<Long, List<OperationTypes>>();
			
			if (scheduledMaintenanceDataBean.getPreviousPreviousMileageInterval() != null) {
				selectedMilestoneServicesByMileage.put(scheduledMaintenanceDataBean.getPreviousPreviousMileageInterval(), milestoneServicesByMileage.get(scheduledMaintenanceDataBean.getPreviousPreviousMileageInterval()));
			}
			if (scheduledMaintenanceDataBean.getPreviousMileageInterval() != null) {
				selectedMilestoneServicesByMileage.put(scheduledMaintenanceDataBean.getPreviousMileageInterval(), milestoneServicesByMileage.get(scheduledMaintenanceDataBean.getPreviousMileageInterval()));
			}
			if (scheduledMaintenanceDataBean.getCurrentMileageInterval() != null) {
				selectedMilestoneServicesByMileage.put(scheduledMaintenanceDataBean.getCurrentMileageInterval(), milestoneServicesByMileage.get(scheduledMaintenanceDataBean.getCurrentMileageInterval()));
			}
			if (scheduledMaintenanceDataBean.getNextMileageInterval() != null) {
				selectedMilestoneServicesByMileage.put(scheduledMaintenanceDataBean.getNextMileageInterval(), milestoneServicesByMileage.get(scheduledMaintenanceDataBean.getNextMileageInterval()));
			}
			if (scheduledMaintenanceDataBean.getNextNextMileageInterval() != null) {
				selectedMilestoneServicesByMileage.put(scheduledMaintenanceDataBean.getNextNextMileageInterval(), milestoneServicesByMileage.get(scheduledMaintenanceDataBean.getNextNextMileageInterval()));
			}
	
			ObjectMapper JSONMapper = new ObjectMapper();
			String selectedMilestoneServicesByMileageAsJSON = JSONMapper.writeValueAsString(selectedMilestoneServicesByMileage);
			model.addAttribute("jsonData", selectedMilestoneServicesByMileageAsJSON);
		
		} catch (Throwable myThrowable) {
			Exception myException = new Exception("Unexpected error from URL "+request.getRequestURI()+"?"+request.getQueryString()+" from referrer "+request.getHeader("referer"));
			myException.printStackTrace(System.out);
			return handleAjaxError(response);
		}
		
		return "maintenance/jsonData";
	}
	
	private GenericVehicle retrieveVehicleData(HttpSession session, Long vehicleId, String alphanumericSubmodelName) throws Exception {
		GenericVehicle vehicle = selectorData.getVehicleById(vehicleId);
		
		// The URL contains a submodel with all non-alphanumeric characters removed. There can be multiple submodels for a given vehicle id.
		// Therefore, we get all of the submodels for this vehicle id, remove the non-alphanumeric characters, and compare them.
		// If one matches, that's the one we used.
		// This is used for display purposes only, so if it doesn't work for some case, it's not a big problem.
		List<String> submodelNameList = scheduledMaintenanceService.getSubmodelNamesByAcesVehicleId(vehicleId);
		if (alphanumericSubmodelName != null) {
			for (String submodelName : submodelNameList) {
				if (alphanumericSubmodelName.toLowerCase().equals(submodelName.toLowerCase().replaceAll("[^a-z0-9]", ""))) {
					vehicle.setSubmodel(submodelName);
					break;
				}
			}
		}
		
		cacheVehicleData(session, vehicle);
		
		return vehicle;
	}
	
	private void cacheVehicleData(HttpSession session, GenericVehicle vehicle) {
		session.setAttribute("cache.vehicle.acesVehicleId", Long.toString(vehicle.getAcesId()));
		session.setAttribute("cache.vehicle.year", Integer.toString(vehicle.getYear()));
		session.setAttribute("cache.vehicle.make", vehicle.getMake());
		session.setAttribute("cache.vehicle.model", vehicle.getModel());
		if (vehicle.getSubmodel() != null) {
			session.setAttribute("cache.vehicle.submodel", vehicle.getSubmodel());
		}
	}
	
	private String handleAjaxError(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setContentLength(0);
		return "maintenance/jsonData";
	}

}
