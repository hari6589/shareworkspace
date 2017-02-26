package com.bsro.service.contact;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.ContactDAO;

@Service
public class ContactServiceImpl implements ContactService {

	private Log logger;

	@Autowired
	private ContactDAO contactDAO;

	public ContactServiceImpl() {
		this.logger = LogFactory.getLog(ContactServiceImpl.class);
	}

	@Override
	public Map getMappedWebSites() {
		return contactDAO.getMappedWebSites();
	}


}
