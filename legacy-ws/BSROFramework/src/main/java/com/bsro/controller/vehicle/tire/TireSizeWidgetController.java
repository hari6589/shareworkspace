package com.bsro.controller.vehicle.tire;

import static com.bsro.ajax.AjaxResponse.STATUS_OK;
import static com.bsro.ajax.AjaxResponse.STATUS_UNEXPECTED_ERROR;

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
import com.bsro.service.tire.TireSizeService;

@Controller
public class TireSizeWidgetController {
	private static final Log logger = LogFactory.getLog(TireVehicleWidgetController.class);
	
	@Autowired
	private TireSizeService tireSizeService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	}
	
	@RequestMapping(value = "/ajax/tire-size/options/cross-aspect-rim.htm", method = RequestMethod.GET)
	public String getNextAvailableItems(@RequestParam(value="cross", required=false) String cross, @RequestParam(value="aspect", required=false)String aspect, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			if (StringUtils.isBlank(cross)) {
				return getCrossOptions(model);
			} else if (StringUtils.isBlank(aspect)) {
				return getAspectOptionsByCross(cross, model);
			} else {
				return this.getRimOptionsByCrossAndAspect(cross, aspect, model);
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
	
	@RequestMapping(value = "/ajax/tire-size/options/cross-aspect-rim/cross.htm", method = RequestMethod.GET)
	public String getCrossOptions(Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			Map<String, String> options = tireSizeService.getCrossSectionOptions();
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving tire size cross sections", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your tire size information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	@RequestMapping(value = "/ajax/tire-size/options/cross-aspect-rim/aspects.htm", method = RequestMethod.GET)
	public String getAspectOptionsByCross(@RequestParam("cross") String crossSection, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {
			Map<String, String> options = tireSizeService.getAspectOptionsByCrossSection(crossSection);
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving tire size aspects", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your tire size information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
	
	@RequestMapping(value = "/ajax/tire-size/options/cross-aspect-rim/rims.htm", method = RequestMethod.GET)
	public String getRimOptionsByCrossAndAspect(@RequestParam("cross") String crossSection, @RequestParam("aspect") String aspect, Model model) throws Exception {
		AjaxResponse response = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		
		String jsonData = null;
		
		try {			
			Map<String, String> options = tireSizeService.getRimOptionsByCrossSectionAndAspect(crossSection, aspect);
			
			response.setStatus(STATUS_OK);
			response.setData(options);
			
			jsonData = JSONMapper.writeValueAsString(response);
			
		} catch (Throwable throwable) {
			logger.error("Problem retrieving tire size rims", throwable);
			response.setStatus(STATUS_UNEXPECTED_ERROR);
			response.getErrors().addGlobalError("We are sorry, an error occured while trying to retrieve your tire size information.\nPlease try again.");
			jsonData = JSONMapper.writeValueAsString(response);
		}
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}
}
