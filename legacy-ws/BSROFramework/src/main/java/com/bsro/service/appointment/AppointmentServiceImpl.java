package com.bsro.service.appointment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.webflow.mvc.servlet.MvcExternalContext;

import com.bfrc.framework.dao.AppointmentDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.store.ListStoresOperator;
import com.bfrc.framework.util.CacheDataUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.appointment.Appointment;
import com.bfrc.pojo.appointment.AppointmentChoice;
import com.bfrc.pojo.appointment.AppointmentCustomer;
import com.bfrc.pojo.appointment.AppointmentServiceAndCat;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreSearch;
import com.bsro.constants.SessionConstants;
import com.bsro.pojo.appointment.ScheduleAppointmentDataBean;
import com.bsro.pojo.form.AppointmentForm;
import com.bsro.service.appointment.rules.AppointmentRulesService;
import com.bsro.service.store.StoreService;
import com.bsro.webservice.BSROWebserviceConfig;

@Service("appointmentService")
public class AppointmentServiceImpl implements AppointmentService {
	
	private final Log logger = LogFactory.getLog(AppointmentServiceImpl.class);
	
	@Resource(name="environmentPropertiesMap")
	private Map<String, String> environmentProperties;
	
	@Autowired
	private AppointmentDAO apptDAO;
	
	@Autowired
	private map.States statesMap;
	
	@Autowired
	private VehicleDAO vehicleDAO;
	
	@Autowired
	private BSROWebserviceConfig config;
	
	@Autowired
	private ScheduleAppointmentService svc;
	
	@Autowired
	AppointmentRulesService appointmentRulesService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ListStoresOperator listStores;
	
	private String EXTENSION = " ext. ";
	private String DROP = "drop";

	public AppointmentForm initializeForm(HttpServletRequest request, String storeNumber, Store preferredStore, 
			String zip, String comments, String maintenanceChoices, String firstName, String lastName, 
			String emailAddress, String noValidation, String dateString){
		AppointmentForm appointment = new AppointmentForm();
				
		if(!StringUtils.isNullOrEmpty(storeNumber)){
			appointment.setStoreNumber(Long.parseLong(storeNumber));
			appointment.setFindStoreById(true);
		}else if(!ServerUtil.isNullOrEmpty(preferredStore)){
			appointment.setStoreNumber(preferredStore.getStoreNumber().longValue());
			appointment.setFindStoreById(false);
		}
		appointment.setZip(zip);
		appointment.setComments(comments);
		if(noValidation == null){
			appointment.setNoValidation("false");
		}else{
			appointment.setNoValidation(noValidation);
		}
		appointment.setFirstName(firstName);
		appointment.setLastName(lastName);
		appointment.setEmailAddress(emailAddress);
		
//		AppointmentParsedComments parsedComments = new AppointmentParsedComments(comments);
//		appointment.setParsedComments(parsedComments);
		
		if(maintenanceChoices != null){
			String [] maintChoices = new String[]{maintenanceChoices};
			appointment.setMaintenanceChoices(maintChoices);
		}
		
		//default the drop/wait values
		appointment.setChoice1Radio(DROP);
		appointment.setChoice2Radio(DROP);
		
		boolean b = true;
		Date testDate = null;
		try{
			if(dateString != null){
				boolean allowDateOverride = Boolean.parseBoolean(environmentProperties.get("allowAppointmentStartDateOverride"));
				Util.debug("allowDateOverride = "+allowDateOverride+", date string = "+dateString);
				if (allowDateOverride) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
					testDate = (Date) formatter.parse(dateString);
				}
			}
		}catch(Exception e){
			//e.printStackTrace();
			testDate = null;
		}
		appointment.setTestDate(testDate);
		
