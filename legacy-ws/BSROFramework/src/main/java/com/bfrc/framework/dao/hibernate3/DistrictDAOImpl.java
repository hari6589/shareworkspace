package com.bfrc.framework.dao.hibernate3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.bfrc.framework.dao.DistrictDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.StoreSearchUtils;
import com.bfrc.pojo.geo.DistrictManager;
import com.bfrc.pojo.geo.HrDistricts;
import com.bfrc.pojo.store.Store;

public class DistrictDAOImpl extends HibernateDAOImpl implements DistrictDAO {

	public List getDistrictsInState(String state) {
		Store bean = new Store();
		bean.setState(state);
		String hql = "select d from HrDistricts d join d.stores s where s.state=:state and (";
		Map m = this.storeDAO.getFullStoreMap(getConfig());
		Iterator i = m.keySet().iterator();
		while(i.hasNext()) {
			String type = (String)i.next();
			hql += "s.storeType='" + type + "'";
			if(i.hasNext())
				hql += " or ";
		}
		hql += ")";
		return getHibernateTemplate().findByValueBean(hql, bean);
	}

	private StoreDAO storeDAO;
	public StoreDAO getStoreDAO() {
		return this.storeDAO;
	}

	public void setStoreDAO(StoreDAO dao) {
		this.storeDAO = dao;
	}

	public com.bfrc.pojo.geo.HrDistricts getDistrict(String id) {
		HrDistricts bean = new HrDistricts();
		bean.setDistrictId(id);
		String hql = "from HrDistricts d "
			+ " where d.districtId=:districtId"; 
		List o = getHibernateTemplate().findByValueBean(hql, bean);
		if(o == null)
			return null;
		return (HrDistricts)o.get(0);
	}
	public List getDistrictsByZone(String zoneId) {
		if(zoneId.length()==1){
			zoneId = "0"+zoneId;
		}
		List l = getHibernateTemplate().findByNamedParam("select distinct d from HrDistricts d"
			+ " where d.districtZone=:zoneId order by d.districtName",
				"zoneId",
				zoneId);
		if(l == null)
			return null;
		return l;
	}
	public List getDistrictsByZone(String zoneId, String brand) {
		if(zoneId.length()==1){
			zoneId = "0"+zoneId;
		}
//		List l = getHibernateTemplate().findByNamedParam("select distinct d from HrDistricts d"
//			+ " where d.districtZone=:zoneId order by d.districtName",
//				"zoneId",
//				zoneId);
//		if(l == null)
//			return null;
//		return l;
		String cons = StoreSearchUtils.getBrandStoreTypesClause(brand);
		String hql = "select distinct d.DISTRICT_ID as di from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID and d.DISTRICT_ZONE = '"+zoneId+"' and "+cons;
		Session s = getSession();
		try{
		List l = s.createSQLQuery(hql).addScalar("di", Hibernate.STRING).list();
		if (l == null || l.size() == 0)
			return null;
		
		String districtIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);
			if(first)
				districtIds+= "'"+lstring+"'";
			else
				districtIds += ", '"+lstring+"'";
				first = false;
		}

