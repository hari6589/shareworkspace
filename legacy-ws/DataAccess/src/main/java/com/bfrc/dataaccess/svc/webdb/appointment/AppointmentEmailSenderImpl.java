/**
 * 
 */
package com.bfrc.dataaccess.svc.webdb.appointment;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.bfrc.dataaccess.model.appointment.email.AppointmentDetails;
import com.bfrc.dataaccess.model.appointment.email.Attribute;
import com.bfrc.dataaccess.model.appointment.email.Attribute_;
import com.bfrc.dataaccess.model.appointment.email.Attributes;
import com.bfrc.dataaccess.model.appointment.email.Attributes_;
import com.bfrc.dataaccess.model.appointment.email.Column;
import com.bfrc.dataaccess.model.appointment.email.Columns;
import com.bfrc.dataaccess.model.appointment.email.Division;
import com.bfrc.dataaccess.model.appointment.email.Row;
import com.bfrc.dataaccess.model.appointment.email.Rows;
import com.bfrc.dataaccess.model.appointment.email.SendAppointmentData;
import com.bfrc.dataaccess.model.appointment.email.SideTable;
import com.bfrc.dataaccess.model.appointment.email.Subscriber;
import com.bfrc.dataaccess.model.appointment.email.SubscriberMessage;
import com.bfrc.dataaccess.model.appointment.email.Table;
import com.oreilly.servlet.Base64Encoder;

/**
 * @author schowdhury
 *
 */
@Deprecated
public class AppointmentEmailSenderImpl implements AppointmentEmailSender{
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private String endpoint;
	private String masterId;
	private String username;
	private String password;
	private String subscriptionState = "SUBSCRIBED";
	private String allowResubscribe = "true";
	private String statusNoWaitURI;
	private String customerEmail;
	private String storeEmail;

	private static final String APPOINTMENT_TABLE 			= "APPOINTMENT";
	private static final String APPOINTMENT_SERVICES_TABLE 	= "APPOINTMENT_SERVICES";
	private static final String STATUS_MESSAGE_NODE 		= "yesws:statusMessage";
	private static final String STATUS_CODE_NODE 			= "yesws:statusCode";
	
	private static final String HTTPS_PROXYHOST 	= "websense.bfr.bfrco.com";
	private static final String HTTPS_PROXYPORT 	= "8080";
	

	/**
	 * @return the masterId
	 */
	public String getMasterId() {
		return masterId;
	}

	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the statusNoWaitURI
	 */
	public String getStatusNoWaitURI() {
		return statusNoWaitURI;
	}

	/**
	 * @param statusNoWaitURI the statusNoWaitURI to set
	 */
	public void setStatusNoWaitURI(String statusNoWaitURI) {
		this.statusNoWaitURI = statusNoWaitURI;
	}
	/**
	 * @return the customerEmail
	 */
	public String getCustomerEmail() {
		return customerEmail;
	}

	/**
	 * @param customerEmail the customerEmail to set
	 */
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	/**
	 * @return the storeEmail
	 */
	public String getStoreEmail() {
		return storeEmail;
	}

	/**
	 * @param storeEmail the storeEmail to set
	 */
	public void setStoreEmail(String storeEmail) {
		this.storeEmail = storeEmail;
	}


