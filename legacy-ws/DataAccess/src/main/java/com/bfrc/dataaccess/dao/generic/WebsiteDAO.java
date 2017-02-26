package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.contact.WebSite;

public interface WebsiteDAO extends IGenericOrmDAO<WebSite, Integer> {

	public Collection<WebSite> findBySiteName(String siteName);
}
