package com.bsro.springframework.security;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.WebUtils;

import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.pojo.store.Store;
import com.bsro.constants.SessionConstants;
import com.bsro.errors.Errors;
import com.bsro.service.security.cookie.WebSiteUserCookieService;
import com.bsro.service.store.StoreService;
import com.bsro.service.user.WebSiteUserService;
import com.bsro.service.vehicle.WebSiteUserVehicleService;

public class BSROAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private final Log logger = LogFactory.getLog(BSROAuthenticationSuccessHandler.class);

	@Autowired
	private WebSiteUserService webSiteUserService;

	@Autowired
	private WebSiteUserVehicleService webSiteUserVehicleService;

	@Autowired
	private StoreService storeService;
	
	@Autowired
	private WebSiteUserCookieService webSiteUserCookieService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

		if (logger.isInfoEnabled())
			logger.info("Executing BSROAuthenticationSuccessHandler...");
		// writing a cookie to store a user name
		// Cookie loggedInUserCookie = new Cookie("fcacUserId",
		// authentication.getName());
		// loggedInUserCookie.setSecure(true);
		// loggedInUserCookie.setMaxAge(expiry)
		// response.addCookie(loggedInUserCookie);

		WebSiteUser user = null;
		// we aren't concerned with what is in the session anymore, let's just
		// replace it
		// if the user has actually logged in.
		// WebSiteUser user = (WebSiteUser)WebUtils.getSessionAttribute(request,
		// SessionConstants.WEBSITEUSER);

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			  String userName = ((UserDetails)principal).getUsername();
			  if(logger.isInfoEnabled()) logger.info("Found a user authentication for " + userName);
		
			 Errors errors = new Errors();
			  user = webSiteUserService.getUser(userName, errors);
			  logErrors("An error occured while obtaining user after successful login",errors);
			  
			  WebUtils.setSessionAttribute(request, SessionConstants.WEBSITEUSER, user);
			  if(logger.isInfoEnabled()) logger.info("Obtained user info and set on session");

			  errors = new Errors();
			  List<UserVehicle> vehicles = 
					  webSiteUserVehicleService.fetchWebSiteUserVehiclesByWebSiteUser(user.getWebSiteUserId(), errors);
			  logErrors("Error occurred obtaining user vehicles", errors);
			  
				if (user.getWebSiteUserVehicleId() == null) {
					if (vehicles != null && vehicles.size() > 0) {
						user.setWebSiteUserVehicleId(Long.valueOf(vehicles.get(0).getId()));
					}
				}
				
			  //update last login date
			  errors = new Errors();
			  user.setLastLoginDate(new Date());
			  user = webSiteUserService.updateUser(user, errors);
			  logErrors("An error occurred while updating the user after successful login",errors);
			  
			WebUtils.setSessionAttribute(request, SessionConstants.WEBSITEUSERVEHICLES, vehicles);
			if(user.getStoreNumber()!=null){
				Store store = storeService.findStoreLightById(user.getStoreNumber());
				WebUtils.setSessionAttribute(request, SessionConstants.PREFERRED_STORE, store);
			}
			if (logger.isInfoEnabled())
				logger.info("Obtained user vehicles and put on session");

			// set up cookies.
			response.addCookie(webSiteUserCookieService.createUserCookie(user));
}
		
		if(logger.isInfoEnabled()) logger.info("...Done Executing BSROAuthenticationSuccessHandler");
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

	private void logErrors(String sourceMessage, Errors errors) {
		logger.error(sourceMessage);
		if (errors.hasErrors()) {
			for (String message : errors.getGlobalErrors()) {
				logger.error(message);
			}
		}
	}

}
