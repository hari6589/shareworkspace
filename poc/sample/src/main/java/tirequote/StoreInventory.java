package tirequote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"storeNumber", "articleNumber", "quantityOnHand", "quantityOnOrder"})
public class StoreInventory {
	
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
	
	public Long getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(Long articleNumber) {
		this.articleNumber = articleNumber;
	}
	
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public Integer getQuantityOnHand() {
		return quantityOnHand;
	}
	public void setQuantityOnHand(Integer quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}
	
	public Integer getQuantityOnOrder() {
		return quantityOnOrder;
	}
	public void setQuantityOnOrder(Integer quantityOnOrder) {
		this.quantityOnOrder = quantityOnOrder;
	}
}