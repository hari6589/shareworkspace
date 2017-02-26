/**
 * 
 */
package com.bfrc.dataaccess.model.store;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;
/**
 * @author smoorthy
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class StoreLocation implements Serializable {

	private static final long serialVersionUID = -1000821882740596453L;

	private Store store;
	private Long distance;
	
	public StoreLocation() {
		
	}
	
	public StoreLocation(Store store, long distance) {
		this.store = store;
		this.distance = distance;
	}
	
	public StoreLocation(StoreLocation storeLocation) {
		this.store = storeLocation.getStore();
		this.distance = storeLocation.getDistance();
	}
	
	public Store getStore() {
		return store;
	}
	
	public void setStore(Store store) {
		this.store = store;
	}
	
	@JsonSerialize(using=ToStringSerializer.class)
	public Long getDistance() {
		return distance;
	}
	
	public void setDistance(Long distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return "StoreLocation { distance = " + distance + ", " + store.toString() + "}";
	}

}
