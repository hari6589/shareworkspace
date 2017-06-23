package com.bsro.api.rest.ws.contact;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.JSONPObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.contact.ContactUs;
import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import app.bsro.model.wifi.WifiResponse_OUT;

import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.model.email.EmailSignup;
import com.bfrc.dataaccess.svc.contact.ContactUsService;
import com.bfrc.dataaccess.svc.mail.MailService;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bsro.core.exception.ws.EmptyDataSetException;
import com.bsro.core.model.HttpHeader;
import com.bsro.core.security.RequireValidToken;

import net.sf.json.util.JSONUtils;
import com.bfrc.framework.dao.EmailSignupDAO;
import com.bfrc.pojo.contact.Feedback;
import com.bfrc.pojo.fleetcare.NaApplication;
import com.bfrc.framework.dao.fleetcare.FleetCareOperator;
import com.bfrc.framework.dao.FleetCareDAO;

@Component
public class ContactUsWebServiceImpl implements ContactUsWebService {

	@Autowired
	private ContactUsService contactUsService;
	
	@Autowired
	private EmailSignupDAO emailSignupDAO;
	
	
	@Autowired
	private FleetCareDAO fleetCareDAO;
	
	@Autowired
	private MailService mailService;
	
	private final static String SUCCESS_RESPONSE_CODE = "000"; 
	private final static String SUCCESS_RESPONSE_MESSAGE = "Success";
	
	private final static String ERR_RESPONSE_CODE = "999"; 
	private final static String ERR_RESPONSE_MESSAGE = "Error";
	
	private final static String DUP_RESPONSE_CODE = "100"; 
	private final static String DUP_RESPONSE_MESSAGE = "Contact already exists";
	
	private final static String YES = "yes";
	private final static String NO = "no";
	
	@Override
	@RequireValidToken
	public List<ValueTextBean> subjectList(HttpHeaders headers) {
		
		String siteName = null;
		try {
			siteName = headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0);
		}catch(Exception E){siteName = null;}
		
		List<ValueTextBean> loData = contactUsService.listAllSubjects(siteName);
		if(loData == null ||loData.size() == 0)
			throw new EmptyDataSetException();
		
		return loData;
	}
	
