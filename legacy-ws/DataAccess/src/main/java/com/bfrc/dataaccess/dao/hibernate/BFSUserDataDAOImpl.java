/**
 * 
 */
package com.bfrc.dataaccess.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO;
import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.myprofile.BFSUser;
import com.bfrc.dataaccess.model.myprofile.MyBackupData;

/**
 * @author schowdhu
 *
 */
public class BFSUserDataDAOImpl extends HibernateDaoSupport implements BFSUserDataDAO {

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO#getSite(java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO#getBfsUser(java.lang.String, java.lang.String, com.bfrc.dataaccess.model.contact.WebSite)
	 */
	@SuppressWarnings("unchecked")
	public BFSUser getBfsUser(String email, String password, WebSite webSite) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(BFSUser.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("password", password));
		criteria.add(Restrictions.eq("userType", webSite));
		
		List<BFSUser> usersList = (List<BFSUser>)criteria.list();
		
		if(usersList != null && !usersList.isEmpty()){
			return usersList.get(0);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO#doesUserExist(java.lang.String, com.bfrc.dataaccess.model.contact.WebSite)
	 */
	public BFSUser doesUserExist(String email, WebSite webSite) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(BFSUser.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("userType", webSite));
		
		List<BFSUser> usersList = (List<BFSUser>)criteria.list();
		
		if(usersList != null && !usersList.isEmpty()){
			return usersList.get(0);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO#saveBfsUser(com.bfrc.dataaccess.model.myprofile.BFSUser)
	 */
	public Long saveBfsUser(BFSUser user) {
		return (Long)getHibernateTemplate().save(user);
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO#updateBfsUser(com.bfrc.dataaccess.model.myprofile.BFSUser)
	 */
	public void updateBfsUser(BFSUser user) {
		user.setRegisterDate(Calendar.getInstance());
		getHibernateTemplate().saveOrUpdate(user);
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO#getUserData(com.bfrc.dataaccess.model.myprofile.BFSUser)
	 */
	public MyBackupData getUserData(BFSUser user) {
		List<MyBackupData> dataList = getHibernateTemplate().find("from MyBackupData bd where bd.user.userId=?",user.getUserId());
		
		if(dataList != null && !dataList.isEmpty()){
			return dataList.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO#saveUserData(com.bfrc.dataaccess.model.myprofile.MyBackupData)
	 */
	public void saveUserData(MyBackupData userData) {
		getHibernateTemplate().save(userData);
	}

}
