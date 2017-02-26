package com.bsro.api.rest.svc.impl;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import app.bsro.model.maintenance.MaintenanceVehicle;
import app.bsro.model.scheduled.maintenance.MaintenanceInvoice;
import app.bsro.model.scheduled.maintenance.ncd.rtq.JobResult;
import app.bsro.model.scheduled.maintenance.ncd.rtq.RtqRequest;
import app.bsro.model.scheduled.maintenance.ncd.rtq.VehicleSearchResult;

import com.bfrc.dataaccess.dao.generic.StoreDAO;
import com.bfrc.dataaccess.http.HttpResponse;
import com.bfrc.dataaccess.http.HttpUtils;
import com.bfrc.dataaccess.http.HttpConstants.ContentType;
import com.bfrc.dataaccess.http.HttpConstants.HttpHeaderParameters;
import com.bfrc.dataaccess.http.HttpConstants.MessageCharset;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.util.XmlUtils;
import com.bsro.api.rest.exception.ExternalServerError;
import com.bsro.api.rest.exception.WebServiceException;
import com.bsro.core.exception.ws.ServerProcessingException;
import com.bsro.api.rest.svc.MaintenanceHistoryService;
import com.bsro.api.rest.util.NcdDateUtils;

/**
 * Implementation of the NCD Service lookup.
 * 
 * @author Brad Balmer
 * 
 */
public class MaintenanceHistoryServiceImpl implements MaintenanceHistoryService {

//	private static Map<Integer, String> validProductTypes;
	private String ncdUri;
	private String ncdSoapPass;
	private String evValue;

	private String SOAP_HEADER;
	private String SOAP_FOOTER;
	private Logger log = Logger.getLogger(getClass().getName());

	private StoreDAO storeDao;
	private JdbcTemplate jdbcTemplate;
	private Map<Long, Store> stores = new HashMap<Long, Store>();
	
	private AsyncTaskExecutor taskExecutor;
	private int threadPoolSize;

	private static Map<String, String> soapHeaderParams;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Initialize a few properties. This function is called by spring after all
	 * properties have been set.
	 */
	public void setupMessage() {

		SOAP_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Header>" + "<AuthHeader xmlns=\"http://bfrco/\">"
				+ "<Password>" + this.ncdSoapPass + "</Password>"
				+ "</AuthHeader>" + "</soap:Header>" + "<soap:Body>"
				+ "<EDWQuery xmlns=\"http://bfrco/\"><XMLString>";

		SOAP_FOOTER = "</XMLString></EDWQuery></soap:Body></soap:Envelope>";

		// ncdSoapPass = new String(AES.decrypt(ncdSoapPass.getBytes(),
		// evValue.getBytes()));

	}

	static {

		// For the job coming back we only want to pull the following valid
		// product types to display
//		validProductTypes = new HashMap<Integer, String>(2);
//		validProductTypes.put(0, "Part");
//		validProductTypes.put(1, "Labor");
		// validProductTypes.put(5, "Note");

		soapHeaderParams = new HashMap<String, String>();
		soapHeaderParams.put(HttpHeaderParameters.SOAP_ACTION.getParam(),
				"http://bfrco/EDWQuery");
		soapHeaderParams.put(HttpHeaderParameters.CONTENT_TYPE.getParam(),
				ContentType.TEXT_XML.getContentType());

	}

	/**
	 * There may be a few product types that we do not want displayed on the
	 * devices (like notes).
	 * 
	 * @return
	 */
//	public static Map<Integer, String> getValidProductTypes() {
//		if (validProductTypes == null)
//			validProductTypes = new HashMap<Integer, String>();
//		return validProductTypes;
//	}

