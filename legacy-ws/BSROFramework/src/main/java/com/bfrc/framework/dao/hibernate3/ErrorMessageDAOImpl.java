/**
 * 
 */
package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.framework.dao.error.ErrorMessageDAO;
import com.bfrc.pojo.error.ErrorMessage;
import com.bfrc.pojo.error.ErrorMessageStatusEnum;

/**
 * @author schowdhu
 *
 */
public class ErrorMessageDAOImpl extends HibernateDaoSupport implements ErrorMessageDAO {

	@Override
	public void saveErrorMessage(ErrorMessage errorMessage) {
		this.getHibernateTemplate().save(errorMessage);
		this.getSession().flush();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<ErrorMessage> getNewErrorMessages() {
		
		Session session = this.getSession();
		
		this.updateMessageStatuses(ErrorMessageStatusEnum.NEW.getStatusCode(), ErrorMessageStatusEnum.PROCESSING.getStatusCode());
		
		Criteria criteria = session.createCriteria(ErrorMessage.class);
		criteria.add(Restrictions.eq("status", ErrorMessageStatusEnum.PROCESSING.getStatusCode()));
		
		List<ErrorMessage> messageList = (List<ErrorMessage>)criteria.list();
		//System.out.println("messagelist size="+messageList.size());
		
		return messageList;
	}

	@Override
	public boolean updateMessageStatuses(String fromStatus, String toStatus){
		
		Object[] values = {toStatus, fromStatus};
		try{
			this.getHibernateTemplate().bulkUpdate("update ErrorMessage errMsg set errMsg.status = ? where errMsg.status = ?", values);
			this.getSession().flush();
		}catch(Exception e){
			return false;
		}

		return true;
	}

}
