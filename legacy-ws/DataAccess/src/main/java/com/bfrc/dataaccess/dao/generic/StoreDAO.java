package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.Store;

public interface StoreDAO extends IGenericOrmDAO<Store, Long> {

	public Collection<Store> findByStateCityAndSiteName(String state, String city, String siteName);
	
	public Collection<String> findStoreTypes();
	
}
