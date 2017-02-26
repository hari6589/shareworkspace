package com.bfrc.framework.data;

import com.bfrc.Bean;
import com.bfrc.framework.dao.AppointmentDAO;

public class ServiceOptions extends java.util.LinkedHashMap  implements Bean{
	
	public AppointmentDAO appointmentDAO;
	public AppointmentDAO getAppointmentDAO(){
		return this.appointmentDAO;
	}
	public void setAppointmentDAO(AppointmentDAO appointmentDAO){
		this.appointmentDAO = appointmentDAO;
		loadData();
	}
	public void loadData() {
		this.putAll(appointmentDAO.getMappedAppointmentServiceDescs());
	}
}