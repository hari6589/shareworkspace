package com.bfrc.framework.spring;

import java.util.*;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.Config;
import com.bfrc.framework.*;

public abstract class HibernateDAOImpl extends HibernateDaoSupport implements Configurable {
	private Config config;

	public Config getConfig() {
		return this.config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public Object getOne(String hql, int id) {
		return getOne(hql, new Integer(id));
	}

	public Object getOne(String hql, long id) {
		return getOne(hql, new Long(id));
	}
	
	public Object getOne(String hql, Object o) {
		List l = getHibernateTemplate().find(hql, o);
		Object out = null;
		if(l != null && l.size() > 0)
			out = l.get(0);
		return out;
	}
}
