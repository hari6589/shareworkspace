package com.bsro.api.rest.ws.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.ValidationErrorList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.maintenance.MaintenanceSearchResults;
import app.bsro.model.maintenance.MaintenanceVehicle;
import app.bsro.model.scheduled.maintenance.JobResults;
import app.bsro.model.scheduled.maintenance.MaintenanceInvoice;

import com.bfrc.dataaccess.model.system.BsroWebServiceRequests;
import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestsService;
import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestServiceImpl.ServiceCode;
import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestServiceImpl.WebServiceId;
import com.bsro.api.rest.exception.WebServiceException;
import com.bsro.api.rest.svc.MaintenanceHistoryService;
import com.bsro.api.rest.util.TimerUtils;
import com.bsro.core.exception.ws.EmptyDataSetException;
import com.bsro.core.exception.ws.InvalidArgumentException;
import com.bsro.core.exception.ws.ServerProcessingException;
import com.bsro.core.model.HttpHeader;
import com.bsro.core.security.RequireValidToken;

/**
 * Implementation of the Maintenance History Web Service
 * @author Brad Balmer
 *
 */
@Component
public class MaintenanceHistoryWebServiceImpl implements MaintenanceHistoryWebService {
	
	@Autowired
	private MaintenanceHistoryService maintenanceHistoryService;
	
	@Autowired
	private WebServiceRequestsService webServiceRequestsService;
	private Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Parameters allowed by this web service
	 * @author Brad Balmer
	 *
	 */
	private enum Params {
		PHONE("phone"), 
		STORE("storeId"), 
		INVOICE("invoiceId"), 
		YEAR("year"),
		MAKE("make"),
		MODEL("model"),
		VEHICLE("vehicleId"), 
		SINCE("sinceDt");
		private String param;

		private Params(String value) {
			this.param = value;
		}
		public String getValue() {
			return this.param;
		}
	};
	
	/**
	 * Do we allow access to the NCD subsystem
	 */
	@Override
	@RequireValidToken
	public Response allowNcdAccess(HttpHeaders headers) {
		
		BsroWebServiceRequests rqst = webServiceRequestsService.getRequest(WebServiceId.NCD_WEB_SERVICE);
		
		Status responseStatus = Status.SERVICE_UNAVAILABLE;
		String message = "Service History database is undergoing routine maintenance.  We apologize for the inconvenience.  Please try again later.";
		if(rqst != null && StringUtils.trimToEmpty(rqst.getWebServiceCode()).equals(ServiceCode.ACTIVE.getCode())) {
			responseStatus = Status.OK;
			message = "";
		}

		ResponseBuilder response = Response.status(responseStatus);
		response.entity(message);
		return response.build();	
	}
	
	/**
	 * Search for vehicles by either phone number or a combination of store number and invoice number.
	 */
	@Override
	@RequireValidToken
	public MaintenanceSearchResults searchForVehicles(HttpHeaders headers,
			UriInfo info, boolean filterResults) {

		String siteType = headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0);
		MultivaluedMap<String, String> params = info.getQueryParameters();
		Map<String, Object> cleanSearchParams = validateSearchQueryParams(params);