	/**
	 * Search for vehicles by phone number
	 */
	public List<MaintenanceVehicle> searchByPhone(List<String> phoneNumbers,
			boolean debug) throws WebServiceException,
			ServerProcessingException {

		Set<Integer> vehicleIds = new HashSet<Integer>();
		List<MaintenanceVehicle> loResult = new ArrayList<MaintenanceVehicle>();
		for (String phone : phoneNumbers) {
			if (phone != null && phone.length() == 10) {

				String area = phone.substring(0, 3);
				String exchange = phone.substring(3, 6);
				String line = phone.substring(6);

				// Generate the SOAP Body to send
				String xml = RtqRequest.getRtq2010((area), (exchange), (line));
				HttpResponse response = null;
				try {
					StringBuilder messageBody = new StringBuilder();
					messageBody.append(SOAP_HEADER);
					messageBody.append(xml);
					messageBody.append(SOAP_FOOTER);
					if(debug) log.severe(messageBody.toString());
					log.info(messageBody.toString());
					response = HttpUtils.post(this.ncdUri, messageBody.toString(), ContentType.TEXT_XML, MessageCharset.UTF8, soapHeaderParams);
					
					if (response.getStatusCode().intValue() > 299) {
						String message = "Received a non-successful status code of "
								+ response.getStatusCode()
								+ " with a body of "
								+ response.getResponseBody();
						log.info(message);
						throw new ExternalServerError(message);
					}
					
					if(debug) log.severe(response.toString());
					List<VehicleSearchResult> vsrs = parseVehicleSearchResult(response.getResponseBody());
					for (VehicleSearchResult vsr : vsrs) {
						if(!vehicleIds.contains(vsr.getVehicleId())) {
			
							MaintenanceVehicle mv = new MaintenanceVehicle(
									vsr.getCustomerId(), 
									vsr.getVehicleId(), 
									vsr.getYear(), 
									vsr.getMake(), 
									vsr.getModel(), 
									vsr.getSubModel());
							loResult.add(mv);
							vehicleIds.add(vsr.getVehicleId());
						}
					}
				} catch (JDOMException e) {
					log.severe("Error parsing XML: " + response.toString()
							+ ". " + e.getMessage());
					throw new ServerProcessingException(e);
				} catch (ExternalServerError E) {
					throw new ServerProcessingException(E);
				} catch (Exception E) {
					throw new WebServiceException(E);
				}
			}
		}
		vehicleIds = null;
		return loResult;
	}

	/**
	 * Search for vehicles by store number and invoice number
	 */
	public List<MaintenanceVehicle> searchByStoreInvoice(String storeNbr,
			String invoiceNbr, boolean debug) throws WebServiceException,
			ServerProcessingException {

		Set<Integer> vehicleIds = new HashSet<Integer>();
		List<MaintenanceVehicle> loResult = new ArrayList<MaintenanceVehicle>();

		// Generate the SOAP Body to send
		String xml = RtqRequest.getRtq2020(storeNbr, invoiceNbr);
		HttpResponse response = null;
		try {
			StringBuilder messageBody = new StringBuilder();
			messageBody.append(SOAP_HEADER);
			messageBody.append(xml);
			messageBody.append(SOAP_FOOTER);

			if(debug) log.severe(messageBody.toString());
			response = HttpUtils.post(this.ncdUri, messageBody.toString(), ContentType.TEXT_XML, MessageCharset.UTF8, soapHeaderParams);
			if(debug) log.severe(response.toString());
			
			if (response.getStatusCode().intValue() > 299)
				throw new ExternalServerError(
						"Received a non-successful status code of "
								+ response.getStatusCode() + " with a body of "
								+ response.getResponseBody());

			List<VehicleSearchResult> vsrs = parseVehicleSearchResult(response.getResponseBody());
			for (VehicleSearchResult vsr : vsrs) {
				if(!vehicleIds.contains(vsr.getVehicleId())) {
					loResult.add(new MaintenanceVehicle(
							vsr.getCustomerId(), 
							vsr.getVehicleId(), 
							vsr.getYear(), 
							vsr.getMake(), 
							vsr.getModel(), 
							vsr.getSubModel()));
					vehicleIds.add(vsr.getVehicleId());
				}
			}
		} catch (JDOMException e) {
			log.severe("Error parsing XML: " + response.toString() + ". "
					+ e.getMessage());
			throw new ServerProcessingException(e);
		} catch (ExternalServerError E) {
			throw new ServerProcessingException(E);
		} catch (Exception E) {
			throw new WebServiceException(E.getMessage());
		}
		vehicleIds = null;
		return loResult;
	}

