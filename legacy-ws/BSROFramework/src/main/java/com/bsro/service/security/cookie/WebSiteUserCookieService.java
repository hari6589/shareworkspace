package com.bsro.service.security.cookie;

import java.util.List;

import javax.servlet.http.Cookie;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserVehicle;

public interface WebSiteUserCookieService {
	public abstract String getUserIdFromCookie(Cookie cookie);

	public abstract Cookie createUserCookie(WebSiteUser user);
	
	

}
