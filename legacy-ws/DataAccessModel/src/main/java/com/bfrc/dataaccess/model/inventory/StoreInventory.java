/**
 * 
 */
package com.bfrc.dataaccess.model.inventory;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@JsonIgnoreProperties({"inventoryId"})
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"storeNumber", "articleNumber", "quantityOnHand", "quantityOnOrder"})
public class StoreInventory implements Serializable{
	

	private static final long serialVersionUID = -3314780104887503544L;
	private Long inventoryId;
	private Long articleNumber;
	private Long storeNumber;
	private Integer quantityOnHand;
	private Integer quantityOnOrder;
	
	
	public Long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	@JsonSerialize(using=ToStringSerializer.class)
	public Long getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(Long articleNumber) {
		this.articleNumber = articleNumber;
	}
	@JsonSerialize(using=ToStringSerializer.class)
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	@JsonSerialize(using=ToStringSerializer.class)
	public Integer getQuantityOnHand() {
		return quantityOnHand;
	}
	public void setQuantityOnHand(Integer quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}
	@JsonSerialize(using=ToStringSerializer.class)
	public Integer getQuantityOnOrder() {
		return quantityOnOrder;
	}
	public void setQuantityOnOrder(Integer quantityOnOrder) {
		this.quantityOnOrder = quantityOnOrder;
	}
}
