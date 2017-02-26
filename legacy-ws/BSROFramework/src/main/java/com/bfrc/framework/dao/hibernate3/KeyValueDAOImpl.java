package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.PlatformTransactionManager;

import com.bfrc.framework.dao.KeyValueDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.keyvalue.KeyValueData;

public class KeyValueDAOImpl extends HibernateDAOImpl implements KeyValueDAO {
	private PlatformTransactionManager txManager;

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<KeyValueData> getHierarchicalKeyValueDataForChildrenOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey) {
		Session session = getSession();

		List<KeyValueData> results = null;
		
		try {
			Query query = session
					.getNamedQuery("GetKeysAndValuesOrderedByValuesToDepth2")
					.setResultTransformer(Transformers.aliasToBean(KeyValueData.class));
			query.setString(0, containerKey);
			query.setString(1, siteName);
			query.setString(2, keyValueCategoryName);

			results = (List<KeyValueData>) query.list();
			
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		return results;
	}

	public KeyValueData lookupHierarchicalKeyValueDataForSpecificDescendantOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey, String parentKey, String childKey) {
		Session session = getSession();

		KeyValueData result = null;
		
		try {
			Query query = session
					.getNamedQuery("GetSpecificDepth2KeyAndValue")
					.setResultTransformer(Transformers.aliasToBean(KeyValueData.class));
			query.setString(0, containerKey);
			query.setString(1, siteName);
			query.setString(2, keyValueCategoryName);
			query.setString(3, parentKey);
			query.setString(4, childKey);

			result = (KeyValueData) query.uniqueResult();
			
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		return result;
	}
	
}
