package com.bsro.service.appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.appointment.AppointmentSchedule;
import com.bfrc.pojo.appointment.AppointmentServiceEnum;
import com.bfrc.pojo.appointment.AppointmentStoreHour;
import com.bsro.controller.appointment.SlickcomboTime;
import com.bsro.controller.appointment.SlickcomboTimeData;
import com.bsro.pojo.appointment.DateStringComparator;
import com.bsro.pojo.appointment.YearMonthComparator;
import com.bsro.service.appointment.rules.AppointmentRulesService;
import com.bsro.service.store.StoreService;


@Service("appointmentAjaxService")
public class AppointmentAjaxServiceImpl implements AppointmentAjaxService {
	
	private final Log logger = LogFactory.getLog(AppointmentAjaxServiceImpl.class);
	
	@Autowired
	AppointmentRulesService appointmentRulesService;
	
	@Autowired
	AppointmentTimesService appointmentTimesService;
	
	@Autowired
	StoreService storeService;
	
	private SimpleDateFormat slickcomboValue = new SimpleDateFormat("h:mma");
	private SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE");
	private static final Integer NUM_DAYS = 180;
	
	
	public AppointmentRulesService getAppointmentRulesService() {
		return appointmentRulesService;
	}

	public void setAppointmentRulesService(
			AppointmentRulesService appointmentRulesService) {
		this.appointmentRulesService = appointmentRulesService;
	}
	
	/* (non-Javadoc)
	 * @see com.bsro.service.appointment.AppointmentAjaxService#getTimesFromSchedule(java.lang.String, java.lang.String, com.bfrc.pojo.appointment.AppointmentSchedule)
	 */
	@Override
	public SlickcomboTimeData getTimesFromSchedule(String month, String date,
			AppointmentSchedule schedule) {
		SlickcomboTimeData data = new SlickcomboTimeData();
		List<SlickcomboTime> appointmentTimes = new ArrayList<SlickcomboTime>();
		
		//get one hour before close
		//get interval for minutes - for pilot stores the interval is different than regular stores
		int interval = 0;
		interval = appointmentRulesService.getAppointmentIntervalPilot().getAppointmentRuleValue().intValue();
			
		int timeBeforeClose = appointmentRulesService.getLatestEligibleAppointmentTime().getAppointmentRuleValue().intValue();

		//replace possible s in value of date for classic funnel
		if (date != null) {
			date = date.replace("S", "");
			date = date.replace("s", "");
		}
		//get date key set for first month, order that.
		Map<String, AppointmentStoreHour> dates = null;
		if (schedule != null) {
			dates = schedule.getScheduleMap().get(month);
		}
		//get first day in that ordered date keyset, and adjust time.

		AppointmentStoreHour storeHours = null;
		if (dates != null) {
			storeHours = dates.get(date);
		}
		if(storeHours == null) {
			data.setData(new ArrayList<SlickcomboTime>());
			return data;
		}
						
		//some days don't start at the store open, so we should check the firstTimeToschedule,
		//otherwise, just assume we start at store open.
		String beginTime;
		if(!StringUtils.hasText(storeHours.getFirstTimeToSchedule())){
			beginTime = storeHours.getOpenTime();
		}
		else {
			beginTime = storeHours.getFirstTimeToSchedule();
		}
		
		//get colon index
		int indexOfColon = beginTime.indexOf(":");
		//get Hours
		int beginTimeHour = Integer.parseInt(beginTime.substring(0, indexOfColon));
		//get Minutes
		int beginTimeMinute = Integer.parseInt(beginTime.substring(indexOfColon+1));
		
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.HOUR_OF_DAY, beginTimeHour);
		startCalendar.set(Calendar.MINUTE, beginTimeMinute);
		
		String endTime = storeHours.getCloseTime();
		//get colon index
		indexOfColon = endTime.indexOf(":");
		//get Hours
		int endTimeHour = Integer.parseInt(endTime.substring(0, indexOfColon));
		//get Minutes
		int endTimeMinute = Integer.parseInt(endTime.substring(indexOfColon+1));

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(Calendar.HOUR_OF_DAY, endTimeHour - timeBeforeClose);
		endCalendar.set(Calendar.MINUTE, endTimeMinute);
		
