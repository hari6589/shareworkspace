package com.bsro.service.appointment.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.bfrc.Config;
import com.bfrc.framework.dao.appointment.AppointmentRuleAdminDAO;
import com.bfrc.framework.dao.appointment.AppointmentRulesDAO;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.appointment.rules.AppointmentRule;
import com.bfrc.pojo.appointment.rules.AppointmentRuleSite;
import com.bfrc.pojo.appointment.rules.AppointmentRuleTarget;
import com.bfrc.pojo.contact.WebSite;
import com.bsro.service.contact.ContactService;

@Service("appointmentRulesService")
public class AppointmentRulesServiceImpl implements AppointmentRulesService {
	
	//private List<AppointmentRule> appointmentRules; 
	
	private List<AppointmentRuleSite> rulesForSite;
	
	private Map<String, AppointmentRuleSite> rulesByName;
	
	private Map<String, Integer> dayNameToDayInt;
	
	private GregorianCalendar dateToRefreshRules;
	
	@Autowired
	private AppointmentRulesDAO rulesDao;
	
	@Autowired
	private AppointmentRuleAdminDAO rulesAdminDAO;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private Config config;
	
	private final String NUMBER_OF_ELIGIBILE_DAYS = "numberOfEligibleDays";
	private final int DEFAULT_NUMBER_OF_ELIGIBILE_DAYS = 30;
	private final String NO_SAMEDAY_APPOINTMENT = "noSamedayAppointment";
	//no default needed for this one.  Just ignored in method if this does not apply
	private final String LATEST_ELIGIBLE_APPT_TIME = "latestEligibleAppointmentTime";
	private final Integer DEFAULT_LATEST_ELIGIBLE_APPT_TIME = new Integer(1);
	private final String NEXT_ELIGIBLE_APPOINTMENT_TIME = "nextEligibleAppointmentTime";
	private final Integer DEFAULT_NEXT_ELIGIBLE_APPOINTMENT_TIME = new Integer(2);
	private final String HOURS_AFTER_OPEN = "hoursAfterOpen";
	private final Integer DEFAULT_HOURS_AFTER_OPEN = new Integer(2);
	private final String HOURS_BEFORE_CLOSE = "hoursBeforeClose";
	private final Integer DEFAULT_HOURS_BEFORE_CLOSE = new Integer(4);
	private final String APPOINTMENT_INTERVAL = "appointmentInterval";
	private final Integer DEFAULT_APPOINTMENT_INTERVAL = new Integer(15);
	private final String APPOINTMENT_RULE_RELOAD = "appointmentRuleReload";
	private final Integer DEFAULT_RELOAD_TIME = new Integer(8);
	private final String MIN_TIME_BETWEEN_FIRST_AND_SECOND_APPOINTMENT = "minTimeBetween1stAnd2ndAppointment";
	private final Integer DEFAULT_MIN_TIME_BETWEEN_FIRST_AND_SECOND_APPOINTMENT = new Integer(0);
	private final String MIN_TIME_BETWEEN_SECOND_AND_THIRD_APPOINTMENT = "minTimeBetween2ndAnd3rdAppointment";
	private final Integer DEFAULT_MIN_TIME_BETWEEN_SECOND_AND_THIRD_APPOINTMENT = new Integer(3);
	private final String NO_TWO_APPOINTMENTS_PRIOR_TO_NOON = "noTwoAppointmentsPriorToNoon";
	private final String MIN_REQUIRED_APPOINTMENTS = "minAppointmentsPerCustomer";
	private final Integer DEFAULT_MIN_REQUIRED_APPOINTMENTS = 1;
	private final String APPOINTMENT_INTERVAL_PILOT = "appointmentIntervalPilot";
	private final String MIN_TIME_BETWEEN_DROPOFF_AND_PICKUP = "minTimeBetweenDropOffAndPickUp";
	
