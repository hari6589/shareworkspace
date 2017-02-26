package com.bsro.api.rest.svc;

import java.util.List;

import app.bsro.model.maintenance.MaintenanceVehicle;
import app.bsro.model.scheduled.maintenance.MaintenanceInvoice;

import com.bsro.api.rest.exception.WebServiceException;
import com.bsro.core.exception.ws.ServerProcessingException;

/**
 * Defines the services available to query in to the BSRO NCD database.
 * @author Brad Balmer
 *
 */
public interface MaintenanceHistoryService {

	/**
	 * Searches the NCD system using up to three phone numbers
	 * @param phoneNumbers
	 * @return a listing of {@link MaintenanceVehicle} Objects
	 * @throws WebServiceException
	 * @throws ServerProcessingException
	 */
	public List<MaintenanceVehicle> searchByPhone(List<String> phoneNumbers, boolean debug) throws WebServiceException, ServerProcessingException;
	
	/**
	 * Searches the NCD system using a store id and an invoice number for the customer.
	 * @param storeNbr - the store number that the customer received service
	 * @param invoiceNbr - the invoice number for the customer
	 * @return a listing of {@link MaintenanceVehicle} Objects
	 * @throws WebServiceException
	 * @throws ServerProcessingException
	 */
	public List<MaintenanceVehicle> searchByStoreInvoice(String storeNbr, String invoiceNbr, boolean debug) throws WebServiceException, ServerProcessingException;
	
	/**
	 * Searches the NCD system using the vehicle id of the customer an an optional since value to limit the results.
	 * @param vehicleId - customer's individual vehicle id
	 * @param sinceVal - long value indicating the date (inclusive) to start the query from
	 * @param storeType - store type (FCAC, TP, ET, etc)
	 * @return
	 * @throws WebServiceException
	 * @throws ServerProcessingException
	 */
	public List<MaintenanceInvoice> retrieveInvoices(String vehicleId, Long sinceVal, String storeType, boolean debug, boolean filterResults) throws WebServiceException, ServerProcessingException;
	
	/**
	 * Takes the search results and matches invoices with them, thus returning ONLY vehicles with relevant (via STORE) invoices. 
	 * @param vehicles
	 * @param storeType
	 * @param debug
	 * @return
	 * @throws WebServiceException
	 * @throws ServerProcessingException
	 */
	public List<MaintenanceVehicle> binding(List<MaintenanceVehicle> vehicles, String storeType, boolean debug, boolean filterResults) throws WebServiceException, ServerProcessingException;
	
}
