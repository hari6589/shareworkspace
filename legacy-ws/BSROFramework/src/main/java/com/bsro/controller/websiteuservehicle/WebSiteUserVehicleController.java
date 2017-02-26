package com.bsro.controller.websiteuservehicle;

import static com.bsro.ajax.AjaxResponse.STATUS_OK;
import static com.bsro.ajax.AjaxResponse.STATUS_UNEXPECTED_ERROR;
import static com.bsro.ajax.AjaxResponse.STATUS_VALIDATION_FAILURE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.util.WebUtils;

import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bsro.ajax.AjaxResponse;
import com.bsro.constants.SessionConstants;
import com.bsro.errors.Errors;
import com.bsro.service.user.WebSiteUserService;
import com.bsro.service.vehicle.WebSiteUserVehicleService;

@Controller
public class WebSiteUserVehicleController {

	private final Log logger = LogFactory.getLog(WebSiteUserVehicleController.class);

	@Autowired
	WebSiteUserVehicleService webSiteUserVehicleService;

	@Autowired
	WebSiteUserService webSiteUserService;

	@Resource(name = "environmentPropertiesMap")
	private Map<String, String> environmentProperties;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	}

	@PostConstruct
	public void initializeController() {

	}

	@RequestMapping(value = "/ajax/my-firestone/create-vehicle/tire.htm", method = RequestMethod.POST)
	public String createWebSiteUserVehicleTire(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("zip") String zip, @RequestParam("year") String vehicleYear, @RequestParam("make") String vehicleMake, @RequestParam("model") String vehicleModel, @RequestParam("submodel") String vehicleSubmodel) throws Exception {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ObjectMapper JSONMapper = new ObjectMapper();
		String jsonData = null;
		
		if(StringUtils.isEmpty(zip)){
		    ajaxResponse.getErrors().addFieldError("zip", "Please enter a zip code.");
		} else if(!com.bfrc.framework.util.Util.isValidZipCode(zip)){
			ajaxResponse.getErrors().addFieldError("zip", "Please enter a valid zip code.");
		}
		
		if (StringUtils.isEmpty(vehicleYear)) {
			ajaxResponse.getErrors().addFieldError("year", "Please select a year.");
		}
		
		if (StringUtils.isEmpty(vehicleMake)) {
			ajaxResponse.getErrors().addFieldError("make", "Please select a make.");
		}
		
		if (StringUtils.isEmpty(vehicleModel)) {
			ajaxResponse.getErrors().addFieldError("model", "Please select a model.");
		}
		
		if (StringUtils.isEmpty(vehicleSubmodel)) {
			ajaxResponse.getErrors().addFieldError("submodel", "Please select a submodel.");
		}
		
		if (!ajaxResponse.getErrors().hasErrors()) {
			Errors errors = new Errors();			
			try {
				WebSiteUser webSiteUser = (WebSiteUser) session.getAttribute(SessionConstants.WEBSITEUSER);
				UserVehicle userVehicle = webSiteUserVehicleService.createWebSiteUserVehicleFromTireData(webSiteUser.getWebSiteUserId(), vehicleYear, vehicleMake, vehicleModel, vehicleSubmodel, errors);
				List<UserVehicle> webSiteUserVehicles = (List<UserVehicle>) WebUtils.getOrCreateSessionAttribute(session, SessionConstants.WEBSITEUSERVEHICLES, ArrayList.class);
				
				webSiteUserVehicles.add(userVehicle);
				setWebSiteUserVehiclesOnSession(session, webSiteUserVehicles);
				webSiteUser.setWebSiteUserVehicleId(userVehicle.getId());				
				webSiteUser.setZip(zip);
				webSiteUser = webSiteUserService.updateUser(webSiteUser, errors);
				
				session.setAttribute(SessionConstants.WEBSITEUSER, webSiteUser);
				session.setAttribute(SessionConstants.WEBSITEUSERSELECTEDVEHICLE, userVehicle);
				
				ajaxResponse.setStatus(STATUS_OK);
			} catch (Throwable throwable) {
				logger.error("Error creating web site user vehicle", throwable);
				ajaxResponse.setStatus(STATUS_UNEXPECTED_ERROR);
				ajaxResponse.getErrors().addGlobalError("We are sorry, an error occured while trying to save your vehicle information.\nPlease try again.");
			}
		} else {
		    ajaxResponse.setStatus(STATUS_VALIDATION_FAILURE);
		}
		
		jsonData = JSONMapper.writeValueAsString(ajaxResponse);
		
		model.addAttribute("jsonData", jsonData);
		
		return "common/jsonData";
	}	

	private void setWebSiteUserVehiclesOnSession(HttpSession session, List<UserVehicle> webSiteUserVehicles) {
		session.setAttribute(SessionConstants.WEBSITEUSERVEHICLES, webSiteUserVehicles);
	}
}
