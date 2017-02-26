package com.bfrc.dataaccess.svc.webdb.appointment;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.dao.generic.AppointmentChoiceDAO;
import com.bfrc.dataaccess.dao.generic.AppointmentCustomerDAO;
import com.bfrc.dataaccess.dao.generic.AppointmentDAO;
import com.bfrc.dataaccess.dao.generic.AppointmentMetadataDAO;
import com.bfrc.dataaccess.dao.generic.AppointmentSentStatusDAO;
import com.bfrc.dataaccess.dao.generic.AppointmentServiceDAO;
import com.bfrc.dataaccess.dao.generic.AppointmentServiceDescDAO;
import com.bfrc.dataaccess.dao.generic.FitmentDAO;
import com.bfrc.dataaccess.dao.generic.StoreDAO;
import com.bfrc.dataaccess.exception.HttpException;
import com.bfrc.dataaccess.exception.InvalidAppointmentException;
import com.bfrc.dataaccess.exception.InvalidEmailException;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.appointment.AppointmentChoice;
import com.bfrc.dataaccess.model.appointment.AppointmentCustomer;
import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.dataaccess.model.appointment.AppointmentMetadata;
import com.bfrc.dataaccess.model.appointment.AppointmentSentStatus;
import com.bfrc.dataaccess.model.appointment.AppointmentService;
import com.bfrc.dataaccess.model.appointment.AppointmentServiceDesc;
import com.bfrc.dataaccess.model.email.EmailSignup;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.vehicle.Fitment;
import com.bfrc.dataaccess.svc.mail.MailService;
import com.bfrc.dataaccess.svc.webdb.EmailService;
import com.bfrc.dataaccess.svc.webdb.IAppointmentService;

@Service("iAppointmentService")
public class AppointmentServiceImpl implements IAppointmentService {
	private Logger log = Logger.getLogger(getClass().getName());
	
	@Autowired
	private AppointmentDAO appointmentDAO;
	@Autowired
	private FitmentDAO fitmentDAO;
	@Autowired
	private AppointmentServiceDAO appointmentServiceDAO;
	@Autowired
	private AppointmentChoiceDAO appointmentChoiceDAO;
	@Autowired
	private AppointmentServiceDescDAO appointmentServiceDescDAO;
	@Autowired
	private AppointmentSentStatusDAO appointmentSentStatusDAO;
	@Autowired
	private AppointmentMetadataDAO appointmentMetadataDAO;
	@Autowired
	private AppointmentCustomerDAO appointmentCustomerDAO;
	@Autowired
	private StoreDAO storeDAO;
	@Autowired
	private EmailService emailService;
	@Autowired
	private MailService mailService;
	@Autowired
	private AppointmentSender appointmentSender;
	@Autowired
	AppointmentPlusScheduler appointmentPlusScheduler;
	
	private Long ZERO = new Long(0);

//	public final static String RECORD_SAVED = "Record Saved";
//	public final static String APPT_EXISTS = "Appointment ID Exists";
//	public final static String APPT_CHOICE_EXISTS = "Appointment ID With this Choice already Exists";
//	public final static String APPT_SERVICE_EXISTS = "Appointment ID With this Service already Exists";

	private static Map<String, Integer> months;
	static {
		months = new HashMap<String, Integer>(12);
		DateFormatSymbols dfs = new DateFormatSymbols();
	    String[] mths = dfs.getMonths();
		for(int i=0;i<mths.length;i++) {
			months.put(StringUtils.trimToEmpty(mths[i]).toLowerCase(), i);
		}
	}
	
	public Collection<AppointmentServiceDesc> listAppointmentServices() {
		Collection<AppointmentServiceDesc> serviceDescs = new ArrayList<AppointmentServiceDesc>();
		Collection<AppointmentServiceDesc> results = appointmentServiceDescDAO.listAll();
		//TODO: WI-4297: remove this and use named query for performance purposes.
		for(AppointmentServiceDesc result : results){
			if(result.getServiceType() == 1){
				serviceDescs.add(result);
			}
		}
		return serviceDescs;
	}
	
