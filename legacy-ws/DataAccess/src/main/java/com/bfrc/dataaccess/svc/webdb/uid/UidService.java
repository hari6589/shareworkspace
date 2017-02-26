package com.bfrc.dataaccess.svc.webdb.uid;

import java.util.List;

import com.bfrc.dataaccess.exception.InvalidUserException;
import com.bfrc.dataaccess.model.uid.BsroUidLogins;
import com.bfrc.dataaccess.model.uid.BsroUids;
import com.bfrc.dataaccess.svc.webdb.uid.UidServiceImpl.ActiveCode;

public interface UidService {

	public BsroUidLogins get(String userName);
	
	public void deleteLogin(String userName);

	public void purgeUser(String userName);
	
	public BsroUidLogins getByUid(Integer uid);
	
	public List<BsroUidLogins> getByStatus(ActiveCode activeCode);
	
	public BsroUids getUid(Integer uid);
	
	public BsroUidLogins authenticate(String userName, String password);
	
	public boolean isUserRegistered(String userName, Integer employeeId);

	/**
	 * This will check the bsro_uids table to ensure that there is a record for this 
	 * employee id before saving the record in the bsro_uid_logins table.
	 * @param bsroUidLogins
	 */
	public void save(BsroUidLogins bsroUidLogins) throws InvalidUserException;

	public String resetPassword(String userName) throws InvalidUserException;
	
	public List<BsroUids> listUids();
	
	public List<BsroUidLogins> listLogins();
}
