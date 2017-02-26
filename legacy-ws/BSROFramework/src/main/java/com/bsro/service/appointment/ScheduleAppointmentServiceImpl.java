package com.bsro.service.appointment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.appointment.CreateAppointment;
import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.framework.dao.AppointmentDAO;
import com.bfrc.framework.dao.EmailSignupDAO;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.appointment.Appointment;
import com.bfrc.pojo.appointment.AppointmentMetadata;
import com.bfrc.pojo.appointment.AppointmentSentStatus;
import com.bfrc.pojo.appointment.AppointmentService;
import com.bsro.pojo.appointment.ScheduleAppointmentDataBean;
import com.bsro.service.store.StoreService;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.BSROWebserviceServiceImpl;

@Service("scheduleAppointmentService")
public class ScheduleAppointmentServiceImpl extends BSROWebserviceServiceImpl implements ScheduleAppointmentService {
	private final Log logger;
	@Autowired
	private AppointmentDAO apptDAO;
	@Autowired
	private EmailSignupDAO emailSignupDAO;
	
	@Autowired
	private StoreService storeService;

	private static final String GENERAL_ERROR = "generalError";
	private static final String UNEXPECTED_ERROR = "unexpectedError";
	private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	private static final String PATH_WEBSERVICE_CREATE_APPOINTMENT = "appointment/create";
	private static final String PATH_WEBSERVICE_BOOK_APPOINTMENT = "appointment/book";
	private static final String PATH_WEBSERVICE_SEND_EMAIL_CUSTOMER = "appointment/email/customer";
	private static final String PATH_WEBSERVICE_SEND_EMAIL_STORE = "appointment/email/store";
	private static final String PATH_WEBSERVICE_CHECK_EMAIL_STATUS = "appointment/email/status";
	private static final String PATH_WEBSERVICE_STORE_SERVICES = "appointment/store-services";
	private static final String PATH_WEBSERVICE_GET_METADATA = "appointment/metadata";
	private static final String APPOINTMENT_TYPE = "new";
	private static final String FCAC_STORE = "FCAC";
	private static final String PARAM_TRACKING_ID ="trackingId";
	private static final String ERROR_WEBSERVICE_APPT_METADATA = "No available employee schedules for the selected service.";
	private static final String ERROR_WEBSERVICE_SERVICES = "Sorry, we encountered an error while contacting your store. Please call your store directly to schedule your appointment.";
	
