package com.bfrc.framework.dao.appointment;

import java.util.List;

import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.appointment.rules.AppointmentRule;
import com.bfrc.pojo.appointment.rules.AppointmentRuleSite;

public class AppointmentRulesDAOImpl extends HibernateDAOImpl implements AppointmentRulesDAO{

	@Override
	public List<AppointmentRule> getAllRules(String siteName) {
		Util.debug("BEGIN initializing rules siteName...");
		
		List<AppointmentRule> rules = getHibernateTemplate().find("select rule from AppointmentRule as rule, AppointmentRuleSite as ruleSite, WebSite w " + 
				" where rule.appointmentRuleId = ruleSite.id.appointmentRuleId and ruleSite.id.siteId = w.siteId and w.name = ?", siteName);
			
		Util.debug("DONE initializing rules siteName...");
		
		
		return rules;
	}
	
	public List<AppointmentRule> getAllRules(int siteId) {
		Util.debug("BEGIN initializing rules siteId...");
		List<AppointmentRule> rules =getHibernateTemplate().find("select rule from AppointmentRule as rule inner join rule.appointmentRuleSites sites with sites.id.siteId =  ? ", siteId);
		Util.debug("DONE initializing rules siteId...");
		return rules;
	}
	
	public List<AppointmentRuleSite> getAllRulesForSite(int siteId) {
		Util.debug("BEGIN initializing rules siteId...");
		List<AppointmentRuleSite> ruleSites =getHibernateTemplate().find("select ruleSite from AppointmentRuleSite as ruleSite where ruleSite.id.siteId =  ? ", siteId);
		Util.debug("DONE initializing rules siteId...");
		return ruleSites;
	}

}
