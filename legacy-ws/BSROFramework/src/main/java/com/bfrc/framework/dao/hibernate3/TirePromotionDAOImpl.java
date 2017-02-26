package com.bfrc.framework.dao.hibernate3;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.TirePromotionDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.tirepromotion.SourcePromotionType;
import com.bfrc.pojo.tirepromotion.TirePromotionArticle;
import com.bfrc.pojo.tirepromotion.TirePromotionDisplay;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;
import com.bfrc.pojo.tirepromotion.TirePromotionSite;
import com.bfrc.pojo.tirepromotion.TirePromotionSiteId;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class TirePromotionDAOImpl extends HibernateDAOImpl implements TirePromotionDAO {

	private PlatformTransactionManager txManager;
	
	public TirePromotionEvent getTirePromotion(Object id) {
		if(id == null) return null;
		long longId = Long.parseLong(id.toString());
		return (TirePromotionEvent)getHibernateTemplate().get(TirePromotionEvent.class,new Long(longId));

	}
	public List getTirePromotionArticles(Object id){
		if(id == null) return null;
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getTirePromotionArticlesByPromoId", "promoId",id.toString());
		
	}
	
	public List getTirePromotionStores(Object id){
		if(id == null) return null;
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getTirePromotionStoresByPromoId", "promoId",id.toString());
		
	}
	
	public List getTirePromotionSites(Object id){
		if(id == null) return null;
		return getHibernateTemplate().find("from TirePromotionSite p where id.promoId = ?", id);
		
	}
	
	public List getTirePromotionTypes(){
		return getHibernateTemplate().loadAll(SourcePromotionType.class);
	}
		
	public List getAllTirePromotionsWithoutBlobs(){
		return getHibernateTemplate().findByNamedQuery("getTirePromotionEventsWithoutBlobs");
	}
	
	public List getAllTirePromotions(){
		return getHibernateTemplate().find("from TirePromotionEvent p order by p.promoId desc");
	}
	
	public List<TirePromotionDisplay> getTirePromotionDisplayInfo(){
		final String queryString = " SELECT " +
               "tpEvent.PROMO_ID as promoId, tpEvent.PROMO_NAME as promoName, tpEvent.STATUS_FLAG as statusFlag, " + 
               "(select min(Start_Date) from tire_promotion.TIRE_PROMOTION_LOCATION where promo_id = tpEvent.promo_id) as startDate, " +
               "(select max(end_date) from tire_promotion.TIRE_PROMOTION_LOCATION where promo_id = tpEvent.promo_id) as endDate " +
            "FROM  tire_promotion.TIRE_PROMOTION_EVENT tpEvent order by promoId desc";
		
		@SuppressWarnings("unchecked")
		List<TirePromotionDisplay> tpDisplays = (List<TirePromotionDisplay>) getHibernateTemplate().execute(new HibernateCallback(){

			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(queryString);
				query.addScalar("promoId", Hibernate.LONG)
				.addScalar("promoName", Hibernate.STRING)
				.addScalar("statusFlag", Hibernate.CHARACTER)
				.addScalar("startDate", Hibernate.DATE)
				.addScalar("endDate", Hibernate.DATE)
				.setResultTransformer(Transformers.aliasToBean(TirePromotionDisplay.class));			
				
				return query.list();
			}
			
		});		
		
		return tpDisplays;
	}
	
	public List<TirePromotionDisplay> getTirePromotionDisplayInfo(final int start,final int maxResults){
		final String queryString = " SELECT " +
               "tpEvent.PROMO_ID as promoId, tpEvent.PROMO_NAME as promoName, tpEvent.STATUS_FLAG as statusFlag, " + 
               "(select min(Start_Date) from tire_promotion.TIRE_PROMOTION_LOCATION where promo_id = tpEvent.promo_id) as startDate, " +
               "(select max(end_date) from tire_promotion.TIRE_PROMOTION_LOCATION where promo_id = tpEvent.promo_id) as endDate " +
            "FROM  tire_promotion.TIRE_PROMOTION_EVENT tpEvent order by promoId desc";
		
		@SuppressWarnings("unchecked")
		List<TirePromotionDisplay> tpDisplays = (List<TirePromotionDisplay>) getHibernateTemplate().execute(new HibernateCallback(){

			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(queryString);
				query.addScalar("promoId", Hibernate.LONG)
				.addScalar("promoName", Hibernate.STRING)
				.addScalar("statusFlag", Hibernate.CHARACTER)
				.addScalar("startDate", Hibernate.DATE)
				.addScalar("endDate", Hibernate.DATE)
				.setResultTransformer(Transformers.aliasToBean(TirePromotionDisplay.class));
				
				query.setFirstResult(start);
				query.setMaxResults(maxResults);
				
				return query.list();
			}
			
		});		
		
		return tpDisplays;
	}
	
	public List getAllTirePromotionsBySiteAndStatus(String siteName, String statusFlag){
		String[] names = new String[]{"siteName","statusFlag"};
		String[] values = new String[]{siteName,statusFlag};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getTirePromotionsBySiteAndStatus", names, values);

	}
	
	public List getPublishedTirePromotionsBySite(String siteName){
		return getAllTirePromotionsBySiteAndStatus(siteName,"P");
	}
	
	public List<TirePromotionEvent> getPublishedTirePromotionsBySite(String siteName, Date startDate){
		return getActiveTirePromotionsBySiteAndStatus(siteName,"P",startDate);
	}
	
	public List<TirePromotionEvent> getActiveTirePromotionsBySiteAndStatus(String siteName, String statusFlag, Date startDate){
		String[] names = new String[]{"siteName","statusFlag","startDate"};
		Object[] values = new Object[]{siteName,statusFlag, startDate};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getActiveTirePromotionsBySiteAndStatus", names, values);

	}
	
	public void createTirePromotion(TirePromotionEvent p) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().save(p);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("TirePromotionDAO could not create promotion ");
			if(p != null)
				System.err.print(p.toString());
			System.err.println();
		    throw ex;
		}
	}
	
	public void updateTirePromotion(TirePromotionEvent p) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
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

	public void deleteTirePromotion(Object id) {
		getHibernateTemplate().delete(getTirePromotion(id));
	}
	
	public SourcePromotionType getTirePromotionType(Object id){
		if(id == null) return null;
		java.lang.Character ch= new  Character(id.toString().charAt(0));
		return (SourcePromotionType)getHibernateTemplate().get(SourcePromotionType.class,ch);
	}
	
	
	public void createTirePromotionSite(TirePromotionSite p) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().save(p);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("TirePromotionDAO could not create promotion site ");
			if(p != null)
				System.err.print(p.toString());
			System.err.println();
		    throw ex;
		}
	}
	
	public void createTirePromotionSites(List ps) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			
			//for(java.util.Iterator it = ps.iterator(); it.hasNext();){
			//	TirePromotionSite p = (TirePromotionSite)it.next();
			//    getHibernateTemplate().save(p);
			//}
			getHibernateTemplate().saveOrUpdateAll(ps);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("TirePromotionDAO could not create promotion site ");

		    throw ex;
		}
	}
	
	
	public void updateTirePromotionSite(TirePromotionSite p) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().update(p);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PromotionDAO could not update promotion site");
			if(p != null)
				System.err.print(p.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void deleteTirePromotionSite(Object id) {
		TirePromotionSiteId tid = ((TirePromotionSite)id).getId();
		getHibernateTemplate().delete(getTirePromotionSite(tid));
	}
	
	public TirePromotionSite getTirePromotionSite(Object id) {
		if(id == null) return null;
		return (TirePromotionSite)getHibernateTemplate().get(TirePromotionSite.class,(TirePromotionSiteId)id);

	}
	
	public Map getTirePromotionStatusFlags(){
		Map promoFlags = new LinkedHashMap();
		promoFlags.put("I", "Inactive");
		promoFlags.put("A", "Approved");
		promoFlags.put("P", "Published");
		return promoFlags;
	}
	
	public double getDiscountAmountByStoreNumberMinQtyPromoId(long article, long storeNumber, byte minQty, long promoId){
		String[] names = new String[]{"article","storeNumber","minQty","promoId"};
		String[] values = new String[]{String.valueOf(article),
				                       String.valueOf(storeNumber),
				                       String.valueOf(minQty),
				                       String.valueOf(promoId)};
		
		List r = getHibernateTemplate().findByNamedQueryAndNamedParam("getDiscountAmountByStoreNumberMinQtyPromoId", names, values);
		if(r != null && r.size() >0){
			Object[] vals = (Object[])r.iterator().next();
			return Double.parseDouble(vals[0].toString());
		}
	    return 0.0d;
		
	}
	
	public List getTirePromotionsBySitesAndStoreNumber(String siteName, long storeNumber){
		String[] names = new String[]{"siteName","storeNumber"};
		String[] values = new String[]{siteName,String.valueOf(storeNumber)};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getTirePromotionsBySitesAndStoreNumber", names, values);
	}
	
	public List getTirePromotionsBySitesAndStoreNumberWithoutBlobs(String siteName, long storeNumber){
		String[] names = new String[]{"siteName","storeNumber"};
		String[] values = new String[]{siteName,String.valueOf(storeNumber)};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getTirePromotionsBySitesAndStoreNumberWithoutBlobs", names, values);
	}
	
	public Map loadTirePromotionData(String siteName, long storeNumber){
		//in article --> promo ids format
		//i.e 2043->10000000|10000001
		Map data = new HashMap();
		List l = getTirePromotionsBySitesAndStoreNumberWithoutBlobs(siteName,storeNumber);
		if(l != null && l.size() > 0){
			for(Iterator it = l.iterator(); it.hasNext();){
			    TirePromotionEvent promo = (TirePromotionEvent)it.next();
			    String  promoId = String.valueOf(promo.getPromoId());
			    List a  = getTirePromotionArticles(new Long(promoId));
			    if(a != null && a.size() > 0){
					for(Iterator ita = a.iterator(); ita.hasNext();){
						TirePromotionArticle art = (TirePromotionArticle)ita.next();
						String articleNum = String.valueOf(art.getId().getArticle());
						if(data.get(articleNum) == null){
						    data.put(String.valueOf(articleNum), String.valueOf(promoId));
						}else{
							String prevStr = (String)data.get(articleNum);
							data.put(String.valueOf(articleNum), prevStr+"|"+String.valueOf(promoId));
						}
					}
					data.put("PROMO_"+promoId,promo);
					
			    }
			    
			}
		}
		return data;
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
	
	public List<TirePromotionEvent> getSpecialTirePromotionEvent(String siteName, long storeNumber, List<Long> articleIds){
		String[] names = new String[]{"siteName","storeNumber","articleIds"};
		Object[] values = new Object[]{siteName, storeNumber, articleIds};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getSpecialTirePromotionEvent", names, values);
	}

	public TirePromotionEvent getTirePromotionByFriendlyId(String promoName) {
		List l = getHibernateTemplate().find("from TirePromotionEvent p where p.promoName = ?", promoName);
		if(l != null && l.size() > 0) {
			return (TirePromotionEvent)l.get(0);
		} else {
			return null;
		}

	}

	public int updateTirePromotionStatus(String promoName, char statusFlag) {
		if ((ServerUtil.isProduction() && statusFlag == '1') || (!ServerUtil.isProduction() && statusFlag == 'A')) {
			statusFlag = 'P';
		}

		final String updateString =
			"update TirePromotionEvent " +
			"set STATUS_FLAG = :statusFlag " +
			"where PROMO_NAME = :promoName ";
		Session s = getSession();
		int updatedEntities = s.createQuery( updateString )
			.setCharacter( "statusFlag", statusFlag )
			.setString( "promoName", promoName )
	        .executeUpdate();
		
		return updatedEntities;
	}	

}
