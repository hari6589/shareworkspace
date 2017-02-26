package com.bfrc.dataaccess.dao;

import com.bfrc.dataaccess.model.user.WebSiteUser;


public interface WebSiteUserDAO {


	WebSiteUser createUser(WebSiteUser webSiteUser);
	WebSiteUser updateUser(WebSiteUser webSiteUser);
	//List<WebSiteUserVehicle> getUserVehicles(Long userId);
	WebSiteUser authenticateUser(String loginName, String pwd, Long webSiteId);
	WebSiteUser getUserByEmail(String email, Long webSiteId);
	WebSiteUser getUserById(Long webSiteUserId);
	
}
