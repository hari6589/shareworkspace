package com.bfrc.framework.dao.store;

import java.text.DecimalFormat;
import java.util.List;

import com.bfrc.Config;
import com.bfrc.LocatorConfig;
import com.bfrc.framework.businessdata.BusinessOperatorSupport;
import com.bfrc.framework.dao.LocatorDAO;
import com.bfrc.pojo.store.BfrcStoreMap;
import com.bfrc.pojo.store.Map;
import com.bfrc.pojo.store.Store;
import com.bfrc.storelocator.util.LocatorUtil;

/**
 * @author Ed Knutson
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LocatorOperator extends BusinessOperatorSupport {
	private LocatorDAO locatorDAO;
	private MapOperator mapOperator;
	private GeocodeOperator geocodeOperator;
	protected static DecimalFormat df = new DecimalFormat("0.#");
	protected static DecimalFormat tf = new DecimalFormat("0");

	public static String formatTime(double time) {
		String out = "<1";
		if(time > 1.0)
			out = tf.format(time);
		return out;
	}
	
	public long getRequestId() {
		return this.locatorDAO.getNextId();
	}
	
	public Float[] geoLocationWithBing(long id, String app, String address, String city, String state, String zip, String remoteIP) {
		Float[] geocode = null;
		try {		
			//WI-1977: replace google geocoding calls with bing geolocation API calls
			geocode = this.geocodeOperator.geoLocationWithBing(id, app, address, city, state, zip, remoteIP);
		}
		catch (Exception ex) {
			System.err.println("GOOGLE GEOCODING ERROR: "+ex.getMessage());
		}		
		return geocode;
	}

	public long[][] getClosestStores(Float[] location, String app) {
		return getClosestStores(location, app, getConfig().getLocator().getRadius().intValue());
	}
	public long[][] getClosestStores(Float[] location, String app, int radius) {
		return getClosestStores(location, app, true, radius);
	}
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius) {
		return getClosestStores(location, app, licensee, radius, false);
	}
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing) {
		return getClosestStores(location, app, licensee, radius, pricing, null);
	}
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state) {
		return getClosestStores(location, app, licensee, radius, pricing, state, false);
	}
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, null);
	}
	
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, 10);
	}
	
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, false);
	}
	
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, ignoreActiveFlag, null);
	}
	
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, ignoreActiveFlag, p, false);
	}
	
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p, boolean isDefaultStore) {
		return getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, ignoreActiveFlag, p, isDefaultStore, false);
	}
	
	public long[][] getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p, boolean isDefaultStore, boolean ignoreStoreTypes) {
		long startTime = System.currentTimeMillis();
		List stores = null;
		//includes temporary closed stores to search results if current app is FCAC and should not be default store
		if(Config.FCAC.equals(app)) {
			if(isDefaultStore)
				stores = locatorDAO.getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, false, p);
			else
				stores = locatorDAO.getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, true, p);
		}else if((Config.TP.equals(app) && !ignoreStoreTypes) || Config.ET.equals(app) || Config.BFRC.equals(app) || ignoreActiveFlag) {
			stores = locatorDAO.getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, true);
		}
		else{
			stores = locatorDAO.getClosestStores(location, app, licensee, radius, pricing, state, full, licenseeType, count, false, p, isDefaultStore, ignoreStoreTypes);
		}
		
		if(stores == null || stores.size() == 0)
			return null;

		int len = stores.size();
		long[][] out = new long[len][3];
		if(location == null || location[0] == null || location[1] == null)
			for(int i=0; i<len; i++) {
				out[i][0] = ((Store)stores.get(i)).getStoreNumber().intValue();
				out[i][1] = 0;
			}
		else for(int i=0; i<len; i++) {
			Object[] row = (Object[])stores.get(i); 
			out[i][0] = ((Integer)row[0]).intValue();
			out[i][1] = ((Integer)row[1]).intValue();
		}
		out[0][2] = (int)(System.currentTimeMillis() - startTime);
		return out;
	}

	public void logStoreQuery(long id, String app, long interval) {
		//this.locatorDAO.log(id, app, "127.0.0.1", "stores", interval);
	}
	/**
	 * @see com.bfrc.framework.Operator#operate(Object)
	 */
	public Object operate(Object o) throws Exception {
		LocatorConfig locatorConfig = getConfig().getLocator();
		java.util.Map m = (java.util.Map)o;
		long id = 0;
		Long idFromParam = (Long)m.get("id");
		if(idFromParam != null)
			id = idFromParam.longValue();
		String app = (String)m.get("app");
		String street = (String)m.get("street");
		String city = (String)m.get("city");
		String state = (String)m.get("state");
		String zip = (String)m.get("zip");
		if(!com.bfrc.framework.util.ServerUtil.isNullOrEmpty(zip)){
			if(!com.bfrc.framework.util.Util.isValidZipCode(zip)){
				throw new Exception("Invalid Zip Code");
			}
			
		}
		String remoteIp = (String)m.get("remoteIP");
		String fetchMap = (String)m.get("fetchMap");
		String operation = (String)m.get(OPERATION);
		Integer zoomStore = (Integer)m.get("zoomStore");
		Float[] location = (Float[])m.get("location");
		String direction = (String)m.get("direction");
		Store[] stores = (Store[])m.get("stores");
		boolean pricing = m.containsKey("pricing");
		Boolean full = (Boolean)m.get("full");
		if(full == null)
			full = new Boolean(false);
		Boolean licensee = (Boolean)m.get("licensee");
		if(licensee == null)
			licensee = new Boolean(true);
		String licenseeType = (String)m.get("licenseeType");
		Map map = (Map)m.get("map");
		Map currMap = (Map)m.get("currMap");
		Store store = (Store)m.get("store");
		//Util.debug("xxxxxxxxxxxxxxxxxxx\t\tm.get(\"5starPrimary\")"+m.get("5starPrimary"));
		if(m.get("partner") != null) {
			app = "partner";
			if(pricing)
				app += "-pricing";
		}else if(m.get("5starPrimary") != null) {
			app = "5starPrimary";
			if(pricing)
				app += "-pricing";
		}
		int zoomLevel = getConfig().getLocator().getZoomLevel().intValue();
		Integer zoomLevelFromParam = (Integer)m.get("zoom");
		if(zoomLevelFromParam != null)
			zoomLevel = zoomLevelFromParam.intValue();
		int radius = getConfig().getLocator().getRadius().intValue();
		Integer radiusFromParam = (Integer)m.get("radius");
		if(radiusFromParam != null)
			radius = radiusFromParam.intValue();
		if(radius < 0)
			radius = locatorConfig.getRadius().intValue();
		int offset = 0;
		Integer offsetFromParam = (Integer)m.get("offset");
		if(offsetFromParam != null)
			offset = offsetFromParam.intValue();
		Integer displayCountFromParam = (Integer)m.get("displayCount");
		int displayCount = 0;
		if(displayCountFromParam != null)
			displayCount = displayCountFromParam.intValue();
		long interval = 0;
		Long intervalFromParam = (Long)m.get("interval");
		if(intervalFromParam != null)
			interval = intervalFromParam.longValue();
		Boolean ignoreActiveFlag = (Boolean)m.get("ignoreActiveFlag");
		if("getRequestId".equals(operation))
			m.put(RESULT, new Long(getRequestId()));
		else if("getMap".equals(operation))
			m.put(RESULT, this.mapOperator.getMap(id, app, street, city, state, zip, location, stores, offset, displayCount, remoteIp,
					fetchMap, zoomLevel, radius, currMap, direction, zoomStore));
		else if("getDirections".equals(operation))
			m.put(RESULT, this.mapOperator.getDirections(id, app, map, store, remoteIp));
		//else if("geocode".equals(operation)  && (Boolean.TRUE.equals(locatorConfig.getGeocodeWithGoogle())))
		//	m.put(RESULT, this.geocodeOperator.geocodeWithGoogle(id, app, street, city, state, zip, remoteIp));
		//WI-1977: replace google geocoding calls with bing geolocation API calls
		else if("geocode".equals(operation))
			m.put(RESULT, this.geocodeOperator.geoLocationWithBing(id, app, street, city, state, zip, remoteIp));
		else if("logStoreQuery".equals(operation))
			logStoreQuery(id, app, interval);
		else if("getClosestStores".equals(operation))
			m.put(RESULT, getClosestStores(location, app, licensee.booleanValue(), radius, pricing, state, full.booleanValue(), licenseeType, 10, ignoreActiveFlag));
		else if("getStoreHTML".equals(operation))
			m.put(RESULT, getStoreHTML(stores, app));
		return SUCCESS;
	}

	/**
	 * Returns the locatorDAO.
	 * @return LocatorDAO
	 */
	public LocatorDAO getLocatorDAO() {
		return this.locatorDAO;
	}

	/**
	 * Sets the locatorDAO.
	 * @param locatorDAO The locatorDAO to set
	 */
	public void setLocatorDAO(LocatorDAO locatorDAO) {
		this.locatorDAO = locatorDAO;
	}

	public String[] getStoreHTML(Store[] stores, String app) {
		int len = stores.length;
		String[] out = new String[len];
		Config config = this.locatorDAO.getConfig();
		String site = config.getSiteName();
		String siteName = "";
		for(int i=0; i<len; i++) {
			Store store = stores[i];
			String number = store.getStoreNumber().toString();
			String paddedNumber = "";
			for(int j=0; j<6-number.length(); j++)
				paddedNumber += "0";
			paddedNumber += number;
			// add store hour -- Wei Wang 03/27/06
			List storeHours = this.locatorDAO.getStoreHour(store.getNumber());
			String hours = LocatorUtil.getStoreHourHTML(storeHours);
//			String hours = com.mastercareusa.storepicker.Store.getLocatorHTML(store.getNumber());
			out[i] = "<td valign=\"top\" align=\"left\"><nobr>" + store.getStoreName() + "</nobr><br />";
			if(Config.FCAC.equals(site))
				out[i] += "#" + paddedNumber + "<br />";
			out[i] += store.getAddress() + "<br />"
			 + store.getCity() + ", " + store.getState() + " " + store.getZip() + "<br />"
			 + store.getPhone();
			if(Config.FANEUIL.equals(app))
				out[i] += "<br /><b>" + this.locatorDAO.getStoreType(store) + "</b>";
			if(!Config.FANEUIL.equalsIgnoreCase(app) && !"partner".equalsIgnoreCase(app) && !Config.PPS.equalsIgnoreCase(app)
					&& !Config.BFRC.equalsIgnoreCase(app)
					&& store.getOnlineAppointmentActiveFlag().intValue() == 1) {
				if(Config.FCFC.equals(site)) {
					String type = store.getType().trim();
					java.util.Map storeMap = this.locatorDAO.getStoreDAO().getFullStoreMap(config);
					BfrcStoreMap m = (BfrcStoreMap)storeMap.get(type.trim());
					if(m.getParentType() != null)
						m = (BfrcStoreMap)storeMap.get(m.getParentType().trim());
					type = m.getStoreType().trim();
					if(Config.FCAC.equalsIgnoreCase(type))
						siteName = "http://www.firestonecompleteautocare.com";
					else if(Config.TP.equalsIgnoreCase(type))
						siteName = "http://www.tiresplus.com";
					else if(Config.HT.equalsIgnoreCase(type))
						siteName = "http://www.hibdontire.com";
					else if(Config.ET.equalsIgnoreCase(type))
						siteName = "http://www.experttire.com";
				}	
				out[i] += "<br /><a target=\"_top\" href=\"" + siteName + "/appointment/selectStore.jsp?storeNumber=" + number + "\">Make an appointment</a>";
			}
			if(Config.FANEUIL.equalsIgnoreCase(app))
				out[i] += "<br /><a href=\"" + getConfig().getLocator().getFaneuilUrl() + number + "\">Select store</a>";
			out[i] += "</td>";
			out[i] += "<td valign=\"top\" align=\"left\">" + hours + "</td>";
		}
		return out;
	}

	public GeocodeOperator getGeocodeOperator() {
		return this.geocodeOperator;
	}

	public void setGeocodeOperator(GeocodeOperator geocodeOperator) {
		this.geocodeOperator = geocodeOperator;
	}

	public MapOperator getMapOperator() {
		return this.mapOperator;
	}

	public void setMapOperator(MapOperator mapOperator) {
		this.mapOperator = mapOperator;
	}

}
