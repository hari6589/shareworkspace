/**
 * 
 */
package com.bsro.api.rest.ws.tires;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.model.vehicle.TirePressure;
import com.bfrc.dataaccess.svc.catalog.CatalogService;
import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.lookup.SeoVehicleData;
import com.bsro.databean.vehicle.FriendlySizeDataBean;
import com.bsro.databean.vehicle.RimDiameter;
import com.bsro.databean.vehicle.TireSize;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;
import com.bsro.service.tire.TireSizeService;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import com.bsro.core.security.RequireValidToken;

/**
 * @author smoorthy
 *
 */
@Component
public class TiresWebServiceImpl implements TiresWebService {
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	@Autowired
	private TireSizeService tireSizeService;
	
	@Autowired
	CatalogService catalogService;
	
	private Logger log = Logger.getLogger(getClass().getName());
	
	private final String DEFAULT_SEO_SITE_NAME = "FCAC";

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getMakeOptions(String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		List<TireVehicleMake> makes = tireVehicleService.getAllMakes();
		Map<String, List<TireVehicleMake>> data = new HashMap<String, List<TireVehicleMake>>();
		data.put("makes", makes);
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(data);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getModelOptionsByMake(String makeName,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName)) {
			return getErrorMessage("Missing input parameters make");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		FriendlyVehicleDataBean friendlyVehicleDataBean = tireVehicleService.getFriendlyVehicleDataBean(makeName, siteName);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(friendlyVehicleDataBean);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContentByMake(String makeName,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName)) {
			return getErrorMessage("Missing input parameters make");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = tireVehicleService.getSEOVehicleDataBean(makeName, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}	

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getYearOptionsByMakeModelNames(
			String makeName, String modelName, String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName) || StringUtils.isNullOrEmpty(modelName)) {
			return getErrorMessage("Missing input parameters {make / model}");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		FriendlyVehicleDataBean friendlyVehicleDataBean = tireVehicleService.getFriendlyVehicleDataBean(makeName, modelName, siteName);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(friendlyVehicleDataBean);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContentByMakeModelNames(String makeName, String modelName,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName) || StringUtils.isNullOrEmpty(modelName)) {
			return getErrorMessage("Missing input parameters {make / model}");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = tireVehicleService.getSEOVehicleDataBean(makeName, modelName, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}	
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSubmodelOptionsByYearMakeModelNames(
			String makeName, String modelName, String year, String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName) || StringUtils.isNullOrEmpty(modelName) || StringUtils.isNullOrEmpty(year)) {
			return getErrorMessage("Missing input parameters {make / model / year}");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		FriendlyVehicleDataBean friendlyVehicleDataBean = tireVehicleService.getFriendlyVehicleDataBean(makeName, modelName, year, siteName);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(friendlyVehicleDataBean);
		
		return response;
	}

