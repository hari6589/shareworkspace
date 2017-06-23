package com.bsro.core.svc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bsro.core.exception.ws.AuthenticationException;
import com.bsro.core.model.HttpHeader;
import com.bsro.core.svc.AuthenticateService;

@Component
public class AuthenticateServiceImpl implements AuthenticateService {

	/*
	 * All of these static maps and such can (and probably should) be replaced with a database implementation when we have time...
	 */
	
	private Logger log = Logger.getLogger(getClass().getName());
	
	private static String iPhoneTokenId = "c4be9530-7774-4cf1-9124-08e53635a00c";
	private static String androidTokenId = "745537bb-1d8c-4f49-976b-8a479ef0ce3f";
	private static String webTokenId = "20338ccf-89e9-40c1-bd61-ebfbe3bb0cab";
	private static String twoByFourTokenId = "07b51830-4d43-4b63-ac6b-bc2ee5738a3f";
	
	private static String fcacTokenId = "1f04ad80-b947-fe80-32bf4a78a69d54acb";
	private static String fcacAppName = "FCAC";
	
	private static String tpTokenId = "3m57gs98-s542-sa76-43az0hg9q68k53dkz";
	private static String tpAppName = "TP";
	
	private static String htAppName = "HT";
	private static String wwtAppName = "WWT";
	
	private static Map<String, String> applicationCredentials = null;
	
	static {
		applicationCredentials = new HashMap<String, String>();
		applicationCredentials.put(fcacAppName, fcacTokenId);
		applicationCredentials.put(tpAppName, tpTokenId);
		applicationCredentials.put(htAppName, twoByFourTokenId);
		applicationCredentials.put(wwtAppName, twoByFourTokenId);
	}
	
	private static String bsroApplicationGroupName = "BSRO";
	
	private static Map<String, List<String>> applicationGroups = null;
	
	static {
		applicationGroups = new HashMap<String, List<String>>();
		List<String> bsroGroupMembers = new ArrayList<String>();
		bsroGroupMembers.add(fcacAppName);
		bsroGroupMembers.add(tpAppName);
		bsroGroupMembers.add(htAppName);
		bsroGroupMembers.add(wwtAppName);
		applicationGroups.put(bsroApplicationGroupName, bsroGroupMembers);
	}
	
	private static Map<String, List<String>> webServicesAccessByApplicationGroup = null;

	
	private static Map<String, List<String>> webServicesAccessByApplication = null;
	
