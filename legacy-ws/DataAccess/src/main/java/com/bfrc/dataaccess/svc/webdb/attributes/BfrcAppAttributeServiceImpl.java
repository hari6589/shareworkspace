package com.bfrc.dataaccess.svc.webdb.attributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.BfrcAppAttributeDAO;
import com.bfrc.dataaccess.model.attribute.BfrcAppAttribute;
import com.bfrc.dataaccess.svc.webdb.AttributeService;

@Service
public class BfrcAppAttributeServiceImpl implements AttributeService {

	public static final String GLOBAL_SITE_TYPE = "GLOBAL";
	
	@Autowired
	private BfrcAppAttributeDAO bfrcAppAttributeDao;
	
	public enum StatusCodes {
		ACTIVE("A"),
		INACTIVE("I");
		
		private String status;
		private StatusCodes(String status){
			this.status = status;
		}
		public String getStatus() {
			return status;
		}
	}
	
	public Set<BfrcAppAttribute> findAllActiveBySiteTypes(List<String> siteTypes) {
		Set<BfrcAppAttribute> loResult = new HashSet<BfrcAppAttribute>();
		
		for(String siteType : siteTypes) {
			loResult.addAll(findActiveBySiteType(siteType));
		}
		
		return loResult;
	}
	
	public Set<BfrcAppAttribute> findActiveBySiteType(String siteType) {
		siteType = StringUtils.trimToNull(siteType);
		if(siteType == null) return null;
	
		Set<BfrcAppAttribute> loResult = new HashSet<BfrcAppAttribute>();
		loResult.addAll(bfrcAppAttributeDao.findBySiteType(siteType, StatusCodes.ACTIVE.getStatus()));
		
		return loResult;
	}
	
	public Set<BfrcAppAttribute> findActiveBySiteAndGroup(String siteType,
			String groupName) {
		siteType = (siteType == null) ? GLOBAL_SITE_TYPE : StringUtils.trimToEmpty(siteType);
		groupName = StringUtils.trimToNull(groupName);
		if(groupName == null) return null;
		
		Set<BfrcAppAttribute> loResult = new HashSet<BfrcAppAttribute>();
		loResult.addAll(bfrcAppAttributeDao.findBySiteAndGroup(siteType, groupName, StatusCodes.ACTIVE.getStatus()));
		
		return loResult;
	}
}
