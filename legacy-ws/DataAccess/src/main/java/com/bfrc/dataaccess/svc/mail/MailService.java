package com.bfrc.dataaccess.svc.mail;

import com.bfrc.dataaccess.model.appointment.Appointment;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author smoorthy
 *
 */
public interface MailService {
	
	public BSROWebServiceResponse postEmailQuote(String source, String siteName, Long quoteId,
			String rebateId, String firstName, String lastName, String emailAddress);
	
	public void sendAppointmentRetryFailedEmail(Appointment appointment, String services);
	
	public void sendAppointmentRetryFailedCountEmail(Integer retryCount);

}
