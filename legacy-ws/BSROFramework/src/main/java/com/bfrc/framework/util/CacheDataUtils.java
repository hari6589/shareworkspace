package com.bfrc.framework.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bfrc.UserSessionData;
import com.bfrc.security.Encode;

public class CacheDataUtils {
	public static String getCachedZip(HttpServletRequest request) {
		return getCachedZip(request, null);
	}

	public static String getCachedZip(HttpServletRequest request, String paramName) {
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String zip = "";
		if(!ServerUtil.isNullOrEmpty(paramName))
			zip = Encode.encodeForJavaScript(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(zip)) {
			zip = (String) session.getAttribute("com.bfrc.pricing.ZipCode");
			if (ServerUtil.isNullOrEmpty(zip)) {
				zip = (String) session.getAttribute("cache.vehicle.zip");
				if (ServerUtil.isNullOrEmpty(zip))
					zip = userSessionData.getZip();
				if (ServerUtil.isNullOrEmpty(zip)) {
					zip = (String) session.getAttribute("cache.battery.zip");
				}
			}
		}
		if (ServerUtil.isNullOrEmpty(zip)||!Util.isValidZipCode(zip))
			zip = "";

		return zip;
	}

	public static String getCachedZipForTireSearch(HttpServletRequest request, String paramName) {
		String zip = getCachedZip(request, paramName);
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);

		if (!ServerUtil.isNullOrEmpty(zip)){
			//session.setAttribute("cache.vehicle.zip", zip);
			userSessionData.setZip(zip);
		}

