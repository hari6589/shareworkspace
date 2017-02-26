/**
 * 
 */
package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.inventory.StoreInventory;

/**
 * @author schowdhu
 *
 */
public interface StoreInventoryDAO extends IGenericOrmDAO<StoreInventory, Long> {

	public Collection<StoreInventory> getInventoryByStore(Long storeNumber);
	
	public Collection<StoreInventory> getInventoryByStoreAndArticle(Long storeNumber, Long articleNo);
}