	@PostConstruct
	public void init(){
		rulesByName = new HashMap<String, AppointmentRuleSite>();
		

		WebSite site = (WebSite)contactService.getMappedWebSites().get(config.getSiteName());
		dateToRefreshRules=new GregorianCalendar();
		
		
		
//		appointmentRules = rulesDao.getAllRules(site.getName());		
//		
//		for(AppointmentRule rule : appointmentRules){
//			rulesByName.put(rule.getAppointmentRuleName(), rule);
//			Util.debug("ruleId: " + rule.getAppointmentRuleId());
//			Util.debug("rule: " + rule.getAppointmentRuleName());
//			
//			for(AppointmentRuleSite ruleSite : rule.getAppointmentRuleSites()){				
//				Util.debug("ruleSite id: " + ruleSite.getId().getSiteId());
//			}
//			
//			Util.debug("Rule targets:");
//			for(AppointmentRuleTarget target : rule.getAppointmentRuleTargets()){
//				Util.debug("targetId: " + target.getAppointmentRuleTargetId());
//				Util.debug("ruleTarget's corresponding rule Id (should match ruleId): " + target.getId().getAppointmentRuleId());
//				Util.debug("target: " + target.getId().getTarget());
//			}
//			
//		}
		

		//appointmentRules = rulesDao.getAllRules(site.getSiteId());
		rulesForSite = rulesDao.getAllRulesForSite(site.getSiteId());
		for(AppointmentRuleSite ruleSite : rulesForSite){
			rulesByName.put(ruleSite.getAppointmentRule().getAppointmentRuleName(), ruleSite);
			//Util.debug("ruleId: " + rule.getAppointmentRuleId() + " ruleName: " + rule.getAppointmentRuleName() + " value " + rule.getAppointmentRuleValue());
			
//			if(!rule.getAppointmentRuleSites().isEmpty()){
//				for(AppointmentRuleSite ruleSite : rule.getAppointmentRuleSites()){				
//					Util.debug("ruleSite id: " + ruleSite.getId().getSiteId());
//				}
//			}			
			
//			if(!rule.getAppointmentRuleTargets().isEmpty()){
//				for(AppointmentRuleTarget target : rule.getAppointmentRuleTargets()){
//					Util.debug("targetId: " + target.getAppointmentRuleTargetId() + " target: " + target.getId().getTarget() + " ruleTarget's corresponding rule Id (should match ruleId): " + target.getId().getAppointmentRuleId());
//				}
//			}		
		}
		
		try{
			AppointmentRuleSite timeToRefreshString = rulesByName.get(APPOINTMENT_RULE_RELOAD);
			dateToRefreshRules.add(Calendar.HOUR, timeToRefreshString.getAppointmentRuleValue());
		}catch(Exception e){
			dateToRefreshRules.add(Calendar.HOUR, DEFAULT_RELOAD_TIME);
		}
		
		dayNameToDayInt = new HashMap<String, Integer>();
		dayNameToDayInt.put("MON", Calendar.MONDAY);
		dayNameToDayInt.put("TUE", Calendar.TUESDAY);
		dayNameToDayInt.put("WED", Calendar.WEDNESDAY);
		dayNameToDayInt.put("THU", Calendar.THURSDAY);
		dayNameToDayInt.put("FRI", Calendar.FRIDAY);
		dayNameToDayInt.put("SAT", Calendar.SATURDAY);
		dayNameToDayInt.put("SUN", Calendar.SUNDAY);
	}
		
	public void checkRulesStillValid(){
		Date newDate = new Date();
		if(newDate.after(dateToRefreshRules.getTime())){
			synchronized(this){
				Util.debug("Executing refresh of the Appointment Rules");
				init();
			}
		}
	}
	
