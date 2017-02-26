package com.bsro.controller.user;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.bfrc.Config;
import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.framework.util.ControllerUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.store.Store;
import com.bsro.constants.CookieConstants;
import com.bsro.constants.SessionConstants;
import com.bsro.errors.Errors;
import com.bsro.service.store.StoreService;
import com.bsro.service.user.WebSiteUserService;

@Controller
public class WebSiteUserController {

	private final Log logger = LogFactory.getLog(WebSiteUserController.class);

	@Autowired
	private WebSiteUserService userService;

	@Autowired
	private StoreService storeService;

	public void setUserService(WebSiteUserService userService) {
		this.userService = userService;
	}

	

	//@RequestMapping(value = "/user/logged-out.htm", method = RequestMethod.GET)
	public String loggedOut(HttpServletResponse response, HttpSession session,Model model) {
		
		Cookie cookie = new Cookie(CookieConstants.WEBSITEUSERID, null);
		response.addCookie(cookie);
		
		session.removeAttribute(SessionConstants.WEBSITEUSER);
		session.removeAttribute(SessionConstants.WEBSITEUSERSELECTEDVEHICLE);
		session.removeAttribute(SessionConstants.WEBSITEUSERVEHICLES);
		return "user/logged-out";
	}

	/*
	 * for new user to create login
	 */

	//@RequestMapping(value = "/user/create-account.htm", method = RequestMethod.GET)
	public String createAccountGet(Model model, String email) {
		return "user/create-account";
	}

