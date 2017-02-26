package com.bfrc.dataaccess.svc.webdb.website;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.WebsiteDAO;
import com.bfrc.dataaccess.model.contact.WebSite;

@Service
public class WebsiteServiceImpl implements WebsiteService {
	 
	@Autowired
	private WebsiteDAO websiteDao;
	
	public WebSite get(Integer id) {
		return websiteDao.get(id);
	}
	
	public WebSite getBySiteName(String siteName) {
		Collection<WebSite> sites = websiteDao.findBySiteName(siteName);
		if(sites != null & sites.size() > 0) {
			return sites.iterator().next();
		}
		return null;
	}
	public String getFrom(Integer siteId) {
		String from = "DO-NOT-REPLY<" + (get(siteId)).getWebmaster() + ">";
		return from;
	}

}