	public AppointmentRuleSite getNumberOfEligibleDays(){
		checkRulesStillValid();
		if(rulesByName.containsKey(NUMBER_OF_ELIGIBILE_DAYS)){
			return rulesByName.get(NUMBER_OF_ELIGIBILE_DAYS);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(NUMBER_OF_ELIGIBILE_DAYS);
			//rule.setAppointmentRuleValue(DEFAULT_NUMBER_OF_ELIGIBILE_DAYS);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_NUMBER_OF_ELIGIBILE_DAYS);
			return ruleSite;
		}		
	}
	
	public AppointmentRuleSite getNoSamedayAppointment(){
		checkRulesStillValid();
		return rulesByName.get(NO_SAMEDAY_APPOINTMENT);		
	}
	
	public AppointmentRuleSite getLatestEligibleAppointmentTime(){
		checkRulesStillValid();
		if(rulesByName.containsKey(LATEST_ELIGIBLE_APPT_TIME)){
			return rulesByName.get(LATEST_ELIGIBLE_APPT_TIME);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(LATEST_ELIGIBLE_APPT_TIME);
			//rule.setAppointmentRuleValue(DEFAULT_LATEST_ELIGIBLE_APPT_TIME);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_LATEST_ELIGIBLE_APPT_TIME);
			return ruleSite;
		}		
	}
	
	public AppointmentRuleSite getNextEligibleAppointmentTime(){
		checkRulesStillValid();
		if(rulesByName.containsKey(NEXT_ELIGIBLE_APPOINTMENT_TIME)){
			return rulesByName.get(NEXT_ELIGIBLE_APPOINTMENT_TIME);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(NEXT_ELIGIBLE_APPOINTMENT_TIME);
//			rule.setAppointmentRuleValue(DEFAULT_NEXT_ELIGIBLE_APPOINTMENT_TIME);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_NEXT_ELIGIBLE_APPOINTMENT_TIME);
			return ruleSite;
		}		
	}
	
	public AppointmentRuleSite getHoursAfterOpen(){
		checkRulesStillValid();
		if(rulesByName.containsKey(HOURS_AFTER_OPEN)){
			return rulesByName.get(HOURS_AFTER_OPEN);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(HOURS_AFTER_OPEN);
//			rule.setAppointmentRuleValue(DEFAULT_HOURS_AFTER_OPEN);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_HOURS_AFTER_OPEN);
			return ruleSite;
		}	
	}
	
	public AppointmentRuleSite getHoursBeforeClose(){
		checkRulesStillValid();
		if(rulesByName.containsKey(HOURS_BEFORE_CLOSE)){
			return rulesByName.get(HOURS_BEFORE_CLOSE);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(HOURS_BEFORE_CLOSE);
//			rule.setAppointmentRuleValue(DEFAULT_HOURS_BEFORE_CLOSE);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_HOURS_BEFORE_CLOSE);
			return ruleSite;
		}			
	}
	
	public AppointmentRuleSite getAppointmentInterval(){
		checkRulesStillValid();
		if(rulesByName.containsKey(APPOINTMENT_INTERVAL)){
			return rulesByName.get(APPOINTMENT_INTERVAL);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(APPOINTMENT_INTERVAL);
//			rule.setAppointmentRuleValue(DEFAULT_APPOINTMENT_INTERVAL);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_APPOINTMENT_INTERVAL);
			return ruleSite;
		}	
	}
	
	public AppointmentRuleSite getMinTimeBetween1stAnd2ndAppointment(){
		checkRulesStillValid();
		if(rulesByName.containsKey(MIN_TIME_BETWEEN_FIRST_AND_SECOND_APPOINTMENT)){
			return rulesByName.get(MIN_TIME_BETWEEN_FIRST_AND_SECOND_APPOINTMENT);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(MIN_TIME_BETWEEN_FIRST_AND_SECOND_APPOINTMENT);
//			rule.setAppointmentRuleValue(DEFAULT_MINIMUM_TIME_BETWEEN_APPOINTMENTS);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_MIN_TIME_BETWEEN_FIRST_AND_SECOND_APPOINTMENT);
			return ruleSite;
		}
				
	}
	