	//@RequestMapping(value = "/user/create-account.htm", method = RequestMethod.POST)
	public String creatUserLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model, @RequestParam(value = "emailAddress", required = true) String email,
			@RequestParam(value = "newPassword", required = true) String password1, @RequestParam(value = "secondNewPassword", required = true) String password2) {

		Errors errors = new Errors();
		try {
			WebSiteUser wsobj = new WebSiteUser();

			Util.debug("creating user controller email:" + email+" pass:"+password1 +" pass2:" + password2);
			wsobj = userService.createUser(email, password1, password2, errors);

			if (!errors.hasErrors()) {
				if (wsobj != null) {
					// we know they just created an account, so lets give them
					// the right access.
					// set up the user as a ROLE_USER in security Context.
					// UserDetails userDetails = // custom UserDetailsImpl
					// Authentication authentication = new
					// UsernamePasswordAuthenticationToken(
					// userDetails, userDetails.getPassword(),
					// userDetails.getAuthorities());
					// SecurityContextHolder.getContext().setAuthentication(authentication);
//					if (isAuthenticated(response, session, model, wsobj, email, password1, errors)) {
//						return "home/index";// to landing page ???
//					} else {
//
//						Util.debug("WebSiteUser failed auth");
//						model.addAttribute("errors", "Login failed, Please try again.");
//						ControllerUtils.setErrors(model, errors);
//					}
//					return createAccountGet(model, email);

					return "user/login";

				} else {
					Util.debug("WebSiteUser is null");
					model.addAttribute("errors", "Login failed, Please try again.");
					ControllerUtils.setErrors(model, errors);
					return createAccountGet(model, email);
				}
			} else {
				Util.debug("Error :  " + errors);
				ControllerUtils.setErrors(model, errors);
				return createAccountGet(model, email);
			}

		} catch (IOException ioEx) {
			Util.debug("IOException : " + ioEx);
			ControllerUtils.setErrors(model, errors);
			return createAccountGet(model, email);
		}
	}

	public boolean isAuthenticated(HttpServletResponse response, HttpSession session, Model model, WebSiteUser webSiteUser, String email, String pwd, Errors errors) {

		if (!isCookieUser(webSiteUser)) {
			try {
				WebSiteUser wsuTmp = userService.authenticateUser(email, pwd, errors);

				if (wsuTmp != null) {
					addUserToSession(response, session, model, webSiteUser);
					return true;
				} else {
					Util.debug("authenticateUser is null ");
					ControllerUtils.setErrors(model, errors);
				}

			} catch (IOException ioEx) {
				ControllerUtils.setErrors(model, errors);
				Util.debug("IOException : " + ioEx);
			}
		}
		return false;

	}

	public boolean isCookieUser(WebSiteUser webSiteUser) {

		Util.debug(" USER TYPE: " + webSiteUser.getUserTypeId());
		return (webSiteUser.getUserTypeId() == WebSiteUser.COOKIE_USER_TYPE_ID);
	}

	public void addUserToSession(HttpServletResponse response, HttpSession session, Model model, WebSiteUser webSiteUser) {

		session.setAttribute(SessionConstants.WEBSITEUSER, webSiteUser);

		Cookie cookie = new Cookie(CookieConstants.WEBSITEUSERID, webSiteUser.getWebSiteUserId().toString());
		cookie.setMaxAge(CookieConstants.EXPIRE_TIME); // 24 hour
		// cookie.setSecure(true);
		response.addCookie(cookie);

		model.addAttribute("userLogin", webSiteUser);
	}

	//@RequestMapping(value = "/user/login.htm", method = RequestMethod.GET)
	public String loginGet(Model model, @RequestParam(value = "loginError", required = false) Boolean validationFailed) {

		if (validationFailed != null && validationFailed) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserDetails) {
				String userName = ((UserDetails) principal).getUsername();
				model.addAttribute("email", userName);
			}
		}
		return "user/login";
	}

	//@RequestMapping(value = "/user/update-store.htm", method = RequestMethod.GET)
	public String updateStore(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model, @RequestParam(value = "storeNumber") Long storeNumber, @RequestParam(value = "returnURL", required = false) String returnURL) {

		//update the website user to the new preferred store
		WebSiteUser webSiteUser = (WebSiteUser) WebUtils.getSessionAttribute(request, SessionConstants.WEBSITEUSER);
		if (webSiteUser != null) {
			Errors errors = new Errors();
			webSiteUser.setStoreNumber(storeNumber);
			try {
				WebUtils.setSessionAttribute(request, SessionConstants.WEBSITEUSER, userService.updateUser(webSiteUser, errors));
			} catch (IOException e) {
				errors.addGlobalError("Unable to update store on user");
				e.printStackTrace();
			}
		}
		
		//set the new preferred store on the session
		Store store = storeService.findStoreLightById(storeNumber);
		WebUtils.setSessionAttribute(request, SessionConstants.PREFERRED_STORE, store);
		//TODO if we ever use this in the future, set the cached zip
		
		if(returnURL!=null&&returnURL.length()>0)
			return "redirect:"+returnURL;
		return "redirect:/index";
	}

	
	//@RequestMapping(value = "/user/reset-password.htm", method = RequestMethod.GET)
	public String resetPasswordGet(Model model, @RequestParam(value = "token", required = true) String token) {

		model.addAttribute("token", token);
		return "user/reset-password";
	}

	//@RequestMapping(value = "/user/reset-password.htm", method = RequestMethod.POST)
	public String resetPasswordPost(Model model, @RequestParam(value = "token", required = true) String token, @RequestParam(value = "email", required = true) String emailAddress,
			@RequestParam(value = "newPassword", required = true) String password1, @RequestParam(value = "secondNewPassword", required = true) String password2) {

		Errors errors = new Errors();

		try {
			WebSiteUser wsuTmp = userService.resetUserPassword(emailAddress, password1, password2, token, errors);
			if (wsuTmp != null) {
				return "user/login";
			} else {
				ControllerUtils.setErrors(model, errors);
				return resetPasswordGet(model, token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ControllerUtils.setErrors(model, errors);
			return resetPasswordGet(model, token);
		}

	}

	//@RequestMapping(value = "/user/forgot-password.htm", method = RequestMethod.GET)
	public String forgotPasswordGet(HttpServletRequest request, HttpSession session, Model model) {

		return "user/forgot-password";
	}

	//@RequestMapping(value = "/user/forgot-password.htm", method = RequestMethod.POST)
	public String forgotPasswordPost(HttpServletRequest request, HttpSession session, Model model, @RequestParam(value = "email", required = true) String emailAddress) {

		Errors errors = new Errors();
		try {
			String siteName = Config.locate(request.getSession().getServletContext()).getSiteName();
			String fullSiteName = Config.locate(request.getSession().getServletContext()).getSiteFullName();
			Boolean worked = userService.sendForgottenPasswordEmail(emailAddress, siteName, fullSiteName, errors);
			if (worked != null && worked) {
				model.addAttribute("errors", "Success! Please check your email.");
				model.addAttribute("email", emailAddress);
				return forgotPasswordGet(request, session, model);

			} else {
				ControllerUtils.setErrors(model, errors);
				return forgotPasswordGet(request, session, model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ControllerUtils.setErrors(model, errors);
			return forgotPasswordGet(request, session, model);
		}
	}

	// TODO what happens when this fails
	//@RequestMapping(value = "/user/create-user-from-cookie.htm", method = RequestMethod.POST)
	public String createUserLoginFromCookieUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model, @RequestParam(value = "email", required = true) String email, @RequestParam(value = "newPassword", required = true) String password1, @RequestParam(value = "secondNewPassword", required = true) String password2, @RequestParam(value = "returnURL", required = false) String returnURL) {

		WebSiteUser webSiteUser = (WebSiteUser) session.getAttribute(SessionConstants.WEBSITEUSER);
		Errors errors = new Errors();
		Util.debug("TESTING THE CREATE USER LOGIN FROM ANON USER: " + webSiteUser);
		if (isCookieUser(webSiteUser)) {
			try {
				WebSiteUser wsuTmp = userService.createLoginUserFromAnonymousUser(webSiteUser.getWebSiteUserId(), email, password1, password2, errors);

				if (wsuTmp != null) {

					addUserToSession(response, session, model, wsuTmp);
					if(returnURL!=null&&returnURL.length()>0)
						return "redirect:"+returnURL;
					return "redirect:/index";
				} else {
					ControllerUtils.setErrors(model, errors);
					Util.debug("createLoginUserFromCookieUser is null ");
				}
			} catch (IOException ioEx) {
				ControllerUtils.setErrors(model, errors);
				Util.debug("IOException : " + ioEx);
			}
		}
		Util.debug("create user from anon global ERRORS : " + errors.getGlobalErrors().toString());
		Util.debug("create user from anon field ERRORS : " + errors.getGlobalErrors().toString());
		if(returnURL!=null&&returnURL.length()>0)
			return "redirect:"+returnURL;
		return "redirect:/index";
	}

}
