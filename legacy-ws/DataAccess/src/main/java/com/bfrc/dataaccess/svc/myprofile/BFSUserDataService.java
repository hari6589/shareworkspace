/**
 * 
 */
package com.bfrc.dataaccess.svc.myprofile;

/**
 * @author schowdhu
 *
 */
public interface BFSUserDataService {
	
	public String registerUser(String email, String password, String userType);
	
	public String authenticateUser(String email, String password, String userType);
	
	public String updateUser(String oldEmail, String newEmail, String oldPassword, 
			String newPassword, String userType);
	
	public boolean doesUserExist(String email, String userType);
	
	public String resetUserPassword(String email, String userType);
	
	public String doesUserDataExist(String email, String userType);
	
	public String saveUserData(String jsonData, String email, String userType);
	
	public String getUserData(String email, String userType);
	
	public String getUserData(String email, String password, String userType);
}
