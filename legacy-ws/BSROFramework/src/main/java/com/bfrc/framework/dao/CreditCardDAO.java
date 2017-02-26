/**
 * 
 */
package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.creditcard.CreditCardContent;

/**
 * @author schowdhury
 *
 */
public interface CreditCardDAO {
	
	public List<CreditCardContent> getCreditCardDetails(String siteName);

}
