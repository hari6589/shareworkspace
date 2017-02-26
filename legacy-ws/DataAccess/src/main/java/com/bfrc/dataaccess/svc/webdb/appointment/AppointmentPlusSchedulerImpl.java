/**
 * 
 */
package com.bfrc.dataaccess.svc.webdb.appointment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.appointment.AppointmentMetaData;
import app.bsro.model.appointment.AppointmentOpenDate;
import app.bsro.model.appointment.AppointmentServiceCategory;
import app.bsro.model.appointment.AppointmentServiceComparator;
import app.bsro.model.appointment.AvailableSlot;
import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;
import com.bfrc.dataaccess.dao.generic.AppointmentServiceDescDAO;
import com.bfrc.dataaccess.model.appointment.AppointmentData;
import com.bfrc.dataaccess.model.appointment.AppointmentServiceDesc;
import com.bfrc.dataaccess.util.JsonObjectMapper;
import com.oreilly.servlet.Base64Encoder;


/**
 * @author schowdhu
 *
 */
@Service("appointmentPlusScheduler")
public class AppointmentPlusSchedulerImpl implements AppointmentPlusScheduler{
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private String baseEndpoint; 							//= "https://ws3.appointment-plus.com";
	private String siteId;									//= "appointplus351/757";
	private String apiKey;									//= "ba182f4615869ffff097559cdec85c96e1543934";
	private String soTimeout;

	private static final String HTTPS_PROXYHOST 			= "prxy-ch-bfr.bfr.bfrco.com";
	private static final String HTTPS_PROXYPORT 			= "8080";
	private static final String URL_QUERY_PARAM_DELIMITER 	= "?";
	private static final String NAME_VALUE_PAIR_DELIMITER 	= "&";
	private static final String NAME_VALUE_DELIMITER 		= "=";
	private static final String GET_SERVICES_URI			= "/Services/GetServices";
	private static final String GET_LOCATIONS_URI			= "/Locations/GetLocations";
	private static final String BRIDGESTONE_RULES_URI		= "/Bridgestone/Rules";
	private static final String GET_OPEN_DATES_URI 			= "/Staff/GetOpenDates";
	private static final String GET_OPEN_SLOTS_URI 			= "/Bridgestone/GetOpenSlots";
	private static final String CREATE_APPOINTMENTS_URI		= "/Appointments/CreateAppointments";
	private static final String GET_CUSTOMERS_URI			= "/Customers/GetCustomers";
	private static final String CREATE_CUSTOMERS_URI		= "/Customers/CreateCustomers";
	private static final String GET_APPOINTMENT_STATUS_URI	= "/Appointments/GetAppointmentStatuses";
	private static final String AP_SITE_ID					= "site_id";
	private static final String AP_API_KEY					= "api_key";
	private static final String AP_RESPONSE_TYPE			= "response_type";
	private static final String AP_RESPONSE_TYPE_VALUE		= "json";
	private static final String API_CALL_SUCCESSFUL			= "success";
	private static final String API_CALL_FAILURE			= "fail";
	private static final String LOCATION					= "location";
	private static final String SERVICES					= "services";
	private static final String METADATA					= "metadata";
	private static final String OPEN_DATES					= "opendates";
	private static final String OPEN_SLOTS					= "openslots";
	private static final String BOOK_APPT					= "bookAppt";
	private static final String GET_CUSTOMER				= "getCustomer";
	private static final String GET_STATUS					= "getStatus";
	private static final String WAIT_FOR_SERVICE_COMPLETION = "wait";
	
	@Autowired
	private JsonObjectMapper jsonObjectMapper;
	
	@Autowired
	private AppointmentServiceDescDAO appointmentServiceDescDAO;
	
	@Autowired
	HibernateUtil hibernateUtil;

	public AppointmentPlusSchedulerImpl() {
	}
	
