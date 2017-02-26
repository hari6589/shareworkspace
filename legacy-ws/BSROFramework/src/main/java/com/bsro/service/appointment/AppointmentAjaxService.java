package com.bsro.service.appointment;

import java.util.Set;

import com.bfrc.pojo.appointment.AppointmentSchedule;
import com.bsro.controller.appointment.SlickcomboTimeData;

public interface AppointmentAjaxService {

	public abstract SlickcomboTimeData getTimesFromSchedule(String month,
			String date, AppointmentSchedule schedule);

	public abstract SlickcomboTimeData getAppointmentDatesFromSchedule(
			String month, AppointmentSchedule schedule);
	
	public SlickcomboTimeData getTimesFromSchedulerService(String month,String day, String year,
			String selectedServiceIds, String locationId, String employeeId);
	
	public SlickcomboTimeData  getDatesFromSchedulerService(String startDate, Integer numDays,
			String locationId, String employeeId);
	
	public SlickcomboTimeData getDropOffPickUpTimesFromSchedule(String month,
			String date, AppointmentSchedule schedule, String option);
	
	public String getYearForMonth(String month, AppointmentSchedule schedule);
	
	public Set<String> getMonthsFromSchedulerService(AppointmentSchedule schedule, 
			Long locationId, Long employeeId);
	
	public Set<String> getMonthsFromSchedulerService(AppointmentSchedule schedule, Integer numDays,
			Long locationId, Long employeeId);
	
	public String getStartDate(String year, String month);
	
	public int getNumDays(String startDate, int maxNumDays);

}