	public String sendEmailData(AppointmentDetails appt, boolean isCustomer) {

		List<Column> apptColList = getAppointmentDetailsColumnList(appt, isCustomer);
		
		Columns apptCols = new Columns();
		apptCols.setColumns(apptColList);
		
		Row apptRow = new Row();
		apptRow.setColumns(apptCols);
		List<Row> apptRowList = new ArrayList<Row>();
		apptRowList.add(apptRow);
				
		Rows apptRows = new Rows();
		apptRows.setRows(apptRowList);	
		
		List<Row> apptSvcsRowList = new ArrayList<Row>();
		
		String[] services = appt.getSelectedServices().split(",");
		for(String serviceId : services){	
			Row apptSvcsRow = new Row();
			Columns apptSvcsCols = new Columns();
			List<Column> apptSvcsColList = getAppointmentServiceColumnList(appt, serviceId, isCustomer);
			apptSvcsCols.setColumns(apptSvcsColList);
			apptSvcsRow.setColumns(apptSvcsCols);
			apptSvcsRowList.add(apptSvcsRow);
		}	

		
		Rows apptSvcsRows = new Rows();
		apptSvcsRows.setRows(apptSvcsRowList);

		List<Table> tables = new ArrayList<Table>();		
		tables.add(new Table(APPOINTMENT_TABLE, apptRows));
		tables.add(new Table(APPOINTMENT_SERVICES_TABLE, apptSvcsRows));
				
		SideTable sideTable = new SideTable();
		sideTable.setTables(tables);
		
		Division division = new Division();
		division.setValue("Transactional");
		
		List<Attribute> attributeList = new ArrayList<Attribute>();
		if(isCustomer){
			if(!StringUtils.isEmpty(this.getCustomerEmail()) && this.getCustomerEmail().length() > 0){
				attributeList.add(new Attribute("email", this.getCustomerEmail()));	
			}else{
				attributeList.add(new Attribute("email", appt.getCustomerEmailAddress()));	
			}
		}else{
			if(!StringUtils.isEmpty(this.getStoreEmail()) && this.getStoreEmail().length() > 0){
				attributeList.add(new Attribute("email", this.getStoreEmail()));
			}else{
				attributeList.add(new Attribute("email", appt.getStoreEmail()));
			}
		}
		attributeList.add(new Attribute("firstName", appt.getCustomerFirstName()));
		attributeList.add(new Attribute("lastName", appt.getCustomerLastName()));
		attributeList.add(new Attribute("postalCode", appt.getCustomerZipCode()));
		//for customer use customer email address 
		if(isCustomer){
			attributeList.add(new Attribute("TransactionID", appt.getTransactionId()));
		}else{
			//for stores use store email address
			attributeList.add(new Attribute("TransactionID", "-"+appt.getTransactionId()));
		}
		
		Attributes attributes = new Attributes();
		attributes.setAttributes(attributeList);

		Subscriber subscriber = new Subscriber();
		subscriber.setDivision(division);
		subscriber.setAttributes(attributes);
		subscriber.setAllowResubscribe(allowResubscribe);
		subscriber.setSubscriptionState(subscriptionState);
		
		List<Attribute_> attribute_List = new ArrayList<Attribute_>();
		//stores will have negative transaction id
		if(isCustomer){
			attribute_List.add(new Attribute_("TRANSACTIONID", appt.getTransactionId()));
		}else{
			attribute_List.add(new Attribute_("TRANSACTIONID", "-"+appt.getTransactionId()));
		}
		
		Attributes_ attributes_ = new Attributes_();
		attributes_.setAttributes(attribute_List);
		
		SubscriberMessage subscriberMessage = new SubscriberMessage();
		subscriberMessage.setAttributes(attributes_);
		subscriberMessage.setMasterId(new Integer(masterId));
		
		SendAppointmentData apptData = new SendAppointmentData();
		apptData.setSubscriber(subscriber);
		apptData.setSubscriberMessage(subscriberMessage);
		apptData.setSideTable(sideTable);
				
		String apptJson="";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			apptJson = ow.writeValueAsString(apptData);
		} catch (JsonGenerationException e1) {
			System.err.println("JSONException while trying to get build request data");
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			System.err.println("JSON Mapping Exception while trying to get build request from pojo");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.err.println("IOException while trying to get build json request from pojo");
			e1.printStackTrace();
		}

