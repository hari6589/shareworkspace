package com.bfrc.framework.dao.hibernate3;

import java.util.*;

import org.hibernate.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.*;
import com.bfrc.framework.spring.HibernateDAOImpl;

import com.bfrc.pojo.customer.CustomerContactusemailLog;
import com.bfrc.pojo.realestate.*;
import com.bfrc.pojo.store.Store;

public class RealEstateDAOImpl extends HibernateDAOImpl implements RealEstateDAO {
    
	private PlatformTransactionManager txManager;
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
    // Real Estate Pages
	public RealestatePage findRealestatePage(Object id)
    {
    	if(id == null) return null;
		return (RealestatePage)getHibernateTemplate().get(RealestatePage.class,id.toString());
    }
	public void createRealestatePage(RealestatePage item) {
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
		}
		
	}
	public List getAllRealestatePages() {
		String hql = "from RealestatePage t order by t.createdDate";
		List l = getHibernateTemplate().find(hql);
		return l;
	}

	public void updateRealestatePage(RealestatePage item) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			ex.printStackTrace();
		}
	}

	public void deleteRealestatePage(RealestatePage item) {
		getHibernateTemplate().delete(item);
	}
	public void deleteRealestatePage(Object id) {
		getHibernateTemplate().delete(findRealestatePage(id));
	}
	
    // Real Estate Region Contact
	public RealestateRegionContact findRealestateRegionContact(Object id)
    {
    	if(id == null) return null;
		return (RealestateRegionContact)getHibernateTemplate().get(RealestateRegionContact.class,id.toString());
    }
	public List getRealestateRegionContactsByWebSite(String webSite) {
		if(webSite == null)
			return null;
		RealestateRegionContact bean = new RealestateRegionContact();
		bean.setWebSite(webSite);
		String hql = "from RealestateRegionContact t where t.webSite=:webSite  order by t.createdDate";
		List l = getHibernateTemplate().findByValueBean(hql,bean);
		return l;
	}
	
	public Map getMappedRealestateRegionContactsByWebSite(String webSite) {
		
		List l = getRealestateRegionContactsByWebSite(webSite);
		if(l != null){
			Map m = new LinkedHashMap();
			for(int i=0; i< l.size(); i++){
				RealestateRegionContact item  = (RealestateRegionContact)l.get(i);
				m.put(item.getRegionId(), item);
			}
			return m;
		}
		return null;
	}
	
	public void createRealestateRegionContact(RealestateRegionContact item) {
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
		}
	}
	public List getAllRealestateRegionContacts() {
		String hql = "from RealestateRegionContact t order by t.createdDate";
		List l = getHibernateTemplate().find(hql);
		return l;
	}

	public void updateRealestateRegionContact(RealestateRegionContact item) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			ex.printStackTrace();
		}
	}

	public void deleteRealestateRegionContact(RealestateRegionContact item) {
		getHibernateTemplate().delete(item);
	}
	public void deleteRealestateRegionContact(Object id) {
		getHibernateTemplate().delete(findRealestateRegionContact(id));
	}
	
	// Real Estate RealestateStoreGallery
	public List getAllRealestateStoreGalleries() {
		String hql = "from RealestateStoreGallery t order by t.createdDate";
		List l = getHibernateTemplate().find(hql);
		return l;
	}
	public RealestateStoreGallery findRealestateStoreGallery(Object id)
    {
    	if(id == null) return null;
		return (RealestateStoreGallery)getHibernateTemplate().get(RealestateStoreGallery.class,new Long(id.toString()));
    }
	
	public List getRealestateStoreGalleriesByYearAndWebSite(Object year, String webSite) {
		if(year == null || webSite == null)
			return null;
		RealestateStoreGallery bean = new RealestateStoreGallery();
		bean.setWebSite(webSite);
		bean.setGalleryYear(new Short(year.toString()));
		String hql = "from RealestateStoreGallery t where t.webSite=:webSite and galleryYear=:galleryYear order by t.sortOrder, t.createdDate";
		List l = getHibernateTemplate().findByValueBean(hql,bean);
		return l;
	}
    
	public List getRealestateStoreGalleryYearsByWebSite(String webSite) {
		if(webSite == null)
			return null;
		List data = new ArrayList();	
		String hql = "select distinct t.galleryYear "
			+ "RealestateStoreGallery t where t.webSite=? order by t.galleryYear";
		List l = getHibernateTemplate().find(hql, new Object[]{webSite});
		if(l != null && l.size() > 0){
			Object o = (Object)l.iterator().next();
			data.add(o);
		}
		return data;
	}
	
	public void createRealestateStoreGallery(RealestateStoreGallery item) {
		
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
		}
	}
	public void updateRealestateStoreGallery(RealestateStoreGallery item) {
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			ex.printStackTrace();
		}
	}

	public void deleteRealestateStoreGallery(RealestateStoreGallery item) {
		getHibernateTemplate().delete(item);
	}
	public void deleteRealestateStoreGallery(Object id) {
		getHibernateTemplate().delete(findRealestateStoreGallery(id));
	}
	
	
	// RealestateSurplusProperty
	public List getAllRealestateSurplusProperties() {
		String hql = "from RealestateSurplusProperty t order by t.createdDate";
		List l = getHibernateTemplate().find(hql);
		return l;
	}
	public RealestateSurplusProperty findRealestateSurplusProperty(Object id)
    {
    	if(id == null) return null;
		return (RealestateSurplusProperty)getHibernateTemplate().get(RealestateSurplusProperty.class,new Long(id.toString()));
    }
	
	public List getRealestateSurplusPropertiesByWebSite(String webSite) {
		if(webSite == null)
			return null;
		RealestateSurplusProperty bean = new RealestateSurplusProperty();
		bean.setWebSite(webSite);
		String hql = "from RealestateSurplusProperty t where t.webSite=:webSite order by t.state, t.city, t.createdDate";
		List l = getHibernateTemplate().findByValueBean(hql,bean);
		return l;
	}
    
	public List getActiveRealestateSurplusPropertiesByWebSite(String webSite) {
		if(webSite == null)
			return null;
		RealestateSurplusProperty bean = new RealestateSurplusProperty();
		bean.setWebSite(webSite);
		String hql = "from RealestateSurplusProperty t where t.webSite=:webSite and status=0 order by t.state, t.city, t.createdDate";
		List l = getHibernateTemplate().findByValueBean(hql,bean);
		return l;
	}
	
	
	public void createRealestateSurplusProperty(RealestateSurplusProperty item) {
		
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
		}
	}
	public void updateRealestateSurplusProperty(RealestateSurplusProperty item) {
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setModifiedDate(new Date());
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			ex.printStackTrace();
		}
	}

	public void deleteRealestateSurplusProperty(RealestateSurplusProperty item) {
		getHibernateTemplate().delete(item);
	}
	public void deleteRealestateSurplusProperty(Object id) {
		getHibernateTemplate().delete(findRealestateSurplusProperty(id));
	}
}
