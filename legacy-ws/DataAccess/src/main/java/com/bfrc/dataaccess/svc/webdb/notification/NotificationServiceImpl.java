/**
 * 
 */
package com.bfrc.dataaccess.svc.webdb.notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.DeviceMessageDAO;
import com.bfrc.dataaccess.model.notification.DeviceMessage;
import com.bfrc.dataaccess.model.notification.ServiceNotification;
import com.bfrc.dataaccess.svc.webdb.NotificationService;

/**
 * @author schowdhu
 *
 */
@Service
public class NotificationServiceImpl implements NotificationService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	DeviceMessageDAO deviceMessageDao;

	public Collection<DeviceMessage> getDeviceMessage(String deviceUUID) {
		logger.info("Inside getDeviceMessage method");
		return deviceMessageDao.getMessagesForDevice(deviceUUID);

	}
	
	public Collection<ServiceNotification> getCurrentMessages(){
		logger.info("Inside getCurrentNotifications method");
		return deviceMessageDao.getCurrentNotifications();
	}

	public boolean updateDeviceMessage(String updateJson) {
		logger.info("Inside updateDeviceMessage method");
		logger.debug("updateJson="+updateJson);
		DeviceMessage message = null;
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			message = mapper.readValue(updateJson, DeviceMessage.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("message======>"+message);
		if(message == null)
			return false;
		else{
		
			try {
				deviceMessageDao.updateDeviceMessage(message);
			} catch (DataAccessException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean deleteDeviceMessages(String deviceMessageIds) {
		logger.info("Inside deleteDeviceMessage method");
		int rows = 0;
		if(deviceMessageIds.indexOf(",") != -1){
			try {
				String[] idArr = deviceMessageIds.split(",");
				List<Long> params = new ArrayList<Long>();
				for (String id : idArr)
					params.add(Long.valueOf(id));
				
				rows = deviceMessageDao.deleteDeviceMessages(params.toArray());
				if(rows == params.size()) return true;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return false;
			} catch (DataAccessException e) {
				e.printStackTrace();
				return false;
			}
		}else{
			Object[] param = {Long.valueOf(deviceMessageIds)};
			try {
				deviceMessageDao.deleteDeviceMessage(Long.valueOf(deviceMessageIds));
				return true;
			} catch (DataAccessException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

}
