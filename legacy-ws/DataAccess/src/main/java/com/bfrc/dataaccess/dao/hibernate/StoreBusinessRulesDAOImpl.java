package com.bfrc.dataaccess.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.store.StoreBusinessRulesDAO;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.store.StoreFlags;
import com.bfrc.dataaccess.model.store.StoreHierarchy;
import com.bfrc.dataaccess.model.store.StoreHolidayHour;
import com.bfrc.dataaccess.model.store.StoreHour;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminAnnouncement;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOfferTemplate;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOfferTemplateImages;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotion;
import com.bfrc.framework.util.Util;

/**
 * @author smoorthy
 *
 */
public class StoreBusinessRulesDAOImpl extends HibernateDaoSupport
		implements StoreBusinessRulesDAO {

	@SuppressWarnings("deprecation")
	public StoreHierarchy getHierarchyByStoreNumber(Long storeNumber) {
		Session s = getSession();
		try{
			String sql = "select distinct d.DISTRICT_ID as districtId, d.DISTRICT_NAME as districtName, r.REGION_ID as regionId, " + 
				"r.REGION_DESCRIPTION as regionName, z.ZONE_NAME as divisionId, z.ZONE_DESCRIPTION as divisionName " +
				"from STORE s, HR_DISTRICTS d, REGION_LIST r, ZONE_LIST z " +
				"WHERE s.DISTRICT_ID = d.DISTRICT_ID AND " +
				"d.REGION_ID = r.REGION_ID AND " +
				"r.ZONE_ID = z.ZONE_ID AND " +
				"s.STORE_NUMBER = "+storeNumber;
			
			List l = s.createSQLQuery(sql)
					.addScalar("districtId", Hibernate.STRING)
					.addScalar("districtName", Hibernate.STRING)
					.addScalar("regionId", Hibernate.STRING)
					.addScalar("regionName", Hibernate.STRING)
					.addScalar("divisionId", Hibernate.STRING)
					.addScalar("divisionName", Hibernate.STRING).list();
			
			StoreHierarchy sh = null;
			if (l != null && !l.isEmpty()) {
				sh = new StoreHierarchy();
				Object[] objects = (Object[]) l.get(0);
				
				sh.setDistrictId((String) objects[0]);
				sh.setDistrictName((String) objects[1]);
				sh.setRegionId((String) objects[2]);
				sh.setRegionName((String) objects[3]);
				sh.setDivisionId((String) objects[4]);
				sh.setDivisionName((String) objects[5]);
			}
			return sh;
		}finally {
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public String checkStoreReasonCode(Long storeNumber) {
		Session s = getSession();
		String sql = "select r.REASON_DESC as reason from store s join store_tempclose_reasons r on s.reason_id = r.reason_id where s.store_number = ?";
		String reason = null;
		try {
			List<String> l = s.createSQLQuery(sql).addScalar("reason", Hibernate.STRING).setLong(0, storeNumber).list();
		
			if(l!=null&&l.size()>0){
				reason = l.get(0);
			}			
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return reason;
	}
	
	@SuppressWarnings("unchecked")
	public List<Store> findStoreByStoreType(List<String> storeTypes){
		List<Store> stores = null;
		
		if (storeTypes == null || storeTypes.size() == 0)
			return null;
		
		StringBuffer storeTypesString = new StringBuffer();
		boolean firstTime = true;
		for (String storeType : storeTypes) {
			if (firstTime) {
				storeTypesString.append("(" + "'" +storeType+ "'");
				firstTime = false;
			} else
				storeTypesString.append(", " + "'" +storeType+ "'");
		}
		storeTypesString.append(")");
		
		Session s = getSession();
		try {
			String sql = "select s.store_name as storeName, s.store_number as storeNumber, s.address as storeAddress, s.store_type as storeStoreType, s.city as storeCity, "
					+ "s.state as storeState, s.zip as storeZip, s.phone as storePhone, s.fax as storeFax, s.manager_name as storeManagerName, s.manager_email_address as storeMEA, "
					+ "s.active_flag as storeActiveFlag, s.geo_match as storeGeoMatch, s.latitude as storeLatitude, s.longitude as storeLongitude, "
					+ "s.postal_status_code as storePostalStatusCode, s.online_appointment_active_flag as storeOAAF, s.tire_pricing_active_flag as storeTPAF, "
					+ "s.tracking_phone as storeTrackingPhone, s.local_page_url as localPageURL, sr.REASON_DESC as reasonDesc, d.district_id as areaId, d.district_name as areaName, "
					+ "r.region_id as regionId, r.region_description as regionName, z.zone_name as divisionId, z.zone_description as divisionName "
					+ "from STORE s, HR_DISTRICTS d, REGION_LIST r, ZONE_LIST z, STORE_TEMPCLOSE_REASONS sr where "
					+ "d.district_id(+) = s.district_id AND r.region_id(+) = d.region_id AND z.zone_id(+) = r.zone_id AND sr.REASON_ID(+) = s.REASON_ID and s.store_type in"
					+ storeTypesString.toString();

			List<Object[]> l = s.createSQLQuery(sql.toUpperCase()).addScalar("storeName", Hibernate.STRING).addScalar("storeNumber", Hibernate.LONG)
					.addScalar("storeAddress", Hibernate.STRING).addScalar("storeStoreType", Hibernate.STRING)
					.addScalar("storeCity", Hibernate.STRING).addScalar("storeState", Hibernate.STRING).addScalar("storeZip", Hibernate.STRING)
					.addScalar("storePhone", Hibernate.STRING).addScalar("storeFax", Hibernate.STRING).addScalar("storeManagerName", Hibernate.STRING)
					.addScalar("storeMEA", Hibernate.STRING).addScalar("storeActiveFlag", Hibernate.BIG_DECIMAL).addScalar("storeGeoMatch", Hibernate.STRING)
					.addScalar("storeLatitude", Hibernate.FLOAT).addScalar("storeLongitude", Hibernate.FLOAT)
					.addScalar("storePostalStatusCode", Hibernate.STRING).addScalar("storeOAAF", Hibernate.BIG_DECIMAL)
					.addScalar("storeTPAF", Hibernate.BIG_DECIMAL).addScalar("storeTrackingPhone", Hibernate.STRING).addScalar("localPageURL", Hibernate.STRING)
					.addScalar("reasonDesc", Hibernate.STRING).addScalar("areaId", Hibernate.STRING).addScalar("areaName", Hibernate.STRING).addScalar("regionId", Hibernate.STRING)
					.addScalar("regionName", Hibernate.STRING).addScalar("divisionId", Hibernate.STRING).addScalar("divisionName", Hibernate.STRING)
					.list();

			if(l!=null && l.size()>0){
				
				stores = new ArrayList<Store>();
				for(Object[] objects : l){
				    int i=0;
					Store store = new Store();
					store.setStoreName((String) objects[i]); i++;
					store.setStoreNumber(((Long) objects[i]).longValue()); i++;
					store.setAddress((String) objects[i]); i++;
					store.setStoreType((String) objects[i]); i++;
					store.setCity((String) objects[i]); i++;
					store.setState((String) objects[i]);i++;
					store.setZip((String) objects[i]);i++;
					store.setPhone((String) objects[i]);i++;
					store.setFax((String) objects[i]);i++;
					store.setManagerName((String) objects[i]);i++;
					store.setManagerEmail((String) objects[i]);i++;
					store.setActiveFlag((BigDecimal) objects[i]);i++;
					store.setGeoMatch((String) objects[i]);i++;
					store.setLatitude((Float) objects[i]);i++;
					store.setLongitude((Float) objects[i]);i++;
					store.setPostalStatusCode((String) objects[i]);i++;
					store.setOnlineAppointmentActiveFlag((BigDecimal) objects[i]);i++;
					store.setTirePricingActiveFlag((BigDecimal) objects[i]);i++;
					store.setTrackingPhone((String) objects[i]);i++;
					store.setLocalPageURL((String) objects[i]);i++;
					store.setReasonDesc((String) objects[i]);i++;
					store.setDistrictId((String) objects[i]);store.setAreaId((String) objects[i]);i++;
					store.setDistrictName((String) objects[i]);store.setAreaName((String) objects[i]);i++;
					store.setRegionId((String) objects[i]);i++;
					store.setRegionName((String) objects[i]);i++;
					store.setDivisionId((String) objects[i]);i++;
					store.setDivisionName((String) objects[i]);i++;
	
					stores.add(store);
				}
				return stores;
			}

		} catch (Exception e) {
			Util.debug(" there was an ERROR");
			e.printStackTrace();
		} finally {
			// s.close();
			this.releaseSession(s);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<StoreHour> findStoreHoursByStoreType(List<String> storeType){
		List<StoreHour> storehours = null;
		Session s = getSession();
		try
		{
			Query query = s.getNamedQuery("com.bfrc.dataaccess.model.store.StoreHour.findStoreHourByStoreType");
			query.setCacheable(false);
			query.setParameterList("storeType", storeType);
			storehours = (ArrayList<StoreHour>) query.list();
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return storehours;
	}

	@SuppressWarnings("unchecked")
	public List<StoreHolidayHour> findStoreHolidayHoursByStoreType(
			List<String> storeType, List<Long> holidayIds) {
		List<StoreHolidayHour> storeHolidayHours = null;
		Session s = getSession();
		try
		{
			Query query = s.getNamedQuery("com.bfrc.dataaccess.model.store.StoreHolidayHour.findStoreHolidayHoursByStoreType");
			query.setCacheable(false);
			query.setParameterList("storeType", storeType);
			query.setParameterList("holidayId", holidayIds);
			storeHolidayHours = (ArrayList<StoreHolidayHour>) query.list();
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return storeHolidayHours;
	}

	public StoreAdminOfferTemplate findOfferTemplateByIdAndBrand(Object id,
			String brand) {
		if (id == null||brand==null)
			return null;
		
		String sqlBrand ="Fcac";
		if(brand.equalsIgnoreCase("TP") || brand.equalsIgnoreCase("TPL"))
			sqlBrand="Tp";
		else if
		(brand.equalsIgnoreCase("ET"))
			sqlBrand = "Et";
		
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"select s.templateId,s.friendlyId, s.categoryId,s.image"+sqlBrand+",s.name,s.price,s.percentOff,s.startDate,s.endDate,s.createdBy,s.createdDate,s.modifiedBy,s.modifiedDate,s.priorityOrder,s.bannerImage"+sqlBrand+",s.subtitle,s.shortDescription,s.priceHtml,s.priceDisclaimer,s.buttonText from StoreAdminOfferTemplate s where s.templateId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			Object[] object = (Object[])l.get(0);
			StoreAdminOfferTemplate saot = new StoreAdminOfferTemplate();
			saot.setTemplateId((Long)object[0]);
			saot.setFriendlyId((String)object[1]);
			saot.setCategoryId((Long)object[2]);
			if(brand.equalsIgnoreCase("fcac"))
				saot.setImageFcac((byte[])object[3]);
			else if(brand.equalsIgnoreCase("tp") || brand.equalsIgnoreCase("tpl"))
				saot.setImageTp((byte[])object[3]);
			else if(brand.equalsIgnoreCase("et"))
				saot.setImageEt((byte[])object[3]);
			saot.setName((String)object[4]);
			saot.setPrice((BigDecimal)object[5]);
			saot.setPercentOff((BigDecimal)object[6]);
			saot.setStartDate((Date)object[7]);
			saot.setEndDate((Date)object[8]);
			saot.setCreatedBy((String)object[9]);
			saot.setCreatedDate((Date)object[10]);
			saot.setModifiedBy((String)object[11]);
			saot.setModifiedDate((Date)object[12]);
			saot.setPriorityOrder((Long)object[13]);
			if(object[14]!=null){
			if(brand.equalsIgnoreCase("fcac"))
				saot.setBannerImageFcac((byte[])object[14]);
			else if(brand.equalsIgnoreCase("tp") || brand.equalsIgnoreCase("tpl"))
				saot.setBannerImageTp((byte[])object[14]);
			else if(brand.equalsIgnoreCase("et"))
				saot.setBannerImageEt((byte[])object[14]);
			}
			saot.setSubtitle((String)object[15]);
			saot.setShortDescription((String)object[16]);
			saot.setPriceHtml((String)object[17]);
			saot.setPriceDisclaimer((String)object[18]);
			saot.setButtonText((String)object[19]);
			return saot;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public StoreAdminOfferTemplateImages findOfferTemplateImagesByIdAndBrand(
			Object id, String brand) {
		if (id == null||brand==null)
			return null;
		
		String sqlBrand = brand.toUpperCase();
				
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"select s.imageId, s.templateId, s.siteName, s.couponImage, s.bannerImage from StoreAdminOfferTemplateImages s where s.templateId=" + lid +" and s.siteName='"+ sqlBrand +"'");
			if (l == null || l.size() == 0)
				return null;
			StoreAdminOfferTemplateImages saoti = new StoreAdminOfferTemplateImages();
			if (l.size() == 1) {
				Object[] object = (Object[])l.get(0);
				saoti.setImageId((Long)object[0]);
				saoti.setTemplateId((Long)object[1]);
				saoti.setSiteName((String)object[2]);
				saoti.setCouponImage((byte[])object[3]);
				saoti.setBannerImage((byte[])object[4]);
			} else {
				for (int idx=0; idx < 2; idx++) {
					Object[] object = (Object[])l.get(idx);
					if (((byte[])object[3]) != null) {
						saoti.setImageId((Long)object[0]);
						saoti.setTemplateId((Long)object[1]);
						saoti.setSiteName((String)object[2]);
						saoti.setCouponImage((byte[])object[3]);
					} else if (((byte[])object[4]) != null) {
						saoti.setBannerImage((byte[])object[4]);
					}
				}
			}
			return saoti;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<StoreAdminPromotion> findNationalCurrentPromotionsByBrand(
			List<String> brand) {
		List<StoreAdminPromotion> storeAdminPromotions = null;
		Session s = getSession();
		try
		{
			Query query = s.getNamedQuery("com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotion.findNationalCurrentPromotionsByBrand");
			query.setCacheable(false);
			query.setParameterList("brand", brand);
			storeAdminPromotions = (ArrayList<StoreAdminPromotion>) query.list();
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return storeAdminPromotions;
	}
	
	@SuppressWarnings("unchecked")
	public List<StoreAdminAnnouncement> findNationalCurrentApprovedAnnouncements(List<String> brand) {
		List<StoreAdminAnnouncement> storeAdminAnnouncement = null;
		Session s = getSession();
		try
		{
			Query query = s.getNamedQuery("com.bfrc.dataaccess.model.storeadmin.StoreAdminAnnouncement.findNationalCurrentApprovedAnnouncements");
			query.setCacheable(false);
			query.setParameterList("brand", brand);
			storeAdminAnnouncement = (ArrayList<StoreAdminAnnouncement>) query.list();
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return storeAdminAnnouncement;
	}

	@SuppressWarnings("unchecked")
	public List<Store> findStoreByStateCityType(String state, String city,
			List<String> storeType) {
		List<Store> stores = null;
		Session s = getSession();
		try
		{
			Query query = s.getNamedQuery("com.bfrc.dataaccess.model.store.Store.findStoresByStateCityAndStoreTypes");
			query.setCacheable(false);
			query.setParameter("state", state);
			query.setParameter("city", city);
			query.setParameterList("storeType", storeType);
			stores = (ArrayList<Store>) query.list();
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return stores;
	}
	
	@SuppressWarnings("unchecked")
	public List<StoreFlags> findStoreFlagsByType(String storeType) {
		List<StoreFlags> storeFlagsList = null;
		Session s = getSession();
		try
		{
			String sql = "select s.STORE_NUMBER,s.ACTIVE_FLAG,s.ONLINE_APPOINTMENT_ACTIVE_FLAG,s.TIRE_PRICING_ACTIVE_FLAG,s.ECOMM_ACTIVE_FLAG from RTMS_WEBDB.STORE s where s.STORE_TYPE = :storeType";
			Query query = s.createSQLQuery(sql);
			query.setCacheable(false);
			query.setParameter("storeType", storeType);
			List stores = query.list();
			if (stores != null && !stores.isEmpty()) {
				storeFlagsList = new ArrayList<StoreFlags>();
				StoreFlags storeFlags = null;
				for (Iterator iterator = stores.iterator(); iterator.hasNext();) {
					Object[] objects = (Object[]) iterator.next();
					storeFlags = new StoreFlags();
					BigDecimal storeNumber = (BigDecimal) objects[0];
					storeFlags.setStoreNumber(storeNumber.longValue());
					storeFlags.setActiveFlag((BigDecimal) objects[1]);
					storeFlags.setOnlineAppointmentActiveFlag((BigDecimal) objects[2]);
					storeFlags.setTirePricingActiveFlag((BigDecimal) objects[3]);
					storeFlags.seteCommActiveFlag((BigDecimal) objects[4]);
					storeFlagsList.add(storeFlags);
				}
			}
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return storeFlagsList;
	}
	
	@SuppressWarnings("unchecked")
	public StoreFlags findStoreFlagsByNumber(String storeNumber){
		StoreFlags storeFlags = null;
		Session s = getSession();				
		try {
			String sql = "select s.STORE_NUMBER,s.ACTIVE_FLAG,s.ONLINE_APPOINTMENT_ACTIVE_FLAG,s.TIRE_PRICING_ACTIVE_FLAG,s.ECOMM_ACTIVE_FLAG from RTMS_WEBDB.STORE s where s.STORE_NUMBER = :storeNumber";
			Query query = s.createSQLQuery(sql);
			query.setParameter("storeNumber", storeNumber);
			query.setCacheable(false);
			List stores =  query.list();
				if (stores != null && !stores.isEmpty()) {
					Object[] objects = (Object[]) stores.get(0);
					storeFlags = new StoreFlags();
					BigDecimal storeNumber1 = (BigDecimal) objects[0];
					storeFlags.setStoreNumber(storeNumber1.longValue());
					storeFlags.setActiveFlag((BigDecimal) objects[1]);
					storeFlags.setOnlineAppointmentActiveFlag((BigDecimal) objects[2]);
					storeFlags.setTirePricingActiveFlag((BigDecimal) objects[3]);
					storeFlags.seteCommActiveFlag((BigDecimal) objects[4]);
				}
		} finally {		
			if (s != null) {
				try {
					this.releaseSession(s);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}		
		return storeFlags;					
	}	
}
