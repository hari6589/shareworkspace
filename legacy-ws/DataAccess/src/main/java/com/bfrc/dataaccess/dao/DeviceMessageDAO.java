/**
 * 
 */
package com.bfrc.dataaccess.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.bfrc.dataaccess.model.notification.DeviceMessage;
import com.bfrc.dataaccess.model.notification.ServiceNotification;

/**
 * @author schowdhu
 *
 */
public interface DeviceMessageDAO {
	
	/**
	 * retrieves a single device message record given a id
	 * @param id
	 * @return
	 */
	public DeviceMessage getMessage(Long id);
	
	/**
	 * Returns all active notifications that is not expired i.e. end date is in 
	 * the future or is null
	 * @return
	 * @throws DataAccessException
	 */
	public Collection<ServiceNotification> getCurrentNotifications() throws DataAccessException;

	/**
	 * Retrieves all messages from a single device given the UUID
	 * @param deviceUUID
	 * @return
	 * @throws DataAccessException
	 */
	public Collection<DeviceMessage> getMessagesForDevice(String deviceUUID) throws DataAccessException;
	
	/**
	 * updates a single device message record
	 * @param message
	 * @throws DataAccessException
	 */
	public void updateDeviceMessage(DeviceMessage message) throws DataAccessException;
	
	/**
	 * deletes a single message on a device
	 * @param deviceMessageId
	 * @throws DataAccessException
	 */
	public void deleteDeviceMessage(Long deviceMessageId) throws DataAccessException;
	
	/**
	 * deletes multiple messages on a device 
	 * @param deviceMessageIds
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteDeviceMessages(Object[] deviceMessageIds) throws DataAccessException;
}
