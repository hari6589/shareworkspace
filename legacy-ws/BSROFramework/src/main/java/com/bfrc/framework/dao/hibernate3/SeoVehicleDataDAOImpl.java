package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import com.bfrc.framework.dao.SeoVehicleDataDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.lookup.SeoVehicleData;

/**
 * @author smoorthy
 *
 */
public class SeoVehicleDataDAOImpl extends HibernateDAOImpl implements SeoVehicleDataDAO {
	
	@Override
	public SeoVehicleData getSEOVehicleData(String siteName, String fileId, String recordId) {
		if(fileId == null || recordId == null)
			return null;
		List l = getHibernateTemplate().findByNamedParam("from SeoVehicleData s where s.fileId=:fileId and s.recordId=:recordId and s.webSite=:webSite",
				new String[]{"fileId", "recordId", "webSite"}, new Object[]{fileId, recordId, siteName});
		if (l == null || l.size() < 1)
			return null;
		
		SeoVehicleData seoVehicleData = (SeoVehicleData) l.get(0);
		return seoVehicleData;
	}

}
