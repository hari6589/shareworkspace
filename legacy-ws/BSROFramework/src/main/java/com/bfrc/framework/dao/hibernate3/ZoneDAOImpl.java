package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.bfrc.framework.dao.ZoneDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.StoreSearchUtils;
import com.bfrc.pojo.geo.ZoneList;
import com.bfrc.pojo.geo.ZoneManager;

public class ZoneDAOImpl extends HibernateDAOImpl implements ZoneDAO {

	
	public ZoneManager getZoneManagerByEmail(String email) {
		String hql = "from ZoneManager zm where zm.emailAddress=?";
		Object[] args = new Object[] { email };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (ZoneManager) l.get(0);
	}
	public List getZonesByEmail(String email) {
		String hql = "select distinct z from ZoneList z, ZoneManager zm where z.id = zm.id.zoneId and zm.emailAddress=? order by z.zoneName";
		Object[] args = new Object[] { email };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		
		return  l;
	}
	public List getZonesByEmail(String email, String brand) {
		Session s = getSession();
		String cons = StoreSearchUtils.getBrandStoreTypesClause(brand);
		try{
		String hql = "select distinct d.DISTRICT_ZONE as dz from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID and "+cons;
		List l = getSession().createSQLQuery(hql).addScalar("dz", Hibernate.STRING).list();
		if (l == null || l.size() == 0)
			return null;
		
		String zoneIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);

			if(lstring.startsWith("0")){
				lstring=lstring.substring(1);
			}
			if(first)
				zoneIds+= lstring;
			else
				zoneIds += ", "+lstring;
				first = false;
		}

		if(zoneIds.length()==0){
			return null;
		}
		hql = "select distinct z from ZoneList z, ZoneManager zm where z.id = zm.id.zoneId and z.id in ("+zoneIds+") and zm.emailAddress=? order by z.zoneName";
		Object[] args = new Object[] { email };
		l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		
		return  l;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	
	public List<ZoneList> getZonesByEmail(String email, String[] brands) {
		Session s = getSession();

		try{
		String hql = "select distinct d.DISTRICT_ZONE as dz from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID";
		if (brands != null && brands.length > 0) {
			hql += " and TRIM(BOTH FROM s.STORE_TYPE) in (:brands)";
		}
		SQLQuery query1 =  s.createSQLQuery(hql);
		
		if (brands != null && brands.length > 0) {
			query1.setParameterList("brands", brands);
		}
		
		List l = query1.addScalar("dz", Hibernate.STRING).list();
		if (l == null || l.size() == 0)
			return null;
		
		String zoneIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);

			if(lstring.startsWith("0")){
				lstring=lstring.substring(1);
			}
			if(first)
				zoneIds+= lstring;
			else
				zoneIds += ", "+lstring;
				first = false;
		}

		if(zoneIds.length()==0){
			return null;
		}
		hql = "select distinct z from ZoneList z, ZoneManager zm where z.id = zm.id.zoneId and z.id in ("+zoneIds+") and zm.emailAddress=? order by z.zoneName";
		Object[] args = new Object[] { email };
		l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		
		return  l;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}	
	
	public List getAllZones() {
		String hql = "from ZoneList z order by z.zoneName";
		List l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		
		return  l;
	}
	public List getAllZonesByBrand(String brand) {	
		Session s= getSession();
		String cons = StoreSearchUtils.getBrandStoreTypesClause(brand);
		try{
		String hql = "select distinct d.DISTRICT_ZONE as dz from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID and "+cons;
		List l = getSession().createSQLQuery(hql).addScalar("dz", Hibernate.STRING).list();
		if (l == null || l.size() == 0)
			return null;
		
		String zoneIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);

			if(lstring.startsWith("0")){
				lstring=lstring.substring(1);
			}
			if(first)
				zoneIds+= lstring;
			else
				zoneIds += ", "+lstring;
				first = false;
		}

		if(zoneIds.length()==0){
			return null;
		}
		hql = "select distinct z from ZoneList z where z.zoneId in("+zoneIds+") order by z.zoneName";
		l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		return  l;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}

	public List<ZoneList> getAllZonesByBrand(String[] brands) {	
		Session s= getSession();

		try{
		String hql = "select distinct d.DISTRICT_ZONE as dz from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID";
		if (brands != null && brands.length > 0) {
			hql += " and TRIM(BOTH FROM s.STORE_TYPE) in (:brands)";
		}
		SQLQuery query1 =  s.createSQLQuery(hql);
		
		if (brands != null && brands.length > 0) {
			query1.setParameterList("brands", brands);
		}
		
		List l = query1.addScalar("dz", Hibernate.STRING).list();
		
		if (l == null || l.size() == 0) {
			return null;
		}
		
		String zoneIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);

			if(lstring.startsWith("0")){
				lstring=lstring.substring(1);
			}
			if(first)
				zoneIds+= lstring;
			else
				zoneIds += ", "+lstring;
				first = false;
		}

		if(zoneIds.length()==0){
			return null;
		}
		hql = "select distinct z from ZoneList z where z.zoneId in("+zoneIds+") order by z.zoneName";
		l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		return  l;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	
}
