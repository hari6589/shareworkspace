package com.bfrc.framework.dao.hibernate3;

import java.util.*;

import org.hibernate.Query;

import com.bfrc.framework.spring.*;
import com.bfrc.framework.util.ServerUtil;

import com.bfrc.framework.dao.EmailSignupDAO;
import com.bfrc.pojo.email.EmailSignup;
import com.bfrc.pojo.email.EmailUnsubscribe;
import com.bfrc.pojo.reward.RewardsLog;

public class EmailSignupDAOImpl extends HibernateDAOImpl implements
		EmailSignupDAO {

	public void confirm(String optinCode) {
		if(optinCode == null)
			return;
		List l = getHibernateTemplate().find("from EmailSignup es where es.optinCode=?", optinCode);
		if(l == null || l.size() == 0)
			return;
		EmailSignup e = (EmailSignup)l.get(0);
		e.setConfirmOptin(optinCode);
		getHibernateTemplate().update(e);
	}

	public boolean subscribe(EmailSignup es) {
		// Note: Hibernate3's merge operation does not reassociate the object with the
		// current Hibernate Session. Instead, it will always copy the state over to
		// a registered representation of the entity. In case of a new entity, it will
		// register a copy as well, but will not update the id of the passed-in object.
		// To still update the ids of the original objects too, we need to register
		// Spring's IdTransferringMergeEventListener on our SessionFactory.
		if(ServerUtil.isNullOrEmpty(es.getSource()))
			es.setSource(getConfig().getSiteName());
		if (getHibernateTemplate().merge(es) != null)
			return true;
		return false;		
	}

	public boolean unsubscribe(Long signUpId) {
		EmailSignup es = getEmailSignup(signUpId);
		EmailUnsubscribe eu = new EmailUnsubscribe();
		eu.setEmailAddress(es.getEmailAddress());
		eu.setSource(es.getSource());
		eu.setUnsubscribeDate(new Date());
		if (getHibernateTemplate().merge(eu) != null) {
			getHibernateTemplate().delete(es);
			return true;
		}
		return false;
	}
	
	public boolean unsubscribe(String email, String siteName) {
		
		EmailUnsubscribe eu = new EmailUnsubscribe();
		boolean success = true;
		try{
		
		List <EmailSignup> es = getEmailSignup(email,siteName);
		
		if(es != null && !es.isEmpty())
		{
		eu.setEmailAddress(email);
		eu.setSource(siteName);
		eu.setUnsubscribeDate(new Date());
		List <EmailSignup> emailSign = new ArrayList<EmailSignup>();
		for(EmailSignup sign:es)
		{
			if(sign.getCreatedDate() !=null)
				sign.setCreatedDate(new Date());
			
			emailSign.add(sign);
		}
		if (getHibernateTemplate().save(eu) != null) {
			getHibernateTemplate().deleteAll(emailSign);
			success = true;
		}
		}
		else
		{
			success = false;
		}
		}catch(Exception ex){
			success = false;
		}
		return success;
	}
	
	public EmailSignup getEmailSignup(Long signUpId) {		
		return (EmailSignup) getHibernateTemplate().load(EmailSignup.class, signUpId);
	}
	
	public List<EmailSignup> getEmailSignup(String email) {
		return getEmailSignup(email, null);
	}

	public List<EmailSignup> getEmailSignup(String email, String siteName) {
		
		List <EmailSignup> user = new ArrayList<EmailSignup>();
		try
		{
			String hql = (siteName != null && !siteName.isEmpty()) ? "from EmailSignup es where lower(es.emailAddress)=:email and es.source=:siteName" : 
				"from EmailSignup es where lower(es.emailAddress)=:email"; 
			Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
			query.setString("email", email);
			if (siteName != null && !siteName.isEmpty()) {
				query.setString("siteName", siteName);
			}
			user = query.list();
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
		return user;
		//return getHibernateTemplate().find("from EmailSignup es where lower(es.emailAddress)=? and es.source=?", new Object[]{email.toLowerCase(),siteName});
	}
	
	public List getDelayedSignups(String source) {
		return getHibernateTemplate().find("from EmailSignup es where es.emailAddress !='blank@bsro.com' and  ? > (es.createdDate+15) and es.source=? and es.actionCode='DELAY'", new Object[]{new Date(), source});
	}
	
	public List getDelayedSignups() {
		return getHibernateTemplate().find("from EmailSignup es where es.emailAddress !='blank@bsro.com' and ? > (es.createdDate+15)  and es.actionCode='DELAY'", new Date());
	}
	
	public long update(EmailSignup es) {
		
		try{
			getHibernateTemplate().update(es);
			return es.getSignupId().longValue();
		}catch(Exception ex){
			
		}
		return -1L;		
	}
	
	public void addRewardsLog(RewardsLog rewardsLog){
		getHibernateTemplate().save(rewardsLog);
	}

}
