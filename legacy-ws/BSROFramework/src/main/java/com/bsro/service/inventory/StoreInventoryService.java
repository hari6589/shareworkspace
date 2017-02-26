/**
 * 
 */
package com.bsro.service.inventory;

import java.util.Map;

import com.bfrc.dataaccess.model.inventory.StoreInventory;

/**
 * @author schowdhu
 *
 */
public interface StoreInventoryService {
	
	public Map<Long, StoreInventory> getInventoryInStore(Long storeNumber);

}
