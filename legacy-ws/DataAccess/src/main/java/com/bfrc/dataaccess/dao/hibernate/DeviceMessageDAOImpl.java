/**
 * This class executes CRUD operation on device messages
 */
package com.bfrc.dataaccess.dao.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.bfrc.dataaccess.dao.DeviceMessageDAO;
import com.bfrc.dataaccess.model.notification.DeviceMessage;
import com.bfrc.dataaccess.model.notification.ServiceNotification;

/**
 * @author schowdhu
 *
 */
public class DeviceMessageDAOImpl extends HibernateDaoSupport implements DeviceMessageDAO {

	public DeviceMessage getMessage(Long id){
		return getHibernateTemplate().get(DeviceMessage.class, id);
	}

	public Collection<DeviceMessage> getMessagesForDevice(String deviceUUID) throws DataAccessException{

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(DeviceMessage.class);
		criteria.add(Restrictions.eq("deviceUUID", deviceUUID));
		criteria.createAlias("serviceNotification", "message");
		criteria.add(Restrictions.le("message.startDate", new Date()));
		criteria.add(Restrictions.or(Restrictions.ge("message.endDate", new Date()),Restrictions.isNull("message.endDate")));
		
		@SuppressWarnings("unchecked")
		List<DeviceMessage> results = (List<DeviceMessage>)criteria.list();
		System.out.println("size======>"+results.size());
		return results;
	}
	
	public Collection<ServiceNotification> getCurrentNotifications() throws DataAccessException {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(ServiceNotification.class);
		criteria.add(Restrictions.le("startDate", new Date()));
		criteria.add(Restrictions.or(Restrictions.ge("endDate", new Date()),Restrictions.isNull("endDate")));

		@SuppressWarnings("unchecked")
		List<ServiceNotification> results = (List<ServiceNotification>)criteria.list();
		return results;
	}

	public void updateDeviceMessage(DeviceMessage message) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(message);
	}

	public void deleteDeviceMessage(Long deviceMessageId) throws DataAccessException{
		DeviceMessage entity = getMessage(deviceMessageId);
		//getHibernateTemplate().delete(entity);
		entity.setDeleteFlag("Y");
		getHibernateTemplate().update(entity);
	}
	
	@Transactional
	public int deleteDeviceMessages(Object[] deviceMessageIds)throws DataAccessException{
		int rows = 0;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String hql = "UPDATE DeviceMessage dm SET dm.deleteFlag = 'Y' WHERE dm.deviceMessageId IN (:ids)";
		rows =  session.createQuery(hql).setParameterList("ids", deviceMessageIds).executeUpdate();
		session.flush();
		return rows;
	}

}
