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

import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.util.CacheDataUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bsro.service.vehicle.tire.TireVehicleService;

public class TireSizeWidgetTag extends SimpleTagSupport {
	private static final Log logger = LogFactory.getLog(TireSizeWidgetTag.class);
	
	private String mode = null;
	
	private VehicleDAO vehicleDAO = null;

	public void doTag() {
		PageContext pageContext = (PageContext) getJspContext();
		try {		
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			HttpSession session = pageContext.getSession();
			
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			if (vehicleDAO == null) {
				vehicleDAO = springContext.getBean(VehicleDAO.class);
			}
			
			initializeTireVehicleWidget(request, session);
			
		} catch (Throwable e) {
			// Swallow it and log it, we don't want to break the page
			logger.error("Problem initializing tire size widget cache on page "+ServerUtil.getMostRelevantUri((HttpServletRequest)pageContext.getRequest()), e);
		}
		
		try {
			getJspBody().invoke(null);
		} catch (Throwable e) {
			// swallow it and render it, we don't want it to break the page any more than it has to
			logger.error("Problem rendering tire size widget on page "+ServerUtil.getMostRelevantUri((HttpServletRequest)pageContext.getRequest()), e);
		}
	}
	
	private void initializeTireVehicleWidget(HttpServletRequest request, HttpSession session) throws Exception {
		Map<String, String> crossOptions = null;
		Map<String, String> aspectOptions = null;
		Map<String, String> rimOptions = null;
		
		String zipCache = CacheDataUtils.getCachedZip(request, "zip");
		
		String selectedZip = "";
		
		//Cross
		String crossCache = request.getParameter("cross");
		if(ServerUtil.isNullOrEmpty(crossCache)) {
			crossCache = (String)session.getAttribute("cache.vehicle.cross");
		} else {
			session.setAttribute("cache.vehicle.cross", crossCache);
		}
		
		if(ServerUtil.isNullOrEmpty(crossCache)) {
			crossCache = "";
		}

		//Aspect
		String aspectCache = request.getParameter("aspect");
		if(ServerUtil.isNullOrEmpty(aspectCache)) {
			aspectCache = (String)session.getAttribute("cache.vehicle.aspect");
		} else {
			session.setAttribute("cache.vehicle.aspect", aspectCache);
		}
		if(ServerUtil.isNullOrEmpty(aspectCache)) aspectCache = "";
		//Rim Size
		String rimCache = request.getParameter("cross");
		if(ServerUtil.isNullOrEmpty(rimCache)) {
			rimCache = (String)session.getAttribute("cache.vehicle.rim");
		} else {
			session.setAttribute("cache.vehicle.rim", rimCache);
		}
			
		crossOptions = vehicleDAO.getVehicleCrossSections();
		
		boolean useCacheSize = false;
		
		if(ServerUtil.isNullOrEmpty(rimCache)) {
			rimCache = "";
		}

		useCacheSize = !"".equalsIgnoreCase(crossCache) && !"".equalsIgnoreCase(aspectCache) && !"".equalsIgnoreCase(rimCache);

	    if(useCacheSize) {
	      	aspectOptions = vehicleDAO.getVehicleAspects(crossCache);

	        rimOptions = vehicleDAO.getVehicleRims(crossCache, aspectCache);
	    }

		request.setAttribute("crossOptions", crossOptions);
	    
	    request.setAttribute("mode", mode);
    	request.setAttribute("useCache", useCacheSize);
    	
		if (zipCache != null && Util.isValidZipCode(zipCache)) {
			selectedZip = zipCache;
			request.setAttribute("zipCache", selectedZip);
		}
		
		if (useCacheSize) {
			request.setAttribute("crossCache", crossCache);
			request.setAttribute("aspectCache", aspectCache);
			request.setAttribute("rimCache", rimCache);
		
			request.setAttribute("aspectOptions", aspectOptions);
			request.setAttribute("rimOptions", rimOptions);
		}
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
