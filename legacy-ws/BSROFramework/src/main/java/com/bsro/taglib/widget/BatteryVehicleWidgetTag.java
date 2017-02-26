package com.bsro.taglib.widget;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import app.bsro.model.battery.Battery;

import com.bfrc.framework.util.CacheDataUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bfrc.security.Encode;
import com.bsro.service.battery.BatteryService;

public class BatteryVehicleWidgetTag extends SimpleTagSupport {
	private static final Log logger = LogFactory.getLog(BatteryVehicleWidgetTag.class);

	private BatteryService batteryService = null;
	
	// defaults to true
	private Boolean includeTracking = true;
	private String productCode = null;
	private String mode = null;
	
	public void doTag() {
		PageContext pageContext = (PageContext) getJspContext();
		try {
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			HttpSession session = pageContext.getSession();
			
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			if (batteryService == null) {
				batteryService = springContext.getBean(BatteryService.class);
			}
			
			initializeBatteryVehicleWidget(request, session);
			
		} catch (Throwable e) {
			// Swallow it and log it, we don't want to break the page
			logger.error("Problem initializing battery widget cache on page "+ServerUtil.getMostRelevantUri((HttpServletRequest)pageContext.getRequest()), e);
		}
		
		try {
			getJspBody().invoke(null);
		} catch (Throwable e) {
			// swallow it and render it, we don't want it to break the page any more than it has to
			logger.error("Problem rendering battery widget on page "+ServerUtil.getMostRelevantUri((HttpServletRequest)pageContext.getRequest()), e);
		}
	}
	
	private void initializeBatteryVehicleWidget(HttpServletRequest request, HttpSession session) throws Exception {
		Battery selectedBattery = null;
		if (!ServerUtil.isNullOrEmpty(getProductCode())) {
			selectedBattery = batteryService.getBatteryByProductCode(getProductCode());
		}

		String zipCache     = CacheDataUtils.getCachedZip(request, "zip");
		String yearCache	= normalizeCache(session, request,	"battery.year",		"cache.battery.year",	"cache.vehicle.year");
		String makeCache	= normalizeCache(session, request,	"battery.make",		"cache.battery.make",	"cache.vehicle.make");
		String modelCache	= normalizeCache(session, request,	"battery.model",	"cache.battery.model",	"cache.vehicle.model");
		String engineCache	= normalizeCache(session, request,	"battery.engine",	"cache.battery.engine",	"cache.vehicle.engine");

		Map<String, String> yearOptions = null;
		Map<String, String> makeOptions = null;
		Map<String, String> modelOptions = null;
		Map<String, String> engineOptions = null;
		
		String selectedYear = "";
		String selectedMake = "";
		String selectedModel = "";
		String selectedEngine = "";
		
		yearOptions = batteryService.getYearOptions();
	
		boolean useCache = !"".equalsIgnoreCase(yearCache) && !"".equalsIgnoreCase(makeCache) && !"".equalsIgnoreCase(modelCache);
		if (useCache) {
			if (yearOptions != null) {					
				if (yearCache != null && yearOptions.containsKey(yearCache)) {
					selectedYear = yearCache;
						// the selected year is still valid
						if (makeCache != null) {
							makeOptions = batteryService.getMakeOptionsByYear(yearCache);
							if (makeOptions != null) {
								if (makeOptions.containsKey(makeCache)) {
									selectedMake = makeCache;
									// the selected make is still valid
									if (modelCache != null) {
										modelOptions = batteryService.getModelOptionsByYearAndMakeName(yearCache, makeCache);
										if (modelOptions != null) {
											if (modelOptions.containsKey(modelCache)) {
												selectedModel = modelCache;
												if (engineCache != null) {
													engineOptions = batteryService.getEngineOptionsByYearAndMakeNameAndModelName(yearCache, makeCache, modelCache);
													if (engineOptions != null) {
														if (engineOptions.containsKey(engineCache)) {
															selectedEngine = engineCache;
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
			useCache = !"".equalsIgnoreCase(selectedYear) && !"".equalsIgnoreCase(selectedMake) && !"".equalsIgnoreCase(selectedModel);
		}
		
		request.setAttribute("selectedBattery", selectedBattery);
		request.setAttribute("includeTracking", includeTracking);
		request.setAttribute("useCache", useCache);
		request.setAttribute("mode", mode);

		request.setAttribute("yearOptions", yearOptions);
		
		if (zipCache != null && Util.isValidZipCode(zipCache)) {
			request.setAttribute("zipCache", zipCache);
		}
		
		if (useCache) {
			request.setAttribute("yearCache", selectedYear);
			request.setAttribute("makeCache", selectedMake);
			request.setAttribute("modelCache", selectedModel);
			request.setAttribute("engineCache", selectedEngine);
		
			request.setAttribute("makeOptions", makeOptions);
			request.setAttribute("modelOptions", modelOptions);
			request.setAttribute("engineOptions", engineOptions);
		}
	}
	
	private String normalizeCache(javax.servlet.http.HttpSession session, HttpServletRequest request, String reqParam, String batteryCache, String vehicleCache) {
		String localCache = Encode.html(request.getParameter(reqParam));
		if (ServerUtil.isNullOrEmpty(localCache)) {
			localCache = (String)session.getAttribute(batteryCache);
		if (ServerUtil.isNullOrEmpty(localCache))
				localCache = (String) session.getAttribute(vehicleCache);
		}
		if (ServerUtil.isNullOrEmpty(localCache)) localCache = "";
		// update battery cache as needed
		if (!localCache.equals((String)session.getAttribute(batteryCache)))
			session.setAttribute(batteryCache, localCache);
		return localCache;
	}

	public Boolean getIncludeTracking() {
		return includeTracking;
	}

	public void setIncludeTracking(Boolean includeTracking) {
		this.includeTracking = includeTracking;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}	
}
