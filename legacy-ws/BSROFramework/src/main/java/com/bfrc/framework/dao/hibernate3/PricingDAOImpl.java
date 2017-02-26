package com.bfrc.framework.dao.hibernate3;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.bfrc.Config;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.StoreScheduleDAO;
import com.bfrc.framework.dao.pricing.Price;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.CacheService;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.admin.AppCacheController;
import com.bfrc.pojo.alignment.AlignmentPricing;
import com.bfrc.pojo.alignment.AlignmentPricingQuote;
import com.bfrc.pojo.pricing.TpArticleLog;
import com.bfrc.pojo.pricing.TpHierarchyPrice;
import com.bfrc.pojo.pricing.TpHierarchyPriceId;
import com.bfrc.pojo.pricing.TpRequestLog;
import com.bfrc.pojo.pricing.TpStoreList;
import com.bfrc.pojo.pricing.TpSuppressionPbidSku;
import com.bfrc.pojo.pricing.TpSuppressionDisplayVehType;
import com.bfrc.pojo.pricing.TpSuppressionDisplayVehId;
import com.bfrc.pojo.pricing.TpTireMaster;
import com.bfrc.pojo.pricing.TpTpmsPrice;
import com.bfrc.pojo.pricing.TpUserLog;
import com.bfrc.pojo.pricing.MobileTireInstallFee;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.Fitment;
import com.bfrc.pojo.tire.Tire;

public class PricingDAOImpl extends HibernateDAOImpl implements PricingDAO {

	public List findTpTireFeesByStoreNumber(Long storeNumber) {
		if(storeNumber == null)
			return null;
		List l = getHibernateTemplate().findByNamedParam("from TpTireFee a where a.storeNumber=:storeNumber",
				"storeNumber",
				storeNumber);
		
		return l;
	}
	
	public TpArticleLog findTpArticleLogById(Long tpArticleId) {
		if(tpArticleId == null) return null;
		return (TpArticleLog)getHibernateTemplate().get(TpArticleLog.class,tpArticleId);
	}
	
	public TpUserLog findTpUserLogByUserId(Long id) {
		if(id == null) return null;
		List l = getHibernateTemplate().findByNamedParam("from TpUserLog a where a.tpUserId=:tpUserId",
				"tpUserId",
				id);
		
		return (l == null || l.size() == 0) ? null : (TpUserLog)l.iterator().next();
		
	}
	
