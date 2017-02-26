package com.bsro.api.rest.ws.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.core.beans.PropertyAccessor;
import com.bsro.api.rest.ws.VersionWebService;

@Component
public class VersionWebServiceImpl implements VersionWebService {

	@Autowired
	private PropertyAccessor propertyAccessor;
	
	public String getVersion() {
		return propertyAccessor.getStringProperty("webServiceVersion");
	}

}
