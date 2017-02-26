package com.bsro.api.rest.ws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.ValidationErrorList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.maintenance.MaintenanceMilestones;
import app.bsro.model.maintenance.MileageTypes;
import app.bsro.model.maintenance.PeriodicMaintenanceMilestones;
import app.bsro.model.maintenance.ScheduledMaintenanceCategories;
import app.bsro.model.maintenance.ScheduledMaintenances;
import app.bsro.model.maintenance.ServiceMaintenanceTO;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.maintenance.Intervals;
import com.bfrc.dataaccess.svc.webdb.IntervalsService;
import com.bsro.api.rest.svc.TransformerService;
import com.bsro.core.exception.ws.EmptyDataSetException;
import com.bsro.core.exception.ws.InvalidArgumentException;
import com.bsro.core.exception.ws.ServerProcessingException;
import com.bsro.core.security.RequireValidToken;

/**
 * Implementation of the Scheduled Maintenance Web Service.
 * @author Brad Balmer
 *
 */
@Component
public class ScheduledMaintenanceWebServiceImpl implements ScheduledMaintenanceWebService {

	@Autowired
	private IntervalsService intervalsService;
	@Autowired
	private TransformerService<ServiceMaintenanceTO, Intervals> intervalTransformerService;
	@Resource(name="maintenanceMilestonesList")
	private List<String> maintenanceMilestonesList;
	private Logger log = Logger.getLogger(getClass().getName());
	
