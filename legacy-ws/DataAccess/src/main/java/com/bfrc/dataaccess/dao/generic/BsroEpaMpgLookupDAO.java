package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.gas.BsroEpaMpgLookup;

public interface BsroEpaMpgLookupDAO extends IGenericOrmDAO<BsroEpaMpgLookup, Long> {

	public Collection<BsroEpaMpgLookup> findByYearMakeModel(String year, String make, String model);
	
}
