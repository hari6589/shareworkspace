package com.bsro.service.appointment;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.pojo.appointment.AppointmentSchedule;
import com.bfrc.pojo.store.Store;
import com.bsro.pojo.form.AppointmentForm;

public interface AppointmentTimesService {
	
	public List getDatesForMonth(Long storeNumber, String month, boolean useClassicRules);
	
	public List getTimesForDate(Long storeNumber, String month, String date, boolean useClassicRules);
	
	public List getMonths(Long storeNumber);
	
	public GregorianCalendar getTimeForStore(Store store, Date date);
	
	public GregorianCalendar getTimeForStore(Long storeNumber, Date date);
	
	public AppointmentSchedule createAppointmentSchedule(Store store, Date date);
	
	public AppointmentSchedule createAppointmentSchedule(Long storeNumber, Date date);
	
	public BSROWebServiceResponse getAvailableTimesForService(Long locationId,
			Long employeeId, String serviceIds, String selectedDate);
	
	public BSROWebServiceResponse getAvailableDays(Long locationId,
			Long employeeId, String startDate, Integer numDays);

}