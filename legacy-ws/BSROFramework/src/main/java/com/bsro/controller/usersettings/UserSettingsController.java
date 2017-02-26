package com.bsro.controller.usersettings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import map.States;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.framework.util.ControllerUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.store.Store;
import com.bsro.constants.CookieConstants;
import com.bsro.constants.SessionConstants;
import com.bsro.errors.Errors;
import com.bsro.service.user.WebSiteUserService;

@Controller
public class UserSettingsController {
	
	private final Log logger = LogFactory.getLog(UserSettingsController.class);

	private final String ATTRIBUTE_ERROR = "errors";
	@Autowired
	private States statesMap;

	@Autowired
	WebSiteUserService webSiteUserService;

	private static SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	//@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(datetimeFormat, true));
	}

	//@ModelAttribute("statesMap")
	public Map<String, String> getSiteNames() {
		return statesMap;
	}
	
//	@ModelAttribute("isUserAllowed")
//	public Boolean checkAdmin(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws ModelAndViewDefiningException {
//		
//		logger.debug("check the user");
//
//		Boolean isAllowed = false;
//
//		Errors errors = new Errors();
//		WebSiteUser wsu = (WebSiteUser)session.getAttribute(SessionConstants.WEBSITEUSER);
//		try{
//			if(wsu!=null && wsu.getUserTypeId()==WebSiteUser.LOGGED_IN_USER_TYPE_ID)
//			{
//				if(webSiteUserService.authenticateUser(wsu.getEmailAddress(), wsu.getPassword(), errors)!=null){
//					isAllowed = true;
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			handleErrors(errors, null);
//			isAllowed =false;
//		}
//		if (!isAllowed) {
//			logger.debug("user not allowed");
//			throw new ModelAndViewDefiningException(new ModelAndView(new RedirectView("/index.jsp")));
//		}
//		return isAllowed;
//	}

	//@ModelAttribute(SessionConstants.WEBSITEUSER)
	public WebSiteUser getFormBackingObjectWebSiteUser(HttpSession session, Model model) throws ModelAndViewDefiningException {


		WebSiteUser wsu = (WebSiteUser)session.getAttribute(SessionConstants.WEBSITEUSER);
		return wsu;
	}

	//@RequestMapping(value = "/user/secured/index.htm", method = RequestMethod.GET)
	public String loadUserPage(HttpServletRequest request, HttpSession session, Model model) {
		

		Store preferredStore = (Store)session.getAttribute(SessionConstants.PREFERRED_STORE);
		model.addAttribute(SessionConstants.PREFERRED_STORE,preferredStore);
		return "/user/secured/index";
	}

	//@RequestMapping(value = "/user/secured/profile.htm", method = RequestMethod.GET)
	public String userSettingsGet(Model model, HttpSession session) {

		WebSiteUser webSiteUser = (WebSiteUser)session.getAttribute(SessionConstants.WEBSITEUSER);
		if(webSiteUser!=null&&webSiteUser.getUserTypeId()==WebSiteUser.LOGGED_IN_USER_TYPE_ID)
			return "user/secured/profile";
		else
			return "redirect:/index";
	}
	
	//@RequestMapping(value = "/user/secured/update-user-profile.htm", method = RequestMethod.POST)
	public String updateUser(HttpServletResponse response, HttpServletRequest request, HttpSession session,Model model, @ModelAttribute("webSiteUser") WebSiteUser webSiteUser, BindingResult result) {
		
		Errors errors = new Errors();
		try {
			WebSiteUser wsu = (WebSiteUser)session.getAttribute(SessionConstants.WEBSITEUSER);
			
			WebSiteUser dbWebSiteUser = webSiteUserService.getUser(wsu.getEmailAddress(),errors);
			if (dbWebSiteUser == null) {
				Util.debug("User is null, redirecting");
				ControllerUtils.setErrors(model, errors);
				return "redirect:/index";
			}

			Util.debug("User is not null, setting attr's");
			dbWebSiteUser.setFirstName(webSiteUser.getFirstName());
			dbWebSiteUser.setLastName(webSiteUser.getLastName());
			dbWebSiteUser.setEmailAddress(webSiteUser.getEmailAddress());
			dbWebSiteUser.setDayTimePhone(webSiteUser.getDayTimePhone());
			dbWebSiteUser.setMobilePhone(webSiteUser.getMobilePhone());
			dbWebSiteUser.setEveningPhone(webSiteUser.getEveningPhone());
			dbWebSiteUser.setCity(webSiteUser.getCity());
			dbWebSiteUser.setState(webSiteUser.getState());
			dbWebSiteUser.setZip(webSiteUser.getZip());
			dbWebSiteUser.setAddress(webSiteUser.getAddress());
			dbWebSiteUser.setEveningPhone(webSiteUser.getEveningPhone());
			
			dbWebSiteUser =	webSiteUserService.updateUser(dbWebSiteUser, errors);
			if(dbWebSiteUser==null){
				Util.debug("The user is null");
			}else{
				setSessionAndCookie(response,session, dbWebSiteUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.debug("Error updating user");
		}

		ControllerUtils.setErrors(model, errors);
		return "redirect:/user/secured/index.htm";
	}
	private void setSessionAndCookie(HttpServletResponse response, HttpSession session,WebSiteUser webSiteUser) {
		session.setAttribute(SessionConstants.WEBSITEUSER, webSiteUser);
		Cookie cookie = new Cookie(CookieConstants.WEBSITEUSERID, webSiteUser.getWebSiteUserId().toString());
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

}