	public long logGetProducts(Long store, UserVehicle vehicle) {
		long id = -1;
		id = ((Long)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT TP_USER_SEQ.NEXTVAL AS USER_ID FROM DUAL";
				SQLQuery query = session.createSQLQuery(sql);
				query.addScalar("USER_ID", Hibernate.LONG);
				return query.uniqueResult();
			}
		})).longValue();
		TpRequestLog log = new TpRequestLog();
		log.setTpUserId(id);
		log.setStoreNumber(store.longValue());
		log.setModelYear(vehicle.getYear());
		log.setMakeName(vehicle.getMake());
		log.setModelName(vehicle.getModel());
		log.setSubmodel(vehicle.getSubmodel());
		log.setRequestDate(new java.util.Date());
		getHibernateTemplate().save(log);
		return id;
	}

	public Long logGetQuote(Long userId, Long storeNumber, List list, UserVehicle vehicle, String article, String rearArticle, String qty, String firstName, String lastName) {
		long id = -1;
		TpUserLog userLog =  null;
		boolean alreadyUserLog = false;
		if(userId != null && userId.longValue() > 0){
			userLog =  findTpUserLogByUserId(id);
			if(userLog != null)
				alreadyUserLog = true;
		}
		if(!alreadyUserLog) {
			userLog = new TpUserLog();
			try{
			    userLog.setQuantity(Long.parseLong(qty));
			}catch(Exception ex){
				
			}
			if(!StringUtils.isNullOrEmpty(firstName))
			    userLog.setFirstName(firstName);
			if(!StringUtils.isNullOrEmpty(lastName))
			    userLog.setLastName(lastName);
			if(!StringUtils.isNullOrEmpty(lastName))
			    userLog.setLastName(lastName);
			if(storeNumber != null && storeNumber.intValue() > 0)
			    userLog.setStoreNumber(storeNumber.intValue());
			if(vehicle != null){
				if(!StringUtils.isNullOrEmpty(vehicle.getYear()))
				    userLog.setModelYear(vehicle.getYear());
				if(!StringUtils.isNullOrEmpty(vehicle.getMake()))
				    userLog.setMakeName(vehicle.getMake());
				if(!StringUtils.isNullOrEmpty(vehicle.getModel()))
				    userLog.setModelName(vehicle.getModel());
				if(!StringUtils.isNullOrEmpty(vehicle.getSubmodel()))
				    userLog.setSubmodel(vehicle.getSubmodel());
			}
			userLog.setRequestDate(new Date());
			userLog.setWebSite(getConfig().getSiteName());
			getHibernateTemplate().save(userLog);
			id = userLog.getTpUserId();
			
		}
		if(article != null && article.indexOf("_") > 0){
			article = article.substring(article.indexOf("_"));
		}
		for (int i = 0; i < list.size(); i++) {
			Tire tp = (Tire) list.get(i);
			if (!String.valueOf(tp.getArticle()).equals(article))
				continue;
			TpArticleLog articleLog = new TpArticleLog();
			MobileTireInstallFee mobileTireInstallFee = findMobileTireInstallFeeByStoreNumber(storeNumber);
			articleLog.setArticleNumber(tp.getArticle());
			articleLog.setTpUserId(id);
			if(!StringUtils.isNullOrEmpty(rearArticle))
			    articleLog.setRearArticle(Long.valueOf(rearArticle));
			articleLog.setOnSale(tp.getOnSale());
			articleLog.setRetailPrice(BigDecimal.valueOf(tp.getRetailPrice()));
			articleLog.setBalanceWeight(BigDecimal.valueOf(tp.getWheelBalanceWeight()));
			articleLog.setValveStem(BigDecimal.valueOf(tp.getValveStem()));
			articleLog.setExciseTax(BigDecimal.valueOf(tp.getExciseTax()));
			articleLog.setTireFee(BigDecimal.valueOf(tp.getTireFee()));
			articleLog.setBalanceLabor(BigDecimal.valueOf(tp.getWheelBalanceLabor()));
			if (mobileTireInstallFee != null) {
				articleLog.setInstallFee(mobileTireInstallFee.getTireInstallFee());
			} else {
				articleLog.setInstallFee(BigDecimal.valueOf(tp.getTireInstallPrice()));
			}
			articleLog.setDisposal(BigDecimal.valueOf(tp.getDisposalPrice()));
			getHibernateTemplate().save(articleLog);
			return  new Long(articleLog.getTpArticleId());
			
		}
		return new Long(-1);
	}
	
	public Long logGetQuote(Long userId, Long storeNumber,List list, UserVehicle vehicle, String article, String rearArticle, String qty, String rearQty, String firstName, String lastName){
		return logGetQuote(userId, storeNumber, list, vehicle, article, rearArticle, qty, rearQty, firstName, lastName, null);
	}
	
	public Long logGetQuote(Long userId, Long storeNumber,List list, UserVehicle vehicle, String article, String rearArticle, String qty, String rearQty, String firstName, String lastName, String siteName){
		long id = -1;
		TpUserLog userLog =  null;
		boolean alreadyUserLog = false;
		if(userId != null && userId.longValue() > 0){
			userLog =  findTpUserLogByUserId(id);
			if(userLog != null)
				alreadyUserLog = true;
		}
		if(!alreadyUserLog) {
			userLog = new TpUserLog();
			try{
			    userLog.setQuantity(Long.parseLong(qty));
			    if(!StringUtils.isNullOrEmpty(rearQty))
			    	userLog.setRearQuantity(Long.valueOf(rearQty));
			}catch(Exception ex){
				
			}
			if(!StringUtils.isNullOrEmpty(firstName))
			    userLog.setFirstName(firstName);
			if(!StringUtils.isNullOrEmpty(lastName))
			    userLog.setLastName(lastName);
			if(!StringUtils.isNullOrEmpty(lastName))
			    userLog.setLastName(lastName);
			if(storeNumber != null && storeNumber.intValue() > 0)
			    userLog.setStoreNumber(storeNumber.intValue());
			if(vehicle != null){
				if(!StringUtils.isNullOrEmpty(vehicle.getYear()))
				    userLog.setModelYear(vehicle.getYear());
				if(!StringUtils.isNullOrEmpty(vehicle.getMake()))
				    userLog.setMakeName(vehicle.getMake());
				if(!StringUtils.isNullOrEmpty(vehicle.getModel()))
				    userLog.setModelName(vehicle.getModel());
				if(!StringUtils.isNullOrEmpty(vehicle.getSubmodel()))
				    userLog.setSubmodel(vehicle.getSubmodel());
			}
			userLog.setRequestDate(new Date());
			
			if(getConfig() != null && !StringUtils.isNullOrEmpty(getConfig().getSiteName()))
				userLog.setWebSite(getConfig().getSiteName());
			else if(!StringUtils.isNullOrEmpty(siteName))
				userLog.setWebSite(siteName);
			
			getHibernateTemplate().save(userLog);
			id = userLog.getTpUserId();
			
		}
		if(article != null && article.indexOf("_") > 0){
			article = article.substring(article.indexOf("_"));
		}
		for (int i = 0; i < list.size(); i++) {
			Tire tp = (Tire) list.get(i);
			if (!String.valueOf(tp.getArticle()).equals(article))
				continue;
			TpArticleLog articleLog = new TpArticleLog();
			MobileTireInstallFee mobileTireInstallFee = findMobileTireInstallFeeByStoreNumber(storeNumber);
			articleLog.setArticleNumber(tp.getArticle());
			articleLog.setTpUserId(id);
			if(!StringUtils.isNullOrEmpty(rearArticle))
			    articleLog.setRearArticle(Long.valueOf(rearArticle));
			articleLog.setOnSale(tp.getOnSale());
			articleLog.setRetailPrice(BigDecimal.valueOf(tp.getRetailPrice()));
			articleLog.setBalanceWeight(BigDecimal.valueOf(tp.getWheelBalanceWeight()));
			articleLog.setValveStem(BigDecimal.valueOf(tp.getValveStem()));
			articleLog.setExciseTax(BigDecimal.valueOf(tp.getExciseTax()));
			articleLog.setTireFee(BigDecimal.valueOf(tp.getTireFee()));
			articleLog.setBalanceLabor(BigDecimal.valueOf(tp.getWheelBalanceLabor()));
			if (mobileTireInstallFee != null) {
				articleLog.setInstallFee(mobileTireInstallFee.getTireInstallFee());
			} else {
				articleLog.setInstallFee(BigDecimal.valueOf(tp.getTireInstallPrice()));
			}
			articleLog.setDisposal(BigDecimal.valueOf(tp.getDisposalPrice()));
			getHibernateTemplate().save(articleLog);
			return  new Long(articleLog.getTpArticleId());
			
		}
		return new Long(-1);
	
	}
	
	public Long logGetQuote(Long userId, Long storeNumber, Tire tire, UserVehicle vehicle, String qty, String firstName, String lastName) {
		long id = -1;
		TpUserLog userLog =  null;
		boolean alreadyUserLog = false;
		if(userId != null && userId.longValue() > 0){
			userLog =  findTpUserLogByUserId(id);
			if(userLog != null)
				alreadyUserLog = true;
		}
		if(!alreadyUserLog) {
			userLog = new TpUserLog();
			try{
			    userLog.setQuantity(Long.parseLong(qty));
			}catch(Exception ex){
				
			}
			if(!StringUtils.isNullOrEmpty(firstName))
			    userLog.setFirstName(firstName);
			if(!StringUtils.isNullOrEmpty(lastName))
			    userLog.setLastName(lastName);
			if(!StringUtils.isNullOrEmpty(lastName))
			    userLog.setLastName(lastName);
			if(storeNumber != null && storeNumber.intValue() > 0)
			    userLog.setStoreNumber(storeNumber.intValue());
			if(vehicle != null){
				if(!StringUtils.isNullOrEmpty(vehicle.getYear()))
				    userLog.setModelYear(vehicle.getYear());
				if(!StringUtils.isNullOrEmpty(vehicle.getMake()))
				    userLog.setMakeName(vehicle.getMake());
				if(!StringUtils.isNullOrEmpty(vehicle.getModel()))
				    userLog.setModelName(vehicle.getModel());
				if(!StringUtils.isNullOrEmpty(vehicle.getSubmodel()))
				    userLog.setSubmodel(vehicle.getSubmodel());
			}
			userLog.setRequestDate(new Date());
			userLog.setWebSite(getConfig().getSiteName());
			getHibernateTemplate().save(userLog);
			id = userLog.getTpUserId();
			
		}
		if(tire != null){
			TpArticleLog articleLog = new TpArticleLog();
			MobileTireInstallFee mobileTireInstallFee = findMobileTireInstallFeeByStoreNumber(storeNumber);
			articleLog.setArticleNumber(tire.getArticle());
			articleLog.setTpUserId(id);
			if(tire.getRearArticle() > 0)
			    articleLog.setRearArticle(Long.valueOf(tire.getRearArticle()));
			articleLog.setOnSale(tire.getOnSale());
			articleLog.setRetailPrice(BigDecimal.valueOf(tire.getRetailPrice()));
			articleLog.setBalanceWeight(BigDecimal.valueOf(tire.getWheelBalanceWeight()));
			articleLog.setValveStem(BigDecimal.valueOf(tire.getValveStem()));
			articleLog.setExciseTax(BigDecimal.valueOf(tire.getExciseTax()));
			articleLog.setTireFee(BigDecimal.valueOf(tire.getTireFee()));
			articleLog.setBalanceLabor(BigDecimal.valueOf(tire.getWheelBalanceLabor()));
			if (mobileTireInstallFee != null) {
				articleLog.setInstallFee(mobileTireInstallFee.getTireInstallFee());
			} else {
				articleLog.setInstallFee(BigDecimal.valueOf(tire.getTireInstallPrice()));
			}
			articleLog.setDisposal(BigDecimal.valueOf(tire.getDisposalPrice()));
			getHibernateTemplate().save(articleLog);
			return  new Long(articleLog.getTpArticleId());
			
		}
		return new Long(-1);
	}
	

	public Price getPrice(int articleNumber, long storeNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getProducts(Fitment f) {
		// TODO Auto-generated method stub
		return null;
	}
	//cxs
	public void createTpArticleLog(TpArticleLog tpArticleLog) {
		getHibernateTemplate().save(tpArticleLog);
	}
	public void updateTpArticleLog(TpArticleLog tpArticleLog) {
		getHibernateTemplate().update(tpArticleLog);
	}
	public void deleteTpArticleLog(TpArticleLog tpArticleLog) {
		getHibernateTemplate().delete(tpArticleLog);
	}
	
//	cxs
	public void createTpUserLog(TpUserLog tpUserLog) {
		getHibernateTemplate().save(tpUserLog);
	}
	public void updateTpUserLog(TpUserLog tpUserLog) {
		getHibernateTemplate().update(tpUserLog);
	}
	public void deleteTpUserLog(TpUserLog tpUserLog) {
		getHibernateTemplate().delete(tpUserLog);
	}
	
	public TpStoreList findTpStoreListByStoreNumber(Object storeNumber){
		
		if(storeNumber == null)
			return null;
		String[] names = {"storeNumber"};
		Object[] values = {storeNumber};
		List l=  getHibernateTemplate().findByNamedQueryAndNamedParam("FindTpStoreListByStoreNumber", names, values);
		if(l != null && !l.isEmpty() ) return (TpStoreList)l.iterator().next();
		return null;
	}
	public TpTireMaster findTpTireMasterByArticle(Object article){
		
		if(article == null) return null;
		return (TpTireMaster)getHibernateTemplate().get(TpTireMaster.class,new Long(article.toString()));
	}
	public TpHierarchyPrice findTpHierarchyPriceByBookIdAndLine(Object bookId, Object line){
		if(bookId == null || line == null) return null;
		TpHierarchyPriceId id = new TpHierarchyPriceId(Long.parseLong(bookId.toString()),Long.parseLong(line.toString()));
		return (TpHierarchyPrice)getHibernateTemplate().get(TpHierarchyPrice.class,id);
	}
	
    public TpHierarchyPrice findTpHierarchyPriceByStoreNumberAndArticle(Object storeNumber, Object article){
		
		if(storeNumber == null)
			return null;
		String[] names = {"storeNumber","article"};
		Object[] values = {storeNumber,article};
		List l=  getHibernateTemplate().findByNamedQueryAndNamedParam("FindTpHierarchyPriceByStoreNumberAndArticle", names, values);
		if(l != null && l.size() > 0) return (TpHierarchyPrice)l.iterator().next();
		return null;
	}
    
    public TpTpmsPrice getVehicleTpmsVSK(Long storeNumber){
    	return getVehicleTpmsPrice("P", storeNumber);
    }
    
    public TpTpmsPrice getVehicleTpmsVSK(String year, String make, String model, String submodel, Long storeNumber){
    	return getVehicleTpmsPrice(year,make,model,submodel,"P", storeNumber);
    }
    
    public TpTpmsPrice getVehicleTpmsVSKLabor(Long storeNumber){
    	return getVehicleTpmsPrice("L",storeNumber);
    }
	
    public TpTpmsPrice getVehicleTpmsVSKLabor(String year, String make, String model, String submodel, Long storeNumber){
    	return getVehicleTpmsPrice(year,make,model,submodel,"L",storeNumber);
    }
    
    public TpTpmsPrice getVehicleTpmsPrice(String priceType, Long storeNumber)
    {
    	Long priceBookId = new Long(-1);
    	TpStoreList tpStore = findTpStoreListByStoreNumber(storeNumber);
    	if ((tpStore != null) && (tpStore.getId() != null)) {
    		priceBookId = Long.valueOf(tpStore.getId().getPriceBookId());
    	}
    	List pl = getHibernateTemplate().find("from TpTpmsPrice a where a.priceType='" + priceType + "' and (a.priceBookId = " + priceBookId + " or a.priceBookId IS NULL) order by a.modelYear, a.makeName, a.modelName, a.submodel");

    	TpTpmsPrice normalTpmsPrice = null;
    	boolean priceBookIdMatchFound = false;
    	if (priceBookId == null) {
    		return null;
    	}
    	for (int i = 0; i < pl.size(); i++) {
    		TpTpmsPrice item = (TpTpmsPrice)pl.get(i);
    		if ("N".equals(item.getExceptionFlag()))
    		{
    			if (!priceBookIdMatchFound) {
    				if ((item.getPriceBookId() != null) && (item.getPriceBookId().longValue() == priceBookId.longValue())) {
    					normalTpmsPrice = item;
    					priceBookIdMatchFound = true;
    				} else {
    					normalTpmsPrice = item;
    				}
    			}
    		}
    	}

    	return normalTpmsPrice;
    }
    
    public TpTpmsPrice getVehicleTpmsPrice(String year, String make, String model, String submodel, String priceType, Long storeNumber)
    {
    	Long priceBookId = new Long(-1);
    	TpStoreList tpStore = findTpStoreListByStoreNumber(storeNumber);
    	if ((tpStore != null) && (tpStore.getId() != null)) {
    		priceBookId = Long.valueOf(tpStore.getId().getPriceBookId());
    	}
    	List pl = getHibernateTemplate().find("from TpTpmsPrice a where a.priceType='" + priceType + "' and (a.priceBookId = " + priceBookId + " or a.priceBookId IS NULL) order by a.modelYear, a.makeName, a.modelName, a.submodel");

    	String tpmsYear = null;
    	String tpmsMake = null;
    	String tpmsModel = null;
    	String tpmsSubmodel = null;
    	TpTpmsPrice normalTpmsPrice = null;
    	boolean priceBookIdMatchFound = false;
    	boolean exceptionCase = false;
    	if ((year == null) || (model == null) || (model == null) || (submodel == null) || (priceBookId == null)) {
    		return null;
    	}
    	for (int i = 0; i < pl.size(); i++) {
    		TpTpmsPrice item = (TpTpmsPrice)pl.get(i);
    		tpmsYear = item.getModelYear();
    		tpmsMake = item.getMakeName();
    		tpmsModel = item.getModelName();
    		tpmsSubmodel = item.getSubmodel();
    		if ("N".equals(item.getExceptionFlag()))
    		{
    			if ((!priceBookIdMatchFound) && (!exceptionCase)) {
    				if ((item.getPriceBookId() != null) && (item.getPriceBookId().longValue() == priceBookId.longValue())) {
    					normalTpmsPrice = item;
    					priceBookIdMatchFound = true;
    				} else {
    					normalTpmsPrice = item;
    				}
    			}
    		}
    		else if (((StringUtils.isNullOrEmpty(tpmsYear)) || ((!StringUtils.isNullOrEmpty(tpmsYear)) && (year.equalsIgnoreCase(tpmsYear)))) && 
    				((StringUtils.isNullOrEmpty(tpmsMake)) || ((!StringUtils.isNullOrEmpty(tpmsMake)) && (make.equalsIgnoreCase(tpmsMake)))) && 
    				((StringUtils.isNullOrEmpty(tpmsModel)) || ((!StringUtils.isNullOrEmpty(tpmsModel)) && (model.equalsIgnoreCase(tpmsModel)))) && (
    						(StringUtils.isNullOrEmpty(tpmsSubmodel)) || ((!StringUtils.isNullOrEmpty(tpmsSubmodel)) && (submodel.equalsIgnoreCase(tpmsSubmodel))))) {
    			exceptionCase = true;
    			normalTpmsPrice = item;
    		}

    	}

    	return normalTpmsPrice;
    }
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function check if this vehicle is in the exclusion list  :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public boolean isAlignmentPricingExclusion(Object year, String make, String model){
    	if(year == null)
    		year="";
    	StringBuffer hql = new StringBuffer();
    	hql.append("from AlignmentExclusion a ");
    	hql.append(" where  ( a.vehicleMake=:make and a.vehicleModel is null and  a.vehicleYear is null) ");
    	hql.append(" or  ( a.vehicleMake is null and a.vehicleModel= :model and  a.vehicleYear is null) ");
    	hql.append(" or  ( a.vehicleMake is null and a.vehicleModel is null and  a.vehicleYear=:year) ");
    	hql.append(" or  ( a.vehicleMake=:make and a.vehicleModel=:model  and  a.vehicleYear is null) ");
    	hql.append(" or  ( a.vehicleMake=:make and a.vehicleModel is null and  a.vehicleYear=:year) ");
    	hql.append(" or  ( a.vehicleMake is null and a.vehicleModel=:model and  a.vehicleYear = :year) ");
    	hql.append(" or  ( a.vehicleMake=:make and a.vehicleModel=:model and  a.vehicleYear=:year) ");
    	String[] paramNames = new String[]{"year","make","model"};
    	Object[] values = new Object[]{Short.valueOf(year.toString()),make,model};
    	List l = getHibernateTemplate().findByNamedParam(hql.toString(), paramNames, values);
    	return (l!= null && l.size()>0);
    }
    
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function get AlignmentPricing                            :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    Store findStoreById(Long storeNumber) {
		if(storeNumber == null)
			return null;
		List l = getHibernateTemplate().findByNamedParam("from Store s where s.storeNumber=:storeNumber",
				"storeNumber",
				storeNumber);
		if(l != null && l.size() > 0)
		    return (Store)l.get(0);
		return null;
	}
    
    public AlignmentPricing getAlignmentPricing(Object storeNumber){
    	//-- store number for phase 2 --//
    	if(storeNumber == null)
    		return null;
    	Store store = findStoreById(Long.valueOf(storeNumber.toString()));
    	String storeType = store.getStoreType();
    	if(store == null)
    		return null;
        String storeWebSourceType = Config.getStoreWebSource(store);
        //for(int i = 0; i< 4- storeType.length(); i++){
        //	storeType += " ";
        //}
        /*
         * List l = getHibernateTemplate().find("from AlignmentPricing ap where ap.storeType = ? and ap.zip = ?", new Object[]{storeType, zip});
    	if(l == null || l.size() == 0){
    	    l = getHibernateTemplate().find("from AlignmentPricing ap where ap.storeType = ?", storeType);
    	}
         */
        String district = store.getDistrict().getDistrictId();
        List l = getHibernateTemplate().find("from AlignmentPricing ap where ap.storeType = '"+storeType+"' and ap.district = '"+district+"'");
    	if(l == null || l.size() == 0){
    	    l = getHibernateTemplate().find("from AlignmentPricing ap where ap.storeType = '"+storeType+"' and ap.district is null" );
    	}
    	/*
    	if(l == null || l.size() == 0){
	    	l = getHibernateTemplate().find("from AlignmentPricing ap where ap.storeType = '"+storeType+"' and ap.zip = '"+zip+"'");
	    	if(l == null || l.size() == 0){
	    	    l = getHibernateTemplate().find("from AlignmentPricing ap where ap.storeType = '"+storeType+"'");
	    	}
    	}
    	*/
    	if(l != null && l.size() > 0)
    		return (AlignmentPricing)l.get(0);
    	return null;
    }
    
    
    public Long createAlignmentPricingQuote(AlignmentPricingQuote alignmentQuote)
    {
    	try
    	{
    		if(alignmentQuote != null && !alignmentQuote.equals(""))
    		{
    			getHibernateTemplate().save(alignmentQuote);
    			return  new Long(alignmentQuote.getAlignmentQuoteId());
    		}else
    		{
    		return new Long(-1);
    		}
    	}catch(Exception e)
    	{
    		return new Long(-1);
    	}
    }
  //--- get global suppressed tire articles article is Integer format ---//
    private Map geAllData(){
    	long start = (new Date()).getTime();
    	List l = getHibernateTemplate().find("from TpSuppressionPbidSku tp");
        List globalList = new ArrayList();
        List theOtherList = new ArrayList();
		for(int i=0; l != null && i<l.size(); i++){
	        TpSuppressionPbidSku item = (TpSuppressionPbidSku)l.get(i);
	        if(item.getStoreNumber() == null && item.getPriceBookId() == null){//global skus
	        	globalList.add(item);
	        }else if(item.getPriceBookId() != null || item.getStoreNumber() != null){//only the items that has PB id or store number
	        	theOtherList.add(item);
	        }
		}
		List<Long> globalData = new ArrayList<Long>();
		Map storeData = new HashMap();
		Map<Long, List<String>> skuVehTypeData = new HashMap<Long, List<String>>();
		
		List<Long> dids = new ArrayList<Long>();
		for(int i=0; globalList != null && i<globalList.size(); i++){
			TpSuppressionPbidSku item = (TpSuppressionPbidSku)globalList.get(i);
			if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
				dids.add(item.getDisplayId());
			}else if(item.getSku() != null && item.getSku().intValue() > 0){
				if(!globalData.contains(item.getSku()))
					globalData.add(item.getSku());
			}
			
		}
		if(dids.size() > 0){
			List al = getHibernateTemplate().findByNamedParam("from Configuration2 c where c.displayId in (:displayIds) ",
					"displayIds",
					dids);
			for(int i=0; al != null && i<al.size(); i++){
				com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(i);
				if(!globalData.contains(Long.valueOf(c.getSku())))
					globalData.add(Long.valueOf(c.getSku()));
			}
		}
		Map<Long, List<String>> displayIdVehicleType = new HashMap<Long, List<String>>();
		List vehTypeDisplayData = getHibernateTemplate().find("from TpSuppressionDisplayVehType tp");
		dids = new ArrayList<Long>();
		for(int i=0; vehTypeDisplayData != null && i<vehTypeDisplayData.size(); i++){
			TpSuppressionDisplayVehId item = ((TpSuppressionDisplayVehType)vehTypeDisplayData.get(i)).getId();
			if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
				dids.add(item.getDisplayId());
				if(!displayIdVehicleType.containsKey(item.getDisplayId())){
					List<String> l1 = new ArrayList<String>();
					l1.add(item.getVehicleType());
					displayIdVehicleType.put(item.getDisplayId(), l1);
				} else {
					((List<String>)displayIdVehicleType.get(item.getDisplayId())).add(item.getVehicleType());
				}
			}
		}
		if(displayIdVehicleType.size() > 0){
			List al = getHibernateTemplate().findByNamedParam("from Configuration2 c where c.displayId in (:displayIds) ",
					"displayIds",
					dids);
			for(int i=0; al != null && i<al.size(); i++){
				com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(i);
				if(!skuVehTypeData.containsKey(c.getSku())){
					skuVehTypeData.put(c.getSku(), displayIdVehicleType.get(c.getDisplayId()));
				}
			}
		}
		try{
			
			dids = new ArrayList();
			Map pb_sn = new HashMap();
			Map disp_skus = new HashMap();
			List allPbIds = new ArrayList();
			List allDispIds = new ArrayList();
			for(int i=0; theOtherList != null && i<theOtherList.size(); i++){
				TpSuppressionPbidSku item = (TpSuppressionPbidSku)theOtherList.get(i);
				if(item.getPriceBookId() != null && item.getPriceBookId().longValue() > 0){
					if(!allPbIds.contains(item.getPriceBookId()))
						allPbIds.add(item.getPriceBookId());
					if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
						if(!allDispIds.contains(item.getDisplayId()))
							allDispIds.add(item.getDisplayId());
					}
				}else if(item.getStoreNumber() != null && item.getStoreNumber().longValue() > 0){
					if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
						if(!allDispIds.contains(item.getDisplayId()))
							allDispIds.add(item.getDisplayId());
					}
				}
				
			}
			List al = null;
			if(allDispIds.size() > 0)
			   al = getHibernateTemplate().findByNamedParam("from Configuration2 c where c.displayId in (:displayIds) ",
																"displayIds",
																allDispIds);
			for(int i=0; al != null && i<al.size(); i++){
				com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(i);
				if(!disp_skus.containsKey(Long.valueOf(c.getDisplayId()))){
					List l1 = new ArrayList();
					l1.add(c);
					disp_skus.put(c.getDisplayId(), l1);
				}else{
					((List)disp_skus.get(c.getDisplayId())).add(c);
				}
			}
			al = null;
			if(allPbIds.size() > 0)
			    al = getHibernateTemplate().findByNamedQueryAndNamedParam("getStoreNumbersByPriceBookIds", "priceBookIds", allPbIds);
			for(int i=0; al != null && i<al.size(); i++){
				Object[] c = (Object[])al.get(i);
				Long sn = (Long)c[0];
				Long pid = (Long)c[1];
				if(!pb_sn.containsKey(Long.valueOf(pid))){
					List l2 = new ArrayList();
					l2.add(sn);
					pb_sn.put(pid, l2);
				}else{
					((List)pb_sn.get(pid)).add(sn);
				}
			}
			for(int i=0; theOtherList != null && i<theOtherList.size(); i++){
				TpSuppressionPbidSku item = (TpSuppressionPbidSku)theOtherList.get(i);
				if(item.getPriceBookId() != null && item.getPriceBookId().longValue() > 0){
					//--- get all store numbers ---//
					List skus = new ArrayList();
					List storeNumebrs = (List)pb_sn.get(item.getPriceBookId());
					//List storeNumebrs = getHibernateTemplate().findByNamedQueryAndNamedParam("getStoreNumbersByPriceBookId", "priceBookId", item.getPriceBookId());
					
					if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
						//List al = getHibernateTemplate().find("from Configuration2 c where c.displayId = ? ", item.getDisplayId());
						al = (List)disp_skus.get(item.getDisplayId());
						for(int j=0; al != null && j<al.size(); j++){
							com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(j);
							if(!skus.contains(Long.valueOf(c.getSku())))
								skus.add(Long.valueOf(c.getSku()));
						}
					}else if(item.getSku() != null && item.getSku().intValue() > 0){
						skus.add(item.getSku());
					}
					for (int j=0;storeNumebrs != null && j<storeNumebrs.size();j++) {
						Long sn = (Long)storeNumebrs.get(j);
						if(storeData.get(sn) == null){
							storeData.put(sn,skus);
						}else{
							List l2 = (List)storeData.get(sn);
							l2.addAll(skus);
						}
					}
				}else if(item.getStoreNumber() != null && item.getStoreNumber().longValue() > 0){
					Long sn = item.getStoreNumber();
					List skus = new ArrayList();
					if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
						//List al = getHibernateTemplate().find("from Configuration2 c where c.displayId = ? ", item.getDisplayId());	
						al = (List)disp_skus.get(item.getDisplayId());
						for(int j=0; al != null && j<al.size(); j++){
							com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(j);
							if(!skus.contains(Long.valueOf(c.getSku())))
								skus.add(Long.valueOf(c.getSku()));
						}
					}else if(item.getSku() != null && item.getSku().intValue() > 0){
						skus.add(item.getSku());
					}
					if(storeData.get(sn) == null){
						storeData.put(sn,skus);
					}else{
						List l2 = (List)storeData.get(sn);
						l2.addAll(skus);
					}
				}
				
			}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		
		Map m = new HashMap();
		m.put("globalData", globalData);
		m.put("storeData", storeData);
		m.put("skuVehTypeData", skuVehTypeData);
		long end = (new Date()).getTime();
		Util.debug("\t\t\t--------------took "+(end-start)+" miliseconds to load the data---------------");
		return m;
		
    }
    //--- get global suppressed tire articles article is Integer format ---//
	public List<Long> getGlobalSppressedTireArticlesDB(){
		List<Long> r = new ArrayList<Long>();
		List l = getHibernateTemplate().find("from TpSuppressionPbidSku tp where tp.priceBookId is null and tp.storeNumber is null");
		List<Long> dids = new ArrayList<Long>();
		for(int i=0; l != null && i<l.size(); i++){
			TpSuppressionPbidSku item = (TpSuppressionPbidSku)l.get(i);
			if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
				dids.add(item.getDisplayId());
			}else if(item.getSku() != null && item.getSku().intValue() > 0){
				if(!r.contains(item.getSku()))
				    r.add(item.getSku());
			}
			
		}
		if(dids.size() > 0){
			List al = getHibernateTemplate().findByNamedParam("from Configuration2 c where c.displayId in (:displayIds) ",
					"displayIds",
					dids);
			for(int i=0; al != null && i<al.size(); i++){
				com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(i);
				if(!r.contains(Long.valueOf(c.getSku())))
				    r.add(Long.valueOf(c.getSku()));
			}
		}
		return r;
	}
	//--- get store number mapped tire articles ---//
	public Map getStoreNumberMappedSppressedTireArticlesDB(){
		Map m = new HashMap();
		try{
		List l = getHibernateTemplate().find("from TpSuppressionPbidSku tp where tp.priceBookId is not null or tp.storeNumber is not null");
		List dids = new ArrayList();
		Map pb_sn = new HashMap();
		Map disp_skus = new HashMap();
		List allPbIds = new ArrayList();
		List allDispIds = new ArrayList();
		for(int i=0; l != null && i<l.size(); i++){
			TpSuppressionPbidSku item = (TpSuppressionPbidSku)l.get(i);
			if(item.getPriceBookId() != null && item.getPriceBookId().longValue() > 0){
				if(!allPbIds.contains(item.getPriceBookId()))
					allPbIds.add(item.getPriceBookId());
				if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
					if(!allDispIds.contains(item.getDisplayId()))
						allDispIds.add(item.getDisplayId());
				}
			}else if(item.getStoreNumber() != null && item.getStoreNumber().intValue() > 0){
				if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
					if(!allDispIds.contains(item.getDisplayId()))
						allDispIds.add(item.getDisplayId());
				}
			}
			
		}
		List al = null;
		if(allDispIds.size() > 0)
		   al = getHibernateTemplate().findByNamedParam("from Configuration2 c where c.displayId in (:displayIds) ",
															"displayIds",
															allDispIds);
		for(int i=0; al != null && i<al.size(); i++){
			com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(i);
			if(!disp_skus.containsKey(Long.valueOf(c.getDisplayId()))){
				List l1 = new ArrayList();
				l1.add(c);
				disp_skus.put(c.getDisplayId(), l1);
			}else{
				((List)disp_skus.get(c.getDisplayId())).add(c);
			}
		}
		al = null;
		if(allPbIds.size() > 0)
		    al = getHibernateTemplate().findByNamedQueryAndNamedParam("getStoreNumbersByPriceBookIds", "priceBookIds", allPbIds);
		for(int i=0; al != null && i<al.size(); i++){
			Object[] c = (Object[])al.get(i);
			Integer sn = (Integer)c[0];
			Long pid = (Long)c[1];
			if(!pb_sn.containsKey(Long.valueOf(pid))){
				List l2 = new ArrayList();
				l2.add(sn);
				pb_sn.put(pid, l2);
			}else{
				((List)pb_sn.get(pid)).add(sn);
			}
		}
		for(int i=0; l != null && i<l.size(); i++){
			TpSuppressionPbidSku item = (TpSuppressionPbidSku)l.get(i);
			if(item.getPriceBookId() != null && item.getPriceBookId().longValue() > 0){
				//--- get all store numbers ---//
				List skus = new ArrayList();
				List storeNumebrs = (List)pb_sn.get(item.getPriceBookId());
				//List storeNumebrs = getHibernateTemplate().findByNamedQueryAndNamedParam("getStoreNumbersByPriceBookId", "priceBookId", item.getPriceBookId());
				
				if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
					//List al = getHibernateTemplate().find("from Configuration2 c where c.displayId = ? ", item.getDisplayId());
					al = (List)disp_skus.get(item.getDisplayId());
					for(int j=0; al != null && j<al.size(); j++){
						com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(j);
						if(!skus.contains(Long.valueOf(c.getSku())))
							skus.add(Long.valueOf(c.getSku()));
					}
				}else if(item.getSku() != null && item.getSku().intValue() > 0){
					skus.add(item.getSku());
				}
				for (int j=0;j<storeNumebrs.size();j++) {
					Integer sn = (Integer)storeNumebrs.get(j);
					if(m.get(sn) == null){
						m.put(sn,skus);
					}else{
						List l2 = (List)m.get(sn);
						l2.addAll(skus);
					}
				}
			}else if(item.getStoreNumber() != null && item.getStoreNumber().intValue() > 0){
				Long sn = item.getStoreNumber();
				List skus = new ArrayList();
				if(item.getDisplayId() != null && item.getDisplayId().longValue() > 0){
					//List al = getHibernateTemplate().find("from Configuration2 c where c.displayId = ? ", item.getDisplayId());	
					al = (List)disp_skus.get(item.getDisplayId());
					for(int j=0; al != null && j<al.size(); j++){
						com.bfrc.pojo.tire.jda2.Configuration c = (com.bfrc.pojo.tire.jda2.Configuration)al.get(j);
						if(!skus.contains(Long.valueOf(c.getSku())))
							skus.add(Long.valueOf(c.getSku()));
					}
				}else if(item.getSku() != null && item.getSku().intValue() > 0){
					skus.add(item.getSku());
				}
				if(m.get(sn) == null){
					m.put(sn,skus);
				}else{
					List l2 = (List)m.get(sn);
					l2.addAll(skus);
				}
			}
			
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return m;
	}
	

	public List getGlobalSppressedTireArticles(){
		CacheService svc = CacheService.getInstance();
		List globalData = (List)svc.get("_TIRESUPPRESSION_GLOBAL_LIST_");
		Map storeMappedData = (Map)svc.get("_TIRESUPPRESSION_MAPPED_DATA_");
		Map<Long, List<String>> skuVehTypeData = (HashMap<Long, List<String>>)svc.get("_TIRESUPPRESSION_VEHTYPE_DISPLAY_LIST_");
		Date date = (Date)svc.get("_TIRESUPPRESSION_LAST_CACHE_DATE_");
		boolean needRefresh =  (globalData == null || date == null);
		AppCacheController acc = fetchCacheControllerById(new Long(1));
		Date modifiedDate = new Date();
		if(acc != null)
		    modifiedDate = acc.getModifiedDate();
		if(date != null){
			//Util.debug(modifiedDate+"-----------------------------"+date);
			if(!modifiedDate.equals(date)){//if date suppression data modified then refresh the caches{
				needRefresh = true;
		    }
		}
		if(needRefresh){
			if(globalData == null)
				globalData = new ArrayList();
			globalData.clear();
			Map allData = geAllData();
			globalData = (List)allData.get("globalData");
			svc.add("_TIRESUPPRESSION_GLOBAL_LIST_", globalData);
			if(storeMappedData == null)
				storeMappedData = new HashMap();
			storeMappedData.clear();
			storeMappedData = (Map)allData.get("storeData");
			if(skuVehTypeData == null)
				skuVehTypeData = new HashMap<Long, List<String>>();
			skuVehTypeData.clear();
			skuVehTypeData = (HashMap<Long, List<String>>)allData.get("skuVehTypeData");
			svc.add("_TIRESUPPRESSION_MAPPED_DATA_", storeMappedData);
			svc.add("_TIRESUPPRESSION_VEHTYPE_DISPLAY_LIST_", skuVehTypeData);
			svc.add("_TIRESUPPRESSION_LAST_CACHE_DATE_", modifiedDate);
		}
		return (List)((ArrayList)globalData).clone();
	}
	//--- get store number mapped tire articles ---//
	public Map getStoreNumberMappedSppressedTireArticles(){
		CacheService svc = CacheService.getInstance();
		Map storeMappedData = (Map)svc.get("_TIRESUPPRESSION_MAPPED_DATA_");
		return storeMappedData;
	}
	
	public Map<Long, List<String>> getGlobalVehTypeDisplayIDSuppressedTireArticles() {
		CacheService svc = CacheService.getInstance();
		Map<Long, List<String>> vehDisplayData = (Map<Long, List<String>>)svc.get("_TIRESUPPRESSION_VEHTYPE_DISPLAY_LIST_");
		return vehDisplayData;
	}
	
	public AppCacheController fetchCacheControllerById(Object id){
		if(id == null)
			return null;
		try{
		List l = getHibernateTemplate().find("from AppCacheController where appId=?", Long.valueOf(id.toString()));
		if (l == null || l.size() == 0)
			return null;
		    return (AppCacheController) l.get(0);
		}catch(Exception ex){
			
		}
		return null;
	}
	
	public void touchSuppressionCacheController(){
		AppCacheController controller = fetchCacheControllerById(new Long(1));
		if(controller != null){
		    controller.setModifiedDate(new Date());
		    getHibernateTemplate().update(controller);
		}
	}
	
	public double[] getTirePricingPriceRange(Long displayId){
		StoreScheduleDAO out = new StoreScheduleDAOImpl();
        String nativeSql = "select SKU from BFS_TIRE_CATALOG_JDA.CONFIGURATION where DISPLAY_ID="+displayId;
    	Session s = getSession();
    	List l = null;
    	try {
			SQLQuery q = s.createSQLQuery(nativeSql);
			q.addScalar("SKU", Hibernate.LONG);
			l = q.list();
    	} finally {
    		//s.close();
			this.releaseSession(s);
    	}
    	List articles = new ArrayList();
        for(int i=0;i<l.size();i++) {
        	articles.add(l.get(i));
        }
        List siteNameList = new ArrayList(); 
        String siteName = getConfig().getSiteName();
        if(siteName!=null && siteName.trim().equalsIgnoreCase("WWT")){
        	siteNameList.add("WW");
        }else if (siteName!=null && siteName.trim().equalsIgnoreCase("HT")){
        	siteNameList.add("HTP");
        }else if (siteName!=null && siteName.trim().equalsIgnoreCase("TP")){
        	siteNameList.add("TP");
        	siteNameList.add("TPL");
        }else {
        	siteNameList.add(siteName);
        }
		double[] prices  = new double[2];
		String[] paramNames = {"storeType","articles"};
		Object[] values = {siteNameList,articles};
		List r = getHibernateTemplate().findByNamedQueryAndNamedParam("getTirePricingPriceRange", paramNames, values);
		if(r != null && r.size() >0){
			try{
			Object[] vals = (Object[])r.iterator().next();
			if((vals[0] != null ) && 
					(vals[1] != null)){
			prices[0] = Double.parseDouble(vals[0].toString());
			prices[1] = Double.parseDouble(vals[1].toString());
			}
			else
				return null;
			}catch(Exception ex){
				return null;
			}
			return prices;
		}
	    return null;
	}
	
	public MobileTireInstallFee findMobileTireInstallFeeByStoreNumber(Long storeNumber){
		if(storeNumber == null) return null;
		return (MobileTireInstallFee)getHibernateTemplate().get(MobileTireInstallFee.class,storeNumber);
	}
	
	public void updateAlignmentPricingQuote(AlignmentPricingQuote alignmentQuote) {
		getHibernateTemplate().update(alignmentQuote);
	}
	
	public AlignmentPricingQuote getAlignmentQuotebyQuoteId(Long quoteId) {
		if(quoteId == null) return null;
		long longId = Long.parseLong(quoteId.toString());
		return (AlignmentPricingQuote)getHibernateTemplate().get(AlignmentPricingQuote.class,new Long(longId));

	}
	
}