	static {
		webServicesAccessByApplicationGroup = new HashMap<String, List<String>>();
		webServicesAccessByApplicationGroup.put("/web-site-user/auth-user", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/create-login-user", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/create-anonymous-user", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/create-login-user-from-anonymous-user", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/get-user", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/get-user-by-id", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/update-user", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/forgot-password", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user/reset-password", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		
		webServicesAccessByApplicationGroup.put("/web-site-user-vehicle/create-user-vehicle/tire", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user-vehicle/create-user-subvehicle", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user-vehicle/update-user-vehicle", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user-vehicle/update-user-subvehicle", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user-vehicle/delete-user-vehicle", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user-vehicle/fetch-user-vehicle-by-user-id", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/web-site-user-vehicle/fetch-user-vehicle-by-id", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		
		webServicesAccessByApplicationGroup.put("/vehicle/tire/options/year-make-model-submodel/years", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/tire/options/year-make-model-submodel/makes", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/tire/options/year-make-model-submodel/models", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/tire/options/year-make-model-submodel/submodels", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		
		webServicesAccessByApplicationGroup.put("/vehicle/battery/options/year-make-model-engine/years", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/battery/options/year-make-model-engine/makes", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/battery/options/year-make-model-engine/models", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/battery/options/year-make-model-engine/engine-sizes", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/battery/get/search-results", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		
		webServicesAccessByApplicationGroup.put("/vehicle/battery/get/battery-quote", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/battery/save/battery-quote", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));
		webServicesAccessByApplicationGroup.put("/vehicle/battery/get/battery-life", new ArrayList<String>(Arrays.asList(bsroApplicationGroupName)));

		
		webServicesAccessByApplication = new HashMap<String, List<String>>();
		// if you wanted specific users to be able to access, you could add them here
		
		// we pull out the members of groups and add them here to spare us having to map users back to groups back to services later
		for (String service : webServicesAccessByApplicationGroup.keySet()) {
			if (!webServicesAccessByApplication.containsKey(service)) {
				List<String> users = new ArrayList<String>();
				webServicesAccessByApplication.put(service, users);
			}
			
			
			List<String> groupsThatCanAccessThisService = webServicesAccessByApplicationGroup.get(service);
			
			if (groupsThatCanAccessThisService != null && !groupsThatCanAccessThisService.isEmpty()) {
				for (String groupName : groupsThatCanAccessThisService) {
					List<String> groupMembers = applicationGroups.get(groupName);
					webServicesAccessByApplication.get(service).addAll(groupMembers);
				}
			}
		}
	}	
	
	private boolean required;

	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isRequired() {
		return required;
	}
	
	public boolean validateToken(HttpHeaders headers) {

		String tokenId = "";

		if(headers == null) {
			log.warning("No headers provided");
			throw new AuthenticationException();
		}
		
		if(headers.getRequestHeader(HttpHeader.Params.TOKEN_ID.getValue()) != null)
			tokenId = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.TOKEN_ID.getValue()).get(0));
		
		tokenId = StringUtils.trimToEmpty(tokenId);
		if(tokenId.equals(iPhoneTokenId) || tokenId.equals(androidTokenId) || tokenId.equals(webTokenId) || tokenId.equals(twoByFourTokenId) || applicationCredentials.containsValue(tokenId)) {
			return true;
		} else {
			log.warning("Invalid "+HttpHeader.Params.TOKEN_ID.toString() +" passed. Cannot validate user.");
			throw new AuthenticationException();
		}
	}
	
	public boolean validateAppNameAndToken(HttpHeaders headers, String serviceName) {
		String appName = "";
		String tokenId = "";
		
		if (headers == null) {
			log.warning("No headers provided");
			throw new AuthenticationException();
		}
		
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));
		}
		
		if(headers.getRequestHeader(HttpHeader.Params.TOKEN_ID.getValue()) != null) {
			tokenId = StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.TOKEN_ID.getValue()).get(0));
		}
		
		if(appName.length() == 0 || tokenId.length() == 0) {
			log.warning(HttpHeader.Params.APP_NAME.toString() +"('"+appName+"') and "+HttpHeader.Params.TOKEN_ID.toString() +" not passed. Cannot validate application.");
			throw new AuthenticationException();
		}
		
		if (serviceName == null || serviceName.isEmpty()) {
			log.warning("There is no service name, so it cannot be determined whether or not the app is authorized to use it");
			throw new AuthenticationException();
		} else {
			boolean isAppAllowed = false;

			if (webServicesAccessByApplication.containsKey(serviceName)) {
				List<String> applicationsThatCanAccessThisService = webServicesAccessByApplication.get(serviceName);
				
				if (applicationsThatCanAccessThisService != null && !applicationsThatCanAccessThisService.isEmpty()) {
					if (applicationsThatCanAccessThisService.contains(appName)) {
						isAppAllowed = true;
					}
				}
			} else {
				log.warning("No access rules have been defined for "+serviceName);
				throw new AuthenticationException();
			}
			
			if (!isAppAllowed) {
				log.warning(appName+" is not permitted to access "+serviceName);
				throw new AuthenticationException();
			}
		}
		
		if (applicationCredentials.get(appName) == null || !applicationCredentials.get(appName).equals(tokenId)) {
			log.warning("Invalid "+HttpHeader.Params.APP_NAME.toString() +"('"+appName+"') and "+HttpHeader.Params.TOKEN_ID.toString() +" passed. Cannot validate application.");
			throw new AuthenticationException();
		} else { 
			return true;
		}
	}	
}
