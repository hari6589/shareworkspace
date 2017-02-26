/**
 * 
 */
package com.bsro.api.rest.ws.notification;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author schowdhu
 *
 */
@Path(NotificationWebService.URL)
public interface NotificationWebService {
	public static final String URL = "/notification";
	
	/** 
	 * <p>
	 * This service method: </br>           
	 * Returns all current notifications for a new device that has not expired.
	 * This can be used by the native apps after initial install or every update.
	 * </p>
	 * 
	 * <b>URL: </b>/{URI}/notification/getall<br/>
	 * <b>Type: </b>GET
	 * @param deviceUUID - the device UUID
	 * @return 1) a JSON array string of notifications
	 * 		   2) "NoNotificationFound" if no notification is found
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/getall")
	public BSROWebServiceResponse getAllNotifications( @Context HttpHeaders headers, 
			@Context HttpServletRequest request);
	
	/** 
	 * <p>
	 * This service method: </br>           
	 * Returns a list of notifications in a JSON array given a device UUID<br/>
	 * This can be used the native applications to show the messages<br/>
	 * on the messages folder.
	 * </p>
	 * 
	 * <b>URL: </b>/{URI}/notification/get<br/>
	 * <b>Type: </b>GET
	 * @param deviceUUID - the device UUID
	 * @return 1) a JSON array string of notifications
	 * 		   2) "NoNotificationFound" if no notification is found for the device.
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/get")
	public BSROWebServiceResponse getNotifications( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@QueryParam("deviceUUID") final String deviceId);
	
	/** 
	 * <p>
	 * This service method: </br>           
	 * Updates a device message if user chooses to pin the message or click the
	 * message which means the message has been read.
	 * </p>
	 * 
	 * <b>URL: </b>/{URI}/notification/update<br/>
	 * <b>Type: </b>PUT
	 * @param messageJson - the device message JSON string
	 * @return 1) "UpdateSuccess" if the update is successful
	 * 		   2) "UpdateFailed" if there an error with update
	 */	
	@PUT
	@Consumes
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/update")
	public BSROWebServiceResponse updateNotification( @Context HttpHeaders headers, 
			@Context HttpServletRequest request, final String messageJson);
	/** 
	 * <p>
	 * This service method: </br>           
	 * Updates the delete flag to 'Y' in database if user chooses to remove the 
	 * message from the device. If there are more than one  message to be deleted, 
	 * then a CSV list of Ids needs to be passed. This is just a soft delete as the
	 * actual message is not deleted
	 * </p>
	 * 
	 * <b>URL: </b>/{URI}/notification/delete<br/>
	 * <b>Type: </b>PUT
	 * @param deviceMessageId - a CSV of IDs to be deleted [Ex-id=1 or id=1,2,3]
	 * @return 1) "DeleteSuccess" if the delete flag is successfully updated
	 * 		   2) "DeleteFailed" if there an error while performing the flag update
	 */		
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/delete")
	public BSROWebServiceResponse deleteNotification( @Context HttpHeaders headers, 
			@Context HttpServletRequest request,
			@QueryParam("id") final String deviceMessageIds);
	

}
