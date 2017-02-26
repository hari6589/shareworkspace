package com.bfrc.framework.dao.hibernate3;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.hibernate.*;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.dao.DataAccessException;

import com.bfrc.Config;
import com.bfrc.framework.businessdata.DataOperator;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StoreSearchUtils;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.geo.HrDistricts;
import com.bfrc.pojo.store.BfrcStoreMap;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreHoliday;
import com.bfrc.pojo.store.StoreHolidayHour;
import com.bfrc.pojo.store.StoreHour;
import com.bfrc.pojo.store.StoreHourId;
import com.bfrc.pojo.store.StoreMilitary;
import com.bfrc.security.Encode;
import com.bfrc.storelocator.util.LocatorUtil;

public class StoreDAOImpl extends HibernateDAOImpl implements StoreDAO, DataOperator {
	
	private int storesCount = 0;

	public Map getFullStoreMap(String siteName) {
		Map out = new HashMap();
		List l = getHibernateTemplate().find("from BfrcStoreMap m where m.siteName=?", new Object[]{siteName});
		if(l != null && l.size() > 0){
			Iterator i = l.iterator();
			while(i.hasNext()) {
				BfrcStoreMap m = (BfrcStoreMap)i.next();
				out.put(m.getStoreType().trim(), m);
			}
		}
		return out;
	}

	public Map getPrimaryStoreMap(String siteName) {
		return getPrimaryStoreMap(siteName, null);
	}
	
	public Map getPrimaryStoreMap(String siteName, String licenseeType) {
		String primaryType = null;
		if(licenseeType != null) {
			Map secondary = getSecondaryStoreMap(siteName);
			Iterator i = secondary.keySet().iterator();
			while(i.hasNext()) {
				BfrcStoreMap m = (BfrcStoreMap)secondary.get(i.next());
				if(m.getStoreType().trim().equals(licenseeType.trim()))
					primaryType = m.getParentType();
			}
		}
		Map out = getFullStoreMap(siteName);
		Iterator i = out.keySet().iterator();
		while(i.hasNext()) {
			BfrcStoreMap m = (BfrcStoreMap)out.get(i.next());
			if(m.getParentType() != null || (primaryType != null && (!primaryType.trim().equals(m.getStoreType().trim()))))
				i.remove();
		}
		return out;
	}

	public Map getSecondaryStoreMap(String siteName) {
		Map out = getFullStoreMap(siteName);
		Iterator i = out.keySet().iterator();
		while(i.hasNext()) {
			BfrcStoreMap m = (BfrcStoreMap)out.get(i.next());
			if(m.getParentType() == null)
				i.remove();
		}
		return out;
	}

	public Map getFullStoreMap(Config c) {
		return getFullStoreMap(c.getSiteName());
	}

	public Map getPrimaryStoreMap(Config c) {
		return getPrimaryStoreMap(c, null);
	}
	
	public Map getPrimaryStoreMap(Config c, String licenseeType) {
		return getPrimaryStoreMap(c.getSiteName(),licenseeType);
	}

	public Map getSecondaryStoreMap(Config c) {
		return getSecondaryStoreMap(c.getSiteName());
	}
	public Store findStoreById(Long storeNumber) {
		if(storeNumber == null)
			return null;
		List l = getHibernateTemplate().findByNamedParam("from Store s where s.storeNumber=:storeNumber",
				"storeNumber",
				storeNumber);
		if (l == null || l.size() < 1)
			return null;

		Store store = (Store)l.get(0);
		return store;
	}
	
	public List getStates(Map param) throws DataAccessException {
		param.put("column", "state");
		return (List)operate(param);
	}

	public List getCities(Map param) throws DataAccessException {
		param.put("column", "city");
		return (List)operate(param);
	}

	public List getStores(Map param) throws DataAccessException {
		param.put("entity", "store");
		return (List)operate(param);
	}

	public List getStoresInZip(Map param) throws DataAccessException {
		param.put("column", "zip");
		return (List)operate(param);
	}

	public List getStoresInState(String state) throws DataAccessException {
		Map param = new HashMap();
		param.put("state", state);
		param.put("entity", "store");
		param.put("column", "state");
		return (List)operate(param);
	}
	
	public List getStoresInState(String state, String pNum) throws DataAccessException {
		Map param = new HashMap();
		param.put("state", state);
		param.put("pageNumber", pNum);
		param.put("entity", "store");
		param.put("column", "state");
		return (List)operate(param);
	}
	
