package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;
import java.util.List;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.StoreHour;
import com.bfrc.dataaccess.model.store.StoreHourId;

public interface StoreHourDAO extends IGenericOrmDAO<StoreHour, StoreHourId> {

	public Collection<StoreHour> findByStoreNumber(Long storeNumber);
	
	public Collection<StoreHour> findStoreHourByStoreType(List<String> storeType);
	
}
