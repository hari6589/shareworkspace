package com.bsro.core.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import app.bsro.model.error.WebServiceExceptionError;

import com.bsro.core.exception.ws.WebServiceException;

@Provider
@Component
public class WebServiceExceptionHandler implements ExceptionMapper<WebServiceException> {	
	@Override
	public Response toResponse(WebServiceException ex) {
		String msg = ex.getMessage();
		String internalError = ex.getInternalErrorMessage();
		
		WebServiceExceptionError response = new WebServiceExceptionError();
		response.setExceptionMessage(msg);
		response.setExceptionStackTrace(internalError);
		
		String jsonResponse = null;
		
		try {
			ObjectMapper JSONMapper = new ObjectMapper();
			jsonResponse = JSONMapper.writeValueAsString(response);
		} catch (Throwable throwable) {
			System.out.println(throwable.getMessage());
		}
		
		return Response.serverError().entity(jsonResponse).type("application/json").build();
	}

}
