package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.*;
import com.bfrc.pojo.appointment.*;
import com.bfrc.pojo.email.EmailSignup;

public interface UserDAO {
	public final static String JOINS = "left join u.appointment left join fetch u.company left join fetch u.store "
		+ "left join fetch u.vehicle";
	void updateUser(User u, User u2);
	void updateVehicle(User u, Long id, UserVehicle v);
	UserVehicle findVehicleById(Long id);
	List findVehiclesByUser(User u);
	void setDefaultStore(User u, Long id);
	void setDefaultVehicle(User u, Long id) throws Exception;
	void insertVehicle(User u, UserVehicle v, boolean defaultVehicle) throws Exception;
	void deleteVehicle(User u, Long id) throws Exception;
	User findUserByEmailAddress(String e);
	User findUserByEmailAddress(String e, int type);
	User findUserByEmailAddress(String e, int type, String optinCode);
	List findUsersByEmailAddress(String e);
	List findUsersByEmailAddress(String email, int typeId);
	void updateAppointment(User u, Appointment appt);
	void signupUser(User u, UserVehicle v) throws Exception;
	List dumpUsers() throws Exception;
	List getUsersByUserTypeAndActive(Object userTypeId,Object active);
	List getUsersByUserTypeAndActive();
	Long getUsersCountByUserTypeAndActive();
	
	List<User> getUsersByIndexAndMaxResults(int index, int maxResults);

	List getPartnerCompanies();
	
	List getUserStores(Object id);
	List getUserAppointments(Object i);
	List getUserVehicles(Object i);
	
	User copyFromEmailSignup(EmailSignup signup);
	User lookupUserProfile(String email);
	User authenticateUser(String email, String pwd, int type);
	User authenticateUser(String deviceId, int type);
	User authenticateUser(String email, String pwd);
	User authenticateUser(String deviceId);
	
	User getUserProfileByEmail(String e, int type);
	
}
