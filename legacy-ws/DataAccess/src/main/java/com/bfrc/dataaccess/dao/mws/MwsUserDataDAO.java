/**
 * 
 */
package com.bfrc.dataaccess.dao.mws;

import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.mws.MwsBackupData;
import com.bfrc.dataaccess.model.mws.MwsUsers;

/**
 * @author schowdhury
 *
 */
public interface MwsUserDataDAO {
	
	public WebSite getSite(String siteName);
	
	public MwsUsers getMwsUser(String email, String password, WebSite webSite);
	
	public MwsUsers doesUserExist(String email, WebSite webSite);
	
	public void saveMwsUser(MwsUsers user);
	
	public void updateMwsUser(MwsUsers user);
	
	public MwsBackupData getUserData(MwsUsers user);
	
	public void saveUserData(MwsBackupData userData);

}
