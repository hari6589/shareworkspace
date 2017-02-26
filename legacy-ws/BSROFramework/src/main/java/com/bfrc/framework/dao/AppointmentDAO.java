package com.bfrc.framework.dao;


import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.pojo.appointment.Appointment;
import com.bfrc.pojo.appointment.AppointmentChoice;
import com.bfrc.pojo.appointment.AppointmentCount;
import com.bfrc.pojo.appointment.AppointmentCustomer;
import com.bfrc.pojo.appointment.AppointmentID;
import com.bfrc.pojo.appointment.AppointmentLog;
import com.bfrc.pojo.appointment.AppointmentMetadata;
import com.bfrc.pojo.appointment.AppointmentSentStatus;
import com.bfrc.pojo.appointment.AppointmentService;
import com.bfrc.pojo.appointment.AppointmentServiceDesc;

public interface AppointmentDAO {
	public void logAppointment(AppointmentLog apptLog) throws DataAccessException;
	public void addAppointment(Appointment appt) throws DataAccessException;
	public void updateAppointment(Appointment appt) throws DataAccessException;
	public void deleteAppointment(Appointment appt) throws DataAccessException;
	public void addAppointmentService(AppointmentService apptService) throws DataAccessException;
	public void addAppointmentChoice(AppointmentChoice apptChoice) throws DataAccessException;
	
	public Integer getServiceId(String serviceDesc) throws DataAccessException;
	public String getServiceDesc(Integer id) throws DataAccessException;
	public Appointment findAppointmentById(Long id) throws DataAccessException;
	public List<AppointmentService> findServicesForAppointment(Long appointmentId);
	//cxs
	public void createAppointmentChoice(AppointmentChoice appointmentChoice) throws DataAccessException;
	public void updateAppointmentChoice(AppointmentChoice appointmentChoice) throws DataAccessException;
	public void deleteAppointmentChoice(AppointmentChoice appointmentChoice) throws DataAccessException;
	//cxs
	public void createAppointmentService(AppointmentService appointmentService) throws DataAccessException;
	public void updateAppointmentService(AppointmentService appointmentService) throws DataAccessException;
	public void deleteAppointmentService(AppointmentService appointmentService) throws DataAccessException;
    
	public AppointmentServiceDesc findAppointmentServiceDescById(Object id) throws DataAccessException;
	public void createAppointmentServiceDesc(AppointmentServiceDesc appointmentServiceDesc) throws DataAccessException;
	public void updateAppointmentServiceDesc(AppointmentServiceDesc appointmentServiceDesc) throws DataAccessException;
	public void deleteAppointmentServiceDesc(AppointmentServiceDesc appointmentServiceDesc) throws DataAccessException;
	public void deleteAppointmentServiceDesc(Object id) throws DataAccessException;
	public List getAllAppointmentServiceDescs() throws DataAccessException;
	public Map getMappedAppointmentServiceDescs() throws DataAccessException;

	public List getAllAppointmentServiceDescsAndCategories();
	public List getAllAppointmentServiceSorted();
	public List getAllAppointmentServicesBySortOrder();
	public List getAllAppointmentServicesBySortOrder(int serviceType);
	public void saveAppointmentSentStatus(AppointmentSentStatus apptSentStatus);
	
	public void addAppointmentCustomer(AppointmentCustomer apptCust) throws DataAccessException;
	public void updateAppointmentCustomer(AppointmentCustomer apptCust) throws DataAccessException;
	public void deleteAppointmentCustomer(AppointmentCustomer apptCust) throws DataAccessException;
	
	public String getAppointmentConfirmationMessage(String storeType) throws DataAccessException;
	
	public void addAppointmentMetadata(AppointmentMetadata metadata) throws DataAccessException;
	public AppointmentMetadata getAppointmentMetadata(Long appointmentId) throws DataAccessException;
	
	public AppointmentData createAppointmentDataBean(Appointment appt);
	
	public void updateSentStatus (Long apptId, String status, Long confirmId, String trackingId, String statusMessage)
			throws DataAccessException;
	
	public List<Appointment> getAppointmentsToRetry() throws DataAccessException;
	
	public AppointmentID[] getErrorAppointmentID() throws DataAccessException;
	public AppointmentCount[] getErrorAppointmentCount() throws DataAccessException;
}
