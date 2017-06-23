package com.bsro.core.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bsro.core.exception.ws.BSROWebServiceUnexpectedErrorResponseException;

@Provider
@Component
public class BSROWebServiceUnexpectedErrorResponseExceptionHandler implements ExceptionMapper<BSROWebServiceUnexpectedErrorResponseException> {
	private static final Log logger = LogFactory.getLog(BSROWebServiceUnexpectedErrorResponseExceptionHandler.class);

	@Override
	public Response toResponse(BSROWebServiceUnexpectedErrorResponseException ex) {
		String msg = ex.getMessage();

		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		logger.error(msg, ex);
		errors.getGlobalErrors().add(msg);
		errors.setExceptionStackTrace(ExceptionUtils.getFullStackTrace(ex));
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());

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
