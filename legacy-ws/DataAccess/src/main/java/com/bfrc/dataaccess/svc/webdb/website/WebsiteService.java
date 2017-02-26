package com.bfrc.dataaccess.svc.webdb.website;

import com.bfrc.dataaccess.model.contact.WebSite;

public interface WebsiteService {
	public WebSite get(Integer Id);
	public WebSite getBySiteName(String siteName);
	public String getFrom(Integer siteId) ;
	
}
