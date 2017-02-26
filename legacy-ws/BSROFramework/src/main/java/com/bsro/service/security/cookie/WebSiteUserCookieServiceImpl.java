package com.bsro.service.security.cookie;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bsro.constants.CookieConstants;

public class WebSiteUserCookieServiceImpl implements WebSiteUserCookieService {
	
	private final Log logger = LogFactory.getLog(WebSiteUserCookieServiceImpl.class);
	
	private String password;	
	private String salt;
	private TextEncryptor textEncryptor;
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@PostConstruct
	public void initializeTextEncryptor() {
	    textEncryptor = Encryptors.text(password, salt);
	    if(logger.isInfoEnabled()){logger.info("Initialized the textEncryptor");}
	}

	@Override
	public Cookie createUserCookie(WebSiteUser user) {

		String userId = String.valueOf(user.getWebSiteUserId());

		Cookie webSiteUserCookie = new Cookie(CookieConstants.WEBSITEUSERID, textEncryptor.encrypt(userId));
		// set to 7 days, is that correct?
		webSiteUserCookie.setMaxAge(CookieConstants.EXPIRE_TIME);
		// webSiteUserCookie.setDomain(pattern);
		// webSiteUserCookie.
		
		return webSiteUserCookie;

	}

	@Override
	public String getUserIdFromCookie(Cookie cookie) {
		String hashedId = cookie.getValue();
		String decryptedId = textEncryptor.decrypt(hashedId);
		return decryptedId;
	}

}
