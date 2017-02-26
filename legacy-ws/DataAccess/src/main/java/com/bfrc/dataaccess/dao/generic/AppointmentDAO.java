package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;
import java.util.List;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.appointment.Appointment;

public interface AppointmentDAO extends IGenericOrmDAO<Appointment, Long> {

	public Collection<Object[]> findWebserviceInfoByWebSiteName(String apptWebSiteName);
	
	public List<Appointment> findAppointmentsToRetry();
	
}