		appointment.setEnteredZip(zip);
		if(appointmentRulesService.getNumberOfEligibleDays()!= null && appointmentRulesService.getNumberOfEligibleDays().getAppointmentRuleValue() != null){
			appointment.setNumEligibleDays(appointmentRulesService.getNumberOfEligibleDays().getAppointmentRuleValue().intValue());
		}
		if(appointmentRulesService.getMinTimeBetween1stAnd2ndAppointment() != null && appointmentRulesService.getMinTimeBetween1stAnd2ndAppointment().getAppointmentRuleValue() != null){
			appointment.setMinTimeBetween1stAnd2ndAppointment(appointmentRulesService.getMinTimeBetween1stAnd2ndAppointment().getAppointmentRuleValue().intValue());
		}
		if(appointmentRulesService.getMinTimeBetween2ndAnd3rdAppointment() != null && appointmentRulesService.getMinTimeBetween2ndAnd3rdAppointment().getAppointmentRuleValue() != null){
			appointment.setMinTimeBetween2ndAnd3rdAppointment(appointmentRulesService.getMinTimeBetween2ndAnd3rdAppointment().getAppointmentRuleValue().intValue());
		}
		appointment.setAllowTwoAppointmentsBeforeNoon(appointmentRulesService.areTwoAppointmentsPriorToNoonAllowed());
		appointment.setDaysForNoPriorToNoonAppointments(appointmentRulesService.getTwoAppointmentsPriortoNoonNotAllowedTargetDays());
		if(appointmentRulesService.getMinRequiredAppointments() != null && appointmentRulesService.getMinRequiredAppointments().getAppointmentRuleValue() != null){
			appointment.setMinRequiredAppointments(appointmentRulesService.getMinRequiredAppointments().getAppointmentRuleValue().intValue());
		}
		if(appointmentRulesService.getMinTimeBetweenDropOffAndPickUp() != null && appointmentRulesService.getMinTimeBetweenDropOffAndPickUp().getAppointmentRuleValue() != null){
			appointment.setMinTimeBetweenPickUpAndDropOff(appointmentRulesService.getMinTimeBetweenDropOffAndPickUp().getAppointmentRuleValue().intValue());
		}
				
