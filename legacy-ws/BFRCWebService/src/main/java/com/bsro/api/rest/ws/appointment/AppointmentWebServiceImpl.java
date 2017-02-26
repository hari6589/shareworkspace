package com.bsro.api.rest.ws.appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.appointment.CreateAppointment;
import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.exception.InvalidAppointmentException;
import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.appointment.AppointmentChoice;
import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.dataaccess.model.appointment.AppointmentService;
import com.bfrc.dataaccess.model.appointment.AppointmentServiceDesc;
import com.bfrc.dataaccess.model.appointment.email.AppointmentDetails;
import com.bfrc.dataaccess.svc.webdb.IAppointmentService;
import com.bfrc.dataaccess.svc.webdb.appointment.AppointmentEmailSender;
import com.bfrc.dataaccess.svc.webdb.appointment.AppointmentPlusScheduler;
import com.bfrc.dataaccess.svc.webdb.appointment.AppointmentSchedulerConfig;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.pojo.appointment.AppointmentSchedule;
import com.bsro.core.exception.ws.EmptyDataSetException;
import com.bsro.core.exception.ws.InvalidArgumentException;
import com.bsro.core.exception.ws.ServerProcessingException;
import com.bsro.core.model.HttpHeader;
import com.bsro.core.security.RequireValidToken;
import com.bsro.core.validation.ErrorList;
import com.bsro.core.validation.Validator;
import com.bsro.pojo.appointment.DateStringComparator;
import com.bsro.pojo.appointment.MonthNameComparator;
import com.bsro.service.appointment.AppointmentTimesService;

/**
 * Implementation of the appointment web service.  This service will create an appointment entry in the
 * BSRO database as well as send the appointment request to a 3rd party.
 * @author Brad Balmer
 *
 */
@Component
public class AppointmentWebServiceImpl implements AppointmentWebService, InitializingBean {

	private Logger log = Logger.getLogger(getClass().getName());
	private static final String DROP_WAIT_OPTION_DROP= "Drop";
	private static final String DROP_WAIT_OPTION_WAIT= "Wait";
	private static final String INVALID_DATE_FORMAT = "Invalid Selected date format"; 
	
	@Autowired
	private IAppointmentService appointmentService;
	
	@Autowired
	private AppointmentSchedulerConfig appointmentSchedulerConfig;
		
	@Autowired
	private AppointmentEmailSender appointmentEmailSender;
	
	@Autowired
	AppointmentPlusScheduler appointmentPlusScheduler;
	
	@Autowired
	AppointmentTimesService appointmentTimesService;
	
	private Map<Integer, String> serviceCache;
	

	/**
	 * @param appointmentSchedulerConfig the appointmentSchedulerConfig to set
	 */
	public void setAppointmentSchedulerConfig(
			AppointmentSchedulerConfig appointmentSchedulerConfig) {
		this.appointmentSchedulerConfig = appointmentSchedulerConfig;
	}

	/**
	 * @param appointmentEmailSender the appointmentEmailSender to set
	 */
	public void setAppointmentEmailSender(
			AppointmentEmailSender appointmentEmailSender) {
		this.appointmentEmailSender = appointmentEmailSender;
	}

	public void afterPropertiesSet() throws Exception {

		serviceCache = new HashMap<Integer, String>();
		try {
			Collection<AppointmentServiceDesc> services = appointmentService.listAppointmentServices();
			for(AppointmentServiceDesc svc : services) {
				serviceCache.put(svc.getServiceId(), svc.getServiceDesc());
			}
		}catch(Exception E) {}
	}

