package com.bfrc.dataaccess.svc.webdb.locator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import map.States;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.dao.store.StoreBusinessRulesDAO;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.svc.webdb.LocatorService;
import com.bfrc.dataaccess.svc.webdb.StoreService;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.StringUtils;

/**
 * @author smoorthy
 *
 */

@Service
public class LocatorServiceImpl implements LocatorService {
	
	@Autowired
	private States states;
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	StoreDAO storeDAO;
	
	@Autowired
	StoreService storeService;
	
	@Autowired
	StoreBusinessRulesDAO storeBusinessRulesDAO;

	public BSROWebServiceResponse getState() {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		response.setPayload(states);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BSROWebServiceResponse getStateBySite(String siteName) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		List states = null;
		
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		Map m = new HashMap();
		m.put("appointment", null);
		try {
			states = storeDAO.getStates(m);
		} catch (Exception e) {
		}
		
		if (states == null || states.isEmpty()) {
			response.setMessage("No state details found for site "+siteName);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		response.setPayload(states);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BSROWebServiceResponse getCityByStateAndSite(String state,
			String siteName) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		List cities = null;
		
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		Map m = new HashMap();
		m.put("appointment", null);
		m.put("state", state);
		try {
			cities = storeDAO.getCities(m);
		} catch (Exception e) {
		}
		
		if (cities == null || cities.isEmpty()) {
			response.setMessage("No city details found for site: "+siteName+", state: "+state);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		response.setPayload(cities);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

	@SuppressWarnings("rawtypes")
	public BSROWebServiceResponse getStoresByStateAndCity(String state,
			String city, String siteName) {
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		List<String> storeTypes = new ArrayList<String>();
		Map storeMap = storeDAO.getPrimaryStoreMap(siteName);
		Iterator i = storeMap.keySet().iterator();		
		while(i.hasNext()) {
			storeTypes.add((String)i.next());
		}
		
		List<Store> stores = storeBusinessRulesDAO.findStoreByStateCityType(state, city, storeTypes);
		
		if (stores == null || stores.isEmpty()) {
			response.setMessage("No stores found for site: "+siteName+", state: "+state+", city: "+city);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		for (Store store : stores) {
			store.setReasonDesc(storeBusinessRulesDAO.checkStoreReasonCode(store.getStoreNumber()));
			store.setHolidayHourMessages(storeService.getStoreHolidayHours(store));
			store.setMilitaryStore(storeService.isMilitaryStore(store.getStoreNumber()));
		}
		
		response.setPayload(stores);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

}
