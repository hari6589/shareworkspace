package com.bsro.controller.appointment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.appointment.AppointmentSchedule;
import com.bsro.pojo.form.AppointmentForm;
import com.bsro.service.appointment.AppointmentAjaxService;
import com.bsro.service.appointment.AppointmentTimesService;
import com.bsro.service.appointment.rules.AppointmentRulesService;
import com.bsro.service.store.StoreService;

@Controller
public class AppointmentAjaxController {
	
	@Autowired
	AppointmentRulesService appointmentRulesService;
	
	@Autowired
	AppointmentTimesService appointmentTimesService;
	
	@Autowired
	AppointmentAjaxService appointmentAjaxService;
	
	@Autowired
	StoreService storeService;
	@Autowired
	private com.bsro.service.appointment.AppointmentService appointmentService;

	private static final String FCAC_STORE = "FCAC";
	private SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE");
	private static final Integer NUM_DAYS = 60;
	
	public AppointmentTimesService getAppointmentTimesService() {
		return appointmentTimesService;
	}

	public void setAppointmentTimesService(
			AppointmentTimesService appointmentTimesService) {
		this.appointmentTimesService = appointmentTimesService;
	}

	public AppointmentRulesService getAppointmentRulesService() {
		return appointmentRulesService;
	}

	public void setAppointmentRulesService(
			AppointmentRulesService appointmentRulesService) {
		this.appointmentRulesService = appointmentRulesService;
	}
	