		return appointment;
	}
	
	public void checkIfMilitaryStore(AppointmentForm appointmentForm) {
		Long storeNumber = appointmentForm.getStoreNumber();
		boolean isMilitaryStore = false;
		if(storeNumber == null || storeNumber.longValue() == 0){
			isMilitaryStore = false;
		}else{			
			isMilitaryStore = storeService.isMilitaryStore(storeNumber);			
		}
		appointmentForm.setIsMilitaryStore(isMilitaryStore);
	}

	public Store syncStore(AppointmentForm appointmentForm, Store store) {
		if (appointmentForm != null && store != null) {
			if (appointmentForm.getStoreNumber() != null) {
				if (store.getStoreNumber() == null || !appointmentForm.getStoreNumber().equals(Long.valueOf(store.getStoreNumber()))) {
					return storeService.findStoreLightById(appointmentForm.getStoreNumber().longValue());
				} else {
					return store;
				}
			} else {
				return null;
			}
		} else if (appointmentForm != null && appointmentForm.getStoreNumber() != null) {
			return storeService.findStoreLightById(appointmentForm.getStoreNumber().longValue());
		}
		return null;
	}
	
	public void setCachedZip(HttpServletRequest request, String enteredZip){
		if(!StringUtils.isNullOrEmpty(enteredZip)){
			CacheDataUtils.setCachedZip(request, enteredZip);
		}
	}
	
	public void populateVehicleNames(AppointmentForm appointmentForm) {
		String makeId = appointmentForm.getVehicleMake();
		String modelId = appointmentForm.getVehicleModel();
		
		String makeName = vehicleDAO.getMakeNameByMakeId(makeId);
		String modelName = vehicleDAO.getModelNameByModelId(modelId);
		
		appointmentForm.setVehicleMakeName(makeName);
		appointmentForm.setVehicleModelName(modelName);				
	}
	
	public List<AppointmentServiceAndCat> getAppointmentServices(){
		
		@SuppressWarnings("unchecked")
		List<AppointmentServiceAndCat> apptServicesList = (List<AppointmentServiceAndCat>)apptDAO.getAllAppointmentServiceDescsAndCategories();		
		return apptServicesList;
	}
	
	public List<AppointmentServiceAndCat> getAppointmentServices(Long storeNumber, AppointmentForm appointmentForm){
		if (storeNumber == null)
			return null;
		Store store = storeService.findStoreLightById(storeNumber.longValue());
		int serviceType = 1;
		if ("MVAN".equalsIgnoreCase(store.getStoreType())) {
			serviceType = 3;
		}
		
		@SuppressWarnings("unchecked")
		List<AppointmentServiceAndCat> apptServicesList = (List<AppointmentServiceAndCat>)apptDAO.getAllAppointmentServicesBySortOrder(serviceType);
		Map<String,String> servicesMap = new HashMap<String,String>();
		for(AppointmentServiceAndCat sc : apptServicesList){
			servicesMap.put(String.valueOf(sc.getServiceId()).toLowerCase(), sc.getServiceDesc());
		}
		//save the services map on the form itself
		appointmentForm.setServicesMap(servicesMap);
		return apptServicesList;
	}
	
	public List<AppointmentServiceAndCat> getAppointmentServices(AppointmentForm appointmentForm){
		
		@SuppressWarnings("unchecked")
		List<AppointmentServiceAndCat> apptServicesList = (List<AppointmentServiceAndCat>)apptDAO.getAllAppointmentServicesBySortOrder();
		Map<String,String> servicesMap = new HashMap<String,String>();
		for(AppointmentServiceAndCat sc : apptServicesList){
			servicesMap.put(String.valueOf(sc.getServiceId()).toLowerCase(), sc.getServiceDesc());
		}
		//save the services map on the form itself
		appointmentForm.setServicesMap(servicesMap);
		return apptServicesList;
	}
	
	public List<AppointmentServiceAndCat> getServicesFromAppointmentPlus(AppointmentForm appointmentForm){
		List<LinkedHashMap<String,String>> dataList = svc.getServicesFromAppointmentPlus();
		List<AppointmentServiceAndCat> serviceList = new ArrayList<AppointmentServiceAndCat>();
		if(!dataList.isEmpty() && dataList.get(0).get("error0") != null){
			Collection<String> errors = dataList.get(0).values();
			List<String> schedulerErrors = new ArrayList<String>();
			schedulerErrors.addAll(errors);
			appointmentForm.setSchedulerErrors(schedulerErrors);
			return serviceList;
		}
		Map<String,String> servicesMap = new HashMap<String,String>();
		for(LinkedHashMap<String,String> data : dataList){
			AppointmentServiceAndCat sc = new AppointmentServiceAndCat();
			
			sc.setServiceId(new Integer(String.valueOf(data.get("serviceId"))));
			sc.setServiceDesc(data.get("serviceDesc"));
			sc.setServiceCategory(data.get("serviceCategory"));
			//logger.info("=======>"+data.get("serviceId").toString() +" "+ data.get("serviceDesc") +" "+ data.get("serviceDesc"));
			serviceList.add(sc);
			servicesMap.put(String.valueOf(sc.getServiceId()).toLowerCase(), sc.getServiceDesc());
		}
		//save the services map on the form itself
		appointmentForm.setServicesMap(servicesMap);
		return serviceList;
	}
	
	
	
	public Map getServiceMap(){
		Map m = apptDAO.getMappedAppointmentServiceDescs();
		return m;
	}
	
	public void printAppointmentInfo(AppointmentForm appointmentForm){
		Util.debug("appointment info:" + appointmentForm);
	}
	
	public map.States getStatesMap(){
		
		return statesMap;
	}
	
	public Appointment scheduleAppointment(AppointmentForm appointmentForm) throws Exception{
		//need to translate from the appointmentForm to the appointment object to send to the ScheduleAppointmentService
		Util.debug("Attempting to send to the web service...");
		Util.debug("Sending the appointment:" + appointmentForm);
		//no parameters on the form should be empty, so we'll just set them.
		//this is the one that we send to the service.
		Appointment pojo = new Appointment();
		
		//no address2 or address1 field on the form, so just set address1 on the pojo
		pojo.setAddress1(appointmentForm.getAddress());
		
		//HOME/WORK/MOBILE - I enjoyed the below comment too much, so I kept it. - RF
		// note the sociocultural assumptions implicit in the following:
		// work phone is daytime, home phone is evening
		String phoneType = appointmentForm.getPhoneType();
		
		//remove all non-difit characters and format the customer day time phone number
		String tempPhone = appointmentForm.getDaytimePhone().toString().replaceAll("[^\\d]", "" );
		String formattedPhoneNumber = tempPhone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
		StringBuffer completePhone = new StringBuffer(formattedPhoneNumber);	
		
		//There is no extension field on the pojo, so cram it into the phone field
		if(!StringUtils.isNullOrEmpty(appointmentForm.getDaytimePhoneExt())){
			completePhone.append(EXTENSION);
			completePhone.append(appointmentForm.getDaytimePhoneExt());
		}
		if (completePhone!= null && completePhone.toString().length() > 0)
			pojo.setDaytimePhone(completePhone.toString());
		else
			pojo.setDaytimePhone(appointmentForm.getDaytimePhone());

		ArrayList choiceList = new ArrayList();
		//saving appointment choices
		if(!ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Date()) && (!ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Time()) ||
				(!ServerUtil.isNullOrEmpty(appointmentForm.getDropOffTime()) && !ServerUtil.isNullOrEmpty(appointmentForm.getPickUpTime())))) {
			//should have a date and time or in case of pilot store with "drop off" option, should have the drop off and pick up time
			AppointmentChoice apptChoice1 = new AppointmentChoice();
			apptChoice1.setDropWaitOption(appointmentForm.getChoice1Radio());
//			if(appointmentForm.getChoice1Radio().equalsIgnoreCase("drop") && storeService.isApptPilotStore(appointmentForm.getStoreNumber())){
//				apptChoice1.setDropOffTime(getAppointmentChoiceTime(appointmentForm, appointmentForm.getChoice1Date(), appointmentForm.getDropOffTime()));
//				apptChoice1.setPickUpTime(getAppointmentChoiceTime(appointmentForm, appointmentForm.getChoice1Date(), appointmentForm.getPickUpTime()));
//			}else{
			apptChoice1.setDatetime(getAppointmentChoiceTime(appointmentForm, appointmentForm.getChoice1Date(), appointmentForm.getChoice1Time()));
//			}
			apptChoice1.setAppointmentId(0L);
			apptChoice1.setChoice(new Integer(1));
			choiceList.add(apptChoice1);
		}
		if(!ServerUtil.isNullOrEmpty(appointmentForm.getChoice2Date()) && !ServerUtil.isNullOrEmpty(appointmentForm.getChoice2Time())) {
			AppointmentChoice apptChoice2 = new AppointmentChoice();
			apptChoice2.setDropWaitOption(appointmentForm.getChoice2Radio());
			apptChoice2.setDatetime(getAppointmentChoiceTime(appointmentForm, appointmentForm.getChoice2Date(), appointmentForm.getChoice2Time()));

			apptChoice2.setAppointmentId(0L);
			apptChoice2.setChoice(new Integer(2));
			choiceList.add(apptChoice2);
		}
		pojo.setChoices(choiceList);
		
		//set the appointment meta-data
		pojo.setLocationId(appointmentForm.getLocationId());
		pojo.setEmployeeId(appointmentForm.getEmployeeId());
		pojo.setAppointmentStatusId(appointmentForm.getAppointmentStatusId());
		pojo.setAppointmentStatusDesc(appointmentForm.getAppointmentStatus());
		pojo.setServiceIds(appointmentForm.getAp_serviceIds());
		pojo.setAp_serviceDescriptions(appointmentForm.getAp_serviceDescriptions());
		
		pojo.setCity(appointmentForm.getCity());
		pojo.setCustomerComments(appointmentForm.getCustomerComments());
		pojo.setComments(appointmentForm.getComments());
		if(!ServerUtil.isNullOrEmpty(appointmentForm.getEmailAddress())){
			pojo.setEmailAddress(appointmentForm.getEmailAddress().trim());
		}
		pojo.setEmailSignup(appointmentForm.getEmailSignup());
		pojo.setFirstName(appointmentForm.getFirstName());
		pojo.setLastName(appointmentForm.getLastName());
		
		HashSet services = new HashSet();
		if (appointmentForm.getMaintenanceChoices() != null) {
			String ap_serviceIds[] = appointmentForm.getAp_serviceIds().split(",");		
			pojo.setServiceIds(appointmentForm.getAp_serviceIds());
			pojo.setMaintenanceChoices(ap_serviceIds);			
			for(int i=0; i<ap_serviceIds.length; i++) {
				com.bfrc.pojo.appointment.AppointmentService apptService = new com.bfrc.pojo.appointment.AppointmentService();				
				apptService.setServiceId(Integer.parseInt(ap_serviceIds[i]));
				services.add(apptService);
			}
		}
		pojo.setServices(services);
		
		pojo.setState(appointmentForm.getState());
		pojo.setStoreNumber(appointmentForm.getStoreNumber());
		pojo.setWebSite(config.getAppName());
		pojo.setWebSiteSource(config.getAppSource());
		pojo.setZip(appointmentForm.getZip());
		
		UserVehicle uv = new UserVehicle();
		if(!ServerUtil.isNullOrEmpty(appointmentForm.getVehicleYear()))
			pojo.setVehicleYear(appointmentForm.getVehicleYear());
		if(!ServerUtil.isNullOrEmpty(appointmentForm.getVehicleMakeName()))
			pojo.setVehicleMake(appointmentForm.getVehicleMakeName());
		if(!ServerUtil.isNullOrEmpty(appointmentForm.getVehicleModelName()))
			pojo.setVehicleModel(appointmentForm.getVehicleModelName());
		if(!ServerUtil.isNullOrEmpty(appointmentForm.getVehicleSubmodel()))
			pojo.setVehicleSubmodel(appointmentForm.getVehicleSubmodel());
		
		String acesVehicleId = appointmentForm.getAcesVehicleId();
		if(!ServerUtil.isNullOrEmpty(acesVehicleId)) {
			//fix for WI 4032(In the older browsers (some times only) acesVehicleId is coming multiple times with "," seperated like "157009,157009")
			String acesVehicleIds[] = {};		    
		    if(acesVehicleId.contains(",")){
		    	acesVehicleIds = acesVehicleId.split(",");
		    	if("".equals(acesVehicleIds[0]))
		    		acesVehicleId = acesVehicleIds[1];
		    	else
		    		acesVehicleId = acesVehicleIds[0];
		    }
			//end fix for 4032
		    
			uv.setAcesVehicleId(Long.valueOf(acesVehicleId));
			uv.setYear(appointmentForm.getVehicleYear().toString());
			uv.setSubmodel(appointmentForm.getVehicleSubmodel());
			//WI-2558: remove call to get data from FITMENT
			//			List fits = vehicleDAO.getFitments(uv.getBaseVehId());
			//			String makeName = "";
			//			String modelName = "";
			//			if(fits != null && fits.size() > 0){
			//				FitmentId fitment = (FitmentId)fits.get(0);
			//				makeName = fitment.getMakeName();
			//				modelName = fitment.getModelName();
			//			}
			pojo.setVehicleMake(appointmentForm.getVehicleMakeName());
			pojo.setVehicleModel(appointmentForm.getVehicleModelName());
			uv.setMake(appointmentForm.getVehicleMakeName());
			uv.setModel(appointmentForm.getVehicleModelName());
			pojo.setVehicle(uv);
			pojo.setMileage(appointmentForm.getMileage());
		}
				
		pojo.setCreatedDate(new Date());
		pojo.setEmailSignup(appointmentForm.getEmailSignup());
		pojo.setTpmsFlag(appointmentForm.getTpms());
		//battery quote id
		if(appointmentForm.getBatteryQuoteId() != null){
			pojo.setBatteryQuoteId(appointmentForm.getBatteryQuoteId());
		}
		
		pojo.setEmailReminder(appointmentForm.getEmailReminderOption()==null||appointmentForm.getEmailReminderOption().isEmpty()?"N":appointmentForm.getEmailReminderOption());
		pojo.setPhoneReminder(appointmentForm.getPhoneReminderOption()==null||appointmentForm.getPhoneReminderOption().isEmpty()?"N":appointmentForm.getPhoneReminderOption());
		
		ScheduleAppointmentDataBean appt = null;					
		try {
			//prior to sending, temporarily clear out apostrophes
			//from first/last/comments/city
			pojo.setFirstName((pojo.getFirstName() == null) ? "" : pojo.getFirstName().replace("'", ""));
			pojo.setLastName((pojo.getLastName() == null) ? "" : pojo.getLastName().replace("'", ""));
			pojo.setComments((pojo.getComments() == null) ? "" : pojo.getComments().replace("'", ""));
			pojo.setCity((pojo.getCity() == null) ? "" : pojo.getCity().replace("'", ""));				
			
			appt = svc.createAppointment(pojo);
			
			//set the errors on the appointment form
			Map<String, String> errors = appt.getErrors();
			Iterator<String> i = errors.values().iterator();
			List<String> errorList = new ArrayList<String>();
			errorList.addAll(errors.values());
			if(!errorList.isEmpty()){
				appointmentForm.setSchedulerErrors(errorList);
			}
			
			// update appointment_customer table with appointment_id
			AppointmentCustomer apptCust = appointmentForm.getAppointmentCustomer();
			if (appt != null && apptCust != null) {
				apptCust.setAppointmentId(appt.getAppointment().getAppointmentId());
				apptDAO.updateAppointmentCustomer(apptCust);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Caught exception from service");
		}
		
		Util.debug("Sent to the web service... errors: " + appt.getErrors());
		Util.debug("Sent to the web service... got back: " + appt.getAppointment().getAppointmentId());
	
		return appt.getAppointment();	
	}

	private Date getAppointmentChoiceTime(AppointmentForm appointmentForm, String choiceDate, String choiceTime) throws ParseException {

		SimpleDateFormat	sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat	yearFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat	monthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat	monthStringFormat = new SimpleDateFormat("MMMM");
		SimpleDateFormat	dateFormat = new SimpleDateFormat("dd");
		Date monthDate = sdf.parse(choiceDate);
		Date formattedDate = null;
		try{
			String year = "";
			if(appointmentForm.getAppointmentSchedule()!=null&&appointmentForm.getAppointmentSchedule().getDateMap()!=null){
				Date selectedDate = appointmentForm.getAppointmentSchedule().getDateMap().get(monthStringFormat.format(monthDate)+dateFormat.format(monthDate));
				if(selectedDate!=null)
					year = "/"+yearFormat.format(selectedDate);
				else year = "/"+yearFormat.format(monthDate);
			}
			//apptChoice.setDatetime(appointmentForm.getDatetimeFormat().parse(monthFormat.format(monthDate)+"/"+dateFormat.format(monthDate)+year+" "+(choiceTime+"m").toUpperCase()));
			formattedDate = appointmentForm.getDatetimeFormat().parse(monthFormat.format(monthDate)+"/"+dateFormat.format(monthDate)+year+" "+(choiceTime+"m").toUpperCase());
		}catch (Exception e) {
			//e.printStackTrace();
			formattedDate = null;
		}
		return formattedDate;
	}
	
	public AppointmentRulesService getAppointmentRulesService() {
		return appointmentRulesService;
	}

	public void setAppointmentRulesService(
			AppointmentRulesService appointmentRulesService) {
		this.appointmentRulesService = appointmentRulesService;
	}
	
	public Store getStoreById(String storeNumber, Store preferredStore, AppointmentForm appointmentForm){
		if (ServerUtil.isNullOrEmpty(storeNumber) && preferredStore == null)
			return null;
		else if (ServerUtil.isNullOrEmpty(storeNumber) && preferredStore != null && !appointmentForm.isFindStoreById())
			return preferredStore;
		else
			return getStoreById(new Long(storeNumber), preferredStore, appointmentForm);
	}
	
	public Store getStoreById(Long storeNumber, Store preferredStore, AppointmentForm appointmentForm){
		try{
			if(preferredStore != null && !appointmentForm.isFindStoreById()){
				return preferredStore;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return storeService.findStoreLightById(storeNumber);
		
	}
	public void savePreferredStoreToSession(MvcExternalContext context, Store store, AppointmentForm appointmentForm){
		HttpServletRequest request = (HttpServletRequest)context.getNativeRequest();
		HttpSession session = request.getSession();
		String enteredZip = appointmentForm.getEnteredZip();
		StoreSearch storeSearch = storeService.createStoreSearchObject(enteredZip);

		if (session == null) {
			Util.debug("The session is null");
		    // There's no session been created during current nor previous requests.
		}
		else if (session.isNew()) {

			Util.debug("The session is new");
			session.setAttribute(SessionConstants.PREFERRED_STORE, store);
		    // The session has been created during the current request.
			if(store != null){
				if(StringUtils.isNullOrEmpty(storeSearch.getZip()))
					CacheDataUtils.setCachedZip(request, store.getZip());
				else
					CacheDataUtils.setCachedZip(request, enteredZip);
			}
		}
		else {
		   session.setAttribute(SessionConstants.PREFERRED_STORE, store);
			if(store != null){
				if(StringUtils.isNullOrEmpty(storeSearch.getZip()))
					CacheDataUtils.setCachedZip(request, store.getZip());
				else
					CacheDataUtils.setCachedZip(request, enteredZip);
			}
		}
	}
	
	public List<Store> searchForClosestStores(HttpServletRequest request, AppointmentForm appointmentForm){
		String enteredZip = appointmentForm.getEnteredZip();
		StoreSearch storeSearch = storeService.createStoreSearchObject(enteredZip);
		appointmentForm.setSearchedStoresBy("zipOrState");
		List<Store> foundStores = storeService.getStoresEligibleForAppointments(storeSearch, request.getRemoteAddr());
		if (storeSearch.getZip() != null && storeSearch.getZip().trim().length() > 0) {
			appointmentForm.setMappedDistance(storeService.getMappedDistance());
		} else {
			appointmentForm.setMappedDistance(null);
		}
		if(storeSearch.getState()!=null && storeSearch.getState().trim().length() > 0){
			appointmentForm.setEnteredState(storeSearch.getState());
		}
		if(storeSearch.getCity()!=null && storeSearch.getCity().trim().length() > 0){
			appointmentForm.setEnteredCity(storeSearch.getCity());
		}
		if(logger.isDebugEnabled()){
			if(foundStores != null && !foundStores.isEmpty()){
				logger.debug("Store Search found: " + foundStores);
			}
		}
		return foundStores;
	}
	
	public List<Store> initializeClosestStores(HttpServletRequest request, String city, String state, AppointmentForm appointmentForm, Store store){
		
		if(!ServerUtil.isNullOrEmpty(city) && !ServerUtil.isNullOrEmpty(state)){
			StoreSearch storeSearch = new StoreSearch();
			storeSearch.setCity(city);
			storeSearch.setState(state);
			appointmentForm.setStoreCity(city);
			appointmentForm.setStoreState(state);
			appointmentForm.setSearchedStoresBy("cityAndState");
			List<Store> foundStores = storeService.getStoresEligibleForAppointments(storeSearch, request.getRemoteAddr());
			if(logger.isDebugEnabled()){
				if(foundStores != null && !foundStores.isEmpty()){
					logger.debug("City/State Appointment Store Search found: " + foundStores);
				}
			}
			return foundStores;
		}
		
		//we should  be using the enteredZip to do an intial stores lookup.
		String enteredZip = appointmentForm.getEnteredZip();
		if(!StringUtils.isNullOrEmpty(enteredZip)){
			StoreSearch storeSearch = storeService.createStoreSearchObject(enteredZip);
			appointmentForm.setSearchedStoresBy("zipOrState");
			List<Store> foundStores = storeService.getStoresEligibleForAppointments(storeSearch, request.getRemoteAddr());
			if(logger.isDebugEnabled()){
				if(foundStores != null && !foundStores.isEmpty()){
					logger.debug("Zip or State Appointment Store Search found: " + foundStores);
				}
			}
			return foundStores;
		} else {
			if(store != null){
				String zip = store.getZip();
				StoreSearch storeSearch = storeService.createStoreSearchObject(zip);
				appointmentForm.setSearchedStoresBy("zipOrState");
				List<Store> foundStores = storeService.getStoresEligibleForAppointments(storeSearch, request.getRemoteAddr());
				if(logger.isDebugEnabled()){
					if(foundStores != null && !foundStores.isEmpty()){
						logger.debug("Zip or State Appointment Store Search found: " + foundStores);
					}
				}
				return foundStores;
			}
		}
		
		return null;
	}
	
	public List<Store> searchForClosestAppointmentStores(String ipAddress, StoreSearch storeSearch){	
		List<Store> foundStores = storeService.getStoresEligibleForAppointments(storeSearch, ipAddress);
		
		return foundStores;
	}
	
	public void stripInvalidVehicleData(AppointmentForm appointmentForm) {
		if(!StringUtils.isNullOrEmpty(appointmentForm.getVehicleMakeName())){
			String vehMake = appointmentForm.getVehicleMakeName();
			if (vehMake.indexOf(",") > 0) {
				vehMake = vehMake.substring(0, vehMake.indexOf(","));
				appointmentForm.setVehicleMakeName(vehMake);
			}
		}
		if(!StringUtils.isNullOrEmpty(appointmentForm.getVehicleModelName())){
			String vehModel = appointmentForm.getVehicleModelName();
			if (vehModel.indexOf(",") > 0) {
				vehModel = vehModel.substring(0, vehModel.indexOf(","));
				appointmentForm.setVehicleModelName(vehModel);
			}
		}
		if(!StringUtils.isNullOrEmpty(appointmentForm.getVehicleSubmodel())){
			String vehSubmodel = appointmentForm.getVehicleSubmodel();
			if (vehSubmodel.indexOf(",") > 0) {
				vehSubmodel = "I don't know";
				appointmentForm.setVehicleSubmodel(vehSubmodel);
			}
		}
		if(!StringUtils.isNullOrEmpty(appointmentForm.getAcesVehicleId())){
			String vehAcesVehicleId = appointmentForm.getAcesVehicleId();
			if (vehAcesVehicleId.indexOf(",") > 0) {
				vehAcesVehicleId = vehAcesVehicleId.substring(0, vehAcesVehicleId.indexOf(","));
				appointmentForm.setAcesVehicleId(vehAcesVehicleId);
			}
		}
	}
	
	public void saveVehicleDataToCache(HttpServletRequest request, AppointmentForm appointmentForm){		
		HttpSession session = request.getSession();
		//Year
		CacheDataUtils.setCachedVehicleYear(session, appointmentForm.getVehicleYear());
		//Make
		CacheDataUtils.setCachedVehicleMake(session, appointmentForm.getVehicleMakeName());
		//Model
		CacheDataUtils.setCachedVehicleModel(session, appointmentForm.getVehicleModelName());
		//Submodel
		CacheDataUtils.setCachedVehicleSubmodel(session, appointmentForm.getVehicleSubmodel());
		//Tpms
		CacheDataUtils.setCachedVehicleTpms(session, appointmentForm.getTpms());
		//Base Vehicle Id
		CacheDataUtils.setCachedAcesVehicleId(session, appointmentForm.getAcesVehicleId());
		//Selected Services
		String[] choiceArr = appointmentForm.getMaintenanceChoices();
		String choices = "";
		if(choiceArr != null && choiceArr.length > 0){
			choices = StringUtils.join(choiceArr, ",");
		}
		
		appointmentForm.setSelectedChoices(choices);
		//CacheDataUtils.setCachedSelectedServices(session, choices);
	}
	

	public boolean doesStoreAllowScheduleAppointment(Store store){
		
		//storeBean.onlineAppointmentActiveFlag == 0
		if(store != null && store.getOnlineAppointmentActiveFlag().equals(new BigDecimal(0))){
			return false;
		}
		
		return true;
	}
	
	public boolean isMobileTireInstallStoreWithoutQuote(Store store, AppointmentForm appointmentForm) {
		if (store != null && "MVAN".equalsIgnoreCase(store.getStoreType())) {
			if (appointmentForm.getParsedComments() == null || appointmentForm.getParsedComments().getRawComments().isEmpty() || appointmentForm.getMaintenanceChoices() == null || appointmentForm.getMaintenanceChoices().length == 0) {
				return true;
			}
		}
		return false;
	}
	
	public void saveCustInfo(AppointmentForm apptForm){
		try {
			// save customer info to table
			AppointmentCustomer apptCust = new AppointmentCustomer();
			apptCust.setFirstName(apptForm.getFirstName());
			apptCust.setLastName(apptForm.getLastName());
			apptCust.setAddress1(apptForm.getAddress());
			apptCust.setCity(apptForm.getCity());
			apptCust.setState(apptForm.getState());
			apptCust.setZip(apptForm.getZip());
			//There is no extension field on the pojo, so cram it into the phone field
			StringBuffer completePhone = new StringBuffer(apptForm.getDaytimePhone());		
			if(!StringUtils.isNullOrEmpty(apptForm.getDaytimePhoneExt())){
				completePhone.append(EXTENSION);
				completePhone.append(apptForm.getDaytimePhoneExt());
			}
			apptCust.setDaytimePhone(completePhone.toString());
			if(!StringUtils.isNullOrEmpty(apptForm.getEmailAddress())){
				apptCust.setEmailAddress(apptForm.getEmailAddress().trim());
			}
			apptCust.setEmailSignup(apptForm.getEmailSignup());	
			apptCust.setWebSite(config.getAppName());
			apptCust.setCreatedDate(new Date());
			apptDAO.addAppointmentCustomer(apptCust);
			
			// save customer info to form
			if (apptCust != null) {
				apptForm.setAppointmentCustomer(apptCust);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void setAppointmentMetadata(AppointmentForm appointmentForm){
		logger.info("Inside getAppointmentMetadata sn ="+ appointmentForm.getStoreNumber() 
				+ " services len = "+appointmentForm.getMaintenanceChoices().length);
		long start = System.currentTimeMillis();

//		This will work if we use the database to get the service list but currently the services
//		are coming from the database
		
//		List<AppointmentServiceAndCat> serviceDetailsList = appointmentService.getAppointmentServices();
//		List<String> servicesDesc = new ArrayList<String>();
//		for(String choiceId : appointmentForm.getServiceIds().split(",")){
//			for(AppointmentServiceAndCat serviceAndCat : serviceDetailsList){
//				if(String.valueOf(serviceAndCat.getServiceId()).equals(choiceId)){
//					servicesDesc.add(serviceAndCat.getServiceDesc());
//					break;
//				}
//			}
//		}
		
		String[] servicesIdArr = appointmentForm.getMaintenanceChoices();
		List<String> servicesDesc = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Map<String,String> servicesMap = appointmentForm.getServicesMap();
		Set<String> serviceMapKeys = servicesMap.keySet();
		
		for(String serviceId : servicesIdArr){
			for(String serviceIdKey : serviceMapKeys){
				if(serviceId.equals(serviceIdKey)){
					servicesDesc.add(servicesMap.get(serviceIdKey));
					break;
				}
			}
		}
		
		//add the other service description when there is a customer miscellaneous repair comment added
		if((appointmentForm.getCustomerComments() != null && !appointmentForm.getCustomerComments().isEmpty())){
			servicesDesc.add("Other");			
		}
		
		String selectedServicesCSV = org.apache.commons.lang3.StringUtils.join(servicesDesc, ",");
		appointmentForm.setAp_serviceDescriptions(selectedServicesCSV);
		logger.info("selectedServicesCSV ======"+selectedServicesCSV);

		if(!ServerUtil.isNullOrEmpty(appointmentForm.getStoreNumber()) && !ServerUtil.isNullOrEmpty(servicesDesc)){
			Map<String,HashMap<String,String>> response = svc.getAppointmentMetadata(String.valueOf(appointmentForm.getStoreNumber()), servicesDesc);	
			if(response.get("error") != null && response.containsKey("error")){
				Map<String,String> errors = response.get("error");
				List<String> schedulerErrors = new ArrayList<String>();
				schedulerErrors.addAll(errors.values());
				appointmentForm.setSchedulerErrors(schedulerErrors);
			}else{
				Map<String,String> map = response.get("data");
				appointmentForm.setLocationId(new Long(map.get("locationId")));
				appointmentForm.setEmployeeId(new Long(map.get("employeeId")));
				appointmentForm.setAppointmentStatusId(new Long(map.get("statusId")));
				appointmentForm.setAppointmentStatus(map.get("statusDescription"));
				appointmentForm.setAp_serviceIds(map.get("serviceId")+ (map.get("additionalServices")!= null 
						&& map.get("additionalServices").length() > 0 ? ","+map.get("additionalServices") : ""));
			}
		}
		long end = System.currentTimeMillis();
		System.err.println("Appointment Metadata processing time = "+ (end-start));
	}

}