	@Override
	@RequireValidToken
	public List<ValueTextBean> serviceList( HttpHeaders headers ) {
		
		Collection<AppointmentServiceDesc> services = null;
		List<ValueTextBean> loResult = new ArrayList<ValueTextBean>();
		try {
			services = appointmentService.listAppointmentServices();
			if(services == null || services.size() == 0)
				throw new EmptyDataSetException("Request returned no services.");
		}catch(Exception E) {
			String msg = "Error processing your request: "+E.getMessage();
			log.severe(msg);
			throw new ServerProcessingException(msg);
		}
		
		for(AppointmentServiceDesc svc : services) {
			loResult.add(new ValueTextBean(svc.getServiceId().toString(), svc.getServiceDesc()));
		}
	
		return loResult;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreServices( Long storeNumber,HttpHeaders headers ) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Collection<AppointmentServiceDesc> services = appointmentPlusScheduler.getServicesFromDB(storeNumber);

		if(services == null || services.isEmpty()){
			response.setMessage(ValidationConstants.NO_DATA_FOUND);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		response.setPayload(services);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}
		
	
	/**
	 * Creates the appointment after first validating the user input.
	 */
	@Override
	@RequireValidToken
	public Response createAppointment(CreateAppointment appt, HttpHeaders headers) {
		
		log.fine(appt.toString());
		
		ErrorList errors = validate(appt);
		if(errors != null && errors.hasErrors())
			throw new InvalidArgumentException(errors);
		
		String globalAppName = null;
		String globalAppSource = null;
		try {
			globalAppName = headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0);
			globalAppSource = headers.getRequestHeader(HttpHeader.Params.APP_SOURCE.getValue()).get(0);
		}catch(Exception E) {
			// do nothing
		}
		
		Appointment appointment = null;
		try {
			log.fine("["+this.getClass().getName()+"]-"+appt.toString());
			
			appointment = buildAppointment(appt);
			appointment.setWebSite(globalAppName);
			appointment.setWebSiteSource(globalAppSource);
			
			//The globalAppName is the appName coming from the HTTPHeader.  IF there is also an appName
			// set in the Appointment data passed in then use what is in the appointment data, otherwise
			// use the appName from the HTTPHeader
			//WI-2575: no need get acesVehicleId to retrieve fitment data from database
			//Long acesVehicleId = null;
			//if(appt.getaAcesVehicleId() != null && !appt.getAcesVehicleId().isEmpty()){
			//	acesVehicleId = new Long(appt.getAcesVehicleId());
			//}
			appointmentService.createAppointment(
					appointment, 
					globalAppName);
//					globalAppName==null?StringUtils.trimToEmpty(appt.getAppName()):globalAppName);
			

		}catch(Exception e) {
			log.severe("Error processing appointment creation: "+e.getMessage());
			e.printStackTrace();
			throw new ServerProcessingException(e.getMessage());
		}
		

		ResponseBuilder response = Response.status(Status.CREATED);
		response.entity(appointment);
		return response.build();
	
	}

