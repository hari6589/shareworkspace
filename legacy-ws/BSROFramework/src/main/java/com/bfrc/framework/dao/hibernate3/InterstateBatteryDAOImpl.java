package com.bfrc.framework.dao.hibernate3;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.interstatebattery.BatteryLifeDuration;
import com.bfrc.pojo.interstatebattery.BatteryLifeRegion;
import com.bfrc.pojo.interstatebattery.InterstateAutomobile;
import com.bfrc.pojo.interstatebattery.InterstateBatteryMaster;
import com.bfrc.pojo.interstatebattery.InterstateBatteryQuote;
import com.bfrc.pojo.interstatebattery.InterstateBatteryWebPrices;
import com.bfrc.security.Encode;

public class InterstateBatteryDAOImpl extends HibernateDaoSupport implements InterstateBatteryDAO {

	private PlatformTransactionManager txManager;
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public List getYearList(){
		
		List l = getHibernateTemplate().findByNamedQuery("getInterstateBatteryYearList");
		if(l != null && l.size() >0){
			List years = new ArrayList();
			for(java.util.Iterator it = l.iterator(); it.hasNext();)
			{
			    //Object[] vals = (Object[])it.next();
			    years.add(it.next());
			    
			}
		    return years;
		}
	    return null;
	}
	
    public List getMakeList(Object year){
		String[] values = new String[]{String.valueOf(year)};
		List l = getHibernateTemplate().findByNamedQuery("getInterstateBatteryMakeList", values);
		if(l != null && l.size() >0){
			List makes = new ArrayList();
			int i =0;
			for(java.util.Iterator it = l.iterator(); it.hasNext();i++)
			{
			    //Object[] vals = (Object[])it.next();
			    makes.add(it.next());
			    
			}
		    return makes;
		}
	    return null;
	}
    
    public List getModelList(Object year, Object make){
		String[] values = new String[]{String.valueOf(year), String.valueOf(make)};
		List l = getHibernateTemplate().findByNamedQuery("getInterstateBatteryModelList", values);
		if(l != null && l.size() >0){
			List makes = new ArrayList();
			int i =0;
			for(java.util.Iterator it = l.iterator(); it.hasNext();i++)
			{
			    //Object[] vals = (Object[])it.next();
			    makes.add(it.next());
			    
			}
		    return makes;
		}
	    return null;
	}
    
    @Override
    public List<String> getModelListIgnoreCase(String year, String make){
		String[] values = new String[]{year, make};
		return getHibernateTemplate().findByNamedQuery("getInterstateBatteryModelListIgnoreCase", values);
	}
    
    public List getEngineList(Object year, Object make, Object model){
		String[] values = new String[]{String.valueOf(year), String.valueOf(make), String.valueOf(model)};
		List l = getHibernateTemplate().findByNamedQuery("getInterstateBatteryEngineList", values);
		if(l != null && l.size() >0){
			List makes = new ArrayList();
			int i =0;
			for(java.util.Iterator it = l.iterator(); it.hasNext();i++)
			{
			    //Object[] vals = (Object[])it.next();
			    makes.add(it.next());
			    
			}
		    return makes;
		}
	    return null;
	}

    @Override
    public List<String> getEngineListIgnoreCase(String year, String make, String model){
		String[] values = new String[]{year, make, model};
		return getHibernateTemplate().findByNamedQuery("getInterstateBatteryEngineListIgnoreCase", values);
	}
    
    public InterstateAutomobile fetchInterstateAutomobile(Long interstateAutomobileId){
    	return getHibernateTemplate().get(InterstateAutomobile.class, interstateAutomobileId);
    }
    
    public List getBatteryList(Object year, Object make, Object model, Object engine){
    	//String hsql = "from InterstateAutomobile where year=? and make=? and model=? and engine=? order by productCode, cca";
		//Object[] args = new Object[] {year, make, model, engine};
    	//return getHibernateTemplate().find(hsql, args);
    	//String cengine = com.bfrc.framework.util.StringUtils.unescapeHtmlLight((String)engine);
    	Object[] args = new Object[] {year, make, model, engine};
    	return  getHibernateTemplate().findByNamedQuery("getInterstateBatteryList", args);
    	
    }

