package com.bfrc.dataaccess.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.WebSiteUserVehicleDAO;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserSubvehicle;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserVehicle;

public class WebSiteUserVehicleDAOImpl extends HibernateDaoSupport implements WebSiteUserVehicleDAO {
	
	public WebSiteUserVehicle createWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle) {
		getHibernateTemplate().save(webSiteUserVehicle);
		return webSiteUserVehicle;
	}
	public WebSiteUserVehicle updateWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle) {
		getHibernateTemplate().update(webSiteUserVehicle);
		return webSiteUserVehicle;
	}
	public void deleteWebSiteUserVehicle(Long webSiteUserVehicleId) {
		getHibernateTemplate().delete(getHibernateTemplate().get(WebSiteUserVehicle.class,webSiteUserVehicleId));
	}
	public WebSiteUserVehicle fetchWebSiteUserVehicle(Long webSiteUserVehicleId) {
		DetachedCriteria crit = DetachedCriteria.forClass(WebSiteUserVehicle.class);
		crit.add(Restrictions.eq("webSiteUserVehicleid", webSiteUserVehicleId));
		crit.setFetchMode("webSiteUserSubvehicles", FetchMode.JOIN);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (WebSiteUserVehicle) getHibernateTemplate().findByCriteria(crit).get(0);
	}
	public List<WebSiteUserVehicle> fetchWebSiteUserVehiclesByWebSiteUser(Long webSiteUserId) {
		DetachedCriteria crit = DetachedCriteria.forClass(WebSiteUserVehicle.class);
		crit.add(Restrictions.eq("webSiteUserId", webSiteUserId));
		crit.setFetchMode("webSiteUserSubvehicles", FetchMode.JOIN);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return getHibernateTemplate().findByCriteria(crit);
	}
	public WebSiteUserSubvehicle createWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle) {
		getHibernateTemplate().save(webSiteUserSubvehicle);
		return webSiteUserSubvehicle;
	}
	public WebSiteUserSubvehicle updateWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle) {
		getHibernateTemplate().update(webSiteUserSubvehicle);
		return webSiteUserSubvehicle;
	}
	public void deleteWebSiteUserSubvehicle(Long webSiteUserSubvehicleId) {
		getHibernateTemplate().delete(getHibernateTemplate().get(WebSiteUserSubvehicle.class,webSiteUserSubvehicleId));
	}
	public WebSiteUserSubvehicle fetchWebSiteUserSubvehicle(Long webSiteUserSubvehicleId) {
		return getHibernateTemplate().get(WebSiteUserSubvehicle.class,webSiteUserSubvehicleId);
	}
	
}
