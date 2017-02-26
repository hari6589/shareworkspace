package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.PlatformTransactionManager;

import com.bfrc.framework.dao.ZipCodeDataDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.zipcode.ZipCodeData;

public class ZipCodeDataDAOImpl extends HibernateDAOImpl implements ZipCodeDataDAO {
	private PlatformTransactionManager txManager;

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ZipCodeData getZipCodeDataByZip(String zip) {
		List<Object[]> l = null;
		Session s = getSession();
		 try{
			 	zip=zip.trim();
				String sql = "select distinct zcd.zipcode as zip, zcd.statename as stateName, zcd.stateabbr as stateAbbr, zcd.timezone as timeZone, zcd.utc as offset, zcd.dst as dayLightFlag from ZIP_CODE_DATA zcd where zcd.zipcode = ?";
		
				Query query = s.createSQLQuery(sql)
						.addScalar("zip", Hibernate.STRING)
						.addScalar("stateName", Hibernate.STRING)
						.addScalar("stateAbbr", Hibernate.STRING)
						.addScalar("timeZone", Hibernate.STRING)
						.addScalar("offset", Hibernate.STRING)
						.addScalar("dayLightFlag", Hibernate.STRING);
				
				query.setString(0, zip);
				
				l = (List<Object[]>) query.list();

				ZipCodeData zipCodeData = new ZipCodeData();
				if(l!=null&&l.size()>0){
					Object[] oArray= l.get(0);
						zipCodeData.setZipcode((String)oArray[0]);
						zipCodeData.setStateName((String)oArray[1]);
						zipCodeData.setStateAbbr((String)oArray[2]);
						zipCodeData.setTimeZone((String)oArray[3]);
						zipCodeData.setUtc((String)oArray[4]);
						zipCodeData.setDst((String)oArray[5]);
					
				}
				Util.debug("===zip code data utc, tz, zip, setDst ==" + zipCodeData.getUtc()+", "+zipCodeData.getTimeZone()+", "+zipCodeData.getZipcode()+", "+zipCodeData.getDst());
				return zipCodeData;
		      }catch(Exception e){
		    	  	e.printStackTrace();
					Util.debug(" ====== exception zipcodeData from db ==== ");
		      } finally {
					if (s != null) {
						try {
							this.releaseSession(s);
						} catch (HibernateException e) {
							e.printStackTrace();
						}
					}
				}
		    return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getStateByZip(String zip) {
		String state = new String();
		List<String> l = null;
		Session s = getSession();
		 try{
				String sql = "select distinct zcd.stateabbr as stateAbbr from ZIP_CODE_DATA zcd where zcd.zipcode = ?";
		
				Query query = s.createSQLQuery(sql).addScalar("stateAbbr", Hibernate.STRING);
						
				query.setString(0, zip);
						
				l = (List<String>) query.list();
				
				if(l!=null&&l.size()>0){
					state = l.get(0);	
				}
				return state;
		      }catch(Exception e){
		    	  	e.printStackTrace();
					Util.debug(" ====== exception getting state with zipcode from db ==== ");
		      } finally {
					if (s != null) {
						try {
							this.releaseSession(s);
						} catch (HibernateException e) {
							e.printStackTrace();
						}
					}
				}
		    return null;
	}
}
