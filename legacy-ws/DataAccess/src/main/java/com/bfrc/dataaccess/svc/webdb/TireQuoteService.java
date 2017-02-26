package com.bfrc.dataaccess.svc.webdb;

import java.io.ByteArrayOutputStream;

import app.bsro.model.tire.TireQuote;
import app.bsro.model.webservice.BSROWebServiceResponse;
import org.codehaus.jackson.JsonNode;

/**
 * @author stallin sevugamoorthy
 *
 */
public interface TireQuoteService {
	
	public BSROWebServiceResponse createQuote(Long storeNumber, String articleNumber, String quantity, 
			Long acesVehicleId, Integer tpms, String firstName, String lastName, String siteName, String emptyCart);
	
	public BSROWebServiceResponse getQuote(Long quoteId);
	
	public BSROWebServiceResponse updateQuote(Long quoteId, JsonNode hybrisQuote);
	
	public byte[] getPdf(Long quoteId);

}
