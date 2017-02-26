/**
 * 
 */
package com.bsro.api.rest.ws.user;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import net.sf.json.util.JSONUtils;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.svc.myprofile.BFSUserDataService;
import com.bfrc.dataaccess.svc.myprofile.BFSUserProfileService;
import com.bfrc.dataaccess.util.ConvertResponseToJson;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.util.ValidatorUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bsro.core.model.HttpHeader;
import com.bsro.core.security.RequireValidToken;
import com.bsro.core.validation.ErrorList;
import com.bsro.core.validation.Validator;

/**
 * @author schowdhu
 *
 */
@Component
public class UserProfileWebServiceImpl implements UserProfileWebService {
	
	private Logger log = Logger.getLogger(getClass().getName());
	
	@Autowired
	private BFSUserProfileService bfsUserProfileService;
	
	@Autowired
	private BFSUserDataService bfsUserDataService;
	
	@Autowired
	private ConvertResponseToJson convertResponseToJson;
//	
//	@Autowired
//	LoginAuthConfig loginAuthConfig;

	@Override
	@RequireValidToken
	public BSROWebServiceResponse addUser(HttpHeaders headers, HttpServletRequest request,
			String data) {
		
		log.fine("Inside addUser");
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.registerUserProfile(data, appName);
	}


	@Override
	@RequireValidToken
	public BSROWebServiceResponse getUser(String email, HttpServletRequest request,
			HttpHeaders headers) {
		log.fine("Inside getUser");
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.getUserProfile(email, appName);
	}


	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateUser(HttpHeaders headers, HttpServletRequest request,
			String data) {
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.updateUserProfile(data, appName);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeUser(String email, HttpHeaders headers, HttpServletRequest request) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.removeUser(email, appName);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse authenticateUser(HttpHeaders headers, HttpServletRequest request,
			String email, String password) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		String response = bfsUserDataService.authenticateUser(email, password, appName);
		if(response.toLowerCase().indexOf("success") != -1){
			return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse authenticateUserWithFB(HttpHeaders headers, HttpServletRequest request,
				final String appId, final String redirectUri){
		return null;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse authenticateUserWithGoogle(HttpHeaders headers, HttpServletRequest request,
				final String appId, final String redirectUri){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateUser(HttpHeaders headers, HttpServletRequest request,
			String oldEmail, String newEmail, String oldPassword,
			String newPassword) {
		if(!ValidatorUtils.isEmailValid(oldEmail) || !ValidatorUtils.isEmailValid(newEmail))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		String response = bfsUserDataService.updateUser(oldEmail, newEmail, oldPassword, newPassword, appName);
		if(response.toLowerCase().indexOf("success") != -1){
			return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse forgotPassword(HttpHeaders headers, HttpServletRequest request, String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);

		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		String response = bfsUserDataService.resetUserPassword(email, appName);
		
		if(response.toLowerCase().indexOf("success") != -1){
			return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse dataExists(HttpHeaders headers, HttpServletRequest request, String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);

		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		String response = bfsUserDataService.doesUserDataExist(email, appName);
		
		if(response.toLowerCase().indexOf("success") != -1){
			return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse backupData(HttpHeaders headers, HttpServletRequest request, 
			String email, String data) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		String response = bfsUserDataService.saveUserData(data, email, appName);
		
		if(response.toLowerCase().indexOf("success") != -1){
			return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse restoreData(HttpHeaders headers, HttpServletRequest request, String email) {

		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		String response = bfsUserDataService.getUserData(email, appName);
		
		if(response.toLowerCase().indexOf("success") != -1){
			return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse restoreSecuredData(HttpHeaders headers, HttpServletRequest request,
			String email, String password){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		String response = bfsUserDataService.getUserData(email, password, appName);
		
		if(response.toLowerCase().indexOf("success") != -1){
			return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return bfsUserProfileService.getJsonMessage(response,BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		
	}
	

	@Override
	public BSROWebServiceResponse registerAdditionalDriver(HttpHeaders headers,
			HttpServletRequest request, String data) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	@RequireValidToken
	public BSROWebServiceResponse addVehicle(HttpHeaders headers, HttpServletRequest request,
			String email,  String data) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		String year="";
		String make="";
		String model="";
		String submodel="";
		try {
			JSONObject jObj = new JSONObject(data);
			year = jObj.getString("year");
			make = jObj.getString("make");
			model = jObj.getString("model");
			submodel = jObj.getString("submodel");
		} catch (JSONException e) {
			System.err.println("JSON exception while trying to parse vehicle data");
		}
		
		if(year == null || year.isEmpty() || make == null || make.isEmpty() ||
				model == null || model.isEmpty() || submodel == null || submodel.isEmpty()){
			return getValidationMessage(ValidationConstants.INVALID_DATA);
		}
		ErrorList errors = new ErrorList();
		if(!Validator.isValidInteger("year", year, 1, Integer.MAX_VALUE, false, errors)
				|| !Validator.isValidString("make", make, false, errors)
				|| !Validator.isValidString("model", model, false, errors)
				|| !Validator.isValidString("submodel", submodel, false, errors)){
			return getValidationErrors(errors);
		}

		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.registerNewVehicle(appName,email, new Integer(year), make, model, submodel, data);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getVehicles(String email, HttpServletRequest request,
			HttpHeaders headers) {
		log.fine("Inside getVehicles");

		if(!ValidatorUtils.isEmailValid(email) )
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.getAllVehicles(email, appName);
	}
	

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getVehicle(String email, String acesVehicleId, String vinNumber,
			HttpServletRequest request,
			HttpHeaders headers) {
		log.fine("Inside getVehicles");
		ErrorList errors = new ErrorList();
		
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!Validator.isValidLong("acesVehicleId", acesVehicleId, Long.MIN_VALUE, Long.MAX_VALUE, false, errors)){
			return getValidationErrors(errors);
		}
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}

		return bfsUserProfileService.getVehicle(email, appName, new Long(acesVehicleId), vinNumber);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getVehicleConfigOptions(final String email,
			final String acesVehicleId, HttpHeaders headers){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.getVehicleConfigurationOptions(email, appName, new Long(acesVehicleId));
		
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateVehicle(HttpHeaders headers,
			HttpServletRequest request, String email, String data) {
		
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.updateVehicle(email, appName, data);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeVehicle(String email, String acesVehicleId, String vinNumber,
			HttpServletRequest request, HttpHeaders headers) {
		ErrorList errors = new ErrorList();
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!Validator.isValidLong("acesVehicleId", acesVehicleId, Long.MIN_VALUE, Long.MAX_VALUE, false, errors)){
			return getValidationErrors(errors);
		}
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.removeVehicle(email, appName, new Long(acesVehicleId), vinNumber);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getUserVehicleMaintenanceHistory(HttpHeaders headers, 
			HttpServletRequest request, String email, String acesVehicleId, String vinNumber) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.getVehicleMaintenanceHistory(email, appName, new Long(acesVehicleId), vinNumber);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveUserVehicleMaintenanceHistory(HttpHeaders headers,
			HttpServletRequest request, String email, String sinceDate, 
			String acesVehicleId, String vinNumber, String jsonData) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		Date downloadDate = new Date();

		if(!ESAPI.validator().isValidDate("sinceDate", sinceDate, new SimpleDateFormat("MM-dd-yyyy"), true)){
			return getValidationMessage(ValidationConstants.INVALID_DATE_FORMAT);
		}else{
			if(sinceDate != null && !sinceDate.isEmpty()){
				try {
					SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
					downloadDate = format.parse(sinceDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bfsUserProfileService.saveVehicleMaintenanceHistory(email, appName, new Long(acesVehicleId), 
								vinNumber, jsonData, downloadDate);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getVehicleGasFillUp(final String email,
							 final String acesVehicleId,
							 String vinNumber,
							 HttpServletRequest request, HttpHeaders headers){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.getVehicleGasFillup(email, appName, new Long(acesVehicleId), vinNumber);
	}
	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveVehicleGasFillUp(final String email,
							final String acesVehicleId, String vinNumber,
							 final String data,HttpServletRequest request,HttpHeaders headers){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.addVehicleGasFillup(email, appName, new Long(acesVehicleId), vinNumber, data);
	}
	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateVehicleGasFillUp(final String email,
							 final String acesVehicleId, String vinNumber,
							 final String data,HttpServletRequest request, HttpHeaders headers){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.updateVehicleGasFillup(email, appName, new Long(acesVehicleId), vinNumber, data);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getMaintenanceServicePerformed(String email,
			 String acesVehicleId, String vinNumber, HttpServletRequest request, HttpHeaders headers){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.getServicePerformed(email, appName, new Long(acesVehicleId), vinNumber);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveMaintenanceServicePerformed(String email,
			 String acesVehicleId, String vinNumber,
			 String data, HttpServletRequest request, HttpHeaders headers){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.addServicePerformed(email, appName, new Long(acesVehicleId), vinNumber, data);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateMaintenanceServicePerformed(String email,
			 String acesVehicleId, String vinNumber, String data,
			 HttpServletRequest request, HttpHeaders headers){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.updateServicePerformed(email, appName, new Long(acesVehicleId), vinNumber, data);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeMaintenanceServicePerformed(String email,
			String id, HttpHeaders headers, HttpServletRequest request){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.removeServicePerformed(email, appName, new Long(id));
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveAppointment(HttpHeaders headers,
			HttpServletRequest request,String email, String acesVehicleId, String vinNumber,
			String appointmentId) {
		ErrorList errors = new ErrorList();
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!Validator.isValidLong("acesVehicleId", acesVehicleId, Long.MIN_VALUE, Long.MAX_VALUE, false, errors)){
			return getValidationErrors(errors);
		}
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.addAppointmentForVehicle(email, appName, new Long(acesVehicleId), vinNumber, new Long(appointmentId));
	}

	

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getFavouriteStores(HttpHeaders headers,
			HttpServletRequest request,String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return 
					getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(
					headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}

		BSROWebServiceResponse response = bfsUserProfileService.getFavouriteStores(email, appName);
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveFavouriteStore(HttpHeaders headers,
			HttpServletRequest request,String email,  String storeNumber) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.saveFavouriteStore(email, appName, storeNumber);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateStoreDistance(HttpHeaders headers, HttpServletRequest request,
			final String email, final String storeNumber){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.updateDistanceToStore(email, appName, storeNumber);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeFavouriteStore(HttpHeaders headers,
			HttpServletRequest request,String email, String storeNumber) {
		log.fine("Inside remove favourite store");
		
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.removeFavouriteStore(email,appName, storeNumber);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveFavouritePromotion(HttpHeaders headers,
			HttpServletRequest request, String promotionId, String email) {
		log.fine("Inside add favourite promotion");
		
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.saveFavouritePromotion(new Long(promotionId), email, appName);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeFavouritePromotion(HttpHeaders headers,
			HttpServletRequest request, String promotionId, String email) {
		log.fine("Inside remove favourite promotion");
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.removeFavouritePromotion(new Long(promotionId), email, appName);
	}

	@Override
	public BSROWebServiceResponse getUserPromotions(HttpHeaders headers,
			HttpServletRequest request, String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.getFavouritePromotions(email, appName);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse saveUserQuote(HttpHeaders headers,
			HttpServletRequest request, String quoteId, String type, String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		if(quoteId == null || quoteId.isEmpty()){
			return getValidationMessage(ValidationConstants.INVALID_QUOTE_ID);
		}
		if(type == null || type.isEmpty()){
			return getValidationMessage(ValidationConstants.INVALID_PRODUCT_TYPE);
		}
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.saveProductQuote(new Long(quoteId), type, email, appName);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeUserQuote(HttpHeaders headers,
			HttpServletRequest request, String quoteId, String type, String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		if(quoteId == null || quoteId.isEmpty()){
			return getValidationMessage(ValidationConstants.INVALID_QUOTE_ID);
		}
		if(type == null || type.isEmpty()){
			return getValidationMessage(ValidationConstants.INVALID_PRODUCT_TYPE);
		}
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.removeProductQuote(new Long(quoteId), type, email, appName);
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getUserQuotes(HttpHeaders headers,
			HttpServletRequest request, String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.getUserSavedQuotes(email, appName);
	}

	@Override
	@RequireValidToken
	public byte[] getImage(HttpHeaders headers, HttpServletRequest request,
			String imageType, String imageProductId) {
		byte[] image = bfsUserProfileService.getImage(imageType, new Long(imageProductId));
		image = Base64.encodeBase64(image);
		return image;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse uploadImage(HttpHeaders headers, HttpServletRequest request,
			String imageType, String imageProductId, String images) {
		
		//byte[] image = images.getBytes();
		
		byte[] image = imageCompress(images);
		/*if(image.length > 15000){
			return getValidationMessage(ValidationConstants.IMAGE_SIZE_TOO_LONG);
		}*/
		
		return bfsUserProfileService.saveImage(imageType, new Long(imageProductId), image);
	}
	
	// This method is used to convert and compress the image and save to jpg format.
	public byte[] imageCompress(String strData) {
		
		int width = 350, height = 200;
		//ByteArrayInputStream in = new ByteArrayInputStream(data);
		byte[] data = strData.getBytes();
		InputStream in = new ByteArrayInputStream(Base64.decodeBase64(strData));
		ByteArrayOutputStream buffer = null;
		try {
			BufferedImage img = ImageIO.read(in);
			int imgWidth = img.getWidth();
			int imgHeight = img.getHeight();
			double reduce =0.0;
			
			// image size reduce calculation
			if(imgWidth > width || imgHeight > height)
    		{
	    		double scalex = (double) width / imgWidth;
	    		double scaley = (double) height / imgHeight;
	    		reduce = Math.min(scalex, scaley);
	    		width = (int) (imgWidth * reduce);
	    		height = (int) (imgHeight * reduce);
    		}
    		else
    		{
    			width = imgWidth;
	    		height = imgHeight;
    		}
			
			Image scaledImage = img.getScaledInstance(width, height,Image.SCALE_SMOOTH);
			BufferedImage imageBuff = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
			imageBuff.getGraphics().drawImage(scaledImage, 0, 0,Color.WHITE, null);

			buffer = new ByteArrayOutputStream();
			ImageIO.write(imageBuff, "jpg", buffer);
			return buffer.toByteArray();

		} catch (IOException e) {
			log.fine("Exception in Image compress "+e.getMessage());
			return strData.getBytes();
		}
		
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeImage(HttpHeaders headers, HttpServletRequest request,
			String imageType, String imageProductId) {
		return bfsUserProfileService.deleteImage(imageType, new Long(imageProductId));
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getUserDevices(HttpHeaders headers,
			HttpServletRequest request, String email) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		return bfsUserProfileService.getRegisteredDevices(email, appName);
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse registerNewDevice(HttpHeaders headers, HttpServletRequest request,
			String email,String data){
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.registerNewDevice(email, appName, data);
		
	}
	

	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateDevice(HttpHeaders headers, HttpServletRequest request,
			String email, String data) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
		
		if(!ServerUtil.isNullOrEmpty(data) && !JSONUtils.mayBeJSON(data))
			return getValidationMessage(ValidationConstants.INVALID_JSON_STRING);
		
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.updateDeviceInfo(email, appName, data);
	}


	@Override
	@RequireValidToken
	public BSROWebServiceResponse removeDevice(HttpHeaders headers, HttpServletRequest request,
			String email, String deviceId) {
		if(!ValidatorUtils.isEmailValid(email))
			return getValidationMessage(ValidationConstants.USER_EMAIL_INVALID);
	
		String  appName = null;
		if(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()) != null) {
			appName = org.apache.commons.lang.StringUtils.trimToEmpty(headers.getRequestHeader(HttpHeader.Params.APP_NAME.getValue()).get(0));			
		}
		
		return bfsUserProfileService.removeDevice(email, appName, new Long(deviceId));
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
	
	private BSROWebServiceResponse getValidationErrors(ErrorList errors){
		List<com.bsro.core.validation.Error> errlist = errors.getErrors();
		Errors errs = new Errors();
		for (com.bsro.core.validation.Error e : errlist){
			errs.getGlobalErrors().add(e.getMessage());
		}
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setErrors(errs);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return  response;
	}


}
