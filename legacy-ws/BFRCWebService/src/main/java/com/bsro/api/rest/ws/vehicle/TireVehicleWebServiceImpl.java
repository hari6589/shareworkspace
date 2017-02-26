package com.bsro.api.rest.ws.vehicle;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bfrc.dataaccess.model.vehicle.SearchParams;
import com.bfrc.dataaccess.model.vehicle.TirePressure;
import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.alignment.AlignmentPricing;
import com.bfrc.pojo.alignment.AlignmentPricingQuote;
import com.bsro.core.exception.ws.BSROWebServiceUnexpectedErrorResponseException;
import com.bsro.core.exception.ws.InvalidArgumentException;
import com.bsro.core.security.RequireValidAppNameAndToken;
import com.bsro.core.security.RequireValidToken;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;

@Component
public class TireVehicleWebServiceImpl implements TireVehicleWebService {
	@Autowired
	private TireVehicleService tireVehicleService;
	
	private Logger log = Logger.getLogger(getClass().getName());

	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-submodel/years")
	public BSROWebServiceResponse getYearOptions(HttpHeaders headers) throws BSROWebServiceUnexpectedErrorResponseException {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			List<String> years = tireVehicleService.getYears();
			
			Map<String, String> dropdownMap = new LinkedHashMap<String, String>();
			
			if (years != null) {
				for (String year : years) {
					dropdownMap.put(year, year);
				}
			}
			
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(dropdownMap);
		} catch(Throwable e){
			BSROWebServiceUnexpectedErrorResponseException exception = new BSROWebServiceUnexpectedErrorResponseException(e);
			throw exception;
		}
		
