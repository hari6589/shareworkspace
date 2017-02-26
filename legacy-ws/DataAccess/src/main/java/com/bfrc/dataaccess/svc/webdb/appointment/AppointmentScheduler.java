package com.bfrc.dataaccess.svc.webdb.appointment;
/*
 * Created on May 30, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.DOMException;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.appointment.AppointmentChoice;
import com.bfrc.dataaccess.util.ValidationConstants;

@Deprecated
public class AppointmentScheduler {
	
	private Log logger = LogFactory.getLog(AppointmentScheduler.class);
	
	final static String DELIMITOR = ",";
	final static String EXTENSION = " ext. ";

	final static String NAMESPACE = "https://scheduler.ssichicago.com/";
	final static String PREFIX = "";	

	private static final String HTTPS_PROXYHOST 	= "prxy-ch-bfr.bfr.bfrco.com";
	private static final String HTTPS_PROXYPORT 	= "8080";
	
	private SOAPConnection connection;
	private SOAPMessage message;
	private SOAPEnvelope envelope;
	
	private AppointmentSchedulerConfig appointmentSchedulerConfig;
	
	public AppointmentScheduler(AppointmentSchedulerConfig appointmentSchedulerConfig) {
		setAppointmentSchedulerConfig(appointmentSchedulerConfig);
		initSoapConnection();
		addAuthHeader();
	}
	
	/**
	 * @param appointmentSchedulerConfig the appointmentSchedulerConfig to set
	 */
	public void setAppointmentSchedulerConfig(
			AppointmentSchedulerConfig appointmentSchedulerConfig) {
		this.appointmentSchedulerConfig = appointmentSchedulerConfig;
	}

	private void initSoapConnection() {
		// set security property
		Security.setProperty("ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl");
		Security.setProperty("ssl.ServerSocketFactory.provider", "com.ibm.jsse2.SSLServerSocketFactoryImpl");
		//System.setProperty("https.proxySet", "true");
		//System.setProperty("https.proxyHost", HTTPS_PROXYHOST);
		//System.setProperty("https.proxyPort",HTTPS_PROXYPORT);


    	try{
	        // First create the connection
	        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
	        connection = soapConnFactory.createConnection();
	
	        // Next, create the actual message
	        MessageFactory messageFactory = MessageFactory.newInstance();
	        message = messageFactory.createMessage();
	
	        SOAPPart soapPart = message.getSOAPPart();
	        envelope = soapPart.getEnvelope();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void addAuthHeader() {
		try {
			// Create and populate the header
		    SOAPHeader header = message.getSOAPHeader();
		    if (header == null)
		        header = envelope.addHeader();
		    // Create the main element and namespace
		    SOAPHeaderElement headerElement = header.addHeaderElement(
		            envelope.createName("AuthHeader", PREFIX, NAMESPACE));
		    // Add parameters
		    headerElement.addChildElement("UID").addTextNode(appointmentSchedulerConfig.getUid());
		    headerElement.addChildElement("Password").addTextNode(appointmentSchedulerConfig.getPassword());
		    logger.debug("uid=" + appointmentSchedulerConfig.getUid() + " password=" + appointmentSchedulerConfig.getPassword());
        } catch (Exception e) {
        	e.printStackTrace();
        }	    
	}
	
	public BSROWebServiceResponse getAvailabilityByDay(String storeNumber, String startDate, String endDate, String servicesCSV){
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		SOAPBody body = null;
		SOAPElement bodyElement = null;

		try {
			// Create and populate the body
			body = envelope.getBody();
			// Create the main element and name space
			bodyElement = body.addChildElement(
					envelope.createName("availabilityByDay", PREFIX, NAMESPACE));
			// Add parameters
			String paddedStore = ("000000" + storeNumber);
			bodyElement.addChildElement("strStore").addTextNode(paddedStore.substring(paddedStore.length()-6));
			bodyElement.addChildElement("strStartDate").addTextNode(startDate);
			bodyElement.addChildElement("strEndDate").addTextNode(endDate);
			bodyElement.addChildElement("strServiceIDs").addTextNode(servicesCSV);
			// Add soap action
			MimeHeaders headers = message.getMimeHeaders();
			headers.addHeader("SOAPAction", NAMESPACE+"availabilityByDay");

			// Save the message
			message.saveChanges();

			// Send the message and get the reply
			SOAPMessage reply = connection.call(message, appointmentSchedulerConfig.getEndpoint());

			SOAPBody responseBody = reply.getSOAPBody();

			//error handling
			AppointmentSchedulerFault fault = checkFaults(responseBody);

			if(fault != null){
				System.err.println("Request xml = " + bodyElement.toString());
				errors.getGlobalErrors().add(fault.getFaultMessage());
				response.setErrors(errors);
				response.setPayload(null);
				response.setStatusCode(fault.getDetailErrorCode());
			}else{
				// Retrieve the result
				ByteArrayOutputStream availXml = new ByteArrayOutputStream();
				reply.writeTo(availXml);
				org.json.JSONObject json = XML.toJSONObject(availXml.toString());
				if(json.getJSONObject("soap:Envelope") != null && json.getJSONObject("soap:Envelope").getJSONObject("soap:Body") != null){  
					org.json.JSONObject availabilityRespJson = json.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("availabilityByDayResponse");
					if(availabilityRespJson != null && availabilityRespJson.getJSONObject("availabilityByDayResult") != null
							&& availabilityRespJson.getJSONObject("availabilityByDayResult").getJSONObject("diffgr:diffgram") != null){
						org.json.JSONObject availabilityJson = availabilityRespJson.getJSONObject("availabilityByDayResult").getJSONObject("diffgr:diffgram").getJSONObject("dsAvailabilityByDay");
						response.setPayload(availabilityJson.toString());
						response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
					}
				}
			}

			// Close the connection           
			connection.close();
		} catch (SOAPException e) {
			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.AVAILABILITY_ERR_MESSAGE, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString(),e);
		} catch (IOException e) {
			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.AVAILABILITY_ERR_MESSAGE, BSROWebServiceResponseCode.VALIDATION_ERROR.toString(),e);
		} catch (JSONException e) {
			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.AVAILABILITY_ERR_MESSAGE, BSROWebServiceResponseCode.VALIDATION_ERROR.toString(),e);
		}
	    return response;
	}	

	public BSROWebServiceResponse getAvailability(String storeNumber, String startDate, String endDate, String servicesCSV) {

		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		SOAPBody body = null;
		SOAPElement bodyElement = null;

		try {
			// Create and populate the body
			body = envelope.getBody();
			// Create the main element and namespace
			bodyElement = body.addChildElement(
					envelope.createName("availability", PREFIX, NAMESPACE));
			// Add parameters
			String paddedStore = ("000000" + storeNumber);
			bodyElement.addChildElement("strStore").addTextNode(paddedStore.substring(paddedStore.length()-6));
			bodyElement.addChildElement("strStartDate").addTextNode(startDate);
			bodyElement.addChildElement("strEndDate").addTextNode(endDate);
			bodyElement.addChildElement("strServiceIDs").addTextNode(servicesCSV);
			// Add soap action
			MimeHeaders headers = message.getMimeHeaders();
			headers.addHeader("SOAPAction", NAMESPACE+"availability");

			// Save the message
			message.saveChanges();

			// Send the message and get the reply
			SOAPMessage reply = connection.call(message, appointmentSchedulerConfig.getEndpoint());

			SOAPBody responseBody = reply.getSOAPBody();

			//error handling
			AppointmentSchedulerFault fault = checkFaults(responseBody);

			if(fault != null){
				System.err.println("Request xml = " + bodyElement.toString());
				errors.getGlobalErrors().add(fault.getFaultMessage());
				response.setErrors(errors);
				response.setPayload(null);
				response.setStatusCode(fault.getDetailErrorCode());
			}else{

				// Retrieve the result
				ByteArrayOutputStream availXml = new ByteArrayOutputStream();
				reply.writeTo(availXml);
				JSONObject json = XML.toJSONObject(availXml.toString());
				if(json.getJSONObject("soap:Envelope") != null && json.getJSONObject("soap:Envelope").getJSONObject("soap:Body") != null){  
					JSONObject availabilityRespJson = json.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("availabilityResponse");
					if(availabilityRespJson != null && availabilityRespJson.getJSONObject("availabilityResult") != null
							&& availabilityRespJson.getJSONObject("availabilityResult").getJSONObject("diffgr:diffgram") != null){
						JSONObject availabilityJson = availabilityRespJson.getJSONObject("availabilityResult").getJSONObject("diffgr:diffgram").getJSONObject("dsAvailability");
						response.setPayload(availabilityJson.toString());
						response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
					}
				}
			}

			// Close the connection           
			connection.close();
		} catch (SOAPException e) {
			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.AVAILABILITY_ERR_MESSAGE, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString(),e);
		} catch (IOException e) {
			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.AVAILABILITY_ERR_MESSAGE, BSROWebServiceResponseCode.VALIDATION_ERROR.toString(),e);
		} catch (JSONException e) {
			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.AVAILABILITY_ERR_MESSAGE, BSROWebServiceResponseCode.VALIDATION_ERROR.toString(),e);
		}
	    return response;
	}	

	/** This method is replaced with AppointmentPlusScheduler.createAppointment**/
	public BSROWebServiceResponse bookAppointment(com.bfrc.dataaccess.model.appointment.AppointmentData appt){
		
//		long apptLng = -1L;
//		BSROWebServiceResponse response = new BSROWebServiceResponse();
//		Errors errors = new Errors();
//		SOAPBody body = null;
//		SOAPElement bodyElement = null;
//
//		try {
//			// Create and populate the body
//			body = envelope.getBody();
//			// Create the main element and namespace
//			bodyElement = body.addChildElement(
//					envelope.createName("bookAppointment", PREFIX, NAMESPACE));
//			// Add parameters
//			String paddedStore = ("000000" + String.valueOf(appt.getStoreNumber()));
//			bodyElement.addChildElement("strStore").addTextNode(paddedStore.substring(paddedStore.length()-6));
//			//bodyElement.addChildElement("lngCustApptID").addTextNode(StringUtils.isBlank(appt.getAppointmentId()) ? "0" :appt.getAppointmentId());
//			bodyElement.addChildElement("lngQuote").addTextNode(StringUtils.isBlank(appt.getQuoteId()) ? "0" : appt.getQuoteId());
//			bodyElement.addChildElement("intVehicleYear").addTextNode(StringUtils.isBlank(appt.getVehicleYear()) ? "0" : appt.getVehicleYear());
//			bodyElement.addChildElement("strVehicleMake").addTextNode(appt.getVehicleMake());
//			bodyElement.addChildElement("strVehicleModel").addTextNode(appt.getVehicleModel());
//			bodyElement.addChildElement("strVehicleSubmodel").addTextNode(appt.getVehicleSubmodel());
//			bodyElement.addChildElement("bolTPMS").addTextNode(StringUtils.isBlank(appt.getTpmsFlag()) || appt.getTpmsFlag() == "0" ? "false" : "true"); 
//			bodyElement.addChildElement("intVehicleMileage").addTextNode(StringUtils.isBlank(appt.getVehicleMileage()) ? "0" : appt.getMileage());
//			bodyElement.addChildElement("strComment").addTextNode(appt.getComments());
//			bodyElement.addChildElement("strNameFirst").addTextNode(appt.getCustomerFirstName());
//			bodyElement.addChildElement("strNameLast").addTextNode(appt.getCustomerLastName());
//			bodyElement.addChildElement("strAddress").addTextNode(appt.getCustomerAddress1());
//			bodyElement.addChildElement("strCity").addTextNode(appt.getCustomerCity());
//			bodyElement.addChildElement("strState").addTextNode(appt.getCustomerState());
//			bodyElement.addChildElement("strZip").addTextNode(appt.getCustomerZipCode());
//
//			String phone = appt.getCustomerDayTimePhone();
//			if(phone.indexOf(EXTENSION) != -1){
//				bodyElement.addChildElement("strPhone").addTextNode(phone.substring(0,phone.indexOf(EXTENSION)));
//				bodyElement.addChildElement("strExt").addTextNode(phone.substring(phone.indexOf(EXTENSION)+EXTENSION.length()));
//			}else{
//				bodyElement.addChildElement("strPhone").addTextNode(phone);
//				bodyElement.addChildElement("strExt").addTextNode("");
//			}
//			bodyElement.addChildElement("strEmail").addTextNode(appt.getCustomerEmailAddress());
//
//			if(appt.getChoice() != null){
//				AppointmentChoice choice = appt.getChoice();
//				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
//
//				if(choice.getDropWaitOption().equalsIgnoreCase("drop")){
//					bodyElement.addChildElement("strRequestDate").addTextNode(dateFormat.format(choice.getDropOffTime()));
//					bodyElement.addChildElement("strRequestTime").addTextNode("");
//					bodyElement.addChildElement("strDropOffTime").addTextNode(timeFormat.format(choice.getDropOffTime()));
//					bodyElement.addChildElement("strPickUpTime").addTextNode(timeFormat.format(choice.getPickUpTime()));
//				}else{
//					bodyElement.addChildElement("strRequestDate").addTextNode(dateFormat.format(choice.getDatetime()));
//					bodyElement.addChildElement("strRequestTime").addTextNode(timeFormat.format(choice.getDatetime()));
//					bodyElement.addChildElement("strDropOffTime").addTextNode("");
//					bodyElement.addChildElement("strPickUpTime").addTextNode("");
//				}
//			}
//
//			bodyElement.addChildElement("strServiceIDs").addTextNode(appt.getSelectedServices());
//
//			// Add soap action
//			MimeHeaders headers = message.getMimeHeaders();
//			headers.addHeader("SOAPAction", NAMESPACE+"bookAppointment");
//
//			// Save the message
//			message.saveChanges();
//
//			// Send the message and get the reply
//			SOAPMessage reply = connection.call(message, appointmentSchedulerConfig.getEndpoint());
//
//			AppointmentSchedulerFault fault = checkFaults(reply.getSOAPBody());
//
//
//			if(fault != null){	
//				System.err.println("Request xml = " + bodyElement.toString());
//				errors.getGlobalErrors().add(fault.getFaultMessage());
//				response.setErrors(errors);
//				//set the default appointment object
//				response.setPayload(apptLng);
//				response.setStatusCode(fault.getDetailErrorCode());
//			}else{
//				// Retrieve the result
//				apptLng = new Integer(reply.getSOAPBody().getTextContent());
//
//				//set the id to the response object
//				response.setPayload(apptLng);
//				response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
//			}
//
//			// Close the connection           
//			connection.close();
//		} catch (NumberFormatException e) {
//			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.BOOK_APPOINTMENT_ERR_MESSAGE, BSROWebServiceResponseCode.VALIDATION_ERROR.toString(),e);
//		} catch (DOMException e) {
//			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.BOOK_APPOINTMENT_ERR_MESSAGE, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString(),e);
//		} catch (SOAPException e) {
//			handleWebServiceError(errors, response, bodyElement.toString(), ValidationConstants.BOOK_APPOINTMENT_ERR_MESSAGE, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString(), e);
//		}
            
	    return null;
	}	
	
	public AppointmentSchedulerFault checkFaults(SOAPBody responseBody){
		AppointmentSchedulerFault sFault = new AppointmentSchedulerFault();
		SOAPFault fault = responseBody.getFault();
		if(fault != null){
			System.err.println("Response xml = "+responseBody.toString());
			sFault.setFaultMessage(fault.getFaultString());
			sFault.setFaultActor(fault.getFaultActor());
			Detail detail = fault.getDetail();
			if(detail != null){
				Iterator<SOAPElement> i = detail.getChildElements();
				while (i.hasNext()){
					SOAPElement error = i.next();
					Iterator<SOAPElement> err = error.getChildElements();
					while(err.hasNext()){
						SOAPElement elem = err.next();
						if(elem.getNodeName().equalsIgnoreCase("ErrorNumber")){
							sFault.setDetailErrorCode(elem.getNodeValue());
						}if(elem.getNodeName().equalsIgnoreCase("ErrorSource")){
							sFault.setDetailErrorSource(elem.getNodeValue());

						}if(elem.getNodeName().equalsIgnoreCase("ErrorMessage")){
							sFault.setDetailErrorMessage(elem.getNodeValue());
						}
					}
				}
			}
			return sFault;
		}
		
		return null;
	}
	
	private void handleWebServiceError(Errors errors, BSROWebServiceResponse response, String requestXml,
			String errorMessage,  String statusCode, Exception e){
		errors.getGlobalErrors().add(errorMessage);
		response.setStatusCode(statusCode);
		response.setPayload(null);
		response.setErrors(errors);
		System.err.println("Request xml = " +requestXml);
		e.printStackTrace();
	}
	
}
