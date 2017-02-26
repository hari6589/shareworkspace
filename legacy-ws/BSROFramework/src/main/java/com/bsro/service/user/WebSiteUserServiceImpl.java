package com.bsro.service.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsro.errors.Errors;

import com.bfrc.Config;
import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.contact.Mail;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.rebrand.BSROWebserviceServiceImpl;

@Service("webSiteUserService")
public class WebSiteUserServiceImpl extends BSROWebserviceServiceImpl implements WebSiteUserService {

	private static final String PATH_USER_BASE = "/web-site-user";

	private static final String PATH_CREATE_USER = "/create-login-user";
	private static final String PATH_CREATE_ANONYMOUS_USER = "/create-anonymous-user";
	private static final String PATH_CREATE_LOGIN_USER_FROM_COOKIE_USER = "/create-login-user-from-anonymous-user";
		
	private static final String PATH_UPDATE_USER = "/update-user";
	private static final String PATH_AUTHENTICATE_USER = "/auth-user";
	private static final String PATH_GET_USER = "/get-user";
	private static final String PATH_GET_USER_BY_ID = "/get-user-by-id";
	private static final String PATH_FORGOT_PASSWORD = "/forgot-password";
	private static final String PATH_RESET_PASSWORD = "/reset-password";

	private static final String PARAM_EMAIL = "email";
	private static final String PARAM_EMAIL_LINK = "emailLink";
	private static final String PARAM_FULL_SITE_NAME = "fullSiteName";

	private static final String PARAM_PASSWORD  = "password";
	private static final String PARAM_PASSWORD_ONE  = "passwordOne";
	private static final String PARAM_PASSWORD_TWO  = "passwordTwo";
	private static final String PARAM_TOKEN  = "token";
	private static final String PARAM_WEBSITEUSER_ID  = "webSiteUserId";	

	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}

	@Autowired
	MailManager mailManager;
	@Autowired
	ContactDAO contactDAO;

	public WebSiteUser createUser(String email, String password1, String password2, Errors errors) throws IOException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_EMAIL,email);
		parameters.put(PARAM_PASSWORD_ONE,password1);
		parameters.put(PARAM_PASSWORD_TWO,password2);
		
		return (WebSiteUser) postWebservice(PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_CREATE_USER, 
				parameters,  
				WebSiteUser.class, 
				errors);
		
		//return null;
	}
	public WebSiteUser createAnonymousUser(Errors errors) throws IOException{
		Map<String, String> parameters = new HashMap<String, String>();
		return (WebSiteUser) postWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_CREATE_ANONYMOUS_USER,
				parameters, 
				WebSiteUser.class, 
				errors);
	}
	public WebSiteUser createLoginUserFromAnonymousUser(Long webSiteUserId, String email, String password1, String password2, Errors errors) throws IOException{
		Map<String, String> parameters = new HashMap<String, String>();	
		parameters.put(PARAM_WEBSITEUSER_ID,webSiteUserId.toString());
		parameters.put(PARAM_EMAIL,email);
		parameters.put(PARAM_PASSWORD_ONE,password1);
		parameters.put(PARAM_PASSWORD_TWO,password2);
		return (WebSiteUser) postWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_CREATE_LOGIN_USER_FROM_COOKIE_USER,
				parameters,			
				WebSiteUser.class, 
				errors);
	}

	public WebSiteUser updateUser(WebSiteUser webSiteUser, Errors errors) throws IOException{
    	return (WebSiteUser) postJSONToWebservice(
    			PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_UPDATE_USER, 
    			webSiteUser, 
    			WebSiteUser.class, 
    			errors);
		
	}

	public WebSiteUser authenticateUser(String email, String pwd, Errors errors) throws IOException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_EMAIL,email);
		parameters.put(PARAM_PASSWORD,pwd);
		return (WebSiteUser) postWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_AUTHENTICATE_USER, 
				parameters, 
				WebSiteUser.class,
				errors);
	}
	public Boolean sendForgottenPasswordEmail(String emailAddress,String siteName, String fullSiteName, Errors errors) throws IOException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_EMAIL,emailAddress);
		String emailLink = getEmailLink(siteName);
		parameters.put(PARAM_EMAIL_LINK,emailLink);
		parameters.put(PARAM_FULL_SITE_NAME,fullSiteName);
		return (Boolean) postWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_FORGOT_PASSWORD, 
				parameters, 
				Boolean.class,
				errors);
	}
	private String getEmailLink(String webSiteName){
		String urlLink = "";
		String userLogin = "/user/reset-password";
		if(Config.FCAC.equals(webSiteName)){
			urlLink=ServerUtil.getBSROSites().get(Config.FCAC) + userLogin;
		}//TODO add more sites when they go active
		
		return urlLink;
		
	}
	
	public WebSiteUser getUser(String email, Errors errors) throws IOException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_EMAIL,email);
		return (WebSiteUser) getWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_GET_USER, 
				parameters, 
				WebSiteUser.class,
				errors);
	}
	
	public WebSiteUser getUser(Long webSiteUserId, Errors errors) throws IOException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_WEBSITEUSER_ID,String.valueOf(webSiteUserId));
		return (WebSiteUser) getWebservice(
				PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_GET_USER_BY_ID, 
				parameters, 
				WebSiteUser.class,
				errors);
	}

	@Override
	public WebSiteUser resetUserPassword(String email, String password1, String password2, String token, Errors errors)
			throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_EMAIL,email);
		parameters.put(PARAM_PASSWORD_ONE,password1);
		parameters.put(PARAM_PASSWORD_TWO,password2);
		parameters.put(PARAM_TOKEN,token);
		
		return (WebSiteUser) postWebservice(PATH_WEBSERVICE_BASE + PATH_USER_BASE + PATH_RESET_PASSWORD, 
				parameters,  
				WebSiteUser.class, 
				errors);
	}	
	
}
