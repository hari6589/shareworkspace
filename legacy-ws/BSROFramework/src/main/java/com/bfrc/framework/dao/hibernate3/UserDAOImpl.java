package com.bfrc.framework.dao.hibernate3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.*;
import org.springframework.transaction.support.*;

import java.util.*;

import com.bfrc.Config;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.StringUtils;

import com.bfrc.framework.dao.*;

import com.bfrc.pojo.*;
import com.bfrc.pojo.appointment.Appointment;
import com.bfrc.pojo.email.EmailSignup;
import com.bfrc.pojo.store.*;

public class UserDAOImpl extends HibernateDAOImpl implements UserDAO {
	Log logger = LogFactory.getLog(UserDAOImpl.class);

	public List dumpUsers() throws Exception {
		String hql = "from User u " + JOINS + " where u.userTypeId=2 and u.active=1";
		return getHibernateTemplate().find(hql);
	}

	protected PlatformTransactionManager txManager;

	private StoreDAO storeDAO;

	public StoreDAO getStoreDAO() {
		return this.storeDAO;
	}

	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
	}

	public void updateAppointment(User u, Appointment appt) {
		u.setAppointment(appt);
		User user = findUserByEmailAddress(u.getEmailAddress());
		u.setVehicle(user.getVehicle());
		getHibernateTemplate().update(u);
	}

	/**
	 * Only finds PPS users currently.
	 */
	public User findUserByEmailAddress(String email) {
		/*
		List l = getHibernateTemplate().find(
				"from User u " + JOINS + " where u.userTypeId=2 and lower(u.emailAddress) = lower(?)", e);
		if(l == null || l.size() == 0)
			return null;
		Object[] objs = (Object[])l.get(0);
		User u = (User)objs[0];
		UserVehicle v = u.getVehicle();
		if(v != null) {
			v = findVehicleById(new Long(v.getId()));
			u.setVehicle(v);
		}
		return u;
		*/
		return findUserByEmailAddress(email,2);
	}

	/**
	 * Finds and pulls a user which matches the email and site passed.
	 * @param e
	 * @param type
	 * @return
	 */
	public User findUserByEmailAddress(String e, int type){
		List l = getHibernateTemplate().find(
				"from User u " + JOINS + " where u.userTypeId=? and lower(u.emailAddress) = lower(?)", new Object[]{new Integer(type), e});
		if(l == null || l.size() == 0)
			return null;
		Object[] objs = (Object[])l.get(0);
		User u = (User)objs[0];
		UserVehicle v = u.getVehicle();
		if(v != null) {
			v = findVehicleById(new Long(v.getId()));
			u.setVehicle(v);
		}
		return u;
	}

	public void updateUser(User curr, User newUser) {
		curr.setAddress(newUser.getAddress());
		curr.setAddress2(newUser.getAddress2());
		curr.setCity(newUser.getCity());
		curr.setState(newUser.getState());
		curr.setZip(newUser.getZip());
		curr.setEmailAddress(newUser.getEmailAddress());
		curr.setFirstName(newUser.getFirstName());
		curr.setLastName(newUser.getLastName());
		curr.setPhone(newUser.getPhone());
		curr.setPasswordHint(newUser.getPasswordHint());
		curr.setOptIn(newUser.getOptIn());
		curr.setOptinCode(newUser.getOptinCode());
		curr.setOptinConfirm(newUser.getOptinConfirm());
		String newPass = newUser.getPassword();
		if(newPass != null && !"".equals(newPass))
			curr.setPassword(newPass);
		getHibernateTemplate().update(curr);
	}

	public List findVehiclesByUser(User u) {
		return getHibernateTemplate().find("from UserVehicle v where v.user.id = ?", new Long(u.getId()));
	}

	public void setDefaultStore(User u, Long storeNumber) {
		Store store = this.storeDAO.findStoreById(storeNumber);
		u.setStore(store);
		getHibernateTemplate().update(u);
	}

	public void setDefaultVehicle(User u, Long id) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		UserVehicle v = null;
		try {
			v = findVehicleById(id);
//System.out.println("vehicle=" + v.getId());
//System.out.println("vehicle.user=" + v.getUser().getId());
//System.out.println("user=" + u.getId());
			if(v != null && u.getId() == v.getUser().getId()) {
//System.out.println("updating default vehicle");
				User user = v.getUser();
				user.setVehicle(v);
				u.setVehicle(v);
				getHibernateTemplate().update(user);
			}
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("UserDAO could not set default vehicle (id=" + id + ") ");
			if(v != null)
				System.err.print(v.toString());
			if(u != null)
				System.err.print("for user " + u.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void updateVehicle(User u, Long vehId, UserVehicle v) {
		UserVehicle uv = null;
		if(vehId != null) {
			List l = getHibernateTemplate().find("from UserVehicle v where v.id="+vehId);
			if(l == null || l.size() == 0)
				return;
			uv = (UserVehicle)l.get(0);
			if(uv.getUser().getId() != u.getId())
				return;
			uv.setAcesVehicleId(v.getAcesVehicleId());
			uv.setYear(v.getYear());
			uv.setMake(v.getMake());
			uv.setModel(v.getModel());
			uv.setSubmodel(v.getSubmodel());
			uv.setOriginalTires(v.getOriginalTires());
			Integer mileage = uv.getCurrentMileage();
			if(mileage != null && !mileage.equals(v.getCurrentMileage())) {
				java.util.Date now = new java.util.Date();
				v.setMileageDate(now);
				uv.setMileageDate(now);
			}
			uv.setAnnualMileage(v.getAnnualMileage());
			uv.setCurrentMileage(v.getCurrentMileage());
			getHibernateTemplate().update(uv);
		} else {
			v.setUser(u);
			v.setActive(true);
			v.setMileageDate(new java.util.Date());
			v.setCreatedDate(v.getMileageDate());
			getHibernateTemplate().save(v);
		}
	}

	public void signupUser(User u, UserVehicle v) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			u.setActive(true);
			u.setCreatedDate(new java.util.Date());
			if(v == null || (v != null && v.getAcesVehicleId() == 0)) {
				v = null;
				u.setVehicle(null);
			}
			getHibernateTemplate().save(u);
			if(v != null)
				insertVehicle(u, v, true);
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("UserDAO could not signup user ");
			if(u != null)
				System.err.print(u.toString());
			if(v != null)
				System.err.print("with vehicle " + v.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void deleteVehicle(User u, Long id) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		UserVehicle v = null;
		try {
			v = findVehicleById(id);
			if(v != null && u.getVehicle() != null && u.getId() == v.getUser().getId()) {
				if(v.getId() == u.getVehicle().getId()) {
					User user = v.getUser();
					u.setVehicle(null);
					user.setVehicle(null);
					getHibernateTemplate().update(user);
				}
				getHibernateTemplate().delete(v);
			}
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("UserDAO could not delete vehicle (id=" + id + ") ");
			if(u != null)
				System.err.print("for user " + u.toString());
			if(v != null)
				System.err.print("with vehicle " + v.toString());
			System.err.println();
		    throw ex;
		}
	}

	public void insertVehicle(User u, UserVehicle v, boolean defaultVehicle) throws Exception {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			if(v != null && v.getAcesVehicleId() > 0) {
				v.setActive(true);
				v.setCreatedDate(u.getCreatedDate());
				v.setMileageDate(u.getCreatedDate());
				v.setUser(u);
				getHibernateTemplate().save(v);
				if(defaultVehicle) {
					if(Config.PPS.equals(getConfig().getSiteName()))
						setDefaultVehicle(u, new Long(v.getId()));
					else {
						u.setVehicle(v);
						getHibernateTemplate().update(u);
					}
				}
			}
			this.txManager.commit(status);
		} catch (Exception ex) {
			this.txManager.rollback(status);
			System.err.print("UserDAO could not insert vehicle ");
			if(u != null)
				System.err.print("for user " + u.toString());
			if(v != null)
				System.err.print("with vehicle " + v.toString());
			System.err.println();
		    throw ex;
		}
	}

	public UserVehicle findVehicleById(Long id) {
		List l = getHibernateTemplate().find(
				"from UserVehicle as v where v.id=" + id);
		if(l == null || l.size() == 0)
			return null;
		return (UserVehicle)l.get(0);
	}

	public PlatformTransactionManager getTxManager() {
		return this.txManager;
	}

	public void setTxManager(PlatformTransactionManager transactionManager) {
		this.txManager = transactionManager;
	}
	
	/**
	 * Only finds all records with this email address
	 */
	public List findUsersByEmailAddress(String e) {
		return findUsersByEmailAddress(e,2);
	}
	/**
	 * Only finds all records with this email address
	 */
	public List findUsersByEmailAddress(String email, int typeId) {
		Object[] vals = new Object[]{new Integer(typeId), email};
		List l = getHibernateTemplate().find(
				"from User u " + JOINS + " where u.userTypeId=? and lower(u.emailAddress) = lower(?)", vals);
		
		return l;
	}
	
	
	/**
	 * Finds and pulls a user which matches the email, site passed, and optin code.
	 * @param e
	 * @param type
	 * @return
	 */
	public User findUserByEmailAddress(String e, int type, String optinCode){
		List l = getHibernateTemplate().find(
				"from User u " + JOINS + " where u.userTypeId=? and lower(u.emailAddress) = lower(?) and OPTIN_CODE = ?", new Object[]{new Integer(type), e,optinCode});
		if(l == null || l.size() == 0)
			return null;
		Object[] objs = (Object[])l.get(0);
		User u = (User)objs[0];
		UserVehicle v = u.getVehicle();
		if(v != null) {
			v = findVehicleById(new Long(v.getId()));
			u.setVehicle(v);
		}
		return u;
	}

	public List getUsersByUserTypeAndActive(Object userTypeId,Object active) {
		String[] names = {"userTypeId","active"};
		Object[] values = {userTypeId, active};
		return  getHibernateTemplate().findByNamedQueryAndNamedParam("GetUsersByUserTypeAndActive", names, values);
		
	}
	
	public List getUsersByUserTypeAndActive() {
		return getUsersByUserTypeAndActive(new Integer(2), new Integer(1));		
	}
	
	public Long getUsersCountByUserTypeAndActive() {
		String[] names = {"userTypeId","active"};
		Object[] values = {new Integer(2), new Integer(1)};
		List results = getHibernateTemplate().findByNamedQueryAndNamedParam("GetUsersCountByUserTypeAndActive", names, values);
		Long result = new Long(0);
		if (!results.isEmpty()) { 
			result = (Long)results.get(0);						
		}
		if (logger.isInfoEnabled()) {
			logger.info("Number of user records to be exported: " + result);
		}
		return result;
	}
	
	public List<User> getUsersByIndexAndMaxResults(int index, int maxResults) {
		Session session = getSession();
		List<User> results = new ArrayList<User>();
		//if (logger.isInfoEnabled()) {
			logger.info("First index of the record to be returned = " + index);
			logger.info("Max results to be returned = " + maxResults);
		//}
		try {
			Criteria crit = session.createCriteria(User.class);
			crit.add(Restrictions.eq("userTypeId", new Integer(2)))
				.add(Restrictions.eq("active", new Boolean(true)));
			results = crit.createCriteria("company")
								.setMaxResults(maxResults)
								.setFirstResult(index)
								.list();
			//if (logger.isInfoEnabled()) {
				logger.info("Number of User records returned for export = " + results.size());
			//}
		} catch(Throwable e) {
			e.printStackTrace();
		} finally {
			releaseSession(session);
		}
		return results;
	}
	
	public List getPartnerCompanies() {
		return  getHibernateTemplate().findByNamedQuery("GetPartnerCompanies");	
	}
	
	public List getUserStores(Object id) {
		if(id == null)
			return null;
		String hsql="from Store s, UserStore us where s.storeNumber = us.storeNumber and us.usersId = "+Long.valueOf(id.toString())+" order by us.defaultFlag desc, us.createdDate desc";
		return getHibernateTemplate().find(hsql);  
	}
	
	public List getUserAppointments(Object id) {
		if(id == null)
			return null;
		String hsql="from Appointment a, UserAppointment ua where a.appointmentId = ua.appointmentId and ua.usersId = "+Long.valueOf(id.toString())+" order by ua.createdDate desc";
		return getHibernateTemplate().find(hsql);  
	}
	public List getUserVehicles(Object id) {
		if(id == null)
			return null;
		String hsql="from UserVehicle a where a.user.id = "+Long.valueOf(id.toString())+" order by a.defaultFlag desc, a.createdDate desc";
		return getHibernateTemplate().find(hsql);  
	}
	
	public User copyFromEmailSignup(EmailSignup signup){
		if(signup == null)
			return null;
		User user = new User();
		user.setActive(true);
		user.setFirstName(signup.getFirstName());
		user.setLastName(signup.getLastName());
		user.setAddress(signup.getAddress1());
		user.setAddress2(signup.getAddress2());
		user.setCity(signup.getCity());
		user.setState(signup.getState());
		user.setZip(signup.getZip());
		user.setEmailAddress(signup.getEmailAddress());
		user.setCreatedDate(new Date());
		user.setOptinCode(signup.getOptinCode());
		return user;
	}
	public User lookupUserProfile(String email){
		if(StringUtils.isNullOrEmpty(email))
			return null;
		User user = null;
		//--- look up Users, email_signup ---//
		List l = getHibernateTemplate().find(
				"from User u  where lower(u.emailAddress) = lower(?) order by u.createdDate desc", new Object[]{email});
		if(l != null &  l.size() > 0)
		   return (User)l.iterator().next();
		//--check email_signup ---//EmailSignup
		l = getHibernateTemplate().find(
				"from EmailSignup u  where lower(u.emailAddress) = lower(?) order by u.createdDate desc", new Object[]{email});
		if(l != null &  l.size() > 0){
			EmailSignup signup = (EmailSignup)l.iterator().next();
			user = copyFromEmailSignup(signup);
			return user;
			
		}
		return null;
	}
	
	public User authenticateUser(String email, String pwd){
		return authenticateUser(email,pwd,User.FCAC);
	}
	public User authenticateUser(String email, String pwd, int type){
		User user = getUserProfileByEmail(email,type);
	    if(user  == null )
			return null;
		if(pwd.equals(user.getPassword()))
			return user;
		return null;
	}
	public User authenticateUser(String deviceId){
		return authenticateUser(deviceId,User.FCAC);
	}
	public User authenticateUser(String deviceId,int type){
		if(StringUtils.isNullOrEmpty(deviceId))
			return null;
		List l = getHibernateTemplate().find(
				"from User u  where u.defaultFlag=1 and u.userTypeId=? and lower(u.deviceId) = lower(?)", new Object[]{new Integer(type), deviceId});
		if(l == null || l.size() == 0)
			return null;
		return (User)l.get(0);
	}
	
	public User getUserProfileByEmail(String email,int type){
		if(StringUtils.isNullOrEmpty(email))
			return null;
		List l = getHibernateTemplate().find(
				"from User u  where u.defaultFlag=1 and u.userTypeId=? and lower(u.emailAddress) = lower(?)", new Object[]{new Integer(type), email});
		if(l == null || l.size() == 0)
			return null;
		return (User)l.get(0);
	}
}