		logger.debug("json="+apptJson);
		return callSubscribeAndSendAPI(apptJson);
	}
	
	public String callSubscribeAndSendAPI(String jsonPayload){
		DefaultHttpClient httpClient = null;
		HttpPost httpPost = null;
		String responseBody="";
		String trackingId ="";

		try {
			httpClient = new DefaultHttpClient();			
			httpPost = new HttpPost(endpoint);
			
			String credentials = username+":"+password;
			String encoding = Base64Encoder.encode(credentials);
			httpPost.addHeader("Authorization", "Basic " + encoding);
			
			System.setProperty("https.proxyHost", HTTPS_PROXYHOST);
			System.setProperty("https.proxyPort", HTTPS_PROXYPORT);

			HttpEntity entity = new StringEntity(jsonPayload);
			httpPost.setEntity(entity);
			httpPost.addHeader("Content-Type", "application/json");

			HttpResponse response = httpClient.execute(httpPost);
			logger.debug("Status code: " + response.getStatusLine().getStatusCode());
			
			//if(HttpStatus.SC_ACCEPTED || HttpStatus.SC_CREATED || HttpStatus)
			//process response
			HttpEntity responseEntity = response.getEntity();						
			try {
				if(responseEntity!=null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    logger.debug(responseBody);
				}
				JSONObject responseJson = new JSONObject(responseBody);
				trackingId = responseJson.getString("trackingId");
			} catch (JSONException e) {
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
		}
		
		httpClient.getConnectionManager().shutdown();
		return trackingId;
		
	}
	
	public String checkProcessingStatus(String trackingId){
		logger.debug("trackingId="+trackingId);

		if (StringUtils.isEmpty(trackingId)){
			return "";
		}
		String url = statusNoWaitURI+"/"+trackingId;
		String statusMsg = "";
		
		DefaultHttpClient httpClient = null;
		HttpGet httpGet = null;
		HttpResponse httpResponse = null;
		
		try {
			httpClient = new DefaultHttpClient();			
			httpGet = new HttpGet(url);
			
			String credentials = username+":"+password;
			String encoding = Base64Encoder.encode(credentials);
			httpGet.addHeader("Authorization", "Basic " + encoding);
			
			System.setProperty("https.proxyHost", HTTPS_PROXYHOST);
			System.setProperty("https.proxyPort", HTTPS_PROXYPORT);
			
			
			httpResponse = httpClient.execute(httpGet);
			HttpEntity responseEntity = httpResponse.getEntity();
			String responseBody = EntityUtils.toString(responseEntity);						
			logger.debug(responseBody);
			
			Document responseXML=null;
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				responseXML = builder.parse(new InputSource(new StringReader(responseBody)));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
			logger.debug("responseXML="+responseXML);
			NodeList statusCodeNodes = responseXML.getElementsByTagName(STATUS_CODE_NODE);
			NodeList statusMsgNodes  = responseXML.getElementsByTagName(STATUS_MESSAGE_NODE);
			if(statusCodeNodes.getLength() > 0)
				statusMsg = statusCodeNodes.item(0).getTextContent();
			if(statusMsgNodes.getLength() > 0)
				statusMsg += ": "+statusMsgNodes.item(0).getTextContent();
			
			//process response body
		} catch (ClientProtocolException e) {
			System.err.println("Protocol Exception is retrieving status message from subscription API");
			e.printStackTrace();
			httpClient.getConnectionManager().shutdown();
		} catch (IOException e) {
			System.err.println("IOException is retrieving status message from subscription API");
			e.printStackTrace();
			httpClient.getConnectionManager().shutdown();
		}
		httpClient.getConnectionManager().shutdown();
		return statusMsg;
	}
	
	private List<Column>  getAppointmentDetailsColumnList(AppointmentDetails apptDetails, boolean isCustomer){
		List<Column> apptColList = new ArrayList<Column>();

		Class<AppointmentDetails> apptDetailsClass = AppointmentDetails.class;
		Method[] methods = apptDetailsClass.getDeclaredMethods();

		int i = 0;
		for(Method method : methods){
			JsonProperty jsonProperty = method.getAnnotation(JsonProperty.class);
			if(jsonProperty != null){	    		
				try {
					String jsonName = jsonProperty.value();
					Object[] args = null;
					Object value = method.invoke(apptDetails, args);
					if(value != null && !StringUtils.isEmpty(String.valueOf(value))){
						Column col = null;
						//if this for store email the appointment and transaction id will be negative
						if(!isCustomer 
							&& (jsonName.equalsIgnoreCase("transactionId") || jsonName.equalsIgnoreCase("appointment_Id"))){
							col = new Column(jsonName, "-"+String.valueOf(value));
						}
						else{
							col = new Column(jsonName, String.valueOf(value));
						}
						apptColList.add(col);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					System.err.println("Illegal access exception in getAppointmentDetailsColumnList method in class "+ this.getClass().getSimpleName());
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					System.err.println("Error when trying to get method value in getAppointmentDetailsColumnList method in class "+ this.getClass().getSimpleName());
					e.printStackTrace();
				}
			}	
			i++;
		}
		
		return apptColList;
	}
	
	private List<Column>  getAppointmentServiceColumnList(AppointmentDetails apptDetails, String serviceId, boolean isCustomer){
		List<Column> apptSvcsColList = new ArrayList<Column>();
		//for customers the transactionIds are positive, for stores, the transaction IDs are negative
		if(isCustomer){
			apptSvcsColList.add(new Column("TRANSACTIONID",apptDetails.getTransactionId() ));
			apptSvcsColList.add(new Column("APPOINTMENT_ID", apptDetails.getAppointmentId()));
		}else{
			apptSvcsColList.add(new Column("TRANSACTIONID","-"+apptDetails.getTransactionId() ));
			apptSvcsColList.add(new Column("APPOINTMENT_ID", "-"+apptDetails.getAppointmentId()));
		}
		apptSvcsColList.add(new Column("SERVICES", serviceId));
		
		return apptSvcsColList;
	}
	
	

}
