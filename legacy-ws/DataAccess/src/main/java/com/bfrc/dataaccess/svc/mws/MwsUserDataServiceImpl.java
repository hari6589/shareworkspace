/**
 * 
 */
package com.bfrc.dataaccess.svc.mws;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.sf.json.JSONObject;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.mws.MwsUserDataDAO;
import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.mws.MwsBackupData;
import com.bfrc.dataaccess.model.mws.MwsUserStatus;
import com.bfrc.dataaccess.model.mws.MwsUsers;
import com.bfrc.dataaccess.svc.webdb.EmailService;
import com.bfrc.dataaccess.util.Sites.SiteTypes;
import com.bfrc.dataaccess.util.StringUtils;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.util.ValidatorUtils;

/**
 * @author schowdhury
 *
 */
@Service("mwsUserDataService")
public class MwsUserDataServiceImpl implements MwsUserDataService {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	MwsUserDataDAO mwsUserDataDAO;
	
	@Autowired
	private EmailService emailService;

	@Resource(name="emailSender")
	private JavaMailSender mailSender;
	
	private static final int passwordResetAttempts = 20;
	
	
	public String registerUser(String email, String password, String userType) {
		logger.info("Inside register user");
		WebSite webSite = getSite(userType);
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, webSite);
		
		if(user != null && user.getUserId() != null){
			return ValidationConstants.USER_ALREADY_EXISTS;
		}

		String encryptPwd = ValidatorUtils.encryptPassword(password);
		logger.info("encryptPwd="+encryptPwd);
		MwsUsers createUser = new MwsUsers(email, email, encryptPwd, webSite);
		mwsUserDataDAO.saveMwsUser(createUser);

