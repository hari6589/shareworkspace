/**
 * 
 */
package com.bfrc.dataaccess.svc.webdb.appointment;

import java.util.Collection;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.dataaccess.model.appointment.AppointmentServiceDesc;

/**
 * @author schowdhu
 *
 */
public interface AppointmentPlusScheduler {
	
	public BSROWebServiceResponse getServices(Long storeNumber);
	
	public Collection<AppointmentServiceDesc> getServicesFromDB(Long storeNumber);
	
	public BSROWebServiceResponse getBridgestoneAppointmentRules(Long storeNumber, String serviceDescCSV);
	
	public BSROWebServiceResponse getStaffOpenDates(Long locationId, String startDate, Integer numDays, Long employeeId);

	public BSROWebServiceResponse getAppointmentOpenSlots(Long locationId,Long employeeId, String startDate, 
			Integer startTimeInMins, Integer numDays, String primaryServiceId, String additionalServiceIds);
	
	public BSROWebServiceResponse createAppointment(AppointmentData appointment, String selectedDate, Integer timeInMins,
			String primaryServiceId, String additionalServiceIds);
}