	public AppointmentServiceDesc getAppointmentServiceDesc(Integer id) {
		return appointmentServiceDescDAO.get(id);
	}
	
	public String getAppointmentServiceDescs(String serviceIds) {
		if (serviceIds == null || serviceIds.isEmpty()) {
			return null;
		}
		
		String serviceDescs = "";
		String[] serviceArr = serviceIds.split(",");
		for (int i = 0; i < serviceArr.length; i++) {
			try {
				AppointmentServiceDesc appointmentServiceDesc = getAppointmentServiceDesc(Integer.valueOf(serviceArr[i].trim()));
				if (!serviceDescs.isEmpty())
					serviceDescs += ",";
				
				serviceDescs += appointmentServiceDesc.getServiceDesc();
			} catch(Exception e) {				
			}			
		}
		
		return serviceDescs;
	}
	
	public void createAppointment(Appointment appointment, String appName) throws InvalidAppointmentException {

		Store store = storeDAO.get(appointment.getStoreNumber().longValue());
		
		//WI-2575: avoid retrieving fitment data from database when the vehicle year/make/model
		//         is already present
		Fitment fitment = null;
		if(appointment.getVehicleYear() == null || appointment.getVehicleYear() == 0
				|| appointment.getVehicleMake() == null || StringUtils.isEmpty(appointment.getVehicleMake())
				|| appointment.getVehicleModel() == null || StringUtils.isEmpty(appointment.getVehicleModel())){
			if(appointment.getVehicleId() != null){
				Collection<Fitment> fitments = fitmentDAO.findByAcesVehicleIdAndSubModel(appointment.getVehicleId(), appointment.getVehicleSubmodel());
				if(fitments != null && fitments.size() >  0) {
					List<Fitment> fs = new ArrayList<Fitment>(fitments);
					fitment = fs.get(0);
				}
			}
			if(fitment != null) {
				appointment.setVehicleYear(Long.valueOf(fitment.getModelYear()));
				appointment.setVehicleMake(fitment.getMakeName());
				appointment.setVehicleModel(fitment.getModelName());
				appointment.setVehicleSubmodel(fitment.getSubmodel());
			}
		}
		
		appointment.setCreatedDate(new Date());
		String optIn = appointment.getEmailSignup();
		if("true".equals(optIn)) {
			appointment.setEmailSignup("on");
			EmailSignup signup = new EmailSignup();
			signup.setEmailAddress(appointment.getEmailAddress());
			signup.setFirstName(appointment.getFirstName());
			signup.setLastName(appointment.getLastName());
			signup.setAddress1(appointment.getAddress1());
			signup.setAddress2(appointment.getAddress2());
			signup.setCity(appointment.getCity());
			signup.setState(appointment.getState());
			signup.setZip(appointment.getZip());
			signup.setSource(appName + " Online Appointment");
			signup.setCreatedDate(new Date());
			try {
				emailService.subscribe(signup);
			} catch(InvalidEmailException ieE) {
				log.severe("Could not save email signup.  Continuing to save appointment.  Cause: "+ieE.getMessage());
			}
		}
		
		//Save the appointment with the services and choices
		try {
			appointmentDAO.save(appointment);
			saveServices(appointment, appointment.getServices());
			saveChoices(appointment, appointment.getChoices());
			
			appointmentSender.sendAppointmentData(appointment, store);
		}catch(HttpException webException) {
			throw new InvalidAppointmentException(webException);
			
		}catch(Exception E) {
			throw new InvalidAppointmentException(E);
		}
		
	}
		
	public void saveChoices(Appointment appointment, List<AppointmentChoice> choices) throws InvalidAppointmentException {
		if(appointment.getAppointmentId() == null || appointment.getAppointmentId().longValue() == 0)
			throw new InvalidAppointmentException("Appointment.id is null or 0.  Cannot save choices without an appointment id.");
		
		for(AppointmentChoice choice : choices) {
			choice.setAppointmentId(appointment.getAppointmentId());
			choice.setAppointmentChoiceId(ZERO);
			appointmentChoiceDAO.save(choice);
		}
		
	}
	
