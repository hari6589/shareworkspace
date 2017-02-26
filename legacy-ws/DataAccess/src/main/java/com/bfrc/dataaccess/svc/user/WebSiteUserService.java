package com.bfrc.dataaccess.svc.user;

import java.io.IOException;

import com.bfrc.dataaccess.model.user.WebSiteUser;

public interface WebSiteUserService {
	WebSiteUser createUser(String email, String password, String webSiteName) throws IOException;
	WebSiteUser updateUser(WebSiteUser webSiteUser) throws IOException;
	//List<WebSiteUserVehicle> getUserVehicles(Long userId);
	WebSiteUser authenticateUser(String email, String pwd, String appName) throws IOException;
	WebSiteUser createWebSiteAnonymousUser(String webSiteName) throws IOException;
	WebSiteUser getUser(String email, String appName) throws IOException;
	Boolean sendForgottenPasswordEmail(String email, String emailLink, String fullSiteName, String appName) throws IOException;
	public WebSiteUser resetPassword( WebSiteUser webSiteUser, String password) throws IOException;
	public WebSiteUser createUserFromAnonymous( WebSiteUser webSiteUser, String email, String password) throws IOException;
	WebSiteUser getUser(Long id) throws IOException;
	
}
