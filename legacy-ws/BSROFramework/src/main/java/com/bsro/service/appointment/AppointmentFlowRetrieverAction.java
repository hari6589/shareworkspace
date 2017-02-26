package com.bsro.service.appointment;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContext;

import com.bfrc.framework.util.Util;
import com.bsro.pojo.form.AppointmentParsedComments;

@Component("appointmentRetrieverAction")
public class AppointmentFlowRetrieverAction {
	public AppointmentParsedComments getComments(RequestContext context) {
		String unparsedComments = context.getRequestParameters().get("appointment.comments", "");
		AppointmentParsedComments parsedComments = new AppointmentParsedComments(unparsedComments);               
        return parsedComments;
   }

	public Long getVehicleYear(RequestContext context) {
		Long vehicleYear = null;
		if(StringUtils.isNotEmpty(context.getRequestParameters().get("vehicle.year", ""))){
			vehicleYear = Long.parseLong(context.getRequestParameters().get("vehicle.year", ""));
		}
        
		Util.debug("getting vehicle.year: " + vehicleYear);
        return vehicleYear;
   }

	public String getVehicleMake(RequestContext context) {
		String vehicleMake = context.getRequestParameters().get("vehicle.make", "");
		Util.debug("getting vehicle.make: " + vehicleMake);
        return vehicleMake;
   }

	public String getVehicleModel(RequestContext context) {
		String vehicleModel = context.getRequestParameters().get("vehicle.model", "");
		Util.debug("getting vehicle.model: " + vehicleModel);
		return vehicleModel;
   }

	public String getVehicleSubmodel(RequestContext context) {
		String vehicleSubmodel = context.getRequestParameters().get("vehicle.submodel", "");
		Util.debug("getting vehicle.submodel: " + vehicleSubmodel);
		return vehicleSubmodel;
   }
	
	public Long getBatteryQuoteId(RequestContext context){
		Long batteryQuoteId = null;
		if(StringUtils.isNotEmpty(context.getRequestParameters().get("appointment.batteryQuoteId", ""))){
			batteryQuoteId = Long.parseLong(context.getRequestParameters().get("appointment.batteryQuoteId", ""));
			Util.debug("getting appointment.batteryQuoteId: " + batteryQuoteId);
		}
		if (batteryQuoteId == null) {
			if(StringUtils.isNotEmpty(context.getRequestParameters().get("appointment.tireQuoteId", ""))){
				batteryQuoteId = Long.parseLong(context.getRequestParameters().get("appointment.tireQuoteId", ""));
				Util.debug("getting appointment.tireQuoteId: " + batteryQuoteId);
			}
		}
		return batteryQuoteId;
	}

	public String getFirstName(RequestContext context){
		String firstName = context.getRequestParameters().get("appointment.firstName", "");
		Util.debug("getting appointment.firstName: " + firstName);
		return firstName;
	}
	
	public String getLastName(RequestContext context){
		String lastName = context.getRequestParameters().get("appointment.lastName", "");
		Util.debug("getting appointment.lastName: " + lastName);
		return lastName;
	}

	public String getEmailAddress(RequestContext context){
		String emailAddress = context.getRequestParameters().get("appointment.emailAddress", "");
		Util.debug("getting appointment.emailAddress: " + emailAddress);
		return emailAddress;
	}
}