	public BSROWebServiceResponse getSeoContentByYearMakeModelNames(
			String makeName, String modelName, String year, String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName) || StringUtils.isNullOrEmpty(modelName) || StringUtils.isNullOrEmpty(year)) {
			return getErrorMessage("Missing input parameters {make / model / year}");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = tireVehicleService.getSEOVehicleDataBean(makeName, modelName, year, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSubmodelOptionByYearMakeModelSubmodelNames(
			String makeName, String modelName, String year, String trim,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName) || StringUtils.isNullOrEmpty(modelName) || StringUtils.isNullOrEmpty(year) || StringUtils.isNullOrEmpty(trim)) {
			return getErrorMessage("Missing input parameters {make / model / year / trim}");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		FriendlyVehicleDataBean friendlyVehicleDataBean = tireVehicleService.getFriendlyVehicleDataBean(makeName, modelName, year, trim, siteName);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(friendlyVehicleDataBean);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContentByYearMakeModelSubmodelNames(
			String makeName, String modelName, String year, String trim,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(makeName) || StringUtils.isNullOrEmpty(modelName) || StringUtils.isNullOrEmpty(year) || StringUtils.isNullOrEmpty(trim)) {
			return getErrorMessage("Missing input parameters {make / model / year / trim}");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = tireVehicleService.getSEOVehicleDataBean(makeName, modelName, year, trim, siteName);		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSEODataByVehicle(Long acesVehicleId,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(acesVehicleId)) {
			return getErrorMessage("Missing input parameters ACES Vehicle Id");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = tireVehicleService.getSEOVehicleDataBean(acesVehicleId, siteName);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSEODataBySize(String cross, String aspect, String rim,
			String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (StringUtils.isNullOrEmpty(cross) || StringUtils.isNullOrEmpty(aspect) || StringUtils.isNullOrEmpty(rim)) {
			return getErrorMessage("Missing input parameters cross/aspect/rim");
		}
		
		if (StringUtils.isNullOrEmpty(siteName)) {
			siteName = DEFAULT_SEO_SITE_NAME;
		}
		
		SeoVehicleData seoVehicleDataBean = tireSizeService.getSeoVehicleData(siteName, "SIZE_TIRE", "/size/"+cross+"-"+aspect+"-r"+rim+"/");
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(seoVehicleDataBean);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getRimDiameters(String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			List<RimDiameter> rimSizes = tireSizeService.getRimDiameters(siteName);
			Map<String, List<RimDiameter>> data = new HashMap<String, List<RimDiameter>>();
			data.put("sizes", rimSizes);
			
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(data);
		} catch (Throwable throwable) {
			return getErrorMessage("Problem retrieving tire size rims");
		}
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getTireSizes(String rimSize, String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			FriendlySizeDataBean sizes = tireSizeService.getVehicleSizesByRimSize(rimSize, siteName);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(sizes);
		} catch (Throwable throwable) {
			return getErrorMessage("Problem retrieving tire sizes by rim diameter");
		}
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSeoContentByRimSize(String rimSize, String siteName, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();		
		try {
			SeoVehicleData seoVehicleData = tireSizeService.getSeoVehicleData(siteName, "SIZE_DIAMETER", "/size/"+rimSize+"/");			
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(seoVehicleData);
		} catch (Throwable throwable) {
			return getErrorMessage("Problem retrieving tire sizes by rim diameter");
		}
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getTiresByVehicleType(String vehicleType, String siteName,
			String sortBy, HttpHeaders headers) {
		log.fine("Returning tire list by vehicle type "+vehicleType);
		BSROWebServiceResponse response = null;
		
		if(StringUtils.isNullOrEmpty(siteName)) {
			response = getErrorMessage("Invalid site name");
			return response;
		}
		
		if(StringUtils.isNullOrEmpty(vehicleType)) {
			response = getErrorMessage("Invalid vehicle type");
			return response;
		}
		
		response = catalogService.getTiresByVehicleType(vehicleType, siteName, sortBy);
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getMakeModelsByVehicleType(String vehicleType, HttpHeaders headers) {
		log.fine("Returning tire list by vehicle type "+vehicleType);
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if(StringUtils.isNullOrEmpty(vehicleType)) {
			response = getErrorMessage("Invalid vehicle type");
			return response;
		}
		
		List<FriendlyVehicleDataBean> friendlyVehicleDataBeans = tireVehicleService.getMakeModelsByVehicleType(vehicleType);
		if (friendlyVehicleDataBeans == null || friendlyVehicleDataBeans.isEmpty()) {
			response = getErrorMessage("No vehicle make / model found for the given category '"+vehicleType+"'");
			return response;
		}
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(friendlyVehicleDataBeans);
		
		return response;
	}
	
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getTirePressureDetails(Long acesVehicleId,
			HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		List<TirePressure> results = null;
		TireVehicle tireVehicle = null;
		
		if(StringUtils.isNullOrEmpty(acesVehicleId)) {
			response = getErrorMessage("Invalid Aces Vehicle Id input.");
			return response;
		}
		
		tireVehicle = tireVehicleService.fetchVehicleByAcesVehicleId(acesVehicleId);
		if (tireVehicle != null) {
			results = tireVehicleService.getTirePressuresByName(tireVehicle.getYear(), tireVehicle.getMakeName(), tireVehicle.getModelName(), tireVehicle.getSubmodelName());
		}
		
		if(results == null || results.size() == 0){
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			response.setMessage(ValidationConstants.NO_DATA_FOUND);
			return response;
		}
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		response.setPayload(results);
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
