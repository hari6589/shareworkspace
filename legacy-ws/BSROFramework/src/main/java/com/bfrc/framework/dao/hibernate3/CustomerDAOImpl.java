package com.bfrc.framework.dao.hibernate3;

import java.io.Serializable;
import java.util.*;

import com.bfrc.framework.dao.CustomerDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.customer.*;
import com.bfrc.pojo.pricing.TpArticleLog;

public class CustomerDAOImpl extends HibernateDAOImpl implements CustomerDAO {
	
	// Customer ContactUs Emails Log
	public Long createCustomerContactusemailLog(CustomerContactusemailLog t) {
		t.setCreationDate(new Date());
		return (Long)getHibernateTemplate().save(t);
	}
	public List getAllCustomerContactusemailLogs() {
		String hql = "from CustomerContactusemailLog t order by t.creationDate";
		List l = getHibernateTemplate().find(hql);
		return l;
	}

	public CustomerContactusemailLog findCustomerContactusemailLog(Object id) {
		if(id == null) return null;
		long longId = Long.parseLong(id.toString());
		return (CustomerContactusemailLog)getHibernateTemplate().get(CustomerContactusemailLog.class,new Long(longId));
	}

	public void updateCustomerContactusemailLog(CustomerContactusemailLog t) {
		getHibernateTemplate().update(t);
	}

	public void deleteCustomerContactusemailLog(CustomerContactusemailLog t) {
		getHibernateTemplate().delete(t);
	}
}