		if(districtIds.length()==0){
			return null;
		}
		hql = "select distinct d from HrDistricts d where d.districtId in("+districtIds+") order by d.districtName";
		l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		return  l;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}

	public List getDistrictsByZone(String zoneId, String[] brands) {
		if(zoneId.length()==1){
			zoneId = "0"+zoneId;
		}

		String hql = "select distinct d.DISTRICT_ID as di from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID and d.DISTRICT_ZONE = ?";
		if (brands != null && brands.length > 0) {
			hql += " and TRIM(BOTH FROM s.STORE_TYPE) in (:brands)";
		}
		Session s = getSession();
		try{
		SQLQuery query1 =  s.createSQLQuery(hql);
		
		query1.setString(0, zoneId);
		
		if (brands != null && brands.length > 0) {
			query1.setParameterList("brands", brands);
		}
		
		List l = query1.addScalar("di", Hibernate.STRING).list();
		if (l == null || l.size() == 0)
			return null;
		
		String districtIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);
			if(first)
				districtIds+= "'"+lstring+"'";
			else
				districtIds += ", '"+lstring+"'";
				first = false;
		}

		if(districtIds.length()==0){
			return null;
		}
		hql = "select distinct d from HrDistricts d where d.districtId in("+districtIds+") order by d.districtName";
		l = getHibernateTemplate().find(hql);
		if (l == null || l.size() == 0)
			return null;
		return  l;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}	
	
	public List getDistrictsByEmail(String email) {
		String hql = "select distinct d from HrDistricts d, DistrictManager dm where d.id = dm.district.id and dm.emailAddress=?";
		Object[] args = new Object[] { email };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		
		return  l;
	}
	public List<HrDistricts> getDistrictsByEmail(String email, String brand) {
		Session s = getSession();
		String cons = StoreSearchUtils.getBrandStoreTypesClause(brand);
		try{
		String hql = "select distinct d.DISTRICT_ID as di from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID and "+cons;
		List l =s.createSQLQuery(hql).addScalar("di", Hibernate.STRING).list();
		if (l == null || l.size() == 0)
			return null;
		
		String districtIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);
			if(first)
				districtIds+= "'"+lstring+"'";
			else
				districtIds += ", '"+lstring+"'";
				first = false;
		}
		if(districtIds.length()==0){
			return null;
		}
		hql = "select distinct d from HrDistricts d, DistrictManager dm where d.id = dm.district.id and d.id in ("+districtIds+") and dm.emailAddress=?";
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
	
	public List<HrDistricts> getDistrictsByEmail(String email, String[] brands) {
		Session s = getSession();

		try{
		String hql = "select distinct d.DISTRICT_ID as di from STORE s, HR_DISTRICTS d where s.DISTRICT_ID = d.DISTRICT_ID";
		if (brands != null && brands.length > 0) {
			hql += " and TRIM(BOTH FROM s.STORE_TYPE) in (:brands)";
		}
		SQLQuery query1 =  s.createSQLQuery(hql);

		if (brands != null && brands.length > 0) {
			query1.setParameterList("brands", brands);
		}
		
		List l = query1.addScalar("di", Hibernate.STRING).list();
		if (l == null || l.size() == 0)
			return null;
		
		String districtIds = "";
		boolean first = true;
		for(int i=0;i<l.size();i++){
			
			String lstring = (String)l.get(i);
			if(first)
				districtIds+= "'"+lstring+"'";
			else
				districtIds += ", '"+lstring+"'";
				first = false;
		}
		if(districtIds.length()==0){
			return null;
		}
		hql = "select distinct d from HrDistricts d, DistrictManager dm where d.id = dm.district.id and d.id in ("+districtIds+") and dm.emailAddress=?";
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
	
	public List getStores(String id) {
		Map m = new HashMap();
		m.put("district", id);
		List l = this.storeDAO.getStores(m);
		List out = new ArrayList();
		for(int i=0;i<l.size();i++) {
			out.add(((Object[])l.get(i))[1]);
		}
		return out;
	}

	public DistrictManager getDistrictManager(String id) {
		HrDistricts bean = new HrDistricts();
		bean.setDistrictId(id);
		String hql = "from DistrictManager dm join dm.district d"
			+ " where d.districtId=:districtId and dm.id.managerType='DM'"; 
		List l = getHibernateTemplate().findByValueBean(hql, bean);
		Object[] o = (Object[])l.get(0); 
		return (DistrictManager)o[0];
	}
	public DistrictManager getDistrictManagerByEmail(String email) {
		String hql = "from DistrictManager dm where dm.emailAddress=?";
		Object[] args = new Object[] { email };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (DistrictManager) l.get(0);
	}
	public List getDistrictIdsByZone(String zoneId) {
		if(zoneId.length()==1){
			zoneId = "0"+zoneId;
		}
		List l = getHibernateTemplate().findByNamedParam("select distinct d from HrDistricts d "
			+ " where d.districtZone=:zoneId order by d.districtName",
				"zoneId",
				zoneId);
		if(l == null)
			return null;
		return l;
	}

}
