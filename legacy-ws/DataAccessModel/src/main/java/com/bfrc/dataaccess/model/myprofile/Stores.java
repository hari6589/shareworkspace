/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Collection;

/**
 * @author schowdhu
 *
 */
public class Stores {
	
	private Collection<MyStore> stores;

	/**
	 * 
	 */
	public Stores() {
	}
	

	/**
	 * @param stores
	 */
	public Stores(Collection<MyStore> stores) {
		this.stores = stores;
	}


	/**
	 * @return the stores
	 */
	public Collection<MyStore> getStores() {
		return stores;
	}

	/**
	 * @param stores the stores to set
	 */
	public void setStores(Collection<MyStore> stores) {
		this.stores = stores;
	}
	
	

}