	/**
	 * Search for invoices (jobs) by the vehicle id and the optional since date.
	 * Limit all results, looking at the store number, to the storeType passed
	 * in.
	 */
	public List<MaintenanceInvoice> retrieveInvoices(String vehicleId, Long sinceVal, String storeType, boolean debug, boolean filterResults)
			throws WebServiceException, ServerProcessingException {

		List<MaintenanceInvoice> loResult = new ArrayList<MaintenanceInvoice>();
		if (StringUtils.trimToNull(storeType) == null) {
			log.warning("null storeType passed to retrieve invoices.  Cannot process without it.");
			return null;
		}

		String fromDate = null;

		// sinceVal is NOT a required parameter so it may be null
		if (sinceVal != null && sinceVal.longValue() > 0)
			fromDate = NcdDateUtils.getNcdFromDt(sinceVal);

		// If there is no fromDate set the default it to a BSRO supplied value
		if (fromDate == null)
			fromDate = "1970-01-01";

		// Generate the SOAP Body to send
		String xml = RtqRequest.getRtq2030(vehicleId, fromDate);
		HttpResponse response = null;
		try {

			StringBuilder messageBody = new StringBuilder();
			messageBody.append(SOAP_HEADER);
			messageBody.append(xml);
			messageBody.append(SOAP_FOOTER);

			
			if (debug)
				log.severe("SENDING: " + messageBody.toString());
			response = HttpUtils.post(this.ncdUri, messageBody.toString(), ContentType.TEXT_XML, MessageCharset.UTF8, soapHeaderParams);
			if (response.getStatusCode().intValue() > 299) {
				throw new ExternalServerError(
						"Received a non-successful status code of "
								+ response.getStatusCode() + " with a body of "
								+ response.getResponseBody());
			}

			if (debug)
				log.severe("RECEIVED: " + response.toString());
			List<JobResult> jobs = parseJobResult(response.getResponseBody());

			if (jobs == null || jobs.size() == 0) {
				return null;
			}

			// We are NOT guaranteed that the jobs coming back will be in
			// invoice order. So, sort them initially before
			// processing them
			if (jobs.size() > 0)
				Collections.sort(jobs);

			loResult = processJobs(jobs, storeType, vehicleId, filterResults);
		} catch (JDOMException e) {
			log.severe("Error parsing XML: " + response.toString() + ". "
					+ e.getMessage());
			throw new ServerProcessingException(e);
		} catch (ExternalServerError E) {
			throw new ServerProcessingException(E);
		} catch (Exception E) {
			E.printStackTrace();
			return new ArrayList<MaintenanceInvoice>(0);
			// throw new WebServiceException(E.getMessage());
		}
		return loResult;
	}

