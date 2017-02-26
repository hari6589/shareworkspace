package com.bfrc.framework.dao.hibernate3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.StoreAdminDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.store.StoreBanner;
import com.bfrc.pojo.storeadmin.StoreAdminAnnouncement;
import com.bfrc.pojo.storeadmin.StoreAdminAnnouncementStoreJoin;
import com.bfrc.pojo.storeadmin.StoreAdminAnnouncementStoreJoinId;
import com.bfrc.pojo.storeadmin.StoreAdminLibraryImage;
import com.bfrc.pojo.storeadmin.StoreAdminOffer;
import com.bfrc.pojo.storeadmin.StoreAdminOfferCategory;
import com.bfrc.pojo.storeadmin.StoreAdminOfferTemplate;
import com.bfrc.pojo.storeadmin.StoreAdminOfferTemplateImages;
import com.bfrc.pojo.storeadmin.StoreAdminPromotion;
import com.bfrc.pojo.storeadmin.StoreAdminPromotionStoreJoin;
import com.bfrc.pojo.storeadmin.StoreAdminPromotionStoreJoinId;
import com.bfrc.pojo.storeadmin.StoreAdminStoreImage;

public class StoreAdminDAOImpl extends HibernateDAOImpl implements
		StoreAdminDAO {
	private PlatformTransactionManager txManager;

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public StoreAdminOfferTemplate findOfferTemplateById(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminOfferTemplate s where s.templateId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminOfferTemplate) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}
	
	public List getAllOfferTemplatesImagesById(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminOfferTemplateImages i where i.templateId=" + lid +" order by i.siteName");
			if (l == null || l.size() == 0)
				return null;
			return l;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public StoreAdminOfferTemplateImages getBannerTemplateImages(Long id, Object siteName, List storeAdminOfferTmplImages) {
		StoreAdminOfferTemplateImages tmplImages = null;
		
		if (id == null || siteName == null)
			return null;
		
		if (storeAdminOfferTmplImages != null) {
			for (int idx = 0; idx < storeAdminOfferTmplImages.size(); idx++) {
				StoreAdminOfferTemplateImages imgs = (StoreAdminOfferTemplateImages)storeAdminOfferTmplImages.get(idx);
				if (id.compareTo(imgs.getTemplateId()) == 0 && siteName.toString().equalsIgnoreCase(imgs.getSiteName()) && imgs.getBannerImage() != null) {
					tmplImages = imgs;
					break;
				}
			}
		}
		
		return tmplImages;
	}
	
	public StoreAdminOfferTemplateImages getCouponTemplateImages(Long id, Object siteName, List storeAdminOfferTmplImages) {
		StoreAdminOfferTemplateImages tmplImages = null;
		
		if (id == null || siteName == null)
			return null;
		
		if (storeAdminOfferTmplImages != null) {
			for (int idx = 0; idx < storeAdminOfferTmplImages.size(); idx++) {
				StoreAdminOfferTemplateImages imgs = (StoreAdminOfferTemplateImages)storeAdminOfferTmplImages.get(idx);
				if (id.compareTo(imgs.getTemplateId()) == 0 && siteName.toString().equalsIgnoreCase(imgs.getSiteName()) && imgs.getCouponImage() != null) {
					tmplImages = imgs;
					break;
				}
			}
		}
		
		return tmplImages;
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
	
	public StoreAdminOfferTemplateImages findOfferTemplateImagesByIdAndBrand(Object id,
			String brand) {
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

	public Long createOfferTemplate(StoreAdminOfferTemplate item) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setCreatedDate(new Date());
			item.setModifiedDate(new Date());
			Long rt = (Long) getHibernateTemplate().save(item);
			this.txManager.commit(status);
			return rt;
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PromotionDAO could not create offer template ");
			System.err.println();
		}
		return new Long(-1);
	}
	
	public Long createOfferTemplateImages(StoreAdminOfferTemplateImages item) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			Long rt = (Long) getHibernateTemplate().save(item);
			this.txManager.commit(status);
			return rt;
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PromotionDAO could not create offer template images");
			System.err.println();
		}
		return new Long(-1);
	}

	public void updateOfferTemplate(StoreAdminOfferTemplate item) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			ex.printStackTrace();
			this.txManager.rollback(status);
			System.err.print("PromotionDAO could not create offer template ");
			System.err.println();
		}
	}
	
	public void deleteOfferTemplate(StoreAdminOfferTemplate item) {
		deleteOfferTemplate(item, false);
	}

	public void deleteOfferTemplate(StoreAdminOfferTemplate item, boolean deleteImages) {
		deleteOffersByTemplate(item.getTemplateId());
		if (deleteImages) {
			deleteTemplateImagesByTemplateId(item.getTemplateId());
		}
		getHibernateTemplate().delete(item);
	}

	public void deleteOfferTemplateById(Object id) {
		StoreAdminOfferTemplate item = findOfferTemplateById(id);
		deleteOffersByTemplate(item.getTemplateId());
		getHibernateTemplate().delete(item);
	}

	public StoreAdminOffer findOfferById(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminOffer s where s.offerId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminOffer) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findOffersByPromotion(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminOffer s where s.promoId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			return l;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public Boolean checkOffersByTemplate(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"select count(s.offerId) from StoreAdminOffer s where s.templateId=" + lid);
			if (l == null || l.size() == 0)
				return false;
			if(((Long)l.get(0)).intValue()==0)
				return false;
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	public void deleteOffersByTemplate(Object id) {
		if (id == null)
			return;
		try {
			Long lid = Long.valueOf(id.toString());
			List<StoreAdminOffer> l = (List<StoreAdminOffer>)getHibernateTemplate().find(
					"from StoreAdminOffer s where s.templateId=" + lid);
			if (l == null || l.size() == 0)
				return;
			getHibernateTemplate().deleteAll(l);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void deleteTemplateImagesByTemplateId(Object id) {
		if (id == null)
			return;
		try {
			Long lid = Long.valueOf(id.toString());
			List<StoreAdminOfferTemplateImages> l = (List<StoreAdminOfferTemplateImages>)getHibernateTemplate().find(
					"from StoreAdminOfferTemplateImages s where s.templateId=" + lid);
			if (l == null || l.size() == 0)
				return;
			getHibernateTemplate().deleteAll(l);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void deleteTemplateImagesByImageId(Object id) {
		if (id == null)
			return;
		try {
			long lid = Long.parseLong(id.toString());
			List<StoreAdminOfferTemplateImages> l = (List<StoreAdminOfferTemplateImages>)getHibernateTemplate().find(
					"from StoreAdminOfferTemplateImages s where s.imageId=" + lid);
			if (l == null || l.size() == 0)
				return;
			getHibernateTemplate().deleteAll(l);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public Long createOffer(StoreAdminOffer item) {
		item.setCreatedDate(new Date());
		item.setModifiedDate(new Date());
		return (Long) getHibernateTemplate().save(item);
	}

	public void updateOffer(StoreAdminOffer item) {
		item.setModifiedDate(new Date());
		getHibernateTemplate().update(item);
	}

	public void deleteOffer(StoreAdminOffer item) {
		getHibernateTemplate().delete(item);
	}

	public void deleteOfferById(Object id) {
		StoreAdminOffer item = findOfferById(id);
		getHibernateTemplate().delete(item);
	}

	public StoreAdminPromotion findPromotionById(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminPromotion s where s.promotionId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminPromotion) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Long createPromotion(StoreAdminPromotion item) {

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setCreatedDate(new Date());
			item.setModifiedDate(new Date());
			Long rt = (Long) getHibernateTemplate().save(item);
			this.txManager.commit(status);
			return rt;
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("storeAdminDao could not create promotion ");
			System.err.println();
		}
		return new Long(-1);
	}

	public void updatePromotion(StoreAdminPromotion item) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			ex.printStackTrace();
			this.txManager.rollback(status);
			System.err.print("storeAdminDao could not update promotion ");
			System.err.println();
		}
	}

	public void deletePromotion(StoreAdminPromotion item) {
		getHibernateTemplate().delete(item);
	}

	public void deletePromotionById(Object id) {
		StoreAdminPromotion item = findPromotionById(id);
		getHibernateTemplate().delete(item);
	}

	public StoreAdminOfferCategory findOfferCategoryById(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminOfferCategory s where s.categoryId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminOfferCategory) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Long createOfferCategory(StoreAdminOfferCategory item) {
		item.setCreatedDate(new Date());
		item.setModifiedDate(new Date());
		return (Long) getHibernateTemplate().save(item);
	}

	public void updateOfferCategory(StoreAdminOfferCategory item) {
		item.setModifiedDate(new Date());
		getHibernateTemplate().update(item);
	}

	public void deleteOfferCategory(StoreAdminOfferCategory item) {
		getHibernateTemplate().delete(item);
	}

	public void deleteOfferCategoryById(Object id) {
		StoreAdminOfferCategory item = findOfferCategoryById(id);
		getHibernateTemplate().delete(item);
	}

	public StoreAdminPromotionStoreJoin findAdminPromotionStoreJoinById(Object promoId, Object storeNumber) {
		if (promoId == null || storeNumber == null)
			return null;
		try {
			Long lpromoId = Long.valueOf(promoId.toString());
			Integer lstoreNumber = Integer.valueOf(storeNumber.toString());
			StoreAdminPromotionStoreJoinId lid = new StoreAdminPromotionStoreJoinId(
					lpromoId, lstoreNumber);
			return findAdminPromotionStoreJoinById(lid);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public StoreAdminPromotionStoreJoin findAdminPromotionStoreJoinById(
			Object id) {
		if (id == null)
			return null;
		try {
			StoreAdminPromotionStoreJoinId lid = (StoreAdminPromotionStoreJoinId) id;
			List l = getHibernateTemplate().find(
					"from StoreAdminPromotionStoreJoin s where s.id.promoId="
							+ lid.getPromoId() + " and s.id.storeNumber="
							+ lid.getStoreNumber());
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminPromotionStoreJoin) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findAdminPromotionStoreJoinByPromotion(Object id) {
		if (id == null)
			return null;
		try {
			List l = getHibernateTemplate().find(
					"from StoreAdminPromotionStoreJoin s where s.id.promoId="
							+ id);
			if (l == null || l.size() == 0)
				return null;
			return l;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findAdminOfferStoreJoinByOffer(Object id) {
		if (id == null)
			return null;
		try {
			List l = getHibernateTemplate().find(
					"from StoreAdminOfferStoreJoin s where s.id.offerId=" + id);
			if (l == null || l.size() == 0)
				return null;
			return l;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public StoreAdminPromotionStoreJoinId createPromotionStoreJoin(
			StoreAdminPromotionStoreJoin item) {
		return (StoreAdminPromotionStoreJoinId) getHibernateTemplate().save(
				item);
	}

	public void updatePromotionStoreJoin(StoreAdminPromotionStoreJoin item) {
		getHibernateTemplate().update(item);
	}

	public void deletePromotionStoreJoin(StoreAdminPromotionStoreJoin item) {
		getHibernateTemplate().delete(item);
	}

	public void deletePromotionStoreJoinById(Object id) {
		StoreAdminPromotionStoreJoin item = new StoreAdminPromotionStoreJoin();
		item.setId((StoreAdminPromotionStoreJoinId) id);
		getHibernateTemplate().delete(item);
	}

	public void deletePromotionStoreJoinById(Object promoId, Object storeNumber) {
		if (promoId == null || storeNumber == null)
			return;
		try {
			Long lpromoId = Long.valueOf(promoId.toString());
			Integer lstoreNumber = Integer.valueOf(storeNumber.toString());
			StoreAdminPromotionStoreJoinId lid = new StoreAdminPromotionStoreJoinId(
					lpromoId, lstoreNumber);
			StoreAdminPromotionStoreJoin item = new StoreAdminPromotionStoreJoin();
			item.setId(lid);
			getHibernateTemplate().delete(item);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List getAllOfferCategories() {
		String hql = "from StoreAdminOfferCategory c order by c.name";
		List l = getHibernateTemplate().find(hql);
		return l;
	}
	
	//public List getAllOfferTemplatesImages() {
	//	String hql = "from StoreAdminOfferTemplateImages i order by i.templateId";
	//	List l = getHibernateTemplate().find(hql);
	//	return l;
	//}
	
	public List getAllOfferTemplatesImages() {
		List saotiList = new ArrayList();
		Session s = getSession();
		try {
			String sql = "select distinct ti.IMAGE_ID as imageId, ti.TEMPLATE_ID as templateId, ti.SITE_NAME as siteName, dbms_lob.getLength(ti.COUPON_IMAGE) as hasCouponImage, " +
					"dbms_lob.getLength(ti.BANNER_IMAGE) as hasBannerImage from STORE_ADMIN_OFFER_TMPL_IMAGES ti";
			List l = s.createSQLQuery(sql)
			.addScalar("imageId", Hibernate.LONG)
			.addScalar("templateId", Hibernate.LONG)
			.addScalar("siteName", Hibernate.STRING)
			.addScalar("hasCouponImage", Hibernate.INTEGER)
			.addScalar("hasBannerImage", Hibernate.INTEGER).list();
			
			for (int i = 0; i < l.size(); i++) {
				int x =0;
				Object[] objects = (Object[]) l.get(i);
				StoreAdminOfferTemplateImages saoti = new StoreAdminOfferTemplateImages();
				saoti.setImageId((Long) objects[x]);x++;
				saoti.setTemplateId((Long) objects[x]);x++;
				saoti.setSiteName((String) objects[x]);x++;
				saoti.setHasCouponImage((objects[x]!=null&&((Integer) objects[x])>0)?true : false);x++;
				saoti.setHasBannerImage((objects[x]!=null&&((Integer) objects[x])>0)?true : false);x++;
				
				saotiList.add(saoti);
			}
			return saotiList;
		} catch(Exception e){
        	e.printStackTrace();
        }finally {
        	//s.close();
			this.releaseSession(s);
		}
    	return saotiList;
	}

	public List getAllOffers() {
		String hql = "from StoreAdminOffer o order by o.createdDate";
		List l = getHibernateTemplate().find(hql);
		return l;
	}
	
	public List getAllWebsites() {
		String hql = "select distinct m.siteName from BfrcStoreMap m order by m.siteName asc";
		List l = getHibernateTemplate().find(hql);
		return l;
	}

//	public List getAllOfferTemplates() {
//		String hql = "from StoreAdminOfferTemplate t order by t.createdDate";
//		List l = getHibernateTemplate().find(hql);
//		return l;
//	}
//	
	public List getAllOfferTemplates() {

		List saotList = new ArrayList();
		  Session s = getSession();
	        try{
			String sql = "select distinct t.TEMPLATE_ID as templateId,t.NAME as name, t.FRIENDLY_ID as friendlyId, t.CATEGORY_ID as categoryId, t.PRICE as price, " +
					"t.PERCENT_OFF as percentOff, t.START_DATE as startDate, t.END_DATE as endDate, t.CREATED_BY as createdBy, t.CREATED_DATE as createdDate, "+
					"t.MODIFIED_BY as modifiedBy, t.MODIFIED_DATE as modifiedDate, t.PRIORITY_ORDER as priorityOrder, t.SUBTITLE as subtitle, t.SHORT_DESCRIPTION as shortDescription, "+
					"t.PRICE_DISCLAIMER as priceDisclaimer, t.PRICE_HTML as priceHtml, t.BUTTON_TEXT as buttonText, "+
					"dbms_lob.getLength(t.IMAGE_FCAC) as hasImageFcac, dbms_lob.getLength(t.IMAGE_TP) as hasImageTp,  dbms_lob.getLength(t.IMAGE_ET) as hasImageEt "+
					"from STORE_ADMIN_OFFER_TEMPLATE t order by t.CREATED_DATE asc";
		
			List l = s.createSQLQuery(sql)
					.addScalar("templateId", Hibernate.LONG)
					.addScalar("name", Hibernate.STRING)
					.addScalar("friendlyId", Hibernate.STRING)
					.addScalar("categoryId", Hibernate.LONG)
					.addScalar("price", Hibernate.BIG_DECIMAL)
					.addScalar("percentOff", Hibernate.BIG_DECIMAL)
					.addScalar("startDate", Hibernate.DATE)
					.addScalar("endDate", Hibernate.DATE)
					.addScalar("createdBy", Hibernate.STRING)
					.addScalar("createdDate", Hibernate.DATE)
					.addScalar("modifiedBy", Hibernate.STRING)
					.addScalar("modifiedDate", Hibernate.DATE)
					.addScalar("priorityOrder", Hibernate.LONG)
					.addScalar("subtitle", Hibernate.STRING)
					.addScalar("shortDescription", Hibernate.STRING)
					.addScalar("priceDisclaimer", Hibernate.STRING)
					.addScalar("priceHtml", Hibernate.STRING)
					.addScalar("buttonText", Hibernate.STRING)
					.addScalar("hasImageFcac", Hibernate.INTEGER)
					.addScalar("hasImageTp", Hibernate.INTEGER)
					.addScalar("hasImageEt", Hibernate.INTEGER).list();
			for (int i = 0; i < l.size(); i++) {
				int x =0;
				Object[] objects = (Object[]) l.get(i);
				StoreAdminOfferTemplate saot = new StoreAdminOfferTemplate();
				saot.setTemplateId((Long) objects[x]);x++;
				saot.setName(((String) objects[x]));x++;
				saot.setFriendlyId(((String) objects[x]));x++;
				saot.setCategoryId(((Long) objects[x]));x++;
				saot.setPrice(((BigDecimal) objects[x]));x++;
				saot.setPercentOff(((BigDecimal) objects[x]));x++;
				saot.setStartDate(((Date) objects[x]));x++;
				saot.setEndDate(((Date) objects[x]));x++;
				saot.setCreatedBy(((String) objects[x]));x++;
				saot.setCreatedDate(((Date) objects[x]));x++;
				saot.setModifiedBy(((String) objects[x]));x++;
				saot.setModifiedDate(((Date) objects[x]));x++;
				saot.setPriorityOrder(((Long) objects[x]));x++;
				saot.setSubtitle(((String) objects[x]));x++;
				saot.setShortDescription(((String) objects[x]));x++;
				saot.setPriceDisclaimer(((String) objects[x]));x++;
				saot.setPriceHtml(((String) objects[x]));x++;
				saot.setButtonText(((String) objects[x]));x++;
				saot.setHasImageFcac((objects[x]!=null&&((Integer) objects[x])>0)?true : false);x++;
				saot.setHasImageTp((objects[x]!=null&&((Integer) objects[x])>0)?true : false);x++;
				saot.setHasImageEt((objects[x]!=null&&((Integer) objects[x])>0)?true : false);x++;
				saotList.add(saot);
			}
			return saotList;
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally {
	        	//s.close();
				this.releaseSession(s);
			}
        	return saotList;
		
	}
	
	public List getAllActiveOfferTemplates(Long categoryId) {
		Session s= getSession();
		try{
		if(categoryId==null)
			return null;
		Date today = new Date();
		String sql = "select t.* from STORE_ADMIN_OFFER_TEMPLATE t where t.CATEGORY_ID = "+categoryId+" and ((t.START_DATE <= :today and t.END_DATE >= :today) or (t.START_DATE <= :today and t.END_DATE is null)) order by t.NAME ";
		List l = s.createSQLQuery(sql).addEntity(StoreAdminOfferTemplate.class).setDate("today", today).list();

		return l;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	public Map getMappedOfferCategories() {
		List l = getAllOfferCategories();
		Map m = new LinkedHashMap();
		for (int i = 0; l != null && i < l.size(); i++) {
			StoreAdminOfferCategory item = (StoreAdminOfferCategory) l.get(i);
			m.put(String.valueOf(item.getCategoryId()), item);
		}
		return m;
	}

	public Map getMappedTemplates() {
		List l = getAllOfferTemplates();
		Map m = new LinkedHashMap();
		for (int i = 0; l != null && i < l.size(); i++) {
			StoreAdminOfferTemplate item = (StoreAdminOfferTemplate) l.get(i);
			m.put(String.valueOf(item.getTemplateId()), item);
		}
		return m;
	}

	public Map getMappedCategoriesWithTemplateId() {
		List l = getAllOfferTemplates();
		Map cm = getMappedOfferCategories();
		Map m = new LinkedHashMap();
		for (int i = 0; l != null && i < l.size(); i++) {
			StoreAdminOfferTemplate item = (StoreAdminOfferTemplate) l.get(i);
			if (!m.containsKey(String.valueOf(item.getTemplateId())))
				m.put(String.valueOf(item.getTemplateId()),
						cm.get(String.valueOf(item.getCategoryId())));
		}
		return m;
	}

	public List findNationalActionItems() {
		List l1 = getHibernateTemplate().findByNamedQuery(
				"findNationalActionItems");
		List l2 = getHibernateTemplate().findByNamedQuery(
				"findNationalActionAnnouncementItems");
		List l3 = new ArrayList();
		if (l1 == null) {
			if (l2 != null)
				l3 = l2;
		} else if (l2 == null)
			l3 = l1;
		else { // merge the lists
			int i = 0, j = 0;
			StoreAdminPromotion p = null;
			StoreAdminAnnouncement a = null;
			while (i < l1.size() && j < l2.size()) {
				p = (StoreAdminPromotion) l1.get(i);
				a = (StoreAdminAnnouncement) l2.get(j);
				if (p.getCreatedDate().before(a.getCreatedDate())) {
					l3.add((Object) a);
					j++;
				} else {
					l3.add((Object) p);
					i++;
				}
			}
			if (i < l1.size()) {
				for (int k = i; k < l1.size(); k++) {
					l3.add(l1.get(k));
				}
			} else if (j < l2.size()) {
				for (int k = j; k < l2.size(); k++) {
					l3.add(l2.get(k));
				}
			}
		}
		return l3;
	}

	public List findZoneManagerActionItemsByEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		List l1 = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerActionItemsByEmail", names, values);
		List l2 = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerActionAnnouncementsByEmail", names, values);
		List l3 = new ArrayList();
		if (l1 == null) {
			if (l2 != null)
				l3 = l2;
		} else if (l2 == null)
			l3 = l1;
		else { // merge the lists
			int i = 0, j = 0;
			StoreAdminPromotion p = null;
			StoreAdminAnnouncement a = null;
			while (i < l1.size() && j < l2.size()) {
				p = (StoreAdminPromotion) l1.get(i);
				a = (StoreAdminAnnouncement) l2.get(j);
				if (p.getCreatedDate().before(a.getCreatedDate())) {
					l3.add((Object) a);
					j++;
				} else {
					l3.add((Object) p);
					i++;
				}
			}
			if (i < l1.size()) {
				for (int k = i; k < l1.size(); k++) {
					l3.add(l1.get(k));
				}
			} else if (j < l2.size()) {
				for (int k = j; k < l2.size(); k++) {
					l3.add(l2.get(k));
				}
			}
		}
		return l3;
	}

	public List findZoneManagerActionItemsByZoneId(Object id) {
		String[] names = { "zoneId" };
		Object[] values = new Object[] { Integer.valueOf(id.toString()) };
		List l1 = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerActionItemsByZoneId", names, values);
		List l2 = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerActionAnnouncementsByZoneId", names, values);
		List l3 = new ArrayList();
		if (l1 == null) {
			if (l2 != null)
				l3 = l2;
		} else if (l2 == null)
			l3 = l1;
		else { // merge the lists
			int i = 0, j = 0;
			StoreAdminPromotion p = null;
			StoreAdminAnnouncement a = null;
			while (i < l1.size() && j < l2.size()) {
				p = (StoreAdminPromotion) l1.get(i);
				a = (StoreAdminAnnouncement) l2.get(j);
				if (p.getCreatedDate().before(a.getCreatedDate())) {
					l3.add((Object) a);
					j++;
				} else {
					l3.add((Object) p);
					i++;
				}
			}
			if (i < l1.size()) {
				for (int k = i; k < l1.size(); k++) {
					l3.add(l1.get(k));
				}
			} else if (j < l2.size()) {
				for (int k = j; k < l2.size(); k++) {
					l3.add(l2.get(k));
				}
			}
		}
		return l3;
	}

	public List findZoneManagerCurrentPromotionsByEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerCurrentPromotionsByEmail", names, values);
	}

	public List findZoneManagerCurrentPromotionsByZoneId(Object id) {
		String[] names = { "zoneId" };
		Object[] values = new Object[] { Integer.valueOf(id.toString()) };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerCurrentPromotionsByZoneId", names, values);

	}

	public List findDistrictManagerCurrentPromotionsByEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerCurrentPromotionsByEmail", names, values);
	}

	public List findDistrictManagerCurrentPromotionsByDistrictId(
			String districtId) {
		String[] names = { "districtId" };
		Object[] values = new Object[] { districtId };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerCurrentPromotionsByDistrictId", names,
				values);
	}

	public List findNationalCurrentPromotions() {
		return getHibernateTemplate().findByNamedQuery(
				"findNationalCurrentPromotions");
	}

	public List findNationalCurrentPromotions(String brand){
		if(brand == null)
			return null;
		String[] names = { "brand" };
		Object[] values = new Object[] { brand.toUpperCase() };
	return getHibernateTemplate().findByNamedQueryAndNamedParam(
			"findNationalCurrentPromotionsByBrand",names,values);
	}
	public StoreAdminAnnouncement findAnnouncementById(Object id) {
		if (id == null)
			return null;
		try {
			Long lid = Long.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminAnnouncement a where a.announcementId="
							+ lid);
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminAnnouncement) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findAdminAnnouncementStoreJoinById(Object announcementId,
			Object storeNumber) {
		if (announcementId == null || storeNumber == null)
			return null;
		try {
			Long lannouncementId = Long.valueOf(announcementId.toString());
			Integer lstoreNumber = Integer.valueOf(storeNumber.toString());
			StoreAdminAnnouncementStoreJoinId lid = new StoreAdminAnnouncementStoreJoinId(
					lannouncementId, lstoreNumber);
			return findAdminAnnouncementStoreJoinById(lid);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findAdminAnnouncementStoreJoinById(Object id) {
		if (id == null)
			return null;
		try {
			StoreAdminAnnouncementStoreJoinId lid = (StoreAdminAnnouncementStoreJoinId) id;
			List l = getHibernateTemplate().find(
					"from StoreAdminAnnouncementStoreJoin s where s.id.announcementId="
							+ lid.getAnnouncementId()
							+ " and s.id.storeNumber=" + lid.getStoreNumber());
			if (l == null || l.size() == 0)
				return null;
			return l;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findAdminAnnouncementStoreJoinByAnnouncement(Object id) {
		if (id == null)
			return null;
		try {
			List l = getHibernateTemplate().find(
					"from StoreAdminAnnouncementStoreJoin s where s.id.announcementId="
							+ id);
			if (l == null || l.size() == 0)
				return null;
			return l;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findZoneManagerCurrentAnnouncementsByEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerCurrentAnnouncementsByEmail", names, values);
	}

	public List findZoneManagerCurrentAnnouncementsByZoneId(Object id) {
		String[] names = { "zoneId" };
		Object[] values = new Object[] { Integer.valueOf(id.toString()) };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerCurrentAnnouncementsByZoneId", names, values);

	}

	public List findDistrictManagerCurrentAnnouncementsByEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		return getHibernateTemplate()
				.findByNamedQueryAndNamedParam(
						"findDistrictManagerCurrentAnnouncementsByEmail",
						names, values);

	}

	public List findDistrictManagerCurrentAnnouncementsByDistrictId(
			String districtId) {
		String[] names = { "districtId" };
		Object[] values = new Object[] { districtId };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerCurrentAnnouncementsByDistrictId", names,
				values);

	}

	public List findNationalCurrentAnnouncements() {
		return getHibernateTemplate().findByNamedQuery(
				"findNationalCurrentAnnouncements");
	}

	public List findNationalExpiredPromotions() {
		return getHibernateTemplate().findByNamedQuery(
				"findNationalExpiredPromotions");
	}

	public List findZoneManagerExpiredPromotionsByEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerExpiredPromotionsByEmail", names, values);
	}

	public List findZoneManagerExpiredPromotionsByZoneId(Object id) {
		String[] names = { "zoneId" };
		Object[] values = new Object[] { Integer.valueOf(id.toString()) };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerExpiredPromotionsByZoneId", names, values);

	}

	public List findDistrictManagerExpiredPromotionsByEmail(String email) {
		String[] names = { "email" };
		Object[] values = new Object[] { email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerExpiredPromotionsByEmail", names, values);

	}

	public List findDistrictManagerExpiredPromotionsByDistrictId(
			String districtId) {
		String[] names = { "districtId" };
		Object[] values = new Object[] { districtId };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerExpiredPromotionsByDistrictId", names,
				values);

	}

	public List findDistrictManagerOfferStoresByOfferId(String offerId,
			String email) {
		String[] names = { "offerId", "email" };
		Object[] values = new Object[] { offerId, email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerOfferStoresByOfferId", names, values);
	}

	public List findZoneManagerOfferStoresByOfferId(String offerId, String email) {
		String[] names = { "offerId", "email" };
		Object[] values = new Object[] { offerId, email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerOfferStoresByOfferId", names, values);
	}

	public List findNationalOfferStoresByOfferId(String offerId) {
		String[] names = { "offerId" };
		Object[] values = new Object[] { offerId };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findNationalOfferStoresByOfferId", names, values);
	}

	public List findDistrictManagerPromotionStoresByPromoId(String promoId,
			String email) {
		String[] names = { "promoId", "email" };
		Object[] values = new Object[] { promoId, email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerPromotionStoresByPromoId", names, values);
	}

	public List findZoneManagerPromotionStoresByPromoId(String promoId,
			String email) {
		String[] names = { "promoId", "email" };
		Object[] values = new Object[] { promoId, email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerPromotionStoresByPromoId", names, values);
	}

	public List findNationalPromotionStoresByPromoId(String promoId) {
		String[] names = { "promoId" };
		Object[] values = new Object[] { promoId };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findNationalPromotionStoresByPromoId", names, values);
	}

	public List findDistrictManagerAnnouncementStoresByAnnouncementId(
			String announcementId, String email) {
		String[] names = { "announcementId", "email" };
		Object[] values = new Object[] { announcementId, email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findDistrictManagerAnnouncementStoresByAnnouncementId", names,
				values);
	}

	public List findZoneManagerAnnouncementStoresByAnnouncementId(
			String announcementId, String email) {
		String[] names = { "announcementId", "email" };
		Object[] values = new Object[] { announcementId, email };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findZoneManagerAnnouncementStoresByAnnouncementId", names,
				values);
	}

	public List findNationalAnnouncementStoresByAnnouncementId(
			String announcementId) {
		String[] names = { "announcementId" };
		Object[] values = new Object[] { announcementId };
		return getHibernateTemplate()
				.findByNamedQueryAndNamedParam(
						"findNationalAnnouncementStoresByAnnouncementId",
						names, values);
	}

	public StoreAdminStoreImage findWorkingStoreAdminStoreImageByStoreNumber(Object id) {
		if (id == null)
			return null;
		try {
			Integer iid = Integer.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminStoreImage s where s.storeNumber=" + iid +" and s.status = 'W'");
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminStoreImage) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public StoreAdminStoreImage findPublishedStoreAdminStoreImageByStoreNumber(Object id) {
		if (id == null)
			return null;
		try {
			Integer iid = Integer.valueOf(id.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminStoreImage s where s.storeNumber=" + iid +" and s.status = 'P'");
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminStoreImage) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public StoreAdminStoreImage findStoreAdminStoreImageById(Object imageId) {
		if (imageId == null)
			return null;
		try {
			Long lid = Long.valueOf(imageId.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminStoreImageImage l where l.imageId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminStoreImage) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List findAllStoreAdminLibraryImages() {
		// Library images can either be 'L'ibrary for stock images to be used multiple times
		// or 'I'ndividual images - for use with one appointment or promotion
		String hql = "from StoreAdminLibraryImage i where type = 'L' order by i.createdDate desc";
		List l = getHibernateTemplate().find(hql);
		return l;
	}

	public StoreAdminLibraryImage findStoreAdminLibraryImageById(Object imageId) {
		if (imageId == null)
			return null;
		try {
			Long lid = Long.valueOf(imageId.toString());
			List l = getHibernateTemplate().find(
					"from StoreAdminLibraryImage l where l.imageId=" + lid);
			if (l == null || l.size() == 0)
				return null;
			return (StoreAdminLibraryImage) l.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public List fetchOtherStoresInConflict(String promoId, List storeNumbers,
			List categoriesUsed, Date startDate, Date endDate) {
        Session s = getSession();
        try{
		if (storeNumbers == null || storeNumbers.size() == 0
				|| categoriesUsed == null || categoriesUsed.size() == 0)
			return null;

		String categoriesString = "";

		for (int i = 0; i < categoriesUsed.size(); i++) {
			if (i == 0)
				categoriesString += "'" + (String) categoriesUsed.get(i) + "'";
			else
				categoriesString += ",'" + (String) categoriesUsed.get(i) + "'";
		}
		List l = new ArrayList(); //max allowable inside 'in' statement is 1000 so take 0-999,1000-1999
		int x = storeNumbers.size()/1000;
		if(storeNumbers.size()%1000>0)
			x++;
		List[] storeNumbersArray = new List[x];
		for(int i = 0; i<x ; i++){
			if(i==(x-1))
				storeNumbersArray[i]=storeNumbers.subList((i*1000), storeNumbers.size());
			else
				storeNumbersArray[i]=storeNumbers.subList((i*1000), (i*1000)+1000);
		}
		for(List storeNumbersPart: storeNumbersArray){
			String storeNumbersString = "";
			for (int i = 0; i < storeNumbersPart.size(); i++) {
				if (i == 0)
					storeNumbersString += "'" + (String) storeNumbersPart.get(i) + "'";
				else
					storeNumbersString += ",'" + (String) storeNumbersPart.get(i) + "'";
			}
			String sql = "select s.STORE_NUMBER as storeNumber,s.ADDRESS as storeAddress,oc.NAME as categoryName,ot.NAME as templateName,p.START_DATE as promoStartDate,p.END_DATE as promoEndDate from RTMS_WEBDB.STORE_ADMIN_OFFER o LEFT OUTER JOIN RTMS_WEBDB.STORE_ADMIN_PROMO p on o.PROMO_ID = p.PROMO_ID LEFT OUTER JOIN RTMS_WEBDB.STORE_ADMIN_PROMO_STORE_JOIN ps on ps.PROMO_ID = p.PROMO_ID LEFT OUTER JOIN RTMS_WEBDB.STORE s on s.STORE_NUMBER = ps.STORE_NUMBER LEFT OUTER JOIN RTMS_WEBDB.STORE_ADMIN_OFFER_TEMPLATE ot on o.TEMPLATE_ID = ot.TEMPLATE_ID LEFT OUTER JOIN RTMS_WEBDB.STORE_ADMIN_OFFER_CATEGORY oc on oc.CATEGORY_ID = ot.CATEGORY_ID where p.PROMO_ID <> '"
					+ promoId
					+ "' and s.STORE_NUMBER in ("
					+ storeNumbersString
					+ ") and oc.CATEGORY_ID in("
					+ categoriesString
					+ ") and p.STATUS <> 'PRE_PENDING' and p.STATUS <> 'INACTIVE' and ((p.START_DATE <:startDate and p.END_DATE >:startDate) or (p.START_DATE < :endDate and p.END_DATE > :endDate) or (p.START_DATE > :startDate and p.END_DATE < :endDate))";
	
			l.addAll(s.createSQLQuery(sql)
					.addScalar("storeNumber", Hibernate.LONG)
					.addScalar("storeAddress", Hibernate.STRING)
					.addScalar("categoryName", Hibernate.STRING)
					.addScalar("templateName", Hibernate.STRING)
					.addScalar("promoStartDate", Hibernate.DATE)
					.addScalar("promoEndDate", Hibernate.DATE)
					.setDate("startDate", startDate).setDate("endDate", endDate)
					.list());
		}

		return l;
        }finally {
        	//s.close();
			this.releaseSession(s);
		}
	}

	public Long createAnnouncement(StoreAdminAnnouncement item){
		item.setCreatedDate(new Date());
		item.setModifiedDate(new Date());
		return (Long)getHibernateTemplate().save(item);
	}
	public void updateAnnouncement(StoreAdminAnnouncement item){
		item.setModifiedDate(new Date());
		getHibernateTemplate().update(item);
	}
	public void deleteAnnouncement(StoreAdminAnnouncement item){
		getHibernateTemplate().delete(item);
	}
	public void deleteAnnouncementById(Object id){
		StoreAdminAnnouncement item = findAnnouncementById(id);
		getHibernateTemplate().delete(item);
	}
	public StoreAdminAnnouncementStoreJoinId createAnnouncementStoreJoin(StoreAdminAnnouncementStoreJoin item){
		return (StoreAdminAnnouncementStoreJoinId)getHibernateTemplate().save(item);
	}
	public void updateAnnouncementStoreJoin(StoreAdminAnnouncementStoreJoin item){
		getHibernateTemplate().update(item);
	}
	public void deleteAnnouncementStoreJoin(StoreAdminAnnouncementStoreJoin item){
		getHibernateTemplate().delete(item);
	}
	public void deleteAnnouncementStoreJoinById(Object id){
		StoreAdminAnnouncementStoreJoin item = new StoreAdminAnnouncementStoreJoin();
		item.setId((StoreAdminAnnouncementStoreJoinId)id);
		getHibernateTemplate().delete(item);
	}
	public void deleteAnnouncementStoreJoinById(Object announcementId, Object storeNumber){
		if(announcementId == null || storeNumber == null)
			return;
		try{
			Long lannouncementId = Long.valueOf(announcementId.toString());
			Integer istoreNumber = Integer.valueOf(storeNumber.toString());
			StoreAdminAnnouncementStoreJoinId lid = new StoreAdminAnnouncementStoreJoinId(lannouncementId, istoreNumber);
			StoreAdminAnnouncementStoreJoin item = new StoreAdminAnnouncementStoreJoin();
			item.setId(lid);
			getHibernateTemplate().delete(item);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void createStoreAdminStoreImage(StoreAdminStoreImage item){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setCreatedDate(new Date());
			item.setModifiedDate(new Date());
			getHibernateTemplate().save(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			ex.printStackTrace();
			System.err.print("StoreAdminDAO could not create store image ");
			System.err.println();
		}
	}
	public void updateStoreAdminStoreImage(StoreAdminStoreImage item){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("StoreAdminDAO could not update store image ");
			System.err.println();
		}
	}
	public void deleteStoreAdminStoreImage(StoreAdminStoreImage item){
		getHibernateTemplate().delete(item);
	}
	public Long createStoreAdminLibraryImage(StoreAdminLibraryImage item){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setCreatedDate(new Date());
			item.setModifiedDate(new Date());
			Long rt = (Long) getHibernateTemplate().save(item);
			this.txManager.commit(status);
			return rt;
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("StoreAdminDAO could not create library image ");
			System.err.println();
		}
		return new Long(-1);
	}
	public void updateStoreAdminLibraryImage(StoreAdminLibraryImage item){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("StoreAdminDAO could not update library image ");
			System.err.println();
		}
	}
	public void deleteStoreAdminLibraryImage(StoreAdminLibraryImage item){
		getHibernateTemplate().delete(item);
	}
	public List findAnnouncementsByStoreId(String storeNum) {
		Long storeId = Long.valueOf(storeNum);
		String[] names = { "storeId" };
		Object[] values = new Object[] { storeId };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findAnnouncementsByStoreId", names, values);
	}
	public List findAnnouncementsByStoreIdAndDate(String storeNum, Date selDate) {
		Long storeId = Long.valueOf(storeNum);
		String[] names = { "storeId", "selDate" };
		Object[] values = new Object[] { storeId, selDate };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findAnnouncementsByStoreIdAndDate", names, values);
	}
	
	public List findPromotionsByStoreId(String storeNum) {
		Long storeId = Long.valueOf(storeNum);
		String[] names = { "storeId" };
		Object[] values = new Object[] { storeId };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findPromotionsByStoreId", names, values);
	}
	public List findPromotionsByStoreIdAndDate(String storeNum, Date selDate) {
		Long storeId = Long.valueOf(storeNum);
		String[] names = { "storeId", "selDate" };
		Object[] values = new Object[] { storeId, selDate };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"findPromotionsByStoreIdAndDate", names, values);
	}

	public void updateBannerSettingOnStores(List<Long> storeNumbers, String bannerSetting) {
		if(bannerSetting!=null&&storeNumbers!=null&&storeNumbers.size()>0){
			//Session s = getSession();
			for(Long storeNumber : (List<Long>)storeNumbers){
				try{
				List<StoreBanner> l = getHibernateTemplate().find(
						"from StoreBanner s where s.storeNumber=" + storeNumber );
				if (l == null || l.size() == 0){
					StoreBanner sb = new StoreBanner();
					sb.setStoreNumber(storeNumber);
					sb.setBannerSetting(bannerSetting);
					getHibernateTemplate().save(sb);
				}else{
					StoreBanner sb = l.get(0);
					sb.setBannerSetting(bannerSetting);
					getHibernateTemplate().update(sb);
				}
				}catch(Exception ex){
					ex.printStackTrace();
					continue;
				}
					
				
			}
//			String hqlUpdate = "update StoreBanner s set s.bannerSetting = :bannerSetting where s.storeNumber in (:storeNumbers)";
//			// or String hqlUpdate = "update Customer set name = :newName where name = :oldName";
//			int updatedEntities = s.createQuery( hqlUpdate )
//			        .setString( "bannerSetting",bannerSetting )
//			        .setParameterList( "storeNumbers", storeNumbers )
//			        .executeUpdate();
		}
	}
}
