package com.bfrc.framework.dao.hibernate3;

import java.util.Calendar;
import java.util.Date;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.GeoDataDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.geodata.BsroCityStateGeoData;
import com.bfrc.pojo.geodata.BsroCityStateGeoDataId;
import com.bfrc.pojo.geodata.BsroIpGeoCF;
import com.bfrc.pojo.geodata.BsroStateGeoData;
import com.bfrc.pojo.geodata.BsroZipGeoData;

public class GeoDataDAOImpl extends HibernateDAOImpl implements GeoDataDAO {
    
	private PlatformTransactionManager txManager;
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	public  Date getDateInDays(int days){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	public  boolean isCacheExpired(Date date){
		if(date == null)
			return false;
		Date now = new Date();
		return now.after(date);
	}
    // Geo Data for ZIP
	public BsroZipGeoData findBsroZipGeoData(Object id)
    {
    	if(id == null) return null;
		return (BsroZipGeoData)getHibernateTemplate().get(BsroZipGeoData.class,id.toString().trim());
    }

	public void createBsroZipGeoData(BsroZipGeoData item) {
		try {
			item.setCheckDate(getDateInDays(14));//two weeks
			getHibernateTemplate().save(item);
		} catch (Exception ex) {
			//this.txManager.rollback(status);
			//ex.printStackTrace();
		}
		
	}

	public void updateBsroZipGeoData(BsroZipGeoData item) {
		try {
			item.setCheckDate(getDateInDays(14));//two weeks
			getHibernateTemplate().update(item);
		} catch (Exception ex) {
			//this.txManager.rollback(status);
			//ex.printStackTrace();
		}
	}

	public void deleteBsroZipGeoData(BsroZipGeoData item) {
		getHibernateTemplate().delete(item);
	}
	public void deleteBsroZipGeoData(Object id) {
		getHibernateTemplate().delete(findBsroZipGeoData(id));
	}
	
	// Geo Data for CITY STATE
	public BsroCityStateGeoData findBsroCityStateGeoData(Object id)
    {
    	if(id == null) return null;
		return (BsroCityStateGeoData)getHibernateTemplate().get(BsroCityStateGeoData.class,(BsroCityStateGeoDataId)id);
    }

	public BsroCityStateGeoData findBsroCityStateGeoData(String city,String state)
    {
    	if(state == null || city == null ) return null;
    	BsroCityStateGeoDataId id = new BsroCityStateGeoDataId();
    	id.setCity(city);
    	id.setState(state);
		return (BsroCityStateGeoData)getHibernateTemplate().get(BsroCityStateGeoData.class,id);
    }
	
	public void createBsroCityStateGeoData(BsroCityStateGeoData item) {
		try {
			item.setCheckDate(getDateInDays(14));//two weeks
			getHibernateTemplate().save(item);
		} catch (Exception ex) {
			//this.txManager.rollback(status);
			//ex.printStackTrace();
		}
		
	}

	public void updateBsroCityStateGeoData(BsroCityStateGeoData item) {
		try {
			item.setCheckDate(getDateInDays(14));//two weeks
			getHibernateTemplate().update(item);
		} catch (Exception ex) {
			//this.txManager.rollback(status);
			//ex.printStackTrace();
		}
	}

	public void deleteBsroCityStateGeoData(BsroCityStateGeoData item) {
		getHibernateTemplate().delete(item);
	}
	public void deleteBsroCityStateGeoData(Object id) {
		getHibernateTemplate().delete(findBsroCityStateGeoData(id));
	}
	
	// Geo Data for STATE
	public BsroStateGeoData findBsroStateGeoData(Object id)
    {
    	if(id == null) return null;
		return (BsroStateGeoData)getHibernateTemplate().get(BsroStateGeoData.class,id.toString().trim());
    }

	public void createBsroStateGeoData(BsroStateGeoData item) {
		try {
			item.setCheckDate(getDateInDays(14));//two weeks
			getHibernateTemplate().save(item);
		} catch (Exception ex) {
			//this.txManager.rollback(status);
			//ex.printStackTrace();
		}
		
	}

	public void updateBsroStateGeoData(BsroStateGeoData item) {
		try {
			item.setCheckDate(getDateInDays(14));//two weeks
			getHibernateTemplate().update(item);
		} catch (Exception ex) {
			//this.txManager.rollback(status);
			//ex.printStackTrace();
		}
	}

	public void deleteBsroStateGeoData(BsroStateGeoData item) {
		getHibernateTemplate().delete(item);
	}
	public void deleteBsroStateGeoData(Object id) {
		getHibernateTemplate().delete(findBsroStateGeoData(id));
	}
	
    // IP Geo CF
	public String getBsroIpGeoCF(Object id) {
		String value = null;
		
    	try {
    		BsroIpGeoCF cf = (BsroIpGeoCF) getHibernateTemplate().get(BsroIpGeoCF.class,id.toString().trim());
    		value = cf.getValue();    		
    	} catch (Exception ex) {
    		// do nothing;
    	}

    	return value;
	}

}
