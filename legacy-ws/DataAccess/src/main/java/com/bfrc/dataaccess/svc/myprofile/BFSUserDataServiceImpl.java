/**
 * 
 */
package com.bfrc.dataaccess.svc.myprofile;

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

import com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO;
import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.myprofile.BFSUser;
import com.bfrc.dataaccess.model.myprofile.MyBackupData;
import com.bfrc.dataaccess.model.myprofile.StatusType;
import com.bfrc.dataaccess.svc.webdb.EmailService;
import com.bfrc.dataaccess.util.Sites.SiteTypes;
import com.bfrc.dataaccess.util.StringUtils;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.util.ValidatorUtils;

/**
 * @author schowdhu
 *
 */
@Service("bfsUserDataService")
public class BFSUserDataServiceImpl implements BFSUserDataService {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	BFSUserDataDAO bfsUserDataDAO;
	
	@Autowired
	private EmailService emailService;

	@Resource(name="emailSender")
	private JavaMailSender mailSender;
	
	private static final int passwordResetAttempts = 20;

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#registerUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String registerUser(String email, String password, String userType) {
		logger.info("Inside register user");
		WebSite webSite = getSite(userType);
		BFSUser user = bfsUserDataDAO.doesUserExist(email, webSite);
		
		if(user != null && user.getUserId() != null){
			return ValidationConstants.USER_ALREADY_EXISTS;
		}

		String encryptPwd = ValidatorUtils.encryptPassword(password);
		logger.info("encryptPwd="+encryptPwd);
		BFSUser createUser = new BFSUser(email, email, encryptPwd, webSite);
		bfsUserDataDAO.saveBfsUser(createUser);