	public AppointmentRuleSite getMinTimeBetween2ndAnd3rdAppointment(){
		checkRulesStillValid();
		if(rulesByName.containsKey(MIN_TIME_BETWEEN_SECOND_AND_THIRD_APPOINTMENT)){
			return rulesByName.get(MIN_TIME_BETWEEN_SECOND_AND_THIRD_APPOINTMENT);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(MIN_TIME_BETWEEN_SECOND_AND_THIRD_APPOINTMENT);
			//rule.setAppointmentRuleValue(DEFAULT_MINIMUM_TIME_BETWEEN_APPOINTMENTS);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_MIN_TIME_BETWEEN_SECOND_AND_THIRD_APPOINTMENT);
			return ruleSite;
		}
				
	}
	
	public AppointmentRuleSite getMinRequiredAppointments(){
		checkRulesStillValid();
		if(rulesByName.containsKey(MIN_REQUIRED_APPOINTMENTS)){
			return rulesByName.get(MIN_REQUIRED_APPOINTMENTS);	
		}else{
			AppointmentRule rule = new AppointmentRule();
			rule.setAppointmentRuleDescription(String.valueOf(DEFAULT_MIN_REQUIRED_APPOINTMENTS));
			//rule.setAppointmentRuleValue(DEFAULT_MINIMUM_TIME_BETWEEN_APPOINTMENTS);
			AppointmentRuleSite ruleSite = new AppointmentRuleSite();
			ruleSite.setAppointmentRule(rule);
			ruleSite.setAppointmentRuleValue(DEFAULT_MIN_REQUIRED_APPOINTMENTS);
			return ruleSite;
		}
	}
	
	public boolean isSamedayAppointmentAllowed(String dayOfWeek){
		AppointmentRuleSite ruleSite = this.getNoSamedayAppointment();
		
		if(ruleSite == null){
			//rule isn't setup, which is OK.
			//Util.debug("noSamedayAppointment rule is not setup for this site.  This may be intentional. " + config.getSiteName());
			return true;
		}else{
			Set<AppointmentRuleTarget> targets = ruleSite.getAppointmentRule().getAppointmentRuleTargets();
			if(targets.isEmpty()){
				//Util.debug("AppointmentRuleTarget for the noSamedayAppointment rule was null / empty, this means data isn't set up correctly ");
				return true;
			}else{
				for(AppointmentRuleTarget target : targets){
					String dayValue = target.getId().getTarget();
					//int dayIntValue = dayNameToDayInt.get(dayValue).intValue();
					//Util.debug(" day that we are checking dayOfWeek= "+dayOfWeek + " day that we are removing dayIntValue = "+dayValue);
					//return false, meaning day is not eligible for same day appointments.
					if(dayValue.equalsIgnoreCase(dayOfWeek)) return false;
				}
				
				//never found a matching day, return true;
				return true;
			}
			
		}

	}
	
	public AppointmentRuleSite getNoTwoAppointmentsPriorToNoon(){
		checkRulesStillValid();
		return rulesByName.get(NO_TWO_APPOINTMENTS_PRIOR_TO_NOON);		
	}
	
