package com.bfrc.framework.dao.hibernate3;

import java.util.*;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.bfrc.framework.dao.AdminDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;

import com.bfrc.pojo.admin.*;

public class AdminDAOImpl extends HibernateDAOImpl implements AdminDAO {
	public GlobalAdmin getUser(String name, String password) {
		String hql = "from GlobalAdmin ga join fetch ga.adminApps apps where ga.name=? and ga.password=? and apps.webSite=?";
		Object[] args = new Object[] { name, password,
				getConfig().getSiteName() };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (GlobalAdmin) l.get(0);
	}

	public String getPassword(String name) {
		if (name == null)
			return null;
		List l = getHibernateTemplate().find(
				"from GlobalAdmin ga where ga.name=?", name);
		if (l != null && l.size() > 0)
			return ((GlobalAdmin) l.get(0)).getPassword();
		return null;
	}

	public GlobalAdmin fetchByEmail(String email) {
		String hql = "from GlobalAdmin ga join fetch ga.adminApps apps where ga.emailAddress=? and apps.webSite=?";
		Object[] args = new Object[] { email, getConfig().getSiteName() };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (GlobalAdmin) l.get(0);
	}
	
	public GlobalAdmin getAdminWithToken(String token) {
		String hql = "from GlobalAdmin ga join fetch ga.adminApps apps where ga.token=? and apps.webSite=?";
		Object[] args = new Object[] { token, getConfig().getSiteName() };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (GlobalAdmin) l.get(0);
	}
	public List<GlobalAdmin> getAllAdminWithEmail(String brand) {
		if(brand == null || brand.length()==0)
			return null;

		String hql = "select distinct ga from GlobalAdmin ga join fetch ga.adminApps apps where ga.emailAddress <> null and ga.emailSettings like '%"+brand+"%' and apps.webSite=?";
		Object[] args = new Object[] {getConfig().getSiteName()};
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (List<GlobalAdmin>) l;
	}
	
	public Long save(GlobalAdmin globalAdmin) {
		return save(globalAdmin, null);
	}
	
	public Long save(GlobalAdmin globalAdmin, long[] appids) {
		
		boolean saved = false;
		int counter = 0;
		Session s = getSession();
		while(!saved){
			try{
				String sqlNextId="select CASE WHEN MAX(USER_ID)>=1000 THEN MAX(USER_ID)+1 ELSE 1000 END as NEXTID from RTMS_WEBDB.GLOBAL_ADMIN";
				SQLQuery query = s.createSQLQuery(sqlNextId);
				query.addScalar("NEXTID", Hibernate.LONG);
				Long nextId = (Long)query.uniqueResult();
				globalAdmin.setUserId(nextId.longValue());
		        Long id = (Long)getHibernateTemplate().save(globalAdmin);
		        if(id != null && id.longValue() > 0)
		        	saved = true;
			}finally {
				//s.close();
				this.releaseSession(s);
			}
			counter++;
	        if(counter >50)//avoid infinite loop
	        	break;
		}
		if(saved){
			if(appids == null)
				appids = new long[]{37};//default to store admin
			for(int i=0; i< appids.length; i++){
				long appid = appids[i];
				AdminAppsUsers aau = new AdminAppsUsers();
				AdminAppsUsersId uid = new AdminAppsUsersId(globalAdmin.getUserId(), appid);
				aau.setId(uid);
				getHibernateTemplate().save(aau);
			}
		}
		return null;
	}
	
	public void update(GlobalAdmin globalAdmin) {
		getHibernateTemplate().update(globalAdmin);
	}
}
