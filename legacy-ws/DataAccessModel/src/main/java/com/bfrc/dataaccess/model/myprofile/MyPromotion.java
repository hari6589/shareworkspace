/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.bfrc.dataaccess.model.promotion.Promotion;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"user","lastModifiedDate"})
public class MyPromotion  {
	
	private Long myPromotionId;
	private BFSUser user;
	private Promotion promotion;
	private Date lastModifiedDate;
	
	
	/**
	 * 
	 */
	public MyPromotion() {
	}
	/**
	 * @param user
	 * @param promotionId
	 * @param lastModifiedDate
	 */
	public MyPromotion(BFSUser user, Promotion promotion) {
		this.user = user;
		this.promotion = promotion;
		this.lastModifiedDate = new Date();
	}
	/**
	 * @return the myPromotionId
	 */
	@JsonProperty("id")
	public Long getMyPromotionId() {
		return myPromotionId;
	}
	/**
	 * @param myPromotionId the myPromotionId to set
	 */
	public void setMyPromotionId(Long myPromotionId) {
		this.myPromotionId = myPromotionId;
	}
	/**
	 * @return the user
	 */
	public BFSUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(BFSUser user) {
		this.user = user;
	}
	/**
	 * @return the promotion
	 */
	public Promotion getPromotion() {
		return promotion;
	}
	/**
	 * @param promotion the promotion to set
	 */
	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyPromotion [myPromotionId=" + myPromotionId + ", user=" + user
				+ ", promotion=" + promotion + ", lastModifiedDate="
				+ lastModifiedDate + "]";
	}
	
	

}
