package com.bfrc.dataaccess.svc.webdb.creditcard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.svc.webdb.CreditCardService;
import com.bfrc.framework.dao.CreditCardDAO;
import com.bfrc.pojo.creditcard.CreditCardContent;

/**
 * @author smoorthy
 *
 */

@Service
public class CreditCardServiceImpl implements CreditCardService {
	
	@Autowired
	CreditCardDAO creditCardDAO;

	public BSROWebServiceResponse getCreditCardDetails(String siteName) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		List<CreditCardContent> creditCardContents = creditCardDAO.getCreditCardDetails(siteName);
		
		if (creditCardContents == null || creditCardContents.isEmpty()) {
			response.setMessage("No card details found for site "+siteName);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		response.setPayload(creditCardContents);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

}