	private static final String ILLEGAL_ARGUMENT = "Illegal Argument passed";
	private static final String NO_DATA_FOUND = "No Data found";
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getCategoryCounts(Long vehicleId, HttpHeaders headers) {		
		
		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		
		if(errors.size() > 0) {
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		List<Intervals> intervals = intervalsService.listIntervalMaintenance(vehicleId);
		List<Intervals> periodic = intervalsService.listPeriodicMaintenance(vehicleId);
		List<Intervals> required = intervalsService.listRequiredMaintenance(vehicleId);
		List<Intervals> milestones = intervalsService.listMaintenanceMilestones(vehicleId);
		
		int requiredCnt = required != null?required.size():0;
		int periodicCnt = periodic != null?periodic.size():0;
		int checksCnt = intervals != null?intervals.size():0;
		int milestonesCnt = milestones != null?milestones.size():0;
		
		ScheduledMaintenanceCategories categories= new ScheduledMaintenanceCategories(requiredCnt, periodicCnt, checksCnt, milestonesCnt);
		response.setPayload(categories);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
		
		
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse typeListByVehicle(Long vehicleId, HttpHeaders headers) {
		

		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		if(errors.size() > 0) {
			
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		List<String> loResult = null;
		try {
			loResult = intervalsService.getDistinctServiceTypesInSvcType(vehicleId, maintenanceMilestonesList);
		}catch(Exception E) {
			String errorMsg = "Server error processing request: "+E.getMessage();
			log.severe(errorMsg);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(errorMsg);
			return response;
		}
		
		if(loResult != null && loResult.size() > 0)
		{
			response.setPayload(loResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			return response;		
		}
		else
		{
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(NO_DATA_FOUND+" for vehicle "+vehicleId);
			return response;
		}
		
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse mileageListByVehicle(Long vehicleId, String serviceType, HttpHeaders headers) {
		
		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		//ESAPI.validator().isValidInput("serviceType", serviceType, "HTTPParameterValue", 50, false, true, errors);
		if(errors.size() > 0) {

			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		List<String> loResult = null;
		try {
			loResult = intervalsService.getDistinctMileageValues(vehicleId, serviceType);
		}catch(Exception E) {
			String errorMsg = "Server error processing request: "+E.getMessage();
			log.severe(errorMsg);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(errorMsg);
			return response;
		}
		
		if(loResult != null && loResult.size() > 0)
		{
			response.setPayload(loResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			return response;		
		}
		else
		{
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(NO_DATA_FOUND+" for vehicle "+vehicleId);
			return response;
		}
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse requiredMaintenance(Long vehicleId, HttpHeaders headers) {

		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		if(errors.size() > 0) {
			
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		ScheduledMaintenances loResult = null;
		List<Intervals> intervals = intervalsService.listRequiredMaintenance(vehicleId);
		
		if(intervals != null && intervals.size() > 0)
		{
			loResult = new ScheduledMaintenances( intervalTransformerService.transform(intervals) );
			response.setPayload(loResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			return response;		
		}
		else
		{
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(NO_DATA_FOUND+" for vehicle "+vehicleId);
			return response;
		}
		
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse periodicMaintenance(Long vehicleId, HttpHeaders headers) {
		
		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		if(errors.size() > 0) {
			log.severe("Invalid argument passed.  "+vehicleId +" is not a valid vehicleId.");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		PeriodicMaintenanceMilestones loResult = null;

		List<Intervals> intervals = intervalsService.listPeriodicMaintenance(vehicleId);
		
		if(intervals != null && intervals.size() > 0)
		{
			loResult = new PeriodicMaintenanceMilestones( intervalTransformerService.transform(intervals) );
			response.setPayload(loResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			return response;		
		}
		else
		{
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(NO_DATA_FOUND+" for vehicle "+vehicleId);
			return response;
		}

	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse serviceCheckMaintenance(Long vehicleId, HttpHeaders headers) {

		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		if(errors.size() > 0) {
			log.severe("Invalid argument passed.  "+vehicleId +" is not a valid vehicleId.");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		ScheduledMaintenances loResult = null;

		List<Intervals> intervals = intervalsService.listIntervalMaintenance(vehicleId);
		
		
		if(intervals != null && intervals.size() > 0)
		{
			loResult = new ScheduledMaintenances( intervalTransformerService.transform(intervals) );
			response.setPayload(loResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			return response;		
		}
		else
		{
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(NO_DATA_FOUND+" for vehicle "+vehicleId);
			return response;
		}
		
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse milestoneMaintenance(Long vehicleId, String serviceType, String mileageInterval, Integer indexId, HttpHeaders headers) {

		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		//ESAPI.validator().isValidInput("serviceType", serviceType, "HTTPParameterValue", 50, false, true, errors);
		//ESAPI.validator().isValidInput("mileageInterval", mileageInterval, "HTTPParameterValue", 8, true, true, errors);
//		ESAPI.validator().isValidInteger("indexId", indexId.toString(), -10, Integer.MAX_VALUE, true, errors);
		if(errors.size() > 0) {
			log.warning("Invalid argument passed for one of vehicle, serviceType or mileageInterval ("+vehicleId+", "+serviceType+" or "+mileageInterval);
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		MaintenanceMilestones loResult = null;
		mileageInterval = StringUtils.trimToNull(mileageInterval);
		
		List<Intervals> intervals = intervalsService.listMaintenanceMilestones(vehicleId, serviceType);
		
		if(intervals == null || intervals.size() == 0)
		{
		response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
		response.setMessage(NO_DATA_FOUND+" for vehicle ("+vehicleId+") and service type("+serviceType+") with mileage ("+mileageInterval+") and index ("+indexId+"");
		return response;
		}
		
		loResult = new MaintenanceMilestones( intervalTransformerService.transform(intervals) );
		
		/*
		 * Need to be able to filter through the results to pull out either a particular mileageInterval record set OR pull out 
		 * one by index.
		 */
		if(mileageInterval != null) {
			List<MileageTypes> data = loResult.getData();
			MileageTypes keep = null;
			for(MileageTypes type : data) {
				if(type.getMileage().equals(mileageInterval)) {
					keep = type;
					break;
				}
			}
			
			if(keep == null) {
				String errorMsg = "IndexOutOfBounds.  Mileage passed ("+mileageInterval+") is not a part of the result set.";
				log.severe(errorMsg);
				//throw new InvalidArgumentException(errorMsg);
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				response.setMessage(errorMsg);
				return response;
			}

			data = new ArrayList<MileageTypes>(1);
			data.add(keep);
			loResult.setData(data);
			
			
		} else if(indexId.intValue() > -1) {
			if( (indexId.intValue() + 1) > loResult.getTotal()) {
				String errorMsg = "IndexOutOfBounds.  Index passed ("+indexId+") is greater than size of result set.";
				log.severe(errorMsg);
				//throw new InvalidArgumentException(errorMsg);
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				response.setMessage(errorMsg);
				return response;
			} else {
				try {
					List<MileageTypes> data = loResult.getData();
					MileageTypes mt = data.get(indexId);
					data = new ArrayList<MileageTypes>(1);
					data.add(mt);
					loResult.setData(data);
				}catch(Exception E) {
					String errorMsg = "Error processing indexed value: "+E.getMessage();
					log.severe(errorMsg);
					//throw new ServerProcessingException(errorMsg);
					response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
					response.setMessage(errorMsg);
					return response;
				}
			}
		}
		response.setPayload(loResult);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;	
	}

}
