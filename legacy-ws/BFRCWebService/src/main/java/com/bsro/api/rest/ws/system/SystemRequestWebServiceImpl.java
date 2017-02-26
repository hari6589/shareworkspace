package com.bsro.api.rest.ws.system;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.model.system.BsroWebServiceRequests;
import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestsService;
import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestServiceImpl.ServiceCode;
import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestServiceImpl.WebServiceId;
import com.bsro.core.exception.ws.InvalidArgumentException;
import com.bsro.core.exception.ws.ServerProcessingException;
import com.bsro.core.security.RequireValidToken;

@Component
public class SystemRequestWebServiceImpl implements SystemRequestWebService {

	@Autowired
	private WebServiceRequestsService webServiceRequestsService;
	
	private boolean validateWebServiceCode(String value) {
		value = StringUtils.trimToNull(value);
		if(value == null) return false; 
		ServiceCode[] codes = ServiceCode.values();
		for(ServiceCode code : codes) {
			if(StringUtils.trimToEmpty(code.getCode()).equals(value))
				return true;
		}
		
		return false;
	}
	
	@Override
	@RequireValidToken
	public BsroWebServiceRequests toggle(HttpHeaders headers, String serviceCd) {
		
		BsroWebServiceRequests record = webServiceRequestsService.getRequest(WebServiceId.NCD_WEB_SERVICE);
//		BsroWebServiceRequests record = new BsroWebServiceRequests();
//		record.setWebServiceType(WebServiceId.NCD_WEB_SERVICE);
		record.setWebServiceCode(serviceCd);
		if(!validateWebServiceCode(serviceCd))
			throw new InvalidArgumentException("Invalid code passed.");
		try {
			webServiceRequestsService.save(record);
		}catch(Exception E) {
			throw new ServerProcessingException(E.getMessage());
		}
		
		return record; 
	}
	
	@Override
	@RequireValidToken
	public BsroWebServiceRequests save(HttpHeaders headers,
			String serviceCd, String serviceMsg) {
		

		if(!validateWebServiceCode(serviceCd))
			throw new InvalidArgumentException("Invalid code passed.");
		
		BsroWebServiceRequests record = webServiceRequestsService.getRequest(WebServiceId.NCD_WEB_SERVICE);
		if(record == null) {
			record = new BsroWebServiceRequests();
			record.setWebServiceType(WebServiceId.NCD_WEB_SERVICE);
		}
		record.setWebServiceCode(serviceCd);
		record.setWebServiceMessage(serviceMsg);
		
		try {
			webServiceRequestsService.save(record);
		}catch(Exception E) {
			throw new ServerProcessingException(E.getMessage());
		}
		
		return record; 
	}
	
	@Override
	@RequireValidToken
	public String get(HttpHeaders headers) {
		return webServiceRequestsService.getRequest(WebServiceId.NCD_WEB_SERVICE).toString();
	}
}
