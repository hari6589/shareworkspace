package com.bsro.api.rest.ws.appointment;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import app.bsro.model.appointment.CreateAppointment;
import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.model.appointment.Appointment;

/**
 * Controls the creation of appointments with the sending of the appointment to MAI.
 * @author Brad Balmer
 *
 */
@Path(AppointmentWebService.URL)
public interface AppointmentWebService {
	public static final String URL = "/appointment";
	
	/**              
	 * Returns all service descriptions that an appointment can be created for.<br/>
	 * <b>URL: </b>/{URI}/appointment/services<br/>
	 * <b>Type: </b>GET
	 * @return List<ValueTextBean> a list of ValueTextBean
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/services")
	public List<ValueTextBean> serviceList(@Context HttpHeaders headers);
	
	/** 
	 * <p>             
	 * Returns all service descriptions and categories that an appointment can be created for.<br/>
	 * for a particular store, If no store is passed, then the unique list of all services for all
	 * stores are returned.
	 * 
	 * The call performs lot better if no store is passed.
	 * </p>
	 * <b>URL: </b>/{URI}/appointment/store-services<br/>
	 * <b>Type: </b>GET
	 * @return response containing a list of store services
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/store-services")
	public BSROWebServiceResponse getStoreServices(@QueryParam("storeNumber") Long storeNumber,
										@Context HttpHeaders headers);
	
	/**
	 * Create an appointment<br/>
	 * <b>URL: </b>/{URI}/appointment/create<br/>
	 * <b>Type: </b>POST
	 * @see Appointment
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/create")
	public Response createAppointment(CreateAppointment appointment, @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * Get available days where at least one time slot is available for a period of time from the
	 * specified start date
	 * Note: <b>User need to call appointment metadata to get the locationId and the employeeId</b>
	 * </p>
	 * <b>URL: </b>/{URI}/appointment/availability/days<br/>
	 * <b>Type: </b>GET
	 * @param locationId 	- locationId for a store number returned from Appointment metadata webservice
	 * @param startDate	    - date in the future from which availability will be queried in yyyyMMdd format [Ex-20140823]
	 * @param numDays 		- number of days for which open slots can be returned.
	 * @param employeeId	- employeeId for the staff returned from Appointment metadata webservice
	 * @param headers
	 * @return response containing the list if appointment open dates{@link AppointmentOpenDate}
	 * @see app.bsro.model.appointment.AppointmentOpenDate
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/availability/days")
	public BSROWebServiceResponse getAvailabilityByDay(@QueryParam("locationId") Long locationId, 
										   @QueryParam("startDate") String startDate,
										   @QueryParam("numDays") Integer numDays, 
										   @QueryParam("employeeId") Long employeeId, 
										   @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * Get available time slots for a particular day for services selected. If there are more than one
	 * service, the addon services should be after the primary services in the CSV.
	 * </p>
	 * <b>URL: </b>/{URI}/appointment/availability/times<br/>
	 * <b>Type: </b>POST
	 * @param locationId 	- the locationId for store number returned from Appointment metadata
	 * @param startDate  	- the user selected date for appointment in yyyyMMdd format [Ex- 20141123]
	 * @param serviceIdCSV 	- CSV of primary service Id and all the addon services returned from
	 *                        appointment metadata webservices. The primary service should be first one
	 *                      - followed by the addon services.
	 * @param headers
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/availability/times")
	public BSROWebServiceResponse getAvailability(@QueryParam("locationId") Long locationId, 
									  @QueryParam("employeeId") Long employeeId,
									  @QueryParam("selectedDate") String selectedDate, 
									  @QueryParam("serviceIds") String serviceIdCSV,
									  @Context HttpHeaders headers);
	
	/**
	 * Book a time slot with the given appointment details
	 * <b>URL: </b>/{URI}/appointment/book
	 * <b>Type: </b>POST
	 * @see com.bfrc.dataaccess.model.appointment.AppointmentData
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/book")
	public BSROWebServiceResponse bookAppointment(com.bfrc.dataaccess.model.appointment.AppointmentData appointment, @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This method creates a JSON object from pojo supplied as parameter and posts data
	 * to Subscriber and Send Yes Mail API for sending email to Customer
	 * </p>
	 * <b>URL: </b>/{URI}/appointment/email/customer
	 * <b>Type: </b>POST
	 * 
	 * @param appt - Appointment Data pojo with all appointment and store and user information
	 * @return tracking confirmation number in response payload
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Path("/email/customer")
	public BSROWebServiceResponse sendEmailDataForCustomer(com.bfrc.dataaccess.model.appointment.AppointmentData appt,
								@Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This method creates a JSON object from pojo supplied as parameter and posts data
	 * to Subscriber and Send Yes Mail API for sending email to Store Manager
	 * </p>
	 * <b>URL: </b>/{URI}/appointment/email/store
	 * <b>Type: </b>POST
	 * 
	 * @param appt - Appointment Data pojo with all appointment and store and user information
	 * @return tracking confirmation number in response payload
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Path("/email/store")
	public BSROWebServiceResponse sendEmailDataForStore(com.bfrc.dataaccess.model.appointment.AppointmentData appt,
								@Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This method accepts the tracking number as parameter and calls the subscriber and send API
	 * to get the status response for the posted appointment data
	 * </p>
	 * <b>URL: </b>/{URI}/appointment/email/status
	 * <b>Type: </b>POST
	 * 
	 * @param trackingId passed from the previous method from Subscriber and Send API
	 * @return status message with the processing status details.
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/email/status")
	public BSROWebServiceResponse checkProcessingStatus(@QueryParam("trackingId") String trackingId, @Context HttpHeaders headers);
	
	/**
	 * <p>
	 * This method accepts the tracking number as parameter and calls the subscriber and send API
	 * to get the status response for the posted appointment data
	 * </p>
	 * <b>URL: </b>/{URI}/appointment/metadata
	 * <b>Type: </b>POST
	 * 
	 * @param trackingId passed from the previous method from Subscriber and Send API
	 * @return status message with the processing status details.
	 * @see app.bsro.model.appointment.AppointmentMetaData
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/metadata")
	public BSROWebServiceResponse getAppointmentMetadata(@QueryParam("storeNumber") Long storeNumber,
										@QueryParam("services") String serviceDescCSV, 
										@Context HttpHeaders headers);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/schedule")
	public BSROWebServiceResponse getAppointmentAvailableSchedules(@QueryParam("storeNumber") Long storeNumber,
										@QueryParam("month") String month, 
										@Context HttpHeaders headers);
	

}
