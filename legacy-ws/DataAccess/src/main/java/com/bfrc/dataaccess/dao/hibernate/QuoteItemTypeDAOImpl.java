package com.bfrc.dataaccess.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.quote.QuoteItemTypeDAO;
import com.bfrc.dataaccess.model.quote.QuoteItemType;

public class QuoteItemTypeDAOImpl extends HibernateDaoSupport implements QuoteItemTypeDAO {

	public QuoteItemType findQuoteItemTypeByFriendlyId(String friendlyId) {
		Session session = getSession();
		
		QuoteItemType quoteItemType = null;
		
		try {
			quoteItemType = (QuoteItemType) session.createCriteria(QuoteItemType.class).add(Restrictions.eq("friendlyId", friendlyId)).uniqueResult();
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
		
		return quoteItemType;
	}
}
