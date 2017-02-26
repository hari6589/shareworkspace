/**
 * 
 */
package com.bsro.api.rest.ws.notification;

import java.util.Collection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.notification.DeviceMessage;
import com.bfrc.dataaccess.model.notification.ServiceNotification;
import com.bfrc.dataaccess.svc.webdb.NotificationService;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.framework.util.ServerUtil;
import com.bsro.core.security.RequireValidToken;
import com.bsro.core.validation.ErrorList;
import com.bsro.core.validation.Validator;
/**
 * @author schowdhu
 *
 */
@Component
public class NotificationWebServiceImpl implements NotificationWebService {
	
	private Logger log = Logger.getLogger(getClass().getName());
	private static final String INVALID_DATE_FORMAT = "Invalid Selected date format"; 
	
	@Autowired
	NotificationService notificationService;
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAllNotifications( @Context HttpHeaders headers, 
			@Context HttpServletRequest request){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		Collection<ServiceNotification> list = notificationService.getCurrentMessages();

		if(list == null || list.isEmpty()){
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			response.setMessage(ValidationConstants.NO_ACTIVE_NOTIFICATION_FOUND);
			response.setPayload(null);
		}else{
			response.setPayload(list);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		}
		return response;
	}


	@Override
	@RequireValidToken
	public BSROWebServiceResponse getNotifications(HttpHeaders headers, HttpServletRequest request,String deviceUUID) {
		
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		Collection<DeviceMessage> list = notificationService.getDeviceMessage(deviceUUID);

		if(list == null || list.isEmpty()){
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			response.setMessage(ValidationConstants.NO_ACTIVE_NOTIFICATION_FOUND);
			response.setPayload(null);
		}else{
			response.setPayload(list);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		}
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateNotification(HttpHeaders headers,
			HttpServletRequest request, String messageJson) {

		BSROWebServiceResponse response = new BSROWebServiceResponse();
		if(ServerUtil.isNullOrEmpty(messageJson)){
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ValidationConstants.INVALID_DATA);
			return response;
		}
		else if(!JSONUtils.mayBeJSON(messageJson)){
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ValidationConstants.INVALID_JSON_STRING);
			return response;
		}			
			
		if(notificationService.updateDeviceMessage(messageJson)){
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			response.setMessage(ValidationConstants.UPDATE_SUCCESS);
		}else{
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(ValidationConstants.UPDATE_FAILED);
		}
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse deleteNotification(HttpHeaders headers,
			HttpServletRequest request, String deviceMessageIds) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ErrorList errors = new ErrorList();
		Validator.isValidString("deviceMessageId", deviceMessageIds, false, errors);

		if(errors.hasErrors()){
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ValidationConstants.INVALID_DATA);
			return response;
		}
		if(notificationService.deleteDeviceMessages(deviceMessageIds)){
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
			response.setMessage(ValidationConstants.DELETE_SUCCESS);
		}else{
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			response.setMessage(ValidationConstants.DELETE_FAILED);
		}
		return response;
	}

}