		return zip;
	}

	public static void setCachedZip(HttpServletRequest request, String zip) {
		if(zip == null || zip.length() < 5){
			return;
		}
		zip = zip.substring(0, 5);
		HttpSession session = request.getSession();
		if (session.getAttribute("com.bfrc.pricing.ZipCode") != null)
			session.setAttribute("com.bfrc.pricing.ZipCode", zip);			
		if (session.getAttribute("cache.battery.zip") != null)
			session.setAttribute("cache.battery.zip", zip);
		session.setAttribute("cache.vehicle.zip", zip);
	}

	public static String getCachedAcesVehicleId(HttpServletRequest request) {
		return getCachedAcesVehicleId(request, null);
	}

	public static String getCachedAcesVehicleId(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "vehicle.acesVehicleId";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String acesVehicleId = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(acesVehicleId)) {
			acesVehicleId = (String) session.getAttribute("cache.vehicle.acesVehicleId");
			if (ServerUtil.isNullOrEmpty(acesVehicleId)) {
				acesVehicleId = userSessionData.getAcesVehicleId();
			}
		}
		if (acesVehicleId == null || acesVehicleId.equals(""))
			acesVehicleId = "";
		else {
			session.setAttribute("cache.vehicle.acesVehicleId", acesVehicleId);
			userSessionData.setAcesVehicleId(acesVehicleId);
		}
		return acesVehicleId;
	}

	public static String getCachedVehicleYear(HttpServletRequest request) {
		return getCachedVehicleYear(request, null);
	}

	public static String getCachedVehicleYear(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "vehicle.year";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String year = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(year)) {
			year = userSessionData.getYear();			
			if (ServerUtil.isNullOrEmpty(year)) {
				year = (String) session.getAttribute("cache.vehicle.year");				
				if (ServerUtil.isNullOrEmpty(year)) {
					//TODO: need to check whether this is required in the caching strategy
					year = (String) session.getAttribute("cache.battery.year");
				}
			}
		}
		if (ServerUtil.isNullOrEmpty(year))
			year = "";
		else {
			session.setAttribute("cache.vehicle.year", year);
			userSessionData.setYear(year);
		}
		return year;
	}

	public static String getCachedVehicleMake(HttpServletRequest request) {
		return getCachedVehicleMake(request, null);
	}

	public static String getCachedVehicleMake(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "vehicle.make";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String make = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(make)) {
			make = userSessionData.getMake();			
			if (ServerUtil.isNullOrEmpty(make)) {
				make = (String) session.getAttribute("cache.vehicle.make");				
				if (ServerUtil.isNullOrEmpty(make)) {
					//TODO: need to check whether this is required in the caching strategy
					make = (String) session.getAttribute("cache.battery.make");
				}
			}
		}
		if (ServerUtil.isNullOrEmpty(make))
			make = "";
		else {
			session.setAttribute("cache.vehicle.make", make);
			userSessionData.setMake(make);
		}
		return make;
	}

	public static String getCachedVehicleModel(HttpServletRequest request) {
		return getCachedVehicleModel(request, null);
	}

	public static String getCachedVehicleModel(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "vehicle.model";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String model = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(model)) {
			model = userSessionData.getModel();	
			if (ServerUtil.isNullOrEmpty(model)) {
				model = (String) session.getAttribute("cache.vehicle.model");
				if (ServerUtil.isNullOrEmpty(model)) {
					//TODO: need to check on this whether this is the correct caching strategy
					model = (String) session.getAttribute("cache.battery.model");
				}
			}
		}
		if (ServerUtil.isNullOrEmpty(model))
			model = "";
		else {
			session.setAttribute("cache.vehicle.model", model);
			userSessionData.setModel(model);
		}
		return model;
	}

	public static String getCachedVehicleSubmodel(HttpServletRequest request) {
		return getCachedVehicleSubmodel(request, null);
	}

	public static String getCachedVehicleSubmodel(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "vehicle.submodel";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String submodel = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(submodel)) {
			submodel = userSessionData.getSubmodel();
			if (ServerUtil.isNullOrEmpty(submodel)) {
				submodel = (String) session.getAttribute("cache.vehicle.submodel");
			}
		}
		if (ServerUtil.isNullOrEmpty(submodel))
			submodel = "";
		else {
			session.setAttribute("cache.vehicle.submodel", submodel);
			userSessionData.setSubmodel(submodel);
		}
		// submodel =
		// com.bfrc.framework.util.StringUtils.unescapeHtmlLight(submodel);
		return submodel;
	}

	public static String getCachedVehicleTpms(HttpServletRequest request) {
		return getCachedVehicleTpms(request, null);
	}

	public static String getCachedVehicleTpms(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "tpms";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String tpms = Encode.html(request.getParameter(paramName));
		if (tpms == null || tpms.equals("")) {
			tpms = (String) session.getAttribute("cache.vehicle.tpms");
			if (ServerUtil.isNullOrEmpty(tpms)) {
				tpms = userSessionData.getTpms();
			}
		}
		if (tpms == null || tpms.equals(""))
			tpms = "";
		else {
			session.setAttribute("cache.vehicle.tpms", tpms);
			userSessionData.setTpms(tpms);
		}
		return tpms;
	}

	public static String getCachedCrossSection(HttpServletRequest request) {
		return getCachedCrossSection(request, null);
	}

	public static String getCachedCrossSection(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "cross";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String cross = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(cross)) {
			cross = (String) session.getAttribute("cache.vehicle.cross");
			if (ServerUtil.isNullOrEmpty(cross)) {
				cross = userSessionData.getCrossSection();
			}
		}
		if (ServerUtil.isNullOrEmpty(cross))
			cross = "";
		else {
			session.setAttribute("cache.vehicle.cross", cross);
			userSessionData.setCrossSection(cross);
		}
		return cross;
	}

	public static String getCachedAspect(HttpServletRequest request) {
		return getCachedAspect(request, null);
	}

	public static String getCachedAspect(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "aspect";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String aspect = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(aspect)) {
			aspect = (String) session.getAttribute("cache.vehicle.aspect");
			if (ServerUtil.isNullOrEmpty(aspect)) {
				aspect = userSessionData.getAspect();
			}
		}
		if (ServerUtil.isNullOrEmpty(aspect))
			aspect = "";
		else {
			session.setAttribute("cache.vehicle.aspect", aspect);
			userSessionData.setAspect(aspect);
		}
		return aspect;
	}

	public static String getCachedRimSize(HttpServletRequest request) {
		return getCachedRimSize(request, null);
	}

	public static String getCachedRimSize(HttpServletRequest request, String paramName) {
		if (paramName == null)
			paramName = "rim";
		HttpSession session = request.getSession();
		UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
		String rim = Encode.html(request.getParameter(paramName));
		if (ServerUtil.isNullOrEmpty(rim)) {
			rim = (String) session.getAttribute("cache.vehicle.rim");
			if (ServerUtil.isNullOrEmpty(rim)) {
				rim = userSessionData.getRimSize();
			}
		}
		if (ServerUtil.isNullOrEmpty(rim))
			rim = "";
		else {
			session.setAttribute("cache.vehicle.rim", rim);
			userSessionData.setRimSize(rim);
		}
		return rim;
	}
	
	public static void setCachedVehicleYear(HttpSession session,Long year){
		if(!ServerUtil.isNullOrEmpty(year)){
			setCachedVehicleYear(session, year.toString());
		}
	}
	
	public static void setCachedVehicleYear(HttpSession session,String year){
		if(!ServerUtil.isNullOrEmpty(year)){
			session.setAttribute("cache.vehicle.year", year);
			UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
			userSessionData.setYear(year);
		}
	}
	
	public static void setCachedVehicleMake(HttpSession session,String make){
		if(!ServerUtil.isNullOrEmpty(make)){
			session.setAttribute("cache.vehicle.make", make);
			UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
			userSessionData.setMake(make);
		}	
	}
	
	public static void setCachedVehicleModel(HttpSession session,String model){
		if(!ServerUtil.isNullOrEmpty(model)){
			session.setAttribute("cache.vehicle.model", model);
			UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
			userSessionData.setModel(model);
		}
	}
	
	public static void setCachedVehicleSubmodel(HttpSession session, String submodel){
		if(!ServerUtil.isNullOrEmpty(submodel)){
			session.setAttribute("cache.vehicle.submodel", submodel);
			UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
			userSessionData.setSubmodel(submodel);
		}
	}
	
	public static void setCachedAcesVehicleId(HttpSession session, String acesVehicleId){
		if(!ServerUtil.isNullOrEmpty(acesVehicleId)){
			session.setAttribute("cache.vehicle.acesVehicleId", acesVehicleId);
			UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
			userSessionData.setAcesVehicleId(acesVehicleId);
		}
	}
	
	public static void setCachedVehicleTpms(HttpSession session, String tpms){
		if(!ServerUtil.isNullOrEmpty(tpms)){
			session.setAttribute("cache.vehicle.tpms", tpms);			
			UserSessionData userSessionData = UserProfileUtils.getUserSessionData(session);
			userSessionData.setTpms(tpms);
		}
	}
}