	public void saveServices(Appointment appointment, Set<AppointmentService> services) throws InvalidAppointmentException {
		if(appointment.getAppointmentId() == null || appointment.getAppointmentId().longValue() == 0)
			throw new InvalidAppointmentException("Appointment.id is null or 0.  Cannot save services without an appointment id.");
			
		for(AppointmentService svc : services) {
			svc.setAppointmentServiceId(ZERO);
			svc.setAppointmentId(appointment.getAppointmentId());
			appointmentServiceDAO.save(svc);
		}
	}
	
	public void saveCustInfo(Appointment appointment) throws InvalidAppointmentException {
		if(appointment.getAppointmentId() == null || appointment.getAppointmentId().longValue() == 0)
			throw new InvalidAppointmentException("Appointment.id is null or 0.  Cannot save customer info without an appointment id.");
		
		AppointmentCustomer apptCust = new AppointmentCustomer();
		apptCust.setAppointmentId(appointment.getAppointmentId());
		apptCust.setFirstName(appointment.getFirstName());
		apptCust.setLastName(appointment.getLastName());
		apptCust.setAddress1(appointment.getAddress1());
		apptCust.setAddress2(appointment.getAddress2());
		apptCust.setCity(appointment.getCity());
		apptCust.setState(appointment.getState());
		apptCust.setZip(appointment.getZip());
		apptCust.setDaytimePhone(appointment.getDaytimePhone());
		apptCust.setEveningPhone(appointment.getEveningPhone());
		apptCust.setCellPhone(appointment.getCellPhone());
		apptCust.setEmailAddress(appointment.getEmailAddress());
		apptCust.setWebSite(appointment.getWebSite());
		apptCust.setCreatedDate(new Date());
		appointmentCustomerDAO.save(apptCust);
	}

	
	public Date getChoiceDatetime(String strMonth, String strDate, String strTime) {
		
		Calendar now = GregorianCalendar.getInstance();
		int thisMonth = new Integer(now.get(Calendar.MONTH)) + 1;
		int year = new Integer(now.get(Calendar.YEAR));
		
		//If the user is scheduling an appointment for the first or second month of the year and we are in one
		// of the last two months of THIS year, increment the year value as the appointment is for next year.
		// NOTE: This logic will ONLY work if we continue to display two months at a time.
		if(("January".equals(strMonth) || "February".equals(strMonth)) && (11 == (thisMonth) || 12 == (thisMonth)))
			year++;
		

		Integer month = months.get(StringUtils.trimToEmpty(strMonth).toLowerCase());
		Integer date = new Integer(strDate);
		Integer hours = new Integer(strTime.substring(0, strTime.indexOf(":")));
		Integer minutes = new Integer(strTime.substring(strTime.indexOf(":")+1));
		
		try {
			now.set(Calendar.YEAR, year);
			now.set(Calendar.MONTH, month);
			now.set(Calendar.DATE, date);
			now.set(Calendar.HOUR_OF_DAY, hours);
			now.set(Calendar.MINUTE, minutes);
			now.set(Calendar.SECOND, 0);
		}catch(Exception E) {
			log.severe("Error creating datetime from month("+strMonth+") date("+strDate+") time("+strTime+")");
			return null;
		}
		return now.getTime();
		//return DateUtils.convertToGmt( now.getTime() );

	}
	

