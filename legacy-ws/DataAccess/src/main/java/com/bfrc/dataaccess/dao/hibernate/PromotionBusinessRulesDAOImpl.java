package com.bfrc.dataaccess.dao.hibernate;



import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.promotion.PromotionBusinessRulesDAO;
import com.bfrc.dataaccess.model.promotion.PromotionBusinessRules;
public class PromotionBusinessRulesDAOImpl extends HibernateDaoSupport implements PromotionBusinessRulesDAO {

	public PromotionBusinessRules findOilPromotionByPriceType(String productSubType) {
		 
		PromotionBusinessRules result = null;
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("com.bfrc.dataaccess.model.promotion.PromotionBusinessRules.findOilPromotionByPriceType");
			query.setString(0, productSubType.trim());
			
			  if (query.uniqueResult() != null) {
				  result = (PromotionBusinessRules) query.uniqueResult();
			  }
		} catch (Exception e){
			e.printStackTrace();
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
