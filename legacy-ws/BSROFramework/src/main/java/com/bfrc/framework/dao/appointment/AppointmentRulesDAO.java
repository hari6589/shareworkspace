package com.bfrc.framework.dao.appointment;

import java.util.List;

import com.bfrc.pojo.appointment.rules.AppointmentRule;
import com.bfrc.pojo.appointment.rules.AppointmentRuleSite;

public interface AppointmentRulesDAO {
	
	public List<AppointmentRule> getAllRules(String siteName);
	
	public List<AppointmentRule> getAllRules(int id);
	
	public List<AppointmentRuleSite> getAllRulesForSite(int siteId);
	
}