	public Long saveAppointment(AppointmentData appointmentData,String confirmationId, 
			String customerId, String statusMessage) throws InvalidAppointmentException {
		Long appointmentId = null;
		try {
			Appointment appointment = createAppointmentBean(appointmentData);
			appointmentId = appointmentDAO.save(appointment);
			appointment.setAppointmentId(appointmentId);
			saveServices(appointment, appointment.getServices());
			saveChoices(appointment, appointment.getChoices());
			saveCustInfo(appointment);
			
			AppointmentMetadata metadata = new AppointmentMetadata();
			metadata.setAppointmentId(appointmentId);
			metadata.setEmployeeId(appointmentData.getEmployeeId());
			metadata.setAppointmentStatusId(appointmentData.getAppointmentStatusId());
			metadata.setLocationId(appointmentData.getLocationId());
			metadata.setServicesCSV(appointmentData.getSelectedServices());
			metadata.setCreatedDate(new Date());
			metadata.setOtherDetails("Services="+getAppointmentServiceDescs(appointmentData.getSelectedServices())+" Status="+appointmentData.getAppointmentStatusDesc());
			appointmentMetadataDAO.save(metadata);
	    	
			//save the appointment sent status even if there is no confirmation id yet
			AppointmentSentStatus apptSentStatus = new AppointmentSentStatus();
	    	apptSentStatus.setAppointmentId(appointmentId);
	    	apptSentStatus.setUpdateDate(new Date());
	    	if(confirmationId != null && !confirmationId.equalsIgnoreCase("")){
	    		apptSentStatus.setStatus("S");
	    		apptSentStatus.setBookingConfirmationId(confirmationId);
	    		apptSentStatus.setOtherDetails(customerId);
	    	}else{
	    		apptSentStatus.setStatus("R");
	    	}
    		apptSentStatus.setAppointmentStatus(statusMessage);
	    	
			appointmentSentStatusDAO.save(apptSentStatus);
			return appointmentId;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAppointmentException(e);
		}
	}
	
	public Appointment createAppointmentBean(AppointmentData apptData){
		Appointment appt = new Appointment();
		appt.setStoreNumber(apptData.getStoreNumber());
		appt.setVehicleYear(Long.valueOf(apptData.getVehicleYear()));
		appt.setVehicleMake(apptData.getVehicleMake());
		appt.setVehicleModel(apptData.getVehicleModel());
		appt.setVehicleSubmodel(apptData.getVehicleSubmodel());
		appt.setMileage(apptData.getMileage() == null || "".equals(apptData.getMileage()) 
				|| "NULL".equalsIgnoreCase(apptData.getMileage())? 0: Integer.valueOf(apptData.getMileage()));
		appt.setComments(apptData.getComments());
		appt.setFirstName(apptData.getCustomerFirstName());
		appt.setLastName(apptData.getCustomerLastName());
		appt.setAddress1(apptData.getCustomerAddress1());
		appt.setAddress2(apptData.getCustomerAddress2());
		appt.setBatteryQuoteId(apptData.getQuoteId() == null || "".equals(apptData.getQuoteId())
				|| "NULL".equalsIgnoreCase(apptData.getQuoteId())? ZERO: Long.valueOf(apptData.getQuoteId()));
		appt.setCity(apptData.getCustomerCity());
		appt.setState(apptData.getCustomerState());
		appt.setZip(apptData.getCustomerZipCode());
		appt.setDaytimePhone(apptData.getCustomerDayTimePhone());
		appt.setEmailAddress(apptData.getCustomerEmailAddress());
		appt.setWebSite(apptData.getWebsiteName());
		appt.setWebSiteSource(apptData.getWebSiteSource());
		List<AppointmentChoice> choices = new ArrayList<AppointmentChoice>();
		choices.add(apptData.getChoice());
		appt.setChoices(choices);
		Set<AppointmentService> services =  new HashSet<AppointmentService>();
		String selectedServices = apptData.getSelectedServices();
		for (String serviceId : selectedServices.split(",")){
			AppointmentService service = new AppointmentService();
			service.setAppointmentServiceId(ZERO);
			service.setServiceId(Integer.valueOf(serviceId));
			services.add(service);
		}
		appt.setServices(services);
		appt.setCreatedDate(new Date());
		appt.seteCommRefNumber(apptData.geteCommRefNumber());
		return appt;
	}

