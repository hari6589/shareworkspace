package com.bsro.filter.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.pojo.store.Store;
import com.bsro.constants.CookieConstants;
import com.bsro.constants.SessionConstants;
import com.bsro.errors.Errors;
import com.bsro.service.security.cookie.WebSiteUserCookieService;
import com.bsro.service.store.StoreService;
import com.bsro.service.user.WebSiteUserService;
import com.bsro.service.vehicle.WebSiteUserVehicleService;

public class AnonymousUserFilter extends AnonymousAuthenticationFilter {
	
	public AnonymousUserFilter(String key, Object principal,
			List<GrantedAuthority> authorities) {
		super(key, principal, authorities);
	}

	public AnonymousUserFilter(String key) {
		super(key);
	}

	private final Log logger = LogFactory.getLog(AnonymousUserFilter.class);

	@Autowired
	private WebSiteUserService webSiteUserService;
	
	@Autowired
	private WebSiteUserVehicleService webSiteUserVehicleService;
	
	@Autowired
	private WebSiteUserCookieService webSiteUserCookieService;
	
	@Autowired
	private StoreService storeService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) res;
		
		if(logger.isInfoEnabled()) logger.info("In the AnonymousUserFilter Referer:" + httpRequest.getHeader("Referer"));

		

		loadUserData(httpRequest, httpResponse);
		
		loadVehicleData(httpRequest, httpResponse);
		
		loadStoreData(httpRequest, httpResponse);
		
		
		
		super.doFilter(req, res, chain);
	}

	private void loadStoreData(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		Store store = (Store) WebUtils.getSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE);
		
		if(store == null){
			WebSiteUser user = (WebSiteUser)WebUtils.getSessionAttribute(httpRequest, SessionConstants.WEBSITEUSER);
			if(user == null)
				return;
			store = storeService.findStoreLightById(user.getStoreNumber());
			WebUtils.setSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE, store);
		}else{
			if(logger.isInfoEnabled()) logger.info("a store was found in the session: " + store.getStoreNumber());
		}
	}

	private void loadVehicleData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		//step 1, check session for WebSiteUserVehicles
		List<UserVehicle> vehicles = (List<UserVehicle>) WebUtils.getSessionAttribute(httpRequest, SessionConstants.WEBSITEUSERVEHICLES);
		
		//means we did not find the vehicles.
		//lets attempt load from user
		if(vehicles == null){
			WebSiteUser user = (WebSiteUser)WebUtils.getSessionAttribute(httpRequest, SessionConstants.WEBSITEUSER);
			
			if(user == null){
				if(logger.isInfoEnabled()) logger.info("No user found on session");
			}else{
				if(logger.isInfoEnabled()) logger.info("Loading WSUVs from user...");

				Errors errors = new Errors();
				
				try {
					List<UserVehicle> loadedVehicles = webSiteUserVehicleService.fetchWebSiteUserVehiclesByWebSiteUser(user.getWebSiteUserId(), errors);

					System.out.println("\n\n\n\nLoaded vehicles: "+loadedVehicles);
					
					if(loadedVehicles != null && !loadedVehicles.isEmpty()){
						if(logger.isInfoEnabled()) logger.info("Adding vehicles to session and cookie: " + loadedVehicles);
						//we have it, now save to session.
						WebUtils.setSessionAttribute(httpRequest, SessionConstants.WEBSITEUSERVEHICLES, loadedVehicles);
						
						//check to see if the preferred vehicle is set
						//if not set it on the user and update
						UserVehicle selectedVehicle = null;
						if (user.getWebSiteUserVehicleId() == null) {
							if (loadedVehicles != null && !loadedVehicles.isEmpty()) {
								System.out.println("User has no selected vehicle, set it to "+loadedVehicles.get(0));
								selectedVehicle = loadedVehicles.get(0);
								user.setWebSiteUserVehicleId(selectedVehicle.getId());
								user = webSiteUserService.updateUser(user, errors);
							}
						} else {
							// make sure it's still valid
							if (loadedVehicles != null && loadedVehicles.size() > 0) {
								for (UserVehicle vehicle : loadedVehicles) {
									if (user.getWebSiteUserVehicleId().equals(vehicle.getId())) {
										System.out.println("User has a valid selected vehicle, set it to "+vehicle);
										selectedVehicle = vehicle;
										break;
									}
								}
								
								// they must have had an invalid one selected, default to first one
								if (selectedVehicle == null) {
									System.out.println("User had an invalid selected vehicle, set it to "+loadedVehicles.get(0));
									selectedVehicle = loadedVehicles.get(0);
									user.setWebSiteUserVehicleId(selectedVehicle.getId());
									user = webSiteUserService.updateUser(user, errors);
								}
								
							}
						}
						
						WebUtils.setSessionAttribute(httpRequest, SessionConstants.WEBSITEUSER, user);
						
						if (selectedVehicle != null) {
							WebUtils.setSessionAttribute(httpRequest, SessionConstants.WEBSITEUSERSELECTEDVEHICLE, selectedVehicle);
						}
					}
				} catch (Throwable throwable) {
					logger.error("There was an error loading anonymous user vehicle data", throwable);
				}
			}
		}else{
			if(logger.isInfoEnabled()) logger.info("A list of WSUVs was found in the session: " + vehicles);
		}
		
	}

	private void loadUserData(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		//step 1, check session, if we have it, no need to do anything.
		WebSiteUser user = (WebSiteUser)WebUtils.getSessionAttribute(httpRequest, SessionConstants.WEBSITEUSER);		

		if(user == null){
			//if we didn't find a WebSiteUser, lets search the cookie first
			//get all cookies
			Cookie webSiteUserCookie = WebUtils.getCookie(httpRequest, CookieConstants.WEBSITEUSERID);
			
			//this means we did not find the cookie.  At this point, we have no cookie'd user and no session user.
			//We need to create an anonymous user
			if(webSiteUserCookie == null){
				if(logger.isInfoEnabled()) logger.info("Creating an anonymous user");
				
				Errors errors = new Errors();
				user = webSiteUserService.createAnonymousUser(errors);
				
				//if there was errors, just skip this and continue with the filter
				//we should hit this processing on the next request.
				//not much we can other than that.
				if(errors.hasErrors()){
					for(String message : errors.getGlobalErrors()){
						logger.error(message);
					}
				}else{				
					//set it on the session
					WebUtils.setSessionAttribute(httpRequest, SessionConstants.WEBSITEUSER, user);
					//also write a cookie.
					httpResponse.addCookie(webSiteUserCookieService.createUserCookie(user));
				}
				
			}else{
				//means we had a cookie, so this is a previous visitor to the site.
				//lets read the cookie and load the user into the session
				
				if(logger.isInfoEnabled()) logger.info("Loading User info from the cookie");
				
				String userId = webSiteUserCookieService.getUserIdFromCookie(webSiteUserCookie);
				
				Errors errors = new Errors();
				user = webSiteUserService.getUser(Long.valueOf(userId), errors);
				
				if(errors.hasErrors()){
					for(String message : errors.getGlobalErrors()){
						logger.error(message);
					}
				}else{
					
					if(logger.isInfoEnabled()) logger.info("Loaded this user id: " + userId);
					
					//we have the user, lets set it.
					WebUtils.setSessionAttribute(httpRequest, SessionConstants.WEBSITEUSER, user);				
					//update cookie, meaning just replace the existing one
					httpResponse.addCookie(webSiteUserCookieService.createUserCookie(user));
				}
								
			}
		}else{
			if(logger.isInfoEnabled()) logger.info("A user was found in the session: " + user.getWebSiteUserId());
		}
	}
	

}