	private SimpleDateFormat slickcomboValue = new SimpleDateFormat("h:mma");
//	private SimpleDateFormat slickcomboText = new SimpleDateFormat("hh:mma");

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	}
	
	
	@RequestMapping(value = "/common/getMonthAndDates.htm", method = RequestMethod.GET)
	public @ResponseBody SlickcomboData getAppointmentMonthAndDateFromSchedule(HttpServletRequest request, HttpSession session, 
			@RequestParam(value="storeNumber", required=false) String storeNumber,
			@RequestParam(value="locationId", required=false) String locationId,
			@RequestParam(value="employeeId", required=false)String employeeId) throws IOException {
	
//		Util.debug(" +_+ in AppointmentAjaxController.getAppointmentMonthAndDate");
		long start = System.currentTimeMillis();
		AppointmentSchedule schedule = (AppointmentSchedule)session.getAttribute("appointmentSchedule");
		SimpleDateFormat currFormat = new SimpleDateFormat("yyyyMMdd");
		//if no schedule has been set yet, just return empty.
		if(schedule == null ) return new SlickcomboData();
		
		List<String> appointmentMonthAndDates = new ArrayList<String>();
		SlickcomboData data = new SlickcomboData();
		
		Long storeNum = null;
		if(!ServerUtil.isNullOrEmpty(storeNumber)) {
			storeNum = new Long(storeNumber);
		}
		
		BSROWebServiceResponse schedulerRes = null;
		Calendar cal = Calendar.getInstance();
		String startDate = currFormat.format(cal.getTime());
		//if there is no location and employee information, return empty schedule data
		if(locationId == null || employeeId == null) return new SlickcomboData();
		
		// get the number of days out to schedule (from database).
		int daysOut = appointmentRulesService.getNumberOfEligibleDays()
						.getAppointmentRuleValue().intValue();
			
		//get the response back from availability service
		schedulerRes = appointmentTimesService.getAvailableDays(new Long(locationId), new Long(employeeId),startDate, daysOut);
		if(schedulerRes != null && schedulerRes.getPayload() != null){
			@SuppressWarnings({ "unchecked" })
			List<LinkedHashMap<String,String>> dataList = (List<LinkedHashMap<String,String>>)schedulerRes.getPayload();
			for(LinkedHashMap<String,String> map : dataList){
				String aDateStr = map.get("availableDate");					
				//SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MMMMMM-d");
				SimpleDateFormat monthFormat = new SimpleDateFormat("MMMMMM");
				SimpleDateFormat dayFormat = new SimpleDateFormat("d");
				Date aDate = null;
				try {
					aDate =  currFormat.parse(aDateStr);
				} catch (ParseException e) {
					e.printStackTrace();
					System.err.println("Incompatible date from availablity web service");
				}
				String sortedMonth = monthFormat.format(aDate);
				String sortedDate = dayFormat.format(aDate);
				//uDaysArr.add(newDateStr);
				appointmentMonthAndDates.add(sortedMonth + "|" + sortedDate);
				//System.out.println(sortedMonth + "|" + sortedDate);
			}				
		}
		long end = System.currentTimeMillis();
		System.err.println("Get Available days processing time = "+ (end-start));
		
		data.setData(appointmentMonthAndDates);		
		return data;
	}
	@RequestMapping(value = "/common/getDates.htm", method = RequestMethod.GET)
	public @ResponseBody SlickcomboTimeData getAppointmentDateFromSchedule(HttpServletRequest request, HttpSession session, 
			@RequestParam(value="storeNumber", required=false) String storeNumber,
			@RequestParam(value="locationId", required=false) String locationId,
			@RequestParam(value="employeeId", required=false) String employeeId,
			@RequestParam(value="month", required=false) String month,
			@RequestParam(value="startDate", required=false) String startDate,
			@RequestParam(value="numDays", required=false) String numDays) throws IOException {
		
	
		//Util.debug(" +_+ in AppointmentAjaxController.getAppointmentMonthAndDate");
		AppointmentSchedule schedule = (AppointmentSchedule)session.getAttribute("appointmentSchedule");
		
		//if no schedule has been set yet, just return empty.
		if(schedule == null ) return new SlickcomboTimeData();
		
		if(ServerUtil.isNullOrEmpty(schedule.getStoreNumber())) schedule.setStoreNumber(storeNumber);
			return appointmentAjaxService.getDatesFromSchedulerService(startDate, new Integer(numDays), locationId, employeeId);		
	}
	
	@RequestMapping(value = "/common/getTimes.htm", method = RequestMethod.GET)
	public @ResponseBody SlickcomboTimeData getTimesFromSchedule(HttpServletRequest request, HttpSession session,
			@RequestParam(value="storeNumber", required=false) String storeNumber, 
			@RequestParam(value="month", required=false) String month, 
			@RequestParam(value="date", required=false) String date) throws IOException {
	
		//Util.debug(" +_+ in AppointmentAjaxController.getTimesFromSchedule month " + month +" date " + date);
		AppointmentSchedule schedule = (AppointmentSchedule)session.getAttribute("appointmentSchedule");
		
		//if no schedule has been set yet, just return empty.
		//this causes slickcombo to kick user back to beginning of funnel
		//if schedule is set but no store number is present, set the store store from parameter
		if(schedule == null ) return new SlickcomboTimeData();
		else if(ServerUtil.isNullOrEmpty(schedule.getStoreNumber())) schedule.setStoreNumber(storeNumber);
		
		//Util.debug("This is the schedule found in the session: " + schedule.printReport());
				
		return appointmentAjaxService.getTimesFromSchedule(month, date, schedule);
	}
	
	
	@RequestMapping(value = "/common/getDropoffTimes.htm", method = RequestMethod.GET)
	public @ResponseBody SlickcomboTimeData getDropoffTimes(HttpServletRequest request, HttpSession session,
			@RequestParam(value="storeNumber", required=false) String storeNumber, 
			@RequestParam(value="month", required=false) String month, 
			@RequestParam(value="date", required=false) String date,
			@RequestParam(value="services", required=false) String services) throws IOException {
	
		//Util.debug(" +_+ in AppointmentAjaxController.getTimesFromSchedule month " + month +" date " + date);
		AppointmentSchedule schedule = (AppointmentSchedule)session.getAttribute("appointmentSchedule");
		
		//if no schedule has been set yet, just return empty.
		//this causes slickcombo to kick user back to beginning of funnel
		//if schedule is set but no store number is present, set the store store from parameter
		if(schedule == null ) return new SlickcomboTimeData();
		else {
			if(ServerUtil.isNullOrEmpty(schedule.getStoreNumber())){ 
				schedule.setStoreNumber(storeNumber);
			}
			if(ServerUtil.isNullOrEmpty(schedule.getServices()))	{
				String[] serviceArr = services.split(",");
				schedule.setServices(serviceArr);
			}
		}
		//Util.debug("This is the schedule found in the session: " + schedule.printReport());				
		return appointmentAjaxService.getDropOffPickUpTimesFromSchedule(month, date, schedule, "dropOff");
	}
	
	@RequestMapping(value = "/common/getPickupTimes.htm", method = RequestMethod.GET)
	public @ResponseBody SlickcomboTimeData getPickupTimes(HttpServletRequest request, HttpSession session,
			@RequestParam(value="storeNumber", required=false) String storeNumber, 
			@RequestParam(value="month", required=false) String month, 
			@RequestParam(value="date", required=false) String date,
			@RequestParam(value="services", required=false) String services) throws IOException {
	
		//Util.debug(" +_+ in AppointmentAjaxController.getTimesFromSchedule month " + month +" date " + date);
		AppointmentSchedule schedule = (AppointmentSchedule)session.getAttribute("appointmentSchedule");
		
		//if no schedule has been set yet, just return empty.
		//this causes slickcombo to kick user back to beginning of funnel
		//if schedule is set but no store number is present, set the store store from parameter
		if(schedule == null ) return new SlickcomboTimeData();
		else {
			if(ServerUtil.isNullOrEmpty(schedule.getStoreNumber())){ 
				schedule.setStoreNumber(storeNumber);
			}
			if(ServerUtil.isNullOrEmpty(schedule.getServices()))	{
				String[] serviceArr = services.split(",");
				schedule.setServices(serviceArr);
			}
		}
		//Util.debug("This is the schedule found in the session: " + schedule.printReport());				
		return appointmentAjaxService.getDropOffPickUpTimesFromSchedule(month, date, schedule, "pickUp");
	}
	
	@RequestMapping(value = "/common/getAppointmentTimes.htm", method = RequestMethod.GET)
	public @ResponseBody SlickcomboTimeData getAppointmentTimes(HttpServletRequest request, HttpSession session,
			@RequestParam(value="storeNumber", required=false) String storeNumber, 
			@RequestParam(value="month", required=false) String month, 
			@RequestParam(value="date", required=false) String date,	
			@RequestParam(value="year", required=false) String year,	
			@RequestParam(value="services", required=false) String services,
			@RequestParam(value="locationId", required=false) String locationId,
			@RequestParam(value="employeeId", required=false) String employeeId) throws IOException {
	
		SlickcomboTimeData data = new SlickcomboTimeData();
		//Util.debug(" +_+ in AppointmentAjaxController.getTimesFromSchedule month " + month +" date " + date);
		AppointmentSchedule schedule = (AppointmentSchedule)session.getAttribute("appointmentSchedule");
		//if no schedule has been set yet, just return empty.
		//this causes slickcombo to kick user back to beginning of funnel
		//if schedule is set but no store number is present, set the store store from parameter
		if(schedule == null ) return new SlickcomboTimeData();
		else {
			if(ServerUtil.isNullOrEmpty(schedule.getStoreNumber())) schedule.setStoreNumber(storeNumber);
			if(ServerUtil.isNullOrEmpty(schedule.getServices()))	{
				String[] serviceArr = services.split(",");
				schedule.setServices(serviceArr);
			}
		}
		Long storeNum = null;
		if(!ServerUtil.isNullOrEmpty(storeNumber)) {
			storeNum = new Long(storeNumber);
		}
		return appointmentAjaxService.getTimesFromSchedulerService(month, date, year,services, locationId, employeeId);	
	}
	
	@RequestMapping(value = "/common/selectTimes.htm", method = RequestMethod.GET)
	public @ResponseBody SlickcomboData getAppointmentTimesNew(HttpServletRequest request, HttpSession session, 
			@RequestParam(value="storeNumber", required=false) String storeNumber, 
			@RequestParam(value="month", required=false) String month, 
			@RequestParam(value="date", required=false) String date) throws IOException {
		
//		Util.debug(" +_+ in AppointmentAjaxController.getAppointmentTimesNew: month " + month +" date " + date);
		AppointmentSchedule schedule = (AppointmentSchedule)session.getAttribute("appointmentSchedule");
		
//		Util.debug("This is the schedule found in the session: " + schedule.printReport());
//		 AppointmentSchedule appointmentSchedule = af.getAppointmentSchedule();
//		if(appointmentSchedule==null)
//			return null;
		
		Map map = new HashMap();
		List<String> appointmentDays = null;
		SlickcomboData data = new SlickcomboData();

//		boolean isGetTimes = false;
//        boolean isGetDays = false;
//		if(!ServerUtil.isNullOrEmpty(storeNumber) && !ServerUtil.isNullOrEmpty(month) && !ServerUtil.isNullOrEmpty(date)) {
//	            isGetTimes = true;
//        } else if(!ServerUtil.isNullOrEmpty(storeNumber)) {
//	            isGetDays = true;
//        }	        
//		if(isGetTimes) {
//			List l = appointmentTimesService.getTimesForDate(new Long(storeNumber), month, date, true);
//			//TODO List l  = appointmentSchedule.getTimes(month, date);
////			List l = appointmentTimesService.getTimesForDate(storeNumber, month, date, useClassicRules);
//
//			Map m = new LinkedHashMap();
//			for(int i=0; i<l.size(); i++) {
//				String value = (String)l.get(i);
//				m.put(value, value);
//			}
////			request.setAttribute("result", m);
////			data.setData(m);
//			
//		} else if(isGetDays) {
//	            
//            //getMonhts
//            //List l = appointmentTimesService.getMonths(new Long(storeNumber));	 
//			List l  = appointmentSchedule.getMonths();
//            appointmentDays = new ArrayList();
//            
//            for(int i=0; i<l.size(); i++) {
//                    String currentMonth = (String)l.get(i);
////                    List l2 = appointmentTimesService.getDatesForMonth(new Long(storeNumber), currentMonth, useClassicRules);
//                   // List l2 = appointmentTimesService.getDatesForMonth(new Long(storeNumber), currentMonth, true);
//                    List l2  = appointmentSchedule.getDates(currentMonth);
//                    for(int j=0; j<l2.size(); j++) {
//                            String currentDay = (String)l2.get(j);
//                            appointmentDays.add(currentMonth+"|"+currentDay);
//                    }
//            }
//        }
		
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonRequest = new String();
//		try{
//			jsonRequest = mapper.writeValueAsString(appointmentDays);
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
		
		//Util.debug("JSON REQUEST" + jsonRequest);
		
		data.setData(appointmentDays);
		return data;
	}
	@RequestMapping(value="/appointment/set-appt-metadata.htm")
	public @ResponseBody AppointmentForm setApptMetada(HttpServletRequest request, HttpSession session, @RequestBody AppointmentForm apptForm, @RequestParam(value="storeNumber", required=false) Long storeNumber, Model model) {
		appointmentService.setAppointmentMetadata(apptForm);
		return apptForm;
	}
	private List<SlickcomboTime> getInvalidSlickcomboList(){
		List<SlickcomboTime> invalidTimes = new ArrayList<SlickcomboTime>();
		SlickcomboTime time = new SlickcomboTime();
		time.setText("NA");
		time.setValue("NA");
		invalidTimes.add(time);
		return invalidTimes;
	}
			
}


