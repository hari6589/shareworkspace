/**
 * 
 */
package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.StoreMilitary;

/**
 * @author smoorthy
 *
 */
public interface StoreMilitaryDAO extends IGenericOrmDAO<StoreMilitary, Long>{
	
	public Collection<StoreMilitary> findMilitaryStore(Long storeNumber);

}