	public Object operate(Object o) throws DataAccessException {
		Map param = (Map)o;
		Config config = getConfig();
		Map storeMap = getPrimaryStoreMap(config, (String)param.get("licenseeType"));
		// build criteria from primary type(s)
		Iterator i = storeMap.keySet().iterator();
		String type = (String)i.next();
		String commonWhere = "(store_type='"+type+"'";
		while(i.hasNext()) {
			type = (String)i.next();
			commonWhere += " or store_type='"+type+"'";
		}
		Boolean licenseeParam = (Boolean)param.get("licensee");
		boolean pricing = param.containsKey("pricing");
		if(licenseeParam == null)
			licenseeParam = new Boolean(true);
		boolean licensee = licenseeParam.booleanValue();
		// add secondary types if "licensee" flag is set
		if(licensee) {
			Map secondaryMap = getSecondaryStoreMap(config);
			i = secondaryMap.keySet().iterator();
			while(i.hasNext()) {
				type = (String)i.next();
				commonWhere += " or store_type='"+type+"'";
			}
		}
		commonWhere += ")";
		if(param.containsKey("partner")) {
			String types = LocatorUtil.getTypesForApp("partner", config, licensee, this, null);
			if(pricing)
				types = LocatorUtil.getTypesForApp("partner-pricing", config, licensee, this, null);
			commonWhere = "active_flag=1 " + types;
		}else if(param.containsKey("fleetCare")) {
			String types = LocatorUtil.getTypesForApp("fleetCare", config, licensee, this, null);
			//Util.debug("xxxxxxxxxxxxxxxxxxx\t\ttypes--------"+types);
			commonWhere = "active_flag=1 " + types;
		}else if(param.containsKey("5starPrimary")) {
			String types = LocatorUtil.getTypesForApp("5starPrimary", config, licensee, this, null);
			if(pricing)
				types = LocatorUtil.getTypesForApp("5starPrimary-pricing", config, licensee, this, null);
			//Util.debug("xxxxxxxxxxxxxxxxxxx\t\ttypes--------"+types);
			commonWhere = "active_flag=1 " + types;
		}
		else commonWhere += " and active_flag=1";
		if(pricing)
			commonWhere += " and tire_pricing_active_flag=1";
		if(param.containsKey("appointment"))
			commonWhere += " and online_appointment_active_flag=1";
		if(!param.containsKey("5starPrimary")) {
			if(Config.FIVESTAR.equals(getConfig().getSiteName())){
				commonWhere += " and FIVESTAR_ACTIVE_FLAG = 1 ";
			}
		}
		
		int pageNumber = 1;				
		String entity = (String)param.get("entity");
		String column = (String)param.get("column");
		String pNum = (String)param.get("pageNumber");
		if (pNum != null)
			pageNumber = Integer.valueOf(pNum).intValue();
		
		if (pageNumber < 1)
			pageNumber = 1;
		
		if(column != null && !"store".equals(entity)) {
			if("zip".equals(column)) {
				String hql = "from Store s where s.zip=:zip and " + commonWhere;
				Store value = new Store();
				value.setZip((String)param.get("zip"));
				return getHibernateTemplate().findByValueBean(hql, value);
			}
			String nativeSql = "select distinct s.";
			String alias = column, nativeColumn = column, where = " where " + commonWhere;
			Store values = new Store();
			if("state".equals(column)) {
				where = ", states sta " + where + " and s.state=sta.state";
				nativeColumn = "state as state, sta.name";
				alias = "name";
			} else if("city".equals(column)) {
				where += " and s.state=:state";
				values.setState((String)param.get("state"));
				nativeColumn = "city";
				alias = "city";
			}
			nativeSql += nativeColumn 
				+ " as " 
				+ alias 
				+ " from store s" 
				+ where + " order by " + alias;
			Session s = getSession();
			List l = null;
			try {
				SQLQuery q = s.createSQLQuery(nativeSql);
				q.addScalar(alias, Hibernate.STRING);
				q.setProperties(values);
				if("name".equals(alias)) {
					q.addScalar("state", Hibernate.STRING);
				}
				l = q.list();
			} finally {
				//s.close();
				this.releaseSession(s);
			}
			return l;
		}
		String district = (String)param.get("district");
		if("store".equals(entity)) {
			String hql = "from Store s where s.state=:state";
			if(!"state".equals(column))
				hql += " and upper(s.city)=upper(:city)";
			if(district != null) {
				hql = "from HrDistricts d join d.stores s where d.districtId=:districtId and " + commonWhere;
				hql += " order by s.city";
				HrDistricts hrDistricts = new HrDistricts();
				hrDistricts.setDistrictId(district);
				List l = getHibernateTemplate().findByValueBean(hql, hrDistricts);
				return l;
			}
			hql += " and " + commonWhere;
			Store value = new Store();
			value.setState((String)param.get("state"));
			if(!"state".equals(column))
				value.setCity((String)param.get("city"));
			if (Config.FCAC.equals(config.getSiteName())) {
				final String queryString = hql;
				final int scrollOffset = pageNumber;
				final int scrollSize = 30;
				final Store storeObj = value;
				return getHibernateTemplate().execute(new HibernateCallback(){
					@Override
					public Object doInHibernate(Session session) throws HibernateException,
							SQLException {
						List resultList = new ArrayList();
						if (!resultList.isEmpty())
							resultList.clear();
						
						Query query = session.createQuery(queryString);
						query.setProperties(storeObj);						
						
						ScrollableResults rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
						if (rs != null) {
							rs.first();
							if (scrollOffset == 1) {
								rs.scroll(((scrollOffset - 1) * scrollSize)-2);
							} else {
								rs.scroll(((scrollOffset - 1) * scrollSize)-1);
							}
							
					        for (int i = 0; i < scrollSize && rs.next(); i++) {
					        	Object[] results = rs.get();
					            resultList.add(results.length == 1 ? results[0]
					                  : results);
					        }
					        
					        rs.last();
					        final int total = rs.getRowNumber() + 1; // start with -1
					        rs.close();
					        setStoreCount(total);
						} else {
							setStoreCount(0);
						}
				        return resultList;						
					}
				});
			} else {
				return getHibernateTemplate().findByValueBean(hql, value);
			}
		}
		return null;
	}

