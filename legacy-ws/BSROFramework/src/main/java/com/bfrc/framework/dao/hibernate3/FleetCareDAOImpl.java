package com.bfrc.framework.dao.hibernate3;

import java.util.*;

import com.bfrc.framework.dao.FleetCareDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.fleetcare.*;

public class FleetCareDAOImpl extends HibernateDAOImpl implements FleetCareDAO {
	
	public NaManager getManagerByState(String state) {
		List managers = getHibernateTemplate().findByNamedQueryAndNamedParam("FindNaManagersByState", "State", state);
		return (NaManager)managers.get(0);
	}
	
	public void addApplication(NaApplication application) {
		getHibernateTemplate().save(application);
	}
}