    @Override
    public List<InterstateAutomobile> getBatteryListIgnoreCase(String year, String make, String model, String engine){
		String[] values = new String[]{year, make, model, engine};
		return getHibernateTemplate().findByNamedQuery("getInterstateBatteryListIgnoreCase", values);
	}
    
    public List getBatteryListLightMakeModelOnly(){
    	String fields = "SELECT distinct MAKE as make, MODEL as model FROM INTERSTATE_AUTOMOBILE";
		Session s = getSession();
		try{
		StringBuffer sb = new StringBuffer();
	    sb.append(fields);
		sb.append("  order by make, model \n");
		String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List items = null;
	    try{
	    	items = s
	    .createSQLQuery(sql)
	    .addScalar("make")
	    .addScalar("model")
	    .setResultTransformer(Transformers.aliasToBean(InterstateAutomobile.class)).list();
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("Error with getBatteryListLightMakeModelOnly: "+sql);
		}
		return items;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
    }
    public List getBatteryListOptional(Object year, Object make, Object model, Object engine){
		Session s = getSession();
		try{
		StringBuffer sb = new StringBuffer();
	    sb.append("SELECT AUTOMOBILE_ID as automobileId, PRODUCT_CODE as productCode, PRODUCT_LINE as productLine, MAKE as make, MODEL as model, YEAR as year, ENGINE  as engine, ");
	    sb.append("      PTR as ptr, NOTE as note, NOTE1 as note1, NOTE2 as note2, NOTE3 as note3, OPTN as optn, BCI as bci, NOTES as notes, CCA as cca ");
	    sb.append(" FROM  INTERSTATE_AUTOMOBILE ");
	    sb.append(" WHERE   YEAR = YEAR ");
	    if(year != null)
		    sb.append(" AND YEAR='" + Encode.escapeDb(year.toString() )+"' \n");
		if(make != null)
		    sb.append(" AND lower(REGEXP_REPLACE(MAKE, '"+StringUtils.NAME_FILTER_REGX+"', ''))= lower('" + StringUtils.nameFilter(make.toString()) +"') \n");
		if(model != null)
		    sb.append(" AND lower(REGEXP_REPLACE(MODEL, '"+StringUtils.NAME_FILTER_REGX+"', ''))= lower('" + StringUtils.nameFilter(model.toString()) + "') \n");
		if(engine != null)
			sb.append(" AND lower(REGEXP_REPLACE(ENGINE, '"+StringUtils.NAME_FILTER_REGX+"', ''))= lower('" + StringUtils.nameFilter(engine.toString()) + "') \n");
		sb.append("  ORDER BY PRODUCT_CODE, CCA \n");
		String sql = sb.toString();
	    ServerUtil.debug(sql);
	    List items = null;
	    try{
	    	items = s
	    .createSQLQuery(sql)
	    .addScalar("automobileId", Hibernate.LONG)
	    .addScalar("model")
	    .addScalar("productCode")
	    .addScalar("productLine")
	    .addScalar("make")
	    .addScalar("model")
	    .addScalar("year", Hibernate.SHORT)
	    .addScalar("engine")
	    .addScalar("ptr")
	    .addScalar("note")
	    .addScalar("note1")
	    .addScalar("note2")
	    .addScalar("note3")
	    .addScalar("optn")
	    .addScalar("bci")
	    .addScalar("notes")
	    .addScalar("cca", Hibernate.LONG)
	    .setResultTransformer(Transformers.aliasToBean(InterstateAutomobile.class)).list();
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("Error with getBatteryListLightMakeModelOnly: "+sql);
		}
		return items;
		}finally {
			//s.close();
			this.releaseSession(s);
		}
    }
    public InterstateBatteryMaster getInterstateBatteryMasterById(Object id)
    {
    	if(id == null) return null;
		return (InterstateBatteryMaster)getHibernateTemplate().get(InterstateBatteryMaster.class,id.toString());
    }
    
    public InterstateBatteryWebPrices getInterstateBatteryWebPricesById(Object id)
    {
    	if(id == null) return null;
		return (InterstateBatteryWebPrices)getHibernateTemplate().get(InterstateBatteryWebPrices.class,id.toString());
    }
    
    public void updateInterstateBatteryWebPrices(InterstateBatteryWebPrices item) throws Exception
    {
    	if( item == null ) return;
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("InterstateBattery could not update InterstateBatteryWebPrices ");
			if(item != null)
				System.err.print(item.toString());
			System.err.println();
		    throw ex;
		}
    }
    
