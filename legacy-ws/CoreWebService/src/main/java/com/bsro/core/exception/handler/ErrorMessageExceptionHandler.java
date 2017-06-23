package com.bsro.core.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;

import com.bsro.core.exception.ws.ErrorMessageException;

@Provider
@Component
public class ErrorMessageExceptionHandler implements ExceptionMapper<ErrorMessageException> {	
	@Override
	public Response toResponse(ErrorMessageException ex) {
		Errors response = new Errors();
		response.setGlobalErrors(ex.getGlobalErrors());
		response.setFieldErrors(ex.getFieldErrors());
		
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
