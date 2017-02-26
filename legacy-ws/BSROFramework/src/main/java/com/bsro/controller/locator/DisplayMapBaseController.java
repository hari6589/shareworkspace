/**
 * 
 */
package com.bsro.controller.locator;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringEscapeUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import com.bsro.springframework.propertyeditor.ReplaceAllEditor;

import com.bfrc.Config;
import com.bfrc.security.Encode;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.framework.util.StoreSearchUtils;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.dao.store.GeocodeOperator;
import com.bfrc.framework.dao.StoreDAO;
import com.bsro.service.store.StoreService;
import com.bsro.service.location.LocationService;
import com.bsro.service.time.TimeZoneDstService;
import com.bfrc.pojo.store.StoreSearch;
import com.bfrc.pojo.store.Store;

public class DisplayMapBaseController {
	private final Log logger = LogFactory.getLog(DisplayMapBaseController.class);
	
	protected StoreDAO storeDAO;
	protected StoreService storeService;
	protected LocationService locationService;
	protected TimeZoneDstService timeZoneDstService;
	protected LocatorOperator locator;
	protected Config thisConfig;
	protected GeocodeOperator geocodeOperator;
	
	protected String indexView;
	protected String defaultView;
	protected String displayMapView;
	protected String getDirectionsView;
	protected String completeAddressView;
	protected String errorView;
	