		//adjust for the next interval time.  
		//adding adjusts for any hour shifts
		int unroundedMinutes = startCalendar.get(Calendar.MINUTE);
		int mod = unroundedMinutes % interval;
		startCalendar.add(Calendar.MINUTE, mod == 0 ? 0 : interval - mod);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);

//		logger.debug("startCalendar: " + startCalendar);
//		logger.debug("endCalendar: " + endCalendar);
		while(startCalendar.before(endCalendar)){
			SlickcomboTime time = new SlickcomboTime();
			time.setValue(slickcomboValue.format(startCalendar.getTime()).toLowerCase().replaceAll("m", ""));
			time.setText(slickcomboValue.format(startCalendar.getTime()).toLowerCase());
			
			startCalendar.add(Calendar.MINUTE, interval);
			
			appointmentTimes.add(time);
		}		
		
		data.setData(appointmentTimes);
		return data;
	}
	
	/* (non-Javadoc)
	 * @see com.bsro.service.appointment.AppointmentAjaxService#getAppointmentDatesFromSchedule(java.lang.String, com.bfrc.pojo.appointment.AppointmentSchedule)
	 */
	@Override
	public SlickcomboTimeData getAppointmentDatesFromSchedule(String month,
			AppointmentSchedule schedule) {
		List<SlickcomboTime> appointmentMonthAndDates = new ArrayList<SlickcomboTime>();
		SlickcomboTimeData data = new SlickcomboTimeData();
		
		//if no schedule has been set yet, just return empty.
		if(schedule == null || !StringUtils.hasText(month)) return new SlickcomboTimeData();
		
		//get date key set for first month, order that.
		Set<String> dates = schedule.getScheduleMap().get(month).keySet();
		//the natural ordering should be sufficient
		//using different comparator that checks the Integer value, based on the string. 
		SortedSet<String> sortedDates = new TreeSet<String>(new DateStringComparator());
		sortedDates.addAll(dates);
		//get first day in that ordered date keyset, and adjust time.
		for(String sortedDate : sortedDates){
			SlickcomboTime slickComboTime = new SlickcomboTime();
			//String sortedDateNoLeadingZeroes = org.apache.commons.lang.StringUtils.stripStart(sortedDate, "0");
			slickComboTime.setText(sortedDate);
			slickComboTime.setValue(sortedDate);
			try{
				//if sat or sun then add s to the value for validation
			if("sat".equalsIgnoreCase(dayOfWeekFormat.format(schedule.getDateMap().get(month+sortedDate)))
					||"sun".equalsIgnoreCase(dayOfWeekFormat.format(schedule.getDateMap().get(month+sortedDate))) )
				slickComboTime.setValue(sortedDate+"S");
			}catch(Exception e){
//				e.printStackTrace();
			}
			appointmentMonthAndDates.add(slickComboTime);
		}
	
		data.setData(appointmentMonthAndDates);
		return data;
	}

	@Override
	public SlickcomboTimeData getTimesFromSchedulerService(String month,
			String day, String year, String selectedServiceIds, String locationId, String employeeId) {
		long start = System.currentTimeMillis();
		SlickcomboTimeData data = new SlickcomboTimeData();
		List<SlickcomboTime> appointmentTimes = new ArrayList<SlickcomboTime>();

		//Set<String> yearsList = schedule.getYearMap().keySet();
		//SortedSet<String> sortedYears = new TreeSet<String>(yearsList);
		//logger.debug("sortedYears.size()="+sortedYears.size());
//		for(String currentYear : sortedYears){
//			logger.debug("Curr year = "+ currentYear);
//			//List<String> monthsList = schedule.getYearMap().get(currentYear);
//			for(String currentMonth : monthsList){
//				logger.debug("Curr month = "+ currentMonth);
//				if(currentMonth.equalsIgnoreCase(month)){
//					year = currentYear;
//					break;
//				}
//			}
//			if(!ServerUtil.isNullOrEmpty(year)){
//				break;
//			}
//		}
		//String storeNumber = schedule.getStoreNumber();
		//logger.debug("storeNumber = "+ storeNumber);
		Date date=null;
		try {
			date = new SimpleDateFormat("MMMMMM", Locale.ENGLISH).parse(month.toUpperCase());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int monthInt = cal.get(Calendar.MONTH);
	    logger.debug("monthInt="+monthInt);
		Calendar startDate = Calendar.getInstance();
		startDate.set(new Integer(year).intValue(), monthInt, new Integer(day).intValue());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String apptDate = format.format(startDate.getTime());
		
		logger.debug("apptDate ="+apptDate);
		//String[] selectedServices = schedule.getServices();
		//logger.debug("schedule.getServices().length = "+ schedule.getServices().length);
		//String selectedServicesCSV = org.apache.commons.lang.StringUtils.join(selectedServices, ",");
		
		BSROWebServiceResponse response= 
				appointmentTimesService.getAvailableTimesForService(new Long(locationId), new Long(employeeId), selectedServiceIds, apptDate);

		if(response != null && !ServerUtil.isNullOrEmpty(response.getPayload())){
			logger.info("response = "+response.getPayload());
			@SuppressWarnings("unchecked")
			List<LinkedHashMap<String,String>> dataList = (List<LinkedHashMap<String,String>>)response.getPayload();
			if(dataList != null && !dataList.isEmpty()){
				logger.info("dataList = "+dataList.size());
				for(LinkedHashMap<String,String> map: dataList){
					String availTime = map.get("availableTime");
					logger.info("time="+availTime);
					Date fDate=null;
					try {
						fDate = slickcomboValue.parse(availTime);
					} catch (ParseException e) {
					}

					SlickcomboTime time = new SlickcomboTime();
					time.setValue(slickcomboValue.format(fDate.getTime()).toLowerCase().replaceAll("m", ""));
					time.setText(slickcomboValue.format(fDate.getTime()).toLowerCase());
					logger.debug("time="+slickcomboValue.format(fDate.getTime()).toLowerCase());

					appointmentTimes.add(time);
				}			
			}else{
				//log the error in System Error
				if(response.getErrors()!= null && response.getErrors().hasGlobalErrors()){
					logError(response.getStatusCode(), response.getErrors().getGlobalErrors().get(0));
				}
			}
		}
		data.setData(appointmentTimes);
		long end = System.currentTimeMillis();
		System.err.println("Get Time Slots processing time = "+ (end-start));
		return data;
	}
	
	@Override
	public SlickcomboTimeData getDropOffPickUpTimesFromSchedule(String month,
			String date, AppointmentSchedule schedule, String option) {
		SlickcomboTimeData data = new SlickcomboTimeData();
		List<SlickcomboTime> appointmentTimes = new ArrayList<SlickcomboTime>();
		AppointmentStoreHour storeHours = null;
		
		//get interval for minutes - for pilot stores the interval is different than regular stores
		int interval = appointmentRulesService.getAppointmentIntervalPilot().getAppointmentRuleValue().intValue();
		
		//for oil change service the interval is always 30 minutes
		if(schedule.getServices() != null && schedule.getServices().length == 1
				&& schedule.getServices()[0].equals(AppointmentServiceEnum.OIL_AND_FILTER_CHANGE.getId())){
			interval = 30;
		}
		logger.debug("month="+month+" date="+date+ " sn="+schedule.getStoreNumber());
		//replace possible s in value of date for classic funnel
		date = date.replace("S", "");
		date = date.replace("s", "");
		
		//remove the leading zero in the date field for the mobile funnel
		date = date.replaceFirst("^0*", "");
		
		//get date key set for first month, order that.
		Map<String, AppointmentStoreHour> dates = schedule.getScheduleMap().get(month);
		//get first day in that ordered date keyset, and adjust time.
		if(dates != null){
			storeHours = dates.get(date);
		}

		if(storeHours == null) {
			//appointmentTimesService.createAppointmentSchedule(new Long(schedule.getStoreNumber()), new Date());
			data.setData(new ArrayList<SlickcomboTime>());
			return data;
		}
						
		//some days don't start at the store open, so we should check the firstTimeToschedule,
		//otherwise, just assume we start at store open.
		String beginTime;
		if(!StringUtils.hasText(storeHours.getFirstTimeToSchedule())){
			beginTime = storeHours.getOpenTime();
		}
		else {
			beginTime = storeHours.getFirstTimeToSchedule();
		}
		
		//get colon index
		int indexOfColon = beginTime.indexOf(":");
		//get Hours
		int beginTimeHour = Integer.parseInt(beginTime.substring(0, indexOfColon));
		//get Minutes
		int beginTimeMinute = Integer.parseInt(beginTime.substring(indexOfColon+1));
		
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.HOUR_OF_DAY, beginTimeHour);
		if(option.equalsIgnoreCase("pickUp")){
			startCalendar.set(Calendar.MINUTE, beginTimeMinute + interval);
		}else{
			startCalendar.set(Calendar.MINUTE, beginTimeMinute);
		}
		
		String endTime = storeHours.getCloseTime();
		//get colon index
		indexOfColon = endTime.indexOf(":");
		//get Hours
		int endTimeHour = Integer.parseInt(endTime.substring(0, indexOfColon));
		//get Minutes
		int endTimeMinute = Integer.parseInt(endTime.substring(indexOfColon+1));

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(Calendar.HOUR_OF_DAY, endTimeHour);
		if(option.equalsIgnoreCase("dropOff")){
			endCalendar.set(Calendar.MINUTE, endTimeMinute-interval);
		}else{
			endCalendar.set(Calendar.MINUTE, endTimeMinute);
		}
		
		//adjust for the next interval time.  
		//adding adjusts for any hour shifts
		int unroundedMinutes = startCalendar.get(Calendar.MINUTE);
		int mod = unroundedMinutes % interval;
		startCalendar.add(Calendar.MINUTE, mod == 0 ? 0 : interval - mod);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);

