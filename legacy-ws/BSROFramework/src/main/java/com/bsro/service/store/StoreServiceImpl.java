package com.bsro.service.store;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import map.States;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bfrc.Config;
import com.bfrc.framework.dao.StoreAdminDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.store.ListStoresOperator;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreHoliday;
import com.bfrc.pojo.store.StoreHolidayHour;
import com.bfrc.pojo.store.StoreHolidayId;
import com.bfrc.pojo.store.StoreMilitary;
import com.bfrc.pojo.store.StoreSearch;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bsro.databean.store.StoreDataBean;
import com.bsro.databean.store.StoreWidgetDataBean;
import com.hibernate.dao.base.BaseDao;

@Service("storeSearchService")
public class StoreServiceImpl implements StoreService {

	@Autowired
	private StoreDAO storeDAO;

	@Autowired
	private LocatorOperator locator;

	@Autowired
	private Config config;

	@Autowired
	private ListStoresOperator listStores;

	@Autowired
	@Qualifier("baseStoreMilitaryDAO")
	BaseDao baseStoreMilitaryDAO;

	@Autowired
	private States statesMap;
	
	@Autowired
	StoreAdminDAO storeAdminDAO;
	
	private Map mappedDistance;

	private final int DAYS_PRIOR_TO_DISPLAY_HOLIDAY = 30;
	private final int MAX_HOLIDAYS_TO_DISPLAY = 2;

	public LocatorOperator getLocator() {
		return locator;
	}

	public void setLocator(LocatorOperator locator) {
		this.locator = locator;
	}