	/**
	 * The data initially sent back by BSRO is normalized. Thus we need to break
	 * it up into the multiple header/footer sections to make it easier for the
	 * front-end devices to consume and use.
	 * 
	 * @param jobs
	 * @return
	 */
	private List<MaintenanceInvoice> processJobs(List<JobResult> jobs,
			String storeType, String vehicleId, boolean filterResults) {
		List<MaintenanceInvoice> loResult = new ArrayList<MaintenanceInvoice>(0);
		if (jobs == null || jobs.size() == 0)
			return null;
		Integer currentInvoiceNbr = new Integer(0);
		MaintenanceInvoice invoice = null;
		Store store = null;
		boolean hasShownError = false; //Only show the SQL Error ONCE instead of each time for values in the for loop
		for (JobResult job : jobs) {
			if (job != null) {
				// We need to make sure that ONLY job results are returned back
				// to the calling application
				// for the store type of the calling application. This needs to
				// be done as the NCD datasource
				// returns records across store types (FCAC, TP, ET) and we only
				// want to display jobs for the
				// particular store type.
				boolean includeJobResult = false;
				try {
					Long storeId = new Long(job.getStoreNbr());
					if (storeId != null) {
						// always hit the DB
//						Store store = storeDao.get(storeId);
//						if(store != null && store.getStoreType() != null)
//							includeJobResult = (StringUtils.trimToEmpty(storeType).equals(StringUtils.trimToEmpty(store.getStoreType())));
						
						//Need to switch from Hibernate to a straight JDBC calls because of the multi-threading issue of NOT having a session
						store = getStore(storeId);
//						String localStoreType = getStoreType(storeId);
						if (store != null) {
							//String localStoreType = store.getStoreType();
							//includeJobResult = (StringUtils.trimToEmpty(storeType).equals(StringUtils.trimToEmpty(localStoreType)));
							
							/*
							 * New logic to also include HTP records when the storeType is TP.  Include Hibdon records with Tires Plus
							 */
							List<String> localStores = new ArrayList<String>();
							String lStore = StringUtils.trimToEmpty(store.getStoreType());
							localStores.add(lStore);
							
							if("HTP".equals(lStore))
								localStores.add("TP");
							
							String mStore = StringUtils.trimToEmpty(storeType);
							includeJobResult = localStores.contains(mStore);
						}
						
					}
				} catch (Exception E) {
					if(!hasShownError)
						log.severe("Unable to find store from storeId: " + job.getStoreNbr() + " - "+E.getMessage());
				
					//Set to true so this statement is not displayed for each JobResult
					hasShownError = true;
				}

				//If we don't want to filter the results then just set to include in the job results
				if(!filterResults)
					includeJobResult = true;
				
				//Need to pass to the MaintenanceInvoice the type and name.  Pass null for these values if the store is not found
				String storeShortType = null;
				String storeName = null;
				if(store != null) {
					storeShortType = store.getStoreType();
					storeName = store.getStoreName();
				}
				
				if (includeJobResult) {
					if (job.getInvoiceNbr().equals(currentInvoiceNbr)) {
						// JobResult is from same invoice as prior JobResult
						invoice.addJob(job, vehicleId);
					} else {
						// New Invoice
						if (currentInvoiceNbr.intValue() == 0) {
							// Is this the very first JobResult
							invoice = new MaintenanceInvoice(job, vehicleId, storeShortType, storeName);
							currentInvoiceNbr = job.getInvoiceNbr();
						} else {
							loResult.add(invoice);
							invoice = new MaintenanceInvoice(job, vehicleId, storeShortType, storeName);
							currentInvoiceNbr = job.getInvoiceNbr();
						}
					}
				} 
			}
		}
		loResult.add(invoice);

		//Ok, this is 'kinda lazy but I added this to 100% ensure that we didn't pass up a null MaintenanceInvoice in the 
		// final List.
		List<MaintenanceInvoice> loFinal = new ArrayList<MaintenanceInvoice>();
		if(loResult == null || loResult.size() == 0) return null;
		for(MaintenanceInvoice mi : loResult) {
			if(mi != null)
				loFinal.add(mi);
		}
		loResult = null;
		return loFinal;
	}
	
