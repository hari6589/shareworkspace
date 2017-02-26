/**
 * 
 */
package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import com.bfrc.framework.dao.MediaCampaignDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.mediacampaign.MediaCampaignParameters;

/**
 * @author smoorthy
 *
 */
public class MediaCampaignDAOImpl extends HibernateDAOImpl implements MediaCampaignDAO {

	/* (non-Javadoc)
	 * @see com.bfrc.framework.dao.MediaCampaignDAO#getAllCampaignParameters()
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MediaCampaignParameters> getAllCampaignParameters() {
		String hql = "from MediaCampaignParameters";
		List<MediaCampaignParameters> l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		
		return (List<MediaCampaignParameters>)l;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllCampaignParameterKeys() {
		String hql = "select distinct keys from MediaCampaignParameters";
		List<String> l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		
		return (List<String>)l;
	}

}