	public void retryFailedAppointments() {
		log.info("Scheduled trigger for failed appointments retry initiated");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
		List<Appointment> apptList = appointmentDAO.findAppointmentsToRetry();
		
		if (apptList != null && !apptList.isEmpty()) {
			log.info("Total appointments to retry: "+apptList.size());
			retryFailedAppointmentsCounts(apptList.size());
			for (Appointment appt : apptList) {
				String webserviceError = "";
				Long recApptId = null;
				Long customerId = null;
				String services = "";
				try {
					log.info("Retry appointment id: "+appt.getAppointmentId());
					AppointmentMetadata metadata = appointmentMetadataDAO.get(appt.getAppointmentId());
					if (metadata != null) {
						String otherDetails = metadata.getOtherDetails(); 
						services = otherDetails.substring(otherDetails.indexOf("Services=")+9,otherDetails.indexOf("Status="));
						AppointmentData appointmentData = createAppointmentDataBean(appt, metadata);
						String dateStr = dateFormat.format(appointmentData.getChoice().getDatetime());
						Date choiceDateTime = null;
						try {
							choiceDateTime = dateFormat.parse(dateStr);
						} catch (ParseException e) {
							log.warning("Invalid Selected date format.");
						}
						String[] dateSplits = dateStr.split("\\s+");
						String selectedDate = dateSplits[0];
						String[] hourMinSplits = dateSplits[1].split(":");
						Integer selectedTime = new Integer(hourMinSplits[0]) * 60 + new Integer(hourMinSplits[1]);
						int firstDelimIdx = appointmentData.getSelectedServices().indexOf(",");
						String primaryServiceId	= "";
						String additionalServiceIds	=	"";
						if(firstDelimIdx != -1){
							primaryServiceId = appointmentData.getSelectedServices().substring(0, firstDelimIdx);
							additionalServiceIds = appointmentData.getSelectedServices().substring(firstDelimIdx+1);
						}else{
							primaryServiceId = appointmentData.getSelectedServices();
						}
						
						BSROWebServiceResponse bookApptResponse = null;
						bookApptResponse = appointmentPlusScheduler.createAppointment(appointmentData, selectedDate, selectedTime, primaryServiceId, additionalServiceIds);
						if(bookApptResponse != null && bookApptResponse.getErrors() != null && bookApptResponse.getErrors().hasGlobalErrors()){
							webserviceError = bookApptResponse.getErrors().getGlobalErrors().get(0);
							throw new Exception(webserviceError);
						} else if (bookApptResponse != null && bookApptResponse.getPayload() != null) {
							AppointmentData responseData = (AppointmentData)bookApptResponse.getPayload();
							recApptId = (responseData.getAppointmentId() != null) ? responseData.getAppointmentId() : null;
							customerId = (responseData.getCustomerId() != null) ? responseData.getCustomerId() : null;
						}
					} else {
						webserviceError =  "No metadata found to retry for appointment id: "+appt.getAppointmentId();
						mailService.sendAppointmentRetryFailedEmail(appt,services);
					}
				} catch(java.net.ConnectException ce){
					log.warning("Couldn't connect to email server");
				} catch (Exception e) {			
					//e.printStackTrace();
					webserviceError = e.toString();
					log.warning("There was an exception while trying to book"+" error=" + webserviceError);
					mailService.sendAppointmentRetryFailedEmail(appt,services);
				} finally{
					if(appt != null){
						if(recApptId != null)
							updateSentStatus(appt.getAppointmentId(),"S", String.valueOf(recApptId), "Customer #:"+customerId,"Retried and Successful");
						else{
							log.info("webserviceError ="+webserviceError);
							updateSentStatus(appt.getAppointmentId(),"F", "", "", 
									webserviceError.length()> 1000 ? webserviceError.substring(0, 1000):webserviceError);
						}
					}
				}
			}	
			
		} else {
			log.info("No appointments to retry.");
		}
	}
	
	public void retryFailedAppointmentsCounts(Integer retryCount) {
		log.info("Scheduled trigger for failed appointments retry count initiated");
		
		List<Appointment> apptList = appointmentDAO.findAppointmentsToRetry();
		
			log.info("Total appointments to retry: "+apptList.size());
			try {
				if(apptList.size()!=0){
					mailService.sendAppointmentRetryFailedCountEmail(apptList.size());
				}
			}catch (Exception e) {			
				//e.printStackTrace();
			}
	}
	
