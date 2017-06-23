package com.bfrc.framework.dao.hibernate3;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.promotion.Promotion;
import com.bfrc.pojo.promotion.PromotionBrand;
import com.bfrc.pojo.promotion.PromotionImages;
import com.bfrc.pojo.promotion.PromotionLandingType;
import com.bfrc.pojo.promotion.PromotionType;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class PromotionDAOImpl extends HibernateDAOImpl implements PromotionDAO {
	
	private final Log logger = LogFactory.getLog(PromotionDAOImpl.class);

    private static final String ORDER_BY = " order by p.orderId, p.friendlyId ";
	public void updatePromotion(PromotionImages p) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			p.setModifiedDate(new Date());
			getHibernateTemplate().update(p);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PromotionDAO could not update promotion ");
			if(p != null)
				System.err.print(p.toString());
			System.err.println();
		    throw ex;
		}
	}

	public List getAllCouponsAndPromos(int type) {
		List l = getAllCouponsAndPromos();
		List out = new ArrayList();
		if(l != null) {
			Iterator i = l.iterator();
			while(i.hasNext()) {
				Promotion p = (Promotion)i.next();
				Boolean b = null;
				switch(type) {
					case MAINT:
						b = p.getMaintOffer();
						break;
					case REPAIR:
						b = p.getRepairOffer();
						break;
					case TIRE:
						b = p.getTireOffer();
					default:
						break;
				}
				if(Boolean.TRUE.equals(b))
					out.add(p);
			}
		}
		return out;
	}

	private PlatformTransactionManager txManager;
	
	public List getAllCouponsAndPromos() {
		return getAllCouponsAndPromos(true);
	}
	
	public List getAllCouponsAndPromos(boolean filterByDate) {
		Promotion p = new Promotion();
		Calendar c = Calendar.getInstance();
		p.setExpirationDate(c.getTime());
//		c.add(Calendar.DATE, 1);
		p.setStartDate(c.getTime());
		p.setWebSite(getConfig().getSiteName());
		String hql = "from Promotion p where p.webSite=:webSite";
		if(filterByDate)
			hql += " and p.startDate<:startDate and "
				+ "(p.expirationDate>:expirationDate or p.expirationDate is null)";
		hql += ORDER_BY;
		return getHibernateTemplate().findByValueBean(hql, p);
	}
	
	protected List getData(Promotion p) {
		Calendar c = Calendar.getInstance();
		return getData(p, c.getTime());
	}
	
	 /**
     * @deprecated
     * @see 
     */
	protected List getData(Promotion p, Date beginDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(beginDate);
		
		p.setExpirationDate(c.getTime());
//		c.add(Calendar.DATE, 1);
		p.setStartDate(c.getTime());
		p.setWebSite(getConfig().getSiteName());
		return getHibernateTemplate().findByValueBean("from Promotion p where p.webSite=:webSite and p.startDate<:startDate and "
				+ "(p.expirationDate>:expirationDate or p.expirationDate is null) and p.promoType=:promoType and "
				+ "(p.suvDisplay is null or p.suvDisplay<2) "
				+ ORDER_BY, p);
	}
	
	public List getCoupons(Date beginDate) {
		Promotion p = new Promotion();
		p.setPromoType('C');
		if (beginDate != null)
			return getData(p, beginDate);
		else
			return getData(p);
	}
	
	public List getCoupons() {
		Promotion p = new Promotion();
		p.setPromoType('C');
		return getData(p);
	}

	public void createPromotion(PromotionImages p) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
//	        long currOrder = p.getOrderId(), prevOrder = 0;
//	        Long maxOrder = getMaxOrderId(currOrder);
//	        if(maxOrder != null)
//	        	prevOrder = maxOrder.longValue();
//	        p.setOrderId((currOrder - prevOrder)/2 + prevOrder);
			p.setCreatedDate(new Date());
