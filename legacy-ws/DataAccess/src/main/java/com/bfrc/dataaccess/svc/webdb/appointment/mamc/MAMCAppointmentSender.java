package com.bfrc.dataaccess.svc.webdb.appointment.mamc;

import java.io.StringReader;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.AppointmentDAO;
import com.bfrc.dataaccess.dao.generic.AppointmentSentStatusDAO;
import com.bfrc.dataaccess.exception.HttpException;
import com.bfrc.dataaccess.exception.InvalidAppointmentException;
import com.bfrc.dataaccess.http.HttpConstants.ContentType;
import com.bfrc.dataaccess.http.HttpConstants.HttpHeaderParameters;
import com.bfrc.dataaccess.http.HttpConstants.MessageCharset;
import com.bfrc.dataaccess.http.HttpResponse;
import com.bfrc.dataaccess.http.HttpUtils;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.appointment.AppointmentChoice;
import com.bfrc.dataaccess.model.appointment.AppointmentSentStatus;
import com.bfrc.dataaccess.model.appointment.AppointmentService;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.svc.webdb.appointment.AppointmentSender;
import com.bfrc.dataaccess.util.XmlUtils;

@Service
public class MAMCAppointmentSender implements AppointmentSender {

	private Logger log = Logger.getLogger(getClass().getName());
	
	@Autowired
	private AppointmentDAO appointmentDAO;
	@Autowired
	private AppointmentSentStatusDAO appointmentSentStatusDAO;
	
	public String apptSoapUri = "/MAMC_Appointment/MAMC_Appointment.asmx";
	public String apptChoiceSoapUri = "/MAMC_Appointment_Choice/MAMC_Appointment_Choice.asmx";
	public String apptServiceSoapUri = "/MAMC_Appointment_Service/MAMC_Appointment_Service.asmx";
	
	public String apptSoapAction = "http://microsoft.com/webservices/MA/InsertAppointmentData";
	public String apptChoiceSoapAction = "http://microsoft.com/webservices/MAChoice/InsertAppointmentChoiceData";
	public String apptServiceSoapAction = "http://microsoft.com/webservices/MAChoice/InsertAppointmentServiceData";

	private static final String SUCCESSFUL_REQUEST = "Record Saved";

	public void sendAppointmentData(Appointment appointment, Store store) throws HttpException, InvalidAppointmentException {

		int successCount = 0;
		Collection<Object[]> wsInfo = appointmentDAO.findWebserviceInfoByWebSiteName(StringUtils.trimToEmpty(appointment.getWebSite()));
		String webSite = null;
		String maiIp = null;
		String appName = null;
		if(wsInfo != null && wsInfo.size() > 0) {
			Object[] O = wsInfo.iterator().next();
			webSite = StringUtils.trimToNull((O[0]).toString());
			maiIp = StringUtils.trimToNull((O[1]).toString());
			appName = StringUtils.trimToNull((O[2]).toString());
		}			
		
		if(webSite == null || maiIp == null || appName == null) {
			log.severe("Cannot send appointment data to mai as site and/or ip is null.  Search by '"+appointment.getWebSite()+"' returned webSite["+webSite+"], maiIp["+maiIp+"], appName["+appName+"]!");
			saveAppointmentSentStatus(successCount, appointment.getAppointmentId());
			throw new HttpException("Invalid bfrc_webservice data: website["+webSite+"] maiIp["+maiIp+"] appName["+appName+"].");
		}
		
		Map<String, String>soapHeaderParams = new HashMap<String, String>();
		soapHeaderParams.put(HttpHeaderParameters.CONTENT_TYPE.getParam(), ContentType.TEXT_XML.getContentType());

		webSite = webSite.toUpperCase();
		successCount += sendAppointment(maiIp, appointment, store, webSite, soapHeaderParams);
		successCount += sendAppointmentServices(maiIp, appointment, webSite, soapHeaderParams);
		successCount += sendAppointmentChoices(maiIp, appointment, webSite, soapHeaderParams);
	
		saveAppointmentSentStatus(successCount, appointment.getAppointmentId());
    	
	}
	
	private void saveAppointmentSentStatus(int successCount, Long appointmentId) {
		
		String status = "S";
		if(successCount != 3) {
			log.warning("Did not successfully send appointment, service and choice: "+successCount);
			status = "F";
		}

    	AppointmentSentStatus apptSentStatus = appointmentSentStatusDAO.get(appointmentId);
    	if(apptSentStatus == null)
    		apptSentStatus = new AppointmentSentStatus();
    	
    	
    	apptSentStatus.setAppointmentId(appointmentId);
    	apptSentStatus.setStatus(status);
    	apptSentStatus.setUpdateDate(new Date());
    	
    	appointmentSentStatusDAO.save(apptSentStatus);
    	
	}
	
