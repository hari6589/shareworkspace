/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.bfrc.dataaccess.model.store.Store;

/**
 * @author schowdhu
 *
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY)
@JsonIgnoreProperties({"driver","lastModifiedDate"})
@JsonPropertyOrder({"id", "distance", "store"})
public class MyStore  {
	
	private Long myStoreId;
	private MyDriver driver;
	
	private Store store;
	private BigDecimal distance;

	private Date lastModifiedDate;

	public MyStore() {
	}

	/**
	 * @param driver
	 * @param storeNumber
	 */
	public MyStore(MyDriver driver, Store store) {
		this.driver = driver;
		this.store = store;
		this.lastModifiedDate = new Date();
	}
	
	/**
	 * @param driver
	 * @param storeNumber
	 * @param distance
	 */
	public MyStore(MyDriver driver, Store store, BigDecimal distance) {
		this.driver = driver;
		this.store = store;
		this.distance = distance;
		this.lastModifiedDate = new Date();
	}
	/**
	 * @return the myStoreId
	 */
	@JsonProperty("id")
	public Long getMyStoreId() {
		return myStoreId;
	}
	/**
	 * @param myStoreId the myStoreId to set
	 */
	@JsonProperty("id")
	public void setMyStoreId(Long myStoreId) {
		this.myStoreId = myStoreId;
	}

	/**
	 * @return the driver
	 */
	@JsonIgnore
	public MyDriver getDriver() {
		return driver;
	}
	/**
	 * @param driver the driver to set
	 */
	@JsonIgnore
	public void setDriver(MyDriver driver) {
		this.driver = driver;
	}

	/**
	 * @return the store
	 */
	@JsonProperty("store")
	public Store getStore() {
		return store;
	}

	/**
	 * @param store the store to set
	 */
	@JsonProperty("store")
	public void setStore(Store store) {
		this.store = store;
	}

	/**
	 * @return the distance
	 */
	public BigDecimal getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(BigDecimal distance) {
		this.distance = distance;
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
		return "MyStore [myStoreId=" + myStoreId + ", driver=" + driver
				+ ", distance=" + distance
				+ ", lastModifiedDate=" + lastModifiedDate + "]";
	}
}