//		logger.debug("startCalendar: " + startCalendar);
//		logger.debug("endCalendar: " + endCalendar);
		while(startCalendar.before(endCalendar)){
			SlickcomboTime time = new SlickcomboTime();
			time.setValue(slickcomboValue.format(startCalendar.getTime()).toLowerCase().replaceAll("m", ""));
			time.setText(slickcomboValue.format(startCalendar.getTime()).toLowerCase());			
			startCalendar.add(Calendar.MINUTE, interval);			
			appointmentTimes.add(time);
		}		
		
		data.setData(appointmentTimes);
		return data;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public SlickcomboTimeData  getDatesFromSchedulerService(String startDate, Integer numDays,
			String locationId, String employeeId) {
		List<SlickcomboTime> appointmentMonthAndDates = new ArrayList<SlickcomboTime>();
		SlickcomboTimeData data = new SlickcomboTimeData();

		Date date=null;
		try {
			date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(startDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
//	    Calendar cal = Calendar.getInstance();
//	    cal.setTime(date);
//	    int monthInt = cal.get(Calendar.MONTH);
//	    logger.debug("monthInt="+monthInt);
//	    //Getting Maximum day for Given Month
//	  	int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//	  	logger.debug("maxDay = "+ maxDay);
//		
//	  	Calendar minDate = Calendar.getInstance();
//		minDate.set(new Integer(year).intValue(), monthInt, 1);
//		if(minDate.before(Calendar.getInstance())){
//			minDate = Calendar.getInstance();
//		}
//		
//		Calendar maxDate = Calendar.getInstance();		
//		maxDate.set(new Integer(year).intValue(), monthInt, maxDay);
		
//		String[] selectedServices = schedule.getServices();	
//		String selectedServicesCSV = org.apache.commons.lang.StringUtils.join(selectedServices, ",");

	
		BSROWebServiceResponse response=null;
		List<String> unavailableDays = new ArrayList<String>();
		try {
			response = appointmentTimesService.getAvailableDays(new Long(locationId), new Long(employeeId), startDate, numDays);
		} catch (Exception e1) {
			//e1.printStackTrace();
			System.err.println("Exception while calling getAvailableDays in class in method getDatesFromSchedulerService "+this.getClass().getSimpleName());
		}
		if(response != null){
			if(!ServerUtil.isNullOrEmpty(response.getPayload())){

				List<LinkedHashMap<String,String>> dataList = (List<LinkedHashMap<String,String>>)response.getPayload();
				for(LinkedHashMap<String,String> map : dataList){
					String availableDay = (String)map.get("availableDay");
					SlickcomboTime slickComboTime = new SlickcomboTime();
					slickComboTime.setText(availableDay);
					slickComboTime.setValue(availableDay);
					try{
						//if sat or sun then add s to the value for validation
						if("saturday".equalsIgnoreCase((String)map.get("dayName"))
								||"sunday".equalsIgnoreCase((String)map.get("dayName"))){
							slickComboTime.setValue(availableDay+"S");
						}
					}catch(Exception e){
					}
					appointmentMonthAndDates.add(slickComboTime);
				}

				//				logger.debug("daysArr.length() = "+ daysArr.length());
				//				for(int i=0; i<daysArr.length(); i++){
				//					JSONObject daysArrObj = daysArr.getJSONObject(i);
				//					if(daysArrObj.getString("intDayAvail").equals("0")){
				//						//get the unavailable days
				//						String dateStr = daysArrObj.getString("datDay");
				//						SimpleDateFormat wsResponseFormat = new SimpleDateFormat("yyyy-MM-dd");
				//						SimpleDateFormat dayFormat = new SimpleDateFormat("d");
				//						Date currDate = null;
				//						try {
				//							currDate = wsResponseFormat.parse(dateStr);
				//						} catch (ParseException e1) {
				//							e1.printStackTrace();
				//						}
				//						String day = dayFormat.format(currDate);
				//						logger.debug("day="+day);
				//						unavailableDays.add(day);
				//						logger.debug("udays.size="+unavailableDays.size());
				//					}
				//				}

			}else{
				//log the error in System Error
				if(response.getErrors()!= null && response.getErrors().hasGlobalErrors()){
					logError(response.getStatusCode(), response.getErrors().getGlobalErrors().get(0));
				}
			}
		}

//		for(String sortedDate : sortedDates){			
//			if(!unavailableDays.isEmpty()){
//				unavailableDay = false;
//				for(String uDayStr : unavailableDays){
//					if(sortedDate.equals(uDayStr)){
//						unavailableDay = true;
//						break;
//					}
//				}
//			}
//			if(!unavailableDay){
//				SlickcomboTime slickComboTime = new SlickcomboTime();
//				slickComboTime.setText(sortedDate);
//				slickComboTime.setValue(sortedDate);
//				try{
//					//if sat or sun then add s to the value for validation
//					if("sat".equalsIgnoreCase(dayOfWeekFormat.format(schedule.getDateMap().get(month+sortedDate)))
//							||"sun".equalsIgnoreCase(dayOfWeekFormat.format(schedule.getDateMap().get(month+sortedDate))) )
//						slickComboTime.setValue(sortedDate+"S");
//				}catch(Exception e){
//				}
//				appointmentMonthAndDates.add(slickComboTime);
//			}
//		}
	
		data.setData(appointmentMonthAndDates);
		return data;		
	}

	//TODO: check this may not be required as the scheduler is returning everything including
	// the year information.
	@Override
	public String getYearForMonth(String month, AppointmentSchedule schedule) {
		String year = "";
		if(schedule != null){
			Set<String> yearsList = schedule.getYearMap().keySet();
			SortedSet<String> sortedYears = new TreeSet<String>(yearsList);
			logger.debug("sortedYears.size()="+sortedYears.size());
			for(String currentYear : sortedYears){
				logger.debug("Curr year = "+ currentYear);
				List<String> monthsList = schedule.getYearMap().get(currentYear);
				for(String currentMonth : monthsList){
					logger.debug("Curr month = "+ currentMonth);
					if(currentMonth.equalsIgnoreCase(month)){
						year = currentYear;
						return year;
					}
				}
			}
		}
		return year;
	}

	@Override
	public Set<String> getMonthsFromSchedulerService(AppointmentSchedule schedule, Integer numDays, Long locationId, Long employeeId) {

		String storeNumber = schedule.getStoreNumber();
		String[] selectedServices = schedule.getServices();
		logger.debug("selectedServices.length = "+ selectedServices.length);

		String services = org.apache.commons.lang.StringUtils.join(selectedServices, ",");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		String start = sdf.format(cal.getTime());

		BSROWebServiceResponse response=null;
		try {
			response = appointmentTimesService.getAvailableDays(locationId, employeeId, start, numDays);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Set<String> appointmentMonths = new HashSet<String>();
		SimpleDateFormat resDateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat monthFormat = new SimpleDateFormat("MMMMMM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		if(response != null){
			if(!ServerUtil.isNullOrEmpty(response.getPayload())){
				try {
					@SuppressWarnings("unchecked")
					List<LinkedHashMap<String,String>> dataList = (List<LinkedHashMap<String,String>>)response.getPayload();

					for(LinkedHashMap<String,String> map : dataList){
						Date availableDate = resDateFormat.parse(String.valueOf(map.get("availableDate")));
						appointmentMonths.add(yearFormat.format(availableDate)+monthFormat.format(availableDate));
					}

				}  catch (ParseException e) {
					e.printStackTrace();
				}

			}else{
				//log the error in System Error
				if(response.getErrors()!= null && response.getErrors().hasGlobalErrors()){
					logError(response.getStatusCode(), response.getErrors().getGlobalErrors().get(0));
				}
			}
		}
		SortedSet<String> sortedApptMonths = new TreeSet<String>(new YearMonthComparator());
		sortedApptMonths.addAll(appointmentMonths);

		return sortedApptMonths;
	}
	
	@Override
	public Set<String> getMonthsFromSchedulerService(AppointmentSchedule schedule, Long locationId, Long employeeId) {
		return getMonthsFromSchedulerService(schedule, NUM_DAYS, locationId, employeeId);
	}
	
	@Override
	public String getStartDate(String year, String month) {
		SimpleDateFormat currFormat = new SimpleDateFormat("yyyyMMdd");
		Date date=null;
		try {
			date = new SimpleDateFormat("MMMMMM", Locale.ENGLISH).parse(month.toUpperCase());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int monthInt = cal.get(Calendar.MONTH);
	    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	  	
		Calendar minDate = Calendar.getInstance();
		minDate.set(new Integer(year).intValue(), monthInt, 1);
		if(minDate.before(Calendar.getInstance())){
			minDate = Calendar.getInstance();
		}
		
		return currFormat.format(minDate.getTime());
	}
	
	@Override
	public int getNumDays(String startDate, int maxDays) {
		SimpleDateFormat currFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
		Date date=null;
		Calendar sDate = Calendar.getInstance();
	    try {
			date = currFormat.parse(startDate);
			sDate.setTime(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		//Getting Maximum day for Given Month
		Calendar maxDate = Calendar.getInstance();
		int maxDay = sDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		maxDate.set(sDate.get(Calendar.YEAR), sDate.get(Calendar.MONTH), maxDay);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, (maxDays - 1));
		if(cal.before(maxDate)){
			maxDate = cal;
		}

		return daysBetween(sDate, maxDate);
	}
	
	private int daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();  
		int daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;
		}
		return daysBetween;  
	}
	
	private void logError(String code, String message){
		System.err.println("Error Code => "+ code);
		System.err.println("Error Message => "+ message);
	}
}
