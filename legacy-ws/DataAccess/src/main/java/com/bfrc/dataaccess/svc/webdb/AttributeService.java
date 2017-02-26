package com.bfrc.dataaccess.svc.webdb;

import java.util.List;
import java.util.Set;

import com.bfrc.dataaccess.model.attribute.BfrcAppAttribute;

public interface AttributeService {

	/**
	 * Returns all active attributes for the sites passed in.  This will return a UNIQUE listing based on the 
	 * attribute name (no duplicate names)
	 * @param siteTypes
	 * @return
	 */
	public Set<BfrcAppAttribute> findAllActiveBySiteTypes(List<String> siteTypes);
	

	/**
	 * Returns all active attributes for the site passed in.  This will return a UNIQUE listing based on the 
	 * attribute name (no duplicate names)
	 * @param siteTypes
	 * @return
	 */
	public Set<BfrcAppAttribute> findActiveBySiteType(String siteType);
	
	/**
	 * Returns all active attributes for the site and group passed in.  If siteType is null
	 * the value will default to 'GLOBAL'.
	 * @param siteType
	 * @param groupName
	 * @return
	 */
	public Set<BfrcAppAttribute> findActiveBySiteAndGroup(String siteType, String groupName);
	
}