	private Store getStore(Long storeId) {
		Store loResult = stores.get(storeId);
		if(loResult != null) return loResult;

		String SQL = "SELECT STORE_TYPE, STORE_NAME FROM STORE WHERE STORE_NUMBER = ?";
		Store store = null;
		try {
			store = (Store) this.jdbcTemplate.queryForObject(
				SQL, 
				new Object[]{storeId}, 
				  new RowMapper<Store>() {

			        public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
			            Store s = new Store();
			            s.setStoreType(rs.getString("STORE_TYPE"));
			            s.setStoreName(rs.getString("STORE_NAME"));
			            return s;
			        }
			    });
		}catch(Exception E) {}
		if(store != null) {
			stores.put(storeId, store);
			return store;
		} else {
			return null;
		}
		
	}

	/**
	 * Take in the raw HTTP SOAP response and convert it into a Listing of
	 * VehicleSearchResults Objects
	 * 
	 * @param result
	 * @return
	 */
	private List<VehicleSearchResult> parseVehicleSearchResult(String result)
			throws JDOMException, ExternalServerError, Exception {

		// result = cleanXml(result);
		SAXBuilder builder = new SAXBuilder();
		List<VehicleSearchResult> resultSet = new ArrayList<VehicleSearchResult>();

		org.jdom.Document document = (org.jdom.Document) builder
				.build(new StringReader(result));
		Element rootNode = document.getRootElement();
		Element edwrtqRecordSet = XmlUtils.findElement("EDWRTQRecordSet",
				rootNode);
		if (edwrtqRecordSet != null) {
			try {
				List records = edwrtqRecordSet.getChildren();
				for (int i = 0; i < records.size(); i++) {
					Element record = (Element) (records.get(i));
					List datas = record.getChildren();
					VehicleSearchResult v = new VehicleSearchResult();
					for (int d = 0; d < datas.size(); d++) {
						Element data = (Element) datas.get(d);
						if (data.getName().equals("XNCD_PARTYID"))
							v.setCustomerId(new Integer(data.getValue()));
						else if (data.getName().equals("XNCD_VEHICLEID"))
							v.setVehicleId(new Integer(data.getValue()));
						else if (data.getName().equals("XYEAR"))
							v.setYear(data.getValue());
						else if (data.getName().equals("XMAKE"))
							v.setMake(data.getValue());
						else if (data.getName().equals("XMODEL"))
							v.setModel(data.getValue());
						else if (data.getName().equals("XSUBMODEL"))
							v.setSubModel(data.getValue());
					}
					resultSet.add(v);
					log.fine(v.toString());

				}
			} catch (Exception E) {
				log.severe("Error parsing result: " + E.getMessage() + " - "
						+ result);
				throw new ExternalServerError(E);
			}
		} else {
			Element edwrtqReturn = XmlUtils.findElement("EDWRTQReturn",
					rootNode);
			if (edwrtqReturn != null) {
				log.info("No data returned");
				return resultSet;
			}
			checkFaultCode(rootNode);
		}
		return resultSet;
	}

	private void checkFaultCode(Element rootNode) throws ExternalServerError {
		Element fault = XmlUtils.findElement("Fault", rootNode);
		if (fault != null) {
			String errorMsg = "";
			List records = fault.getChildren();
			for (int i = 0; i < records.size(); i++) {
				Element record = (Element) (records.get(i));
				if (record.getName().equals("faultcode"))
					errorMsg += record.getValue() + " ";
				else if (record.getName().equals("faultstring"))
					errorMsg += record.getValue() + " ";
			}
			throw new ExternalServerError(errorMsg);
		} else {
			log.severe("Unknown response from server.  Cannot parse.");
			throw new ExternalServerError(
					"Unknown response from server.  Cannot parse.");
		}
	}

	/**
	 * Take in the raw HTTP SOAP response and convert it into a Listing of
	 * JobResult Objects
	 * 
	 * @param result
	 * @return
	 */
	private List<JobResult> parseJobResult(String result) throws JDOMException,
			ExternalServerError, Exception {

		result = cleanXml(result);

		SAXBuilder builder = new SAXBuilder();
		List<JobResult> resultSet = new ArrayList<JobResult>();

		org.jdom.Document document = (org.jdom.Document) builder
				.build(new StringReader(result));
		Element rootNode = document.getRootElement();
		Element edwrtqRecordSet = XmlUtils.findElement("EDWRTQRecordSet",
				rootNode);
		if (edwrtqRecordSet != null) {
			try {
				List records = edwrtqRecordSet.getChildren();
				for (int i = 0; i < records.size(); i++) {
					Element record = (Element) (records.get(i));
					List datas = record.getChildren();
					JobResult j = new JobResult();
					for (int d = 0; d < datas.size(); d++) {
						Element data = (Element) datas.get(d);

						if (data.getName().equals("XINVOICEDATE"))
							j.setInvoiceDate(data.getValue());
						else if (data.getName().equals("XINVOICENBR"))
							j.setInvoiceNbr(new Integer(data.getValue()));
						else if (data.getName().equals("XCOMPANY"))
							j.setCompanyIndicator(data.getValue());
						else if (data.getName().equals("XSTORENBR"))
							j.setStoreNbr(data.getValue());
						else if (data.getName().equals("XADDRESS"))
							j.setStoreAddress(data.getValue());
						else if (data.getName().equals("XCITY"))
							j.setStoreCity(data.getValue());
						else if (data.getName().equals("XSTATE"))
							j.setStoreState(data.getValue());
						else if (data.getName().equals("XZIPCODE"))
							j.setStoreZip(data.getValue());
						else if (data.getName().equals("XMILEAGE"))
							j.setMileage(new Integer(data.getValue()));
						else if (data.getName().equals("XINVOICEAMOUNT"))
							j.setInvoiceAmt(new Double(data.getValue()));
						else if (data.getName().equals("XJOBDESC"))
							j.setDescription(data.getValue());
						else if (data.getName().equals("XSTATUS"))
							j.setStatus(data.getValue());
						else if (data.getName().equals("XARTICLENBR"))
							j.setArticleNbr(new Integer(data.getValue()));
						else if (data.getName().equals("XSEQUENCENBR"))
							j.setSequencNbr(new Double(data.getValue()));
						else if (data.getName().equals("XITEMDESC"))
							j.setItemDescription(data.getValue());
						else if (data.getName().equals("XPRODTYPE"))
							j.setItemType(new Integer(data.getValue()));
						else if (data.getName().equals("XQUANTITY"))
							j.setQty(new Integer(data.getValue()));
						else if (data.getName().equals("XPRICE"))
							j.setItemPrice(new Double(data.getValue()));

					}
					resultSet.add(j);
					log.fine(j.toString());

				}
			} catch (Exception E) {
				log.severe("Error parsing result: " + E.getMessage() + " - "
						+ result);
			}
		} else {
			log.info("No EDWRTQRecordSet found: " + result);
			Element edwrtqReturn = XmlUtils.findElement("EDWRTQReturn",
					rootNode);
			if (edwrtqReturn != null) {
				log.info("No data returned");
				return new ArrayList<JobResult>(0);
			}
			checkFaultCode(rootNode);
		}
		return resultSet;
	}

	
	
	public List<MaintenanceVehicle> binding(List<MaintenanceVehicle> vehicles, final String storeType, final boolean debug, final boolean filterResults)
			throws WebServiceException, ServerProcessingException {
	
		if(vehicles == null || vehicles.size() == 0) return null;
		
		List<MaintenanceVehicle> goodResult = new ArrayList<MaintenanceVehicle>();
		
		if(debug)
			log.severe("Starting the binding of vehicles to invoices for "+vehicles.size()+ " vehicles");
		
		List<Future<Map<MaintenanceVehicle, List<MaintenanceInvoice>>>> futures = new ArrayList<Future<Map<MaintenanceVehicle, List<MaintenanceInvoice>>>>();
		for(final MaintenanceVehicle vehicle : vehicles) {
			
			Future<Map<MaintenanceVehicle, List<MaintenanceInvoice>>> future = taskExecutor.submit(new Callable<Map<MaintenanceVehicle, List<MaintenanceInvoice>>>() {
				
				public Map<MaintenanceVehicle, List<MaintenanceInvoice>> call() throws Exception {
					
					try {

						if(debug)
							log.severe("Retrieve for vehicle "+vehicle.getVehicleId());
						
							List<MaintenanceInvoice> invoices = retrieveInvoices(vehicle.getVehicleId().toString(), 0l, storeType, debug, filterResults);
						if(invoices != null && invoices.size() > 0) {
							if(debug) log.severe("  *** FOUND "+invoices.size()+" invoices to return for "+vehicle.getVehicleId());
							Map<MaintenanceVehicle, List<MaintenanceInvoice>> data = new HashMap<MaintenanceVehicle, List<MaintenanceInvoice>>();
							data.put(vehicle, invoices);
							return data;
						} else {
							if(debug) log.severe("  *** NO invoices to return for "+vehicle.getVehicleId());
							return null;
						}
					}catch(Exception E) {
						log.severe(E.getMessage());
					}
					return null;
				}
				
				
			});

			futures.add(future);
		}
		
		for(Future<Map<MaintenanceVehicle, List<MaintenanceInvoice>>> future : futures) {
			try {
				Map<MaintenanceVehicle, List<MaintenanceInvoice>> value = future.get();
				if(value != null) {
					Iterator<MaintenanceVehicle> iter = value.keySet().iterator();
					while(iter.hasNext()) {
						MaintenanceVehicle KEY = iter.next();
						KEY.setInvoices(value.get(KEY));
						
						goodResult.add(KEY);
					}
				}
			} catch (InterruptedException e) {
				log.severe("InterruptedException getting future value: "+e.getMessage());
			} catch (ExecutionException e) {
				log.severe("ExecutionException getting future value: "+e.getMessage());
			}
		}
	
		
		return goodResult;
	}
	
	

	public void setNcdUri(String ncdUri) {
		this.ncdUri = ncdUri;
	}

	public void setNcdSoapPass(String ncdSoapPass) {
		this.ncdSoapPass = ncdSoapPass;
	}

	public void setStoreDao(StoreDAO storeDao) {
		this.storeDao = storeDao;
	}

	public void setEvValue(String evValue) {
		this.evValue = evValue;
	}
	
	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	 /**
	  * I HATE doing this but I could not figure out a way to process the XML and clean it BEFORE
	  * loading it into a DOM.  There is a good chance that the data is bad and it just craps out because
	  * some of the xml contains & and < and > in the elements.
	  */
	private String cleanXml(String value) {
		
		// INITIAL CLEAN -
		value = value.replaceAll("&", "&amp;");
		 value = value.replaceAll("'", "&apos;");
		 value = value.replaceAll(">", "&gt;");
		 value = value.replaceAll("<", "&lt;");
		
		 //Put back the main elements
		 value = value.replaceFirst("&lt;", "<");
		 value = value.replaceFirst("&gt;", ">");
		
		 value = value.replaceAll("&lt;soap", "<soap");
		 value = value.replaceAll("\"&gt;", "\">");
		 value = value.replaceAll("Body&gt;", "Body>");
		 value = value.replaceAll("Envelope&gt;", "Envelope>");
		
		 String[] elements = new String[]{"EDWQueryResult","EDWRTQRecordSet",
		 "EDWRTQRecord", "XINVOICEDATE","XINVOICENBR","XCOMPANY",
		 "XSTORENBR","XADDRESS",
		 "XCITY","XSTATE","XZIPCODE","XMILEAGE","XINVOICEAMOUNT","XJOBDESC","XSTATUS","XARTICLENBR","XSEQUENCENBR",
		 "XITEMDESC","XPRODTYPE","XQUANTITY","XPRICE","records_processed",
		 "status_message"};
		
		 for(String e : elements) {
		 String badOne = "&lt;"+e+"&gt;";
		 String badTwo = "&lt;/"+e+"&gt;";
		 String goodOne = "<"+e+">";
		 String goodTwo = "</"+e+">";
		 value = value.replaceAll(badOne, goodOne);
		 value = value.replaceAll(badTwo, goodTwo);
		
		 }
		 value = value.replaceAll("&lt;EDW", "<EDW");
		 value = value.replaceAll("&lt;/EDWRTQReturn&gt;", "</EDWRTQReturn>");
		 value =
		 value.replaceAll("&lt;/EDWQueryResponse&gt;","</EDWQueryResponse>");
		 value = value.replaceAll("&lt;/soap:Body>","</soap:Body>");
		 value = value.replaceAll("&lt;/soap:Envelope>","</soap:Envelope>");
        
		return value;
	}

}
