/**
 * 
 */
package com.bfrc.dataaccess.svc.webdb;

import java.util.Collection;

import com.bfrc.dataaccess.model.notification.DeviceMessage;
import com.bfrc.dataaccess.model.notification.ServiceNotification;

/**
 * @author schowdhu
 *
 */
public interface NotificationService {
	
	/**
	 * 
	 * @return
	 */
	public Collection<ServiceNotification> getCurrentMessages();
	
	/**
	 * 
	 * @param deviceUUID
	 * @return
	 */
	public Collection<DeviceMessage> getDeviceMessage(String deviceUUID);
	
	/**
	 * 
	 * @param updateJson
	 * @return
	 */
	public boolean updateDeviceMessage(String updateJson);
	
	/**
	 * 
	 * @param deviceMessageIds
	 * @return
	 */
	public boolean deleteDeviceMessages(String deviceMessageIds);

}