	/**
	 * Converts the {@link CreateAppointment} Object into an {@link Appointment} object.
	 * @param appt
	 * @return
	 */
	private Appointment buildAppointment(CreateAppointment appt) {
		
		Appointment appointment = new Appointment();
		
		appointment.setStoreNumber( com.bfrc.framework.util.StringUtils.isNullOrEmpty(appt.getStoreId()) ? null : new Long(appt.getStoreId()) );
		appointment.setMileage( com.bfrc.framework.util.StringUtils.isNullOrEmpty(appt.getMileage()) ? null : new Integer(appt.getMileage()) );
		appointment.setComments(appt.getComments());
		appointment.setEmailAddress(appt.getEmail());
		appointment.setDaytimePhone(appt.getDayPhone());
		appointment.setEveningPhone(appt.getEveningPhone());
		appointment.setCellPhone(appt.getCellPhone());
		appointment.setEmailSignup(appt.getRcvPromos());
		appointment.setFirstName(appt.getFirstName());
		appointment.setLastName(appt.getLastName());
		appointment.setAddress1(appt.getAddress1());
		appointment.setAddress2(appt.getAddress2());
		appointment.setCity(appt.getCity());
		appointment.setState(appt.getState());
		appointment.setZip(appt.getZip());
		appointment.setEmailReminder(appt.getEmailReminder());
		appointment.setPhoneReminder(appt.getPhoneReminder());
		appointment.setVehicleId( com.bfrc.framework.util.StringUtils.isNullOrEmpty(appt.getAcesVehicleId()) ? null : new Long(appt.getAcesVehicleId()) );
		appointment.setVehicleYear( com.bfrc.framework.util.StringUtils.isNullOrEmpty(appt.getYear()) ? null : new Long(appt.getYear()) );
		appointment.setVehicleMake(appt.getMake());
		appointment.setVehicleModel(appt.getModel());
		appointment.setVehicleSubmodel(appt.getSubmodel());

		if(appt.getBatteryQuoteId() != null && !StringUtils.trimToEmpty(appt.getBatteryQuoteId()).isEmpty()) {
			try {
				appointment.setBatteryQuoteId(new Long(appt.getBatteryQuoteId()));
			} catch (NumberFormatException numberFormatException) {
				if (log.isLoggable(Level.WARNING)) {
					log.warning("Received invalid battery quote id: "+appt.getBatteryQuoteId()+", ignoring");
				}
			}
		}
		
		Set<AppointmentService> services = new HashSet<AppointmentService>();
		List<AppointmentChoice> choices = new ArrayList<AppointmentChoice>();
		
		if(appt.getServices() != null) {
			long ZERO = 0;
			for(String i : appt.getServices()) {
				String desc = StringUtils.trimToEmpty(serviceCache.get(new Integer(i)));
				services.add(new AppointmentService(ZERO, new Integer(i), desc, ZERO));
			}
		}
		
		if(appt.getChoices() != null) {
			int idx = 1;
			for(app.bsro.model.appointment.AppointmentChoice choice : appt.getChoices()) {
				Date datetime = appointmentService.getChoiceDatetime(choice.getMonth(), choice.getDate(), choice.getTime());
				
				String dropWait = StringUtils.trimToNull(choice.getDropWait());
				if(dropWait == null)
					dropWait = appt.getDropWait() == null?"drop":appt.getDropWait();
				
				choices.add(new AppointmentChoice(0l, idx, datetime, dropWait, 0l));
				idx++;
			}
		}
		
		appointment.setServices(services);
		appointment.setChoices(choices);
		
		return appointment;
	}
	
	
	/**
	 * Validation of the CreateAppointment Object
	 * @param appt
	 * @return
	 */
	private ErrorList validate(CreateAppointment appt) {
				
		ErrorList errors = new ErrorList();
		
		Validator.isValidLong("storeId", appt.getStoreId(), Long.MIN_VALUE, Long.MAX_VALUE, false, errors);
		//WI-2048: Appointments can have no vehicle records associated
		//Validator.isValidInteger("acesVehicleId", appt.getAcesVehicleId(), 1, Integer.MAX_VALUE, false, errors);
		//Validator.isValidInteger("mileage", appt.getMileage(), 1, Integer.MAX_VALUE, false, errors);
		Validator.isValidInteger("services", appt.getServices() != null?Integer.toString(appt.getServices().size()):"0", 1, Integer.MAX_VALUE, false, errors);
		Validator.isValidInteger("choices", appt.getChoices() != null?Integer.toString(appt.getChoices().size()):"0", 1, Integer.MAX_VALUE, false, errors);
		Validator.isValidString("firstName", appt.getFirstName(), 1, 50, false, errors);
		Validator.isValidString("lastName", appt.getLastName(), 1, 80, false, errors);
		
		return errors;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAvailabilityByDay(Long locationId, String startDate,
			Integer numDays, Long employeeId, HttpHeaders headers) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date initialDate = null;
		try {
			initialDate = dateFormat.parse(startDate);
		} catch (ParseException e) {
			BSROWebServiceResponse res = new BSROWebServiceResponse();
			res.setMessage(INVALID_DATE_FORMAT);
			res.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return res;
		}
		return appointmentPlusScheduler.getStaffOpenDates(locationId, startDate, numDays, employeeId);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAvailability(Long locationId, Long employeeId,
			   						String selectedDate,  String serviceIdCSV, 
			   						HttpHeaders headers) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date apptDate = null;
		try {
			apptDate = dateFormat.parse(selectedDate);
		} catch (ParseException e) {
			BSROWebServiceResponse res = new BSROWebServiceResponse();
			res.setMessage(INVALID_DATE_FORMAT);
			res.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return res;
		}
		//start from midnight, get slots for entire day if selected day is future
		Integer startTimeInMins = 0;
		
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(apptDate);
		if(cal.before(today)){
			SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
			String currentHourMin = hmFormat.format(today.getTime());
			String[] hourMinSplits = currentHourMin.split(":");
			startTimeInMins = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
		}
		
		int numDays = 1; //we always want slots for the selected day only
		int firstDelimIdx = serviceIdCSV.indexOf(",");
		String primaryServiceId ="";
		String secondaryServiceIds ="";
		if(firstDelimIdx != -1){
			primaryServiceId = serviceIdCSV.substring(0, firstDelimIdx);
			secondaryServiceIds = serviceIdCSV.substring(firstDelimIdx+1);
		}else{
			primaryServiceId = serviceIdCSV;
		}
		
		return appointmentPlusScheduler.getAppointmentOpenSlots(locationId, employeeId, selectedDate, startTimeInMins, 
				numDays, primaryServiceId, secondaryServiceIds);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse bookAppointment(com.bfrc.dataaccess.model.appointment.AppointmentData appointment, HttpHeaders headers) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
		String dateStr = dateFormat.format(appointment.getChoice().getDatetime());

		Date choiceDateTime = null;
		try {
			choiceDateTime = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			BSROWebServiceResponse res = new BSROWebServiceResponse();
			res.setMessage(INVALID_DATE_FORMAT);
			res.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			return res;
		}

		String[] dateSplits = dateStr.split("\\s+");
		String selectedDate = dateSplits[0];
		String[] hourMinSplits = dateSplits[1].split(":");
		Integer selectedTime = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
		
		int firstDelimIdx = appointment.getSelectedServices().indexOf(",");
		String primaryServiceId	= "";
		String additionalServiceIds	=	"";
		if(firstDelimIdx != -1){
			primaryServiceId = appointment.getSelectedServices().substring(0, firstDelimIdx);
			additionalServiceIds = appointment.getSelectedServices().substring(firstDelimIdx+1);
		}else{
			primaryServiceId = appointment.getSelectedServices();
		}

		BSROWebServiceResponse response = null;
		try {
			response = appointmentPlusScheduler.createAppointment(appointment, selectedDate, selectedTime, primaryServiceId, additionalServiceIds);
		} catch (Exception e1) {
			System.err.println("Error in scheduling appointment");
		}
		finally{
			if(response != null && response.getPayload() != null){
				try {
					AppointmentData responseData = (AppointmentData)response.getPayload();
					appointmentService.saveAppointment(appointment, String.valueOf(responseData.getAppointmentId()), 
							"Customer #:"+String.valueOf(responseData.getCustomerId()), appointment.getAppointmentStatusDesc());
				} catch (InvalidAppointmentException e) {
					e.printStackTrace();
				}
			}
			else{
				String webserviceError = "";
				try {
					if(response != null && response.getErrors() != null)
						webserviceError = response.getErrors().getGlobalErrors().get(0);
				
					appointmentService.saveAppointment(appointment, "", "", webserviceError);
				} catch (InvalidAppointmentException e) {
					e.printStackTrace();
				}
			}
		}
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse sendEmailDataForCustomer(com.bfrc.dataaccess.model.appointment.AppointmentData appt, HttpHeaders headers) {
		AppointmentDetails apptDetails = createAppointmentDetailsBean(appt, true);
		String trackingId = appointmentEmailSender.sendEmailData(apptDetails, true);
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setPayload(trackingId);
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse sendEmailDataForStore(com.bfrc.dataaccess.model.appointment.AppointmentData appt, HttpHeaders headers) {
		AppointmentDetails apptDetails = createAppointmentDetailsBean(appt, false);
		String trackingId = appointmentEmailSender.sendEmailData(apptDetails, false);
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setPayload(trackingId);
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse checkProcessingStatus(String trackingId, HttpHeaders headers) {
		String trackingMessage= appointmentEmailSender.checkProcessingStatus(trackingId);
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setPayload(trackingMessage);
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAppointmentMetadata(Long storeNumber,
			String serviceDescCSV, HttpHeaders headers){
		return appointmentPlusScheduler.getBridgestoneAppointmentRules(storeNumber, serviceDescCSV);
	}
	
	private AppointmentDetails createAppointmentDetailsBean(com.bfrc.dataaccess.model.appointment.AppointmentData appt, boolean isCustomer){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		AppointmentDetails apptDetails = new AppointmentDetails();
		Date now = new Date();
		String datetime = sdf.format(now);
		apptDetails.setTransactionId(String.valueOf(appt.getStoreNumber())+datetime);
		apptDetails.setAppointmentId(String.valueOf(appt.getAppointmentId()));
		apptDetails.setStoreNumber(String.valueOf(appt.getStoreNumber()));
//		apptDetails.setStoreName(appt.getStoreName());
//		apptDetails.setStoreAddress(appt.getStoreAddress());
//		apptDetails.setStoreCity(appt.getStoreCity());
//		apptDetails.setStoreState(appt.getStoreState());
//		apptDetails.setStoreZip(appt.getStoreZip());
//		apptDetails.setStorePhone(appt.getStorePhone());
//		apptDetails.setStoreFax(appt.getStoreFax());
//		apptDetails.setStoreEmail(appt.getStoreEmail());
//
//		if(!ServerUtil.isNullOrEmpty(appt.getStoreManagerName())){
//			String[] tokens = appt.getStoreManagerName().split(" ");
//			if(tokens != null && tokens.length > 0 && !ServerUtil.isNullOrEmpty(tokens[0])){
//				apptDetails.setStoreManagerFirstName(tokens[0]);
//			}else if(tokens != null && tokens.length > 1 && !ServerUtil.isNullOrEmpty(tokens[1])){
//				apptDetails.setStoreManagerLastName(tokens[1]);
//			}
//		}
		apptDetails.setVehicleYear(String.valueOf(appt.getVehicleYear()));
		apptDetails.setVehicleMake(appt.getVehicleMake());
		apptDetails.setVehicleModel(appt.getVehicleModel());
		apptDetails.setVehicleSubmodel(appt.getVehicleSubmodel());
		apptDetails.setMileage(String.valueOf(appt.getMileage()));
		apptDetails.setComments(appt.getComments());
		apptDetails.setCustomerFirstName(appt.getCustomerFirstName());
		apptDetails.setCustomerLastName(appt.getCustomerLastName());
		apptDetails.setCustomerAddress1(appt.getCustomerAddress1());
		apptDetails.setCustomerAddress2(appt.getCustomerAddress2());
		apptDetails.setCustomerCity(appt.getCustomerCity());
		apptDetails.setCustomerState(appt.getCustomerState());
		apptDetails.setCustomerZipCode(appt.getCustomerZipCode());
		apptDetails.setCustomerDayTimePhone(appt.getCustomerDayTimePhone());
		apptDetails.setCustomerEveningPhone(appt.getCustomerEveningPhone());
		apptDetails.setCustomerCellPhone(appt.getCustomerCellPhone());
		apptDetails.setCustomerEmailAddress(appt.getCustomerEmailAddress());
//		if(isCustomer){
//			apptDetails.setEmail(appt.getCustomerEmailAddress());
//		}else{
//			apptDetails.setEmail(appt.getStoreEmail());
//		}
		//apptDetails.setReceivePromos(appt.getEmailSignup());
		apptDetails.setAppointmentType(appt.getAppointmentType());
		if(appt.getChoice()!= null){
			AppointmentChoice choice = appt.getChoice();
			if("drop".equalsIgnoreCase(choice.getDropWaitOption())){
				apptDetails.setDropWaitOption(DROP_WAIT_OPTION_DROP);	
			}else{
				apptDetails.setDropWaitOption(DROP_WAIT_OPTION_WAIT);	
			}
			if(choice.getDropOffTime() != null){
				apptDetails.setDropOffDateTime(format.format(choice.getDropOffTime()));
			}else{
				apptDetails.setDropOffDateTime("");
			}
			if(choice.getPickUpTime() != null){
				apptDetails.setPickUpDateTime(format.format(choice.getPickUpTime()));
			}else{
				apptDetails.setPickUpDateTime("");
			}
			if(choice.getDatetime() != null){
				apptDetails.setAppointmentDateTime(format.format(choice.getDatetime()));
			}else{
				apptDetails.setAppointmentDateTime("");
			}	
		}
		
		apptDetails.setSelectedServices(appt.getSelectedServices());
		apptDetails.setCreatedDate(format.format(new Date()));
		apptDetails.setWebsiteName(appt.getWebsiteName());

		return apptDetails;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAppointmentAvailableSchedules(
			Long storeNumber, String month, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Map<String, SortedSet<String>> data = new HashMap<String, SortedSet<String>>();
		if (com.bfrc.framework.util.StringUtils.isNullOrEmpty(storeNumber)) {
			Errors errors = new Errors();
			errors.getGlobalErrors().add("Invalid store number.");
			response.setErrors(errors);
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setPayload(null);
			
			return response;
		}
		
		try {
			AppointmentSchedule appointmentSchedule = appointmentTimesService.createAppointmentSchedule(storeNumber, new Date());
			if ((month == null || month.isEmpty())) {
				Set<String> yearsList = appointmentSchedule.getYearMap().keySet();
				SortedSet<String> sortedYears = new TreeSet<String>(yearsList);
				SortedSet<String> sortedMonths = new TreeSet<String>(new MonthNameComparator());
				for (String currentYear : sortedYears) {
					List<String> monthsList = appointmentSchedule.getYearMap().get(currentYear);
					sortedMonths.clear();
					sortedMonths.addAll(monthsList);
					data.put("month", sortedMonths);
					
					response.setPayload(data);
					response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
				}
			} else {
				List<String> dates = appointmentSchedule.getDates(month);
				SortedSet<String> sortedDates = new TreeSet<String>(new DateStringComparator());
				sortedDates.addAll(dates);
				data.put("date", sortedDates);
				
				response.setPayload(data);
				response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			}
		} catch (Exception e) {
			//do nothing return empty list
			Errors errors = new Errors();
			if ((month == null || month.isEmpty())) {
				errors.getGlobalErrors().add("Problem retrieving appointment availability months");
			} else {
				errors.getGlobalErrors().add("Problem retrieving appointment availability dates");
			}
			response.setErrors(errors);
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setPayload(null);
		}
				
		return response;
	}

}
