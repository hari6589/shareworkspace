package com.bfrc.framework.dao.hibernate3;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bfrc.framework.dao.GrandOpeningDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.grandopening.BsroGrandOpening;

public class GrandOpeningDAOImpl extends HibernateDAOImpl implements GrandOpeningDAO {
    private PlatformTransactionManager txManager;
    public PlatformTransactionManager getTxManager() {
        return txManager;
    }

    public void setTxManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }


    public BsroGrandOpening findGrandOpening(Object id)
    {
        if(id == null) return null;
        return (BsroGrandOpening)getHibernateTemplate().get(BsroGrandOpening.class,Long.valueOf(id.toString()));
    }
    public void createGrandOpening(BsroGrandOpening item) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = this.txManager.getTransaction(def);
        try {
            Date now = new Date();
            item.setCreatedDate(now);
            item.setModifiedDate(now);
            getHibernateTemplate().save(item);
            this.txManager.commit(status);
        } catch (Exception ex) {
            this.txManager.rollback(status);
            ex.printStackTrace();
        }
        
    }
    public List getAllGrandOpenings() {
        String hql = "from BsroGrandOpening t order by t.eventId desc";
        List l = getHibernateTemplate().find(hql);
        return l;
    }

    public void updateGrandOpening(BsroGrandOpening item) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = this.txManager.getTransaction(def);
        try {
            item.setModifiedDate(new Date());
            getHibernateTemplate().update(item);
            this.txManager.commit(status);
        } catch (Exception ex) {
            this.txManager.rollback(status);
            ex.printStackTrace();
        }
    }

    public void deleteGrandOpening(BsroGrandOpening item) {
        getHibernateTemplate().delete(item);
    }
    public void deleteGrandOpening(Object id) {
        getHibernateTemplate().delete(findGrandOpening(id));
    }
    
    public int updateGoApproved(Long eventId, boolean approved){		
    	String offerStartDate = "trunc(sysdate), ";
		if (approved) {
			offerStartDate = "OFFER_START_DATE, ";
		}
		final String updateString = 
			"update BsroGrandOpening " +
			"set START_DATE = " + offerStartDate +
			"    APPROVED = :approved " +
			"where EVENT_ID = :eventId ";
		Session s = getSession();
		int updatedEntities = s.createQuery( updateString )
				.setBoolean( "approved", approved )
				.setLong( "eventId", eventId )
		        .executeUpdate();	
		
		return updatedEntities;
	}
    public int deleteGrandOpenings(Object[] eventIds) {
    	final String deleteString = "DELETE FROM BsroGrandOpening go WHERE go.eventId IN (:eventIds)";
    	Session s = getSession();
    	int updatedEntities = s.createQuery( deleteString )
		.setParameterList("eventIds", eventIds)
        .executeUpdate();
    	return updatedEntities;
    }
    
}