	public List getStoreStates() {
		return getHibernateTemplate().findByNamedQueryAndValueBean("StoreState", getConfig());
	}
	
	public List getStoreCities(String state) {
		String[] names = {"siteName", "state"};
		Object[] values = new Object[names.length];
		values[0] = getConfig().getSiteName();
		values[1] = state;
		return getHibernateTemplate().findByNamedQueryAndNamedParam("StoreCity", names, values);
	}

	public List getStores(String state, String city) {
		String[] names = {"siteName", "state", "city"};
		Object[] values = new Object[names.length];
		values[0] = getConfig().getSiteName();
		values[1] = state;
		values[2] = city;
		List stores = getHibernateTemplate().findByNamedQueryAndNamedParam("StoreByStateCity", names, values);
		for (int i=0;i<stores.size();i++) {
			Store store = (Store) stores.get(i);
			List storeHours = getStoreHours(store.getStoreNumber());
			String storeHour = getStoreHourHTML(storeHours);
			store.setStoreHour(storeHour);			
		}
		return stores;		
	}

	public List getStoreHours(Long storeNumber) {
		return getHibernateTemplate().findByNamedQueryAndNamedParam("StoreHours", "storeNumber", storeNumber);
	}
	
	public String getStoreHourHTML(List storeHours) {
		String out = "";
		if (storeHours == null || storeHours.isEmpty())
			return out;
		for (int i=0; i<7; i++) {
			StoreHour curr = getHour(storeHours, DAY_ABBREV[i]);
			for (int j=i+1; j<=7; j++) {
				StoreHour test = null;
				if (j<7)
					test = getHour(storeHours, DAY_ABBREV[j]);
				if (curr != null && curr.equals(test))
                    continue;
				out = out + getHourHTML(i, j - 1, curr);
				i = j - 1;
				break;
			}			
		}
		return out;		
	}

	private static StoreHour getHour(List l, String day) {	
		StoreHour hour = null;		
		for (int i=0; i<l.size(); i++) {
			hour = (StoreHour) l.get(i);
			if (hour != null && hour.getId().getWeekDay().equals(day)) {
				return hour;
			}				
		}
		return null;
	}
	
	private static String getHourHTML(int begin, int end, StoreHour hour) {
		if(hour == null || hour.getOpenTime() == null || hour.getCloseTime() == null)
            return "";
        String out = DAY_LABEL_ABBREV[begin];
        if(end > begin)
        {
            out = out + "-" + DAY_LABEL_ABBREV[end];            
        }
        return out + ": " + format(hour.getOpenTime()) + "-" + format(hour.getCloseTime()) + "<br/>";
	}
	
	private static String format(String time)
    {
        String temp = pad(time);
        int hour = Integer.valueOf(temp.substring(0, 2)).intValue();
        String m = "am";
        if(hour > 11)
        {
            m = "pm";
            if(hour > 12)
                hour -= 12;
        }
        String out = Integer.toString(hour) + ":" + temp.substring(3, 5);
        return out + m;
    }
	
	private static String pad(String param)
    {
        if(param == null)
            return null;
        String temp = param.trim();
        if(temp.length() < 5)
            return "0" + temp;
        return temp;
    }
	
	public void setStoreCount(int count) {
		this.storesCount = count;
	}
	
	public int getStoreCount() {
		return this.storesCount;
	}

	public static final int MONDAY = 0;
    public static final int TUESDAY = 1;
    public static final int WEDNESDAY = 2;
    public static final int THURSDAY = 3;
    public static final int FRIDAY = 4;
    public static final int SATURDAY = 5;
    public static final int SUNDAY = 6;
    public static final String DAY_LABEL[] = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    public static final String DAY_ABBREV[] = {
        "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"
    };
    public static final String DAY_LABEL_ABBREV[] = {
        "M", "TU", "W", "TH", "F", "Sa", "Su"
    };
    
