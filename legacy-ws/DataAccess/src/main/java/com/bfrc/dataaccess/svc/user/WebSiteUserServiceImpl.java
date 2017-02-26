package com.bfrc.dataaccess.svc.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.WebSiteUserDAO;
import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.dataaccess.svc.webdb.website.WebsiteService;
import com.bfrc.dataaccess.util.StringUtils;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.contact.Mail;

@Service
public class WebSiteUserServiceImpl implements WebSiteUserService {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private WebSiteUserDAO webSiteUserDao;
	@Autowired
	private MailManager mailManager;
	@Autowired
	WebsiteService webSiteService;
	
	private int PASSWORD_RESET_HOURS = 24;
	
	public WebSiteUser createUser(String email, String password, String webSiteName) throws IOException {
		WebSite webSite = webSiteService.getBySiteName(webSiteName);
		WebSiteUser webSiteUser = webSiteUserDao.getUserByEmail(email, Long.valueOf(webSite.getSiteId()));
		if(webSiteUser==null && webSite!=null){
			Date currentDate = new Date();
			webSiteUser = new WebSiteUser();
			webSiteUser.setEmailAddress(email);
			webSiteUser.setCreatedDate(currentDate);
			webSiteUser.setModifiedDate(currentDate);
			webSiteUser.setWebSiteId(Long.valueOf(webSite.getSiteId()));
			webSiteUser.setUserTypeId(WebSiteUser.LOGGED_IN_USER_TYPE_ID);

			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);			
			webSiteUser.setPassword(hashedPassword);
			
			return webSiteUserDao.createUser(webSiteUser);
			
		}else return null;
	}

	public WebSiteUser updateUser(WebSiteUser webSiteUser) throws IOException {

		webSiteUser.setModifiedDate(new Date());
		return webSiteUserDao.updateUser(webSiteUser);
	}

	public WebSiteUser authenticateUser(String email, String pwd, String appName) throws IOException {
		WebSite webSite = webSiteService.getBySiteName(appName);
		WebSiteUser webSiteUser = webSiteUserDao.authenticateUser(email, pwd, Long.valueOf(webSite.getSiteId()));
		if(webSiteUser!=null){
			webSiteUser.setLastLoginDate(new Date());
			return webSiteUserDao.updateUser(webSiteUser);
		}
		return null;
	}
	
	public WebSiteUser getUser(String email, String appName) throws IOException {

		WebSite webSite = webSiteService.getBySiteName(appName);
		WebSiteUser webSiteUser = webSiteUserDao.getUserByEmail(email, Long.valueOf(webSite.getSiteId()));
		if(webSiteUser!=null){
			return webSiteUser;
		}
		return null;
	}
	
	public WebSiteUser getUser(Long id) throws IOException {
		WebSiteUser webSiteUser = webSiteUserDao.getUserById(id);
		if(webSiteUser!=null){
			return webSiteUser;
		}
		return null;
	}
	
	public WebSiteUser createWebSiteAnonymousUser(String webSiteName) throws IOException {
		
		WebSite webSite = webSiteService.getBySiteName(webSiteName);
		if(webSite!=null){
			WebSiteUser webSiteUser = new WebSiteUser();
			Date currentDate = new Date();
			webSiteUser.setCreatedDate(currentDate);
			webSiteUser.setModifiedDate(currentDate);
			webSiteUser.setLastLoginDate(currentDate);
			webSiteUser.setWebSiteId(Long.valueOf(webSite.getSiteId()));
			webSiteUser.setEmailAddress(String.valueOf(currentDate.getTime()) );
			return webSiteUserDao.createUser(webSiteUser);
		}return null;
	}
	
	public WebSiteUser resetPassword(WebSiteUser webSiteUser, String password) throws IOException {
		
		if(webSiteUser==null||password==null||password.length()==0)
			return null;
		webSiteUser.setUserTypeId(WebSiteUser.LOGGED_IN_USER_TYPE_ID);
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);			
		webSiteUser.setPassword(hashedPassword);
		webSiteUser.setPasswordReset(false);
		webSiteUser.setPasswordResetMaxDateTime(null);
		webSiteUser.setPasswordResetToken(null);
		return updateUser(webSiteUser);
		
	}
	public WebSiteUser createUserFromAnonymous(WebSiteUser webSiteUser, String email, String password) throws IOException {
		
		if(webSiteUser==null||email==null||email.length()==0||password==null||password.length()==0)
			return null;
			webSiteUser.setEmailAddress(email);
			Date currentDate = new Date();
			webSiteUser.setCreatedDate(currentDate);
			webSiteUser.setModifiedDate(currentDate);
			webSiteUser.setLastLoginDate(currentDate);
			webSiteUser.setUserTypeId(WebSiteUser.LOGGED_IN_USER_TYPE_ID);

			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);			
			webSiteUser.setPassword(hashedPassword);
			
			return webSiteUserDao.updateUser(webSiteUser);
		
	}
	public Boolean sendForgottenPasswordEmail(String email, String emailLink, String fullSiteName, String webSiteName) throws IOException {

		WebSite webSite = webSiteService.getBySiteName(webSiteName);
		WebSiteUser webSiteUser = webSiteUserDao.getUserByEmail(email, Long.valueOf(webSite.getSiteId()));
		if(webSiteUser==null){
			Util.debug("user is null");
			return false;
		}
		webSiteUser.setPasswordReset(true);
		webSiteUser.setPasswordResetToken(com.bfrc.dataaccess.util.StringUtils.generatePassword());
		Date passwordResetDateTime = new Date((new Date()).getTime()+PASSWORD_RESET_HOURS*60*60*1000);
		webSiteUser.setPasswordResetMaxDateTime(passwordResetDateTime);
		updateUser(webSiteUser);
		//TODO when webservice can send emails then uncomment
		return sendUserEmail(webSiteUser, emailLink, fullSiteName);
//		return true;
		
	}
	private boolean sendUserEmail(WebSiteUser webSiteUser, String link, String fullSiteName){
			
			if(webSiteUser == null || link ==null){

				if(link==null)Util.debug("link is null");
				else Util.debug("user is null");
				return false;
				
			}
			try{
//				ClassPathResource cpr = new ClassPathResource("emails/email_pwd_user.html"); //Doesn't work, can't find 
//				String emailLocation = cpr.getFile().getAbsolutePath();
//				Util.debug("============email Location: " + emailLocation);
				String content = StringUtils.createForgotPasswordEmailString();
				Map<String,String> data = new HashMap<String,String>();
				data.put("FIRST_NAME", webSiteUser.getFirstName());
				data.put("EMAIL_ADDRESS", webSiteUser.getEmailAddress());
				data.put("NAME", webSiteUser.getEmailAddress());
				data.put("LINK", link);
				//String message = ServerUtil.populateEmailMessage(emailLocation, data);
				String message = ServerUtil.populateEmailMessageContent(content, data);
				
				Mail mail = new Mail();
				mail.setTo(new String[] {webSiteUser.getEmailAddress()});
				mail.setSubject(fullSiteName + " - Reset Password");
				String from = webSiteService.getFrom(Integer.valueOf(webSiteUser.getWebSiteId().toString()));
				mail.setFrom(from);
				mail.setHtml(true);
				mail.setBody(message);
				mailManager.sendMail(mail);
			return true;
			} catch (Throwable ex) {
				Util.debug("===FAILED");
				ex.printStackTrace();
			}
			return false;
		}
}
