package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.StoreHoliday;

/**
 * @author smoorthy
 *
 */
public interface StoreHolidayDAO extends IGenericOrmDAO<StoreHoliday, Long>{
	
	public Collection<StoreHoliday> findStoreHolidaysBetweenStartAndEndDates(String startYear, String endYear);

}
