package com.bridgestone.bsro.aws.mobile.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bridgestone.bsro.aws.mobile.model.BFSUser;
import com.bridgestone.bsro.aws.mobile.model.SequenceNumber;
import com.bridgestone.bsro.aws.mobile.model.StatusType;
import com.bridgestone.bsro.aws.mobile.service.mail.Mailer;
import com.bridgestone.bsro.aws.mobile.util.ValidationConstants;
import com.bridgestone.bsro.aws.mobile.util.ValidationUtility;

public class MobileAppUserService {
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
	
	public String registerUser(DynamoDBMapper dynamoDBMapper, String appName, String email, String password) {
		try {
			
			if(!ValidationUtility.isEmailValid(email))
				return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
			
			if(!ValidationUtility.isPasswordValid(password))
				return ValidationUtility.responseToJson(ValidationConstants.USER_PASSWORD_INVALID);
			
			BFSUser bfsUser = getBFSUser(dynamoDBMapper, email, appName);
			
			if(bfsUser != null) {
				return "User Already Exist!";
			}
			
			Long userId = getUserId(dynamoDBMapper);
			BFSUser bfsUserNew = new BFSUser(userId, email, appName,
					ValidationUtility.encryptPassword(password),
					dateFormat.format(new Date()).toString().toUpperCase(), 
					email, "A",
					dateFormat.format(new Date()).toString().toUpperCase(), 
					0);
			
			dynamoDBMapper.save(bfsUserNew, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
			return "Success";
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return  "Exception Occured!";
		}
	}
	
	public String authenticateUser(DynamoDBMapper dynamoDBMapper, String appName, String email, String password) {
		try {
			if(!ValidationUtility.isEmailValid(email))
				return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
			
			if(!ValidationUtility.isPasswordValid(password))
				return ValidationUtility.responseToJson(ValidationConstants.USER_PASSWORD_INVALID);
			
			BFSUser bfsUser = getBFSUser(dynamoDBMapper, email, appName);
			if(bfsUser == null) {
				return ValidationUtility.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if(ValidationUtility.validateUserEncryptedPassword(bfsUser.getPassword(), password)) {
					if(StatusType.ACTIVE.getValue().equals(bfsUser.getStatus())) {
						return ValidationUtility.responseToJson(ValidationConstants.USER_AUTHENTICATION_SUCCESSFUL);
					}
				}
				
				if(StatusType.LOCKED.getValue().equals(bfsUser.getStatus())) {
					return ValidationUtility.responseToJson(ValidationConstants.USER_ACCOUNT_LOCKED);
				}
				
				int failedAttempts = bfsUser.getUnsuccessfulAttempts() + 1;
				bfsUser.setUnsuccessfulAttempts(failedAttempts);
				if(ValidationUtility.lockUserAccount(failedAttempts)) {
					bfsUser.setStatus(StatusType.LOCKED.getValue());
				}
				
				dynamoDBMapper.save(bfsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
			}
			return ValidationUtility.responseToJson(ValidationConstants.USER_AUTHENTICATION_FAILED);
			
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return  "Exception Occured!";
		}
	}
	
	public String doesUserDataExist(DynamoDBMapper dynamoDBMapper, String appName, String email) {
		try {
			if(!ValidationUtility.isEmailValid(email))
				return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
			
			BFSUser bfsUser = getBFSUser(dynamoDBMapper, email, appName);
			
			if(bfsUser == null) {
				return ValidationUtility.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if (bfsUser.getUserId() != null) {
					if(bfsUser != null && bfsUser.getJsonData() != null ) {
						return ValidationConstants.USER_ALREADY_HAS_BACKUP_DATA;
					}
				}
			}
			return ValidationConstants.USER_HAS_NO_BACKUP_DATA;
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return  "Exception Occured!";
		}
	}
	
	public String backupData(DynamoDBMapper dynamoDBMapper, String appName, String email, String data) {
		try {
			if(!ValidationUtility.isEmailValid(email))
				return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
			
			System.out.println(data);
			
			if(ValidationUtility.isNullOrEmpty(data))
				return ValidationUtility.responseToJson(ValidationConstants.INVALID_JSON_STRING);
			
			BFSUser bfsUser = getBFSUser(dynamoDBMapper, email, appName);
			
			if(bfsUser == null) {
				return ValidationUtility.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if (bfsUser.getUserId() != null) {
					bfsUser.setJsonData(data);
					bfsUser.setDescFlag("B");
					bfsUser.setJsonDataLastModifiedDate(dateFormat.format(new Date()).toString().toUpperCase());
					
					dynamoDBMapper.save(bfsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
					
					JSONObject successJson = new JSONObject();
					successJson.put("Message", ValidationConstants.USER_DATA_BACKUP_SUCCESSFUL);
					successJson.put("lastModifiedDesc", bfsUser.getDescFlag());
					//successJson.put("lastModifiedDate", new SimpleDateFormat("MM-dd-yyyy h:mm a").format(bfsUser.getJsonDataLastModifiedDate()));
					
					return successJson.toString();
				}
			}
			return ValidationUtility.responseToJson(ValidationConstants.USER_DATA_BACKUP_FAILED);
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return  "Exception Occured!";
		}
	}
	
	public String restoreData(DynamoDBMapper dynamoDBMapper, String appName, String email) {
		try {
			if(!ValidationUtility.isEmailValid(email))
				return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
			
			BFSUser bfsUser = getBFSUser(dynamoDBMapper, email, appName);
			
			if(bfsUser == null) {
				return ValidationUtility.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if (bfsUser.getUserId() != null) {
					if(bfsUser != null && bfsUser.getJsonData() != null ) {
						bfsUser.setDescFlag("R");
						dynamoDBMapper.save(bfsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
						return bfsUser.getJsonData();
					}
				}
			}
			return ValidationUtility.responseToJson(ValidationConstants.USER_DATA_RESTORE_ERROR);
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return  "Exception Occured!";
		}
	}
	
	public String forgotPassword(DynamoDBMapper dynamoDBMapper, String appName, String email) {
		try {
			if(!ValidationUtility.isEmailValid(email))
				return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
			
			BFSUser bfsUser = getBFSUser(dynamoDBMapper, email, appName);
			
			if(bfsUser == null) {
				return ValidationUtility.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				String newPassword = ValidationUtility.generatePassword(8);
				
				bfsUser.setPassword(ValidationUtility.encryptPassword(newPassword));
				bfsUser.setUnsuccessfulAttempts(0);
				bfsUser.setStatus(StatusType.ACTIVE.getValue());
				
				dynamoDBMapper.save(bfsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
				
				String emailFrom = Mailer.getDoNotReply(appName);
				String emailTo = email;
				String emailSubject = "Request for Password reset for your "+ Mailer.getSiteNameString(appName) + " account";
				String emailBody = Mailer.generateEmailBody(newPassword, appName);
				
				try {
					//send email to user with the new password
					Mailer.sendEmail(emailTo, emailFrom, emailSubject, emailBody);
				} catch (Exception e) {
					System.out.println("Error sending email to user: " + e.getMessage());
					return ValidationConstants.USER_PASSWORD_RESET_FAILED;
				}	
				
			}
			return ValidationUtility.responseToJson(ValidationConstants.USER_PASSWORD_RESET_SUCCESSFUL);
			
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return  "Exception Occured!";
		}
	}
	
	public String updateUser(DynamoDBMapper dynamoDBMapper, String appName, String oldEmail, String newEmail, String oldPassword, String newPassword) {
		try {
			if(!ValidationUtility.isEmailValid(oldEmail) || !ValidationUtility.isEmailValid(newEmail))
				return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_INVALID);
			
			if(!ValidationUtility.isPasswordValid(oldPassword) || !ValidationUtility.isPasswordValid(newPassword))
				return ValidationUtility.responseToJson(ValidationConstants.USER_PASSWORD_INVALID);
			
			BFSUser bfsUser = getBFSUser(dynamoDBMapper, oldEmail, appName);
			
			if(bfsUser == null) {
				return ValidationUtility.responseToJson(ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if(ValidationUtility.validateUserEncryptedPassword(bfsUser.getPassword(), oldPassword)) {
					if(!oldEmail.equals(newEmail)){
						// Get the user detail
						BFSUser bfsUserNew = getBFSUser(dynamoDBMapper, newEmail, appName);
						
						// Check the newEmail address is already registered
						if(bfsUserNew != null  && !oldEmail.equals(newEmail)) {
							return ValidationUtility.responseToJson(ValidationConstants.USER_ALREADY_EXISTS);
						}
						
						// Set the newEmail address
						bfsUser.setEmail(newEmail);
						bfsUser.setPreviousEmail(oldEmail);
						
						if(!oldPassword.equals(newPassword)){
							bfsUser.setPassword(ValidationUtility.encryptPassword(newPassword));
							dynamoDBMapper.save(bfsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
							return ValidationUtility.responseToJson(ValidationConstants.USER_UPDATE_SUCCESSFUL);
						}
						dynamoDBMapper.save(bfsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
						return ValidationUtility.responseToJson(ValidationConstants.USER_EMAIL_UPDATE_SUCCESSFUL);
					} else if(!oldPassword.equals(newPassword)) {
						bfsUser.setPassword(ValidationUtility.encryptPassword(newPassword));
						dynamoDBMapper.save(bfsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
						return ValidationUtility.responseToJson(ValidationConstants.USER_PASSWORD_UPDATE_SUCCESSFUL);
					}
				}
			}
			return ValidationUtility.responseToJson(ValidationConstants.USER_UPDATE_FAILED);
			
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return  "Exception Occured!";
		}
	}
	
	private Long getUserId(DynamoDBMapper dynamoDBMapper){
		
    	SequenceNumber keys = dynamoDBMapper.load(SequenceNumber.class, 1,new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT));
    	Long quoteId = keys.getUserId();
    	
    	keys.setUserId(quoteId+1);
    	
    	DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig(SaveBehavior.UPDATE);
    	dynamoDBMapper.save(keys, dynamoDBMapperConfig);
    	
    	return quoteId;
	}
	
	private BFSUser getBFSUser(DynamoDBMapper dynamoDBMapper, String email, String appName) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
	    eav.put(":email", new AttributeValue().withS(email));
	    eav.put(":userType", new AttributeValue().withS(appName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("email = :email AND userType = :userType").withExpressionAttributeValues(eav);
        
        ArrayList<BFSUser> bfsUser = new  ArrayList<BFSUser>(dynamoDBMapper.scan(BFSUser.class, scanExpression));
        
        if(bfsUser != null && !bfsUser.isEmpty() && bfsUser.size() > 0) {
        	return bfsUser.get(0);
        } else {
        	return null;
        }
	}

}