		return response;
	}

	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-submodel/makes")
	public BSROWebServiceResponse getMakeOptionsByYear(HttpHeaders headers, String year) throws BSROWebServiceUnexpectedErrorResponseException {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			List<TireVehicleMake> makes = tireVehicleService.getMakesByYear(year);
			Map<String, String> dropdownMap = new LinkedHashMap<String, String>();
			
			if (makes != null) {
				for (TireVehicleMake make : makes) {
					dropdownMap.put(make.getName(), make.getName());
				}
			}
			
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(dropdownMap);
		} catch(Throwable e){
			BSROWebServiceUnexpectedErrorResponseException exception = new BSROWebServiceUnexpectedErrorResponseException(e);
			throw exception;
		}
		
		return response;
	}

	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-submodel/models")
	public BSROWebServiceResponse getModelOptionsByYearAndMake(HttpHeaders headers, String year, String make) throws BSROWebServiceUnexpectedErrorResponseException {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			List<TireVehicleModel> models = tireVehicleService.getModelsByYearAndMakeName(year, make);
			Map<String, String> dropdownMap = new LinkedHashMap<String, String>();
			
			if (models != null) {
				for (TireVehicleModel model : models) {
					dropdownMap.put(model.getName(), model.getName());
				}
			}
			
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(dropdownMap);
		} catch(Throwable e){
			BSROWebServiceUnexpectedErrorResponseException exception = new BSROWebServiceUnexpectedErrorResponseException(e);
			throw exception;
		}
		
		return response;
	}

	@Override
	@RequireValidAppNameAndToken(URL+"/options/year-make-model-submodel/submodels")
	public BSROWebServiceResponse getSubModelOptionsByYearAndMakeAndModel(HttpHeaders headers, String year, String make, String model) throws BSROWebServiceUnexpectedErrorResponseException {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			List<TireVehicleSubModel> submodels = tireVehicleService.getSubModelsByYearAndMakeNameAndModelName(year, make, model);

			Map<String, String> dropdownMap = new LinkedHashMap<String, String>();
			
			if (submodels != null) {
				for (TireVehicleSubModel submodel : submodels) {
					dropdownMap.put(submodel.getName(), submodel.getName());
				}
			}
			
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(dropdownMap);
		} catch(Throwable e){
			BSROWebServiceUnexpectedErrorResponseException exception = new BSROWebServiceUnexpectedErrorResponseException(e);
			throw exception;
		}
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getTirePressureDetails(HttpHeaders headers, UriInfo uriInfo) 
			throws BSROWebServiceUnexpectedErrorResponseException{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
		if(params.isEmpty()) {
			log.severe("No query parameters passed");
			response.setMessage("No query parameters passed");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
			
		Map<String, Object> validateSearchParams = new HashMap<String, Object>();
		try {
			validateSearchParams = validateSearchQueryParams(params);
		} catch (InvalidArgumentException e) {
			
			response.setMessage(ValidationConstants.INSUFFICIENT_PARAMS_PASSED);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}			
		
		List<TirePressure> results = null;
		if(validateSearchParams.get(SearchParams.MAKE.getValue()) != null){
			results = tireVehicleService.getTirePressuresByName((String)validateSearchParams.get(SearchParams.YEAR.getValue()), 
					(String)validateSearchParams.get(SearchParams.MAKE.getValue()), 
					(String)validateSearchParams.get(SearchParams.MODEL.getValue()), 
					(String)validateSearchParams.get(SearchParams.SUBMODEL.getValue()));
		}else{
			results = tireVehicleService.getTirePressuresById((String)validateSearchParams.get(SearchParams.YEAR.getValue()), 
					(Long)validateSearchParams.get(SearchParams.MAKEID.getValue()), 
					(Long)validateSearchParams.get(SearchParams.MODELID.getValue()), 
					(Long)validateSearchParams.get(SearchParams.SUBMODELID.getValue()));
		}
		if(results.size() == 0){
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			response.setMessage(ValidationConstants.NO_DATA_FOUND);
			return response;
		}
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		response.setPayload(results);
		return response;
	}
	
	/**
	 * Validate the query parameters passed in
	 * @param params
	 * @return
	 */
	private Map<String, Object> validateSearchQueryParams(MultivaluedMap<String, String> params) 
			throws InvalidArgumentException{
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		String year = StringUtils.trimToEmpty(params.getFirst(SearchParams.YEAR.getValue()));
		
		String make = StringUtils.trimToEmpty(params.getFirst(SearchParams.MAKE.getValue()));
		String model = StringUtils.trimToEmpty(params.getFirst(SearchParams.MODEL.getValue()));
		String submodel = StringUtils.trimToEmpty(params.getFirst(SearchParams.SUBMODEL.getValue()));

		String makeId = StringUtils.trimToEmpty(params.getFirst(SearchParams.MAKEID.getValue()));
		String modelId = StringUtils.trimToEmpty(params.getFirst(SearchParams.MODELID.getValue()));
		String submodelId = StringUtils.trimToEmpty(params.getFirst(SearchParams.SUBMODELID.getValue()));
		
		if(year.isEmpty()){
			throw new InvalidArgumentException(ValidationConstants.INSUFFICIENT_PARAMS_PASSED);
			
		}
		if((make.isEmpty() || model.isEmpty()) && (makeId.isEmpty() || modelId.isEmpty())){
			throw new InvalidArgumentException("Make and Model are required");
		}
		
		if(!year.isEmpty() && !make.isEmpty() && !model.isEmpty()){
			searchParams.put(SearchParams.YEAR.getValue(), year);
			searchParams.put(SearchParams.MAKE.getValue(), make);
			searchParams.put(SearchParams.MODEL.getValue(), model);
			if(!submodel.isEmpty())
				searchParams.put(SearchParams.SUBMODEL.getValue(), submodel);
			
			return searchParams;
		}
	
		if(!year.isEmpty() && !makeId.isEmpty() && !modelId.isEmpty()){
			searchParams.put(SearchParams.YEAR.getValue(), year);
			searchParams.put(SearchParams.MAKEID.getValue(), Long.valueOf(makeId));
			searchParams.put(SearchParams.MODELID.getValue(), Long.valueOf(modelId));
			if(!submodelId.isEmpty())
				searchParams.put(SearchParams.SUBMODELID.getValue(), Long.valueOf(submodelId));
			
			return searchParams;
		}
		
		return null;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getStoreInventory(HttpHeaders headers, String storeNumber){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Collection<StoreInventory> results = tireVehicleService.getInventoryByStore(new Long(storeNumber));
		if(results.size() == 0){
			response.setMessage(ValidationConstants.NO_DATA_FOUND);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		response.setPayload(results);
		return response;

	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAlignmentPricing(HttpHeaders headers,String acesVehicleId,String storeNumber,String siteName)
	{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		if(!ServerUtil.isNullOrEmpty(storeNumber) && !ServerUtil.isNullOrEmpty(siteName)) {
			response = tireVehicleService.getAlignmentPricingByStore(acesVehicleId,new Long(storeNumber),siteName);
			return response;
		} else {
			response.setMessage("Store number and siteName should not empty");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return response;
		}
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAlignmentQuote(HttpHeaders headers,String quoteId, String siteName)
	{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if(!ServerUtil.isNullOrEmpty(quoteId) && !ServerUtil.isNullOrEmpty(siteName))
		{
		AlignmentPricingQuote results = tireVehicleService.getAlignmentQuote(new Long(quoteId),siteName);
		if(results == null){
			response.setMessage(ValidationConstants.NO_DATA_FOUND);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			response.setPayload(results);
			return response;
		}
		else
		{
			response.setMessage("quoteId and siteName should not empty");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return response;
		}
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse createRepairAlignmentQuote(HttpHeaders headers, String storeNumber, 
			String articleNumber, String altype,String acesVehicleId, String alpricingId,String firstName,
			String lastName, String emailId, String siteName) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (ServerUtil.isNullOrEmpty(storeNumber) || ServerUtil.isNullOrEmpty(articleNumber) || ServerUtil.isNullOrEmpty(altype)|| ServerUtil.isNullOrEmpty(acesVehicleId)|| ServerUtil.isNullOrEmpty(alpricingId)|| ServerUtil.isNullOrEmpty(siteName)) {
			String errmsg = getErrorMessage(storeNumber, articleNumber, altype, acesVehicleId, alpricingId,siteName);
			response = getValidationMessage(errmsg);			
		} else {
			response = tireVehicleService.createRepairAlignmentQuote(Long.parseLong(storeNumber), articleNumber, altype, Long.parseLong(acesVehicleId), alpricingId, firstName, lastName, emailId, siteName);
		}
		return response;
	}
	
	private BSROWebServiceResponse getValidationMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}
	
	private String getErrorMessage(String storeNumber, String articleNumber, String altype, String acesVehicleId, String alpricingId, String siteName) {
		StringBuffer buf = new StringBuffer();
		String errmsg = null;
		int errFieldsCount = 0;
		if (ServerUtil.isNullOrEmpty(storeNumber)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("store number");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(articleNumber)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("article number");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(altype)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("altype");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(acesVehicleId)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("acesVehicleId");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(alpricingId)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("alpricingId");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(siteName)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("site name");
			++errFieldsCount;
		}
		
		errmsg = (errFieldsCount == 1) ? "Missing parameter value for field " : "Missing parameter value for fields ";
		errmsg += buf.toString();
		return errmsg;
	}
}
