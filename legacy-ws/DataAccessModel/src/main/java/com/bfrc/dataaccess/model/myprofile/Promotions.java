/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Collection;

/**
 * @author schowdhu
 *
 */
public class Promotions {
	
	private Collection<MyPromotion> promotions;

	/**
	 * 
	 */
	public Promotions() {
	}

	/**
	 * @param promotions
	 */
	public Promotions(Collection<MyPromotion> promotions) {
		this.promotions = promotions;
	}

	/**
	 * @return the promotions
	 */
	public Collection<MyPromotion> getPromotions() {
		return promotions;
	}

	/**
	 * @param promotions the promotions to set
	 */
	public void setPromotions(Collection<MyPromotion> promotions) {
		this.promotions = promotions;
	}
}
