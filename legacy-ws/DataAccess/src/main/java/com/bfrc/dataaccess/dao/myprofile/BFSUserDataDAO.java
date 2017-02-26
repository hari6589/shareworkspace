/**
 * 
 */
package com.bfrc.dataaccess.dao.myprofile;

import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.myprofile.BFSUser;
import com.bfrc.dataaccess.model.myprofile.MyBackupData;

/**
 * @author schowdhu
 *
 */
public interface BFSUserDataDAO {
	
	public WebSite getSite(String siteName);
	
	public BFSUser getBfsUser(String email, String password, WebSite webSite);
	
	public BFSUser doesUserExist(String email, WebSite webSite);
	
	public Long saveBfsUser(BFSUser user);
	
	public void updateBfsUser(BFSUser user);
	
	public MyBackupData getUserData(BFSUser user);
	
	public void saveUserData(MyBackupData userData);
}
