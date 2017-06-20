package com.bridgestone.bsro.aws.appointment.services;

import com.amazonaws.services.lambda.runtime.Context;

public interface IAppointmentService {
	public Object getAppointmentService(Object input, Context context);
	public Object getAppointmentMetadata(Object input, Context context);
	public Object getAppointmentDays(Object input, Context context);
	public Object getAppointmentTimeSlots(Object input, Context context);
	public Object bookAppointment(Object input, Context context);
}