	public StoreDAO getStoreDAO() {
		return storeDAO;
	}

	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
	}

	@Override
	public Store getStoreById(Long storeNumber) {
		if (storeNumber == null) {
			return null;
		}
		Store store = storeDAO.findStoreById(Long.valueOf(storeNumber));
		if (store != null) {
			Util.debug(" found a store: " + storeNumber);
			store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
		}
		return store;
	}

	@Override
	/**
	 * Determine if storeNumber is in the STORE_MILITARY table.
	 * @param storeNumber store number to search for 
	 * @return true if that store number exists on the table
	 */
	public boolean isMilitaryStore(Long storeNumber) {
		boolean isMilitaryStore = false;

		if (storeNumber == null) {
			return isMilitaryStore;
		}

		StoreMilitary militaryStore = storeDAO.findMilitaryStore(storeNumber);
		if (militaryStore == null) {
			isMilitaryStore = false;
		} else if (militaryStore.getStoreNumber() == storeNumber.longValue())
			isMilitaryStore = true;

		return isMilitaryStore;
	}

	public void setMilitaryStores(List<Store> stores) {

		List<Long> storeNumbers = new ArrayList<Long>();

		for (Store s : stores) {
			Long storeNumber = s.getStoreNumber();
			storeNumbers.add(storeNumber);
		}

		List<Long> militaryStoreNumbers = storeDAO.findMilitaryStores(storeNumbers);

		for (Store s : stores) {
			if (militaryStoreNumbers.contains(s.getStoreNumber())) {
				s.setMilitaryStore(true);
			} else {
				s.setMilitaryStore(false);
			}
		}
	}

	public String getPipeDelimitedMilitaryStoreNumbers() {
		List mstores = getMilitaryStores();

		StringBuffer sb = new StringBuffer();

		if (mstores != null && mstores.size() > 0) {
			StoreMilitary mstore = (StoreMilitary) mstores.get(0);
			sb.append("|" + mstore.getStoreNumber() + "|");
			for (int i = 1; i < mstores.size(); i++) {
				mstore = (StoreMilitary) mstores.get(i);
				sb.append(mstore.getStoreNumber() + "|");
			}
		}

		return sb.toString();
	}

	public List getMilitaryStores() {
		return baseStoreMilitaryDAO.find("from StoreMilitary", (Object[]) null);
	}

	public StoreWidgetDataBean initializeStoreWidget(String context, Long selectedStoreNumber, String siteName, String address, String city, String state, String zip, Integer radius,
			String remoteIP, String geoPoint, Boolean ignoreRadius, Boolean storesWithTirePricingOnly, Boolean includeLicensees, String licenseeType, Boolean partner, Boolean fiveStarPrimary,
			int count) {

		boolean hasLocationInformation = StringUtils.isNotBlank(address) || (StringUtils.isNotBlank(city) && StringUtils.isNotBlank(state)) || StringUtils.isNotBlank(zip)
				|| StringUtils.isNotBlank(geoPoint);

		StoreDataBean storeDataBean = new StoreDataBean();

		if (hasLocationInformation) {
			// Getting active and inactive store's for change store option in oil funnel.
			if("oil_changeStore".equals(context)){
				storeDataBean = searchStoresByAddress(siteName, address, city, state, zip, radius, remoteIP, geoPoint, ignoreRadius, storesWithTirePricingOnly, includeLicensees, licenseeType, partner,
						fiveStarPrimary, count, false, true);
			}
			else
			{
				storeDataBean = searchStoresByAddress(siteName, address, city, state, zip, radius, remoteIP, geoPoint, ignoreRadius, storesWithTirePricingOnly, includeLicensees, licenseeType, partner,
						fiveStarPrimary, count, true);
			}

		} else {
			if (selectedStoreNumber != null) {
				Store selectedStore = findStoreLightById(selectedStoreNumber);
				// Util.debug(" stores found: " + selectedStore);
				if (selectedStore != null) {
//					selectedStore.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(selectedStore.getStoreNumber()), true));
//					selectedStore.setMilitaryStore(isMilitaryStore(selectedStore.getStoreNumber()));
					List<Store> stores = new ArrayList<Store>();
					stores.add(selectedStore);
					storeDataBean.setStoreList(stores);
				}
			}
		}

		StoreWidgetDataBean storeWidgetDataBean = new StoreWidgetDataBean();
		storeWidgetDataBean.setCity(city);
		storeWidgetDataBean.setZip(zip);
		storeWidgetDataBean.setGeoPoint(geoPoint);
		storeWidgetDataBean.setContext(context);
		storeWidgetDataBean.setStoreDataBean(storeDataBean);
		storeWidgetDataBean.selectStore(selectedStoreNumber);

		return storeWidgetDataBean;
	}
	


	@Override
	public StoreDataBean searchStoresByState(String app, String state, String remoteIP, Boolean storesWithTirePricingOnly, Boolean includeLicensees, Boolean partnerPricing, Boolean fiveStarPrimary, Boolean lightStores, String p) {
		StoreDataBean bean = new StoreDataBean();
		
		if (partnerPricing) {
			app = "partner";
			if (storesWithTirePricingOnly)
				app += "-pricing";
		} else if (fiveStarPrimary) {
			app = "5starPrimary";
			if (storesWithTirePricingOnly)
				app += "-pricing";
		}

		long requestId = locator.getRequestId();
		Float[] location = locator.geoLocationWithBing(requestId, app, null, null, state, null, remoteIP);
		bean.setLatitude(location[1]);
		bean.setLongitude(location[0]);
		
		long[][] storeArray = locator.getClosestStores(null, app, includeLicensees, 5, storesWithTirePricingOnly, state, false, null, 10, true, p);
		
		Store[] stores = null;
		// Util.debug("checking storeArray");
		if (storeArray != null && storeArray.length > 0) {
			bean.setStoresCount(storeDAO.getStoreCount());

			// Util.debug("checking storeArray not empty");
			if (lightStores) {

				// Util.debug("light stores is true");
				List<Long> storeIds = new ArrayList<Long>();
				for (int i = 0; i < storeArray.length; i++)
					storeIds.add(storeArray[i][0]);
				List<Store> storeList = storeDAO.findStoresLightByIds(storeIds);
				stores = storeList.toArray(new Store[storeList.size()]);

				// Util.debug("stores : "+stores);
			} else {
				// Util.debug("light stores is false");
				stores = locator.getLocatorDAO().getStores(storeArray);
			}
		}

		List<Store> lstores = new ArrayList<Store>();
		if (stores != null && stores.length > 0) {
			for (int i = 0; i < stores.length; i++) {
				Store store = stores[i];
				lstores.add(store);
				store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
				store.setHolidayHourMessages(getStoreHolidayHours(store));
			}				

			setMilitaryStores(lstores);
			bean.setStoreList(lstores);
		}
		
		return bean;
	}
	
	public StoreDataBean searchStoresByAddress(String siteName, String address, String city, String state, String zip, Integer radius, String remoteIP, String geoPoint, Boolean ignoreRadius,
			Boolean storesWithTirePricingOnly, Boolean includeLicensees, String licenseeType, Boolean partnerPricing, Boolean fiveStarPrimary, int count) {
		return searchStoresByAddress(siteName, address, city, state, zip, radius, remoteIP, geoPoint, ignoreRadius, storesWithTirePricingOnly, includeLicensees, licenseeType, partnerPricing,
				fiveStarPrimary, count, false);
	}

	/**
	 * ignoreRadius defaults to false includeLicensees defaults to true
	 * ignoreActiveFlag to true enable both active and inactive store's
	 * for change store option in oil funnel.
	 * 
	 * @param siteName
	 * @param address
	 * @param city
	 * @param state
	 * @param zip
	 * @param radius
	 * @param defaultRadius
	 * @param remoteIP
	 * @param geoPoint
	 * @param ignofreRadius
	 * @param storesWithTirePricingOnly
	 * @param includeLicensees
	 * @param licenseeType
	 * @param partnerPricing
	 * @param fiveStarPrimary
	 * @param count
	 * @param lightStores
	 * @param ignoreActiveFlag
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StoreDataBean searchStoresByAddress(String siteName, String address, String city, String state, String zip, Integer radius, String remoteIP, String geoPoint, Boolean ignoreRadius,
			Boolean storesWithTirePricingOnly, Boolean includeLicensees, String licenseeType, Boolean partnerPricing, Boolean fiveStarPrimary, int count, boolean lightStores, boolean ignoreActiveFlag) {
		StoreDataBean bean = new StoreDataBean();

		String app = siteName;
		if (partnerPricing) {
			app = "partner";
			if (storesWithTirePricingOnly)
				app += "-pricing";
		} else if (fiveStarPrimary) {
			app = "5starPrimary";
			if (storesWithTirePricingOnly)
				app += "-pricing";
		}

		Float[] location;
		long requestId = locator.getRequestId();
		if (ServerUtil.isNullOrEmpty(geoPoint)) {
			location = locator.geoLocationWithBing(requestId, app, address, city, state, zip, remoteIP);
		} else {
			location = new Float[2];
			String[] pointString = geoPoint.split(",");
			location[1] = new Float(pointString[0]);
			location[0] = new Float(pointString[1]);
		}
		if (location == null)
			return bean;
		bean.setLatitude(location[1]);
		bean.setLongitude(location[0]);
		long[][] storeArray = null;
		Integer r = config.getLocator().getRadius();
		if (!ServerUtil.isNullOrEmpty(radius)) {
			try {
				r = Integer.valueOf(radius.toString());
			} catch (Exception ex) {
				r = config.getLocator().getRadius();
			}
		}
		try {			
			storeArray = locator.getClosestStores (location, app, includeLicensees, r.intValue(), storesWithTirePricingOnly, null, ignoreRadius, licenseeType, count, ignoreActiveFlag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Store[] stores = null;
		// Util.debug("checking storeArray");
		if (storeArray != null && storeArray.length > 0) {

			// Util.debug("checking storeArray not empty");
			if (lightStores) {

				// Util.debug("light stores is true");
				List<Long> storeIds = new ArrayList<Long>();
				for (int i = 0; i < storeArray.length; i++)
					storeIds.add(storeArray[i][0]);
				List<Store> storeList = findStoresLightByIds(storeIds);
				stores = storeList.toArray(new Store[storeList.size()]);

				// Util.debug("stores : "+stores);
			} else {
				// Util.debug("light stores is false");
				stores = locator.getLocatorDAO().getStores(storeArray);
			}
		}
		HashMap<Long, Long> distances = new HashMap<Long, Long>();
		List<Store> lstores = new ArrayList<Store>();
		if (stores != null && stores.length > 0) {
			for (int i = 0; i < stores.length; i++) {
				Store store = stores[i];
				lstores.add(store);
				store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
				distances.put(store.getStoreNumber(), storeArray[i][1]);
			}				

			setMilitaryStores(lstores);
			setHolidayHours(lstores);
			bean.setStoreList(lstores);
			bean.setMappedDistance(distances);
		}
		return bean;
	}
	
	/**
	 * ignoreRadius defaults to false includeLicensees defaults to true
	 * 
	 * @param siteName
	 * @param address
	 * @param city
	 * @param state
	 * @param zip
	 * @param radius
	 * @param defaultRadius
	 * @param remoteIP
	 * @param geoPoint
	 * @param ignoreRadius
	 * @param storesWithTirePricingOnly
	 * @param includeLicensees
	 * @param licenseeType
	 * @param partnerPricing
	 * @param fiveStarPrimary
	 * @param count
	 * @param lightStores
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StoreDataBean searchStoresByAddress(String siteName, String address, String city, String state, String zip, Integer radius, String remoteIP, String geoPoint, Boolean ignoreRadius,
			Boolean storesWithTirePricingOnly, Boolean includeLicensees, String licenseeType, Boolean partnerPricing, Boolean fiveStarPrimary, int count, boolean lightStores) {
		StoreDataBean bean = new StoreDataBean();

		String app = siteName;
		if (partnerPricing) {
			app = "partner";
			if (storesWithTirePricingOnly)
				app += "-pricing";
		} else if (fiveStarPrimary) {
			app = "5starPrimary";
			if (storesWithTirePricingOnly)
				app += "-pricing";
		}

		Float[] location;
		long requestId = locator.getRequestId();
		if (ServerUtil.isNullOrEmpty(geoPoint)) {
			location = locator.geoLocationWithBing(requestId, app, address, city, state, zip, remoteIP);
		} else {
			location = new Float[2];
			String[] pointString = geoPoint.split(",");
			location[1] = new Float(pointString[0]);
			location[0] = new Float(pointString[1]);
		}
		if (location == null)
			return bean;
		bean.setLatitude(location[1]);
		bean.setLongitude(location[0]);
		long[][] storeArray = null;
		Integer r = config.getLocator().getRadius();
		if (!ServerUtil.isNullOrEmpty(radius)) {
			try {
				r = Integer.valueOf(radius.toString());
			} catch (Exception ex) {
				r = config.getLocator().getRadius();
			}
		}
		try {
			boolean ignoreActiveFlag = Config.FCAC.equals(siteName);
			storeArray = locator.getClosestStores (location, app, includeLicensees, r.intValue(), storesWithTirePricingOnly, null, ignoreRadius, licenseeType, count, ignoreActiveFlag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Store[] stores = null;
		// Util.debug("checking storeArray");
		if (storeArray != null && storeArray.length > 0) {

			// Util.debug("checking storeArray not empty");
			if (lightStores) {

				// Util.debug("light stores is true");
				List<Long> storeIds = new ArrayList<Long>();
				for (int i = 0; i < storeArray.length; i++)
					storeIds.add(storeArray[i][0]);
				List<Store> storeList = findStoresLightByIds(storeIds);
				stores = storeList.toArray(new Store[storeList.size()]);

				// Util.debug("stores : "+stores);
			} else {
				// Util.debug("light stores is false");
				stores = locator.getLocatorDAO().getStores(storeArray);
			}
		}
		HashMap<Long, Long> distances = new HashMap<Long, Long>();
		List<Store> lstores = new ArrayList<Store>();
		if (stores != null && stores.length > 0) {
			for (int i = 0; i < stores.length; i++) {
				Store store = stores[i];
				lstores.add(store);
				store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
				distances.put(store.getStoreNumber(), storeArray[i][1]);
			}				

			setMilitaryStores(lstores);
			setHolidayHours(lstores);
			bean.setStoreList(lstores);
			bean.setMappedDistance(distances);
		}
		return bean;
	}

	/**
	 * Accepts a comma-delimited address string and attempts to break it up into
	 * logical address, city, state, and zip fields. It analyzes them for
	 * validity with respect to our list of states and list of stores.
	 * 
	 * Returns a Map containing "address," city," "state," and "zip" key/value
	 * pairs for any found.
	 * 
	 * @param addressString
	 * @param siteName
	 * @return
	 */
	public Map<String, String> parseAddressString(String addressString, String siteName) {
		Map<String, String> addressComponents = new HashMap<String, String>();

		map.States stateList = locator.getGeocodeOperator().getStates();

		String city = null;
		String state = null;
		String address = null;
		String zip = null;
		try {
			if (addressString != null) {
				boolean stateFound = false;
				boolean cityFound = false;

				// is the address a zip or city, state
				String[] locArray = addressString.split("[, ]+");
				// ok, let's work our way back through it - first, look for zip
				// at the end
				int len = locArray.length;
				int last = len - 1;
				// last position a zip?
				if ((locArray[last].matches("[0-9]{5}") && locArray[last].length() == 5) || (locArray[last].matches("[0-9]{5}-[0-9]{4}") && locArray[last].length() == 10)) {
					zip = locArray[last];
					last--;
				}

				if (last >= 0) {
					if (locArray[last].matches("[a-zA-Z]{2}") && locArray[last].length() == 2) {
						// could be a state
						state = locArray[last].toUpperCase();
						if (state.trim().length() == 2 && stateList.containsKey(state.trim().toUpperCase())) {
							stateFound = true;
							last--;
						}
					}
					if (last >= 0) {
						HashMap map = new HashMap();
						if (stateFound) {
							// check for city
							city = locArray[last];
							map.put("appointment", null);
							if (Config.PPS.equals(siteName))
								map.put("partner", "");
							map.put("state", state);
							String result = listStores.operate(map);
							List l = (List) map.get("result");
							HashMap<String, String> cityMap = new HashMap();
							for (int i = 0; i < l.size(); i++) {
								String value = (String) l.get(i);
								cityMap.put(value.toLowerCase(), value.toLowerCase());
							}
							if (cityMap.containsKey((Object) city.toLowerCase())) {
								cityFound = true;
								last--;
							} else if (last > 0) { // try two word city
								city = locArray[last - 1] + " " + locArray[last];
								if (cityMap.containsKey((Object) city.toLowerCase())) {
									cityFound = true;
									last -= 2;
								} else if (last > 1) { // try three word city
									city = locArray[last - 2] + " " + locArray[last - 1] + " " + locArray[last];
									if (cityMap.containsKey((Object) city.toLowerCase())) {
										cityFound = true;
										last -= 3;
									}
								}
							}

						}
					}
					if (!cityFound)
						city = "";
				}
				if (last >= 0) {
					StringBuffer temp = new StringBuffer();
					for (int i = 0; i <= last; i++) {
						temp.append(locArray[i]);
						if (i < last)
							temp.append(" ");
					}
					address = temp.toString();
				}
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Exception occurred when parsing the navAddress string. Values follow:");
			sb.append("\n\t navAddress: ");
			sb.append(addressString == null ? "null" : addressString);
			sb.append("\n\t address: ");
			sb.append(address == null ? "null" : address);
			sb.append("\n\t state: ");
			sb.append(state == null ? "null" : state);
			sb.append("\n\t zip: ");
			sb.append(zip == null ? "null" : zip);
			sb.append("\nEXCEPTION:");
			StringWriter stackTrace = new StringWriter();
			e.printStackTrace(new PrintWriter(stackTrace));
			sb.append(stackTrace.toString());
			sb.append("============\n");
			System.err.println(sb.toString());
		}

		addressComponents.put("address", address);
		addressComponents.put("city", city);
		addressComponents.put("state", state);
		addressComponents.put("zip", zip);

		return addressComponents;
	}

	public List<Store> searchStoresByStoreNumbers(String storeNumbers) {
		List<Store> stores = (List<Store>) storeDAO.findStoresByStoreNumbers(storeNumbers);
		if (stores != null) {
			for (Store store : stores) {
				store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
			}
		}

		return stores;
	}

	public List getStoreHours(Long storeNumber) {
		return storeDAO.getStoreHours(storeNumber);
	}

	public List<StoreHoliday> getStoreHolidaysBetweenDates(Date start, Date end) {

		return storeDAO.getStoreHolidaysBetweenDates(start, end);

	}

	public StoreHolidayHour getStoreHolidayHour(Long storeNumber, Long holidayId) {
		return storeDAO.getStoreHolidayHour(storeNumber, holidayId);
	}

	public List<StoreHoliday> getStoreLocatorHolidays() {
		// get holidays for next 30 days.
		Date startDate = new Date();
		Date endDate = org.apache.commons.lang.time.DateUtils.addDays(startDate, DAYS_PRIOR_TO_DISPLAY_HOLIDAY);
		// need to ensure these are in order by date
		List<StoreHoliday> holidays = storeDAO.getOrderedStoreHolidaysBetweenDates(startDate, endDate);

		// only show 2 per reqs.
		List<StoreHoliday> finalHolidays = new ArrayList<StoreHoliday>();
		try {
			if (holidays != null) {
				for (int i = 0; (i < MAX_HOLIDAYS_TO_DISPLAY && i < holidays.size()); i++) {
					finalHolidays.add(holidays.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return finalHolidays;
	}

	public String getStoreHolidayMessage(StoreHoliday holiday, StoreHolidayHour hours) {
		StringBuffer message = new StringBuffer();
		// means we have no hours, the store is closed.
		if (hours == null) {
			message.append(holiday.getDescription());
			message.append(": CLOSED ");			
//			message.append(getMonthName(holiday.getId()));
//			message.append(". ");
//			message.append(holiday.getId().getDay());
//			message.append(", ");
//			message.append(holiday.getId().getYear());
		} else {
			// store is open.
			message.append(holiday.getDescription());
//			message.append(getMonthName(holiday.getId()));
//			message.append(". ");
//			message.append(holiday.getId().getDay());
//			message.append(", ");
//			message.append(holiday.getId().getYear());
			// message.append(" Hours: ");
			message.append(": ");
			message.append(convertToStandardTime(hours.getOpenTime()));
			message.append("-");
			message.append(convertToStandardTime(hours.getCloseTime()));

		}
		return message.toString();
	}

	private String convertToStandardTime(String time) {
		// get colon index
		int indexOfColon = time.indexOf(":");
		// get Hours
		int beginTimeHour = Integer.parseInt(time.substring(0, indexOfColon).trim());
		// get Minutes
		int beginTimeMinute = Integer.parseInt(time.substring(indexOfColon + 1).trim());

		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.HOUR_OF_DAY, beginTimeHour);
		startCalendar.set(Calendar.MINUTE, beginTimeMinute);

		String formattedTime = new SimpleDateFormat("h:mma").format(startCalendar.getTime()).toLowerCase();

		return formattedTime;

	}

	private String getMonthName(StoreHolidayId holidayId) {
		LocalDate date = new LocalDate(holidayId.getYear(), holidayId.getMonth(), holidayId.getDay());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String monthName = formatter.print(date);

		return monthName;

	}

	public List<String> getHolidayHoursMessages(Store store, List<StoreHoliday> holidayList) {

		List<String> holidayHours = new ArrayList<String>();
		if (holidayList != null && holidayList.size() > 0) {
			for (StoreHoliday holiday : holidayList) {
				StoreHolidayHour holidayHour = getStoreHolidayHour(store.getStoreNumber(), holiday.getHolidayId());
				holidayHours.add(getStoreHolidayMessage(holiday, holidayHour));
			}
		}

		return holidayHours;
	}

	@Override
	public String checkStoreReasonCode(long storeNumber) {
		return storeDAO.checkStoreReasonCode(Long.valueOf(storeNumber));
	}

	@Override
	public Store findStoreLightById(Long storeNumber) {
		Store selectedStore = storeDAO.findStoreLightById(storeNumber);
		if (selectedStore != null) {
			selectedStore.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(selectedStore.getStoreNumber()), true));
			selectedStore.setHolidayHourMessages(getStoreHolidayHours(selectedStore));
			selectedStore.setMilitaryStore(isMilitaryStore(storeNumber));
		}
		return selectedStore;
	}

	@Override
	public List<Store> findStoresLightByIds(List<Long> storeNumbers) {
		List<Store> stores = storeDAO.findStoresLightByIds(storeNumbers);
		for (Store store : stores) {
			store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getStoreNumber()), true));
			store.setHolidayHourMessages(getStoreHolidayHours(store));
		}
		setMilitaryStores(stores);
		return stores;
	}

	@Override
	public Store[] getStoresLight(long[][] storeNumber) {
		int len = storeNumber.length;
		Store[] out = new Store[len];
		for(int i=0; i<len; i++) {
			Store s = findStoreLightById(storeNumber[i][0]);
			out[i] = new Store(s);
		}
		return out;
	}
	
	@Override
	public StoreSearch createStoreSearchObject(String navZip) {
		String zip = "";
		String city = "";
		String state = "";
		if (navZip != null && navZip.trim().length() > 0 && Util.isValidZipCode(navZip.trim())) {
			Util.debug("found navZip, setting zip");
			zip = navZip.trim();
		} else if (navZip != null && navZip.trim().length() > 0) {
			String myState = navZip.trim().toUpperCase();

			if (myState.length() == 2 && statesMap.containsKey(myState)) {
				Util.debug("found navZip, setting state");
				state = myState;
			} else if (myState.length() != 2) {
				if (myState.indexOf(",") >= 0) {
					city = myState.substring(0, myState.indexOf(","));
					myState = myState.substring(myState.indexOf(",") + 1).trim();
					Util.debug("found navZip, setting city");
				}
				String myStateArray[] = myState.split("\\s+");
				myState = myStateArray[myStateArray.length-1];
				if (myState.length() == 2 && statesMap.containsKey(myState)) {
					Util.debug("found navZip, setting state");
					state = myState;
					if (myStateArray.length > 1)
					{
						for (int idx = 0; idx < myStateArray.length-1; idx++)
						{
							city += myStateArray[idx].trim()+" ";
						}
						city = city.trim();
					}
				} else {
					String tok = "";
					myState = "";
					for (int idx = myStateArray.length-1; idx >= 0; idx--)
					{
						tok = myStateArray[idx];
						myState = tok + " " + myState;
						for (String stateAbbr : statesMap.keySet()) {
							if (myState.trim().equalsIgnoreCase(statesMap.get(stateAbbr))) {
								Util.debug("found navZip, setting state abbr");
								state = stateAbbr;
								navZip = navZip.trim().toUpperCase();
								city = navZip.substring(0, navZip.indexOf(myState.trim())).trim().toUpperCase();
								break;
							}
						}
						if (state.length() > 0)
						{
							break;
						}
					}
				}
			}
		}
		
		StoreSearch storeSearch = new StoreSearch();
		storeSearch.setSearchEntered(navZip);
		storeSearch.setCity(city);
		storeSearch.setState(state);
		storeSearch.setZip(zip);

		return storeSearch;
	}

	public List<Store> getStoresEligibleForAppointments(StoreSearch storeSearch, String ipAddress) {

		// we only return at most 10, but need 20 so we should always have 10 to
		// display.
		StoreDataBean stores = new StoreDataBean();
		if ( ServerUtil.isNullOrEmpty(storeSearch.getZip()) && ServerUtil.isNullOrEmpty(storeSearch.getCity()) && !ServerUtil.isNullOrEmpty(storeSearch.getState())  ) {
			stores = searchStoresByState(config.getSiteName(), storeSearch.getState(), ipAddress, false, true, false, true, true, "20");
		} else {
			stores = searchStoresByAddress(config.getSiteName(), null, storeSearch.getCity(), storeSearch.getState(), storeSearch.getZip(), null, ipAddress, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, null, Boolean.FALSE, Boolean.FALSE, 20);
		}
		
		setMappedDistance(stores.getMappedDistance());

		List<Store> storeList = stores.getStoreList();

		List<Store> filteredList = new ArrayList<Store>();
		// filter out by appointment flag
		int numberAdded = 0;
		if (storeList != null) {
			for (Store s : storeList) {
				
				if (numberAdded < 10 && s.getOnlineAppointmentActiveFlag().intValue() == 1) {
					filteredList.add(s);
					numberAdded++;
				} else {
					continue;
				}
			}
		}
		//
		// //get only 10 stores if we found more than 10.
		// if(filteredList.size() > 10){
		// filteredList = filteredList.subList(0, 9);
		// }

		setMilitaryStores(filteredList);

		return filteredList;
	}
	
	public void setMappedDistance(Map mappedDistance) {
		this.mappedDistance = mappedDistance;
	}
	
	public Map getMappedDistance() {
		return this.mappedDistance;
	}

	public Map<Long, List<String>> getStoreHolidayHours(List<Store> stores) {

		Map<Long, List<String>> storeHolidayHourMap = new HashMap<Long, List<String>>();

		List<StoreHoliday> holidayList = getStoreLocatorHolidays();
		if (stores != null) {
			for (Store s : stores) {
				List<String> holidayHours = getHolidayHoursMessages(s, holidayList);
				storeHolidayHourMap.put(s.getStoreNumber(), holidayHours);
			}
		}

		return storeHolidayHourMap;

	}

	public List<String> getStoreHolidayHours(Store store) {

		List<Store> stores = new ArrayList<Store>();
		stores.add(store);

		Map<Long, List<String>> storeHolidayHourMap = getStoreHolidayHours(stores);

		List<String> holidayHourMessages = storeHolidayHourMap.get(store.getStoreNumber());

		return holidayHourMessages;

	}
	
	public void setHolidayHours(List<Store> stores) {
		List<StoreHoliday> holidayList = getStoreLocatorHolidays();
		if(stores != null){
			for(Store s : stores){
				List<String> holidayHours = getHolidayHoursMessages(s, holidayList);
				s.setHolidayHourMessages(holidayHours);
			}
		}
		
	}

	@Override
	public List<Store> findStoresLightByStoreNumbers(String pipeDelimitedStoreNumbers) {
		if(pipeDelimitedStoreNumbers == null)
			return null;
		try{
			StringTokenizer stringTokenizer = new StringTokenizer(pipeDelimitedStoreNumbers, "|");
			List<Long> storeNumbers = new ArrayList<Long>();
			while(stringTokenizer.hasMoreTokens()){
				storeNumbers.add(Long.valueOf(stringTokenizer.nextToken()));
				if(storeNumbers.size() >999)
					break;
			}
			return findStoresLightByIds(storeNumbers);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
}
