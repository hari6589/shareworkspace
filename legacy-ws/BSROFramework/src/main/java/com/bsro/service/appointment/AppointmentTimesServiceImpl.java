package com.bsro.service.appointment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.resource.spi.IllegalStateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.framework.dao.appointment.ListAppointmentTimesOperator;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.appointment.AppointmentSchedule;
import com.bfrc.pojo.appointment.AppointmentServiceAndCat;
import com.bfrc.pojo.appointment.AppointmentStoreHour;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreHoliday;
import com.bfrc.pojo.store.StoreHolidayHour;
import com.bfrc.pojo.store.StoreHour;
import com.bfrc.pojo.zipcode.ZipCodeData;
import com.bsro.pojo.appointment.DateStringComparator;
import com.bsro.pojo.appointment.MonthNameComparator;
import com.bsro.pojo.form.AppointmentForm;
import com.bsro.service.appointment.rules.AppointmentRulesService;
import com.bsro.service.store.StoreService;
import com.bsro.service.time.TimeZoneDstService;
import com.bsro.service.time.TimeZoneDstServiceImpl;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.BSROWebserviceServiceImpl;

@Service("appointmentTimesService")
public class AppointmentTimesServiceImpl extends BSROWebserviceServiceImpl implements AppointmentTimesService {

	private Log logger;

	private static String GMT_TIME_ZONE = "GMT";
	private static final String PATH_WEBSERVICE_GET_DAY_AVAILABILITY = "appointment/availability/days";
	private static final String PATH_WEBSERVICE_GET_TIME_AVAILABILITY = "appointment/availability/times";

	// <!-- allowing for different times depending on version of funnel. -->
	// <!-- minFromClose - aka latest time allowed before store closing -->
	// <property name="classicMinFromClose" value="3"/>
	// <property name="newMinFromClose" value="1"/>
	// <!-- bufferedHours is the next allowable appt time from current time -->
	// <property name="classicBufferedHours" value="24"/>
	// <property name="newBufferedHours" value="4"/>

	public AppointmentTimesServiceImpl() {
		this.logger = LogFactory.getLog(AppointmentTimesServiceImpl.class);
	}

	@Autowired
	private ListAppointmentTimesOperator listAppointmentTimes;

	@Autowired
	private StoreService storeService;

	@Autowired
	private TimeZoneDstService tzdService;

