/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"user","lastModifiedDate"})
public class MyProductQuote {

	private Long myProductQuoteId;
	
	private BFSUser user;

	private ProductType productType;

	private Long quoteId;

	private Date expireDate;
	
	private Date lastModifiedDate;
	
	/**
	 * 
	 */
	public MyProductQuote() {
	}
	
	
	/**
	 * @param user
	 * @param productType
	 * @param quoteId
	 */
	public MyProductQuote(BFSUser user, ProductType productType, Long quoteId) {
		this.user = user;
		this.productType = productType;
		this.quoteId = quoteId;
	}


	/**
	 * @param user
	 * @param productType
	 * @param quoteId
	 * @param expireDate
	 */
	public MyProductQuote(BFSUser user, ProductType productType, Long quoteId,
			Date expireDate) {
		this.user = user;
		this.productType = productType;
		this.quoteId = quoteId;
		this.expireDate = expireDate;
	}


	/**
	 * @return the myProductQuoteId
	 */
	@JsonProperty("id")
	public Long getMyProductQuoteId() {
		return myProductQuoteId;
	}
	/**
	 * @param myProductQuoteId the myProductQuoteId to set
	 */
	public void setMyProductQuoteId(Long myProductQuoteId) {
		this.myProductQuoteId = myProductQuoteId;
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
	 * @return the productType
	 */
	@JsonProperty("type")
	public ProductType getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	@JsonProperty("type")
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	/**
	 * @return the quoteId
	 */
	public Long getQuoteId() {
		return quoteId;
	}
	/**
	 * @param quoteId the quoteId to set
	 */
	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
	}
	/**
	 * @return the expireDate
	 */
	public Date getExpireDate() {
		return expireDate;
	}
	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
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
		return "MyProductQuote [myProductQuoteId=" + myProductQuoteId
				+ ", productType=" + productType
				+ ", quoteId=" + quoteId + ", expireDate=" + expireDate
				+ ", lastModifiedDate=" + lastModifiedDate + "]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((quoteId == null) ? 0 : quoteId.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyProductQuote other = (MyProductQuote) obj;
		if (expireDate == null) {
			if (other.expireDate != null)
				return false;
		} else if (!expireDate.equals(other.expireDate))
			return false;
		if (productType != other.productType)
			return false;
		if (quoteId == null) {
			if (other.quoteId != null)
				return false;
		} else if (!quoteId.equals(other.quoteId))
			return false;
		return true;
	}
	
	
}
