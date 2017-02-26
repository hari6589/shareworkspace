package com.bfrc.framework.dao.hibernate3;

import java.util.*;

import com.bfrc.framework.dao.MaintenanceDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.Util;

import com.bfrc.pojo.*;
import com.bfrc.pojo.maintenance.MaintSignup;

public class MaintenanceDAOImpl extends HibernateDAOImpl implements MaintenanceDAO {
	public List getServiceList(long acesVehicleId) {
		
		return getServiceList(acesVehicleId,null);
	}
	
	public List getServiceList(long acesVehicleId, String serviceType) {
		boolean byDefault = false;
		if(serviceType == null){
			serviceType ="SEVERE";
			byDefault = true;
		}
		String hql = "select distinct maintInterval.id.mileageInterval, opType.operationType, maintComponent.component " +
		"from Intervals maintInterval left join maintInterval.component maintComponent " +
		"left join maintInterval.operationType opType " +
		"where maintInterval.id.acesVehicleId=? and maintInterval.id.serviceType=? " +
		"order by maintInterval.id.mileageInterval, opType.operationType, maintComponent.component";
		
		List l = getHibernateTemplate().find(hql, new Object[]{new Long(acesVehicleId), serviceType});
		if(byDefault && (l== null || l.size()==0)){
			//check ALL and PRIMIERE if know service available
			if(l== null || l.size()==0){
				l = getHibernateTemplate().find(hql, new Object[]{new Long(acesVehicleId), "ALL"});
			}
			if(l== null|| l.size()==0){
				l = getHibernateTemplate().find(hql, new Object[]{new Long(acesVehicleId), "PREMIUM"});
			}
		}
		return l;
	}
	
	public List getServiceTypeList(long acesVehicleId) {
		String hql = "select distinct maintInterval.id.serviceType " +
		"from Intervals maintInterval left join maintInterval.component maintComponent " +
		"left join maintInterval.operationType opType " +
		"where maintInterval.id.acesVehicleId=?  " +
		"order by 1";
		return getHibernateTemplate().find(hql, new Long(acesVehicleId));
	}
	
	public void insertSignup(MaintSignup m) {
		if(m.getDutySchedule() == null)
			m.setDutySchedule(new Character('S'));
		m.setCreatedDate(new Date());
		getHibernateTemplate().save(m);
	}

	public List getServicesForUserAndDefaultVehicle(User u) {
		if(u == null)
			return null;
		UserVehicle v = u.getVehicle();
		if(v == null)
			return null;
		return getServicesForUserAndVehicle(u, v);
	}

	public List getServicesForUserAndVehicleAndMileage(User u, UserVehicle v, Integer mileage) {
		if(u == null)
			return null;
		if(v == null)
			return null;
		Date mileageDate = v.getMileageDate();
		if(mileageDate == null) {
			Util.debug("mileageDate null for vehicle " + v.getId());
			return null;
		}
		long daysElapsed = (new java.util.Date().getTime() - mileageDate.getTime()) / (1000 * 60 * 60 * 24);
		Integer currMileage = v.getCurrentMileage();
		if(mileage != null)
			currMileage = mileage;
		Integer annualMileage = v.getAnnualMileage();
		int startMileage = currMileage.intValue() + (int)(((double)annualMileage.intValue()*daysElapsed) / 365.0);
		int stopMileage = startMileage + (int)(((double)annualMileage.intValue()*(daysElapsed + DAYS)) / 365.0);
		String hql = "select maintInterval.id.serviceType, maintInterval.id.mileageInterval, " +
			"maintInterval.id.footnoteTxt, maintComponent, opType " +
			"from Intervals maintInterval left join maintInterval.component maintComponent " +
			"left join maintInterval.operationType opType " +
			"where maintInterval.id.acesVehicleId=? and maintInterval.id.mileageInterval >= " +
			startMileage + " and maintInterval.id.mileageInterval <= " + stopMileage + 
			" and maintInterval.id.serviceType='SEVERE' " +
			"order by maintInterval.id.mileageInterval, opType.operationType, maintComponent.component";
		return getHibernateTemplate().find(hql, new Long(v.getAcesVehicleId()));
	}

	public List getServicesForUserAndVehicle(User u, UserVehicle v) {
		return getServicesForUserAndVehicleAndMileage(u, v, null);
	}
}
