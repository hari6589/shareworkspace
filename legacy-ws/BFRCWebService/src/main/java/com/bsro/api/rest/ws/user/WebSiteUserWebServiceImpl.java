package com.bsro.api.rest.ws.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceRequest;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.dataaccess.svc.user.WebSiteUserService;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.util.ValidatorUtils;
import com.bfrc.framework.util.Util;
import com.bsro.core.model.HttpHeader;
import com.bsro.core.security.RequireValidAppNameAndToken;

@Component
public class WebSiteUserWebServiceImpl implements WebSiteUserWebService {

	private Logger log = Logger.getLogger(getClass().getName());

	private final String MESSAGE_PASSWORD_RESET_FAILED="Password Reset Failed";
	private final String MESSAGE_PASSWORD_RESET_NOT_ELIGIBLE="User is not eligible for password reset";
	private final String MESSAGE_LOGIN_FAIL="Login credentials are not valid";
	
	@Autowired
	private WebSiteUserService webSiteUserService;

	@RequireValidAppNameAndToken(URL+"/auth-user")
	public BSROWebServiceResponse authenticateWebSiteUser( HttpHeaders headers, String email, String password) {

		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
			Errors errors = new Errors();
		try{
			String  appName = null;
			if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
				appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
			}
			if(StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(password)){
				WebSiteUser user =  webSiteUserService.authenticateUser(email,password, appName);
				response.setPayload(user);
			}else
			{
				errors.getGlobalErrors().add(ValidationConstants.INVALID_REQUEST);
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
			}
		}catch(Throwable e){
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			errors.getGlobalErrors().add("An exception has occurred while authenticating a web site user.");
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		return response;
	}
	
	@RequireValidAppNameAndToken(URL+"/get-user")
	public BSROWebServiceResponse getWebSiteUser(HttpHeaders headers, String email) {

		BSROWebServiceResponse response = new BSROWebServiceResponse();
			Errors errors = new Errors();
		try{
			if(StringUtils.isNotEmpty(email)){
				String  appName = null;
				if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
					appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
				}
				WebSiteUser user =  webSiteUserService.getUser(email, appName);
				response.setPayload(user);
			}else{
				
				errors.getGlobalErrors().add(ValidationConstants.INVALID_REQUEST);
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
			}
		}catch(Throwable e){
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			errors.getGlobalErrors().add("An exception has occurred while obtaining a web site user.");
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		return response;
	}
	
	@RequireValidAppNameAndToken(URL+"/get-user-by-id")
	public BSROWebServiceResponse getWebSiteUser(HttpHeaders headers, Long webSiteUserId) {

		BSROWebServiceResponse response = new BSROWebServiceResponse();
			Errors errors = new Errors();
		try{
			if(webSiteUserId != null){
				WebSiteUser user =  webSiteUserService.getUser(webSiteUserId);
				response.setPayload(user);
			}else{				
				errors.getGlobalErrors().add(ValidationConstants.INVALID_REQUEST);
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
			}
		}catch(Throwable e){
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			errors.getGlobalErrors().add("An exception has occurred while obtaining a web site user by id.");
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		return response;
	}
	
	@RequireValidAppNameAndToken(URL+"/forgot-password")
	public BSROWebServiceResponse sendForgottenPasswordEmail(HttpHeaders headers, String email, String emailLink, String fullSiteName) {
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
			Errors errors = new Errors();
		try{
			String  appName = null;
			if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
				appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
			}
			Boolean worked =  webSiteUserService.sendForgottenPasswordEmail(email, emailLink, fullSiteName, appName);
			if(worked==null||!worked)
			{
				errors.getGlobalErrors().add("An error has occurred while sending forgotten password email.");
				response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());		
			}
			response.setPayload(worked);
		}catch(Exception e){
			e.printStackTrace();
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			errors.getGlobalErrors().add("An exception has occurred while sending forgotten email password.");
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		return response;
	}

