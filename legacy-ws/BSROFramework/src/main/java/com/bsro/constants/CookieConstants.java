package com.bsro.constants;



/**
 * This is to be used only for cookie related items
 *
 */
public class CookieConstants {

	//cookie is in seconds.  7 days, 24 hours, 60 minutes, 60 seconds
	//this also lines up with remember-me cookie for authentication in spring-security file
	public static int EXPIRE_TIME = 365 * 24 * 60 * 60;
	public static final String WEBSITEUSERID = "webSiteUserId";
	public static final String PREFERRED_STORE_NUMBER = "preferredStoreNumber";
}