		return ValidationConstants.USER_SUCCESSFULLY_REGISTERED;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#authenticateUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String authenticateUser(String email, String password,
			String userType) {
		logger.info("Inside authenticateUser");
		BFSUser user = bfsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return ValidationConstants.USER_HAS_NOT_REGISTERED;
		}
		else{
			if(user.getUserId() != null){
				if(ValidatorUtils.validateUserEncryptedPassword(user.getPassword(),password)){
					if(StatusType.ACTIVE.getValue().equals(user.getStatus()))
						return ValidationConstants.USER_AUTHENTICATION_SUCCESSFUL;	
				}
				if(StatusType.LOCKED.getValue().equals(user.getStatus()))
					return ValidationConstants.USER_ACCOUNT_LOCKED;
				
				int failedAttempts = user.getUnsuccessfulAttempts();
				failedAttempts++;
				user.setUnsuccessfulAttempts(failedAttempts);
				if(ValidatorUtils.lockUserAccount(failedAttempts)){
					user.setStatus(StatusType.LOCKED.getValue());
				}
				bfsUserDataDAO.saveBfsUser(user);
			}
		}
		return ValidationConstants.USER_AUTHENTICATION_FAILED;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#updateUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String updateUser(String oldEmail, String newEmail,
			String oldPassword, String newPassword, String userType) {
		logger.info("Inside updateUser");
		BFSUser user = bfsUserDataDAO.doesUserExist(oldEmail, getSite(userType));
		if(user == null ){
			return ValidationConstants.USER_DOES_NOT_EXIST;
		}else{			
			if(ValidatorUtils.validateUserEncryptedPassword(user.getPassword(), oldPassword)){
				if(!oldEmail.equals(newEmail)){
					BFSUser checkNewUser = bfsUserDataDAO.doesUserExist(newEmail, getSite(userType));
					if(checkNewUser != null && !oldEmail.equals(newEmail)){
						return ValidationConstants.USER_ALREADY_EXISTS;
					}
					user.setPreviousEmail(oldEmail);
					user.setEmail(newEmail);
					if(!oldPassword.equals(newPassword)){
						user.setPassword(ValidatorUtils.encryptPassword(newPassword));
						bfsUserDataDAO.saveBfsUser(user);
						return ValidationConstants.USER_UPDATE_SUCCESSFUL;
					}
					bfsUserDataDAO.saveBfsUser(user);
					return ValidationConstants.USER_EMAIL_UPDATE_SUCCESSFUL;
				}
				else if(!oldPassword.equals(newPassword)){
					user.setPassword(ValidatorUtils.encryptPassword(newPassword));
					bfsUserDataDAO.saveBfsUser(user);
					return ValidationConstants.USER_PASSWORD_UPDATE_SUCCESSFUL;
				}				
			}
		}		
		return ValidationConstants.USER_UPDATE_FAILED;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#doesUserExist(java.lang.String, java.lang.String)
	 */
	public boolean doesUserExist(String email, String userType) {
		BFSUser user = bfsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user != null && user.getUserId() != null){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#resetUserPassword(java.lang.String, java.lang.String)
	 */
	public String resetUserPassword(String email, String userType) {
		logger.info("Inside resetUserPassword");
		BFSUser user = bfsUserDataDAO.doesUserExist(email, getSite(userType));

		if(user == null){
			return ValidationConstants.USER_DOES_NOT_EXIST;
		}else{
			int i = 0;
			String newPassword = com.bfrc.dataaccess.util.StringUtils.generatePassword(8);
			while(i < passwordResetAttempts){
				newPassword = com.bfrc.dataaccess.util.StringUtils.generatePassword(8);
				i++;
			}
			
			user.setPassword(ValidatorUtils.encryptPassword(newPassword));
			user.setUnsuccessfulAttempts(0);
			user.setStatus(StatusType.ACTIVE.getValue());
			bfsUserDataDAO.saveBfsUser(user);
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

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#doesUserDataExist(java.lang.String, java.lang.String)
	 */
	public String doesUserDataExist(String email, String userType) {
		logger.info("Inside doesUserDataExist");
		BFSUser user = bfsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return ValidationConstants.USER_DOES_NOT_EXIST;
		}else {
			if(user.getUserId() != null){
				MyBackupData userData = bfsUserDataDAO.getUserData(user);
				if(userData != null && userData.getJsonData() != null ){
					return ValidationConstants.USER_ALREADY_HAS_BACKUP_DATA;
				}
			}
		}
		return ValidationConstants.USER_HAS_NO_BACKUP_DATA;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#saveUserData(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String saveUserData(String jsonData, String email, String userType) {
		logger.info("Inside saveUserData");
		BFSUser user = bfsUserDataDAO.doesUserExist(email, getSite(userType));
		MyBackupData newUserData=null;
		if(user == null){
			return StringUtils.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
		}else{
			if(user.getUserId() != null){
				try {
					MyBackupData currentData = bfsUserDataDAO.getUserData(user);
					if(currentData != null && currentData.getJsonData().length() > 0){
						newUserData = currentData;
						newUserData.setJsonData(Hibernate.createClob(jsonData));
						newUserData.setDescFlag("B");
						newUserData.setLastModifiedDate(new java.util.Date());
					}else{
						newUserData = new MyBackupData(user, Hibernate.createClob(jsonData),"B");
					}

					bfsUserDataDAO.saveUserData(newUserData);
					logger.info("user data saved");
					MyBackupData retrieveData = bfsUserDataDAO.getUserData(user);

					JSONObject successJson = new JSONObject();
					SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy h:mm a");
					successJson.put("Message", ValidationConstants.USER_DATA_BACKUP_SUCCESSFUL);
					successJson.put("lastModifiedDesc", retrieveData.getDescFlag());
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

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#getUserData(java.lang.String, java.lang.String)
	 */
	public String getUserData(String email, String userType) {
		logger.info("Inside getUserData");
		BFSUser user = bfsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return StringUtils.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
		}else {
			try {
				if(user.getUserId() != null){
					MyBackupData userData = bfsUserDataDAO.getUserData(user);
					if(userData != null && userData.getJsonData() != null ){
						userData.setDescFlag("R");
						bfsUserDataDAO.saveUserData(userData);
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

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserDataService#getUserData(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getUserData(String email, String password, String userType) {
		logger.info("Inside retrieveUserData");
		BFSUser user = bfsUserDataDAO.doesUserExist(email, getSite(userType));
		if(user == null){
			return StringUtils.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
		}else {
			//match user password
			String userAuthResposne = authenticateUser(email, password, userType);
			if(ValidationConstants.USER_AUTHENTICATION_SUCCESSFUL.equalsIgnoreCase(userAuthResposne)){
				try {
					if(user.getUserId() != null){
						MyBackupData userData = bfsUserDataDAO.getUserData(user);
						if(userData != null && userData.getJsonData() != null ){
							userData.setDescFlag("R");
							bfsUserDataDAO.saveUserData(userData);
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
		return bfsUserDataDAO.getSite(webSiteName);
	}

}
