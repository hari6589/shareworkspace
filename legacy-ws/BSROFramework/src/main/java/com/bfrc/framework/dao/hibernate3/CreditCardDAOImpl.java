/**
 * 
 */
package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import com.bfrc.framework.dao.CreditCardDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.creditcard.CreditCardContent;

/**
 * @author schowdhury
 *
 */
public class CreditCardDAOImpl extends HibernateDAOImpl implements CreditCardDAO {

	/* (non-Javadoc)
	 * @see com.bfrc.framework.dao.CreditCardDAO#getCreditCardDetails(java.lang.Long)
	 */
	@Override
	public List<CreditCardContent> getCreditCardDetails(String siteName) {		
		return getHibernateTemplate().find("Select ccc from CreditCardContent ccc join ccc.website ws where ws.name = '"+siteName+"'");
	}

}
