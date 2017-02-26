package com.bsro.service.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bsro.controller.user.WebSiteUserController;
import com.bsro.service.user.WebSiteUserService;
import com.bsro.errors.Errors;

public class WebSiteUserSecurityServiceImpl implements UserDetailsService {

	private final Log logger = LogFactory.getLog(WebSiteUserSecurityServiceImpl.class);
	
	@Autowired
	private WebSiteUserService webSiteUserService;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		// this is intended to retrieve a user that Spring will authenticate
		// against.  This is NOT doing the authentication

		if(logger.isInfoEnabled()) logger.info("SecurityService loadUserByUsername");
		
		Errors errors = new Errors();
		WebSiteUser ourStoredUser = new WebSiteUser();
//		ourStoredUser.setUserTypeId(WebSiteUser.LOGGED_IN_USER_TYPE_ID);
		//need a get user
		try {
			ourStoredUser = webSiteUserService.getUser(userName, errors);
		} catch (IOException e) {
			logger.error("There was an issue obtaining the user via webservice, stack trace is below. userName: " + userName);			
			e.printStackTrace();			
		}
		
		//TODO check errors list

		UserDetails userDetails = null;
		
		//TODO determine what make user enabled / expire / pw locked / account locked
		userDetails = new User(
				ourStoredUser.getEmailAddress(), //USER
				ourStoredUser.getPassword(), //PW
				true, //ENABLED
				true, //NOT EXPIRED
				true, //PASSWORD IS NOT EXPIRED
				true, //ACCOUNT IS NOT LOCKED
				getAuthorities(ourStoredUser.getUserTypeId()));		

		return userDetails;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(long userTypeId) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);

		// All users are granted with ROLE_USER access
		// Therefore this user gets a ROLE_USER by default
		if(logger.isInfoEnabled()) logger.info("Grant ROLE_ANONYMOUS to this user");
		authList.add(new GrantedAuthorityImpl("ROLE_ANONYMOUS"));

		// Check if this user has admin access
		// We interpret Integer(1) as an admin user
		if (userTypeId == WebSiteUser.LOGGED_IN_USER_TYPE_ID) {
			// User has admin access
			if(logger.isInfoEnabled()) logger.info("Grant ROLE_USER to this user");
			authList.add(new GrantedAuthorityImpl("ROLE_USER"));
		}

		// Return list of granted authorities
		return authList;
	}

}
