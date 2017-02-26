package com.bsro.service.appointment.rules;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bfrc.framework.dao.appointment.AppointmentRuleAdminDAO;
import com.bfrc.framework.dao.appointment.AppointmentRulesDAO;
import com.bfrc.pojo.appointment.rules.AppointmentRule;
import com.bfrc.pojo.appointment.rules.AppointmentRuleSite;

public interface AppointmentRulesService {
	
	public void checkRulesStillValid();
	
	public AppointmentRuleSite getNumberOfEligibleDays();
	
	public AppointmentRuleSite getNoSamedayAppointment();
	public AppointmentRuleSite getLatestEligibleAppointmentTime();
	
	public AppointmentRuleSite getNextEligibleAppointmentTime();
	
	public AppointmentRuleSite getHoursAfterOpen();
	
	public AppointmentRuleSite getHoursBeforeClose();
	
	public boolean isSamedayAppointmentAllowed(String dayOfWeek);
	
	public AppointmentRuleSite getAppointmentInterval();
	
	public AppointmentRuleSite getMinRequiredAppointments();
	
	public AppointmentRuleSite getMinTimeBetween1stAnd2ndAppointment();
	
	public AppointmentRuleSite getMinTimeBetween2ndAnd3rdAppointment();
	
	public AppointmentRuleSite getNoTwoAppointmentsPriorToNoon();
	
	public List<Integer> getTwoAppointmentsPriortoNoonNotAllowedTargetDays();
	
	public AppointmentRuleSite getMinTimeBetweenDropOffAndPickUp();
	
	public AppointmentRuleSite getAppointmentIntervalPilot();
	
	public boolean areTwoAppointmentsPriorToNoonAllowed();
	
	public void setRulesDao(AppointmentRulesDAO rulesDao);

	public void setRulesAdminDAO(AppointmentRuleAdminDAO rulesAdminDAO);
	
	public AppointmentRule getRule(int appointmentId) throws DataAccessException;
	
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