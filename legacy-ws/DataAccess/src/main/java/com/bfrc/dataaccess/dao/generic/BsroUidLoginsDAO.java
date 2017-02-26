package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.uid.BsroUidLogins;

public interface BsroUidLoginsDAO extends
		IGenericOrmDAO<BsroUidLogins, String> {

	public Collection<BsroUidLogins> findByCredentials(String userName, String password);
	public Collection<BsroUidLogins> findByEmployeeId(Integer employeeId);
	public Collection<BsroUidLogins> findByActiveCd(String activeCd);
	
}
