package com.bfrc.framework.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.bfrc.Config;
import com.bfrc.framework.dao.ZipCodeDataDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreSearchResult;
import com.bfrc.storelocator.util.LocatorUtil;

public class StoreSearchUtils {
	public static final int STORES_TO_SHOW_DEFAULT	= 30;
	public static final int STORES_TO_SHOW_TP		= 35;
	
	public static StoreSearchResult searchStores(HttpServletRequest request,
			String queryLocation, Integer radius, int storesToShow) {
		if (Util.isValidZipCode(queryLocation))
			return searchStores(request, null, null, null, null, queryLocation,
					radius, storesToShow);
		else {
			if (queryLocation == null)
				queryLocation = "";
			ServletContext application = request.getSession()
					.getServletContext();
			LocatorOperator locator = (LocatorOperator) Config.locate(
					application, "locator");
			map.States stateList = locator.getGeocodeOperator().getStates();
			if (queryLocation.trim().length() == 2
					&& stateList
							.containsKey(queryLocation.trim().toUpperCase())) {
				queryLocation = queryLocation.trim().toUpperCase();
				return searchStores(request, null, null, null, queryLocation,
						null, radius, storesToShow);
			} else {
				Iterator it = stateList.entrySet().iterator();
				java.util.Map.Entry entry = null;
				while (it.hasNext()) {
					entry = (java.util.Map.Entry) it.next();
					if (queryLocation.trim().equalsIgnoreCase(
							(String) entry.getValue())) {
						queryLocation = (String) entry.getKey();
						return searchStores(request, null, null, null,
								queryLocation, null, radius, storesToShow);
					}
				}
			}
			return searchStores(request, null, queryLocation, null, null, null,
					radius, storesToShow);
		}
	}

	public static StoreSearchResult searchStores(HttpServletRequest request,
			String queryLocation) {
		return searchStores(request, null, queryLocation, null, null, null,
				null, STORES_TO_SHOW_DEFAULT);
	}

	public static StoreSearchResult searchStoresByGeoPoint(
			HttpServletRequest request, String geoPoint, Integer radius,
			int storesToShow) {
		return searchStores(request, geoPoint, null, null, null, null, radius,
				storesToShow);
	}

	public static StoreSearchResult searchStoresByGeoPoint(
			HttpServletRequest request, String geoPoint) {
		return searchStores(request, geoPoint, null, null, null, null, null, STORES_TO_SHOW_DEFAULT);
	}

	public static StoreSearchResult searchStores(HttpServletRequest request,
			String address, String city, String state, String zip) {
		return searchStores(request, null, address, city, state, zip, null, STORES_TO_SHOW_DEFAULT);
	}
	
	public static StoreSearchResult searchStores(HttpServletRequest request,
			String address, String city, String state, String zip, int storesToShow) {
		return searchStores(request, null, address, city, state, zip, null, storesToShow);
	}

	public static StoreSearchResult searchStores(HttpServletRequest request,
			String geoPoint, String address, String city, String state,
			String zip, Integer radius, int storesToShow) {
		StoreSearchResult result = new StoreSearchResult();
		result.setAddress(address);
		result.setCity(city);
		result.setState(state);
		result.setZip(zip);
		result.setGeoPoint(geoPoint);
		ServletContext application = request.getSession().getServletContext();
		boolean hasError = false;
		long startTime = System.currentTimeMillis();
		LocatorOperator locator = (LocatorOperator) Config.locate(application,
				"locator");
		result.setLocator(locator);
		String errorCode = null;
		if (ServerUtil.isNullOrEmpty(address) && ServerUtil.isNullOrEmpty(city)
				&& ServerUtil.isNullOrEmpty(state)
				&& ServerUtil.isNullOrEmpty(zip)
				&& ServerUtil.isNullOrEmpty(geoPoint)) {
			errorCode = "missingall";
		}

		result.setRadius(radius);
		result.setStoresToShow(storesToShow);
		boolean isByState = result.isByState();
		String app = locator.getConfig().getSiteName();
		String remoteIP = request.getRemoteAddr();
		Float[] location = null;

		long requestId = locator.getRequestId();
		if (result.isValidGeoPoint()) {
			location = result.getPoint();
		} else {
			location = locator.geoLocationWithBing(requestId, app, address, city,
					state, zip, remoteIP);
		}

		result.setLocation(location);
		// Get the list of stores
		long[][] storeArray = null;
		if (isByState)
			// location, app, licensee, radius, pricing, state
			storeArray = locator.getClosestStores(null, app, true,
					result.getRadius(), false, state);
		else {
			// location, app, licensee, radius, pricing, state, full,
			// licenseeType, count
			storeArray = locator.getClosestStores(location, app, true,
					result.getRadius(), false, state, false, null,
					result.getStoresToShow());
		}
		Store[] stores = null;
		long[] distances = null;
		List<Store> lstores = new ArrayList<Store>();

		// Did we get a list of store IDs back?
		if (storeArray != null && storeArray.length > 0) {
			// Go get the actual stores
			stores = locator.getLocatorDAO().getStores(storeArray);

			// Build the distances array and set the store hours
			if (stores != null && stores.length > 0) {
				distances = new long[0];
				if (stores != null && stores.length > 0) {
					distances = new long[stores.length];
					for (int i = 0; i < stores.length; i++) {
						lstores.add(stores[i]);
						stores[i].setStoreHour(LocatorUtil.getStoreHourHTML(
								locator.getLocatorDAO().getStoreHour(
										stores[i].getNumber()), true));
						distances[i] = storeArray[i][1];
					}
					locator.getLocatorDAO().setHolidayHours(lstores);
				}
			}
			result.setStores(stores);
			result.setDistances(distances);
		} else {
			errorCode = "nostore";
		}
		result.setErrorCode(errorCode);
		return result;
	}
	