	private AppointmentData createAppointmentDataBean(Appointment appt, AppointmentMetadata metadata){
		AppointmentData sendAppt = new AppointmentData();
		String otherDetails = metadata.getOtherDetails();
		String statusDesc = otherDetails.substring(otherDetails.indexOf("Status=")+7);
		
		sendAppt.setAppointmentId(appt.getAppointmentId());
		sendAppt.setStoreNumber(appt.getStoreNumber());

		sendAppt.setVehicleYear(String.valueOf(appt.getVehicleYear()));
		sendAppt.setVehicleMake(appt.getVehicleMake());
		sendAppt.setVehicleModel(appt.getVehicleModel());
		sendAppt.setVehicleSubmodel(appt.getVehicleSubmodel() == null ? "":appt.getVehicleSubmodel());
		sendAppt.setMileage(appt.getMileage() == null ? "":String.valueOf(appt.getMileage()));
		sendAppt.setComments(appt.getComments());
		sendAppt.setCustomerFirstName(appt.getFirstName());
		sendAppt.setCustomerLastName(appt.getLastName());
		sendAppt.setCustomerAddress1(appt.getAddress1());
		sendAppt.setCustomerAddress2(appt.getAddress2());
		sendAppt.setCustomerCity(appt.getCity());
		sendAppt.setCustomerState(appt.getState());
		sendAppt.setCustomerZipCode(appt.getZip());
		sendAppt.setCustomerDayTimePhone(appt.getDaytimePhone());
		sendAppt.setCustomerEveningPhone(appt.getEveningPhone());
		sendAppt.setCustomerCellPhone(appt.getCellPhone());
		sendAppt.setCustomerEmailAddress(appt.getEmailAddress());
		sendAppt.setEmployeeId(metadata.getEmployeeId());
		sendAppt.setLocationId(metadata.getLocationId());
		sendAppt.setAppointmentStatusId(metadata.getAppointmentStatusId());
		sendAppt.setAppointmentStatusDesc(statusDesc);
		sendAppt.setQuoteId(String.valueOf(appt.getBatteryQuoteId()));
		//sendAppt.setReceivePromos(appt.getEmailSignup());
		sendAppt.setAppointmentType("New");
		if(!appt.getChoices().isEmpty()){
			for(int i=0; i < appt.getChoices().size(); i++){
				AppointmentChoice choice = (AppointmentChoice)appt.getChoices().get(i);
				if(choice != null){
					com.bfrc.dataaccess.model.appointment.AppointmentChoice apptChoice 
					= new com.bfrc.dataaccess.model.appointment.AppointmentChoice();
					apptChoice.setChoice(1);
					apptChoice.setAppointmentChoiceId(choice.getAppointmentChoiceId());
					apptChoice.setAppointmentId(choice.getAppointmentId());
					apptChoice.setDropWaitOption(choice.getDropWaitOption());
					apptChoice.setDatetime(choice.getDatetime());
					sendAppt.setChoice(apptChoice);
				}
			}
		}
		
		sendAppt.setSelectedServices(metadata.getServicesCSV());
		sendAppt.setWebsiteName(appt.getWebSite());
		return sendAppt;
	}
	
	private void updateSentStatus (Long apptId, String status, String confirmId, String trackingId, 
			String statusMessage) throws DataAccessException{
		try {
			AppointmentSentStatus apptSentStatus = new AppointmentSentStatus();
	    	apptSentStatus.setAppointmentId(apptId);
	    	apptSentStatus.setStatus(status);
	    	apptSentStatus.setUpdateDate(new Date());
	    	apptSentStatus.setBookingConfirmationId(confirmId);
	    	apptSentStatus.setOtherDetails(trackingId);
	    	apptSentStatus.setAppointmentStatus(statusMessage);
	    	appointmentSentStatusDAO.update(apptSentStatus);
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}

}
