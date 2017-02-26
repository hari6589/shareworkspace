package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.HrDistricts;

/**
 * @author smoorthy
 *
 */
public interface DistrictDAO extends IGenericOrmDAO<HrDistricts, Long> {
	
	public Collection<HrDistricts> findDistrictByStoreNumber(Long storeNumber);

}