	private Long getLocationId(Long storeNumber){
		logger.info("Inside getLocationId API method sn = "+storeNumber);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			httpPost = new HttpPost(baseEndpoint+GET_LOCATIONS_URI);
			
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("store_number", String.valueOf(storeNumber)));
			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity != null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    BSROWebServiceResponse wsResponse = processAPIResponse(responseBody, LOCATION);
					httpClient.getConnectionManager().shutdown();	
					return new Long(String.valueOf(wsResponse.getPayload()));
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
	}
	
	public Collection<AppointmentServiceDesc> getServicesFromDB(Long storeNumber){
		logger.info("Inside getServicesFromDB API method sn = "+storeNumber);
		String hql = "from AppointmentServiceDesc where serviceType = 1 order by sortOrder";
		List<AppointmentServiceDesc> results = new ArrayList<AppointmentServiceDesc>();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Collection<AppointmentServiceDesc> coll 
			= (Collection<AppointmentServiceDesc>)appointmentServiceDescDAO.execOrmQuery(hql, null, new ArrayList());
		
		for(AppointmentServiceDesc serviceDesc : coll){
			serviceDesc.setAssignedCategory("");//reset to empty before setting value
			if(serviceDesc.getCategories() != null && !serviceDesc.getCategories().isEmpty())
					serviceDesc.setAssignedCategory(serviceDesc.getCategories().get(0).getCategoryDesc());
			results.add(serviceDesc);			
		}
		return results;
	}
	
	public BSROWebServiceResponse getServices(Long storeNumber){
		logger.info("Inside getServices API method sn = "+storeNumber);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		Long locationId = null;
		if(storeNumber != null){
			locationId = getLocationId(storeNumber);
			logger.info("LocationId="+locationId);
		}
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			httpPost = new HttpPost(baseEndpoint+GET_SERVICES_URI);
			StringBuilder params = new StringBuilder();
			if(storeNumber != null){
				postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("c_id", String.valueOf(locationId)));
				params = appendParameters(postParameters);
			}else{
				params = appendParameters(new ArrayList<NameValuePair>());
			}
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity != null) {
				    responseBody = EntityUtils.toString(responseEntity);
					httpClient.getConnectionManager().shutdown();
					return processAPIResponse(responseBody, SERVICES);
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
		
	}
	
	public BSROWebServiceResponse getBridgestoneAppointmentRules(Long storeNumber, String serviceDescCSV){		
		logger.info("Inside BridgestoneAppointmentRules API sn= "+ storeNumber + " serviceDesc = "+serviceDescCSV);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			httpPost = new HttpPost(baseEndpoint+BRIDGESTONE_RULES_URI);

			postParameters = new ArrayList<NameValuePair>();
			String paddedStore = ("000000" + String.valueOf(storeNumber));
			postParameters.add(new BasicNameValuePair("store_number", String.valueOf(paddedStore.substring(paddedStore.length()-6))));
			postParameters.add(new BasicNameValuePair("services", String.valueOf(serviceDescCSV)));

			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity!=null) {
				    responseBody = EntityUtils.toString(responseEntity);
					httpClient.getConnectionManager().shutdown();	
					return processAPIResponse(responseBody, METADATA);
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
	}
	
	public BSROWebServiceResponse getStaffOpenDates(Long locationId, String startDate, Integer numDays, Long employeeId){
		logger.info("Inside getStaffOpenDates API");
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			httpPost = new HttpPost(baseEndpoint+GET_OPEN_DATES_URI);

			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("num_days", String.valueOf(numDays)));
			postParameters.add(new BasicNameValuePair("start_date", String.valueOf(startDate)));
			postParameters.add(new BasicNameValuePair("c_id", String.valueOf(locationId)));
			postParameters.add(new BasicNameValuePair("employee_id", String.valueOf(employeeId)));

			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity!=null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    logger.info("responseBody="+responseBody);
				    httpClient.getConnectionManager().shutdown();
				    return processAPIResponse(responseBody, OPEN_DATES);
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
	
	}
	
	public BSROWebServiceResponse getAppointmentOpenSlots(Long locationId, Long employeeId, String startDate, 
			Integer startTimeInMins, Integer numDays, String primaryServiceId, String secondaryServiceIds){
		logger.info("Inside getAppointmentOpenSlots API");
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			httpPost = new HttpPost(baseEndpoint+GET_OPEN_SLOTS_URI);
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("num_days", String.valueOf(numDays)));
			postParameters.add(new BasicNameValuePair("c_id", String.valueOf(locationId)));
			postParameters.add(new BasicNameValuePair("employee_id", String.valueOf(employeeId)));
			postParameters.add(new BasicNameValuePair("start_date", String.valueOf(startDate)));
			postParameters.add(new BasicNameValuePair("start_time", String.valueOf(startTimeInMins)));
			postParameters.add(new BasicNameValuePair("service", String.valueOf(primaryServiceId)));
			if(secondaryServiceIds != null && !secondaryServiceIds.isEmpty())
				postParameters.add(new BasicNameValuePair("addons", String.valueOf(secondaryServiceIds)));
			postParameters.add(new BasicNameValuePair("show_duplicates", "no"));

			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity!=null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    httpClient.getConnectionManager().shutdown();
				    return processAPIResponse(responseBody, OPEN_SLOTS);
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
	}
	
	public Long getCustomer(String preferredPhoneNumber, String email, String lastName){
		logger.info("Inside getCustomer API "+ preferredPhoneNumber+" "+email);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			httpPost = new HttpPost(baseEndpoint+GET_CUSTOMERS_URI);
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("day_phone", preferredPhoneNumber));
			postParameters.add(new BasicNameValuePair("email", email));
			postParameters.add(new BasicNameValuePair("last_name", lastName));

			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity!=null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    BSROWebServiceResponse wsResponse = processAPIResponse(responseBody, GET_CUSTOMER);
				    logger.info("found customer Id="+String.valueOf(wsResponse.getPayload()));
				    httpClient.getConnectionManager().shutdown();
				    return (Long)wsResponse.getPayload();
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
	}

	public Long createCustomer(Long locationId,String firstName,
			String lastName, String email, String preferredPhoneNumber) {
		logger.info("Inside createCustomer API"+locationId+" "+firstName+ " "+lastName+" "+email+" "+preferredPhoneNumber);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			
			httpPost = new HttpPost(baseEndpoint+CREATE_CUSTOMERS_URI);
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("c_id", String.valueOf(locationId)));
			postParameters.add(new BasicNameValuePair("first_name", String.valueOf(firstName)));
			postParameters.add(new BasicNameValuePair("last_name", String.valueOf(lastName)));
			postParameters.add(new BasicNameValuePair("email", String.valueOf(email)));
			postParameters.add(new BasicNameValuePair("day_phone", String.valueOf(preferredPhoneNumber)));

			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity!=null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    httpClient.getConnectionManager().shutdown();
				    BSROWebServiceResponse wsResponse = processAPIResponse(responseBody, GET_CUSTOMER);
				    logger.info("created customerId="+String.valueOf(wsResponse.getPayload()));
				    return (Long)wsResponse.getPayload();
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
	}
	//Note: this is not required at the moment as the appointment status is being returned by 
	//      the meta-data method.
	private String getAppointmentStatus(Long statusId){
		logger.info("Inside getAppointmentStatus statusId = "+ statusId);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);				
			httpPost = new HttpPost(baseEndpoint+GET_APPOINTMENT_STATUS_URI);
			postParameters = new ArrayList<NameValuePair>();
			
			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity!=null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    httpClient.getConnectionManager().shutdown();
				    BSROWebServiceResponse wsResponse = processAPIResponse(responseBody, GET_STATUS);
				    @SuppressWarnings("unchecked")
					LinkedHashMap<Long,String> results = (LinkedHashMap<Long,String>)wsResponse.getPayload();
				    if(statusId != null){
				    	logger.info("id = "+ statusId + " status = "+ results.get(statusId));
				    	return results.get(statusId);
				    }
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		return null;
	}
	
	public BSROWebServiceResponse createAppointment(AppointmentData appointment, String selectedDate, 
			Integer timeInMins, String primaryServiceId, String additionalServiceIds) {
		logger.info("Inside createAppointment "+ appointment);
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		ArrayList<NameValuePair> postParameters;
		
		Long customerId = getCustomer(appointment.getCustomerDayTimePhone(), 
				appointment.getCustomerEmailAddress(), appointment.getCustomerLastName());
		logger.info("retrieved customer id="+customerId);

		if(customerId == null){
			//call to create a customer and return customer Id
			customerId = createCustomer(appointment.getLocationId(), appointment.getCustomerFirstName(), 
					appointment.getCustomerLastName(), appointment.getCustomerEmailAddress(), 
					appointment.getCustomerDayTimePhone());
			logger.info("created customer id="+customerId);
		}
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(this.getSoTimeout()));
		try {
			httpClient = new DefaultHttpClient(httpParams);	
			//if(!ServerUtil.isProduction()) {
			//	httpClient = WebClientDevWrapper.wrapClient(httpClient);
			//}
			httpPost = new HttpPost(baseEndpoint+CREATE_APPOINTMENTS_URI);
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("c_id", String.valueOf(appointment.getLocationId())));
			postParameters.add(new BasicNameValuePair("employee_id", String.valueOf(appointment.getEmployeeId())));
			postParameters.add(new BasicNameValuePair("customer_id", String.valueOf(customerId)));
			postParameters.add(new BasicNameValuePair("status_id", String.valueOf(appointment.getAppointmentStatusId())));
			postParameters.add(new BasicNameValuePair("date", selectedDate));
			postParameters.add(new BasicNameValuePair("start_time", String.valueOf(timeInMins)));
			postParameters.add(new BasicNameValuePair("service_id", primaryServiceId));
			if(additionalServiceIds != null && !additionalServiceIds.isEmpty())
				postParameters.add(new BasicNameValuePair("addons", additionalServiceIds));
			if(appointment.getQuoteId() != null && !appointment.getQuoteId().isEmpty())
				postParameters.add(new BasicNameValuePair("po_number", String.valueOf(appointment.getQuoteId())));
			//WI-3517: comments and miscellaneous repair notes are both categorized as customer notes in Appt Plus system
			
			if(appointment.geteCommRefNumber() != null && !appointment.geteCommRefNumber().isEmpty())
				postParameters.add(new BasicNameValuePair("vin", String.valueOf(appointment.geteCommRefNumber())));

			StringBuilder appointmentNotes = new StringBuilder();
			if(appointment.getCustomerNotes() != null && !appointment.getCustomerNotes().isEmpty()){
				appointmentNotes.append(appointment.getCustomerNotes());
				if(!appointment.getCustomerNotes().endsWith("."))
					appointmentNotes.append(".");
			}
			if(appointment.getComments() != null && !appointment.getComments().isEmpty()){
				appointmentNotes.append(appointment.getComments()+".");
				if(!appointment.getComments().endsWith("."))
					appointmentNotes.append(".");
			}
			//combine both notes as customer notes
			if(appointmentNotes.length() > 0)
				postParameters.add(new BasicNameValuePair("customer_notes", appointmentNotes.toString()));

			postParameters.add(new BasicNameValuePair("other_vehicle", appointment.getChoice().getDropWaitOption()));
			postParameters.add(new BasicNameValuePair("override", "true"));
			if(appointment.getChoice().getDropWaitOption().equalsIgnoreCase(WAIT_FOR_SERVICE_COMPLETION)){
				postParameters.add(new BasicNameValuePair("override", "false"));
			}
			postParameters.add(new BasicNameValuePair("aces_year", appointment.getVehicleYear()));
			postParameters.add(new BasicNameValuePair("aces_make", appointment.getVehicleMake()));
			postParameters.add(new BasicNameValuePair("aces_model", appointment.getVehicleModel()));
			postParameters.add(new BasicNameValuePair("override_aces_submodel", "true"));
			if(appointment.getVehicleSubmodel() != null && !appointment.getVehicleSubmodel().isEmpty())
				postParameters.add(new BasicNameValuePair("aces_submodel", appointment.getVehicleSubmodel()));
			if(appointment.getMileage() != null && !appointment.getMileage().isEmpty())
				postParameters.add(new BasicNameValuePair("odometer", appointment.getMileage()));

			StringBuilder params = appendParameters(postParameters);
			String finalUrl = httpPost.getURI().toString() + params.toString();
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			logger.info("httpost uri = "+httpPost.getURI().toString());
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			try {
				if(responseEntity	!=	null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    httpClient.getConnectionManager().shutdown();
				    BSROWebServiceResponse wsResponse = processAPIResponse(responseBody, BOOK_APPT);
				    
				    return wsResponse;
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API "+ this.getClass().getSimpleName());
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.err.println("Protocol Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception in calling Subscribe and Send API in class "+ this.getClass().getSimpleName());
			httpClient.getConnectionManager().shutdown();
			e.printStackTrace();
		}		
		return null;
	}
	
	@SuppressWarnings("unused")
	private HttpPost createHttpPostWithBasicAuth(String apiEndpoint){
		HttpPost httpPost = new HttpPost(apiEndpoint);
		String credentials = siteId+":"+apiKey;
		String encoding = Base64Encoder.encode(credentials);
		httpPost.addHeader("Authorization", "Basic " + encoding);
		
		return httpPost;
	}

	private BSROWebServiceResponse processAPIResponse(String responseBody, String method){
		//logger.info("Inside processAPIResponse and param: "+ responseBody);
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		org.json.JSONObject responseJson = null;
		try {
			responseJson = new org.json.JSONObject(responseBody);
			if(responseJson.getString("result").equalsIgnoreCase(API_CALL_SUCCESSFUL)
					&& !responseJson.getString("count").isEmpty()){
				if(responseJson.has("errors") && responseJson.getJSONArray("errors").length() > 0){
					org.json.JSONArray array = responseJson.getJSONArray("errors");
					Errors errors = new Errors();
					for(int i = 0; i < array.length(); i++){
						errors.getGlobalErrors().add(array.getString(i));
					}
					response.setErrors(errors);
					response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
				}else if(Integer.valueOf(responseJson.getString("count")) > 0
						&& responseJson.has("data") && responseJson.get("data") != null){
					if(method.equals(METADATA)){
						AppointmentMetaData mdata = (AppointmentMetaData)deSerializeJSON(responseJson.getString("data"), AppointmentMetaData.class);
						response.setPayload(mdata);
					}else if(method.equals(LOCATION)){
						org.json.JSONArray dataArray = responseJson.getJSONArray("data");
						Long locId = null;
						if(dataArray.length()>0 && !dataArray.isNull(0)){
							locId = dataArray.getJSONObject(0).getLong("location_id");
							logger.info("locId = "+locId);
						}
						response.setPayload(locId);	
						
					}else if(method.equals(SERVICES)){
						org.json.JSONArray dataArray = responseJson.getJSONArray("data");
						SortedSet<AppointmentServiceCategory> serviceCats = new TreeSet<AppointmentServiceCategory>(new AppointmentServiceComparator());
						for (int i =0; i < dataArray.length(); i++) {
							String serviceDesc = dataArray.getJSONObject(i).getString("title");
							String display = dataArray.getJSONObject(i).getString("display");
							Integer serviceType = dataArray.getJSONObject(i).getInt("service_type");
							//filtering results based on service type and display level
							if(!serviceDesc.equalsIgnoreCase("State Inspection") && display.equalsIgnoreCase("yes") && serviceType == 1){
								AppointmentServiceCategory serviceCat = new AppointmentServiceCategory(
										dataArray.getJSONObject(i).getLong("service_id"),serviceDesc);
								//TODO: this is hard-coded currently, but needs to come from the service(iteration-2)
								if(serviceDesc.equalsIgnoreCase("Oil & Filter Change")){serviceCat.setSortOrder(1);}
								if(serviceDesc.equalsIgnoreCase("Tire Replacement")){serviceCat.setSortOrder(2);}
								if(serviceDesc.equalsIgnoreCase("Tire Rotation")){serviceCat.setSortOrder(3);}
								if(serviceDesc.equalsIgnoreCase("Wheel Balance")){serviceCat.setSortOrder(4);}
								if(serviceDesc.equalsIgnoreCase("Wheel Alignment")){serviceCat.setSortOrder(5);}
								if(serviceDesc.equalsIgnoreCase("Flat Repair")){serviceCat.setSortOrder(6);}
								if(serviceDesc.equalsIgnoreCase("Brakes")){serviceCat.setSortOrder(7);}
								if(serviceDesc.equalsIgnoreCase("Starting & Charging/Battery")){serviceCat.setSortOrder(8);}
								if(serviceDesc.equalsIgnoreCase("Complete Vehicle Inspection")){serviceCat.setSortOrder(9);}
								if(serviceDesc.equalsIgnoreCase("Wiper Blades")){serviceCat.setSortOrder(11);}
								if(serviceDesc.equalsIgnoreCase("Air Filter & Cabin Air Filter")){serviceCat.setSortOrder(12);}
								if(serviceDesc.equalsIgnoreCase("Lighting & Bulbs")){serviceCat.setSortOrder(13);}
								if(serviceDesc.equalsIgnoreCase("Air Conditioning Service")){serviceCat.setSortOrder(14);}
								if(serviceDesc.equalsIgnoreCase("Engine Tune-up")){serviceCat.setSortOrder(15);}
								if(serviceDesc.equalsIgnoreCase("Manufacturer Scheduled Maintenance")){serviceCat.setSortOrder(16);}
								if(serviceDesc.equalsIgnoreCase("Coolant Fluid Exchange")){serviceCat.setSortOrder(17);}
								if(serviceDesc.equalsIgnoreCase("Brake Fluid Exchange")){serviceCat.setSortOrder(18);}
								if(serviceDesc.equalsIgnoreCase("Transmission Fluid Service")){serviceCat.setSortOrder(19);}
								if(serviceDesc.equalsIgnoreCase("Other")){serviceCat.setSortOrder(20);}
								if(serviceDesc.equalsIgnoreCase("Tire Rotation") || serviceDesc.equalsIgnoreCase("Tire Replacement")
										|| serviceDesc.equalsIgnoreCase("Wheel Balance") || serviceDesc.equalsIgnoreCase("Wheel Alignment")
										|| serviceDesc.equalsIgnoreCase("Flat Repair")){
									serviceCat.setServiceCategory("Tires");
								}else if(serviceDesc.equalsIgnoreCase("Air Conditioning Service") || serviceDesc.equalsIgnoreCase("Complete Vehicle Inspection")
										|| serviceDesc.equalsIgnoreCase("Wiper Blades")
										|| serviceDesc.equalsIgnoreCase("Air Filter & Cabin Air Filter") || serviceDesc.equalsIgnoreCase("Lighting & Bulbs")
										|| serviceDesc.equalsIgnoreCase("Engine Tune-up") || serviceDesc.equalsIgnoreCase("Manufacturer Scheduled Maintenance")
										|| serviceDesc.equalsIgnoreCase("Coolant Fluid Exchange") || serviceDesc.equalsIgnoreCase("Brake Fluid Exchange")
										|| serviceDesc.equalsIgnoreCase("Transmission Fluid Service")){
									serviceCat.setServiceCategory("Repair & Other Maintenance");
								}else if(serviceDesc.equalsIgnoreCase("Other"))
									serviceCat.setServiceCategory("Miscellaneous Repair");
								else
									serviceCat.setServiceCategory("");
								serviceCats.add(serviceCat);
							}
						}
						logger.info("serviceCats.size = "+serviceCats.size());
						response.setPayload(serviceCats);
					}else if(method.equals(OPEN_DATES)){
						org.json.JSONArray dataArray = responseJson.getJSONArray("data");
						List<AppointmentOpenDate> openDates = new ArrayList<AppointmentOpenDate>();
						for (int i =0; i < dataArray.length(); i++) {
							AppointmentOpenDate openDate = new AppointmentOpenDate(
									dataArray.getJSONObject(i).getString("date"),
									dataArray.getJSONObject(i).getString("month"),
									dataArray.getJSONObject(i).getString("day"),
									dataArray.getJSONObject(i).getString("year"));
							openDate.setDay_Name(dataArray.getJSONObject(i).getString("day_name"));
							openDates.add(openDate);
						}
						logger.info("openDates.size = "+openDates.size());
						response.setPayload(openDates);
					}else if(method.equals(OPEN_SLOTS)){
						org.json.JSONArray dataArray = responseJson.getJSONArray("data");
						List<AvailableSlot> slots = new ArrayList<AvailableSlot>();
						for (int i =0; i < dataArray.length(); i++) {
							AvailableSlot slot = new AvailableSlot();
							slot.setDate(dataArray.getJSONObject(i).getString("date"));
							//-------------------------------------------------------------
							// Date time manipulation: convert total mins to hour-mins
							// Ex- 480 = 8:00 am and 1230 = 8:30 pm
							//-------------------------------------------------------------
							int startTime = dataArray.getJSONObject(i).getInt("start_time");
							int hour = Math.abs(new Integer(startTime/60));
							int mins = startTime%60;
							String paddedMins = ("00"+mins).substring(("00"+mins).length()-2);
							String am_pm = "am";
							if(hour > 11){
								am_pm = "pm";
								if(hour >= 13){
									hour = hour - 12;
								}
							}
							slot.setStart_Time(hour +":"+ paddedMins + am_pm);
							if(dataArray.getJSONObject(i).has("employee_id"))
								slot.setEmployeeId(dataArray.getJSONObject(i).getString("employee_id"));
							if(dataArray.getJSONObject(i).has("room_id"))
								slot.setRoomId(dataArray.getJSONObject(i).getString("room_id"));
							
							slots.add(slot);
						}
						logger.info("slots.size = "+slots.size());
						response.setPayload(slots);
					}else if(method.equals(GET_CUSTOMER)){
						org.json.JSONObject data = responseJson.optJSONObject("data");
						Long customerId = null;
						if(data != null){
							customerId = data.getLong("customer_id");
						}else{
							logger.warn("More than one customer found with the same phone number / email");
							org.json.JSONArray dataArr = responseJson.optJSONArray("data");
							for (int i = 0; i < dataArr.length(); i++) {
								org.json.JSONObject obj = dataArr.getJSONObject(i);
								customerId = obj.getLong("customer_id");
								if(customerId != null){
									break;
								}
							}
						}
						logger.info("customer_id="+customerId);
						response.setPayload(customerId);
					}else if(method.equals(GET_STATUS)){
						org.json.JSONArray dataArray = responseJson.getJSONArray("data");
						LinkedHashMap<Long,String> statuses = new LinkedHashMap<Long,String>();
						for (int i =0; i < dataArray.length(); i++) {
							statuses.put(dataArray.getJSONObject(i).getLong("status_id"),dataArray.getJSONObject(i).getString("description"));
						}	
						response.setPayload(statuses);
					}else if(method.equals(BOOK_APPT)){
						org.json.JSONObject obj = responseJson.getJSONObject("data");
						logger.info("bookApptJson===="+obj.toString(1));
						AppointmentData confirmAppt = new AppointmentData();
						confirmAppt.setAppointmentId(obj.getLong("appt_id"));
						confirmAppt.setLocationId(obj.getLong("c_id"));
						confirmAppt.setCustomerId(obj.getLong("customer_id"));
						confirmAppt.setEmployeeId(obj.getLong("employee_id"));
						confirmAppt.setRoomId(obj.getLong("employee_id"));
						confirmAppt.setSelectedServices(obj.getString("service_id") + obj.getString("addons")==null?"":","+obj.getString("addons"));
						confirmAppt.setCustomerId(obj.getLong("customer_id"));
						confirmAppt.setComments(obj.getString("customer_notes"));
						confirmAppt.setAppointmentStatusId(obj.getLong("status_id"));
						confirmAppt.setQuoteId(obj.getString("po_number"));
						confirmAppt.seteCommRefNumber(obj.getString("vin"));
						response.setPayload(confirmAppt);
					}
					response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
				}
			}else if(responseJson.getString("result").equalsIgnoreCase(API_CALL_FAILURE)){
				if(responseJson.has("errors")
						&& responseJson.getJSONArray("errors").length() > 0){
					org.json.JSONArray array = responseJson.getJSONArray("errors");
					Errors errors = new Errors();
					for(int i = 0; i < array.length(); i++){
						errors.getGlobalErrors().add(array.getString(i));
					}
					response.setErrors(errors);
				}				
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			}else{
				response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.name());
				response.setMessage("Error while trying to process API response");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			System.err.println("JsonException while trying to parse response"+e.toString());
			response.setMessage("Error while trying to parse API response");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
		} catch (Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object deSerializeJSON(String json, Class clazz){
		logger.info("Inside deSerializeJSON");
		Object obj = null;
		try {
			obj = jsonObjectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	private StringBuilder appendParameters(List<NameValuePair> parameters) throws UnsupportedEncodingException {
		StringBuilder paramBuilder = new StringBuilder(); 
		paramBuilder.append(URL_QUERY_PARAM_DELIMITER).append(AP_SITE_ID);
		paramBuilder.append(NAME_VALUE_DELIMITER).append(URLEncoder.encode(siteId,"UTF-8"));
		paramBuilder.append(NAME_VALUE_PAIR_DELIMITER).append(AP_API_KEY);
		paramBuilder.append(NAME_VALUE_DELIMITER).append(URLEncoder.encode(apiKey,"UTF-8"));
		paramBuilder.append(NAME_VALUE_PAIR_DELIMITER).append(AP_RESPONSE_TYPE);
		paramBuilder.append(NAME_VALUE_DELIMITER).append(URLEncoder.encode(AP_RESPONSE_TYPE_VALUE,"UTF-8"));
		
		for (NameValuePair nameValuePair : parameters) {
			paramBuilder.append(NAME_VALUE_PAIR_DELIMITER).append(nameValuePair.getName());
			paramBuilder.append(NAME_VALUE_DELIMITER).append(URLEncoder.encode(nameValuePair.getValue(),"UTF-8"));
		}
		return paramBuilder;
	}


	/**
	 * @return the baseEndpoint
	 */
	public String getBaseEndpoint() {
		return baseEndpoint;
	}
	/**
	 * @param baseEndpoint the baseEndpoint to set
	 */
	public void setBaseEndpoint(String baseEndpoint) {
		this.baseEndpoint = baseEndpoint;
	}


	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}


	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}


	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}


	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public String getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(String soTimeout) {
		this.soTimeout = soTimeout;
	}

	/**
	 * @param jsonObjectMapper the jsonObjectMapper to set
	 */
	public void setJsonObjectMapper(JsonObjectMapper jsonObjectMapper) {
		this.jsonObjectMapper = jsonObjectMapper;
	}


}