/*	@Override
	@RequireValidToken
	public ContactUs createContactUsTest(HttpHeaders headers) {
		ContactUs us = new ContactUs();
		String storeId = null;
		
		us.setAddress("123 S. Main");
		us.setAddress2("Address 2");
		us.setCellPhone("3123332323");
		us.setCity("Glen Ellyn");
		us.setEmail("here@there.com");
		us.setEveningPhone("2344343333");
		us.setFeedbackId(170);
		us.setFirstName("Brad");
		us.setLastName("Balmer");
		us.setMessage("Some random message to send with this");
		us.setPhone("1323232222");
		us.setState("IL");
		us.setZip("60606");
		
		try {
			BSROWebServiceResponse resp = createContactUs(us, storeId, null, null, headers);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return us;
	}*/

	@Override
	@RequireValidToken
	public BSROWebServiceResponse createContactUs(Object object, String storeId, Long acesVehicleId,
			String siteName, boolean newFleet, HttpHeaders headers) {
		String appDevice = null;
		Long storeNumber = new Long(0);
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		try {
			if (siteName == null || siteName.isEmpty())
				siteName = headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0);
		}catch(Exception E){siteName = null;}
		try {
			appDevice = headers.getRequestHeader(HttpHeader.Params.APP_DEVICE.getValue()).get(0);
		}catch(Exception E){appDevice = null;}
		if(storeId != null) {
			if(NumberUtils.isNumber(storeId)){
				storeNumber = new Long(storeId);
			}
		}
		
		if (siteName == null || siteName.isEmpty()) {
			return getValidationMessage("Invalid site name input");			
		}
		
		if(newFleet)
		{
			NaApplication naApplication = null;
			ObjectMapper mapper = new ObjectMapper();
			if(object != null)
				naApplication = mapper.convertValue(object, NaApplication.class);			
			
			if (naApplication == null ) {
				String msg =  "Invalid inputs.";
				return getValidationMessage(msg);			
			}
			
			naApplication.setCreatedDate(new java.util.Date());
			fleetCareDAO.addApplication(naApplication);
			mailService.sendNewFleetRegistrationEmail(naApplication);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
			response.setMessage("Successfully Registered");
			response.setPayload(null);
		}
		else
		{
			ContactUs contactUs = null;
			String status = "";
			ObjectMapper mapper = new ObjectMapper();
			if(object != null)
				contactUs = mapper.convertValue(object, ContactUs.class);			
			
			if(siteName.equalsIgnoreCase("FFC")){
				String msg = "";
				if (contactUs == null ) {
					msg =  "Invalid inputs.";
					return getValidationMessage(msg);			
				}
				else if(ServerUtil.isNullOrEmpty(contactUs.getFeedbackId())){ 	
					
					msg = "Invalid FeedbackId Value.";
					return getValidationMessage(msg);			
				}
				else if(contactUs.getFeedbackId()==44  && ServerUtil.isNullOrEmpty(storeId)){
			
					msg = "Invalid StoreNumber";
					return getValidationMessage(msg);			
				}
				
				status = contactUsService.sendFleetCareContactUs(contactUs, siteName, storeNumber);
				if(!status.equalsIgnoreCase("success")){
					msg =  status;
					return getValidationMessage(msg);				
				}
				response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
				response.setMessage("Feedback posted successfully");
				response.setPayload(null);
				
				
			}
			else
			{
				
				
				if (contactUs == null || contactUs.getFeedbackId() <= 0) {
					String msg = (contactUs == null) ? "Invalid inputs." : (contactUs.getFeedbackId() <= 0) ? "Invalid feedback id" : "Invalid inputs.";
					return getValidationMessage(msg);			
				}
				
				contactUsService.sendContactUs(contactUs, siteName, storeNumber, acesVehicleId, appDevice);
				response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
				response.setMessage("Feedback posted successfully");
				response.setPayload(null);
			}
				
		}	
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse emailSignup(HttpHeaders headers, HttpServletRequest request,String siteName,
			String data){
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		try
		{
		JSONObject str = new JSONObject(data);
		EmailSignup emailSignup = new EmailSignup();
		
		String fName=null,lName=null,source= null,email= null,confirmEmail= null;
		
		if (str.has("email"))
			email = (String) str.get("email");
		if (str.has("confirmEmail"))
			confirmEmail = (String) str.get("confirmEmail");
		
		if (str.has("firstName"))
		fName = (String) str.get("firstName");
		if (str.has("lastName"))
		lName = (String) str.get("lastName");
		if (str.has("source"))
		source = (String) str.get("source");
		
		if (ServerUtil.isNullOrEmpty(fName) || ServerUtil.isNullOrEmpty(lName) || ServerUtil.isNullOrEmpty(source) || ServerUtil.isNullOrEmpty(siteName)|| ServerUtil.isNullOrEmpty(email)|| ServerUtil.isNullOrEmpty(confirmEmail)) 
		{
			String errmsg = getErrorMessage(fName,lName,source,siteName,email,confirmEmail);
			response = getValidationMessage(errmsg);
			return response;
		}
		else if(!email.equalsIgnoreCase(confirmEmail))
		{
			response = getValidationMessage("Email address and Confirm Email Address Should be same");
			return response;
		}
		else if(! email.trim().matches("^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$"))
		{
			response = getValidationMessage("Invalid Email Address");
			return response;
		}
		else
		{
		emailSignup.setFirstName(fName);
		emailSignup.setLastName(lName);
		emailSignup.setAddress1(str.has("address1") ? (String) str.get("address1") : null);
		emailSignup.setCity(str.has("city") ? (String) str.get("city") : null);
		emailSignup.setState(str.has("state") ? (String) str.get("state") : null);
		emailSignup.setZip(str.has("zip") ? (String) str.get("zip") : null);
		emailSignup.setEmailAddress(email);
		emailSignup.setSource(source);
		emailSignup.setCreatedDate(new Date());
		
		String signupCode = String.valueOf((int)(Math.random()*10000000));
		while(signupCode.length() < 10)
			signupCode += String.valueOf((int)(Math.random()*10000000));
		if(signupCode.length() > 10)
			signupCode = signupCode.substring(0, 10);
		emailSignup.setOptinCode(signupCode);
		
		Long id = contactUsService.createEmailSignup(emailSignup);
		
		if(id != null )
		{
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
		response.setMessage("Data inserted successfully");
		response.setPayload(emailSignup);
		}
		else
		{
			response = getValidationMessage("Data is not inseted");
		}
		}
		}catch(Exception e)
		{
			System.out.println(" Exception in  emailSignup ::"+e.getMessage());
		}
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse emailSignupFetch(HttpHeaders headers, HttpServletRequest request,String signUpId,
			String siteName){
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		EmailSignup emailSignup = null;
		try
		{
		if (ServerUtil.isNullOrEmpty(siteName) || ServerUtil.isNullOrEmpty(signUpId))
		{
			String errmsg = getErrorMessageForSignUp(signUpId,siteName);
			response = getValidationMessage(errmsg);
			return response;
		}
		else
		{
			emailSignup = contactUsService.getEmailSignup(Long.parseLong(signUpId));
				
		if(!ServerUtil.isNullOrEmpty(emailSignup))
		{
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
			response.setMessage("Data  Fetched Successfully");
			response.setPayload(emailSignup);
			return response;
		}
		else
		{
			response.setMessage("NoDataFound");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
		}
		}catch(Exception e)
		{
			response = getValidationMessage("Exception occured emailSignupFetch method ::"+e.getMessage());
		}
		return response;
	}
	
	@Override
	public String saveWifiContact(HttpHeaders headers, HttpServletRequest request, HttpServletResponse response, String firstName, String lastName,
			String emailAddress, String source, String callback, String optin) {
		EmailSignup emailSignup = new EmailSignup();
		WifiResponse_OUT out = new WifiResponse_OUT(SUCCESS_RESPONSE_CODE,SUCCESS_RESPONSE_MESSAGE);
		String jsonpStr = "";
		
		try {
			if(StringUtils.isNullOrEmpty(firstName) || 
					StringUtils.isNullOrEmpty(lastName) || 
					StringUtils.isNullOrEmpty(emailAddress) || 
					StringUtils.isNullOrEmpty(source)||
					StringUtils.isNullOrEmpty(optin)||
					StringUtils.isNullOrEmpty(callback)) {
				out.setResponseCode(ERR_RESPONSE_CODE);
				out.setResponseMessage(ERR_RESPONSE_MESSAGE);
			} else if(!isExistingContact(emailAddress)) {
				emailSignup.setFirstName(firstName);
				emailSignup.setLastName(lastName);
				emailSignup.setEmailAddress(emailAddress);
				emailSignup.setSource(source);
				emailSignup.setConfirmOptin(optin);
				emailSignup.setCreatedDate(new Date());
				
				Long id = contactUsService.createEmailSignup(emailSignup);
				if(id == null) {
					out.setResponseCode(ERR_RESPONSE_CODE);
					out.setResponseMessage(ERR_RESPONSE_MESSAGE);					
				}
			} else {
				out.setResponseCode(DUP_RESPONSE_CODE);
				out.setResponseMessage(DUP_RESPONSE_MESSAGE);
			}
			ObjectMapper objectMapper = new ObjectMapper();
			jsonpStr = objectMapper.writeValueAsString(new JSONPObject(callback,out));
		} catch(Exception e) {
			out.setResponseCode(ERR_RESPONSE_CODE);
			out.setResponseMessage(ERR_RESPONSE_MESSAGE);
		}
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");
		response.addHeader("Access-Control-Max-Age", "1234");
		return jsonpStr;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse emailSignupUnsubscribe(HttpHeaders headers, HttpServletRequest request,String emailId,
			String siteName){
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		boolean result = false;
		try
		{
		if (ServerUtil.isNullOrEmpty(siteName) && ServerUtil.isNullOrEmpty(emailId))
		{
			String errmsg = getErrorMessageForSignUp(emailId,siteName);
			response = getValidationMessage(errmsg);
			return response;
		}
		else
		{
			if(!ServerUtil.isNullOrEmpty(emailId))
				result = emailSignupDAO.unsubscribe(emailId,siteName);
				//result = emailSignupDAO.unsubscribe(Long.parseLong(signUpId));
			//else if(!ServerUtil.isNullOrEmpty(email))
			//	result = emailSignupDAO.unsubscribe(email);
				
		if(result)
		{
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
			response.setMessage("User UnSubscribed Successfully");
			response.setPayload(null);
		}
		else
		{
			response.setMessage("NoDataFound");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
		}
		}catch(Exception e)
		{
			response = getValidationMessage("Exception occured emailSignupUnsubscribe method ::"+e.getMessage());
		}
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse categoriesList(String siteName, HttpHeaders headers) {
		if (ServerUtil.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name input.");
		}
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		List categories = contactUsService.listAllCategories(siteName);
		
		if (categories == null || categories.isEmpty()) {
			response.setMessage("No Data Found");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
		response.setPayload(categories);
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse inquiriesList(String siteName, String categoryId, HttpHeaders headers) {
		if (ServerUtil.isNullOrEmpty(siteName)) {
			return getValidationMessage("Invalid site name input.");
		}
		
		if (ServerUtil.isNullOrEmpty(categoryId)) {
			return getValidationMessage("Invalid category id input.");
		}
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Integer catId = null;
		try {
			catId = Integer.valueOf(categoryId);
		} catch (Exception exp) {
			catId = Integer.valueOf("-1");
		}
		
		List<Feedback> feedbacks = contactUsService.listEnquiries(siteName, catId);
		if (feedbacks == null || feedbacks.isEmpty()) {
			response.setMessage("No Data Found");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
		response.setPayload(feedbacks);
		return response;
	}
	
	private boolean isExistingContact(String email)
	{
		boolean isExistingContact = false;
		
		if(emailSignupDAO.getEmailSignup(email).size() > 0)
		{
			isExistingContact = true;
		}
		return isExistingContact;
		
	}	
	
	private String getErrorMessage(String fName, String lName, String source, String siteName,String email, String confirmEmail) {
		StringBuffer buf = new StringBuffer();
		String errmsg = null;
		int errFieldsCount = 0;
		if (ServerUtil.isNullOrEmpty(fName)) {
				buf.append("FirstName");
				++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(lName)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("LastName");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(source)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Source");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(siteName)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("siteName");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(email)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Email");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(confirmEmail)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Confirm Email Address");
			++errFieldsCount;
		}
		
		errmsg = (errFieldsCount == 1) ? "Missing parameter value for field " : "Missing parameter value for fields ";
		errmsg += buf.toString();
		return errmsg;
	}
	
	private String getErrorMessageForSignUp(String emailId, String siteName) {
		StringBuffer buf = new StringBuffer();
		String errmsg = null;
		int errFieldsCount = 0;
		
		if (ServerUtil.isNullOrEmpty(emailId)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("emailId");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(siteName)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("siteName");
			++errFieldsCount;
		}
		
		errmsg = (errFieldsCount == 1) ? "Missing parameter value for field " : "Missing parameter value for fields ";
		errmsg += buf.toString();
		return errmsg;
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
}
