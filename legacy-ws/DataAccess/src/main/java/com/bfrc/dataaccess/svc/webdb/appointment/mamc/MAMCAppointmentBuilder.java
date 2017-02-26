package com.bfrc.dataaccess.svc.webdb.appointment.mamc;

import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.appointment.AppointmentChoice;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.util.XmlUtils;

public class MAMCAppointmentBuilder {

	private static Logger log = Logger.getLogger(MAMCAppointmentBuilder.class.getName());
	
	private static final String SOAP_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>";
	private static final String SOAP_FOOTER = "</soap:Body></soap:Envelope>";
	
	public static String buildInsertAppointment(Appointment appointment, Store store, String webDb) {
		StringBuilder builder = new StringBuilder();
		builder.append(SOAP_HEADER);
		builder.append("<InsertAppointmentData xmlns=\"http://microsoft.com/webservices/MA\">");
	    builder.append("<varAppointment_ID>").append(appointment.getAppointmentId().intValue()).append("</varAppointment_ID>");
		builder.append("<varVehicle_Year>").append(appointment.getVehicleYear() == null?0:appointment.getVehicleYear().intValue()).append("</varVehicle_Year>");
		builder.append("<varVehicle_make>").append(XmlUtils.escape(appointment.getVehicleMake()==null?"":appointment.getVehicleMake())).append("</varVehicle_make>");
		builder.append("<Vehicle_Model>").append(XmlUtils.escape(appointment.getVehicleModel()==null?"":appointment.getVehicleModel())).append("</Vehicle_Model>");
	    builder.append("<Vehicle_SubModel>").append(XmlUtils.escape(appointment.getVehicleSubmodel()==null?"":appointment.getVehicleSubmodel())).append("</Vehicle_SubModel>");
	    builder.append("<mileage>").append(appointment.getMileage() == null?0:appointment.getMileage().intValue()).append("</mileage>");
	    builder.append("<comments>").append(XmlUtils.escape(appointment.getComments())).append("</comments>");
	    builder.append("<First_name>").append(XmlUtils.escape(appointment.getFirstName())).append("</First_name>");
	    builder.append("<Last_name>").append(XmlUtils.escape(appointment.getLastName())).append("</Last_name>");
	    builder.append("<Address1>").append(XmlUtils.escape(appointment.getAddress1())).append("</Address1>");
	    builder.append("<Address2>").append(XmlUtils.escape(appointment.getAddress2())).append("</Address2>");
	    builder.append("<City>").append(XmlUtils.escape(appointment.getCity())).append("</City>");
	    builder.append("<varState>").append(XmlUtils.escape(appointment.getState())).append("</varState>");
	    builder.append("<Zip>").append(XmlUtils.escape(appointment.getZip())).append("</Zip>");
	    builder.append("<DayTime_Phone>").append(StringUtils.trimToEmpty(appointment.getDaytimePhone())).append("</DayTime_Phone>");
	    builder.append("<Evening_Phone>").append(StringUtils.trimToEmpty(appointment.getEveningPhone())).append("</Evening_Phone>");
	    builder.append("<Cell_Phone>").append(StringUtils.trimToEmpty(appointment.getCellPhone())).append("</Cell_Phone>");
	    builder.append("<EMail_Address>").append(XmlUtils.escape(appointment.getEmailAddress())).append("</EMail_Address>");
	    builder.append("<Store_Number>").append(appointment.getStoreNumber() == null?0:appointment.getStoreNumber().intValue()).append("</Store_Number>");
	    builder.append("<Store_Name>").append(XmlUtils.escape(store.getStoreName())).append("</Store_Name>");
	    builder.append("<Store_Address>").append(XmlUtils.escape(store.getAddress())).append("</Store_Address>");
	    builder.append("<Store_City>").append(XmlUtils.escape(store.getCity())).append("</Store_City>");
	    builder.append("<Store_State>").append(XmlUtils.escape(store.getState())).append("</Store_State>");
	    builder.append("<Store_Zip>").append(XmlUtils.escape(store.getZip())).append("</Store_Zip>");
	    builder.append("<Store_Phone>").append(StringUtils.trimToEmpty(store.getPhone())).append("</Store_Phone>");
	    builder.append("<Store_Fax>").append(StringUtils.trimToEmpty(store.getFax())).append("</Store_Fax>");
	    builder.append("<webdb>").append(webDb).append("</webdb>");
	    builder.append("<Email_Reminder>").append(StringUtils.trimToEmpty(appointment.getEmailReminder())).append("</Email_Reminder>");
	    builder.append("<Phone_Reminder>").append(StringUtils.trimToEmpty(appointment.getPhoneReminder())).append("</Phone_Reminder>");
	    builder.append("</InsertAppointmentData>");
	    builder.append(SOAP_FOOTER);

		log.fine(builder.toString());
		 
		return builder.toString();
	}

	
	//YYYY-MM-DDThh:mm:ss
	private static final DateTimeFormatter XML_DATE_TIME_FORMAT = ISODateTimeFormat.dateTimeNoMillis();
	public static String buildInsertAppointmentChoiceData(AppointmentChoice choice, String webDb) {
		DateTime dt = new DateTime(choice.getDatetime().getTime());
		String xmlDateTime = XML_DATE_TIME_FORMAT.print(dt);
		StringBuilder builder = new StringBuilder();
		builder.append(SOAP_HEADER);
		builder.append("<InsertAppointmentChoiceData xmlns=\"http://microsoft.com/webservices/MAChoice\">");
	    builder.append("<varAppointment_ID>").append(choice.getAppointmentId().intValue()).append("</varAppointment_ID>");
	    builder.append("<varChoice>").append(choice.getChoice().intValue()).append("</varChoice>");
	    builder.append("<drop_wait_option>").append(choice.getDropWaitOption()).append("</drop_wait_option>");
	    builder.append("<varChoice_date>").append(xmlDateTime).append("</varChoice_date>");
	    builder.append("<webdb>").append(webDb).append("</webdb>");
	    builder.append("</InsertAppointmentChoiceData>");
	    builder.append(SOAP_FOOTER);
	    
		log.fine(builder.toString());

	    return builder.toString();
	}
	
	public static String buildInsertAppointmentServiceData(Long appointmentId, String serviceDescription, String webDb) {
		StringBuilder builder = new StringBuilder();
		builder.append(SOAP_HEADER);
		builder.append("<InsertAppointmentServiceData xmlns=\"http://microsoft.com/webservices/MAChoice\">");
	    builder.append("<varAppointment_ID>").append(appointmentId.intValue()).append("</varAppointment_ID>");
	    builder.append("<service_desc>").append(XmlUtils.escape(serviceDescription)).append("</service_desc>");
	    builder.append("<webdb>").append(webDb).append("</webdb>");
	    builder.append("</InsertAppointmentServiceData>");
	    builder.append(SOAP_FOOTER);
	    
		log.fine(builder.toString());

	    return builder.toString();
	}
}
