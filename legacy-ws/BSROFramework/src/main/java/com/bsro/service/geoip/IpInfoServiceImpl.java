package com.bsro.service.geoip;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.Config;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreSearch;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bsro.constants.SessionConstants;
import com.bsro.service.store.StoreService;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.BSROWebserviceServiceImpl;

@Service("ipInfoService")
public class IpInfoServiceImpl extends BSROWebserviceServiceImpl implements IpInfoService {
	
	private static final String PATH_WEBSERVICE_GET_ZIP = "ipinfo/ip";

	@Autowired
	StoreService storeService;
	
	@Autowired
	Config thisConfig;
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}

	public Store getStoreByZip(String navZip, String remoteIP) {
		Store store = null;
		
		try {
			StoreSearch storeSearch = storeService.createStoreSearchObject(navZip);
			String app = thisConfig.getSiteName();
			long requestId = locator.getRequestId();
			Float[] location = new Float[2];
			location = locator.geoLocationWithBing(requestId, app, null, null, storeSearch.getState(), storeSearch.getZip(), remoteIP);

			long[][] storeArray = null;
			Store[] stores = null;
			storeArray = locator.getClosestStores(location, app, true, 5, false, storeSearch.getState(), false, null, 1);
			// Did we get a list of store IDs back?
			if (storeArray != null && storeArray.length > 0) {
				// Go get the actual stores
				stores = locator.getLocatorDAO().getStores(storeArray);
				// Build the distances array and set the store hours
				if (stores != null && stores.length > 0) {
					store = stores[0];
					store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(stores[0].getNumber()), true));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return store;
	}

	@Override
	public void setPreferredStoreByIp(String remoteIP, HttpSession session) {
		Store store = (Store) session.getAttribute(SessionConstants.PREFERRED_STORE);
		if (store == null) { 
			// get geo info by ip
			//String zip = getZipByIp(remoteIP);
			Map<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("ipAddress", remoteIP);
			StringBuilder webservicePath = null;
			String zip = "";
			BSROWebServiceResponse response = null;
			try {
				webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_GET_ZIP);
				response = (BSROWebServiceResponse)getWebservice(webservicePath.toString(), parameters, BSROWebServiceResponse.class);
				if(response != null && response.getPayload() != null){
					zip = response.getPayload().toString();
				}else{
					if(response != null && response.getErrors() != null && response.getErrors().hasErrors()){
						System.err.println(response.getErrors().getGlobalErrors().get(0));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (zip != null) {
				// get store info by zip
				store = getStoreByZip(zip, remoteIP);			
				if (store != null) {
					// set preferred store
					session.setAttribute(SessionConstants.PREFERRED_STORE, store);
				}
			}
		}
	}
	
	
	@Override
	public String findZipfromIPAddress(String remoteIP) {
		// get geo info by ip
		//String zip = getZipByIp(remoteIP);
		Map<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("ipAddress", remoteIP);
		StringBuilder webservicePath = null;
		String zip = "";
		BSROWebServiceResponse response = null;
		try {
			webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_GET_ZIP);
			response = (BSROWebServiceResponse)getWebservice(webservicePath.toString(), parameters, BSROWebServiceResponse.class);
			if(response != null && response.getPayload() != null){
				zip = response.getPayload().toString();
			}else{
				if(response != null && response.getErrors() != null && response.getErrors().hasErrors()){
					System.err.println(response.getErrors().getGlobalErrors().get(0));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zip;
	}
}