	@RequireValidAppNameAndToken(URL+"/create-login-user")
	public BSROWebServiceResponse createWebSiteUser( HttpHeaders headers, String email,  String passwordOne,
			String passwordTwo) {
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
		}
		//Util.debug("creating user email:" + email+" pass:"+passwordOne +" pass2:" + passwordTwo);
		try{
			if(StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(passwordOne) && StringUtils.isNotEmpty(passwordTwo)){
				
				if(ValidatorUtils.doPasswordsMatch(passwordOne,passwordTwo)){
					
					if(!ValidatorUtils.isEmailValid(email)){
						errors.getFieldErrors().put("email", new ArrayList<String>(Arrays.asList(ValidationConstants.EMAIL_VALIDATION)));
						response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
					}
					
					if(!ValidatorUtils.isPasswordValid(passwordOne)){
						errors.getFieldErrors().put("passwordOne", new ArrayList<String>(Arrays.asList(ValidationConstants.PASSWORD_VALIDATION)));
						response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
					}
					
					if(errors.getFieldErrors().isEmpty() && errors.getGlobalErrors().isEmpty()){

						WebSiteUser user = webSiteUserService.createUser(email,passwordOne, appName);
						if(user == null){
							//successful but user already existed 
							response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
							errors.getFieldErrors().put("email", new ArrayList<String>(Arrays.asList(ValidationConstants.EMAIL_ALREADY_EXISTS)));
							response.setPayload(null);
						}else{
							response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
							response.setPayload(user);
						}
					}
					
				}else{
					errors.getGlobalErrors().add(ValidationConstants.PASSWORDS_DID_NOT_MATCH);
					response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				}
			}else{
				errors.getGlobalErrors().add(ValidationConstants.INVALID_REQUEST);
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
			}
		}catch(Exception e){
			//do we just print these here? log?
			log.log(Level.SEVERE, "An exception has occurred while creating a web site user.", e);
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			errors.getGlobalErrors().add("An exception has occurred while creating a web site user.");
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		
		return response;
	}
	
	@RequireValidAppNameAndToken(URL+"/reset-password")
	public BSROWebServiceResponse resetPassword( HttpHeaders headers, String email,  String passwordOne,
			String passwordTwo, String token) {
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		try{
			if(StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(passwordOne) && StringUtils.isNotEmpty(passwordTwo) && StringUtils.isNotEmpty(token)){
				
				if(ValidatorUtils.doPasswordsMatch(passwordOne,passwordTwo)){
					
					
					if(!ValidatorUtils.isEmailValid(email)){
						errors.getFieldErrors().put("email", new ArrayList<String>(Arrays.asList(ValidationConstants.EMAIL_VALIDATION)));
						response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
					}
					
					if(!ValidatorUtils.isPasswordValid(passwordOne)){
						errors.getFieldErrors().put("passwordOne", new ArrayList<String>(Arrays.asList(ValidationConstants.PASSWORD_VALIDATION)));
						response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
					}
					
					if(errors.getFieldErrors().isEmpty() && errors.getGlobalErrors().isEmpty()){
						String  appName = null;
						if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
							appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
						}
						WebSiteUser user = webSiteUserService.getUser(email, appName);
						if(user == null){
							//successful but user already existed 
							response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
							errors.getFieldErrors().put("email", new ArrayList<String>(Arrays.asList(MESSAGE_PASSWORD_RESET_FAILED)));
							response.setPayload(null);
						}else{
							
							if(user.isPasswordReset()&&token.equals(user.getPasswordResetToken())&&user.getPasswordResetMaxDateTime()!=null&&(new Date()).before(user.getPasswordResetMaxDateTime()))
							{
								WebSiteUser successUser = webSiteUserService.resetPassword(user, passwordOne);
								if(successUser == null){
									//successful but user already existed 
									response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
									errors.getGlobalErrors().add(MESSAGE_PASSWORD_RESET_FAILED);
									response.setPayload(null);
								}else{						
									response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
									response.setPayload(user);
								}
							}else{
								response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
								errors.getGlobalErrors().add(MESSAGE_PASSWORD_RESET_NOT_ELIGIBLE);
								response.setPayload(null);
							}
						}
					}
					
				}else{
					errors.getGlobalErrors().add(ValidationConstants.PASSWORDS_DID_NOT_MATCH);
					response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				}
			}else{
				errors.getGlobalErrors().add(ValidationConstants.INVALID_REQUEST);
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
			}
		}catch(Exception e){
			//do we just print these here? log?
			log.log(Level.SEVERE, "An exception has occurred while resetting a web site user password.", e);
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			errors.getGlobalErrors().add("An exception has occurred while resetting a web site user password.");
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		
		return response;
	}
	