		return ValidationConstants.USER_SUCCESSFULLY_REGISTERED;
	}
	
	public String authenticateUser(String email, String password, String userType){
		logger.info("Inside authenticateUser");
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return ValidationConstants.USER_HAS_NOT_REGISTERED;
		}
		else{
			if(user.getUserId() != null){
				if(ValidatorUtils.validateUserEncryptedPassword(user.getPassword(),password)){
					if(MwsUserStatus.ACTIVE.getStatusCode().equals(user.getStatus()))
						return ValidationConstants.USER_AUTHENTICATION_SUCCESSFUL;	
				}
				if(MwsUserStatus.LOCKED.getStatusCode().equals(user.getStatus()))
					return ValidationConstants.USER_ACCOUNT_LOCKED;
				
				int failedAttempts = user.getUnsuccessfulAttempts();
				failedAttempts++;
				user.setUnsuccessfulAttempts(failedAttempts);
				if(ValidatorUtils.lockUserAccount(failedAttempts)){
					user.setStatus(MwsUserStatus.LOCKED.getStatusCode());
				}
				mwsUserDataDAO.saveMwsUser(user);
			}
		}
		return ValidationConstants.USER_AUTHENTICATION_FAILED;
	}

	public boolean doesUserExist(String email, String userType) {
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user != null && user.getUserId() != null){
			return true;
		}
		return false;
	}


	public String updateUser(String oldEmail, String newEmail, String oldPassword, String newPassword, String userType) {
		logger.info("Inside updateUser");
		MwsUsers user = mwsUserDataDAO.doesUserExist(oldEmail, getSite(userType));
		if(user == null ){
			return ValidationConstants.USER_DOES_NOT_EXIST;
		}else{			
			if(ValidatorUtils.validateUserEncryptedPassword(user.getPassword(), oldPassword)){
				if(!oldEmail.equals(newEmail)){
					MwsUsers checkNewUser = mwsUserDataDAO.doesUserExist(newEmail, getSite(userType));
					if(checkNewUser != null && !oldEmail.equals(newEmail)){
						return ValidationConstants.USER_ALREADY_EXISTS;
					}
					user.setPreviousEmail(oldEmail);
					user.setEmail(newEmail);
					if(!oldPassword.equals(newPassword)){
						user.setPassword(ValidatorUtils.encryptPassword(newPassword));
						mwsUserDataDAO.saveMwsUser(user);
						return ValidationConstants.USER_UPDATE_SUCCESSFUL;
					}
					mwsUserDataDAO.saveMwsUser(user);
					return ValidationConstants.USER_EMAIL_UPDATE_SUCCESSFUL;
				}
				else if(!oldPassword.equals(newPassword)){
					user.setPassword(ValidatorUtils.encryptPassword(newPassword));
					mwsUserDataDAO.saveMwsUser(user);
					return ValidationConstants.USER_PASSWORD_UPDATE_SUCCESSFUL;
				}				
			}
		}		
		return ValidationConstants.USER_UPDATE_FAILED;
	}

	public String resetUserPassword(String email, String userType) {
		logger.info("Inside resetUserPassword");
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, getSite(userType));

		if(user == null){
			return ValidationConstants.USER_DOES_NOT_EXIST;
		}else{
			int i = 0;
			String newPassword = com.bfrc.dataaccess.util.StringUtils.generatePassword(8);
			while(!ValidatorUtils.isPasswordValid(newPassword) && i < passwordResetAttempts){
				newPassword = com.bfrc.dataaccess.util.StringUtils.generatePassword(8);
				i++;
			}
			
			user.setPassword(ValidatorUtils.encryptPassword(newPassword));
			user.setUnsuccessfulAttempts(0);
			user.setStatus(MwsUserStatus.ACTIVE.getStatusCode());
			mwsUserDataDAO.saveMwsUser(user);
			logger.info("New Generated password="+newPassword);

			//send email
			String emailFrom = emailService.getDoNotReply(userType);
			String emailTo = email;
			String emailSubject = "Request for Password reset for your "+ getSiteNameString(userType) + " account";
			String emailBody = generateEmailBody(newPassword, userType);

			try {
				//send email to user with the new password
				sendEmail(emailTo, emailFrom, emailSubject, emailBody);
			} catch (MailException ex) {
				logger.severe("Error sending email to user: " + ex.getMessage());
				return ValidationConstants.USER_PASSWORD_RESET_FAILED;
			} catch(MessagingException me){
				logger.severe("Error sending email to user: " + me.getMessage());
				return ValidationConstants.USER_PASSWORD_RESET_FAILED;
			}		
		}
		return ValidationConstants.USER_PASSWORD_RESET_SUCCESSFUL;
	}

	public String saveUserData(String jsonData, String email, String userType) {
		logger.info("Inside saveUserData");
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, getSite(userType));
		MwsBackupData newUserData=null;
		if(user == null){
			return StringUtils.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
		}else{
			if(user.getUserId() != null){
				try {
					MwsBackupData currentData = mwsUserDataDAO.getUserData(user);
					if(currentData != null && currentData.getJsonData().length() > 0){
						newUserData = currentData;
						newUserData.setJsonData(Hibernate.createClob(jsonData));
						newUserData.setLastModifiedDesc("B");
						newUserData.setLastModifiedDate(new java.util.Date());
					}else{
						newUserData = new MwsBackupData(user, Hibernate.createClob(jsonData),"B");
					}

					mwsUserDataDAO.saveUserData(newUserData);
					logger.info("user data saved");
					MwsBackupData retrieveData = mwsUserDataDAO.getUserData(user);

					JSONObject successJson = new JSONObject();
					SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy h:mm a");
					successJson.put("Message", ValidationConstants.USER_DATA_BACKUP_SUCCESSFUL);
					successJson.put("lastModifiedDesc", retrieveData.getLastModifiedDesc());
					successJson.put("lastModifiedDate", format.format(retrieveData.getLastModifiedDate()));
					logger.info(successJson.toString(1));

					return successJson.toString();					
				} catch (SQLException e2) {
					logger.severe("SQLException "+ e2.getMessage());
				}
			}
		}
		return StringUtils.responseToJson(ValidationConstants.USER_DATA_BACKUP_FAILED);
	}

	public String getUserData(String email, String userType) {
		logger.info("Inside getUserData");
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return StringUtils.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
		}else {
			try {
				if(user.getUserId() != null){
					MwsBackupData userData = mwsUserDataDAO.getUserData(user);
					if(userData != null && userData.getJsonData() != null ){
						userData.setLastModifiedDesc("R");
						mwsUserDataDAO.saveUserData(userData);
						logger.info(com.bfrc.dataaccess.util.StringUtils.clobToString(userData.getJsonData()));
						return com.bfrc.dataaccess.util.StringUtils.clobToString(userData.getJsonData());
					}
					return StringUtils.responseToJson(ValidationConstants.USER_HAS_NO_BACKUP_DATA);
				}
			} catch (IOException ioe) {
				logger.severe("IOException "+ ioe.getMessage());
				System.err.println("IOException in method getUserData in class "+ this.getClass().getSimpleName());
			} catch (SQLException e) {
				logger.severe("SQLException "+ e.getMessage());
				System.err.println("SQLException in method getUserData in class "+ this.getClass().getSimpleName());
			}
		}
		return StringUtils.responseToJson(ValidationConstants.USER_DATA_RESTORE_ERROR);
	}
	
	public String getUserData(String email, String password, String userType) {
		logger.info("Inside retrieveUserData");
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return StringUtils.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
		}else {
			//match user password
			String userAuthResposne = authenticateUser(email, password, userType);
			if(ValidationConstants.USER_AUTHENTICATION_SUCCESSFUL.equalsIgnoreCase(userAuthResposne)){
				try {
					if(user.getUserId() != null){
						MwsBackupData userData = mwsUserDataDAO.getUserData(user);
						if(userData != null && userData.getJsonData() != null ){
							userData.setLastModifiedDesc("R");
							mwsUserDataDAO.saveUserData(userData);
							logger.info(com.bfrc.dataaccess.util.StringUtils.clobToString(userData.getJsonData()));
							return com.bfrc.dataaccess.util.StringUtils.clobToString(userData.getJsonData());
						}
						return StringUtils.responseToJson(ValidationConstants.USER_HAS_NO_BACKUP_DATA);
					}
				} catch (IOException ioe) {
					logger.severe("IOException "+ ioe.getMessage());
					System.err.println("IOException in method getUserData in class "+ this.getClass().getSimpleName());
				} catch (SQLException e) {
					logger.severe("SQLException "+ e.getMessage());
					System.err.println("SQLException in method getUserData in class "+ this.getClass().getSimpleName());
				}
			}else{
				return StringUtils.responseToJson(userAuthResposne);
			}
		}
		return StringUtils.responseToJson(ValidationConstants.USER_DATA_RESTORE_ERROR);
	}
	
	public String doesUserDataExist(String email, String userType) {
		logger.info("Inside doesUserDataExist");
		MwsUsers user = mwsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return ValidationConstants.USER_DOES_NOT_EXIST;
		}else {
			if(user.getUserId() != null){
				MwsBackupData userData = mwsUserDataDAO.getUserData(user);
				if(userData != null && userData.getJsonData() != null ){
					return ValidationConstants.USER_ALREADY_HAS_BACKUP_DATA;
				}
			}
		}
		return ValidationConstants.USER_HAS_NO_BACKUP_DATA;
	}
	
	private String generateEmailBody(String newPassword, String siteName){
		StringBuffer sb = new StringBuffer();
		sb.append("You recently requested that your "+ siteName +" password be reset.\r\n");
		sb.append("Your new password is "+newPassword+".\n\n");
		sb.append("If you have not requested this change, please contact administrator using Contact Us feature.\n\n");
		sb.append("Thank You,\n");
		sb.append(getSiteNameString(siteName) + " Team.\r\n");
		return sb.toString();
	}
	
	private void sendEmail(String toUser, String fromUser, String subject, String body) 
				throws MailException, MessagingException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(toUser);
		helper.setFrom(fromUser);
		helper.setSubject(subject);
		helper.setText(body, true);

		this.mailSender.send(message);		
	}
	
	private String getSiteNameString(String userType){
		if(userType.equalsIgnoreCase(SiteTypes.TP.name())){
			return "Tires Plus Mobile App";
		}else if(userType.equals(SiteTypes.FCAC.name())){
			return "Firestone Mobile App";
		}
		return "";
	}
	
	private WebSite getSite(String webSiteName){
		return mwsUserDataDAO.getSite(webSiteName);
	}
	
}
