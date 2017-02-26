/**
 * 
 */
package com.bsro.api.rest.ws.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;


/**
 * @author schowdhury
 *
 */
@Path(UserProfileWebService.URL)
public interface UserProfileWebService {
	
	public static final String URL = "/profile";
	
	/**
	 * <p>
	 * This service method:
	 * 1) accepts an user json data along with driver information as parameter<br/>
	 * 2) will set the driver email same as the user email if no separate driver email is present
	 * 3) checks for existing user using the email address provided and then proceed to save the user<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/user/add<br/>
	 * <b>Type: </b>POST
	 * @param data - user json string with at least one driver information.
	 * @return 1) if user does not exist, return "UserHasNotRegistered". <br/>
	 *         2) if user is added successfully, "UserSuccessfullyRegistered" is returned.
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/add")
	public BSROWebServiceResponse addUser( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@NotNull final String data);
	
	/**
	 * <p>
	 * This service method:
	 * 1) accepts an user email as parameter<br/>
	 * 2) checks for existing user return user json data with driver information<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/user/get<br/>
	 * <b>Type: </b>GET
	 * @param email - user email address
	 * @return 1) if user does not exist, return "UserDoesNotExist". <br/>
	 *         2) otherwise user json data is returned.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user/get")
	public BSROWebServiceResponse getUser(@NotNull @QueryParam("email") final String email,
						  @Context HttpServletRequest request,
						  @Context HttpHeaders headers);

	/**
	 * <p>
	 * This service method:
	 * 1) accepts an user json data along with existing driver information as parameter<br/>
	 * 2) checks for existing user using the email address provided and then proceed to update the user<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/user/update<br/>
	 * <b>Type: </b>PUT
	 * @param data - user json string with at least one driver information.
	 * @return 1) if user does not exist, return "UserHasNotRegistered". <br/>
	 *         2) if user is added successfully, "UserUpdateSuccess" is returned.
	 */
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/update")
	public BSROWebServiceResponse updateUser( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@NotNull final String data);
	
