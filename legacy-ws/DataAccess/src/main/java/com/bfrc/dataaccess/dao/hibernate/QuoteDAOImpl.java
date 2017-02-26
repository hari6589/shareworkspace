package com.bfrc.dataaccess.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.quote.QuoteDAO;
import com.bfrc.dataaccess.model.oil.Oil;
import com.bfrc.dataaccess.model.quote.Quote;
import com.bfrc.dataaccess.model.quote.TpTireQuotesLedger;

public class QuoteDAOImpl extends HibernateDaoSupport implements QuoteDAO {
	
	public Quote findQuoteByIdAndSite(Long quoteId, String siteName) {		
		Session session = getSession();
		
		Quote quote = null;
		
		try {
			quote = (Quote) session.createCriteria(Quote.class).add(Restrictions.eq("quoteId", quoteId)).add(Restrictions.eq("webSite", siteName)).uniqueResult();
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
		
		return quote;
	}
	
	public void save(Quote quote) {
		this.getHibernateTemplate().save(quote);
	}
	
	public void update(Quote quote) {
		this.getHibernateTemplate().update(quote);
	}
	
	public TpTireQuotesLedger getTireQuotesLedger(Long quoteId){
		TpTireQuotesLedger tpTireQuotesLedger = null;
		
		try {
			List<TpTireQuotesLedger> l = getHibernateTemplate().findByNamedParam("from TpTireQuotesLedger t where t.quoteId=:quoteId","quoteId", quoteId);
			if (l == null || l.size() < 1)
				return null;
			else
				tpTireQuotesLedger=l.get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return tpTireQuotesLedger;
	}
	
	public void saveTpTireQuotesLedger(TpTireQuotesLedger tpTireQuotesLedger) {
		this.getHibernateTemplate().saveOrUpdate(tpTireQuotesLedger);
	}
}
