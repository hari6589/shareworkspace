/**
 * 
 */
package com.bfrc.framework.dao.error;

import java.util.List;

import com.bfrc.pojo.error.ErrorMessage;

/**
 * @author schowdhu
 *
 */
public interface ErrorMessageDAO {
	
	public void saveErrorMessage(ErrorMessage errorMessage);
	
	public List<ErrorMessage> getNewErrorMessages();
	
	public boolean updateMessageStatuses(String fromStatus, String toStatus);

}