		boolean DEBUG = getDebugParam(headers);
		List<MaintenanceVehicle> tmpVehicles = new ArrayList<MaintenanceVehicle>();
		try {
			if(cleanSearchParams.containsKey(Params.PHONE.getValue())) {
				Object phoneObj = cleanSearchParams.get(Params.PHONE.getValue());
				List<String> phoneSearchNumbers = new ArrayList<String>();
				if(phoneObj instanceof String)
					phoneSearchNumbers.add((String)phoneObj);
				else
					phoneSearchNumbers = (List<String>)phoneObj;
				
				tmpVehicles = maintenanceHistoryService.searchByPhone(phoneSearchNumbers, DEBUG);
			} else {
				String storeNbr = (String)cleanSearchParams.get(Params.STORE.getValue());
				String invoiceNbr = null;
				try {
					invoiceNbr =  (String)cleanSearchParams.get(Params.INVOICE.getValue()) ;
				}catch(Exception E) { throw new InvalidArgumentException("Invalid invoice number format.  Must be a number."); }
				
				if(storeNbr == null || invoiceNbr == null) throw new InvalidArgumentException("Both a store number and an invoice number are required");
				log.fine("Searching by invoice ("+invoiceNbr+") and store number ("+storeNbr+")");
				tmpVehicles = maintenanceHistoryService.searchByStoreInvoice(storeNbr, invoiceNbr, DEBUG);
			}
			if(DEBUG) {
				log.severe("Found "+tmpVehicles.size()+" vehicles");
				for(MaintenanceVehicle mv : tmpVehicles) {
					log.severe("  "+mv.getVehicleId() + " - "+mv.getMake() + " : "+mv.getModel());
				}
			}
		}catch(WebServiceException e) {
			log.severe("Error processing searchForVehicles: "+e.getMessage());
			throw new ServerProcessingException(e.getMessage());
		}
		//Make sure this variable is not null coming out of the search
		if(tmpVehicles == null) tmpVehicles = new ArrayList<MaintenanceVehicle>(0);
		
		List<MaintenanceVehicle> vehicles = new ArrayList<MaintenanceVehicle>();
		MaintenanceVehicle filter = (MaintenanceVehicle)cleanSearchParams.get("FILTER");
		List<Integer> found = new ArrayList<Integer>();
		for(MaintenanceVehicle vehicle : tmpVehicles) {

			if(!found.contains(vehicle.getVehicleId())) {
				//We only want to return results where the make and model of the return results
				// match what is passed in.
				if(vehicle.equals(filter)) {
					
					//We are no longer performing a strict match on the model as the NCD data coming back
					// does NOT match the fitment data used to select an automobile.  So, do a soft match
					// on the model to see if the ncd data contains the filter model.
	//				String filterModel = StringUtils.trimToEmpty(filter.getModel()).toLowerCase();
	//				String ncdModel = StringUtils.trimToEmpty(vehicle.getModel()).toLowerCase();
	//				if(StringUtils.contains(ncdModel, filterModel))
						vehicle.setModelMatch(true);
	//				else
	//					vehicle.setModelMatch(false);
					
					
					//If the year passed in matches the return result then set the flag.  This indicates
					// a more positive match.
					if(StringUtils.trimToEmpty(vehicle.getYear()).equals(filter.getYear()))
						vehicle.setYearMatch(true);
					else
						vehicle.setYearMatch(false);
					vehicles.add(vehicle);
					found.add(vehicle.getVehicleId());
				} else {
					log.info("Ignoring vehicle from search result as the vehicle does not match the make/model passed in: "+vehicle.toString()+" != "+filter.toString());
				}
			}
		}
		//clean up
		found = null;
		if(vehicles == null || vehicles.size() == 0)
			throw new EmptyDataSetException("No data found for requested parameters.");

		
		
		TimerUtils timer = null;
		if(DEBUG) timer = new TimerUtils();
//		Map<MaintenanceVehicle, List<MaintenanceInvoice>> bindings = null;
		List<MaintenanceVehicle> bindings = null;
		try {
			bindings = maintenanceHistoryService.binding(vehicles, siteType, DEBUG, filterResults);
		} catch (ServerProcessingException e) {
			log.severe("Error processing bindings in searchForVehicles: "+e.getMessage());
			throw new ServerProcessingException(e.getMessage());
		} catch (WebServiceException e) {
			log.severe("Error processing bindings in searchForVehicles: "+e.getMessage());
			throw new ServerProcessingException(e.getMessage());
		}
		
		if(DEBUG) {
//			log.severe("ENDING TIMER");
			timer.end();
			timer.stdOutLog();
		}