	public ScheduleAppointmentServiceImpl() {
		this.logger = LogFactory.getLog(ScheduleAppointmentServiceImpl.class);
	}
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}


	@SuppressWarnings("unchecked")
	public ScheduleAppointmentDataBean createAppointment(Appointment appt) throws IOException {
		ScheduleAppointmentDataBean bean = new ScheduleAppointmentDataBean();
		HashMap<String, String> errors = new HashMap<String, String>();
		if (appt == null){
			errors.put(GENERAL_ERROR, "Create appointment called with null appointment.");
			bean.setErrors(errors);
		}
		// web service format data
		CreateAppointment createBean = new CreateAppointment();
		// move values from incoming bean to web service bean
		createBean.setAppName("");
		createBean.setAppointmentId("");
		createBean.setStoreId((appt.getStoreNumber() == null) ? "" : appt.getStoreNumber().toString());
		UserVehicle uv = appt.getVehicle();
		if (uv == null)
			createBean.setAcesVehicleId("0");
		else
			createBean.setAcesVehicleId((uv.getAcesVehicleId() == 0L)? "0" : new Long(uv.getAcesVehicleId()).toString());
		createBean.setYear((appt.getVehicleYear() == null) ? "" : appt.getVehicleYear().toString());
		createBean.setMake((appt.getVehicleMake() == null) ? "" : appt.getVehicleMake());
		createBean.setModel((appt.getVehicleModel() == null) ? "" : appt.getVehicleModel());
		createBean.setSubmodel((appt.getVehicleSubmodel() == null) ? "" : appt.getVehicleSubmodel());
		createBean.setMileage((appt.getMileage() == null) ? "0" : appt.getMileage().toString());
		createBean.setComments((appt.getComments() == null) ? "" : appt.getComments());
		createBean.setAddress1((appt.getAddress1() == null) ? "" : appt.getAddress1());
		createBean.setAddress2((appt.getAddress2() == null) ? "" : appt.getAddress2());
		createBean.setFirstName((appt.getFirstName() == null) ? "" : appt.getFirstName());
		createBean.setLastName((appt.getLastName() == null) ? "" : appt.getLastName());
		createBean.setCity((appt.getCity() == null) ? "" : appt.getCity());
		createBean.setState((appt.getState() == null) ? "" : appt.getState());
		createBean.setZip((appt.getZip() == null) ? "" : appt.getZip());
		createBean.setEmail((appt.getEmailAddress() == null) ? "" : appt.getEmailAddress());
		createBean.setDayPhone((appt.getDaytimePhone() == null) ? "" : appt.getDaytimePhone());
		createBean.setDayPhoneExt("");
		createBean.setEveningPhone((appt.getEveningPhone() == null) ? "" : appt.getEveningPhone());
		createBean.setEveningPhoneExt("");
		createBean.setCellPhone((appt.getCellPhone() == null) ? "" : appt.getCellPhone());
		createBean.setEmailReminder((appt.getEmailReminder() == null ? "N": appt.getEmailReminder()));
		createBean.setPhoneReminder((appt.getPhoneReminder() == null ? "N" : appt.getPhoneReminder()));
		
		if(!ServerUtil.isNullOrEmpty(appt.getEmailSignup())){	
			if(appt.getEmailSignup().equalsIgnoreCase("on"))
				createBean.setRcvPromos("true");
			else
				createBean.setRcvPromos("false");
		}else{
			createBean.setRcvPromos("false");
		}
		// Web service now supports wait or drop option in each choice - leave this one blank
		createBean.setDropWait("");
		// get services into new list
		Set<AppointmentService> services = appt.getServices();
		List<String> newServices = new ArrayList<String>();
		for(AppointmentService service : services) {
			newServices.add(service.getServiceId().toString());
		}
		createBean.setServices(newServices);
		// get choices into new list
		List<com.bfrc.pojo.appointment.AppointmentChoice> choices = appt.getChoices();
		List<app.bsro.model.appointment.AppointmentChoice> createChoices = new ArrayList<app.bsro.model.appointment.AppointmentChoice>();
		com.bfrc.pojo.appointment.AppointmentChoice oldChoice = null;
//		String dropWait = null;
		DecimalFormat df = new DecimalFormat("00");
		for(com.bfrc.pojo.appointment.AppointmentChoice choice : choices) {
			app.bsro.model.appointment.AppointmentChoice createChoice = new app.bsro.model.appointment.AppointmentChoice();
			GregorianCalendar cal = new GregorianCalendar();
//			GregorianCalendar cal2 = new GregorianCalendar();			
//			if(storeService.isApptPilotStore(appt.getStoreNumber()) && choice.getDropWaitOption().equalsIgnoreCase("drop")){
//				cal.setTime(choice.getDropOffTime());
//				cal2.setTime(choice.getPickUpTime());
//			}else{
//				cal.setTime(choice.getDatetime());
//			}
//			if (dropWait == null)
			createChoice.setDropWait(choice.getDropWaitOption());
			createChoice.setMonth(months[cal.get(Calendar.MONTH)]);
			createChoice.setDate(df.format(cal.get(Calendar.DAY_OF_MONTH)));
			createChoice.setTime(df.format(cal.get(Calendar.HOUR_OF_DAY))+":"+df.format(cal.get(Calendar.MINUTE)));
			createChoices.add(createChoice);
		}
		createBean.setChoices(createChoices);
//		createBean.setDropWait(dropWait);

		createBean.setBatteryQuoteId((appt.getBatteryQuoteId() == null) ? "" : appt.getBatteryQuoteId().toString());
		// log when Debug
		//load the bean to book and send appointment email data
		AppointmentData apptDetails = apptDAO.createAppointmentDataBean(appt);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonRequest = new String();
		try{
			jsonRequest = mapper.writeValueAsString(apptDetails);
							
		} catch (Exception e){
			e.printStackTrace();
		}
		Util.debug("Send to Create Appointment webservice: "+jsonRequest.toString());

		com.bfrc.dataaccess.model.appointment.Appointment recAppt = null;
		String recApptId = "";
		String customerId = "";
		StringBuilder webservicePath = null;
		String webserviceError = "";

		try {
			LinkedHashMap<String,Object> results = new LinkedHashMap<String,Object>();
				
			// send appointment to Appointment Plus
			BSROWebServiceResponse bookApptResponse = callBookAppointmentWebservice(apptDetails); 
			if(bookApptResponse != null && bookApptResponse.getErrors() != null && bookApptResponse.getErrors().hasGlobalErrors()){
				webserviceError = bookApptResponse.getErrors().getGlobalErrors().get(0);
				errors.put(GENERAL_ERROR, webserviceError);
				bean.setErrors(errors);
				logError(bookApptResponse.getStatusCode(), webserviceError);
				throw new IOException(webserviceError);
					
			} else if (bookApptResponse != null && bookApptResponse.getPayload() != null) {
				results = (LinkedHashMap<String,Object>)bookApptResponse.getPayload();					
				recApptId = String.valueOf(results.get("appointmentId"));
				customerId = String.valueOf(results.get("customerId"));
			}

		} catch (Exception e) {
			String displayError = "There was an unexpected error with the book appointment service";
			System.err.println(displayError + " in class "+this.getClass().getSimpleName());
			errors.put(UNEXPECTED_ERROR, displayError);
			bean.setErrors(errors);
			webserviceError = e.toString();
			e.printStackTrace();
		}
		finally{
			// save appointment to database				
			saveAppointment(appt, recApptId, customerId, 
				webserviceError.length()> 1000 ? webserviceError.substring(0, 1000):webserviceError);
		}
		
			
		bean.setAppointment(appt);
		return bean;
	}
	
	public BSROWebServiceResponse callBookAppointmentWebservice(AppointmentData apptDetails) throws IOException{ 
		StringBuilder webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_BOOK_APPOINTMENT);
		return (BSROWebServiceResponse)this.postJSONToWebservice(webservicePath.toString(), apptDetails, BSROWebServiceResponse.class);
	}

	private void saveAppointment (Appointment appt, String bookingId, String customerId, String errorMessage) {
		ScheduleAppointmentDataBean bean = new ScheduleAppointmentDataBean();
		// Old method - save to db yourself		
		try {
			// hang onto those extra guys
			Set<AppointmentService> services = appt.getServices();
			List<com.bfrc.pojo.appointment.AppointmentChoice> choices = appt.getChoices();
			//have to set to new because Hibernate complains if we don't.  
			appt.setServices(new HashSet());
			appt.setChoices(new ArrayList());
			//combine the miscellaneous repair comment with the additional comments
			if(appt.getCustomerComments()!= null && !appt.getCustomerComments().isEmpty()){
				StringBuffer comments = new StringBuffer(appt.getComments());
				if(comments.length() > 0)
					comments.append(", ");
				comments.append("Repair Comments: ").append(appt.getCustomerComments());
				appt.setComments(comments.toString());
			}
			//save appointment
			apptDAO.addAppointment(appt);
			
			//save the metadata if available
			//WI-4031: add for purpose of re-try for failed appointments
			AppointmentMetadata metadata = new AppointmentMetadata();
			metadata.setAppointmentId(appt.getAppointmentId());
			metadata.setLocationId(appt.getLocationId());
			metadata.setEmployeeId(appt.getEmployeeId());
			metadata.setAppointmentStatusId(appt.getAppointmentStatusId());
			metadata.setOtherDetails("Services="+appt.getAp_serviceDescriptions()
					+ " Status="+appt.getAppointmentStatusDesc());
			metadata.setServicesCSV(appt.getServiceIds());
			metadata.setCreatedDate(new Date());
			apptDAO.addAppointmentMetadata(metadata);
		
			//saving appointment services
			// most fields already set
			for(AppointmentService service : services) {
				service.setAppointmentServiceId(0L);
				service.setAppointmentId(appt.getAppointmentId());
				apptDAO.addAppointmentService(service);
			}
			//set them back for display
			appt.setServices(services);
			
			//saving appointment choices
			// most fields already set
			for(com.bfrc.pojo.appointment.AppointmentChoice choice : choices) {
				choice.setAppointmentChoiceId(0L);
				choice.setAppointmentId(appt.getAppointmentId());
				apptDAO.addAppointmentChoice(choice);
			}
			//set them back for display
			appt.setChoices(choices);
			
			//save email signup
			if("on".equals(appt.getEmailSignup())) {
				com.bfrc.pojo.email.EmailSignup signup = new com.bfrc.pojo.email.EmailSignup();
				signup.setEmailAddress(appt.getEmailAddress());
				signup.setFirstName(appt.getFirstName());
				signup.setLastName(appt.getLastName());
				signup.setAddress1(appt.getAddress1());
				signup.setAddress2(appt.getAddress2());
				signup.setCity(appt.getCity());
				signup.setState(appt.getState());
				signup.setZip(appt.getZip());
				signup.setSource(appt.getWebSite() + " Online Appointment");
				//signup.setSource(appt.getWebSite());
				signup.setCreatedDate(new Date());
				emailSignupDAO.subscribe(signup);
			}

			
			AppointmentSentStatus apptSentStatus = new AppointmentSentStatus();
			//should have this ID after we added via apptDao
	    	apptSentStatus.setAppointmentId(appt.getAppointmentId());
	    	if(bookingId.isEmpty()){
	    		//this is IMPORTANT
				//set the status as retry to be picked up by the retry processor.
	    		apptSentStatus.setStatus("R");
	    		apptSentStatus.setEmailStatusMessage(errorMessage);
	    	}
	    	else{
	    		apptSentStatus.setBookingConfirmationId(bookingId);
	    		apptSentStatus.setEmailTrackingNumber("Customer Id: "+customerId);
	    		apptSentStatus.setStatus("S"); //status is 'success' in this case
	    		apptSentStatus.setEmailStatusMessage("Successful");
	    	}

	    	apptSentStatus.setUpdateDate(new Date());
	    	apptDAO.saveAppointmentSentStatus(apptSentStatus);

	    } catch (Exception e){
	    	System.err.println("After failing to send to the BSRO web service, the appointment also failed saving directly to database.");
	    	e.printStackTrace();
	    }

	}
	
	private void logError(String code, String message){
		System.err.println("Error Code => "+ code);
		System.err.println("Error Message => "+ message);
	}
	
	@SuppressWarnings("unchecked")
	public List<LinkedHashMap<String,String>> getServicesFromAppointmentPlus(){
		logger.info("Inside getServicesFromAppointmentPlus ");
		BSROWebServiceResponse response = null;
		List<LinkedHashMap<String,String>> errors = new ArrayList<LinkedHashMap<String,String>>();
		LinkedHashMap<String, String> errorMap = new LinkedHashMap<String, String>();
		StringBuilder webservicePath = null;
		try {
			webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_STORE_SERVICES);
			response = (BSROWebServiceResponse) getWebservice(webservicePath.toString(), new LinkedHashMap<String, String>(), BSROWebServiceResponse.class);
		} catch (IOException e) {
			logger.error("Error calling webservice method at path "+ webservicePath + "in method getAvailableDays");
			e.printStackTrace();
		}
		if(response != null){
			if(response.getPayload() !=null){
				return (List<LinkedHashMap<String,String>>)response.getPayload();
			}else{
				if(response.getErrors()!= null && response.getErrors().hasGlobalErrors()){
					for(int i =0; i < response.getErrors().getGlobalErrors().size(); i++){
						if(i==0){
							logError(response.getStatusCode(), response.getErrors().getGlobalErrors().get(i));
						}
						errorMap.put("error"+i, response.getErrors().getGlobalErrors().get(i));
					}
					errors.add(errorMap);
				}
			}
		}
		if(errors.isEmpty()){
			errorMap.put("error0", ERROR_WEBSERVICE_SERVICES);
			errors.add(errorMap);
		}
		return errors;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,HashMap<String,String>> getAppointmentMetadata(String storeNumber, List<String> selectedServiceDesc){
		logger.info("Inside getAppointmentMetadata sn = "+ storeNumber
				+ " selectedServiceDescCSV len = "+selectedServiceDesc.size());
		String selectedServicesCSV = org.apache.commons.lang3.StringUtils.join(selectedServiceDesc, ",");
		Map<String,HashMap<String,String>> results = new HashMap<String,HashMap<String,String>>();
		HashMap<String,String> wsErrors = new HashMap<String,String>();
		if(!ServerUtil.isNullOrEmpty(storeNumber) && !ServerUtil.isNullOrEmpty(selectedServicesCSV)){
			Map<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("storeNumber", storeNumber);
			parameters.put("services", selectedServicesCSV);
			StringBuilder webservicePath = null;
			BSROWebServiceResponse response = null;
			try {
				webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_GET_METADATA);
				response = (BSROWebServiceResponse) getWebservice(webservicePath.toString(), parameters, BSROWebServiceResponse.class);
			} catch (IOException e) {
				logger.error("Error calling webservice method at path "+ webservicePath + "in method getAvailableDays");
				e.printStackTrace();
			}
			if(response != null){
				if(response.getPayload() != null){
					results.put("data", (LinkedHashMap<String,String>)response.getPayload());
					return results;
				}
				else{
					if(response.getErrors()!= null && response.getErrors().hasGlobalErrors()){
						for(int i =0; i < response.getErrors().getGlobalErrors().size(); i++){
							if(i==0){
								logError(response.getStatusCode(), response.getErrors().getGlobalErrors().get(i));
							}
							wsErrors.put(response.getStatusCode(), response.getErrors().getGlobalErrors().get(i));
						}
						results.put("error", wsErrors);
					}
				}
			}
		}
		if(results.isEmpty()){
			wsErrors.put("genericError", ERROR_WEBSERVICE_APPT_METADATA);
			results.put("error", wsErrors);
		}
		return results;
	}
}
