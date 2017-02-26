package com.bfrc.dataaccess.svc.webdb.realestate;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.svc.webdb.RealEstateService;
import com.bfrc.dataaccess.util.Sites;
import com.bfrc.framework.dao.RealEstateDAO;
import com.bfrc.pojo.realestate.RealestateSurplusProperty;

/**
 * @author smoorthy
 *
 */

@Service
public class RealEstateServiceImpl implements RealEstateService {
	
	@Autowired
	RealEstateDAO realEstateDAO;

	@SuppressWarnings("unchecked")
	public BSROWebServiceResponse getSurplusPropertyDetails(String siteName) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		List<RealestateSurplusProperty> properties = (List<RealestateSurplusProperty>) realEstateDAO.getActiveRealestateSurplusPropertiesByWebSite(siteName.toLowerCase());
		
		if (properties == null || properties.isEmpty()) {
			response.setMessage("No surplus properties found for site "+siteName);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		for (RealestateSurplusProperty property : properties) {
			if (property != null && property.getThumbImage() != null) {
				String thumbImageURL = Sites.getWebSiteAppRoot(siteName)+"/common/showimage.jsp?i="+property.getPropertyId()+"&t=jpg&w=thumb&src=re&it=property";
				String masterImageURL = Sites.getWebSiteAppRoot(siteName)+"/common/showimage.jsp?i="+property.getPropertyId()+"&t=jpg&w=image&src=re&it=property";
				
				property.setThumbImageURL(thumbImageURL);
				property.setMasterImageURL(masterImageURL);
			}
		}
		
		response.setPayload(properties);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

}