	private int sendAppointment(String maiIp, Appointment appointment, Store store, String appName, Map<String,String> soapHeaderParams) throws HttpException, InvalidAppointmentException {

		soapHeaderParams.put(HttpHeaderParameters.SOAP_ACTION.getParam(), apptSoapAction);
		String body = "";
		String destination = "";
		try {
			body = MAMCAppointmentBuilder.buildInsertAppointment(appointment, store, appName);
			destination = maiIp+apptSoapUri;
			HttpResponse httpResponse = 
				HttpUtils.post(
					destination, 
					body, 
					ContentType.TEXT_XML, 
					MessageCharset.UTF8, 
					soapHeaderParams);
			
			if(httpResponse.getStatusCode() < 300)
				validateResponse(httpResponse.getResponseBody(), "InsertAppointmentDataResult", SUCCESSFUL_REQUEST);
			else
				throw new HttpException("Non-Successful response ("+httpResponse.getStatusCode()+"). "+StringUtils.trimToEmpty(httpResponse.getResponseBody()));
		}catch(Exception e) {
			log.severe("Error inside MAMCAppointmentSender.sendAppointment: "+e.getMessage()+". DESTINATION: "+destination+" BODY: "+body);
			//throw new HttpException(e);
			return 0;
		}
		return 1;
	}	
	
	private int sendAppointmentChoices(String maiIp, Appointment appointment, String appName, Map<String,String> soapHeaderParams) throws HttpException, InvalidAppointmentException {

		soapHeaderParams.put(HttpHeaderParameters.SOAP_ACTION.getParam(), apptChoiceSoapAction);
		String body = "";
		String destination = maiIp+apptChoiceSoapUri;
		try {
			List<AppointmentChoice> apptChoice = appointment.getChoices();
	    	if (apptChoice.size() != 0) {
	
				for(AppointmentChoice choice : apptChoice) {
					Calendar cal = GregorianCalendar.getInstance();
					cal.setTime(choice.getDatetime());
					body = MAMCAppointmentBuilder.buildInsertAppointmentChoiceData(choice, appName);
					HttpResponse httpResponse = 
							HttpUtils.post(
								destination, 
								body, 
								ContentType.TEXT_XML, 
								MessageCharset.UTF8, 
								soapHeaderParams);
					if(httpResponse.getStatusCode() < 300)
						validateResponse(httpResponse.getResponseBody(), "InsertAppointmentChoiceDataResult", SUCCESSFUL_REQUEST);
					else
						throw new HttpException("Non-Successful response ("+httpResponse.getStatusCode()+"). "+StringUtils.trimToEmpty(httpResponse.getResponseBody()));
				}
	    	}		
    	}catch(Exception e) {
			log.severe("Error inside MAMCAppointmentSender.sendAppointmentChoices: "+e.getMessage()+". DESTINATION: "+destination+" BODY: "+body);
			//throw new HttpException(e);
			return 0;
		}
			
		return 1;
	}
	
	private int sendAppointmentServices(String maiIp, Appointment appointment, String appName, Map<String,String> soapHeaderParams) throws HttpException, InvalidAppointmentException {
		
		soapHeaderParams.put(HttpHeaderParameters.SOAP_ACTION.getParam(), apptServiceSoapAction);
		String body = "";
		String destination = maiIp+apptServiceSoapUri;
		try {
	    	Set<AppointmentService> apptService = appointment.getServices();
	    	if (apptService.size()!=0) {
	
		    	for(AppointmentService service : apptService) {
		    		body = MAMCAppointmentBuilder.buildInsertAppointmentServiceData(service.getAppointmentId(), service.getServiceNameTx(), appName);
		    		HttpResponse httpResponse = 
		    			HttpUtils.post(
		    				destination, 
		    				body, 
		    				ContentType.TEXT_XML, 
		    				MessageCharset.UTF8, 
		    				soapHeaderParams);

					if(httpResponse.getStatusCode() < 300)
						validateResponse(httpResponse.getResponseBody(), "InsertAppointmentServiceDataResult", SUCCESSFUL_REQUEST);
					else
						throw new HttpException("Non-Successful response ("+httpResponse.getStatusCode()+"). "+StringUtils.trimToEmpty(httpResponse.getResponseBody()));
	
		    	}
	    	}
		}catch(Exception e) {
			log.severe("Error inside MAMCAppointmentSender.sendAppointmentServices: "+e.getMessage()+". DESTINATION: "+destination+" BODY: "+body);
			//throw new HttpException(e);
			return 0;
		}
		return 1;
	}
	
	private void validateResponse(String result, String element, String successText) throws InvalidAppointmentException {
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document document = null;
		try {
			document = (org.jdom.Document)builder.build(new StringReader(result));
		}catch(Exception E) {
			throw new InvalidAppointmentException("Error parsing result into Document: "+E.getMessage());
		}
		Element rootNode = document.getRootElement();
		Element searchResult = XmlUtils.findElement(element, rootNode);
		if(searchResult != null) {
			String resultString = StringUtils.trimToEmpty(searchResult.getValue());
			log.fine(element+": "+resultString);
			if(!resultString.equals(successText))
				throw new InvalidAppointmentException(resultString);
		}
	}
}