	@RequireValidAppNameAndToken(URL+"/create-anonymous-user")
	public BSROWebServiceResponse createWebSiteAnonymousUser(HttpHeaders headers) {
				
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		try{
			String  appName = null;
			if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
				appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
			}
			WebSiteUser user =  webSiteUserService.createWebSiteAnonymousUser(appName);
			response.setPayload(user);
		}catch(Throwable e){
			log.log(Level.SEVERE, "An exception has occurred while creating a web site user.", e);
			errors.getGlobalErrors().add("An exception has occurred while creating a web site user from an anon user.");
			response.setErrors(errors);
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		return response;
	}

	@RequireValidAppNameAndToken(URL+"/create-login-user-from-anonymous-user")
	public BSROWebServiceResponse createLoginUserFromAnonymousUser(HttpHeaders headers,Long webSiteUserId, String email, String passwordOne, String passwordTwo) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		try{
			WebSiteUser webSiteUser = webSiteUserService.getUser(webSiteUserId);
			if(webSiteUser !=null){
				if(StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(passwordOne) && StringUtils.isNotEmpty(passwordTwo)){
					
					if(ValidatorUtils.doPasswordsMatch(passwordOne,passwordTwo)){
						
						if(!ValidatorUtils.isEmailValid(email)){
							errors.getFieldErrors().put("email", new ArrayList<String>(Arrays.asList(ValidationConstants.EMAIL_VALIDATION)));
							response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
						}
						
						if(!ValidatorUtils.isPasswordValid(passwordOne)){
							errors.getFieldErrors().put("passwordOne", new ArrayList<String>(Arrays.asList(ValidationConstants.PASSWORD_VALIDATION)));
							response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
						}
						
						if(errors.getGlobalErrors().isEmpty() && errors.getFieldErrors().isEmpty()){
							String  appName = null;
							if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
								appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
							}
							WebSiteUser user = webSiteUserService.getUser(email, appName);
							if(user != null){
								response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
								errors.getGlobalErrors().add(ValidationConstants.EMAIL_ALREADY_EXISTS);
								response.setPayload(null);
							}else{
								WebSiteUser webUser = webSiteUserService.createUserFromAnonymous(webSiteUser, email, passwordOne);
	
								if(webUser == null){

									response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
									errors.getFieldErrors().put("email", new ArrayList<String>(Arrays.asList(ValidationConstants.EMAIL_ALREADY_EXISTS)));
									response.setPayload(null);
								}else{
									response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
									response.setPayload(webUser);
								}
							}
						}
						
					}else{
						errors.getGlobalErrors().add(ValidationConstants.PASSWORDS_DID_NOT_MATCH);
					}
				}else{
					errors.getGlobalErrors().add(ValidationConstants.INVALID_REQUEST);
				}
			}
		}catch(Throwable e){
			log.log(Level.SEVERE, "An exception has occurred while creating a web site user from an anonymous user.", e);
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			errors.getGlobalErrors().add("An exception has occurred while creating a web site user from an anonymous user.");
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		
		return response;
	}
	
	@RequireValidAppNameAndToken(URL+"/update-user")
	public BSROWebServiceResponse updateWebSiteUser( HttpHeaders headers, BSROWebServiceRequest request) {
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		ObjectMapper mapper = new ObjectMapper();
		WebSiteUser webSiteUser = mapper.convertValue(request.getRequestPayload(), WebSiteUser.class);
		
		try{
			if(webSiteUser !=null){
				validateWebSiteUser(errors, webSiteUser);
				if(errors.getFieldErrors().isEmpty() && errors.getGlobalErrors().isEmpty()){
					response.setPayload(webSiteUserService.updateUser(webSiteUser));
				}
			}else{
				Util.debug("FAILED SAVING IN WEBSERVICE" );
			}
		}catch(Throwable e){
			e.printStackTrace();
			log.log(Level.SEVERE, "An exception has occurred while updating a web site user.", e);
			errors.getGlobalErrors().add("An exception has occurred while updating a web site user.");
			errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(e));
			response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
		}
		if(!errors.getFieldErrors().isEmpty() || !errors.getGlobalErrors().isEmpty()){			
			response.setErrors(errors);
		}
		return response;
	}


	private void validateWebSiteUser(Errors errors, WebSiteUser webSiteUser) {
		if(StringUtils.isNotEmpty(webSiteUser.getFirstName()) && !ValidatorUtils.isFirstNameValid(webSiteUser.getFirstName())) {
			errors.getFieldErrors().put("firstName", new ArrayList<String>(Arrays.asList(ValidationConstants.FIRST_NAME_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getLastName()) && !ValidatorUtils.isLastNameValid(webSiteUser.getLastName())) {
			errors.getFieldErrors().put("lastName", new ArrayList<String>(Arrays.asList(ValidationConstants.LAST_NAME_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getAddress()) && !ValidatorUtils.isAddressValid(webSiteUser.getAddress())) {
			errors.getFieldErrors().put("address", new ArrayList<String>(Arrays.asList(ValidationConstants.ADDRESS_VALIDATION)));					
		}
		if(StringUtils.isNotEmpty(webSiteUser.getCity()) && !ValidatorUtils.isCityValid(webSiteUser.getCity())) {
			errors.getFieldErrors().put("city", new ArrayList<String>(Arrays.asList(ValidationConstants.CITY_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getState()) && !ValidatorUtils.isStateValid(webSiteUser.getState())) {
			errors.getFieldErrors().put("state", new ArrayList<String>(Arrays.asList(ValidationConstants.STATE_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getZip()) && !ValidatorUtils.isZipCodeValid(webSiteUser.getZip())) {
			errors.getFieldErrors().put("zip", new ArrayList<String>(Arrays.asList(ValidationConstants.ZIP_CODE_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getDayTimePhone()) && !ValidatorUtils.isPhoneValid(webSiteUser.getDayTimePhone())) {
			errors.getFieldErrors().put("dayTimePhone", new ArrayList<String>(Arrays.asList(ValidationConstants.PHONE_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getEveningPhone()) && !ValidatorUtils.isPhoneValid(webSiteUser.getEveningPhone())) {
			errors.getFieldErrors().put("eveningPhone", new ArrayList<String>(Arrays.asList(ValidationConstants.PHONE_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getMobilePhone()) && !ValidatorUtils.isPhoneValid(webSiteUser.getMobilePhone())) {
			errors.getFieldErrors().put("mobilePhone", new ArrayList<String>(Arrays.asList(ValidationConstants.PHONE_VALIDATION)));
		}
		if(StringUtils.isNotEmpty(webSiteUser.getPassword()) && !ValidatorUtils.isPasswordValid(webSiteUser.getPassword())) {
			errors.getFieldErrors().put("password", new ArrayList<String>(Arrays.asList(ValidationConstants.PASSWORD_VALIDATION)));
		}
	}

}
