package com.bsro.controller.vehicle.tire;

import static com.bsro.ajax.AjaxResponse.STATUS_OK;
import static com.bsro.ajax.AjaxResponse.STATUS_UNEXPECTED_ERROR;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsro.ajax.AjaxResponse;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.service.vehicle.tire.TireVehicleService;

@Controller
public class TireVehicleWidgetController {
	private static final Log logger = LogFactory.getLog(TireVehicleWidgetController.class);
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	}
	
	@RequestMapping(value = "/ajax/vehicle/tire/options-or-vehicle-data/year-make-model-vehicle.htm", method = RequestMethod.GET)
	public String getNextAvailableItems(@RequestParam(value="year", required=false) String year, @RequestParam(value="make", required=false)String makeName, @RequestParam(value="model", required=false) String modelName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			if (StringUtils.isBlank(year)) {
				return getYearOptions(model);
			} else if (StringUtils.isBlank(makeName)) {
				return getMakeOptionsByYear(year, model);
			} else if (StringUtils.isBlank(modelName)) {
				return getModelOptionsByYearAndMakeName(year, makeName, model);
			} else {
				return getVehiclesByYearAndMakeNameAndModelName(year, makeName, modelName, model);
			}
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle data", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	@RequestMapping(value = "/ajax/vehicle/tire/options/year-make-model-submodel/years.htm", method = RequestMethod.GET)
	public String getYearOptions(Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			Map<String, String> options = tireVehicleService.getYearOptions();
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle years", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	@RequestMapping(value = "/ajax/vehicle/tire/options/year-make-model-submodel/makes.htm", method = RequestMethod.GET)
	public String getMakeOptionsByYear(@RequestParam("year") String year, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {
			Map<String, String> options = tireVehicleService.getMakeOptionsByYear(year);
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle makes", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	@RequestMapping(value = "/ajax/vehicle/tire/options/year-make-model-submodel/models.htm", method = RequestMethod.GET)
	public String getModelOptionsByYearAndMakeName(@RequestParam("year") String year, @RequestParam("make") String makeName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			Map<String, String> options = tireVehicleService.getModelOptionsByYearAndMakeName(year, makeName);
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle models", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	@RequestMapping(value = "/ajax/vehicle/tire/options/year-make-model-submodel/submodels.htm", method = RequestMethod.GET)
	public String getSubModelOptionsByYearAndMakeNameAndModelName(@RequestParam("year") String year, @RequestParam("make")String makeName, @RequestParam("model") String modelName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {
			Map<String, String> options = tireVehicleService.getSubModelOptionsByYearAndMakeNameAndModelName(year, makeName, modelName);
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle submodels", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	@RequestMapping(value = "/ajax/vehicle/tire/year-make-model-submodel/vehicles.htm", method = RequestMethod.GET)
	public String getVehiclesByYearAndMakeNameAndModelName(@RequestParam("year") String year, @RequestParam("make")String makeName, @RequestParam("model") String modelName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {
			List<TireVehicle> vehicles = tireVehicleService.getVehiclesByYearAndMakeNameAndModelName(year, makeName, modelName);
			
			response.setStatus(STATUS_OK);
			response.setData(vehicles);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicles", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}

	@RequestMapping(value = "/ajax/vehicle/tire/options-or-vehicle-data/make-model-submodel-vehicle.htm", method = RequestMethod.GET)
	public String getNextAvailableItemsMakeFirst(@RequestParam(value="make", required=false)String makeName, @RequestParam(value="model", required=false) String modelName,  @RequestParam(value="submodel", required=false) String submodelName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			if (StringUtils.isBlank(makeName)) {
				return getMakeOptions(model);
			} else if (StringUtils.isBlank(modelName)) {
				return getModelOptionsByMake(makeName, model);
			} else if (StringUtils.isBlank(submodelName)) {
				return getSubmodelOptionsByMakeAndModel(makeName, modelName, model);
			} else {
				return getVehiclesByMakeNameAndModelNameAndSubmodelName(makeName, modelName, submodelName, model);
			}
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle data", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	private String getMakeOptions(Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			Map<String, String> options = tireVehicleService.getMakeOptions();
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle makes", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	public String getModelOptionsByMake(@RequestParam("make") String makeName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {
			Map<String, String> options = tireVehicleService.getModelOptionsByMakeName(makeName);
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle models", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}

	public String getSubmodelOptionsByMakeAndModel(@RequestParam("make") String makeName, @RequestParam("model") String modelName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			Map<String, String> options = tireVehicleService.getSubModelOptionsByMakeNameAndModelName(makeName, modelName);
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicle submodels", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}

	public String getVehiclesByMakeNameAndModelNameAndSubmodelName(@RequestParam("make")String makeName, @RequestParam("model") String modelName, @RequestParam("submodel") String submodelName, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {
			List<TireVehicle> vehicles = tireVehicleService.getVehiclesByMakeNameModelNameSubmodelName(makeName, modelName, submodelName);
			
			response.setStatus(STATUS_OK);
			response.setData(vehicles);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving vehicles", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your vehicle information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}	
}
