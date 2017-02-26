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
import com.bfrc.security.Encode;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.service.vehicle.tire.TireVehicleService;

public class TireVehicleWidgetTag extends SimpleTagSupport {
	private static final Log logger = LogFactory.getLog(TireVehicleWidgetTag.class);
	
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
		// localVar = normalizeCache(session, request, reqParam, tiresCache, vehicleCache)
		//ZIP
		String zipCache = CacheDataUtils.getCachedZip(request, "zip");
		zipCache = Encode.encodeForJavaScript(zipCache);
		//Year
		String yearCache = CacheDataUtils.getCachedVehicleYear(request, "vehicle.year");
		yearCache = Encode.encodeForJavaScript(yearCache);
		//Make
		String makeCache = CacheDataUtils.getCachedVehicleMake(request, "vehicle.make");
		//makeCache = ESAPI.encoder().encodeForJavaScript(makeCache);
		//Model
		String modelCache = CacheDataUtils.getCachedVehicleModel(request, "vehicle.model");
		//modelCache = ESAPI.encoder().encodeForJavaScript(modelCache);
		//Submodel
		String submodelCache = CacheDataUtils.getCachedVehicleSubmodel(request, "vehicle.submodel");
		//submodelCache = ESAPI.encoder().encodeForJavaScript(submodelCache);
		//Tpms
		String tpmsCache = CacheDataUtils.getCachedVehicleTpms(request,"tpms");
		//tpmsCache = ESAPI.encoder().encodeForJavaScript(tpmsCache);
		//Aces Vehicle Id
		String acesVehicleId = CacheDataUtils.getCachedAcesVehicleId(request,"vehicle.acesVehicleId");
		acesVehicleId = Encode.encodeForJavaScript(acesVehicleId);
		
		Map<String, String> yearOptions = null;
		Map<String, String> makeOptions = null;
		Map<String, String> modelOptions = null;
		Map<String, String> submodelOptions = null;
		
		String selectedYear = "";
		String selectedMake = "";
		String selectedModel = "";
		String selectedSubmodel = "";
		String selectedTpms = tpmsCache;
		String selectedZip = "";
		Boolean disableTpmsChoice = false;

		yearOptions = tireVehicleService.getYearOptions();
        //Modify the condition to maintain the catch only if yearCache is true. 
		boolean useCache = !"".equalsIgnoreCase(yearCache);
		if (useCache) {
			/*********************** Process Cache Values **************************/
			if (yearOptions != null) {					
				if (yearCache != null && yearOptions.containsKey(yearCache)) {
					selectedYear = yearCache;
					// the selected year is still valid
					if (makeCache != null) {
						makeOptions = tireVehicleService.getMakeOptionsByYear(yearCache);
						if (makeOptions != null) {
							if (makeOptions.containsKey(makeCache)) {
								selectedMake = makeCache;
								// the selected make is still valid
								if (modelCache != null) {
									modelOptions = tireVehicleService.getModelOptionsByYearAndMakeName(yearCache, makeCache);
									if (modelOptions != null) {
										if (modelOptions.containsKey(modelCache)) {
											selectedModel = modelCache;
											List<TireVehicle> tireVehicles = tireVehicleService.getVehiclesByYearAndMakeNameAndModelName(selectedYear, selectedMake, selectedModel);;
											if (tireVehicles != null) {
												boolean hasSetAcesVehicleId = false;
												boolean hasSetSelectedTpms = false;
												submodelOptions = new LinkedHashMap<String, String>();
												for (TireVehicle tireVehicle : tireVehicles) {
													submodelOptions.put(tireVehicle.getSubmodelName(), tireVehicle.getSubmodelName());
													if (!hasSetSelectedTpms) {
														// if it's 1 for this vehicle, then we need to pre-select 1
														if (tireVehicle.getHasTpms().equals(1)) {
															disableTpmsChoice = true;
															selectedTpms = String.valueOf(tireVehicle.getHasTpms());
														}
														hasSetSelectedTpms = true;
													}
													if (submodelCache != null) {
														if (tireVehicle.getSubmodelName().equals(submodelCache)) {
															selectedSubmodel = submodelCache;
															// some submodels have different acesVehicleId so changed the logic to set acesVehicleId from the right sub model. '1999 Ford Ranger XLT 4x2 Reg. Cab' with zip code 60139. 
															if (!hasSetAcesVehicleId) {
																acesVehicleId = String.valueOf(tireVehicle.getAcesVehicleId());
																hasSetAcesVehicleId = true;
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
			}
			useCache = (selectedMake != null && selectedYear != null);
		}


		request.setAttribute("mode", mode);
		request.setAttribute("useCache", useCache);

		if (zipCache != null && Util.isValidZipCode(zipCache)) {
			selectedZip = zipCache;
			request.setAttribute("zipCache", selectedZip);
		}

		request.setAttribute("yearOptions", yearOptions);
		request.setAttribute("disableTpmsChoice", disableTpmsChoice);
		
		if (useCache) {
			if(ServerUtil.isValidYear(yearCache))
				request.setAttribute("yearCache", selectedYear);
			request.setAttribute("makeCache", selectedMake);
			request.setAttribute("modelCache", selectedModel);
			request.setAttribute("submodelCache", selectedSubmodel);
			request.setAttribute("tpmsCache", selectedTpms);
			if(ServerUtil.isNumber(acesVehicleId))
				request.setAttribute("acesVehicleId", acesVehicleId);
			
			request.setAttribute("makeOptions", makeOptions);
			request.setAttribute("modelOptions", modelOptions);
			request.setAttribute("submodelOptions", submodelOptions);
		}
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