	public static boolean checkZipCode(HttpServletRequest request, String zip){
		return checkZipCode(request.getSession().getServletContext(), zip);
	}

	public static boolean checkZipCode(ServletContext context, String zip){
		com.bfrc.framework.dao.store.GeocodeOperator geocodeOperator
        = (com.bfrc.framework.dao.store.GeocodeOperator)com.bfrc.Config.locate(context, "geocodeOperator");
		
		ZipCodeDataDAO zipCodeDataDAO = (ZipCodeDataDAO) com.bfrc.Config.locate(context, "ZipCodeDataDAO");
		
		if(com.bfrc.framework.util.StringUtils.isNullOrEmpty(zip)){
		    return false;
		}
		if(!com.bfrc.framework.util.Util.isValidZipCode(zip)){
			return false;
		}
		try{
			// Check the postal service table first
			String state = zipCodeDataDAO.getStateByZip(zip);
			if (state != null && !state.isEmpty()) {
				return true;
			}
			// Then check the cache
			if (geocodeOperator.isZipCodeInCache(zip)) {
				return true;
			} else {
				// If all else fails, geocode
				//WI-1977: replace google geocoding calls with bing api calls
				//java.util.Map geoData = geocodeOperator.getGoogleGeoData(null, null, null, zip);
				java.util.Map geoData = geocodeOperator.getBingGeoLocationData(null, null, null, zip);
				if(!"200".equals(geoData.get("statusCode"))){
					return false;
				}
			}
		}catch(Exception ex){
			return false;
		}
		return true;
	}
	
	public static boolean isHolidayStoreType(String type) {
		if(type == null) return false;
		return StoreSearchUtils.validHolidayStoreTypesToCheck.contains("|"+type.trim().toLowerCase()+"|");
	}
	
	public static boolean isHolidaySite(String siteName) {
		if(siteName == null) return false;
		return StoreSearchUtils.validHolidaySitesToCheck.contains("|"+siteName.trim().toLowerCase()+"|");
	}
	
