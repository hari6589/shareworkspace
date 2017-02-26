/**
 * 
 */
package com.bfrc.dataaccess.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.mws.MwsUserDataDAO;
import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.mws.MwsBackupData;
import com.bfrc.dataaccess.model.mws.MwsUsers;

/**
 * @author schowdhury
 *
 */
public class MWSUserDataDAOImpl extends HibernateDaoSupport implements MwsUserDataDAO {

	@SuppressWarnings("unchecked")
	public WebSite getSite(String siteName) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(WebSite.class);
		Criterion name = Restrictions.eq("name", siteName).ignoreCase();
		criteria.add(name);
		
		List<WebSite> wsList = (List<WebSite>)criteria.list();
		
		if(wsList != null && !wsList.isEmpty()){
			return wsList.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MwsUsers getMwsUser(String email, String password, WebSite webSite) {
		
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MwsUsers.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("password", password));
		criteria.add(Restrictions.eq("userType", webSite));
		
		List<MwsUsers> usersList = (List<MwsUsers>)criteria.list();
		
		if(usersList != null && !usersList.isEmpty()){
			return usersList.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MwsUsers doesUserExist(String email, WebSite webSite) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MwsUsers.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("userType", webSite));
		
		List<MwsUsers> usersList = (List<MwsUsers>)criteria.list();
		
		if(usersList != null && !usersList.isEmpty()){
			return usersList.get(0);
		}
		
		return null;
	}

	public void saveMwsUser(MwsUsers user) {
		
		getHibernateTemplate().save(user);

	}


	public void updateMwsUser(MwsUsers user) {
		user.setRegisterDate(new Date());
		getHibernateTemplate().saveOrUpdate(user);
	}

	@SuppressWarnings("unchecked")
	public MwsBackupData getUserData(MwsUsers user) {
		
		List<MwsBackupData> dataList = getHibernateTemplate().find("from MwsBackupData bd where bd.user.userId=?",user.getUserId());
		
		if(dataList != null && !dataList.isEmpty()){
			return dataList.get(0);
		}
		return null;
	}

	public void saveUserData(MwsBackupData userData) {
		getHibernateTemplate().save(userData);
	}

}
