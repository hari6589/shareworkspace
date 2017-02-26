package com.bfrc.framework.dao.hibernate3;

import java.util.Date;
import java.util.List;

import com.bfrc.framework.dao.IpAccessDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.admin.AdminIpAddress;

public class IpAccessDAOImpl extends HibernateDAOImpl implements IpAccessDAO {

	public AdminIpAddress findUniqueBy(String attribute, String id) {
		if (id == null)
			return null;
		String hql = "from AdminIpAddress ipa where ipa." + attribute + " =?";
		Object[] args = new Object[] { id };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (AdminIpAddress) l.get(0);
	}

	public AdminIpAddress checkIpAddress(String ip) {
		if (ip == null||ip.length()==0)
			return null;
		String hql = "from AdminIpAddress ipa where ? like (CONCAT(ipa.ipAddress,'%')) and (ipa.isDeleted is null or ipa.isDeleted = false) ";
		Object[] args = new Object[] { ip };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (AdminIpAddress) l.get(0);
	}
	public List<AdminIpAddress> getAllIps() {
		String hql = "from AdminIpAddress ipa where ipa.isDeleted is null or ipa.isDeleted = false";
		List l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		return (List<AdminIpAddress>) l;
	}

	public void save(AdminIpAddress ipAddress) {

		ipAddress.setCreatedDate(new Date());
		ipAddress.setModifiedDate(new Date());
		if (findUniqueBy("ipAddress", ipAddress.getIpAddress()) != null)
			getHibernateTemplate().saveOrUpdate(ipAddress);
		else
			getHibernateTemplate().save(ipAddress);
	}

	public void update(AdminIpAddress ipAddress) {
		ipAddress.setModifiedDate(new Date());
		getHibernateTemplate().update(ipAddress);
	}
}