	//-- exception list for store holiday hours --//
	public static String holidayDateToCheck="20131129";
	public static String holidayToCheckName="Independence Day";
	public static String validHolidayStoreTypesToCheck="|tp|tpl|ww|htp|fcac|afd|et|";
	public static String validHolidaySitesToCheck="|tp|fcac|et|";
	public static List<String> holidayHoursExceptionStoreNumberList;
	static {
		holidayHoursExceptionStoreNumberList = new ArrayList<String>();
		holidayHoursExceptionStoreNumberList.add("1228");
		holidayHoursExceptionStoreNumberList.add("1716");
		holidayHoursExceptionStoreNumberList.add("2569");
		holidayHoursExceptionStoreNumberList.add("2593");
		holidayHoursExceptionStoreNumberList.add("3700");
		holidayHoursExceptionStoreNumberList.add("3921");
		holidayHoursExceptionStoreNumberList.add("3964");
		holidayHoursExceptionStoreNumberList.add("4332");
		holidayHoursExceptionStoreNumberList.add("5126");
		holidayHoursExceptionStoreNumberList.add("5223");
		holidayHoursExceptionStoreNumberList.add("5835");
		holidayHoursExceptionStoreNumberList.add("6114");
		holidayHoursExceptionStoreNumberList.add("6157");
		holidayHoursExceptionStoreNumberList.add("6254");
		holidayHoursExceptionStoreNumberList.add("6378");
		holidayHoursExceptionStoreNumberList.add("6548");
		holidayHoursExceptionStoreNumberList.add("7110");
		holidayHoursExceptionStoreNumberList.add("10340");
		holidayHoursExceptionStoreNumberList.add("10693");
		holidayHoursExceptionStoreNumberList.add("10804");
		holidayHoursExceptionStoreNumberList.add("10820");
		holidayHoursExceptionStoreNumberList.add("10855");
		holidayHoursExceptionStoreNumberList.add("10898");
		holidayHoursExceptionStoreNumberList.add("10901");
		holidayHoursExceptionStoreNumberList.add("10944");
		holidayHoursExceptionStoreNumberList.add("10979");
		holidayHoursExceptionStoreNumberList.add("11037");
		holidayHoursExceptionStoreNumberList.add("11053");
		holidayHoursExceptionStoreNumberList.add("11061");
		holidayHoursExceptionStoreNumberList.add("11088");
		holidayHoursExceptionStoreNumberList.add("11142");
		holidayHoursExceptionStoreNumberList.add("11150");
		holidayHoursExceptionStoreNumberList.add("11169");
		holidayHoursExceptionStoreNumberList.add("11231");
		holidayHoursExceptionStoreNumberList.add("11258");
		holidayHoursExceptionStoreNumberList.add("11274");
		holidayHoursExceptionStoreNumberList.add("11304");
		holidayHoursExceptionStoreNumberList.add("11320");
		holidayHoursExceptionStoreNumberList.add("11428");
		holidayHoursExceptionStoreNumberList.add("11533");
		holidayHoursExceptionStoreNumberList.add("11576");
		holidayHoursExceptionStoreNumberList.add("11622");
		holidayHoursExceptionStoreNumberList.add("11657");
		holidayHoursExceptionStoreNumberList.add("11711");
		holidayHoursExceptionStoreNumberList.add("11770");
		holidayHoursExceptionStoreNumberList.add("11819");
		holidayHoursExceptionStoreNumberList.add("11894");
		holidayHoursExceptionStoreNumberList.add("11908");
		holidayHoursExceptionStoreNumberList.add("11932");
		holidayHoursExceptionStoreNumberList.add("11940");
		holidayHoursExceptionStoreNumberList.add("11959");
		holidayHoursExceptionStoreNumberList.add("11991");
		holidayHoursExceptionStoreNumberList.add("12025");
		holidayHoursExceptionStoreNumberList.add("12068");
		holidayHoursExceptionStoreNumberList.add("12076");
		holidayHoursExceptionStoreNumberList.add("12122");
		holidayHoursExceptionStoreNumberList.add("12238");
		holidayHoursExceptionStoreNumberList.add("12300");
		holidayHoursExceptionStoreNumberList.add("12351");
		holidayHoursExceptionStoreNumberList.add("12424");
		holidayHoursExceptionStoreNumberList.add("12432");
		holidayHoursExceptionStoreNumberList.add("12475");
		holidayHoursExceptionStoreNumberList.add("12483");
		holidayHoursExceptionStoreNumberList.add("12505");
		holidayHoursExceptionStoreNumberList.add("12548");
		holidayHoursExceptionStoreNumberList.add("12556");
		holidayHoursExceptionStoreNumberList.add("12580");
		holidayHoursExceptionStoreNumberList.add("12599");
		holidayHoursExceptionStoreNumberList.add("12637");
		holidayHoursExceptionStoreNumberList.add("12645");
		holidayHoursExceptionStoreNumberList.add("12688");
		holidayHoursExceptionStoreNumberList.add("12769");
		holidayHoursExceptionStoreNumberList.add("12785");
		holidayHoursExceptionStoreNumberList.add("12823");
		holidayHoursExceptionStoreNumberList.add("12858");
		holidayHoursExceptionStoreNumberList.add("12874");
		holidayHoursExceptionStoreNumberList.add("12882");
		holidayHoursExceptionStoreNumberList.add("13056");
		holidayHoursExceptionStoreNumberList.add("13064");
		holidayHoursExceptionStoreNumberList.add("13226");
		holidayHoursExceptionStoreNumberList.add("13307");
		holidayHoursExceptionStoreNumberList.add("13315");
		holidayHoursExceptionStoreNumberList.add("13374");
		holidayHoursExceptionStoreNumberList.add("13412");
		holidayHoursExceptionStoreNumberList.add("13420");
		holidayHoursExceptionStoreNumberList.add("13463");
		holidayHoursExceptionStoreNumberList.add("13498");
		holidayHoursExceptionStoreNumberList.add("13587");
		holidayHoursExceptionStoreNumberList.add("13757");
		holidayHoursExceptionStoreNumberList.add("13854");
		holidayHoursExceptionStoreNumberList.add("13889");
		holidayHoursExceptionStoreNumberList.add("14117");
		holidayHoursExceptionStoreNumberList.add("14524");
		holidayHoursExceptionStoreNumberList.add("14532");
		holidayHoursExceptionStoreNumberList.add("14540");
		holidayHoursExceptionStoreNumberList.add("14575");
		holidayHoursExceptionStoreNumberList.add("14710");
		holidayHoursExceptionStoreNumberList.add("14893");
		holidayHoursExceptionStoreNumberList.add("14990");
		holidayHoursExceptionStoreNumberList.add("15032");
		holidayHoursExceptionStoreNumberList.add("15180");
		holidayHoursExceptionStoreNumberList.add("15199");
		holidayHoursExceptionStoreNumberList.add("15458");
		holidayHoursExceptionStoreNumberList.add("15504");
		holidayHoursExceptionStoreNumberList.add("15539");
		holidayHoursExceptionStoreNumberList.add("15636");
		holidayHoursExceptionStoreNumberList.add("15644");
		holidayHoursExceptionStoreNumberList.add("15768");
		holidayHoursExceptionStoreNumberList.add("15814");
		holidayHoursExceptionStoreNumberList.add("15911");
		holidayHoursExceptionStoreNumberList.add("15946");
		holidayHoursExceptionStoreNumberList.add("15970");
		holidayHoursExceptionStoreNumberList.add("16020");
		holidayHoursExceptionStoreNumberList.add("16047");
		holidayHoursExceptionStoreNumberList.add("16128");
		holidayHoursExceptionStoreNumberList.add("16705");
		holidayHoursExceptionStoreNumberList.add("16721");
		holidayHoursExceptionStoreNumberList.add("16748");
		holidayHoursExceptionStoreNumberList.add("16861");
		holidayHoursExceptionStoreNumberList.add("17493");
		holidayHoursExceptionStoreNumberList.add("17523");
		holidayHoursExceptionStoreNumberList.add("17655");
		holidayHoursExceptionStoreNumberList.add("17663");
		holidayHoursExceptionStoreNumberList.add("17671");
		holidayHoursExceptionStoreNumberList.add("17701");
		holidayHoursExceptionStoreNumberList.add("17809");
		holidayHoursExceptionStoreNumberList.add("18031");
		holidayHoursExceptionStoreNumberList.add("18325");
		holidayHoursExceptionStoreNumberList.add("18406");
		holidayHoursExceptionStoreNumberList.add("18716");
		holidayHoursExceptionStoreNumberList.add("18732");
		holidayHoursExceptionStoreNumberList.add("18740");
		holidayHoursExceptionStoreNumberList.add("18856");
		holidayHoursExceptionStoreNumberList.add("18902");
		holidayHoursExceptionStoreNumberList.add("18945");
		holidayHoursExceptionStoreNumberList.add("19178");
		holidayHoursExceptionStoreNumberList.add("19445");
		holidayHoursExceptionStoreNumberList.add("19488");
		holidayHoursExceptionStoreNumberList.add("19631");
		holidayHoursExceptionStoreNumberList.add("19852");
		holidayHoursExceptionStoreNumberList.add("19992");
		holidayHoursExceptionStoreNumberList.add("20370");
		holidayHoursExceptionStoreNumberList.add("20389");
		holidayHoursExceptionStoreNumberList.add("20451");
		holidayHoursExceptionStoreNumberList.add("20516");
		holidayHoursExceptionStoreNumberList.add("20540");
		holidayHoursExceptionStoreNumberList.add("20591");
		holidayHoursExceptionStoreNumberList.add("20877");
		holidayHoursExceptionStoreNumberList.add("20974");
		holidayHoursExceptionStoreNumberList.add("21156");
		holidayHoursExceptionStoreNumberList.add("21199");
		holidayHoursExceptionStoreNumberList.add("21342");
		holidayHoursExceptionStoreNumberList.add("21458");
		holidayHoursExceptionStoreNumberList.add("21563");
		holidayHoursExceptionStoreNumberList.add("21652");
		holidayHoursExceptionStoreNumberList.add("21725");
		holidayHoursExceptionStoreNumberList.add("21784");
		holidayHoursExceptionStoreNumberList.add("21962");
		holidayHoursExceptionStoreNumberList.add("22071");
		holidayHoursExceptionStoreNumberList.add("22691");
		holidayHoursExceptionStoreNumberList.add("22705");
		holidayHoursExceptionStoreNumberList.add("22845");
		holidayHoursExceptionStoreNumberList.add("22888");
		holidayHoursExceptionStoreNumberList.add("22985");
		holidayHoursExceptionStoreNumberList.add("23116");
		holidayHoursExceptionStoreNumberList.add("23175");
		holidayHoursExceptionStoreNumberList.add("23183");
		holidayHoursExceptionStoreNumberList.add("23213");
		holidayHoursExceptionStoreNumberList.add("23221");
		holidayHoursExceptionStoreNumberList.add("23248");
		holidayHoursExceptionStoreNumberList.add("23264");
		holidayHoursExceptionStoreNumberList.add("23493");
		holidayHoursExceptionStoreNumberList.add("23574");
		holidayHoursExceptionStoreNumberList.add("23620");
		holidayHoursExceptionStoreNumberList.add("23639");
		holidayHoursExceptionStoreNumberList.add("23841");
		holidayHoursExceptionStoreNumberList.add("23884");
		holidayHoursExceptionStoreNumberList.add("24716");
		holidayHoursExceptionStoreNumberList.add("24732");
		holidayHoursExceptionStoreNumberList.add("24856");
		holidayHoursExceptionStoreNumberList.add("24899");
		holidayHoursExceptionStoreNumberList.add("24929");
		holidayHoursExceptionStoreNumberList.add("24953");
		holidayHoursExceptionStoreNumberList.add("25054");
		holidayHoursExceptionStoreNumberList.add("25224");
		holidayHoursExceptionStoreNumberList.add("25259");
		holidayHoursExceptionStoreNumberList.add("25267");
		holidayHoursExceptionStoreNumberList.add("25380");
		holidayHoursExceptionStoreNumberList.add("25550");
		holidayHoursExceptionStoreNumberList.add("25984");
		holidayHoursExceptionStoreNumberList.add("26093");
		holidayHoursExceptionStoreNumberList.add("26379");
		holidayHoursExceptionStoreNumberList.add("26603");
		holidayHoursExceptionStoreNumberList.add("27022");
		holidayHoursExceptionStoreNumberList.add("27049");
		holidayHoursExceptionStoreNumberList.add("28258");
		holidayHoursExceptionStoreNumberList.add("28355");
		holidayHoursExceptionStoreNumberList.add("28460");
		holidayHoursExceptionStoreNumberList.add("28487");
		holidayHoursExceptionStoreNumberList.add("28533");
		holidayHoursExceptionStoreNumberList.add("28541");
		holidayHoursExceptionStoreNumberList.add("28746");
		holidayHoursExceptionStoreNumberList.add("28800");
		holidayHoursExceptionStoreNumberList.add("29106");
		holidayHoursExceptionStoreNumberList.add("29602");
		holidayHoursExceptionStoreNumberList.add("29629");
		holidayHoursExceptionStoreNumberList.add("29734");
		holidayHoursExceptionStoreNumberList.add("29777");
		holidayHoursExceptionStoreNumberList.add("29815");
		holidayHoursExceptionStoreNumberList.add("29874");
		holidayHoursExceptionStoreNumberList.add("29904");
		holidayHoursExceptionStoreNumberList.add("29947");
		holidayHoursExceptionStoreNumberList.add("29971");
		holidayHoursExceptionStoreNumberList.add("267356");
		holidayHoursExceptionStoreNumberList.add("267433");
		holidayHoursExceptionStoreNumberList.add("267434");
		holidayHoursExceptionStoreNumberList.add("267487");
		holidayHoursExceptionStoreNumberList.add("286710");
		holidayHoursExceptionStoreNumberList.add("289566");
		holidayHoursExceptionStoreNumberList.add("301700");
		holidayHoursExceptionStoreNumberList.add("301702");
		holidayHoursExceptionStoreNumberList.add("306091");
		holidayHoursExceptionStoreNumberList.add("307884");
		holidayHoursExceptionStoreNumberList.add("308529");
		holidayHoursExceptionStoreNumberList.add("314640");
		holidayHoursExceptionStoreNumberList.add("314642");
		holidayHoursExceptionStoreNumberList.add("317134");
		holidayHoursExceptionStoreNumberList.add("321260");
		holidayHoursExceptionStoreNumberList.add("321277");
		holidayHoursExceptionStoreNumberList.add("321305");
		holidayHoursExceptionStoreNumberList.add("325029");
		holidayHoursExceptionStoreNumberList.add("325547");
		holidayHoursExceptionStoreNumberList.add("328305");
		holidayHoursExceptionStoreNumberList.add("328315");
		holidayHoursExceptionStoreNumberList.add("329314");
		holidayHoursExceptionStoreNumberList.add("329316");
		holidayHoursExceptionStoreNumberList.add("330294");
		holidayHoursExceptionStoreNumberList.add("330295");
		holidayHoursExceptionStoreNumberList.add("343425");
		holidayHoursExceptionStoreNumberList.add("344698");
		holidayHoursExceptionStoreNumberList.add("344700");
		holidayHoursExceptionStoreNumberList.add("347181");
		holidayHoursExceptionStoreNumberList.add("349492");
		holidayHoursExceptionStoreNumberList.add("351021");
		holidayHoursExceptionStoreNumberList.add("352043");
		holidayHoursExceptionStoreNumberList.add("352686");
		holidayHoursExceptionStoreNumberList.add("355332");
		holidayHoursExceptionStoreNumberList.add("355336");
		holidayHoursExceptionStoreNumberList.add("355337");
		holidayHoursExceptionStoreNumberList.add("356288");
		holidayHoursExceptionStoreNumberList.add("356612");
		holidayHoursExceptionStoreNumberList.add("356720");
		holidayHoursExceptionStoreNumberList.add("357203");
		holidayHoursExceptionStoreNumberList.add("554553");
		holidayHoursExceptionStoreNumberList.add("591440");
		holidayHoursExceptionStoreNumberList.add("592811");
		holidayHoursExceptionStoreNumberList.add("595039");
		holidayHoursExceptionStoreNumberList.add("595055");
		holidayHoursExceptionStoreNumberList.add("595179");
		holidayHoursExceptionStoreNumberList.add("595241");
		holidayHoursExceptionStoreNumberList.add("595268");
		holidayHoursExceptionStoreNumberList.add("595306");
		holidayHoursExceptionStoreNumberList.add("595322");
		holidayHoursExceptionStoreNumberList.add("595365");
		holidayHoursExceptionStoreNumberList.add("595381");
		holidayHoursExceptionStoreNumberList.add("595403");
		holidayHoursExceptionStoreNumberList.add("595535");
		holidayHoursExceptionStoreNumberList.add("595543");
		holidayHoursExceptionStoreNumberList.add("595586");
		holidayHoursExceptionStoreNumberList.add("595594");
		holidayHoursExceptionStoreNumberList.add("598763");
		holidayHoursExceptionStoreNumberList.add("598860");
		holidayHoursExceptionStoreNumberList.add("599255");
		holidayHoursExceptionStoreNumberList.add("599611");
		holidayHoursExceptionStoreNumberList.add("650137");
		holidayHoursExceptionStoreNumberList.add("650145");
		holidayHoursExceptionStoreNumberList.add("650153");
		holidayHoursExceptionStoreNumberList.add("650196");
		holidayHoursExceptionStoreNumberList.add("655643");
		holidayHoursExceptionStoreNumberList.add("655678");
		//-- expert tire --//
		holidayHoursExceptionStoreNumberList.add("16276");
		holidayHoursExceptionStoreNumberList.add("16527");
		holidayHoursExceptionStoreNumberList.add("27030");
		holidayHoursExceptionStoreNumberList.add("655686");
		holidayHoursExceptionStoreNumberList.add("655694");
		holidayHoursExceptionStoreNumberList.add("655708");
		holidayHoursExceptionStoreNumberList.add("1074");
	}
	
	public static String getBrandStoreTypesClause(String brand){
		if(brand == null)
			return "";
		//-- add TPL to the list and just for TP, NO AFD for FCAC --//
		String cons  = "( s.STORE_TYPE = '"+brand+"' ";
		if("tp".equalsIgnoreCase(brand))
			cons +=" or s.STORE_TYPE ='TPL' ";
		if("fcac".equalsIgnoreCase(brand))
			cons +=" or s.STORE_TYPE ='MVAN' ";
		cons +=" ) ";
		return cons;
	
	}
}