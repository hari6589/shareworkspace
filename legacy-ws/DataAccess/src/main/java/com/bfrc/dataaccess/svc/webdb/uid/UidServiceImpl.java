package com.bfrc.dataaccess.svc.webdb.uid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.BsroUidLoginsDAO;
import com.bfrc.dataaccess.dao.generic.BsroUidsDAO;
import com.bfrc.dataaccess.exception.InvalidUserException;
import com.bfrc.dataaccess.model.uid.BsroUidLogins;
import com.bfrc.dataaccess.model.uid.BsroUids;

@Service
public class UidServiceImpl implements UidService {

	@Autowired
	private BsroUidLoginsDAO bsroUidLoginsDao;
	@Autowired
	private BsroUidsDAO bsroUidsDao;
	
	public enum ActiveCode {
		ACTIVE("A"), INACTIVE("I"), DELETE("D");
		private String code;
		private ActiveCode(String code) { this.code = code; }
		public String getCode() { return code; }
	}
	
	public enum TermOfServiceCode {
		YES("Y"), NO("N");
		private String code;
		private TermOfServiceCode(String code) { this.code = code; }
		public String getCode() { return code; }
	}
	
	public BsroUidLogins get(String userName) {
		return bsroUidLoginsDao.get(userName);
	}
	
	public void deleteLogin(String userName) {
		BsroUidLogins bye = get(userName);
		bsroUidLoginsDao.delete(bye);
	}
	
	public void purgeUser(String userName) {
		BsroUidLogins bye = get(userName);
		BsroUids uid = bsroUidsDao.get(bye.getUserId());
		
		bsroUidsDao.delete(uid);
		bsroUidLoginsDao.delete(bye);
	}
	
	public BsroUidLogins getByUid(Integer uid) {
		Collection<BsroUidLogins> logins = bsroUidLoginsDao.findByEmployeeId(uid);
		if(logins != null && logins.size() == 1)
			return new ArrayList<BsroUidLogins>(logins).get(0);
		else return null;
	}
	
	public BsroUids getUid(Integer uid) {
		return bsroUidsDao.get(uid);
	}
	
	public List<BsroUidLogins> getByStatus(ActiveCode activeCode) {
		Collection<BsroUidLogins> logins = bsroUidLoginsDao.findByActiveCd(activeCode.code);
		List<BsroUidLogins> result = new ArrayList<BsroUidLogins>();
		if(logins != null)
			result = new ArrayList<BsroUidLogins>(logins);
		
		return result;
	}
	
	public BsroUidLogins authenticate(String userName, String password) {
		Collection<BsroUidLogins> users = bsroUidLoginsDao.findByCredentials(userName, password);
		if(users != null && users.size() == 1)
			return (new ArrayList<BsroUidLogins>(users)).get(0);
		else
			return null;
	}
	
	public void save(BsroUidLogins bsroUidLogins) throws InvalidUserException {
		BsroUids uidHeader = getUidRecord(bsroUidLogins.getUserId());
		if(uidHeader != null && uidHeader.getUserId() != null && uidHeader.getUserId().longValue() > 0) {
			bsroUidLoginsDao.save(bsroUidLogins);
		} else {
			throw new InvalidUserException("User does not exist in the UID table");
		}
	}
	
	public boolean isUserRegistered(String userName, Integer employeeId) {
		BsroUidLogins tmp = get(userName);
		
		if(tmp == null || tmp.getUserId() == null || tmp.getUserId().longValue() == 0) {
			//tmp is null so the userName has not already been taken. Now check to see if the employeeId
			// has already been registered
			Collection<BsroUidLogins> logins = bsroUidLoginsDao.findByEmployeeId(employeeId);
			if(logins == null || logins.size() == 0)
				return false;
			else return true;
		}
		else return true;
	}
	
	public String resetPassword(String userName) throws InvalidUserException {
		BsroUidLogins l = this.get(userName);
		if(l != null) {
			String uuid = UUID.randomUUID().toString();
			l.setPasswordResetCd(uuid);
			save(l);
			return uuid;
		}
		return null;
	}
	
	public List<BsroUids> listUids() {
		return new ArrayList<BsroUids>(bsroUidsDao.listAll());
	}
	
	public List<BsroUidLogins> listLogins() {
		return new ArrayList<BsroUidLogins>(bsroUidLoginsDao.listAll());
	}
	
	private BsroUids getUidRecord(Integer employeeId) {
		return bsroUidsDao.get(employeeId);
	}
}