		if(bindings == null || bindings.size() == 0)
			throw new EmptyDataSetException("No data found for requested parameters.");
 
		List<MaintenanceVehicle> loFinalResult = new ArrayList<MaintenanceVehicle>();
		loFinalResult.addAll(bindings);

		MaintenanceSearchResults searchResults =  new MaintenanceSearchResults(loFinalResult);
		return searchResults;
	}

	/**
	 * Searches for invoices (jobs) by the vehicle id.
	 */
	@Override
	@RequireValidToken
	public JobResults  selectJobData(HttpHeaders headers, String vehicleId, Long sinceVal, boolean filterResults) {
		log.fine(vehicleId+", "+sinceVal);

		//First, validate the input
		ValidationErrorList errors = new ValidationErrorList();
		ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
		
		if(errors.size() > 0) {
			log.warning("Error validating vehicleId of "+vehicleId);
			throw new InvalidArgumentException(errors);
		}

		//Convert the since into a Date
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String since = null;
			if(sinceVal != null && sinceVal.intValue() > 0) {
				Calendar c = GregorianCalendar.getInstance();
				c.setTimeInMillis(sinceVal);
				since = sdf.format(c.getTime());
			}
		}catch(Exception E) {
			String msg = "The since date is not a valid date ("+sinceVal+").";
			log.warning(msg);
			throw new InvalidArgumentException(msg);
		}
		
		JobResults srchResults = new JobResults();
		List<MaintenanceInvoice> invoices = null;
		String siteType = headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0);
		try {
			log.info("Calling retrieveInvoices using("+vehicleId+", "+sinceVal+", "+siteType+")");
			invoices = maintenanceHistoryService.retrieveInvoices(vehicleId, sinceVal, siteType, getDebugParam(headers), filterResults);
		}catch(WebServiceException e) {
			e.printStackTrace();
			log.severe("Error processing retrieveInvoices: "+e.getMessage());
			throw new ServerProcessingException(e.getMessage());
		}

		if(invoices == null || invoices.size() == 0 || invoices.get(0) == null)
			throw new EmptyDataSetException("No data found for requested parameters."); 

		srchResults.setInvoices(invoices);
		return srchResults;
	}
	
	@Override
	@RequireValidToken
	public JobResults selectInvoiceData(HttpHeaders headers, UriInfo info, boolean filterResults) {

		MultivaluedMap<String, String> params = info.getQueryParameters();
		List<String> vehicleIds = params.get(Params.VEHICLE.getValue());
		Long sinceVal = null;

		if (params.containsKey(Params.SINCE.getValue())) {

			sinceVal = new Long(params.getFirst(Params.SINCE.getValue()));
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (sinceVal != null && sinceVal.intValue() > 0) {
					Calendar c = GregorianCalendar.getInstance();
					c.setTimeInMillis(sinceVal);
					String since = sdf.format(c.getTime());
				}
			} catch (Exception E) {
				String msg = "The since date is not a valid date (" + sinceVal + ").";
				log.warning(msg);
				throw new InvalidArgumentException(msg);
			}

		}
		
		
		if(vehicleIds != null && vehicleIds.size() > 0) {
			List<MaintenanceInvoice> finalInvoices = new ArrayList<MaintenanceInvoice>(); 
			for(String vehicleId : vehicleIds) {
				//First, validate the input
				ValidationErrorList errors = new ValidationErrorList();
				ESAPI.validator().isValidInteger("vehicleId", vehicleId.toString(), 1, Integer.MAX_VALUE, false, errors);
				
				if(errors.size() > 0) {
					log.warning("Error validating vehicleId of "+vehicleId);
					throw new InvalidArgumentException(errors);
				}
			}
			
			JobResults srchResults = new JobResults();
			String siteType = headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0);
			for (String vehicleId : vehicleIds) {

				try {
					List<MaintenanceInvoice> invoices = null;
					log.info("Calling retrieveInvoices using(" + vehicleId + ", " + sinceVal + ", " + siteType + ")");
					invoices = maintenanceHistoryService.retrieveInvoices(vehicleId, sinceVal, siteType, getDebugParam(headers), filterResults);
					if(invoices != null)
						finalInvoices.addAll(invoices);
				} catch (WebServiceException e) {
					e.printStackTrace();
					log.severe("Error processing retrieveInvoices: " + e.getMessage());
					throw new ServerProcessingException(e.getMessage());
				}
			}

			if (finalInvoices == null || finalInvoices.size() == 0)
				throw new EmptyDataSetException("No data found for requested parameters.");

			srchResults.setInvoices(finalInvoices);
			return srchResults;
		} else {
			throw new InvalidArgumentException("Invoices required for search");
		}
	}

	
	/**
	 * Validate the query parameters passed in
	 * @param params
	 * @return
	 */
	private Map<String, Object> validateSearchQueryParams(MultivaluedMap<String, String> params) {
		if(params == null) {
			log.severe("No query parameters passed: NULL");
			throw new InvalidArgumentException("No query parameters passed");
		}
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		List<String> phones = new ArrayList<String>();
		
		Iterator<String> iter = params.keySet().iterator();
		while(iter.hasNext()) {
			String KEY = iter.next();
			List<String> keyVals = params.get(KEY);
			if(keyVals.size() > 1) {				
				for(String VAL : keyVals) {

					if(!validatePhone(VAL)) {
						throw new InvalidArgumentException("Phone must be a valid 10-digit number");
					}
					
					phones.add(VAL);
				}
				if(Params.PHONE.getValue().equals(KEY))
					searchParams.put(KEY, phones);
				
			} else {
				String VAL = params.getFirst(KEY);
				try {
					if(KEY.equals(Params.PHONE.getValue())) {
						if(!validatePhone(VAL))
							throw new Exception();
						searchParams.put(KEY, VAL);
					}  else { //else if(KEY.equals(Params.INVOICE.getValue()) || KEY.equals(Params.)){
					
						//searchParams.put(KEY, ESAPI.validator().getValidInput(KEY, VAL, "HTTPParameterValue", Integer.MAX_VALUE, false, true));
						searchParams.put(KEY, VAL);
					}
				}catch(Exception vE) {
					throw new InvalidArgumentException(VAL+" is invalid for the parameter "+KEY);
				}
			}
		}
		
		if(searchParams.size() == 0) {
			log.info("No query parameters passed into search");
			throw new InvalidArgumentException("No query parameters passed");
		}
		
		String year = StringUtils.trimToEmpty((String)searchParams.get(Params.YEAR.getValue()));
		String make = StringUtils.trimToNull((String)searchParams.get(Params.MAKE.getValue()));
		String model = StringUtils.trimToNull((String)searchParams.get(Params.MODEL.getValue()));
		
		if(make == null || model == null)
			throw new InvalidArgumentException("Make and/or model parameters not passed.");
		
		MaintenanceVehicle filter = new MaintenanceVehicle();
		filter.setYear(year);
		filter.setMake(make);
		filter.setModel(model);
		searchParams.put("FILTER", filter);
		
		return searchParams;
	}
	
	/**
	 * Function to validate the phone number
	 * @param val
	 * @return
	 */
	private boolean validatePhone(String val) {
		if(!ESAPI.validator().isValidInput("phone", val, "HTTPParameterValue", 10, false, true))
			return false;
		
		if(val.length() != 10)
			return false;
		
		return true;
	}
	
	private boolean getDebugParam(HttpHeaders headers) {
	
		try {
		String val = headers.getRequestHeader(HttpHeader.Params.DEBUG_CALL.getValue()).get(0);
		if(val != null)
			return new Boolean(val);
		}catch(Exception E){}
		return false;
	}

}
