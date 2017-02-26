package com.bfrc.dataaccess.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.quote.QuoteTypeDAO;
import com.bfrc.dataaccess.model.quote.QuoteType;

public class QuoteTypeDAOImpl extends HibernateDaoSupport implements QuoteTypeDAO {

	public QuoteType findQuoteTypeByFriendlyId(String friendlyId) {
		Session session = getSession();
		
		QuoteType quoteType = null;
		
		try {
			quoteType = (QuoteType) session.createCriteria(QuoteType.class).add(Restrictions.eq("friendlyId", friendlyId)).uniqueResult();
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
		
		return quoteType;
	}
}
