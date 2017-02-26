/**
 * 
 */
package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.StoreHolidayHour;

/**
 * @author smoorthy
 *
 */
public interface StoreHolidayHourDAO extends IGenericOrmDAO<StoreHolidayHour, Long>{
	
	public Collection<StoreHolidayHour> findStoreHolidayHour(Long storeNumber, Long holidayId);
	
}
