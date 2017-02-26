/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"serviceHistoryJob"})
@JsonPropertyOrder({"id", "articleNbr", "sequenceNbr", "description", "quantity", "itemPrice", "price", "type"})
public class MyServiceHistoryJobDetail  {
	

	private Long myServiceHistJobDetailId;
	private MyServiceHistoryJob serviceHistoryJob;
	private Long articleNumber;
	private Integer quantity;
	private Integer sequence;
	private BigDecimal price;
	private String description;
	private String type;
	private BigDecimal itemPrice;

	public MyServiceHistoryJobDetail() {
	}
	/**
	 * @return the myServiceHistJobDetailId
	 */
	@JsonProperty("id")
	public Long getMyServiceHistJobDetailId() {
		return myServiceHistJobDetailId;
	}
	/**
	 * @param myServiceHistJobDetailId the myServiceHistJobDetailId to set
	 */
	@JsonProperty("id")
	public void setMyServiceHistJobDetailId(Long myServiceHistJobDetailId) {
		this.myServiceHistJobDetailId = myServiceHistJobDetailId;
	}
	/**
	 * @return the serviceHistoryJob
	 */
	public MyServiceHistoryJob getServiceHistoryJob() {
		return serviceHistoryJob;
	}
	/**
	 * @param serviceHistoryJob the serviceHistoryJob to set
	 */
	public void setServiceHistoryJob(MyServiceHistoryJob serviceHistoryJob) {
		this.serviceHistoryJob = serviceHistoryJob;
	}
	/**
	 * @return the articleNumber
	 */
	@JsonProperty("articleNbr")
	public Long getArticleNumber() {
		return articleNumber;
	}
	/**
	 * @param articleNumber the articleNumber to set
	 */
	@JsonProperty("articleNbr")
	public void setArticleNumber(Long articleNumber) {
		this.articleNumber = articleNumber;
	}
	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the sequence
	 */
	@JsonProperty("sequenceNbr")
	public Integer getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	@JsonProperty("sequenceNbr")
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the itemPrice
	 */
	public BigDecimal getItemPrice() {
		return itemPrice;
	}
	/**
	 * @param itemPrice the itemPrice to set
	 */
	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}
	@Override
	public String toString() {
		return "MyServiceHistoryJobDetail [myServiceHistJobDetailId="
				+ myServiceHistJobDetailId + ", articleNumber=" + articleNumber
				+ ", quantity=" + quantity + ", sequence=" + sequence
				+ ", price=" + price + ", description=" + description
				+ ", type=" + type + ", itemPrice=" + itemPrice + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((articleNumber == null) ? 0 : articleNumber.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((itemPrice == null) ? 0 : itemPrice.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MyServiceHistoryJobDetail other = (MyServiceHistoryJobDetail) obj;
		if (articleNumber == null) {
			if (other.articleNumber != null)
				return false;
		} else if (!articleNumber.equals(other.articleNumber))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (itemPrice == null) {
			if (other.itemPrice != null)
				return false;
		} else if (!itemPrice.equals(other.itemPrice))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}