	public StoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}

	public LocatorOperator getLocator() {
		return locator;
	}

	public void setLocator(LocatorOperator locator) {
		this.locator = locator;
	}
	
	public Config getThisConfig() {
		return thisConfig;
	}

	public void setThisConfig(Config thisConfig) {
		this.thisConfig = thisConfig;
	}
	
	public GeocodeOperator getGeocodeOperator() {
		return geocodeOperator;
	}
	
	public void setGeocodeOperator(GeocodeOperator geocodeOperator)
	{
		this.geocodeOperator = geocodeOperator;
	}
	
	public StoreDAO getStoreDAO() {
		return storeDAO;
	}
	
	public void setStoreDAO(StoreDAO storeDAO)
	{
		this.storeDAO = storeDAO;
	}
	
	public LocationService getLocationService() {
		return locationService;
	}
	
	public void setLocationService(LocationService locationService)
	{
		this.locationService = locationService;
	}
	
	public TimeZoneDstService getTimeZoneDstService() {
		return timeZoneDstService;
	}
	
	public void setTimeZoneDstService(TimeZoneDstService timeZoneDstService)
	{
		this.timeZoneDstService = timeZoneDstService;
	}
	
	public String getIndexView() {
		return indexView;
	}

	public void setIndexView(String indexPage) {
		this.indexView = indexPage;
	}
	
	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}
	
	public String getDiplayMapView() {
		return displayMapView;
	}
	
	public void setDiplayMapView(String displayMapView) {
		this.displayMapView = displayMapView;
	}
	
	public String getDirectionsView() {
		return getDirectionsView;
	}
	
	public void setDirectionsView(String getDirectionsView) {
		this.getDirectionsView = getDirectionsView;
	}
	
	public String getCompleteAddressView() {
		return completeAddressView;
	}
	
	public void setCompleteAddressView(String completeAddressView) {
		this.completeAddressView = completeAddressView;
	}
	
	public String getErrorView() {
		return errorView;
	}
	
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}
	
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.registerCustomEditor(String.class, new ReplaceAllEditor(false, "&#39;", "'"));
	}
	
	public String locateIndex(HttpServletRequest request, HttpSession session, Model model, String zip, String city, String state, String address) throws Exception {
		zip = Encode.encodeForJavaScript(zip);
		state = Encode.encodeForJavaScript(state);
		
		model.addAttribute("zip", zip != null ? zip : "");
		model.addAttribute("city", city != null ? city : "");
		model.addAttribute("state", state != null ? state : "");
		model.addAttribute("address", address != null ? address : "");
		
		return getIndexView();
	}
	
	public String displayMapExecute(Model model, HttpServletRequest request, HttpSession session, 
			String zip, String city, String state, String address, String fromUrl, String fromPage) {
		return displayMapExecute(model, request, session, zip, city, state, address, fromUrl, fromPage, null);
	}
	public String displayMapExecute(Model model, HttpServletRequest request, HttpSession session, 
				String zip, String city, String state, String address, String fromUrl, String fromPage, String p) {
		try {
			long startTime = System.currentTimeMillis();

			String app = thisConfig.getSiteName();
			String remoteIP = request.getRemoteAddr();
			long requestId = locator.getRequestId();
			boolean displayStores = false;
			Float[] location;

			// String navZip = (String)session.getAttribute("zip");
			// if((zip == null || zip.equals("")) && navZip != null &&
			// isValidZipCode(navZip))
			// zip = navZip;
			
			if(zip!=null|| !zip.equals("")){
				try{
					StoreSearch storeSearch = storeService.createStoreSearchObject(zip);
					zip = storeSearch.getZip();
					state = storeSearch.getState();
					city = storeSearch.getCity();
				} catch(Exception e) {
					/*do nothing*/
				}
			}
			
			String parameters = "";

			if (state == null || state.equals("")) {
				state = locationService.lookupStateWithStoresByZipCode(zip);
			}
			String geoPoint = (String) session.getAttribute("geoPoint");

			// If the only input value is the state, perform a state only lookup
			boolean isByState = false;
			if ((city == null || city.equals("")) && (address == null || address.equals("")) && (zip == null || zip.equals("")))
				isByState = true;

			// Create address description text
			String addressDescription = "";
			if (address != null && !address.equals("")) {
				addressDescription += address + ", ";
				parameters += "address=" + address;
			}
			if (city != null && !city.equals("")) {
				addressDescription += city + ", ";
				parameters += (parameters == "" ? "" : "&") + "city=" + city;
			}
			if (state != null && !state.equals("")) {
				addressDescription += state + " ";
				parameters += (parameters == "" ? "" : "&") + "state=" + state;
			}
			if (zip != null && !zip.equals("")) {
				addressDescription += zip;
				parameters += (parameters == "" ? "" : "&") + "zip=" + zip;
			}

			if (geoPoint == null || "".equals(geoPoint))
			{
				location = locator.geoLocationWithBing(requestId, app, address, city, state, zip, remoteIP);
	        }
	        else
	        {
	        	location = new Float[2];
	        	String[] pointString = geoPoint.split(",");
	        	location[1] = new Float(pointString[0]);
	        	location[0] = new Float(pointString[1]);	        	
	        }
			
			 //Correct zip code location issue
			if ((city == null || city.equals("")) && (address == null || address.equals("")) && (zip != null && zip.equals("30024"))) {
				location[0] = new Float(location[0].floatValue() - .04f);
				location[1] = new Float(location[1].floatValue() - .005f);
			}
			
			//Get the list of stores
			long[][] storeArray = null;
			if (isByState) {
				//location, app, licensee, radius, pricing, state
				storeArray = locator.getClosestStores(null, app, true, 5, false, state, false, null, StoreSearchUtils.STORES_TO_SHOW_TP);
			} else if (Config.TP.equalsIgnoreCase(thisConfig.getSiteName())) {
				//location, app, licensee, radius, pricing, state, full, licenseeType, count
				storeArray = locator.getClosestStores(location, app, true, 5, false, state, false, null, StoreSearchUtils.STORES_TO_SHOW_TP);
			} else if(Config.WWT.equalsIgnoreCase(thisConfig.getSiteName()) || Config.HT.equalsIgnoreCase(thisConfig.getSiteName())){
				storeArray = locator.getClosestStores(location, app, true, 5, false, state, true, null, StoreSearchUtils.STORES_TO_SHOW_DEFAULT, true);
			}else {
				storeArray = locator.getClosestStores(location, app, true, 5, false, state, false, null, StoreSearchUtils.STORES_TO_SHOW_DEFAULT);
			}
			
			Store[] stores = null;
			List<Store> lstores = new ArrayList<Store>();
			long[] distances = null;
			int storesCount = 0;
			//Did we get a list of store IDs back?
			if (storeArray != null && storeArray.length > 0) {
				displayStores = true;
				//Go get the actual stores
				stores = locator.getLocatorDAO().getStores(storeArray);
				
				//Build the distances array and set the store hours
				if (stores != null && stores.length > 0) {
					distances = new long[0];
					if (stores != null && stores.length > 0) {
						distances = new long[stores.length];
						for(int i = 0 ; i < stores.length ; i++) {
							lstores.add(stores[i]);
							stores[i].setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(stores[i].getNumber()), true));
							distances[i] = storeArray[i][1];	
						}
						locator.getLocatorDAO().setHolidayHours(lstores);
					}
				}				
			}
			
			storesCount = (lstores != null) ? lstores.size() : 0;
			
			model.addAttribute("displayStores", displayStores);
			model.addAttribute("isByState", isByState);
			model.addAttribute("stores", lstores);
			model.addAttribute("address", address);
			model.addAttribute("city", city);
			model.addAttribute("state", state);
			model.addAttribute("zip", zip);
			model.addAttribute("location", location);
			model.addAttribute("distances", distances);
			model.addAttribute("addressDescription", addressDescription);
			model.addAttribute("parameters", parameters);
			model.addAttribute("fromUrl", fromUrl);
			model.addAttribute("storesCount", storesCount);
			model.addAttribute("p", p);
			
			addMilitaryStoreData(model, session);
		} catch (Exception ex) {
			model.addAttribute("errors", "An exception occured, please try again.");
			
			if(fromPage != null ){
				return getDefaultView()+"?searchSuccess=false&trackPage="+fromPage;
			}
			return getDefaultView()+"?searchSuccess=false";
		}
		
		return getDiplayMapView();
	}
	
	public String getDirections(HttpServletRequest request, HttpSession session, Model model, String navZip, String zip, String city, String state, String address, String print, Long storeNumber) throws Exception {
		if (StringUtils.isNullOrEmpty(storeNumber))
			return getErrorView();
		
		if(request.getParameter("zip")!=null){
			zip=request.getParameter("zip");
		}
		if(zip==null||zip.isEmpty()){
			try{
				if(navZip!=null&&!navZip.isEmpty()){	
					StoreSearch storeSearch = storeService.createStoreSearchObject(navZip);
					zip = storeSearch.getZip();
				}
			}catch(Exception e)
			{
				/*do nothing*/
			}
		}
		
		city = Encode.encodeForJavaScript(city);
		address = Encode.encodeForJavaScript(address);
		zip = Encode.encodeForJavaScript(zip);
		if(zip!=null&&!zip.isEmpty()){
			String tempState=null;
			tempState = timeZoneDstService.getStateByZip(zip);
			if(tempState!=null&&!tempState.isEmpty())
				state=tempState;
		}
		
		model.addAttribute("directionsFromZip", zip != null ? zip : "");
		model.addAttribute("city", city != null ? city : "");
		model.addAttribute("state", state != null ? state : "");
		model.addAttribute("address", address != null ? address : "");
		model.addAttribute("nitrogenStores", getNitrogenStores());

		addMilitaryStoreData(model, session);

		long startTime = System.currentTimeMillis();
		String app = thisConfig.getSiteName();
		String remoteIP = request.getRemoteAddr();
		long requestId = locator.getRequestId();

		Store store = null;
		try {
			store = storeService.findStoreLightById(storeNumber);
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("errors", "Please enter your full address.");
			model.addAttribute("storeNumber", storeNumber);
			return getDirectionsView();
		}
		model.addAttribute("store", store);
		model.addAttribute("storeNumber", storeNumber);
		model.addAttribute("isPrintMode", "1".equals(print));
		String parameters = "";
		String fullAddress = "";
		if (address != null){
			parameters += "address=" + java.net.URLEncoder.encode(address, "UTF-8");
			fullAddress += address + " ";
		}
		if (city != null){
			parameters += (parameters == "" ? "" : "&") + "city=" + java.net.URLEncoder.encode(city, "UTF-8");
			fullAddress += city + " ";
		}
		if (state != null){
			parameters += (parameters == "" ? "" : "&") + "state=" + java.net.URLEncoder.encode(state, "UTF-8");
			fullAddress += state + " ";
		}
		if (zip != null || navZip != null){
			parameters += (parameters == "" ? "" : "&") + "zip=" + java.net.URLEncoder.encode(zip == null ? navZip : zip, "UTF-8");
			fullAddress += (zip == null ? navZip : zip);
		}
		if (storeNumber != null)
			parameters += (parameters == "" ? "" : "&") + "storeNumber=" + storeNumber;

		fullAddress = fullAddress.trim();

		// Create address description text
		String addressDescription = "";
		if (address != null && !address.equals(""))
			addressDescription += address + ", ";
		if (city != null && !city.equals(""))
			addressDescription += city + ", ";
		if (state != null && !state.equals(""))
			addressDescription += state + " ";
		if (zip != null && !zip.equals(""))
			addressDescription += zip;

		model.addAttribute("fullAddress", fullAddress);
		model.addAttribute("addressDescription", addressDescription);
		model.addAttribute("parameters", parameters);
		return getDirectionsView();
	}
	
	private void addMilitaryStoreData(Model model, HttpSession session) {
		model.addAttribute("storeMilitaryNumbers", storeService.getPipeDelimitedMilitaryStoreNumbers());
		model.addAttribute("storeMilitary_Disclaimer", "This location is only available to customers with military exchange privileges.");
		model.addAttribute("storeMilitaryDisclaimer", "This location only serves customers with military exchange privileges.");
	}
	
	public String getNitrogenStores() {
		if (Config.TP.equalsIgnoreCase(thisConfig.getSiteName())) {
			return "|233512|233510|400637|400831|400769|400645|347237|";
		} else if (Config.HT.equalsIgnoreCase(thisConfig.getSiteName())) {
			return "|517909|517895|517704|517615|517585|517720|237119|326507|326506|323022|";
		}
		return null;
	}
}
