package com.bfrc.dataaccess.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.oil.OilDAO;
import com.bfrc.dataaccess.model.oil.OatsOilRecommendationCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleMakeCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearToMake;
import com.bfrc.dataaccess.model.oil.Oil;
import com.bfrc.dataaccess.model.oil.OilChange;
import com.bfrc.dataaccess.model.oil.OilFilter;
import com.bfrc.dataaccess.model.oil.OilType;

public class OilDAOImpl extends HibernateDaoSupport implements OilDAO {

	public Oil get(Long articleNumber) {
		if (articleNumber == null)
			return null;
		return (Oil) getHibernateTemplate().get(Oil.class,
				Long.valueOf(articleNumber.toString()));
	}
	
	public OilType getOilType(Long oilTypeId) {
		if (oilTypeId == null)
			return null;
		return (OilType) getHibernateTemplate().get(OilType.class,
				Long.valueOf(oilTypeId.toString()));
	}
	
	public OilChange getOilChange(Long articleNumber) {
		if (articleNumber == null)
			return null;
		return (OilChange) getHibernateTemplate().get(OilChange.class,
				articleNumber);
	}
	
	public List<Oil> findOilByOATSName(String oatsName) {
		Session session = getSession();

		try {
			Query query = session.getNamedQuery("com.bfrc.dataaccess.model.oil.Oil.findOilByOATSName");
			query.setString(0, oatsName);

			@SuppressWarnings("unchecked")
			List<Oil> results = (List<Oil>) query.list();

			return results;
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public Oil findAdditionalOilByType(Long oilTypeId) {		
		Session session = getSession();
		
		Oil additionalOil = null;
		
		try {
			additionalOil = (Oil) session.createCriteria(Oil.class).add(Restrictions.eq("oilTypeId", oilTypeId)).add(Restrictions.eq("quartsAreConsideredAdditional", true)).uniqueResult();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		return additionalOil;
	}
	
	public Oil findHighMileageOilByViscosity(String viscosity) {		
		Session session = getSession();

		try {
			Query query = session.getNamedQuery("com.bfrc.dataaccess.model.oil.Oil.findHighMileageOilByViscosity");
			query.setString(0, viscosity);

			@SuppressWarnings("unchecked")
			Oil result = (Oil) query.uniqueResult();

			return result;
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public OilChange findOilChangeByOilType(Long oilTypeId) {		
		Session session = getSession();
		
		OilChange oilChange = null;
		
		try {
			oilChange = (OilChange) session.createCriteria(OilChange.class).add(Restrictions.eq("oilTypeId", oilTypeId)).uniqueResult();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		return oilChange;
	}
		
	public OilFilter getOilFilter() {	
		Session session = getSession();
		
		OilFilter oilFilter = null;
		
		try {
			oilFilter =  (OilFilter) session.createCriteria(OilFilter.class).uniqueResult();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		return oilFilter;
	}

	public void saveOatsVehicleYearCache(OatsVehicleYearCache oatsVehicleYearCache) {
		 getHibernateTemplate().save(oatsVehicleYearCache);		
	}
	
	public void saveOatsVehicleYearToMake(OatsVehicleYearToMake oatsVehicleYearToMake) {
		 getHibernateTemplate().save(oatsVehicleYearToMake);
	}

	public void saveOatsVehicleMakeCache(OatsVehicleMakeCache oatsVehicleMakeCache) {
		 getHibernateTemplate().save(oatsVehicleMakeCache);
	}

	public void saveOatsVehicleCache(OatsVehicleCache oatsVehicleCache) {
		getHibernateTemplate().save(oatsVehicleCache);
	}
	
	public void saveOatsOilRecommendationCache(OatsOilRecommendationCache oatsOilRecommendationCache) {
		getHibernateTemplate().save(oatsOilRecommendationCache);
	}
}
