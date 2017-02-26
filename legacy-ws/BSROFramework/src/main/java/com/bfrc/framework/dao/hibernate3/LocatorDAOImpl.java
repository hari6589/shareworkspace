package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.bfrc.Config;
import com.bfrc.framework.dao.LocatorDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.StoreSearchUtils;
import com.bfrc.pojo.geo.LocatorLog;
import com.bfrc.pojo.geo.LocatorLogId;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreHour;
import com.bfrc.pojo.store.StoreHourId;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bsro.service.store.StoreService;
/**
 * @author edc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LocatorDAOImpl extends HibernateDAOImpl implements LocatorDAO {
	
	private StoreDAO storeDAO;
	private StoreService storeService;

	public List getClosestStores(Float[] location, String app) {
		return getClosestStores(location, app, false);
	}

	public List getClosestStores(Float[] location, String app, boolean licensee) {
		return getClosestStores(location, app, licensee, getConfig().getLocator().getRadius().intValue());
	}

	public List getClosestStores(Float[] location, String app, boolean licensee, int radius) {
		return getClosestStores(location, app, licensee, radius, false);
	}
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing) {
		return getClosestStores(location, app, licensee, radius, pricing, null);
	}
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state) {
		return getClosestStores(location, app, licensee, radius, pricing, state, false);
	}

	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, null);
	}
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, 10);
	}
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, true);
	}
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, ignoreActiveFlag, null);
	}
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, ignoreActiveFlag, p, false);
	}
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p, boolean isDefaultStore) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, ignoreActiveFlag, p, isDefaultStore, false);
	}
	
	@Override
	public List getClosestStores(Float[] location, String app,
			boolean licensee, int radius, boolean pricing, String state,
			boolean full, String licenseeType, int count,
			boolean ignoreActiveFlag, String p, boolean isDefaultStore, boolean ignoreStoreTypes) {
		boolean byState = location == null || location[0] == null || location[1] == null;
		if(byState) {
			return this.storeDAO.getStoresInState(state, p);
		}
		int miles = 0;
		if (!ignoreStoreTypes) {
			if(app.startsWith("PWT") && radius==6){
				miles = 5;
			} else {
				miles = getConfig().getLocator().getRadii()[radius].intValue();
			}
		} else if (ignoreStoreTypes && radius > 0) {
			miles = radius;
		} else {
			miles = getConfig().getLocator().getRadii()[5].intValue();
		}
		double latitude = 0.74 * miles / 50.0;
		double longitude = 1.2 * miles / 50.0;
		Float x = location[0], y = location[1];
		List out;
		String nativeSql = "SELECT STORE_NUMBER, "
			+ "ROUND(DISTANCE_IN_MILES, 2) DISTANCE_IN_MILES "
			+ "FROM (SELECT STORE_NUMBER, ADDRESS, CITY, STATE, ZIP, PHONE, "
            + "RTMS_WEBDB.DISTANCE(LATITUDE, LONGITUDE, " + y + ", " + x + ") DISTANCE_IN_MILES "
            + "FROM RTMS_WEBDB.STORE WHERE ";
    	if(!full)
	    	nativeSql += "LATITUDE BETWEEN (" + y + " - " + latitude + ")  AND (" + y + " + " + latitude + ") "
	            + "AND LONGITUDE BETWEEN (" + x + " - " + longitude + ") AND (" + x + " + " + longitude + ") "
	            + "AND ";
    	if(pricing)
    		nativeSql += "TIRE_PRICING_ACTIVE_FLAG = 1 AND ";
    	if(!app.startsWith("5starPrimary")) {
	    	if(Config.FIVESTAR.equals(getConfig().getSiteName())){
	    		nativeSql += "FIVESTAR_ACTIVE_FLAG = 1 AND ";
			}
    	}
    	String types = LocatorUtil.getTypesForApp(app, getConfig(), licensee, getStoreDAO(), licenseeType);
    	String activeFragment = "ACTIVE_FLAG = 1 ";
    	if(ignoreActiveFlag) {
    		activeFragment = "(ACTIVE_FLAG = 1 OR ACTIVE_FLAG = 0) ";
    	}
    	if(!ignoreStoreTypes)
    		nativeSql += activeFragment + types + "ORDER BY DISTANCE_IN_MILES) WHERE ";
    	else
    		nativeSql += activeFragment + "AND district_id is not null ORDER BY DISTANCE_IN_MILES) WHERE ";
    	if(!full)
    	    nativeSql += "DISTANCE_IN_MILES <= " + miles + " AND ";
    	nativeSql += "ROWNUM <= " + count;
		Session s = getSession();
		try {
			//System.out.println("SQL ======> "+nativeSql);
			SQLQuery q = s.createSQLQuery(nativeSql);
			q.addScalar("STORE_NUMBER", Hibernate.INTEGER);
			q.addScalar("DISTANCE_IN_MILES", Hibernate.INTEGER);
			out = q.list();
		} finally {
			//s.close();
			this.releaseSession(s);
		}
		return out;
	}

	public long getNextId() {
		long id = -1;
		String nativeSql = "SELECT LOCATOR_LOG_SEQ.NEXTVAL AS NEXT_ID FROM DUAL";
		Session s = getSession();
		List l = null;
		try {
			SQLQuery q = s.createSQLQuery(nativeSql);
			q.addScalar("NEXT_ID", Hibernate.LONG);
			l = q.list();
		} finally {
			//s.close();
			this.releaseSession(s);
		}
		id = ((Long)l.get(0)).longValue();
		return id;
	}
	
	public Store[] getStores(long[][] storeNumber) {
		int len = storeNumber.length;
		Store[] out = new Store[len];
		for(int i=0; i<len; i++) {
			Store s = getStore(storeNumber[i][0]);
			out[i] = new Store(s);
		}
		return out;
	}

	public Store[] getStoresLight(long[][] storeNumber) {
		int len = storeNumber.length;
		Store[] out = new Store[len];
		for(int i=0; i<len; i++) {
			Store s = getStoreLight(storeNumber[i][0]);
			out[i] = new Store(s);
		}
		return out;
	}
	public String getStoreType(Store store) {
		String out = "BSRO Corporate";
		String type = store.getType();
		if(type.startsWith("DLR")) {
			if("Tires Plus".equals(store.getStoreName()))
				out = "MTA Corporate";
			else out = "BSRO Dealer";
		}
		return out;
	}
	
	public List getHolidayHours(Store store) {
		return this.storeService.getStoreHolidayHours(store);
	}
	
	public void setHolidayHours(List<Store> stores) {
		this.storeService.setHolidayHours(stores);
	}

	public List getStoreHour(long storeNumber) {
		List l = getHibernateTemplate().find("from StoreHour s where s.id.storeNumber=?",storeNumber);	
		try{
			  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		        String strToday = sdf.format(new java.util.Date());
		        Store store = getStoreLight(storeNumber);
		        if(strToday.compareTo(StoreSearchUtils.holidayDateToCheck) <=0 && store!=null && StoreSearchUtils.isHolidayStoreType(store.getStoreType())){
		  	      	 if(l!=null && l.size()>0 && !StoreSearchUtils.holidayHoursExceptionStoreNumberList.contains(String.valueOf(storeNumber))){
		  	      		//Util.debug("\t\t\t\t\t  Close for "+store.getStoreNumber());
		        		StoreHour sh = new StoreHour();
		        		StoreHourId shi = new StoreHourId();
		        		shi.setStoreNumber(storeNumber);
		        		shi.setWeekDay(StoreSearchUtils.holidayToCheckName);
		        		sh.setCloseTime("Closed");
		        		sh.setOpenTime("Closed");
		        		sh.setId(shi);
		        		l.add(sh);
			        }
		        }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		return l;
	}
	
	public Store getStore(long storeNumber) {
		return this.storeDAO.findStoreById(storeNumber);
	}
	public Store getStoreLight(long storeNumber) {
		return this.storeDAO.findStoreLightById(storeNumber);
	}
	public String getNameForStoreNumber(long number) {
		return getStore(number).getStoreName();
	}

	public void log(long id, String app, String remoteIP, String type, long interval) {
		log(id, app, remoteIP, type, interval, null, null, null, null);
	}

	public void log(long id, String app, String remoteIP, String type, long interval, String street, String city, String state, String zip) {
		log(id, app, remoteIP, type, interval, street, city, state, zip, 0);
	}
	
	public void log(long id, String app, String remoteIP, String type, long interval, String street, String city, String state, String zip, long storeNumber) {
		/*if(street != null && street.length() > 50)
			street= street.substring(0,50);
    	if(city != null && city.length() > 50)
    		city= city.substring(0,50);
    	if(state != null && state.length() > 2)
    		state= state.substring(0,2);
    	if(zip != null && zip.length() > 10)
    		zip= zip.substring(0,10);
		LocatorLogId logId = new LocatorLogId();
		logId.setRequestType(type);
		logId.setRequestId(new Long(id));
		LocatorLog log = new LocatorLog();
		log.setInterval(new Long(interval));
		log.setAppName(LocatorUtil.getLogNameForApp(app));
		log.setCity(city);
		log.setCreatedDate(new java.util.Date());
		log.setRemoteIp(remoteIP);
		log.setState(state);
		log.setStoreNumber(new Long(storeNumber));
		log.setStreet(street);
		log.setZip(zip);
		log.setId(logId);
		getHibernateTemplate().save(log);*/
	}
	
	/**
	 * @see com.bfrc.framework.Operator#operate(Object)
	 */
	public Object operate(Object o) throws Exception {
		throw new RuntimeException("operate not implemented");
	}
	
	public StoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}

	public StoreDAO getStoreDAO() {
		return this.storeDAO;
	}

	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
	}

}
