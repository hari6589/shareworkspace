package com.bfrc.framework.dao.hibernate3;

import java.util.*;

import org.hibernate.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.*;
import com.bfrc.framework.spring.HibernateDAOImpl;

import com.bfrc.pojo.geodata.*;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.survey.MindshareTiresurveyDetails;

public class SurveyDAOImpl extends HibernateDAOImpl implements SurveyDAO {
    
	private PlatformTransactionManager txManager;
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	public Map getValidMappedMindshareTiresurveyDetails(){
		Map id_data = new LinkedHashMap();
		List items = getHibernateTemplate().findByNamedQuery(
				"getMindshareTiresurveyDetailsData");
		for(int i=0; items != null && i < items.size(); i++ ){
			MindshareTiresurveyDetails item = (MindshareTiresurveyDetails)items.get(i);
			id_data.put(item.getId(), item);
		}
		return id_data;
	}
	
	public Map getMappedMindshareTiresurveyDetails(){
		Map id_data = new LinkedHashMap();
		List items = getHibernateTemplate().findByNamedQuery(
				"getAllMindshareTiresurveyDetailsData");
		for(int i=0; items != null && i < items.size(); i++ ){
			MindshareTiresurveyDetails item = (MindshareTiresurveyDetails)items.get(i);
			id_data.put(item.getId(), item);
		}
		return id_data;
	}
	
	
}