	public boolean areTwoAppointmentsPriorToNoonAllowed(){
		AppointmentRuleSite ruleSite = this.getNoTwoAppointmentsPriorToNoon();
		
		if(ruleSite == null || ruleSite.getAppointmentRuleValue() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public AppointmentRuleSite getMinTimeBetweenDropOffAndPickUp(){
		checkRulesStillValid();
		if(rulesByName.containsKey(MIN_TIME_BETWEEN_DROPOFF_AND_PICKUP)){
			return rulesByName.get(MIN_TIME_BETWEEN_DROPOFF_AND_PICKUP);	
		}else{
			return null;
		}
	}
	
	public AppointmentRuleSite getAppointmentIntervalPilot(){
		checkRulesStillValid();
		if(rulesByName.containsKey(APPOINTMENT_INTERVAL_PILOT)){
			return rulesByName.get(APPOINTMENT_INTERVAL_PILOT);	
		}else{
			return null;
		}
	}
	
	@Override
	public List<Integer> getTwoAppointmentsPriortoNoonNotAllowedTargetDays(){
		List<Integer> days = new ArrayList<Integer>();
		AppointmentRuleSite ruleSite = this.getNoTwoAppointmentsPriorToNoon();
		if (ruleSite != null) {
			Set<AppointmentRuleTarget> targets = ruleSite.getAppointmentRule().getAppointmentRuleTargets();
			if(!targets.isEmpty()){
				for(AppointmentRuleTarget target : targets){
					String dayValue = target.getId().getTarget();
					if(dayValue.equalsIgnoreCase("SAT")){
						days.add(Calendar.SATURDAY);
					}else if(dayValue.equalsIgnoreCase("SUN")){
						days.add(Calendar.SUNDAY);
					}else if(dayValue.equalsIgnoreCase("MON")){
						days.add(Calendar.MONDAY);
					}else if(dayValue.equalsIgnoreCase("TUE")){
						days.add(Calendar.TUESDAY);
					}else if(dayValue.equalsIgnoreCase("WED")){
						days.add(Calendar.WEDNESDAY);
					}else if(dayValue.equalsIgnoreCase("THU")){
						days.add(Calendar.THURSDAY);
					}else if(dayValue.equalsIgnoreCase("FRI")){
						days.add(Calendar.FRIDAY);
					}
				}
			}
		}
		return days;
		
	}
	
	
	//setter injection for appointmentRulesAdminDAO for junit tests
	

	public void setRulesDao(AppointmentRulesDAO rulesDao) {
		this.rulesDao = rulesDao;
	}

	public void setRulesAdminDAO(AppointmentRuleAdminDAO rulesAdminDAO) {
		this.rulesAdminDAO = rulesAdminDAO;
	}

	@Override
	public AppointmentRule getRule(int appointmentId)
			throws DataAccessException {
		//List<AppointmentRule> rules = rulesDao.getAllRules(1);
		AppointmentRule rule = rulesAdminDAO.getRule(appointmentId);
		return rule;
	}

	@Override
	public Serializable addNewRule(AppointmentRule rule)
			throws DataAccessException {
		return rulesAdminDAO.addNewRule(rule);
	}

	@Override
	public void updateRule(AppointmentRule rule) throws DataAccessException {
		rulesAdminDAO.updateRule(rule);
		
	}

	@Override
	public void deleteRule(AppointmentRule rule) throws DataAccessException {
		rulesAdminDAO.deleteRule(rule);
		
	}

	@Override
	public void assignRuleToSite(AppointmentRuleSite ruleSite)
			throws DataAccessException {
		rulesAdminDAO.assignRuleToSite(ruleSite);
		
	}

	@Override
	public void removeRuleFromSite(AppointmentRuleSite ruleSite)
			throws DataAccessException {
		rulesAdminDAO.removeRuleFromSite(ruleSite);
		
	}

	@Override
	public void updateRuleValueForSite(AppointmentRuleSite ruleSite)
			throws DataAccessException {
		rulesAdminDAO.updateRuleValueForSite(ruleSite);
		
	}

	@Override
	public List<AppointmentRule> getRulesNotDefinedForSite(int siteId)
			throws DataAccessException {
		return rulesAdminDAO.getRulesNotDefinedForSite(siteId);
	}

	@Override
	public boolean isRuleAssignedToSite(int ruleId, int siteId) throws DataAccessException{
		return rulesAdminDAO.isRuleAssignedToSite(ruleId, siteId);
	}
	@Override
	public AppointmentRuleSite getAppointmentRuleSite(int ruleId, int siteId) throws DataAccessException{
		return rulesAdminDAO.getAppointmentRuleSite(ruleId, siteId);
	}
	
}
