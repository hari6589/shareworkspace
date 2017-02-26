package com.bfrc.dataaccess.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;

import com.bfrc.dataaccess.dao.WebSiteUserDAO;
import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserSubvehicle;

public class WebSiteUserDAOImpl extends HibernateDaoSupport implements WebSiteUserDAO {

	protected PlatformTransactionManager txManager;
	
	public WebSiteUser authenticateUser(String loginName, String pwd, Long webSiteId) {
			
		if(pwd!=null){
			DetachedCriteria crit = DetachedCriteria.forClass(WebSiteUser.class);
			crit.add(Restrictions.eq("emailAddress", loginName));
			crit.add(Restrictions.eq("webSiteId", webSiteId));
			List<WebSiteUser> usersToCheck = getHibernateTemplate().findByCriteria(crit);
	
			//TODO encrypt/decrypt password 
			if(usersToCheck!=null && usersToCheck.size()>0){
				WebSiteUser userToCheck = usersToCheck.get(0);
				if(userToCheck.getPassword()!=null){
					//decrypt String decryptedPassword = EncryptionUtil.decrypt(userToCheck.getPassword());
					String decryptedPassword = userToCheck.getPassword();
					if(decryptedPassword.equals(pwd))
						return userToCheck;
				}
			}
		
		}
		return null;
			
	}
	public WebSiteUser createUser(WebSiteUser webSiteUser) {
		getHibernateTemplate().save(webSiteUser);
		return webSiteUser;
	}
	public WebSiteUser updateUserPassword(WebSiteUser webSiteUser, String password) {
		//TODO encrypt password 
		//encrypt webSiteUser.setPassword(EncryptionUtil.encrypt(userToCheck.getPassword());
		webSiteUser.setPassword(password);
		getHibernateTemplate().update(webSiteUser);
		return webSiteUser;
	}
	public WebSiteUser updateUser(WebSiteUser webSiteUser) {
		//decrypt String decryptedPassword = EncryptionUtil.encrypt(userToCheck.getPassword());
		getHibernateTemplate().update(webSiteUser);
		return webSiteUser;
	}
	public WebSiteUser getUserByEmail(String email, Long webSiteId) {
		DetachedCriteria crit = DetachedCriteria.forClass(WebSiteUser.class);
		crit.add(Restrictions.eq("emailAddress", email));
		crit.add(Restrictions.eq("webSiteId", webSiteId));
		List<WebSiteUser> usersToCheck = getHibernateTemplate().findByCriteria(crit);
		if(!usersToCheck.isEmpty())
			return usersToCheck.get(0);
		return null;
	}
	public WebSiteUser getUserById(Long webSiteUserId) {
		return getHibernateTemplate().get(WebSiteUser.class,webSiteUserId);		
	}

	
}
