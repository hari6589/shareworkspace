package com.bfrc.dataaccess.svc.webdb.appointment;

import com.bfrc.dataaccess.exception.HttpException;
import com.bfrc.dataaccess.exception.InvalidAppointmentException;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.store.Store;

public interface AppointmentSender {

	public void sendAppointmentData(Appointment appointment, Store store) throws HttpException, InvalidAppointmentException;
	
}