	/**
	 * <p>
	 * This service method:
	 * 1) accepts an user email as parameter<br/>
	 * 2) checks for existing user remove user from database<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/user/remove<br/>
	 * <b>Type: </b>DELETE
	 * @param email - user email address
	 * @return 1) if user does not exist, return "UserDoesNotExist". <br/>
	 *         2) otherwise "UserRemoveSuccess" is returned.
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/remove")
	public BSROWebServiceResponse removeUser(@NotNull @QueryParam("email") final String email,
			@Context HttpHeaders headers, 
			@Context HttpServletRequest request);
	
	/**
	 * <p>
	 * This service method:
	 * 1) accepts an user's email and password as parameter so that the user can be added to the DB.<br/>
	 * 2) validates input parameters so to check for correct email format and password.<br/>
	 * 3) checks for existing user using the email address provided and then proceed matching the password<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/user/auth<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user
	 * @param password - password chosen by user
	 * @return 1) if user does not exist, return "UserHasNotRegistered". <br/>
	 *         2) if password is correct and return "UserAuthSuccess" otherwise return "UserAuthFailed".
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/auth/email")
	public BSROWebServiceResponse authenticateUser(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email,
			@QueryParam("authString") final String password);	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/auth/fb")
	public BSROWebServiceResponse authenticateUserWithFB(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("appId") final String appId,
			@QueryParam("redirectUri") final String redirectUri);	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/auth/google")
	public BSROWebServiceResponse authenticateUserWithGoogle(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("code") final String authCode,
			@QueryParam("redirectUri") final String redirectUri);	
	
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
	 * <b>URL: </b>/{URI}/profile/user/auth/update<br/>
	 * <b>Type: </b>PUT
	 * @param oldEmail - old email address of user
	 * @param newEmail - new email address of user
	 * @param oldPassword - old password of user
	 * @param newPassword - new password of user
	 * @return 1) "UserEmailUpdateSuccess" when the email update process is successful.<br/>
     * 		   2) "UserPwdUpdateSuccess" when the password update process is successful.<br/>
     * 		   3) "UserUpdateSuccess" when both email and password is successfully updated.<br/>
     * 		   4) "UserNoUpdate" if anything above fails.<br/>
	 */
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/auth/update")
	public BSROWebServiceResponse updateUser(@Context HttpHeaders headers,
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
	 * <b>URL: </b>/{URI}/profile/user/forgot-password<br/>
	 * <b>Type: </b>POST
	 * @param email - email address of user<br/>
	 * @return 1) "ResetPwdSuccess" string if the reset is successful.<br/>
     * 		   2) "ResetPwdServerError" string if the reset is somehow unsuccessful.<br/>
     * 		   3) "UserNotExist" if email address passed is not found in persistent storage.<br/> 
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/forgot-password")
	public BSROWebServiceResponse forgotPassword(@Context HttpHeaders headers,
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
	 * <b>URL: </b>/{URI}/profile/user/check-data<br/>
	 * <b>Type: </b>GET
	 * @param email - email address of user<br/>
	 * @return 1) "NoBackupFound" string if user has no backup data<br/>
     * 		   2) "YesBackupFound" string if user has backup data<br/>
     * 		   3) "UserNotExist" if email address passed is not found in persistent storage.<br/> 
	 */		
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/check-data")
	public BSROWebServiceResponse dataExists(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method:<br/>
	 * 1) accepts two fields, email field and a long string containing JSON data.<br/>
     * 2) stores the exact JSON data into persistent storage including the user's ID.<br/>
     * 3) stores the DB timestamp to the last_modified_date field whenever an update operation is written to the backup table.<br/>
     * 4) stores the "B" into the LAST_MODIFIED_DESC so that we know that the user has backed up his/her stuff. 
     *    In the future we may have "S" for sync feature.<br/>
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/backup/data<br/>
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
	@Path("/backup/data")
	public BSROWebServiceResponse backupData(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@FormParam("email") final String email,
			@FormParam("data") final String data);
	
	/**
	 * <p>
	 * This service method<br/>
	 * accepts an email address and return module and json_data all together in 1 JSON string to the client.
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/backup/restore<br/>
	 * <b>Type: </b>GET
	 * @param email - email address of user
	 * @return  1) a string "RestoreError" for any error.<br/>
	 * 			2) user backup json data string to the client, if there is no error.<br/>
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/backup/restore")
	public BSROWebServiceResponse restoreData(@Context HttpHeaders headers,
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
	 * <b>URL: </b>/{URI}/profile/backup/restore-secure<br/>
	 * <b>Type: </b>GET
	 * @param email - email address of user
	 * @param password - password of user
	 * @return  1) if authentication fails, then the corresponding authentication error is returned.<br/>
	 * 			2) a string "RestoreError" for any error.<br/>
	 * 			3) user backup json data string to the client, if there is no error.<br/>
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/backup/restore-secure")
	public BSROWebServiceResponse restoreSecuredData(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@QueryParam("email") final String email,
			@QueryParam("authString") final String password);
	
	/**
	 * <p>
	 * This service method<br/>
	 * accepts an user json string with additional driver information to save
	 * If driver email is not specified, it is set from the user email if not already used.
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/user/driver/add<br/>
	 * <b>Type: </b>POST
	 * @param data - user json data
	 * @return  1) if user does not exist, "UserNotExist" is returned.<br/>
	 * 			2) otherwise, "DriverSuccessfullyAdded" is returned <br/>
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/user/driver/add")
	public BSROWebServiceResponse registerAdditionalDriver( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@NotNull final String data);
	
	/**
	 * <p>
	 * This service method<br/>
	 * registers a new vehicle for the current driver  all the vehicle 
	 * details from the user year, make, model, submodel and retrieves  
	 * the fitment data from the database and add to user vehicle table
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/vehicle/add<br/>
	 * <b>Type: </b>POST
	 * @param email   - email of user
	 * @param year     - driver vehicle year
	 * @param make     - driver vehicle make
	 * @param model    - driver vehicle model
	 * @param submodel - driver vehicle sub-model (optional)
	 * @return  1) if driver is not found, "DriverDoesNotExist" is returned.<br/> 
	 * 			2) if vehicle fitment data does not exist, "VehicleFitmentDataNotFound" is returned.<br/>
	 * 			3) otherwise, "VehicleSaveSuccess" is returned <br/>
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/vehicle/add")
	public BSROWebServiceResponse addVehicle( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email, final String data);
	/**
	 * <p>
	 * This service method<br/>
	 * accepts an user email and return all the vehicles for the user for all the drivers
	 * associated with it.
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/vehicle/get/all<br/>
	 * <b>Type: </b>GET
	 * @param email   - email of the user
	 * @return  1) if user is not found, "UserDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, a vehicle json data is returned
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/get/all")
	public BSROWebServiceResponse getVehicles(@NotNull @QueryParam("email") final String email,
										  @Context HttpServletRequest request,
										  @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves an particular vehicle for a driver based on the aces vehicle id
	 * associated with it.
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/get<br/>
	 * <b>Type: </b>GET
     * 
     * @param email - email address of user
	 * @param acesVehicleId  - acesVehicle Id from ACES vehicle model
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, a vehicle json data is returned
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/get")
	public BSROWebServiceResponse getVehicle(@QueryParam("email") final String email,
							 @NotNull @QueryParam("acesId") final String acesVehicleId,
							 @QueryParam("vin") String vinNumber,
							 @Context HttpServletRequest request,
							 @Context HttpHeaders headers);
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves an particular vehicle for a driver based on the aces vehicle id
	 * associated with it.
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/update<br/>
	 * <b>Type: </b>PUT
     * 
	 * @param email    - email of the driver
	 * @param data - vehicle json data
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, "VehicleUpdateSuccessful" is returned.
	 */
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/vehicle/update")
	public BSROWebServiceResponse updateVehicle( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email, final String data);
	
	/**
	 * <p>
	 * This service method<br/>
	 * removes an particular vehicle associated to a driver based on the base vehicle id
	 * and submodel passed as parameter.
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/remove<br/>
	 * <b>Type: </b>DELETE
     * 
	 * @param email
	 * @param baseVehicleId
	 * @param submodel
	 * @return  1) if driver is not found, "DriverDoesNotExist" is returned.<br/> 
	 *          2) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/>
	 * 			3) otherwise, "VehicleRemoveSuccess" is returned.
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/remove")
	public BSROWebServiceResponse removeVehicle(@QueryParam("email") final String email,
							@NotNull @QueryParam("acesId") final String acesVehicleId,
							@QueryParam("vin") final String vinNumber,
							@Context HttpServletRequest request,
							@Context HttpHeaders headers);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/config/get")
	public BSROWebServiceResponse getVehicleConfigOptions(@QueryParam("email") final String email,
							@NotNull @QueryParam("acesId") final String acesVehicleId,
							@Context HttpHeaders headers);
	/**
	 * <p>
	 * This service method<br/>
	 * adds an appointment to the user appointment table for a vehicle and its
	 * associated driver. The user is also saved for the appointment
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/appointment/add<br/>
	 * <b>Type: </b>POST
     * 
	 * @param email
	 * @param acesVehicleId
	 * @param appointmentId
	 * @return  1) if driver is not found, "DriverDoesNotExist" is returned.<br/> 
	 *          2) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/>
	 * 			2) if appointment is not found, "AppointmentDoesNotExist" is returned.<br/>
	 *          4) otherwise, "AppointmentSaveSuccess" is returned.
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/vehicle/appointment/add")
	public BSROWebServiceResponse saveAppointment( @Context HttpHeaders headers, 
					@Context HttpServletRequest request,
					@NotNull @QueryParam("email") final String email,
					@NotNull @QueryParam("acesId") final String acesVehicleId,
					@NotNull @QueryParam("vin") final String vinNumber,
					@NotNull @QueryParam("apptId") final String appointmentId);
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves the user maintenance history from the database. 
	 * Both NCD and customer personal maintenance data for the vehicle will be retrieved.
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/history/get<br/>
	 * <b>Type: </b>GET
     * 
	 * @param email
	 * @param acesVehicleId
	 * @return  1) "VehicleDoesNotExist" if no vehicle is found<br/>
	 *          2) "NoDataExist" if no vehicle history data is found<br/>
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/vehicle/history/get")
	public BSROWebServiceResponse getUserVehicleMaintenanceHistory(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email,
			@NotNull @QueryParam("acesId") String acesVehicleId,
			@QueryParam("vin") String vinNumber);
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves the user maintenance history from the database. 
	 * Both NCD and customer personal maintenance data for the vehicle will be retrieved.
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/history/save<br/>
	 * <b>Type: </b>GET
     * 
	 * @param email - vehicle driver email
	 * @param jsonData - history data from NCD service along with vehicle details like
	 *                   baseVehicleId and sub-model
	 * @param sinceDate - the date used to retrieve the NCD data for the vehicle. if nothing passed
	 *                    the current date time will be used as download date.
	 * @return 1) "VehicleDoesNotExist" if no vehicle is found<br/>
	 *         2) "InvalidVehicleData" if there is an issue parsing the data<br/>
	 *         3) "vehicleHistorySaved" if data is successfully saved
	 *         4) "AcesIdAndHistoryDataDonotMatch" if the acesId passed doesn't match with the
	 *                vehicle data coming from NCD
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/vehicle/history/save")
	public BSROWebServiceResponse saveUserVehicleMaintenanceHistory(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email,
			@QueryParam("sinceDt") final String sinceDate, 
			@QueryParam("acesId") final String acesVehicleId, 
			@QueryParam("vin") final String vinNumber, 
			final String jsonData);
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves an particular vehicle gas fill up details for a user vehicle
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/gasfillup/get<br/>
	 * <b>Type: </b>GET
     * 
     * @param email - email address of user
	 * @param acesVehicleId  - acesVehicle Id from ACES vehicle model
	 * @param vin  - VIN number of vehicle (if present)
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, an array of vehicle gas fill up JSON data is returned
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/gasfillup/get")
	public BSROWebServiceResponse getVehicleGasFillUp(@QueryParam("email") final String email,
							 @NotNull @QueryParam("acesId") final String acesVehicleId,
							 @QueryParam("vin") String vinNumber,
							 @Context HttpServletRequest request,
							 @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This service method<br/>
	 * save a Vehicle gas fill up details
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/gasfillup/save<br/>
	 * <b>Type: </b>POST
     * 
     * @param email - email address of user
	 * @param acesVehicleId  - acesVehicle Id from ACES vehicle model
	 * @param vin  - VIN number of vehicle (if present)
	 * @param data  - json data with all the gas fill up details
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, "VehicleGasSaved" is returned
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/gasfillup/save")
	public BSROWebServiceResponse saveVehicleGasFillUp(@QueryParam("email") final String email,
							 @NotNull @QueryParam("acesId") final String acesVehicleId,
							 @QueryParam("vin") String vinNumber,
							 final String data,
							 @Context HttpServletRequest request,
							 @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This service method<br/>
	 * updates a Vehicle gas fill up details
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/gasfillup/update<br/>
	 * <b>Type: </b>POST
     * 
     * @param email - email address of user
	 * @param acesVehicleId  - acesVehicle Id from ACES vehicle model
	 * @param vin  - VIN number of vehicle (if present)
	 * @param data  - JSON data with all the gas fill up details
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, "VehicleGasSaved" is returned
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/gasfillup/update")
	public BSROWebServiceResponse updateVehicleGasFillUp(@QueryParam("email") final String email,
							 @NotNull @QueryParam("acesId") final String acesVehicleId,
							 @QueryParam("vin") String vinNumber,
							 final String data,
							 @Context HttpServletRequest request,
							 @Context HttpHeaders headers);
	
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves an particular vehicle maintenance details for a user vehicle
	 * for all non-FCAC stores. This is data entered by user for tracking purposes.
	 * The maintenance services performed in FCAC stores are returned via the
	 * service/history end point.
	 * 
     * </p>
	 * <b>URL: </b>/{URI}/vehicle/performed/service/get<br/>
	 * <b>Type: </b>GET
     * 
     * @param email - email address of user
	 * @param acesVehicleId  - acesVehicle Id from ACES vehicle model
	 * @param vin  - VIN number of vehicle (if present)
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, an array of maintenance service performed data is returned
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/performed/service/get")
	public BSROWebServiceResponse getMaintenanceServicePerformed(@QueryParam("email") final String email,
							 @NotNull @QueryParam("acesId") final String acesVehicleId,
							 @QueryParam("vin") String vinNumber,
							 @Context HttpServletRequest request,
							 @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This service method<br/>
	 * save a user entered vehicle maintenance performed in a non-FCAC store.
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/performed/service/save<br/>
	 * <b>Type: </b>POST
     * 
     * @param email - email address of user
	 * @param acesVehicleId  - acesVehicle Id from ACES vehicle model
	 * @param vin  - VIN number of vehicle (if present)
	 * @param data  - JSON data with all the gas fill up details
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, "ServicePerformedSaved" is returned
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/performed/service/save")
	public BSROWebServiceResponse saveMaintenanceServicePerformed(@QueryParam("email") final String email,
							 @NotNull @QueryParam("acesId") final String acesVehicleId,
							 @QueryParam("vin") String vinNumber,
							 final String data,
							 @Context HttpServletRequest request,
							 @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This service method<br/>
	 * save a user entered vehicle maintenance performed in a non-FCAC store.
     * </p>
	 * <b>URL: </b>/{URI}/profile/vehicle/performed/service/update<br/>
	 * <b>Type: </b>POST
     * 
     * @param email - email address of user
	 * @param acesVehicleId  - acesVehicle Id from ACES vehicle model
	 * @param vin  - VIN number of vehicle (if present)
	 * @param data  - JSON data with all the gas fill up details
	 * @return  1) if vehicle is not found, "VehicleDoesNotExist" is returned.<br/> 
	 * 			2) otherwise, "ServicePerformedSaved" is returned
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/vehicle/performed/service/update")
	public BSROWebServiceResponse updateMaintenanceServicePerformed(@QueryParam("email") final String email,
							 @NotNull @QueryParam("acesId") final String acesVehicleId,
							 @QueryParam("vin") String vinNumber,
							 final String data,
							 @Context HttpServletRequest request,
							 @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This service method<br/>
	 * removes a user entered service performed
	 * </p>

	 * <b>URL: </b>/{URI}/profile/vehicle/performed/service/remove<br/>
	 * <b>Type: </b>DELETE
	 * 
	 * @param email
	 * @param data
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "DeviceDoesNotExist" if the device does not exist
	 *         3) "DeviceSuccessfullyRemoved" if device is removed successfully
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/vehicle/performed/service/remove")
	public BSROWebServiceResponse removeMaintenanceServicePerformed(@QueryParam("email") final String email,
			@QueryParam("id") final String id, @Context HttpHeaders headers,
			@Context HttpServletRequest request);
	
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves the favorite store information for a particular driver
     * </p>
	 * <b>URL: </b>/{URI}/profile/store/get<br/>
	 * <b>Type: </b>GET
	 * 
	 * @param email			- email of user
	 * @return 1) "UserDoesNotExist" if driver does not exist
	 *         2) "StoreDoesNotExist" if store is not found in the database of the user type
	 *         3) a json string with the store information, if successful
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/store/get")
	public BSROWebServiceResponse getFavouriteStores(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email);


	/**
	 * <p>
	 * This service method<br/>
	 * save a favorite store for the user
     * </p>
	 * <b>URL: </b>/{URI}/profile/store/save<br/>
	 * <b>Type: </b>GET
	 * 
	 * @param email
	 * @param storeNumber
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "StoreDoesNotExist" if store is not found in the database of the user type
	 *         3) "StoreSaveSuccess" if successfully saved
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/store/save")
	public BSROWebServiceResponse saveFavouriteStore(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email,
			@NotNull @QueryParam("storeNumber") final String storeNumber);
	
	/**
	 * <p>
	 * This service method<br/>
	 * updates the distance to the store when user address changes
     * </p>
	 * <b>URL: </b>/{URI}/profile/store/update<br/>
	 * <b>Type: </b>PUT
	 * 
	 * @param email
	 * @param storeNumber
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "StoreDoesNotExist" if store is not found in the database of the user type
	 *         3) "StoreSaveSuccess" if successfully saved
	 *         4) "AddressInvalid" if the user address or store adsress is not a valid geo-location.
	 */
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/store/update")
	public BSROWebServiceResponse updateStoreDistance(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email,
			@NotNull @QueryParam("storeNumber") final String storeNumber);
	
	/**
	 * <p>
	 * This service method<br/>
	 * removes a favorite store for the user
     * </p>
	 * <b>URL: </b>/{URI}/profile/store/remove<br/>
	 * <b>Type: </b>DELETE
	 * 
	 * @param email
	 * @param storeNumber
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "StoreDoesNotExist" if store is not saved as a favorite store
	 *         3) "StoreSaveSuccess" if successfully saved
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/store/remove")
	public BSROWebServiceResponse removeFavouriteStore(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email,
			@NotNull @QueryParam("storeNumber") final String storeNumber);
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves favorite promotions for a user
     * </p>
	 * <b>URL: </b>/{URI}/profile/promotion/get<br/>
	 * <b>Type: </b>GET
	 * 
	 * @param email
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) Otherwise, a json with user promotion data 
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/promotion/get")
	public BSROWebServiceResponse getUserPromotions(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method<br/>
	 * saves a favorite promotions for a user in the user promotion table
     * </p>
	 * <b>URL: </b>/{URI}/profile/promotion/save<br/>
	 * <b>Type: </b>POST
	 * 
	 * @param promotionId
	 * @param email
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "PromotionDoesNotExist" if no such promotion exist currently
	 *         3) "PromotionSaveSuccess" when promotion successfully saved
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/promotion/save")
	public BSROWebServiceResponse saveFavouritePromotion(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("pId") final String promotionId,
			@NotNull @QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method<br/>
	 * saves a favorite promotions for a user in the user promotion table
     * </p>
	 * <b>URL: </b>/{URI}/profile/promotion/remove<br/>
	 * <b>Type: </b>DELETE
	 * 
	 * @param promotionId
	 * @param email
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "PromotionDoesNotExist" if no such promotion exist currently
	 *         3) "PromotionRemoveSuccess" when promotion successfully removed
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/promotion/remove")
	public BSROWebServiceResponse removeFavouritePromotion(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("pId") final String promotionId,
			@NotNull @QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method<br/>
	 * saves a favorite promotions for a user in the user promotion table
     * </p>
	 * <b>URL: </b>/{URI}/profile/quote/get<br/>
	 * <b>Type: </b>GET
	 * 
	 * @param email
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) a json with the user quote information when successful
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/quote/get/all")
	public BSROWebServiceResponse getUserQuotes(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email);

	/**
	 * <p>
	 * This service method<br/>
	 * saves a user favorite product quote to the user quote table.
     * </p>
	 * <b>URL: </b>/{URI}/profile/quote/save<br/>
	 * <b>Type: </b>POST
	 * 
	 * @param quoteId - id of the quote
	 * @param type - type of product (ex- battery, tire, oil change etc)
	 * @param email - user email
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         3) "QuoteDoesNotExist" when quote does not exist in the system
	 *         2) "QuoteSaveSuccess" when quote is successfully save
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/quote/save")
	public BSROWebServiceResponse saveUserQuote(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("quoteId") final String quoteId,
			@NotNull @QueryParam("type") final String type,
			@NotNull @QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method<br/>
	 * removes a user favorite from the user table.
     * </p>
	 * <b>URL: </b>/{URI}/profile/quote/remove<br/>
	 * <b>Type: </b>DELETE
	 * 
	 * @param quoteId
	 * @param type
	 * @param email
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         3) "QuoteDoesNotExist" when quote does not exist in the system
	 *         2) "QuoteRemoveSuccess" when quote is successfully deleted
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/quote/remove")
	public BSROWebServiceResponse removeUserQuote(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("quoteId") final String quoteId,
			@NotNull @QueryParam("type") final String type,
			@NotNull @QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method<br/>
	 * save an image for a particular product. If there is an existing image
	 * it is overwritten. 
	 * </p>

	 * <b>URL: </b>/{URI}/profile/image/{type}/{productId}/upload
	 * <b>Type: </b>PUT
	 * 
	 * @param imageType ex- vehicle, device, driver
	 * @param imageProductId
	 * @param image - raw binary image data as a byte array
	 * @return 1) "ImageInvalidType" if image type is invalid
	 *         2) "ImageSizeTooLong" if the size is above 15 mb
	 *         2) "ImageSaveError" if there is an issue trying to save an image
	 *         3) "ImageSuccessfullySaved" if successful
	 */
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/image/{type}/{productId}/upload")
	public BSROWebServiceResponse uploadImage(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @PathParam("type") final String imageType,
			@NotNull @PathParam("productId") final String imageProductId,
			@NotNull final String image);
	
	/**
	 * <p>
	 * This service method<br/>
	 * deletes an image for a particular product
	 * </p>

	 * <b>URL: </b>/{URI}/profile/image/{type}/{productId}/remove
	 * <b>Type: </b>DELETE
	 * @param imageType
	 * @param imageProductId
	 * @return "ImageDoesNotExist" if image is not found
	 *         "ImageRemoveSuccess" if image is successfully deleted
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/image/{type}/{productId}/remove")
	public BSROWebServiceResponse removeImage(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @PathParam("type") final String imageType,
			@NotNull @PathParam("productId") final String imageProductId);
	
	/**
	 * <p>
	 * This service method<br/>
	 * retrieves an image for a particular product. Images can be 
	 * saved for vehicles, devices and drivers.
     * </p>
     * 
	 * <b>URL: </b>/{URI}/profile/image/{type}/{productId}/get
	 * <b>Type: </b>GET
	 * 
	 * @param imageType 
	 * @param imageProductId
	 * @return 1) "ImageDoesNotExist" if no image found.<br/>
	 *         2) raw binary data of the image if found
	 */
	@GET
	@Produces("image/jpeg")
	@Path("/image/{type}/{productId}/get")
	public byte[] getImage(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @PathParam("type") final String imageType,
			@NotNull @PathParam("productId") final String imageProductId);	
	
	/**
	 * <p>
	 * This service method<br/>
	 * returns a JSON array of all devices that user has registered to access application
     * </p>
	 * <b>URL: </b>/{URI}/profile/device/get<br/>
	 * <b>Type: </b>GET
	 * 
	 * @param email
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) a JSON with the registered devices for the user
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/device/get")
	public BSROWebServiceResponse getUserDevices(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email);
	
	/**
	 * <p>
	 * This service method<br/>
	 * registers a new device for the user to the database. An unique
	 * identifier needs to be sent for each device along with JSON data.
	 * </p>

	 * <b>URL: </b>/{URI}/profile/device/add
	 * <b>Type: </b>POST
	 * 
	 * @param email - user email
	 * @param data - a json data with the device information
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "DeviceSuccessfullySaved" if device is saved successfully
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/device/add")
	public BSROWebServiceResponse registerNewDevice(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email, final String data);
	/**
	 * <p>
	 * This service method<br/>
	 * updates a new device for the user to the database. 
	 * </p>

	 * <b>URL: </b>/{URI}/profile/device/update
	 * <b>Type: </b>PUT
	 * 
	 * @param email - user email
	 * @param data - a json data with the new device information
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "DeviceSuccessfullyUpdated" if device is updated successfully
	 */
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/device/update")
	public BSROWebServiceResponse updateDevice(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email, final String data);
	/**
	 * <p>
	 * This service method<br/>
	 * removes a new device from the database
	 * </p>

	 * <b>URL: </b>/{URI}/profile/device/remove
	 * <b>Type: </b>PUT
	 * 
	 * @param email
	 * @param data
	 * @return 1) "UserDoesNotExist" if user does not exist
	 *         2) "DeviceDoesNotExist" if the device does not exist
	 *         3) "DeviceSuccessfullyRemoved" if device is removed successfully
	 */
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/device/remove")
	public BSROWebServiceResponse removeDevice(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@NotNull @QueryParam("email") final String email,
			@NotNull @QueryParam("id") final String deviceId);
	
	
}
