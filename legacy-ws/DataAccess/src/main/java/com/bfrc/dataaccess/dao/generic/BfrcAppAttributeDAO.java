package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.attribute.BfrcAppAttribute;
import com.bfrc.dataaccess.svc.webdb.attributes.BfrcAppAttributeServiceImpl.StatusCodes;

public interface BfrcAppAttributeDAO extends IGenericOrmDAO<BfrcAppAttribute, Long> {

	/**
	 * 
	 * @param siteType
	 * @param statCd {@link StatusCodes}
	 * @return
	 */
	public Collection<BfrcAppAttribute> findBySiteType(String siteType, String statCd);
	
	/**
	 * 
	 * @param siteType
	 * @param groupName
	 * @param statCd {@link StatusCodes}
	 * @return
	 */
	public Collection<BfrcAppAttribute> findBySiteAndGroup(String siteType, String groupName, String statCd);
	
}
