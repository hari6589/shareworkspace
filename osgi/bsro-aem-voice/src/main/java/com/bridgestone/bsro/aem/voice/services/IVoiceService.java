package com.bridgestone.bsro.aem.voice.services;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.bridgestone.bsro.aem.core.exception.BSROBusinessException;

/*
*
* Created by  rreddykandari on : Jan 25, 2016 for the project : bsro-aem-appointment-service
*/
@SuppressWarnings("rawtypes")
public interface IVoiceService {

	/**
	 * Gets the appointment service assignments by storeNumber and serviceNamesList.
	 *
	 * @param request
	 * @param storeNumber
	 * @param serviceNamesList
	 * @return the appointment service assignments Map
	 * @throws BSROBusinessException
	 */
	public Map getServiceAssignments(HttpServletRequest request,
									 String storeNumber, String serviceNamesList) throws BSROBusinessException;
	
	
	/**
	 * Gets the available days by location id,employee id,start date,num of days.
	 *
	 * @param request
	 * @param locationId
	 * @param employeeId
	 * @param startDate
	 * @param numDays
	 * @return the available days Map
	 * @throws BSROBusinessException
	 */
	public Map getAvailableDays(HttpServletRequest request, String locationId,
						 String employeeId, String startDate, String numDays) throws BSROBusinessException;
	
	/**
	 * Gets the available times by location id,employee id,selected date,service ids.
	 *
	 * @param request
	 * @param locationId
	 * @param employeeId
	 * @param selectedDate
	 * @param serviceIds
	 * @return the available times Map
	 * @throws BSROBusinessException
	 */
	public Map getAvailableTimes(HttpServletRequest request, String locationId,
						  String employeeId, String selectedDate, String serviceIds) throws BSROBusinessException;
	/**
	 * Post the appointment confirmation
	 *
	 * @param request
	 * @param vehicleType
	 * @return the appointment confirmation Map
	 * @throws BSROBusinessException
	 */
	public Map appointmentConfirm(HttpServletRequest request, String vehicleType)
			throws BSROBusinessException;

	/**
	 * Get All Services
	 *
	 * @param request
	 * @return the All Services Map
	 * @throws BSROBusinessException
	 */
	public Map<String, Object> getAllServices(HttpServletRequest request)throws BSROBusinessException;

}
