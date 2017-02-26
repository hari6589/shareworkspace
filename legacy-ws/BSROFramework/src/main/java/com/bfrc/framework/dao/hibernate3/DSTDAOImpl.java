package com.bfrc.framework.dao.hibernate3;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.transaction.PlatformTransactionManager;

import com.bfrc.framework.dao.DSTDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.Util;

public class DSTDAOImpl extends HibernateDAOImpl implements DSTDAO {
	private PlatformTransactionManager txManager;

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	@Override
	public Map<String, Date> getStartAndEndDateForYear(String year) {
		Map<String, Date> dates = new HashMap<String,Date>();
		List<Object[]> l = null;
		Session s = getSession();
		 try{
				String sql = "select dst.start_date as startDate, dst.end_date as endDate"
						+" from DAY_LIGHT_SAVINGS dst where dst.year = '"+year+"'";
		
				l =		s.createSQLQuery(sql)
						.addScalar("startDate", Hibernate.DATE)
						.addScalar("endDate", Hibernate.DATE).list();
				if(l!=null){
					for(Object[] oArray: l){
						dates.put("start", (Date)oArray[0]);
						dates.put("end",  (Date)oArray[1]);
					}
				}
				return dates;
		      }catch(Exception e){
		    	  	e.printStackTrace();
					//Util.debug(" ====== exception fetching day light savings from db ==== ");
		      } finally {
					if (s != null) {
						try {
							this.releaseSession(s);
						} catch (HibernateException e) {
							e.printStackTrace();
						}
					}
				}
		    return dates;
	}
	
}
