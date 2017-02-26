package com.bsro.api.rest.ws.pricing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import app.bsro.model.error.Errors;
import app.bsro.model.tire.QuoteItem;
import app.bsro.model.tire.TireQuote;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import org.codehaus.jackson.JsonNode;

import com.bfrc.dataaccess.svc.webdb.TireQuoteService;
import com.bfrc.framework.util.ServerUtil;

import com.bsro.core.security.RequireValidToken;
/**
 * Implementation of tire quote web service. This service will create a tire quote and save in database or
 * get the existing quote for a quote id from database.
 * 
 * @author stallin sevugamoorthy
 *
 */
@Component
public class TireQuoteWebServiceImpl implements TireQuoteWebService {
	
	@Autowired
	TireQuoteService tireQuoteService;
	
	private Logger log = Logger.getLogger(getClass().getName());

	@Override
	@RequireValidToken
	public BSROWebServiceResponse createQuote(final Long storeNumber, final String articleNumber, final String quantity, 
			final Long acesVehicleId, final Integer tpms, final String firstName, final String lastName, final String siteName, final String emptyCart, HttpHeaders headers) {
		BSROWebServiceResponse res = new BSROWebServiceResponse();
		
		if (hasQuoteInputs(storeNumber, articleNumber, acesVehicleId, tpms, siteName)) {
			res = tireQuoteService.createQuote(storeNumber, articleNumber, quantity, acesVehicleId, tpms, firstName, lastName, siteName, emptyCart);
		} else {
			String errmsg = getErrorMessage(storeNumber, articleNumber, acesVehicleId, tpms, siteName);
			res = getValidationMessage(errmsg);			
		}
		return res;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getQuote(Long quoteId, HttpHeaders headers) {
		BSROWebServiceResponse res = null;
		
		if (ServerUtil.isNullOrEmpty(quoteId)) {
			String errmsg = "Invalid tire quote id.";
			res = getValidationMessage(errmsg);
		} else {
			res = tireQuoteService.getQuote(quoteId);
		}
				
		return res;
	}
	
	@Override
	public Response getPdf(Long quoteId, HttpHeaders headers) {
		ResponseBuilder responseBuilder = null;
		
		if (ServerUtil.isNullOrEmpty(quoteId)) {
			responseBuilder = javax.ws.rs.core.Response.status(Status.BAD_REQUEST);
		} else {
			byte[] pdfdata = tireQuoteService.getPdf(quoteId);
			responseBuilder = Response.ok(new ByteArrayInputStream(pdfdata));
		    responseBuilder.header("Cache-Control", "cache, must-revalidate");
			responseBuilder.header("Pragma", "public");
		    responseBuilder.header("Content-Disposition", "filename=tirequote.pdf");
		}
				
		return responseBuilder.build();
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse updateQuote(JsonNode hybrisQuote, Long quoteId, HttpHeaders headers) {
		BSROWebServiceResponse response = null;
		if (ServerUtil.isNullOrEmpty(quoteId)) {
			String errmsg = "Invalid tire quote id.";
			response = getValidationMessage(errmsg);
		} else {
			response =  tireQuoteService.updateQuote(quoteId, hybrisQuote);
		}		
		return response;
	}
	
	private boolean hasQuoteInputs(Long storeNumber, String articleNumber, Long acesVehicleId, Integer tpms, String siteName) {
		boolean isvalid = true;
		if (ServerUtil.isNullOrEmpty(storeNumber) || ServerUtil.isNullOrEmpty(articleNumber) || ServerUtil.isNullOrEmpty(siteName)) {
			isvalid = false;
		}
		return isvalid;
	}
	
	private String getErrorMessage(Long storeNumber, String articleNumber, Long acesVehicleId, Integer tpms, String siteName) {
		StringBuffer buf = new StringBuffer();
		String errmsg = null;
		int errFieldsCount = 0;
		if (ServerUtil.isNullOrEmpty(storeNumber)) {
				buf.append("store number");
				++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(articleNumber)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("article number");
			++errFieldsCount;
		}
		if (ServerUtil.isNullOrEmpty(siteName)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("site name");
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
