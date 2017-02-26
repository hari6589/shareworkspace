package com.bsro.service.user;

import java.io.IOException;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bsro.errors.Errors;

public interface WebSiteUserService {

	WebSiteUser createUser(String email, String password1, String password2, Errors errors) throws IOException;
	WebSiteUser resetUserPassword(String email, String password1, String password2, String token, Errors errors) throws IOException;
	WebSiteUser updateUser(WebSiteUser webSiteUser, Errors errors) throws IOException;
	//List<WebSiteUserVehicle> getUserVehicles(Long userId);
	WebSiteUser authenticateUser(String loginName, String pwd, Errors errors) throws IOException;
	WebSiteUser createLoginUserFromAnonymousUser(Long webSiteUserId, String email, String password1, String password2,Errors errors) throws IOException;
	Boolean sendForgottenPasswordEmail(String emailAddress, String siteName,String fullSiteName, Errors errors) throws IOException;
	WebSiteUser getUser(String email, Errors errors) throws IOException;
	WebSiteUser getUser(Long webSiteUserId, Errors errors) throws IOException;
	WebSiteUser createAnonymousUser(Errors errors) throws IOException;
	
	
}
