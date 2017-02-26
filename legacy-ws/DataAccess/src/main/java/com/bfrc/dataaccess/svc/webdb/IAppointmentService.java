package com.bfrc.dataaccess.svc.webdb;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.bfrc.dataaccess.exception.InvalidAppointmentException;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.appointment.AppointmentChoice;
import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.dataaccess.model.appointment.AppointmentService;
import com.bfrc.dataaccess.model.appointment.AppointmentServiceDesc;

public interface IAppointmentService {

	/**
	 * Returns a listing of all AppointmentServiceDesc records
	 * @return
	 */
	public Collection<AppointmentServiceDesc> listAppointmentServices();
	public AppointmentServiceDesc getAppointmentServiceDesc(Integer id);
	public String getAppointmentServiceDescs(String serviceIds);
	
	public void createAppointment(Appointment appointment, String appName) throws InvalidAppointmentException;
	
	public void saveServices(Appointment appointment, Set<AppointmentService> services) throws InvalidAppointmentException;
	
	public void saveChoices(Appointment appointment, List<AppointmentChoice> choices) throws InvalidAppointmentException;
	public Date getChoiceDatetime(String month, String date, String time);
	
	public void retryFailedAppointments();
	
	public Long saveAppointment(AppointmentData appointmentData,String confirmationId, String customerId, 
			String statusMessage) throws InvalidAppointmentException;
	
	public void saveCustInfo(Appointment appointment) throws InvalidAppointmentException;

}
