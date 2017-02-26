/**
 * 
 */
package com.bsro.api.rest.ws.user;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.svc.myprofile.BFSUserDataService;
import com.bfrc.dataaccess.util.StringUtils;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.util.ValidatorUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bsro.core.model.HttpHeader;
import com.bsro.core.security.RequireValidToken;

/**
 * @author schowdhury
 *
 */
@Component
public class MobileAppUserWebServiceImpl implements MobileAppUserWebService {
	
	private Logger log = Logger.getLogger(getClass().getName());
	
	@Autowired
	BFSUserDataService bfsUserDataService;

	@Override
	@RequireValidToken
	public String registerUser(HttpHeaders headers,HttpServletRequest request,
			String email, String password) {
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(email))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);

		if(!ValidatorUtils.isPasswordValid(password))
			return StringUtils.responseToJson(ValidationConstants.USER_PASSWORD_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		log.info("appName="+appName);
		return StringUtils.responseToJson(bfsUserDataService.registerUser(email, password, appName));
	}

	@Override
	@RequireValidToken
	public String authenticateUser(HttpHeaders headers, HttpServletRequest request,
			String email, String password) {
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(email))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}

		return StringUtils.responseToJson(bfsUserDataService.authenticateUser(email, password, appName));
	}

	@Override
	@RequireValidToken
	public String updateUser(HttpHeaders headers, HttpServletRequest request,
			String oldEmail, String newEmail, String oldPassword,
			String newPassword) {
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(oldEmail) || !ValidatorUtils.isEmailValid(newEmail))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);

		if(!com.bfrc.dataaccess.util.ValidatorUtils.isPasswordValid(newPassword) || !com.bfrc.dataaccess.util.ValidatorUtils.isPasswordValid(newPassword))
			return StringUtils.responseToJson(ValidationConstants.USER_PASSWORD_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return StringUtils.responseToJson(bfsUserDataService.updateUser(oldEmail, newEmail, oldPassword, newPassword, appName));
	}

	@Override
	@RequireValidToken
	public String forgotPassword(HttpHeaders headers, HttpServletRequest request, String email) {
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(email))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);

		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return StringUtils.responseToJson(bfsUserDataService.resetUserPassword(email, appName));
	}
	
	@Override
	@RequireValidToken
	public String dataExists(HttpHeaders headers, HttpServletRequest request, String email) {
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(email))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);

		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return StringUtils.responseToJson(bfsUserDataService.doesUserDataExist(email, appName));
	}

	@Override
	@RequireValidToken
	public String backupData(HttpHeaders headers, HttpServletRequest request, 
			String email, String data) {
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(email))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return StringUtils.responseToJson(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserDataService.saveUserData(data, email, appName);
	}

	@Override
	@RequireValidToken
	public String restoreData(HttpHeaders headers, HttpServletRequest request, String email) {
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(email))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserDataService.getUserData(email, appName);
	}
	
	@Override
	@RequireValidToken
	public String restoreSecuredData(HttpHeaders headers, HttpServletRequest request,
			String email, String password){
		if(!request.isSecure()){
			return StringUtils.responseToJson(ValidationConstants.REQUEST_FORBIDDEN);
		}
		if(!ValidatorUtils.isEmailValid(email))
			return StringUtils.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserDataService.getUserData(email, password, appName);
		
	}
	

}