    public Map getMappedBatteryMaster()
    {
    	List l = getHibernateTemplate().find("from InterstateBatteryMaster order by productCode");
    	if(l != null && l.size() > 0){
    		Map m = new HashMap();
    		for(java.util.Iterator it = l.iterator(); it.hasNext();)
			{
    			InterstateBatteryMaster item = (InterstateBatteryMaster)it.next();
    			m.put(item.getProductCode(),item);
			}
    		return m;
    	}
    	return null;
    }
    
    public Map getMappedBatteryWebPrices()
    {
    	List l = getHibernateTemplate().find("from InterstateBatteryWebPrices order by product");
    	if(l != null && l.size() > 0){
    		Map m = new HashMap();
    		for(java.util.Iterator it = l.iterator(); it.hasNext();)
			{
    			InterstateBatteryWebPrices item = (InterstateBatteryWebPrices)it.next();
    			m.put(item.getProduct(),item);
			}
    		return m;
    	}
    	return null;
    }
    
    public InterstateBatteryQuote getInterstateBatteryQuote(Object id) {
		if(id == null) return null;
		long longId = Long.parseLong(id.toString());
		return (InterstateBatteryQuote)getHibernateTemplate().get(InterstateBatteryQuote.class,new Long(longId));

	}
    
    public void createInterstateBatteryQuote(InterstateBatteryQuote item) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		//TransactionStatus status = this.txManager.getTransaction(def);
		try {
			item.setCreatedDate(new Date());
			getHibernateTemplate().save(item);
			//this.txManager.commit(status);
		} catch (Exception ex) {
			//this.txManager.rollback(status);
			System.err.print("InterstateBatteryQuote could not create promotion ");
			if(item != null)
				System.err.print(item.toString());
			System.err.println();
		    throw ex;
		}
	}
	
	public void updateInterstateBatteryQuote(InterstateBatteryQuote item) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("PromotionDAO could not update promotion ");
			if(item != null)
				System.err.print(item.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void deleteInterstateBatteryQuote(Object id) {
		getHibernateTemplate().delete(getInterstateBatteryQuote(id));
	}
	
	public BatteryLifeDuration getBatteryLifeDuration(Object id) {
		if(id == null) return null;
		long longId = Long.parseLong(id.toString());
		return (BatteryLifeDuration)getHibernateTemplate().get(BatteryLifeDuration.class,new Long(longId));

	}
    
    public void createBatteryLifeDuration(BatteryLifeDuration item) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().save(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("Could not create BatteryLifeDuration");
			if(item != null)
				System.err.print(item.toString());
			System.err.println();
		    throw ex;
		}
	}
	
	public void updateBatteryLifeDuration(BatteryLifeDuration item) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("Could not update BatteryLifeDuration ");
			if(item != null)
				System.err.print(item.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void deleteBatteryLifeDuration(Object id) {
		getHibernateTemplate().delete(getBatteryLifeDuration(id));
	}
	
	public BatteryLifeRegion getBatteryLifeRegion(Object id) {
		if(id == null) return null;
		return (BatteryLifeRegion)getHibernateTemplate().get(BatteryLifeRegion.class,id.toString());

	}
    
    public void createBatteryLifeRegion(BatteryLifeRegion item) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().save(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("Could not create BatteryLifeRegion");
			if(item != null)
				System.err.print(item.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void updateBatteryLifeRegion(BatteryLifeRegion item) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			getHibernateTemplate().update(item);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("Could not update BatteryLifeRegion");
			if(item != null)
				System.err.print(item.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void deleteBatteryLifeRegion(Object id) {
		getHibernateTemplate().delete(getBatteryLifeRegion(id));
	}
	
	public BatteryLifeDuration getBatteryLifeDurationByZipCode(Object id) {
		if(id == null || id.toString().length() < 5) return null;
		String zip3 = id.toString().substring(0,3);
		List l = getHibernateTemplate()
		         .findByNamedQueryAndNamedParam("getBatteryLifeDurationByZipCode", "zip3",zip3);
		if(l != null && l.size() > 0)
			return (BatteryLifeDuration)l.iterator().next();
        return null;

	}
}
