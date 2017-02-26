/**
 * 
 */
package com.bfrc.dataaccess.svc.webdb.appointment;

import com.bfrc.dataaccess.model.appointment.email.AppointmentDetails;


/**
 * @author schowdhury
 *
 */
@Deprecated
public interface AppointmentEmailSender {
	
	/**
	 * This method creates a JSON object from pojo supplied as parameter and posts data
	 * to Subscriber and Send Yes Mail API for sending email to Customer
	 * 
	 * @param appt - Appointment Details pojo with all appointment and store and user information
	 * @return tracking confirmation number 
	 */
	public String sendEmailData(AppointmentDetails appt, boolean isCustomer);
	
	/**
	 * This method accepts the tracking number as parameter and calls the subscriber and send API
	 * to get the status response for the posted appointment data
	 * 
	 * @param trackingId passed from the previous method from Subscriber and Send API
	 * @return status message with the processing status details.
	 */
	public String checkProcessingStatus(String trackingId);
	

}