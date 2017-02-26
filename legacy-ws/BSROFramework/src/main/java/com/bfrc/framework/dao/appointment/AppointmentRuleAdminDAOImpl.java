/**
 * 
 */
package com.bfrc.framework.dao.appointment;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockMode;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;

import com.bfrc.pojo.appointment.rules.AppointmentRule;
import com.bfrc.pojo.appointment.rules.AppointmentRuleSite;

/**
 * @author schowdhu
 *
 */
public class AppointmentRuleAdminDAOImpl extends HibernateDaoSupport implements AppointmentRuleAdminDAO {
	
	private PlatformTransactionManager txManager;
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	@Override
	public Serializable addNewRule(AppointmentRule rule) throws DataAccessException {
		return getHibernateTemplate().save(rule);

	}
	
	@Override
	public AppointmentRule getRule(int appointmentRuleId) throws DataAccessException {
		List<AppointmentRule> rule = getHibernateTemplate().find("select ar from AppointmentRule ar where ar.appointmentRuleId=?", appointmentRuleId);
		return rule.get(0);
	}

	@Override
	public void updateRule(AppointmentRule rule) throws DataAccessException {
		getHibernateTemplate().update(rule, LockMode.WRITE);
		
	}

	@Override
	public void deleteRule(AppointmentRule rule) throws DataAccessException {
		getHibernateTemplate().delete(rule, LockMode.WRITE);
		
	}

	@Override
	public void assignRuleToSite(AppointmentRuleSite ruleSite)
			throws DataAccessException {
		getHibernateTemplate().save(ruleSite);
		
	}

	@Override
	public void removeRuleFromSite(AppointmentRuleSite ruleSite)
			throws DataAccessException {
		getHibernateTemplate().delete(ruleSite, LockMode.WRITE);
		
	}

	@Override
	public void updateRuleValueForSite(AppointmentRuleSite ruleSite)
			throws DataAccessException {
		getHibernateTemplate().update(ruleSite, LockMode.WRITE);
		
	}

	@Override
	public List<AppointmentRule> getRulesNotDefinedForSite(int siteId)
			throws DataAccessException {
		return getHibernateTemplate().find("from AppointmentRule ar where ar.appointmentRuleId NOT IN " +
				"(select ars.id.appointmentRuleId from appointmentRuleSite ars where ars.id.siteId = ?)", siteId);
	}
	
	@Override
	public boolean isRuleAssignedToSite(int ruleId, int siteId){
		Object[] params = {ruleId, siteId};
		List<AppointmentRuleSite> ruleSites = getHibernateTemplate().find("from AppointmentRuleSite ars where ars.id.appointmentRuleId = ? " +
				" and ars.id.siteId = ? ");
		return ruleSites.size() > 0;
	}

	@Override
	public AppointmentRuleSite getAppointmentRuleSite(int ruleId, int siteId)
			throws DataAccessException {
		Object[] params = {ruleId, siteId};
		List<AppointmentRuleSite> list =getHibernateTemplate().find("from AppointmentRuleSite ars where ars.id.appointmentRuleId = ? " +
			" and ars.id.siteId = ?", params);
		if(!list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}

}
