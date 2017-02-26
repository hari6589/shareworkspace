package com.bsro.service.appointment;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.pojo.appointment.Appointment;
import com.bsro.pojo.appointment.ScheduleAppointmentDataBean;


public interface ScheduleAppointmentService {
	public ScheduleAppointmentDataBean createAppointment(Appointment appt) throws IOException;
	
	public List<LinkedHashMap<String,String>> getServicesFromAppointmentPlus();
	
	public Map<String,HashMap<String,String>> getAppointmentMetadata(String storeNumber, List<String> selectedServiceDesc); 
	
	public BSROWebServiceResponse callBookAppointmentWebservice(AppointmentData apptDetails) throws IOException;
}