	@Autowired
	private AppointmentRulesService appointmentRulesService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}

	private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
	private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("d");
	private SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE");
	private SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
	private SimpleDateFormat completeDateFormat = new SimpleDateFormat(
			"MMMM/d/yyyy");
	private final boolean DEBUG_MODE = false;

	public ListAppointmentTimesOperator getListAppointmentTimes() {
		return listAppointmentTimes;
	}

	public void setListAppointmentTimes(
			ListAppointmentTimesOperator listAppointmentTimes) {
		this.listAppointmentTimes = listAppointmentTimes;
	}

	private int classicBufferedHours = 24;
	private int newBufferedHours = 4;
	private int classicMinFromClose = 3;
	private int newMinFromClose = 1;

	@Override
	public List getDatesForMonth(Long storeNumber, String month,
			boolean useClassicRules) {
		HashMap map = new HashMap();

		map.put(ListAppointmentTimesOperator.STORE_NUMBER,
				String.valueOf(storeNumber));
		if (useClassicRules) {
			map.put(ListAppointmentTimesOperator.MIN_HOURS_FROM_CLOSE,
					classicMinFromClose);
			map.put(ListAppointmentTimesOperator.BUFFEREDHOURS,
					classicBufferedHours);
		} else {
			// NEW RULES
			map.put(ListAppointmentTimesOperator.MIN_HOURS_FROM_CLOSE,
					newMinFromClose);
			map.put(ListAppointmentTimesOperator.BUFFEREDHOURS,
					newBufferedHours);
		}

		map.put(ListAppointmentTimesOperator.MONTH, month);

		// need to carry this down.
		map.put(ListAppointmentTimesOperator.USE_CLASSIC_RULES, useClassicRules);

		Util.debug(" getTimesForDate: useClassicRules " + useClassicRules
				+ " storeNumber: " + storeNumber + " minFromClose "
				+ map.get(ListAppointmentTimesOperator.MIN_HOURS_FROM_CLOSE)
				+ " bufferedHours "
				+ map.get(ListAppointmentTimesOperator.BUFFEREDHOURS)
				+ " month " + month);

		List returnList = new ArrayList(0);
		try {
			String result = listAppointmentTimes.operate(map);
			returnList = (List) map.get("result");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	public List getTimesForDate(Long storeNumber, String month, String date,
			boolean useClassicRules) {
		HashMap map = new HashMap();

		map.put(ListAppointmentTimesOperator.STORE_NUMBER,
				String.valueOf(storeNumber));
		if (useClassicRules) {
			map.put(ListAppointmentTimesOperator.MIN_HOURS_FROM_CLOSE,
					classicMinFromClose);
			map.put(ListAppointmentTimesOperator.BUFFEREDHOURS,
					classicBufferedHours);
		} else {
			// NEW RULES
			map.put(ListAppointmentTimesOperator.MIN_HOURS_FROM_CLOSE,
					newMinFromClose);
			map.put(ListAppointmentTimesOperator.BUFFEREDHOURS,
					newBufferedHours);

		}

		map.put(ListAppointmentTimesOperator.MONTH, month);
		map.put(ListAppointmentTimesOperator.DATE, date.replaceAll("S", ""));

		// need to carry this down.
		map.put(ListAppointmentTimesOperator.USE_CLASSIC_RULES, useClassicRules);

		Util.debug(" getTimesForDate: useClassicRules " + useClassicRules
				+ " storeNumber: " + storeNumber + " minFromClose "
				+ map.get(ListAppointmentTimesOperator.MIN_HOURS_FROM_CLOSE)
				+ " bufferedHours "
				+ map.get(ListAppointmentTimesOperator.BUFFEREDHOURS)
				+ " month " + month + " date " + date);
		List returnList = new ArrayList(0);
		try {
			String result = listAppointmentTimes.operate(map);
			returnList = (List) map.get("result");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	public List getMonths(Long storeNumber) {
		HashMap map = new HashMap();
		map.put(ListAppointmentTimesOperator.STORE_NUMBER,
				String.valueOf(storeNumber));

		Util.debug(" getMonths: " + " storeNumber: " + storeNumber);

		List returnList = new ArrayList(0);
		try {
			String result = listAppointmentTimes.operate(map);
			returnList = (List) map.get("result");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnList;
	}

	public GregorianCalendar getTimeForStore(Long storeNumber, Date date) {
		if (storeNumber == null)
			return null;
		Store store = storeService.findStoreLightById(storeNumber.longValue());
		return getTimeForStore(store, date);
	}

	public GregorianCalendar getTimeForStore(Store store, Date date) {
		if (store == null)
			return null;
		ZipCodeData zcd = tzdService.getZipCodeDataByZip(store.getZip());
		TimeZone tz = TimeZone.getTimeZone(GMT_TIME_ZONE);
		GregorianCalendar adjustedTime = new GregorianCalendar(tz);
		if (date != null)
			adjustedTime.setTime(date);

		if (DEBUG_MODE)
			Util.debug("====calendar gmt time with set date ==="
					+ adjustedTime.getTime());
		try {
			if (zcd == null) {
				Util.debug("The zip code data doesn't exist for zip: "
						+ store.getZip());
				throw new IllegalStateException(
						"The zip code data doesn't exist for zip: "
								+ store.getZip());
			}
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
					"yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone(GMT_TIME_ZONE));
			// Local time zone
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat(
					"yyyy-MMM-dd HH:mm:ss");
			// Time in GMT
			if (date == null)
				date = new Date();
			Date gmtDateTime = dateFormatLocal
					.parse(dateFormatGmt.format(date));
			adjustedTime.setTime(gmtDateTime);

			if (DEBUG_MODE)
				Util.debug("====calendar local time ==="
						+ adjustedTime.getTime() + " ==utc= " + zcd.getUtc());
			// getting utc but its a decimal -6.0 = 6 hours behind, .0 or .5 are
			// only decimal value => 0 or 30 min offset
			String[] offsetArr = zcd.getUtc().split("\\.");
			int offsetHrs = Integer.parseInt(offsetArr[0]);
			int offsetMins = Integer.parseInt(offsetArr[1]) * 60;
			if (DEBUG_MODE)
				Util.debug("====offset hours, minutes values === " + offsetHrs
						+ " , " + offsetMins);
			adjustedTime.add(Calendar.HOUR_OF_DAY, (offsetHrs));
			adjustedTime.add(Calendar.MINUTE, (offsetMins));
			try {
				adjustedTime.setTimeZone(TimeZone
						.getTimeZone(TimeZoneDstServiceImpl.timeZoneMap.get(zcd
								.getTimeZone())));
			} catch (Exception e) {
				// Util.debug("ERROR: can't set time zone!!!!!");
				e.printStackTrace();
			}
			if (DEBUG_MODE)
				Util.debug("====adjusted store time ==="
						+ adjustedTime.getTime() + " offset = "
						+ adjustedTime.getTimeZone().getRawOffset());
		} catch (Exception e) {
			e.printStackTrace();
			adjustedTime.setTime(new Date());
			// Util.debug("====exception store time ==="+adjustedTime.getTime());
		}

		if ("Y".equals(zcd.getDst())) {
			if (tzdService.isInDayLightSavings(adjustedTime.getTime())) {
				adjustedTime.add(Calendar.HOUR_OF_DAY, 1);
			}

		}
		if (DEBUG_MODE)
			Util.debug("====adjusted store time for DST ==="
					+ adjustedTime.getTime());
		return adjustedTime;

	}

	public AppointmentSchedule createAppointmentSchedule(Long storeNumber,
			Date date) {
		if (storeNumber == null)
			return null;
		Store store = storeService.findStoreLightById(storeNumber.longValue());
		return createAppointmentSchedule(store, date);
	}

	public AppointmentSchedule createAppointmentSchedule(Store store, Date date) {
		GregorianCalendar storeTime = getTimeForStore(store, date);

		if (DEBUG_MODE)
			Util.debug("Using DATE ====" + date + " store time is "
					+ storeTime.getTime());
		GregorianCalendar loopTime = (GregorianCalendar) storeTime.clone();
		GregorianCalendar endTime = (GregorianCalendar) storeTime.clone();

		AppointmentSchedule appointmentSchedule = new AppointmentSchedule();
		Map<String, AppointmentStoreHour> storeHoursMap = new HashMap<String, AppointmentStoreHour>();
		Map<String, Map<String, AppointmentStoreHour>> scheduleMap = new HashMap<String, Map<String, AppointmentStoreHour>>();
		appointmentSchedule.setScheduleMap(scheduleMap);
		appointmentSchedule.setStoreNumber(store.getStoreNumber().toString());

		List<StoreHour> storeHours = storeService.getStoreHours(store
				.getStoreNumber());

		for (StoreHour storeHour : storeHours) {
			if (DEBUG_MODE)
				Util.debug("Adding "
						+ storeHour.getId().getWeekDay().toLowerCase());
			AppointmentStoreHour ash = new AppointmentStoreHour();
			ash.setOpenTime(storeHour.getOpenTime().trim());
			ash.setCloseTime(storeHour.getCloseTime().trim());
			storeHoursMap
					.put(storeHour.getId().getWeekDay().toLowerCase(), ash);
		}

		// get the number of days out to schedule (from database).
		int daysOut = appointmentRulesService.getNumberOfEligibleDays()
				.getAppointmentRuleValue().intValue();
		if (DEBUG_MODE)
			Util.debug("Eligible number of calendar days for Appointments: "
					+ daysOut);

		for (int i = 0; i < daysOut; i++) {
			String monthString = monthFormat.format(loopTime.getTime());
			String yearString = yearFormat.format(loopTime.getTime());
			String dateString = dateFormat.format(loopTime.getTime());
			String dayString = dayOfWeekFormat.format(loopTime.getTime());
			if (DEBUG_MODE)
				Util.debug("monthString " + monthString);
			if (DEBUG_MODE)
				Util.debug("dateString " + dateString);
			if (DEBUG_MODE)
				Util.debug("dayString " + dayString);

			if (appointmentSchedule.getScheduleMap().get(monthString) == null)
				appointmentSchedule.getScheduleMap().put(monthString,
						new HashMap<String, AppointmentStoreHour>());

			// if we have the month, but not the date, add it along with that
			// day's store hours.
			if (appointmentSchedule.getDateMap() == null) {
				appointmentSchedule.setDateMap(new HashMap<String, Date>());
			}
			if (appointmentSchedule.getYearMap() == null) {
				appointmentSchedule
						.setYearMap(new HashMap<String, List<String>>());
			}
			if (appointmentSchedule.getScheduleMap().get(monthString)
					.get(dateString) == null
					&& storeHoursMap.get(dayString.toLowerCase()) != null) {
				AppointmentStoreHour storeHour = storeHoursMap.get(dayString
						.toLowerCase());
				// need a new object for each day, otherwise only a reference is
				// obtained from the storeHoursMap.
				AppointmentStoreHour ash = new AppointmentStoreHour();
				ash.setOpenTime(storeHour.getOpenTime().trim());
				ash.setCloseTime(storeHour.getCloseTime().trim());
				appointmentSchedule.getScheduleMap().get(monthString)
						.put(dateString, ash);
				appointmentSchedule.getDateMap().put(monthString + dateString,
						loopTime.getTime());
				if (appointmentSchedule.getYearMap().get(yearString) == null) {
					appointmentSchedule.getYearMap().put(yearString,
							new ArrayList<String>());
				}
				if (!appointmentSchedule.getYearMap().get(yearString)
						.contains(monthString)) {
					appointmentSchedule.getYearMap().get(yearString)
							.add(monthString);
				}
			}

			loopTime.add(Calendar.DATE, 1);
		}

		if (DEBUG_MODE)
			Util.debug("AppointmentSchedule Created for all days: "
					+ appointmentSchedule.printReport());

		endTime.add(Calendar.DATE, daysOut);
		// Checking for holidays during the time period of the schedule
		List<StoreHoliday> storeHolidays = storeService
				.getStoreHolidaysBetweenDates(storeTime.getTime(),
						endTime.getTime());
		// if there are holidays, go through and set to null, then check for
		// special store hours and if they exist set them.
		if (storeHolidays != null && storeHolidays.size() > 0) {

			if (DEBUG_MODE)
				Util.debug("+++++found storeHolidays ");
			SimpleDateFormat holidayDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy");
			for (StoreHoliday storeHoliday : storeHolidays) {
				try {
					if (DEBUG_MODE)
						Util.debug("+++++store Holiday: "
								+ storeHoliday.getId().getMonth() + "/"
								+ storeHoliday.getId().getDay() + "/"
								+ storeHoliday.getId().getYear());
					Date holidayDate = holidayDateFormat.parse(storeHoliday
							.getId().getMonth()
							+ "/"
							+ storeHoliday.getId().getDay()
							+ "/"
							+ storeHoliday.getId().getYear());
					if (appointmentSchedule.getScheduleMap().get(
							monthFormat.format(holidayDate)) != null) {
						// if we found a holiday, remove it.
						appointmentSchedule.getScheduleMap()
								.get(monthFormat.format(holidayDate))
								.remove(dateFormat.format(holidayDate));
						StoreHolidayHour storeHolidayHour = storeService
								.getStoreHolidayHour(store.getStoreNumber(),
										storeHoliday.getHolidayId());
						if (storeHolidayHour != null) {
							if (DEBUG_MODE)
								Util.debug("+++++found storeHolidayHour ");
							// This only sets open and close time on the
							// storeHour object because that will be all we need
							AppointmentStoreHour apptStoreHour = new AppointmentStoreHour();
							apptStoreHour.setOpenTime(storeHolidayHour
									.getOpenTime().trim());
							apptStoreHour.setCloseTime(storeHolidayHour
									.getCloseTime().trim());

							String holidayMonthString = monthFormat
									.format(holidayDate);
							String holidayDateString = dateFormat
									.format(holidayDate);
							// replace the existing day with holiday hours in
							// the main map
							appointmentSchedule.getScheduleMap()
									.get(holidayMonthString)
									.put(holidayDateString, apptStoreHour);

							// this covers the case when a store is closed, but
							// there may be a holiday for that day.
							if (!appointmentSchedule.getDateMap().containsKey(
									holidayMonthString + holidayDateString)) {
								appointmentSchedule.getDateMap().put(
										holidayMonthString + holidayDateString,
										holidayDate);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		if (DEBUG_MODE)
			Util.debug("AppointmentSchedule Modified for Store Hours and Holidays: "
					+ appointmentSchedule.printReport());


		int addToOpen = 0;
		int samedayCutoffPriorToClose = 0;
		int addForNextAvailableAppointmentTime = 0;
		int latestAppointmentTime = 0;
		
		// check if today is even open. If it's closed, just shift to next
		// available day and add time to open.

		if (appointmentSchedule.getScheduleMap().containsKey(
				monthFormat.format(storeTime.getTime()))
				&& !appointmentSchedule.getScheduleMap()
						.get(monthFormat.format(storeTime.getTime()))
						.containsKey(dateFormat.format(storeTime.getTime()))) {
			// means today is a closed, whether it's a holiday or store is
			// closed.
			addTimeToNextAvailableDay(appointmentSchedule, addToOpen);
			if (DEBUG_MODE)
				Util.debug("AppointmentSchedule Modified since today is not open.  Done: "
						+ appointmentSchedule.printReport());
			cleanupEmptyMonthsAndYears(appointmentSchedule);
			determineMinAndMaxDates(appointmentSchedule);
			return appointmentSchedule;
		}

		// check if current day is not allowed due to same day rule, remove it.

		if (DEBUG_MODE)
			Util.debug(" day of week: " + storeTime.get(Calendar.DAY_OF_WEEK)); // TODO
		String dayOfWeek = dayOfWeekFormat.format(storeTime.getTime());

		if (DEBUG_MODE)
			Util.debug(" day of week: " + dayOfWeek);
		// boolean isTodayAllowed =
		// appointmentRulesService.isSamedayAppointmentAllowed(storeTime.get(Calendar.DAY_OF_WEEK));
		boolean isTodayAllowed = appointmentRulesService.isSamedayAppointmentAllowed(dayOfWeek);

		String storeTimeMonth = monthFormat.format(storeTime.getTime());
		String storeTimeDate = dateFormat.format(storeTime.getTime());

		if (DEBUG_MODE)
			Util.debug("CAN'T TRUST THE GC HOUR OF DAY, MUST FORMAT DATE---------------"
					+ hourFormat.format(storeTime.getTime()));
		// have to format the hour first in order to get the CORRECT hour of day
		// since the GC is using it's own date tracking..
		int correctHour = Integer.parseInt(hourFormat.format(storeTime
				.getTime()));

		// turning the currentStoreHour into MILITARY TIME
		int currentStoreHour = (correctHour * 100)
				+ storeTime.get(Calendar.MINUTE);
		// get the store hours for the current day.
		// we should still have these hours. we'll remove them when ready
		AppointmentStoreHour storeDayHours = appointmentSchedule
				.getScheduleMap().get(storeTimeMonth).get(storeTimeDate);

		int storeOpen = Integer.parseInt(storeDayHours.getOpenTime()
				.replaceAll(":", ""));
		int storeClose = Integer.parseInt(storeDayHours.getCloseTime()
				.replaceAll(":", ""));

		if (DEBUG_MODE)
			Util.debug("currentStoreHour: " + currentStoreHour + " storeOpen:"
					+ storeOpen);

		// get the earliest day, it could be today or tomorrow, depending if
		// today was allowed or not.
		AppointmentStoreHour hoursToAdjust = getNextAvailableDay(appointmentSchedule);
		storeOpen = Integer.parseInt(hoursToAdjust.getOpenTime().replaceAll(
				":", ""));
		storeClose = Integer.parseInt(hoursToAdjust.getCloseTime().replaceAll(
				":", ""));

		if (currentStoreHour < storeOpen) {
			if (DEBUG_MODE)
				Util.debug("EARLY bird special.");
			// if we are before store open time, we need to add the database
			// value to allow MAI enough time to contact store.
			int firstTimeToSchedule = storeOpen + addToOpen;
			hoursToAdjust
			.setFirstTimeToSchedule(addColonToTime(firstTimeToSchedule));
			if (DEBUG_MODE)
				Util.debug("EARLY storeDayHours " + hoursToAdjust);
		} else if (currentStoreHour >= storeOpen
				&& currentStoreHour < (storeClose - samedayCutoffPriorToClose)) {
			// if we are between store Open time AND before the sameDayCutoff
			// time
			if (currentStoreHour > (storeClose - (samedayCutoffPriorToClose + latestAppointmentTime))) {
				if (DEBUG_MODE)
					Util.debug("Entering the MAGIC HOUR, samedayCutoffPriorToClose + latestAppointmentTime");
				// remove day if we are in the window of
				// currentStoreHour = (storeClose - (hoursBeforeStoreClose +
				// latestEligibleAppointmentTime ))
				appointmentSchedule.getScheduleMap().get(storeTimeMonth)
				.remove(storeTimeDate);
				// NOT setting the firstTimeToSchedule
				// since we still have sufficient time in current day for MAI to
				// contact store

			} else {
				if (DEBUG_MODE)
					Util.debug("BASIC adjustment, where we just add 4 hours to current time.");
				// we still have enough time to do a sameday appointment, we
				// just add 4 hours from current time.
				int firstTimeToSchedule = currentStoreHour
						+ addForNextAvailableAppointmentTime;
				hoursToAdjust
				.setFirstTimeToSchedule(addColonToTime(firstTimeToSchedule));
				if (DEBUG_MODE)
					Util.debug("BASIC storeDayHours " + hoursToAdjust);
			}
		} else if (currentStoreHour >= (storeClose - samedayCutoffPriorToClose)) {
			if (DEBUG_MODE)
				Util.debug("AFTER storeClose - samedayCutoffPriorToClose ");
			// in this case, we need to shift to the following day and add the
			// buffer time.
			// remove the current day since it's no longer eligible.
			appointmentSchedule.getScheduleMap().get(storeTimeMonth)
			.remove(storeTimeDate);

			addTimeToNextAvailableDay(appointmentSchedule, addToOpen);
		}


		if (DEBUG_MODE)
			Util.debug("AppointmentSchedule Modified, adjusted for next available appointment time: "
					+ appointmentSchedule.printReport());

		cleanupEmptyMonthsAndYears(appointmentSchedule);

		// set min/max of the schedule:
		determineMinAndMaxDates(appointmentSchedule);

		return appointmentSchedule;
	}

	private void cleanupEmptyMonthsAndYears(
			AppointmentSchedule appointmentSchedule) {
		// Removing months that have no dates for them
		// Set<String> months = appointmentSchedule.getScheduleMap().keySet();
		List<String> months = new ArrayList<String>(appointmentSchedule
				.getScheduleMap().keySet());
		if (DEBUG_MODE)
			Util.debug(" AppointmentSchedule Number of months before hand:"
					+ appointmentSchedule.getScheduleMap().size());
		for (String month : months) {
			// only if the month has any key/value mappings.
			if (appointmentSchedule.getScheduleMap().get(month).keySet()
					.isEmpty()) {
				appointmentSchedule.getScheduleMap().remove(month);
				for (String year : appointmentSchedule.getYearMap().keySet()) {
					if (appointmentSchedule.getYearMap().get(year)
							.contains(month)) {
						appointmentSchedule.getYearMap().get(year)
								.remove(month);
					}
				}
			}
		}
		if (DEBUG_MODE)
			Util.debug(" AppointmentSchedule Number of months after removing empty months:"
					+ appointmentSchedule.getScheduleMap().size());

		List<String> years = new ArrayList<String>(appointmentSchedule
				.getYearMap().keySet());
		if (DEBUG_MODE)
			Util.debug(" Appointment Schedule: Number of Years: "
					+ appointmentSchedule.getYearMap().size());
		for (String year : years) {
			if (appointmentSchedule.getYearMap().get(year).isEmpty()) {
				appointmentSchedule.getYearMap().remove(year);
			}
		}
		if (DEBUG_MODE)
			Util.debug(" Appointment Schedule: Number of Years: "
					+ appointmentSchedule.getYearMap().size());

		if (DEBUG_MODE)
			Util.debug("AppointmentSchedule Modified for empty months: "
					+ appointmentSchedule.printReport());
	}

	private void determineMinAndMaxDates(AppointmentSchedule appointmentSchedule) {
		try {

			Set<String> yearsList = appointmentSchedule.getYearMap().keySet();
			SortedSet<String> sortedYears = new TreeSet<String>(yearsList);

			// set the min
			// get first year
			String firstYear = sortedYears.first();
			SortedSet<String> sortedMonths = new TreeSet<String>(
					new MonthNameComparator());
			// get first month
			List<String> monthsList = appointmentSchedule.getYearMap().get(
					firstYear);
			sortedMonths.addAll(monthsList);
			String firstMonth = sortedMonths.first();
			// get first date
			Set<String> dates = appointmentSchedule.getScheduleMap()
					.get(firstMonth).keySet();
			SortedSet<String> sortedDates = new TreeSet<String>(
					new DateStringComparator());
			sortedDates.addAll(dates);
			String firstDate = sortedDates.first();

			Date minDate = new Date();
			try {
				minDate = completeDateFormat.parse(firstMonth + "/" + firstDate
						+ "/" + firstYear);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(minDate);
			int monthNumber = (gc.get(Calendar.MONTH) + 1);
			appointmentSchedule.setMinDate(monthNumber + "/" + firstDate + "/"
					+ firstYear);
			if (DEBUG_MODE)
				Util.debug("min: " + firstMonth + " " + monthNumber + " "
						+ firstDate + " " + firstYear);

			// set the max
			// get the last year
			String lastYear = sortedYears.last();
			String lastMonth = "";
			String lastDate = "";
			// if they aren't equal, then we have to do this again.
			if (!lastYear.equalsIgnoreCase(firstYear)) {
				SortedSet<String> sortedMonthsForLast = new TreeSet<String>(
						new MonthNameComparator());
				// get last month
				List<String> monthsLast = appointmentSchedule.getYearMap().get(
						lastYear);
				sortedMonthsForLast.addAll(monthsLast);
				lastMonth = sortedMonthsForLast.last();
				// get last date
				Set<String> datesLast = appointmentSchedule.getScheduleMap()
						.get(lastMonth).keySet();
				SortedSet<String> sortedDatesLast = new TreeSet<String>(
						new DateStringComparator());
				sortedDatesLast.addAll(datesLast);
				lastDate = sortedDatesLast.last();
			} else {
				// we can use lists from getting min, but just do
				// sortedMonths.last
				lastMonth = sortedMonths.last();
				// get last date
				Set<String> datesLast = appointmentSchedule.getScheduleMap()
						.get(lastMonth).keySet();
				SortedSet<String> sortedDatesLast = new TreeSet<String>(
						new DateStringComparator());
				sortedDatesLast.addAll(datesLast);
				lastDate = sortedDatesLast.last();
			}

			Date maxDate = new Date();
			try {
				maxDate = completeDateFormat.parse(lastMonth + "/" + lastDate
						+ "/" + lastYear);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			GregorianCalendar maxGc = new GregorianCalendar();
			maxGc.setTime(maxDate);
			int maxMonthNumber = (maxGc.get(Calendar.MONTH) + 1);
			if (DEBUG_MODE)
				Util.debug("max: " + lastMonth + " " + maxMonthNumber + " "
						+ lastDate + " " + lastYear);
			appointmentSchedule.setMaxDate(maxMonthNumber + "/" + lastDate
					+ "/" + lastYear);

		} catch (Exception e) {
			// Do nothing and prevent going to logs.
		}

	}

	private AppointmentStoreHour getNextAvailableDay(
			AppointmentSchedule appointmentSchedule) {
		AppointmentStoreHour ash = null;
		/*
		 * Attempting to get the next available open day. We can't depend on
		 * simply increasing the date by 1 since the next day might be closed or
		 * a holiday.
		 */
		// get month keySet, order that.
		Set<String> yearsList = appointmentSchedule.getYearMap().keySet();
		SortedSet<String> sortedYears = new TreeSet<String>(yearsList);

		yearFor: for (String year : sortedYears) {
			SortedSet<String> sortedMonths = new TreeSet<String>(
					new MonthNameComparator());
			List<String> monthsList = appointmentSchedule.getYearMap()
					.get(year);
			sortedMonths.addAll(monthsList);
			// go through set and find it's days.
			monthFor: for (String sortedMonth : sortedMonths) {
				// Util.debug(" Looking for this month:" + sortedMonth);
				if (appointmentSchedule.getScheduleMap().get(sortedMonth)
						.isEmpty()) {
					// means we didn't have any more days for that month. we'll
					// continue
					// on and grab the next month.
					// Util.debug("no dates left for the month.");
					continue monthFor;
				} else {
					// Util.debug(" The month had dates left... ");
					// get date key set for first month, order that.
					Set<String> dates = appointmentSchedule.getScheduleMap()
							.get(sortedMonth).keySet();
					// the natural ordering should be sufficient
					SortedSet<String> sortedDates = new TreeSet<String>(
							new DateStringComparator());
					sortedDates.addAll(dates);
					// get first day in that ordered date keyset.
					ash = appointmentSchedule.getScheduleMap().get(sortedMonth)
							.get(sortedDates.first());
					// we're done, lets get out of here.
					break yearFor;
					// int nextEligibleDayOpen =
					// Integer.parseInt(ash.getOpenTime().replaceAll(":", ""));
					// ash.setFirstTimeToSchedule(addColonToTime(nextEligibleDayOpen
					// + addToOpen));
					// Util.debug("These hours were set: " + ash);

				}
			}
		}

		return ash;
	}

	private void addTimeToNextAvailableDay(
			AppointmentSchedule appointmentSchedule, int addToOpen) {
		/*
		 * Attempting to get the next available open day. We can't depend on
		 * simply increasing the date by 1 since the next day might be closed or
		 * a holiday.
		 */
		// get month keySet, order that.
		Set<String> yearsList = appointmentSchedule.getYearMap().keySet();
		SortedSet<String> sortedYears = new TreeSet<String>(yearsList);

		yearFor: for (String year : sortedYears) {
			SortedSet<String> sortedMonths = new TreeSet<String>(
					new MonthNameComparator());
			List<String> monthsList = appointmentSchedule.getYearMap()
					.get(year);
			sortedMonths.addAll(monthsList);
			// go through set and find it's days.
			monthFor: for (String sortedMonth : sortedMonths) {
				// Util.debug(" Looking for this month:" + sortedMonth);
				if (appointmentSchedule.getScheduleMap().get(sortedMonth)
						.isEmpty()) {
					// means we didn't have any more days for that month. we'll
					// continue
					// on and grab the next month.
					// Util.debug("no dates left for the month.");
					continue monthFor;
				} else {
					// Util.debug(" The month had dates left... ");
					// get date key set for first month, order that.
					Set<String> dates = appointmentSchedule.getScheduleMap()
							.get(sortedMonth).keySet();
					// the natural ordering should be sufficient
					SortedSet<String> sortedDates = new TreeSet<String>(
							new DateStringComparator());
					sortedDates.addAll(dates);
					// get first day in that ordered date keyset, and adjust
					// time.
					AppointmentStoreHour ash = appointmentSchedule
							.getScheduleMap().get(sortedMonth)
							.get(sortedDates.first());
					int nextEligibleDayOpen = Integer.parseInt(ash
							.getOpenTime().replaceAll(":", ""));
					ash.setFirstTimeToSchedule(addColonToTime(nextEligibleDayOpen
							+ addToOpen));
					// Util.debug("These hours were set: " + ash);
					// we're done, lets get out of here.
					break yearFor;
				}
			}
		}
	}

	private String addColonToTime(int time) {
		String returnTime = Integer.toString(time);
		int minuteStartIndex = returnTime.length() - 2;
		returnTime = returnTime.substring(0, minuteStartIndex) + ":"
				+ returnTime.substring(minuteStartIndex, returnTime.length());
		return returnTime;
	}

	@Override
	public BSROWebServiceResponse getAvailableTimesForService(Long locationId,
			Long employeeId, String serviceIds, String selectedDate) {
		logger.info("Inside getAvailableDays loc = "+ locationId + " serviceIds = "+ serviceIds
				+ " empId = "+ employeeId + " selectedDate = "+selectedDate);
		BSROWebServiceResponse response = null;
		if(!ServerUtil.isNullOrEmpty(locationId) && !ServerUtil.isNullOrEmpty(employeeId) 
				&& !ServerUtil.isNullOrEmpty(selectedDate)){
			Map<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("locationId", String.valueOf(locationId));
			parameters.put("employeeId", String.valueOf(employeeId));
			parameters.put("selectedDate", selectedDate);
			parameters.put("serviceIds", serviceIds);
			StringBuilder webservicePath = null;
			try {
				webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_GET_TIME_AVAILABILITY);
				response = (BSROWebServiceResponse) getWebservice(webservicePath.toString(), parameters, BSROWebServiceResponse.class);
			} catch (IOException e) {
				logger.error("Error calling webservice method at path "+ PATH_WEBSERVICE_GET_TIME_AVAILABILITY 
						+ "in method getAvailableTimesForService");
				e.printStackTrace();
			}
		}

		return response;
	}

	@Override
	public BSROWebServiceResponse getAvailableDays(Long locationId,
			Long employeeId, String startDate, Integer numDays) {
		logger.info("Inside getAvailableDays loc = "+ locationId 
				+ " empId = "+ employeeId + " startDate = "+ startDate +" numDays = "+numDays);
		BSROWebServiceResponse response = null;
		if(!ServerUtil.isNullOrEmpty(locationId) && !ServerUtil.isNullOrEmpty(employeeId) 
				&& !ServerUtil.isNullOrEmpty(numDays)){
			Map<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("locationId", String.valueOf(locationId));
			parameters.put("employeeId", String.valueOf(employeeId));
			parameters.put("startDate", startDate);
			parameters.put("numDays", String.valueOf(numDays));
			StringBuilder webservicePath = null;
			try {
				webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_GET_DAY_AVAILABILITY);
				response = (BSROWebServiceResponse) getWebservice(webservicePath.toString(), parameters, BSROWebServiceResponse.class);
			} catch (IOException e) {
				logger.error("Error calling webservice method at path "+ webservicePath + "in method getAvailableDays");
				e.printStackTrace();
			}
		}
		return response;
	}

}
