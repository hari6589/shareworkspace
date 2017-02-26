/**
 * 
 */
package com.bfrc.framework.dao.appointment;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bfrc.pojo.appointment.rules.AppointmentRule;
import com.bfrc.pojo.appointment.rules.AppointmentRuleSite;

/**
 * @author schowdhu
 *
 */
public interface AppointmentRuleAdminDAO {
	
	public AppointmentRule getRule(int appointmentRuleId) throws DataAccessException;
	
	public Serializable addNewRule(AppointmentRule rule) throws DataAccessException;
	
	public void updateRule(AppointmentRule rule) throws DataAccessException;
	
	public void deleteRule(AppointmentRule rule) throws DataAccessException;
	
	public void assignRuleToSite(AppointmentRuleSite ruleSite) throws DataAccessException;
	
	public void removeRuleFromSite(AppointmentRuleSite ruleSite) throws DataAccessException;
	
	public void updateRuleValueForSite(AppointmentRuleSite ruleSite) throws DataAccessException;
	
	public List<AppointmentRule> getRulesNotDefinedForSite(int siteId) throws DataAccessException;
	
	public boolean isRuleAssignedToSite(int ruleId, int siteId) throws DataAccessException;
	
	public AppointmentRuleSite getAppointmentRuleSite(int ruleId, int siteId) throws DataAccessException;
	
}