    public List findStoresByStoreNumbers(List storeNumbers) {
		if(storeNumbers == null)
			return null;
		List<Store> tempList = new ArrayList<Store>(); //max allowable inside 'in' statement is 1000 so take 0-999,1000-1999
		int x = storeNumbers.size()/1000;
		if(storeNumbers.size()%1000>0)
			x++;
		List[] storeNumbersArray = new List[x];
		for(int i = 0; i<x ; i++){
			if(i==(x-1))
				storeNumbersArray[i]=storeNumbers.subList((i*1000), storeNumbers.size());
			else
				storeNumbersArray[i]=storeNumbers.subList((i*1000), (i*1000)+1000);
		}
		for(List storeNumbersPart: storeNumbersArray){
			List<Store> storeList = getHibernateTemplate().findByNamedParam("from Store s where s.storeNumber in (:storeNumbers) ",
					"storeNumbers",
					storeNumbersPart);
			if(storeList!=null&&storeList.size()>0)
				tempList.addAll(storeList);
		}
		
		
		//Preserve Order
		Map<Long,Store> storeMap = new HashMap<Long,Store>();
		List<Store> l = new ArrayList<Store>();
		if(tempList!=null&&tempList.size()>0){
			for(Store store: tempList)
				storeMap.put(store.getStoreNumber(), store);
			for(int i=0;i<storeNumbers.size();i++){
				Store store = storeMap.get((Long)storeNumbers.get(i));
				if(store!=null){
					l.add(store);
				}
			}
		}
		return l;
	}
    public List findStoresByStoreNumbers(String pipeDelimitedStoreNumbers) {
		if(pipeDelimitedStoreNumbers == null)
			return null;
		try{
			String[] tokens = StringUtils.tokenToArray(pipeDelimitedStoreNumbers, "|");
			List l = new ArrayList();
			for(int i =0; tokens != null && i<tokens.length; i++){
				l.add(Long.valueOf(tokens[i]));
				if(l.size() >999)
					break;
			}
			return findStoresByStoreNumbers(l);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
    public List findSiteStoreList(Config thisConfig){
    	return findSiteStoreList(thisConfig.getSiteName());
    }
    public List findSiteStoreList(String siteName){
    	
    	Map storeMap = getFullStoreMap(siteName);
    	if(storeMap != null && storeMap.size() > 0){
	        List storeTypes = new ArrayList();
	        storeTypes.addAll(storeMap.keySet());
	        String cons = "'"+(String)storeTypes.get(0)+"'";
	        for(int i=1; i<storeTypes.size(); i++){
	            cons +=",'"+storeTypes.get(i)+"'";
	        }
	        String fiveStarCons = "";
	        if("5STR".equalsIgnoreCase(siteName)){
	        	fiveStarCons  =" AND FIVESTAR_ACTIVE_FLAG = 1 ";
	        }
	        String hql = "from Store s where s.storeType in ("+cons+") "+fiveStarCons+" and active_flag=1 order by s.storeName";
	        List<Store> stores = getHibernateTemplate().find(hql);
	        
	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
	        String strToday = sdf.format(new java.util.Date());
	        
	        if(strToday.compareTo(StoreSearchUtils.holidayDateToCheck) <=0 && StoreSearchUtils.isHolidayStoreType(siteName)){
	        if(stores!=null&&stores.size()>0){
	        	for(Store store : stores){
	        		if(!StoreSearchUtils.holidayHoursExceptionStoreNumberList.contains(String.valueOf(store.getStoreNumber()))){
	        			//Util.debug("\t\t\t\t\t  Close for "+store.getStoreNumber());
	        			StoreHour sh = new StoreHour();
	        			StoreHourId shi = new StoreHourId();
	        			shi.setStoreNumber(store.getStoreNumber());
	        			shi.setWeekDay(StoreSearchUtils.holidayToCheckName);
	        			sh.setCloseTime("Closed");
	        			sh.setOpenTime("Closed");
	        			sh.setId(shi);
	        			store.getHours().add(sh);
	        		}
	        	}
	        }
	        }
	        return stores;
    	}
    	return null;
    } 
    
    public List findStoresByZoneManagerEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		List stores = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findStoresByZoneManagerEmail", names, values);
		return stores;
	}

	public List findStoresByZoneId(Object id) {
		String[] names = { "zoneId" };
		Object[] values = new Object[] { Integer.valueOf(id.toString()) };
		List stores = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findStoresByZoneId", names, values);
		return stores;
	}

	public List findStoresLightByZone(String id, String brand) {
		Session s = getSession();
		String cons = StoreSearchUtils.getBrandStoreTypesClause(brand);
		try{
		if (id == null)
			return null;
		String sql = "select distinct s.STORE_NAME as storeName, s.STORE_NUMBER as storeNumber, s.ADDRESS as storeAddress, s.STORE_TYPE as storeStoreType from STORE s, HR_DISTRICTS d where s.DISTRICT_ID in (select DISTRICT_ID from HR_DISTRICTS hd, ZONE_MANAGER zm  where hd.DISTRICT_ZONE=zm.ZONE_ID and  hd.DISTRICT_ZONE='"
				+ id
				+ "') and "+cons
				+ " order by s.STORE_NUMBER asc";

		List l = s.createSQLQuery(sql)
				.addScalar("storeName", Hibernate.STRING)
				.addScalar("storeNumber", Hibernate.LONG)
				.addScalar("storeAddress", Hibernate.STRING)
				.addScalar("storeStoreType", Hibernate.STRING).list();
		List stores = new ArrayList();
		for (int i = 0; i < l.size(); i++) {
			Object[] objects = (Object[]) l.get(i);
			Store store = new Store();
			store.setStoreName((String) objects[0]);
			store.setStoreNumber(((Long) objects[1]).longValue());
			store.setAddress((String) objects[2]);
			store.setStoreType((String) objects[3]);
			stores.add(store);
		}
		return stores;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}

	public List findStoresByDistrictManagerEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		List stores = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findStoresByDistrictManagerEmail", names, values);
		return stores;
	}

	public List findStoresByDistrictId(Object id) {
		String[] names = { "districtId" };
		Object[] values = new Object[] { id };
		List stores = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findStoresByDistrictId", names, values);
		return stores;
	}

	@SuppressWarnings("unchecked")
	public Store findStoreLightById(Long storeNumber) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(storeNumber);
		List<Store> stores = findStoresLightByIds(ids);
		if(stores != null && stores.size()>0){
			Store store =  stores.get(0);
			return store;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Store> findStoresLightByIds(List<Long> storeNumbers) {

		//Util.debug("storeNumber " + storeNumber);
		if (storeNumbers == null || storeNumbers.size() == 0)
			return null;
		
		StringBuffer storeNumbersString = new StringBuffer();
		boolean firstTime = true;
		for (Long storeNumber : storeNumbers) {
			if (firstTime) {
				storeNumbersString.append("(" + storeNumber);
				firstTime = false;
			} else
				storeNumbersString.append(", " + storeNumber);
		}
		storeNumbersString.append(")");

		//Util.debug("doing the fetch for stores");
		Session s = getSession();
		try {
			String sql = "select s.store_name as storeName, s.store_number as storeNumber, s.address as storeAddress, s.store_type as storeStoreType, s.city as storeCity, s.state as storeState, s.zip as storeZip, s.phone as storePhone, s.manager_name as storeManagerName, s.manager_email_address as storeMEA, s.active_flag as storeActiveFlag, "
					+ "s.WEBSITE_NAME as storeWebSiteName, s.geo_match as storeGeoMatch, s.latitude as storeLatitude, s.longitude as storeLongitude, s.postal_status_code as storePostalStatusCode, s.online_appointment_active_flag as storeOAAF, s.tire_pricing_active_flag as storeTPAF, "
					+ "s.fivestar_active_flag as storeFSAF, s.tracking_phone as storeTrackingPhone, sr.REASON_DESC as reasonDesc from Store s, STORE_TEMPCLOSE_REASONS sr where sr.REASON_ID(+) = s.REASON_ID and s.store_number in"
					+ Encode.escapeDb(storeNumbersString.toString());

			//Util.debug("this is the before sql "+sql);
			List<Object[]> l = s.createSQLQuery(sql.toUpperCase()).addScalar("storeName", Hibernate.STRING).addScalar("storeNumber", Hibernate.LONG)
					.addScalar("storeAddress", Hibernate.STRING).addScalar("storeStoreType", Hibernate.STRING)
					.addScalar("storeCity", Hibernate.STRING).addScalar("storeState", Hibernate.STRING).addScalar("storeZip", Hibernate.STRING)
					.addScalar("storePhone", Hibernate.STRING).addScalar("storeManagerName", Hibernate.STRING)
					.addScalar("storeMEA", Hibernate.STRING).addScalar("storeActiveFlag", Hibernate.BIG_DECIMAL)
					.addScalar("storeWebSiteName", Hibernate.STRING).addScalar("storeGeoMatch", Hibernate.STRING)
					.addScalar("storeLatitude", Hibernate.FLOAT).addScalar("storeLongitude", Hibernate.FLOAT)
					.addScalar("storePostalStatusCode", Hibernate.STRING).addScalar("storeOAAF", Hibernate.BIG_DECIMAL)
					.addScalar("storeTPAF", Hibernate.BIG_DECIMAL).addScalar("storeFSAF", Hibernate.BOOLEAN)
					.addScalar("storeTrackingPhone", Hibernate.STRING).addScalar("reasonDesc", Hibernate.STRING)
					//.setResultTransformer(Transformers.aliasToBean(Store.class))
					.list();


			if(l!=null && l.size()>0){
				
				List<Store> stores = new ArrayList<Store>(storeNumbers.size());
				for(int i=0;i<storeNumbers.size();i++)
					stores.add(null);
				for(Object[] objects : l){
				    int i=0;
					Store store = new Store();
					//Util.debug("store name result " + (String) objects[i]);
					store.setStoreName((String) objects[i]); i++;
					store.setStoreNumber(((Long) objects[i]).longValue()); i++;
					store.setAddress((String) objects[i]); i++;
					store.setStoreType((String) objects[i]); i++;
					store.setCity((String) objects[i]); i++;
					store.setState((String) objects[i]);i++;
					store.setZip((String) objects[i]);i++;
					store.setPhone((String) objects[i]);i++;
					store.setManagerName((String) objects[i]);i++;
					store.setManagerEmail((String) objects[i]);i++;
					store.setActiveFlag((BigDecimal) objects[i]);i++;
					store.setWebsiteName((String) objects[i]);i++;
					store.setGeoMatch((String) objects[i]);i++;
					store.setLatitude((Float) objects[i]);i++;
					store.setLongitude((Float) objects[i]);i++;
					store.setPostalStatusCode((String) objects[i]);i++;
					store.setOnlineAppointmentActiveFlag((BigDecimal) objects[i]);i++;
					store.setTirePricingActiveFlag((BigDecimal) objects[i]);i++;
					store.setFivestarActiveFlag((Boolean) objects[i]);i++;
					store.setTrackingPhone((String) objects[i]);i++;
					store.setReasonDesc((String) objects[i]);
	
					stores.set(storeNumbers.indexOf(store.getStoreNumber()),store);
				}
				return stores;
			}

		} catch (Exception e) {
			Util.debug(" there was an ERROR");
			e.printStackTrace();
		} finally {
			// s.close();
			this.releaseSession(s);
		}
		return null;

	}

	public List getStoresLightByDistrict(String id, String brand) {
        Session s = getSession();
        String cons = StoreSearchUtils.getBrandStoreTypesClause(brand);
        try{
		String sql = "select distinct s.STORE_NAME as storeName, s.STORE_NUMBER as storeNumber, s.ADDRESS as storeAddress, s.STORE_TYPE as storeStoreType from STORE s, HR_DISTRICTS d where s.DISTRICT_ID ='"
				+ id
				+ "' and "+cons
				+ " order by s.STORE_NUMBER asc";

		List l = s.createSQLQuery(sql)
				.addScalar("storeName", Hibernate.STRING)
				.addScalar("storeNumber", Hibernate.LONG)
				.addScalar("storeAddress", Hibernate.STRING)
				.addScalar("storeStoreType", Hibernate.STRING).list();
		List stores = new ArrayList();
		for (int i = 0; i < l.size(); i++) {
			Object[] objects = (Object[]) l.get(i);
			Store store = new Store();
			store.setStoreName((String) objects[0]);
			store.setStoreNumber(((Long) objects[1]).longValue());
			store.setAddress((String) objects[2]);
			store.setStoreType((String) objects[3]);
			stores.add(store);
		}
		return stores;
        }finally {
        	//s.close();
			this.releaseSession(s);
		}

	}

	public List getStoresLightByDistrict(String id, String[] brands) {
        Session s = getSession();

        try{
		String sql = "select distinct s.STORE_NAME as storeName, s.STORE_NUMBER as storeNumber, s.ADDRESS as storeAddress, s.STORE_TYPE as storeStoreType from STORE s, HR_DISTRICTS d where s.DISTRICT_ID =?";
		if (brands != null && brands.length > 0) {
			sql += " and TRIM(BOTH FROM s.STORE_TYPE) in (:brands)";
		}
		sql += " order by s.STORE_NUMBER asc";

		SQLQuery query = s.createSQLQuery(sql);
		
		query.setString(0, id);
		
		if (brands != null && brands.length > 0) {
			query.setParameterList("brands", brands);
		}
		
		List l = query
				.addScalar("storeName", Hibernate.STRING)
				.addScalar("storeNumber", Hibernate.LONG)
				.addScalar("storeAddress", Hibernate.STRING)
				.addScalar("storeStoreType", Hibernate.STRING).list();
		List stores = new ArrayList();
		for (int i = 0; i < l.size(); i++) {
			Object[] objects = (Object[]) l.get(i);
			Store store = new Store();
			store.setStoreName((String) objects[0]);
			store.setStoreNumber(((Long) objects[1]).longValue());
			store.setAddress((String) objects[2]);
			store.setStoreType((String) objects[3]);
			stores.add(store);
		}
		return stores;
        }finally {
        	//s.close();
			this.releaseSession(s);
		}

	}
	
	public List getStoresLightByDistrict(String id) {
		String hql = "select s.storeName, s.storeNumber, s.address, s.storeType from Store s where s.district.id=:districtId";
		hql += " order by s.storeNumber asc";
		HrDistricts hrDistricts = new HrDistricts();
		hrDistricts.setDistrictId(id);
		List l = getHibernateTemplate().findByNamedParam(hql, "districtId", id);
		List stores = new ArrayList();
		for (int i = 0; i < l.size(); i++) {
			Object[] objects = (Object[]) l.get(i);
			Store store = new Store();
			store.setStoreName((String) objects[0]);
			store.setStoreNumber((Long) objects[1]);
			store.setAddress((String) objects[2]);
			store.setStoreType((String) objects[3]);
			stores.add(store);
		}
		return stores;

	}

public List findSiteStoreLightList(String siteName){
    	
    	Map storeMap = getFullStoreMap(siteName);
    	if(storeMap != null && storeMap.size() > 0){
	        List storeTypes = new ArrayList();
	        storeTypes.addAll(storeMap.keySet());
	        String cons = "'"+(String)storeTypes.get(0)+"'";
	        for(int i=1; i<storeTypes.size(); i++){
	            cons +=",'"+storeTypes.get(i)+"'";
	        }
	        String fiveStarCons = "";
	        if("5STR".equalsIgnoreCase(siteName)){
	        	fiveStarCons  =" AND FIVESTAR_ACTIVE_FLAG = 1 ";
	        }
	        String hql = "select s.storeName, s.storeNumber, s.address, s.storeType, s.city, s.state, s.zip from Store s where s.storeType in ("+cons+") "+fiveStarCons+" and active_flag=1 order by s.storeName";
	        List l = getHibernateTemplate().find(hql);
			List<Store> stores = new ArrayList<Store>();
			for (int i = 0; i < l.size(); i++) {
				Object[] objects = (Object[]) l.get(i);
				Store store = new Store();
				store.setStoreName((String) objects[0]);
				store.setStoreNumber((Long) objects[1]);
				store.setAddress((String) objects[2]);
				store.setStoreType((String) objects[3]);
				store.setCity((String) objects[4]);
				store.setState((String) objects[5]);
				store.setZip((String) objects[6]);
				stores.add(store);
			}
	        
	        return stores;
    	}
    	return null;
    } 
	
	public List getStoresLightByAdresses(String address, String city, String state, String zip){
		String fields = "select STORE_NUMBER as storeNumber, DISTRICT_ID as districtId, ADDRESS as address, CITY as city, cast(STATE as VARCHAR2(2)) as state, ZIP as zip, PHONE as phone, TRACKING_PHONE as trackingPhone, MANAGER_NAME as managerName, MANAGER_EMAIL_ADDRESS managerEmailAddress, cast(STORE_TYPE as VARCHAR2(4)) as storeType from STORE";
		Session s = getSession();
		try{
		StringBuffer sb = new StringBuffer();
	    sb.append(fields);
		sb.append(" WHERE ACTIVE_FLAG = 1 \n");
		if(address != null)
		    sb.append(" AND lower(REGEXP_REPLACE(ADDRESS, '"+StringUtils.NAME_FILTER_REGX+"', ''))= lower('" + StringUtils.nameFilter(address) +"') \n");
		if(city != null)
		    sb.append(" AND lower(REGEXP_REPLACE(CITY, '"+StringUtils.NAME_FILTER_REGX+"', ''))= lower('" + StringUtils.nameFilter(city) + "') \n");
		if(state != null)
			sb.append(" AND lower(STATE)=lower('" + Encode.escapeDb(state)+"') \n");
		if(zip != null)
		    sb.append(" AND ZIP ='" + Encode.escapeDb(zip)+"' \n");
		String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List stores = null;
	    try{
	    	stores = s
	    .createSQLQuery(sql)
	    .addScalar("storeNumber",Hibernate.LONG)
	    .addScalar("districtId")
	    .addScalar("address")
	    .addScalar("city")
	    .addScalar("state")
	    .addScalar("zip")
	    .addScalar("phone")
	    .addScalar("trackingPhone")
	    .addScalar("managerName")
	    .addScalar("managerEmailAddress")
	    .addScalar("storeType")
	    .setResultTransformer(Transformers.aliasToBean(Store.class)).list();
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("Error with getStoresLightByAdresses: "+sql);
		}
		return stores;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	public List getStoresLightCityStateOnly(){
		String fields = "select distinct CITY as city, cast(STATE as VARCHAR2(2)) as state from STORE";
		Session s = getSession();
		try{
		StringBuffer sb = new StringBuffer();
	    sb.append(fields);
		sb.append(" WHERE ACTIVE_FLAG = 1 \n");
		Map storeMap = getFullStoreMap(getConfig());
		Iterator i = storeMap.keySet().iterator();
		String type = (String)i.next();
		String commonWhere = " and (store_type='"+type+"'";
		while(i.hasNext()) {
			type = (String)i.next();
			commonWhere += " or store_type='"+type+"'";
		}
		sb.append(commonWhere+")");
		sb.append(" order by state, city  \n");
		String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List stores = null;
	    try{
	    	stores = s
	    .createSQLQuery(sql)
	    .addScalar("city")
	    .addScalar("state")
	    .setResultTransformer(Transformers.aliasToBean(Store.class)).list();
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("Error with getStoresLightByAdresses: "+sql);
		}
		return stores;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	
	public StoreMilitary findMilitaryStore(Long storeNumber){			
		List l = getHibernateTemplate().find("from StoreMilitary sm where sm.storeNumber=?", new Object[]{storeNumber});
		StoreMilitary sm = null;
		if(l != null && l.size() > 0){
			Iterator i = l.iterator();
			sm = (StoreMilitary)i.next();			
		}
		return sm;		
	}
	
	public List<Long> findMilitaryStores(List<Long> storeNumbers){
		List<Long> l = new ArrayList<Long>();
		if(storeNumbers != null && !storeNumbers.isEmpty()){
			l = getHibernateTemplate().findByNamedParam("select sm.storeNumber from StoreMilitary sm where sm.storeNumber in (:storeNumbers)", "storeNumbers", storeNumbers);
		}
		
		return l;		
	}

	public List<StoreHoliday> getStoreHolidaysBetweenDates(Date start,Date end){
		//TODO figure out how to fetch these by month, day, year strings
		if(start == null || end==null)
			return null;
		String startYear,endYear;
		SimpleDateFormat yearSDF = new SimpleDateFormat("yyyy");
		startYear = yearSDF.format(start);
		endYear = yearSDF.format(end);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		GregorianCalendar gcStart = new GregorianCalendar();
		gcStart.setTime(start);
		gcStart.add(Calendar.DATE, -1);
		
		GregorianCalendar gcEnd = new GregorianCalendar();
		gcEnd.setTime(end);
		//12AM end day
		gcEnd.set(Calendar.HOUR_OF_DAY, 0);
		gcEnd.set(Calendar.MINUTE, 0);
		gcEnd.set(Calendar.SECOND, 0);
		gcEnd.set(Calendar.MILLISECOND, 0);
		gcEnd.set(Calendar.AM_PM, Calendar.AM);
		
		List<StoreHoliday> l = getHibernateTemplate().find("from StoreHoliday sm where (sm.id.year >= "+startYear+" and  sm.id.year <= "+endYear+")");
		try{
			//doing this to get to all zeros.
			Date gcEndDate = dateFormat.parse( (gcEnd.get(Calendar.MONTH) + 1) +"/"+(gcEnd.get(Calendar.DAY_OF_MONTH))+"/"+gcEnd.get(Calendar.YEAR));
			Util.debug("gcEndDate" + gcEndDate);
			List<StoreHoliday> storeHolidaysToReturn = new ArrayList<StoreHoliday>();
			if(l!=null){
				for(StoreHoliday sh:l){
					Date middleDate = dateFormat.parse(sh.getId().getMonth() +"/"+sh.getId().getDay()+"/"+sh.getId().getYear());
					Util.debug("middleDate" + dateFormat.parse(sh.getId().getMonth() +"/"+sh.getId().getDay()+"/"+sh.getId().getYear()));
					if(middleDate.after(gcStart.getTime()) && middleDate.before(gcEndDate)){
						storeHolidaysToReturn.add(sh);
					}
				}		
			}
		
			return storeHolidaysToReturn;		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<StoreHoliday> getOrderedStoreHolidaysBetweenDates(Date start,Date end){
		//TODO figure out how to fetch these by month, day, year strings
		if(start == null || end==null)
			return null;
		String startYear,endYear;
		SimpleDateFormat yearSDF = new SimpleDateFormat("yyyy");
		startYear = yearSDF.format(start);
		endYear = yearSDF.format(end);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		GregorianCalendar gcStart = new GregorianCalendar();
		gcStart.setTime(start);
		gcStart.add(Calendar.DATE, -1);
		
		GregorianCalendar gcEnd = new GregorianCalendar();
		gcEnd.setTime(end);
		//12AM end day
		gcEnd.set(Calendar.HOUR_OF_DAY, 0);
		gcEnd.set(Calendar.MINUTE, 0);
		gcEnd.set(Calendar.SECOND, 0);
		gcEnd.set(Calendar.MILLISECOND, 0);
		gcEnd.set(Calendar.AM_PM, Calendar.AM);
		
		List<StoreHoliday> l = getHibernateTemplate().find("from StoreHoliday sm where (sm.id.year >= "+startYear+" and  sm.id.year <= "+endYear+") order by year, month, day");
		try{
			//doing this to get to all zeros.
			Date gcEndDate = dateFormat.parse( (gcEnd.get(Calendar.MONTH) + 1) +"/"+(gcEnd.get(Calendar.DAY_OF_MONTH))+"/"+gcEnd.get(Calendar.YEAR));
			Util.debug("gcEndDate" + gcEndDate);
			List<StoreHoliday> storeHolidaysToReturn = new ArrayList<StoreHoliday>();
			if(l!=null){
				for(StoreHoliday sh:l){
					Date middleDate = dateFormat.parse(sh.getId().getMonth() +"/"+sh.getId().getDay()+"/"+sh.getId().getYear());
					//Util.debug("middleDate" + dateFormat.parse(sh.getId().getMonth() +"/"+sh.getId().getDay()+"/"+sh.getId().getYear()));
					if(middleDate.after(gcStart.getTime()) && middleDate.before(gcEndDate)){
						storeHolidaysToReturn.add(sh);
					}
				}		
			}
		
			return storeHolidaysToReturn;		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public StoreHolidayHour getStoreHolidayHour(Long storeNumber, Long holidayId){
		List l = getHibernateTemplate().find("from StoreHolidayHour sm where sm.storeHolidayHourId.storeNumber=? and sm.storeHolidayHourId.holidayId=?", new Object[]{storeNumber,holidayId});
		StoreHolidayHour sm = null;
		if(l != null && l.size() > 0){
			Iterator i = l.iterator();
			sm = (StoreHolidayHour)i.next();			
		}
		return sm;		
	}

	@Override
	public String checkStoreReasonCode(long storeNumber) {
		Session s = getSession();
		String sql = "select r.REASON_DESC as reason from store s join store_tempclose_reasons r on s.reason_id = r.reason_id where s.store_number = ?";
		String reason = null;
		try {
			List<String> l = s.createSQLQuery(sql).addScalar("reason", Hibernate.STRING).setLong(0, storeNumber).list();
		
			if(l!=null&&l.size()>0){
				reason = l.get(0);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}	
		finally {
		
			//s.close();
			this.releaseSession(s);
		}
		return reason;
	}
}
