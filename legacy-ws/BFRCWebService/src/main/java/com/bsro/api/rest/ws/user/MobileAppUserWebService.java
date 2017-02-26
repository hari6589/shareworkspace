/**
 * 
 */
package com.bsro.api.rest.ws.user;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * @author schowdhury
 *
 */
@Path(MobileAppUserWebService.URL)
public interface MobileAppUserWebService {
	public static final String URL = "/mobile-app-user";
	
	/** 
	 * <p>
	 * This service method: </br>           
	 * 1) validates input parameters so to check for correct email format and password constraints.<br/>
	 * 2) looks up email in persistent storage for existence.<br/>
	 * 3) creates user into persistent storage and uses passed  parameters (email string, password string, user_type string) 
	 *    in the DB fields. Add date should also be injected.</br>
	 * 4) uses the AppName header string to determine the user_type field in the DB.</br>
	 * </p>
	 * 
	 * <b>URL: </b>/{URI}/mobile-app-user/register<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user
	 * @param password - password chosen by user
	 * @return 1)"UserRegistrationSuccess" as respond string as a respond once the create process is successful.</br>
	 * 		   2)"UserExist" as respond string if found in persistence storage.
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/register")
	public String registerUser( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@QueryParam("email") final String email,
			@QueryParam("authString") final String password);
	
	/**
	 * <p>
	 * This service method:
	 * 1) accepts an user's email and password as parameter so that the user can be added to the DB.<br/>
	 * 2) validates input parameters so to check for correct email format and password.<br/>
	 * 3) checks for existing user using the email address provided and then proceed matching the password<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/mobile-app-user/authenticate<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user
	 * @param password - password chosen by user
	 * @return 1) if user does not exist, return "UserHasNotRegistered". <br/>
	 *         2) if password is correct and return "UserAuthSuccess" otherwise return "UserAuthFailed".
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/authenticate")
	public String authenticateUser(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email,
			@QueryParam("authString") final String password);	

	/**
	 * <p>
	 * This service method:
	 * 1) accepts 4 parameters such as newEmail, oldEmail, newPasswd, oldPasswd.<br/>
	 * 2) updates the user's email/password from an old email/password to a new email/password. <br/>
	 * 3) only allows if supplied password matches current password.<br/>
	 * 4) validates input parameters so to check for correct email format.<br/>
     * 5) updates the user's password to newPasswd. Only allowed if supplied password matches the current password.<br/>
     * 6) updates email and password all at once and return "UserUpdateSuccess".<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/mobile-app-user/update<br/>
	 * <b>Type: </b>POST
	 * @param oldEmail - old email address of user
	 * @param newEmail - new email address of user
	 * @param oldPassword - old password of user
	 * @param newPassword - new password of user
	 * @return 1) "UserEmailUpdateSuccess" when the email update process is successful.<br/>
     * 		   2) "UserPwdUpdateSuccess" when the password update process is successful.<br/>
     * 		   3) "UserUpdateSuccess" when both email and password is successfully updated.<br/>
     * 		   4) "UserNoUpdate" if anything above fails.<br/>
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/update")
	public String updateUser(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("oEmail") final String oldEmail,
			@QueryParam("nEmail") final String newEmail,
			@QueryParam("oAuthString") final String oldPassword,
			@QueryParam("nAuthString") final String newPassword);
	
	/**
	 * <p>
	 * This service method:<br/>
     * 1) accepts email address as parameter.<br/>
     * 2) validates input parameters so to check for correct email format.<br/>
 	 * 3) checks if email address passed exist in persistent storage.<br/>
     * 4) creates a random password and email it to the user if the email address passed exist in persistent storage.<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/mobile-app-user/forgot-password<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user<br/>
	 * @return 1) "ResetPwdSuccess" string if the reset is successful.<br/>
     * 		   2) "ResetPwdServerError" string if the reset is somehow unsuccessful.<br/>
     * 		   3) "UserNotExist" if email address passed is not found in persistent storage.<br/> 
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/forgot-password")
	public String forgotPassword(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method:<br/>
     * 1) accepts email address as parameter.<br/>
     * 2) validates input parameters so to check for correct email format.<br/>
 	 * 3) checks if email address passed exist in persistent storage.<br/>
     * 4) checks whether already has backup data<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/mobile-app-user/check-data<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user<br/>
	 * @return 1) "Empty" string if user has no backup data<br/>
     * 		   2) "Backup" string if user has backup data<br/>
     * 		   3) "UserNotExist" if email address passed is not found in persistent storage.<br/> 
	 */		
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/check-data")
	public String dataExists(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method:<br/>
	 * 1) accepts two fields, email field and a long string containing JSON data.<br/>
     * 2) stores the exact JSON data into persistent storage including the user's ID.<br/>
     * 3) stores the DB timestamp to the last_modified_date field whenever an update operation is written to the backup table.<br/>
     * 4) stores the "B" into the LAST_MODIFIED_DESC so that we know that the user has backed up his/her stuff. In the future we may have "S" for sync feature.<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/mobile-app-user/backup<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user
	 * @param data - json data 
	 * @return  1) return "BackupFailed" if backup is unsuccessful.<br/>
     * 			2) return a JSON string containing "BackupSuccess", LAST_MODIFIED_DESC and 
     * 			   LAST_MODIFIED_DATE field when the DB row is successfully created.<br/>
     * 			3) check if a user has backup data or is empty and return the string "Backup" 
     * 			   or "Empty" respectively to the client.<br/>
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/backup")
	public String backupData(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@FormParam("email") final String email,
			@FormParam("data") final String data);
	
	/**
	 * <p>
	 * This service method<br/>
	 * accepts an email address and return module and json_data all together in 1 JSON string to the client.
     * </p>
     * 
	 * <b>URL: </b>/{URI}/mobile-app-user/restore<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user
	 * @return  1) a string "RestoreError" for any error.<br/>
	 * 			2) module and JSON_DATA all together in 1 JSON string to the client, if there is no error.<br/>
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/restore")
	public String restoreData(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email);
	
	
	/**
	 * <p>
	 * This service method<br/>
	 * accepts an email address, password and return module and json_data 
	 * all together in 1 JSON string to the client after authenticating the user password.
	 *  
     * </p>
     * 
	 * <b>URL: </b>/{URI}/mobile-app-user/restore-secure<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user
	 * @param password - password of user
	 * @return  1) if authentication fails, then the corresponding authentication error is returned.<br/>
	 * 			2) a string "RestoreError" for any error.<br/>
	 * 			3) module and JSON_DATA all together in 1 JSON string to the client, if there is no error.<br/>
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/restore-secure")
	public String restoreSecuredData(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email,
			@QueryParam("authString") final String password);

}
