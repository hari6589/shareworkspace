package com.bsro.api.rest.ws.oilbatteryseo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.bsro.compare.oil.OilChangePackageComparator;
import app.bsro.model.error.Errors;
import app.bsro.model.oil.OilChangePackage;
import app.bsro.model.oil.OilChangeQuote;
import app.bsro.model.oil.OilChangeSearchResult;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import com.bfrc.dataaccess.svc.oil.IOilService;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.lookup.SeoVehicleData;
import com.bsro.oil.util.OilDataConstants;
import com.bsro.oil.util.OilDataFacade;
import com.bsro.api.rest.ws.oil.IOilWs;
import java.io.IOException;
import com.bsro.core.security.RequireValidToken;

@Component
public class OilBatterySEOWebServiceImpl implements OilBatterySEOWebService {

	private Logger log = Logger.getLogger(getClass().getName());
	private final String DEFAULT_SEO_SITE_NAME = "FCAC";
	
	@Autowired
	private IOilService oilService;

	public IOilService getOilService() {
		return oilService;
	}

	public void setOilService(IOilService oilService) {
		this.oilService = oilService;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContent(String seoContent,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
			
		if (StringUtils.isNullOrEmpty(seoContent)) {
			return getErrorMessage("Missing input parameters seoContent");
		}
		else if(!(seoContent.equals("oil") || seoContent.equals("battery")))
		{
			return getErrorMessage("Invalid input parameters seoContent");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = oilService.getSEOVehicleDataBean(seoContent, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}	
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContentByMake(String friendlyMakeName, String seoContent,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(friendlyMakeName)) {
			return getErrorMessage("Missing input parameters make");
		}
		
		if (StringUtils.isNullOrEmpty(seoContent)) {
			return getErrorMessage("Missing input parameters seoContent");
		}
		else if(!(seoContent.equals("oil") || seoContent.equals("battery")))
		{
			return getErrorMessage("Invalid input parameters seoContent");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = oilService.getSEOVehicleDataBean(friendlyMakeName, seoContent, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}	
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContentByMakeModelNames(String friendlyMakeName, String friendlyModelName, String seoContent,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(friendlyMakeName) || StringUtils.isNullOrEmpty(friendlyModelName)) {
			return getErrorMessage("Missing input parameters {make / model}");
		}
		
		if (StringUtils.isNullOrEmpty(seoContent)) {
			return getErrorMessage("Missing input parameters seoContent");
		}
		else if(!(seoContent.equals("oil") || seoContent.equals("battery")))
		{
			return getErrorMessage("Invalid input parameters seoContent");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = oilService.getSEOVehicleDataBean(friendlyMakeName, friendlyModelName, seoContent, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}	
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContentByYearMakeModelNames(
			String friendlyMakeName, String friendlyModelName, String year, String seoContent, String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(friendlyMakeName) || StringUtils.isNullOrEmpty(friendlyModelName) || StringUtils.isNullOrEmpty(year)) {
			return getErrorMessage("Missing input parameters {make / model / year}");
		}
		
		if (StringUtils.isNullOrEmpty(seoContent)) {
			return getErrorMessage("Missing input parameters seoContent");
		}
		else if(!(seoContent.equals("oil") || seoContent.equals("battery")))
		{
			return getErrorMessage("Invalid input parameters seoContent");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = oilService.getSEOVehicleDataBean(friendlyMakeName, friendlyModelName, year, seoContent, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}
	
	private BSROWebServiceResponse getErrorMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}

}