//            if(ServerUtil.isNullOrEmpty(p.getWebSite())){
//			    p.setWebSite(getConfig().getSiteName());
//            }
			getHibernateTemplate().save(p);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PromotionDAO could not create promotion ");
			if(p != null)
				System.err.print(p.toString());
			System.err.println();
		    throw ex;
		}
	}

	public Promotion getPromotion(long id) {
		Promotion p = new Promotion();
		p.setPromotionId(id);
		List l = getHibernateTemplate().findByValueBean("from Promotion p where p.promotionId=:promotionId", p);
		if(l != null && l.size() > 0) {
			return (Promotion)l.get(0);
		} else return null;
	}
	
	public Promotion getPromotionByFriendlyId(String friendlyId) {
		Promotion p = new Promotion();
		p.setFriendlyId(friendlyId);
		List l = getHibernateTemplate().findByValueBean("from Promotion p where p.friendlyId=:friendlyId", p);
		if(l != null && l.size() > 0) {
			return (Promotion)l.get(0);
		} else return null;
	}

	public PromotionImages getPromotionImages(long id) {
		PromotionImages p = new PromotionImages();
		p.setPromotionId(id);
		List l = getHibernateTemplate().findByValueBean("from PromotionImages p where p.promotionId=:promotionId", p);
		if(l != null && l.size() > 0) {
			return (PromotionImages)l.get(0);
		} else return null;
	}
	public PromotionImages getPromotionImagesById(Object id, boolean checkDates) {
		PromotionImages p = new PromotionImages();
		if(id == null)
			return null;
		long lid =0;
		try{
			lid = Long.parseLong(id.toString());
		}catch(Exception ex){
			
		}
		List l = null;
		String cons = checkDates ? " and "+getCheckDatesClause() : "";
		if(lid > 0){
		    p.setPromotionId(lid);
		    l = getHibernateTemplate().findByValueBean("from PromotionImages p where p.promotionId=:promotionId"+cons, p);
		}else{
			p.setFriendlyId(id.toString());
		    l = getHibernateTemplate().findByValueBean("from PromotionImages p where p.friendlyId=:friendlyId"+cons, p);
		    if( l== null || l.size() == 0){
		    	p.setLandingPageId(id.toString());
				l = getHibernateTemplate().findByValueBean("from PromotionImages p where p.landingPageId=:landingPageId"+cons, p);
		    }
		}
		if(l != null && l.size() > 0) {
			return (PromotionImages)l.get(0);
		} else return null;
	}
	public PromotionImages getPromotionImagesById(Object id) {
		return getPromotionImagesById(id, false);
	}
	
	public PromotionImages getPromotionImagesByLandingPageID(Object id){
		if(id == null)
			return null;
		/*PromotionImages p = new PromotionImages();
		p.setLandingPageId(id.toString());
		List l = getHibernateTemplate().findByValueBean("from PromotionImages p where p.landingPageId=:landingPageId", p);
		if(l != null && l.size() > 0) {
			return (PromotionImages)l.get(0);
		} else return null;*/
		// Instead of loading List with many PromotionImages and returning only first element, just loaded only first element.
		Session session = getSession();
		try {
			Criteria cr = session.createCriteria(PromotionImages.class);
			cr.add(Expression.eq("landingPageId", id.toString()));
			PromotionImages result = (PromotionImages)cr.uniqueResult();
			return result;
		} finally {
			releaseSession(session);
		}
	}
	public List getPromotions(Date beginDate) {
		Promotion p = new Promotion();
		p.setPromoType('P');
		List l = null;
		if (beginDate != null)
			l = getData(p, beginDate);
		else
			l = getData(p);
		
		List out = new ArrayList();
		if(l != null) {
			Iterator i = l.iterator();
			while(i.hasNext()) {
				Promotion test = (Promotion)i.next();
				boolean hasImage = test.getImageBlob() != null;
				boolean hasThumb = test.getThumbBlob() != null;
				boolean hasFlashIcon = test.getFlashIconBlob() != null;
				if(hasImage || hasThumb || hasFlashIcon) {
					PromotionImages images = getPromotionImages(test.getPromotionId());
					images.setHasImage(hasImage);
					images.setHasThumb(hasThumb);
					images.setHasFlashIcon(hasFlashIcon);
					out.add(images);
				} else out.add(test);
			}
		}
		return out;
	}
	
	public List getPromotions() {
		return getPromotions(null);
	}

	public void deletePromotion(Integer id) {
		getHibernateTemplate().delete(getPromotion(id.intValue()));
	}

	public Long getMaxOrderId(long curr) {
		Session s = getSession();
		Long id = null;
		try {
			SQLQuery query = s.createSQLQuery("select max(p.order_id) as maxOrder from Promotion p where p.order_id < ?");
			query.setLong(0, curr);
			query.addScalar("maxOrder", Hibernate.LONG);
			id = (Long)query.uniqueResult();
		} finally {
			//s.close();
			this.releaseSession(s);
		}
//System.err.println("type of result=" + query.uniqueResult().getClass().getName());
		return id;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	public byte[] resizeImage(byte[] in, int thumbWidth, int thumbHeight) throws Exception {
		Image image = Toolkit.getDefaultToolkit().createImage(in);
	    MediaTracker mediaTracker = new MediaTracker(new Container());
	    mediaTracker.addImage(image, 0);
	    mediaTracker.waitForID(0);
	    // determine thumbnail size from WIDTH and HEIGHT
	    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
	    int imageWidth = image.getWidth(null);
	    int imageHeight = image.getHeight(null);
	    double imageRatio = (double)imageWidth / (double)imageHeight;
	    if (thumbRatio < imageRatio) {
	      thumbHeight = (int)(thumbWidth / imageRatio);
	    } else {
	      thumbWidth = (int)(thumbHeight * imageRatio);
	    }
	    // draw original image to thumbnail image object and
	    // scale it to the new size on-the-fly
	    BufferedImage thumbImage = new BufferedImage(thumbWidth, 
	      thumbHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = thumbImage.createGraphics();
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
	    // save thumbnail image to OUTFILE
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    JPEGEncodeParam param = encoder.
	      getDefaultJPEGEncodeParam(thumbImage);
	    int quality = getConfig().getThumbnailQuality().intValue();
	    quality = Math.max(0, Math.min(quality, 100));
	    param.setQuality((float)quality / 100.0f, false);
	    encoder.setJPEGEncodeParam(param);
	    encoder.encode(thumbImage);
	    return out.toByteArray();
	}
	
	public List getAllSitesCouponsAndPromos() {
		return getAllSitesCouponsAndPromos(true);
	}
	
	String getCheckDatesClause(){
		return " p.startDate < sysdate and (p.expirationDate>sysdate or p.expirationDate is null)";
	}
	
	String getCheckDatesClauseParameter(){
		return " p.startDate < :testDate and (p.expirationDate> :testDate or p.expirationDate is null)";
	}
	
	
	public List getAllSitesCouponsAndPromos(boolean filterByDate) {
		Promotion p = new Promotion();
		Calendar c = Calendar.getInstance();
		p.setExpirationDate(c.getTime());
//		c.add(Calendar.DATE, 1);
		p.setStartDate(c.getTime());
		p.setWebSite(getConfig().getSiteName());
		String hql = "from Promotion p ";
		if(filterByDate)
			hql += " where "+getCheckDatesClause();
			/*
			hql += " where p.startDate<:startDate and "
				+ "(p.expirationDate>:expirationDate or p.expirationDate is null)";
		   */
		hql += " order by p.webSite, p.orderId";
		List l = getHibernateTemplate().findByValueBean(hql, p);
		List out = new ArrayList();
		if(l != null) {
			Iterator i = l.iterator();
			while(i.hasNext()) {
				Promotion test = (Promotion)i.next();
				/*boolean hasImage = test.getImageBlob() != null;
				boolean hasThumb = test.getThumbBlob() != null;
				boolean hasFlashIcon = test.getFlashIconBlob() != null;
				if(hasImage || hasThumb || hasFlashIcon) {
					PromotionImages images = getPromotionImages(test.getPromotionId());
					images.setHasImage(hasImage);
					images.setHasThumb(hasThumb);
					images.setHasFlashIcon(hasFlashIcon);
					out.add(images);
				} else*/ 
				//Commented out loading PromotionImages object to list and load only Promotion object always, 
				//as we are not using/displaying promotion images anywhere in the place where we are calling this API method.
				out.add(test);
			}
		}
		return out;
	}
	public List findPromotionsByIds(List ids) {
		return findPromotionsByIds(ids,false);
	}
	public List findPromotionsByIds(List ids, boolean checkDates) {
		if(ids == null)
			return null;
		String cons = checkDates ? " and "+getCheckDatesClause() : "";
		return getHibernateTemplate().findByNamedParam("from PromotionImages p where p.promotionId in (:ids)"+cons+ORDER_BY,
				"ids",
				ids);
	}
	public List findPromotionsByIds(String pipeDelimitedIds) {
		 return findPromotionsByIds(pipeDelimitedIds,false);
	}
	public List findPromotionsByIds(String pipeDelimitedIds, boolean checkDates) {
		if(pipeDelimitedIds == null)
			return null;
		try{
			String[] tokens = StringUtils.tokenToArray(pipeDelimitedIds, "|");
			List l = new ArrayList();
			for(int i =0; tokens != null && i<tokens.length; i++){
				l.add(Integer.valueOf(tokens[i]));
				if(l.size() >999)
					break;
			}
			return findPromotionsByIds(l, checkDates);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
		
	}
	public List findPromotionsByFriendlyIds(List friendlyIds) {
		return findPromotionsByFriendlyIds(friendlyIds,false);
	}
	public List findPromotionsByFriendlyIds(List friendlyIds, boolean checkDates) {
		if(friendlyIds == null)
			return null;
		String cons = checkDates ? " and "+getCheckDatesClause() : "";
		return getHibernateTemplate().findByNamedParam("from PromotionImages p where p.friendlyId in (:friendlyIds)"+cons+ORDER_BY,
				"friendlyIds",
				friendlyIds);
	}
	
	public List findPromotionsByFriendlyIds(List friendlyIds, Date testDate) {
		if(friendlyIds == null || testDate == null)
			return null;
//		String query = "from PromotionImages p where p.friendlyId in (:friendlyIds) and "+getCheckDatesClauseParameter()+ORDER_BY;
		String[] paramNames = {"friendlyIds","testDate"};		
		Object[] values= {friendlyIds,testDate};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("findPromoByFriendlyIdsWithoutBlobs", paramNames, values);
	}
	
	public List findPromotionsByFriendlyIds(String pipeDelimitedIds) {
		return findPromotionsByFriendlyIds(pipeDelimitedIds,false);
	}
	public List findPromotionsByFriendlyIds(String pipeDelimitedIds, boolean checkDates) {
		if(pipeDelimitedIds == null)
			return null;
		try{
			String[] tokens = StringUtils.tokenToArray(pipeDelimitedIds, "|");
			List l = new ArrayList();
			for(int i =0; tokens != null && i<tokens.length; i++){
				l.add(tokens[i]);
				if(l.size() >999)
					break;
			}
			return findPromotionsByFriendlyIds(l, checkDates);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
		
	}
		
	public List<Promotion> getActivePromotionsByType(String site, String promotionType, Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion "
				+ getPromotionWhereClause()
				+ "and promo_type = :promoType "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", site);
			query.setString("promoType", promotionType);
			query.setTimestamp("startDate", startDate);
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}
	
	public List<Promotion> getActivePromotionsByLandingPageId(String site, String landingPageId, Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and landing_page_id = :landingPageId "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		try{			 
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", site);
			query.setString("landingPageId", landingPageId);
			query.setTimestamp("startDate", startDate);
			results = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}
	
	public String getDisclaimerByLandingPageId(String site, String landingPageId) {
		
		Session session = getSession();
		String disclaimer = null;
		try{			 
			if(landingPageId!=null && landingPageId.equals("rewards")){
				SQLQuery query1 = session.createSQLQuery("select DESCRIPTION from PROMOTION_DISCLAIMER where WEB_SITE = '"+site+"' and LANDING_PAGE_ID = '"+landingPageId+"' ");
				 disclaimer = (String)query1.uniqueResult();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return disclaimer;
	}
	
	public List<Promotion> getHomePageCoupons(String site, Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and home_page_offer = 1 "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", site);
			query.setTimestamp("startDate", startDate);
			results = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}
	
	public List<PromotionType> getPromotionTypes() {
		String promoTypeQuery = "SELECT * FROM PROMOTION_TYPE";
		
		List<PromotionType> results = new ArrayList<PromotionType>();
		Session session = getSession();
		try{
			SQLQuery query = session.createSQLQuery(promoTypeQuery);
			query.addEntity(PromotionType.class);
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}
	
	public List<Object[]> checkPromotionsForImage(List<Long> promoIds){
		StringBuffer queryString = new StringBuffer();
		List<Object[]> results = new ArrayList<Object[]>();
		if(promoIds == null || promoIds.isEmpty()){
			return results;
		}
		
		Session session = getSession();
		queryString.append("select promotion_id as promotionId, ");
		queryString.append("CASE when THUMBNAIL is null then '0' else '1' end as hasImage ");
		queryString.append("from promotion where promotion_id in (:promoIds)");
		
		try{
			SQLQuery query = session.createSQLQuery(queryString.toString());
			query.addScalar("promotionId", Hibernate.LONG);
			query.addScalar("hasImage", Hibernate.BOOLEAN);
			query.setParameterList("promoIds", promoIds);
			
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		return results;
	}

	@Override
	public List<Promotion> getActiveRepairOffers(String siteName, Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and REPAIR_OFFER = 1 "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", siteName);
			query.setTimestamp("startDate", startDate);
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}

	@Override
	public List<Promotion> getActiveMaintenanceOffers(String siteName,
			Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and MAINT_OFFER = 1 "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", siteName);
			query.setTimestamp("startDate", startDate);				
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}
	

	@Override
	public List<Promotion> getActiveRepairAndHomeOffers(String siteName, Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and REPAIR_OFFER = 1 "
				+ "and home_page_offer = 1 "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", siteName);
			query.setTimestamp("startDate", startDate);
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}

	@Override
	public List<Promotion> getActiveMaintenanceAndHomeOffers(String siteName,
			Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and MAINT_OFFER = 1 "
				+ "and home_page_offer = 1 "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", siteName);
			query.setTimestamp("startDate", startDate);				
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}
		
	@Override
	public List<Promotion> getActiveTireCoupons(String siteName, Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ " and TIRE_OFFER = 1 "
				+ getPromotionOrderByClause();
		
		List<Promotion> results = new ArrayList<Promotion>();
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(Promotion.class);
			query.setString("website", siteName);
			query.setTimestamp("startDate", startDate);				
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}
	
	public PromotionImages getActivePromotionImagesById(String siteName, long id, Date startDate) {
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and PROMOTION_ID = :id ";
		
		PromotionImages result = null;
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(PromotionImages.class);
			query.setString("website", siteName);
			query.setTimestamp("startDate", startDate);
			query.setLong("id", id);
			List<PromotionImages> list =query.list();
			if(list != null && list.size() > 0) 
				result = (PromotionImages)list.get(0);
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		return result;

	}
	
	public PromotionImages getActivePromotionImagesByFriendlyId(String siteName, String friendlyId, Date startDate) {
		
		if(StringUtils.isNullOrEmpty(friendlyId))
			return null;
		
		String promoQuery = 
				getPromotionSelectClause()
				+ "from promotion " 
				+ getPromotionWhereClause()
				+ "and FRIENDLY_ID = :friendlyId ";
		
		PromotionImages result = null;
		Session session = getSession();
		
		try{
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.addEntity(PromotionImages.class);
			query.setString("website", siteName);
			query.setTimestamp("startDate", startDate);
			query.setString("friendlyId", friendlyId);
			
			List<PromotionImages> list =query.list();
			if(list != null && list.size() > 0) 
				result = (PromotionImages)list.get(0);
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		return result;
	}
	
	private String getPromotionSelectClause(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT /*+ index (promotion promotion_i1) */ ");
		sb.append("PROMOTION_ID, START_DATE, EXPIRATION_DATE, DESCRIPTION, TIRE_ID, TIRE_ID_2, ");
		sb.append("SUV_DISPLAY, URL, DEFAULT_SIZE, NEW_WINDOW, MAINT_OFFER, REPAIR_OFFER, TIRE_OFFER, HOME_PAGE_OFFER, WIDTH, ");
		sb.append("HEIGHT, ORDER_ID, TARGET, CREATED_DATE, PROMO_TYPE, null as IMAGE, null as THUMBNAIL, ");
		sb.append("null as FLASH_ICON, WEB_SITE, LANDING_PAGE_ID, null as LANDING_PAGE_ICON, ");
		sb.append("PRICE_INFO, FRIENDLY_ID, DISCLAIMER, MODIFIED_DATE, OFFER_START, OFFER_END, OFFER_DESCRIPTION, APPROVED,IMAGE_FILE_ID,URL_TEXT, ");
		sb.append("T5_TITLE, T5_PRICE, T5_DESCRIPTION, OFFER_WITHOUT_PRICE ");
		return sb.toString();
	}
	
	private String getPromotionWhereClause(){
		StringBuffer sb = new StringBuffer();
		sb.append("where web_site = :website and start_date < :startDate ");
		sb.append("and (expiration_date > :startDate or expiration_date is null) ");
		return sb.toString();
	}
	
	private String getPromotionOrderByClause(){
		StringBuffer sb = new StringBuffer();
		sb.append("order by ORDER_ID asc ");
		return sb.toString();
	}

	public PromotionLandingType findPromotionLandingType(String webSite,String promoName) {
		PromotionLandingType p = new PromotionLandingType();
		p.setWebSite(webSite);
		p.setPromoName(promoName);
		List<PromotionLandingType> l = getHibernateTemplate().findByValueBean("from PromotionLandingType p where p.promoName=:promoName and p.webSite=:webSite", p);
		if(l != null && l.size() > 0) {
			return (PromotionLandingType)l.get(0);
		} else {
			return null;
		}
	}
	
	public List<PromotionLandingType> getPromotionLandingTypes() {
		String promoTypeQuery = "SELECT * FROM PROMOTION_LANDING_TYPE";
		
		List<PromotionLandingType> results = new ArrayList<PromotionLandingType>();
		Session session = getSession();
		try{
			SQLQuery query = session.createSQLQuery(promoTypeQuery);
			query.addEntity(PromotionLandingType.class);
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		return results;
	}

	public Object[] findOilPromotionByPriceType(String friendlyId) {
		if(friendlyId == null)
			return null;			
		String promoQuery = "SELECT * FROM PROMOTION_BUSINESS_RULES WHERE PRODUCT_SUB_TYPE = :friendlyId";	
		List<Object[]> results = new ArrayList<Object[]>();		
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery(promoQuery);
			query.setString("friendlyId", friendlyId);
			results = query.list();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			releaseSession(session);
		}
		Object[] obj = null;
		if(results.size() > 0)
			obj = results.get(0);
		return obj;
	
		
	}
	
	public int updatePromotionDisabled(Long promotionId, boolean disabled){
		String expirationDate = "OFFER_END + 86399/86400 ";
		if (disabled) {
			expirationDate = "trunc(sysdate) - 1/86400 ";
		}

		final String updateString = 
			"update Promotion " +
			"set EXPIRATION_DATE = " + expirationDate +
			"where PROMOTION_ID = :promotionId ";
		Session s = getSession();
		int updatedEntities = s.createQuery( updateString )
		        .setLong( "promotionId", promotionId )
		        .executeUpdate();	
		
		return updatedEntities;
	}

	public int updatePromotionApproved(Long promotionId, boolean approved){
		String startDate = "OFFER_START, ";
		if (!approved) {
			if (ServerUtil.isProduction()) {
				startDate = "trunc(add_months(sysdate,120)),";
			} else {
				startDate = "trunc(sysdate), ";
			}
		}

		final String updateString = 
			"update Promotion " +
			"set START_DATE = " + startDate +
			"    MODIFIED_DATE = sysdate, " +
			"    APPROVED = :approved " +
			"where PROMOTION_ID = :promotionId ";
		Session s = getSession();
		int updatedEntities = s.createQuery( updateString )
				.setBoolean( "approved", approved )
				.setLong( "promotionId", promotionId )
		        .executeUpdate();	
		
		return updatedEntities;
	}
	
	public List<String> getPromotionImageFileIds(){
		List<String> ids = new ArrayList<String>();;
		
		String selectString = 
				"select VALUE from BFS_TIRE_CATALOG_JDA.DISPLAY " +
				"union " + 
				"select VALUE from RTMS_WEBDB.PROMOTION_DISPLAY " +
				"order by VALUE ";
		Session s = getSession();
		try {			
			SQLQuery q = s.createSQLQuery( selectString );			
			ids = q.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(s);
		}
		
		return ids;
	}

	public int updatePromotionStartDate(Long promotionId, String startDate){
		if (startDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
			startDate = "to_date('" + startDate + "','MM/DD/YYYY'),";
		} else {
			startDate = "OFFER_START, ";
		}
		
		final String updateString = 
			"update Promotion " +
			"set START_DATE = " + startDate +
			"    MODIFIED_DATE = sysdate " +
			"where PROMOTION_ID = :promotionId ";
		Session s = getSession();
		int updatedEntities = s.createQuery( updateString )
				.setLong( "promotionId", promotionId )
		        .executeUpdate();	
		
		return updatedEntities;
	}
	

	public Map<Long, PromotionBrand> getPromotionBrandsDetails() {
		String promoTypeQuery = "SELECT * FROM PROMOTION_BRAND";
		Map<Long,PromotionBrand> promotionBrandDetail = new HashMap<Long, PromotionBrand>();
		PromotionBrand  promotionBrand = null;
		
		List<PromotionBrand> results = new ArrayList<PromotionBrand>();
		Session session = getSession();
		try{
			SQLQuery query = session.createSQLQuery(promoTypeQuery);
			query.addEntity(PromotionBrand.class);
			results = query.list();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			releaseSession(session);
		}
		
		 for (int i=0; i<results.size(); i++) {
			 promotionBrand = (PromotionBrand) results.get(i);
			 promotionBrandDetail.put(promotionBrand.getBrandId(), promotionBrand);
		 }
	
		return promotionBrandDetail;
	}

}
