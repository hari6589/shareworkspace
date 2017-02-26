package com.bsro.taglib.widget;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bfrc.framework.util.CacheDataUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.service.vehicle.tire.TireVehicleService;

public class TireFinderMakeFirstWidgetTag extends SimpleTagSupport {
	private static final Log logger = LogFactory.getLog(TireFinderMakeFirstWidgetTag.class);
	
	private String mode = null;
	
	private TireVehicleService tireVehicleService = null;

	public void doTag() {
		PageContext pageContext = (PageContext) getJspContext();
		try {		
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			HttpSession session = pageContext.getSession();
			
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			if (tireVehicleService == null) {
				tireVehicleService = springContext.getBean(TireVehicleService.class);
			}
			
			initializeTireVehicleWidget(request, session);
			
		} catch (Throwable e) {
			// Swallow it and log it, we don't want to break the page
			logger.error("Problem initializing tire widget cache on page "+ServerUtil.getMostRelevantUri((HttpServletRequest)pageContext.getRequest()), e);
		}
		
		try {
			getJspBody().invoke(null);
		} catch (Throwable e) {
			// swallow it and render it, we don't want it to break the page any more than it has to
			logger.error("Problem rendering tire widget on page "+ServerUtil.getMostRelevantUri((HttpServletRequest)pageContext.getRequest()), e);
		}
	}
	
	private void initializeTireVehicleWidget(HttpServletRequest request, HttpSession session) throws Exception {
		String zipCache = CacheDataUtils.getCachedZip(request, "zip");

		//Make
		String makeCache = request.getParameter("vehicleMake");
		if(ServerUtil.isNullOrEmpty(makeCache))
			makeCache = CacheDataUtils.getCachedVehicleMake(request, "vehicle.make");

		//Model
		String modelCache = request.getParameter("vehicleModel");
		if(ServerUtil.isNullOrEmpty(modelCache))
			modelCache = CacheDataUtils.getCachedVehicleModel(request, "vehicle.model");

		//Submodel
		String submodelCache = "";
		//Year
		String yearCache = "";
		//Tpms
		String tpmsCache = "";
		//Aces Vehicle Id
		String acesVehicleId = "";

		Map<String, String> makeOptions = null;
		Map<String, String> modelOptions = null;
		Map<String, String> submodelOptions = null;
		Map<String, String> yearOptions = null;
		
		String selectedMake = "";
		String selectedModel = "";
		String selectedSubmodel = "";
		String selectedYear = "";
		String selectedTpms = tpmsCache;
		String selectedZip = "";
		Boolean disableTpmsChoice = false;

		makeOptions = tireVehicleService.getMakeOptions();
		
    	boolean useCache = !"".equalsIgnoreCase(makeCache) && !"".equalsIgnoreCase(modelCache);
    	if (useCache) {
		/*********************** Process Cache Values **************************/
			if (makeOptions != null) {					
				if (makeCache != null && makeOptions.containsKey(makeCache)) {
					selectedMake = makeCache;
						if (modelCache != null) {
							modelOptions = tireVehicleService.getModelOptionsByMakeName(makeCache);
							if (modelOptions != null) {
								if (modelOptions.containsKey(modelCache)) {
									selectedModel = modelCache;
									if (submodelCache != null) {
										submodelOptions = tireVehicleService.getSubModelOptionsByMakeNameAndModelName(makeCache, modelCache);
										if (submodelOptions != null) {
											if (submodelOptions.containsKey(submodelCache)) {
												selectedSubmodel = submodelCache;
												if (submodelCache != null) {
													List<TireVehicle> tireVehicles = tireVehicleService.getVehiclesByMakeNameModelNameSubmodelName(makeCache, modelCache, submodelCache);
													if (tireVehicles != null) {
														boolean hasSetAcesVehicleId = false;
														boolean hasSetSelectedTpms = false;
														yearOptions = new LinkedHashMap<String, String>();
														for (TireVehicle tireVehicle : tireVehicles) {
															yearOptions.put(tireVehicle.getYear(), tireVehicle.getYear());
															// all vehicles at this point have the same hasSetAcesVehicleId and same tpms value
															if (!hasSetAcesVehicleId) {
																acesVehicleId = String.valueOf(tireVehicle.getAcesVehicleId());
																hasSetAcesVehicleId = true;
															}
															if (!hasSetSelectedTpms) {
																// if it's 1 for this vehicle, then we need to pre-select 1
																if (tireVehicle.getHasTpms().equals(1)) {
																	disableTpmsChoice = true;
																	selectedTpms = String.valueOf(tireVehicle.getHasTpms());
																}
																hasSetSelectedTpms = true;
															}
															if (tireVehicle.getYear().equals(yearCache)) {
																selectedYear = yearCache;
															}
														}
													}
													}
												}
											}
										}
									}
								}
							}
						}
				}
			useCache = (selectedMake != null && selectedModel != null);
    	}

    	
    	request.setAttribute("mode", mode);
    	request.setAttribute("useCache", useCache);
    	
		if (zipCache != null && Util.isValidZipCode(zipCache)) {
			selectedZip = zipCache;
			request.setAttribute("zipCache", selectedZip);
		}

		request.setAttribute("makeOptions", makeOptions);

		request.setAttribute("disableTpmsChoice", disableTpmsChoice);
		
		if (useCache) {
			request.setAttribute("yearCache", selectedYear);
			request.setAttribute("makeCache", selectedMake);
			request.setAttribute("modelCache", selectedModel);
			request.setAttribute("submodelCache", selectedSubmodel);
			request.setAttribute("tpmsCache", selectedTpms);
			request.setAttribute("acesVehicleId", acesVehicleId);

			request.setAttribute("modelOptions", modelOptions);
			request.setAttribute("submodelOptions", submodelOptions);
			request.setAttribute("yearOptions", yearOptions);
		}
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
